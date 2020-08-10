import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:orderupv2/bloc/order_history_bloc/order_history_bloc.dart';
import 'package:orderupv2/bloc/order_history_bloc/order_history_events.dart';
import 'package:orderupv2/event/order_list_event.dart';
import 'package:orderupv2/mixins/date_formatter.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/models/order.dart';

class OrderHistory extends StatefulWidget {
  @override
  _OrderHistoryState createState() => _OrderHistoryState();
}

class _OrderHistoryState extends State<OrderHistory> {
  OrderHistoryBloc bloc;
  final List<String> columns = ['Date', 'Status', 'Total'];
  final List<String> status = [
    'ALL',
    StatusConstant.FOR_DELIVERY,
    StatusConstant.PAID,
    StatusConstant.PENDING
  ];
  bool sortAscending = false;
  int index = 0;
  String sortStatus = 'All';

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
                  DropdownButton(
                    value: sortStatus,
                    items: List.generate(status.length, (index) {
                      return DropdownMenuItem(
                        child: Text(status[index]),
                        value: status[index],
                      );
                    }),
                    onChanged: (value) {
                      setState(() => this.sortStatus = value);
                      sortByStatus(this.sortStatus, orders);
                    },
                  ),
                  Expanded(
                    child: SingleChildScrollView(
                      child: Card(
                        elevation: 3,
                        child: DataTable(
                          sortColumnIndex: index,
                          sortAscending: sortAscending,
                          columns: List.generate(columns.length, (index) {
                            return DataColumn(
                              numeric: index == 2,
                              onSort: (columnIndex, ascending) =>
                                  sortData(columnIndex, ascending, orders),
                              label: Text(
                                columns[index],
                                style: TextStyle(fontWeight: FontWeight.bold),
                              ),
                            );
                          }),
                          rows: List.generate(orders.length, (index) {
                            Order order = orders[index];
                            return DataRow(cells: [
                              DataCell(
                                  Text(DateFormatter.toDateString(order.date))),
                              DataCell(Text('${order.status}')),
                              DataCell(Text('${order.total}'))
                            ]);
                          }),
                        ),
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
      index = columnIndex;
      sortAscending = ascending;
    });
    switch (columnIndex) {
      case 0:
        !ascending
            ? orders.sort((o1, o2) => o1.date.compareTo(o2.date))
            : orders.sort((o1, o2) => o2.date.compareTo(o1.date));
        break;
      case 1:
        break;
      case 2:
        !ascending
            ? orders.sort((o1, o2) => o1.total.compareTo(o2.total))
            : orders.sort((o1, o2) => o2.total.compareTo(o1.total));
        break;
    }
    bloc.add(OrderHistorySet(orders));
  }

  void sortByStatus(String sortStatus, List<Order> orders) {
    var orderList;
    if (sortStatus == 'ALL') {
      orderList = bloc.initialState;
    } else {
      orderList = orders.where((order) => order.status.toString() == sortStatus);
    }
    sortData(index, sortAscending, orderList);
  }
}
