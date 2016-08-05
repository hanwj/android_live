package com.xcyo.baselib.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xcyo.baselib.R;
import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.BaseServerParamHandler;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.baselib.utils.SystemBarTintManager;

import java.lang.reflect.ParameterizedType;

/**
 * Created by wanghongyu on 29/12/15.
 */
public abstract class BaseActivity<P extends BaseActivityPresenter> extends FragmentActivity {
    private static final String TAG = "BaseActivity";
    private int viewBgColor = R.color.mainBaseColor;
    private P mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.onCreate(this);
        pushMessageInit();
        initArgs();
        initViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            View v = getWindow().getDecorView();
            View convertView = ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
            if(convertView != null){
                convertView.setFitsSystemWindows(true);
            }
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintColor(getResources().getColor(viewBgColor));
        }
        initEvents();
        mPresenter.loadDatas();
    }

    public P presenter(){
        return mPresenter;
    }

    private P createPresenter(){
        Class genericClass = (Class)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            return (P)genericClass.newInstance();
        } catch (InstantiationException e) {
            LogUtil.e(TAG, e);
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, e);
        }
        return null;
    }
    //设置背景颜色，必须在initView里面设置
    protected void setViewBgColor(int color) {
        viewBgColor = color;
    }

    protected abstract void initArgs();
    protected abstract void initViews();
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
        mPresenter.callServer(evt, t);
    }

    public void addFragment(BaseFragment frag, int rid, boolean addToBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(rid, frag,frag.getClass().getSimpleName());
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void replaceFragment(BaseFragment frag, int rid, boolean addToBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(rid, frag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void removeFragment(BaseFragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(frag);
        transaction.commit();
    }

    //事件监听，activity中慎用
    public void mapEvent(String evt, Event.EventCallback cb){
        presenter().mapEvent(evt, cb);
    }

    public void dispatch(String evt, Object data){
        presenter().dispatch(evt, data);
    }

    public void dispatch(String evt){
        presenter().dispatch(evt);
    }

    private void pushMessageInit(){
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
