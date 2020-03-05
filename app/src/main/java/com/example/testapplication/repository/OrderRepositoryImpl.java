package com.example.testapplication.repository;

import android.util.Log;

import com.example.testapplication.pojo.Client;
import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.pojo.Order;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

class OrderRepositoryImpl extends OrderRepository {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public Consumer save(Order order) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(order);
        realm.commitTransaction();
        realm.close();
        return null;
    }

    @Override
    public List<Consumer> getOrders(Client client) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.refresh();
            List<Consumer> list = realm.where(Consumer.class)
                    .equalTo("name",client.getName())
                    .equalTo("location", client.getLocation())
                    .findAll();
            return realm.copyFromRealm(list);
        } catch (Exception e) {
            Log.d(TAG, "getOrders: Error Occurred");
        }
        return new ArrayList<>();
    }
}
