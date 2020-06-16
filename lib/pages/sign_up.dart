import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:orderupv2/components/loading.dart';
import 'package:orderupv2/mixins/alert_message.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';

class SignUp extends StatefulWidget {
  @override
  _SignUpState createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
  final _emailStep = GlobalKey<FormState>();
  final _userInfoStep = GlobalKey<FormState>();
  final _additionalInfoStep = GlobalKey<FormState>();

  final AuthService _authService = AuthService();
  final AccountServiceImpl _accountService = AccountServiceImpl();
  bool _isLoading = false;
  String _loadingMessage = "Please Wait";
  Account _account = Account();
  String _password;
  String _verifyPassword;

  int _currentStep = 0;
  bool _isComplete = false;

  List<Step> _getSteps() {
    return [
      Step(
        title: Text('Personal'),
        isActive: _currentStep == 0,
        state: _getState(0),
        content: Form(
          key: _userInfoStep,
          child: Column(
            children: <Widget>[
              TextFormField(
                validator: (value) => _isNull(value),
                decoration: InputDecoration(hintText: 'First Name'),
                initialValue: _account.firstName,
                onChanged: (value) =>
                    setState(() => _account.firstName = value),
              ),
              TextFormField(
                validator: (value) => _isNull(value),
                decoration: InputDecoration(hintText: 'Last Name'),
                initialValue: _account.lastName,
                onChanged: (value) => setState(() => _account.lastName = value),
              ),
            ],
          ),
        ),
      ),
      Step(
        title: Text('Additional'),
        isActive: _currentStep == 1,
        state: _getState(1),
        content: Form(
          key: _additionalInfoStep,
          child: Column(
            children: <Widget>[
              TextFormField(
                validator: (value) => _isNull(value),
                decoration: InputDecoration(hintText: 'Address'),
                initialValue: _account.location,
                onChanged: (value) => setState(() => _account.location = value),
              ),
              TextFormField(
                validator: (value) => _isNull(value),
                decoration: InputDecoration(hintText: 'Contact Number'),
                initialValue: _account.contactNo,
                onChanged: (value) =>
                    setState(() => _account.contactNo = value),
              ),
            ],
          ),
        ),
      ),
      Step(
        title: Text('Email'),
        isActive: _currentStep == 2,
        state: _getState(2),
        content: Form(
          key: _emailStep,
          child: Column(
            children: <Widget>[
              TextFormField(
                validator: (value) => _isNull(value),
                decoration: InputDecoration(hintText: 'Email'),
                initialValue: _account.email,
                onChanged: (value) => setState(() => _account.email = value),
              ),
              TextFormField(
                obscureText: true,
                validator: (value) => _isNull(value),
                decoration: InputDecoration(hintText: 'Password'),
                initialValue: _password,
                onChanged: (value) => setState(() => _password = value),
              ),
              TextFormField(
                // todo check if password and verify password are the same
                obscureText: true,
                validator: (value) => _isNull(value),
                decoration: InputDecoration(hintText: 'Verify Password'),
                initialValue: _verifyPassword,
                onChanged: (value) => setState(() => _verifyPassword = value),
              ),
            ],
          ),
        ),
      ),
    ];
  }

  void _next() {
    _currentStep + 1 != _getSteps().length
        ? _goTo(_currentStep + 1)
        : setState(() => _isComplete = true);
  }

  void _goTo(int step) {
    setState(() => _currentStep = step);
  }

  void _cancel() {
    if (_currentStep > 0) {
      _goTo(_currentStep - 1);
    } else {
      Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    return _isLoading
        ? Loading(message: _loadingMessage)
        : Scaffold(
            body: Stepper(
              steps: _getSteps(),
              currentStep: _currentStep,
              onStepContinue: () {
                if (_validate(_currentStep)) {
                  _isComplete ? _submit() : _next();
                }
              },
              onStepCancel: _cancel,
              onStepTapped: (step) {
                if (_validate(step)) {
                  _goTo(step);
                }
              },
            ),
          );
    // Scaffold(
//            backgroundColor: primaryColor,
//            body: SafeArea(
//              child: SingleChildScrollView(
//                child: Container(
//                  padding: EdgeInsets.fromLTRB(20, 100, 20, 10),
//                  child: Form(
//                    key: _signUpKey,
//                    child: Column(
//                      crossAxisAlignment: CrossAxisAlignment.stretch,
//                      children: <Widget>[
//                        TextFormField(
//                          onChanged: (value) => _account.firstName = value,
//                          validator: (value) {
//                            return validateIfEmpty(value);
//                          },
//                          decoration: textInputDecoration.copyWith(
//                              hintText: "First Name"),
//                        ),
//                        SizedBox(height: 10),
//                        TextFormField(
//                          onChanged: (value) => _account.lastName = value,
//                          validator: (value) {
//                            return validateIfEmpty(value);
//                          },
//                          decoration:
//                              textInputDecoration.copyWith(hintText: "Last Name"),
//                        ),
//                        SizedBox(height: 10),
//                        TextFormField(
//                          onChanged: (value) => _account.location = value,
//                          validator: (value) {
//                            return validateIfEmpty(value);
//                          },
//                          decoration:
//                              textInputDecoration.copyWith(hintText: "Address"),
//                        ),
//                        SizedBox(height: 10),
//                        TextFormField(
//                          onChanged: (value) => _account.contactNo = value,
//                          validator: (value) {
//                            return validateIfEmpty(value);
//                          },
//                          decoration: textInputDecoration.copyWith(
//                              hintText: "Contact Number"),
//                          keyboardType: TextInputType.phone,
//                        ),
//                        SizedBox(height: 10),
//                        TextFormField(
//                          onChanged: (value) => _account.email = value.trim(),
//                          validator: (value) {
//                            return validateIfEmpty(value);
//                          },
//                          decoration:
//                              textInputDecoration.copyWith(hintText: "Email"),
//                          keyboardType: TextInputType.emailAddress,
//                        ),
//                        SizedBox(height: 10),
//                        TextFormField(
//                          obscureText: true,
//                          onChanged: (value) => _password = value.trim(),
//                          validator: (value) =>
//                              verifyPassword(value, _verifyPassword),
//                          decoration:
//                              textInputDecoration.copyWith(hintText: "Password"),
//                        ),
//                        SizedBox(height: 10),
//                        TextFormField(
//                          obscureText: true,
//                          onChanged: (value) => _verifyPassword = value.trim(),
//                          validator: (value) => verifyPassword(value, _password),
//                          decoration: textInputDecoration.copyWith(
//                              hintText: "Verify Password"),
//                        ),
//                        SizedBox(height: 30),
//                        RaisedButton(
//                          padding: EdgeInsets.all(15),
//                          color: Colors.white,
//                          onPressed: () async {
//                            if (_signUpKey.currentState.validate()) {
//                              await registerEmailAndPassword();
//                              await saveUserInfo();
//                              showLoading("Account Created");
//                              Navigator.canPop(context);
//                            }
//                          },
//                          child: Text(
//                            "SUBMIT",
//                            style: TextStyle(
//                              color: primaryColor,
//                              fontWeight: FontWeight.bold,
//                              fontSize: 18
//                            ),
//                          ),
//                        ),
//                      ],
//                    ),
//                  ),
//                ),
//              ),
//            ),
//          );
  }

  Future saveUserInfo() async {
    showLoading("Finishing Up");
    await _accountService.create(_account);
  }

  Future registerEmailAndPassword()async {
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

  String _isNull(String value) {
    return value == null || value.trim().isEmpty ? 'Required' : null;
  }

  bool _validate(int step) {
    try {
      switch (step) {
        case 0:
          return _userInfoStep.currentState.validate();
        case 1:
          return _additionalInfoStep.currentState.validate();
        case 2:
          return _emailStep.currentState.validate();
        default:
          return true;
      }
    } catch (e) {
      return false;
    }
  }

  void _resetErrors(int step) {
    switch (step) {
      case 0:
        _userInfoStep.currentState.reset();
        break;
      case 1:
        _additionalInfoStep.currentState.reset();
        break;
      case 2:
        _emailStep.currentState.reset();
        break;
    }
  }

  _submit() async {
    try {
      await registerEmailAndPassword();
      await saveUserInfo();
      showLoading("Account Created");
      Navigator.pop(context);
    } catch (e) {
      setState(() => _isLoading = false);
      AlertMessage.show(e, true, context);
    }
  }

  StepState _getState(int step) {
    if(_currentStep == step) {
      return StepState.editing;
    } else if(_validate(step)) {
      _resetErrors(step);
      return StepState.complete;
    } else {
      return StepState.disabled;
    }
  }
}
