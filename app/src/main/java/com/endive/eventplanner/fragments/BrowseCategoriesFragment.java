package com.endive.eventplanner.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.HomeActivity;
import com.endive.eventplanner.adapter.CategoriesGridAdapter;
import com.endive.eventplanner.pojo.CategoryListPojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/7/2017.
 */

public class BrowseCategoriesFragment extends Fragment {
    private HomeActivity ctx;
    private View view;
    private GridView category_name_grid;
    private ArrayList<CategoryListPojo> items;
    private boolean fragmentResume = false;
    private boolean fragmentVisible = false;
    private boolean fragmentOnCreated = false;
    private CategoriesGridAdapter adapter = null;
    private ImageView map_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_browse_category, container, false);

        initialize();

        setListener();
        return view;
    }

    private void initialize() {
        ctx = (HomeActivity) getActivity();
        items = new ArrayList<>();
        map_image = (ImageView) view.findViewById(R.id.map_image);
        category_name_grid = (GridView) view.findViewById(R.id.category_name_grid);

        if (!fragmentResume && fragmentVisible && ctx != null && ctx.getCategoryData() != null) {   //only when first time fragment is created
            items = ctx.getCategoryData().getData().getCategory();
            setAdapter(items);
        }
    }

    private void setListener() {
        map_image.setOnClickListener(ctx);
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);

        if (visible && isResumed() && items != null && items.size() == 0) {   // only at fragment screen is resumed
            fragmentResume = true;
            fragmentVisible = false;
            fragmentOnCreated = true;

            if (ctx.getCategoryData() != null && ctx.getCategoryData().getData() != null) {
                items = ctx.getCategoryData().getData().getCategory();
                setAdapter(items);
            }
        } else if (visible) {        // only at fragment onCreated
            fragmentResume = false;
            fragmentVisible = true;
            fragmentOnCreated = true;
        } else if (!visible && fragmentOnCreated) {// only when you go out of fragment screen
            fragmentVisible = false;
            fragmentResume = false;
        }
    }

    private void setAdapter(ArrayList<CategoryListPojo> arr) {
        adapter = new CategoriesGridAdapter(ctx, ctx.getCategoryData().getData().getCategory());
        category_name_grid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
