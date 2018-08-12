package com.endive.eventplanner.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.HistoryAdapter;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.HistoryPojo;
import com.endive.eventplanner.pojo.PaymentNonceResultPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends BaseActivity implements View.OnClickListener {

    private HistoryActivity ctx = this;
    private RecyclerView history_list;
    private HistoryAdapter adapter;
    private ConnectionDetector cd;
    private EventDialogs dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setHeader(getResources().getString(R.string.history));

        initialize();
        setListener();
    }

    private void initialize() {
        history_list = (RecyclerView) findViewById(R.id.history_list);
        ctx.setRecyclerLayoutManager(history_list);

        cd = new ConnectionDetector(ctx);
        dialog = new EventDialogs(ctx);
        getHistoryListData();
    }

    private void setListener() {

    }

    private void getHistoryListData() {
        if (ctx.adapter != null)
            ctx.adapter.notifyDataSetChanged();
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<HistoryPojo> call = apiService.getHistory("TicketHistory", getFromPrefs(EventConstant.USER_ID));
            call.enqueue(new Callback<HistoryPojo>() {
                @Override
                public void onResponse(Call<HistoryPojo> call, Response<HistoryPojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
                        setAdapter(response.body().getData());
                    } else if (response.body() != null) {
                        dialog.displayCommonDialog(response.body().getError_message());
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<HistoryPojo> call, Throwable t) {
                    // Log error here since request failed
                    System.out.println("retrofit hh failure " + t.getMessage());
                    d.dismiss();
                }
            });
        } else {
            dialog.displayCommonDialog(ctx.getResources().getString(R.string.no_internet_connection));
        }
    }

    private void setAdapter(ArrayList<PaymentNonceResultPojo> arr) {
        if(arr != null) {
            adapter = new HistoryAdapter(ctx, arr);
            history_list.setAdapter(ctx.adapter);
        }
    }

    @Override
    public void onClick(View v) {
        PaymentNonceResultPojo data = (PaymentNonceResultPojo) v.getTag(R.string.data);
        Intent intent = new Intent(ctx, PlacedOrderDetailActivity.class);
        intent.putExtra("payment_response", data);
        intent.putExtra("from", "history");
        startActivity(intent);
    }
}
