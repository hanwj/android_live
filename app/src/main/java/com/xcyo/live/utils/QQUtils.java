package com.xcyo.live.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xcyo.live.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TDJ on 2016/6/18.
 */
public class QQUtils implements IUiListener, LuncherUtils {

    public static final String LUNCHER_ACTION = "luncher.qq.action";
    private static final String QQ_PID = "201221220";
    private Activity mContext;
    private Tencent mTencent;

    private IUiListener mListener;


    public QQUtils(Activity context) {
        this.mContext = context;
        mListener = this;
        mTencent = Tencent.createInstance(QQ_PID, this.mContext.getApplicationContext());
    }

    @Override
    public void startLogin() {
        if (mTencent != null && !mTencent.isSessionValid() && mContext instanceof Activity) {
            mTencent.login((Activity) mContext, "all", mListener);
        }
    }

    public void setIUiListener(IUiListener mListener) {
        if (mListener != null) {
            this.mListener = mListener;
        }
    }


    @Override
    public void onComplete(Object o) {
        android.util.Log.d("TAG", o.toString());
//        {
//            "ret":0,
//                "pay_token":"xxxxxxxxxxxxxxxx",
//                "pf":"openmobile_android",
//                "expires_in":"7776000",
//                "openid":"xxxxxxxxxxxxxxxxxxx",
//                "pfkey":"xxxxxxxxxxxxxxxxxxx",
//                "msg":"sucess",
//                "access_token":"xxxxxxxxxxxxxxxxxxxxx"
//        }
        try {
            JSONObject object = new JSONObject(o.toString());

            Intent handIntent = new Intent();
            handIntent.setAction(QQUtils.LUNCHER_ACTION);
            handIntent.putExtra(LuncherUtils.LUNCHER_PARAMS_TAG, object.getString("openid"));
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(handIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(UiError uiError) {
        android.util.Log.d("TAG", uiError.errorMessage);
    }

    @Override
    public void onCancel() {
        android.util.Log.d("TAG", "onCancel");
    }

    private void LogoutQQ() {
        if (mTencent != null) {
            mTencent.logout(mContext);
            this.mContext = null;
        }
    }


    @Override
    public void share(String url, String title, String disc, Bitmap icon) {

        final android.os.Bundle params = new android.os.Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, disc);
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mContext.getResources().getString(R.string.app_name));
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        mTencent.shareToQQ(mContext, params, this);

    }

    public void shareToQzone (String url, String title, String disc, Bitmap icon) {
        //分享类型
        final android.os.Bundle params = new android.os.Bundle();
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, disc);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
//        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, "");
        mTencent.shareToQzone(mContext, params, this);
    }
}
