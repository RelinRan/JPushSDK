package com.android.jpush;

import android.os.Bundle;

/**
 * Author: Relin
 * Describe:消息监听
 * Date:2020/5/14 16:17
 */
public interface OnJPushMessageListener {

    void onRegistrationId(Bundle bundle, String id);

    void onMessageReceived(Bundle bundle, String title, String message, String extras);

    void onNotificationReceived(Bundle bundle, String title, String message, String extras);

    void onNotificationOpened(Bundle bundle);

    void onRichPushCallback(Bundle bundle);

    void onConnectionChange(Bundle bundle, boolean isConnected);

}
