import 'package:flutter/material.dart';
import 'package:orderupv2/components/client_item.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:provider/provider.dart';

class ClientList extends StatelessWidget {
  @override
  Widget build(BuildContext context) {

    List _clients = [];
    final account = Provider.of<Account>(context);
    _clients = account == null ? [] : account.clients;

    return ListView.builder(
      itemBuilder: (context, position) {
        return ClientItem(_clients[position]);
      },
      itemCount: _clients.length,
    );
  }
}
