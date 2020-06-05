import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/components/create_item_tab.dart';
import 'package:orderupv2/components/custom_alert_dialog.dart';
import 'package:orderupv2/components/order_info_card.dart';
import 'package:orderupv2/mixins/alert_message.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/client_service.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:progress_dialog/progress_dialog.dart';

class PurchaseTab extends StatefulWidget {
  final Client client;
  final Order order;
  final bool isUpdate;
  final bool isPriceEditable;

  PurchaseTab(this.client, this.order,
      {@required this.isUpdate, @required this.isPriceEditable});

  @override
  _PurchaseTabState createState() => _PurchaseTabState();
}

class _PurchaseTabState extends State<PurchaseTab> {
  bool isLoading = false;
  Order order;
  ProgressDialog progressDialog;
  final AccountService _accountService = AccountServiceImpl();

  @override
  Widget build(BuildContext context) {
    // todo move to a single method
    order = widget.order ?? Order();
    order.items = order.items ?? [];
    order.total = order.total ?? 0;
    progressDialog = initProgressDialog();

    var isForPayment = order.forPayment ?? false;

    return Scaffold(
      backgroundColor: primaryColor,
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
      floatingActionButton: FloatingActionButton(
        elevation: 0,
        shape: CircleBorder(side: BorderSide(width: 3, color: primaryColor)),
        tooltip: isForPayment ? 'Paid' : 'Add Item',
        splashColor: primaryColor,
        backgroundColor: Colors.white,
        child: Icon(
          isForPayment ? Feather.dollar_sign : Icons.add,
          size: 20,
          color: primaryColor,
        ),
        onPressed: () {
          // show dialog create item dialog
          isForPayment ? updateOrderToPaid() : showCreateSheet(Item());
        },
      ),
      bottomNavigationBar: BottomAppBar(
        // bottom options
        color: highlightColor,
        child: Row(
          children: <Widget>[
            Expanded(
              child: FlatButton(
                padding: EdgeInsets.symmetric(vertical: 12),
                onPressed: () {
                  Navigator.pop(context);
                },
                child: Icon(
                  Feather.x,
                  size: 28,
                  color: Colors.white,
                ),
              ),
            ),
            Expanded(
              child: FlatButton(
                disabledColor: Colors.grey,
                padding: EdgeInsets.symmetric(vertical: 12),
                onPressed:
                    order.items.length <= 0 ? null : () => showAlertDialog(),
                child: Icon(
                  Icons.send,
                  size: 28,
                  color: Colors.white,
                ),
              ),
            ),
          ],
        ),
      ),
      body: SafeArea(
        child: Column(
          children: <Widget>[
            Container(
              child: !widget.isUpdate
                  ? null
                  : OrderInfoCard(
                      orderId: order.id,
                      date: DateFormatter.toDateString(order.date),
                      time: DateFormatter.toTimeString(order.date),
                      status: order.status,
                      total: '${order.total}',
                    ),
            ),
            Expanded(
              child: ListView.separated(
                padding: EdgeInsets.all(8),
                itemBuilder: (context, position) {
                  var item = order.items[position];
                  return Card(
                    shape: RoundedRectangleBorder(),
                    margin: EdgeInsets.symmetric(vertical: 0),
                    child: InkWell(
                      onTap: () => showCreateSheet(item),
                      child: Container(
                        padding: EdgeInsets.symmetric(horizontal: 10),
                        child: Row(
                          children: <Widget>[
                            Expanded(flex: 2, child: Text(item.name)),
                            Icon(Feather.dollar_sign),
                            Expanded(flex: 1, child: Text('${item.price}')),
                            IconButton(
                              icon: Icon(Feather.minus),
                              iconSize: 18,
                              color: Colors.red[700],
                              onPressed: () => setState(() {
                                item.quantity > 0
                                    ? item.quantity -= 1
                                    : item.quantity = 0;
                                if (item.quantity <= 0 && !widget.isUpdate) {
                                  order.items.remove(item);
                                }
                              }),
                            ),
                            Text('${item.quantity}'),
                            IconButton(
                              icon: Icon(Feather.plus),
                              onPressed: () =>
                                  setState(() => item.quantity += 1),
                              iconSize: 18,
                              color: Colors.red[700],
                            ),
                            Text('${item.package}'),
                          ],
                        ),
                      ),
                    ),
                  );
                },
                separatorBuilder: (context, position) => Divider(
                  height: 1,
                  thickness: 1,
                  color: Colors.black,
                ),
                itemCount: order.items.length,
              ),
            ),
          ],
        ),
      ),
    );
  }

  void sendOrder(Order order) async {
    progressDialog.show();
    progressDialog.update(message: 'Creating your order');
    Order result = widget.isUpdate
        ? await OrderService().update(order)
        : await OrderService().create(order);

    if (result != null) {
      progressDialog.update(message: 'Sending to client');
      await _accountService.addToOrderList(result.id);
      progressDialog.update(message: 'Finishing up');
      await ClientService().addClientOrders(result.id, result.to);
      progressDialog.hide();
      Navigator.pop(context, result);
      AlertMessage.show('Success', 'Order Sent', false, context);
    }
    progressDialog.hide();
  }

  void showCreateSheet(Item selectedItem) async {
    Item item = selectedItem == null ? Item() : selectedItem;
    var itemIndex = order.items.indexOf(item);
    Item result = await showModalBottomSheet(
        context: context,
        builder: (context) {
          return Container(
            padding: EdgeInsets.all(10),
            child: CreateItemTab(
              item: item,
              onContinue: () => showCreateSheet(
                itemIndex < 0 ? Item() : order.items[itemIndex + 1] ?? Item(),
              ),
              editablePrice: widget.isPriceEditable,
            ),
          );
        });

    if (result != null) {
      if (order.items == null) {
        order.items = List<Item>();
      }
      setState(() {
        itemIndex < 0
            ? order.items.add(result)
            : order.items[itemIndex] = result;
        order.total = 0;
        order.items.forEach((element) => order.total += element.price);
      });
    }
  }

  Order buildOrder(Order order) {
    Order newOrder = Order();
    newOrder.id = order.id ?? OrderService.generateOrderId();
    newOrder.from = order.from ?? AccountServiceImpl.account.id;
    newOrder.to = order.to ?? widget.client.id;
    newOrder.date = order.date ?? DateTime.now().millisecondsSinceEpoch;
    newOrder.forPayment = order.forPayment ?? false;
    newOrder.items = order.items;
    newOrder.total = 0;
    newOrder.items.forEach((item) => newOrder.total += item.price);
    newOrder.status = newOrder.total == 0
        ? StatusConstant.PENDING
        : StatusConstant.FOR_DELIVERY;
    newOrder.forPayment = newOrder.status == StatusConstant.FOR_DELIVERY;
    return newOrder;
  }

  ProgressDialog initProgressDialog() {
    ProgressDialog dialog = ProgressDialog(
      context,
      type: ProgressDialogType.Normal,
      isDismissible: true,
    );
    dialog.style(
      progressWidget: SpinKitWave(
        color: Colors.blue,
        size: 25,
      ),
      message: 'Please Wait',
    );
    return dialog;
  }

  void updateOrderToPaid() {
    Order paidOrder = buildOrder(order);
    paidOrder.status = StatusConstant.PAID;
    paidOrder.forPayment = true;
    sendOrder(paidOrder);
  }

  showAlertDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => CustomAlertDialog(
          title: Text('Finalizing Order'),
          content: Text(
              'Are you sure with your order? \nYou can\'t modify once status is changed',
              textAlign: TextAlign.center),
          onNo: () => Navigator.pop(context),
          onYes: () {
            Navigator.pop(context);
            sendOrder(buildOrder(order));
          }),
    );
  }
}
