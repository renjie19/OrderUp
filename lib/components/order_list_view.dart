import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderListView extends StatelessWidget {
 final List<Order> orders;
 final IconData iconData;
 OrderListView(this.orders, this.iconData);

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      padding: EdgeInsets.symmetric(horizontal: 10),
      itemBuilder: (context, position) {
        var order = orders[position];
        return Card(
            child: ListTile(
              leading: Icon(
                iconData,
                color: highlightColor[900],
              ),
              title: Text(
                DateFormatter.toDateString(order.date),
              ),
              trailing: Text(order.status),
            ));
      },
      itemCount: orders.length,
    );
  }
}
