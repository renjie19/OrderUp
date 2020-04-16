package com.example.testapplication.shared.util;

import com.example.testapplication.shared.pojo.Client;

import java.util.HashMap;
import java.util.Map;

public enum ClientMapper {
    INSTANCE;

    public Map<String, Object> clientToMap(Client client) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", client.getToken());
        map.put("name", client.getName());
        map.put("contact", client.getContactNo());
        map.put("location", client.getLocation());
        return map;
    }
}
