package com.example.testapplication.core.service;

import java.util.HashMap;
import java.util.Map;

public enum ServiceFactory {
    INSTANCE;

    private Map<String, Object> services = new HashMap<>();
}