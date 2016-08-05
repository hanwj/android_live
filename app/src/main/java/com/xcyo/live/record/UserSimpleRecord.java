package com.xcyo.live.record;

import com.xcyo.baselib.record.BaseRecord;

import java.util.List;

/**
 * Created by caixiaoxiao on 1/6/16.
 */
public class UserSimpleRecord extends BaseRecord {
    public User user;
    public UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo == null ? new UserInfo() : userInfo;
    }

    public User getUser() {
        return user == null ? new User() : user;
    }

    public static class User{
        //基本信息
        public String uid;
        public String niceId;
        public String alias;//昵称
        public String avatar;//头像
        public String cover;//封面
        public String coin;//钻石
        public String bean;//豆
        public String sex;//性别
        public String coinExp;//总经验
        public String beanExp;//豆经验
        public String totalAward; //总XX(应该是收益<蒙的>)
        public String totalPay;//总直播
        public String totalReviceGift;//总收到礼物数
        public String totalSendGift;//总送出礼物数

        public String gender;
        public String level;//等级
        public String signature;//签名
        public boolean isFriend;

        public String vip;//认证信息

        //直播信息
        public String pid;//平台ID
        public String mobile;//手机号
        public String email;//邮箱

        public String location;
        public String num;
        public String topic;
        public String roomId;
    }

    public static class UserInfo{
        public String uid;
        public String province;
        public String city;
        public String gender;
        public String birthday;
        public String horos;
        public String weight;
        public String bust;
        public String waist;
        public String isHide;
        public String hips;
    }

    public SingerRecord createSingerRecord(){
        SingerRecord record = new SingerRecord();
        record.uid = getUser().uid;
        record.roomId = getUser().roomId;
        record.alias = getUser().alias;
        record.avatar = getUser().avatar;
        record.cover = getUser().cover;
        record.level = getUser().level;
        return record;
    }
}
