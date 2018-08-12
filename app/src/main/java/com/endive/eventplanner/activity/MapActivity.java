package com.endive.eventplanner.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.MapPagerAdapter;
import com.endive.eventplanner.adapter.MyInfoWindowAdapter;
import com.endive.eventplanner.interfaces.UpdateImageIntercace;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.FilterPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.pojo.SomethingInterestingPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Hashtable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, UpdateImageIntercace {

    private GoogleMap googleMap;
    private MapActivity ctx = this;
    private ArrayList<SomethingInterestingDataPojo> data;
    private ImageView back, nav_invisible, nav;
    private Hashtable<String, String> markers;
    private ViewPager viewPager;
    private ConnectionDetector cd;
    private EventDialogs dialog;
    private MapPagerAdapter adapter;
    private CameraPosition cameraPosition;
    private ArrayList<Marker> marker_arr;
    private String category, event_type, price_min, price_max, sort;
    private int cameraPos = 0;
    private ImageView filter, search;
    private String startDate, endDate;
    private String location = "";
    private ImageView header_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        EventConstant.ACTIVITIES.add(ctx);
        initialize();
        setDrawerAndToolbar("");
        setListeners();
    }

    private void initialize() {
        data = (ArrayList<SomethingInterestingDataPojo>) getIntent().getSerializableExtra("data");
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);

        markers = new Hashtable<>();
        marker_arr = new ArrayList<>();
        cd = new ConnectionDetector(ctx);
        dialog = new EventDialogs(ctx);
        back = (ImageView) findViewById(R.id.back);
        nav_invisible = (ImageView) findViewById(R.id.nav_invisible);
        nav = (ImageView) findViewById(R.id.nav);
        nav.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) findViewById(R.id.view_pager_map);
        filter = (ImageView) findViewById(R.id.filter);
        search = (ImageView) findViewById(R.id.search);
        header_logo = (ImageView) findViewById(R.id.header_logo);
        back.setVisibility(View.GONE);
        nav_invisible.setVisibility(View.GONE);
        search.setVisibility(View.VISIBLE);
        filter.setVisibility(View.VISIBLE);
        header_logo.setVisibility(View.VISIBLE);
    }

    private void setListeners() {
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, FilterActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, SearchActivity.class);
                startActivity(intent);
            }
        });

//        list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.putExtra("data", data);
//                setResult(Activity.RESULT_OK, intent);
//                finish();
//            }
//        });

        setAdapter(data);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                updateMapCenterPoint(data.get(position));
                cameraPos = position;
                setCameraPosition(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        this.googleMap.getUiSettings().setCompassEnabled(false);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(true);

        updateMapView();
    }

    private void updateMapView() {
        markers.clear();
        marker_arr.clear();
        if (googleMap != null) {
            int i = 0;
            for (SomethingInterestingDataPojo obj : data) {
                if (obj.getLatitude() != null && obj.getLongitude() != null && !obj.getLatitude().equals("") && !obj.getLongitude().equals("")) {
                    final Marker mark = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(obj.getLatitude()), Double.parseDouble(obj.getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_pin))
                            .title(obj.getTitle()).snippet(getDate(obj.getStart_time())));
                    if (i == 0)
                        updateMapCenterPoint(obj);
                    i++;
                    markers.put(mark.getId(), obj.getImage());
                    marker_arr.add(mark);
                }
            }
            setCameraPosition(cameraPos);
            googleMap.setOnMarkerClickListener(this);
            googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(ctx, markers));
        }
    }

    private void updateMapCenterPoint(SomethingInterestingDataPojo obj) {
        if (obj.getLatitude() != null && obj.getLongitude() != null && !obj.getLatitude().equals("") && !obj.getLongitude().equals("")) {
            cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(Double.parseDouble(obj.getLatitude()), Double.parseDouble(obj.getLongitude()))).zoom(15).build();
        }

    }

    private void setCameraPosition(int pos) {
        if (cameraPosition != null) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
        }
        if (pos >= 0 && pos < marker_arr.size())
            marker_arr.get(pos).showInfoWindow();
    }

    @Override
    public void onResume() {
        super.onResume();
        FilterPojo filterPojo = getFilterData();
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
        if (ctx.getBooleanFromPrefs(EventConstant.REFRESH_REQUIRED)) {
            data.clear();
            adapter.notifyDataSetChanged();
            if (googleMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                googleMap.clear();
            }
            getInterestingListData();
        }
    }

    private void getInterestingListData() {
        data.clear();
        adapter.notifyDataSetChanged();
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<SomethingInterestingPojo> call = apiService.getInterestingEvents("UpcomingEvents", ctx.getFromPrefs(EventConstant.USER_ID), category, event_type, price_min, price_max, sort, startDate, endDate, "", location, "");
            call.enqueue(new Callback<SomethingInterestingPojo>() {
                @Override
                public void onResponse(Call<SomethingInterestingPojo> call, Response<SomethingInterestingPojo> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        data = response.body().getData();
                        ctx.saveBooleanIntoPrefs(EventConstant.REFRESH_REQUIRED, false);
                        if (googleMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                            googleMap.clear();
                            updateMapView();
                        }

                        setAdapter(data);
                    } else {
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
            dialog.displayCommonDialog(getResources().getString(R.string.no_internet_connection));
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

            case R.id.like:
                data = (SomethingInterestingDataPojo) view.getTag(R.string.data);
                if (!getFromPrefs(EventConstant.USER_ID).equals(""))
                    setFavorite(data, "SetLike", this);
                else {
                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
                break;

            case R.id.map_pager_row:
                data = (SomethingInterestingDataPojo) view.getTag(R.string.data);
                Intent intent = new Intent(ctx, EventDetailsActivity.class);
                intent.putExtra("data", data);
                startActivityForResult(intent, EventConstant.SHOW_DETAIL);
                break;

            default:
                break;
        }
    }

    private void setAdapter(ArrayList<SomethingInterestingDataPojo> arr) {
        adapter = new MapPagerAdapter(this, arr, (int) getResources().getDimension(R.dimen.map_view_pager_height));
        if (!ctx.getBooleanFromPrefs(EventConstant.REFRESH_REQUIRED)) {
            viewPager.setAdapter(adapter);
        } else {
            saveBooleanIntoPrefs(EventConstant.REFRESH_REQUIRED, false);
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }

        if (arr != null && cameraPos < arr.size())
            viewPager.setCurrentItem(cameraPos);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        for (int i = 0; i < marker_arr.size(); i++) {
            if (marker.getId().equals(marker_arr.get(i).getId())) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data", data);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSuccess(@NonNull String value) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(@NonNull Throwable throwable) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if (resultCode == Activity.RESULT_OK) {
            /*if (requestCode == EventConstant.GET_LOCATION) {
                String location = dataIntent.getStringExtra("location");
                saveBooleanIntoPrefs(EventConstant.REFRESH_REQUIRED, true);
                FilterPojo obj = getFilterData();
                if (location.equals("All Locations")) {
                    obj.setLocation("");
                    location = getResources().getString(R.string.location);
                } else {
                    obj.setLocation(location);
                }
                saveFilterData(obj);
                location_view.setText(location);
            } else*/
            if (requestCode == EventConstant.SHOW_DETAIL) {
                SomethingInterestingDataPojo dataObj = (SomethingInterestingDataPojo) dataIntent.getSerializableExtra("data");
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getId().equals(dataObj.getId())) {
                        data.get(i).setIs_favourite(dataObj.isIs_favourite());
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
