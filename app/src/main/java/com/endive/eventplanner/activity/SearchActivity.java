package com.endive.eventplanner.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.SearchAdapter;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.CategoriesDataPojo;
import com.endive.eventplanner.pojo.CategoriesPojo;
import com.endive.eventplanner.pojo.TopCategoryPojo;
import com.endive.eventplanner.pojo.TopSearchPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventDialogs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private SearchActivity ctx = this;
    private RecyclerView trending_categories_list;
    private RecyclerView trending_search_list;
    private ConnectionDetector cd;
    private EventDialogs dialog;
    private android.support.v7.widget.SearchView searchView;
    private ImageView back;
    private LinearLayout trending_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(searchView != null) {
            searchView.setIconified(false);
            searchView.clearFocus();
        }
    }

    private void initialize() {
        cd = new ConnectionDetector(ctx);
        dialog = new EventDialogs(ctx);
        trending_view = (LinearLayout) findViewById(R.id.trending_view);
        trending_categories_list = (RecyclerView) findViewById(R.id.trending_categories_list);
        trending_categories_list.setHasFixedSize(true);
        setRecyclerLayoutManager(trending_categories_list);

        trending_search_list = (RecyclerView) findViewById(R.id.trending_search_list);
        trending_search_list.setHasFixedSize(true);
        setRecyclerLayoutManager(trending_search_list);
        back = (ImageView) findViewById(R.id.back);
        searchView = (android.support.v7.widget.SearchView) findViewById(R.id.search_box);
        getTrendingResult();
    }

    @Override
    public void onClick(View view) {
        TopSearchPojo data = null;
        TopCategoryPojo catData = null;
        switch (view.getId()) {
            case R.id.root_layout_categories:
                String type = (String) view.getTag(R.string.type);
                hideSoftKeyboard();
                if (type.equals("search")) {
                    data = (TopSearchPojo) view.getTag(R.string.data);
                    navigateToSearchResult(data.getKeyword(), type);
                } else {
                    catData = (TopCategoryPojo) view.getTag(R.string.data);
                    navigateToSearchResult(catData.getCategory_id(), type);
                }
                break;

            default:
                break;
        }
    }

    private void setListener() {
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do your task here
                hideSoftKeyboard();
                navigateToSearchResult(query, "");
                return false;
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                finish();
            }
        });

        trending_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });

        trending_categories_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });

        trending_search_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }

    private void navigateToSearchResult(String keyword, String type) {
        Intent intent = new Intent(ctx, SearchResultActivity.class);
        intent.putExtra("keyword", keyword);
        intent.putExtra("type", type);
        intent.putExtra("api", "Search");
        intent.putExtra("header", getResources().getString(R.string.header_search_result));
        startActivity(intent);
    }

    private void getTrendingResult() {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<CategoriesPojo> call = apiService.getCategories("TopSearch");
            call.enqueue(new Callback<CategoriesPojo>() {
                @Override
                public void onResponse(Call<CategoriesPojo> call, Response<CategoriesPojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
                        setAdapter("category", response.body().getData());
                        setAdapter("search", response.body().getData());
                    } else {
                        dialog.displayCommonDialog(response.body().getError_message());
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<CategoriesPojo> call, Throwable t) {
                    // Log error here since request failed
                    System.out.println("retrofit hh failure " + t.getMessage());
                    d.dismiss();
                }
            });
        }
    }

    private void setAdapter(String type, CategoriesDataPojo data) {
        SearchAdapter adapter = new SearchAdapter(ctx, data, type);
        if (type.equals("category"))
            trending_categories_list.setAdapter(adapter);
        else
            trending_search_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
