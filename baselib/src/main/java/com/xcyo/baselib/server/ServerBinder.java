package com.xcyo.baselib.server;

import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.event.EventMapper;
import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.baselib.server.callback.CommonCallback;
import com.xcyo.baselib.server.callback.DownloadCallback;
import com.xcyo.baselib.server.paramhandler.BaseServerParamHandler;
import com.xcyo.baselib.utils.Constants;
import com.xcyo.baselib.utils.LogUtil;
import com.xutils.common.Callback;
import com.xutils.http.RequestParams;
import com.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by wanghongyu on 30/12/15.
 */
public class ServerBinder extends EventMapper {
    private static final String TAG = "ServerBinder";

    private Event mEvent;
    private HashMap<String, ServerBinderData> mBindDatas;
    private String mHostUrl;

    private ServerBinder(){
        mEvent = Event.getInstance();
        mBindDatas = new HashMap<String, ServerBinderData>();
    }

    private static ServerBinder sBinder = null;
    public synchronized static ServerBinder getInstance(){
        if (sBinder == null){
            sBinder = new ServerBinder();
        }
        return sBinder;
    }

    public static void initServer(BaseServerRegister register){
        getInstance().registerServerMethods(register);
    }

    public void registerServerMethods(BaseServerRegister register){
        register.doRegister(this);
        mHostUrl = register.getHostUrl();
    }

    public static void destroy(){
        if (sBinder != null){
            sBinder.onExit();
            sBinder = null;
        }
    }

    public void bindDownload(String evt){
        if (mBindDatas.containsKey(evt)){
            LogUtil.e(TAG, "[ERROR] evt:" + evt + " has been binded already in bindDownload");
            return;
        }

        ServerBinderData data = new ServerBinderData();
        data.event = evt;
        data.type = Constants.SERVER_DOWNLOAD;
        mBindDatas.put(evt, data);
    }

    public void bind(String evt, String method, String type, Class recordClass){
        if (mBindDatas.containsKey(evt)){
            LogUtil.e(TAG, "[ERROR] evt:" + evt + " has been binded already in bindDownload");
            return;
        }

        if(recordClass != null && !BaseRecord.class.isAssignableFrom(recordClass)){
            LogUtil.e(TAG, "[ERROR] evt:" + evt + ", " + recordClass.getSimpleName() + " is not subclass of BaseRecord");
            return;
        }

        ServerBinderData data = new ServerBinderData();
        data.method = method;
        data.type = type;
        data.clazz = recordClass;
        data.event = evt;
        mBindDatas.put(evt, data);
    }

    public void call(String evt, BaseServerParamHandler params){
        call(evt, params, false);
    }

    public void call(String evt, BaseServerParamHandler params, boolean force){
        if (!mBindDatas.containsKey(evt)){
            LogUtil.e(TAG, "[ERROR] server call method evt is not found: " + evt);
            return;
        }

        if(isOngoingTask(evt, params)){
            LogUtil.e(TAG, "[ERROR] server call method evt is already in queue: " + evt);
            if (force) {
                removeOneOngoingTask(evt, params).cancel();
            }else{
                return;
            }
        }

        LogUtil.i(TAG, "call " + evt + ", " + params + "," + force);

        ServerBinderData data = mBindDatas.get(evt).clone();

        data.hostUrl = mHostUrl + "/" + data.method;
        data.params = params;

        switch(data.type){
            case Constants.SERVER_DOWNLOAD:
                download(data);
                break;
            case Constants.SERVER_UPLOAD:
                upload(data);
                break;
            case Constants.SERVER_POST:
                post(data);
                break;
            case Constants.SERVER_GET:
                get(data);
                break;
        }
    }

    public void cancel(String evt, BaseServerParamHandler param){
        if (!mBindDatas.containsKey(evt)){
            LogUtil.e(TAG, "[ERROR] server cancel method evt is not found: " + evt);
            return;
        }
        if(isOngoingTask(evt, param)){
            Callback.Cancelable c = getOngoingTask(evt, param);
            c.cancel();
        }
    }
    
    private void preHandleParams(BaseServerParamHandler paramHandler, RequestParams params){
        //// TODO: 30/12/15 需要加token之类的东西
//        paramHandler.addOneParam(params, "token", "12345");

    }

    private void download(ServerBinderData data){
        data.hostUrl = data.params.getValue(Constants.DOWNLOAD_FILE_NAME_PARAMS_KEY);
        RequestParams requestParams = data.params.toRequestParams(data.hostUrl);
        preHandleParams(data.params, requestParams);
        Callback.Cancelable c = x.http().get(requestParams, new DownloadCallback(this, data, mEvent));
        addOneOngoingTask(data.event, data.params, c);
    }

    private void upload(ServerBinderData data){
        RequestParams requestParams = data.params.toRequestParams(data.hostUrl);
        preHandleParams(data.params, requestParams);
        Callback.Cancelable c = x.http().post(requestParams, new CommonCallback(this, data, mEvent));
        addOneOngoingTask(data.event, data.params, c);
    }

    private void post(final ServerBinderData data){
        RequestParams requestParams = data.params.toRequestParams(data.hostUrl);
        preHandleParams(data.params, requestParams);
        Callback.Cancelable c = x.http().post(requestParams, new CommonCallback(this, data, mEvent));
        addOneOngoingTask(data.event, data.params, c);
    }

    private void get(final ServerBinderData data){
        RequestParams requestParams = data.params.toRequestParams(data.hostUrl);
        preHandleParams(data.params, requestParams);
        Callback.Cancelable c = x.http().get(requestParams, new CommonCallback(this, data, mEvent));
        addOneOngoingTask(data.event, data.params, c);
    }

    Callback.Cancelable removeOneOngoingTask(String evt, BaseServerParamHandler param){
        if (isOngoingTask(evt, param)) {
            Callback.Cancelable c = getOngoingHttpTasks().get(evt).remove(param);
            if (getOngoingHttpTasks().get(evt).size() == 0){
                getOngoingHttpTasks().remove(evt);
            }
            return c;
        }
        return null;
    }

    private void addOneOngoingTask(String evt, BaseServerParamHandler param, Callback.Cancelable c){
        HashMap<String, Callback.Cancelable> paramToCancelMap = getOngoingHttpTasks().get(evt);
        if(!getOngoingHttpTasks().containsKey(evt)){
            paramToCancelMap = new HashMap<>();
            getOngoingHttpTasks().put(evt, paramToCancelMap);
        }
        paramToCancelMap.put(param.toString(), c);
    }

    private boolean isOngoingTask(String evt, BaseServerParamHandler param){
        if(param != null) {
            return getOngoingHttpTasks().containsKey(evt) && getOngoingHttpTasks().get(evt).containsKey(param);
        }
        return false;
    }

    private Callback.Cancelable getOngoingTask(String evt, BaseServerParamHandler param){
        if(isOngoingTask(evt, param)) {
            return getOngoingHttpTasks().get(evt).get(param.toString());
        }
        return null;
    }

    private HashMap<String, HashMap<String, Callback.Cancelable>>getOngoingHttpTasks(){
        if(mOngoingHttpTaskMap == null){
            mOngoingHttpTaskMap = new HashMap<>();
        }
        return mOngoingHttpTaskMap;
    }

    private HashMap<String, HashMap<String, Callback.Cancelable>> mOngoingHttpTaskMap;
}
