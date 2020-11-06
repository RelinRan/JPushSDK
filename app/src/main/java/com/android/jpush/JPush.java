package com.android.jpush;

import android.content.Context;
import android.content.IntentFilter;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jiguang.api.JCoreInterface;
import cn.jpush.android.api.JPushInterface;

/**
 * Author: Relin
 * Describe:极光推送
 * Date:2020/5/14 13:28
 */
public class JPush {
    /**
     * 通知
     */
    public static final String START_TRANSIT = "transit.";
    /**
     * 操作Tag通知
     */
    public static final String ACTION_OPERATOR_TAG = "cn.android.push.action_tag_operator";
    /**
     * 操作检查Tag通知
     */
    public static final String ACTION_OPERATOR_CHECK_TAG = "cn.android.push.action_check_tag_operator";
    /**
     * 操作Alias通知
     */
    public static final String ACTION_OPERATOR_ALIAS = "cn.android.push.action_alias_operator";
    /**
     * 操作手机号通知
     */
    public static final String ACTION_OPERATOR_MOBILE_NUMBER = "cn.android.push.action_mobile_number_operator";
    /**
     * 操作Tag Alias 结果对象
     */
    public static final String EXTRA_OPERATOR_JPUSH_MESSAGE = "JPushMessage";
    /**
     * 消息ID
     */
    public static final String EXTRA_MSG_ID = JPushInterface.EXTRA_MSG_ID;
    /**
     * 自定义消息 - 标题
     */
    public static final String EXTRA_CUSTOM_TITLE = JPushInterface.EXTRA_TITLE;
    /**
     * 自定义消息 - 消息
     */
    public static final String EXTRA_CUSTOM_CONTENT = JPushInterface.EXTRA_MESSAGE;
    /**
     * 自定义消息 - 自定义字段
     */
    public static final String EXTRA_CUSTOM_EXTRAS = JPushInterface.EXTRA_EXTRA;
    /**
     * 通知栏信息 - 标题
     */
    public static final String EXTRA_NOTIFICATION_TITLE = JPushInterface.EXTRA_NOTIFICATION_TITLE;
    /**
     * 通知栏信息 - 内容
     */
    public static final String EXTRA_NOTIFICATION_CONTENT = JPushInterface.EXTRA_ALERT;
    /**
     * 通知栏信息 - 自定义字段
     */
    public static final String EXTRA_NOTIFICATION_EXTRAS = JPushInterface.EXTRA_EXTRA;

    /**
     * 设置调试模式
     *
     * @param debug
     */
    public static void setDebugMode(boolean debug) {
        JPushInterface.setDebugMode(debug);
    }

    /**
     * 是否调试模式
     *
     * @return
     */
    public static boolean debugMode() {
        return JCoreInterface.getDebugMode();
    }


    /**
     * 初始化SDK
     *
     * @param context 上下文对象
     */
    public static void init(Context context) {
        JPushInterface.init(context);
    }

    /**
     * 初始化标签和别名
     *
     * @param context
     * @param value
     */
    public static void initAliasTag(Context context, String value) {
        int sequence = JPushUser.sequence(context);
        JPushUser.setValue(context, value);
        JPushUser.setSequence(context, String.valueOf(sequence));
        create(context, value, sequence);
    }

    /**
     * 创建推送
     *
     * @param context
     */
    private static void create(Context context, String value, int sequence) {
        resumePush(context);
        AliasTags.sequence = sequence;
        AliasTags.name = value;
        TagAliasHelper.with(context.getApplicationContext()).addAlias(value);
        TagAliasHelper.with(context.getApplicationContext()).addTags(value);
        //设置标签
        AliasTags.setAlias(context, value);
        AliasTags.setTags(context, value);
    }

    /**
     * 恢复推送
     *
     * @param context
     */
    public static void resumePush(Context context) {
        if (JPushInterface.isPushStopped(context.getApplicationContext())) {
            JPushInterface.resumePush(context.getApplicationContext());
        }
    }

    /**
     * 停止推送
     *
     * @param context
     */
    public static void stopPush(Context context) {
        JPushInterface.stopPush(context.getApplicationContext());
    }

    /**
     * 退出登录
     *
     * @param context
     */
    public static void logout(Context context) {
        TagAliasHelper.with(context.getApplicationContext()).removeTagAlias();
        JPushInterface.stopPush(context.getApplicationContext());
    }

    /**
     * 推送监听
     */
    private static JPushTransitReceiver receiver;

    private static OnJPushMessageListener messageListener;
    private static OnJPushAliasTagsListener aliasTagsListener;
    /**
     * 添加消息监听
     *
     * @param context
     * @param onJPushMessageListener
     */
    public static void addJPushMessageListener(Context context, OnJPushMessageListener onJPushMessageListener) {
        messageListener =onJPushMessageListener;
        addJPushListener(context, messageListener, aliasTagsListener);
    }

    /**
     * 添加Alias和Tag操作监听
     *
     * @param context
     * @param onJPushAliasTagsListener
     */
    public static void addJPushAliasTagsListener(Context context, OnJPushAliasTagsListener onJPushAliasTagsListener) {
        aliasTagsListener = onJPushAliasTagsListener;
        addJPushListener(context, messageListener, aliasTagsListener);
    }

    /**
     * 添加推送监听
     *
     * @param context
     * @param onJPushMessageListener
     * @param onJPushAliasTagsListener
     */
    public static void addJPushListener(Context context, OnJPushMessageListener onJPushMessageListener, OnJPushAliasTagsListener onJPushAliasTagsListener) {
        messageListener =onJPushMessageListener;
        aliasTagsListener = onJPushAliasTagsListener;
        if (receiver == null) {
            receiver = new JPushTransitReceiver();
            receiver.setOnJPushMessageListener(onJPushMessageListener);
            receiver.setOnJPushAliasTagsListener(onJPushAliasTagsListener);
            IntentFilter filter = new IntentFilter();
            //消息通知
            filter.addAction(JPush.START_TRANSIT + JPushInterface.ACTION_REGISTRATION_ID);
            filter.addAction(JPush.START_TRANSIT + JPushInterface.ACTION_MESSAGE_RECEIVED);
            filter.addAction(JPush.START_TRANSIT + JPushInterface.ACTION_NOTIFICATION_RECEIVED);
            filter.addAction(JPush.START_TRANSIT + JPushInterface.ACTION_NOTIFICATION_OPENED);
            filter.addAction(JPush.START_TRANSIT + JPushInterface.ACTION_RICHPUSH_CALLBACK);
            filter.addAction(JPush.START_TRANSIT + JPushInterface.ACTION_CONNECTION_CHANGE);
            //Tag Alias 通知
            filter.addAction(JPush.ACTION_OPERATOR_TAG);
            filter.addAction(JPush.ACTION_OPERATOR_CHECK_TAG);
            filter.addAction(JPush.ACTION_OPERATOR_ALIAS);
            filter.addAction(JPush.ACTION_OPERATOR_MOBILE_NUMBER);
            //注册
            context.registerReceiver(receiver, filter);
        } else {
            receiver.setOnJPushMessageListener(onJPushMessageListener);
            receiver.setOnJPushAliasTagsListener(onJPushAliasTagsListener);
        }
    }

    /**
     * 移除推送监听
     *
     * @param context
     */
    public static void destroy(Context context) {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
        TagAliasHelper.with(context).destroy();
    }

}
