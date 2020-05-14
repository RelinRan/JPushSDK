package com.android.jpush;

import android.os.Bundle;

/**
 * Author: Relin
 * Describe:消息监听
 * Date:2020/5/14 16:17
 */
public interface OnJPushMessageListener {

    /**
     * 消息注册
     *
     * @param bundle
     * @param id
     */
    void onRegistrationId(Bundle bundle, String id);

    /**
     * 自定义消息
     *
     * @param bundle  参数
     * @param title   标题
     * @param message 消息
     * @param extras  额外参数
     */
    void onMessageReceived(Bundle bundle, String title, String message, String extras);

    /**
     * 通知消息
     *
     * @param bundle  参数
     * @param title   标题
     * @param message 消息
     * @param extras  额外参数
     */
    void onNotificationReceived(Bundle bundle, String title, String message, String extras);

    /**
     * 通知栏点击
     *
     * @param bundle 参数
     */
    void onNotificationOpened(Bundle bundle);

    /**
     * 富文本
     *
     * @param bundle 参数
     */
    void onRichPushCallback(Bundle bundle);

    /**
     * 连接状态
     *
     * @param bundle      参数
     * @param isConnected 是否连接
     */
    void onConnectionChange(Bundle bundle, boolean isConnected);

}
