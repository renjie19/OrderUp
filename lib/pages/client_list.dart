import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/bloc/account_bloc.dart';
import 'package:orderupv2/bloc/order_list_bloc.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:provider/provider.dart';

import 'orders.dart';

class ClientList extends StatefulWidget {
  @override
  _ClientListState createState() => _ClientListState();
}

class _ClientListState extends State<ClientList> {
  AccountBloc accountBloc;
  OrderListBloc orderListBloc;
  Account account;


  @override
  void dispose() {
    super.dispose();
    accountBloc.close();
    orderListBloc.close();
  }

  @override
  void initState() {
    super.initState();
    accountBloc = BlocProvider.of<AccountBloc>(context);
    orderListBloc = BlocProvider.of<OrderListBloc>(context);
  }

  @override
  Widget build(BuildContext context) {
    var account = Provider.of<Account>(context);
    var orders = Provider.of<List<Order>>(context);
    List<Client> clients = _loadClientOrders(account, orders);
    return BlocBuilder(
        bloc: accountBloc,
        builder: (context, accountUpdate) {
          account = accountUpdate;
          return Scaffold(
            backgroundColor: primaryColor[800],
            body: ListView.builder(
              padding: EdgeInsets.only(top: 20),
              itemCount: clients.length,
              itemBuilder: (context, position) {
                var client = clients[position];
                return Padding(
                  padding:
                      const EdgeInsets.symmetric(vertical: 0, horizontal: 10),
                  child: Card(
                    child: ListTile(
                      title: Center(
                          child:
                              Text('${client.firstName} ${client.lastName}')),
                      onTap: () {
                        Navigator.push(context,
                            MaterialPageRoute(builder: (context) {
                          return StreamProvider<Account>.value(
                            value: accountBloc.userData,
                            child: StreamProvider<List<Order>>.value(
                              value: orderListBloc.orderUpdates,
                              child: MultiProvider(
                                  providers: [
                                    BlocProvider<OrderListBloc>(create: (context) => OrderListBloc()),
                                    BlocProvider<AccountBloc>(create: (context) => AccountBloc()),
                                  ],
                                  child: Orders(client)),
                            ),
                          );
                        }));
                      },
                    ),
                  ),
                );
              },
            ),
            floatingActionButton: FloatingActionButton(
              backgroundColor: highlightColorSecondary,
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) {
//                  return QrPage(account.id);
                return null;
                }));
              },
              child: Icon(
                Feather.user_plus,
                color: primaryColor[700],
              ),
            ),
          );
        });
  }

  List<Client> _loadClientOrders(Account account, List<Order> orders) {
    var clients = account == null ? [] : account.clients;

    if (orders != null) {
      orders.forEach((orderUpdate) {
        var match = account.orders.firstWhere(
                (order) => order.id == orderUpdate.id,
            orElse: () => null);
        match == null
            ? account.orders.add(orderUpdate)
            : account.orders[account.orders.indexOf(match)] = orderUpdate;
      });
    }

    for (Client client in clients) {
      client.orders = account.orders
          .where((e) => e.to == client.id || e.from == client.id)
          .toList(growable: true);
    }
    return clients;
  }
}
