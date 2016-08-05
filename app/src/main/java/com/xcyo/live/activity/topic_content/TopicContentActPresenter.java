package com.xcyo.live.activity.topic_content;

import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by caixiaoxiao on 3/6/16.
 */
public class TopicContentActPresenter extends BaseActivityPresenter<TopicContentActivity,TopicContentActRecord> {

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("back".equals(str)){
                mActivity.finish();
            }else if ("enjoy".equals(str)){

            }
        }
    }
    public void loadSingerList(){
        callServer(ServerEvents.CALL_SERVER_METHOD_TOPIC_ROOM,new PostParamHandler("topic",mActivity.getTopic()));
    }
}