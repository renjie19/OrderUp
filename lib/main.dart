import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:orderupv2/pages/sign_up.dart';
import 'package:orderupv2/pages/wrapper.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:provider/provider.dart';

void main() => runApp(OrderUp());

class OrderUp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return StreamProvider<FirebaseUser>.value(
      value: AuthService().user,
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        home: Wrapper(),
        routes: {
          "/signUp" : (context) => SignUp(),
        },
      ),
    );
  }
}