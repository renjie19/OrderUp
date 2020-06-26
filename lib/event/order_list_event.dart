import 'package:orderupv2/shared/models/order.dart';

abstract class OrderListEvent{}

class OrderListAdd extends OrderListEvent{
  final Order order;
  OrderListAdd(this.order);
}
class OrderListUpdate  extends OrderListEvent{
  final Order order;
  OrderListUpdate(this.order);
}
class OrderListDelete  extends OrderListEvent{
  final Order order;
  OrderListDelete(this.order);
}
class OrderListSet  extends OrderListEvent{
  final List<Order> orders;
  OrderListSet(this.orders);
}