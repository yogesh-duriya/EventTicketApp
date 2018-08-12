package com.endive.eventplanner.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.MapActivity;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/11/2017.
 */

public class MapPagerAdapter extends PagerAdapter {
    // Declare Variables
    private MapActivity ctx;
    private ArrayList<SomethingInterestingDataPojo> events_arr;
    private LayoutInflater inflater;
    private int width;

    public MapPagerAdapter(MapActivity context, ArrayList<SomethingInterestingDataPojo> events_arr, int width) {
        this.ctx = context;
        this.events_arr = events_arr;
        this.width = width;
    }

    @Override
    public int getCount() {
        return events_arr.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        SomethingInterestingDataPojo data = events_arr.get(position);
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.map_pager_layout, container, false);

        ImageView event_image = (ImageView) itemView.findViewById(R.id.event_image_map);
        TextView event_date = (TextView) itemView.findViewById(R.id.event_date);
        TextView event_name = (TextView) itemView.findViewById(R.id.event_name);
        TextView event_location = (TextView) itemView.findViewById(R.id.event_location);
        LinearLayout map_pager_row = (LinearLayout) itemView.findViewById(R.id.map_pager_row);

        TextView price = (TextView) itemView.findViewById(R.id.price);
        ImageView heart = (ImageView) itemView.findViewById(R.id.heart);
        ImageView like = (ImageView) itemView.findViewById(R.id.like);
        ImageView share = (ImageView) itemView.findViewById(R.id.share);
        ImageView cart = (ImageView) itemView.findViewById(R.id.cart);
//        HorizontalScrollView tags_list = (HorizontalScrollView) itemView.findViewById(R.id.tags_list);
//
//        LinearLayout topLinearLayout = new LinearLayout(ctx);
//        // topLinearLayout.setLayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.FILL_PARENT);
//        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        topLinearLayout.setPadding(5, 5, 5, 5);
//        ctx.setTagList(topLinearLayout, data);
//        tags_list.addView(topLinearLayout);

        event_name.setText(data.getTitle());
        event_date.setText(ctx.getDate(data.getStart_time()));
        event_location.setText(data.getVenue());
        if (data.getEvent_type_id().equals("1") && data.getPrice().equals(""))
            price.setText(ctx.getResources().getString(R.string.free));
        else
            price.setText(ctx.getResources().getString(R.string.dollar) + data.getPrice().replace(".00", ""));

        // Capture position and set to the ImageView
        ctx.setImageInLayout(ctx, width, (int) ctx.getResources().getDimension(R.dimen.map_view_pager_height), events_arr.get(position).getImage(), event_image);

        if (data.isIs_favourite())
            heart.setImageResource(R.mipmap.map_heart_list_hover);
        else
            heart.setImageResource(R.mipmap.map_heart_list);

        if (data.isIs_liked())
            like.setImageResource(R.mipmap.map_list_like_hover);
        else
            like.setImageResource(R.mipmap.map_list_like);

        if (data.isIs_in_cart())
            cart.setImageResource(R.mipmap.map_list_cart_hover);
        else
            cart.setImageResource(R.mipmap.map_list_cart);

        heart.setTag(R.string.data, data);
        like.setTag(R.string.data, data);
        cart.setTag(R.string.data, data);
        share.setTag(R.string.data, data);
        map_pager_row.setTag(R.string.data, data);

        heart.setOnClickListener(ctx);
        like.setOnClickListener(ctx);
        cart.setOnClickListener(ctx);
        share.setOnClickListener(ctx);
        map_pager_row.setOnClickListener(ctx);

        // Add viewpager_item.xml to ViewPager
        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((View) object);

    }
}
