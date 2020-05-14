# AndroidKit
Android 极光推送集成（Android aurora push integration）

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
	        implementation 'com.android.support:design:27.+'
                implementation 'com.android.support:recyclerview-v7:27.+'
	}
```
## Application配置

### A.初始化和销毁
```
    private void initJPush(String alias) {
        JPush.setDebugMode(true);
        //监听有连接监听 + Tag/Alias操作监听
        JPush.addJPushListener(null,null);
        JPush.init(this, alias);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JPush.removeJPushListener(this);
    }

```
### B.退出登录
```
        JPush.logout(context);
```
### C.停止推送
```
        JPush.stopPush(context);
```
### C.恢复推送
```
        JPush.resumePush(context);
```