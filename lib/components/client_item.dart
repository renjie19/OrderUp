import 'package:flutter/material.dart';
import 'package:orderupv2/pages/orders.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:provider/provider.dart';

class ClientItem extends StatelessWidget {
  final Client client;
  ClientItem(this.client);

  @override
  Widget build(BuildContext context) {
    final orders = Provider.of<Account>(context).orders.where((order) => order.to == client.id || order.from == client.id).toList();
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 10),
      child: Card(
        child: ListTile(
          title: Center(child: Text('${client.firstName} ${client.lastName}')),
          onTap: (){
            //proceed to client page
            Navigator.push(context, PageRouteBuilder(
                pageBuilder: (context, animation, secondaryAnimation) {
                  return Orders(orders, client);
                }));
          },
        ),
      ),
    );
  }
}
