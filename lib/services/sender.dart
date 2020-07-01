import 'dart:convert';

import 'package:orderupv2/event/purchase_event.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/client_service.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:sms_maintained/sms.dart';

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

    if(client != null) {
      String message = jsonEncode(client);

      SimCardsProvider provider = SimCardsProvider();
      var sim = await provider.getSimCards();
      SmsMessage smsMessage = SmsMessage(client.contactNo, message);
      smsMessage.onStateChanged.listen((smsState) {
        switch(smsState) {
          case SmsMessageState.Fail:
            event.onFail('Sending Failed');
            break;
          case SmsMessageState.Sent:
            event.isUpdate
                ? orderService.update(event.order)
                : orderService.create(event.order);
            accountService.addToOrderList(order.id);
            clientService.addClientOrders(order.id, order.to);
            event.onComplete(order);
            event.onComplete(client.orders.last);
            break;
          default:
            break;
        }
      });
      SmsSender().sendSms(smsMessage, simCard: sim[0]);
    } else {
      event.onFail('Sending Failed. Client does not exist in your list.');
    }
  }
}
