package com.jazz.direct.libs;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jazz.direct.libs.Action.DirectMainActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by TDJ on 2016/5/31.
 */
public class DirectUtils {



    /*一些常量**/

    public static final String direct_isLiveStream = "direct/livestream";//动作

    public static final String direct_directUrl = "direct/directUrl";//路径

    public static final String direct_camera = "direct/camera";//摄像头
    public static final String direct_light = "direct/light";//闪光灯权限
    public static final String direct_code = "direct/code";//解码格式
    public static final String direct_controller = "direct/controller";//自定义控制台


    ///////////摄像头相关 0没有摄像头, 1有前置摄像头, 2有后置摄像头
    public static final int CAMERA_NONE = 0;
    public static final int CAMERA_FRONT = 1;
    public static final int CAMERA_REAR = 2;
    public static final int CAMERA_FRONT_REAR = 3;

    ///////////////解码器相关  HD硬解码 , RD软解码
    public static final String CODE_HD = "hard_decoding";
    public static final String CODE_RD = "software_decoding";

    public static final class Action{
        public static final String ACTION_START = "com.direct.controller.start";
        public static final String ACTION_STOP = "com.direct.controller.stop";
        public static final String ACTION_RESTART = "com.direct.controller.restart";
        public static final String ACTION_RELEASE = "com.direct.controller.release";
        public static final String ACTION_MUTE = "com.direct.controller.mute";
        public static final String ACTION_SWITCH_CAMERA = "com.direct.controller.switch.camera";
        public static final String ACTION_SWITCH_BITRATE = "com.direct.controller.switch.bitrate";
        public static final String ACTION_FILTER = "com.direct.controller.filter";
        public static final String ACTION_OPENFIGHT = "com.direct.controller.openfight";
    }

    private static final List<Device> rd_list = new ArrayList<>();//需要上传哪些设备是支持硬解码的
    static{//欲载入部分设备
        rd_list.add(new Device("HM2A", "19"));
        rd_list.add(new Device("XM5", "23"));
        rd_list.add(new Device("XM4S", "23"));
    }


    public static final void openDirectActivity(android.content.Context context, Class<? extends DirectMainActivity> cls, String directUrl){
        openDirectActivity(context, cls, directUrl, null, -1);
    }

    public static final void openDirectActivity(android.content.Context context, Class<? extends DirectMainActivity> cls, String directUrl, int mCustomControllerResourceId){
        openDirectActivity(context, cls, directUrl, null, mCustomControllerResourceId);
    }

    public static final void openDirectActivity(android.content.Context context, Class<? extends DirectMainActivity> cls, String directUrl, Map<String, String> otherParms){
        openDirectActivity(context, cls, directUrl, otherParms, -1);
    }

    public static final void openDirectActivity(android.content.Context context, Class<? extends DirectMainActivity> cls, String directUrl, Map<String, String> otherParms, int mCustomControllerResourceId){
        if(!checkCameraPermission(context)){
            AlertUtils.showTips(context, "没有获取到摄像头的权限,前往设置");
            return ;
        }
        final int camera_init = checkCameraFacing(context);
        if(camera_init == 0){
            AlertUtils.showTips(context, "没有检测到摄像设备,无法开始直播");
            return ;
        }

        final Intent init_Intent = getIntent(context, cls);
        init_Intent.putExtra(direct_directUrl, directUrl);
        init_Intent.putExtra(direct_isLiveStream, true);
        init_Intent.putExtra(direct_camera, camera_init);
        init_Intent.putExtra(direct_code, getDeviceDeCode());
        init_Intent.putExtra(direct_light,checkFlashLightPermission(context));
        init_Intent.putExtra(direct_controller, mCustomControllerResourceId);
        if(otherParms != null){
            Iterator<Map.Entry<String, String>> it = otherParms.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, String> entry = it.next();
                init_Intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        context.startActivity(init_Intent);
    }

    public static final void openVideoStream(android.content.Context context, Class<? extends DirectMainActivity> cls, String directUrl, Map<String, String> otherParms){

        final Intent init_Intent = getIntent(context, cls);
        init_Intent.putExtra(direct_isLiveStream, false);
        init_Intent.putExtra(direct_directUrl, directUrl);
        if(otherParms != null){
            Iterator<Map.Entry<String, String>> it = otherParms.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, String> entry = it.next();
                init_Intent.putExtra(entry.getKey(), entry.getValue());
            }
        }

        context.startActivity(init_Intent);
    }

    private static final Intent getIntent(Context context, Class<? extends DirectMainActivity> cls){
        return new Intent(context, cls);
    }

    /**
     * 获取摄像头位置
     * @param context
     * @return
     */
    private static final int checkCameraFacing(Context context){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
            AlertUtils.showTips(context, "直播手机的版本不能低于android 4.2系统");
            return 0;
        }else{
            final int cameraCount = android.hardware.Camera.getNumberOfCameras();
            int camera_init = 0;
            for(int i = 0; i < cameraCount; i++){
                android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
                android.hardware.Camera.getCameraInfo(i, info);
                if(0 == info.facing){ //Back_facing
                    camera_init += 2;
                }else if(1 == info.facing){//font_facing
                    camera_init += 1;
                }
            }
            return camera_init;
        }
    }

    /**
     * 判断是否拥有摄像头权限
     * @param context
     * @return
     */
    private static final boolean checkCameraPermission(Context context){
        PackageManager pm = context.getPackageManager();
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CAMERA", context.getPackageName());
    }

    /**
     * 判断是否拥有闪光灯的权限
     * @param context
     * @return
     */
    private static final boolean checkFlashLightPermission(Context context){
        return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission("android.permission.FLASHLIGHT", context.getPackageName());
    }

    /**
     * 打开设置
     * @param context
     */
    private static final void startSetting(Context context){
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    /**
     * 上传devices列表
     * @param devices
     */
    public static final void upLoadDeviceRD(List<Device> devices){
        if(devices == null)
            return ;
        rd_list.addAll(devices);
    }

    /**
     * 判断设备是否支持硬解码
     * @return
     */
    private static final String getDeviceDeCode(){
        String model = Build.MODEL;
        int api = Build.VERSION.SDK_INT;
        for(int i = 0; i < rd_list.size(); i++)
        {
            if(model.equalsIgnoreCase(rd_list.get(i).getModel()) && String.valueOf(api).equals(rd_list.get(i).getApi())){
                return CODE_RD;
            }

        }
        return CODE_HD;
    }
}
