import 'package:flutter/material.dart';
import 'package:orderupv2/components/order_info_card.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';

class ReceiptPreview extends StatelessWidget {
  final Order order;
  final Account account;

  ReceiptPreview({this.order, this.account});

  @override
  Widget build(BuildContext context) {
    Client client = account.clients
        .where((client) => client.id == order.to || client.id == order.from)
        .first;

    return Scaffold(
      backgroundColor: primaryColor[700],
      appBar: AppBar(backgroundColor: primaryColor[700], elevation: 0,),
      body: SafeArea(
        child: Container(
          padding: EdgeInsets.symmetric(horizontal: 10),
          child: Card(
            child: Container(
              padding: EdgeInsets.symmetric(horizontal: 10),
              child: Column(
                children: <Widget>[
                  Container(
                    padding: EdgeInsets.all(5),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: <Widget>[
                        Center(child: Text('Receipt Info', style: _getTextStyle().copyWith(fontSize: 18.0),)),
                        SizedBox(height: 5),
                        Row(
                          children: <Widget>[
                            Text('Ordered To: '),
                            Text(
                              order.to == account.id
                                  ? '${account.firstName} ${account.lastName}'
                                  : '${client.firstName} ${client.lastName}',
                              style: _getTextStyle(),
                            ),
                          ],
                        ),
                        SizedBox(height: 5),
                        Row(
                          children: <Widget>[
                            Text('Delivery For: '),
                            Text(
                              order.from == account.id
                                  ? '${account.firstName} ${account.lastName}'
                                  : '${client.firstName} ${client.lastName}',
                              style: _getTextStyle(),
                            ),
                          ],
                        ),
                        SizedBox(height: 5),
                        Row(
                          children: <Widget>[
                            Text('Deliver to location: '),
                            Text(
                              order.from == account.id
                                  ? '${account.location}'
                                  : '${client.location}',
                              style: _getTextStyle(),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                  Divider(color: Colors.black),
                  OrderInfoCard(
                    orderId: order.id,
                    date: DateFormatter.toDateString(order.date),
                    time: DateFormatter.toTimeString(order.date),
                    total: '${order.total}',
                    status: order.status,
                  ),
                  Divider(color: Colors.black),
                  Text('Items', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),),
                  Expanded(
                    child: ListView.builder(
                      itemCount: order.items.length,
                      itemBuilder: (context, position) {
                        var item = order.items[position];
                        return Container(
                          margin: EdgeInsets.symmetric(horizontal: 5),
                          color: Colors.white,
                          padding: EdgeInsets.all(10),
                          child: Row(
                            children: <Widget>[
                              Expanded(
                                flex: 2,
                                child: Text(item.name),
                              ),
                              Text('${item.quantity}'),
                              Text(item.package),
                              Expanded(
                                  flex: 1,
                                  child: Center(child: Text('${item.price}')))
                            ],
                          ),
                        );
                      },
                    ),
                  ),
                  SizedBox(height: 10)
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  _getTextStyle() {
    return TextStyle(
      fontSize: 16,
      fontWeight: FontWeight.bold,
    );
  }
}
