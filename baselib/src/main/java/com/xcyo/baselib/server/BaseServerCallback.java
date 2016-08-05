package com.xcyo.baselib.server;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.baselib.utils.Constants;
import com.xcyo.baselib.utils.DataUtils;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.baselib.utils.TimeUtils;
import com.xutils.common.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by wanghongyu on 30/12/15.
 */

public class BaseServerCallback<T> implements Callback.CommonCallback<T>, Callback.ProgressCallback<T> {
    private static final String TAG = "BaseServerCallback";
    private ServerBinder mBinder;
    private ServerBinderData mBinderData;
    private Event mEvent;

    private static ExecutorService execu = Executors.newFixedThreadPool(5);
    private static Handler mHandler = new Handler();

    public BaseServerCallback(ServerBinder binder, ServerBinderData data, Event event) {
        mBinder = binder;
        mBinderData = data;
        mEvent = event;
    }

    @Override
    public void onWaiting() {
        LogUtil.i(mBinderData.event, "onWaiting");
    }

    @Override
    public void onStarted() {
        LogUtil.i(mBinderData.event, "onStarted");
    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        LogUtil.i(mBinderData.event, "onLoading");
        mBinderData.percent = current * 1.0f / total;
        mEvent.dispatch(Constants.CALL_SERVER_METHOD_ON_PROGRESS, mBinderData);
    }

    @Override
    public void onSuccess(T result) {
        LogUtil.i(mBinderData.event, "onSuccess:" + result);
        handleResponse(result, mBinderData);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        LogUtil.e(mBinderData.event, ex);
        realHandleResponse(getErrorResponseJson("networkerror", "网络错误"), mBinderData, true);
    }

    @Override
    public void onCancelled(CancelledException cex) {
        LogUtil.i(mBinderData.event, "onCancelled:");
        realHandleResponse(getErrorResponseJson("cancel", "user cancel"), mBinderData, true);
    }

    @Override
    public void onFinished() {
        LogUtil.i(mBinderData.event, "onFinished:");
        mBinder.removeOneOngoingTask(mBinderData.event, mBinderData.params);
    }

    private String getErrorResponseJson(String status, String msg) {
        return "{\"s\": \"" + status + "\",\"m\":\"" + msg + "\"" + ",\"timestamp\":" + TimeUtils.getCurrTime() + ",\"d\":{" + "}}";
    }

    private void handleResponse(T response, ServerBinderData binderData) {
        if (response instanceof String) {
            realHandleResponse((String) response, binderData, false);
        } else if (response instanceof File) {
            LogUtil.i(TAG, "response=" + response);
            if (response != null && ((File) response).exists() && ((File) response).length() > 100) {
                realHandleResponse(getErrorResponseJson("ok", "ok"), mBinderData, true);
            } else {
                realHandleResponse(getErrorResponseJson("ok", "file not exist"), mBinderData, true);
            }
        }
    }

    private void realHandleResponse(String response, ServerBinderData binderData, boolean forceSkipDecrypt) {
        if (!forceSkipDecrypt && binderData.params.getSecret()) {
            handResultData(response, binderData);
            return ;
        }
        workOnThread(response, binderData);
    }

    private void handResultData(final String response, final ServerBinderData binderData) {
        ConfigThreadPoll();
        execu.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] decryptByte = DataUtils.decompress(DataUtils.decrypt(DataUtils.getFromBase64(response)));
                    mHandler.post(handResutlOnUiThread(new String(decryptByte), binderData));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ConfigThreadPoll(){
        int count  = ((ThreadPoolExecutor)execu).getActiveCount();
        if(count == 5){
            execu.shutdown();
            execu = Executors.newFixedThreadPool(5);
        }
    }

    private Runnable handResutlOnUiThread(final String response, final ServerBinderData binderData) {
        return new Runnable() {
            @Override
            public void run() {
                workOnThread(response, binderData);
            }
        };
    }


    private void workOnThread(final String response, final ServerBinderData binderData){
        try {
            LogUtil.e(TAG + " " + binderData.method + " decrypt:", response);
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("s");
            String msg = "";
            Object data = null;
            if (jsonObject.has("m")){
                msg = jsonObject.getString("m");
            }
            if (jsonObject.has("d")){
                data = jsonObject.get("d");
            }

            binderData.msg = msg;
            binderData.setErrCodeRecord(status);
            switch (binderData.errorCode) {
                case Constants.CALL_SERVER_METHOD_SUCC:
                    if (data != null && binderData.clazz != null && data instanceof JSONObject) {
                        try {
                            binderData.record = (BaseRecord) new Gson().fromJson(data.toString(), binderData.clazz);
                        } catch (JsonSyntaxException e) {
                            binderData.responseData = data.toString();
                        }
                    } else {
                        binderData.responseData = data == null ? "" : data.toString();
                    }
                    break;
                case Constants.CALL_SERVER_METHOD_ERROR_SERVER:
                    mEvent.dispatch(Constants.CALL_SERVER_METHOD_ERROR, binderData);
                    break;
                case Constants.CALL_SERVER_METHOD_ERROR_NETWORK:
                    mEvent.dispatch(Constants.CALL_SERVER_METHOD_ERROR, binderData);
                    break;
                case Constants.CALL_SERVER_METHOD_ERROR_USERCANCEL:
                    mEvent.dispatch(Constants.CALL_SERVER_METHOD_ERROR, binderData);
                    break;
                default:
                    break;
            }

            mEvent.dispatch(binderData.event, binderData);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
    }
}
