import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/bloc/order_list_bloc.dart';
import 'package:orderupv2/bloc/purchase_bloc.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/pages/purchase_tab.dart';
import 'package:orderupv2/pages/receipt_preview.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/custom_callback.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderListView extends StatefulWidget {
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
  _OrderListViewState createState() => _OrderListViewState();
}

class _OrderListViewState extends State<OrderListView> {
  OrderListBloc orderListBloc;

  @override
  void dispose() {
    super.dispose();
    orderListBloc.close();
  }


  @override
  void initState() {
      super.initState();
      orderListBloc = BlocProvider.of<OrderListBloc>(context);
  }

  @override
  Widget build(BuildContext context) {
    return SliverList(
      delegate: SliverChildBuilderDelegate(
        (context, position) {
          return ListTile(
            onTap: () async {
              await onOrderSelect(position, context);
            },
            leading: Icon(
              widget.iconData,
              color: highlightColor[900],
            ),
            title: Text(
              DateFormatter.toDateString(widget.orders[position].date),
            ),
            subtitle: Text(
              DateFormatter.toTimeString(widget.orders[position].date),
            ),
            trailing: Text(widget.orders[position].status),
          );
        },
        childCount: widget.orders.length,
      ),
    );
  }

  Future onOrderSelect(int position, BuildContext context) async {
    var selectedOrder = widget.orders[position];
    var isFromCurrentUser = selectedOrder.from == widget.client.id;
    var isStillPending = selectedOrder.status == StatusConstant.PENDING;
    var isPaid = selectedOrder.status == StatusConstant.PAID;
    Order order = (isFromCurrentUser || isStillPending) && !isPaid
        ? await showPurchaseTab(context, selectedOrder)
        : await showReceiptPreview(context, selectedOrder);

    /// called to return the updated object to the Order list page
    if (order != null) {
      widget.callBack.run(order);
    }
  }

  Future<Order> showPurchaseTab(BuildContext context, Order selectedOrder) {
    return Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) {
          return MultiBlocProvider(
            providers: [
              BlocProvider<OrderListBloc>(create: (context) => orderListBloc),
              BlocProvider<PurchaseBloc>(create: (context) => PurchaseBloc(order: selectedOrder)),
            ],
            child: PurchaseTab(
              widget.client,
              selectedOrder,
              isUpdate: true,
              isPriceEditable: selectedOrder.from == widget.client.id,
            ),
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
