package com.xcyo.baselib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.presenter.BaseFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.BaseServerParamHandler;
import com.xcyo.baselib.utils.LogUtil;

import java.lang.reflect.ParameterizedType;

/**
 * Created by wanghongyu on 31/12/15.
 */
public abstract class BaseFragment<P extends BaseFragmentPresenter> extends Fragment{
    private static final String TAG = "BaseFragment";
    private P mPresenter;
    private boolean mHidden = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.onCreate(this);
        initArgs();
        View v = initViews(inflater, container);
        applyHidden(v);
        initEvents();
        mPresenter.loadDatas();
        return v;
    }

    public boolean OnBackProgress(){
        return false;
    }

    public P presenter(){
        return mPresenter;
    }

    private P createPresenter(){
        Class genericClass = (Class)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            return (P)genericClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            LogUtil.e(TAG, e);
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, e);
        }
        return null;
    }

    private void applyHidden(View v){
        if(v != null) {
            if (mHidden) {
                v.setVisibility(View.INVISIBLE);
            } else {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    private void applyHidden(){
        applyHidden(getView());
    }

    public void setHidden(boolean hidden){
        if (hidden != mHidden){
            mHidden = hidden;
        }
        applyHidden();
    }

    protected abstract void initArgs();
    protected abstract View initViews(LayoutInflater inflater, ViewGroup container);
    protected abstract void initEvents();
    public abstract void onServerCallback(String evt, ServerBinderData data);
//    public abstract void onClick(View v, Object data);

    public void onServerFailedCallback(String evt,ServerBinderData data){
        //请求出错
    }

    protected void addOnClickListener(View view, Object data){
        mPresenter.addOnClickListener(view, data);
    }

    protected void callServer(String evt, BaseServerParamHandler t){
        mPresenter.callServer(evt,  t);
    }

    protected void addFragment(BaseFragment frag, int rid, boolean addToBackStack){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(rid, frag, frag.getClass().getSimpleName());
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    protected void replaceFragment(BaseFragment frag, int rid, boolean addToBackStack){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(rid, frag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    protected void removeFragment(BaseFragment frag){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.remove(frag);
        transaction.commit();
    }

    public void mapEvent(String evt, Event.EventCallback cb){
        presenter().mapEvent(evt, cb);
    }

    public void dispatch(String evt, Object data){
        presenter().dispatch(evt, data);
    }

    public void dispatch(String evt){
        presenter().dispatch(evt);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }
}
