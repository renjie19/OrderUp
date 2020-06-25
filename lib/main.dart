import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/bloc/custom_bloc_delegate.dart';
import 'package:orderupv2/pages/sign_up.dart';
import 'package:orderupv2/pages/wrapper.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:provider/provider.dart';

void main() {
  BlocSupervisor.delegate = CustomBlocDelegate();
  runApp(OrderUp());
}

class OrderUp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return StreamProvider<FirebaseUser>.value(
      value: AuthService().user,
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        initialRoute: '/',
        routes: {
          '/' : (context) => Wrapper(),
          '/signUp' : (context) => SignUp(),
        },
      ),
    );
  }
}