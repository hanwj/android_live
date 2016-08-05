package com.xcyo.live.activity.live_preview;

import android.view.View;

import com.jazz.direct.libs.DirectUtils;
import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.R;
import com.xcyo.live.activity.room_live.LiveActivity;
import com.xcyo.live.helper.LocationHelper;
import com.xcyo.live.utils.QQUtils;
import com.xcyo.live.utils.WeChatUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caixiaoxiao on 3/6/16.
 */
public class LivePreviewActPresenter extends BaseActivityPresenter<LivePreviewActivity,LivePreviewActRecord> {
    private boolean isLocationOn = true;
    private boolean isPrivate = false;
    private LocationHelper mHelper;

    @Override
    public void loadDatas() {
        super.loadDatas();
        this.mHelper = new LocationHelper(mActivity, termination);
    }

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("back".equals(str)){
                mActivity.finish();
            }else if ("topic".equals(str)){
                mActivity.startAddTopicActivity();
            }else if ("location".equals(str)) {
                setLocation();
            }else if ("privateLive".equals(str)){
                setPrivate();
            }else if ("startLive".equals(str)){
                invite();
            }else if (str.startsWith("share")){
                int pos = Integer.parseInt(str.substring(5));
                mActivity.setThirdShared(pos);
            }
        }
    }

    private void setLocation(){
        String str = "定位关";
        isLocationOn = !isLocationOn;
        if (isLocationOn){
            this.mHelper.startLoac();
            str = "定位开";
        }
        mActivity.setLocationText(str);
    }
    protected void setPrivate(){
        isPrivate = !isPrivate;
        mActivity.setPrivate(isPrivate);
    }

    private LocationHelper.LocationTermination termination = new LocationHelper.LocationTermination() {
        @Override
        public void termination(String lat, String lng, String city) {
            if(isLocationOn){
                mActivity.setLocationText(city);
            }
        }

        @Override
        public void locFailed(int code) {
            if(isLocationOn){
                mActivity.setLocationText("定位失败");
            }
        }
    };

    protected void releaseLoc(){
        if(this.mHelper != null){
            this.mHelper.destoryLoac();
        }
    }

    private void invite(){
        int index = mActivity.getCurThirdShared();
        switch (index){
            case 0:
                new WeChatUtils(mActivity).share("wechat", "", "", "", null);
                break;
            case 1:
                new WeChatUtils(mActivity).share("wechatMount", "", "", "", null);
                break;
            case 2:
                new QQUtils(mActivity).share("", "", "", null);
                break;
            case 3:
                new QQUtils(mActivity).shareToQzone("", "", "", null);
                break;
            default:
                openLive();
                return;
        }
    }

    private void openLive(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("title", mActivity.getLiveTitle());
        int index = mActivity.getCurThirdShared();
        switch (index){
            case 0:
                params.put("share", "wechat");
                break;
            case 1:
                params.put("share", "wechatMount");
                break;
            case 2:
                params.put("share", "QQ");
                break;
            case 3:
                params.put("share", "Qzone");
                break;
        }
        if(isLocationOn){
            params.put("locat", mActivity.getSeat());
        }
        DirectUtils.openDirectActivity(getActivity(), LiveActivity.class, null, params, R.layout.view_live_controller);//R.layout.view_live_controller
        mActivity.finish();
    }
}
