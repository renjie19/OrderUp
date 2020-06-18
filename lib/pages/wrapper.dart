import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:orderupv2/pages/login.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/order.dart';
import 'package:provider/provider.dart';

import 'home.dart';

class Wrapper extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final user = Provider.of<FirebaseUser>(context);

    return user == null
        ? Login()
        : StreamProvider<Account>.value(
            /// required as need to initialize id for logged in user
            value: AccountServiceImpl(id: user.uid).userData,
            child: Home(),
          );
  }
}
