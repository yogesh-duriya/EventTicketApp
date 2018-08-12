package com.endive.eventplanner.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.EventsListAdapter;
import com.endive.eventplanner.interfaces.UpdateImageIntercace;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.pojo.SomethingInterestingPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendedEventsActivity extends BaseActivity implements View.OnClickListener, UpdateImageIntercace {

    private RecommendedEventsActivity ctx = this;
    private LinearLayout filter_layout;
    private ConnectionDetector cd;
    private EventDialogs dialog;
    private RecyclerView search_result;
    public ArrayList<SomethingInterestingDataPojo> items;
    private EventsListAdapter adapter;
    private SomethingInterestingDataPojo obj;
    private String tags = "";
    private TextView no_data_available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        EventConstant.ACTIVITIES.add(ctx);
        setHeader(getResources().getString(R.string.header_recommend));
        initialize();
    }

    private void initialize() {
        cd = new ConnectionDetector(ctx);
        dialog = new EventDialogs(ctx);
        items = new ArrayList<>();
        search_result = (RecyclerView) findViewById(R.id.search_result);
        no_data_available = (TextView) findViewById(R.id.no_data_available);
        search_result.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ctx);
        search_result.setLayoutManager(mLayoutManager);
        filter_layout = (LinearLayout) findViewById(R.id.filter_layout);
        filter_layout.setVisibility(View.GONE);
        obj = (SomethingInterestingDataPojo) getIntent().getSerializableExtra("data");
        for (int i = 0; i < obj.getTags().size(); i++) {
            tags += obj.getTags().get(i) + ",";
        }
        tags = tags.substring(0, tags.length() - 1);
        getRecommendedListData();
    }

    private void getRecommendedListData() {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<SomethingInterestingPojo> call = apiService.getRecommendedEvents("RecommendedEvents", getFromPrefs(EventConstant.USER_ID), tags, obj.getLatitude(), obj.getLongitude(), obj.getVenue(), obj.getStart_time(), obj.getEnd_time(), obj.getId());
            call.enqueue(new Callback<SomethingInterestingPojo>() {
                @Override
                public void onResponse(Call<SomethingInterestingPojo> call, Response<SomethingInterestingPojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
                        items.clear();
                        if (response.body().getData() != null)
                            items.addAll(response.body().getData());
                        if (items == null || items.size() == 0) {
                            no_data_available.setVisibility(View.VISIBLE);
                        } else {
                            no_data_available.setVisibility(View.GONE);
                        }
                        saveBooleanIntoPrefs(EventConstant.REFRESH_REQUIRED, false);
                        setAdapter(items);
                    } else {
                        no_data_available.setVisibility(View.VISIBLE);
                        dialog.displayCommonDialog(response.body().getError_message());
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<SomethingInterestingPojo> call, Throwable t) {
                    // Log error here since request failed
                    d.dismiss();
                }
            });
        } else {
            dialog.displayCommonDialog(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void setAdapter(ArrayList<SomethingInterestingDataPojo> arr) {
        if (!ctx.getBooleanFromPrefs(EventConstant.REFRESH_REQUIRED)) {
            adapter = new EventsListAdapter(ctx, arr, "recommended");
            search_result.setAdapter(adapter);
        } else {
            saveBooleanIntoPrefs(EventConstant.REFRESH_REQUIRED, false);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        SomethingInterestingDataPojo data;
        switch (view.getId()) {
            case R.id.share:
                data = (SomethingInterestingDataPojo) view.getTag(R.string.data);
                displaySharingIntent(data);
                break;

            case R.id.heart:
                data = (SomethingInterestingDataPojo) view.getTag(R.string.data);
                if (!getFromPrefs(EventConstant.USER_ID).equals(""))
                    setFavorite(data, "SetFavourite", this);
                else {
                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
                break;

            case R.id.home_row:
                data = (SomethingInterestingDataPojo) view.getTag(R.string.data);
                removeActivity(getResources().getString(R.string.package_name) + ".EventDetailsActivity");
                Intent intent = new Intent(ctx, EventDetailsActivity.class);
                intent.putExtra("data", data);
                startActivityForResult(intent, EventConstant.SHOW_DETAIL);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EventConstant.START_MAP) {
                items.clear();
                items.addAll((ArrayList<SomethingInterestingDataPojo>) data.getSerializableExtra("data"));
                adapter.notifyDataSetChanged();
            } else if (requestCode == EventConstant.SHOW_DETAIL) {
                SomethingInterestingDataPojo dataObj = (SomethingInterestingDataPojo) data.getSerializableExtra("data");
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getId().equals(dataObj.getId())) {
                        items.get(i).setIs_favourite(dataObj.isIs_favourite());
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSuccess(@NonNull String value) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(@NonNull Throwable throwable) {

    }
}
