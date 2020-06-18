import 'package:orderupv2/shared/models/order.dart';

abstract class OrderServic {

  //create
  Future create(Order order);

  //update
  Future update(Order order);

  // get orders by id list
  Future<List<Order>> orders();

  // order stream
  Stream<List<Order>> get orderUpdates;

}