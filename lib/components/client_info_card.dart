import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:orderupv2/shared/constants.dart';
import 'package:orderupv2/shared/models/client.dart';

class ClientInfoCard extends StatefulWidget {
  final Client client;

  ClientInfoCard(this.client);

  @override
  _ClientInfoCardState createState() => _ClientInfoCardState();
}

class _ClientInfoCardState extends State<ClientInfoCard> {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(15),
      child: Row(
        children: <Widget>[
          CircleAvatar(
            backgroundColor: Colors.white,
            child: Icon(
              Icons.person,
              size: 35,
            ),
          ),
          SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Text(
                  '${widget.client.firstName} ${widget.client.lastName}',
                  style: TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.bold,
                      fontSize: 20,
                      fontFamily: 'Fredoka',
                      letterSpacing: 1.5),
                ),
                Text(
                  widget.client.location,
                  style: TextStyle(
                      fontFamily: 'Fredoka',
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      letterSpacing: 1),
                ),
              ],
            ),
          ),
          IconButton(
            icon: Icon(
              Feather.shopping_bag,
              color: Colors.white,
            ),
            onPressed: () {
              // todo show order create page
            },
          ),
        ],
      ),
    );
  }
}
