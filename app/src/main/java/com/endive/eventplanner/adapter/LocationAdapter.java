package com.endive.eventplanner.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.FilterActivity;
import com.endive.eventplanner.pojo.LocationDataPojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/28/2017.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private Activity ctx;
    private ArrayList<LocationDataPojo> locationList;
    private int lastPosition = -1;

    public LocationAdapter(Activity ctx, ArrayList<LocationDataPojo> arr) {
        this.ctx = ctx;
        locationList = arr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_categories, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocationDataPojo data = locationList.get(position);
        holder.category_name.setText(data.getCity());
        holder.right_arrow.setVisibility(View.GONE);
        if (data.isSelected()) {
            holder.category_selected_checkbox.setImageResource(R.mipmap.circle_colour);
        } else {
            holder.category_selected_checkbox.setImageResource(R.mipmap.circle);
        }
        holder.root_layout_categories.setTag(R.string.data, data);
        holder.root_layout_categories.setTag(R.string.type, "location");
        holder.root_layout_categories.setOnClickListener((FilterActivity) ctx);

//        setAnimation(holder.itemView, position);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx,
                    (position > lastPosition) ? R.anim.up_from_bottom
                            : R.anim.down_from_top);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView category_name, venue_name;
        private ImageView category_selected_checkbox, right_arrow;
        private LinearLayout root_layout_categories;

        public ViewHolder(View itemView) {
            super(itemView);
            category_name = (TextView) itemView.findViewById(R.id.category_name);
            venue_name = (TextView) itemView.findViewById(R.id.venue_name);
            category_selected_checkbox = (ImageView) itemView.findViewById(R.id.category_selected_checkbox);
            right_arrow = (ImageView) itemView.findViewById(R.id.right_arrow);
            root_layout_categories = (LinearLayout) itemView.findViewById(R.id.root_layout_categories);
        }
    }
}
