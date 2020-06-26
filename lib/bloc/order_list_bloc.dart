import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/event/order_list_event.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderListBloc extends Bloc<OrderListEvent, List<Order>> {
  final OrderService _orderService = OrderService();

  @override
  List<Order> get initialState => AccountServiceImpl.account.orders;

  @override
  Stream<List<Order>> mapEventToState(OrderListEvent orderEvent) async* {
    switch(orderEvent.runtimeType) {
      case OrderListAdd:
        OrderListAdd event = orderEvent;
        var index = state.indexOf(state.firstWhere((order) => order.id == event.order.id, orElse: () => null));
        if(index < 0) {
          state.add(event.order);
          yield List.from(state);
        }
        break;
      case OrderListUpdate:
        OrderListUpdate event = orderEvent;
        var index = state.indexOf(state.firstWhere((order) => order.id == event.order.id, orElse: () => null));
        if(index >= 0) {
          state[index] = event.order;
          AccountServiceImpl.account.orders[index] = event.order;
          yield List.from(state);
        }
        break;
      case OrderListDelete:
        OrderListDelete event = orderEvent;
        state.remove(event.order);
        yield List.from(state);
        break;
      case OrderListSet:
        OrderListSet event = orderEvent;
        yield List.from(event.orders);
        break;
    }
  }

  List<Order> get currentState {
    return state;
  }

  Stream<List<Order>> get orderUpdates {
    return _orderService.orderUpdates;
  }
  
  
}