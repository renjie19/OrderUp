import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:orderupv2/pages/login.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:provider/provider.dart';

import 'home.dart';

class Wrapper extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final user = Provider.of<FirebaseUser>(context);

    return user == null
        ? Login()
        : StreamProvider<Account>.value(
            value: AccountService(uid: user.uid).userData,
            child: Home(user.uid),
          );
  }
}
