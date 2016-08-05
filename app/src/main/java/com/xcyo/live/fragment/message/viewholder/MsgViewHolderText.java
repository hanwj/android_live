package com.xcyo.live.fragment.message.viewholder;

import android.graphics.Color;
import android.widget.TextView;

import com.xcyo.baselib.utils.Util;
import com.xcyo.live.R;

/**
 * Created by caixiaoxiao on 5/7/16.
 */
public class MsgViewHolderText extends MsgViewHolderBase{
    private TextView mContentText;
    @Override
    protected int getContentViewResId() {
        return R.layout.item_message_text;
    }

    @Override
    protected void inflateContentView() {
        mContentText = (TextView)findViewById(R.id.item_content);
    }

    @Override
    protected void setContent() {
        adjustLayoutAndStyle();
        mContentText.setText(message.getContent());
    }

    private void adjustLayoutAndStyle(){
        if (isReceivedMessage()){
            mContentText.setPadding(Util.dp(19),Util.dp(14),Util.dp(14),Util.dp(14));
            mContentText.setTextColor(0xffffffff);
        }else {
            mContentText.setPadding(Util.dp(14),Util.dp(14),Util.dp(19),Util.dp(14));
            mContentText.setTextColor(0xff666666);
        }
    }
}
