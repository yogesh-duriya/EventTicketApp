package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/26/2017.
 */

public class PackageDataPojo {
    private ArrayList<EventPackageDetailPojo> eventpackage_detail;

    public ArrayList<EventPackageDetailPojo> getEventpackage_detail() {
        return eventpackage_detail;
    }

    public void setEventpackage_detail(ArrayList<EventPackageDetailPojo> eventpackage_detail) {
        this.eventpackage_detail = eventpackage_detail;
    }
}
