import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/bloc/order_history_bloc/order_history_bloc.dart';
import 'package:orderupv2/bloc/order_history_bloc/order_history_events.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/constants/status_constants.dart';
import 'package:orderupv2/shared/custom_callback.dart';
import 'package:orderupv2/shared/models/order.dart';

import 'order_list_data_source.dart';

class OrderHistory extends StatefulWidget {
  @override
  _OrderHistoryState createState() => _OrderHistoryState();
}

class _OrderHistoryState extends State<OrderHistory> implements CustomCallBack {
  static final List<String> columns = ['Date', 'Status', 'Total'];
  static final List<String> status = [
    'ALL',
    StatusConstant.FOR_DELIVERY,
    StatusConstant.PAID,
    StatusConstant.PENDING
  ];
  static final List<String> target = ['ALL', 'USER', 'CLIENT'];
  static const ROWS_PER_PAGE = 7;

  OrderHistoryBloc bloc;
  bool sortAscending = false;
  int index = 0;
  String sortStatus = status[0];
  String targetValue = target[0];

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
            child: AspectRatio(
              aspectRatio: 1,
              child: Scaffold(
                backgroundColor: primaryColor,
                body: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Card(
                        margin:
                            EdgeInsets.symmetric(vertical: 8, horizontal: 5),
                        elevation: 4,
                        child: Padding(
                          padding: const EdgeInsets.all(10.0),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.stretch,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Text(
                                  "Name: ${client.firstName} ${client.lastName}"),
                              Text("Address: ${client.location}"),
                              Text("Contact #: ${client.contactNo}")
                            ],
                          ),
                        ),
                      ),
                      Card(
                        elevation: 4,
                        child: Padding(
                          padding: const EdgeInsets.symmetric(
                              vertical: 5, horizontal: 8),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.stretch,
                            children: [
                              Row(children: [
                                Text('Status: ',
                                    style: TextStyle(fontSize: 12)),
                                DropdownButton(
                                  value: sortStatus,
                                  items: List.generate(status.length, (index) {
                                    var statusType = status[index];
                                    return DropdownMenuItem(
                                      child: Text(
                                        statusType,
                                        style: TextStyle(fontSize: 12),
                                      ),
                                      value: statusType,
                                    );
                                  }),
                                  onChanged: (value) {
                                    setState(() => this.sortStatus = value);
                                    updateDataByFilter(client.id);
                                  },
                                ),
                                SizedBox(width: 15),
                                Text('Source: ',
                                    style: TextStyle(fontSize: 12)),
                                DropdownButton(
                                    value: targetValue,
                                    items:
                                        List.generate(target.length, (index) {
                                      var targetType = target[index];
                                      return DropdownMenuItem(
                                        child: Text(targetType,
                                            style: TextStyle(fontSize: 12)),
                                        value: targetType,
                                      );
                                    }),
                                    onChanged: (value) {
                                      setState(() => this.targetValue = value);
                                      updateDataByFilter(client.id);
                                    })
                              ]),
                              Text(
                                'Order Count: ${orders.length}',
                                style: TextStyle(fontSize: 12),
                              ),
                            ],
                          ),
                        ),
                      ),
                      Expanded(
                        child: SingleChildScrollView(
                          child: PaginatedDataTable(
                            actions: [
                              IconButton(
                                splashRadius: 25,
                                color: Colors.black,
                                icon: Icon(Feather.shopping_cart,),
                                onPressed: () => loadCreateOrderPage(null),
                              )
                            ],
                            sortColumnIndex: this.index,
                            sortAscending: this.sortAscending,
                            rowsPerPage: ROWS_PER_PAGE,
                            header: Text('ORDER RECORDS'),
                            source: OrderListDataSource(orders, this),
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
                          ),
                        ),
                      ),
                    ]),
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

  void updateDataByFilter(String id) {
    var result = sortOrdersBySource(this.targetValue, id);
    result = sortOrdersByStatus(this.sortStatus, result);
    sortData(this.index, this.sortAscending, result);
  }

  List<Order> sortOrdersByStatus(String sortStatus, List<Order> orders) {
    if (sortStatus == 'ALL') {
      return orders;
    } else {
      return orders
          .where((order) => order.status.toString() == sortStatus)
          .toList();
    }
  }

  List<Order> sortOrdersBySource(String source, String id) {
    if (source == 'ALL') {
      return bloc.initialState;
    } else if (source == 'USER') {
      return bloc.initialState.where((order) => order.from == id).toList();
    } else {
      return bloc.initialState.where((order) => order.to == id).toList();
    }
  }

  @override
  void run(Object order) {
    if(order != null && order is Order) {
      loadCreateOrderPage(order.id);
    }
  }

  void loadCreateOrderPage(String orderId) {
    print('Loading order $orderId');
    // TODO: load create order page
  }
}
