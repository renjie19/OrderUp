import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:orderupv2/shared/models/account.dart';

class AccountService {
  final Firestore _fireStore = Firestore.instance;
  CollectionReference _userCollection;
  String uid;

  AccountService({this.uid}) {
    _userCollection = _fireStore.collection("Users");
  }

  Account _mapToAccount(DocumentSnapshot snapshot) {
    return Account(
      id: uid,
      firstName: snapshot["firstName"],
      lastName: snapshot["lastname"],
      location: snapshot["location"],
      email: snapshot["email"],
      contactNo: snapshot["contactNo"],
      clients: snapshot['clients'],
      orders: snapshot["orders"]
    );
  }

  // get account
  Stream<Account> get userData{
    return _userCollection.document(uid).snapshots().map(_mapToAccount);
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

  // update account
}