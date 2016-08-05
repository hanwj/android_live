package com.xcyo.live.activity.user_right;

import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by TDJ on 2016/6/7.
 */
public class RightPresent extends BaseActivityPresenter<RightActivity, RightRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String)data;
        if("finish".equals(action)){
            getActivity().finish();
        }else if("".equals(action)){

        }
    }
}
