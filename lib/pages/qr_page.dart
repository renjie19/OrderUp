import 'package:flutter/material.dart';
import 'package:flutter_barcode_scanner/flutter_barcode_scanner.dart';
import 'package:orderupv2/mixins/alert_message.dart';
import 'package:orderupv2/services/account_service.dart';
import 'package:orderupv2/services/account_service_impl.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:qr_flutter/qr_flutter.dart';

class QrPage extends StatelessWidget {
  final AccountService service = AccountServiceImpl();

  Future scanQr(BuildContext context) async {
    try {
      String result = await FlutterBarcodeScanner.scanBarcode("#FFFFFF", 'Cancel', true, ScanMode.QR);
      if(result != null) {
        service.addClient(result);
        AlertMessage.show('Client Added', false, context);
      }
    } catch (e) {
      AlertMessage.show(e, true, context);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      backgroundColor: primaryColor,
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Center(
            child: QrImage(
              data: AccountServiceImpl.account.id,
              size: 250,
              backgroundColor: Colors.white,
            ),
          ),
          SizedBox(height: 50),
          FlatButton(
            onPressed: () async{
              await scanQr(context);
            },
            child: Text('Scan QR'),
            color: Colors.white,
          )
        ],
      )
    );
  }
}
