package com.liuhai.wcbox.lock.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;



public abstract class BaseActivity extends AppCompatActivity {
	public String TAG;
	public String name="";

	/**
	 * 启动当前activity的Activity
	 */
	public String fromActivityName;


	/**
	 * 1、在该方法中自动调用initLayoutId(),initView(),findView()，无需开发者手动调用
	 * 2、初始化EventBus.getDefault().register(this);
	 * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TAG = this.getClass().getSimpleName();
		super.onCreate(savedInstanceState);
		hideSoftInput();
		fromActivityName=getIntent().getStringExtra("FromActivityName");
		log(fromActivityName+ "  startActivity  "+TAG);
		log(TAG+ " onCreate------------------------------------------------------------------");
	}


	@Override
	public void onDestroy() {
		log(TAG+ " onDestroy------------------------------------------------------------------");
		super.onDestroy();
		System.gc();

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		log(TAG + " onResume-------------------------------------------------------------------");
		super.onResume();
	}

	@Override
	protected void onPause() {
		log(TAG + " onPause-------------------------------------------------------------------");
		super.onPause();
	}

	public void setPageName(String name){
		this.name=name;
	}

	/**
	 * 隐藏软键盘
	 */
	protected void hideSoftInput(){
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		if(getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED){
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
    }

	/**
	 * 返回
	 * @param v
     */
	public void back(View v){
		finish();
	}

	@Override
	public void finish() {
		super.finish();

	}

	/**
	 * 打印log，通过tag：Railway12306
	 * @param message
     */
	public void log(String message){
		Log.i("LockScreen", TAG + ": \n   " + message);
		Log.i(TAG, message + "");
	}

	public void showFragment(Fragment f, int id) {
		FragmentTransaction mft = getSupportFragmentManager().beginTransaction();
		if (f.isAdded()) {
			mft.show(f);
		} else {
			mft.add(id, f);
		}
		mft.commitAllowingStateLoss();
	}

	public void hideFragment(Fragment f ) {
		FragmentTransaction mft = getSupportFragmentManager().beginTransaction();
		if (f.isAdded()) {
			mft.hide(f);
		}
		mft.commitAllowingStateLoss();
	}

	@Override
	public void startActivity(Intent intent) {
		intent.putExtra("FromActivityName",TAG);
		super.startActivity(intent);
//		overridePendingTransition(R.anim.slide_right_in, R.anim.suo_xiao_08);
	}
	public void startActivity(Class<?> cls) {
		Intent intent=new Intent(this,cls);
		intent.putExtra("FromActivityName",TAG);
		super.startActivity(intent);
//		overridePendingTransition(R.anim.slide_right_in, R.anim.suo_xiao_08);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		this.overridePendingTransition(R.anim.fangda_from08,
//				R.anim.slide_right_out);

	}
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		try {
			// TODO Auto-generated method stub
			super.startActivityForResult(intent, requestCode);
//			overridePendingTransition(R.anim.slide_right_in, R.anim.suo_xiao_08);
		} catch (Exception e){
			// TODO: handle exception
		}
	}

}
