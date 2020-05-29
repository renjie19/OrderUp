import 'package:flutter/material.dart';
import 'package:orderupv2/components/logout_alert.dart';
import 'package:orderupv2/pages/account_management.dart';
import 'package:orderupv2/pages/client_list.dart';
import 'package:orderupv2/components/loading.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:provider/provider.dart';

class Home extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final account = Provider.of<Account>(context);
    final List<Widget> pages = [
      Container(),
      Container(),
      ClientList(),
    ];
    return account == null
        ? Loading(message: 'Getting User Data')
        : DefaultTabController(
            length: pages.length,
            child: Scaffold(
              backgroundColor: primaryColor,
              appBar: AppBar(
                leading: IconButton(
                  icon: Icon(Icons.account_circle),
                  onPressed: () {
                    Navigator.push(context, PageRouteBuilder(
                        pageBuilder: (context, animation, secondaryAnimation) {
                      return AccountManagement(account);
                    }));
                  },
                ),
                title: Text("ORDERUP"),
                bottom: TabBar(
                  indicatorColor: highlightColor[900],
                  labelColor: highlightColor[900],
                  unselectedLabelColor: Colors.white,
                  tabs: <Widget>[
                    Tab(
                      icon: Icon(Icons.assessment),
                      text: 'Summary',
                      iconMargin: EdgeInsets.only(bottom: 2),
                    ),
                    Tab(
                      icon: Icon(Icons.event_note),
                      text: 'Reports',
                      iconMargin: EdgeInsets.only(bottom: 2),
                    ),
                    Tab(
                      icon: Icon(Icons.people),
                      text: 'Clients',
                      iconMargin: EdgeInsets.only(bottom: 2),
                    ),
                  ],
                ),
                actions: <Widget>[
                  IconButton(
                      icon: Icon(
                        Icons.exit_to_app,
                        color: Colors.white,
                      ),
                      onPressed: () {
                        showDialog(
                            context: context,
                            barrierDismissible: false,
                            builder: (context) => LogOutDialog());
                      }),
                ],
              ),
              body: TabBarView(
                children: pages,
              ),
            ),
          );
  }
}
