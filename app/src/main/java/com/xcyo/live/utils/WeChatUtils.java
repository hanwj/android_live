package com.xcyo.live.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xcyo.baselib.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Created by TDJ on 2016/6/18.
 */
public class WeChatUtils implements LuncherUtils{

    public static final String LUNCHER_ACTION = "luncher.qq.action";

    private static final String WECHAT_PID = "23521352";

    private IWXAPI api;

    private Context mContext;

    private static final int MAX_SCRET_LENGTH = 32;
    private static final String s = "0123456789ABCDEFGHIJKLMNOPQRESTUVWXYZabcdefghijklmnopqrestuvwxyz";
    private static final String scret = getScret();

    private static final String buildScret(){
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < MAX_SCRET_LENGTH; i++){
            buffer.append(s.charAt((int)Math.random() * (s.length()-1)));
        }
        return buffer.toString();
    }

    public static final String getScret(){
        return scret;
    }

    public WeChatUtils(Context context){
        this.mContext = context;
        regWXAPI();
    }

    private void regWXAPI(){
        api = WXAPIFactory.createWXAPI(mContext, WECHAT_PID, true);
        api.registerApp(WECHAT_PID);
    }

    @Override
    public void startLogin(){
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "";
        req.openId = WECHAT_PID;
        if(checkWX()){
            api.sendReq(req);
        }
    }

    private boolean checkWX(){
        return api.isWXAppInstalled() && api.isWXAppSupportAPI();
    }

    @Override
    public void share(String url, String title, String disc, Bitmap icon) {
        share("wechat", url, title, disc, icon);
    }

    public void share(String enverment, String url, String title, String disc, Bitmap icon) {
        if(TextUtils.isEmpty(url) || TextUtils.isEmpty(title) || TextUtils.isEmpty(disc)){
            ToastUtil.toastTip(mContext, "分享参数出错,无法调起分享");
            return ;
        }
        WXWebpageObject webPage = getWXShare(url);
        WXMediaMessage msg = buildWXMsg(webPage, title, disc, icon);
        if(msg != null && checkWX()){
            SendMessageToWX.Req req = configReq(enverment, msg);
            api.sendReq(req);
        }
    }

    private WXWebpageObject getWXShare(String url){
        WXWebpageObject page = new WXWebpageObject();
        page.webpageUrl = url;
        return page;
    }

    private WXMediaMessage buildWXMsg(WXMediaMessage.IMediaObject object, String title, String disc, Bitmap icon){
        if(object == null)
            return null;
        WXMediaMessage msg = new WXMediaMessage(object);
        msg.title = title;
        msg.description = disc;
        if(icon != null){
            msg.thumbData = bmpToByteArray(icon);
        }
        return msg;
    }

    byte[] bmpToByteArray(Bitmap b){
        ByteArrayOutputStream bps = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, bps);
        return bps.toByteArray();
    }

    SendMessageToWX.Req configReq(String enverment, WXMediaMessage msg){
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = enverment.equals("wechat") ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        return req;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
