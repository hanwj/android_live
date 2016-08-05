package com.xcyo.live.activity.room;

import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.live.model.RoomModel;

/**
 * Created by TDJ on 2016/7/6.
 */
public class RoomRecord extends BaseRecord {
    RoomModel.Room room;
    RoomModel.Live live;
    RoomModel.Singer singer;

    public RoomModel.Room getRoom() {
        return room;
    }

    public RoomModel.Live getLive() {
        return live;
    }

    public RoomModel.Singer getSinger() {
        return singer;
    }


    public void setRoom(RoomModel.Room room){
        this.room = room;
    }

    public void setLive(RoomModel.Live live){
        this.live = live;
    }

    public void setSinger(RoomModel.Singer singer) {
        this.singer = singer;
    }

    public String getYunxinRoomId(){
        return room != null ? room.yunxinRoomId : "";
    }
}
