package com.example.testapplication.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.R;
import com.example.testapplication.ui.adapter.ItemListAdapter;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Item;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.ui.presenter.OrderPagePresenter;
import com.example.testapplication.shared.util.DateUtil;
import com.example.testapplication.ui.views.OrderPageView;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import io.realm.RealmList;

public class OrderPage extends BaseActivity implements OrderPageView {

    private TextView date, total;
    private EditText consumerField, location;
    private Order order;
    private RecyclerView rv;
    private ItemListAdapter adapter;

    private EditText itemName, quantity, price, pckg;
    private OrderPagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_order_list);

        presenter = new OrderPagePresenter(this);

        order = getIntent().getParcelableExtra("data");
        if (order == null) {
            order = new Order();
            order.setItems(new RealmList<>());
            order.setClient(new Client());
        }
        initializeComponents();
        setAdapter();
    }

    private void setAdapter() {
        adapter = new ItemListAdapter(order.getItems(), getOnClickListener());
        rv.setAdapter(adapter);
    }

    private View.OnClickListener getOnClickListener() {
        return view -> {
            Item item = (Item) view.getTag();
            showItemEditAlertDialog(item, updatePrice(item));
        };
    }

    private void showItemEditAlertDialog(Item item, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View editView = getLayoutInflater().inflate(R.layout.item_edit, null);

        itemName = editView.findViewById(R.id.itemName);
        quantity = editView.findViewById(R.id.quantity);
        price = editView.findViewById(R.id.value);
        pckg = editView.findViewById(R.id.pkg);


        itemName.setText(item.getName());
        quantity.setText(String.valueOf(item.getQuantity()));
        pckg.setText(item.getPackaging());
        price.setText(String.valueOf(item.getPrice()));
        alert.setView(editView);

        alert.setPositiveButton("SUBMIT", listener);
        alert.setNegativeButton("CANCEL", null);
        alert.create().show();
    }

    private void initializeComponents() {
        this.consumerField = findViewById(R.id.consumerName);
        this.consumerField.setText(order.getClient().getName());
        this.consumerField.setEnabled(false);

        this.location = findViewById(R.id.consumerLocation);
        this.location.setText(order.getClient().getLocation());
        this.location.setEnabled(false);

        this.date = findViewById(R.id.orderDate);
        this.date.setText(order.getDate() != 0
                ? DateUtil.getStringDate(order.getDate())
                : DateUtil.getStringDate(System.currentTimeMillis()));
        this.date.setEnabled(false);

        this.total = findViewById(R.id.totalField);
        this.total.setText(String.valueOf(order.getTotal()));

        this.rv = findViewById(R.id.itemListRv);
        this.rv.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.add).setOnClickListener(view -> {
            Item item = new Item();
            showItemEditAlertDialog(item, createItemAndAddToOrder(item));
        });

        findViewById(R.id.done).setOnClickListener(view -> {
            buildConsumerOrder(order);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Are you sure with your items?\n");
            alert.setPositiveButton("YES", (dialog, which) -> {
                showProgressBar("Sending Request... Please Wait...");
                saveOrUpdateOrderThenSend();
            });
            alert.setNegativeButton("NO", null);
            alert.show();
        });
    }

    private void saveOrUpdateOrderThenSend() {
        if(order.getTotal() <= 0) {
            this.order = presenter.saveOrder(order);
        } else {
            this.order = presenter.updateOrder(order);
        }
        sendNotification();
    }

    private void buildConsumerOrder(Order order) {
        order.getClient().setName(consumerField.getText().toString());
        order.setDate(System.currentTimeMillis());
        order.getClient().setLocation(location.getText().toString());
        order.setStatus(order.getTotal() == 0 ? "PENDING" : "FOR DELIVERY");
    }

    private DialogInterface.OnClickListener createItemAndAddToOrder(Item item) {
        return (dialog, which) -> {
            item.setName(itemName.getText().toString());
            item.setQuantity(Integer.parseInt(quantity.getText().toString()));
            item.setPackaging(pckg.getText().toString());
            order.getItems().add(item);
            adapter.notifyDataSetChanged();
        };
    }

    private DialogInterface.OnClickListener updatePrice(Item item) {
        return (dialog, which) -> {
            item.setPrice(Double.parseDouble(price.getText().toString()));
            total.setText(String.valueOf(order.getTotal()));
            adapter.notifyDataSetChanged();
        };
    }

    private void sendLongTextMessage() {
        PendingIntent sent = PendingIntent.getBroadcast(this, 0, new Intent("SENT"), 0);
        ArrayList<PendingIntent> sentPendingIntent = new ArrayList<>();
        sentPendingIntent.add(sent);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode() == Activity.RESULT_OK) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }, new IntentFilter("SENT"));
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> completeMessage = smsManager.divideMessage(new GsonBuilder().create().toJson(order));
        //TODO externalize setting of receipient
        smsManager.sendMultipartTextMessage("+639486324067", null,
                completeMessage, sentPendingIntent, null);
    }

    private void sendNotification() {
        new Thread(() -> {
            presenter.sendNotification(order);
        }).run();
    }

    @Override
    public void onSuccess(String message) {
        runOnUiThread(() -> {
            hideProgressBar();
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public void onFailure(String message) {
        runOnUiThread(() -> {
            hideProgressBar();
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }
}
