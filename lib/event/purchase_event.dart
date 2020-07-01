import 'package:flutter/cupertino.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';

abstract class PurchaseEvent{}

class PurchaseItemCreate extends PurchaseEvent{
  final Item item;

  PurchaseItemCreate({@required this.item});
}

class PurchaseItemUpdate extends PurchaseEvent{
  final Item item;

  PurchaseItemUpdate({@required this.item});
}

class PurchaseItemDelete extends PurchaseEvent{
  final int index;

  PurchaseItemDelete({@required this.index});
}

class PurchaseSendOrder extends PurchaseEvent {
  final Order order;
  final bool isUpdate;
  final Function onComplete;
  final Function onFail;

  PurchaseSendOrder({@required this.order,@required  this.isUpdate, this.onComplete, this.onFail});
}