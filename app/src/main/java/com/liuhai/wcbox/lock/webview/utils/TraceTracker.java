package com.liuhai.wcbox.lock.webview.utils;

import java.util.HashMap;
import java.util.Map;

public class TraceTracker {

	private static Map<String, TaskRecord> records = new HashMap<String, TaskRecord>();
	private static TraceTracker traceTracker;

	public static synchronized TraceTracker getInstance() {
		if (traceTracker == null) {
			traceTracker = new TraceTracker();
		}
		return traceTracker;
	}

	public void startTrace(String tag, boolean isDisplayResultImmediately) {
		if (!records.containsKey(tag)) {
			TaskRecord record = new TaskRecord();
			record.startTrace(tag, isDisplayResultImmediately);
			records.put(tag, record);
		} else {
			records.get(tag).startTrace(tag, isDisplayResultImmediately);
		}
	}

	public void endingTrace(String tag) {
		if (records.containsKey(tag)) {
			records.get(tag).endingTrace(tag);
		}
	}

}
