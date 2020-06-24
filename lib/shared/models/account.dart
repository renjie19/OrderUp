import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/order.dart';

class Account {
  String id;
  String firstName;
  String lastName;
  String location;
  String email;
  String contactNo;
  List<Client> clients;
  List<Order> orders;

  Account({this.id, this.firstName, this.lastName, this.location, this.email,
      this.contactNo, this.clients, this.orders});
}