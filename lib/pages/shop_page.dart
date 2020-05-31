import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:orderupv2/shared/constants.dart';

class ShopPage extends StatefulWidget {
  @override
  _ShopPageState createState() => _ShopPageState();
}

class _ShopPageState extends State<ShopPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: primaryColor,
      bottomNavigationBar: BottomAppBar(
        color: primaryColor,
        child: Stack(
          overflow: Overflow.visible,
          alignment: Alignment.center,
          children: <Widget>[
            Container(
              decoration: BoxDecoration(
                color: Colors.white,
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
                        color: Colors.blueAccent,
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
                        color: Colors.blueAccent,
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
                hoverElevation: 8,
                splashColor: primaryColor,
                backgroundColor: highlightColor,
                elevation: 3,
                child: Icon(Icons.add, size: 20, color: Colors.white,),
                onPressed: () {},
              ),
            ),
          ],
        ),
      ),
      body: Column(
        children: <Widget>[],
      ),
    );
  }
}
