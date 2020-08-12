import 'package:flutter/material.dart';

enum Event {
  ADD,
  UPDATE,
  DELETE,
  SET
}

class BlocEvent<T> {
  Event _event;
  T _data;

  BlocEvent({@required Event event,@required T data}) {
    this._event = event;
    this._data = data;
  }

  Event getEvent() {
    return _event;
  }

  T getData() {
    return _data;
  }

}