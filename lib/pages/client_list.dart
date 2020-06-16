import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/components/client_item.dart';
import 'package:orderupv2/pages/qr_page.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:provider/provider.dart';

class ClientList extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    final account = Provider.of<Account>(context);
    List _clients = account == null ? [] : account.clients;

    return Scaffold(
      backgroundColor: primaryColor[800],
      body: ListView.builder(
        padding: EdgeInsets.only(top: 20),
        itemBuilder: (context, position) {
          return ClientItem(_clients[position]);
        },
        itemCount: _clients.length,
      ),
      floatingActionButton: FloatingActionButton(
        backgroundColor: highlightColorSecondary,
        onPressed: (){
          Navigator.push(context, MaterialPageRoute(
              builder: (context) {
                return QrPage(account.id);
              }));
        },
        child: Icon(Feather.user_plus, color: primaryColor[700],),
      ),
    );
  }
}
