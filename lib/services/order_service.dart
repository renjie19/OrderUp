import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderService {
  static final CollectionReference _orderCollectionReference = Firestore.instance.collection("Orders");

  // getOrders
  Future<List<Order>> orders(orderIds) async {
    List<Order> orders = [];
    List<Item> itemList = [];
    for(String id in orderIds) {
      DocumentSnapshot snapshot = await _orderCollectionReference.document(id).get();
      Map<String, Object> item = snapshot.data;
      itemList.add(Item(
          name: item['name'],
          package: item['packaging'],
          price: item['price'],
          quantity: item['quantity']
      ));
      orders.add(Order(
        id: snapshot['id'],
        date: snapshot['date'],
        forPayment: snapshot['forPayment'],
        items: itemList,
        status: snapshot['status'],
        total: snapshot['total'],
        to: snapshot['to'],
        from: snapshot['from']
      ));
    }
    return orders;
  }

  static String generateOrderId() {
    return _orderCollectionReference.document().documentID;
  }

  // update order

}