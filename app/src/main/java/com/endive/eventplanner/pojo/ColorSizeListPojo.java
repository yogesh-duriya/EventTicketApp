package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by arpit.jain on 11/9/2017.
 */

public class ColorSizeListPojo implements Serializable{
    private String merchandise_property_id;
    private String merchandise_id;
    private String merchandise_type;
    private String color_name;
    private String color;
    private String size;
    private String status_color;
    private String status_size;
    private boolean selected = false;

    public String getStatus_size() {
        return status_size;
    }

    public void setStatus_size(String status_size) {
        this.status_size = status_size;
    }

    public String getStatus_color() {
        return status_color;
    }

    public void setStatus_color(String status_color) {
        this.status_color = status_color;
    }

    public String getMerchandise_property_id() {
        return merchandise_property_id;
    }

    public void setMerchandise_property_id(String merchandise_property_id) {
        this.merchandise_property_id = merchandise_property_id;
    }

    public String getMerchandise_id() {
        return merchandise_id;
    }

    public void setMerchandise_id(String merchandise_id) {
        this.merchandise_id = merchandise_id;
    }

    public String getMerchandise_type() {
        return merchandise_type;
    }

    public void setMerchandise_type(String merchandise_type) {
        this.merchandise_type = merchandise_type;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
