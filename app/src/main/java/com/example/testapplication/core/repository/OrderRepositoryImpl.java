package com.example.testapplication.core.repository;

import android.util.Log;

import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Consumer;
import com.example.testapplication.shared.pojo.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;

class OrderRepositoryImpl extends OrderRepository {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public Order save(Order order) {
        if(order.getId() == null) {
            order.setId(UUID.randomUUID().toString());
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(order);
        realm.commitTransaction();
        realm.close();
        return order;
    }

    @Override
    public List<Order> getOrders(Client client) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.refresh();
            List<Order> list = realm.where(Order.class).equalTo("client.token",client.getToken()).findAll();
            return realm.copyFromRealm(list);
        } catch (Exception e) {
            Log.d(TAG, "getOrders: Error Occurred");
        }
        return new ArrayList<>();
    }

    @Override
    public Order getOrder(String id) {
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.refresh();
            Order order = realm.where(Order.class)
                    .equalTo("id", id).findFirst();
            return realm.copyFromRealm(order);
        } catch (Exception e) {
            Log.d(TAG, "getOrder: "+e.getMessage());
        }
        return null;
    }

    @Override
    public Order update(Order order) {
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.refresh();
            return realm.copyToRealmOrUpdate(order);
        } catch (Exception e) {
            Log.d(TAG, "update: "+e.getMessage());
        }
        return order;
    }
}