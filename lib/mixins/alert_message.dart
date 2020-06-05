import 'package:flushbar/flushbar.dart';
import 'package:flutter/material.dart';

class AlertMessage{
  static show(String title, String message, bool error, BuildContext context) {
    Flushbar(
      borderRadius: 5,
      padding: EdgeInsets.all(5),
      margin: EdgeInsets.all(5),
      backgroundColor: error ? Colors.redAccent : Colors.green,
      duration: Duration(seconds: 3),
      flushbarPosition: FlushbarPosition.TOP,
      titleText: Center(
        child: Text(
          title ?? '',
          style: TextStyle(
            color: Colors.white,
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      messageText: Center(
        child: Text(
          message ?? '',
          style: TextStyle(
              color: Colors.white,
              fontSize: 14,
              fontWeight: FontWeight.bold
          ),
        ),
      ),
    ).show(context);
  }
}