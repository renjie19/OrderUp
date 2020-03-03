package com.example.testapplication.repository;

import android.util.Log;

import com.example.testapplication.OrderUp;
import com.example.testapplication.pojo.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;

class ConsumerRepositoryImpl extends ConsumerRepository {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public Consumer save(Consumer consumer) {
        consumer.setId(UUID.randomUUID().toString());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(consumer);
        realm.commitTransaction();
        realm.close();
        return null;
    }

    @Override
    public List<Consumer> getAll() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.refresh();
            List<Consumer> list = realm.where(Consumer.class).findAll();
            return realm.copyFromRealm(list);
        } catch (Exception e) {
            Log.d(TAG, "getAll: Error Occurred");
        }
        return new ArrayList<>();
    }
}
