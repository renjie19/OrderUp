import 'dart:convert';

import 'package:orderupv2/shared/models/account.dart';

class Client extends Account  {
  Client({String id, String firstName, String lastName, String location, String email, String contactNo, List orders}){
    super.id = id;
    super.firstName = firstName;
    super.lastName = lastName;
    super.location = location;
    super.email = email;
    super.contactNo = contactNo;
    super.orders = orders;
  }

  Map toJson() {
    return {
      'id' : super.id,
      'firstName' : super.firstName,
      'lastName' : super.lastName,
      'location' : super.location,
      'email' : super.email,
      'contactNo' : super.contactNo,
      'orders' : orders != null ? jsonEncode(orders) : []
    };
  }
}