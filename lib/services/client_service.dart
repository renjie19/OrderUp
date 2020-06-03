import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:orderupv2/shared/models/client.dart';

class ClientService {
  static final Firestore _clientStore = Firestore.instance;
  final CollectionReference _usersCollectionReference = _clientStore.collection("Users");

  // get a client by id

  // get List of clients by id
  Future<List<Client>> clients(clientIds) async{
    List<Client> clients = [];
    for(String id in clientIds) {
      DocumentSnapshot snapshot = await _usersCollectionReference.document(id).get();
      clients.add(Client(
        id: id,
        firstName: snapshot['firstName'],
        lastName: snapshot["lastName"],
        location: snapshot["location"],
        email: snapshot["email"],
        contactNo: snapshot["contactNo"],
      ));
    }
    return clients;
  }

  // update client (add order id)
  Future addClientOrders(String orderId, String clientId) async{
    await _usersCollectionReference.document(clientId).updateData({'orders': FieldValue.arrayUnion([orderId])});
  }

}