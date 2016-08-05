package com.xcyo.live.activity.main;

import android.view.View;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.live.nim.NIMManage;
import com.xcyo.live.nim.NIMUserInfoCache;
import com.xcyo.live.model.UserModel;
import com.xcyo.live.record.LoginServerRecord;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by caixiaoxiao on 31/5/16.
 */
public class MainActPresent extends BaseActivityPresenter<MainActivity,MainActRecord> {
    private static String TAG = MainActPresent.class.getSimpleName();
    @Override
    public void loadDatas() {
        callServer(ServerEvents.CALL_SERVER_METHOD_SYSTEM_START,new PostParamHandler());
//        autoLogin();
    }

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_SYSTEM_START.equals(evt)){

        }else if (ServerEvents.CALL_SERVER_METHOD_LOGIN.equals(evt)){
            //登陆成功，更新用户账号信息
            UserModel.getInstance().updateAccountInfo((LoginServerRecord) data.record);

            RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo loginInfo) {
                }

                @Override
                public void onFailed(int i) {
                }

                @Override
                public void onException(Throwable throwable) {
                }
            };
            NIMManage.getInstance().login(UserModel.getInstance().getUid(),UserModel.getInstance().getYunxinToken(),callback);
        }
    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("home".equals(str)){
                mActivity.showFragment(MainActivity.HOME_FRAG);
            }else if("me".equals(str)){
                mActivity.showFragment(MainActivity.ME_FRAG);
            }else if("live".equals(str)){
//                DirectUtils.openDirectActivity(getActivity(), LiveActivity.class, "rtmp://p1.live.126.net/live/b0bcaa86d7f34069831358de0c75aaad?wsSecret=3dc4d91bfcd4ae306ffc9dc2f0237ce7&wsTime=1464777627");
                mActivity.showLivePreviewActivity();
            }
        }
    }

    //自动登录
    private void autoLogin(){
//        callServer(ServerEvents.CALL_SERVER_METHOD_LOGIN,new PostParamHandler("source","qq","type","pid","pid","qq_67890"));
        callServer(ServerEvents.CALL_SERVER_METHOD_LOGIN,new PostParamHandler("source","qq","type","pid","pid","qq_0123"));
    }
}
