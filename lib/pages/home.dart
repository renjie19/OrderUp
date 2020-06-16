import 'package:flutter/material.dart';
import 'package:orderupv2/components/custom_alert_dialog.dart';
import 'package:orderupv2/pages/account_management.dart';
import 'package:orderupv2/pages/client_list.dart';
import 'package:orderupv2/components/loading.dart';
import 'package:orderupv2/pages/main_summary.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:provider/provider.dart';

class Home extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final account = Provider.of<Account>(context);
    final List<Widget> pages = [
      MainSummary(account: account),
      ClientList()
    ];
    final List<Map<String, Object>> tabs = [
      {'icon': Icon(Icons.assessment), 'text': 'Summary'},
      {'icon': Icon(Icons.people), 'text': 'Clients'},
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
                  indicatorColor: highlightColor[500],
                  labelColor: highlightColor[500],
                  unselectedLabelColor: Colors.white,
                  tabs: List<Widget>.generate(tabs.length, (index) {
                    return Tab(
                        icon: tabs[index]['icon'],
                        text: tabs[index]['text'],
                        iconMargin: EdgeInsets.only(bottom: 2));
                  }),
                ),
                actions: <Widget>[
                  IconButton(
                      icon: Icon(Icons.exit_to_app, color: Colors.white),
                      onPressed: () {
                        showDialog(
                            context: context,
                            barrierDismissible: false,
                            builder: (context) => CustomAlertDialog(
                                title: Text('LOGOUT'),
                                content: Text(
                                    'You are logging out.\nAre You Sure?',
                                    textAlign: TextAlign.center),
                                onNo: () => Navigator.pop(context),
                                onYes: () {
                                  Navigator.pop(context);
                                  AuthService().signOut();
                                }));
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
