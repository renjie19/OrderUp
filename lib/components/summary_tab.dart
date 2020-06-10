import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/order.dart';

class SummaryTab extends StatelessWidget {
  final List<Order> orders;
  final Account account;

  SummaryTab(this.orders, this.account);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Card(
        child: ConstrainedBox(
          constraints: BoxConstraints(
            maxHeight: 180,
          ),
          child: GridView.count(
            padding: EdgeInsets.all(10),
            shrinkWrap: true,
            crossAxisCount: 2,
            children: <Widget>[
              _getReportTile('Number of stores to deliver', _countShopsToDeliver(orders)),
              _getReportTile('Total amount to collect', '\$${_countTotalAmountToCollect(orders)}'),
            ],
          ),
        ),
      ),
    );
  }

  _countShopsToDeliver(List<Order> orders) {
    if (account != null && orders != null) {
      return _filterItemsForDelivery(orders).length;
    }
    return 0;
  }

  List<Order> _filterItemsForDelivery(List<Order> orders) {
    return orders
        .where((element) {
          return element.to == account.id &&
              element.status == StatusConstant.FOR_DELIVERY;
        }).toList();
  }

  _getReportTile(String s, count) {
    return Card(
      elevation: 3,
      child: GridTile(
        footer: Text(s, style: _getTextStyle(),textAlign: TextAlign.center,),
        child: Center(
          child: Text(
            '$count',
            style: _getTextStyle().copyWith(fontSize: 40),
          ),
        ),
      ),
    );
  }

  // todo add filter to only delivery within the week
  _countTotalAmountToCollect(List<Order> orders) {
    var totalAmount = 0.0;
    _filterItemsForDelivery(orders).forEach((element) {
      totalAmount += element.total;
    });
    return totalAmount == 0 ? 0 : totalAmount;
  }

  TextStyle _getTextStyle() {
    return TextStyle(
      fontSize: 20,
      fontWeight: FontWeight.bold,
    );
  }


}
