package com.pushsdk.library.wrapper;

import java.util.HashSet;
import java.util.Set;

import com.meizu.cloud.pushsdk.PushManager;

import android.content.Context;
import android.text.TextUtils;

public class GPushFlymePushManagerWrap
{
	protected static void init( Context context, String strAppID, String strAppKey  )
	{
		if ( null == context || null == strAppID || TextUtils.isEmpty( strAppID ) || null == strAppKey || TextUtils.isEmpty( strAppKey ) )
		{
			return;
		}
		m_strAppID = strAppID;
		m_strAppKey = strAppKey;
		PushManager.register( context, strAppID, strAppKey );
	}
	
	protected static void startPush( Context context )
	{
		if ( null == context || null == m_strAppID || TextUtils.isEmpty( m_strAppID ) || null == m_strAppKey || TextUtils.isEmpty( m_strAppKey ) )
		{
			return;
		}
		PushManager.switchPush( context, m_strAppID, m_strAppKey, PushManager.getPushId( context ), 0, true );
		PushManager.switchPush( context, m_strAppID, m_strAppKey, PushManager.getPushId( context ), 1, true );
	}
	
	protected static void setAliasAndTags( Context context, String strAlias, Set<String> setTags )
	{
		if ( null == context || null == m_strAppID || TextUtils.isEmpty( m_strAppID ) || null == m_strAppKey || TextUtils.isEmpty( m_strAppKey ) )
		{
			return;
		}
		if ( null != strAlias )
		{
			if ( null != m_strLastAlias && !TextUtils.isEmpty( m_strLastAlias ) && !m_strLastAlias.equalsIgnoreCase( strAlias ) )
			{
				PushManager.unSubScribeAlias( context, m_strAppID, m_strAppKey, PushManager.getPushId( context ), m_strLastAlias );
			}
			if ( !TextUtils.isEmpty( strAlias ) )
			{
				PushManager.subScribeAlias( context, m_strAppID, m_strAppKey, PushManager.getPushId( context ), strAlias );
				m_strLastAlias = strAlias;
			}
		}
		if ( null != setTags )
		{
			if ( null != m_setLastTags && !m_setLastTags.isEmpty() )
			{
				Set<String> setUnSubTags = new HashSet<String>();
				for ( String strTag : m_setLastTags )
				{
					if ( null != strTag && !strTag.isEmpty() && !setTags.contains( strTag ) )
					{
						setUnSubTags.add( strTag );
					}
				}
				if ( !setUnSubTags.isEmpty() )
				{
					PushManager.unSubScribeTags( context, m_strAppID, m_strAppKey, PushManager.getPushId( context ), generateTags( setUnSubTags ) );
				}
				m_setLastTags.clear();
			}
			if ( !setTags.isEmpty() )
			{
				PushManager.subScribeTags( context, m_strAppID, m_strAppKey, PushManager.getPushId( context ), generateTags( setTags ) );
				for ( String strTag : setTags )
				{
					m_setLastTags.add( strTag );
				}
			}
		}
	}
	
	private static String generateTags( Set<String> setTag )
	{
		StringBuffer strBufTags = new StringBuffer();
		int nTagIndex = 0;
		for ( String strTag : setTag )
		{
			if ( null != strTag && !strTag.isEmpty() )
			{
				strBufTags.append( strTag );
				if ( nTagIndex != setTag.size() - 1 )
				{
					strBufTags.append( "," );
				}
			}
			++nTagIndex;
		}
		String strTags = strBufTags.toString();
		return strTags;
	}
	
	private static Set<String> m_setLastTags = new HashSet<String>();
	private static String m_strLastAlias = null;
	private static String m_strAppID = "";
	private static String m_strAppKey = "";
}
