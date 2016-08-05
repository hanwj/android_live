package com.xcyo.baselib.server.paramhandler;

import com.xcyo.baselib.utils.LogUtil;
import com.xutils.http.RequestParams;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by wanghongyu on 30/12/15.
 */
public abstract class BaseServerParamHandler implements Serializable{
    private static final String TAG = "BaseServerParamHandler";
    protected HashMap<String, String> mKeyValues;
    protected HashMap<String ,String> mExtraKeyValues;
    protected boolean isSecret = true;
    protected BaseServerParamHandler(){
    }

    public boolean getSecret(){
        return isSecret;
    }

    public BaseServerParamHandler(boolean isSecret, String... params){
        this.isSecret = isSecret;
        if (params.length % 2 != 0){
            LogUtil.e(TAG, "[ERROR]params count cant devide by 2");
        }

        initArgs(params);
    }

    public BaseServerParamHandler(String... params){
        this(true,params);
    }

    protected void initArgs(String []params){
        mKeyValues = new HashMap<String, String>();
        if (params != null) {
            for (int i = 0; i < params.length; i += 2) {
                mKeyValues.put(params[i], params[i + 1]);
            }
        }
    }

    public abstract void addOneParam(RequestParams params, String key, String value);

    protected BaseServerParamHandler addOnePairParams(String key, String value){
        if (key != null && value != null && key.length() > 0 && value.length() > 0) {
            mKeyValues.put(key, value);
        }
        return this;
    }

    public String getValue(String key){
        return mKeyValues.get(key);
    }

    public RequestParams toRequestParams(String uri){
        RequestParams req = new RequestParams(uri);
        for(String key: mKeyValues.keySet()){
            addOneParam(req, key, mKeyValues.get(key));
        }
        int a = 2;

        return req;
    }

    @Override
    public boolean equals(Object o) {
        BaseServerParamHandler paramHandler = (BaseServerParamHandler) o;
        if(paramHandler != null) {
            return paramHandler.mKeyValues.equals(mKeyValues);
        }
        return false;
    }

    @Override
    public String toString() {
        return mKeyValues.toString();
    }
}
