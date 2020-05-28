import 'package:flutter/material.dart';
import 'package:orderupv2/pages/login.dart';
import 'package:orderupv2/pages/sign_up.dart';

void main() {
  runApp(MaterialApp(
    debugShowCheckedModeBanner: false,
    home: Login(),
    routes: {
    "/signUp" : (context) => SignUp(),
    },
  ));
}