package com.example.testapplication.shared.enums;

public enum StatusEnum {
    PENDING("Pending"),
    FOR_DELIVERY("For Delivery"),
    PAID("Paid");
    private String label;

    StatusEnum(String s) {
        this.label = s;
    }
}
