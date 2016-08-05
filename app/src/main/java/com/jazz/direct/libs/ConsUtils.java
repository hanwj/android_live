package com.jazz.direct.libs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * Created by TDJ on 2016/5/31.
 */
public class ConsUtils {

    public static final int[] getDisplayMetrics(@NonNull android.content.Context context){
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        return new int[]{metric.widthPixels, metric.heightPixels};
    }

    public static final boolean isWIFI(@NonNull android.content.Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info != null && info.isConnected();
    }

    public static final boolean isNet(@NonNull android.content.Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }
}
