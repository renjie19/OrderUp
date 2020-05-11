package com.example.testapplication.core.service;

import com.example.testapplication.core.repository.AccountRepository;
import com.example.testapplication.core.repository.RepositoryEnum;
import com.example.testapplication.core.repository.RepositoryFactory;
import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Item;
import com.example.testapplication.shared.pojo.Order;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

public class FirebaseServiceImpl implements FirebaseService {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private static final String USER_COLLECTION = "Users";
    private static final String ORDER_COLLECTION = "Order";

    private AccountRepository accountRepository;

    public FirebaseServiceImpl() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mStore = FirebaseFirestore.getInstance();
        this.accountRepository = (AccountRepository) RepositoryFactory.INSTANCE.create(RepositoryEnum.ACCOUNT);
    }

    @Override
    public void createUser(Account account, CallBack callBack) {
        DocumentReference reference = mStore.collection(USER_COLLECTION).document(mAuth.getUid());
        account.setId(reference.getId());
        reference.set(buildAccountMap(account))
                .addOnSuccessListener(aVoid -> callBack.onSuccess(null))
                .addOnFailureListener(e -> callBack.onFailure(null));
        reference.addSnapshotListener((documentSnapshot, e) -> {
            getAccountAndSave(documentSnapshot.getData(), null);
        });
    }

    @Override
    public void updateUser(Account account) {
        getDocumentId(USER_COLLECTION, "id", account.getId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                mStore.collection(USER_COLLECTION)
                        .document((String) object)
                        .update(buildAccountMap(account));
            }

            @Override
            public void onFailure(Object object) {

            }
        });
    }

    @Override
    public void getAccount(String id, CallBack callBack) {
        mStore.collection(USER_COLLECTION)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.getData() != null) {
                        getAccountAndSave(documentSnapshot.getData(), callBack);
                    }
                })
                .addOnFailureListener(e -> callBack.onFailure(e.getMessage()));
    }

    @Override
    public void login(String email, String password, CallBack callBack) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> callBack.onSuccess(authResult.getUser().getUid()))
                .addOnFailureListener(e -> callBack.onFailure(null));
    }

    @Override
    public void signUp(String email, String password, CallBack callBack) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    callBack.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callBack.onFailure(e.getMessage());
                });
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }

    @Override
    public void createOrder(Order order) {
        DocumentReference reference = mStore.collection(ORDER_COLLECTION).document();
        reference.addSnapshotListener((documentSnapshot, e) -> {
            // update local
        });
        reference.set(buildOrderMap(order))
                .addOnSuccessListener(aVoid -> {

                })
                .addOnFailureListener(e -> {

                });
    }

    @Override
    public void updateOrder(Order order) {
        getDocumentId(ORDER_COLLECTION, "id", order.getId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                mStore.collection(ORDER_COLLECTION)
                        .document((String) object)
                        .update(buildOrderMap(order))
                        .addOnSuccessListener(aVoid -> {

                        })
                        .addOnFailureListener(e -> {

                        });
            }

            @Override
            public void onFailure(Object object) {

            }
        });
    }

    @Override
    public void removeClient(Client client) {
        mStore.collection(USER_COLLECTION)
                .document(mAuth.getUid())
                .update("clients", FieldValue.arrayRemove(client.getUid()));
    }

    @Override
    public void addClient(Client client) {
        mStore.collection(USER_COLLECTION)
                .document(mAuth.getUid())
                .update("clients", FieldValue.arrayUnion(client.getUid()));
    }

    private void getDocumentId(String collection, String field, String param, CallBack callBack) {
        mStore.collection(collection)
                .whereEqualTo(field, param)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
                        callBack.onSuccess(queryDocumentSnapshots.getDocuments().get(0).getId());
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    private Map<String, Object> buildAccountMap(Account account) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", account.getId());
        map.put("token", account.getToken());
        map.put("firstName", account.getFirstName());
        map.put("lastName", account.getLastName());
        map.put("location", account.getLocation());
        map.put("contactNo", account.getContactNumber());
        map.put("email", account.getEmail());
        map.put("clients", new RealmList<>());
        return map;
    }

    private Map<String, Object> buildOrderMap(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("client", order.getClient().getUid());
        map.put("from", mAuth.getUid());
        map.put("date", order.getDate());
        map.put("status", order.getStatus());
        map.put("forPayment", order.isForPayment());
        map.put("total", order.getTotal());
        map.put("items", getItemMaps(order.getItems()));
        return map;
    }

    private void getAccountAndSave(Map<String, Object> data, CallBack callBack) {
        Account account = new Account();
        account.setFirstName((String) data.get("firstName"));
        account.setLastName((String) data.get("lastName"));
        account.setLocation((String) data.get("location"));
        account.setContactNumber((String) data.get("contactNumber"));
        account.setEmail((String) data.get("email"));
        account.setToken((String) data.get("token"));
        getClientsFromMap((List) data.get("clients"), account, callBack);
    }

    private void getClientsFromMap(List clients, Account account, CallBack callBack) {
        account.setClients(new RealmList<>());
        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (Object id : clients) {
            tasks.add(mStore.collection(USER_COLLECTION).whereEqualTo("id", id).get());
        }
        Task<List<QuerySnapshot>> taskResult = Tasks.whenAllSuccess(tasks);
        taskResult.addOnSuccessListener(querySnapshots -> {
            for (QuerySnapshot snapshots : querySnapshots) {
                Map<String, Object> clientMap = snapshots.getDocuments().get(0).getData();
                if (clientMap != null) {
                    account.getClients().add(getClient(clientMap));
                }
            }
            accountRepository.save(account);
            if(callBack != null) {
                callBack.onSuccess(null);
            }
        });
    }

    private Client getClient(Map<String, Object> map) {
        Client client = new Client();
        client.setUid((String) map.get("id"));
        client.setName(String.format("%s %s", map.get("firstName"), map.get("lastName")));
        client.setLocation((String) map.get("location"));
        client.setContactNo((String) map.get("contactNo"));
        client.setToken((String) map.get("token"));
        return client;
    }

    private List<Map<String, Object>> getItemMaps(List<Item> items) {
        List<Map<String, Object>> itemsMap = new ArrayList<>();
        for (Item item : items) {
            itemsMap.add(getItemMap(item));
        }
        return itemsMap;
    }

    private Map<String, Object> getItemMap(Item item) {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("name", item.getName());
        itemMap.put("price", item.getPrice());
        itemMap.put("quantity", item.getQuantity());
        itemMap.put("packaging", item.getPackaging());
        return itemMap;
    }
}
