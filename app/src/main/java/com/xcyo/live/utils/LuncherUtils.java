package com.xcyo.live.utils;

import android.graphics.Bitmap;

/**
 * Created by TDJ on 2016/6/18.
 */
public interface LuncherUtils {

    public static final String LUNCHER_PARAMS_TAG = "luncher.tag.code";

    public void startLogin();

    public void share(String url, String title, String disc, Bitmap icon);
}
