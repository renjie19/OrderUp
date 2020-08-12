import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/event/bloc_event.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderHistoryBloc extends Bloc<BlocEvent<List<Order>>, List<Order>>{

  String id;
  AccountService _accountService;
  OrderService _orderService;

  OrderHistoryBloc(this.id) {
    initStream();
  }

  @override
  List<Order> get initialState => getOrdersByClient(this.id, AccountServiceImpl.account.orders);

  @override
  Stream<List<Order>> mapEventToState (BlocEvent<List<Order>> event) async* {
    var orders = state;
    switch(event.getEvent()) {
      case Event.ADD:
        orders.addAll(event.getData());
        yield List.from(orders);
        break;
      case Event.UPDATE:
        for(Order orderData in event.getData()) {
          int index = orders.indexWhere((order) => order.id == orderData.id);
          if(index >= 0) {
            orders[index] = orderData;
          }
        }
        yield List.from(orders);
        break;
      case Event.DELETE:
        for(Order orderData in event.getData()) {
          int index = orders.indexWhere((order) => order.id == orderData.id);
          if(index >= 0) {
            orders.removeAt(index);
          }
        }
        yield List.from(orders);
        break;
      case Event.SET:
        yield event.getData();
        break;
    }
  }

  List<Order> getOrdersByClient(String id, orders) {
    return List.from(orders.where((order) => order.from == id || order.to == id));
  }

  Client getClient() {
    if(this.id != null) {
      return AccountServiceImpl.account.clients.firstWhere((client) => client.id == id, orElse: null);
    }
    return null;
  }

  void initStream() {
    _accountService = AccountServiceImpl();
    _orderService = OrderService();

    _accountService.userData.listen((account) {
      if(account != null) {
        this.add(BlocEvent(event: Event.SET, data: getOrdersByClient(id, account.orders)));
      }
    });

    _orderService.orderUpdates.listen((orders) {
      if(orders != null && orders.isNotEmpty) {
        for(Order order in orders) {
          int index = AccountServiceImpl.account.orders.indexWhere((element) => element.id == order.id);
          if(index >= 0) {
            AccountServiceImpl.account.orders[index] = orders[0];
          }
        }
        this.add(BlocEvent(event: Event.UPDATE, data: orders));
      }
    });
  }

}