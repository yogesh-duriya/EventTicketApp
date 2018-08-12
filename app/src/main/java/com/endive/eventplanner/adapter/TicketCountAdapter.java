package com.endive.eventplanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.BuyTicketActivity;
import com.endive.eventplanner.activity.EventDetailsActivity;
import com.endive.eventplanner.pojo.TicketCountPojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 11/2/2017.
 */

public class TicketCountAdapter extends RecyclerView.Adapter<TicketCountAdapter.ViewHolder> {

    private EventDetailsActivity ctx;
    private BuyTicketActivity obj;
    private Context activity;
    private ArrayList<TicketCountPojo> arr;
    private String from;

    public TicketCountAdapter(Context ctx, ArrayList<TicketCountPojo> arr, String from) {
        activity = ctx;
        if (from.equals("eventDetail"))
            this.ctx = (EventDetailsActivity) ctx;
        else
            obj = (BuyTicketActivity) ctx;
        this.arr = arr;
        this.from = from;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_no_of_tickets, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.number_of_ticket.setText(arr.get(position).getCount());
        holder.number_of_ticket.setTag(R.string.data, arr.get(position));
        if (from.equals("eventDetail"))
            holder.number_of_ticket.setOnClickListener(ctx);
        else
            holder.number_of_ticket.setOnClickListener(obj);
        if (arr.get(position).isSelected()) {
            holder.number_of_ticket.setBackground(activity.getResources().getDrawable(R.drawable.circle_bg));
        } else {
            holder.number_of_ticket.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView number_of_ticket;

        public ViewHolder(View itemView) {
            super(itemView);
            number_of_ticket = (TextView) itemView.findViewById(R.id.number_of_ticket);
        }
    }
}
