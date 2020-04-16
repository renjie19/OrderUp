package com.example.testapplication.core.service;

import com.example.testapplication.core.repository.AccountRepository;
import com.example.testapplication.core.repository.RepositoryEnum;
import com.example.testapplication.core.repository.RepositoryFactory;
import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.util.AccountMapper;
import com.example.testapplication.shared.util.ClientMapper;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

class AccountServiceImpl implements AccountService {
    private AccountRepository repository;
    private final String TAG = getClass().getSimpleName();

    public AccountServiceImpl() {
        this.repository = (AccountRepository) RepositoryFactory.INSTANCE.create(RepositoryEnum.ACCOUNT);
    }

    @Override
    public void save(Account account) {
        repository.save(account);
    }

    @Override
    public Account getAccount() {
        Account account = repository.getAccount();
        if (account == null) {
            account = new Account();
            account.setClients(new RealmList<>());
        }
        return account;
    }

    @Override
    public Account update(Account account) {
        return null;
    }

    @Override
    public Account addClient(Client client) {
        Map<String, Client> clientMap = new HashMap<>();
        Account account = repository.getAccount();
        for (Client savedClient : account.getClients()) {
            clientMap.put(savedClient.getUid(), savedClient);
        }
        clientMap.put(client.getUid(), client);
        account.setClients(new RealmList<>());
        for (Map.Entry<String, Client> uniqueClients : clientMap.entrySet()) {
            account.getClients().add(uniqueClients.getValue());
        }
        return repository.save(account);
    }

    @Override
    public void deleteClient(Client client) {
        Account account = repository.getAccount();
        RealmList<Client> clientRealmList = account.getClients();
        Client toBeRemoved = null;
        for(Client c : clientRealmList) {
            if(c.getUid().equals(client.getUid())) {
                toBeRemoved = c;
            }
        }
        clientRealmList.remove(toBeRemoved);
        account.setClients(clientRealmList);
        repository.save(account);
    }

    @Override
    public void clearData() {
        repository.clearData();
    }

    @Override
    public void saveAccountFromSnapshot(DocumentSnapshot snapshot, String id, CallBack callBack) {
        clearData();
        List<DocumentReference> list = (List<DocumentReference>)snapshot.get("clients");
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for( DocumentReference documentReference : list) {
            Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
            tasks.add(documentSnapshotTask);
        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            RealmList<Client> clientList = new RealmList<>();
            for(Object object : objects) {
                clientList.add(ClientMapper.INSTANCE.mapToClient(((DocumentSnapshot)object).getData(), ((DocumentSnapshot)object).getId()));
            }
            saveData(snapshot, id, clientList, callBack);
        });
    }

    @Override
    public void restoreClient(int itemIndex, Client removedClient) {
        Account account = repository.getAccount();
        RealmList<Client> clients = account.getClients();
        clients.add(itemIndex, removedClient);
        account.setClients(clients);
        repository.save(account);
    }

    private void saveData(DocumentSnapshot snapshot, String id, RealmList<Client> clientList, CallBack callBack) {
        Map<String, Object> data = snapshot.getData();
        if (data == null) {
            data = new HashMap<>();
        }
        Account account = AccountMapper.INSTANCE.documentToAccount(data, id);
        account.setClients(clientList);
        repository.save(account);
        callBack.run();
    }

}
