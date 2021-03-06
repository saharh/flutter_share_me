package zhuoyuan.li.fluttershareme;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterShareMePlugin
 */
public class FlutterShareMePlugin implements MethodCallHandler {

    private Activity activity;
    private static CallbackManager callbackManager;

    /**
     * Plugin registration.
     */
    private FlutterShareMePlugin(MethodChannel channel, Activity activity) {
        MethodChannel _channel = channel;
        this.activity = activity;
        _channel.setMethodCallHandler(this);
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_share_me");
        channel.setMethodCallHandler(new FlutterShareMePlugin(channel, registrar.activity()));
        callbackManager = CallbackManager.Factory.create();
    }

    /**
     * method
     *
     * @param call
     * @param result
     */
    @Override
    public void onMethodCall(MethodCall call, Result result) {
        String url, msg, title;

        switch (call.method) {
            case "shareFacebook":
                url = call.argument("url");
                msg = call.argument("msg");
                shareToFacebook(url, msg, result);
                break;
            case "shareTwitter":
                url = call.argument("url");
                msg = call.argument("msg");
                shareToTwitter(url, msg, result);
                break;
            case "shareWhatsApp":
                msg = call.argument("msg");
                shareWhatsApp(msg, result);
                break;
            case "shareWeChat":
                title = call.argument("title");
                msg = call.argument("msg");
                shareWeChat(msg, title, result);
                break;
            case "system":
                title = call.argument("title");
                msg = call.argument("msg");
                shareSystem(result, title, msg);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    /**
     * 调用系统分享
     *
     * @param msg    String
     * @param result Result
     */
    private void shareSystem(Result result, String title, String msg) {
        activity.runOnUiThread(() -> {
            try {
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, msg);
                activity.startActivity(Intent.createChooser(textIntent, title));
                result.success("success");
            } catch (Exception var7) {
                result.error("error", var7.toString(), "");
            }
        });
    }

    /**
     * share to twitter
     *
     * @param url    String
     * @param msg    String
     * @param result Result
     */
    private void shareToTwitter(String url, String msg, Result result) {
        //这里分享一个链接，更多分享配置参考官方介绍：https://dev.twitter.com/twitterkit/android/compose-tweets
        activity.runOnUiThread(() -> {
            try {
                TweetComposer.Builder builder = new TweetComposer.Builder(activity)
                        .text(msg);
                if (url != null && url.length() > 0) {
                    builder.url(new URL(url));
                }

                builder.show();
                result.success("success");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                result.error("error", e.toString(), "");
            }
        });
    }

    /**
     * share to Facebook
     *
     * @param url    String
     * @param msg    String
     * @param result Result
     */
    private void shareToFacebook(String url, String msg, Result result) {
//        FacebookSdk.setApplicationId("158255858300344");
//        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        // this part is optional
//        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//            @Override
//            public void onSuccess(Sharer.Result result) {
//                System.out.println("--------------------success");
//
//            }
//
//            @Override
//            public void onCancel() {
//                System.out.println("-----------------onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                System.out.println("---------------onError");
//            }
//        });
        activity.runOnUiThread(() -> {
            ShareDialog shareDialog = new ShareDialog(activity);
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(url))
                    .setQuote(msg)
                    .build();
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                result.success("success");
            }
        });
    }

    /**
     * share to whatsapp
     *
     * @param msg    String
     * @param result Result
     */
    private void shareWhatsApp(String msg, Result result) {
        activity.runOnUiThread(() -> {
            try {
                Intent textIntent;
                textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.setPackage("com.whatsapp");
                textIntent.putExtra(Intent.EXTRA_TEXT, msg);
                activity.startActivity(textIntent);
                result.success("success");
            } catch (Exception var9) {
                result.error("error", var9.toString(), "");
            }
        });
    }

    private void shareWeChat(String msg, String title, Result result) {
        activity.runOnUiThread(() -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (!resInfo.isEmpty()) {
                    List<Intent> targetedShareIntents = new ArrayList<>();
                    for (ResolveInfo info : resInfo) {
                        Intent targeted = new Intent(Intent.ACTION_SEND);
                        targeted.setType("text/plain");
                        ActivityInfo activityInfo = info.activityInfo;
                        // Shared content
                        targeted.putExtra(Intent.EXTRA_TEXT, msg);
                        // Shared headlines
//                    targeted.putExtra(Intent.EXTRA_SUBJECT, "theme");
                        targeted.setPackage(activityInfo.packageName);
                        targeted.setClassName(activityInfo.packageName, info.activityInfo.name);
                        PackageManager pm = activity.getApplication().getPackageManager();
                        // Wechat has two distinctions. - Friendship circle and Wechat
                        if (info.activityInfo.applicationInfo.loadLabel(pm).toString().equals("WeChat") || info.activityInfo.packageName.contains("tencent.mm")) {
                            targetedShareIntents.add(targeted);
                        }
                    }
                    if (targetedShareIntents.isEmpty()) {
                        result.success("success");
                        return;
                    }
                    Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), title);
                    if (chooserIntent == null) {
                        result.success("success");
                        return;
                    }
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                    activity.startActivity(chooserIntent);
                }
                result.success("success");
            } catch (Exception var9) {
                result.error("error", var9.toString(), "");
            }
        });
    }
}
