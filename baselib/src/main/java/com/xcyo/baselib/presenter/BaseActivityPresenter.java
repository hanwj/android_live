package com.xcyo.baselib.presenter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.event.EventMapper;
import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.baselib.server.ServerBinder;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.BaseServerParamHandler;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.baselib.utils.Constants;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.baselib.utils.ToastUtil;
import com.xcyo.baselib.utils.Util;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wanghongyu on 31/12/15.
 */
public abstract class BaseActivityPresenter<A extends BaseActivity, R extends BaseRecord> extends EventMapper implements View.OnClickListener{
    private static final String TAG = "BaseActivityPresenter";
    private HashMap<View, Object> mClickDataMap;
    protected R mRecord;
    protected A mActivity;
    @Override
    public void onClick(View v) {
        Object data = mClickDataMap.get(v);
        onClickInner(v, data);
    }

    public void loadDatas(){
    }

    public void onCreate(A activity){
        mActivity = activity;
        mRecord = createRecord();
        mClickDataMap = new HashMap<View, Object>();
        this.onCreate();
    }

    public R record(){
        return mRecord;
    }

    private R createRecord(){
        Class genericClass = (Class)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        try {
            return (R)genericClass.newInstance();
        } catch (InstantiationException e) {
            LogUtil.e(TAG, e);
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, e);
        }
        return null;
    }

    protected abstract void onServerCallback(String evt, ServerBinderData data);
    protected abstract void onClick(View v, Object data);
    protected Activity getActivity(){
        return mActivity;
    }

    protected void onServerCallbackInner(String evt, ServerBinderData data){
        onServerCallback(evt, data);
        if(mActivity != null){
            mActivity.onServerCallback(evt, data);
            List<Fragment> fragments = mActivity.getSupportFragmentManager().getFragments();
            if (fragments != null) {
                for (int i = 0; i < fragments.size(); i++) {
                    Fragment frag = fragments.get(i);
                    if (frag != null && frag instanceof BaseFragment){
                        BaseFragment baseFragment = (BaseFragment) frag;
                        baseFragment.presenter().onServerCallback(evt, data);
                        baseFragment.onServerCallback(evt, data);
                    }
                }
            }
        }
    }

    protected void onServerFailedCallbackInner(String evt, ServerBinderData data){
        onServerFailedCallback(evt, data);
        if(mActivity != null){
            mActivity.onServerFailedCallback(evt, data);
            List<Fragment> fragments = mActivity.getSupportFragmentManager().getFragments();
            if (fragments != null) {
                for (int i = 0; i < fragments.size(); i++) {
                    Fragment frag = fragments.get(i);
                    if (frag != null && frag instanceof BaseFragment){
                        BaseFragment baseFragment = (BaseFragment) frag;
                        baseFragment.presenter().onServerFailedCallback(evt, data);
                        baseFragment.onServerFailedCallback(evt, data);
                    }
                }
            }
        }
    }

    protected void onServerFailedCallback(String evt,ServerBinderData data){
        ToastUtil.toastTip(getActivity(),data.msg);
    }

    protected void onClickInner(View v, Object data){
//        if (mActivity != null){
//            mActivity.onClick(v, data);
//        }
        onClick(v, data);
    }

    public void addOnClickListener(View view, Object data){
        view.setOnClickListener(this);
        mClickDataMap.put(view, data);
    }

    public void callServer(String evt,  BaseServerParamHandler t){
        Event.getInstance().mapEvent(evt, this, new Event.EventCallback() {
            @Override
            public boolean onEvent(String evt, Object data) {
                if (((ServerBinderData) data).errorCode == Constants.CALL_SERVER_METHOD_SUCC) {
                    onServerCallbackInner(evt, (ServerBinderData) data);
                } else {
                    onServerFailedCallbackInner(evt, (ServerBinderData) data);
                }
                return true;
            }
        });
        if (Util.isNetworkAvailable(getActivity())){
            ServerBinder.getInstance().call(evt, t);
        }else {
            ServerBinderData data = new ServerBinderData();
            data.errorCode = Constants.CALL_SERVER_METHOD_ERROR_NETWORK;
            data.msg = "当前没有可用网络";
            dispatch(evt, data);
        }
    }

    public void mapEvent(String evt, Event.EventCallback cb){
        Event.getInstance().mapEvent(evt, this, cb);
    }

    public void dispatch(String evt, Object data){
        Event.getInstance().dispatch(evt, data);
    }

    public void dispatch(String evt){
        Event.getInstance().dispatch(evt);
    }


    //子类可以使用如下生命周期
    public void onCreate(){
    }
    public void onDestroy(){
        onExit();
    }
    public void onStart(){
    }
    public void onStop(){
    }
    public void onResume(){
    }
    public void onPause(){
    }
}
