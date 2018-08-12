package com.endive.eventplanner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.pojo.EventPackageDetailPojo;
import com.endive.eventplanner.pojo.MerchandisePropertyDetailPojo;
import com.endive.eventplanner.pojo.PackageMerchandiseDetailPojo;
import com.endive.eventplanner.pojo.PackageTicketTypeDetailPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.ViewAnimationUtils;
import com.endive.eventplanner.view.CircularTextView;

import java.util.ArrayList;

/**
 * Created by upasna.mishra on 11/3/2017.
 */

public class PackageDetailActivity extends BaseActivity {

    private PackageDetailActivity ctx = this;
    private LinearLayout desc_layout, desc, available_ticket, available_product, proceed, lin_ticket_info, merchandise_list;
    private ImageView desc_down, ticket_down, product_down;
    private TextView desc_data;
    private EventPackageDetailPojo packageData;
    private ArrayList<PackageMerchandiseDetailPojo> arr;
    private SomethingInterestingDataPojo data;
    private int ticketCount = 0;
    private String venue_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);

        EventConstant.ACTIVITIES.add(ctx);

        data = (SomethingInterestingDataPojo) getIntent().getSerializableExtra("event_detail");
        packageData = (EventPackageDetailPojo) getIntent().getSerializableExtra("package_list");
        venue_id = getIntent().getStringExtra("venue_id");
        setHeader(packageData.getPackage_detail().getTitle());
        initialize();
        setListener();
    }

    private void initialize() {
        desc_layout = (LinearLayout) findViewById(R.id.desc_layout);
        desc = (LinearLayout) findViewById(R.id.desc);
        available_ticket = (LinearLayout) findViewById(R.id.available_ticket);
        available_product = (LinearLayout) findViewById(R.id.available_product);
        proceed = (LinearLayout) findViewById(R.id.proceed);
        lin_ticket_info = (LinearLayout) findViewById(R.id.lin_ticket_info);
        desc_down = (ImageView) findViewById(R.id.desc_down);
        ticket_down = (ImageView) findViewById(R.id.ticket_down);
        product_down = (ImageView) findViewById(R.id.product_down);
        desc_data = (TextView) findViewById(R.id.desc_data);

        merchandise_list = (LinearLayout) findViewById(R.id.merchandise_list);

        ArrayList<PackageTicketTypeDetailPojo> arrTicket = packageData.getPackage_detail().getPackage_ticket_type_detail();
        lin_ticket_info.removeAllViews();
        for (int i = 0; i < arrTicket.size(); i++) {
            View itemView = LayoutInflater.from(ctx).inflate(R.layout.package_ticket_row_layout, null, false);
            TextView tv_ticket_type = (TextView) itemView.findViewById(R.id.tv_ticket_type);
            TextView tv_total_ticket = (TextView) itemView.findViewById(R.id.tv_total_ticket);
            TextView tv_total_price_text = (TextView) itemView.findViewById(R.id.tv_total_price_text);
            TextView tv_total_ticket_text = (TextView) itemView.findViewById(R.id.tv_total_ticket_text);
            tv_total_price_text.setTextColor(ContextCompat.getColor(ctx, R.color.header));
            tv_total_ticket_text.setTextColor(ContextCompat.getColor(ctx, R.color.header));
            tv_ticket_type.setTextColor(ContextCompat.getColor(ctx, R.color.hint_color));
            tv_total_ticket.setTextColor(ContextCompat.getColor(ctx, R.color.hint_color));

            tv_ticket_type.setText(" " + arrTicket.get(i).getTicket_type_detail().getTitle());
            tv_total_ticket.setText(" " + arrTicket.get(i).getTicket_quantity());
            ticketCount += Integer.parseInt(arrTicket.get(i).getTicket_quantity());
            lin_ticket_info.addView(itemView);
        }

        desc_data.setText(packageData.getPackage_detail().getPackage_ticket_type_detail().get(0).getTicket_type_detail().getDescription());
        arr = packageData.getPackage_detail().getPackage_merchandise_detail();
        for (int i = 0; i < arr.size(); i++) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.row_layout_merchandise_detail, null, false);
            LinearLayout colors_layout = (LinearLayout) itemView.findViewById(R.id.colors_layout);
            LinearLayout size_layout = (LinearLayout) itemView.findViewById(R.id.size_layout);
            TextView merchandise_name = (TextView) itemView.findViewById(R.id.merchandise_name);
            TextView merchandise_quantity = (TextView) itemView.findViewById(R.id.merchandise_quantity);
            TextView desc_merchandise = (TextView) itemView.findViewById(R.id.desc_merchandise);
            ImageView merchandise_image = (ImageView) itemView.findViewById(R.id.merchandise_image);
            setProfileImageInLayout(ctx, (int) getResources().getDimension(R.dimen.merchandise_image), (int) getResources().getDimension(R.dimen.merchandise_image), arr.get(i).getMerchandise_detail().getImage(), merchandise_image);
            merchandise_quantity.setText(" ( " + arr.get(i).getMerchandise_quantity() + " )");
            merchandise_name.setText(arr.get(i).getMerchandise_detail().getName());
            desc_merchandise.setText(arr.get(i).getMerchandise_detail().getDescription());

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
    }

    private void setListener() {
        desc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descLayoutClick();
            }
        });

        available_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availableTicketClick();
            }
        });

        available_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availableProductClick();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (packageData.isIs_seating_arrangement()) {
                    Intent intent = new Intent(ctx, WebUrlActivity.class);
                    intent.putExtra("event_detail", data);
                    intent.putExtra("packageData", packageData);
                    intent.putExtra("ticketCount", ticketCount);
                    intent.putExtra("venue_id", venue_id);
                    intent.putExtra("header", getResources().getString(R.string.header_select_seats));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ctx, FillTicketDetailsActivity.class);
                    intent.putExtra("event_detail", data);
                    intent.putExtra("packageData", packageData);
                    intent.putExtra("venue_id", venue_id);
                    intent.putExtra("ticketCount", ticketCount);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void setArrow(ImageView image, boolean check) {
        if (check)
            image.setImageResource(R.mipmap.down_arrow_black);
        else
            image.setImageResource(R.mipmap.right_arrow_black);
    }

    private void descLayoutClick() {
        if (desc.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(desc);
            setArrow(desc_down, true);
        } else {
            ViewAnimationUtils.collapse(desc);
            setArrow(desc_down, false);
        }
    }

    private void availableTicketClick() {
        if (lin_ticket_info.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(lin_ticket_info);
            setArrow(ticket_down, true);
        } else {
            ViewAnimationUtils.collapse(lin_ticket_info);
            setArrow(ticket_down, false);
        }
    }

    private void availableProductClick() {
        if (merchandise_list.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(merchandise_list);
            setArrow(product_down, true);
        } else {
            ViewAnimationUtils.collapse(merchandise_list);
            setArrow(product_down, false);
        }
    }
}
