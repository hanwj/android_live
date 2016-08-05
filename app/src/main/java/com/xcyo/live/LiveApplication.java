package com.xcyo.live;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xcyo.baselib.server.ServerBinder;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.baselib.utils.Util;
import com.xcyo.live.server.ServerRegister;
import com.xutils.x;

/**
 * Created by caixiaoxiao on 31/5/16.
 */
public class LiveApplication extends Application {
    public static String TAG = "LiveApplication";
    private int mVersionCode = 1;
    private String mVersionName = "1.0.0";
    private String mChannelName = "inner";
    private String mDeviceId = "xxx";
    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initNim();
        LogUtil.e(TAG,"LiveApplication onCreate");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.e(TAG,"LiveApplication onLowMemory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtil.e(TAG,"LiveApplication onTerminate");
    }

    private void init(){
        //获取版本信息
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pInfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            mVersionCode = pInfo.versionCode;
            mVersionName = pInfo.versionName;
            ApplicationInfo aInfo = pm.getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA);
            mChannelName = aInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mDeviceId = ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

        //初始化 xutils
        x.Ext.init(this);
        ServerBinder.initServer(new ServerRegister());
        //初始化 baselib
        Util.context = this;
        Util.versionCode = mVersionCode;
        Util.versionName = mVersionName;
        Util.channelName = mChannelName;
        Util.deviceId = mDeviceId;
    }

    private void initNim(){
        SDKOptions options = new SDKOptions();
        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        /*StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = WelcomeActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.drawable.ic_stat_notify_msg;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = ${Screen.width} / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.drawable.avatar_def;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };*/
        NIMClient.init(this,loginInfo(),null);
    }

    private LoginInfo loginInfo(){
        return null;
    }
}
