import 'package:orderupv2/shared/models/order.dart';

abstract class OrderHistoryEvents {}

class OrderHistoryAdd extends OrderHistoryEvents {
  Order order;
  OrderHistoryAdd(this.order);
}

class OrderHistoryUpdate extends OrderHistoryEvents {
  Order order;
  OrderHistoryUpdate(this.order);
}

class OrderHistoryDelete extends OrderHistoryEvents {
  Order order;
  OrderHistoryDelete(this.order);
}

class OrderHistorySet extends OrderHistoryEvents {
  List<Order> orders;
  OrderHistorySet(this.orders);
}