import 'package:flutter/material.dart';
import 'package:flutter_share_me/flutter_share_me.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            RaisedButton(
              child: Text('share to twitter'),
              onPressed: () async {
                var response = await FlutterShareMe().shareToTwitter(url: 'https://github.com/lizhuoyuan', msg: 'hello flutter! ');
                if (response == 'success') {
                  print('navigate success');
                }
              },
            ),
            RaisedButton(
              child: Text('share to shareWhatsApp'),
              onPressed: () {
                FlutterShareMe().shareToWhatsApp(msg: 'hello,this is my github:https://github.com/lizhuoyuan');
              },
            ),
            RaisedButton(
              child: Text('share to shareFacebook'),
              onPressed: () {
                FlutterShareMe().shareToFacebook(url: 'https://github.com/lizhuoyuan', msg: 'Hello Flutter');
//                FlutterShareMe().shareToFacebook(url: 'https://wabi.onelink.me/S90r/bcca1a1b', msg: 'Hello Flutter');
//                FlutterShareMe().shareToFacebook(url: 'https://www.wabi-app.com', msg: 'Hello Flutter');
//                FlutterShareMe().shareToFacebook(url: 'https://play.google.com/store/apps/details?id=com.applaudsoft.wabi.virtual_number', msg: 'Hello Flutter');
              },
            ),
            RaisedButton(
              child: Text('share to WeChat'),
              onPressed: () async {
                var response = await FlutterShareMe().shareToWeChat(msg: 'Hello Flutter', title: 'Sharing Title');
                if (response == 'success') {
                  print('navigate success');
                }
              },
            ),
            RaisedButton(
              child: Text('share to System'),
              onPressed: () async {
                var response = await FlutterShareMe().shareToSystem(msg: 'Hello Flutter');
                if (response == 'success') {
                  print('navigate success');
                }
              },
            ),
          ],
        ),
      ),
    );
  }
}
