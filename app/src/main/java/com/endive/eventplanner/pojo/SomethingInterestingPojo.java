package com.endive.eventplanner.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/7/2017.
 */

public class SomethingInterestingPojo extends BasePojo implements Serializable{
    private ArrayList<SomethingInterestingDataPojo> data;

    public ArrayList<SomethingInterestingDataPojo> getData() {
        return data;
    }

    public void setData(ArrayList<SomethingInterestingDataPojo> data) {
        this.data = data;
    }
}
