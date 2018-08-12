package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/24/2017.
 */

public class VenuePojo extends BasePojo {

    private ArrayList<VenueDataPojo> data;

    public ArrayList<VenueDataPojo> getData() {
        return data;
    }

    public void setData(ArrayList<VenueDataPojo> data) {
        this.data = data;
    }
}
