package com.xcyo.live.activity.user_wxbind;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by TDJ on 2016/6/7.
 */
public class BindWXPresent extends BaseActivityPresenter<BindWXActivity, BindWXRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("goBind".equals(action)){

        }else if("finish".equals(action)){
            getActivity().finish();
        }
    }
}
