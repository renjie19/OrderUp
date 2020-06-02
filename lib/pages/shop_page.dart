import 'dart:developer';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:orderupv2/components/item_create.dart';
import 'package:orderupv2/components/order_info_card.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/client.dart';
import 'package:orderupv2/shared/models/item.dart';
import 'package:orderupv2/shared/models/order.dart';

class ShopPage extends StatefulWidget {
  final Client client;
  final Order order;

  final createItemKey = GlobalKey<FormState>();

  ShopPage(this.client, this.order);

  @override
  _ShopPageState createState() => _ShopPageState();
}

class _ShopPageState extends State<ShopPage> {
  Order order;

  @override
  Widget build(BuildContext context) {
    order = widget.order == null ? Order() : widget.order;
    List<Item> list = order.items ?? [];

    return Scaffold(
      backgroundColor: primaryColor,
      bottomNavigationBar: BottomAppBar(
        // bottom options
        color: primaryColor,
        child: Stack(
          overflow: Overflow.visible,
          alignment: Alignment.center,
          children: <Widget>[
            Container(
              decoration: BoxDecoration(
                color: highlightColor[700],
              ),
              child: Row(
                children: <Widget>[
                  Expanded(
                    child: FlatButton(
                      padding: EdgeInsets.symmetric(vertical: 12),
                      onPressed: () {},
                      child: Icon(
                        Icons.delete,
                        size: 28,
                        color: Colors.white,
                      ),
                    ),
                  ),
                  Expanded(
                    child: FlatButton(
                      padding: EdgeInsets.symmetric(vertical: 12),
                      onPressed: () {},
                      child: Icon(
                        Icons.send,
                        size: 28,
                        color: Colors.white,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            Positioned(
              bottom: 23,
              child: FloatingActionButton(
                elevation: 0,
                shape: CircleBorder(
                    side: BorderSide(width: 4, color: primaryColor)
                ),
                tooltip: 'Add Item',
                splashColor: primaryColor,
                backgroundColor: Colors.white,
                child: Icon(
                  Icons.add,
                  size: 20,
                  color: primaryColor,
                ),
                onPressed: () {
                  // show dialog create item dialog
                  displayCreateDialog(Item());
                },
              ),
            ),
          ],
        ),
      ),
      body: SafeArea(
        child: Column(
          children: <Widget>[
            OrderInfoCard(),
            Expanded(
              child: ListView.separated(
                padding: EdgeInsets.all(8),
                itemBuilder: (context, position) {
                  return Container(
                    color: Colors.white,
                    child: ListTile(
                      onTap: () {},
                      title: Text('${list[position]}'),
                    ),
                  );
                },
                separatorBuilder: (context, position) =>
                    Divider(height: 1, thickness: 1, color: Colors.black,),
                itemCount: list.length,
              ),
            ),
          ],
        ),
      ),
    );
  }

  void displayCreateDialog(Item selectedItem) async{
    Item item = selectedItem == null ? Item() : selectedItem;
    Item result = await showModalBottomSheet(context: context, builder: (context) {
      return Container(
        padding: EdgeInsets.all(10),
        child: ItemCreate(item),
      );
    });
    print('result');
    print(result);
    print(result.name);
    print(result.quantity);
    print(result.price);
    print(result.package);

    if(result != null) {
      if(order.items == null) {
        order.items = List<Item>();
      }
      setState(() {
        order.items.add(result);
      });
    }

  }
}
