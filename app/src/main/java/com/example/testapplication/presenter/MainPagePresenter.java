package com.example.testapplication.presenter;

import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.repository.ConsumerRepository;

import java.util.List;

public class MainPagePresenter {
    private final ConsumerRepository consumerRepository = ConsumerRepository.getInstance();


    public List<Consumer> getAll(){
        return consumerRepository.getAll();
    }
}
