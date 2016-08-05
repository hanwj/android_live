package com.xcyo.live.activity.room_player;

import android.widget.ImageView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.R;
import com.xcyo.live.activity.room.RoomActivity;


/**
 * Created by TDJ on 2016/6/2.
 */
public class VideoActivity extends RoomActivity<VideoPresent> {

    private String roomId;
    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void initArg() {
        roomId = getIntent().getStringExtra("roomId");
    }

    @Override
    protected boolean isSinger() {
        return false;
    }

    @Override
    protected int getControllView() {
        return R.layout.controller_player;
    }

    @Override
    protected void initControllView() {
        chat = (ImageView) findViewById(R.id.player_chat);
        recent = (ImageView) findViewById(R.id.player_recent);
        share = (ImageView) findViewById(R.id.player_share);
        gift = (ImageView) findViewById(R.id.player_gift);
        exit = (ImageView) findViewById(R.id.player_exit);
    }

    private ImageView chat;
    private ImageView recent;
    private ImageView share;
    private ImageView gift;
    private ImageView exit;


    @Override
    protected void initEvent() {
        super.initEvent();
        addOnClickListener(chat, "chat");
        addOnClickListener(recent, "recent");
        addOnClickListener(share, "share");
        addOnClickListener(gift, "gift");
        addOnClickListener(exit, "exit");
    }

    protected String getRoomId(){
        return roomId;
    }


    protected void startVideo(String url){
        setVideoPathWithStart(url);
    }
}
