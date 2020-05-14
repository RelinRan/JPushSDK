package com.android.jpush;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;

public class TagAliasHelper {

    public static TagAliasHelper helper;
    private Context context;

    public static final String ALIAS = "alias";
    public static final String SEQUENCE = "sequence";
    public static final String TAGS = "tags";
    public static final String TAG = "TagAliasHelper";
    public static final String LOG_NAME = "jpush_log.txt";

    public static final int CHECK_TAG_BIND_STATE = 0;
    public static final int SET_TAG_ALIAS = 1;
    private Set<String> set;

    private int sequence;

    private TagAliasHelper() {

    }

    private TagAliasHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    public static TagAliasHelper with(Context context) {
        if (helper == null) {
            synchronized (TagAliasHelper.class) {
                if (helper == null) {
                    helper = new TagAliasHelper(context);
                }
            }
        }
        return helper;
    }

    public Set<String> getTagsSet() {
        return set;
    }

    public void removeAlias() {
        JPushStorage.with(context.getApplicationContext()).put(ALIAS, "");
    }

    public void removeTags() {
        JPushStorage.with(context.getApplicationContext()).put(TAGS, "");
    }

    public void removeTagAlias() {
        JPushStorage.with(context.getApplicationContext()).put(ALIAS, "");
        JPushStorage.with(context.getApplicationContext()).put(TAGS, "");
    }

    public void addAlias(String alias) {
        sequence = JPushStorage.with(context.getApplicationContext()).getInt(SEQUENCE, UUID.randomUUID().variant());
        JPushStorage.with(context.getApplicationContext()).put(SEQUENCE, sequence);
        JPushStorage.with(context.getApplicationContext()).put(ALIAS, alias);
    }


    public void addTags(String tags) {
        sequence = JPushStorage.with(context.getApplicationContext()).getInt(SEQUENCE, UUID.randomUUID().variant());
        JPushStorage.with(context.getApplicationContext()).put(SEQUENCE, sequence);
        JPushStorage.with(context.getApplicationContext()).put(TAGS, tags);
    }


    public void addAliasTags(String name) {
        sequence = JPushStorage.with(context.getApplicationContext()).getInt(SEQUENCE, UUID.randomUUID().variant());
        JPushStorage.with(context.getApplicationContext()).put(SEQUENCE, sequence);
        JPushStorage.with(context.getApplicationContext()).put(ALIAS, name);
        JPushStorage.with(context.getApplicationContext()).put(TAGS, name);
    }

    public String getAlias() {
        return JPushStorage.with(context.getApplicationContext()).getString(ALIAS, "");
    }

    public String getTags() {
        return JPushStorage.with(context.getApplicationContext()).getString(TAGS, "");
    }

    public int getSequence() {
        return JPushStorage.with(context.getApplicationContext()).getInt(SEQUENCE, UUID.randomUUID().variant());
    }

    /**
     * tag 增删查改的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.i(TAG, "----[onTagOperatorResult]--->code:" + jPushMessage.getErrorCode() + ",tags:" + (jPushMessage.getTags() == null ? "" : jPushMessage.getTags().toString()));
        parseMessage(context, jPushMessage, false);
    }

    /**
     * 查询某个 tag 与当前用户的绑定状态的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.i(TAG, "-> onCheckTagOperatorResult code:" + jPushMessage.getErrorCode() + ",TagCheckStateResult:" + jPushMessage.getTagCheckStateResult() + ",tags:" + (jPushMessage.getTags() == null ? "" : jPushMessage.getTags().toString()));
        parseMessage(context, jPushMessage, true);
    }

    /**
     * alias 相关的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.i(TAG, "->onAliasOperatorResult code:" + jPushMessage.getErrorCode() + ",alias:" + jPushMessage.getAlias());

        parseMessage(context, jPushMessage, true);
    }

    /**
     * 设置手机号码会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.i(TAG, "->onMobileNumberOperatorResult code:" + jPushMessage.getErrorCode() + ",alias:" + jPushMessage.getAlias());
    }

    private void parseMessage(Context context, JPushMessage jPushMessage, boolean isReconnect) {
        if (jPushMessage.getErrorCode() == 0) {
            Log.i(TAG, "->设置成功");
        }
        if (jPushMessage.getErrorCode() == 6001 || jPushMessage.getTags() == null) {
            Log.i(TAG, "->无效的设置(3.0.7以前的旧接口设置 tag/alias 不应参数都为 null，3.0.7 开始的新 tag/alias 接口报此错误码表示 tag/alias 参数不能为空)");
            handler.sendEmptyMessage(SET_TAG_ALIAS);
        }
        if (!isReconnect) {
            return;
        }
        if (jPushMessage.getErrorCode() == 6012 || jPushMessage.getAlias() == null) {
            Log.i(TAG, "->无效的设置(3.0.7以前的旧接口设置 tag/alias 不应参数都为 null，3.0.7 开始的新 tag/alias 接口报此错误码表示 tag/alias 参数不能为空)");
            handler.sendEmptyMessage(SET_TAG_ALIAS);
        }
        if (jPushMessage.getErrorCode() == 6022) {
            Log.i(TAG, "->alias 操作正在进行中，暂时不能进行其他 alias 操作。(3.0.7 版本新增的错误码，多次调用 alias 相关的 API，请在获取到上一次调用回调后再做下一次操作；在未取到回调的情况下，等待 20 秒后再做下一次操作。)");
            handler.sendEmptyMessageDelayed(CHECK_TAG_BIND_STATE, 20 * 1000);
        }
        if (jPushMessage.getErrorCode() == 6014 || jPushMessage.getErrorCode() == 6024) {
            if (jPushMessage.getErrorCode() == 6014) {
                Log.i(TAG, "->服务器繁忙,建议重试。(3.0.7 版本新增的错误码)");
                handler.sendEmptyMessageDelayed(CHECK_TAG_BIND_STATE, 60 * 1000);
            }
            if (jPushMessage.getErrorCode() == 6024) {
                Log.i(TAG, "->服务器内部错误。(3.1.1 版本新增的错误码；服务器内部错误，过一段时间再重试。)");
                handler.sendEmptyMessageDelayed(CHECK_TAG_BIND_STATE, 60 * 1000);
            }
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHECK_TAG_BIND_STATE:
                    Log.i(TAG, "->checkTagBindState sequence：" + getSequence() + ",alias:" + getAlias());
                    JPushInterface.checkTagBindState(context.getApplicationContext(), getSequence(), getAlias());
                    break;
                case SET_TAG_ALIAS:
                    JPushInterface.setAlias(context.getApplicationContext(), getSequence(), getAlias());
                    set = new LinkedHashSet<>();
                    set.add(getTags());
                    JPushInterface.setTags(context.getApplicationContext(), getSequence(), set);
                    break;
            }
        }
    };


}
