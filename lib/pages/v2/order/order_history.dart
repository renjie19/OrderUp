import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/bloc/order_history_bloc/order_history_bloc.dart';
import 'package:orderupv2/bloc/purchase_bloc/puchase_bloc.dart';
import 'package:orderupv2/event/bloc_event.dart';
import 'package:orderupv2/pages/v2/purchase/purchase_tab.dart';
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
  String sourceValue = target[0];
  Event purchaseEvent;

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
          orders = sortOrdersBySource(this.sourceValue, client.id);
          orders = sortOrdersByStatus(this.sortStatus, orders);
          return SafeArea(
            child: Scaffold(
              backgroundColor: primaryColor,
              body: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    AspectRatio(
                      aspectRatio: 5/2,
                      child: Card(
                        elevation: 4,
                        child: Padding(
                          padding: const EdgeInsets.symmetric(
                              vertical: 5, horizontal: 8),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.stretch,
                            children: [
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.stretch,
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Text(
                                      "Name: ${client.firstName.toUpperCase()} ${client.lastName.toUpperCase()}",
                                      style: TextStyle(fontSize: 16, fontStyle: FontStyle.italic)),
                                  Text("Address: ${client.location.toUpperCase()}",
                                      style: TextStyle(fontSize: 16, fontStyle: FontStyle.italic)),
                                  Text("Contact #: ${client.contactNo}",
                                      style: TextStyle(fontSize: 16, fontStyle: FontStyle.italic))
                                ],
                              ),
                              Divider(),
                              Text("FILTERS", style: TextStyle(fontStyle: FontStyle.italic)),
                              Row(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Text('Status: ',
                                        style: TextStyle(fontSize: 12, fontStyle: FontStyle.italic)),
                                    DropdownButton(
                                      isDense: true,
                                      value: sortStatus,
                                      items:
                                          List.generate(status.length, (index) {
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
                                        style: TextStyle(fontSize: 12, fontStyle: FontStyle.italic)),
                                    DropdownButton(
                                        isDense: true,
                                        value: sourceValue,
                                        items: List.generate(target.length,
                                            (index) {
                                          var targetType = target[index];
                                          return DropdownMenuItem(
                                            child: Text(targetType,
                                                style: TextStyle(fontSize: 12)),
                                            value: targetType,
                                          );
                                        }),
                                        onChanged: (value) {
                                          setState(
                                              () => this.sourceValue = value);
                                          updateDataByFilter(client.id);
                                        })
                                  ]),
                            ],
                          ),
                        ),
                      ),
                    ),
                    AspectRatio(
                      aspectRatio: 10/15,
                      child: SingleChildScrollView(
                        child: Expanded(
                          child: PaginatedDataTable(
                            actions: [
                              IconButton(
                                splashRadius: 25,
                                color: Colors.black,
                                icon: Icon(
                                  FontAwesome.shopping_cart,
                                ),
                                onPressed: () => loadCreateOrderPage(null),
                              )
                            ],
                            sortColumnIndex: this.index,
                            sortAscending: this.sortAscending,
                            rowsPerPage: ROWS_PER_PAGE,
                            header: Row(
                              children: [
                                Icon(FontAwesome.book, color: Colors.black,),
                                Text('ORDER RECORDS', style: TextStyle(fontStyle: FontStyle.italic),)
                              ],
                            ),
                            source: OrderListDataSource(orders, this),
                            columns: List.generate(columns.length, (index) {
                              return DataColumn(
                                numeric: index == 2,
                                onSort: (columnIndex, ascending) =>
                                    sortData(columnIndex, ascending, orders),
                                label: Text(
                                  columns[index],
                                  style:
                                      TextStyle(fontStyle: FontStyle.italic),
                                ),
                              );
                            }),
                          ),
                        ),
                      ),
                    ),
                  ]),
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
    bloc.add(BlocEvent(event: Event.SET, data: orders));
  }

  void updateDataByFilter(String id) {
    var result = sortOrdersBySource(this.sourceValue, id);
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
      return bloc.initialState.where((order) => order.to == id).toList();
    } else {
      return bloc.initialState.where((order) => order.from == id).toList();
    }
  }

  @override
  void run(Object order) {
    if (order != null && order is Order) {
      loadCreateOrderPage(order.id);
    }
  }

  void loadCreateOrderPage(String orderId) async {
    print('Loading order $orderId');
    setPurchaseTabEvent(orderId == null);
    Order result = await Navigator.push(context, MaterialPageRoute(builder: (context) {
      return BlocProvider<PurchaseBloc>(
        create: (context) => PurchaseBloc(orderId, bloc.getClient().id),
        child: PurchaseTab(),
      );
    }));
    if(result != null) {
      bloc.add(BlocEvent(event: this.purchaseEvent, data: [result]));
      setState(() => this.purchaseEvent = null);
    }
  }

  void setPurchaseTabEvent(bool isNewOrder) {
    setState(() => purchaseEvent = isNewOrder ? Event.ADD : Event.UPDATE);
  }
}
