package com.xcyo.live.activity.user_info;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.BaseServerParamHandler;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.activity.attention_list.AttentionListActivity;
import com.xcyo.live.activity.fans_list.FansListActivity;
import com.xcyo.live.record.UserSimpleRecord;
import com.xcyo.live.utils.ServerEvents;
import com.xcyo.live.activity.message.MessageActivity;

/**
 * Created by caixiaoxiao on 12/6/16.
 */
public class UserInfoActPresenter extends BaseActivityPresenter<UserInfoActivity,UserInfoActRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {
        if(ServerEvents.CALL_SERVER_METHOD_OTHER_BASE_INFO.equals(evt)){
            mRecord.mRecord = (UserSimpleRecord)data.record;
            mActivity.showUserInfo(mRecord.getRecord().getUser());
        }
    }

    @Override
    public void loadDatas() {
        super.loadDatas();
        obtainUserInfo();
    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("back".equals(str)){
                mActivity.finish();
            }else if ("attention".equals(str)){
                Intent intent = new Intent(mActivity, AttentionListActivity.class);
                mActivity.startActivity(intent);
            }else if ("fans".equals(str)){
                Intent intent = new Intent(mActivity, FansListActivity.class);
                mActivity.startActivity(intent);
            }else if ("goAttention".equals(str)){

            }else if ("goMsg".equals(str)){
                Intent intent = new Intent(mActivity, MessageActivity.class);
                intent.putExtra("uid",mActivity.getUid());
                intent.putExtra("alias",mActivity.getAlias());
                mActivity.startActivity(intent);
            }else if ("goForbid".equals(str)){

            }
        }
    }

    public void obtainUserInfo(){
        callServer(ServerEvents.CALL_SERVER_METHOD_OTHER_BASE_INFO, new PostParamHandler("uid", mActivity.getUid()));
    }
}
