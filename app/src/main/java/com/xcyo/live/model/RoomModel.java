package com.xcyo.live.model;

/**
 * Created by TDJ on 2016/6/30.
 */
public class RoomModel {
    private RoomModel(){}
    private static final class RoomInstance{
        private static final RoomModel t = new RoomModel();
    }

    public static RoomModel getInstance(){
        return RoomInstance.t;
    }

    private Room room;
    private Live live;
    private Singer singer;


    public void setRoom(Room room) {
        this.room = room;
    }

    public void setLive(Live live) {
        this.live = live;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    public Room getRoom() {
        return room == null ? new Room() : room;
    }

    public Live getLive() {
        return live == null ? new Live() : live;
    }

    public Singer getSinger() {
        return singer == null ? new Singer() : singer;
    }

    public static final class Room{
        public String roomId;
        public String cover;
        public String notice;
        public String welcome;
        public String rtmpId;
        public String createTime;
        public String cdn;
        public String uid;
        public String yunxinRoomId;
    }

    public static final class Live{
        public String liveId;
        public String roomId;
        public String beginTime;
        public String lastTime;
        public String pushUrl;
        public String httpPullUrl;
        public String hlsPullUrl;
        public String rtmpPullUrl;
        public String year;
        public String month;
        public String day;
        public String title;
        public String locate;
        public String yunxinLiveId;
    }

    public static final class Singer{
        public String uid;
        public String niceId;
        public String alias;
        public String avatar;
        public String coin;
        public String bean;
        public String coinExp;
        public String beanExp;
        public String totalAward;
        public String totalPay;
        public String exPassword;
        public String vip;
        public String isHide;
        public String totalReviceGift;
        public String totalSendGift;
        public String updateLock;
        public String createTime;
        public String loginTime;
        public String roomId;
        public String sex;
        public String desc;
    }

    public void destoryResource(){
        this.room = null;
        this.live = null;
        this.singer = null;
    }
}
