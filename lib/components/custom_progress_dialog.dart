import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:progress_dialog/progress_dialog.dart';

class CustomProgressDialog {
  ProgressDialog _dialog;
  String _message = 'Please Wait';
  CustomProgressDialog(BuildContext context){
    _dialog = ProgressDialog(
      context,
      type: ProgressDialogType.Normal,
      isDismissible: false,
    );

    _dialog.style(
      progressWidget: SpinKitWave(
        color: Colors.blue,
        size: 25,
      ),
      message: _message,
    );
  }

  void show({String message}) {
    _message = message ?? _message;
    _dialog.show();
  }

  void update({String message}) {
    _dialog.update(message: message ?? _message);
  }
}