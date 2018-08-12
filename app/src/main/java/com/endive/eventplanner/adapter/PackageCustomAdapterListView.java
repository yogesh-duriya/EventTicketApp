package com.endive.eventplanner.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.BuyTicketActivity;
import com.endive.eventplanner.pojo.EventPackageDetailPojo;
import com.endive.eventplanner.pojo.MerchandisePropertyDetailPojo;
import com.endive.eventplanner.pojo.PackageMerchandiseDetailPojo;
import com.endive.eventplanner.pojo.PackageTicketTypeDetailPojo;
import com.endive.eventplanner.view.CircularTextView;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 11/23/2017.
 */

public class PackageCustomAdapterListView extends ArrayAdapter<EventPackageDetailPojo> {

    private BuyTicketActivity ctx;

    // View lookup cache
    private static class ViewHolder {
        private TextView tv_desc, tv_buy_now, tv_pack_name;
        private RelativeLayout lin_top_back;
        private LinearLayout lin_ticket_info;

        public ViewHolder(View view) {
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);

            tv_buy_now = (TextView) view.findViewById(R.id.tv_buy_now);
            tv_pack_name = (TextView) view.findViewById(R.id.tv_pack_name);
            lin_top_back = (RelativeLayout) view.findViewById(R.id.lin_top_back);
            lin_ticket_info = (LinearLayout) view.findViewById(R.id.lin_ticket_info);

        }
    }

    public PackageCustomAdapterListView(ArrayList<EventPackageDetailPojo> data, Activity context) {
        super(context, R.layout.row_layout_package, data);
        this.ctx = (BuyTicketActivity) context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final EventPackageDetailPojo data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder holder; // view lookup cache stored in tag

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_layout_package, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (data.isIs_sold()) {
            holder.lin_top_back.setBackground(ContextCompat.getDrawable(ctx, R.drawable.dark_grey_top_round));
            holder.tv_buy_now.setBackground(ContextCompat.getDrawable(ctx, R.drawable.dark_grey_round_btn));
        } else {
            holder.lin_top_back.setBackground(ContextCompat.getDrawable(ctx, R.drawable.blue_round_top));
            holder.tv_buy_now.setBackground(ContextCompat.getDrawable(ctx, R.drawable.sign_up_round_btn));
            holder.tv_buy_now.setOnClickListener(ctx);
            holder.tv_buy_now.setTag(R.string.packageList, data);
        }

        holder.tv_pack_name.setText(data.getPackage_detail().getTitle());
        ArrayList<PackageTicketTypeDetailPojo> arrTicket = data.getPackage_detail().getPackage_ticket_type_detail();
        holder.lin_ticket_info.removeAllViews();
        for(int i = 0; i< arrTicket.size(); i++){
            View itemView = LayoutInflater.from(ctx).inflate(R.layout.package_ticket_row_layout, null, false);
            TextView tv_ticket_type = (TextView) itemView.findViewById(R.id.tv_ticket_type);
            TextView tv_total_ticket = (TextView) itemView.findViewById(R.id.tv_total_ticket);
            tv_ticket_type.setText(arrTicket.get(i).getTicket_type_detail().getTitle());
            tv_total_ticket.setText(arrTicket.get(i).getTicket_quantity());
            holder.lin_ticket_info.addView(itemView);
        }

        holder.tv_desc.setText(data.getPackage_detail().getPackage_ticket_type_detail().get(0).getTicket_type_detail().getDescription());
        ArrayList<PackageMerchandiseDetailPojo> arr = data.getPackage_detail().getPackage_merchandise_detail();
        LinearLayout merchandise_list = (LinearLayout) convertView.findViewById(R.id.merchandise_list);
        merchandise_list.removeAllViews();
        for (int i = 0; i < arr.size(); i++) {
            View itemView = LayoutInflater.from(ctx).inflate(R.layout.row_layout_merchandise, null, false);
            LinearLayout colors_layout = (LinearLayout) itemView.findViewById(R.id.colors_layout);
            LinearLayout size_layout = (LinearLayout) itemView.findViewById(R.id.size_layout);
            TextView merchandise_name = (TextView) itemView.findViewById(R.id.merchandise_name);
            TextView merchandise_quantity = (TextView) itemView.findViewById(R.id.merchandise_quantity);
            merchandise_quantity.setText(arr.get(i).getMerchandise_quantity());
            merchandise_name.setText(arr.get(i).getMerchandise_detail().getName());

            MerchandisePropertyDetailPojo arrMerchandise = arr.get(i).getMerchandise_detail().getMerchandise_property_detail();
            for (int j = 0; j < arrMerchandise.getColor().size(); j++) {
                View colorView = LayoutInflater.from(ctx).inflate(R.layout.row_layout_color, null, false);
                CircularTextView color_view = (CircularTextView) colorView.findViewById(R.id.color_view);
                if (arrMerchandise.getColor().get(j).getColor() != null && arrMerchandise.getColor().get(j).getColor().length() > 0) {
                    color_view.setSolidColor("#" + arrMerchandise.getColor().get(j).getColor());
                    color_view.setStrokeWidth(1);
                    color_view.setStrokeColor();
                }

                colors_layout.addView(colorView);
            }

            for (int j = 0; j < arrMerchandise.getSize().size(); j++) {
                View sizeView = LayoutInflater.from(ctx).inflate(R.layout.row_layout_size, null, false);
                TextView size_view = (TextView) sizeView.findViewById(R.id.size_view);
                size_view.setText(arrMerchandise.getSize().get(j).getSize());
                size_layout.addView(sizeView);
            }

            merchandise_list.addView(itemView);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
