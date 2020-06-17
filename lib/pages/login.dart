import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:orderupv2/components/logo.dart';
import 'package:orderupv2/components/loading.dart';
import 'package:orderupv2/mixins/alert_message.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:the_validator/the_validator.dart';

class Login extends StatefulWidget {
  @override
  _LoginState createState() => _LoginState();
}

class _LoginState extends State<Login> {
  final _loginKey = GlobalKey<FormState>();

  final AuthService _authService = AuthService();

  String _email;
  String _password;

  // for loading screen
  bool _isLoading = false;
  String _loadingMessage = "Logging In";

  // for hiding and showing password
  bool _isVisible = false;

  @override
  Widget build(BuildContext context) {
    return _isLoading
        ? Loading(message: _loadingMessage)
        : Scaffold(
            backgroundColor: primaryColor[700],
            body: SafeArea(
              child: SingleChildScrollView(
                child: Container(
                  padding: EdgeInsets.fromLTRB(20, 70, 20, 0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: <Widget>[
                      Logo(),
                      SizedBox(height: 20),
                      Form(
                        key: _loginKey,
                        child: Column(
                          children: <Widget>[
                            TextFormField(
                              initialValue: _email,
                              onChanged: (value) => _email = value.trim(),
                              decoration: textInputDecoration.copyWith(
                                hintText: 'Email',
                                prefixIcon: Icon(Icons.email),
                              ),
                              validator: FieldValidator.email(
                                  message: 'Provide valid email'),
                              keyboardType: TextInputType.emailAddress,
                              textInputAction: TextInputAction.next,
                            ),
                            SizedBox(height: 10),
                            TextFormField(
                              initialValue: _password,
                              onChanged: (value) => _password = value,
                              obscureText: !_isVisible,
                              decoration: textInputDecoration.copyWith(
                                  hintText: 'Password',
                                  prefixIcon: Icon(Icons.lock),
                                  suffixIcon: IconButton(
                                      icon: Icon(
                                        _isVisible
                                            ? Icons.visibility
                                            : Icons.visibility_off,
                                        color: Colors.grey,
                                      ),
                                      onPressed: () {
                                        setState(
                                            () => _isVisible = !_isVisible);
                                      })),
                              validator:
                                  FieldValidator.required(message: 'Required'),
                            ),
                          ],
                        ),
                      ),
                      SizedBox(height: 30),
                      RaisedButton(
                        color: Colors.white,
                        elevation: 1,
                        padding: EdgeInsets.all(10),
                        onPressed: () async {
                          if (_loginKey.currentState.validate()) {
                            setState(() => _isLoading = true);
                            var result =
                                await _authService.signIn(_email, _password);
                            setState(() => _isLoading = false);
                            showErrorMessage(result, context);
                          }
                        },
                        child: Text(
                          "LOGIN",
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 18,
                            color: primaryColor[700],
                          ),
                        ),
                      ),
                      OutlineButton(
                        onPressed: () {
                          Navigator.pushNamed(context, '/signUp');
                        },
                        child: Text(
                          "SIGN UP",
                          style: TextStyle(
                              fontWeight: FontWeight.bold, fontSize: 18),
                        ),
                        borderSide: BorderSide(width: 1.5, color: Colors.white),
                        textColor: Colors.white,
                      )
                    ],
                  ),
                ),
              ),
            ),
          );
  }

  void showErrorMessage(PlatformException result, BuildContext context) {
    if (result != null) {
      AlertMessage.show(getError(result), true, context);
    }
  }

  String getError(PlatformException result) {
    print(result.code);
    switch (result.code) {
      case 'ERROR_INVALID_EMAIL':
        return 'INVALID EMAIL';
      case 'ERROR_USER_NOT_FOUND':
        return 'EMAIL DOES NOT EXIST';
      case 'ERROR_WRONG_PASSWORD':
        return 'WRONG PASSWORD';
      case 'ERROR_TOO_MANY_REQUESTS':
        return 'TRIED MULTIPLE TIMES. TRY AGAIN LATER';
      default:
        return '${result.code}\n{result.message}';
    }
  }
}