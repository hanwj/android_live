package com.xcyo.baselib.utils;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by wanghongyu on 30/12/15.
 */
public class LogUtil {
    public static boolean isOpen = true;
    public static void i(String tag, String msg){
        if (Constants.LOG_OPEN){
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if (Constants.LOG_OPEN){
            Log.e(tag, msg);
        }
    }

    public static String[] exceptionToString(Throwable e){
        StackTraceElement[] eles = e.getStackTrace();
        String []ret = new String[eles.length + 1];
        ret[0] = e.toString();
        for (int i = 0; i < eles.length; i++) {
            ret[i + 1] = "  at " + eles[i].toString();
        }
        return ret;
    }

    public static void e(String tag, Throwable tr){
        String exarr[] = exceptionToString(tr);
        for (int i = 0; i < exarr.length; i++){
            e(tag, exarr[i]);
        }
    }
}
