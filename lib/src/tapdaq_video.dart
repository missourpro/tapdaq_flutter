import 'dart:async';

import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

enum TapdaqVideoEvent {
  didLoad,
  didFailToLoad,
  willDisplay,
  didDisplay,
  didFailToDisplay,
  didClose,
  didClick
}

abstract class TapdaqVideoEventHandler {
  final Function(TapdaqVideoEvent, Map<String, dynamic>) _listener;

  TapdaqVideoEventHandler(
      Function(TapdaqVideoEvent, Map<String, dynamic>) listener)
      : _listener = listener;

  Future<dynamic> handleEvent(MethodCall call) async {
    switch (call.method) {
      case 'didLoad':
        _listener(TapdaqVideoEvent.didLoad, null);
        break;
      case 'didFailToLoad':
        _listener(TapdaqVideoEvent.didFailToLoad,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'willDisplay':
        _listener(TapdaqVideoEvent.willDisplay, null);
        break;
      case 'didDisplay':
        _listener(TapdaqVideoEvent.didDisplay, null);
        break;
      case 'didFailToDisplay':
        _listener(TapdaqVideoEvent.didFailToDisplay,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'didClose':
        _listener(TapdaqVideoEvent.didClose, null);
        break;
      case 'didClick':
        _listener(TapdaqVideoEvent.didClick, null);
        break;
    }

    return null;
  }
}

class TapdaqVideo extends TapdaqVideoEventHandler {
  static const MethodChannel _channel =
  MethodChannel('tapdaq_flutter/video');

  int id;
  MethodChannel _adChannel;
  final String adTag;
  final void Function(TapdaqVideoEvent, Map<String, dynamic>) listener;

  TapdaqVideo({
    @required this.adTag,
    this.listener,
  }) : super(listener) {
    id = hashCode;
    if (listener != null) {
      _adChannel = MethodChannel('tapdaq_flutter/video');
      _adChannel.setMethodCallHandler(handleEvent);
    }
  }

  Future<bool> get isLoaded async {
    final bool result =
    await _channel.invokeMethod('isLoaded', <String, dynamic>{
      'adTag': adTag,
    });
    return result;
  }

  void load() async {
    await _channel.invokeMethod('load', <String, dynamic>{
      'adTag': adTag,
    });
  }

  void show() async {
    if (await isLoaded == true) {
      await _channel.invokeMethod('show', <String, dynamic>{
        'adTag': adTag,
      });
    }
  }

}