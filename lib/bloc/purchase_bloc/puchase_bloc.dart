import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/event/bloc_event.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';

class PurchaseBloc extends Bloc<BlocEvent<List<Item>>, List<Item>> {

  String orderId;
  String clientId;
  Order _order;
  
  PurchaseBloc(this.orderId,this.clientId);

  @override
  List<Item> get initialState => getOrderItems(orderId);

  @override
  Stream<List<Item>> mapEventToState(BlocEvent<List<Item>> event) async* {
    var list = state;
    switch(event.getEvent()) {
      case Event.ADD:
        list.addAll(event.getData());
        yield list;
        break;
      case Event.UPDATE:
        for(Item item in event.getData()) {
          int index = list.indexOf(item);
          if(index >= 0) {
            list[index] = item;
          }
        }
        yield list;
        break;
      case Event.DELETE:
        for(Item item in event.getData()) {
          list.remove(item);
        }
        yield list;
        break;
      case Event.SET:
        yield event.getData();
        break;
    }
  }

  List<Item> getOrderItems(String orderId) {
    var result;
    if(orderId != null && orderId.isNotEmpty) {
      result = AccountServiceImpl.account.orders.firstWhere((order) => order.id == orderId, orElse: () => null);
      if(result != null) {
        this._order = result;
        return result.items;
      }
    }
    this._order = result;
    return [];
  }

  Client getClientInfo() {
    return AccountServiceImpl.account.clients.firstWhere((client) => client.id == clientId, orElse: () => null);
  }

  Order getOrder() {
    if(this._order == null) {
      this._order = Order();
      _order.id = OrderService.generateOrderId();
      _order.date = DateTime.now().millisecondsSinceEpoch;
      _order.from = AccountServiceImpl.account.id;
      _order.to = this.clientId;
      _order.items = state;
      _order.status = StatusConstant.PENDING;
      _order.total = state.fold(0.0, (previousValue, element) => previousValue + element.price);
    }
    return this._order;
  }

  bool isForPayment() {
    var order = getOrder();
    return order.from == this.clientId && order.status == StatusConstant.FOR_DELIVERY;
  }

  bool isForSendingOrder() {
    var order = getOrder();
    return order.status == StatusConstant.PENDING;
  }



}