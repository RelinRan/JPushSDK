package com.android.jpush;

import android.content.Context;

import cn.jpush.android.api.JPushMessage;

/**
 * Author: Relin
 * Describe:消息监听
 * Date:2020/5/14 16:17
 */
public interface OnJPushAliasTagsListener {

    /**
     * Tag和Alias增删查改的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    void onTagOperatorResult(Context context, JPushMessage jPushMessage);

    /**
     * 查询某个Tag与当前用户的绑定状态的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage);

    /**
     * Alias相关的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    void onAliasOperatorResult(Context context, JPushMessage jPushMessage);

    /**
     * 设置手机号码会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage);

}
