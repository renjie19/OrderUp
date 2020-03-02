package com.example.testapplication.repository;

import com.example.testapplication.OrderUp;
import com.example.testapplication.pojo.Consumer;

import java.util.List;

import io.realm.Realm;

class ConsumerRepositoryImpl extends ConsumerRepository {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public Consumer save(Consumer consumer) {
//        Realm.getDefaultInstance().executeTransactionAsync(
//                realm -> {
//                    Number id = realm.where(Consumer.class).max("id");
//                    if(id == null){
//                        id = -1;
//                    }
//                    consumer.setId(id.longValue()+1);
//                    realm.insert(consumer);},
//                () -> Log.d(TAG, "SAVING SUCCESS..."),
//                error -> Log.d(TAG, "ERROR SAVING...." + error.getMessage()));
//        return consumer;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insert(consumer);
        realm.where(Consumer.class).findAll();
        realm.close();
        return null;
    }

    @Override
    public List<Consumer> getAll() {
        Realm.getDefaultInstance().refresh();

        return Realm.getDefaultInstance().where(Consumer.class).findAll();
    }
}
