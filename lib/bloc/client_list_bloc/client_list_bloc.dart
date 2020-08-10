import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/shared/models/client.dart';

import 'client_list_event.dart';

class ClientListBloc extends Bloc<ClientListEvents, List<Client>> {

  @override
  List<Client> get initialState => AccountServiceImpl.account.clients;

  @override
  Stream<List<Client>> mapEventToState(ClientListEvents event) async* {
   if(event.client != null || event is ClientListEventSet) {
     switch(event.runtimeType) {
       case ClientListEventAdd:
         state.add(event.client);
         yield state;
         break;
       case ClientListEventUpdate:
         int index = state.indexWhere((client) => client.id == event.client.id);
         if(index >= 0) {
           state[index] = event.client;
         }
         yield state;
         break;
       case ClientListEventDelete:
         int index = state.indexWhere((client) => client.id == event.client.id);
         if(index >= 0) {
           state.removeAt(index);
         }
         yield state;
         break;
       case ClientListEventSet:
         yield (event as ClientListEventSet).clients;
         break;
     }
   }
  }

}