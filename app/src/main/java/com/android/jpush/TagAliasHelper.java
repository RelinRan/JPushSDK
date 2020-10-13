package com.android.jpush;

import android.content.Context;
import android.content.IntentFilter;
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
    public static boolean debug = JPush.debugMode();
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
    public static final String TAG = "JPush-TagAliasHelper";
    /**
     * 检查绑定状态
     */
    public static final int CHECK_TAG_BIND_STATE = 0;
    /**
     * 设置别名标识
     */
    public static final int SET_TAG_ALIAS = 1;

    //https://docs.jiguang.cn/jpush/client/Android/android_api/#_250
    public static final int codeArr[] = {
            6001,
            6002,
            6003,
            6004,
            6005,

            6006,
            6007,
            6008,
            6009,
            6011,

            6012,
            6013,
            6014,
            6015,
            6016,

            6017,
            6018,
            6019,
            6020,
            6021,

            6022,
            6023,
            6024,
            6025,
            6026,

            60227,
            -997,
            1005,
            1008,
            1009,

            -996,
            -994,
    };
    public static final String msgArr[] = {
            "描述：无效的设置   \n|详细解释：3.0.7 以前的旧接口设置 tag/alias 不应参数都为 null，3.0.7 开始的新 tag/alias 接口报此错误码表示 tag/alias 参数不能为空",
            "描述：设置超时    \n|详细解释：(建议重试，一般出现在网络不佳、初始化尚未完成时。",
            "描述：Alias字符串不合法 \n|详细解释：有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符( 2.1.6 支持)@!#$&*+=.|",
            "描述：Alias超长，最多40个字节 \n|详细解释：中文 UTF-8 是 3 个字节",
            "描述：某一个 tag 字符串不合法 \n|详细解释：有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符( 2.1.6 支持)@!#$&*+=.|",

            "描述：某一个tag超长,一个tag最多 40个字节  \n|详细解释：中文 UTF-8 是 3 个字节",
            "描述：tag 超出总长度限制 \n|详细解释：3.0.7 版本中新增 tag/alias 接口最长度最多 5000 字节，tag/alias 老接口总长度最多 7000 字节",
            "描述：tag 超出总长度限制    \n|详细解释：3.0.7 版本中新增 tag/alias 接口最长度最多 5000 字节，tag/alias 老接口总长度最多 7000 字节",
            "描述：未知错误    \n|详细解释：由于权限问题，导致的 PushService 启动异常，客户端日志中将有详细的报错信息，可据此排查。",
            "描述：短时间内操作过于频繁  \n|详细解释：10s 内设置 tag 或 alias 大于 10 次，或 10s 内设置手机号码大于 3 次",

            "描述：在JPush服务stop状态下设置了tag或alias或手机号码    \n|详细解释：3.0.0 版本新增的错误码，调了 stopPush 必须调用 resumePush 恢复服务后方可调用其他的 API，开发者可根据这个错误码的信息做相关处理或者提示。",
            "描述：用户设备时间轴异常    \n|详细解释：3.0.6 版本新增的错误码。设备本地时间轴异常变化影响了设置频率。",
            "描述：服务器繁忙,建议重试    \n|详细解释：3.0.7 版本新增的错误码",
            "描述：appkey 在黑名单中    \n|详细解释：3.0.7 版本新增，该 appkey 在黑名单中，请联系\b support 解除",
            "描述：无效用户    \n|详细解释：3.0.7 版本新增的错误码",

            "描述：无效请求    \n|详细解释：3.0.7 版本新增的错误码",
            "描述：Tags 过多    \n|详细解释：3.0.7 版本新增，该设备设置的 tag 数超过 1000 个，建议先清除部分 tag",
            "描述：查询请求已过期   \n|详细解释：3.0.7 版本新增的错误码",
            "描述：tag/alias 操作暂停   \n|详细解释：3.0.7 版本新增的错误码，建议过一段时间再设置",
            "描述：tags 操作正在进行中，暂时不能进行其他 tags 操作   \n|详细解释：3.0.7 版本新增的错误码，多次调用 tag 相关的 API，请在获取到上一次调用回调后再做下一次操作；在未取到回调的情况下，等待 20 秒后再做下一次操作。",

            "描述：alias 操作正在进行中，暂时不能进行其他 alias 操作   \n|详细解释：3.0.7 版本新增的错误码，多次调用 alias 相关的 API，请在获取到上一次调用回调后再做下一次操作；在未取到回调的情况下，等待 20 秒后再做下一次操作。",
            "描述：手机号码不合法   \n|详细解释：3.1.1 版本新增的错误码；只能以 “+” 或者 数字开头；后面的内容只能包含 “-” 和 数字。",
            "描述：服务器内部错误   \n|详细解释：3.1.1 版本新增的错误码；服务器内部错误，过一段时间再重试。",
            "描述：手机号码太长   \n|详细解释：3.1.1 版本新增的错误码；手机号码过长，目前极光检测手机号码的最大长度为 20。",
            "描述：数据包体过大   \n|详细解释：3.1.5 版本新增的错误码；数据包体过大，目前极光支持的数据通信包体最大为 8128。",

            "描述：别名绑定的设备数超过限制   \n|详细解释：3.5.8 版本新增的错误码；极光于 2020/03/10 对「别名设置」的上限进行限制，最多允许绑定 10 个设备，如需更高上限，请联系商务。",
            "描述：注册失败/登录失败   \n|详细解释：（一般是由于没有网络造成的）如果确保设备网络正常，还是一直遇到此问题，则还有另外一个原因：JPush 服务器端拒绝注册。而这个的原因一般是：你当前 App 的 Android 包名以及 AppKey，与你在 Portal 上注册的应用的 Android 包名与 AppKey 不相同。",
            "描述：包名和 AppKey 不匹配   \n|详细解释：请检查客户端配置的包名与官网对应 Appkey 应用下配置的包名是否一致",
            "描述：AppKey 非法   \n|详细解释：请到官网检查此应用信息中的 appkey，确认无误",
            "描述：当前的 appkey 下没有创建 Android 应用；你所使用的 SDK 版本低于 1.8.2。   \n|详细解释：请到官网检查此应用的应用详情；更新应用中集成的极光 SDK 至最新。",

            "描述：网络连接断开   \n|详细解释：如果确保设备网络正常，可能是由于包名不正确，服务器强制断开客户端的连接。",
            "描述：网络连接超时   \n|详细解释：如果确保设备网络正常，可能是由于包名不正确，服务器强制断开客户端的连接。",
    };

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
        if (context == null) {
            return;
        }
        JPushStorage.with(context.getApplicationContext()).put(ALIAS, "");
    }

    /**
     * 删除标识
     */
    public void removeTags() {
        if (context == null) {
            return;
        }
        JPushStorage.with(context.getApplicationContext()).put(TAGS, "");
    }

    /**
     * 删除别名标识
     */
    public void removeTagAlias() {
        if (context == null) {
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
        if (context == null) {
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
        if (context == null) {
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
        if (context == null) {
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
        if (context == null) {
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
        if (context == null) {
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
        parseMessage("onTagOperatorResult", jPushMessage);
    }

    /**
     * 查询某个 tag 与当前用户的绑定状态的操作会在此方法中回调结果。
     *
     * @param context      上下文对象
     * @param jPushMessage 推送消息
     */
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        parseMessage("onCheckTagOperatorResult", jPushMessage);
    }

    /**
     * alias 相关的操作会在此方法中回调结果。
     *
     * @param context      上下文对象
     * @param jPushMessage 推送消息
     */
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        parseMessage("onMobileNumberOperatorResult", jPushMessage);
    }

    /**
     * 设置手机号码会在此方法中回调结果。
     *
     * @param context      上下文对象
     * @param jPushMessage 推送消息
     */
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        parseMessage("onMobileNumberOperatorResult", jPushMessage);
    }

    /**
     * 解析消息
     *
     * @param methodName   方法名
     * @param jPushMessage 推送消息
     */
    private void parseMessage(String methodName, JPushMessage jPushMessage) {
        int errorCode = jPushMessage.getErrorCode();
        Log.i(TAG, "┌─────────────────────────────────────────────────────────────────────");
        Log.i(TAG, "|methodName：" + methodName);
        Log.i(TAG, "|code：" + errorCode);
        if (errorCode == 0) {
            if (debug) {
                Log.i(TAG, "|描述：设置成功");
                Log.i(TAG, "|详细解释：tag/alias设置成功 alias=" + getAlias() + ",tags=" + getTags());
                if (handler != null) {
                    handler.removeMessages(SET_TAG_ALIAS);
                }
            }
        } else {
            if (debug) {
                String msg = codeMsg(jPushMessage.getErrorCode());
                if (msg.length() > 0) {
                    String msgSplit[] = msg.split("\n");
                    Log.i(TAG, "|" + msgSplit[0]);
                    Log.i(TAG, msgSplit[1]);
                }
            }
            if (handler != null) {
                if (errorCode == 6024 || errorCode == 6014 || errorCode == 6011 || errorCode == 6001) {
                    handler.sendEmptyMessageDelayed(SET_TAG_ALIAS, 60 * 1000);
                } else {
                    handler.sendEmptyMessageDelayed(SET_TAG_ALIAS, 10 * 1000);
                }
            }
        }
        Log.i(TAG, "└─────────────────────────────────────────────────────────────────────");
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
                    if (context != null) {
                        JPushInterface.setAlias(context.getApplicationContext(), getSequence(), getAlias());
                        set = new LinkedHashSet<>();
                        set.add(getTags());
                        JPushInterface.setTags(context.getApplicationContext(), getSequence(), set);
                    }
                    break;
            }
        }
    };

    public String codeMsg(int code) {
        for (int i = 0; i < codeArr.length; i++) {
            if (code == codeArr[i]) {
                return msgArr[i];
            }
        }
        return "";
    }

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
