package com.jazz.direct.libs.Action;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.jazz.direct.libs.AlertUtils;
import com.jazz.direct.libs.DirectUtils;
import com.jazz.direct.libs.ViewUtils;
import com.jazz.direct.libs.operation.VideoInterface;
import com.jazz.direct.libs.operation.VideoTermination;
import com.netease.LSMediaCapture.lsMediaCapture;
import com.netease.LSMediaCapture.lsMessageHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/5/31.
 */
public abstract class DirectMainFragment extends Fragment implements lsMessageHandler, View.OnClickListener, VideoInterface {

    private final String TAG = getClass().getSimpleName();

    private boolean m_liveStreamingOn = false;//直播是否开始
    private boolean m_liveStreamingInit = false;//直播是否开始初始化
    private boolean m_liveStreamingInitFinished = false;//直播是否初始化完成
    private boolean m_tryToStopLivestreaming = false;//离开直播
    private boolean m_startVideoCamera = false;//开启预览

    protected DirectMainActivity activity;
    protected DirectMainHelper mHelper;
    protected FrameLayout mFrame;
    protected final List<Camera.Size> camera_size_list = new ArrayList<>();

    private String mDirectUrl = "";
    private int mDirectCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
    private boolean mDirectLight = false;
    private String mDirectCode = DirectUtils.CODE_RD;

    private lsMediaCapture mLSMediaCapture;
    private View mControllerContainer;

    //SDK统计相关变量
    protected lsMediaCapture.LSLiveStreamingParaCtx mLSLiveStreamingParaCtx = null;
    //SDK日志级别相关变量
    private int mLogLevel = DirectMainHelper.LS_LOG.LS_LOG_DEFAULT;

    private DirectMainHelper.HAVE style = DirectMainHelper.HAVE.HAVE_AV;
    private DirectMainHelper.RESOLUTION resolution = DirectMainHelper.RESOLUTION.HD;

    private boolean hardWareEnc = false;

    public int getDirectCamera() {
        return mDirectCamera;
    }

    public String getDirectCode() {
        return mDirectCode;
    }

    public boolean getHardWareEnc(){
        return hardWareEnc;
    }

    public boolean isStreamingInitFinished(){
        return m_liveStreamingInitFinished;
    }

    public void setStreamingStatus(boolean status){
        this.m_liveStreamingOn = status;
    }

    protected boolean getStreamingStatus(){
        return m_liveStreamingOn;
    }

    public int getHaveStyle(){
        if(style == DirectMainHelper.HAVE.HAVE_AUDIO){
            return 0;
        }else if(style == DirectMainHelper.HAVE.HAVE_VIDEO){
            return 1;
        }
        return 2;
    }

    public DirectMainHelper.RESOLUTION getResolution() {
        return resolution;
    }

    public lsMediaCapture getLSMediaCapture() {
        synchronized (mLSMediaCapture){
            return mLSMediaCapture;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLog = true;
        this.activity = (DirectMainActivity) getActivity();
        mDirectCode = getMainIntent().getStringExtra(DirectUtils.direct_code);
        mDirectUrl = getMainIntent().getStringExtra(DirectUtils.direct_directUrl);
        mDirectLight = getMainIntent().getBooleanExtra(DirectUtils.direct_light, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mFrame = ViewUtils.createDirectMainView(this.activity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setKeepScreenOn(true);
        RelativeLayout layer = ViewUtils.getFrameContainer(this.activity, getDirectSurfaceView());
        mFrame.addView(layer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mFrame.addView(mControllerContainer = ViewUtils.getControllerContainer(this.activity), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        View controllerView = configControllerView();
        if(controllerView != null){
            ((ViewGroup)mControllerContainer).addView(controllerView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            addClickEvent();
        }
    }

    protected abstract View getDirectSurfaceView();
    protected abstract void configPreViewSize(int mVideoPreViewWidth, int mVideoPreViewHeight);
    protected abstract void startVideoPreView(lsMediaCapture mLSMediaCapture, final lsMediaCapture.LSLiveStreamingParaCtx mLSLiveStreamingParaCtx);
    protected abstract View configControllerView();
    protected abstract void addClickEvent();
    @Override public abstract void onClick(View v);

    protected Intent getMainIntent(){
        return this.activity.getIntent();
    }

    private int mVideoPreViewWidth = 480;
    private int mVideoPreViewHeight = 640;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHelper = new DirectMainHelper(this, mFrame, termination);

        int mainCode = getMainIntent().getIntExtra(DirectUtils.direct_camera, 0);
        mDirectCamera = mHelper.switchCameraCode(mainCode);
        camera_size_list.addAll(mHelper.getCameraSupportSize(mDirectCamera));

        Camera.Size mSize = getPreViewSize();
        if(mSize == null){
            AlertUtils.showTips(activity, "没有适合的分辨率");
            return ;
        }

        mVideoPreViewWidth = mSize.width;
        mVideoPreViewHeight = mSize.height;

        mLSMediaCapture = new lsMediaCapture(this, this.activity, mVideoPreViewWidth, mVideoPreViewHeight);
        configPreViewSize(mVideoPreViewWidth,mVideoPreViewHeight);
        if(mLSMediaCapture != null){
            mLSMediaCapture.setTraceLevel(mLogLevel, mHelper.getLogPath());

            startVideoPreView(mLSMediaCapture, mLSLiveStreamingParaCtx = mHelper.paraSet(mLSMediaCapture, mSize));

            if(!TextUtils.isEmpty(mDirectUrl)){
                initLiveSteam();
            }
        }
    }

    protected void initLiveSteam(){
        m_liveStreamingInit = true;
        m_liveStreamingInitFinished = mLSMediaCapture.initLiveStream(mDirectUrl, mLSLiveStreamingParaCtx);
    }

    public Camera.Size getPreViewSize(){
        if(camera_size_list.size() > 0){
            if(camera_size_list.size() >= 2){
                return camera_size_list.get((camera_size_list.size() - 1) / 2);
            }else{
                return camera_size_list.get(0);
            }
        }
        return null;
    }


    private VideoTermination termination = new VideoTermination() {
        @Override
        public void onTermination(int msgCode) {
            switch (msgCode){
                case DirectMainHelper.ACTION_START:
                    break;
                case DirectMainHelper.ACTION_POUSE:
                    break;
                case DirectMainHelper.ACTION_CAMERA_SWITCH:
                    break;
                case DirectMainHelper.ACTION_FILTER:
                    break;
                case DirectMainHelper.ACTION_FRAME_SWITCH:
                    break;
                case DirectMainHelper.ACTION_MUTE:
                    break;
                case DirectMainHelper.ACTION_STOP:
                    break;
                case DirectMainHelper.ACTION_VOICE:
                    break;
            }
        }

        @Override
        public void onTerminationFailed(int msgCode, String msg) {

        }
    };

    //摘自云信Demo
    //处理SDK抛上来的异常和事件，用户需要在这里监听各种消息，进行相应的处理。
    //例如监听断网消息，用户根据断网消息进行直播重连
    //注意：直播重连请使用restartLiveStream，在网络带宽较低导致发送码率帧率降低时，可以调用这个接口重启直播，改善直播质量
    //在网络断掉的时候（用户可以监听 MSG_RTMP_URL_ERROR 和 MSG_BAD_NETWORK_DETECT ），用户不可以立即调用改接口，而是应该在网络重新连接以后，主动调用这个接口。
    //如果在网络没有重新连接便调用这个接口，直播将不会重启
    @Override
    public void handleMessage(int msg, Object o) {
        LOG("" + msg);
        switch (msg) {
            case MSG_INIT_LIVESTREAMING_OUTFILE_ERROR://初始化直播出错
            case MSG_INIT_LIVESTREAMING_VIDEO_ERROR:
            case MSG_INIT_LIVESTREAMING_AUDIO_ERROR:
                break;
            case MSG_START_LIVESTREAMING_ERROR://开始直播出错
                break;
            case MSG_STOP_LIVESTREAMING_ERROR://停止直播出错
                break;
            case MSG_AUDIO_PROCESS_ERROR://音频处理出错
                AlertUtils.showTips(this.activity, "音频处理失败");
                break;
            case MSG_VIDEO_PROCESS_ERROR://视频处理出错
                AlertUtils.showTips(this.activity, "视频处理失败");
                break;
            case MSG_START_PREVIEW_ERROR://视频预览出错，可能是获取不到camera的使用权限
                AlertUtils.showTips(this.activity, "没有找到摄像头设备");
                break;
            case MSG_AUDIO_RECORD_ERROR://音频采集出错，获取不到麦克风的使用权限
                AlertUtils.showTips(this.activity, "没有找到麦克风设备");
                break;
            case MSG_RTMP_URL_ERROR://断网消息
                AlertUtils.showTips(this.activity, "网络状态不佳,请检查网络状态之后,重新开始直播");
                break;
            case MSG_URL_NOT_AUTH://直播URL非法，URL格式不符合视频云要求
                AlertUtils.showTips(this.activity, "直播地址非法,无法开始直播");
                break;
            case MSG_SEND_STATICS_LOG_ERROR://发送统计信息出错
                break;
            case MSG_SEND_HEARTBEAT_LOG_ERROR://发送心跳信息出错
                break;
            case MSG_AUDIO_SAMPLE_RATE_NOT_SUPPORT_ERROR://音频采集参数不支持
                break;
            case MSG_AUDIO_PARAMETER_NOT_SUPPORT_BY_HARDWARE_ERROR://音频参数不支持
                break;
            case MSG_NEW_AUDIORECORD_INSTANCE_ERROR://音频实例初始化出错
                break;
            case MSG_AUDIO_START_RECORDING_ERROR://音频采集出错
                break;
            case MSG_OTHER_AUDIO_PROCESS_ERROR://音频操作的其他错误
                break;
            case MSG_QOS_TO_STOP_LIVESTREAMING://网络QoS极差，码率档次降到最低
                break;
            case MSG_HW_VIDEO_PACKET_ERROR://视频硬件编码出错反馈消息
                break;
            case MSG_WATERMARK_INIT_ERROR://视频水印操作初始化出错
                break;
            case MSG_WATERMARK_PIC_OUT_OF_VIDEO_ERROR://视频水印图像超出原始视频出错
                break;
            case MSG_WATERMARK_PARA_ERROR://视频水印参数设置出错
                break;
            case MSG_CAMERA_PREVIEW_SIZE_NOT_SUPPORT_ERROR://camera采集分辨率不支持
                break;
            case MSG_START_PREVIEW_FINISHED://camera采集预览完成
                m_startVideoCamera = true;
                break;
            case MSG_START_LIVESTREAMING_FINISHED://开始直播完成
                break;
            case MSG_STOP_LIVESTREAMING_FINISHED://停止直播完成
                break;
            case MSG_STOP_VIDEO_CAPTURE_FINISHED:
                if(mLSMediaCapture != null)
                {
                    //继续视频推流，推最后一帧图像
                    mLSMediaCapture.resumeVideoEncode();
                }
                break;
            case MSG_STOP_RESUME_VIDEO_CAPTURE_FINISHED:
                //开启视频preview
                if(mLSMediaCapture != null){
                    mLSMediaCapture.resumeVideoPreview();
                    //开启视频推流，推正常帧
                    mLSMediaCapture.startVideoLiveStream();
                }
                break;
            case MSG_STOP_AUDIO_CAPTURE_FINISHED:
                if(mLSMediaCapture != null){
                    //继续音频推流，推静音帧
                    mLSMediaCapture.resumeAudioEncode();
                }
                break;
            case MSG_STOP_RESUME_AUDIO_CAPTURE_FINISHED:
                //开启音频推流，推正常帧
                mLSMediaCapture.startAudioLiveStream();
                break;
            case MSG_SWITCH_CAMERA_FINISHED://切换摄像头完成
                break;
            case MSG_SEND_STATICS_LOG_FINISHED://发送统计信息完成
                break;
            case MSG_SERVER_COMMAND_STOP_LIVESTREAMING://服务器下发停止直播的消息反馈，暂时不使用
                break;
            case MSG_GET_STATICS_INFO://获取统计信息的反馈消息
                break;
            case MSG_BAD_NETWORK_DETECT://如果连续一段时间（10s）实际推流数据为0，会反馈这个错误消息
                break;
            case MSG_SCREENSHOT_FINISHED://视频截图完成后的消息反馈
                break;
            case MSG_SET_CAMERA_ID_ERROR://设置camera出错（对于只有一个摄像头的设备，如果调用了不存在的摄像头，会反馈这个错误消息）
                AlertUtils.showTips(this.activity, "没有找到摄像头设备,无法切换视角");
                break;
        }
    }

    @Override
    public void onPause(){
        mHelper.pouseAV();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mHelper.resumeAV();
    }

    @Override
    public void onDestroy() {
        destoryVideo();
        super.onDestroy();
    }

    @Override
    public void startVideo() {
        if(mDirectUrl == null){
            mDirectUrl = getMainIntent().getStringExtra(DirectUtils.direct_directUrl);
        }
        if(TextUtils.isEmpty(mDirectUrl)){
            AlertUtils.showTips(getActivity(), "推流地址为空,无法开始直播");
            return ;
        }
        initLiveSteam();
        if(!m_liveStreamingInitFinished){
            AlertUtils.showTips(getActivity(), "推流未初始化完毕,无法开始");
            return ;
        }
        if(!m_liveStreamingOn){
            mHelper.startAV();
        }
    }



    @Override
    public void pouseVideo() {
        mHelper.pouseAV();
    }

    @Override
    public void resumeVideo() {
        mHelper.resumeAV();
    }

    @Override
    public void reStartVideo() {
        destoryVideo();
        startVideo();
    }

    @Override
    public void destoryVideo() {
        if(m_liveStreamingInit) {
            m_liveStreamingInit = false;
        }
        //停止直播调用相关API接口
        if(mLSMediaCapture != null && m_liveStreamingOn) {

            //停止直播，释放资源
            mLSMediaCapture.stopLiveStreaming();

            //如果没有关闭视频预览
            if(m_startVideoCamera){
                mLSMediaCapture.stopVideoPreview();
                mLSMediaCapture.destroyVideoPreview();
            }
            mLSMediaCapture = null;
        } else if(mLSMediaCapture != null && m_startVideoCamera){
            mLSMediaCapture.stopVideoPreview();
            mLSMediaCapture.destroyVideoPreview();
            mLSMediaCapture = null;
        }
        if(m_liveStreamingOn) {
            m_liveStreamingOn = false;
        }
    }

    private boolean isLog = false;
    protected void LOG(String msg){
        if(isLog){
            android.util.Log.d(TAG, ""+msg);
        }
    }
}
