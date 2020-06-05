import 'package:flutter/material.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';

class SummaryTab extends StatelessWidget {
  final List<Order> orders;

  SummaryTab(this.orders);

  @override
  Widget build(BuildContext context) {
    final List<Map<String, Object>> itemSummary = summarizeOrders(orders ?? []);
    return Scaffold(
      body: Container(
        child: Card(
          color: Colors.green,
          child: Column(
            children: <Widget>[
              Center(child: Text('For Delivery'),),
              Expanded(
                child: PageView.builder(
                    itemCount: itemSummary.length,
                    itemBuilder: (context, position) {
                      var item = itemSummary[position];
                      return ListTile(
                        title: Text(item['name']),
                        trailing: Text('${item['quantity']}'),
                      );
                    }),
              ),
            ],
          ),
        ),
      ),
    );
  }

  List<Map<String, Object>> summarizeOrders(List<Order> orders) {
    orders = orders
        .where((element) => element.status == StatusConstant.FOR_DELIVERY)
        .toList();
    Map<String, Object> summary = {};
    for (Order order in orders) {
      for (Item item in order.items) {
        int count = summary[item.name];
        summary[item.name] =
            count == null ? item.quantity : count + item.quantity;
      }
    }
    List<Map<String, Object>> list = [];
    summary.forEach((key, value) {
      list.add({'name': key, 'quantity': value});
    });
    return list;
  }
}
