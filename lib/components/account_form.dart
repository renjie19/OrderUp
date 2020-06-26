import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/bloc/account_bloc.dart';
import 'package:orderupv2/components/custom_progress_dialog.dart';
import 'package:orderupv2/event/account_event.dart';
import 'package:orderupv2/mixins/alert_message.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:the_validator/the_validator.dart';

class AccountForm extends StatefulWidget {
  final Account account;

  AccountForm(this.account);

  @override
  _AccountFormState createState() => _AccountFormState();
}

class _AccountFormState extends State<AccountForm> {
  Account account;
  final accountFormKey = GlobalKey<FormState>();
  CustomProgressDialog dialog;

  @override
  void initState() {
    super.initState();
    account = widget.account;
    dialog = CustomProgressDialog(context);
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 10, vertical: 5),
      child: Form(
        key: accountFormKey,
        child: Column(
          children: <Widget>[
            TextFormField(
              initialValue: account.firstName,
              onChanged: (value) => setState(() => account.firstName = value),
              validator: FieldValidator.required(message: 'Required'),
              decoration: InputDecoration(labelText: 'First Name'),
            ),
            TextFormField(
              initialValue: account.lastName,
              onChanged: (value) => setState(() => account.lastName = value),
              validator: FieldValidator.required(message: 'Required'),
              decoration: InputDecoration(labelText: 'Last Name'),
            ),
            TextFormField(
              initialValue: account.location,
              onChanged: (value) => setState(() => account.location = value),
              validator: FieldValidator.required(message: 'Required'),
              decoration: InputDecoration(labelText: 'Location'),
            ),
            TextFormField(
              initialValue: account.contactNo,
              onChanged: (value) => setState(() => account.contactNo = value),
              validator: FieldValidator.required(message: 'Required'),
              decoration: InputDecoration(labelText: 'Contact No'),
              keyboardType: TextInputType.number,
            ),
            ButtonBar(
              children: <Widget>[
                FlatButton(
                  child: Text('CANCEL'),
                  onPressed: () => Navigator.pop(context),
                ),
                FlatButton(
                  child: Text('SAVE'),
                  color: primaryColor,
                  onPressed: () {
                    try {
                      if (accountFormKey.currentState.validate()) {
                        BlocProvider.of<AccountBloc>(context).add(AccountUpdate(account));
                        Navigator.pop(context);
                      }
                    } catch (e) {
                      AlertMessage.show(e.toString(), true, context);
                    }
                  },
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
