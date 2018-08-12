package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 12/22/2017.
 */

public class HistoryPojo extends BasePojo{
    private ArrayList<PaymentNonceResultPojo> data;

    public ArrayList<PaymentNonceResultPojo> getData() {
        return data;
    }

    public void setData(ArrayList<PaymentNonceResultPojo> data) {
        this.data = data;
    }
}
