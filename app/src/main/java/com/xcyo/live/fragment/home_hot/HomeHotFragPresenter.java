package com.xcyo.live.fragment.home_hot;

import android.view.View;

import com.xcyo.baselib.presenter.BaseFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by caixiaoxiao on 1/6/16.
 */
public class HomeHotFragPresenter extends BaseFragmentPresenter<HomeHotFragment,HomeHotFragRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {

    }

    public void loadSingerList(){
        callServer(ServerEvents.CALL_SERVER_METHOD_LIVE_HOT,new PostParamHandler());
    }
}
