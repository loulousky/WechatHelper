package com.liuhai.wcbox.lock.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.util.UUID;

public class DeviceUtils {
	/**
	 * 获取手机IMEI号
	 */
	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();

		return imei;
	}
	 /** 
	* deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符 
	*  
	* 渠道标志为： 
	* 1，andriod（a） 
	* 
	* 识别符来源标志： 
	* 1， wifi mac地址（wifi）； 
	* 2， IMEI（imei）； 
	* 3， 序列号（sn）； 
	* 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。 
	* 
	* @param context 
	* @return 
	*/ 
	public static String getDeviceId(Context context) { 

		StringBuilder deviceId = new StringBuilder(); 
		// 渠道标志 
		deviceId.append("A"); 
	
		try { 
			//IMEI（imei） 
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
			String imei = tm.getDeviceId(); 
			if(!isEmpty(imei)){ 
				deviceId.append("imei"); 
				deviceId.append(imei);  
				return deviceId.toString(); 
			} 
			
			//wifi mac地址 
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
			WifiInfo info = wifi.getConnectionInfo(); 
			String wifiMac = info.getMacAddress(); 
			if(!isEmpty(wifiMac)){ 
				deviceId.append("wifi"); 
				deviceId.append(wifiMac); 
		
				return deviceId.toString(); 
			} 
	
			//序列号（sn） 
			String sn = tm.getSimSerialNumber(); 
			if(!isEmpty(sn)){ 
				deviceId.append("sn"); 
				deviceId.append(sn); 
				return deviceId.toString(); 
			} 
	
			//如果上面都没有， 则生成一个id：随机码 
			String uuid = getUUID(context); 
			if(!isEmpty(uuid)){ 
				deviceId.append("id"); 
				deviceId.append(uuid); 
				return deviceId.toString(); 
			} 
		} catch (Exception e) { 
			e.printStackTrace(); 
			deviceId.append("id").append(getUUID(context)); 
		} 
		return deviceId.toString(); 
	}


	/** 
	* 得到全局唯一UUID 
	*/ 
	public static String getUUID(Context context){ 
		String uuid = "";
		SharedPreferences mShare = context.getSharedPreferences("sysCacheMap", Activity.MODE_PRIVATE); 
		if(mShare != null){ 
			uuid = mShare.getString("uuid", "");
			if(isEmpty(uuid)){ 
				uuid = UUID.randomUUID().toString(); 
 
				SharedPreferences.Editor editor = mShare.edit(); 
				editor.putString("uuid", uuid); 
				editor.commit(); 
			}
		} 
		return uuid; 
	} 
	
	private static boolean isEmpty(String str){
		boolean ret = false;
		if(str == null || "".equals(str)){
			ret = true;
		}
		return ret;
	}
}
