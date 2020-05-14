package com.example.testapplication.shared.enums;

public enum StatusEnum {
    PENDING("PENDING"),
    FOR_DELIVERY("FOR DELIVERY"),
    PAID("PAID");

    public final String label;

    private StatusEnum(String label) {
        this.label = label;
    }
}
