import 'package:flutter/cupertino.dart';
import 'package:orderupv2/shared/models/item.dart';

abstract class PurchaseEvent {}

class PurchaseEventAdd extends PurchaseEvent{
  Item item;
  PurchaseEventAdd({@required this.item});
}

class PurchaseEventUpdate extends PurchaseEvent{
  Item item;
  PurchaseEventUpdate({@required this.item});
}

class PurchaseEventDelete extends PurchaseEvent{
  Item item;
  PurchaseEventDelete({@required this.item});
}

class PurchaseEventSet extends PurchaseEvent{
  List<Item> items;
  PurchaseEventSet({@required this.items});
}

