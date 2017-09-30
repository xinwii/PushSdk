package com.pushsdk.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pushsdk.library.utils.PushDeviceInfoMgr;
import com.pushsdk.library.wrapper.GPushWrapper;
import com.pushsdk.library.wrapper.PushSdkCallBack;

/**
 * Created by xuxinwei on 2017/9/25.
 */

public class MyApplication extends Application {
    private boolean fromPushSdk = false;//如果是从推送进入，可以根据这个参数判断是否需要从欢迎页跳往某个界面
    private boolean loginState = false;

    public boolean isFromPushSdk() {
        return fromPushSdk;
    }

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }
        initRomPush();
    }

    /**
     * 初始化系统级推送
     */
    private void initRomPush() {

        GPushWrapper.getInstance().init(this);
        GPushWrapper.getInstance().setClickCallBack(new PushSdkCallBack() {
            @Override
            public void onNotificationMessageClicked(Context context, String message) {
                //可以不做任何处理，采用系统推送的方案，但是会有很多问题，比如，app被杀死，应该从欢迎页进入
                Log.e("Push", "Push click");
                if (ActivityManager.getActivities(context, false).size() > 0) {
                    Log.e("Push", "activity size = " + ActivityManager.getActivities(context, false).size());
                    if (loginState) {
                        Log.e("Push", "islogin");
                        //todo 已登录状态跳转某个界面
                        /*Intent intent = new Intent(context, MyActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/
                    } else {
                        //未登录,但是程序没有被杀死，这时候希望进入activity栈的最后一个activity，通过空activity把app从后台拉到前台，在杀死这个activity
                        Log.e("Push", "not login new task");
                        Intent intent = new Intent(context, EmptyActivity.class);
                        fromPushSdk = true;//标记从pushsdk进入，还可以采用eventbus
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    //后台没有运行中的activity，基本代表程序被杀死，这时候启动welcomeActivity(启动页)
                    Log.e("Push", "welcome new task");
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    fromPushSdk = true;//标记从pushsdk进入，还可以采用eventbus
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void upLoadToken(String token, int RomType) {
                //网络请求上传token
                // PushDeviceInfoMgr.MI_UI_ROM;
                // PushDeviceInfoMgr.HUAWEI_EM_UI_ROM;
                //PushDeviceInfoMgr.MEIZU_FLYME_OS_ROM;
            }
        });
    }
}
