package com.pushsdk.library.wrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.xiaomi.mipush.sdk.MiPushClient;

public class GPushMiPushClientWrap
{
	private static boolean shouldInit( Context context ) 
    {
		if ( null == context )
		{
			return false;
		}
        ActivityManager activityMgr = ( (ActivityManager) context.getSystemService( Context.ACTIVITY_SERVICE ) );
        List<RunningAppProcessInfo> processInfos = activityMgr.getRunningAppProcesses();
        String strMainProcessName = context.getPackageName();
        int nCurPID = Process.myPid();
        for ( RunningAppProcessInfo info : processInfos )
        {
            if ( info.pid == nCurPID && strMainProcessName.equals( info.processName ) ) 
            {
                return true;
            }
        }
        return false;
    }
	
	protected static void init( Context context, String strAppId, String strAppKey )
	{
		if ( null == context )
		{
			return;
		}
		Log.e("GPush", "into mi push init." );
		if ( ( null == strAppId ) || ( 0 == strAppId.length() ) || ( null == strAppKey ) || ( 0 == strAppKey.length() ) )
		{
			Log.e("GPush", "mi push init, app id or app key is invalid." );
			return;
		}
		if ( shouldInit( context ) )
		{
			Log.e("GPush", "mi push client registerPush." );
			MiPushClient.registerPush( context, strAppId, strAppKey );
		}
	}
	
	protected static void setAliasAndTags( Context context, String strAlias, Set<String> setTags )
	{
		if ( null != strAlias )
		{
			if ( null != m_strLastAlias && !TextUtils.isEmpty( m_strLastAlias ) && !m_strLastAlias.equalsIgnoreCase( strAlias ) )
			{
				MiPushClient.unsetAlias( context, m_strLastAlias, null );
			}
			if ( !TextUtils.isEmpty( strAlias ) )
			{
				MiPushClient.setAlias( context, strAlias, null );
				m_strLastAlias = strAlias;
			}
		}
		if ( null != setTags )
		{
			if ( null != m_setLastTags && !m_setLastTags.isEmpty() )
			{
				for ( String strTag : m_setLastTags )
				{
					if ( null != strTag && !strTag.isEmpty() && !setTags.contains( strTag ) )
					{
						MiPushClient.unsubscribe( context, strTag, null );
					}
				}
				m_setLastTags.clear();
			}
			if ( !setTags.isEmpty() )
			{
				for ( String strTag : setTags )
				{
					if ( null != strTag && !strTag.isEmpty() )
					{
						MiPushClient.subscribe( context, strTag, null );
						m_setLastTags.add( strTag );
					}
				}
			}
		}
	}
	
	protected static void setPushTime( Context context, Set<Integer> setWeekDays, int nStartHour, int nEndHour )
	{
		if ( null != setWeekDays )
		{
			if ( setWeekDays.isEmpty() )
			{
				MiPushClient.setAcceptTime( context, 0, 0, 0, 0, null );
			}
			else
			{
				MiPushClient.setAcceptTime( context, nStartHour, 0, nEndHour + 1, 0, null );
			}
		}
	}
	
	protected static void stopPush( Context context )
	{
		MiPushClient.pausePush( context, null );
		m_bIsPushPaused = true;
	}
	
	protected static void resumePush( Context context )
	{
		MiPushClient.resumePush( context, null );
		m_bIsPushPaused = false;	
	}
	
	protected static boolean isPushStopped(Context context)
	{
		return m_bIsPushPaused;
	}
	
	public static void clearAllNotifications( Context context )
	{
		MiPushClient.clearNotification( context );
	}
	
	public static void clearNotificationById( Context context, int nNotificationId )
	{
		MiPushClient.clearNotification( context, nNotificationId );
	}
	
	private static final String MI_PUSH_LOG_TAG = "com.sdg.sdgpushnotificationservice.mipush";
	private static Set<String> m_setLastTags = new HashSet<String>();
	private static String m_strLastAlias = null;
	private static boolean m_bIsPushPaused = false;
}
