package com.example.testapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.testapplication.adapter.MainPageOrderListAdapter;
import com.example.testapplication.R;
import com.example.testapplication.pojo.Client;
import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.presenter.OrderPagePresenter;

import java.util.List;


public class OrderPage extends BaseActivity {
    private MainPageOrderListAdapter adapter;
    private RecyclerView rv;
    private Client client;
    private TextView clientName;
    private TextView clientAddress;
    private List<Consumer> list;
    private OrderPagePresenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page);
        client = getIntent().getParcelableExtra("data");
        presenter = new OrderPagePresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeComponents();
        initializeAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add: {
                Intent intent = new Intent(this, ConsumerOrderList.class);
                intent.putExtra("ACTION","CREATE");
                startActivity(intent);
            }
            case R.id.remove: {

            }
            case R.id.updated: {

            }
        }
        return true;
    }

    private void initializeComponents() {
        list = presenter.getOrders(client);
        this.rv = findViewById(R.id.orderRv);
        this.rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeAdapter() {
        this.adapter = new MainPageOrderListAdapter(list, getOnClickListener());
        this.rv.setAdapter(this.adapter);
    }

    private OnClickListener getOnClickListener() {
        return view -> {
            Consumer consumer = (Consumer) view.getTag();
            Intent intent = new Intent(this, ConsumerOrderList.class);
            intent.putExtra("ACTION","VIEW");
            intent.putExtra("consumer", consumer);
            startActivity(intent);
        };
    }
}
