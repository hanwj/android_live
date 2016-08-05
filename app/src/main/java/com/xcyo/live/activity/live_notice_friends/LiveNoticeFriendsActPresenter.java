package com.xcyo.live.activity.live_notice_friends;

import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by caixiaoxiao on 22/6/16.
 */
public class LiveNoticeFriendsActPresenter extends BaseActivityPresenter<LiveNoticeFriendsActivity,LiveNoticeFriendsActRecord> {

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("back".equals(str)){
                mActivity.finish();
            }
        }
    }
}
