import 'package:flutter/material.dart';
import 'package:orderupv2/pages/orders.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:provider/provider.dart';

class ClientItem extends StatelessWidget {
  final Client client;

  ClientItem(this.client);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 10),
      child: Card(
        child: ListTile(
          title: Center(child: Text('${client.firstName} ${client.lastName}')),
          onTap: () {
            //proceed to client page
            Navigator.push(context, MaterialPageRoute(
                builder: (context) {
              return StreamProvider<List<Order>>.value(
                  value: OrderService().orderUpdates,
                  child: Orders(client));
            }));
          },
        ),
      ),
    );
  }
}
