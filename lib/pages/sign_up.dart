import 'package:flutter/material.dart';
import 'package:orderupv2/shared/constants.dart';

class SignUp extends StatefulWidget {
  @override
  _SignUpState createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
  final _signUpKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blue,
      body: Container(
        padding: EdgeInsets.fromLTRB(20, 50, 20, 10),
        child: SingleChildScrollView(
          child: Form(
            key: _signUpKey,
            child: Column(
              children: <Widget>[
                TextFormField(
                  onChanged: (value) {},
                  validator: (value) {
                    return validateIfEmpty(value);
                  },
                  decoration: textInputDecoration.copyWith(hintText: "First Name"),
                ),
                SizedBox(height: 10),
                TextFormField(
                  onChanged: (value) {},
                  validator: (value) {
                    return validateIfEmpty(value);
                  },
                  decoration:
                      textInputDecoration.copyWith(hintText: "Last Name"),
                ),
                SizedBox(height: 10),
                TextFormField(
                  onChanged: (value) {},
                  validator: (value) {
                    return validateIfEmpty(value);
                  },
                  decoration: textInputDecoration.copyWith(hintText: "Address"),
                ),
                SizedBox(height: 10),
                TextFormField(
                  onChanged: (value) {},
                  validator: (value) {
                    return validateIfEmpty(value);
                  },
                  decoration:
                      textInputDecoration.copyWith(hintText: "Contact Number"),
                  keyboardType: TextInputType.phone,
                ),
                SizedBox(height: 10),
                TextFormField(
                  onChanged: (value) {},
                  validator: (value) {
                    return validateIfEmpty(value);
                  },
                  decoration: textInputDecoration.copyWith(hintText: "Email"),
                  keyboardType: TextInputType.emailAddress,
                ),
                SizedBox(height: 10),
                TextFormField(
                  obscureText: true,
                  onChanged: (value) {},
                  validator: (value) {
                    return validateIfEmpty(value);
                  },
                  decoration:
                      textInputDecoration.copyWith(hintText: "Password"),
                ),
                SizedBox(height: 10),
                TextFormField(
                  obscureText: true,
                  onChanged: (value) {},
                  validator: (value) {
                    return validateIfEmpty(value);
                  },
                  decoration:
                      textInputDecoration.copyWith(hintText: "Verify Password"),
                ),
                SizedBox(height: 10),
                RaisedButton(
                  child: Text("SUBMIT"),
                  onPressed: () {
                    if(_signUpKey.currentState.validate()) {
                      // sign up
                    }
                  },
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  String validateIfEmpty(String value) => value.isEmpty ? "Required" : null;
}
