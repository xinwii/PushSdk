package com.pushsdk.library.wrapper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;


import com.pushsdk.library.utils.PushDeviceInfoMgr;

import java.util.Set;

/**
 * Created by xuxinwei on 2017/9/11.
 */

public class GPushWrapper {
    private final static String MI_APP_ID = "";
    private final static String MI_APP_KEY = "";
    private final static String MEIZU_APP_ID = "";
    private final static String MEIZU_APP_KEY = "";
    public static int ROM_TYPE = -1;
    private PushSdkCallBack callBack;

    private GPushWrapper() {
    }

    private static GPushWrapper gPushWrapper = null;

    public static GPushWrapper getInstance() {
        if (gPushWrapper == null) {
            synchronized (GPushWrapper.class) {
                if (gPushWrapper == null) {
                    gPushWrapper = new GPushWrapper();
                }
            }
        }
        return gPushWrapper;
    }

    public void init(Context context) {
        ROM_TYPE = PushDeviceInfoMgr.getROMType();
        Log.e("GPush", ">>>>>>>>>>>>>>>>>>>>>>>>rom type =" + ROM_TYPE);
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String miId = applicationInfo.metaData.getString("MI_ID");
            String miKey = applicationInfo.metaData.getString("MI_KEY");
            String flymeId = applicationInfo.metaData.getString("FLYME_ID");
            String flymeKey = applicationInfo.metaData.getString("FLYME_KEY");
            switch (ROM_TYPE) {
                case PushDeviceInfoMgr.MI_UI_ROM:
                    GPushMiPushClientWrap.init(context, miId, miKey);
                    break;
                case PushDeviceInfoMgr.HUAWEI_EM_UI_ROM:
                    GPushHuaweiPushManagerWrap.init(context);
                    break;
                case PushDeviceInfoMgr.MEIZU_FLYME_OS_ROM:
                    GPushFlymePushManagerWrap.init(context, flymeId, flymeKey);
                    break;
                //其他手机系统默认采用小米推送
                case PushDeviceInfoMgr.OTHER_ROM:
                    GPushMiPushClientWrap.init(context, miId, miKey);
                    break;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //通知栏点击监听
    public void onNotificationMessageClicked(Context context, String message) {
        callBack.onNotificationMessageClicked(context, message);
    }

    //上报token，小米是regId,魅族是pushId,华为是token
    public void upLoadToken(String token,int romType) {
        callBack.upLoadToken(token,romType);
    }

    public void setClickCallBack(PushSdkCallBack callBack) {
        this.callBack = callBack;
    }

    public void setAliasAndTags(Context context, String strAlias, Set<String> setTags) {
        switch (ROM_TYPE) {
            case PushDeviceInfoMgr.MI_UI_ROM:
                //扩展参数，暂时没有用途，直接填null
                GPushMiPushClientWrap.setAliasAndTags(context, strAlias, setTags);
                break;
            case PushDeviceInfoMgr.HUAWEI_EM_UI_ROM:
                GPushHuaweiPushManagerWrap.setAliasAndTags(context, strAlias, setTags);
                break;
            case PushDeviceInfoMgr.MEIZU_FLYME_OS_ROM:
                GPushFlymePushManagerWrap.setAliasAndTags(context, strAlias, setTags);
                break;
            case PushDeviceInfoMgr.OTHER_ROM:
                //扩展参数，暂时没有用途，直接填null
                GPushMiPushClientWrap.setAliasAndTags(context, strAlias, setTags);
                break;
        }
    }

}
