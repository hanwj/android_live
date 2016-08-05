package com.xcyo.live.helper;

import android.app.Activity;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by TDJ on 2016/7/5.
 */
public class LocationHelper implements com.baidu.location.BDLocationListener {

    public LocationHelper(Activity activity, LocationTermination termination){
        this.mActivity = activity;
        mClient = new LocationClient(this.mActivity);
        this.mClient.registerLocationListener(this);
        mClient.setLocOption(getLocOption());
        this.mTermination = termination;
    }

    private LocationClientOption getLocOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(true);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        return option;
    }

    private Activity mActivity;
    private LocationClient mClient;
    private LocationTermination mTermination;

    public void startLoac(){
        if(this.mClient != null && !this.mClient.isStarted()){
            this.mClient.start();
        }
    }

    private void stopLoac(){
        if(this.mClient != null){
            this.mClient.stop();
        }
    }

    public void destoryLoac(){
        if(this.mClient != null){
            this.mClient.stop();
            this.mClient.unRegisterLocationListener(this);
            this.mClient = null;
        }
    }

    public void setTermination(LocationTermination mTermination) {
        this.mTermination = mTermination;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        android.util.Log.e("TAG", bdLocation.getLocType()+"");
        if(bdLocation.getLocType() == 61 || bdLocation.getLocType() == 161){
            String time = bdLocation.getTime();
            String lat = bdLocation.getLatitude()+"";
            String lng = bdLocation.getLongitude()+"";
            String radiud = bdLocation.getRadius()+"";
            String mCity = bdLocation.getCity();
            if(mTermination != null){
                this.mTermination.termination(lat, lng, mCity);
            }else{
                this.mTermination.locFailed(bdLocation.getLocType());
            }
        }
    }

    public interface LocationTermination{
        public void termination(String lat, String lng, String city);
        public void locFailed(int code);
    }
}
