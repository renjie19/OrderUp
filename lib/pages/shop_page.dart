import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/components/item_create.dart';
import 'package:orderupv2/components/order_info_card.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/client_service.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:progress_dialog/progress_dialog.dart';

class ShopPage extends StatefulWidget {
  final Client client;
  final Order order;
  final bool isUpdate;

  ShopPage(this.client, this.order, {@required this.isUpdate});

  @override
  _ShopPageState createState() => _ShopPageState();
}

class _ShopPageState extends State<ShopPage> {
  bool isLoading = false;
  Order order;
  ProgressDialog progressDialog;
  AccountService accountService;

  @override
  Widget build(BuildContext context) {
    // todo move to a single method
    accountService = AccountService();
    order = widget.order ?? Order();
    order.items = order.items ?? [];
    order.total = order.total ?? 0;
    progressDialog = initProgressDialog();

    return Scaffold(
      backgroundColor: primaryColor,
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
      floatingActionButton: FloatingActionButton(
        elevation: 0,
        shape: CircleBorder(side: BorderSide(width: 3, color: primaryColor)),
        tooltip: 'Add Item',
        splashColor: primaryColor,
        backgroundColor: Colors.white,
        child: Icon(
          Icons.add,
          size: 20,
          color: primaryColor,
        ),
        onPressed: () {
          // show dialog create item dialog
          showCreateSheet(Item());
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
                    order.items.length <= 0 ? null : () => sendOrder(order),
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
                      )),
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
                              onPressed: () => setState(() {
                                item.quantity > 0
                                    ? item.quantity -= 1
                                    : item.quantity = 0;
                                if (item.quantity <= 0 && !widget.isUpdate) {
                                  order.items.remove(item);
                                }
                              }),
                              iconSize: 18,
                              color: Colors.red[700],
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
    order = buildOrder(order);
    progressDialog.update(message: 'Creating your order');
    //todo change to update if for update
    Order result = widget.isUpdate ? await OrderService().update(order) : await OrderService().create(order) ;
    if (result != null) {
      progressDialog.update(message: 'Sending to client');
      await accountService.addToOrderList(result.id);
      progressDialog.update(message: 'Finishing up');
      await ClientService().addClientOrders(result.id, result.to);
      progressDialog.hide();
      Navigator.pop(context, result);
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
            child: ItemCreate(item, () => showCreateSheet(itemIndex == null ? Item() : order.items[itemIndex + 1] ?? Item()),
                widget.order.status == StatusConstant.PENDING),
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
    newOrder.from = order.from ?? AccountService.account.id;
    newOrder.to = order.to ?? widget.client.id;
    newOrder.date = order.date ?? DateTime.now().millisecondsSinceEpoch;
    newOrder.forPayment = order.forPayment ?? false;
    newOrder.items = order.items;
    newOrder.total = 0;
    newOrder.items.forEach((item) => newOrder.total += item.price);
    newOrder.status = newOrder.total == 0
        ? StatusConstant.PENDING
        : StatusConstant.FOR_DELIVERY;
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
}
