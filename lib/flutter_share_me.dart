import 'package:flutter/services.dart';

class FlutterShareMe {
  final MethodChannel _channel = const MethodChannel('flutter_share_me');

  ///share to facebook
  Future<String> shareToFacebook({String url = '', String msg = ''}) async {
    final Map<String, Object> arguments = Map<String, dynamic>();
    arguments.putIfAbsent('msg', () => msg);
    arguments.putIfAbsent('url', () => url);
    return await _channel.invokeMethod('shareFacebook', arguments);
  }

  ///share to twitter
  Future<String> shareToTwitter({String msg = '', String url = ''}) async {
    final Map<String, Object> arguments = Map<String, dynamic>();
    arguments.putIfAbsent('msg', () => msg);
    arguments.putIfAbsent('url', () => url);
    return await _channel.invokeMethod('shareTwitter', arguments);
  }

  ///share to whatsapp
  Future<String> shareToWhatsApp({String msg}) async {
    final Map<String, Object> arguments = Map<String, dynamic>();
    arguments.putIfAbsent('msg', () => msg);
    return await _channel.invokeMethod('shareWhatsApp', arguments);
  }

  ///share to whatsapp
  Future<String> shareToWeChat({String msg, String title}) async {
    final Map<String, Object> arguments = Map<String, dynamic>();
    arguments.putIfAbsent('msg', () => msg);
    arguments.putIfAbsent('title', () => title);
    return await _channel.invokeMethod('shareWeChat', arguments);
  }

  ///use system share ui
  Future<String> shareToSystem({String msg, String title}) async {
    Map<String, Object> arguments = Map<String, dynamic>();
    arguments.putIfAbsent('msg', () => msg);
    arguments.putIfAbsent('title', () => title);
    return await _channel.invokeMethod('system', arguments);
  }
}
