import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class StreamWrapper<T> extends StatelessWidget {
  final Widget child;
  final Stream<T> streamProvider;

  StreamWrapper({@required this.child, @required this.streamProvider});

  @override
  Widget build(BuildContext context) {
    return StreamProvider<T>.value(
        value: streamProvider,
        child: child,
    );
  }
}
