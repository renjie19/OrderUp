import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/client_service.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderService {
  static final CollectionReference _orderCollectionReference =
      Firestore.instance.collection("Orders");

  // getOrders
  Future<List<Order>> orders(orderIds) async {
    List<Order> orders = [];
    List<Item> itemList = [];
    for (String id in orderIds) {
      DocumentSnapshot snapshot =
          await _orderCollectionReference.document(id).get();
      Map<String, Object> item = snapshot.data;
      itemList.add(Item(
          name: item['name'],
          package: item['packaging'],
          price: item['price'],
          quantity: item['quantity']));
      orders.add(Order(
          id: snapshot['id'],
          date: snapshot['date'],
          forPayment: snapshot['forPayment'],
          items: itemList,
          status: snapshot['status'],
          total: snapshot['total'],
          to: snapshot['to'],
          from: snapshot['from']));
    }
    return orders;
  }

  // create order
  Future create(Order order) async {
    DocumentReference orderDocumentReference =
        _orderCollectionReference.document();
    order.id = orderDocumentReference.documentID;
    return await orderDocumentReference.setData(mapOrder(order)).then((value) {
      return order;
    }).catchError((error) {
      print('order create error: $error');
      return null;
    });
  }

  static String generateOrderId() {
    return _orderCollectionReference.document().documentID;
  }

  Map<String, Object> mapOrder(Order order) {
    List<Map<String, Object>> items = [];
    for (Item item in order.items) {
      items.add({
        'name': item.name,
        'quantity': item.quantity,
        'packaging': item.package,
        'price': item.price
      });
    }
    Map<String, Object> orderMap = {
      'id': order.id,
      'from': order.from,
      'to': order.to,
      'date': order.date,
      'forPayment': order.forPayment,
      'items': items,
      'status': order.status,
      'total': order.total
    };
    return orderMap;
  }

// update order

}
