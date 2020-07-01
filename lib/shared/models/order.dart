import 'dart:convert';

import 'package:orderupv2/shared/models/item.dart';

class Order {
  String to;
  String from;
  String id;
  int date;
  bool forPayment = false;
  List<Item> items;
  String status;
  double total;

  Order({
    this.id,
    this.date,
    this.forPayment,
    this.items,
    this.status,
    this.total,
    this.to,
    this.from,
  });

  Map toJson() {
    return {
      'id' : this.id,
      'date' : this.date,
      'forPayment' : this.forPayment,
      'status' : this.status,
      'items' : this.items != null ? jsonEncode(this.items) : [],
      'total' : this.total,
      'to' : this.to,
      'from' : this.from,
    };
  }
}
