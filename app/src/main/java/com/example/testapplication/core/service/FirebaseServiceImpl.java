package com.example.testapplication.core.service;

import com.example.testapplication.OrderUp;
import com.example.testapplication.core.repository.AccountRepository;
import com.example.testapplication.core.repository.AccountRepositoryImpl;
import com.example.testapplication.core.repository.OrderRepository;
import com.example.testapplication.core.repository.OrderRepositoryImpl;
import com.example.testapplication.shared.callback.CallBack;
import com.example.testapplication.shared.callback.OnComplete;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.pojo.Item;
import com.example.testapplication.shared.pojo.Order;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.example.testapplication.shared.pojo.CustomTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;


public class FirebaseServiceImpl implements FirebaseService {
    private static final String CHANNEL_ID = "0001";
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private static final String USER_COLLECTION = "Users";
    private static final String ORDER_COLLECTION = "Orders";

    private AccountRepository accountRepository;
    private OrderRepository orderRepository;

    public FirebaseServiceImpl() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mStore = FirebaseFirestore.getInstance();
        this.accountRepository = new AccountRepositoryImpl();
        this.orderRepository = new OrderRepositoryImpl();
    }

    @Override
    public void createUser(Account account, OnComplete<CustomTask<Account>> task) {
        DocumentReference reference = mStore.collection(USER_COLLECTION).document(mAuth.getUid());
        account.setId(reference.getId());
        reference.set(buildAccountMap(account))
                .addOnCompleteListener(task1 -> task.onComplete(new CustomTask<>(account, task1.getException(), task1.isSuccessful())));

        reference.addSnapshotListener((documentSnapshot, e) -> getAccountAndSave(documentSnapshot.getData(), null));
    }

    @Override
    public void updateUser(Account account) {
        getDocumentId(USER_COLLECTION, "id", account.getId(), task -> {
            if (task.isSuccessful()) {
                mStore.collection(USER_COLLECTION)
                        .document(task.getResult())
                        .update(buildAccountMap(account));
            }
        });
    }

    @Override
    public void getAccount(String id, OnComplete<CustomTask<Account>> customTask) {
        mStore.collection(USER_COLLECTION)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    boolean successful = task.isSuccessful();
                    if (successful) {
                        DocumentSnapshot result = task.getResult();
                        if (result != null && result.exists() && result.getData() != null) {
                            getAccountAndSave(result.getData(), customTask);
                        }
                    } else {
                        customTask.onComplete(new CustomTask<>(null, task.getException(), false));
                    }
                });
    }

    @Override
    public void login(String email, String password, OnComplete<CustomTask<String>> customTask) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task1 -> {
                    String id = null;
                    final boolean successful = task1.isSuccessful();
                    if(successful) {
                        FirebaseUser user = task1.getResult().getUser();
                        id = user.getUid();
                    }
                    customTask.onComplete(new CustomTask<>(id, task1.getException(), successful));
                });
    }

    @Override
    public void signUp(String email, String password, OnComplete<CustomTask> customTask) {
        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(task -> customTask.onComplete(new CustomTask<>(null, task.getException(), task.isSuccessful())));
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }

    @Override
    public void createOrder(Order order, OnComplete<CustomTask<Order>> task) {
        DocumentReference reference = mStore.collection(ORDER_COLLECTION).document();
        order.setId(reference.getId());
        reference.set(buildOrderMap(order))
                .addOnCompleteListener(task1 -> {
                    boolean successful = task1.isSuccessful();
                    if(successful) {
                        addOrderToClient(reference.getId(), order.getClient().getUid());
                        addOrderToAccount(reference.getId());
                    }
                    task.onComplete(new CustomTask<>(order, task1.getException(), successful));
                });
        addListenerToOrder(reference);
    }

    private void addListenerToOrder(DocumentReference reference) {
        reference.addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null && documentSnapshot.exists() && documentSnapshot.getData() != null) {
                buildOrder(documentSnapshot.getData(), new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        Order order = isNewClient((Order) object);
                        isNewOrderUpdate(order);
                        orderRepository.save(order);
                    }

                    @Override
                    public void onFailure(Object object) {
                        // nothing
                    }
                });
            }
        });
    }

    private void isNewOrderUpdate(Order order) {
        if(order != null && order.getClient() != null) {
            Order orderCopy = orderRepository.getOrder(order.getId());
            if(orderCopy != null) {
                boolean isSame = order.equals(orderCopy);
                if(!isSame) {
                    OrderUp.createNotification("Order Update", String.format("You have an order update from %s", order.getClient().getName()), order);
                }
            } else if(!order.getId().equals(mAuth.getUid())){
                OrderUp.createNotification("New Order", String.format("You have an order from %s", order.getClient().getName()), order);
            }
        }
    }

    private Order isNewClient(Order object) {
        boolean isNewClient = true;
        Account account = accountRepository.getAccount();
        Order order = object;
        for (Client client : account.getClients()) {
            if (client.getUid().equals(order.getId())) {
                isNewClient = false;
            }
        }
        if(isNewClient) {
            addClient(order.getClient());
        }
        return order;
    }

    private void buildOrder(Map<String, Object> data, CallBack callBack) {
        Order order = new Order();
        order.setDate((long) data.get("date"));
        order.setId((String) data.get("id"));
        order.setForPayment((boolean) data.get("forPayment"));
        order.setStatus((String) data.get("status"));
        order.setTotal((double) data.get("total"));
        order.setItems(buildItems((List) data.get("items")));
        String to = (String) data.get("to");
        String from = (String) data.get("from");
        order.setPriceEditable(order.getTotal() == 0 ? !from.equals(mAuth.getUid()) : !to.equals(mAuth.getUid()));
        getClient(to, from, order, callBack);
    }

    private void addOrderToClient(String orderId, String clientId) {
        mStore.collection(USER_COLLECTION)
                .document(clientId)
                .update("orders", FieldValue.arrayUnion(orderId));
    }

    private void addOrderToAccount(String orderId) {
        mStore.collection(USER_COLLECTION)
                .document(mAuth.getUid())
                .update("orders", FieldValue.arrayUnion(orderId));
    }

    @Override
    public void updateOrder(Order order, OnComplete<CustomTask<Order>> customTask) {
        getDocumentId(ORDER_COLLECTION, "id", order.getId(), result -> {
            if(result.isSuccessful()) {
                mStore.collection(ORDER_COLLECTION)
                        .document(result.getResult())
                        .update(buildOrderMap(order))
                        .addOnCompleteListener(task -> {
                            customTask.onComplete(new CustomTask<>(order, task.getException(), task.isSuccessful()));
                        });
            }
        });
    }

    @Override
    public void removeClient(Client client) {
        List<Order> ordersByClient = orderRepository.getOrdersByClient(client);
        List<String> orderIds = new ArrayList<>();
        for (Order order : ordersByClient) {
            orderIds.add(order.getId());
        }
        mStore.collection(USER_COLLECTION)
                .document(mAuth.getUid())
                .update("orders",FieldValue.arrayRemove(orderIds.toArray(new Object[orderIds.size()]))
                        ,"clients", FieldValue.arrayRemove(client.getUid()));
        orderRepository.removeOrdersByClient(client);
    }

    @Override
    public void addClient(Client client) {
        if (client != null) {
            mStore.collection(USER_COLLECTION)
                    .document(mAuth.getUid())
                    .update("clients", FieldValue.arrayUnion(client.getUid()));
        }
    }

    @Override
    public void initializeListeners() {
        mStore.collection(USER_COLLECTION).document(mAuth.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> mStore.collection(USER_COLLECTION)
                        .document(documentSnapshot.getId())
                        .addSnapshotListener((accountSnapshot, e) -> {
                            getAccountAndSave(accountSnapshot.getData(), null);
                            List<String> orderIds = (List<String>) accountSnapshot.get("orders");
                            if (orderIds != null && orderIds.size() > 0) {
                                for (String id : orderIds) {
                                    DocumentReference orderReference = mStore.collection(ORDER_COLLECTION).document(id);
                                    addListenerToOrder(orderReference);
                                }
                            }
                        }));
    }

    private void getDocumentId(String collection, String field, String param, OnComplete<CustomTask<String>> customTask) {
        mStore.collection(collection)
                .whereEqualTo(field, param)
                .get()
                .addOnCompleteListener(task -> {
                    boolean successful = task.isSuccessful();
                    String id = null;
                    if(successful) {
                        QuerySnapshot result = task.getResult();
                        if (result != null && !result.getDocuments().isEmpty()) {
                            id = result.getDocuments().get(0).getId();
                        }
                    }
                    customTask.onComplete(new CustomTask<>(id, task.getException(), successful));
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

        RealmList<String> clientIds = new RealmList<>();
        for (Client client : account.getClients()) {
            clientIds.add(client.getUid());
        }
        map.put("clients", clientIds);
        map.put("orders", new ArrayList<String>());
        return map;
    }

    private Map<String, Object> buildOrderMap(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("to", order.getClient().getUid());
        map.put("from", mAuth.getUid());
        map.put("date", order.getDate());
        map.put("status", order.getStatus());
        map.put("forPayment", order.isForPayment());
        map.put("total", order.getTotal());
        map.put("items", getItemMaps(order.getItems()));
        return map;
    }

    private void getAccountAndSave(Map<String, Object> data, OnComplete<CustomTask<Account>> task) {
        Account account = new Account();
        account.setId((String) data.get("id"));
        account.setFirstName((String) data.get("firstName"));
        account.setLastName((String) data.get("lastName"));
        account.setLocation((String) data.get("location"));
        account.setContactNumber((String) data.get("contactNo"));
        account.setEmail((String) data.get("email"));
        account.setToken((String) data.get("token"));
        getClientsFromMap((List) data.get("clients"), clients -> {
            account.setClients(clients);
            accountRepository.save(account);
            if (task != null) {
                task.onComplete(new CustomTask<>(account, null, true));
            }
        });
    }

    private void getClientsFromMap(List clients, OnComplete<RealmList<Client>> onComplete) {
        RealmList<Client> clientRealmList = new RealmList<>();
        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (Object id : clients) {
            tasks.add(mStore.collection(USER_COLLECTION).whereEqualTo("id", id).get());
        }
        Task<List<QuerySnapshot>> taskResult = Tasks.whenAllSuccess(tasks);
        taskResult.addOnSuccessListener(querySnapshots -> {
            for (QuerySnapshot snapshots : querySnapshots) {
                List<DocumentSnapshot> documents = snapshots.getDocuments();
                if (!documents.isEmpty()) {
                    Map<String, Object> clientMap = documents.get(0).getData();
                    clientRealmList.add(getClient(clientMap));
                }
            }
            onComplete.onComplete(clientRealmList);
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

    private void getClient(String to, String from, Order order, CallBack callBack) {
        String id = mAuth.getUid().equals(to) ? from : to;
        Account account = accountRepository.getAccount();
        Client clientAccount = null;
        for (Client client : account.getClients()) {
            if (client.getUid() != null && id != null && client.getUid().equals(id)) {
                clientAccount = client;
            }
        }
        if (clientAccount == null) {
            mStore.collection(USER_COLLECTION)
                    .document(id)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && documentSnapshot.getData() != null) {
                            order.setClient(getClient(documentSnapshot.getData()));
                            callBack.onSuccess(order);
                        }
                    })
                    .addOnFailureListener(e -> callBack.onFailure(e.getMessage()));
        }
        order.setClient(clientAccount);
        callBack.onSuccess(order);
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

    private RealmList<Item> buildItems(List<Map<String, Object>> items) {
        RealmList<Item> itemList = new RealmList<>();
        for (Map<String, Object> itemMap : items) {
            itemList.add(getItemFromMap(itemMap));
        }
        return itemList;
    }

    private Item getItemFromMap(Map<String, Object> itemMap) {
        Item item = new Item();
        item.setName((String) itemMap.get("name"));
        item.setPackaging((String) itemMap.get("packaging"));
        item.setPrice(Double.parseDouble(String.valueOf(itemMap.get("price"))));
        item.setQuantity(Integer.parseInt(String.valueOf(itemMap.get("quantity"))));
        return item;
    }
}
