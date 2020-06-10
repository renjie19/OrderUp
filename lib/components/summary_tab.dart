import 'package:flutter/material.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';

class SummaryTab extends StatelessWidget {
  final List<Order> orders;
  final AccountService _accountService = AccountServiceImpl();
  final String title;

  SummaryTab(this.orders, this.title);

  @override
  Widget build(BuildContext context) {
    final List<Map<String, Object>> itemSummary = summarizeOrders(orders ?? []);
    return Container(
      child: Card(
        child: Column(
          children: <Widget>[
            Text(
              title,
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 18
              ),
            ),
            ConstrainedBox(
              constraints: BoxConstraints(
                  maxHeight: itemSummary.isEmpty
                      ? double.minPositive + 56
                      : itemSummary.length * 56.0),
              child: ListView.builder(
                  itemCount: itemSummary.length,
                  itemBuilder: (context, index) {
                    var item = itemSummary[index];
                    return ListTile(
                      onTap: () {},
                      title: Text(item['name']),
                      trailing: Text('${item['quantity']} '),
                    );
                  }),
            ),
          ],
        ),
      ),
    );
  }

  List<Map<String, Object>> summarizeOrders(List<Order> orders) {
    Map<String, Object> summary = {};
    for (Order order in orders) {
      for (Item item in order.items) {
        List<Item> value = summary['${item.name}-${item.package}'];
        if (value == null) {
          value = [];
        }
        value.add(item);
        summary['${item.name}-${item.package}'] = value;
      }
    }
    List<Map<String, Object>> list = [];
    summary.forEach((key, value) {
      List<Item> items = value;
      int quantity = 0;
      String name = '';
      String package = '';

      items.forEach((element) {
        name = element.name;
        quantity += element.quantity;
        package = element.package;
      });

      if (quantity > 0) {
        list.add({'name': name, 'quantity': '$quantity $package'});
      }
    });
    return list;
  }
}
