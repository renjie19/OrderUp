import 'package:flutter/material.dart';
import 'package:orderupv2/services/auth_service.dart';

class LogOutDialog extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return AlertDialog(
        title: Text(
          'Log Out',
          textAlign: TextAlign.center,
        ),
        content: Text(
          'Are you sure to log out?',
          textAlign: TextAlign.center,
        ),
        actions: <Widget>[
          Column(
            children: <Widget>[
              Container(
                width: double.maxFinite,
                child: FlatButton(
                  onPressed: () {
                    Navigator.pop(context);
                    AuthService().signOut();
                  },
                  child: Text(
                    'Yes',
                    style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold
                    ),
                  ),
                ),
              ),
              Container(
                width: double.maxFinite,
                child: FlatButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: Text(
                    'No',
                    style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold
                    ),
                  ),
                ),
              ),
            ],
          )
        ],
    );
  }
}
