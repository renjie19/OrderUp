import 'dart:core';

class Account {
  String id;
  String firstName;
  String lastName;
  String location;
  String email;
  String contactNo;
  List clients;
  List orders;

  Account({this.id, this.firstName, this.lastName, this.location, this.email,
      this.contactNo, this.clients, this.orders});
}