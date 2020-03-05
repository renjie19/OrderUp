package com.example.testapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.R;
import com.example.testapplication.pojo.Client;

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
        return new ClientListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.client_entry_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientListViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.clientName.setText(client.getName());
        holder.entryContainer.setOnClickListener(listener);
        holder.entryContainer.setTag(client);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ClientListViewHolder extends RecyclerView.ViewHolder {
        private TextView clientName;
        private CardView entryContainer;
        public ClientListViewHolder(@NonNull View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.client_name);
            entryContainer = itemView.findViewById(R.id.entry_container);
        }
    }
}
