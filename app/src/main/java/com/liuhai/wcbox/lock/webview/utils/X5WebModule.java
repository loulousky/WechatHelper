package com.liuhai.wcbox.lock.webview.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class X5WebModule {
	
	/**
	 * 
	 */

	private Context mContext;
	private X5WebModule mX5WebModule;
	
	////////////////////////////////////////////////////////////////////////////////
	private X5WebModule(){
		
	}
	
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * get instance of X5WebModule
	 * @param context app context
	 * @return factory which can produce web server
	 */
	public X5WebModule getInstance(Context context){
		this.mContext = context;
		if(mX5WebModule==null){
			mX5WebModule = new X5WebModule();
		}
		return mX5WebModule;
	}
	
	/**
	 * when X5WebModule initiated, you can 
	 * module instance can return http web page
	 * @param webPageSettings
	 */
	public static void simulateWebPage(WebPageSettings webPageSettings){
		
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	//
	
	public class WebPageSettings{
		Map <String,String> settings;
		private static final int CAPACITY=10;
		
		/**
		 * http header paramates
		 */
		public static final String CACHE_CONTROL = "cache_control";
		public static final String DATE ="date";
		public static final String SERVER = "server";
		public static final String CONTENT_TYPE = "content-type";
		public static final String LAST_MODEIFIED = "Last-modified";
		public static final String CONTENT_LENGTH = "content-length";
		public static final String CONTENT_RANGE = "content-range";

		public WebPageSettings(){

		}
		
		/**
		 * 
		 * @param key
		 * @param value
		 */
		public void putSettings(String key, String value) throws Exception{
			if(settings!=null){
				settings = new HashMap<String, String>(CAPACITY);
			}
			if(key.equals(CACHE_CONTROL)
					||key.equals(CONTENT_LENGTH)
					||key.equals(CONTENT_RANGE)
					||key.equals(CONTENT_TYPE)
					||key.equals(LAST_MODEIFIED)
					||key.equals(SERVER)
					||key.equals(DATE)){
			settings.put(key, value);	
			}else{
				throw new Exception("illegal http header format");
			}
		}
		
	}
}
