import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/shared/constants/constants.dart';
import 'package:orderupv2/shared/models/item.dart';

class ItemCreate extends StatefulWidget {
  final Item item;
  final Function onContinue;

  ItemCreate(this.item, this.onContinue);

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
        itemSelected.quantity == null ? 1 : itemSelected.quantity;
    itemSelected.price = itemSelected.price == null ? 0 : itemSelected.price;
    return Form(
      key: _createItemKey,
      child: Column(
        children: <Widget>[
          TextFormField(
            initialValue: itemSelected.name,
            decoration: textInputDecoration.copyWith(hintText: 'Name'),
            onChanged: (value) => itemSelected.name = value,
            validator: (value) => itemSelected.name == null ? 'Required' : null,
          ),
          Row(
            children: <Widget>[
              IconButton(
                icon: Icon(Feather.minus),
                onPressed: () {
                  setState(() {
                    if(itemSelected.quantity > 0) {
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
              Expanded(
                child: TextFormField(
                  initialValue: itemSelected.package ?? '',
                  decoration:
                      textInputDecoration.copyWith(hintText: 'pc(s)'),
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
                  Navigator.pop(context);
                },
                child: Text('CANCEL'),
              ),
              FlatButton(
                onPressed: () {
                  if(_createItemKey.currentState.validate()) {
                    setState(() => itemSelected.package = 'pc(s)');
                    Navigator.pop(context, itemSelected);
                    widget.onContinue();
                  }
                },
                child: Text('ADD AND CONTINUE'),
              ),
              FlatButton(
                color: Colors.blue,
                onPressed: () {
                  if(_createItemKey.currentState.validate()) {
                    setState(() => itemSelected.package = 'pc(s)');
                    Navigator.pop(context, itemSelected);
                  }
                },
                child: Text('FINISH'),
              ),
            ],
          )
        ],
      ),
    );
  }
}
