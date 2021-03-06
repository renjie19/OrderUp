package com.example.testapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.R;
import com.example.testapplication.shared.pojo.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

public class OrderPageAdapter extends RecyclerView.Adapter<OrderPageAdapter.ItemListViewHolder> implements Adapter{
    private List<Item> mItems;
    private View.OnClickListener mOnClickListener;

    public OrderPageAdapter(List<Item> mItems, View.OnClickListener mOnClickListener) {
        if(mItems == null) {
            mItems = new RealmList<>();
        }
        this.mItems = mItems;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        Item item = mItems.get(position);
        holder.order.setText(item.getName());
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.price.setText(String.format("%,.2f", item.getPrice()));
        holder.classification.setText(item.getPackaging() == null || item.getPackaging().isEmpty() ? "pc(s)" : item.getPackaging());
        holder.itemCard.setOnClickListener(mOnClickListener);
        holder.itemCard.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemListViewHolder extends RecyclerView.ViewHolder {
        private TextView order, quantity, price, classification;
        private CardView itemCard;
        ItemListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.order = itemView.findViewById(R.id.orderName);
            this.quantity = itemView.findViewById(R.id.quantity);
            this.price = itemView.findViewById(R.id.price);
            this.classification = itemView.findViewById(R.id.pkg);
            this.itemCard = itemView.findViewById(R.id.itemCard);
        }
    }
}
