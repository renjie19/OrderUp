import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/bloc/order_history_bloc/order_history_events.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderHistoryBloc extends Bloc<OrderHistoryEvents, List<Order>>{

  String id;

  OrderHistoryBloc(this.id);

  @override
  List<Order> get initialState => getOrdersById(this.id, AccountServiceImpl.account.orders);

  @override
  Stream<List<Order>> mapEventToState (OrderHistoryEvents event) async* {
    var orders = state;
    if ( event is OrderHistoryAdd ) {
      orders.add(event.order);
      yield orders;
    } else if ( event is OrderHistoryUpdate ) {
      int index = orders.indexWhere((order) => order.id == event.order.id);
      if(index >= 0) {
        orders[index] = event.order;
        yield orders;
      }
    } else if ( event is OrderHistoryDelete ) {
      int index = orders.indexWhere((order) => order.id == event.order.id);
      if(index >= 0) {
        orders.removeAt(index);
        yield orders;
      }
    } else if ( event is OrderHistorySet ) {
      if(event.orders != null) {
        yield event.orders;
      }
    }
  }

  List<Order> getOrdersById(String id, orders) {
    return List.from(orders.where((order) => order.from == id || order.to == id));
  }

  Client getClient() {
    if(this.id != null) {
      return AccountServiceImpl.account.clients.firstWhere((client) => client.id == id, orElse: null);
    }
    return null;
  }

}