package com.endive.eventplanner.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by arpit.jain on 12/19/2017.
 */

public class PaymentNoncePackageDataPojo implements Serializable {
    private String name;
    private String gender;
    private String age;
    private ArrayList<PaymentNoncePackageProductPojo> product_array;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<PaymentNoncePackageProductPojo> getProduct_array() {
        return product_array;
    }

    public void setProduct_array(ArrayList<PaymentNoncePackageProductPojo> product_array) {
        this.product_array = product_array;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
