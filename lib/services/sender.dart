import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:orderupv2/event/purchase_event.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/client_service.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';

abstract class Sender {
  final OrderService orderService = OrderService();
  final AccountService accountService = AccountServiceImpl();
  final ClientService clientService = ClientService();

  Future send(Order order, PurchaseSendOrder event);
}

class OnlineSender extends Sender {
  @override
  Future send(Order order, PurchaseSendOrder event) async {
    order = event.isUpdate
        ? await orderService.update(event.order)
        : await orderService.create(event.order);
    if (order != null) {
      await accountService.addToOrderList(order.id);
      await clientService.addClientOrders(order.id, order.to);
      event.onComplete(order);
    }
    return order;
  }
}

class OfflineSender extends Sender {
  @override
  Future send(Order order, PurchaseSendOrder event) async {
    order = event.order;
    order.id = OrderService.generateOrderId();

    Client client = AccountServiceImpl.account.clients
        .firstWhere((client) => client.id == order.to, orElse: () => null);
    client.orders = [order];

    try {
      if(client != null) {
        String message = jsonEncode(client);

        String result = await MethodChannel("sendSms")
            .invokeMethod("send", <String, Object>{"phone":client.contactNo, "msg":message});
        event.onComplete(order);
      } else {
        event.onFail('Sending Failed. Client does not exist in your list.');
      }
    } on PlatformException catch (e) {
      if(event != null) {
        event.onFail(e.message);
      }
    } catch(ex) {
      if(event != null) {
        event.onFail(ex.toString());
      }
    }
  }
}
