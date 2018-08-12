package com.endive.eventplanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.CouponActivity;
import com.endive.eventplanner.pojo.CouponDataPojo;

import java.util.ArrayList;

/**
 * Created by upasna.mishra on 11/3/2017.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private CouponActivity ctx;
    private ArrayList<CouponDataPojo> couponList;
    private String coupon_code;

    public CouponAdapter(CouponActivity ctx, ArrayList<CouponDataPojo> arr, String coupon_code) {
        this.ctx = ctx;
        couponList = arr;
        this.coupon_code = coupon_code;
    }

    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_coupon, parent, false);
        CouponAdapter.ViewHolder viewHolder = new CouponAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CouponAdapter.ViewHolder holder, final int position) {
        CouponDataPojo data = couponList.get(position);
        holder.tv_coupon_name.setText(data.getCoupon_name());
        holder.tv_coupon_code.setText(data.getCoupon_code());
        if (data.getCoupon_code_price_type().equals("1")) {
            holder.tv_amount.setText(ctx.getResources().getString(R.string.dollar)+" " + data.getCoupon_code_amount());
        } else {
            holder.tv_amount.setText(data.getCoupon_code_amount() +" "+ctx.getResources().getString(R.string.percent));
        }

        if (data.isSelected()) {
            holder.iv_circle.setImageResource(R.mipmap.circle_colour);
        } else {
            holder.iv_circle.setImageResource(R.mipmap.circle);
        }

        holder.lin_coupon_select.setTag(R.string.data, data);
        holder.lin_coupon_select.setOnClickListener(ctx);
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_circle;
        private TextView tv_coupon_name, tv_coupon_code, tv_amount;
        private LinearLayout lin_coupon_select;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_circle = (ImageView) itemView.findViewById(R.id.iv_circle);
            tv_coupon_code = (TextView) itemView.findViewById(R.id.tv_coupon_code);
            tv_coupon_name = (TextView) itemView.findViewById(R.id.tv_coupon_name);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            lin_coupon_select = (LinearLayout) itemView.findViewById(R.id.lin_coupon_select);
        }
    }
}
