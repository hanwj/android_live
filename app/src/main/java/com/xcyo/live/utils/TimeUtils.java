package com.xcyo.live.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by caixiaoxiao on 5/7/16.
 */
public class TimeUtils {
    public static String getTimeShowString(long milliseconds){
        String dayString = "";
        String timeString = "";

        Date date = new Date(milliseconds);
        Date today = new Date();
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY,0);
        todayStart.set(Calendar.MINUTE,0);
        todayStart.set(Calendar.SECOND,0);
        todayStart.set(Calendar.MILLISECOND,0);
        Date todayBegin = todayStart.getTime();
        Date yesterdayBegin = new Date(todayBegin.getTime() - 24 * 3600 * 1000);
        Date preyesterBegin = new Date(yesterdayBegin.getTime() - 24 * 3600 * 1000);
        if (!date.before(todayBegin)){
            dayString = "今天";
        }else if (!date.before(yesterdayBegin)){
            dayString = "昨天";
        }else if (!date.before(preyesterBegin)){
            dayString = "前天";
        }else{

        }

        return dayString + " " + timeString;
    }
}
