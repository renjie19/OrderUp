import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/pages/shop_page.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderListView extends StatelessWidget {
  final List<Order> orders;
  final IconData iconData;
  final Client client;

  OrderListView({this.orders, this.iconData, this.client}) {
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
              // show shop page
              Order order = await Navigator.push(context, MaterialPageRoute(
                builder: (context) {
                  return ShopPage(client, orders[position], isUpdate: true);
                },
              ));
//              if(order != null) {
//                print('adding order');
//                setState(() {
//                  account.orders.add(order);
//                });
//              }
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
