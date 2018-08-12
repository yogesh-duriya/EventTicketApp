package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by arpit.jain on 11/6/2017.
 */

public class PersonTicketDetailPojo implements Serializable{
    private String name = "";
    private String age = "";
    private String gender = "1";
    private EventPackageDetailPojo packageData;
    private String seat = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public EventPackageDetailPojo getPackageData() {
        return packageData;
    }

    public void setPackageData(EventPackageDetailPojo packageData) {
        this.packageData = packageData;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
