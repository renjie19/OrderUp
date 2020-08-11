import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/bloc/account_bloc.dart';
import 'package:orderupv2/bloc/client_list_bloc/client_list_bloc.dart';
import 'package:orderupv2/bloc/order_list_bloc.dart';
import 'package:orderupv2/components/custom_alert_dialog.dart';
import 'package:orderupv2/components/loading.dart';
import 'package:orderupv2/pages/account_management.dart';
import 'package:orderupv2/pages/main_summary.dart';
import 'package:orderupv2/pages/v2/client/clients_tab.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:provider/provider.dart';

class Home extends StatefulWidget {
  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  @override
  Widget build(BuildContext context) {
    final account = Provider.of<Account>(context);
    final List<Widget> pages = [MainSummary(), ClientsTab()];
    final List<Map<String, Object>> tabs = [
      {'icon': Icon(Icons.assessment), 'text': 'Summary'},
      {'icon': Icon(Icons.people), 'text': 'Clients'},
    ];

    return account == null
        ? Loading(message: 'Getting User Data')
        : MultiBlocProvider(
            providers: [
              BlocProvider<OrderListBloc>(create: (context) => OrderListBloc()),
              BlocProvider<AccountBloc>(create: (context) => AccountBloc()),
              BlocProvider<ClientListBloc>(create: (context) => ClientListBloc()),
            ],
            child: DefaultTabController(
                length: pages.length,
                child: StreamProvider<List<Order>>.value(
                  value: OrderService().orderUpdates,
                  child: Scaffold(
                    backgroundColor: primaryColor,
                    appBar: AppBar(
                      backgroundColor: primaryColor[700],
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
                          icon: Icon(Icons.account_circle),
                          onPressed: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => BlocProvider(
                                    create: (context) => AccountBloc(),
                                    child: AccountManagement()),
                              ),
                            );
                          },
                        ),
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
                )),
          );
  }
}
