import 'package:orderupv2/shared/models/account.dart';

abstract class AccountEvent{}

class AccountCreate extends AccountEvent{
  final Account account;

  AccountCreate(this.account);
}
class AccountUpdate extends AccountEvent{
  final Account account;

  AccountUpdate(this.account);
}
