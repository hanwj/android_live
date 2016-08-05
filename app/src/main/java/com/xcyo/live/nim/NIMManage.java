package com.xcyo.live.nim;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.event.EventMapper;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.live.utils.ServerEvents;

import java.util.List;

/**
 * Created by caixiaoxiao on 1/7/16.
 */
public class NIMManage {
    private final static String TAG = NIMManage.class.getSimpleName();
    private NIMManage(){

    }
    private static class Singleton{
        final static NIMManage instance = new NIMManage();
    }

    public static NIMManage getInstance(){
        return Singleton.instance;
    }


    public void login(String account,String token, final RequestCallback<LoginInfo> callback){
        NIMClient.getService(AuthService.class).login(new LoginInfo(account,token)).setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                LogUtil.e(TAG,"Login OK");
                initNIM();
                callback.onSuccess(loginInfo);
            }

            @Override
            public void onFailed(int i) {
                LogUtil.e(TAG,"Login failed : " + i);
                callback.onFailed(i);
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    private Observer<List<RecentContact>> mRecentContactObserve = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            Event.getInstance().dispatch(ServerEvents.RECENTCONTACT_LIST_UPDATE,recentContacts);
        }
    };

    private void initNIM(){
        //读取本地用户资料
        NIMUserInfoCache.getInstance().buildCache();
        NIMUserInfoCache.getInstance().registerObservers(true);
        //监听最近会话变更
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(mRecentContactObserve, true);

    }

}
