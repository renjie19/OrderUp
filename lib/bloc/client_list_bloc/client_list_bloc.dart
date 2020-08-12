import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/event/bloc_event.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/shared/models/client.dart';

class ClientListBloc extends Bloc<BlocEvent<List<Client>>, List<Client>> {
  @override
  List<Client> get initialState => AccountServiceImpl.account.clients;

  @override
  Stream<List<Client>> mapEventToState(BlocEvent<List<Client>> event) async* {
    var list = state;
    switch (event.getEvent()) {
      case Event.ADD:
        list.addAll(event.getData());
        yield list;
        break;
      case Event.UPDATE:
        for (Client clientData in event.getData()) {
          int index = list.indexWhere((client) => client.id == clientData.id);
          if (index >= 0) {
            list[index] = clientData;
          }
        }
        yield list;
        break;
      case Event.DELETE:
        for (Client clientData in event.getData()) {
          int index = list.indexWhere((client) => client.id == clientData.id);
          if (index >= 0) {
            list.removeAt(index);
          }
        }
        yield list;
        break;
      case Event.SET:
        yield event.getData();
        break;
    }
  }
}
