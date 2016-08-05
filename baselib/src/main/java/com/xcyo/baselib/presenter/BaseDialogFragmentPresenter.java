package com.xcyo.baselib.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.ui.BaseDialogFragment;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.baselib.server.ServerBinderData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghongyu on 31/12/15.
 */
public abstract class BaseDialogFragmentPresenter<F extends BaseDialogFragment, R extends BaseRecord> extends BaseActivityPresenter<BaseActivity, R>{
    protected F mFragment;
    public void onCreate(F fragment) {
        mFragment = fragment;
        super.onCreate(null);
    }

    public F fragment(){
        return mFragment;
    }

    protected void onServerCallbackInner(String evt, ServerBinderData data){
        onServerCallback(evt, data);
        if(mFragment != null){
            mFragment.onServerCallback(evt, data);
            List<Fragment> fragments = mFragment.getChildFragmentManager().getFragments();
            if(fragments != null) {
                for (int i = 0; i < fragments.size(); i++) {
                    BaseFragment baseFragment = (BaseFragment) fragments.get(i);
                    baseFragment.presenter().onServerCallback(evt, data);
                    baseFragment.onServerCallback(evt, data);
                }
            }
        }
    }

    protected void onClickInner(View v, Object data){
//        if (mFragment != null){
//            mFragment.onClick(v, data);
//        }
        onClick(v, data);
    }

    @Override
    protected Activity getActivity() {
        return mFragment.getActivity();
    }
}
