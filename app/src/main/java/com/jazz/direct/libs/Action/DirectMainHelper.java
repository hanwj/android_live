package com.jazz.direct.libs.Action;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;

import com.jazz.direct.libs.DirectUtils;
import com.jazz.direct.libs.operation.VideoTermination;
import com.netease.LSMediaCapture.lsMediaCapture;
import com.netease.livestreamingFilter.filter.Filters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class DirectMainHelper {

    private static final String TAG = "[DirectMainHelper]";

    private DirectMainFragment fragment;
    private ViewGroup container;
    private VideoTermination termination;

    //获取日志文件路径
    protected String getLogPath(){
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return Environment.getExternalStorageDirectory().getPath()+ "/log/";
            }
        } catch (Exception e) {
        }
        return null;
    }

    public DirectMainHelper(DirectMainFragment fragment, ViewGroup container, VideoTermination termination){
        this.fragment = fragment;
        this.container = container;
        this.termination = termination;
    }

    public void setVideoTermination(VideoTermination termination){
        this.termination = termination;
    }

    public enum HAVE{
        HAVE_AUDIO, HAVE_VIDEO, HAVE_AV;//音频，视频，音视频
    }

    public enum RESOLUTION{
        SD, HD, FLT; //标清，高清，流畅
    }

    public static final class LS_AUDIO_CODE{
        public static final int LS_AUDIO_CODEC_AAC = 0;
        public static final int LS_AUDIO_CODEC_SPEEX = 1;
        public static final int LS_AUDIO_CODEC_MP3 = 2;
        public static final int LS_AUDIO_CODEC_G711A = 3;
        public static final int LS_AUDIO_CODEC_G711U = 4;
    }

    public static final class LS_AUDIO_CODEC{
        public static final int LS_VIDEO_CODEC_AVC = 0;
        public static final int LS_VIDEO_CODEC_VP9 = 1;
        public static final int LS_VIDEO_CODEC_H265 = 2;
    }

    public static final class LS_AUDIO_FORMAT{
        public static final int FLV = 0;
        public static final int RTMP = 1;
    }

    public static final class CAMERA_ORIENTATION{
        public static final int CAMERA_ORIENTATION_PORTRAIT = 0;
        public static final int CAMERA_ORIENTATION_LANDSCAPE = 1;
        public static final int CAMERA_ORIENTATION_PORTRAIT_UPSIDEDOWN = 2;
        public static final int CAMERA_ORIENTATION_LANDSCAPE_LEFTSIDERIGHT = 3;
    }

    public static final class LS_LOG{
        public static final int LS_LOG_QUIET       = 0x00;            //!< log输出模式：不输出
        public static final int LS_LOG_ERROR       = 1 << 0;          //!< log输出模式：输出错误
        public static final int LS_LOG_WARNING     = 1 << 1;          //!< log输出模式：输出警告
        public static final int LS_LOG_INFO        = 1 << 2;          //!< log输出模式：输出信息
        public static final int LS_LOG_DEBUG       = 1 << 3;          //!< log输出模式：输出调试信息
        public static final int LS_LOG_DETAIL      = 1 << 4;          //!< log输出模式：输出详细
        public static final int LS_LOG_RESV        = 1 << 5;          //!< log输出模式：保留
        public static final int LS_LOG_LEVEL_COUNT = 6;
        public static final int LS_LOG_DEFAULT     = LS_LOG_QUIET;	//!< log输出模式：默认输出警告
    }

    /**相关事物常量Code**/
    public static final int ACTION_START = 0xe86;
    public static final int ACTION_POUSE = 0xe87;
    public static final int ACTION_STOP = 0xe88;

    public static final int ACTION_MUTE = 0xd43;//静音
    public static final int ACTION_VOICE = 0xd44;//开启声音

    public static final int ACTION_FILTER = 0xfc3;//滤镜

    public static final int ACTION_CAMERA_SWITCH = 0xb24;//摄像转换
    public static final int ACTION_FRAME_SWITCH = 0xaaa;//帧率转换

    ///////以下部分代码摘自（网易云信Demo）
    //查询Android摄像头支持的采样分辨率
    private Thread mCameraThread;
    private Looper mCameraLooper;
    private Camera mCamera;

    public final void openCamera(final int mCameraID) {
        final Semaphore lock = new Semaphore(0);
        final RuntimeException[] exception = new RuntimeException[1];
        mCameraThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mCameraLooper = Looper.myLooper();
                try {
                    mCamera = Camera.open(mCameraID);
                } catch (RuntimeException e) {
                    exception[0] = e;
                } finally {
                    lock.release();
                    Looper.loop();
                }
            }
        });
        mCameraThread.start();
        lock.acquireUninterruptibly();
    }

    public final void lockCamera() {
        try {
            mCamera.reconnect();
        } catch (Exception e) {
        }
    }

    public final void releaseCamera() {
        if (mCamera != null) {
            lockCamera();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public final List<Camera.Size> getCameraSupportSize(final int mCameraID) {
        openCamera(mCameraID);
        if(mCamera != null) {
            Camera.Parameters param = mCamera.getParameters();
            List<Camera.Size> previewSizes = param.getSupportedPreviewSizes();
            releaseCamera();
            return previewSizes;
        }
        return new ArrayList<>();
    }

    /**
     * 转换本地摄像头代码至系统摄像头代码
     * @param mainCode
     * @return
     */
    protected int switchCameraCode(int mainCode){
        if(mainCode == 0){
            this.fragment.getActivity().finish();
        }else if(mainCode == 1){
            return Camera.CameraInfo.CAMERA_FACING_FRONT;
        }else if(mainCode == 2){
            return Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        return Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    //音视频参数设置
    public lsMediaCapture.LSLiveStreamingParaCtx paraSet(final lsMediaCapture mLSMediaCapture, final Camera.Size mSize){
        //创建参数实例
        lsMediaCapture.LSLiveStreamingParaCtx mLSLiveStreamingParaCtx = mLSMediaCapture.new LSLiveStreamingParaCtx();
        mLSLiveStreamingParaCtx.eHaraWareEncType = mLSLiveStreamingParaCtx.new HardWareEncEnable();
        mLSLiveStreamingParaCtx.eOutFormatType = mLSLiveStreamingParaCtx.new OutputFormatType();
        mLSLiveStreamingParaCtx.eOutStreamType = mLSLiveStreamingParaCtx.new OutputStreamType();
        mLSLiveStreamingParaCtx.sLSAudioParaCtx = mLSLiveStreamingParaCtx.new LSAudioParaCtx();
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.codec = mLSLiveStreamingParaCtx.sLSAudioParaCtx.new LSAudioCodecType();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx = mLSLiveStreamingParaCtx.new LSVideoParaCtx();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new LSVideoCodecType();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new CameraPosition();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.interfaceOrientation = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new CameraOrientation();

//        //滤镜模式下不开视频水印
//        if(!mLSLiveStreamingParaCtx.eHaraWareEncType.hardWareEncEnable && mWaterMarkOn) {
//            waterMark();
//        }

        //设置摄像头信息，并开始本地视频预览
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition.cameraPosition = fragment.getDirectCamera();//默认后置摄像头，用户可以根据需要调整

        //输出格式：视频、音频和音视频
        mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType = fragment.getHaveStyle();

        //输出封装格式
        mLSLiveStreamingParaCtx.eOutFormatType.outputFormatType = LS_AUDIO_FORMAT.RTMP;//0:flv 1:rtmp

        //摄像头参数配置
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.interfaceOrientation.interfaceOrientation = CAMERA_ORIENTATION.CAMERA_ORIENTATION_PORTRAIT;//0:竖屏 1:横屏 2:竖屏倒立 3:横屏反向

        //音频编码参数配置
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.samplerate = 44100;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.bitrate = 64000;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.frameSize = 2048;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.channelConfig = AudioFormat.CHANNEL_IN_MONO;
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.codec.audioCODECType = LS_AUDIO_CODE.LS_AUDIO_CODEC_AAC;
        //硬件编码参数设置
        mLSLiveStreamingParaCtx.eHaraWareEncType.hardWareEncEnable = DirectUtils.CODE_HD.equals(fragment.getDirectCode()) ? true : false;

        //如下是编码分辨率等信息的设置
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_AUDIO_CODEC.LS_VIDEO_CODEC_AVC;//
        CameraBitrate bt = new CameraBitrate().calCameraBitrate(mSize, fragment.getResolution(), fragment.getHardWareEnc());;
//
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = bt.fps;
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = bt.bitrate;
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = bt.width;
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = bt.height;

        Log.d(TAG,bt.toString());
        return mLSLiveStreamingParaCtx;
    }


    public static final class CameraBitrate{
        //查询摄像头支持的采集分辨率信息相关变量
        //-------------1、普通模式----------------
        //采集分辨率     编码分辨率     建议码率
        //1280x720     1280x720     1500kbps
        //1280x720     960x540      800kbps
        //960x720      960x720      1000kbps
        //960x720      960x540      800kbps
        //960x540      960x540      800kbps
        //640x480      640x480      600kbps
        //640x480      640x360      500kbps
        //320x240      320x240      250kbps
        //320x240      320x180      200kbps
        //-------------2、滤镜模式----------------
        //采集分辨率     编码分辨率     建议码率
        //1280x720     1280x720     1650kbps
        //960x720      960x720      1100kbps
        //960x540      960x540      880kbps
        //640x480      640x480      660kbps
        //320x240      320x240      275kbps
        int[] hd = new int[]{1280, 960, 640, 320};

        private int width = 640;
        private int height = 480;
        private int bitrate = 600000;
        private int fps = 15;

        @Override
        public String toString() {
            return "CameraBitrate:{width="+width+" | height="+height+" | bitrate="+bitrate+" | fps="+fps+"}";
        }

        public CameraBitrate calCameraBitrate(@NonNull Camera.Size z, RESOLUTION resolution, boolean isFilter){
            int width = getFitHeight(z.width);
            if(isFilter){
                switch (width){
                    case 1280:
                        this.fps = 20;
                        this.width = 1280;
                        this.height = 720;
                        this.bitrate = 1650000;
                        break;
                    case 960:
                        if(RESOLUTION.HD == resolution){
                            this.fps = 20;
                            this.width = 960;
                            this.height = 720;
                            this.bitrate = 1100000;
                        }else if(RESOLUTION.SD == resolution){
                            this.fps = 20;
                            this.width = 960;
                            this.height = 540;
                            this.bitrate = 880000;
                        }
                        break;
                    case 640:
                        this.fps = 15;
                        this.width = 640;
                        this.height = 480;
                        this.bitrate = 660000;
                        break;
                    case 320:
                        this.fps = 15;
                        this.width = 320;
                        this.height = 240;
                        this.bitrate = 275000;
                        break;
                }
            }else{
                switch (width){
                    case 1280:
                        if(RESOLUTION.HD == resolution){
                            this.fps = 20;
                            this.width = 1280;
                            this.height = 720;
                            this.bitrate = 1500000;
                        }else{
                            this.fps = 20;
                            this.width = 960;
                            this.height = 540;
                            this.bitrate = 800000;
                        }
                        break;
                    case 960:
                        if(RESOLUTION.HD == resolution){
                            this.fps = 20;
                            this.width = 960;
                            this.height = 720;
                            this.bitrate = 1000000;
                        }else if(RESOLUTION.SD == resolution){
                            this.fps = 20;
                            this.width = 960;
                            this.height = 540;
                            this.bitrate = 800000;
                        }else{
                            this.fps = 20;
                            this.width = 960;
                            this.height = 540;
                            this.bitrate = 800000;
                        }
                        break;
                    case 640:
                        if(RESOLUTION.HD == resolution){
                            this.fps = 20;
                            this.width = 640;
                            this.height = 480;
                            this.bitrate = 600000;
                        }else{
                            this.fps = 15;
                            this.width = 640;
                            this.height = 360;
                            this.bitrate = 500000;
                        }
                        break;
                    case 320:
                        if(RESOLUTION.HD == resolution){
                            this.fps = 15;
                            this.width = 320;
                            this.height = 240;
                            this.bitrate = 250000;
                        }else{
                            this.fps = 15;
                            this.width = 320;
                            this.height = 180;
                            this.bitrate = 150000;
                        }
                        break;
                }
            }
            return this;
        }

        private int getFitHeight(int mainHeight){
            int qh = 640;
            for(int h : hd){
                qh = h;
                if(h < mainHeight){
                    break;
                }
            }
            return qh;
        }
    }

///////////滤镜
    private enum FilterType {//普通 黑白 夜景 模糊 美白 黄昏
        NORMAL, BLACK_WHITE, NIGHT_MODE, BLUR, FACE_WHITEN, SEPIA
    }

    private static class FilterList {
        public List<String> names = new LinkedList<String>();
        public List<FilterType> filters = new LinkedList<FilterType>();

        public void addFilter(final String name, final FilterType filter) {
            names.add(name);
            filters.add(filter);
        }
    }

    private static int createFilterForType(final Context context, final FilterType type) {
        int filter = Filters.FILTER_NONE;
        switch (type) {
            case NORMAL:
                filter = Filters.FILTER_NONE;
                break;
            case BLACK_WHITE:
                filter = Filters.FILTER_BLACK_WHITE;
                break;
            case NIGHT_MODE:
                filter = Filters.FILTER_NIGHT;
                break;
            case BLUR:
                filter = Filters.FILTER_BLUR;
                break;
            case FACE_WHITEN:
                filter = Filters.FILTER_WHITEN;
                break;
            case SEPIA:
                filter = Filters.FILTER_SEPIA;
                break;
            default:
                throw new IllegalStateException("No filter of that type!");
        }

        return filter;

    }

    public static void showDialog(final Context context, final OnFilterChosenListener listener) {
        final FilterList filters = new FilterList();

        filters.addFilter("普通", FilterType.NORMAL);
        filters.addFilter("黑白", FilterType.BLACK_WHITE);
        filters.addFilter("夜景", FilterType.NIGHT_MODE);
        filters.addFilter("模糊", FilterType.BLUR);
        filters.addFilter("美白", FilterType.FACE_WHITEN);
        filters.addFilter("黄昏", FilterType.SEPIA);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(filters.names.toArray(new String[filters.names.size()]),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int item) {
                        listener.onFilterChosenListener(createFilterForType(context, filters.filters.get(item)));
                    }
                });

        builder.create().show();
    }

    public interface OnFilterChosenListener {
        void onFilterChosenListener(int filter);
    }


    public void showFilterDialog(Context context, final lsMediaCapture mLSMediaCapture){
        showDialog(context, new OnFilterChosenListener() {
            @Override
            public void onFilterChosenListener(int filter) {
                mLSMediaCapture.setFilterType(filter);
            }
        });
    }


    //开始直播
    protected void startAV(){
        lsMediaCapture mLSMediaCapture = fragment.getLSMediaCapture();
        if(fragment.isStreamingInitFinished() && mLSMediaCapture != null) {
            //8、开始直播
            mLSMediaCapture.startLiveStreaming();
            fragment.setStreamingStatus(true);
            if(termination != null){this.termination.onTermination(ACTION_START);}
            return ;
        }else{
            if(termination != null){this.termination.onTerminationFailed(ACTION_START, "this mLSMediaCapture is null");}
        }
    }

    protected void pouseAV(){
        lsMediaCapture mLSMediaCapture = fragment.getLSMediaCapture();
        if(fragment.getStreamingStatus() && mLSMediaCapture != null) {
            //8、开始直播
            mLSMediaCapture.resumeVideoEncode();
            if(termination != null){this.termination.onTermination(ACTION_POUSE);}
        }
    }

    protected void resumeAV(){
        lsMediaCapture mLSMediaCapture = fragment.getLSMediaCapture();
        if(fragment.getStreamingStatus() && mLSMediaCapture != null) {
            //8、开始直播
            mLSMediaCapture.stopVideoEncode();
            if(termination != null){this.termination.onTermination(ACTION_START);}
        }
    }

    //切换摄像头
    protected void switchCamera() {
        lsMediaCapture mLSMediaCapture = fragment.getLSMediaCapture();
        if(mLSMediaCapture != null) {
            mLSMediaCapture.switchCamera();
            if(termination != null){this.termination.onTermination(ACTION_CAMERA_SWITCH);}
        }
    }

    /**
     * 开启闪光灯
     * @param isOpen true kaiqi false guanbi
     */
    protected void openFight(boolean isOpen){
        lsMediaCapture mLSMediaCapture = fragment.getLSMediaCapture();
        if(mLSMediaCapture != null) {
            if(isOpen){
                mLSMediaCapture.setCameraFlashPara(true);
            }else{
                mLSMediaCapture.setCameraFlashPara(false);
            }
        }
    }

    //是否静音 true:静音 false推送正常音频
    protected void switchVudio(boolean isEncode){
        lsMediaCapture mLSMediaCapture = fragment.getLSMediaCapture();
        if(mLSMediaCapture != null){
            if(isEncode){
                mLSMediaCapture.stopAudioEncode();
                if(termination != null){this.termination.onTermination(ACTION_MUTE);}
            }else{
                mLSMediaCapture.stopAudioRecord();
                if(termination != null){this.termination.onTermination(ACTION_VOICE);}
            }
        }
    }
}