package com.endive.eventplanner.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.EventsListAdapter;
import com.endive.eventplanner.adapter.OrganizerAdapter;
import com.endive.eventplanner.interfaces.UpdateImageIntercace;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.OrganizerPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.DepthPageTransformer;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;
import com.endive.eventplanner.view.CircularImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.endive.eventplanner.R.id.pager;

/**
 * Created by upasna.mishra on 10/30/2017.
 */

public class OrganizeProfileActivity extends BaseActivity implements View.OnClickListener, UpdateImageIntercace {

    private CircularImageView profile_image;
    private TextView company_name, organizer_name, web_site_url, tab1_text, tab2_text, tab3_text, tab4_text;
    private LinearLayout tab1_layout, tab2_layout, tab3_layout, tab1_selector, tab2_selector, tab3_selector, tab4_layout, tab4_selector;
    private OrganizeProfileActivity ctx = this;
    private ViewPager viewPager;
    private OrganizerAdapter mAdapter;
    private ConnectionDetector cd = null;
    private EventDialogs dialog;
    public static String organizer_id;
    public EventsListAdapter adapter = null;
    public EventsListAdapter adapterUpcoming = null;
    public EventsListAdapter adapterPrevious = null;
    private int fragment_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_profile);
        setHeader(getResources().getString(R.string.header_organizer_profile));
        initialize();
        setListener();
    }

    private void initialize() {
        dialog = new EventDialogs(ctx);
        cd = new ConnectionDetector(ctx);
        mAdapter = new OrganizerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(pager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        profile_image = (CircularImageView) findViewById(R.id.profile_image);
        company_name = (TextView) findViewById(R.id.company_name);
        organizer_name = (TextView) findViewById(R.id.organizer_name);
        web_site_url = (TextView) findViewById(R.id.web_site_url);
        tab1_text = (TextView) findViewById(R.id.tab1_text);
        tab2_text = (TextView) findViewById(R.id.tab2_text);
        tab3_text = (TextView) findViewById(R.id.tab3_text);
        tab4_text = (TextView) findViewById(R.id.tab4_text);
        tab1_layout = (LinearLayout) findViewById(R.id.tab1_layout);
        tab2_layout = (LinearLayout) findViewById(R.id.tab2_layout);
        tab3_layout = (LinearLayout) findViewById(R.id.tab3_layout);
        tab4_layout = (LinearLayout) findViewById(R.id.tab4_layout);
        tab1_selector = (LinearLayout) findViewById(R.id.tab1_selector);
        tab2_selector = (LinearLayout) findViewById(R.id.tab2_selector);
        tab3_selector = (LinearLayout) findViewById(R.id.tab3_selector);
        tab4_selector = (LinearLayout) findViewById(R.id.tab4_selector);
        tab1_text.setText(getResources().getString(R.string.current_event));
        tab2_text.setText(getResources().getString(R.string.upcomming_event));
        tab3_text.setText(getResources().getString(R.string.prev_event));
        tab4_text.setVisibility(View.GONE);
        tab4_selector.setVisibility(View.GONE);
        tab4_layout.setVisibility(View.GONE);
        organizer_id = getIntent().getStringExtra("id");
        organizerData(getIntent().getStringExtra("id"), "1");
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
                        company_name.setText(response.body().getData().getOrganizer_detail().getCompany_name());
                        organizer_name.setText(response.body().getData().getOrganizer_detail().getOrganizer_name());
                        web_site_url.setText(response.body().getData().getOrganizer_detail().getOrganizer_website());
                        if (response.body().getData().getOrganizer_detail().getProfile_pic() != "") {
                            setProfileImageInLayout(ctx, (int) getResources().getDimension(R.dimen.profile_image),
                                    (int) getResources().getDimension(R.dimen.profile_image),
                                    response.body().getData().getOrganizer_detail().getProfile_pic(), profile_image);
                        } else {
                            profile_image.setBackground(getResources().getDrawable(R.mipmap.profile_bg));
                        }
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
            dialog.displayCommonDialog(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void setListener() {
        web_site_url.setOnClickListener(this);
        tab1_layout.setOnClickListener(this);
        tab2_layout.setOnClickListener(this);
        tab3_layout.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                fragment_pos = position;
                setStyle(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        SomethingInterestingDataPojo data;
        switch (view.getId()) {
            case R.id.web_site_url:
                Intent web_url = new Intent(this, WebLinkActivity.class);
                web_url.putExtra("web_url", web_site_url.getText().toString());
                web_url.putExtra("header", getResources().getString(R.string.header_organizer_profile));
                startActivity(web_url);
                break;

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

            case R.id.tab3_layout:
                if (viewPager.getCurrentItem() != 2) {
                    viewPager.setCurrentItem(2);
                    setStyle(2);
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
                    updateAdapter();
                } else {
                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setStyle(int position) {
        if (position == 0) {
            tab2_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint_color));
            tab3_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint_color));
            tab1_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.button_color));

            tab2_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.filter_divider));
            tab3_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.filter_divider));
            tab1_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.button_color));
        } else if (position == 1) {
            tab2_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.button_color));
            tab1_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint_color));
            tab3_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint_color));

            tab1_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.filter_divider));
            tab3_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.filter_divider));
            tab2_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.button_color));
        } else if (position == 2) {
            tab3_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.button_color));
            tab1_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint_color));
            tab2_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint_color));

            tab1_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.filter_divider));
            tab2_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.filter_divider));
            tab3_selector.setBackgroundColor(ContextCompat.getColor(ctx, R.color.button_color));
        }
    }

    @Override
    public void onSuccess(@NonNull String value) {
        updateAdapter();
    }

    @Override
    public void onError(@NonNull Throwable throwable) {

    }

    private void updateAdapter() {
        if (fragment_pos == 0)
            adapter.notifyDataSetChanged();
        else if (fragment_pos == 1)
            adapterUpcoming.notifyDataSetChanged();
        else if (fragment_pos == 2)
            adapterPrevious.notifyDataSetChanged();
    }
}
