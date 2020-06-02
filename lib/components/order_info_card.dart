import 'package:flutter/material.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/services/order_service.dart';

class OrderInfoCard extends StatelessWidget {
  final String orderId;

  OrderInfoCard({this.orderId});

  @override
  Widget build(BuildContext context) {
    String id = orderId;
    if(orderId == null) {
      id = OrderService.generateOrderId();
    }

    return Container(
      width: double.maxFinite,
      padding: EdgeInsets.all(8),
      color: Colors.white,
      child: Column(
        children: <Widget>[
          Text(
            'Order Info',
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 20,
            ),
          ),
          SizedBox(height: 8),
          Container(
            padding: EdgeInsets.all(8),
            alignment: Alignment.topLeft,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Text('Order Id: $id'),
                Text(
                    'Date: ${DateFormatter.toDateString(DateTime.now().millisecondsSinceEpoch)}'),
                // todo add a time getter from millis
                Text('Time: 03:00 PM')
              ],
            ),
          ),
        ],
      ),
    );
  }
}
