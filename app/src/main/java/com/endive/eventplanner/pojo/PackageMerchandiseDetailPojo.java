package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by arpit.jain on 10/26/2017.
 */

public class PackageMerchandiseDetailPojo implements Serializable {

    private String package_merchandise_id;
    private String merchandise_id;
    private String merchandise_quantity;
    private String status;
    private MerchandiseDetailPojo merchandise_detail;

    public String getPackage_merchandise_id() {
        return package_merchandise_id;
    }

    public void setPackage_merchandise_id(String package_merchandise_id) {
        this.package_merchandise_id = package_merchandise_id;
    }

    public String getMerchandise_id() {
        return merchandise_id;
    }

    public void setMerchandise_id(String merchandise_id) {
        this.merchandise_id = merchandise_id;
    }

    public String getMerchandise_quantity() {
        return merchandise_quantity;
    }

    public void setMerchandise_quantity(String merchandise_quantity) {
        this.merchandise_quantity = merchandise_quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MerchandiseDetailPojo getMerchandise_detail() {
        return merchandise_detail;
    }

    public void setMerchandise_detail(MerchandiseDetailPojo merchandise_detail) {
        this.merchandise_detail = merchandise_detail;
    }
}
