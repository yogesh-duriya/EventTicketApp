package com.endive.eventplanner.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

public class SearchResultActivity extends BaseActivity implements View.OnClickListener, UpdateImageIntercace {

    private SearchResultActivity ctx = this;
    private LinearLayout filter_layout;
    private ConnectionDetector cd;
    private EventDialogs dialog;
    private RecyclerView search_result;
    public ArrayList<SomethingInterestingDataPojo> items;
    private String category, event_type, price_min, price_max, sort;
    private FilterPojo filterPojo;
    private String startDate, endDate;
    private EventsListAdapter adapter;
    private String keyword = "";
    private String apiName;
    private TextView no_data_available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        initialize();
    }

    private void initialize() {
        cd = new ConnectionDetector(ctx);
        dialog = new EventDialogs(ctx);
        items = new ArrayList<>();
        no_data_available = (TextView) findViewById(R.id.no_data_available);
        search_result = (RecyclerView) findViewById(R.id.search_result);
        search_result.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ctx);
        search_result.setLayoutManager(mLayoutManager);
        filter_layout = (LinearLayout) findViewById(R.id.filter_layout);
        filter_layout.setVisibility(View.GONE);

        apiName = getIntent().getStringExtra("api");
        keyword = getIntent().getStringExtra("keyword");
        String type = getIntent().getStringExtra("type");
        if (type.equals("category")) {
            category = keyword;
            keyword = "";
        }

        setHeader(getIntent().getStringExtra("header"));
        getInterestingListData();
    }

    private void getInterestingListData() {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<SomethingInterestingPojo> call = apiService.getInterestingEvents(apiName, getFromPrefs(EventConstant.USER_ID), category, event_type, price_min, price_max, sort, startDate, endDate, keyword, "", keyword);
            call.enqueue(new Callback<SomethingInterestingPojo>() {
                @Override
                public void onResponse(Call<SomethingInterestingPojo> call, Response<SomethingInterestingPojo> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus_code().equals("1")) {
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
                            dialog.displayCommonDialogFinish(response.body().getError_message());
                        }
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
        if (filterPojo.getStartDate() != null) {
            startDate = filterPojo.getStartDate();
        }
        if (filterPojo.getData() != null) {
            for (int i = 0; i < filterPojo.getData().getCategory().size(); i++) {
                if (filterPojo.getData().getCategory().get(i).is_selected()) {
                    category = filterPojo.getData().getCategory().get(i).getId();
                    break;
                }
            }
            for (int i = 0; i < filterPojo.getData().getEvent_type().size(); i++) {
                if (filterPojo.getData().getEvent_type().get(i).is_selected()) {
                    event_type = filterPojo.getData().getEvent_type().get(i).getId();
                    break;
                }
            }
        }
        if (ctx.getBooleanFromPrefs(EventConstant.REFRESH_REQUIRED))
            getInterestingListData();
    }

    private void setAdapter(ArrayList<SomethingInterestingDataPojo> arr) {
        if (!ctx.getBooleanFromPrefs(EventConstant.REFRESH_REQUIRED)) {
            adapter = new EventsListAdapter(ctx, arr, "search");
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
                if (!getFromPrefs(EventConstant.USER_ID).equals("")) {
                    setFavorite(data, "SetFavourite", ctx);
                    adapter.notifyDataSetChanged();
                } else {
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
                if (getIntent().getStringExtra("header").equalsIgnoreCase(getResources().getString(R.string.past)))
                    intent.putExtra("from", "past");
                startActivityForResult(intent, EventConstant.SHOW_DETAIL);
                break;

            case R.id.event_location:
                data = (SomethingInterestingDataPojo) view.getTag(R.string.data);
                if (data.getLatitude() != null && data.getTitle() != null && !data.getLatitude().equals("") && !data.getLongitude().equals("")) {
                    double latitude = Float.parseFloat(data.getLatitude());
                    double longitude = Float.parseFloat(data.getLongitude());
                    String uriBegin = "geo:" + latitude + "," + longitude;
                    String query = latitude + "," + longitude + "(" + data.getOrganizer_name() + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intentMapView = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intentMapView);
                }
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
            } else if (requestCode == EventConstant.SHOW_DETAIL) {
                SomethingInterestingDataPojo dataObj = (SomethingInterestingDataPojo) data.getSerializableExtra("data");
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getId().equals(dataObj.getId())) {
                        items.get(i).setIs_favourite(dataObj.isIs_favourite());
                        break;
                    }
                }
            }
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccess(@NonNull String value) {
        if (getIntent().getStringExtra("header").equalsIgnoreCase(getResources().getString(R.string.favorite))) {
            for (int i = 0; i < items.size(); i++) {
                if (!items.get(i).isIs_favourite()) {
                    items.remove(i);
                    adapter.notifyItemRemoved(i);
                    break;
                }
            }
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(@NonNull Throwable throwable) {

    }
}
