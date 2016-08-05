package com.xcyo.live.dialog.live_message;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseDialogFragment;
import com.xcyo.live.R;
import com.xcyo.live.fragment.message.MessageFragment;

/**
 * Created by caixiaoxiao on 27/6/16.
 */
public class LiveMessageDialogFragment extends BaseDialogFragment<LiveMessageDialogPresenter>{
    private TextView mTitleText;
    private ImageView mBackImg;
    private ImageView mUserInfoImg;
    private String toUid,toAlias;
    private MessageFragment mMsgFrag;
    @Override
    protected void initArgs() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_live_message,null);
        mTitleText = (TextView)v.findViewById(R.id.message_title);
        mBackImg = (ImageView)v.findViewById(R.id.message_back);
        mUserInfoImg = (ImageView)v.findViewById(R.id.message_user);
        mUserInfoImg.setVisibility(View.INVISIBLE);
        mTitleText.setText(toAlias);
        initMessageContent();
        return v;
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mBackImg,"back");
        addOnClickListener(mUserInfoImg, "userInfo");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    public void onResume() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams ll = getDialog().getWindow().getAttributes();
        ll.gravity = Gravity.BOTTOM;
        ll.width = WindowManager.LayoutParams.MATCH_PARENT;
        ll.height = getResources().getDimensionPixelOffset(R.dimen.room_message_height);
        getDialog().getWindow().setAttributes(ll);
        super.onResume();
    }

    public LiveMessageDialogFragment init(String toUid,String toAlias){
        this.toUid = toUid;
        this.toAlias = toAlias;
        return this;
    }

    protected void initMessageContent(){
        mMsgFrag = new MessageFragment();
        addFragment(mMsgFrag,R.id.message_frag_container,false);
        mMsgFrag.setTo(toUid,toAlias);
    }
}