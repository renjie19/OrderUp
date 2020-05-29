import 'package:flutter/material.dart';
import 'package:orderupv2/shared/models/account.dart';

class ClientItem extends StatelessWidget {
  final Account client;


  ClientItem(this.client);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 10),
      child: Card(
        child: ListTile(
          title: Center(child: Text('${client.firstName} ${client.lastName}')),
          onTap: (){
            //proceed to client page
          },
        ),
      ),
    );
  }
}
