import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/pages/shop_page.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:provider/provider.dart';

class ClientInfoCard extends StatelessWidget {
  final Client client;

  ClientInfoCard(this.client);

  @override
  Widget build(BuildContext context) {
    return FlexibleSpaceBar(
      titlePadding: EdgeInsets.fromLTRB(30, 0, 0, 40),
      title: ListTile(
        title: Text(
          '${client.firstName} ${client.lastName}',
          style: TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.bold,
              fontSize: 20,
              fontFamily: 'Fredoka',
              letterSpacing: 1.5),
        ),
        subtitle: Text(
          client.location,
          style: TextStyle(
              fontFamily: 'Fredoka',
              fontSize: 18,
              fontWeight: FontWeight.bold,
              letterSpacing: 1),
        ),
      ),
    );
  }
}
