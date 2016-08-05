package com.xcyo.live.activity.add_topic;

import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by caixiaoxiao on 6/6/16.
 */
public class AddTopicActPresenter extends BaseActivityPresenter<AddTopicActivity,AddTopicActRecord> {
    @Override
    public void loadDatas() {
        super.loadDatas();
        callServer(ServerEvents.CALL_SERVER_METHOD_TOPIC_LIST, new PostParamHandler());

    }

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
