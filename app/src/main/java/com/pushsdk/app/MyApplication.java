package com.pushsdk.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.pushsdk.library.utils.PushDeviceInfoMgr;
import com.pushsdk.library.wrapper.GPushWrapper;
import com.pushsdk.library.wrapper.PushSdkCallBack;

/**
 * Created by xuxinwei on 2017/9/25.
 */

public class MyApplication extends Application {
    private boolean fromPushSdk = false;//如果是从推送进入，可以根据这个参数判断是否需要从欢迎页跳往某个界面

    public boolean isFromPushSdk() {
        return fromPushSdk;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
                Log.e("GPush", "GPush click");
                boolean isLogin = true;
                if (isLogin) {
                    //有登录态跳往哪里
                } else {
                    //没有登录态从欢迎界面进入，
                    fromPushSdk = true;
                    /*Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                }
            }

            @Override
            public void upLoadToken(String token,int RomType) {
                //网络请求上传token
               // PushDeviceInfoMgr.MI_UI_ROM;
               // PushDeviceInfoMgr.HUAWEI_EM_UI_ROM;
                //PushDeviceInfoMgr.MEIZU_FLYME_OS_ROM;
            }
        });
    }
}
