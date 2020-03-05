package com.example.testapplication.service;

import java.util.HashMap;
import java.util.Map;

public enum ServiceFactory {
    INSTANCE;

    private Map<String, Object> services = new HashMap<>();
}
