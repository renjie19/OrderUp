import 'package:flutter/material.dart';
import 'package:orderupv2/components/client_item.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';

class Orders extends StatefulWidget {
  final List<Order> orders;
  final Client client;

  Orders(this.orders, this.client);

  @override
  _OrdersState createState() => _OrdersState(client, orders);
}

class _OrdersState extends State<Orders> {
  Client client;
  List<Order> orders;

  _OrdersState(this.client, this.orders);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.add_circle_outline),
            onPressed: () {},
          )
        ],
      ),
      backgroundColor: primaryColor,
      body: Container(
        padding: EdgeInsets.symmetric(vertical: 0, horizontal: 10),
        height: double.maxFinite,
        child: ListView.builder(
          itemBuilder: (context, position) {
            var order = orders[position];
            return Card(
                child: ListTile(
              leading: Icon(order.from == client.id
                  ? Icons.file_download
                  : Icons.file_upload),
              title: Text(
                DateFormatter.toDateString(order.date),
              ),
              trailing: Text(order.status),
            ));
          },
          itemCount: orders.length,
        ),
      ),
    );
  }
}
