package com.pushsdk.library.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;


public class PushDeviceInfoMgr
{
    private final static String DEFAULT_IMEI = "000000000000000";
	private final static String DEFAULT_ANDROID_ID = "0000000000000000";
	//private final static String PUSH_SERVICE_INFO_FILE_NAME = "gpush_service_info.xml";
	public final static int OTHER_ROM = 0;
	public final static int MI_UI_ROM = 1;
	private final static String MI_UI_VERSION_NAME_PROPERTY = "ro.miui.ui.version.name";
	private static String m_strMIUIVersionName = null;
	public final static int HUAWEI_EM_UI_ROM = 2;
	private final static String EM_UI_API_LEVEL_PROPERTY = "ro.build.version.emui";
	private static String m_strEMUIVersion = null;
	public final static int MEIZU_FLYME_OS_ROM = 3;
	private final static String FLYME_OS_DISPLAY_ID_PROPERTY = "ro.build.display.id";
	private static String m_strFlymeOSDispalyID = null;
	
	public static String getSystemProperty( String strPropertyKey ) 
	{  
	    String strPropertyValue = null;  
	    BufferedReader input = null;  
	    try 
	    {  
	        Process process = Runtime.getRuntime().exec( "getprop " + strPropertyKey );  
	        input = new BufferedReader( new InputStreamReader( process.getInputStream() ), 1024 );  
	        strPropertyValue = input.readLine();  
	        input.close();  
	    } 
	    catch (IOException ex) 
	    {  
	        return null;  
	    }
	    finally 
	    {  
	        if (input != null) 
	        {  
	            try 
	            {  
	                input.close();  
	            } 
	            catch (IOException e) 
	            {  
	            }  
	        }  
	    }  
	    return strPropertyValue;  
	}
	
	public static int getROMType()
	{
		int nRomType = OTHER_ROM;
		if ( ( null != m_strMIUIVersionName || null != ( m_strMIUIVersionName = getSystemProperty( MI_UI_VERSION_NAME_PROPERTY ) ) ) 
				&& !TextUtils.isEmpty( m_strMIUIVersionName ) )
		{
			nRomType = MI_UI_ROM;
			Log.e("GPush", "current rom type is miui, the miui version name is: " + m_strMIUIVersionName );
		}
		else if ( ( null != m_strEMUIVersion || null != ( m_strEMUIVersion = getSystemProperty( EM_UI_API_LEVEL_PROPERTY ) ) )
					&& !TextUtils.isEmpty( m_strEMUIVersion ) )
		{
			nRomType = HUAWEI_EM_UI_ROM;
			Log.e("GPush", "current rom type is emui, the emui version name is: " + m_strEMUIVersion );
		}
		else if ( ( null != m_strFlymeOSDispalyID || null != ( m_strFlymeOSDispalyID = getSystemProperty( FLYME_OS_DISPLAY_ID_PROPERTY ) ) )
					&& ( m_strFlymeOSDispalyID.contains("flyme") || m_strFlymeOSDispalyID.toLowerCase().contains("flyme") ) )
		{
			nRomType = MEIZU_FLYME_OS_ROM;
			Log.e("GPush", "current rom type is flyme, the flyme display id is: " + m_strFlymeOSDispalyID );
		}
		else
		{
			Log.e("GPush", "current rom type is other rom" );
		}
		return nRomType;
	}
	
	public static String getOsVersion()
	{
		int nOsVersion = 0;  
        try  
        {  
           nOsVersion = Integer.valueOf( android.os.Build.VERSION.SDK_INT );  
        }  
        catch (NumberFormatException e)  
        {  
        } 
        String strOsVersion = String.valueOf( nOsVersion );
        return strOsVersion;  
	}
	
	public static String getDeviceManufacturer()
	{
		String strDeivceManufacturer = "";
		strDeivceManufacturer = android.os.Build.MANUFACTURER;
    	return strDeivceManufacturer;
	}
	
	public static String getDeviceModel()
	{
		String strDeivceModel = "";
		strDeivceModel = android.os.Build.MODEL;
    	return strDeivceModel;
	}
	
	public static String getImei( Context context )
	{
		String strImei = DEFAULT_IMEI;
		if(null != context)
		{
			try 
			{
				TelephonyManager telMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				if ( null != telMgr )
				{
					strImei = telMgr.getDeviceId();
				}
			}
			catch (Exception e)
			{
			}
		}
		return strImei;
	}
	
	public static String getAndroidID( Context context )
	{
		String strAndroid = DEFAULT_ANDROID_ID;
		if(null != context)
		{
			try 
			{
				strAndroid = Secure.getString( context.getContentResolver(), Secure.ANDROID_ID );
			} 
			catch (Exception e)
			{
			}
		}
		return strAndroid;
	}
	
	public static String getDeviceID( Context context )
	{
		String strDeviceID = null;
		String strImei = getImei( context );
		if( null == strImei )
		{
			strImei = DEFAULT_IMEI;
		}
		String strAndroidID = getAndroidID( context );
		if( null == strAndroidID )
		{
			strAndroidID = DEFAULT_ANDROID_ID;
		}
		if( strImei.equalsIgnoreCase( DEFAULT_IMEI ) && strAndroidID.equalsIgnoreCase( DEFAULT_ANDROID_ID ) )
		{
			UUID uuid = UUID.randomUUID();
			strDeviceID = uuid.toString();
			strDeviceID = strDeviceID.toUpperCase();
		}
		else
		{
			strDeviceID = strImei + "-" + strAndroidID;
		}
		return strDeviceID;
	}
	
	public static String GetNetworkOperator( Context context )
	{
		String strNetworkOperator = "unknown";
		if(null == context)
		{
			return strNetworkOperator;
		}
		try
		{  
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
            String strIMSI = telephonyManager.getSubscriberId();  
            if ( null == strIMSI ) 
            {  
                if (TelephonyManager.SIM_STATE_READY == telephonyManager.getSimState()) 
                {  
                    String strOperatorCode = telephonyManager.getSimOperator();  
                    if (strOperatorCode != null)
                    {  
                        if ( strOperatorCode.equals("46000") || strOperatorCode.equals("46002") || strOperatorCode.equals("46007") )
                        {  
                        	strNetworkOperator = "CMCC";  
                        }
                        else if ( strOperatorCode.equals("46001") || strOperatorCode.equals("46006") )
                        {  
                        	strNetworkOperator = "CUCC";  
                        }
                        else if ( strOperatorCode.equals("46003") || strOperatorCode.equals("46005") ) 
                        {  
                        	strNetworkOperator = "CTCC";  
                        }  
                        else 
                        {
                        	
                        }
                    }  
                }  
            } 
            else
            {  
                if ( strIMSI.startsWith("46000") || strIMSI.startsWith("46002") || strIMSI.startsWith("46007") )
                {  
                	strNetworkOperator = "CMCC";  
                }
                else if ( strIMSI.startsWith("46001") || strIMSI.startsWith("46006") ) 
                {  
                	strNetworkOperator = "CUCC";  
                }
                else if ( strIMSI.startsWith("46003") || strIMSI.startsWith("46005") ) 
                {  
                	strNetworkOperator = "CTCC";  
                }  
                else 
                {
                	
                }
            }  
        } 
		catch (Exception e) 
        {  
            e.printStackTrace();  
        }  
		return strNetworkOperator;
	}
	
	//Get the network type, cmcc cucc ctcc  2g, 3g,4g,wifi
	public static String GetNetworkType( Context context )
	{
		if( null == context )
		{
			return "unknown";
		}
		String strNetWorkType = "other";
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );  
        NetworkInfo networkInfo = null; 
        try
		{
        	networkInfo = connectivityManager.getActiveNetworkInfo();
		}
        catch (Exception e)
		{
			// TODO: handle exception
        	return "unknown";
		}
        if( null != networkInfo && networkInfo.isConnected() )
        {
        	//WIFI connect environment;
        	if( networkInfo.getType() == ConnectivityManager.TYPE_WIFI  )
        	{
        		strNetWorkType = "WIFI";
        	}
        	//mobile connect environment
        	else if( networkInfo.getType() == ConnectivityManager.TYPE_MOBILE  )
        	{
        		String strNetworkOperator = GetNetworkOperator( context );
                if( strNetworkOperator.equalsIgnoreCase( "CMCC" ) 
                		|| strNetworkOperator.equalsIgnoreCase( "CTCC" ) 
                		|| strNetworkOperator.equalsIgnoreCase( "CUCC" ))
                {
                	int nSubType = networkInfo.getSubtype();
                	strNetWorkType = String.format( Locale.getDefault(), "%s-%d", strNetworkOperator, nSubType );
                }
                else
                {
                	strNetWorkType = "unknow";
                }
        	}
        	//other connect environment
        	else
        	{
        		strNetWorkType = "other";
        	}
        }
        return strNetWorkType;
	}
	
}