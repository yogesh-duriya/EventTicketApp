package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/25/2017.
 */

public class TicketPojo extends BasePojo {
    private ArrayList<TicketDataPojo> data;

    public ArrayList<TicketDataPojo> getData() {
        return data;
    }

    public void setData(ArrayList<TicketDataPojo> data) {
        this.data = data;
    }
}
