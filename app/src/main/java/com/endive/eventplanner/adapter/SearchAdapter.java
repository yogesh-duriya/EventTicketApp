package com.endive.eventplanner.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.SearchActivity;
import com.endive.eventplanner.pojo.CategoriesDataPojo;
import com.endive.eventplanner.pojo.TopCategoryPojo;
import com.endive.eventplanner.pojo.TopSearchPojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/26/2017.
 */

public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Activity ctx;
    private CategoriesDataPojo data;
    private ArrayList<TopSearchPojo> searchList;
    private ArrayList<TopCategoryPojo> categoriesList;
    private String type;

    public SearchAdapter(Activity ctx, CategoriesDataPojo arr, String type) {
        this.ctx = ctx;
        data = arr;
        searchList = data.getTop_search();
        categoriesList = data.getTop_category();
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
        TopCategoryPojo data;
        TopSearchPojo searchData;
        holder.category_selected_checkbox.setVisibility(View.GONE);
        if (type.equals("category")) {
            data = categoriesList.get(position);
            holder.category_name.setText(data.getCategory_name());

            holder.root_layout_categories.setTag(R.string.data, data);
        } else {
            searchData = searchList.get(position);
            holder.category_name.setText(searchData.getKeyword());

            holder.root_layout_categories.setTag(R.string.data, searchData);
        }
        holder.root_layout_categories.setTag(R.string.type, type);
        holder.root_layout_categories.setOnClickListener((SearchActivity) ctx);
    }

    @Override
    public int getItemCount() {
        if (type.equals("category")) {
            return categoriesList.size();
        } else {
            return searchList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView category_name;
        private LinearLayout root_layout_categories;
        private ImageView category_selected_checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            category_name = (TextView) itemView.findViewById(R.id.category_name);
            root_layout_categories = (LinearLayout) itemView.findViewById(R.id.root_layout_categories);
            category_selected_checkbox = (ImageView) itemView.findViewById(R.id.category_selected_checkbox);
        }
    }
}
