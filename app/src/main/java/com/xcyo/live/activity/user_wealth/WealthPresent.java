package com.xcyo.live.activity.user_wealth;

import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by TDJ on 2016/6/8.
 */
public class WealthPresent extends BaseActivityPresenter<WealthActivity, WealthRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("contact".equals(action)){

        }
    }
}
