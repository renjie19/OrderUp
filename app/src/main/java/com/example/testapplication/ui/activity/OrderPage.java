package com.example.testapplication.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.R;
import com.example.testapplication.shared.Preferences;
import com.example.testapplication.shared.enums.StatusEnum;
import com.example.testapplication.ui.adapter.OrderPageAdapter;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Item;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.ui.presenter.OrderPagePresenter;
import com.example.testapplication.shared.util.DateUtil;
import com.example.testapplication.ui.views.OrderPageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import io.realm.RealmList;

public class OrderPage extends BaseActivity implements OrderPageView {

    private TextView date, total;
    private EditText consumerField, location;
    private Order order;
    private RecyclerView rv;
    private OrderPageAdapter adapter;
    private Button doneBtn, addItemBtn;

    private EditText itemName, quantity, price, pckg;
    private OrderPagePresenter presenter;
    private String action;

    private Drawable icon;
    private ColorDrawable background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        this.presenter = new OrderPagePresenter(this);

        this.order = getIntent().getParcelableExtra("data");
        this.action = getIntent().getStringExtra("ACTION");
        if (this.order == null) {
            this.order = new Order();
            this.order.setItems(new RealmList<>());
            this.order.setClient(new Client());
        }
        initializeComponents();
        setAdapter();
    }

    private void setAdapter() {
        adapter = new OrderPageAdapter(order.getItems(), getOnClickListener());
        rv.setAdapter(adapter);
        //TODO move management of list as current list is saved not on the adapter
        new ItemTouchHelper(getItemTouchHelper()).attachToRecyclerView(rv);
    }



    private View.OnClickListener getOnClickListener() {
        return view -> {
            Item item = (Item) view.getTag();
            showItemEditAlertDialog(item, updatePrice(item));
        };
    }

    private void showItemEditAlertDialog(Item item, DialogInterface.OnClickListener listener) {
        if (isNotPaid()) {
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
            price.setEnabled(order.getId() != null);
            alert.setView(editView);

            alert.setPositiveButton("SUBMIT", listener);
            alert.setNeutralButton("CANCEL", null);
            alert.setNegativeButton("SUBMIT AND CONTINUE", (dialog, which) -> {
                listener.onClick(dialog, which);
                switch (action) {
                    case "VIEW":
                        int itemIndex = order.getItems().indexOf(item);
                        if (++itemIndex <= order.getItems().size() - 1) {
                            Item nextItem = order.getItems().get(itemIndex);
                            showItemEditAlertDialog(nextItem, updatePrice(nextItem));
                        }
                        break;
                    case "CREATE":
                        Item newItem = new Item();
                        showItemEditAlertDialog(newItem, createItemAndAddToOrder(newItem));
                        break;
                }
            });
            alert.create().show();
        }
    }

    private boolean isNotPaid() {
        if(order.getStatus() != null){
            return !order.getStatus().equals(StatusEnum.PAID.toString());
        }
        return true;
    }

    private void initializeComponents() {
        this.icon = ContextCompat.getDrawable(this, R.drawable.ic_delete_black_24dp);
        this.background = new ColorDrawable(0xF2F13131);
        this.consumerField = findViewById(R.id.consumerName);
        this.consumerField.setEnabled(false);

        this.location = findViewById(R.id.consumerLocation);
        this.location.setEnabled(false);

        this.date = findViewById(R.id.orderDate);
        this.date.setEnabled(false);

        this.total = findViewById(R.id.totalField);
        this.total.setText(String.valueOf(order.getTotal()));

        this.rv = findViewById(R.id.itemListRv);
        this.rv.setLayoutManager(new LinearLayoutManager(this));

        this.doneBtn = findViewById(R.id.done);
        this.addItemBtn = findViewById(R.id.add);

        setFieldData();
        setClickListeners();
    }

    private void setClickListeners() {
        final boolean isPaid = order.getStatus() != null && order.getStatus().equals(StatusEnum.PAID.toString());
        final boolean isForPayment = order != null && order.isForPayment();

        if (isPaid) {
            this.addItemBtn.setEnabled(false);
        } else if (isForPayment) {
            this.addItemBtn.setEnabled(false);
            this.doneBtn.setText(StatusEnum.PAID.name());
            this.doneBtn.setOnClickListener(payOnItemClick());
        } else {
            addItemBtn.setOnClickListener(view -> {
                Item item = new Item();
                showItemEditAlertDialog(item, createItemAndAddToOrder(item));
            });

            doneBtn.setOnClickListener(view -> {
                buildClientOrder(order);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Are you sure with your items?");
                alert.setPositiveButton("YES", (dialog, which) -> {
                    showProgressBar("Sending Request... Please Wait...");
                    saveOrUpdateOrderThenSend();
                });
                alert.setNegativeButton("NO", null);
                alert.show();
            });
        }
    }

    private void setFieldData() {
        this.consumerField.setText(order.getClient().getName());
        this.location.setText(order.getClient().getLocation());
        this.date.setText(order.getDate() != 0
                ? DateUtil.getStringDate(order.getDate())
                : DateUtil.getStringDate(System.currentTimeMillis()));
    }

    private void saveOrUpdateOrderThenSend() {
        if (order.getTotal() <= 0) {
            this.order = presenter.saveOrder(order);
        } else {
            this.order.setForPayment(true);
            this.order = presenter.updateOrder(order);
        }

        if (Preferences.getMode()) {
            sendNotification();
        } else {
            sendLongTextMessage();
        }
    }

    private void buildClientOrder(Order order) {
        order.setDate(System.currentTimeMillis());
        order.setStatus(order.getTotal() == 0 ? StatusEnum.PENDING.name() : StatusEnum.FOR_DELIVERY.name());
    }

    private View.OnClickListener payOnItemClick() {
        return v -> {
            this.order.setStatus(StatusEnum.PAID.name());
            this.presenter.updateOrder(order);
        };
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
            item.setName(itemName.getText().toString());
            item.setQuantity(Integer.parseInt(quantity.getText().toString()));
            item.setPackaging(pckg.getText().toString());
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
                    hideProgressBar();
                }
            }
        }, new IntentFilter("SENT"));
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> completeMessage = smsManager.divideMessage(new GsonBuilder().create().toJson(order));
        smsManager.sendMultipartTextMessage(order.getClient().getContactNo(), null,
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

    private ItemTouchHelper.SimpleCallback getItemTouchHelper() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            int index;
            Item item;
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                index = viewHolder.getAdapterPosition();
                item = order.getItems().get(index);
                order.getItems().remove(item);
                adapter.notifyDataSetChanged();
                showSnackUndoBar();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + icon.getIntrinsicHeight();

                if (dX > 0) { // Swiping to the right
                    int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                    int iconRight = itemView.getLeft() + iconMargin;
                    icon.setBounds(iconRight, iconTop, iconLeft, iconBottom);

                    background.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                            itemView.getBottom());
    //            } else if (dX < 0) { // Swiping to the left
    //                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
    //                int iconRight = itemView.getRight() - iconMargin;
    //                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
    //
    //                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
    //                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
                icon.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            private void showSnackUndoBar() {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Removed Successfully", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> undoDelete())
                        .show();
            }

            private void undoDelete() {
                order.getItems().add(index, item);
                adapter.notifyDataSetChanged();
            }
        };
    }
}
