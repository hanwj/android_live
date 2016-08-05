package com.xcyo.live.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by caixiaoxiao on 16/6/16.
 */
public class Constants {
    public final static String SEX_FEMALE = "0";
    public final static String SEX_MALE = "1";
    public final static String SEX_ALL = "2";

    public static final String SD_CACHE = Environment.getExternalStorageDirectory().getPath() + "/" + "com.xcyo.live"+"/";

    static{
        File cache = new File(SD_CACHE);
        if(!cache.exists()){
            cache.mkdirs();
        }
    }
}
