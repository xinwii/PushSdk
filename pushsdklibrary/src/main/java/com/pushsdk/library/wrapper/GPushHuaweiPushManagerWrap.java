package com.pushsdk.library.wrapper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.huawei.android.pushagent.api.PushManager;
//import com.huawei.hms.api.ConnectionResult;
//import com.huawei.hms.api.HuaweiApiAvailability;
//import com.huawei.hms.api.HuaweiApiClient;
//import com.huawei.hms.api.HuaweiApiClient.ConnectionCallbacks;
//import com.huawei.hms.api.HuaweiApiClient.OnConnectionFailedListener;
//import com.huawei.hms.support.api.client.PendingResult;
//import com.huawei.hms.support.api.client.ResultCallback;
//import com.huawei.hms.support.api.push.HuaweiPush;
//import com.huawei.hms.support.api.push.TokenResult;
import android.content.Context;
import android.text.TextUtils;

public class GPushHuaweiPushManagerWrap
{
//	static class GPushHuaweiPushListener implements ConnectionCallbacks, OnConnectionFailedListener
//	{
//		@Overridehaim
//		public void onConnected()
//		{
//			//华为移动服务client连接成功，在这边处理业务自己的事件
//			GPushBaseUtility.doPushServiceLogger( "huawei client api connect succeed." );
//			PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken( m_huaweiApiClient );
//			tokenResult.setResultCallback( new ResultCallback<TokenResult>() 
//			{
//				@Override
//				public void onResult(TokenResult result) 
//				{
//					if ( null != result )
//					{
//						if ( null != result.getTokenRes() )
//						{
//							GPushBaseUtility.doPushServiceLogger( "get token result coming, the result is:" + String.valueOf( result.getTokenRes().getRetCode() ) );
//						}
//					}
//				}	
//			} );
//		}
//
//		@Override
//		public void onConnectionSuspended(int arg0) 
//		{
//			//HuaweiApiClient断开连接的时候，业务可以处理自己的事件
//			if ( null != m_huaweiApiClient )
//			{
//				m_huaweiApiClient.connect();
//			}
//		}
//
//		@Override
//		public void onConnectionFailed(ConnectionResult arg0) 
//		{
//			int nErrorCode = arg0.getErrorCode();
//			GPushBaseUtility.doPushServiceLogger( "huawei client api connect failed, the error code is:" + String.valueOf( nErrorCode ) );
//		}
//	}
	
	public static void init( Context context )
	{
//		if ( null == m_huaweiApiClient )
//		{
//			m_huaweiApiClient = new HuaweiApiClient.Builder( context )
//			.addApi( HuaweiPush.PUSH_API )
//			.addConnectionCallbacks( new GPushHuaweiPushListener() )
//			.addOnConnectionFailedListener( new GPushHuaweiPushListener() )
//			.build();
//		}
//		m_huaweiApiClient.connect();
		PushManager.requestToken( context );
	}
	
	public static void setAliasAndTags( Context context, String strAlias, Set<String> setTags )
	{
		List<String> lstDelTags = new ArrayList<String>();
		Map<String, String> mapTags = new HashMap<String, String>();
		if ( null != strAlias )
		{
			if ( null != strAlias )
			{
				if ( TextUtils.isEmpty( strAlias ) )
				{
					lstDelTags.add( m_strAliasKey );
				}
				else
				{
					mapTags.put( m_strAliasKey, strAlias );
				}
			}
		}
		if ( null != setTags )
		{
			if ( null != m_setLastTags && !m_setLastTags.isEmpty() )
			{
				for ( String strLastTag : m_setLastTags  )
				{
					if ( !setTags.contains( strLastTag ) )
					{
						lstDelTags.add( strLastTag );
					}
				}
				m_setLastTags.clear();
			}
			for ( String strTag : setTags  )
			{
				if ( null != strTag && !TextUtils.isEmpty( strTag ) )
				{
					try
					{
						mapTags.put( strTag , EncoderByMd5( strTag ) );
						m_setLastTags.add( strTag );
					}
					catch (NoSuchAlgorithmException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (UnsupportedEncodingException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		if ( !lstDelTags.isEmpty() )
		{
			PushManager.deleteTags( context , lstDelTags );
			//HuaweiPush.HuaweiPushApi.deleteTags( m_huaweiApiClient, lstDelTags );
		}
		if ( !mapTags.isEmpty() )
		{
			PushManager.setTags( context, mapTags );
			//HuaweiPush.HuaweiPushApi.setTags( m_huaweiApiClient, mapTags );
		}
	}
	
	private static String m_strAliasKey = "alias";
	private static Set<String> m_setLastTags = new HashSet<String>();
	//private static HuaweiApiClient m_huaweiApiClient = null;
	private static String EncoderByMd5(String strSrc) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		//确定计算方法
		MessageDigest md5Encrypter = MessageDigest.getInstance( "MD5" );
		if ( null != md5Encrypter )
		{
			byte[] bytesRet = md5Encrypter.digest( strSrc.getBytes() );
			StringBuilder builderRet = new StringBuilder(40);
			for( byte byteItem : bytesRet )
			{
				if( ( byteItem & 0xff ) >> 4 == 0 )
				{
					builderRet.append( "0" ).append( Integer.toHexString( byteItem & 0xff ) );
				}
				else
				{
					builderRet.append( Integer.toHexString( byteItem & 0xff ) );
				}
			}
			String strMD5Encrypt = builderRet.toString().toUpperCase();
			return strMD5Encrypt;
		}
		return "";
	}
}
