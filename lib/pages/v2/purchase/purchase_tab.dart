import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/bloc/purchase_bloc/puchase_bloc.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/models/item.dart';

class PurchaseTab extends StatefulWidget {
  @override
  _PurchaseTabState createState() => _PurchaseTabState();
}

class _PurchaseTabState extends State<PurchaseTab> {
  PurchaseBloc bloc;
  static final columns = ['NAME', 'QTY', 'PACKAGE', 'PRICE'];

  @override
  void dispose() {
    super.dispose();
    bloc.close();
  }

  @override
  Widget build(BuildContext context) {
    bloc = BlocProvider.of<PurchaseBloc>(context);
    return BlocBuilder(
      bloc: bloc,
      builder: (context, items) {
        return SafeArea(
          child: Scaffold(
            appBar: AppBar(
              title: Text('Purchase Tab'),
              titleSpacing: 1,
              elevation: 8,
              actions: [
                IconButton(
                  splashRadius: 20,
                  icon: Icon(FontAwesome.cart_plus),
                  tooltip: 'Add Item',
                  onPressed: (){},
                ),
              ],
            ),
            floatingActionButton: getActionButton(),
            body: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Expanded(
                    child: SingleChildScrollView(
                      child: Card(
                        child: DataTable(
                          columns: List.generate(columns.length, (index) {
                            String column = columns[index];
                            return DataColumn(
                                numeric: column == columns[1] ||
                                    column == columns[3],
                                label: Text(
                                  column,
                                  style: TextStyle(fontStyle: FontStyle.italic),
                                ));
                          }),
                          rows: List.generate(items.length, (index) {
                            Item item = items[index];
                            return DataRow(cells: [
                              DataCell(Text('${item.name}')),
                              DataCell(Text('${item.quantity}')),
                              DataCell(Text('${item.package}')),
                              DataCell(Text('${item.price}')),
                            ]);
                          }),
                        ),
                      ),
                    ),
                  ),
                ]),
          ),
        );
      },
    );
  }

  FloatingActionButton getActionButton() {
    if(bloc.getOrder().status == StatusConstant.PAID) {
      return null;
    }
    return FloatingActionButton(
      onPressed: (){
        //TODO add function for processing order
      },
      child: Icon(
        getFloatingButtonIcon(),
      ),
    );
  }

  IconData getFloatingButtonIcon() {
    if(bloc.isForPayment())  {
      return FontAwesome.dollar;
    } else if (bloc.isForSendingOrder()) {
      return FontAwesome.send_o;
    } else {
      return FontAwesome.warning;
    }
  }
}
