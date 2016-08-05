package com.xcyo.live.fragment.message.viewholder;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xcyo.live.R;
import com.xcyo.live.utils.TimeUtils;

/**
 * Created by caixiaoxiao on 5/7/16.
 */
public abstract class MsgViewHolderBase {
    protected IMMessage message;
    private View view;
    private TextView mTimeText;
    private ImageView mLeftIcon,mRightIcon;
    private FrameLayout mContentContainer;

    public View getView(LayoutInflater inflater){
        view = inflater.inflate(R.layout.item_message,null);
        inflate();
        return view;
    }

    protected  void inflate(){
        mTimeText = (TextView)findViewById(R.id.item_time);
        mLeftIcon = (ImageView)findViewById(R.id.item_left_head);
        mRightIcon = (ImageView)findViewById(R.id.item_right_head);
        mContentContainer = (FrameLayout)findViewById(R.id.item_content_container);
        view.inflate(view.getContext(), getContentViewResId(), mContentContainer);
        inflateContentView();
    }

    public void refresh(IMMessage message){
        this.message = message;
        setTime();
        setHeadIcon();
        setContent();
        setContentBg();
    }

    private void setTime(){
        if (isShowTime()){
            mTimeText.setVisibility(View.VISIBLE);
            mTimeText.setText(TimeUtils.getTimeShowString(message.getTime()));
        }else{
            mTimeText.setVisibility(View.GONE);
        }
    }

    private void setHeadIcon(){
        ImageView show = isReceivedMessage() ? mLeftIcon : mRightIcon;
        ImageView hide = isReceivedMessage() ? mRightIcon : mLeftIcon;
        hide.setVisibility(View.GONE);
        show.setVisibility(View.VISIBLE);
        //加载头像
        show.setImageResource(R.mipmap.temp_singer_photo);
    }

    private void setContentBg(){
        if (isReceivedMessage()){
            mContentContainer.setBackgroundResource(R.drawable.left_message_bg);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mContentContainer.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
        }else{
            mContentContainer.setBackgroundResource(R.drawable.right_message_bg);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mContentContainer.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
        }
    }

    protected boolean isShowTime(){
        return true;
    }

    protected boolean isReceivedMessage(){
        return message.getDirect() == MsgDirectionEnum.In;
    }

    public View findViewById(int id){
        return view.findViewById(id);
    }

    protected abstract int getContentViewResId();
    protected abstract void inflateContentView();
    protected abstract void setContent();

    public static MsgViewHolderBase getViewHolderByMessage(IMMessage message){
        if (message.getMsgType() == MsgTypeEnum.text){
            return new MsgViewHolderText();
        }
        return new MsgViewHolderText();
    }
}
