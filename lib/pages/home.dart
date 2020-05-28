import 'package:flutter/material.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:provider/provider.dart';

class Home extends StatelessWidget {
  final String id;

  Home({this.id});

  @override
  Widget build(BuildContext context) {
    return StreamProvider<Account>.value(
      value: AccountService(uid: id).userData,
      child: Scaffold(
        backgroundColor: primaryColor,
        appBar: AppBar(
          actions: <Widget>[
            IconButton(
              icon: Icon(Icons.exit_to_app, color: Colors.white,),
              onPressed: () {
                AuthService().signOut();
              },
            )
          ],
          title: Text("ORDERUP"),
        ),
        body: Column(
          children: <Widget>[
            Text("Home"),

          ],
        ),
      ),
    );
  }
}
