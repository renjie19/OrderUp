import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/item.dart';

class ItemCreate extends StatefulWidget {
  final Item item;

  ItemCreate(this.item);

  @override
  _CreateItemState createState() => _CreateItemState(item);
}

class _CreateItemState extends State<ItemCreate> {
  final _createItemKey = GlobalKey<FormState>();
  Item itemSelected;

  _CreateItemState(this.itemSelected);

  @override
  Widget build(BuildContext context) {
    itemSelected.quantity =
        itemSelected.quantity == null ? 0 : itemSelected.quantity;
    itemSelected.price = itemSelected.price == null ? 0 : itemSelected.price;
    return Form(
      key: _createItemKey,
      child: Column(
        children: <Widget>[
          TextFormField(
            initialValue: itemSelected.name,
            decoration: textInputDecoration.copyWith(hintText: 'Name'),
            onChanged: (value) => itemSelected.name = value,
          ),
          Row(
            children: <Widget>[
              IconButton(
                icon: Icon(Feather.minus),
                onPressed: () {
                  setState(() {
                    itemSelected.quantity -= 1;
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
              Expanded(
                child: TextFormField(
                  initialValue: itemSelected.package ?? 'pc(s)',
                  decoration:
                      textInputDecoration.copyWith(hintText: 'Packaging'),
                  onChanged: (value) => itemSelected.package = value
                ),
              ),
            ],
          ),
          TextFormField(
            initialValue:
                itemSelected.price == 0 ? '0' : '${itemSelected.price}',
            keyboardType: TextInputType.number,
            decoration: textInputDecoration.copyWith(
                hintText: 'Price', prefixIcon: Icon(Feather.dollar_sign)),
            onChanged: (value){
              itemSelected.price = double.parse(value);
            },
          ),
          ButtonBar(
            children: <Widget>[
              FlatButton(
                onPressed: () {
                  Navigator.pop(context, itemSelected);
                },
                child: Text('SUBMIT'),
              ),
            ],
          )
        ],
      ),
    );
  }
}
