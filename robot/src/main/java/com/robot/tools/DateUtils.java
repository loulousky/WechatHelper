package com.robot.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式化工具
 * 
 * @author zengtao 2015年5月6日 下午3:27:14
 */
public class DateUtils {

	public static String dateToString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		return df.format(date);
	}
}
