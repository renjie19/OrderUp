import 'package:flutter/material.dart';

const textInputDecoration = InputDecoration(
  fillColor: Colors.white,
  filled: true,
  enabledBorder: OutlineInputBorder(
    borderSide: BorderSide(
      color: Colors.white,
      width: 2,
    ),
  ),
  focusedBorder: OutlineInputBorder(
    borderSide: BorderSide(color: highlightColor, width: 2),
  ),
  focusedErrorBorder: OutlineInputBorder(
    borderSide: BorderSide(color: highlightColor, width: 2),
  ),
);

const primaryColor = Color.fromRGBO(40, 123, 192, 1);
const highlightColor = Colors.deepOrange;