package com.example.testapplication.shared.util;

import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public enum AccountMapper {
    INSTANCE;

    public Client accountToClient(Account account) {
        Client client = new Client();
        client.setName(String.format("%s %s",account.getFirstName(),account.getLastName()));
        client.setLocation(account.getLocation());
        client.setContactNo(account.getContactNumber());
        client.setToken(account.getToken());
        client.setUid(account.getId());
        return client;
    }

    public Account documentToAccount(DocumentSnapshot object, String id) {
        Account account = new Account();
        account.setId(id);
        account.setFirstName((String) object.get("firstName"));
        account.setLastName((String) object.get("lastName"));
        account.setLocation((String)object.get("location"));
        account.setContactNumber((String)object.get("contact"));
        account.setEmail((String)object.get("email"));
        RealmList<Client> clientList = getClientsFromDocumentReference(object.get("clients"));
        account.setClients(clientList);
        return account;
    }

    private RealmList<Client> getClientsFromDocumentReference(Object object) {
        RealmList<Client> clientList = new RealmList<>();
        if(object instanceof ArrayList) {
            for(Object item : ((ArrayList)object)) {
                if(item instanceof DocumentReference)
                clientList.add(FirebaseUtil.INSTANCE.getClientFromDocumentReference((DocumentReference) item));
            }
        }
        return clientList;
    }
}
