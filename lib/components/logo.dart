import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Logo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Image(
          image: AssetImage("lib/assets/images/logo_no_bg.png"),
          height: 200,
          color: Colors.white,
        ),
        Text(
          "ORDERUP",
          style: TextStyle(
            color: Colors.black,
            fontSize: 60,
            fontWeight: FontWeight.bold,
            fontFamily: "Fredoka",
            letterSpacing: 2,
          ),
        )
      ],
    );
  }
}
