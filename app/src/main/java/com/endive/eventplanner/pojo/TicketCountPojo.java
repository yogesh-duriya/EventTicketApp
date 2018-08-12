package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by arpit.jain on 11/2/2017.
 */

public class TicketCountPojo implements Serializable{
    private String count;
    private boolean selected;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
