package com.liuhai.wcbox.lock.webview.utils;

import android.content.Context;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebView.HitTestResult;

public class LongPressListenerWrapper implements OnLongClickListener{
	
	/**
	 * 一个长按监听器的实例，可以在此实现针对不同长按对象 引导不同的长按处理
	 */

	private Context mContext;
	private WebView webview;
	
	public LongPressListenerWrapper(WebView webview ,Context context){
		this.webview = webview;
		this.mContext=context;
	}

	@Override
	public boolean onLongClick(View v) {
		int type = webview.getHitTestResult().getType();
		switch(type){
		case HitTestResult.ANCHOR_TYPE:			
		case HitTestResult.IMAGE_ANCHOR_TYPE:
			
			return doAnchorLongPressEvent();
			
		case HitTestResult.IMAGE_TYPE:

			return doImageLongPressEvent();
			
		case HitTestResult.EDIT_TEXT_TYPE:

			return doTextLongPressEvent();
			
		case HitTestResult.UNKNOWN_TYPE:
			return doUnknownAreaPressEvent();
			
		default:
			
			break;

		}
		
		return false;
	}
	
	
	
	
	private boolean doImageLongPressEvent(){
		Toast.makeText(mContext, "长按图片", Toast.LENGTH_SHORT).show();
		return true;
	}
	
	private boolean doEdiableAreaLongPressEvent(){
		Toast.makeText(mContext, "长按输入框", Toast.LENGTH_SHORT).show();
		return true;
	}
	
	private boolean doTextLongPressEvent(){
		
		return false;//返回false 将会调用X5内核的长按机制，例如文本类的处理最好交由X5内核负责较为稳妥
	}

	private boolean doAnchorLongPressEvent(){
		Toast.makeText(mContext, "长按超链接", Toast.LENGTH_SHORT).show();
		return true;
	}
	
	private boolean doUnknownAreaPressEvent(){
		Toast.makeText(mContext, "空白区域", Toast.LENGTH_SHORT).show();
		
		return true;
	}
	
	
}
