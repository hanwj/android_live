package com.xcyo.live.activity.room;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomStatusChangeData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.NotificationType;
import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.baselib.utils.ToastUtil;
import com.xcyo.live.activity.room.viewholder.ChatRoomMsgRecord;
import com.xcyo.live.model.UserModel;
import com.xcyo.live.dialog.live_files.LiveFilesDialogFragment;
import com.xcyo.live.model.RoomModel;
import com.xcyo.live.utils.ServerEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TDJ on 2016/7/6.
 */
public abstract class RoomPresent<A extends RoomActivity, R extends RoomRecord> extends BaseActivityPresenter<A, R> {

    @Override
    public void onCreate() {
        super.onCreate();
        mHelper = new RoomHelper(this);
    }

    protected void destoryHelper(){
        mHelper.destory();
    }

    @Override
    public void loadDatas() {
        super.loadDatas();
    }
    private static final String TAG = RoomPresent.class.getSimpleName();
    private RoomHelper mHelper;
    private boolean isBarrage = false;

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {
        if(ServerEvents.CALL_SERVER_METHOD_ROOM_SEND_END.equals(evt)){

        }
    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String)data;
        if("show_me".equals(action)){
            LiveFilesDialogFragment live = new LiveFilesDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("files_uid", RoomModel.getInstance().getSinger().uid);
            live.setArguments(bundle);
            live.show(mActivity.getSupportFragmentManager(), LiveFilesDialogFragment.class.getName());
        }else if ("root".equals(action)){
            mActivity.showSendLayout(false);
        }else if ("chat".equals(action)){
            mActivity.showSendLayout(true);
        }else if ("barrage".equals(action)){
            isBarrage = mActivity.switchBarrage();
        }else if ("send".equals(action)){
            if(!isBarrage){
                sendMessage();
            }else{
                mHelper.sendDan(getYunxinRoomId(), mActivity.getInput());
            }
        }else if("contribution".equals(action)) {
            goContribution();
        }
    }

    private void goContribution(){

    }

    protected String getYunxinRoomId(){
        return record().getYunxinRoomId();
    }


    /***--------聊天室 begin-------***/
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

    private Observer<List<ChatRoomMessage>> incomingMsgObserver = new Observer<List<ChatRoomMessage>>() {//GC_FOR_ALLOC
        @Override
        public void onEvent(List<ChatRoomMessage> chatRoomMessages) {
            handleReiveMessages(chatRoomMessages);
        }
    };

    protected void initChatRoom(){
        enterChatRoom();
        registerObserver(true);
    }

    protected void registerObserver(boolean register){
        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineObserver, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingMsgObserver, register);
    }


    public void sendMessage(){
        String content = mActivity.getInput();
        if (TextUtils.isEmpty(content)){
            return;
        }
        String yunxinRoomId = getYunxinRoomId();
        content = ChatRoomMsgRecord.createRoomTextMsg(content);
        final ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(yunxinRoomId, content);
        NIMClient.getService(ChatRoomService.class)
                .sendMessage(message, false)
                .setCallback(new RequestCallback<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        mActivity.clearInput();
                        List<ChatRoomMsgRecord> messages = new ArrayList<ChatRoomMsgRecord>();
                        messages.add(ChatRoomMsgRecord.genInstance(message.getContent()));
                        mActivity.addMessage(messages, true);
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }

    //处理新接收的消息
    public void handleReiveMessages(List<ChatRoomMessage> chatRoomMessages){
        List<ChatRoomMsgRecord> messagesInListView = new ArrayList<>();
        for (ChatRoomMessage msg : chatRoomMessages){
            if (MsgTypeEnum.text == msg.getMsgType()){
                ChatRoomMsgRecord record = ChatRoomMsgRecord.genInstance(msg.getContent());
                messagesInListView.add(record);
            }else if (MsgTypeEnum.notification == msg.getMsgType()){
                ChatRoomNotificationAttachment attachment = (ChatRoomNotificationAttachment) msg.getAttachment();
                if (NotificationType.ChatRoomMemberIn == attachment.getType()){
                    //用户进入通知
                    if (!msg.getFromAccount().equals(UserModel.getInstance().getUid())){
                        mActivity.getUiomanager().put(msg.getFromAccount() + "进来了");
                        getChatRoomMember(0, 60);
                    }
                }else if (NotificationType.ChatRoomMemberExit == attachment.getType()){
                    //用户退出通知
                    getChatRoomMember(0, 60);
                }
            }
        }
        if (messagesInListView.size() > 0){
            mActivity.addMessage(messagesInListView,false);
        }

    }

    //登录聊天室
    protected void enterChatRoom(){
        String yunxinRoomId = getYunxinRoomId();
        if (TextUtils.isEmpty(yunxinRoomId)){
            LogUtil.e(TAG, "error : yunxinRoomId is empty");
            return;
        }
        EnterChatRoomData data = new EnterChatRoomData(yunxinRoomId);
        Map<String,Object> exMap = new HashMap<>();
        exMap.put("level","1");
        data.setExtension(exMap);
        NIMClient.getService(ChatRoomService.class).enterChatRoom(data).setCallback(new RequestCallback<EnterChatRoomResultData>() {

            @Override
            public void onSuccess(EnterChatRoomResultData o) {
                getChatRoomMember(0, 60);//3439073
            }

            @Override
            public void onFailed(int i) {
                if (200 != 1) {
                    handNIMErr(i);
                }
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    /**
     * 获取聊天室成员信息
     * <p/>
     * roomId          聊天室id
     * memberQueryType 成员查询类型。见{@link MemberQueryType}
     * time            查询固定成员列表用ChatRoomMember.getUpdateTime,
     * 查询游客列表用ChatRoomMember.getEnterTime，
     * 填0会使用当前服务器最新时间开始查询，即第一页，单位毫秒
     * limit           条数限制
     * InvocationFuture 可以设置回调函数。回调中返回成员信息列表
     */
    protected void getChatRoomMember(long time, int limit) {

        NIMClient.getService(ChatRoomService.class)
                .fetchRoomMembers(getYunxinRoomId(), MemberQueryType.GUEST, time, limit)
                .setCallback(
                        new RequestCallbackWrapper<List<ChatRoomMember>>() {
                            @Override
                            public void onResult(int code, List<ChatRoomMember> result, Throwable exception) {
                                if (200 != code) {
                                    handNIMErr(code);
                                }
                                mActivity.showAudience(result);
                            }
                        }
                );
    }

    protected void getChatRoomInfo(String roomId){
        NIMClient.getService(ChatRoomService.class)
                .fetchRoomInfo(roomId).setCallback(new RequestCallback<ChatRoomInfo>() {
            @Override
            public void onSuccess(ChatRoomInfo chatRoomInfo) {
            }

            @Override
            public void onFailed(int i) {
                if (200 != 1) {
                    handNIMErr(i);
                }
            }

            @Override
            public void onException(Throwable throwable) {
            }
        });
    }

    private void handNIMErr(int code){
        switch (code){
            case 408:
                ToastUtil.toastTip(mActivity, "网络请求失败");
                break;
            case 1000:
                ToastUtil.toastTip(mActivity, "房间错误");
                break;
        }
    }

    protected void exitRoom(String roomId){
        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
    }
    /***--------聊天室 end-------***/

    @Override
    public void onDestroy() {
        registerObserver(false);
        exitRoom(getYunxinRoomId());
        destoryHelper();
        super.onDestroy();
    }
}
