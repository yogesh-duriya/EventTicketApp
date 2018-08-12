package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by arpit.jain on 10/26/2017.
 */

public class PackageTicketDetailPojo implements Serializable {

    private String ticket_type_id;
    private String title;
    private String description;
    private String image;

    public String getTicket_type_id() {
        return ticket_type_id;
    }

    public void setTicket_type_id(String ticket_type_id) {
        this.ticket_type_id = ticket_type_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
