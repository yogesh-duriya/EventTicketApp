package com.endive.eventplanner.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.EventsListAdapter;
import com.endive.eventplanner.adapter.HomePagerAdapter;
import com.endive.eventplanner.interfaces.UpdateImageIntercace;
import com.endive.eventplanner.pojo.CategoryListPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.util.DepthPageTransformer;
import com.endive.eventplanner.util.EventConstant;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity implements View.OnClickListener, UpdateImageIntercace {

    private TextView tab1_text, tab2_text;
    private LinearLayout tab1_selector, tab2_selector;
    private ViewPager viewPager;
    private HomeActivity ctx = this;
    private HomePagerAdapter mAdapter;
    private ImageView back, nav_invisible, nav, search;
    public ImageView filter;
    private boolean doubleBackToExitPressedOnce = false;
    public EventsListAdapter adapter = null;
    public ArrayList<SomethingInterestingDataPojo> items;
    private GoogleApiClient googleApiClient;
    private ImageView header_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventConstant.ACTIVITIES.add(ctx);

        initialize();
        setListeners();
        setDrawerAndToolbar("");

        googleApiClient = new GoogleApiClient.Builder(HomeActivity.this).addApi(LocationServices.API).build();
        googleApiClient.connect();

    }

    private void initialize() {
        mAdapter = new HomePagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        tab1_text = (TextView) findViewById(R.id.tab1_text);
        tab2_text = (TextView) findViewById(R.id.tab2_text);
        tab2_text.setText(getResources().getString(R.string.something_interesting));
        tab1_text.setText(getResources().getString(R.string.browse_categories));
        tab1_selector = (LinearLayout) findViewById(R.id.tab1_selector);
        tab2_selector = (LinearLayout) findViewById(R.id.tab2_selector);
        filter = (ImageView) findViewById(R.id.filter);
        search = (ImageView) findViewById(R.id.search);
        back = (ImageView) findViewById(R.id.back);
        header_logo = (ImageView) findViewById(R.id.header_logo);
        nav_invisible = (ImageView) findViewById(R.id.nav_invisible);
        nav = (ImageView) findViewById(R.id.nav);
        nav.setVisibility(View.VISIBLE);
        header_logo.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        back.setVisibility(View.GONE);
        nav_invisible.setVisibility(View.GONE);
    }

    private void setListeners() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                setStyle(position);
                if (position == 0)
                    filter.setVisibility(View.GONE);
                else
                    filter.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, SearchActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tab1_layout).setOnClickListener(this);
        findViewById(R.id.tab2_layout).setOnClickListener(this);
    }

    private void setStyle(int position) {
        if (position == 0) {
            tab2_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint_color));
            tab1_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.button_color));

            tab2_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.hint_color));
            tab1_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.button_color));
        } else if (position == 1) {
            tab2_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.button_color));
            tab1_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint_color));

            tab2_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.button_color));
            tab1_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.hint_color));
        }
    }

    @Override
    public void onClick(View view) {
        SomethingInterestingDataPojo data;
        switch (view.getId()) {
            case R.id.tab1_layout:
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                    setStyle(0);
                }
                break;

            case R.id.tab2_layout:
                if (viewPager.getCurrentItem() != 1) {
                    viewPager.setCurrentItem(1);
                    setStyle(1);
                }
                break;

            case R.id.share:
                data = (SomethingInterestingDataPojo) view.getTag(R.string.data);
                displaySharingIntent(data);
                break;

            case R.id.heart:
                data = (SomethingInterestingDataPojo) view.getTag(R.string.data);
                if (!getFromPrefs(EventConstant.USER_ID).equals("")) {
                    setFavorite(data, "SetFavourite", this);
                    adapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
                break;

            case R.id.like:
                data = (SomethingInterestingDataPojo) view.getTag(R.string.data);
                if (!getFromPrefs(EventConstant.USER_ID).equals("")) {
                    setFavorite(data, "SetLike", this);
                } else {
                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
                break;

            case R.id.home_row:
                data = (SomethingInterestingDataPojo) view.getTag(R.string.data);
                Intent intent = new Intent(ctx, EventDetailsActivity.class);
                intent.putExtra("data", data);
                startActivityForResult(intent, EventConstant.SHOW_DETAIL);
                break;

            case R.id.category_image:
                CategoryListPojo dataCat = (CategoryListPojo) view.getTag(R.string.data);
                navigateToSearchResult(dataCat.getId(), "category", dataCat.getCategory_name());
                break;

            case R.id.map_image:
                Intent intentMap = new Intent(ctx, MapActivity.class);
                intentMap.putExtra("data", ctx.items);
                startActivityForResult(intentMap, EventConstant.START_MAP);
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

    private void navigateToSearchResult(String cat_id, String type, String header) {
        Intent intent = new Intent(ctx, SearchResultActivity.class);
        intent.putExtra("keyword", cat_id);
        intent.putExtra("type", type);
        intent.putExtra("api", "Search");
        intent.putExtra("header", header);
        startActivity(intent);
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
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        displayToast(getResources().getString(R.string.back_exit));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onSuccess(@NonNull String value) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
    }

}
