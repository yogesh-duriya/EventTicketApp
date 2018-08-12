package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/6/2017.
 */

public class CountriesPojo extends BasePojo{
    private ArrayList<CountriesDataPojo> data;

    public ArrayList<CountriesDataPojo> getData() {
        return data;
    }

    public void setData(ArrayList<CountriesDataPojo> data) {
        this.data = data;
    }
}
