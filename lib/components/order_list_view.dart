import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderListView extends StatelessWidget {
  final List<Order> orders;
  final IconData iconData;

  OrderListView({this.orders, this.iconData}) {
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
            onTap: () {},
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
//    return SliverList(
//      delegate: SliverChildBuilderDelegate((context, position) {
//          var order = orders[position];
//          return Card(
//              child: ListTile(
//                onTap: () {},
//                leading: Icon(
//                  iconData,
//                  color: highlightColor[900],
//                ),
//                title: Text(
//                  DateFormatter.toDateString(order.date),
//                ),
//                trailing: Text(order.status),
//              ));
//        },
//        childCount: orders.length,
//      ),
//    );

//    return ListView.builder(
//      padding: EdgeInsets.symmetric(horizontal: 10),
//      itemBuilder: (context, position) {
//        var order = orders[position];
//        return Card(
//            child: ListTile(
//              onTap: (){},
//              leading: Icon(
//                iconData,
//                color: highlightColor[900],
//              ),
//              title: Text(
//                DateFormatter.toDateString(order.date),
//              ),
//              trailing: Text(order.status),
//            ));
//      },
//      itemCount: orders.length,
//    );
  }
}
