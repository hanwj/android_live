package com.xcyo.live.activity.room_live;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.R;
import com.xcyo.live.activity.room.RoomActivity;


/**
 * Created by TDJ on 2016/6/2.
 */
public class LiveActivity extends RoomActivity<LivePresent> {

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected int getControllView() {
        return R.layout.controller_live;
    }

    @Override
    protected void initControllView() {
        chat = (ImageView) findViewById(R.id.live_chat);
        recent = (ImageView) findViewById(R.id.live_recent);
        music = (ImageView) findViewById(R.id.live_music);
        pop = (ImageView) findViewById(R.id.live_pop);
        exit = (ImageView) findViewById(R.id.live_exit);

        findViewById(R.id.room_singler_follow).setVisibility(View.GONE);
    }
    private ImageView chat;
    private ImageView recent;
    private ImageView music;
    private ImageView pop;
    private ImageView exit;

    private LiveHelper mHelper;
    @Override
    protected boolean isSinger() {
        return true;
    }

    @Override
    protected void initArg() {
        mHelper = new LiveHelper(this);
    }

    LiveHelper getLiveHelper(){
        return mHelper;
    }

    protected Intent getMainIntent(){
        return getIntent();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        addOnClickListener(chat, "chat");
        addOnClickListener(recent, "recent");
        addOnClickListener(music, "music");
        addOnClickListener(pop, "pop");
        addOnClickListener(exit, "exit");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.destoryResource();
    }
}
