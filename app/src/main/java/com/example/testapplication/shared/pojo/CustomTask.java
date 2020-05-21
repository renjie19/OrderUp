package com.example.testapplication.shared.pojo;

import lombok.Data;

@Data
public class CustomTask<T> {
    private T result;
    private Exception exception;
    private boolean isSuccessful;

    public CustomTask(T result, Exception exception, boolean isSuccessful) {
        this.result = result;
        this.exception = exception;
        this.isSuccessful = isSuccessful;
    }
}
