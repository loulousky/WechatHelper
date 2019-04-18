package com.liuhai.wcbox.lock.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DeviceScreen {

	public static String getAdSize(Context context){
		
		
        
        int height = getScreenHeight(context);
        
        if(height > 500){
        	return "480*800";
        }else{
        	return "320*480";
        } 
	}
	
	
   public static int getAdHeight(Context context){
		
		
        
        int height = getScreenHeight(context);
        /*
        if(height > 500){
        	return 800;
        }else{
        	return 480;
        } */
        
        return height;
	}
	
	public static int getScreenHeight(Context context){
		int height = getMetric(context).heightPixels;
		return height;
	}
	
	public static DisplayMetrics  getMetric(Context context){
		DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        wm.getDefaultDisplay().getMetrics(metric);
        
        return metric;
	}
	
	public static int getScreenWidth(Context context){
		int height = getMetric(context).widthPixels;
		return height;
	}
}
