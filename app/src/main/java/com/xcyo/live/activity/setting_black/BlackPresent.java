package com.xcyo.live.activity.setting_black;

import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by TDJ on 2016/6/18.
 */
public class BlackPresent extends BaseActivityPresenter<BlackActivity, BlackRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String)data;
        if("finish".equals(action)){
            mActivity.finish();
        }
    }
}
