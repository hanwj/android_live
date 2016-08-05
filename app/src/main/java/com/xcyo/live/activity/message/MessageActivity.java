package com.xcyo.live.activity.message;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.fragment.message.MessageFragment;


/**
 * Created by caixiaoxiao on 8/6/16.
 */
public class MessageActivity extends BaseActivity<MessageActPresenter>{
    private TextView mTitleText;
    private ImageView mBackImg;
    private ImageView mUserInfoImg;
    private String toUid,toAlias;
    private MessageFragment mMsgFrag;
    @Override
    protected void initArgs() {
        Intent intent = getIntent();
        toUid = intent.getStringExtra("uid");
        toAlias = intent.getStringExtra("alias");
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_message);
        mTitleText = (TextView)findViewById(R.id.message_title);
        mBackImg = (ImageView)findViewById(R.id.message_back);
        mUserInfoImg = (ImageView)findViewById(R.id.message_user);
        mTitleText.setText(toAlias);
        initMessageContent();
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mBackImg,"back");
        addOnClickListener(mUserInfoImg, "userInfo");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    protected void initMessageContent(){
        mMsgFrag = (MessageFragment) getSupportFragmentManager().findFragmentById(R.id.message_content_frag);
        mMsgFrag.setTo(toUid,toAlias);
    }

    protected String getToUid(){
        return toUid;
    }

    protected String getToAlias(){
        return toAlias;
    }
}
