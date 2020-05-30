import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:provider/provider.dart';

class Orders extends StatefulWidget {
  final Client client;
  final List<Order> orders;

  Orders(this.client, this.orders);

  @override
  _OrdersState createState() => _OrdersState();
}

class _OrdersState extends State<Orders> {
  List<Order> orderList;

  @override
  Widget build(BuildContext context) {
    filterOrderByClient(Provider.of<Account>(context));
    return Scaffold(
      backgroundColor: primaryColor,
      appBar: AppBar(
        title: ListTile(
          contentPadding: EdgeInsets.fromLTRB(10, 10, 0, 5),
          leading: CircleAvatar(
            backgroundColor: Colors.white,
            child: Icon(
              Icons.person,
              size: 35,
            ),
          ),
          title: Text(
            '${widget.client.firstName} ${widget.client.lastName}',
            style: TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.bold,
                fontSize: 20,
                fontFamily: 'Fredoka'),
          ),
          subtitle: Text(
            widget.client.location,
            style: TextStyle(
              fontFamily: 'Fredoka',
              fontSize: 18,
              fontWeight: FontWeight.bold
            ),
          ),
        ),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.add_circle_outline),
            onPressed: () {},
          )
        ],
      ),
      body: Container(
        padding: EdgeInsets.symmetric(vertical: 10, horizontal: 10),
        height: double.maxFinite,
        child: ListView.builder(
          itemBuilder: (context, position) {
            var order = orderList[position];
            return Card(
                child: ListTile(
              leading: getAppropriateIcon(order, widget.client),
              title: Text(
                DateFormatter.toDateString(order.date),
              ),
              trailing: Text(order.status),
            ));
          },
          itemCount: orderList.length,
        ),
      ),
    );
  }

  Icon getAppropriateIcon(Order order, Client client) {
    return Icon(
      order.from == client.id
          ? FontAwesome.truck
          : FontAwesome.shopping_cart,
      color: order.from == widget.client.id
          ? highlightColor[900]
          : primaryColor,
    );
  }

  void filterOrderByClient(Account account) {
    setState(() {
      if (account != null) {
        orderList = account.orders
            .where((order) =>
                order.to == widget.client.id || order.from == widget.client.id)
            .toList();
      } else {
        orderList = widget.orders;
      }
    });
  }
}
