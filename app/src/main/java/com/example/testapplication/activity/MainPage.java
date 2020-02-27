package com.example.testapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.testapplication.adapter.MainPageOrderListAdapter;
import com.example.testapplication.R;
import com.example.testapplication.pojo.Consumer;
import com.example.testapplication.presenter.MainPagePresenter;

import java.util.ArrayList;
import java.util.List;


public class MainPage extends BaseActivity {
    private MainPageOrderListAdapter adapter;
    private RecyclerView rv;
    private List<Consumer> list;
    private MainPagePresenter presenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        presenter = new MainPagePresenter();
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

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void initializeComponents() {
        list = presenter.getAll();
        if(list == null) {
            list = new ArrayList<>();
        }
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
