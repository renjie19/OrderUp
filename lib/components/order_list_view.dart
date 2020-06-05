import 'package:flutter/material.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/pages/purchase_tab.dart';
import 'package:orderupv2/pages/receipt_preview.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/custom_callback.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderListView extends StatelessWidget {
  final List<Order> orders;
  final IconData iconData;
  final Client client;
  final CustomCallBack callBack;

  OrderListView({this.orders, this.iconData, this.client, this.callBack}) {
    /// for sorting of orders by latest date
    this.orders.sort((o1, o2) {
      return o2.date.compareTo(o1.date);
    });
  }

  @override
  Widget build(BuildContext context) {
    return SliverList(
      delegate: SliverChildBuilderDelegate(
        (context, position) {
          return ListTile(
            onTap: () async {
              var selectedOrder = orders[position];
              var isFromCurrentUser = selectedOrder.from == client.id;
              var isStillPending = selectedOrder.status == StatusConstant.PENDING;
              Order order = isFromCurrentUser || isStillPending
                  ? await showPurchaseTab(context, selectedOrder)
                  : await showReceiptPreview(context, selectedOrder);

              /// called to return the updated object to the selectedOrder list page
              if (order != null) {
                callBack.run(order);
              }
            },
            leading: Icon(
              iconData,
              color: highlightColor[900],
            ),
            title: Text(
              DateFormatter.toDateString(orders[position].date),
            ),
            subtitle: Text(
              DateFormatter.toTimeString(orders[position].date),
            ),
            trailing: Text(orders[position].status),
          );
        },
        childCount: orders.length,
      ),
    );
  }

  Future<Order> showPurchaseTab(BuildContext context, Order selectedOrder) {
    return Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) {
          return PurchaseTab(
            client,
            selectedOrder,
            isUpdate: true,
            isPriceEditable: selectedOrder.from == client.id,
          );
        },
      ),
    );
  }

  Future<Order> showReceiptPreview(BuildContext context, Order selectedOrder) {
    return Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) {
          return ReceiptPreview(
            order: selectedOrder,
            account: AccountServiceImpl.account,
          );
        },
      ),
    );
  }
}
