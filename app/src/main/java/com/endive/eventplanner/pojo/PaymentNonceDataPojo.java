package com.endive.eventplanner.pojo;

import java.io.Serializable;

/**
 * Created by arpit.jain on 11/30/2017.
 */

public class PaymentNonceDataPojo extends BasePojo implements Serializable{
    private PaymentNonceResultPojo result;

    public PaymentNonceResultPojo getResult() {
        return result;
    }

    public void setResult(PaymentNonceResultPojo result) {
        this.result = result;
    }
}
