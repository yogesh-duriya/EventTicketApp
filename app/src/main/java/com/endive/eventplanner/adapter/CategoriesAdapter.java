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
import com.endive.eventplanner.pojo.CategoriesDataPojo;
import com.endive.eventplanner.pojo.CategoryListPojo;
import com.endive.eventplanner.pojo.EventListPojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/9/2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private Activity ctx;
    private CategoriesDataPojo data;
    private ArrayList<CategoryListPojo> categoriesList;
    private ArrayList<EventListPojo> eventList;
    private String from;
    private String type;
    private int lastPosition = -1;

    public CategoriesAdapter(Activity ctx, CategoriesDataPojo arr, String from, String type) {
        this.ctx = ctx;
        data = arr;
        categoriesList = data.getCategory();
        eventList = data.getEvent_type();
        this.from = from;
        this.type = type;
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
        CategoryListPojo data;
        EventListPojo eventData;
        if (from.equals("filter") && type.equals("category")) {
            data = categoriesList.get(position);
            holder.category_name.setText(data.getCategory_name());
            if (data.is_selected()) {
                holder.category_selected_checkbox.setImageResource(R.mipmap.circle_colour);
            } else {
                holder.category_selected_checkbox.setImageResource(R.mipmap.circle);
            }
            holder.root_layout_categories.setTag(R.string.data, data);
            holder.root_layout_categories.setTag(R.string.type, "category");
            holder.root_layout_categories.setOnClickListener((FilterActivity) ctx);
            holder.right_arrow.setVisibility(View.GONE);
        } else {
            eventData = eventList.get(position);
            holder.category_name.setText(eventData.getEvent_type_name());
            if (eventData.is_selected()) {
                holder.category_selected_checkbox.setImageResource(R.mipmap.circle_colour);
            } else {
                holder.category_selected_checkbox.setImageResource(R.mipmap.circle);
            }
            holder.root_layout_categories.setTag(R.string.type, "event");
            holder.root_layout_categories.setTag(R.string.data, eventData);
            holder.root_layout_categories.setOnClickListener((FilterActivity) ctx);
            holder.right_arrow.setVisibility(View.GONE);
        }

        setAnimation(holder.itemView, position);
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
        if (type.equals("category")) {
            return categoriesList.size();
        } else {
            return eventList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView category_name;
        private ImageView right_arrow, category_selected_checkbox;
        private LinearLayout root_layout_categories;

        public ViewHolder(View itemView) {
            super(itemView);
            category_name = (TextView) itemView.findViewById(R.id.category_name);
            category_selected_checkbox = (ImageView) itemView.findViewById(R.id.category_selected_checkbox);
            right_arrow = (ImageView) itemView.findViewById(R.id.right_arrow);
            root_layout_categories = (LinearLayout) itemView.findViewById(R.id.root_layout_categories);
        }
    }
}
