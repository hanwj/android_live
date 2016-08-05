package com.xcyo.live.server;

import com.xcyo.baselib.server.BaseServerRegister;
import com.xcyo.baselib.server.ServerBinder;
import com.xcyo.baselib.utils.Constants;
import com.xcyo.live.activity.user_edit.EditRecord;
import com.xcyo.live.activity.user_edit_icon.EditIconRecord;
import com.xcyo.live.record.HomeNewServerRecord;
import com.xcyo.live.record.LoginServerRecord;
import com.xcyo.live.record.RoomRecord;
import com.xcyo.live.record.SearchUserListServerRecord;
import com.xcyo.live.record.SingerListServerRecord;
import com.xcyo.live.record.TopicListServerRecord;
import com.xcyo.live.record.UserRecord;
import com.xcyo.live.record.UserSimpleRecord;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by caixiaoxiao on 31/5/16.
 */
public class ServerRegister extends BaseServerRegister {

    private String hostUrl = "http://qindamoni.xcyo.com";

    @Override
    protected void doRegister(ServerBinder binder) {
        binder.bind(ServerEvents.CALL_SERVER_METHOD_SYSTEM_START,"system/start", Constants.SERVER_POST,null);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_LIVE_HOT,"live/hot",Constants.SERVER_POST, SingerListServerRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_LIVE_FOLLOW,"live/follow",Constants.SERVER_POST, SingerListServerRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_LIVE_NEW,"live/new",Constants.SERVER_POST, HomeNewServerRecord.class);

        binder.bind(ServerEvents.CALL_SERVER_METHOD_TOPIC_LIST,"topic/list",Constants.SERVER_POST, TopicListServerRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_TOPIC_ROOM,"topic/room",Constants.SERVER_POST,SingerListServerRecord.class);

        binder.bind(ServerEvents.CALL_SERVER_METHOD_LOGIN,"passport/login",Constants.SERVER_POST,LoginServerRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_LOGIN_SMS, "passport/sms", Constants.SERVER_POST, null);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_LOGIN_MOBILE, "passport/mobile", Constants.SERVER_POST, LoginServerRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_LOGIN_LUNCHER_OTHER, "passport/login", Constants.SERVER_POST, LoginServerRecord.class);

        binder.bind(ServerEvents.CALL_SERVER_METHOD_USR_UPLOAD_NAME, "user/alias", Constants.SERVER_POST, EditRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_IMAGE_QN_TOKEN, "user/token", Constants.SERVER_POST, null);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_USR_UPLOAD_ICON, "user/avatar", Constants.SERVER_POST, EditIconRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_USER_START, "user/start", Constants.SERVER_POST, UserRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_USER_SEARCH, "user/search", Constants.SERVER_POST, SearchUserListServerRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_OTHER_DETAIL_INFO, "user/other", Constants.SERVER_POST, UserSimpleRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_OTHER_BASE_INFO, "user/info", Constants.SERVER_POST, UserSimpleRecord.class);

        //房间相关
        binder.bind(ServerEvents.CALL_SERVER_METHOD_ROOM_CREATE, "room/create", Constants.SERVER_POST, RoomRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_ROOM_LIVE, "room/live", Constants.SERVER_POST, RoomRecord.class);
        binder.bind(ServerEvents.CALL_SERVER_METHOD_ROOM_SEND_END, "room/send-dan", Constants.SERVER_POST, null);

        //资料卡
        binder.bind(ServerEvents.CALL_SERVER_METHOD_FILE_FOLLOW, "user/follow", Constants.SERVER_POST, null);


    }

    @Override
    protected String getHostUrl() {
        return hostUrl;
    }
}
