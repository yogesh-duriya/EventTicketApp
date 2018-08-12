package com.endive.eventplanner.pojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 11/23/2017.
 */

public class EventDetailPojo extends BasePojo{
    private ArrayList<TicketDataPojo> ticket_detail;
    private ArrayList<VenueDataPojo> venue_detail;
    private PackageDataPojo package_detail;

    public ArrayList<TicketDataPojo> getTicket_detail() {
        return ticket_detail;
    }

    public void setTicket_detail(ArrayList<TicketDataPojo> ticket_detail) {
        this.ticket_detail = ticket_detail;
    }

    public ArrayList<VenueDataPojo> getVenue_detail() {
        return venue_detail;
    }

    public void setVenue_detail(ArrayList<VenueDataPojo> venue_detail) {
        this.venue_detail = venue_detail;
    }

    public PackageDataPojo getPackage_detail() {
        return package_detail;
    }

    public void setPackage_detail(PackageDataPojo package_detail) {
        this.package_detail = package_detail;
    }
}
