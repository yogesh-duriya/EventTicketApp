package com.endive.eventplanner.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.CouponAdapter;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.CouponDataPojo;
import com.endive.eventplanner.pojo.CouponPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventDialogs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by upasna.mishra on 11/3/2017.
 */

public class CouponActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rv_coupon_list;
    private LinearLayout apply;
    private ConnectionDetector cd;
    private EventDialogs dialog;
    private ArrayList<CouponDataPojo> couponArr;
    private String amount = "0", coupon_code, amount_type;
    private CouponAdapter adapter;
    private CouponDataPojo data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        setHeader(getResources().getString(R.string.header_coupons));
        initialize();
        setListener();
    }

    private void initialize() {
        couponArr = new ArrayList<>();

        data = (CouponDataPojo) getIntent().getSerializableExtra("coupon_data");
        rv_coupon_list = (RecyclerView) findViewById(R.id.rv_coupon_list);
        apply = (LinearLayout) findViewById(R.id.apply);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_coupon_list.setLayoutManager(mLayoutManager);
        rv_coupon_list.setItemAnimator(new DefaultItemAnimator());

        cd = new ConnectionDetector(this);
        dialog = new EventDialogs(this);

        getCouponList(getIntent().getStringExtra("event_id"));
    }

    private void getCouponList(String event_id) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(this);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<CouponPojo> call = apiService.getCouponData("EventCoupons", event_id);
            call.enqueue(new Callback<CouponPojo>() {
                @Override
                public void onResponse(Call<CouponPojo> call, Response<CouponPojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
                        couponArr = response.body().getData();
                        if (data != null && data.isSelected())
                            for (int i = 0; i < couponArr.size(); i++) {
                                if (data.getCoupon_code().equals(couponArr.get(i).getCoupon_code())) {
                                    couponArr.get(i).setSelected(data.isSelected());
                                    break;
                                }
                            }
                        setAdapter(couponArr);
                    } else if (response.body() != null) {
                        dialog.displayCommonDialog(response.body().getError_message());
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<CouponPojo> call, Throwable t) {
                    // Log error here since request failed
                    System.out.println("retrofit hh failure " + t.getMessage());
                    d.dismiss();
                }
            });
        } else {
            dialog.displayCommonDialog(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void setAdapter(ArrayList<CouponDataPojo> arr) {
        adapter = new CouponAdapter(this, arr, coupon_code);
        if (arr != null)
            rv_coupon_list.setAdapter(adapter);
    }

    private void setListener() {
        apply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply:
                if (data == null || !data.isSelected()) {
                    displayToast(getResources().getString(R.string.select_coupon));
                } else {
                    Intent coupon_intent = new Intent();
                    coupon_intent.putExtra("coupon_data", data);
                    setResult(Activity.RESULT_OK, coupon_intent);
                    finish();
                }
                break;

            case R.id.lin_coupon_select:
//                if(data != null)
//                    data.setSelected(!data.isSelected());
                data = (CouponDataPojo) view.getTag(R.string.data);
                data.setSelected(!data.isSelected());
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent coupon_intent = new Intent();
        setResult(Activity.RESULT_CANCELED, coupon_intent);
        finish();
    }
}
