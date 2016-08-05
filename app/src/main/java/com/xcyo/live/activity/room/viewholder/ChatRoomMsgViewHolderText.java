package com.xcyo.live.activity.room.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.live.R;

/**
 * Created by caixiaoxiao on 8/7/16.
 */
public class ChatRoomMsgViewHolderText extends ChatRoomMsgViewHolderBase {
    public View rightLayout;
    public ImageView headIcon;
    public TextView name;
    public TextView lvl;
    public TextView content;
    @Override
    protected void inflate() {
        rightLayout = findViewById(R.id.item_right_layout);
        headIcon = (ImageView) findViewById(R.id.item_head);
        name = (TextView) findViewById(R.id.item_name);
        lvl = (TextView) findViewById(R.id.item_lvl);
        content = (TextView) findViewById(R.id.item_content);
    }

    @Override
    protected int getResId() {
        return R.layout.item_room_chat_msg;
    }

    @Override
    public void bindData(ChatRoomMsgRecord record) {
        name.setText(record.from.alias);
        lvl.setText(record.from.level);
        content.setText(record.content);
        rightLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        ViewGroup.LayoutParams layoutParams = headIcon.getLayoutParams();
        layoutParams.height = rightLayout.getMeasuredHeight();
        headIcon.setLayoutParams(layoutParams);
    }

    @Override
    public int getType() {
        return ChatRoomMsgRecord.TEXT;
    }
}
