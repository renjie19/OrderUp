package com.example.testapplication.core.repository;

import android.util.Log;

import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

class OrderRepositoryImpl implements OrderRepository {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public Order save(Order order) {
        if (order.getId() == null) {
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
    public List<Order> getOrdersByClient(Client client) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.refresh();
            List<Order> list = realm.where(Order.class)
                    .equalTo("client.token", client.getToken())
                    .sort("date", Sort.DESCENDING).findAll();
            return realm.copyFromRealm(list);
        } catch (Exception e) {
            Log.d(TAG, "getOrders: Error Occurred");
        } finally {
            realm.close();
        }
        return new ArrayList<>();
    }

    @Override
    public void removeOrdersByClient(Client client) {
        Realm realm = Realm.getDefaultInstance();
        realm.refresh();
        realm.executeTransactionAsync(realm1 -> {
            RealmResults<Order> list = realm1.where(Order.class)
                    .equalTo("client.uid", client.getUid()).findAll();
            list.deleteAllFromRealm();
        });
    }

    @Override
    public Order getOrder(String id) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.refresh();
            Order order = realm.where(Order.class)
                    .equalTo("id", id).findFirst();
            return realm.copyFromRealm(order);
        } catch (Exception e) {
            Log.d(TAG, "getOrder: " + e.getMessage());
        } finally {
            realm.close();
        }
        return null;
    }

    @Deprecated
    @Override
    public Order update(Order order) {
        Realm realm = Realm.getDefaultInstance();
        realm.refresh();
        Order savedOrder = realm.where(Order.class).equalTo("id", order.getId()).findFirst();
        realm.executeTransaction(realm1 -> {
            savedOrder.setItems(new RealmList<>());
            savedOrder.getItems().addAll(order.getItems());
            savedOrder.setStatus(order.getStatus());
            savedOrder.setForPayment(order.isForPayment());
        });
        return realm.copyFromRealm(savedOrder);
    }
}
