import 'package:flutter/material.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/shared/custom_callback.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderListDataSource extends DataTableSource {
  List<Order> orders;
  CustomCallBack callBack;

  OrderListDataSource(this.orders, this.callBack);

  @override
  DataRow getRow(int index) {
    Order order = orders[index];
    return DataRow(
      cells: [
        DataCell(
            Text(DateFormatter.toDateString(order.date),
                style: TextStyle(fontSize: 12)),
            onTap: () => callBack.run(order)),
        DataCell(Text('${order.status}', style: TextStyle(fontSize: 12)),
            onTap: () => callBack.run(order)),
        DataCell(Text('${order.total}', style: TextStyle(fontSize: 12)),
            onTap: () => callBack.run(order))
      ],
    );
  }

  @override
  bool get isRowCountApproximate => false;

  @override
  int get rowCount => orders.length;

  @override
  int get selectedRowCount => 0;
}
