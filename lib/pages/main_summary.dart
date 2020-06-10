import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:orderupv2/components/summary_tab.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/order.dart';

class MainSummary extends StatelessWidget {
  final Account account;

  MainSummary({this.account});

  @override
  Widget build(BuildContext context) {
    var orders = account != null ? account.orders : [];
    return Scaffold(
      body: Container(),
//      body: Container(
//        color: primaryColor,
//        padding: EdgeInsets.symmetric(vertical: 5, horizontal: 10),
//        child: Column(
//          children: <Widget>[
//            Expanded(
//              child: Card(
//                child: SummaryTab(
//                    _filterOrdersForDelivery(orders, account),
//                    'For Delivery'),
//              ),
//            ),
//            Expanded(
//              child: Card(
//                child: SummaryTab(
//                    _filterOrdersToBeReceived(orders, account),
//                    'For Delivery'),
//              ),
//            ),
//          ],
//        ),
//      ),
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
}
