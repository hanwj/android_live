package com.xcyo.live.activity.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.baselib.utils.ToastUtil;
import com.xcyo.live.activity.login_sms.LoginSMSActivity;
import com.xcyo.live.activity.main.MainActivity;
import com.xcyo.live.model.UserModel;
import com.xcyo.live.nim.NIMManage;
import com.xcyo.live.record.LoginServerRecord;
import com.xcyo.live.utils.LuncherUtils;
import com.xcyo.live.utils.QQUtils;
import com.xcyo.live.utils.ServerEvents;
import com.xcyo.live.utils.WeChatUtils;

/**
 * Created by TDJ on 2016/6/12.
 */
public class LoginPresent extends BaseActivityPresenter<LoginActivity, LoginRecord> {

    public static final int RQ_CODE = 12;
    public static final int R_CODE = 0xD3D3D3;

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {
        if(ServerEvents.CALL_SERVER_METHOD_LOGIN_LUNCHER_OTHER.equals(evt)){
            mRecord.serverRecord = (LoginServerRecord) data.record;
            UserModel.getInstance().updateAccountInfo(mRecord.getServerRecord());
            mActivity.setResult(0xD3D3D3);
            getActivity().finish();
        }else if (ServerEvents.CALL_SERVER_METHOD_LOGIN.equals(evt)){
            onLoginSucess((LoginServerRecord)data.record);
        }
    }

    @Override
    protected void onServerFailedCallback(String evt, ServerBinderData data) {
        super.onServerFailedCallback(evt, data);
        if(ServerEvents.CALL_SERVER_METHOD_LOGIN_LUNCHER_OTHER.equals(evt)){

        }
    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("wechat".equals(action)){
//            getWechatToken();
            autoLogin("qq_01234");
        }else if("qq".equals(action)){
//            getQQUserToken();
            autoLogin("qq_036541");
        }else if("phone".equals(action)){
//            goPhoneSms();
            autoLogin("qq_67890");
        }else if("more".equals(action)){
            autoLogin("qq_0123");
        }
    }

    private void getQQUserToken(){
        new QQUtils(mActivity).startLogin();
    }

    private void goPhoneSms(){
        getActivity().startActivityForResult(new Intent(getActivity(), LoginSMSActivity.class), RQ_CODE);
    }

    private void getWechatToken(){
        new WeChatUtils(mActivity).startLogin();
    }


    public BroadcastReceiver getmReceiver() {
        return mReceiver;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(QQUtils.LUNCHER_PARAMS_TAG.equals(intent.getAction())){
                String code = intent.getStringExtra(LuncherUtils.LUNCHER_PARAMS_TAG);
                callQQLogin(code);
            }else if(WeChatUtils.LUNCHER_PARAMS_TAG.equals(intent.getAction())){
                String code = intent.getStringExtra(LuncherUtils.LUNCHER_PARAMS_TAG);
                callWechatLogin(code);
            }
        }
    };

    private void callQQLogin(@NonNull String code){
        callServer(ServerEvents.CALL_SERVER_METHOD_LOGIN_LUNCHER_OTHER, new PostParamHandler("source", "qq", "type", "pid", "pid", code));
    }

    private void callWechatLogin(@NonNull String code){
        callServer(ServerEvents.CALL_SERVER_METHOD_LOGIN_LUNCHER_OTHER, new PostParamHandler("source", "wechat", "type", "pid", "pid", code));
    }

    //仅供开发测试用
    private void autoLogin(String pid){
        callServer(ServerEvents.CALL_SERVER_METHOD_LOGIN,new PostParamHandler("source","qq","type","pid","pid",pid));
    }

    private void onLoginSucess(LoginServerRecord record){
        UserModel.getInstance().updateAccountInfo(record);
        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
            }

            @Override
            public void onFailed(int i) {
                ToastUtil.toastTip(getActivity(),"请检查你的网络");
            }

            @Override
            public void onException(Throwable throwable) {
            }
        };
        NIMManage.getInstance().login(record.uid,record.yunxinToken,callback);
    }
}
