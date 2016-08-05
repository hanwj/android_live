package com.xcyo.baselib.server;

import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.baselib.server.paramhandler.BaseServerParamHandler;
import com.xcyo.baselib.utils.Constants;

import java.io.Serializable;

/**
 * Created by wanghongyu on 31/12/15.
 */
public class ServerBinderData implements Cloneable, Serializable{
    //注册参数
    public String method;
    public String type;
    public Class clazz;
    public String event;

    //输入参数
    public String hostUrl;
    public BaseServerParamHandler params;

    //输出参数
    public BaseRecord record;
    public String msg;
    public int errorCode;
    public float percent;
    public String responseData;
    public boolean isDownload(){
        return type.equals(Constants.SERVER_DOWNLOAD);
    }

    public boolean isGet(){
        return type.equals(Constants.SERVER_GET);
    }

    public boolean isPost(){
        return type.equals(Constants.SERVER_POST);
    }

    public boolean isUpload(){
        return type.equals(Constants.SERVER_UPLOAD);
    }

    @Override
    protected ServerBinderData clone() {
        ServerBinderData data = new ServerBinderData();
        data.method = method;
        data.type = type;
        data.clazz = clazz;
        data.event = event;
        data.hostUrl = hostUrl;
        data.record = record;
        data.params = params;
        data.msg = msg;
        data.percent = percent;
        return data;
    }

    public void setErrCodeRecord(String code){
        if("ok".equals(code)) {
            errorCode = Constants.CALL_SERVER_METHOD_SUCC;
        }else if ("cancel".equals(code)){
            errorCode = Constants.CALL_SERVER_METHOD_ERROR_USERCANCEL;
        }else if("networkerror".equals(code)){
            errorCode = Constants.CALL_SERVER_METHOD_ERROR_NETWORK;
        }else{
            errorCode = Constants.CALL_SERVER_METHOD_ERROR_SERVER;
        }
    }

    @Override
    public String toString() {
        return "method:" + method + ",type=" + type + ",clazz=" + clazz.getSimpleName() +
                ",event:" + event + ",record=" + (record == null? "null" :record.getClass().getSimpleName()) +
                ",params=" + (params == null ? "null" : params) + ",msg=" + (msg == null ? "null": msg) +
                ",hostUrl=" + (hostUrl == null ? "null": hostUrl) + ",percent" + percent
                ;
    }
}