package com.example.testapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.testapplication.R;
import com.example.testapplication.adapter.ClientListAdapter;
import com.example.testapplication.pojo.Client;

import java.util.List;

public class ClientList extends BaseActivity {
    private List<Client> clients;
    private RecyclerView clientRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeComponents();
        initializeAdapter();
    }

    private void initializeAdapter() {
        clientRv.setAdapter(new ClientListAdapter(clients, v -> {
            Client client = (Client)v.getTag();
            Intent intent = new Intent(this, OrderPage.class);
            intent.putExtra("data", client);
            startActivity(intent);
        }));
    }

    private void initializeComponents() {
        this.clientRv = findViewById(R.id.clientRv);
        this.clientRv.setLayoutManager(new LinearLayoutManager(this));
    }
}
