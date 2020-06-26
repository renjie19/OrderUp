import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/event/account_event.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/shared/models/account.dart';



class AccountBloc extends Bloc<AccountEvent, Account> {
  AccountService _service;
  
  AccountBloc(){
    _service = AccountServiceImpl();
  }

  @override
  Account get initialState => AccountServiceImpl.account;

  @override
  Stream<Account> mapEventToState(AccountEvent event) async* {
    if(event is AccountCreate) {
      yield await _service.create(event.account);
    } else if(event is AccountUpdate) {
      var result = await _service.update(event.account);
      //todo: add equasomething to identify change and trigger rebuild
      yield _mapToNewObject(result);
    } else {
      throw Exception('Event not found');
    }
  }

  Stream<Account> get userData {
    return _service.userData;
  }

  Account _mapToNewObject(result) {
    return Account(
      id: result.id,
      firstName: result.firstName,
      lastName: result.lastName,
      location: result.location,
      email: result.email,
      contactNo: result.contactNo
    );
  }

}