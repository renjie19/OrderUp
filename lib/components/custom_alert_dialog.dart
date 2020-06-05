import 'package:flutter/material.dart';
import 'package:orderupv2/services/auth_service.dart';

class CustomAlertDialog extends StatelessWidget {
  final Widget title;
  final Widget content;
  final Function onYes;
  final Function onNo;

  CustomAlertDialog({
    @required this.title,
    @required this.content,
    @required this.onYes,
    @required this.onNo,
  });

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Center(
        child: title,
      ),
      content: content,
      actions: <Widget>[
        Column(
          children: <Widget>[
            Container(
              width: double.maxFinite,
              child: FlatButton(
                onPressed: () => onYes(),
                child: Text(
                  'Yes',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                ),
              ),
            ),
            Container(
              width: double.maxFinite,
              child: FlatButton(
                onPressed: () => onNo(),
                child: Text(
                  'No',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                ),
              ),
            ),
          ],
        )
      ],
    );
  }
}
