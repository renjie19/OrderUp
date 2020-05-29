import 'package:flutter/material.dart';
import 'package:orderupv2/pages/client_list.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:provider/provider.dart';

class Home extends StatelessWidget {
  AccountService _accountService;
  final List<Widget> pages = [
    Container(
      color: Colors.red,
    ),
    Container(
      color: Colors.white,
    ),
    ClientList(),
  ];

  Home(String id) {
   _accountService = AccountService(uid: id);
  }

  @override
  Widget build(BuildContext context) {
    return StreamProvider<Account>.value(
      value: _accountService.userData,
      child: DefaultTabController(
        length: pages.length,
        child: Scaffold(
          backgroundColor: primaryColor,
          appBar: AppBar(
            elevation: 0,
            title: Text("ORDERUP"),
            bottom: TabBar(
              tabs: <Widget>[
                Tab(icon: Icon(Icons.assessment)),
                Tab(icon: Icon(Icons.event_note)),
                Tab(icon: Icon(Icons.people)),
              ],
            ),
            actions: <Widget>[
              IconButton(
                icon: Icon(
                  Icons.exit_to_app,
                  color: Colors.white,
                ),
                onPressed: () {
                  AuthService().signOut();
                },
              )
            ],
          ),
          body: TabBarView(
            children: pages,
          ),
        ),
      ),
    );
  }
}
