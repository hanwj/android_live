package com.xcyo.live.activity.user_info;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.live.R;
import com.xcyo.live.record.UserRecord;
import com.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 12/6/16.
 */
public class UserInfoActivity extends BaseActivity<UserInfoActPresenter> {
    private ImageView mBackImg;
    private TextView mTitleText;
    private ImageView mUserIconImg;
    private TextView mUserLvlText;

    private TextView mUserName, mUserLevelTip; private ImageView mUserSex;

    private TextView mAttentionText;
    private TextView mFansText;
    private TextView mSignatureText;
    private RadioGroup mInfoRG;
    private FrameLayout mInfoContainer;
    private List<BaseFragment> mFrags;
    private View mAttionLayout,mMsgLayout,mForbidLayout;
    private int[] mRBIDs = {R.id.user_info_home,R.id.user_info_live};
    private String mUid,mAlias;
    @Override
    protected void initArgs() {
        mUid = getIntent().getStringExtra("uid");
        mAlias = getIntent().getStringExtra("alias");
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_user_info);
        mBackImg = (ImageView)findViewById(R.id.user_info_back);
        mTitleText = (TextView)findViewById(R.id.user_info_title);
        mUserIconImg = (ImageView)findViewById(R.id.frag_me_icon);
        mUserLvlText = (TextView)findViewById(R.id.frag_me_no);

        mUserName = (TextView)findViewById(R.id.frag_me_name);
        mUserSex = (ImageView)findViewById(R.id.frag_me_sex);
        mUserLevelTip = (TextView)findViewById(R.id.frag_me_level_tip);


        mAttentionText = (TextView)findViewById(R.id.frag_me_attention);
        mFansText = (TextView)findViewById(R.id.frag_me_fans);
        mSignatureText = (TextView)findViewById(R.id.frag_me_signature);
        mInfoRG = (RadioGroup)findViewById(R.id.user_info_rg);
        mAttionLayout = findViewById(R.id.user_info_attention_layout);
        mMsgLayout = findViewById(R.id.user_info_message_layout);
        mForbidLayout = findViewById(R.id.user_info_forbid_layout);
        setUserInfo();
    }

    private void setUserInfo(){
        mFrags = new ArrayList<>();
        mFrags.add((BaseFragment)getSupportFragmentManager().findFragmentById(R.id.user_info_home_frag));
        mFrags.add((BaseFragment)getSupportFragmentManager().findFragmentById(R.id.user_info_live_frag));
        showFragment(0);
    }

    protected void showUserInfo(UserRecord.User record){
        x.image().bind(mUserIconImg, "http://file.xcyo.com/" + record.avatar);
        mUserLvlText.setText("悠悠号:" + handEmpty(record.uid));
        mUserName.setText(handEmpty(record.alias));
        mAttentionText.setText("关注:" + 0);
        mFansText.setText("粉丝:" + 0);
        mSignatureText.setText(TextUtils.isEmpty(record.signature) ? "这家伙很懒,没有留下任何东西~" : record.signature);
    }

    private String handEmpty(String content){
        if(TextUtils.isEmpty(content)){
            return "";
        }
        return content;
    }

    protected String getUid(){
        if(TextUtils.isEmpty(mUid)){
            return "";
        }
        return mUid;
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mBackImg, "back");
        addOnClickListener(mAttentionText, "attention");
        addOnClickListener(mFansText, "fans");
        addOnClickListener(mAttionLayout, "goAttention");
        addOnClickListener(mMsgLayout, "goMsg");
        addOnClickListener(mForbidLayout, "goForbid");
        mInfoRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < mRBIDs.length; i++) {
                    if (mRBIDs[i] == checkedId) {
                        showFragment(i);
                    }
                }
            }
        });

    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    private void showFragment(int position){
        for (int i = 0; i < mFrags.size();i++){
            BaseFragment frag = mFrags.get(i);
            if (i == position){
                frag.setHidden(true);
                frag.getView().setVisibility(View.VISIBLE);
            }else {
                frag.setHidden(false);
                frag.getView().setVisibility(View.GONE);
            }
        }
    }


    protected String getAlias(){
        return mAlias;
    }
}
