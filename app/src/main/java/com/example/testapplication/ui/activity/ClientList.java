package com.example.testapplication.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testapplication.R;
import com.example.testapplication.shared.callback.DeleteCallback;
import com.example.testapplication.shared.callback.SwipeDeleteCallback;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.ui.adapter.ClientListAdapter;
import com.example.testapplication.ui.presenter.ClientListPresenter;
import com.example.testapplication.ui.views.ClientListView;

import java.util.List;

public class ClientList extends BaseActivity implements ClientListView, DeleteCallback {
    private List<Client> clients;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView clientRv;
    private ClientListPresenter presenter;
    private ClientListAdapter adapter;
    private int requestCode = 0;
    private final int CAMERA_PERMISSION_CODE = 201;

    private int itemIndex;
    private Client removedClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0f);
        setContentView(R.layout.client_list);
        initializeComponents();
        initializeAdapter();
        initializeListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadList();
    }

    private void reloadList() {
        this.clients = presenter.getListOfClients();
        adapter.setList(clients);
        adapter.notifyDataSetChanged();
    }

    private void initializeListeners() {
        presenter.initListeners(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        CharSequence title = item.getTitle();
        if(title != null && title.equals("Account")) {
           startActivity(new Intent(this, AccountManagement.class));
       }
        return true;
    }

    private void initializeAdapter() {
        this.clients = presenter.getListOfClients();
        this.adapter = new ClientListAdapter(this.clients, v -> {
            Client client = (Client) v.getTag();
            Intent intent = new Intent(this, OrderTrail.class);
            intent.putExtra("data", client);
            startActivity(intent);
        });
        clientRv.setAdapter(adapter);
        new ItemTouchHelper(new SwipeDeleteCallback(getWindow().getDecorView().getRootView(), this)).attachToRecyclerView(clientRv);
    }

    private void initializeComponents() {
        initActionBar();
        this.presenter = new ClientListPresenter(null);
        this.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        this.clientRv = findViewById(R.id.clientRv);
        this.clientRv.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.addClientBtn).setOnClickListener(v -> startQrScanner());
        this.swipeRefreshLayout.setOnRefreshListener(() -> {
            reloadList();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void initActionBar() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_logo_36dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ORDERUP");
    }

    private void startQrScanner() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent(this, BarCodeScanner.class), requestCode);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Client client = data.getParcelableExtra("data");
            if (client != null) {
                presenter.addClient(client);
                this.clients = presenter.getListOfClients();
                adapter.setList(this.clients);
                adapter.notifyDataSetChanged();
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
                    presenter.signOut();
                    finish();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    public void onDelete(int position) {
        this.itemIndex = position;
        this.removedClient = clients.get(itemIndex);
        this.clients.remove(removedClient);
        adapter.notifyDataSetChanged();
        presenter.deleteClient(removedClient);
    }

    @Override
    public void onUndo() {
        this.clients.add(itemIndex, removedClient);
        adapter.notifyDataSetChanged();
        presenter.restoreClient(itemIndex, removedClient);
    }

    @Override
    public void onSnackbarDismissed(int event) {
        if(event != 1) {
            presenter.removeClientFromStore(removedClient);
        }
    }

    @Override
    public void onSuccess(Object object) {
        reloadList();
    }

    @Override
    public void onFailure(Object object) {

    }
}
