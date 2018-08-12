package com.endive.eventplanner.pojo;

/**
 * Created by arpit.jain on 10/13/2017.
 */

public class EventListPojo {
    private String id;
    private String event_type_name;
    private boolean is_selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_type_name() {
        return event_type_name;
    }

    public void setEvent_type_name(String event_type_name) {
        this.event_type_name = event_type_name;
    }

    public boolean is_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}
