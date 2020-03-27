package com.example.testapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.testapplication.R;
import com.example.testapplication.shared.pojo.Order;
import com.example.testapplication.shared.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderTrailAdapter extends Adapter<OrderTrailAdapter.MainPageViewHolder> {
    private List<Order> list;
    private OnClickListener onClickListener;

    public OrderTrailAdapter(List<Order> list, OnClickListener onClickListener2) {
        this.list = list;
        if(this.list == null){
            this.list = new ArrayList<>();
        }
        this.onClickListener = onClickListener2;
    }

    public MainPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
        return new MainPageViewHolder(view);
    }

    public void onBindViewHolder(MainPageViewHolder holder, int position) {
        Order order = this.list.get(position);
        holder.dateTv.setText(DateUtil.getStringDate(order.getDate()));
        holder.statusTv.setText(order.getStatus());
        holder.parentView.setOnClickListener(this.onClickListener);
        holder.parentView.setTag(order);
    }

    public int getItemCount() {
        return this.list.size();
    }

    class MainPageViewHolder extends ViewHolder {
        private TextView dateTv;
        private LinearLayout parentView;
        private TextView statusTv;

        private MainPageViewHolder(View itemView) {
            super(itemView);
            this.dateTv = itemView.findViewById(R.id.date);
            this.statusTv = itemView.findViewById(R.id.status);
            this.parentView = itemView.findViewById(R.id.parent);
        }
    }
}
