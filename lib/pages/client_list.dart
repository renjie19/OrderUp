import 'package:flutter/material.dart';
import 'package:orderupv2/components/client_item.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:provider/provider.dart';

class ClientList extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    final account = Provider.of<Account>(context);
    List _clients = account == null ? [] : account.clients;

    return ListView.builder(
      padding: EdgeInsets.only(top: 20),
      itemBuilder: (context, position) {
        return ClientItem(_clients[position]);
      },
      itemCount: _clients.length,
    );
  }
}
