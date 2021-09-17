import 'package:flutter/services.dart';

class Tapdaq {
  static const MethodChannel _channel = MethodChannel('tapdaq_flutter');
  final String appId;
  final String clientKey;
  final bool gdpr;
  final bool consentStatus;
  final bool restricted;

  Tapdaq({
    this.appId,
    this.clientKey,
    this.gdpr,
    this.consentStatus,
    this.restricted
  });

  void initialize() async {
    _channel.invokeMethod('initialize', <String, dynamic>{
      'AppId': appId,
      'ClientKey': clientKey,
      'GDPR': gdpr,
      'ConsentStatus': consentStatus,
      'Restricted': restricted
    });
    _channel.setMethodCallHandler(handleEvent);
  }

  void test() async {
    _channel.invokeMethod('testMediation', null);
  }

  Future<dynamic> handleEvent(MethodCall call) async {
    if (call.method == "didInit") {
      onInitialize.call();
    } else {
      onFail.call();
    }
  }

  // TODO: Implement Callbacks
  void onFail(){}
  void onInitialize(){}
}
