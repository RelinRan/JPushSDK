package com.android.jpush;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;

public class AliasTags {

    /**
     * 序列
     */
    public static int sequence = UUID.randomUUID().variant();

    /**
     * 名称
     */
    public static String name;

    /**
     * 设置标签
     *
     * @param context
     * @param alias
     */
    public static void setAlias(Context context, String alias) {
        JPushInterface.setAlias(context.getApplicationContext(), sequence, alias);
    }

    /**
     * 设置标识
     *
     * @param context
     * @param tag
     */
    public static void setTags(Context context, String tag) {
        Set<String> set = new HashSet<>();
        set.add(tag);
        JPushInterface.setTags(context.getApplicationContext(), sequence, set);
    }

}
