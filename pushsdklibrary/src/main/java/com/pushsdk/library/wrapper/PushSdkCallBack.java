package com.pushsdk.library.wrapper;

import android.content.Context;

/**
 * Created by xuxinwei on 2017/9/25.
 * 推送监听接口，在注册后回调上报token，点击通知栏后监听点击事件
 */

public interface PushSdkCallBack {
    //点击通知栏
    void onNotificationMessageClicked(Context context, String message);
    //上报token
    void upLoadToken(String token,int RomType);
}
