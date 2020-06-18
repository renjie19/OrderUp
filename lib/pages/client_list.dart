import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/components/client_item.dart';
import 'package:orderupv2/pages/qr_page.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:provider/provider.dart';

class ClientList extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var account = Provider.of<Account>(context);
    var orders = Provider.of<List<Order>>(context);
    List<Client> clients = _loadClientOrders(account, orders);

    return Scaffold(
      backgroundColor: primaryColor[800],
      body: ListView.builder(
        padding: EdgeInsets.only(top: 20),
        itemBuilder: (context, position) {
          return ClientItem(clients[position]);
        },
        itemCount: clients.length,
      ),
      floatingActionButton: FloatingActionButton(
        backgroundColor: highlightColorSecondary,
        onPressed: () {
          Navigator.push(context, MaterialPageRoute(builder: (context) {
            return QrPage(account.id);
          }));
        },
        child: Icon(
          Feather.user_plus,
          color: primaryColor[700],
        ),
      ),
    );
  }

  List<Client> _loadClientOrders( Account account, List<Order> orders) {
    var clients = account == null ? [] : account.clients;

    if (orders != null) {
      orders.forEach((orderUpdate) {
        var match = account.orders.firstWhere((order) => order.id == orderUpdate.id, orElse: () => null);
        match == null
            ? account.orders.add(orderUpdate)
            : account.orders[account.orders.indexOf(match)] = orderUpdate;
      });
    }

    for (Client client in clients) {
      client.orders = account.orders
          .where((e) => e.to == client.id || e.from == client.id)
          .toList(growable: true);
    }
    return clients;
  }
}
