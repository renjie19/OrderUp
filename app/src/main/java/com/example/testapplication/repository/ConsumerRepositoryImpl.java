package com.example.testapplication.repository;

import android.util.Log;

import com.example.testapplication.pojo.Consumer;

import java.util.List;

import io.realm.Realm;

class ConsumerRepositoryImpl extends ConsumerRepository {
    private final String TAG = this.getClass().getSimpleName();
    private Realm realm = Realm.getDefaultInstance();

    @Override
    public Consumer save(Consumer consumer) {
        realm.executeTransactionAsync(
                realm -> {
                    Number id = realm.where(Consumer.class).max("id");
                    if(id == null){
                        id = -1;
                    }
                    consumer.setId(id.longValue()+1);
                    realm.insert(consumer);},
                () -> Log.d(TAG, "SAVING SUCCESS..."),
                error -> Log.d(TAG, "ERROR SAVING...." + error.getMessage()));
        return consumer;
    }

    @Override
    public List<Consumer> getAll() {
        return realm.where(Consumer.class).findAll();
    }
}
