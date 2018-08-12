package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by arpit.jain on 10/26/2017.
 */

public class MerchandiseDetailPojo  implements Serializable{

    private String merchandise_id;
    private String name;
    private String description;
    private String status;
    private String image;
    private MerchandisePropertyDetailPojo merchandise_property_detail;

    public String getMerchandise_id() {
        return merchandise_id;
    }

    public void setMerchandise_id(String merchandise_id) {
        this.merchandise_id = merchandise_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MerchandisePropertyDetailPojo getMerchandise_property_detail() {
        return merchandise_property_detail;
    }

    public void setMerchandise_property_detail(MerchandisePropertyDetailPojo merchandise_property_detail) {
        this.merchandise_property_detail = merchandise_property_detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
