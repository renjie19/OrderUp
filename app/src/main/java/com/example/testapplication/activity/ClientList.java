package com.example.testapplication.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.testapplication.R;
import com.example.testapplication.adapter.ClientListAdapter;
import com.example.testapplication.pojo.Client;
import com.example.testapplication.presenter.ClientListPresenter;
import com.example.testapplication.views.ClientListView;

import java.util.List;

public class ClientList extends BaseActivity implements ClientListView {
    private List<Client> clients;
    private RecyclerView clientRv;
    private ClientListPresenter presenter;
    private ClientListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_list);
        initializeComponents();
        presenter.getListOfClients();
        initializeAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initializeAdapter() {
        this.adapter = new ClientListAdapter(clients, v -> {
            Client client = (Client)v.getTag();
            Intent intent = new Intent(this, OrderPage.class);
            intent.putExtra("data", client);
            startActivity(intent);
        });
        clientRv.setAdapter(adapter);
    }

    private void initializeComponents() {
        this.presenter = new ClientListPresenter(this);
        this.clientRv = findViewById(R.id.clientRv);
        this.clientRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void setListOfClients(List<Client> clients) {
        this.clients = clients;
    }
}
