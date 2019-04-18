package com.robot.bean;

/**
 * 映射服务器返回的结果
 * 
 * @author zengtao 2015年5月6日 上午9:50:52
 */
public class Result {

	private int code; // code码
	private String text; // 信息

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
