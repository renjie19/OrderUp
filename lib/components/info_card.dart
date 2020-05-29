import 'package:flutter/material.dart';

class InfoCard extends StatelessWidget {
  final String label;
  final String info;
  final EdgeInsetsGeometry padding;


  InfoCard({@required this.label,@required this.info,this.padding});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: padding,
      width: double.maxFinite,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
            label,
            style: TextStyle(
              color: Colors.white,
              fontSize: 14,
              fontWeight: FontWeight.bold,
              fontFamily: 'Fredoka',
              letterSpacing: 2,
            ),
          ),
          Text(
            info,
            style: TextStyle(
              color: Colors.black,
              fontSize: 20,
              fontWeight: FontWeight.bold,
              fontFamily: 'Fredoka',
              letterSpacing: 1,
            ),
          )
        ],
      ),
    );
  }
}
