import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/components/client_info_card.dart';
import 'package:orderupv2/components/order_list_view.dart';
import 'package:orderupv2/pages/purchase_tab.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/custom_callback.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:provider/provider.dart';

class Orders extends StatefulWidget{
  final Client client;
  final List<Order> orders;

  Orders(this.client, this.orders);

  @override
  _OrdersState createState() => _OrdersState();

}

class _OrdersState extends State<Orders> implements CustomCallBack {
  List<Order> orderList;
  bool isSelected = false;
  bool toReceive = false;
  Account account;

  final List<Map<String, Object>> type = [
    {'label': 'Deliver', 'icon': Feather.truck},
    {'label': 'Receive', 'icon': Feather.box}
  ];

  @override
  Widget build(BuildContext context) {
    account = Provider.of<Account>(context);
    filterOrderByClient(account);
    return SafeArea(
        child: Scaffold(
          body: CustomScrollView(
            slivers: <Widget>[
              SliverAppBar(
                elevation: 20,
                expandedHeight: 200,
                floating: false,
                pinned: true,
                bottom: PreferredSize(
                  preferredSize: Size(double.maxFinite, 50),
                  child: typeWidget(),
                ),
                flexibleSpace: ClientInfoCard(widget.client),
                actions: <Widget>[
                  IconButton(
                    icon: Icon(
                      Feather.shopping_bag,
                      color: Colors.white,
                    ),
                    onPressed: () async {
                      Order order = await Navigator.push(context, MaterialPageRoute(
                        builder: (context) {
                          return PurchaseTab(widget.client, Order(), isUpdate: false, isPriceEditable: false,);
                        },
                      ));
                      if(order != null) {
                        setState(() {
                          account.orders.add(order);
                        });
                      }
                    },
                  ),
                ],
              ),
              OrderListView(
                client: widget.client,
                orders: filterByType(orderList, toReceive),
                iconData: toReceive ? Feather.box : Feather.truck,
                callBack: this,
              ),
            ],
          ),
        ));
  }

  /// for filtering order based on type
  List<Order> filterByType(List<Order> orders, bool toReceive) {
    return orders
        .where(
            (order) => (toReceive ? order.to : order.from) == widget.client.id)
        .toList();
  }

  /// for filtering new update from stream
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

  Widget typeWidget() {
    return Wrap(
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
    );
  }

  @override
  void run(Object object) {
    Order order = object;
    if(order != null && account != null) {
      setState(() {
        var copy = account.orders.where((element) => element.id == order.id).first;
        var index = account.orders.indexOf(copy);
        print('index: $index');
        print(order.status);
        index < 0 ? account.orders.add(order) : account.orders[index] = order;
      });
    }
  }
}

