import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

enum TapdaqNativeEvent {
  didLoad,
  didFailToLoad,
  didRefresh,
  didFailToRefresh,
  didClick
}


abstract class TapdaqNativeEventHandler {
  final Function(TapdaqNativeEvent, Map<String, dynamic>) _listener;

  TapdaqNativeEventHandler(
      Function(TapdaqNativeEvent, Map<String, dynamic>) listener)
      : _listener = listener;

  Future<dynamic> handleEvent(MethodCall call) async {
    switch (call.method) {
      case 'didLoad':
        _listener(TapdaqNativeEvent.didLoad, null);
        break;
      case 'didFailToLoad':
        _listener(TapdaqNativeEvent.didFailToLoad,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'didRefresh':
        _listener(TapdaqNativeEvent.didRefresh, null);
        break;
      case 'didFailToRefresh':
        _listener(TapdaqNativeEvent.didFailToRefresh,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'didClick':
        _listener(TapdaqNativeEvent.didClick, null);
        break;
    }

    return null;
  }
}

class TapdaqNativeController extends TapdaqNativeEventHandler {
  final MethodChannel _channel;

  TapdaqNativeController(
      int id, Function(TapdaqNativeEvent, Map<String, dynamic>) listener)
      : _channel = MethodChannel('tapdaq_flutter/native_$id'),
        super(listener) {
    if (listener != null) {
      _channel.setMethodCallHandler(handleEvent);
    }
  }

  void dispose() {
    _channel.invokeMethod('dispose');
  }
}

class TapdaqNative extends StatefulWidget {
  final String adTag;
  final void Function(TapdaqNativeEvent, Map<String, dynamic>) listener;
  final void Function(TapdaqNativeController) onNativeCreated;

  TapdaqNative(
      {Key key,
      @required this.adTag,
      this.listener,
      this.onNativeCreated})
      : super(key: key);

  @override
  _TapdaqNativeState createState() => _TapdaqNativeState();
}

class _TapdaqNativeState extends State<TapdaqNative> {
  TapdaqNativeController _controller;

  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return
        Container(
        width: 300.0,
        height: 300.0,
        child: AndroidView(
          key: UniqueKey(),
          viewType: 'tapdaq_flutter/native',
          creationParams: <String, dynamic>{
            "adTag": widget.adTag,
          },
          creationParamsCodec: StandardMessageCodec(),
          onPlatformViewCreated: _onPlatformViewCreated,
        ),
      );
    } else {
      return Text('$defaultTargetPlatform is not yet supported');
    }
  }

  @override
  void dispose() {
    super.dispose();
  }

  void _onPlatformViewCreated(int id) {
    _controller = TapdaqNativeController(id, widget.listener);
    if (widget.onNativeCreated != null) {
      widget.onNativeCreated(_controller);
    }
  }
}
