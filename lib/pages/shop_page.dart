import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:orderupv2/components/order_info_card.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/client.dart';

class ShopPage extends StatefulWidget {
  final Client client;

  ShopPage(this.client);

  @override
  _ShopPageState createState() => _ShopPageState();
}

class _ShopPageState extends State<ShopPage> {
  @override
  Widget build(BuildContext context) {
    List<String> list = [];
    //test data
    for (int x = 0; x < 20; x++) {
      list.add('Item $x');
    }

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
                isExtended: true,
                tooltip: 'Add Item',
                splashColor: primaryColor,
                backgroundColor: Colors.white,
                elevation: 5,
                child: Icon(
                  Icons.add,
                  size: 20,
                  color: primaryColor,
                ),
                onPressed: () {},
              ),
            ),
          ],
        ),
      ),
      body: SafeArea(
        child: Column(
          children: <Widget>[
            OrderInfoCard(widget.client),
            Expanded(
              child: ListView.separated(
                padding: EdgeInsets.all(8),
                itemBuilder: (context, position) {
                  return Container(
                    color: Colors.white,
                    child: ListTile(
                      onTap: (){},
                      title: Text(list[position]),
                    ),
                  );
                },
                separatorBuilder: (context, position) => Divider(height: 1,thickness: 1, color: Colors.black,),
                itemCount: list.length,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
