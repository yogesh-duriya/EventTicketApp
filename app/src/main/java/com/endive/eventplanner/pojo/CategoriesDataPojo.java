package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/9/2017.
 */

public class CategoriesDataPojo {
    private ArrayList<CategoryListPojo> category;
    private ArrayList<EventListPojo> event_type;
    private ArrayList<TopSearchPojo> top_search;
    private ArrayList<TopCategoryPojo> top_category;

    public ArrayList<CategoryListPojo> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<CategoryListPojo> category) {
        this.category = category;
    }

    public ArrayList<EventListPojo> getEvent_type() {
        return event_type;
    }

    public void setEvent_type(ArrayList<EventListPojo> event_type) {
        this.event_type = event_type;
    }

    public ArrayList<TopSearchPojo> getTop_search() {
        return top_search;
    }

    public void setTop_search(ArrayList<TopSearchPojo> top_search) {
        this.top_search = top_search;
    }

    public ArrayList<TopCategoryPojo> getTop_category() {
        return top_category;
    }

    public void setTop_category(ArrayList<TopCategoryPojo> top_category) {
        this.top_category = top_category;
    }
}
