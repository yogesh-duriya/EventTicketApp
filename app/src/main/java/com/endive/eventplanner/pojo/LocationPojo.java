package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/31/2017.
 */

public class LocationPojo extends BasePojo{
    private ArrayList<LocationDataPojo> data;

    public ArrayList<LocationDataPojo> getData() {
        return data;
    }

    public void setData(ArrayList<LocationDataPojo> data) {
        this.data = data;
    }
}
