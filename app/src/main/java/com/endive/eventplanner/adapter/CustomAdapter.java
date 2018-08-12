package com.endive.eventplanner.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.BuyTicketActivity;
import com.endive.eventplanner.pojo.TicketDataPojo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by arpit.jain on 11/23/2017.
 */

public class CustomAdapter extends ArrayAdapter<TicketDataPojo> {

    private ArrayList<TicketDataPojo> dataSet;
    private BuyTicketActivity ctx;

    // View lookup cache
    private static class ViewHolder {
        private TextView title_ticket, desc_ticket, date, price;
        private LinearLayout buy_ticket;

        public ViewHolder(View itemView) {
            title_ticket = (TextView) itemView.findViewById(R.id.title_ticket);
            desc_ticket = (TextView) itemView.findViewById(R.id.desc_ticket);
            date = (TextView) itemView.findViewById(R.id.date);
            price = (TextView) itemView.findViewById(R.id.price);
            buy_ticket = (LinearLayout) itemView.findViewById(R.id.buy_ticket);
        }
    }

    public CustomAdapter(ArrayList<TicketDataPojo> data, Activity context) {
        super(context, R.layout.row_layout_tickets, data);
        this.dataSet = data;
        this.ctx = (BuyTicketActivity) context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TicketDataPojo data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder holder; // view lookup cache stored in tag

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_layout_tickets, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title_ticket.setText(data.getTicket_type_title());
        holder.desc_ticket.setText(data.getTicket_type_description());

        SpannableStringBuilder builder = new SpannableStringBuilder();
        Date date = ctx.getFormattedDate(data.getStart_time());
        Date endDate = ctx.getFormattedDate(data.getEnd_time());
        builder.append(ctx.getColoredString("Available  ", ContextCompat.getColor(ctx, R.color.light_green)));
        builder.append(ctx.getColoredString("From ", ContextCompat.getColor(ctx, R.color.black)));
        builder.append(ctx.getColoredString(ctx.getDay(date) + " " + ctx.getMonth(date) + ", " + ctx.getYear(date), ContextCompat.getColor(ctx, R.color.hint_color)));
        builder.append(ctx.getColoredString(" To ", ContextCompat.getColor(ctx, R.color.black)));
        builder.append(ctx.getColoredString(ctx.getDay(endDate) + " " + ctx.getMonth(endDate) + ", " + ctx.getYear(endDate), ContextCompat.getColor(ctx, R.color.hint_color)));
        holder.date.setText(builder, TextView.BufferType.SPANNABLE);
        holder.price.setText(ctx.getResources().getString(R.string.dollar) + " " + data.getPrice());
        holder.buy_ticket.setTag(R.string.data, data);
        holder.buy_ticket.setOnClickListener(ctx);
        // Return the completed view to render on screen
        return convertView;
    }
}