package com.xcyo.baselib.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by lovingyou on 2016/2/2.
 */
public class ToastUtil {
    private static boolean isOpen = false;
    private static Toast toast;

    public static void toastTip(Context context,@NonNull String msg) {
        showToast(context, msg);
    }

    public static void toastDebug(Context context,@NonNull String msg) {
        if (Constants.LOG_OPEN) {
            showToast(context, msg);
        }
    }

    /**
     * 单例吐司
     *
     * @param context
     * @param msg
     */
    private static void showToast(Context context,@NonNull String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
