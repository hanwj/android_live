package com.jazz.direct.libs.Action;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jazz.direct.libs.AlertUtils;
import com.jazz.direct.libs.ConsUtils;
import com.jazz.direct.libs.DirectUtils;
import com.jazz.direct.libs.operation.VideoInterface;
import com.jazz.direct.libs.view.NEMediaController;
import com.jazz.direct.libs.view.NEVideoView;

/**
 * Created by TDJ on 2016/6/29.
 */
public class DirectPlayerFragment extends Fragment implements VideoInterface{

    private Context mContext;

    public static final int NELP_LOG_UNKNOWN = 0; //!< log输出模式：输出详细
    public static final int NELP_LOG_DEFAULT = 1; //!< log输出模式：输出详细
    public static final int NELP_LOG_VERBOSE = 2; //!< log输出模式：输出详细
    public static final int NELP_LOG_DEBUG   = 3; //!< log输出模式：输出调试信息
    public static final int NELP_LOG_INFO    = 4; //!< log输出模式：输出标准信息
    public static final int NELP_LOG_WARN    = 5; //!< log输出模式：输出警告
    public static final int NELP_LOG_ERROR   = 6; //!< log输出模式：输出错误
    public static final int NELP_LOG_FATAL   = 7; //!< log输出模式：一些错误信息，如头文件找不到，非法参数使用
    public static final int NELP_LOG_SILENT  = 8; //!< log输出模式：不输出

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mController = new NEMediaController(mContext);
        mVideoPath = ((Activity)mContext).getIntent().getStringExtra(DirectUtils.direct_directUrl);
        return mVideoView = new NEVideoView(mContext);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setKeepScreenOn(true);
        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        if(view instanceof NEVideoView){
            ((NEVideoView)view).setVideoScalingMode(NEVideoView.VIDEO_SCALING_MODE_FILL);
//            ((NEVideoView)view).setOnCompletionListener();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!ConsUtils.isNet(mContext)){
            AlertUtils.showTips(mContext, "未连接网络,请检查网络");
            return ;
        }
        if(!ConsUtils.isWIFI(mContext)){
            AlertUtils.showTips(mContext, "当前的连接不是WIFI网络");
        }
        initVideo();
    }

    private NEVideoView mVideoView;
    private NEMediaController mController;

    private String mVideoPath;

    @Override
    public void onResume() {
        super.onResume();
        if(mVideoView != null && mVideoView.isPaused()){
            resumeVideo();
        }else{
            startVideo();
        }
    }

    private void initVideo(){
        mVideoView.setMediaController(mController);
        mController.setVisibility(View.GONE);

        mVideoView.setBufferStrategy(0); //直播低延时
        mVideoView.setBufferPrompt(null);//缓冲View
        mVideoView.setMediaType("livestream");//livestream 直播 videoondemand点播
        mVideoView.setHardwareDecoder(false);//是否硬解码
        mVideoView.setPauseInBackground(true);
        mVideoView.setMute(false);
        mVideoView.setLogLevel(NELP_LOG_SILENT); //设置log级别
    }

    @Override
    public void onDestroy() {
        destoryVideo();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        pouseVideo();
        super.onPause();
    }

    @Override
    public void startVideo() {
        if(mVideoPath == null){
            mVideoPath = ((Activity)mContext).getIntent().getStringExtra(DirectUtils.direct_directUrl);
        }
        if(TextUtils.isEmpty(mVideoPath)){
            return ;
        }
        mVideoView.setVideoPath(mVideoPath == null ? "" : mVideoPath);
        mVideoView.requestFocus();
        mVideoView.start();
    }

    @Override
    public void reStartVideo() {
        if(!TextUtils.isEmpty(mVideoPath)){
            this.mVideoView.stopPlayback();
            startVideo();
        }
    }

    @Override
    public void pouseVideo() {
        if(this.mVideoView != null && this.mVideoView.isPlaying()){
            this.mVideoView.pause();
        }
    }

    @Override
    public void resumeVideo() {
        if(mVideoView != null && mVideoView.isPaused()){
            mVideoView.start();
        }
    }

    @Override
    public void destoryVideo() {
        if(this.mVideoView != null){
            this.mVideoView.release_resource();
        }
    }
}
