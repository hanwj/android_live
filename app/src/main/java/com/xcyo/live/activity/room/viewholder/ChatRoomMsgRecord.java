package com.xcyo.live.activity.room.viewholder;

import com.google.gson.Gson;
import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.live.model.UserModel;

/**
 * Created by caixiaoxiao on 8/7/16.
 */
public class ChatRoomMsgRecord extends BaseRecord{
    public ChatRoomUserRecord from;
    public ChatRoomUserRecord to;
    public int type;
    public String content;

    public String url; //公告里面包含的链接

    public String light; //玩家点亮颜色

    //礼物相关
    public String tag; //礼物tag
    public String time;//礼物连送次数
    public String id;//礼物id

    public static class ChatRoomUserRecord{
        public String alias;
        public String avatar;
        public String level;
        public String gender;
    }

    public final static int TEXT = 0;    //普通文本
    public final static int GIFT = 1;    //礼物消息
    public final static int NOTICE = 2;  //公告
    public final static int ENTER = 3;   //进入房间
    public final static int BARRAGE = 4; //弹幕
    public final static int LIGHT = 5;   //点亮
    public final static int SHARE = 6;   //分享
    public final static int PAUSE = 7;   //暂停
    public final static int FOLLOW = 8;  //关注
    public final static int KICK = 9;    //踢人
    public final static int MANAGE = 10; //设管理
    public final static int FORBID = 11; //禁言
    public final static int BLACK = 12;  //拉黑

    //解析聊天信息
    public static ChatRoomMsgRecord genInstance(String data){
        return new Gson().fromJson(data,ChatRoomMsgRecord.class);
    }

    //创建一条文本消息
    public static String createRoomTextMsg(String content){
        ChatRoomMsgRecord record = createBaseRecord();
        record.type = TEXT;
        record.content = content;
        return new Gson().toJson(record);
    }

    //创建一条弹幕
    public static String createRoomBarrageTextMsg(String content){
        ChatRoomMsgRecord record = createBaseRecord();
        record.type = BARRAGE;
        record.content = content;
        return new Gson().toJson(record);
    }

    public static ChatRoomMsgRecord createBaseRecord(){
        ChatRoomMsgRecord record = new ChatRoomMsgRecord();
        ChatRoomUserRecord from = new ChatRoomUserRecord();
//        from.alias = UserModel.getInstance().getUser().alias;
//        from.avatar = UserModel.getInstance().getUser().avatar;
//        from.level = UserModel.getInstance().getUser().level;
//        from.gender = UserModel.getInstance().getUser().gender;
        from.alias = "我";
        from.avatar = "";
        from.level = "12";
        from.gender = "1";
        record.from = from;
        return record;
    }
}
