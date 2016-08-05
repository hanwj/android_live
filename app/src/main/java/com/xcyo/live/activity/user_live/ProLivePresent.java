package com.xcyo.live.activity.user_live;

import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by TDJ on 2016/6/8.
 */
public class ProLivePresent extends BaseActivityPresenter<ProLiveActivity, ProLiveRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("finish".equals(action)){
            getActivity().finish();
        }else if("reNew".equals(action)){
            mActivity.handReAction(action);
            handReNewAction();
        }else if("reHot".equals(action)){
            mActivity.handReAction(action);
            handReHotAction();
        }
    }

    private void handReNewAction(){
        handReHotAction();
    }

    private void handReHotAction(){

    }
}
