package com.example.testapplication.ui.activity;

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


import com.example.testapplication.ui.adapter.MainPageOrderListAdapter;
import com.example.testapplication.R;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.ui.presenter.OrderPagePresenter;

import java.util.List;


public class OrderPage extends BaseActivity {
    private MainPageOrderListAdapter adapter;
    private RecyclerView rv;
    private Client client;
    private TextView clientName;
    private TextView clientAddress;
    private List<Order> list;
    private OrderPagePresenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page);
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
        list = presenter.getOrders(getIntent().getParcelableExtra("data"));
        this.rv = findViewById(R.id.orderRv);
        this.clientName = findViewById(R.id.clientName);
        this.clientAddress = findViewById(R.id.clientAddress);
        this.rv.setLayoutManager(new LinearLayoutManager(this));
        if(list != null && list.size() > 0){
            if(client == null) {
                client = list.get(0).getClient();
                this.clientName.setText(client.getName());
                this.clientAddress.setText(client.getLocation());
            }
        }

    }

    private void initializeAdapter() {
        this.adapter = new MainPageOrderListAdapter(list, getOnClickListener());
        this.rv.setAdapter(this.adapter);
    }

    private OnClickListener getOnClickListener() {
        return view -> {
            Order order = (Order) view.getTag();
            Intent intent = new Intent(this, ConsumerOrderList.class);
            intent.putExtra("ACTION","VIEW");
            intent.putExtra("data", order);
            startActivity(intent);
        };
    }
}
