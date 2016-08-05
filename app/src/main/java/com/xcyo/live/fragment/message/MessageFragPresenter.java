package com.xcyo.live.fragment.message;

import android.text.TextUtils;
import android.view.View;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xcyo.baselib.presenter.BaseFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.baselib.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 27/6/16.
 */
public class MessageFragPresenter extends BaseFragmentPresenter<MessageFragment,MessageFragRecord>{
    private Observer<List<IMMessage>> mReceiveMessageObserver;
    @Override
    public void loadDatas() {
        super.loadDatas();
        //监听接受消息
        mReceiveMessageObserver = new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {

            }
        };
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(mReceiveMessageObserver,true);

    }

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("voiceView".equals(str)){
                mFragment.setMessageType(MessageFragment.TYPE_VOICE);
            }else if ("keyboardView".equals(str)){
                mFragment.setMessageType(MessageFragment.TYPE_KEYBOARD);
            }else if ("voice".equals(str)){
                ToastUtil.toastDebug(mActivity, "语音");
            }else if ("faceView".equals(str)){
                mFragment.setBottomContainer(true, MessageFragment.FACE_VIEW);
            }else if ("moreView".equals(str)){
                mFragment.setBottomContainer(true, MessageFragment.MORE_VIEW);
            }else if ("send".equals(str)){
                sendTextMsg();
            }
        }
    }

    //发送文本消息
    private void sendTextMsg(){
        String content = mFragment.getInputString();
        if (TextUtils.isEmpty(content)){
            return;
        }
        IMMessage contentMsg = MessageBuilder.createTextMessage(mFragment.getToUid(), SessionTypeEnum.P2P,content);
        NIMClient.getService(MsgService.class).sendMessage(contentMsg, true).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtil.e("sendMsg", "success");
            }

            @Override
            public void onFailed(int i) {
                LogUtil.e("sendMsg", "failed:" + i);
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
        List<IMMessage> messages = new ArrayList<>();
        messages.add(contentMsg);
        mFragment.addMessages(messages,true);
        mFragment.clearInputString();
    }
}
