package com.endive.eventplanner.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.CategoriesPojo;
import com.endive.eventplanner.pojo.FilterPojo;
import com.endive.eventplanner.pojo.LocationPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.splunk.mint.Mint;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    private Handler handler;
    private Runnable runnable;
    private ConnectionDetector cd;
    private String identifier = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mint.initAndStartSession(this.getApplication(), "fd79cdd3");

        setContentView(R.layout.activity_splash);
        initialize();

        saveBooleanIntoPrefs(EventConstant.REFRESH_REQUIRED, false);
        EventConstant.ACTIVITIES = new ArrayList<>();
        saveFilterData(new FilterPojo());

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            TextView versionText = (TextView) findViewById(R.id.version);
            versionText.setText(getResources().getString(R.string.app_version) + " " + version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

//        displayToast("xhxhxxhxhx "+height);
//        displayToast("xhxhxxhxhx "+getResources().getDisplayMetrics());
//        ZopimChat.init("5IS95oiI62u67lyI2hu4Dvm4F0MOqZNq");
        getAndroidId();
    }

    private void getAndroidId() {
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        saveIntoPrefs(EventConstant.ANDROID_ID, identifier);
    }

    private void initialize() {
        cd = new ConnectionDetector(this);
        getEventTypeAndCategoryData();
        getLocation();
        getClientToken();
    }

    private void getEventTypeAndCategoryData() {
        if (cd.isConnectingToInternet()) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<CategoriesPojo> call = apiService.getCategories("GetEventType");
            call.enqueue(new Callback<CategoriesPojo>() {
                @Override
                public void onResponse(Call<CategoriesPojo> call, Response<CategoriesPojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
                        saveCategoryData(response.body());
                    }
                }

                @Override
                public void onFailure(Call<CategoriesPojo> call, Throwable t) {
                    // Log error here since request failed
                }
            });
        }
    }

    private void getLocation() {
        if (cd.isConnectingToInternet()) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<LocationPojo> call = apiService.getLocations("Locations");
            call.enqueue(new Callback<LocationPojo>() {
                @Override
                public void onResponse(Call<LocationPojo> call, Response<LocationPojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
                        saveLocationData(response.body());
                    }
                }

                @Override
                public void onFailure(Call<LocationPojo> call, Throwable t) {
                    // Log error here since request failed
                    System.out.println("retrofit hh failure " + t.getMessage());
                }
            });
        }
    }

    private void getClientToken() {
        /*if (cd.isConnectingToInternet()) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<BasePojo> call = apiService.getClientToken("PaymentApi");
            call.enqueue(new Callback<BasePojo>() {
                @Override
                public void onResponse(Call<BasePojo> call, Response<BasePojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
                        saveIntoPrefs(EventConstant.CLIENT_TOKEN, response.body().getClient_token());*/
                        handler = new Handler();
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent;
                                if (getFromPrefs(EventConstant.USER_ID).equals(""))
                                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                                else
                                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        };
                        handler.postDelayed(runnable, 200);
                   /* }
                }

                @Override
                public void onFailure(Call<BasePojo> call, Throwable t) {
                    // Log error here since request failed
                    t.printStackTrace();
                    System.out.println("hh failure "+t.toString());
                    System.out.println("hh failure "+call.toString());
                }
            });
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (handler != null)
            handler.removeCallbacks(runnable);
    }
}
