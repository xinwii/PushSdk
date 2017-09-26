package com.pushsdk.library.wrapper;

import android.content.Context;

/**
 * Created by xuxinwei on 2017/9/25.
 */

public interface PushSdkCallBack {
    void onNotificationMessageClicked(Context context, String message);
    void upLoadToken(String token,int RomType);
}
