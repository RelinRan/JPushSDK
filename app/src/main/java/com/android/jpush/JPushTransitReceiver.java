package com.android.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;

/**
 * Author: Relin
 * Describe:消息接收器监听
 * Date:2020/5/14 16:16
 */
public class JPushTransitReceiver extends BroadcastReceiver {

    private final String TAG = "JPushTransitReceiver";
    private OnJPushMessageListener onJPushMessageListener;
    private OnJPushAliasTagsListener onJPushAliasTagsListener;

    public void setOnJPushMessageListener(OnJPushMessageListener onJPushMessageListener) {
        this.onJPushMessageListener = onJPushMessageListener;
    }

    public void setOnJPushAliasTagsListener(OnJPushAliasTagsListener onJPushAliasTagsListener) {
        this.onJPushAliasTagsListener = onJPushAliasTagsListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        //推送注册ID
        if ((JPush.START_TRANSIT + JPushInterface.ACTION_REGISTRATION_ID).equals(intent.getAction())) {
            String id = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            if (onJPushMessageListener != null) {
                onJPushMessageListener.onRegistrationId(bundle, id);
            }
        }
        //自定义消息
        else if ((JPush.START_TRANSIT + JPushInterface.ACTION_MESSAGE_RECEIVED).equals(intent.getAction())) {
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (onJPushMessageListener != null) {
                onJPushMessageListener.onMessageReceived(bundle, title, message, extras);
            }
        }
        //通知栏消息
        else if ((JPush.START_TRANSIT + JPushInterface.ACTION_NOTIFICATION_RECEIVED).equals(intent.getAction())) {
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (onJPushMessageListener != null) {
                onJPushMessageListener.onNotificationReceived(bundle, title, message, extras);
            }
        }
        //点开通知
        else if ((JPush.START_TRANSIT + JPushInterface.ACTION_NOTIFICATION_OPENED).equals(intent.getAction())) {
            if (onJPushMessageListener != null) {
                onJPushMessageListener.onNotificationOpened(bundle);
            }
        }
        //富文本
        else if ((JPush.START_TRANSIT + JPushInterface.ACTION_RICHPUSH_CALLBACK).equals(intent.getAction())) {
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            if (onJPushMessageListener != null) {
                onJPushMessageListener.onRichPushCallback(bundle);
            }
        }
        //连接改变
        else if ((JPush.START_TRANSIT + JPushInterface.ACTION_CONNECTION_CHANGE).equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            if (onJPushMessageListener != null) {
                onJPushMessageListener.onConnectionChange(bundle, connected);
            }
        }
        //Tag和Alias增删查改的操作会在此方法中回调结果。
        else if (JPush.ACTION_OPERATOR_TAG.equals(intent.getAction())) {
            JPushMessage jPushMessage = (JPushMessage) intent.getSerializableExtra(JPush.EXTRA_OPERATOR_JPUSH_MESSAGE);
            if (onJPushAliasTagsListener != null) {
                onJPushAliasTagsListener.onTagOperatorResult(context, jPushMessage);
            }
        }
        //查询某个Tag与当前用户的绑定状态的操作会在此方法中回调结果。
        else if (JPush.ACTION_OPERATOR_CHECK_TAG.equals(intent.getAction())) {
            JPushMessage jPushMessage = (JPushMessage) intent.getSerializableExtra(JPush.EXTRA_OPERATOR_JPUSH_MESSAGE);
            if (onJPushAliasTagsListener != null) {
                onJPushAliasTagsListener.onCheckTagOperatorResult(context, jPushMessage);
            }
        }
        //Alias相关的操作会在此方法中回调结果。
        else if (JPush.ACTION_OPERATOR_ALIAS.equals(intent.getAction())) {
            JPushMessage jPushMessage = (JPushMessage) intent.getSerializableExtra(JPush.EXTRA_OPERATOR_JPUSH_MESSAGE);
            if (onJPushAliasTagsListener != null) {
                onJPushAliasTagsListener.onAliasOperatorResult(context, jPushMessage);
            }
        }
        //设置手机号码会在此方法中回调结果。
        else if (JPush.ACTION_OPERATOR_MOBILE_NUMBER.equals(intent.getAction())) {
            JPushMessage jPushMessage = (JPushMessage) intent.getSerializableExtra(JPush.EXTRA_OPERATOR_JPUSH_MESSAGE);
            if (onJPushAliasTagsListener != null) {
                onJPushAliasTagsListener.onMobileNumberOperatorResult(context, jPushMessage);
            }
        }
    }

}
