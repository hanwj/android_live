package com.xcyo.baselib.utils;

import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.event.EventMapper;
import com.xcyo.baselib.server.ServerBinder;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.DownloadParamHandler;
import com.xcyo.baselib.view.FlashDataParser;
import com.xcyo.baselib.view.FlashView;

/**
 * Created by wanghongyu on 4/1/16.
 *
 *

    LogUtil.i("wanghy", "begin download 40003.zip");
    FlashFileDownloader.getInstance().downloadAnimFile(getContext(), "http://file.xcyo.com/40003.zip", "40003.zip", null, FlashView.Downloader.DownloadType.ZIP, new FlashView.Downloader.DownloadCallback() {
        @Override
        public void onComplete(boolean succ) {
        LogUtil.i("wanghy", "download 40003.zip ret = " + succ);
        }

        @Override
        public void onProgress(float per) {
            LogUtil.i("wanghy", "download 40003.zip percent=" + per);
            }
        });
 */
public class FlashFileDownloader extends FlashDataParser.Downloader {

    private static final String FLASH_DOWNLAODER_KEY = "flash_downloader_key";

    private EventMapper mMapper = new EventMapper() {
        @Override
        protected void onExit() {
            Event.getInstance().unmapEventsWithTarget(this);
        }
    };

    private static FlashFileDownloader sFlashDownloader = null;
    private FlashFileDownloader(){
        ServerBinder.getInstance().bindDownload(FLASH_DOWNLAODER_KEY);
    }
    public static FlashFileDownloader getInstance(){
        if(sFlashDownloader == null){
            sFlashDownloader = new FlashFileDownloader();
        }
        return sFlashDownloader;
    }

    @Override
    public void download(String url, String outFile, final DownloadCallback cb) {
        ServerBinder.getInstance().call(FLASH_DOWNLAODER_KEY, new DownloadParamHandler(url, outFile));
        Event.getInstance().mapEvent(Constants.CALL_SERVER_METHOD_ON_PROGRESS, mMapper, new Event.EventCallback() {
            @Override
            public boolean onEvent(String evt, Object data) {
                ServerBinderData binderData = (ServerBinderData) data;
                if (binderData != null) {
                    if (binderData.event.equals(FLASH_DOWNLAODER_KEY)) {//下载过程中
                        cb.onProgress(binderData.percent);
                    }
                }
                return true;
            }
        });

        Event.getInstance().mapEvent(FLASH_DOWNLAODER_KEY, mMapper, new Event.EventCallback() {
            @Override
            public boolean onEvent(String evt, Object data) {
                ServerBinderData binderData = (ServerBinderData) data;
                if (binderData.event.equals(evt)){//下载成功
                    cb.onComplete(true);
                }
                return false;
            }
        });

        Event.getInstance().mapEvent(Constants.CALL_SERVER_METHOD_ERROR, mMapper, new Event.EventCallback() {
            @Override
            public boolean onEvent(String evt, Object data) {
                ServerBinderData binderData = (ServerBinderData) data;
                if (binderData.event.equals(FLASH_DOWNLAODER_KEY)){
                    if (cb != null){
                        cb.onComplete(false);
                    }
                }
                return false;
            }
        });
    }
}
