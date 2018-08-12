package com.endive.eventplanner.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.BaseActivity;
import com.endive.eventplanner.activity.HomeActivity;
import com.endive.eventplanner.activity.OrganizeProfileActivity;
import com.endive.eventplanner.activity.RecommendedEventsActivity;
import com.endive.eventplanner.activity.SearchResultActivity;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by arpit.jain on 10/7/2017.
 */

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolder> {

    private HomeActivity ctx;
    private SearchResultActivity obj;
    private OrganizeProfileActivity organ;
    private RecommendedEventsActivity recommend;
    private ArrayList<SomethingInterestingDataPojo> eventsList;
    private String from, set_price;
    private LinearLayout topLinearLayout;
    private Context context;
    private int lastPosition = -1;
    private boolean showStartHeader, secondHeader;
    private String id, id_later;

    public EventsListAdapter(Activity ctx, ArrayList<SomethingInterestingDataPojo> arr, String from) {
        eventsList = arr;
        context = ctx;
        this.from = from;
        showStartHeader = true;
        secondHeader = true;
        if (from.equals("home")) {
            this.ctx = (HomeActivity) ctx;
        } else if (from.equals("search")) {
            obj = (SearchResultActivity) ctx;
        } else if (from.equals("recommended")) {
            recommend = (RecommendedEventsActivity) ctx;
        } else {
            organ = (OrganizeProfileActivity) ctx;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_home_screen, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SomethingInterestingDataPojo data = eventsList.get(position);

        holder.event_name.setText(data.getTitle());
        holder.event_location.setText(data.getVenue());
//        holder.tags_list.removeAllViews();

        if (data.isIs_favourite())
            holder.heart.setImageResource(R.mipmap.map_heart_list_hover);
        else
            holder.heart.setImageResource(R.mipmap.map_heart_list);

        if (data.isIs_liked())
            holder.like.setImageResource(R.mipmap.map_list_like_hover);
        else
            holder.like.setImageResource(R.mipmap.map_list_like);

        if (data.isIs_in_cart())
            holder.cart.setImageResource(R.mipmap.map_list_cart_hover);
        else
            holder.cart.setImageResource(R.mipmap.map_list_cart);

        holder.heart.setTag(R.string.data, data);
        holder.like.setTag(R.string.data, data);
        holder.cart.setTag(R.string.data, data);
        holder.share.setTag(R.string.data, data);
        holder.home_row.setTag(R.string.data, data);
        holder.event_location.setTag(R.string.data, data);

        if (from.equals("home")) {
            holder.heart.setOnClickListener(ctx);
            holder.like.setOnClickListener(ctx);
            holder.cart.setOnClickListener(ctx);
            holder.share.setOnClickListener(ctx);
            holder.home_row.setOnClickListener(ctx);
            holder.event_location.setOnClickListener(ctx);
        } else if (from.equals("search")) {
            holder.heart.setOnClickListener(obj);
            holder.like.setOnClickListener(obj);
            holder.cart.setOnClickListener(obj);
            holder.share.setOnClickListener(obj);
            holder.home_row.setOnClickListener(obj);
            holder.event_location.setOnClickListener(obj);
        } else if (from.equals("recommended")) {
            holder.heart.setOnClickListener(recommend);
            holder.like.setOnClickListener(recommend);
            holder.cart.setOnClickListener(recommend);
            holder.share.setOnClickListener(recommend);
            holder.home_row.setOnClickListener(recommend);
            holder.event_location.setOnClickListener(recommend);
        } else {
            holder.heart.setOnClickListener(organ);
            holder.like.setOnClickListener(organ);
            holder.cart.setOnClickListener(organ);
            holder.share.setOnClickListener(organ);
            holder.home_row.setOnClickListener(organ);
            holder.event_location.setOnClickListener(organ);
        }

        if ((data.getEvent_type_id().equals("1") && data.getPrice().equals("")) || data.getPrice() == null)
            holder.price.setText(context.getResources().getString(R.string.free));
        else
            holder.price.setText(context.getResources().getString(R.string.dollar) + data.getPrice().replace(".00", ""));

        Date date = ((BaseActivity) context).getFormattedDate(data.getStart_time());
        holder.date.setText(((BaseActivity) context).getDay(date) + "\n" + ((BaseActivity) context).getMonth(date));
        holder.event_date.setVisibility(View.GONE);

        ((BaseActivity) context).setImageInLayout(context, 600, (int) context.getResources().getDimension(R.dimen.home_row_layout_height), data.getImage(), holder.event_image);
        String startDate = ((BaseActivity) context).getFilterData().getStartDate();
        if (startDate != null && !startDate.equals("") && from.equals("home")) {
            if (startDate.equals(((BaseActivity) context).getDate(date)) && showStartHeader && id == null) {
                id = data.getId();
                showStartHeader = false;
                holder.list_title.setText(context.getResources().getString(R.string.events_on) + " " + startDate);
                holder.header.setVisibility(View.VISIBLE);
            } else if (!startDate.equals(((BaseActivity) context).getDate(date)) && secondHeader && id_later == null) {
                id_later = data.getId();
                secondHeader = false;
                holder.list_title.setText(context.getResources().getString(R.string.events_on_upcoming));
                holder.header.setVisibility(View.VISIBLE);
            } else {
                holder.header.setVisibility(View.GONE);
                holder.header.setVisibility(View.GONE);
            }
        }
        if (id != null && id.equals(data.getId())) {
            holder.list_title.setText(context.getResources().getString(R.string.events_on) + " " + startDate);
            holder.header.setVisibility(View.VISIBLE);
        }
        if (id_later != null && id_later.equals(data.getId())) {
            holder.list_title.setText(context.getResources().getString(R.string.events_on_upcoming));
            holder.header.setVisibility(View.VISIBLE);
        }
//        topLinearLayout = new LinearLayout(context);
//        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        topLinearLayout.setPadding(5, 5, 5, 5);
//
////        if (data.getTags() != null)
////            ((BaseActivity) context).setTagList(topLinearLayout, data);
//
//        holder.tags_list.addView(topLinearLayout);


        setAnimation(holder.itemView, position);

    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
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
        return eventsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView event_image, heart, like, share, cart;
        private TextView price, event_date, event_name, event_location;
        private TextView date, list_title;
        private LinearLayout header;
        private HorizontalScrollView tags_list;
        private LinearLayout home_row;

        public ViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price);
            event_date = (TextView) itemView.findViewById(R.id.event_date);
            date = (TextView) itemView.findViewById(R.id.date);
            event_name = (TextView) itemView.findViewById(R.id.event_name);
            event_location = (TextView) itemView.findViewById(R.id.event_location);
            list_title = (TextView) itemView.findViewById(R.id.list_title);
            header = (LinearLayout) itemView.findViewById(R.id.header);
            event_image = (ImageView) itemView.findViewById(R.id.event_image);
            heart = (ImageView) itemView.findViewById(R.id.heart);
            like = (ImageView) itemView.findViewById(R.id.like);
            share = (ImageView) itemView.findViewById(R.id.share);
            cart = (ImageView) itemView.findViewById(R.id.cart);
            home_row = (LinearLayout) itemView.findViewById(R.id.home_row);
            tags_list = (HorizontalScrollView) itemView.findViewById(R.id.tags_list);
        }
    }
}
