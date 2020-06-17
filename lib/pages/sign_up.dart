import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:orderupv2/components/loading.dart';
import 'package:orderupv2/mixins/alert_message.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/services/auth_service.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:the_validator/the_validator.dart';

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
  String _continueLabel = 'Continue';

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
                validator: (value) => _validateIfEmpty(value),
                decoration: InputDecoration(labelText: 'First Name'),
                initialValue: _account.firstName,
                onChanged: (value) =>
                    setState(() => _account.firstName = value),
              ),
              TextFormField(
                validator: (value) => _validateIfEmpty(value),
                decoration: InputDecoration(labelText: 'Last Name'),
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
                validator: (value) => _validateIfEmpty(value),
                decoration: InputDecoration(labelText: 'Address'),
                initialValue: _account.location,
                onChanged: (value) => setState(() => _account.location = value),
              ),
              TextFormField(
                validator: FieldValidator.number(noSymbols:true, message: 'Provide valid number'),
                decoration: InputDecoration(labelText: 'Contact Number'),
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
                validator: FieldValidator.email(message: 'Provide valid email'),
                decoration: InputDecoration(labelText: 'Email'),
                initialValue: _account.email,
                onChanged: (value) => setState(() => _account.email = value),
              ),
              TextFormField(
                obscureText: true,
                validator: (value) => _validatePassword(value, _verifyPassword),
                decoration: InputDecoration(labelText: 'Password'),
                initialValue: _password,
                onChanged: (value) => setState(() => _password = value),
              ),
              TextFormField(
                // todo check if password and verify password are the same
                obscureText: true,
                validator: (value) => _validatePassword(value, _password),
                decoration: InputDecoration(labelText: 'Verify Password'),
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

    String label = _currentStep == _getSteps().length - 1 ? 'Finish' : 'Continue';
    setState(() => _continueLabel = label);
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
            appBar: AppBar(
              title: Text(
                'Sign up Form',
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 18
                ),
              ),
              centerTitle: true,
            ),
            body: Stepper(
              steps: _getSteps(),
              currentStep: _currentStep,
              onStepContinue: () {
                if (_validate(_currentStep)) {
                  _isComplete || _currentStep == _getSteps().length - 1
                      ? _submit()
                      : _next();
                }
              },
              onStepCancel: _cancel,
              onStepTapped: (step) {
                if (_validate(step)) {
                  _goTo(step);
                }
              },
              controlsBuilder: (BuildContext context,
                  {VoidCallback onStepCancel, VoidCallback onStepContinue}) {
                return Row(
                  children: <Widget>[
                    FlatButton(
                      color: primaryColor,
                      child: Text(
                        _continueLabel,
                        style: TextStyle(color: highlightColorSecondary),
                      ),
                      onPressed: onStepContinue,
                    ),
                    FlatButton(
                      child: Text('Cancel'),
                      onPressed: onStepCancel,
                    ),
                  ],
                );
              },
            ),
          );
  }

  Future saveUserInfo() async {
    showLoading("Finishing Up");
    await _accountService.create(_account);
  }

  Future registerEmailAndPassword() async {
    showLoading("Signing Up");
    var result = await _authService.signUp(_account.email, _password);
    _account.id = result;
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

  String _validatePassword(String value, String compareWith) {
    if (value.isEmpty) {
      return "Required";
    } else if (value.length < 6) {
      return "Characters should be 6 or more";
    } else if (compareWith.isNotEmpty && value != compareWith) {
      return "Password are not the same";
    }
    return null;
  }

  String _validateIfEmpty(String value) => value.isEmpty ? "Required" : null;

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

  _submit() async {
    try {
      await registerEmailAndPassword();
      await saveUserInfo();
      showLoading("Account Created");
      Navigator.pop(context);
    } catch (e) {
      setState(() => _isLoading = false);
      AlertMessage.show(e.message, true, context);
    }
  }

  StepState _getState(int step) {
    if (_currentStep == step) {
      return StepState.editing;
    } else if (_validate(step)) {
      return StepState.complete;
    } else {
      return StepState.disabled;
    }
  }
}
