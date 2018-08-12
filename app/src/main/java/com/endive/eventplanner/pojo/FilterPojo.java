package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/12/2017.
 */

public class FilterPojo {
    private boolean relevance, date = true;
    private boolean paid, free;
    private String price_min = "0", price_max = "2000";
    private CategoriesDataPojo data;
    private String startDate, endDate;
    private String location="";
    private ArrayList<LocationDataPojo> locationArr;

    public boolean isRelevance() {
        return relevance;
    }

    public void setRelevance(boolean relevance) {
        this.relevance = relevance;
    }

    public boolean isDate() {
        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public CategoriesDataPojo getData() {
        return data;
    }

    public void setData(CategoriesDataPojo data) {
        this.data = data;
    }

    public String getPrice_min() {
        return price_min;
    }

    public void setPrice_min(String price_min) {
        this.price_min = price_min;
    }

    public String getPrice_max() {
        return price_max;
    }

    public void setPrice_max(String price_max) {
        this.price_max = price_max;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<LocationDataPojo> getLocationArr() {
        return locationArr;
    }

    public void setLocationArr(ArrayList<LocationDataPojo> locationArr) {
        this.locationArr = locationArr;
    }
}
