import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/client_service.dart';
import 'package:orderupv2/services/order_service.dart';
import 'package:orderupv2/shared/models/account.dart';

class AccountServiceImpl implements AccountService {
  final _clientService = ClientService();
  final _orderService = OrderService();
  final CollectionReference _userCollection = Firestore.instance.collection("Users");

  String uid;
  static Account account;
  static AccountServiceImpl _instance;

  /// makes the initialization private
  AccountServiceImpl._internal({this.uid});

  /// exposed to initialize the class
  factory AccountServiceImpl({String id}) {
    if (_instance == null) {
      _instance = AccountServiceImpl._internal(uid: id);
    }
    _instance.uid = id;
    return _instance;
  }

  // get account
  Stream<Account> get userData {
    return _userCollection.document(uid).snapshots().asyncMap(_mapToAccount);
  }

  // register account
  Future create(Account account) async {
    DocumentReference document = _userCollection.document(account.id);
    await document.setData({
      "id": account.id,
      "firstName": account.firstName,
      "lastName": account.lastName,
      "location": account.location,
      "contactNo": account.contactNo,
      "email": account.email,
      "orders": [],
      "clients": []
    });
  }

  @override
  Future update(Account account) {
    // todo implement update logic
    return null;
  }

  @override
  Future addToOrderList(String orderId) async {
    await _userCollection.document(account.id).updateData({
      'orders': FieldValue.arrayUnion([orderId])
    });
  }

  Future<Account> _mapToAccount(DocumentSnapshot snapshot) async {
    if (snapshot != null && snapshot.exists) {
      account = Account(
        id: uid,
        firstName: snapshot["firstName"],
        lastName: snapshot["lastName"],
        location: snapshot["location"],
        email: snapshot["email"],
        contactNo: snapshot["contactNo"],
        clients: await _clientService.clients(snapshot['clients']),
        orders: await _orderService.orders(snapshot['orders']),
      );
    }
    return account;
  }


}
