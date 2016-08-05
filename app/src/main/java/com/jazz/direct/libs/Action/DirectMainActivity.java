package com.jazz.direct.libs.Action;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.jazz.direct.libs.AlertUtils;
import com.jazz.direct.libs.DirectUtils;
import com.jazz.direct.libs.operation.VideoInterface;
import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/5/31.
 */
public abstract class DirectMainActivity<P extends BaseActivityPresenter> extends BaseActivity<P>{

    private static final String TAG = "[DirectMainActivity]";

    private FrameLayout mStackFrame = null;
    private LayoutInflater mInflater = null;
    private VideoInterface mVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initArg();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setViewBgColor(android.R.color.transparent);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        mStackFrame = createStackView();
        mInflater = LayoutInflater.from(this);
        mStackFrame.setFitsSystemWindows(true);
        super.setContentView(mStackFrame);
//        initVideo();
        initView();
        initEvent();
    }

    protected abstract void initView();
    protected abstract void initArg();
    protected abstract void initEvent();

    @Override
    protected final void initArgs() {}

    @Override
    protected final void initViews() {}

    @Override
    protected final void initEvents() {}

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if(view == null)
            return ;
        initChildView(view, params);
    }

    @Override
    public void setContentView(View view) {
        if(view == null)
            return ;
        initChildView(view, null);
    }

    @Override
    public void setContentView(int layoutResID) {
        if(layoutResID <= 0){
            throw new ExceptionInInitializerError("this view is not legal.");
        }
        View childView = mInflater.inflate(layoutResID, null, true);
        if(childView == null){
            return ;
        }
        initChildView(childView, null);
    }

    private void initChildView(final View childView, final ViewGroup.LayoutParams params){
        childView.setBackgroundColor(configChildBackgroundColor());
        if(params == null){
            this.mStackFrame.addView(childView);
        }else{
            this.mStackFrame.addView(childView, params);
        }
    }


    private final FrameLayout createStackView(){
        FrameLayout frame = new FrameLayout(this);
        frame.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        frame.setBackgroundColor(Color.BLACK);
        return frame;
    }

    private final FrameLayout createDirectView(){
        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.direct_frame_container);
        frame.setLayoutParams(new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels));
        frame.setBackgroundColor(Color.GRAY);
        return frame;
    }

//    private final FrameLayout createMainView(){
//        FrameLayout frame = new FrameLayout(this);
//        frame.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//        frame.setBackgroundColor(Color.TRANSPARENT);
//        return frame;
//    }
    private int configChildBackgroundColor(){
        return Color.TRANSPARENT;
    }

    protected void setVideoPath(String urlPath){
        if(!TextUtils.isEmpty(urlPath)){
            getMainIntent().putExtra(DirectUtils.direct_directUrl, urlPath);
            return ;
        }
        AlertUtils.showTips(this, "地址为空");
    }

    protected void setVideoPathWithStart(String urlPath){
        setVideoPath(urlPath);
        if(mVideo != null){
            mVideo.startVideo();
        }else{
            AlertUtils.showTips(this, "初始化未完成,无法开始播放");
        }
    }

    protected void initVideo(){
        mStackFrame.addView(createDirectView());
        FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        if(!getMainIntent().getBooleanExtra(DirectUtils.direct_isLiveStream, false)){
            manager.add(R.id.direct_frame_container, fragment = new DirectPlayerFragment() , DirectPlayerFragment.class.getName()).commit();
        }else if(DirectUtils.CODE_HD.equals(getMainIntent().getStringExtra(DirectUtils.direct_code))){
            manager.add(R.id.direct_frame_container, fragment = new DirectHDFragment(), DirectHDFragment.class.getName()).commit();
        }else{
            manager.add(R.id.direct_frame_container, fragment = new DirectRDFragment(), DirectRDFragment.class.getName()).commit();
        }
        if(fragment != null){
            mVideo = (VideoInterface)fragment;
        }
    }


    private Intent getMainIntent(){
        return getIntent();
    }

    private void LOG(String msg){
        android.util.Log.d(TAG, ""+msg);
    }
}
