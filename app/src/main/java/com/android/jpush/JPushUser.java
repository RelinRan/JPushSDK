package com.android.jpush;

import android.content.Context;

import java.util.UUID;

public class JPushUser {

    /**
     * 获取用户值
     *
     * @param context
     * @return
     */
    public static String value(Context context) {
        return JPushStorage.with(context.getApplicationContext()).getString("JPUSH_USER_VALUE", "");
    }

    /**
     * 设置用户值
     *
     * @param context 上下文
     * @param value   用户值
     */
    public static void setValue(Context context, String value) {
        JPushStorage.with(context.getApplicationContext()).put("JPUSH_USER_VALUE", value);
    }

    /**
     * 获取序列值
     *
     * @param context 上下文
     * @return
     */
    public static int sequence(Context context) {
        String sequence = JPushStorage.with(context.getApplicationContext()).getString("JPUSH_USER_SEQUENCE", UUID.randomUUID().variant() + "");
        return Integer.parseInt(sequence);
    }

    /**
     * 设置序列值
     *
     * @param context  上下文
     * @param sequence 序列值
     */
    public static void setSequence(Context context, String sequence) {
        JPushStorage.with(context.getApplicationContext()).put("JPUSH_USER_SEQUENCE", sequence);
    }


}
