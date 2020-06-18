import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:orderupv2/shared/models/client.dart';

class ClientService {
  static final Firestore _clientStore = Firestore.instance;
  final CollectionReference _usersCollectionReference =
      _clientStore.collection("Users");

  // get a client by id

  // get List of clients by id
  Future clients(clientIds) async {
    List<Client> clients = [];

    var result = await _usersCollectionReference.getDocuments();
    List<DocumentSnapshot> clientDocuments = result.documents
        .where((element) => clientIds.contains(element.documentID))
        .toList();

    for (DocumentSnapshot document in clientDocuments) {
      if (document.exists) {
        clients.add(Client(
          id: document['id'],
          firstName: document['firstName'],
          lastName: document["lastName"],
          location: document["location"],
          email: document["email"],
          contactNo: document["contactNo"],
        ));
      }
    }
    return clients;
  }

  // update client (add order id)
  Future addClientOrders(String orderId, String clientId) async {
    await _usersCollectionReference.document(clientId).updateData({
      'orders': FieldValue.arrayUnion([orderId])
    });
  }
}
