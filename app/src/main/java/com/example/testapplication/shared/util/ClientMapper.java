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

    public Client mapToClient(Map<String, Object> map, String id) {
        Client client = new Client();
        client.setName(String.format("%s %s",map.get("firstName"), map.get("lastName")));
        client.setLocation(String.valueOf(map.get("location")));
        client.setContactNo(String.valueOf(map.get("contact")));
        client.setToken(String.valueOf(map.get("token")));
        client.setUid(id);
        return client;
    }
}
