package com.liuhai.wcbox.lock.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
	public String TAG;
	public String name="";

	private int mLayoutId;
	public View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		log(TAG
				+ " onCreate------------------------------------------------------------------------------------");
	}



	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	public void setPageName(String name){
		this.name=name;
	}

	@Override
	public void onDestroy() {
		log(TAG
				+ " onDestroy------------------------------------------------------------------------------------");
		super.onDestroy();
	}

 

	@Override
	public void onPause() {
		log(TAG
				+ " onPause------------------------------------------------------------------------------------");
		super.onPause();
	}

	@Override
	public void onResume() {
		log(TAG
				+ " onResume------------------------------------------------------------------------------------");
		super.onResume();
	}

	@Override
	public void onStop() {
		log(TAG
				+ " onStop------------------------------------------------------------------------------------");
		super.onStop();
	}

	@Override
	public void onStart() {
		log(TAG
				+ " onStart------------------------------------------------------------------------------------");
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		log(TAG + " onCreateView------------------------------------------------------------------------------------");
		return super.onCreateView(inflater, container, savedInstanceState);
	}


	public void log(String log) {
		Log.i("LockScreen", TAG + "    " + log);
		Log.i(TAG, log);
	}
	public View findViewById(int id){
		return  rootView.findViewById(id);
	}

	public void startActivity(Class<?> cls) {
		Intent intent=new Intent(this.getActivity(),cls);
		intent.putExtra("FromActivityName",TAG);
		super.startActivity(intent);
	}

	@Override
	public void startActivity(Intent intent) {
		intent.putExtra("FromActivityName",TAG);
		super.startActivity(intent);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}
}
