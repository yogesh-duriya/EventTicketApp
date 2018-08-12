package com.endive.eventplanner.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.OrganizeProfileActivity;
import com.endive.eventplanner.adapter.EventsListAdapter;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.OrganizerPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by upasna.mishra on 10/30/2017.
 */

public class UpcomingFragment extends Fragment {

    private View view;
    private RecyclerView rv_package_list;
    private boolean fragmentResume = false;
    private boolean fragmentVisible = false;
    private boolean fragmentOnCreated = false;
    private ArrayList<SomethingInterestingDataPojo> eventArr;
    private OrganizeProfileActivity ctx;
    private ConnectionDetector cd;
    private EventDialogs dialog;
    private TextView no_data_available;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_packages, container, false);
        initialize();
        if (!fragmentResume && fragmentVisible && eventArr != null && eventArr.size() == 0) {   //only when first time fragment is created
            organizerData(OrganizeProfileActivity.organizer_id, "2");
        }
        return view;
    }

    private void initialize() {
        eventArr = new ArrayList<SomethingInterestingDataPojo>();
        rv_package_list = (RecyclerView) view.findViewById(R.id.rv_package_list);
        no_data_available = (TextView) view.findViewById(R.id.no_data_available);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv_package_list.setLayoutManager(mLayoutManager);
        rv_package_list.setItemAnimator(new DefaultItemAnimator());

        ctx = (OrganizeProfileActivity) getActivity();
        cd = new ConnectionDetector(getActivity());
        dialog = new EventDialogs(getActivity());
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed() && eventArr != null && eventArr.size() == 0) {   // only at fragment screen is resumed
            fragmentResume = true;
            fragmentVisible = false;
            fragmentOnCreated = true;
            organizerData(OrganizeProfileActivity.organizer_id, "2");
        } else if (visible) {        // only at fragment onCreated
            fragmentResume = false;
            fragmentVisible = true;
            fragmentOnCreated = true;
        } else if (!visible && fragmentOnCreated) {// only when you go out of fragment screen
            fragmentVisible = false;
            fragmentResume = false;
        }
    }

    private void organizerData(String organizer_id, String type) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<OrganizerPojo> call = apiService.getOrganizerData("EventOrganiserProfile", organizer_id, ctx.getFromPrefs(EventConstant.USER_ID), type);
            call.enqueue(new Callback<OrganizerPojo>() {
                @Override
                public void onResponse(Call<OrganizerPojo> call, Response<OrganizerPojo> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        eventArr = response.body().getData().getEvent_detail();
                        if (eventArr != null && eventArr.size() == 0) {
                            no_data_available.setVisibility(View.VISIBLE);
                        } else {
                            no_data_available.setVisibility(View.GONE);
                        }
                        setAdapter(eventArr);
                    } else {
                        dialog.displayCommonDialog(response.body().getError_message());
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<OrganizerPojo> call, Throwable t) {
                    // Log error here since request failed
                    System.out.println("retrofit hh failure " + t.getMessage());
                    d.dismiss();
                }
            });
        } else {
            dialog.displayCommonDialog(getContext().getResources().getString(R.string.no_internet_connection));
        }
    }

    private void setAdapter(ArrayList<SomethingInterestingDataPojo> arr) {
        ctx.adapterUpcoming = new EventsListAdapter(ctx, arr, "OrganizerProfile");
        if (arr != null) {
            rv_package_list.setAdapter(ctx.adapterUpcoming);
        }
    }
}
