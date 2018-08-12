package com.endive.eventplanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.HomeActivity;
import com.endive.eventplanner.pojo.CategoryListPojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 11/21/2017.
 */

public class CategoriesGridAdapter extends BaseAdapter {
    private HomeActivity ctx;
    private ArrayList<CategoryListPojo> categoriesList;

    public CategoriesGridAdapter(HomeActivity ctx, ArrayList<CategoryListPojo> categoriesList) {
        this.ctx = ctx;
        this.categoriesList = categoriesList;
    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_layout_categories_image, null);
            holder = new ViewHolder();
            holder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
            holder.categoryImage = (ImageView) convertView.findViewById(R.id.category_image);
            holder.semi_transparent_layout = (LinearLayout) convertView.findViewById(R.id.semi_transparent_layout);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.categoryName.setText(categoriesList.get(position).getCategory_name());
        ctx.setCategoryImageInLayout(ctx, categoriesList.get(position).getCategory_icon(), holder.categoryImage);
        convertView.setTag(holder);

        holder.categoryImage.getViewTreeObserver().addOnPreDrawListener(new   ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.categoryImage.getViewTreeObserver().removeOnPreDrawListener(this);

                int height = holder.categoryImage.getHeight();
                int width = holder.categoryImage.getWidth();
                holder.semi_transparent_layout.requestLayout();
                holder.semi_transparent_layout.getLayoutParams().height = height;
                holder.semi_transparent_layout.getLayoutParams().width = width;
                return false;
            }
        });

        holder.categoryImage.setTag(R.string.data, categoriesList.get(position));
        holder.categoryImage.setTag(R.string.type, "category");
        holder.categoryImage.setOnClickListener(ctx);

        return convertView;
    }

    // convert view class
    class ViewHolder {
        private TextView categoryName;
        private ImageView categoryImage;
        private LinearLayout semi_transparent_layout;
    }
}
