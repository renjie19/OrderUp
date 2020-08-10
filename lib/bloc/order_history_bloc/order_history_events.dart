import 'package:orderupv2/shared/models/order.dart';

abstract class OrderHistoryEvents {}

class OrderHistoryAdd extends OrderHistoryEvents {
  Order order;
}

class OrderHistoryUpdate extends OrderHistoryEvents {
  Order order;
}

class OrderHistoryDelete extends OrderHistoryEvents {
  Order order;
}

class OrderHistorySet extends OrderHistoryEvents {
  List<Order> orders;
}