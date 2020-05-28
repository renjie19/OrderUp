import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:orderupv2/shared/models/account.dart';

class AccountService {
  final Firestore _fireStore = Firestore.instance;
  CollectionReference _userCollection;
  
  AccountService() {
    _userCollection = _fireStore.collection("Users");
  }

  // get account

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