import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/bloc/purchase_bloc/PurchaseEvent.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';

class PurchaseBloc extends Bloc<PurchaseEvent, List<Item>> {

  String orderId;
  String clientId;
  
  PurchaseBloc(this.orderId,this.clientId);

  @override
  List<Item> get initialState => getOrderItems(orderId);

  @override
  Stream<List<Item>> mapEventToState(PurchaseEvent event) async* {
    var list = state;
    if(event is PurchaseEventAdd) {
      list.add(event.item);
      yield list;
    } else if(event is PurchaseEventUpdate) {
      int index = list.indexOf(event.item);
      if(index >= 0) {
        list[index] = event.item;
      }
      yield list;
    } else if(event is PurchaseEventDelete) {
      list.remove(event.item);
      yield list;
    } else {
      yield (event as PurchaseEventSet).items;
    }
  }

  List<Item> getOrderItems(String orderId) {
    if(orderId != null && orderId.isNotEmpty) {
      return AccountServiceImpl.account.orders.firstWhere((order) => order.id == orderId, orElse: () => Order()).items;
    }
    return [];
  }

  Client getClientInfo() {
    return AccountServiceImpl.account.clients.firstWhere((client) => client.id == clientId, orElse: () => null);
  }

}