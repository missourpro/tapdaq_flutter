import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

enum TapdaqBannerEvent {
  didLoad,
  didFailToLoad,
  didRefresh,
  didFailToRefresh,
  didClick
}

class TapdaqBannerSize {
  static const STANDARD = {"width": 320.0, "height": 50.0, "name": "STANDARD"};
  static const LARGE = {"width": 320.0, "height": 100.0, "name": "LARGE"};
  static const MEDIUM_RECT = {
    "width": 320.0,
    "height": 250.0,
    "name": "MEDIUM_RECT"
  };
  static const LEADERBOARD = {
    "width": 468.0,
    "height": 60.0,
    "name": "LEADERBOARD"
  };
  static const SMART = {"width": -1.0, "height": 50.0, "name": "SMART"};
}

abstract class TapdaqBannerEventHandler {
  final Function(TapdaqBannerEvent, Map<String, dynamic>) _listener;

  TapdaqBannerEventHandler(
      Function(TapdaqBannerEvent, Map<String, dynamic>) listener)
      : _listener = listener;

  Future<dynamic> handleEvent(MethodCall call) async {
    switch (call.method) {
      case 'didLoad':
        _listener(TapdaqBannerEvent.didLoad, null);
        break;
      case 'didFailToLoad':
        _listener(TapdaqBannerEvent.didFailToLoad,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'didRefresh':
        _listener(TapdaqBannerEvent.didRefresh, null);
        break;
      case 'didFailToRefresh':
        _listener(TapdaqBannerEvent.didFailToRefresh,
            Map<String, dynamic>.from(call.arguments));
        break;
      case 'didClick':
        _listener(TapdaqBannerEvent.didClick, null);
        break;
    }

    return null;
  }
}

class TapdaqBannerController extends TapdaqBannerEventHandler {
  final MethodChannel _channel;

  TapdaqBannerController(
      int id, Function(TapdaqBannerEvent, Map<String, dynamic>) listener)
      : _channel = MethodChannel('tapdaq_flutter/banner_$id'),
        super(listener) {
    if (listener != null) {
      _channel.setMethodCallHandler(handleEvent);
    }
  }

  void dispose() {
    _channel.invokeMethod('dispose');
  }
}

class TapdaqBanner extends StatefulWidget {
  final String adTag;
  final adSize;
  final void Function(TapdaqBannerEvent, Map<String, dynamic>) listener;
  final void Function(TapdaqBannerController) onBannerCreated;

  TapdaqBanner(
      {Key key,
      @required this.adTag,
      @required this.adSize,
      this.listener,
      this.onBannerCreated})
      : super(key: key);

  @override
  _TapdaqBannerState createState() => _TapdaqBannerState();
}

class _TapdaqBannerState extends State<TapdaqBanner> {
  TapdaqBannerController _controller;

  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return
        Container(
        width: widget.adSize['width'] >= 0
            ? widget.adSize['width']
            : double.infinity,
        height: widget.adSize['height'] >= 0
            ? widget.adSize['height']
            : double.infinity,
        child: AndroidView(
          key: UniqueKey(),
          viewType: 'tapdaq_flutter/banner',
          creationParams: <String, dynamic>{
            "adTag": widget.adTag,
            "adSize": widget.adSize['name'],
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
    _controller = TapdaqBannerController(id, widget.listener);
    if (widget.onBannerCreated != null) {
      widget.onBannerCreated(_controller);
    }
  }
}
