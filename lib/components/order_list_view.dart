import 'package:flutter/material.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/pages/shop_page.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/custom_callback.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderListView extends StatelessWidget {
  final List<Order> orders;
  final IconData iconData;
  final Client client;
  final CustomCallBack callBack;

  OrderListView({this.orders, this.iconData, this.client, this.callBack}) {
    /// for sorting of orders by latest date
    this.orders.sort((o1,o2) {
      return o2.date.compareTo(o1.date);
    });
  }

  @override
  Widget build(BuildContext context) {
    return SliverList(
      delegate: SliverChildBuilderDelegate(
        (context, position) {
          return ListTile(
            onTap: () async{
              /// shows shop page
              Order order = await Navigator.push(context, MaterialPageRoute(
                builder: (context) {
                  return ShopPage(client, orders[position], isUpdate: true);
                },
              ));
              if(order != null) {
                callBack.runFunction(order);
              }
            },
            leading: Icon(
              iconData,
              color: highlightColor[900],
            ),
            title: Text(
              DateFormatter.toDateString(orders[position].date),
            ),
            subtitle: Text(
              DateFormatter.toTimeString(orders[position].date),
            ),
            trailing: Text(orders[position].status),
          );
        },
        childCount: orders.length,
      ),
    );
  }
}
