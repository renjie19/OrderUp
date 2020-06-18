import 'package:orderupv2/shared/models/account.dart';

abstract class AccountService {

  // create account
  Future create(Account account);
  
  // update account
 Future update(Account account);

  // get account
  Stream<Account> get userData;

  // todo should be moved
  // add order to ids
  Future addToOrderList(String orderId);

  Future addClient(String id);
}