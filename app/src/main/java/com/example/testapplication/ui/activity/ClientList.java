package com.example.testapplication.ui.activity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.testapplication.R;
import com.example.testapplication.ui.adapter.ClientListAdapter;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.ui.presenter.ClientListPresenter;
import com.example.testapplication.ui.views.ClientListView;

import java.util.List;

public class ClientList extends BaseActivity implements ClientListView {
    private List<Client> clients;
    private RecyclerView clientRv;
    private ClientListPresenter presenter;
    private ClientListAdapter adapter;
    private int requestCode = 0;

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
        adapter.notifyDataSetChanged();
    }

    private void initializeAdapter() {
        this.adapter = new ClientListAdapter(clients, v -> {
            Client client = (Client)v.getTag();
            Intent intent = new Intent(this, OrderTrail.class);
            intent.putExtra("data", client);
            startActivity(intent);
        });
        clientRv.setAdapter(adapter);
    }

    private void initializeComponents() {
        this.presenter = new ClientListPresenter(this);
        this.clientRv = findViewById(R.id.clientRv);
        this.clientRv.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.addClientBtn).setOnClickListener(v -> startActivityForResult(new Intent(this, BarCodeScanner.class), requestCode));
    }

    @Override
    public void setListOfClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            Client client = data.getParcelableExtra("data");
            if(client != null) {
                presenter.addClient(client);
            }
        }
    }
}
