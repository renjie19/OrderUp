import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/event/purchase_event.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/client_service.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';

class PurchaseBloc extends Bloc<PurchaseEvent, Order> {
  Order order;
  OrderService _orderService = OrderService();
  AccountService _accountService = AccountServiceImpl();
  ClientService _clientService = ClientService();

  PurchaseBloc({this.order});

  @override
  Order get initialState => order ?? _getNewOrder();

  @override
  Stream<Order> mapEventToState(PurchaseEvent event) async* {
    Order newState = state;
    switch (event.runtimeType) {
      case PurchaseItemCreate:
        yield _addItem(newState, event);
        break;
      case PurchaseItemUpdate:
        yield _updateItem(newState, event);
        break;
      case PurchaseItemDelete:
        yield _deleteItem(newState, event);
        break;
      case PurchaseSendOrder:
        yield await _sendOrder(newState, event);
        break;
    }
  }

  Order _deleteItem(Order newState, PurchaseItemDelete event) {
    newState.items.removeAt(event.index);
    _updateTotal(newState);
    return _mapToNewObject(newState);
  }

  Order _updateItem(Order newState, PurchaseItemUpdate event) {
    var index = newState.items.indexOf(event.item);
    if (index >= 0) {
      newState.items[index] = Item(name: event.item.name, package: event.item.package, quantity: event.item.quantity, price: event.item.price);
    }
    _updateTotal(newState);
    return _mapToNewObject(newState);
  }

  Order _addItem(Order newState, PurchaseItemCreate event) {
    newState.items == null
        ? newState.items = []
        : newState.items = newState.items;
    newState.items.add(event.item);
    _updateTotal(newState);
    return _mapToNewObject(newState);
  }

  Future _sendOrder(Order newState, PurchaseSendOrder event) async {
    newState = event.isUpdate
        ? await _orderService.update(event.order)
        : await _orderService.create(event.order);
    if(newState != null) {
      await _accountService.addToOrderList(newState.id);
      await _clientService.addClientOrders(newState.id, newState.to);
      event.onComplete(newState);
    }
    return _mapToNewObject(newState);
  }

  void _updateTotal(Order newState) {
    newState.total = 0;
    newState.items.forEach((item) => newState.total += item.price);
  }

  _getNewOrder() {
    Order order = Order();
    order.items = [];
    return order;
  }

  _mapToNewObject(Order newState) {
    Order order = Order();
    order.items = newState.items;
    order.total = newState.total;
    order.to = newState.to;
    order.from = newState.from;
    order.status = newState.status;
    order.id = newState.id;
    order.forPayment = newState.forPayment;
    order.date = newState.date;
    return order;
  }
}
