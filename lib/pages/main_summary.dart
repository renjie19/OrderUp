import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:orderupv2/components/summary_list_tab.dart';
import 'package:orderupv2/components/summary_tab.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:provider/provider.dart';

class MainSummary extends StatelessWidget {
  final Account account;

  MainSummary({this.account});

  @override
  Widget build(BuildContext context) {
    var update = Provider.of<List<Order>>(context);
    var orders = account != null ? account.orders : [];
    _updateSummary(update, orders);

    return Scaffold(
      body: Container(
        height: double.maxFinite,
        padding: EdgeInsets.symmetric(vertical: 5, horizontal: 8),
        color: primaryColor[800],
        child: SingleChildScrollView(
          child: Column(
            children: <Widget>[
              SummaryTab(orders, account),
              SummaryListTab(_filterOrdersForDelivery(orders ?? [], account), 'Items To Delivery'),
//              SummaryListTab(_filterOrdersToBeReceived(orders ?? [], account), 'To Be Received'),
            ],
          ),
        ),
      ),
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
      update.forEach((orderUpdate) {
        var result =
        orderList.firstWhere((element) => orderUpdate.id == element.id);
        result == null
            ? orderList.add(orderUpdate)
            : orderList[orderList.indexOf(result)] = orderUpdate;
      });
    }
  }
}
