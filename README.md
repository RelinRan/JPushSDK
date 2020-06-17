# AndroidKit
Android 极光推送集成（Android aurora push integration） -  [官方API文档](https://docs.jiguang.cn/jpush/client/Android/android_sdk/)
## 方法一  ARR依赖
[JPushSDK.arr](https://github.com/RelinRan/JPushSDK/blob/master/JPushSDK.aar)
```
android {
    ....
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
}

dependencies {
    implementation(name: 'JPushSDK', ext: 'aar')
}

```

## 方法二   JitPack依赖
### A.项目/build.grade
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### B.项目/app/build.grade
```
	dependencies {
	        implementation 'com.github.RelinRan:JPushSDK:1.0.0'
	}
```
### AndroidManifest.xml配置
```
    <application>
        <!--极光推送-->
        <meta-data
            android:name="JPUSH_APPKEY"
            android:value="ac0c04b9441c9077a53931a4" /> <!--值来自开发者平台取得的AppKey-->
    </application>
```
### 初始化和销毁
```
    private void initJPush(String alias) {
        //消息监听 + Tag/Alias操作监听
        JPush.addJPushMessageListener(XXXX);
        JPush.addJPushAliasTagsListener(XXXX);
        JPush.init(this, alias);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁推送，一般程序主页需要
        JPush.destroy(this);
    }
```

### 设置调试模式
```
        JPush.setDebugMode(true);
```
### 退出推送
```
        JPush.logout(context);
```
### 停止推送
```
        JPush.stopPush(context);
```
### 恢复推送
```
        JPush.resumePush(context);
```
### Tag/Alias监听 - [官方客户端错误码定义](https://docs.jiguang.cn/jpush/client/Android/android_api/#_248)
```
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
```
### 消息监听
```
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
 * @param bundle  参数
 * @param isConnected 是否连接
 */
void onConnectionChange(Bundle bundle, boolean isConnected);
```