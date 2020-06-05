import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';

class OrderInfoCard extends StatelessWidget {
  final String orderId;
  final String date;
  final String time;
  final String status;
  final String total;

  OrderInfoCard({
    @required this.orderId,
    @required this.date,
    @required this.time,
    @required this.status,
    @required this.total
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Container(
        padding: EdgeInsets.all(10),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Center(
              child: Text('ORDER INFO',
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
            ),
            Text('Order Id'),
            Text(
              orderId,
              style: TextStyle(fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 5),
            Text('Date'),
            Text(
              date,
              style: TextStyle(fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 5),
            Text('Time'),
            Text(
              time,
              style: TextStyle(fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 5),
            SizedBox(height: 5),
            Text('Status'),
            Text(
              status,
              style: TextStyle(fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 5),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: <Widget>[
                Text('Total: '),
                Icon(Feather.dollar_sign, size: 18,),
                Text(
                  total,
                  style: TextStyle(fontWeight: FontWeight.bold),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }
}
