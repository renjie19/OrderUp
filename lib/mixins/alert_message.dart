import 'package:flushbar/flushbar.dart';
import 'package:flutter/material.dart';

class AlertMessage{
  static show(String message, bool error, BuildContext context) {
    Flushbar(
      borderRadius: 5,
      margin: EdgeInsets.symmetric(horizontal: 8),
      backgroundColor: error ? Colors.red : Colors.green,
      duration: Duration(seconds: 3),
      flushbarPosition: FlushbarPosition.TOP,
      messageText: Text(
        message ?? '',
        style: TextStyle(
            color: Colors.white,
            fontSize: 14,
            fontWeight: FontWeight.bold
        ),
      ),
    ).show(context);
  }
}