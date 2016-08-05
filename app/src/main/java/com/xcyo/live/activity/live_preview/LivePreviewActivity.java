package com.xcyo.live.activity.live_preview;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.utils.Handler;
import com.xcyo.live.R;
import com.xcyo.live.activity.add_topic.AddTopicActivity;
import com.xcyo.live.activity.live_notice_friends.LiveNoticeFriendsActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by caixiaoxiao on 3/6/16.
 */
public class LivePreviewActivity extends BaseActivity<LivePreviewActPresenter>{
    private ImageView mCloseImg;
    private EditText mEditText;
    private TextView mTopicText;
    private TextView mLocationText;
    private TextView mLockedText;
    private Button mStartLive;
    private View mThirdSharedView;
    private ImageView mWechatShareImg,mFriendShareImg,mQQShareImg,mQQZoneShareImg;
    private ImageView[] mShareImgs;
    private String[] mShareNames = {"微信","朋友圈","QQ","QQ空间"};
    private int curThirdShared = -1;
    private PopupWindow mShareTipPop = null;
    private Handler mPopHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                if (mShareTipPop != null && mShareTipPop.isShowing()){
                    mShareTipPop.dismiss();
                    mShareTipPop = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_live_preview);
        mCloseImg = (ImageView)findViewById(R.id.live_preview_close);
        mEditText = (EditText)findViewById(R.id.live_preview_title);
        mTopicText = (TextView)findViewById(R.id.live_preview_topic);
        mLocationText = (TextView)findViewById(R.id.live_preview_location);
        mLockedText = (TextView)findViewById(R.id.live_preview_locked);
        mStartLive = (Button)findViewById(R.id.live_preview_start_live);
        mThirdSharedView = findViewById(R.id.live_preview_third_shard_layout);
        mWechatShareImg = (ImageView)findViewById(R.id.live_preview_wechat_share);
        mFriendShareImg = (ImageView)findViewById(R.id.live_preview_friend_share);
        mQQShareImg = (ImageView)findViewById(R.id.live_preview_qq_share);
        mQQZoneShareImg = (ImageView)findViewById(R.id.live_preview_qqzone_share);
        mShareImgs = new ImageView[]{mWechatShareImg,mFriendShareImg,mQQShareImg,mQQZoneShareImg};
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                setThirdShared(0);
            }
        });
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mCloseImg,"back");
        addOnClickListener(mTopicText,"topic");
        addOnClickListener(mLocationText,"location");
        addOnClickListener(mStartLive, "startLive");
        addOnClickListener(mLockedText, "privateLive");
        for (int i = 0; i < mShareImgs.length;i++){
            addOnClickListener(mShareImgs[i],"share" + i);
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            private int editStart;
            private int editBeforeCount;
            private int editCount;
            private String oldStr;
            private boolean ischanged = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editStart = start;
                editBeforeCount = count;
                editCount = after - count;
                oldStr = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ischanged = !s.toString().equals(oldStr);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editCount == 1) {
                    String addStr = s.subSequence(editStart + editBeforeCount, editStart + editBeforeCount + 1).toString();
                    if ("#".equals(addStr)) {
                        startAddTopicActivity();
                        int selection = editStart + editBeforeCount;
                        mEditText.setText(s.delete(editStart + editBeforeCount, editStart + editBeforeCount + 1));
                        mEditText.setSelection(selection);
                        return;
                    }
                }
                if (ischanged) {
                    int selection = mEditText.getSelectionStart();
                    mEditText.setText(createTopicStyle(s.toString()));
                    mEditText.setSelection(selection);
                }
            }
        });
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101){
            if (data != null){
                int editStart = mEditText.getSelectionStart();
                int editEnd = mEditText.getSelectionEnd();
                String topic = data.getStringExtra("topic");
                mEditText.getText().replace(editStart,editEnd,topic);
            }
        }else if (requestCode == 102){
            List<String> uids = data.getStringArrayListExtra("uid");
            if (uids.size() == 0){
                setPrivate(false);
            }else {
                mLockedText.setText(uids.size() + "");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Spannable createTopicStyle(String str){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Pattern p = Pattern.compile("#.*?#");
        Matcher matcher = p.matcher(str);
        int start = 0;
        while(matcher.find()){
            String skipStr = str.substring(start,matcher.start());
            Spannable skipSpan = new SpannableString(skipStr);
            skipSpan.setSpan(new ForegroundColorSpan(Color.WHITE),0,skipStr.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.append(skipSpan);

            String matcherStr = matcher.group(0);
            Spannable span = new SpannableString(matcherStr);
            span.setSpan(new ForegroundColorSpan(Color.RED), 0, matcherStr.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.append(span);
            start = matcher.end();
        }
        if (start < str.length()){
            String lastStr = str.substring(start);
            Spannable span = new SpannableString(lastStr);
            span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, lastStr.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.append(span);
        }
        return builder;
    }

    protected void setThirdShared(int pos){
        for (int i = 0;i < mShareImgs.length;i++){
            mShareImgs[i].setSelected(false);
        }
        if (pos < mShareImgs.length && pos != curThirdShared){
            mShareImgs[pos].setSelected(true);
            curThirdShared = pos;
            showShareTip(pos);
        }else {
            curThirdShared = -1;
        }
    }

    protected void showShareTip(int pos){
        mPopHandler.removeMessages(0);
        if (mShareTipPop != null){
            mShareTipPop.dismiss();
            mShareTipPop = null;
        }
        View contentView = getLayoutInflater().inflate(R.layout.layout_share_tip,null);
        TextView contentText = (TextView)contentView.findViewById(R.id.share_content);
        contentText.setText(mShareNames[pos] + "分享已开");
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView shareImg = mShareImgs[pos];
        int shareWidth = shareImg.getWidth();
        int shareHeight = shareImg.getHeight();
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int contentWidth = contentView.getMeasuredWidth();
        int contentHeight = contentView.getMeasuredHeight();
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAsDropDown(shareImg, (shareWidth - contentWidth) / 2, -(shareHeight + contentHeight));
        mShareTipPop = popupWindow;
        mPopHandler.sendEmptyMessageDelayed(0,3000);

    }

    int getCurThirdShared(){
        for(int i = 0; i < mShareImgs.length; i++){
            if(mShareImgs[i].isSelected()){
                return i;
            }
        }
        return -1;
    }

    String getLiveTitle(){
        return mEditText.getText().toString().trim();
    }

    String getSeat(){
        return mLocationText.getText().toString().trim();
    }

    protected void setLocationText(String text){
        mLocationText.setText(text);
    }
    protected void setPrivate(boolean isPrivate){
        mLockedText.setText("");
        if (isPrivate){
            mThirdSharedView.setVisibility(View.GONE);
            Drawable drawable = getResources().getDrawable(R.mipmap.live_locked_icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mLockedText.setCompoundDrawables(drawable, null, null, null);
            Intent intent = new Intent(this, LiveNoticeFriendsActivity.class);
            startActivityForResult(intent, 102);
            overridePendingTransition(R.anim.anim_right_to_left_enter,R.anim.anim_empty);
        }else {
            mThirdSharedView.setVisibility(View.VISIBLE);
            Drawable drawable = getResources().getDrawable(R.mipmap.live_unlocked_icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mLockedText.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void startAddTopicActivity(){
        Intent intent = new Intent(this, AddTopicActivity.class);
        startActivityForResult(intent,101);
        overridePendingTransition(R.anim.anim_bottom_to_top_enter,R.anim.anim_empty);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter().releaseLoc();
    }
}
