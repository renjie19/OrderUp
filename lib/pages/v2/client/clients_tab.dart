import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/bloc/client_list_bloc/client_list_bloc.dart';
import 'package:orderupv2/bloc/order_history_bloc/order_history_bloc.dart';
import 'package:orderupv2/event/bloc_event.dart';
import 'package:orderupv2/pages/qr_page.dart';
import 'package:orderupv2/pages/v2/order/order_history.dart';
import 'package:orderupv2/shared/models/client.dart';

class ClientsTab extends StatefulWidget {
  @override
  _ClientsTabState createState() => _ClientsTabState();
}

class _ClientsTabState extends State<ClientsTab> {
  ClientListBloc bloc;

  @override
  Widget build(BuildContext context) {
    initializeStreams();
    initializeBlocs();
    return BlocBuilder(
      bloc: bloc,
      builder: (context, clients) {
        return Scaffold(
          body: ListView.builder(
            itemCount: clients.length,
            itemBuilder: (context, position) {
              Client client = clients[position];
              return Padding(
                  padding: EdgeInsets.symmetric(vertical: 10, horizontal: 5),
                  child: Card(
                    elevation: 5,
                    child: ListTile(
                      title: Text(
                        "${client.firstName} ${client.lastName}",
                        textAlign: TextAlign.center,
                      ),
                      onTap: () => loadClientOrderHistory(client.id),
                    ),
                  ));
            },
          ),
          floatingActionButton: FloatingActionButton(
            onPressed: loadQrScannerPage,
            child: Icon(Feather.user_plus),
          ),
        );
      },
    );
  }

  void initializeBlocs() {
    bloc = BlocProvider.of<ClientListBloc>(context);
  }

  void loadQrScannerPage() async {
    var result = await Navigator.push(context, MaterialPageRoute(builder: (context) {
      return QrPage();
    }));

    if( result != null ) {
      bloc.add(BlocEvent(event: Event.ADD, data: result));
      loadQrScannerPage();
    }
  }

  void loadClientOrderHistory(id) {
    Navigator.push(context, MaterialPageRoute(builder: (context) {
      return BlocProvider<OrderHistoryBloc>(
        create: (context) => OrderHistoryBloc(id),
        child: OrderHistory(),
      );
    }));
  }

  void initializeStreams() {

  }
}
