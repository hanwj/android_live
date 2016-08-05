package com.xcyo.live.dialog.live_chat;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomStatusChangeData;
import com.xcyo.baselib.presenter.BaseDialogFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.activity.live_end.LiveEndActivity;
import com.xcyo.live.activity.room.viewholder.ChatRoomMsgRecord;
import com.xcyo.live.anim_live.SlidGift;
import com.xcyo.live.dialog.live_gift.LiveGiftDialogFragment;
import com.xcyo.live.dialog.live_gift.LiveGiftDialogPresent;
import com.xcyo.live.dialog.live_message_list.LiveMessageListDialogFragment;
import com.xcyo.live.dialog.live_share.LiveShareDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 6/7/16.
 */
public class LiveChatDialogPresenter extends BaseDialogFragmentPresenter<LiveChatDialogFragment,LiveChatDialogRecord> {
    private final static String TAG = LiveChatDialogPresenter.class.getSimpleName();
    private Observer<ChatRoomStatusChangeData> onlineObserver = new Observer<ChatRoomStatusChangeData>() {
        @Override
        public void onEvent(ChatRoomStatusChangeData chatRoomStatusChangeData) {
            if (StatusCode.UNLOGIN == chatRoomStatusChangeData.status){

            }else if (StatusCode.LOGINED == chatRoomStatusChangeData.status){

            }else if (StatusCode.LOGINING == chatRoomStatusChangeData.status){

            }else if (StatusCode.KICKOUT == chatRoomStatusChangeData.status){

            }
        }
    };

    private Observer<List<ChatRoomMessage>> incomingMsgObserver = new Observer<List<ChatRoomMessage>>() {
        @Override
        public void onEvent(List<ChatRoomMessage> chatRoomMessages) {
//            mFragment.addMessage(chatRoomMessages,true);
        }
    };

    @Override
    public void loadDatas() {
        super.loadDatas();
        initChatRoom();
    }

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("root".equals(str)){
                mFragment.showSendLayout(false);
            }else if ("barrage".equals(str)){
                mFragment.switchBarrage();
            }else if ("send".equals(str)){
                sendMessage();
            }else if("chat".equals(str)){
                mFragment.showSendLayout(true);
            }else if("recent".equals(str)){
                new LiveMessageListDialogFragment().show(mFragment.getActivity().getSupportFragmentManager(), LiveMessageListDialogFragment.class.getName());
            }else if("music".equals(str)){
            }else if("pop".equals(str)){
                mFragment.getLiveHelper().showUpPop(v);
            }else if("share".equals(str)){
                new LiveShareDialogFragment().show(mFragment.getActivity().getSupportFragmentManager(), LiveShareDialogFragment.class.getName());
            }else if("gift".equals(str)){
                new LiveGiftDialogFragment().setGiftCallBack(getGiftListener()).show(mFragment.getActivity().getSupportFragmentManager(), LiveGiftDialogFragment.class.getName());
            }else if("exit".equals(str)){
                getActivity().startActivity(new Intent(getActivity(), LiveEndActivity.class));
                getActivity().finish();
            }
        }
    }

    private LiveGiftDialogPresent.OnGiftCallBack getGiftListener(){
        return new LiveGiftDialogPresent.OnGiftCallBack() {
            @Override
            public void gift() {
                SlidGift.GiftRecord record = new SlidGift.GiftRecord();
                record.gid = ((int)(Math.random()*10))+"";
                record.disc = "兄弟会之剑";
                record.giftNumber = ((int)(Math.random()*100))+"";
                record.name = "哈哈";
                mFragment.getGiftManager().put(record);
            }
        };
    }

    protected void initChatRoom(){
        registerObserver(true);
    }

    protected void registerObserver(boolean register){
        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineObserver, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingMsgObserver, register);
    }


    public void sendMessage(){
        String content = mFragment.getInput();
        if (TextUtils.isEmpty(content)){
            return;
        }
        String yunxinRoomId = mFragment.getYunxinRoomId();
        content = ChatRoomMsgRecord.createRoomTextMsg(content);
        final ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(yunxinRoomId,content);
        NIMClient.getService(ChatRoomService.class)
                .sendMessage(message,false)
                .setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mFragment.clearInput();
                List<ChatRoomMessage> messages = new ArrayList<ChatRoomMessage>();
                messages.add(message);
//                mFragment.addMessage(messages, false);
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    @Override
    public void onDestroy() {
        registerObserver(false);
        super.onDestroy();
    }
}
