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

    /**
     * 序列
     */
    private int sequence;
    /**
     * Tags
     */
    private Set<String> set;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 调试模式
     */
    public static boolean debug = false;
    /**
     * 助手
     */
    public static TagAliasHelper helper;
    /**
     * 别名
     */
    public static final String ALIAS = "alias";
    /**
     * 序列
     */
    public static final String SEQUENCE = "sequence";
    /**
     * 标识
     */
    public static final String TAGS = "tags";
    /**
     * 日志标识
     */
    public static final String TAG = "TagAliasHelper";
    /**
     * 检查绑定状态
     */
    public static final int CHECK_TAG_BIND_STATE = 0;
    /**
     * 设置别名标识
     */
    public static final int SET_TAG_ALIAS = 1;

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

    /**
     * 设置调试模式
     *
     * @param debug
     */
    public static void debug(boolean debug) {
        TagAliasHelper.debug = debug;
    }

    /**
     * 获取标识
     *
     * @return
     */
    public Set<String> getTagsSet() {
        return set;
    }

    /**
     * 删除别名
     */
    public void removeAlias() {
        if (context==null){
            return;
        }
        JPushStorage.with(context.getApplicationContext()).put(ALIAS, "");
    }

    /**
     * 删除标识
     */
    public void removeTags() {
        if (context==null){
            return;
        }
        JPushStorage.with(context.getApplicationContext()).put(TAGS, "");
    }

    /**
     * 删除别名标识
     */
    public void removeTagAlias() {
        if (context==null){
            return;
        }
        JPushStorage.with(context.getApplicationContext()).put(ALIAS, "");
        JPushStorage.with(context.getApplicationContext()).put(TAGS, "");
    }

    /**
     * 添加别名
     *
     * @param alias
     */
    public void addAlias(String alias) {
        if (context==null){
            return;
        }
        sequence = JPushStorage.with(context.getApplicationContext()).getInt(SEQUENCE, UUID.randomUUID().variant());
        JPushStorage.with(context.getApplicationContext()).put(SEQUENCE, sequence);
        JPushStorage.with(context.getApplicationContext()).put(ALIAS, alias);
    }

    /**
     * 添加标识
     *
     * @param tags 标识
     */
    public void addTags(String tags) {
        if (context==null){
            return;
        }
        sequence = JPushStorage.with(context.getApplicationContext()).getInt(SEQUENCE, UUID.randomUUID().variant());
        JPushStorage.with(context.getApplicationContext()).put(SEQUENCE, sequence);
        JPushStorage.with(context.getApplicationContext()).put(TAGS, tags);
    }

    /**
     * 添加别名标识
     *
     * @param name 别名标识
     */
    public void addAliasTags(String name) {
        if (context==null){
            return;
        }
        sequence = JPushStorage.with(context.getApplicationContext()).getInt(SEQUENCE, UUID.randomUUID().variant());
        JPushStorage.with(context.getApplicationContext()).put(SEQUENCE, sequence);
        JPushStorage.with(context.getApplicationContext()).put(ALIAS, name);
        JPushStorage.with(context.getApplicationContext()).put(TAGS, name);
    }

    /**
     * 获取别名
     *
     * @return
     */
    public String getAlias() {
        if (context==null){
            return "";
        }
        return JPushStorage.with(context.getApplicationContext()).getString(ALIAS, "");
    }

    /**
     * 获取标识
     *
     * @return
     */
    public String getTags() {
        if (context==null){
            return null;
        }
        return JPushStorage.with(context.getApplicationContext()).getString(TAGS, "");
    }

    /**
     * 获取序列
     *
     * @return
     */
    public int getSequence() {
        return JPushStorage.with(context.getApplicationContext()).getInt(SEQUENCE, UUID.randomUUID().variant());
    }

    /**
     * tag 增删查改的操作会在此方法中回调结果。
     *
     * @param context      上下文对象
     * @param jPushMessage 推送消息
     */
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        if (debug) {
            Log.i(TAG, "----[onTagOperatorResult]--->code:" + jPushMessage.getErrorCode() + ",tags:" + (jPushMessage.getTags() == null ? "" : jPushMessage.getTags().toString()));
        }
        parseMessage(context, jPushMessage, false);
    }

    /**
     * 查询某个 tag 与当前用户的绑定状态的操作会在此方法中回调结果。
     *
     * @param context      上下文对象
     * @param jPushMessage 推送消息
     */
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        if (debug) {
            Log.i(TAG, "-> onCheckTagOperatorResult code:" + jPushMessage.getErrorCode() + ",TagCheckStateResult:" + jPushMessage.getTagCheckStateResult() + ",tags:" + (jPushMessage.getTags() == null ? "" : jPushMessage.getTags().toString()));
        }
        parseMessage(context, jPushMessage, true);
    }

    /**
     * alias 相关的操作会在此方法中回调结果。
     *
     * @param context      上下文对象
     * @param jPushMessage 推送消息
     */
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        if (debug) {
            Log.i(TAG, "->onAliasOperatorResult code:" + jPushMessage.getErrorCode() + ",alias:" + jPushMessage.getAlias());
        }
        parseMessage(context, jPushMessage, true);
    }

    /**
     * 设置手机号码会在此方法中回调结果。
     *
     * @param context      上下文对象
     * @param jPushMessage 推送消息
     */
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        if (debug) {
            Log.i(TAG, "->onMobileNumberOperatorResult code:" + jPushMessage.getErrorCode() + ",alias:" + jPushMessage.getAlias());
        }
    }


    /**
     * 解析消息
     *
     * @param context      上下文对象
     * @param jPushMessage 推送消息
     * @param isReconnect  是否连接
     */
    private void parseMessage(Context context, JPushMessage jPushMessage, boolean isReconnect) {
        if (jPushMessage.getErrorCode() == 0) {
            if (debug) {
                Log.i(TAG, "->设置成功");
            }
        }
        if (jPushMessage.getErrorCode() == 6001 || jPushMessage.getTags() == null) {
            if (debug) {
                Log.i(TAG, "->无效的设置(3.0.7以前的旧接口设置 tag/alias 不应参数都为 null，3.0.7 开始的新 tag/alias 接口报此错误码表示 tag/alias 参数不能为空)");
            }
            if (handler != null) {
                handler.sendEmptyMessage(SET_TAG_ALIAS);
            }
        }
        if (!isReconnect) {
            return;
        }
        if (jPushMessage.getErrorCode() == 6012 || jPushMessage.getAlias() == null) {
            if (debug) {
                Log.i(TAG, "->无效的设置(3.0.7以前的旧接口设置 tag/alias 不应参数都为 null，3.0.7 开始的新 tag/alias 接口报此错误码表示 tag/alias 参数不能为空)");
            }
            if (handler != null) {
                handler.sendEmptyMessage(SET_TAG_ALIAS);
            }
        }
        if (jPushMessage.getErrorCode() == 6022) {
            if (debug) {
                Log.i(TAG, "->alias 操作正在进行中，暂时不能进行其他 alias 操作。(3.0.7 版本新增的错误码，多次调用 alias 相关的 API，请在获取到上一次调用回调后再做下一次操作；在未取到回调的情况下，等待 20 秒后再做下一次操作。)");
            }
            if (handler != null) {
                handler.sendEmptyMessageDelayed(CHECK_TAG_BIND_STATE, 20 * 1000);
            }
        }
        if (jPushMessage.getErrorCode() == 6014 || jPushMessage.getErrorCode() == 6024) {
            if (jPushMessage.getErrorCode() == 6014) {
                if (debug) {
                    Log.i(TAG, "->服务器繁忙,建议重试。(3.0.7 版本新增的错误码)");
                }
                if (handler != null) {
                    handler.sendEmptyMessageDelayed(CHECK_TAG_BIND_STATE, 60 * 1000);
                }
            }
            if (jPushMessage.getErrorCode() == 6024) {
                if (debug) {
                    Log.i(TAG, "->服务器内部错误。(3.1.1 版本新增的错误码；服务器内部错误，过一段时间再重试。)");
                }
                if (handler != null) {
                    handler.sendEmptyMessageDelayed(CHECK_TAG_BIND_STATE, 60 * 1000);
                }
            }
        }
    }

    /**
     * 消息Handler
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHECK_TAG_BIND_STATE:
                    if (debug) {
                        Log.i(TAG, "->checkTagBindState sequence：" + getSequence() + ",alias:" + getAlias());
                    }
                    if (context != null) {
                        JPushInterface.checkTagBindState(context.getApplicationContext(), getSequence(), getAlias());
                    }
                    break;
                case SET_TAG_ALIAS:
                    if (context!=null){
                        JPushInterface.setAlias(context.getApplicationContext(), getSequence(), getAlias());
                        set = new LinkedHashSet<>();
                        set.add(getTags());
                        JPushInterface.setTags(context.getApplicationContext(), getSequence(), set);
                    }
                    break;
            }
        }
    };

    /**
     * 销毁对象
     */
    public void destroy() {
        if (handler != null) {
            handler.removeMessages(CHECK_TAG_BIND_STATE);
            handler.removeMessages(SET_TAG_ALIAS);
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

}
