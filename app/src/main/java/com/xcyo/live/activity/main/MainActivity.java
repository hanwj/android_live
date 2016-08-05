package com.xcyo.live.activity.main;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.baselib.utils.ToastUtil;
import com.xcyo.live.R;
import com.xcyo.live.activity.appstart.AppStartActivity;
import com.xcyo.live.activity.live_preview.LivePreviewActivity;
import com.xcyo.live.fragment.home.HomeFragment;
import com.xcyo.live.fragment.me.MeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 31/5/16.
 */
public class MainActivity extends BaseActivity<MainActPresent> {
    private long lastTimeOnClickBack = -1;
    public static final int HOME_FRAG = 0,ME_FRAG = 1;
    private int curFrag = -1;
    private ViewGroup mViewContainer;
    private ImageView mHomeImgView,mLiveImgView,mMeImgView;
    private List<BaseFragment> mFrags = new ArrayList<>();
    private List<View> mBottomSelectViews = new ArrayList<>();
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_main);
        mViewContainer = (ViewGroup)findViewById(R.id.main_container);
        mHomeImgView = (ImageView)findViewById(R.id.main_home);
        mLiveImgView = (ImageView)findViewById(R.id.main_live);
        mMeImgView = (ImageView)findViewById(R.id.main_me);

        mFrags.add(new HomeFragment());
        mFrags.add(new MeFragment());
        for (BaseFragment frag:mFrags){
            addFragment(frag,R.id.main_container,false);
        }
        mBottomSelectViews.add(mHomeImgView);
        mBottomSelectViews.add(mMeImgView);
        showFragment(HOME_FRAG);
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mHomeImgView, "home");
        addOnClickListener(mMeImgView, "me");
        addOnClickListener(mLiveImgView, "live");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }


    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastTimeOnClickBack > 2000){
            lastTimeOnClickBack = curTime;
            ToastUtil.toastTip(this,"再按一次退出程序");
        } else {
            Intent intent = new Intent(this, AppStartActivity.class);
            intent.putExtra("close", 1);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        
    }


    public void showFragment(int id){
        if (curFrag == id){
            return;
        }

        for (int i=0;i<mFrags.size();i++){
            BaseFragment frag = mFrags.get(i);
            frag.setHidden(i != id);
            mBottomSelectViews.get(i).setSelected(i == id);
        }
        curFrag = id;
    }

    public void showLivePreviewActivity(){
        Intent intent = new Intent(this, LivePreviewActivity.class);
//        Intent intent = new Intent(this, LiveEndActivity.class);
        startActivity(intent);
    }
}
