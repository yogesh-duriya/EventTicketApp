package com.endive.eventplanner.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.FilterActivity;
import com.endive.eventplanner.activity.HomeActivity;
import com.endive.eventplanner.adapter.EventsListAdapter;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.FilterPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.pojo.SomethingInterestingPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SomethingInterestingFragment extends Fragment {

    private RecyclerView interesting_events_list;
    private HomeActivity ctx;
    private ConnectionDetector cd;
    private EventDialogs dialog;
    private View view;
    private boolean fragmentResume = false;
    private boolean fragmentVisible = false;
    private boolean fragmentOnCreated = false;
    private String category, event_type, price_min, price_max, sort = "2";
    private FilterPojo filterPojo;
    private String startDate, endDate;
    private String location = "";
    private TextView no_data_available;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.interesting_fragment_layout, container, false);

        initialize();
        setListeners();

        return view;
    }

    private void initialize() {
        ctx = (HomeActivity) getActivity();
        cd = new ConnectionDetector(getActivity());
        dialog = new EventDialogs(getActivity());
        ctx.items = new ArrayList<>();
        no_data_available = (TextView) view.findViewById(R.id.no_data_available);
        interesting_events_list = (RecyclerView) view.findViewById(R.id.interesting_events_list);
        ctx.setRecyclerLayoutManager(interesting_events_list);
        interesting_events_list.setHasFixedSize(true);
    }

    private void setListeners() {
        ctx.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, FilterActivity.class);
                startActivity(intent);
            }
        });

        getInterestingListData();
    }

    private void getInterestingListData() {
        ctx.items.clear();
        if (ctx.adapter != null)
            ctx.adapter.notifyDataSetChanged();
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<SomethingInterestingPojo> call = apiService.getInterestingEvents("UpcomingEvents", ctx.getFromPrefs(EventConstant.USER_ID), category, event_type, price_min, price_max, sort, startDate, endDate, "", location, "");
            call.enqueue(new Callback<SomethingInterestingPojo>() {
                @Override
                public void onResponse(Call<SomethingInterestingPojo> call, Response<SomethingInterestingPojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
//                        ctx.items.addAll(response.body().getData());
                        ctx.items = new ArrayList<>();
                        ctx.items = response.body().getData();
                        if (ctx.items != null && ctx.items.size() == 0) {
                            no_data_available.setVisibility(View.VISIBLE);
                        } else {
                            no_data_available.setVisibility(View.GONE);
                        }
                        ctx.saveBooleanIntoPrefs(EventConstant.REFRESH_REQUIRED, false);
                        setAdapter(ctx.items);
                    } else if (response.body() != null) {
                        no_data_available.setVisibility(View.VISIBLE);
                        dialog.displayCommonDialog(response.body().getError_message());
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<SomethingInterestingPojo> call, Throwable t) {
                    // Log error here since request failed
                    System.out.println("retrofit hh failure " + t.getMessage());
                    d.dismiss();
                }
            });
        } else {
            dialog.displayCommonDialog(ctx.getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        filterPojo = ctx.getFilterData();
        if (filterPojo.isRelevance())
            sort = "1";
        else if (filterPojo.isDate())
            sort = "2";
        price_max = filterPojo.getPrice_max();
        price_min = filterPojo.getPrice_min();
        startDate = "";
        endDate = "";
        if (filterPojo.getStartDate() != null) {
            startDate = filterPojo.getStartDate();
        }
        if (filterPojo.getData() != null) {
            for (int i = 0; i < filterPojo.getData().getCategory().size(); i++) {
                if (filterPojo.getData().getCategory().get(i).is_selected()) {
                    category = filterPojo.getData().getCategory().get(i).getId();
                    break;
                } else
                    category = "";
            }
            for (int i = 0; i < filterPojo.getData().getEvent_type().size(); i++) {
                if (filterPojo.getData().getEvent_type().get(i).is_selected()) {
                    event_type = filterPojo.getData().getEvent_type().get(i).getId();
                    break;
                } else
                    event_type = "";
            }
        }
        if (filterPojo.getLocationArr() != null) {
            for (int i = 0; i < filterPojo.getLocationArr().size(); i++) {
                if (filterPojo.getLocationArr().get(i).isSelected()) {
                    location = filterPojo.getLocationArr().get(i).getCity();
                    break;
                } else
                    location = "";
            }
        }
        if (location.equals("All Locations"))
            location = "";
        if (ctx.getBooleanFromPrefs(EventConstant.REFRESH_REQUIRED))
            getInterestingListData();
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed() && ctx.items != null && ctx.items.size() == 0) {   // only at fragment screen is resumed
            fragmentResume = true;
            fragmentVisible = false;
            fragmentOnCreated = true;
            getInterestingListData();
        } else if (visible) {        // only at fragment onCreated
            fragmentResume = false;
            fragmentVisible = true;
            fragmentOnCreated = true;
//            ctx.filter.setVisibility(View.VISIBLE);
            if (ctx.adapter != null)
                ctx.adapter.notifyDataSetChanged();
        } else if (!visible && fragmentOnCreated) {// only when you go out of fragment screen
            fragmentVisible = false;
            fragmentResume = false;
        }
    }

    private void setAdapter(ArrayList<SomethingInterestingDataPojo> arr) {
        if (!ctx.getBooleanFromPrefs(EventConstant.REFRESH_REQUIRED)) {
            ctx.adapter = new EventsListAdapter(ctx, arr, "home");
            interesting_events_list.setAdapter(ctx.adapter);
        } else {
            ctx.saveBooleanIntoPrefs(EventConstant.REFRESH_REQUIRED, false);
            ctx.adapter.notifyDataSetChanged();
        }
    }
}
