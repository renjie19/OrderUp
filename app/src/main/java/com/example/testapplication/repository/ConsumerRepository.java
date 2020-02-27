package com.example.testapplication.repository;

import com.example.testapplication.pojo.Consumer;

import java.util.List;

public abstract class ConsumerRepository {
    private static ConsumerRepository repository;

    protected ConsumerRepository(){}

    public static ConsumerRepository getInstance(){
        if(repository == null){
            repository = new ConsumerRepositoryImpl();
        }
        return repository;
    }

    public abstract Consumer save(Consumer consumer);
    public abstract List<Consumer> getAll();
}
