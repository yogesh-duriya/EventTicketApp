package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by arpit.jain on 10/24/2017.
 */

public class VenueDataPojo implements Serializable{
    private String id;
    private String venue_name;
    private String city;
    private String address;
    private String latitude;
    private String longitude;
    private String start_date;
    private String end_date;
    private String distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
