package com.android.jpush;

import android.content.Context;
import android.content.Intent;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 */
public class JPushMsgReceiver extends JPushMessageReceiver {

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        sendReceiver(context, JPush.ACTION_OPERATOR_TAG, jPushMessage);
        TagAliasHelper.with(context).onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        sendReceiver(context, JPush.ACTION_OPERATOR_CHECK_TAG, jPushMessage);
        TagAliasHelper.with(context).onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        sendReceiver(context, JPush.ACTION_OPERATOR_ALIAS, jPushMessage);
        TagAliasHelper.with(context).onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        sendReceiver(context, JPush.ACTION_OPERATOR_MOBILE_NUMBER, jPushMessage);
        TagAliasHelper.with(context).onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    private void sendReceiver(final Context context,final  String action,final  JPushMessage jPushMessage) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                if (context==null){
                    return;
                }
                Intent intent = new Intent(action);
                intent.putExtra(JPush.EXTRA_OPERATOR_JPUSH_MESSAGE, jPushMessage);
                context.sendBroadcast(intent);
            }
        }.start();
    }

}
