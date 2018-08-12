package com.endive.eventplanner.pojo;

/**
 * Created by upasna.mishra on 11/6/2017.
 */

public class ContactDataPojo {

    private String id, address, support_email, phone_no, fax_no, copyright_text, deleted_at, created_at, updated_at,
            address_latitude, address_longitude;

    public String getAddress_latitude() {
        return address_latitude;
    }

    public void setAddress_latitude(String address_latitude) {
        this.address_latitude = address_latitude;
    }

    public String getAddress_longitude() {
        return address_longitude;
    }

    public void setAddress_longitude(String address_longitude) {
        this.address_longitude = address_longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSupport_email() {
        return support_email;
    }

    public void setSupport_email(String support_email) {
        this.support_email = support_email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getFax_no() {
        return fax_no;
    }

    public void setFax_no(String fax_no) {
        this.fax_no = fax_no;
    }

    public String getCopyright_text() {
        return copyright_text;
    }

    public void setCopyright_text(String copyright_text) {
        this.copyright_text = copyright_text;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
