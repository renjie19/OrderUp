import 'package:flutter/material.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/client.dart';

class OrderInfoCard extends StatelessWidget {
  final Client client;
  final Account account = AccountService.account;

  OrderInfoCard(this.client);

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.white,
      margin: EdgeInsets.fromLTRB(0, 0, 0, 5),
      child: Container(
        width: double.maxFinite,
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
                  Text(
                      'Date \n${DateFormatter.toDateString(DateTime.now().millisecondsSinceEpoch)}'),
                  SizedBox(height: 10,),

                  // todo add a time getter from millis
                  Text('Time\n 03:00 PM')
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
