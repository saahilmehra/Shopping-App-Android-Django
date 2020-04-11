package com.saahil.smehrashop.Models;

public class PaymentStatus {
    String payment_status;

    public PaymentStatus(){

    }

    public PaymentStatus(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}
