package com.endive.eventplanner.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.HistoryActivity;
import com.endive.eventplanner.pojo.PaymentNonceResultPojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 12/22/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<PaymentNonceResultPojo> arr_tickets_list;
    private HistoryActivity ctx;

    public HistoryAdapter(Activity ctx, ArrayList<PaymentNonceResultPojo> arr) {
        arr_tickets_list = arr;
        this.ctx = (HistoryActivity) ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_history, parent, false);
        HistoryAdapter.ViewHolder viewHolder = new HistoryAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PaymentNonceResultPojo data = arr_tickets_list.get(position);

        holder.event_name.setText(data.getEvent_title());
        holder.event_location.setText(data.getVenue_name());
        holder.no_of_tickets_history.setText(ctx.getResources().getString(R.string.tickets)+" : "+data.getNo_of_tickets());
        holder.event_date.setText(ctx.getDate(data.getEvent_date()));
        ctx.setImageInLayout(ctx,  (int) ctx.getResources().getDimension(R.dimen.map_view_pager_height), (int) ctx.getResources().getDimension(R.dimen.map_view_pager_height), data.getEvent_image(), holder.event_image);
        holder.itemView.setTag(R.string.data, data);
        holder.itemView.setOnClickListener(ctx);
    }

    @Override
    public int getItemCount() {
        return arr_tickets_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView event_image;
        private TextView event_date, event_name, event_location, no_of_tickets_history;
        public ViewHolder(View itemView) {
            super(itemView);
            event_date = (TextView) itemView.findViewById(R.id.event_start_history);
            event_name = (TextView) itemView.findViewById(R.id.event_name_history);
            event_location = (TextView) itemView.findViewById(R.id.venue_history);
            no_of_tickets_history = (TextView) itemView.findViewById(R.id.no_of_tickets_history);
            event_image = (ImageView) itemView.findViewById(R.id.event_image_history);
        }
    }
}
