package com.xcyo.baselib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.SystemClock;
import android.text.format.DateUtils;

public class TimeUtils {
	private static long timeOffset = Long.MAX_VALUE;
	
	public static long getLocalTime(){
		return (long)(getLocalTimeMs() / 1000.0);
	}
	
	public static long getCurrTime(){
		return getCurrTimeInner(getLocalTime(), 1);
	}

	private static long getCurrTimeInner(long base, long factor){
		if(timeOffset != Long.MAX_VALUE && timeOffset != 0){
			return base + timeOffset * factor;
		}
		return base;
	}

	public static long getLocalTimeMs(){
		return System.currentTimeMillis();
	}

	public static long getCurrTimeMs(){
		return getCurrTimeInner(getLocalTimeMs(), 1000);
	}
	
	public static void adjustTimeOff(long timeStamp){
		timeOffset = timeStamp - getLocalTime();
	}
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm",Locale.SIMPLIFIED_CHINESE);
	public static String getCurrTimeStr()
	{
		String time = simpleDateFormat.format(System.currentTimeMillis());
		return time;
	}
}
