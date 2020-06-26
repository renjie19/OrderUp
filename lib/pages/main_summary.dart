import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/bloc/account_bloc.dart';
import 'package:orderupv2/bloc/order_list_bloc.dart';
import 'package:orderupv2/components/notification.dart';
import 'package:orderupv2/components/summary_list_tab.dart';
import 'package:orderupv2/components/summary_tab.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:provider/provider.dart';

class MainSummary extends StatefulWidget {

  @override
  _MainSummaryState createState() => _MainSummaryState();
}

class _MainSummaryState extends State<MainSummary> {
  AccountBloc accountBloc;
  OrderListBloc orderListBloc;


  @override
  void dispose() {
    super.dispose();
    accountBloc.close();
    orderListBloc.close();
  }

  @override
  Widget build(BuildContext context) {
    orderListBloc = BlocProvider.of<OrderListBloc>(context);
    accountBloc = BlocProvider.of<AccountBloc>(context);
    var update = Provider.of<List<Order>>(context);
    return BlocBuilder<OrderListBloc, List<Order>>(
      bloc: orderListBloc,
      builder: (context, orders) {
        _updateSummary(update, orders);
        return Scaffold(
          body: Container(
            height: double.maxFinite,
            padding: EdgeInsets.symmetric(vertical: 5, horizontal: 8),
            color: primaryColor[800],
            child: SingleChildScrollView(
              child: Column(
                children: <Widget>[
                  SummaryTab(orders, accountBloc.initialState),
                  //todo: add pending order counter
                  SummaryListTab(_filterOrdersForDelivery(orders ?? [], accountBloc.initialState), 'Items To Delivery'),
                ],
              ),
            ),
          ),
        );
      }
    );
  }

  List<Order> _filterOrdersForDelivery(List<Order> list, Account account) {
    if (account != null && list.isNotEmpty) {
      return list.where((order) {
        return order.from != account.id &&
            order.status == StatusConstant.FOR_DELIVERY;
      }).toList();
    }
    return [];
  }

  List<Order> _filterOrdersToBeReceived(List<Order> list, Account account) {
    if (account != null && list.isNotEmpty) {
      return list.where((order) {
        return order.from == account.id &&
            order.status == StatusConstant.FOR_DELIVERY;
      }).toList();
    }
    return [];
  }

  void _updateSummary(List<Order> update, List<Order> orderList) {
    if (update != null) {
      bool showNotif = update.isNotEmpty && update.length != orderList.length;
      update.forEach((orderUpdate) {
        var result =
        orderList.firstWhere((element) => orderUpdate.id == element.id);
        if(result == null) {
          orderList.add(orderUpdate);
          showNotification(showNotif, true);
        } else {
          if(result != orderUpdate) {
            orderList[orderList.indexOf(result)] = orderUpdate;
            var forDelivery = orderUpdate.status == StatusConstant.FOR_DELIVERY;
            showNotification(showNotif && forDelivery, false);
          }
        }
      });
      update.clear();
    }
  }

  void showNotification(bool show, bool isNew) {
    if(show) {
      OrderNotification.show(isNew);
    }
  }
}
