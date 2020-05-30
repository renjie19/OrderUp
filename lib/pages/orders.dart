import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/components/client_info_card.dart';
import 'package:orderupv2/components/order_list_view.dart';
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
  bool isSelected = false;
  bool toReceive = false;

  final List<Map<String, Object>> type = [
    {'label': 'Deliver', 'icon': Feather.truck},
    {'label': 'Receive', 'icon': Feather.box}
  ];

  @override
  Widget build(BuildContext context) {
    filterOrderByClient(Provider.of<Account>(context));
    return SafeArea(
      child: Scaffold(
        backgroundColor: primaryColor,
        body: Column(
          children: <Widget>[
            Row(
              mainAxisSize: MainAxisSize.max,
              children: <Widget>[
                IconButton(
                  icon: Icon(Icons.arrow_back, color: Colors.white),
                  onPressed: () {
                    Navigator.pop(context);
                  },
                ),
                Expanded(child: ClientInfoCard(widget.client)),
              ],
            ),
            Wrap(
              spacing: 30,
              children: List<Widget>.generate(type.length, (index) {
                return ChoiceChip(
                  elevation: 5,
                  pressElevation: 30,
                  selectedColor: highlightColor[700],
                  backgroundColor: Colors.grey,
                  padding: EdgeInsets.symmetric(horizontal: 10),
                  avatar: Icon(
                    type[index]['icon'],
                    color: Colors.white,
                  ),
                  label: Text(
                    type[index]["label"],
                    style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                        letterSpacing: 1.5),
                  ),
                  selected: index % 2 != 0 ? isSelected : !isSelected,
                  onSelected: (value) => onTypeSelect(index),
                );
              }),
            ),
            Expanded(
              child: OrderListView(
                filterByType(orderList, toReceive),
                toReceive ? Feather.box : Feather.truck,
              ),
            ),
          ],
        ),
      ),
    );
  }

  List<Order> filterByType(List<Order> orders, bool toReceive) {
    return orders
        .where(
            (order) => (toReceive ? order.to : order.from) == widget.client.id)
        .toList();
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

  void onTypeSelect(index) {
    setState(() {
      isSelected = index % 2 != 0;
      toReceive = index % 2 != 0;
    });
  }
}

