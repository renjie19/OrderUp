import 'package:flutter/material.dart';
import 'package:orderupv2/pages/client_list.dart';
import 'package:orderupv2/pages/loading.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
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
    final account = Provider.of<Account>(context);
    return account == null
        ? Loading(message: 'Getting User Data')
        : DefaultTabController(
            length: pages.length,
            child: Scaffold(
              backgroundColor: primaryColor,
              appBar: AppBar(
                leading: IconButton(
                  icon: Icon(Icons.account_circle),
                  onPressed: (){
                    // go to account page
                  },
                ),
                title: Text("ORDERUP"),
                bottom: TabBar(
                  indicatorColor: highlightColor[900],
                  labelColor: highlightColor[900],
                  unselectedLabelColor: Colors.white,
                  tabs: <Widget>[
                    Tab(icon: Icon(Icons.assessment), text: 'Summary', iconMargin: EdgeInsets.only(bottom: 2),),
                    Tab(icon: Icon(Icons.event_note), text: 'Reports', iconMargin: EdgeInsets.only(bottom: 2),),
                    Tab(icon: Icon(Icons.people), text: 'Clients', iconMargin: EdgeInsets.only(bottom: 2),),
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
                  ),
                ],
              ),
              body: TabBarView(
                children: pages,
              ),
            ),
          );
  }
}
