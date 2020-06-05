import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:orderupv2/components/loading.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';

class SignUp extends StatefulWidget {
  @override
  _SignUpState createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
  final _signUpKey = GlobalKey<FormState>();

  final AuthService _authService = AuthService();
  final AccountServiceImpl _accountService = AccountServiceImpl();
  bool _isLoading = false;
  String _loadingMessage = "Please Wait";
  Account _account = Account();
  String _password;
  String _verifyPassword;

  @override
  Widget build(BuildContext context) {
    return _isLoading
        ? Loading(message: _loadingMessage)
        : Scaffold(
            backgroundColor: primaryColor,
            body: SafeArea(
              child: SingleChildScrollView(
                child: Container(
                  padding: EdgeInsets.fromLTRB(20, 100, 20, 10),
                  child: Form(
                    key: _signUpKey,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: <Widget>[
                        TextFormField(
                          onChanged: (value) => _account.firstName = value,
                          validator: (value) {
                            return validateIfEmpty(value);
                          },
                          decoration: textInputDecoration.copyWith(
                              hintText: "First Name"),
                        ),
                        SizedBox(height: 10),
                        TextFormField(
                          onChanged: (value) => _account.lastName = value,
                          validator: (value) {
                            return validateIfEmpty(value);
                          },
                          decoration:
                              textInputDecoration.copyWith(hintText: "Last Name"),
                        ),
                        SizedBox(height: 10),
                        TextFormField(
                          onChanged: (value) => _account.location = value,
                          validator: (value) {
                            return validateIfEmpty(value);
                          },
                          decoration:
                              textInputDecoration.copyWith(hintText: "Address"),
                        ),
                        SizedBox(height: 10),
                        TextFormField(
                          onChanged: (value) => _account.contactNo = value,
                          validator: (value) {
                            return validateIfEmpty(value);
                          },
                          decoration: textInputDecoration.copyWith(
                              hintText: "Contact Number"),
                          keyboardType: TextInputType.phone,
                        ),
                        SizedBox(height: 10),
                        TextFormField(
                          onChanged: (value) => _account.email = value.trim(),
                          validator: (value) {
                            return validateIfEmpty(value);
                          },
                          decoration:
                              textInputDecoration.copyWith(hintText: "Email"),
                          keyboardType: TextInputType.emailAddress,
                        ),
                        SizedBox(height: 10),
                        TextFormField(
                          obscureText: true,
                          onChanged: (value) => _password = value.trim(),
                          validator: (value) =>
                              verifyPassword(value, _verifyPassword),
                          decoration:
                              textInputDecoration.copyWith(hintText: "Password"),
                        ),
                        SizedBox(height: 10),
                        TextFormField(
                          obscureText: true,
                          onChanged: (value) => _verifyPassword = value.trim(),
                          validator: (value) => verifyPassword(value, _password),
                          decoration: textInputDecoration.copyWith(
                              hintText: "Verify Password"),
                        ),
                        SizedBox(height: 30),
                        RaisedButton(
                          padding: EdgeInsets.all(15),
                          color: Colors.white,
                          onPressed: () async {
                            if (_signUpKey.currentState.validate()) {
                              await registerEmailAndPassword();
                              await saveUserInfo();
                              showLoading("Account Created");
                              Navigator.canPop(context);
                            }
                          },
                          child: Text(
                            "SUBMIT",
                            style: TextStyle(
                              color: primaryColor,
                              fontWeight: FontWeight.bold,
                              fontSize: 18
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
          );
  }

  Future saveUserInfo() async {
    showLoading("Finishing Up");
    await _accountService.create(_account);
  }

  Future registerEmailAndPassword() async {
    showLoading("Signing Up");
    var id = await _authService.signUp(_account.email, _password);
    _account.id = id;
  }

  void hideLoading() {
    setState(() => _isLoading = false);
  }

  void showLoading(String message) {
    setState(() {
      _loadingMessage = message;
      _isLoading = true;
    });
  }

  String verifyPassword(String value, String compareWith) {
    if (value.isEmpty) {
      return "Required";
    } else if (value.length < 6) {
      return "Characters should be 6 or more";
    } else if (compareWith.isNotEmpty && value != compareWith) {
      return "Password are not the same";
    }
    return null;
  }

  String validateIfEmpty(String value) => value.isEmpty ? "Required" : null;
}
