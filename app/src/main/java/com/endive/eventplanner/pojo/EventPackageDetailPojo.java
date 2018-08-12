package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by arpit.jain on 10/26/2017.
 */

public class EventPackageDetailPojo implements Serializable {

    private String eventpackage_id;
    private String event_id;
    private String package_id;
    private String start_time;
    private String end_time;
    private String price;
    private String quantity;
    private String status;
    private PackageDetailPojo package_detail;
    private boolean is_sold;
    private boolean is_seating_arrangement;

    public boolean isIs_seating_arrangement() {
        return is_seating_arrangement;
    }

    public void setIs_seating_arrangement(boolean is_seating_arrangement) {
        this.is_seating_arrangement = is_seating_arrangement;
    }

    public String getEventpackage_id() {
        return eventpackage_id;
    }

    public void setEventpackage_id(String eventpackage_id) {
        this.eventpackage_id = eventpackage_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PackageDetailPojo getPackage_detail() {
        return package_detail;
    }

    public void setPackage_detail(PackageDetailPojo package_detail) {
        this.package_detail = package_detail;
    }

    public boolean isIs_sold() {
        return is_sold;
    }

    public void setIs_sold(boolean is_sold) {
        this.is_sold = is_sold;
    }
}
