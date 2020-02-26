package com.example.testapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.R;
import com.example.testapplication.pojo.Item;

import java.util.List;

import io.realm.RealmList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder> {
    private List<Item> items;
    private View.OnClickListener onClickListener;

    public ItemListAdapter(List<Item> items, View.OnClickListener onClickListener) {
        if(items == null) {
            items = new RealmList<>();
        }
        this.items = items;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        Item item = items.get(position);
        holder.order.setText(item.getName());
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.price.setText(String.valueOf(item.getPrice()));
        holder.classification.setText(item.getPackaging() == null ? "pc(s)" : item.getPackaging());
        holder.itemCard.setOnClickListener(onClickListener);
        holder.itemCard.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemListViewHolder extends RecyclerView.ViewHolder {
        private TextView order, quantity, price, classification;
        private CardView itemCard;
        public ItemListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.order = itemView.findViewById(R.id.orderName);
            this.quantity = itemView.findViewById(R.id.quantity);
            this.price = itemView.findViewById(R.id.price);
            this.classification = itemView.findViewById(R.id.pkg);
            this.itemCard = itemView.findViewById(R.id.itemCard);
        }
    }
}
