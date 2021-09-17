import 'dart:async';

import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

enum TapdaqInterstitialEvent {
  didLoad,
  didFailToLoad,
  willDisplay,
  didDisplay,
  didFailToDisplay,
  didClose,
  didClick
}

abstract class TapdaqInterstitialEventHandler {
  final Function(TapdaqInterstitialEvent, Map<String, dynamic>) _listener;

  TapdaqInterstitialEventHandler(
      Function(TapdaqInterstitialEvent, Map<String, dynamic>) listener)
      : _listener = listener;

  Future<dynamic> handleEvent(MethodCall call) async {
    switch (call.method) {
      case 'didLoad':
        _listener(TapdaqInterstitialEvent.didLoad, null);
        break;
      case 'didFailToLoad':
        _listener(TapdaqInterstitialEvent.didFailToLoad,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'willDisplay':
        _listener(TapdaqInterstitialEvent.willDisplay, null);
        break;
      case 'didDisplay':
        _listener(TapdaqInterstitialEvent.didDisplay, null);
        break;
      case 'didFailToDisplay':
        _listener(TapdaqInterstitialEvent.didFailToDisplay,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'didClose':
        _listener(TapdaqInterstitialEvent.didClose, null);
        break;
      case 'didClick':
        _listener(TapdaqInterstitialEvent.didClick, null);
        break;
    }

    return null;
  }
}

class TapdaqInterstitial extends TapdaqInterstitialEventHandler {
  static const MethodChannel _channel =
  MethodChannel('tapdaq_flutter/interstitial');

  int id;
  MethodChannel _adChannel;
  final String adTag;
  final void Function(TapdaqInterstitialEvent, Map<String, dynamic>) listener;

  TapdaqInterstitial({
    @required this.adTag,
    this.listener,
  }) : super(listener) {
    id = hashCode;
    if (listener != null) {
      _adChannel = MethodChannel('tapdaq_flutter/interstitial');
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