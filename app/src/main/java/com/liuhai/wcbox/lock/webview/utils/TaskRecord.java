package com.liuhai.wcbox.lock.webview.utils;

import android.util.Log;

public class TaskRecord {
	private long startTime;
	private String tag;
	private long endingTime;
	private boolean isDisplayResultImmediately;

	public void startTrace(String tag, boolean isDisplayResultImmediately) {
		this.tag = tag;
		this.startTime = System.currentTimeMillis();
		this.isDisplayResultImmediately = isDisplayResultImmediately;
	}

	public void endingTrace(String tag) {
		this.endingTime = System.currentTimeMillis();
		if (isDisplayResultImmediately) {
			Log.i("TraceTracker", "tag:" + tag + ": startwith:" + startTime + " ending with:" + endingTime
					+ "useing time:" + (endingTime - startTime) + "ms");
		}
	}
}
