package com.android.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Author: Relin
 * Describe:极光消息
 * Date:2020/5/14 14:28
 */
public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPushReceiver";
    private String extras;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (JPush.debugMode()){
                Log.i(TAG, "->onReceive  action: " + intent.getAction() + ", extras: " + printBundle(bundle));
            }
            if (context!=null){
                Intent receiverIntent = new Intent(JPush.START_TRANSIT+intent.getAction());
                receiverIntent.putExtras(bundle);
                context.sendBroadcast(receiverIntent);
            }
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                if (JPush.debugMode()){
                    Log.i(TAG, "->registration Id : " + regId);
                }
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                if (JPush.debugMode()){
                    Log.i(TAG, "->custom message: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                }
                processCustomMessage(context, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                //保存服务器推送下来的通知的标题。
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                //保存服务器推送下来的通知内容。
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                //保存服务器推送下来的附加字段。这是个 JSON 字符串。
                extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                if (JPush.debugMode()){
                    Log.i(TAG, "->notification message title:" + title + ",content：" + content + ",extras：" + extras);
                }
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                if (JPush.debugMode()){
                    Log.i(TAG, "->opened notification");
                }
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                if (JPush.debugMode()){
                    Log.i(TAG, "->rich push callback: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                }
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                if (JPush.debugMode()){
                    Log.i(TAG, "->connected state change to " + connected);
                }
            } else {
                if (JPush.debugMode()){
                    Log.i(TAG, "->Unhandled intent - " + intent.getAction());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印所有数据
     *
     * @param bundle
     * @return
     */
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\n->key:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\n->key:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    if (JPush.debugMode()){
                        Log.i(TAG, "->This message has no Extra data");
                    }
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\n->key:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.i(TAG, "->Get message extra JSON error!");
                }
            } else {
                sb.append("\n->key:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    /**
     * 处理自定义消息
     *
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (JPush.debugMode()){
            Log.i(TAG, "->processCustomMessage msgId:" + msgId + ",title:" + title + ",message:" + message + ",extras:" + extras);
        }
    }

}
