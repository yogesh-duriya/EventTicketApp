package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/31/2017.
 */

public class OrganizerDataPojo {

    private ArrayList<SomethingInterestingDataPojo> event_detail;
    private OrganizerProfilePojo organizer_detail;

    public ArrayList<SomethingInterestingDataPojo> getEvent_detail() {
        return event_detail;
    }

    public void setEvent_detail(ArrayList<SomethingInterestingDataPojo> event_detail) {
        this.event_detail = event_detail;
    }

    public OrganizerProfilePojo getOrganizer_detail() {
        return organizer_detail;
    }

    public void setOrganizer_detail(OrganizerProfilePojo organizer_detail) {
        this.organizer_detail = organizer_detail;
    }
}
