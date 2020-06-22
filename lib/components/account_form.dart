import 'package:flutter/material.dart';
import 'package:orderupv2/components/custom_progress_dialog.dart';
import 'package:orderupv2/mixins/alert_message.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/account_service_impl.dart';
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
  AccountService accountService;
  final accountFormKey = GlobalKey<FormState>();
  CustomProgressDialog dialog;

  @override
  void initState() {
    super.initState();
    account = widget.account;
    accountService = AccountServiceImpl();
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
                  onPressed: () async {
                    try {
                      if (accountFormKey.currentState.validate()) {
                        await accountService.update(account);
                        Navigator.pop(context, account);
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
