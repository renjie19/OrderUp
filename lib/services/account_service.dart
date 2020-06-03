import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:orderupv2/services/client_service.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/models/account.dart';

class AccountService {
  final Firestore _fireStore = Firestore.instance;
  final _clientService = ClientService();
  final _orderService = OrderService();
  CollectionReference _userCollection;
  
  String uid;
  static Account account;

  AccountService({this.uid}) {
    _userCollection = _fireStore.collection("Users");
  }

  Future<Account> _mapToAccount(DocumentSnapshot snapshot) async{
    if(snapshot != null && snapshot.exists) {
      account =  Account(
        id: uid,
        firstName: snapshot["firstName"],
        lastName: snapshot["lastName"],
        location: snapshot["location"],
        email: snapshot["email"],
        contactNo: snapshot["contactNo"],
        clients: await _clientService.clients(snapshot['clients']),
        orders:  await _orderService.orders(snapshot['orders']),
      );
    }
    return account;
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

  Future addToOrderList(String orderId) async{
    await _userCollection.document(account.id).updateData({'orders': FieldValue.arrayUnion([orderId])});
  }

  // update account
}