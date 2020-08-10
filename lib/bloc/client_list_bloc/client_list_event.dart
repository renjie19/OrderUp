import 'package:orderupv2/shared/models/client.dart';

abstract class ClientListEvents {
  Client client;

  ClientListEvents(this.client);
}

class ClientListEventAdd extends ClientListEvents {
  ClientListEventAdd(Client client) : super(client);

}

class ClientListEventUpdate extends ClientListEvents {
  ClientListEventUpdate(Client client) : super(client);

}

class ClientListEventDelete extends ClientListEvents {
  ClientListEventDelete(Client client) : super(client);

}

class ClientListEventSet extends ClientListEvents {
  List<Client> clients;

  ClientListEventSet(this.clients) : super(null);
}