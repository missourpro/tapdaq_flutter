import 'dart:async';

import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

enum TapdaqRewardedEvent {
  didLoad,
  didFailToLoad,
  willDisplay,
  didDisplay,
  didFailToDisplay,
  didClose,
  didClick,
  didVerify,
  onUserDeclined
}

abstract class TapdaqRewardedEventHandler {
  final Function(TapdaqRewardedEvent, Map<String, dynamic>) _listener;

  TapdaqRewardedEventHandler(
      Function(TapdaqRewardedEvent, Map<String, dynamic>) listener)
      : _listener = listener;

  Future<dynamic> handleEvent(MethodCall call) async {
    switch (call.method) {
      case 'didLoad':
        _listener(TapdaqRewardedEvent.didLoad, null);
        break;
      case 'didFailToLoad':
        _listener(TapdaqRewardedEvent.didFailToLoad,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'willDisplay':
        _listener(TapdaqRewardedEvent.willDisplay, null);
        break;
      case 'didDisplay':
        _listener(TapdaqRewardedEvent.didDisplay, null);
        break;
      case 'didFailToDisplay':
        _listener(TapdaqRewardedEvent.didFailToDisplay,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'didClose':
        _listener(TapdaqRewardedEvent.didClose, null);
        break;
      case 'didClick':
        _listener(TapdaqRewardedEvent.didClick, null);
        break;
      case 'didVerify':
        _listener(TapdaqRewardedEvent.didClick,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'onUserDeclined':
        _listener(TapdaqRewardedEvent.didClick, null);
        break;
    }

    return null;
  }
}

class TapdaqRewarded extends TapdaqRewardedEventHandler {
  static const MethodChannel _channel =
      MethodChannel('tapdaq_flutter/rewarded');

  int id;
  MethodChannel _adChannel;
  final String adTag;
  final void Function(TapdaqRewardedEvent, Map<String, dynamic>) listener;

  TapdaqRewarded({
    @required this.adTag,
    this.listener,
  }) : super(listener) {
    id = hashCode;
    if (listener != null) {
      _adChannel = MethodChannel('tapdaq_flutter/rewarded');
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
