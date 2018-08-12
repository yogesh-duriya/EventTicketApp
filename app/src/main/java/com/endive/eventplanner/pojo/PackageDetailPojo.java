package com.endive.eventplanner.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/26/2017.
 */

public class PackageDetailPojo implements Serializable {

    private String package_id;
    private String title;
    private String created_by;
    private String created_by_id;
    private ArrayList<PackageTicketTypeDetailPojo> package_ticket_type_detail;
    private ArrayList<PackageMerchandiseDetailPojo> package_merchandise_detail;

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(String created_by_id) {
        this.created_by_id = created_by_id;
    }

    public ArrayList<PackageTicketTypeDetailPojo> getPackage_ticket_type_detail() {
        return package_ticket_type_detail;
    }

    public void setPackage_ticket_type_detail(ArrayList<PackageTicketTypeDetailPojo> package_ticket_type_detail) {
        this.package_ticket_type_detail = package_ticket_type_detail;
    }

    public ArrayList<PackageMerchandiseDetailPojo> getPackage_merchandise_detail() {
        return package_merchandise_detail;
    }

    public void setPackage_merchandise_detail(ArrayList<PackageMerchandiseDetailPojo> package_merchandise_detail) {
        this.package_merchandise_detail = package_merchandise_detail;
    }
}
