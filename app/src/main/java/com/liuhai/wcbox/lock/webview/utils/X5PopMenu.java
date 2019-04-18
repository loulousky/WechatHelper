package com.liuhai.wcbox.lock.webview.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class X5PopMenu extends LinearLayout{
	/**
	 * 弹性便捷的弹出菜单
	 * @param context
	 */
	
	private static Context mContext;
	private static final float SCALE_WIDTH =  0.3f;
	private static final float SCALE_HEIGHT =  0.1f;
	private static ArrayList<Button> buttons;
	private static X5PopMenu popMenu;
	private static ViewGroup parentView;
	private static Dialog dialog;
	
	

	private X5PopMenu(Context context) {
		this(context,null);
	}
	
	private X5PopMenu(Context context, AttributeSet attr){
		super(context,attr);
		mContext = context;
	}
	
	public static X5PopMenu createInstance(Context context){
		mContext = context;
		if(popMenu == null){
			popMenu =new X5PopMenu(mContext);
			popMenu.setOrientation(LinearLayout.VERTICAL);
			
		}
		return popMenu;
	}
	
	public static void showInParent(ViewGroup parentView ,final int posX, final int poxY){
		if(popMenu!=null&&parentView!=null){
			popMenu.initView();
			dialog =new Dialog(mContext){
				@Override
				protected void onCreate(Bundle savedInstanceState) {
					// TODO Auto-generated method stub
					super.onCreate(savedInstanceState);
					setContentView(popMenu);
					WindowManager.LayoutParams params =getWindow().getAttributes();
					params.x=posX;
					params.y=poxY;
					getWindow().setAttributes(params);
					getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
					
				}
				
			};
			dialog.show();
		}
	}
	
	
	/**
	 * 初始化弹出菜单的界面
	 */
	private synchronized void initView(){
		int deviceWidth,deviceHeight;
		if(mContext != null){
			WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			deviceWidth=windowManager.getDefaultDisplay().getWidth();
			deviceHeight=windowManager.getDefaultDisplay().getHeight();
			if(buttons!=null){
				int size=buttons.size();
				int btnWidth=(int) (deviceWidth*SCALE_WIDTH);
				int btnHeight=(int) (deviceHeight*SCALE_HEIGHT);
				for(int i=0 ;i<size; i++){
					Button button = buttons.get(i);
					button.setBackgroundColor(Color.BLACK);
					button.setTextColor(Color.WHITE);
					button.setAlpha(0.5f);
					button.setTextSize(10);
					button.setWidth(btnWidth);
					button.setHeight(btnHeight);
					this.addView(button);
					
				}
			}
			
			
		}
	}
	
	/**
	 * 添加一个新的按钮
	 * @param btnName 按钮的名称
	 * @param listener 监听事件
	 */
	public void createNewButton(String btnName, OnClickListener listener){
		if(buttons==null){
			buttons= new ArrayList<Button>();
		}
		Button button = new Button(mContext);
		button.setText(btnName);
		button.setOnClickListener(listener);
		buttons.add(button);
	}
	
	public void dismiss(){
		if(dialog!=null){
			dialog.dismiss();
		}
	}
	
	

}
