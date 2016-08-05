package com.xcyo.baselib.server.paramhandler;

import android.nfc.Tag;
import android.os.Build;

import com.xcyo.baselib.utils.DataUtils;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.baselib.utils.TimeUtils;
import com.xcyo.baselib.utils.Util;
import com.xutils.DbManager;
import com.xutils.config.DbConfigs;
import com.xutils.http.RequestParams;
import com.xutils.http.cookie.DbCookieStore;
import com.xutils.x;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wanghongyu on 30/12/15.
 */
public class PostParamHandler extends BaseServerParamHandler {
    private String key = "";
    private static Map<String,String> mExtraKeyValue = null;
    static{
        mExtraKeyValue = new HashMap<String,String>(){
            {
                String clientInfo = "os:android,osVersion:" + Build.VERSION.RELEASE + ",phoneBrand:" + Build.BRAND
                        + ",phoneModel:" + Build.MODEL + ",appVersionName:" + Util.versionName + ",appVersionCode:" + Util.versionCode
                        + ",uuid:" + Util.deviceId + ",channel:" + Util.channelName;
                put("clientInfo",clientInfo);
                put("platform","android");
            }
        };
    }

    private PostParamHandler(){}
    public PostParamHandler(String... params){
        this(false,params);
    }

    public PostParamHandler(boolean isSecret, String ...params){
        super(isSecret, params);
    }


    @Override
    public void addOneParam(RequestParams params, String key, String value){
        LogUtil.e("Post", "参数为:" + value);
//        LogUtil.e("Post", "压缩为:"+DataUtils.getBase64(DataUtils.compress(value.getBytes())));
//        LogUtil.e("Post", "加密为:"+DataUtils.getBase64(DataUtils.encrypt(value.getBytes())));
//        LogUtil.e("Post", "加密压缩为:"+DataUtils.getBase64(DataUtils.encrypt(DataUtils.compress(value.getBytes()))));
        if (isSecret){
            params.addBodyParameter(key, DataUtils.getBase64(DataUtils.encrypt(DataUtils.compress(value.getBytes()))));
        }else{
            params.addBodyParameter(key, value);
            params.addHeader("YO-C-C", "1");
            params.addHeader("YO-C-E", "1");
        }
        params.setUseCookie(true);
    }

    /**
     * tandongjie
     * 修改post请求,将请求的参数转为json上传
     * @param uri
     * @return
     */
    @Override
    public RequestParams toRequestParams(String uri){
        RequestParams req = new RequestParams(uri);
        JSONObject params = new JSONObject();//mKeyValues
        Iterator<Map.Entry<String, String>> it = mKeyValues.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String> entry = it.next();
            if(entry != null){
                try {
                    if(val2JSON(entry.getValue())){
                        params.put(entry.getKey(), new JSONObject(entry.getValue()));
                    }else{
                        params.put(entry.getKey(), entry.getValue());
                    }
                } catch (JSONException e) {
                }
            }
        }
        JSONObject extra = new JSONObject(mExtraKeyValue);
        JSONObject data = new JSONObject();
        try {
            data.put("p",params);
            data.put("e",extra);
//            data.put("sign",);
            data.put("time", TimeUtils.getCurrTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addOneParam(req, key, data.toString());
        return req;
    }

    private boolean val2JSON(String content){
        if(content != null){
            try {
                JSONObject json = new JSONObject(content);
                return true;
            } catch (JSONException e) {
//                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
