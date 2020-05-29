import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:orderupv2/shared/models/account.dart';
import 'package:orderupv2/shared/models/client.dart';

class AccountService {
  final Firestore _fireStore = Firestore.instance;
  CollectionReference _userCollection;
  String uid;
  Account _account;

  AccountService({this.uid}) {
    _userCollection = _fireStore.collection("Users");
  }

  Future<Account> _mapToAccount(DocumentSnapshot snapshot) async{
    _account = Account(
      id: uid,
      firstName: snapshot["firstName"],
      lastName: snapshot["lastName"],
      location: snapshot["location"],
      email: snapshot["email"],
      contactNo: snapshot["contactNo"],
      clients: await getClients(snapshot['clients']),
      orders: snapshot["orders"]
    );
    return _account;
  }

  // get account
  Stream<Account> get userData{
    return _userCollection.document(uid).snapshots().asyncMap(_mapToAccount);
  }


  // register account
  Future registerAccount(Account account) async{
    DocumentReference document = _userCollection.document(account.id);
    await document.setData({
      "id" : account.id,
      "firstName" : account.firstName,
      "lastName" : account.lastName,
      "location" : account.location,
      "contactNo" : account.contactNo,
      "email" : account.email,
      "orders" : [],
      "clients" : []
    });
  }

  Future<List<Client>> getClients(clientIds) async{
    List<Client> clients = [];
    for(String id in clientIds) {
      DocumentSnapshot snapshot = await _userCollection.document(id).get();
      clients.add(Client(
        id: id,
        firstName: snapshot["firstName"],
        lastName: snapshot["lastName"],
        location: snapshot["location"],
        email: snapshot["email"],
        contactNo: snapshot["contactNo"],
        // todo add mapping of orders
      ));
    }
    return clients;
  }

  // update account
}