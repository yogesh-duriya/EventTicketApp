package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by upasna.mishra on 11/6/2017.
 */

public class CouponPojo extends BasePojo {

    private ArrayList<CouponDataPojo> data;

    public ArrayList<CouponDataPojo> getData() {
        return data;
    }

    public void setData(ArrayList<CouponDataPojo> data) {
        this.data = data;
    }
}
