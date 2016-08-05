package com.xcyo.live.activity.home_filter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by caixiaoxiao on 7/6/16.
 */
public class HomeFilterActPresenter extends BaseActivityPresenter<HomeFilterActivity,HomeFilterActRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("back".equals(str)){
                mActivity.finish();
            }else if ("confirm".equals(str)){
                Intent intent = new Intent();
                intent.putExtra("sex",mActivity.getCheckedSex());
                intent.putExtra("city",mActivity.getSelectedCity());
                mActivity.setResult(Activity.RESULT_OK,intent);
                mActivity.finish();
            }
        }
    }
}
