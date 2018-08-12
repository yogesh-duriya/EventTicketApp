package com.endive.eventplanner.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/26/2017.
 */

public class MerchandisePropertyDetailPojo implements Serializable {
    private ArrayList<ColorSizeListPojo> color;
    private ArrayList<ColorSizeListPojo> size;

    public ArrayList<ColorSizeListPojo> getColor() {
        return color;
    }

    public void setColor(ArrayList<ColorSizeListPojo> color) {
        this.color = color;
    }

    public ArrayList<ColorSizeListPojo> getSize() {
        return size;
    }

    public void setSize(ArrayList<ColorSizeListPojo> size) {
        this.size = size;
    }
}
