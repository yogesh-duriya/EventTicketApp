package com.endive.eventplanner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.endive.eventplanner.R;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.EventPackageDetailPojo;
import com.endive.eventplanner.pojo.SeatsPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.pojo.TicketDataPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;

import org.apache.http.util.EncodingUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by upasna.mishra on 10/31/2017.
 */

public class WebUrlActivity extends BaseActivity {

    private WebView webview;
    private Intent intent;
    private String web_link, header;
    private TicketDataPojo dataTicket;
    private int ticketCount;
    private ConnectionDetector cd;
    private SomethingInterestingDataPojo data;
    private EventPackageDetailPojo packageData;
    private String venue_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_web_url);
        intent = getIntent();
        header = intent.getStringExtra("header");
        venue_id = intent.getStringExtra("venue_id");
        data = (SomethingInterestingDataPojo) intent.getSerializableExtra("event_detail");
        dataTicket = (TicketDataPojo) intent.getSerializableExtra("dataTicket");
        packageData = (EventPackageDetailPojo) getIntent().getSerializableExtra("packageData");
        ticketCount = intent.getIntExtra("ticketCount", 0);
        setHeader(header);
        initialize();
    }

    private void initialize() {
        web_link = intent.getStringExtra("web_url");
        cd = new ConnectionDetector(this);

        webview = (WebView) findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(EventConstant.SEAT_BASE_URL+"thanks")) {
                    getSelectedSeats();
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
            }
        });

        String type;
        if (data.getEvent_type_id().equals("1"))
            type = "3";
        else
            type = "1";
        if (ticketCount > 0) {
            String url = EventConstant.SEAT_BASE_URL+"select-event-seat";
            String postData = "";
            if (dataTicket != null)
                postData = "booking_type=" + type + "&event_group_ticket_id=" + dataTicket.getEvent_group_ticket_id() + "&event_package_id=&event_id=" + data.getId() + "&device_id=" + getFromPrefs(EventConstant.ANDROID_ID) + "&no_of_ticket=" + ticketCount + "&venue_id=" + venue_id;
            else if (packageData != null)
                postData = "booking_type=2&event_group_ticket_id=&event_package_id=" + packageData.getEventpackage_id() + "&event_id=" + data.getId() + "&device_id=" + getFromPrefs(EventConstant.ANDROID_ID) + "&no_of_ticket=" + ticketCount + "&venue_id=" + venue_id;
            else
                postData = "booking_type=" + type + "&event_group_ticket_id=&event_package_id=&event_id=" + data.getId() + "&device_id=" + getFromPrefs(EventConstant.ANDROID_ID) + "&no_of_ticket=" + ticketCount + "&venue_id=" + venue_id;
            webview.postUrl(url, EncodingUtils.getBytes(postData, "base64"));
        } else {
            openURL(web_link);
        }
    }

    private void openURL(String url) {
        webview.loadUrl(url);
        webview.requestFocus();
    }

    private void getSelectedSeats() {
        if (cd.isConnectingToInternet()) {

            ApiClient.changeApiBaseUrl(EventConstant.SEAT_BASE_URL);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);


            Call<SeatsPojo> call = apiService.getSeats(getFromPrefs(EventConstant.ANDROID_ID));
            call.enqueue(new Callback<SeatsPojo>() {
                @Override
                public void onResponse(Call<SeatsPojo> call, Response<SeatsPojo> response) {
                    ApiClient.changeApiBaseUrl(EventConstant.BASE_URL);
                    Intent intent = new Intent(WebUrlActivity.this, FillTicketDetailsActivity.class);
                    intent.putExtra("ticketCount", ticketCount);
                    intent.putExtra("event_detail", data);
                    intent.putExtra("dataTicket", dataTicket);
                    intent.putExtra("venue_id",venue_id);
                    intent.putExtra("seats", response.body().getData().getObjects());
                    intent.putExtra("packageData", packageData);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<SeatsPojo> call, Throwable t) {
                    // Log error here since request failed
                }
            });
        }
    }
}
