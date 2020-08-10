import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/bloc/order_history_bloc/order_history_bloc.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderHistory extends StatefulWidget {
  @override
  _OrderHistoryState createState() => _OrderHistoryState();
}

class _OrderHistoryState extends State<OrderHistory> {
  OrderHistoryBloc bloc;
  final List<String> columns = ['Date', 'Status', 'Total'];
  bool _ascending = false;
  int _index = 0;

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    bloc = BlocProvider.of<OrderHistoryBloc>(context);
    var client = bloc.getClient();
    return BlocBuilder(
        bloc: bloc,
        builder: (context, orders) {
          return SafeArea(
            child: Scaffold(
              body: Column(
                children: [
                  Card(
                    color: primaryColor,
                    margin: EdgeInsets.all(10),
                    elevation: 5,
                    child: Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text("Name: ${client.firstName} ${client.lastName}"),
                          Text("Address: ${client.location}"),
                          Text("Contact #: ${client.contactNo}")
                        ],
                      ),
                    ),
                  ),
                  Expanded(
                    child: SingleChildScrollView(
                      child: DataTable(
                        sortColumnIndex: _index,
                        sortAscending: _ascending,
                        columns: List.generate(columns.length, (index) {
                          return DataColumn(
                            numeric: index == 2,
                            onSort: (columnIndex, ascending) => sortData(columnIndex, ascending, orders),
                            label: Text(
                              columns[index],
                              style: TextStyle(fontWeight: FontWeight.bold),
                            ),
                          );
                        }),
                        rows: List.generate(orders.length, (index) {
                          Order order = orders[index];
                          return DataRow(
                            cells: [
                              DataCell(Text(DateFormatter.toDateString(order.date))),
                              DataCell(Text('${ order.status }')),
                              DataCell(Text('${ order.total }'))
                            ]
                          );
                        }),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          );
        });
  }

  sortData(int columnIndex, bool ascending, List<Order> orders) {
    setState(() {
      _index = columnIndex;
      _ascending = ascending;
      orders.sort((o1, o2) => o1.total.compareTo(o2.total));
    });

    // TODO: implement sorting based on column index and ascending bool
  }
}
