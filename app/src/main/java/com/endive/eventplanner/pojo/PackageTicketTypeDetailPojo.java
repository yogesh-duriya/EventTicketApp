package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by arpit.jain on 10/26/2017.
 */

public class PackageTicketTypeDetailPojo implements Serializable{

    private String package_ticket_type_id;
    private String package_id;
    private String ticket_type_id;
    private String ticket_quantity;
    private PackageTicketDetailPojo ticket_type_detail;

    public String getPackage_ticket_type_id() {
        return package_ticket_type_id;
    }

    public void setPackage_ticket_type_id(String package_ticket_type_id) {
        this.package_ticket_type_id = package_ticket_type_id;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getTicket_type_id() {
        return ticket_type_id;
    }

    public void setTicket_type_id(String ticket_type_id) {
        this.ticket_type_id = ticket_type_id;
    }

    public String getTicket_quantity() {
        return ticket_quantity;
    }

    public void setTicket_quantity(String ticket_quantity) {
        this.ticket_quantity = ticket_quantity;
    }

    public PackageTicketDetailPojo getTicket_type_detail() {
        return ticket_type_detail;
    }

    public void setTicket_type_detail(PackageTicketDetailPojo ticket_type_detail) {
        this.ticket_type_detail = ticket_type_detail;
    }
}
