import 'package:flutter/material.dart';
import 'package:orderupv2/pages/orders.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:provider/provider.dart';

class ClientItem extends StatelessWidget {
  final Client client;

  ClientItem(this.client);

  @override
  Widget build(BuildContext context) {
    final account = Provider.of<Account>(context);
    final orders = account.orders
        .where((order) => order.to == client.id || order.from == client.id)
        .toList();

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 10),
      child: Card(
        child: ListTile(
          title: Center(child: Text('${client.firstName} ${client.lastName}')),
          onTap: () {
            //proceed to client page
            Navigator.push(context, MaterialPageRoute(
                builder: (context) {
              return StreamProvider<Account>.value(
                  value: AccountServiceImpl().userData,
                  child: Orders(client, orders));
            }));
          },
        ),
      ),
    );
  }
}
