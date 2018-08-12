package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by upasna.mishra on 11/6/2017.
 */

public class CouponDataPojo implements Serializable{

    private String coupon_name, coupon_code, coupon_code_type, coupon_code_price_type, coupon_code_amount;
    private boolean isSelected;

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getCoupon_code_type() {
        return coupon_code_type;
    }

    public void setCoupon_code_type(String coupon_code_type) {
        this.coupon_code_type = coupon_code_type;
    }

    public String getCoupon_code_price_type() {
        return coupon_code_price_type;
    }

    public void setCoupon_code_price_type(String coupon_code_price_type) {
        this.coupon_code_price_type = coupon_code_price_type;
    }

    public String getCoupon_code_amount() {
        return coupon_code_amount;
    }

    public void setCoupon_code_amount(String coupon_code_amount) {
        this.coupon_code_amount = coupon_code_amount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
