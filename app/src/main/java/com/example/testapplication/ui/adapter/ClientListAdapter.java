package com.example.testapplication.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.R;
import com.example.testapplication.shared.pojo.Client;

import java.util.List;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ClientListViewHolder> {
    private List<Client> clients;
    private View.OnClickListener listener;

    public ClientListAdapter(List<Client> clients, View.OnClickListener listener) {
        this.clients = clients;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ClientListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_entry_layout, parent, false);
        return new ClientListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientListViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.clientName.setText(client.getName());
        holder.clientName.setOnClickListener(listener);
        holder.clientName.setTag(client);
        holder.clientName.setBackgroundColor(position % 2 == 0 ? Color.WHITE : 0x42A5F5);
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public List<Client> getList() {
        return clients;
    }

    public void setList(List<Client> clients) {
        this.clients = clients;
    }

    public class ClientListViewHolder extends RecyclerView.ViewHolder {
        private TextView clientName;
        public ClientListViewHolder(@NonNull View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.client_name);
        }
    }
}
