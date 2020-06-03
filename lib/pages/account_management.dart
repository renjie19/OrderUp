import 'package:flutter/material.dart';
import 'package:orderupv2/components/info_card.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';

class AccountManagement extends StatefulWidget {
  final Account _account;

  @override
  _AccountManagementState createState() => _AccountManagementState(_account);

  AccountManagement(this._account);
}

class _AccountManagementState extends State<AccountManagement> {
  final Account account;
  final _accountForm = GlobalKey<FormState>();

  _AccountManagementState(this.account);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        backgroundColor: primaryColor,
      ),
      body: Container(
        color: primaryColor,
        height: double.maxFinite,
        padding: EdgeInsets.symmetric(vertical: 10, horizontal: 25),
        child: SingleChildScrollView(
          child: Form(
            key: _accountForm,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                CircleAvatar(
                  radius: 70,
                  backgroundColor: Colors.white,
                  child: Image(
                    image: AssetImage('lib/assets/images/logo_no_bg.png'),
                  ),
                ),
                Container(
                  margin: EdgeInsets.only(top: 20),
                  width: double.maxFinite,
                  child: Column(
                    children: <Widget>[
                      InfoCard(label: 'First Name', info: account.firstName, padding: EdgeInsets.only(bottom: 10),),
                      InfoCard(label: 'Last Name', info: account.lastName, padding: EdgeInsets.only(bottom: 10),),
                      InfoCard(label: 'Location', info: account.location, padding: EdgeInsets.only(bottom: 10),),
                      InfoCard(label: 'Contact No.', info: account.contactNo, padding: EdgeInsets.only(bottom: 10),),
                      InfoCard(label: 'Email', info: account.email, padding: EdgeInsets.only(bottom: 10),),
                      SizedBox(height: 40),
                      FlatButton(
                        color: Colors.white,
                        child: Column(
                          children: <Widget>[
                            Icon(Icons.edit, size: 28,),
                            Text('Edit')
                          ],
                        ),
                        onPressed: (){
                          //show edit alert dialog
                        },
                      )
                    ],
                  )
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
