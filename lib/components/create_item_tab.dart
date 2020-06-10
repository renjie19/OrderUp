import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/item.dart';

class CreateItemTab extends StatefulWidget {
  final Item item;
  final Function onContinue;
  final bool editablePrice;

  CreateItemTab({this.item, this.onContinue, this.editablePrice});

  @override
  _CreateItemState createState() => _CreateItemState(item);
}

class _CreateItemState extends State<CreateItemTab> {
  final _createItemKey = GlobalKey<FormState>();
  Item itemSelected;

  _CreateItemState(this.itemSelected);

  @override
  Widget build(BuildContext context) {
    itemSelected.quantity =
        itemSelected.quantity == null ? 1 : itemSelected.quantity;
    itemSelected.price = itemSelected.price == null ? 0 : itemSelected.price;
    return Container(
      color: Colors.grey[300],
      padding: EdgeInsets.all(5),
      child: Form(
        key: _createItemKey,
        child: Column(
          children: <Widget>[
            TextFormField(
              initialValue: itemSelected.name,
              decoration: textInputDecoration.copyWith(hintText: 'Name'),
              onChanged: (value) => itemSelected.name = value,
              validator: (value) =>
                  itemSelected.name == null ? 'Required' : null,
            ),
            SizedBox(height: 5),
            Container(
              color: Colors.white,
              child: Row(
                children: <Widget>[
                  IconButton(
                    icon: Icon(Feather.minus),
                    onPressed: () {
                      setState(() {
                        if (itemSelected.quantity > 0) {
                          itemSelected.quantity -= 1;
                        }
                      });
                    },
                  ),
                  Text('${itemSelected.quantity}'),
                  IconButton(
                    icon: Icon(Icons.add),
                    onPressed: () {
                      setState(() {
                        itemSelected.quantity += 1;
                      });
                    },
                  ),
                  Container(child: SizedBox(width: 5, height: 59,),color: Colors.grey[300],),
                  Expanded(
                    child: TextFormField(
                        initialValue: itemSelected.package ?? '',
                        decoration:
                            textInputDecoration.copyWith(hintText: 'pc(s)'),
                        onChanged: (value) => itemSelected.package = value),
                  ),
                ],
              ),
            ),
            SizedBox(height: 5),
            TextFormField(
              initialValue:
                  itemSelected.price == 0 ? '0' : '${itemSelected.price}',
              enabled: widget.editablePrice,
              keyboardType: TextInputType.number,
              decoration: textInputDecoration.copyWith(
                disabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.grey, width: 30)),
                hintText: 'Price',
                prefixIcon: Icon(Feather.dollar_sign),
              ),
              onChanged: (value) {
                itemSelected.price = double.parse(value);
              },
            ),
            ButtonBar(
              children: <Widget>[
                FlatButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: Text('CANCEL'),
                ),
                FlatButton(
                  onPressed: () {
                    if (_createItemKey.currentState.validate()) {
                      setState(() => itemSelected.package = itemSelected.package ?? 'pc(s)');
                      Navigator.pop(context, itemSelected);
                      widget.onContinue();
                    }
                  },
                  child: Text('ADD AND CONTINUE'),
                ),
                FlatButton(
                  color: Colors.blue,
                  onPressed: () {
                    if (_createItemKey.currentState.validate()) {
                      setState(() => itemSelected.package = itemSelected.package ?? 'pc(s)');
                      Navigator.pop(context, itemSelected);
                    }
                  },
                  child: Text('FINISH'),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }
}
