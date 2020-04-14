package com.example.testapplication.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.testapplication.R;
import com.example.testapplication.ui.adapter.ClientListAdapter;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.ui.presenter.ClientListPresenter;
import com.example.testapplication.ui.views.ClientListView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ClientList extends BaseActivity implements ClientListView {
    private List<Client> clients;
    private RecyclerView clientRv;
    private ClientListPresenter presenter;
    private ClientListAdapter adapter;
    private int requestCode = 0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0f);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, AccountManagement.class));
        return true;
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
        this.mAuth = FirebaseAuth.getInstance();
        this.presenter = new ClientListPresenter(this);
        this.clientRv = findViewById(R.id.clientRv);
        this.clientRv.setLayoutManager(new LinearLayoutManager(this));
        this.clientRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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

    @Override
    public void onBackPressed() {
        confirmLogOut();
    }

    private void confirmLogOut() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("You are signing out. Are You Sure?")
                .setPositiveButton("SIGN OUT", (dialog1, which) -> {
                    mAuth.signOut();
                    finish();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }
}
