import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/bloc/account_bloc.dart';
import 'package:orderupv2/components/account_form.dart';
import 'package:orderupv2/components/info_card.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/custom_callback.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AccountManagement extends StatefulWidget {

  @override
  _AccountManagementState createState() => _AccountManagementState();

  AccountManagement();
}

class _AccountManagementState extends State<AccountManagement> implements CustomCallBack{
  SharedPreferences preferences;
  bool status = false;
  AccountBloc bloc;

  _AccountManagementState() {
    _initSharedPreferences();
  }

  @override
  void initState() {
    super.initState();
    bloc = BlocProvider.of<AccountBloc>(context);
  }

  @override
  Widget build(BuildContext context) {
    return BlocBuilder(
      bloc: bloc,
      builder: (context, account) {
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
                          InfoCard(
                            label: 'First Name',
                            info: account.firstName,
                            padding: EdgeInsets.only(bottom: 10),
                          ),
                          InfoCard(
                            label: 'Last Name',
                            info: account.lastName,
                            padding: EdgeInsets.only(bottom: 10),
                          ),
                          InfoCard(
                            label: 'Location',
                            info: account.location,
                            padding: EdgeInsets.only(bottom: 10),
                          ),
                          InfoCard(
                            label: 'Contact No.',
                            info: account.contactNo,
                            padding: EdgeInsets.only(bottom: 10),
                          ),
                          InfoCard(
                            label: 'Email',
                            info: account.email,
                            padding: EdgeInsets.only(bottom: 10),
                          ),
                          Row(
                            children: <Widget>[
                              Switch.adaptive(
                                value: status,
                                onChanged: (value) {
                                  preferences.setBool('status', value);
                                  setState(() => status = value);
                                },
                                activeColor: highlightColor,
                              ),
                              Text('Offline',style: TextStyle(fontSize: 18,fontWeight: FontWeight.bold,),)
                            ],
                          ),
                          SizedBox(height: 40),
                          FlatButton(
                            color: Colors.white,
                            child: Column(
                              children: <Widget>[
                                Icon(
                                  Icons.edit,
                                  size: 28,
                                ),
                                Text('Edit',)
                              ],
                            ),
                            onPressed: () => _showBottomBar(account),
                          )
                        ],
                      ))
                ],
              ),
            ),
          ),
        );
      },
    );
  }

  _showBottomBar(account) async {
    await showModalBottomSheet(
        context: context,
        builder: (context) {
          return AccountForm(account, bloc);
        });
  }

  void _initSharedPreferences() async {
    preferences = await SharedPreferences.getInstance();
    setState(() => status = preferences.getBool('status') ?? false);
  }

  @override
  void run(Object account) {
  }
}
