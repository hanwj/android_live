package com.xcyo.baselib.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wanghongyu on 30/12/15.
 */
public class Util {
    private static final String TAG = "baselib.Util";
    public static Context context = null;
    public static int versionCode = 1; //版本号
    public static String versionName = "1.0.0";
    public static String channelName = "自定义";
    public static String deviceId = "xxx";
    //显示popupwindow

    //获取状态栏高度
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (sbar == 0) {
            sbar = Util.dp(20);
        }
        return sbar;
    }

    //获取屏幕高度
    public static int getScreenHeight() {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth() {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    //dp sp px 之间转换
    public static int px2dp(int px) {
        return (int) (1.0f * px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int dp2px(int dp) {
        return (int) (1.0f * dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int sp2px(int sp) {
        return (int) (1.0f * sp * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public static int px2sp(int px) {
        return (int) (1.0f * px / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public static int sp2dp(int sp) {
        return px2dp(sp2px(sp));
    }

    public static int dp2sp(int dp) {
        return px2sp(dp2px(dp));
    }

    public static int dp(int dp) {
        return dp2px(dp);
    }

    public static int sp(int sp) {
        return sp2px(sp);
    }

    /**
     * 检测网络状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //读取asstes图片
    public static Bitmap getImageFromAssetsFile(String fileName, Context context) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    //验证邮箱格式
    public static boolean isEmail(String email) {
        String str = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();

    }
    //验证手机号码格式
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^1(3[0-9]|4[57]|5[0-35-9]|8[025-9]|7[0-9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /*检查字符串是否为空或者Empty*/
    public static boolean isEmpty(String content){
        if(content == null)
            return true;
        if(content.equalsIgnoreCase("null"))
            return true;
        if(content.isEmpty())
            return true;
        return false;
    }

    /*处理字符串为null*/
    public static final String handNull(String content){
        return content == null ? "" : content;
    }

    /***
     * 获取文件在sd卡中的存储根目录，一般是：/sdcard/.[package name]/
     * @param ctx
     * @return
     */
    public static String getExternalStorageDirectory(Context ctx){
        String path;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            return null;
        }
        return path + "/" + "." + ctx.getPackageName();
    }

    /***
     * 判断sdcard上是否存在该文件
     * @param filePath  相对路径
     * @param ctx
     * @return
     */
    public static boolean existFileInSDCard(String filePath,Context ctx){
        String sdDir = getExternalStorageDirectory(ctx);
        if (sdDir != null){
            filePath = sdDir + "/" + filePath;
            File file = new File(filePath);
            if (file.exists() && file.isFile()){
                return true;
            }
        }
        return false;
    }
}
