package com.xcyo.live.nim;

import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xcyo.baselib.model.BaseModel;
import com.xcyo.baselib.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caixiaoxiao on 30/6/16.
 */
public class NIMUserInfoCache extends BaseModel{
    private static String TAG = NIMUserInfoCache.class.getSimpleName();
    private static class Singleton{
        final static NIMUserInfoCache instance = new NIMUserInfoCache();
    }
    private NIMUserInfoCache(){}
    public static NIMUserInfoCache getInstance(){
        return Singleton.instance;
    }

    private Map<String,NimUserInfo> account2UserMap = new HashMap<>();

    public void buildCache(){
        List<NimUserInfo> users = NIMClient.getService(UserService.class).getAllUserInfo();

    }

    //监听用户资料变更或者添加
    private Observer<List<NimUserInfo>> userInfoUpdateObserver = new Observer<List<NimUserInfo>>() {
        @Override
        public void onEvent(List<NimUserInfo> nimUserInfos) {
            if (nimUserInfos == null || nimUserInfos.isEmpty()){
                return;
            }
            addOrUpdateUsers(nimUserInfos);
        }
    };
    public void registerObservers(boolean register){
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(userInfoUpdateObserver, register);
    }

    /***
     * 服务器获取用户资料
     * @param account
     * @param callback
     */
    public void getUserInfoFromRemote(String account, final RequestCallback<NimUserInfo> callback){
        List<String> accounts = new ArrayList<>();
        accounts.add(account);
        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {
            @Override
            public void onResult(int i, List<NimUserInfo> nimUserInfos, Throwable throwable) {
                NimUserInfo user = null;
                if (i == ResponseCode.RES_SUCCESS && nimUserInfos != null && nimUserInfos.size() > 0) {
                    user = nimUserInfos.get(0);
                }
                if (callback != null) {
                    if (i == ResponseCode.RES_SUCCESS) {
                        callback.onSuccess(user);
                    } else {
                        callback.onFailed(i);
                    }
                }
            }
        });
    }

    /***
     * 批量获取用户资料
     * @param accounts
     * @param callback
     */
    public void getUserInfoFromRemote(List<String> accounts,final RequestCallback<List<NimUserInfo>> callback){
        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(callback);
    }

    /***
     * 更新用户缓存数据
     * @param users
     */
    private void addOrUpdateUsers(List<NimUserInfo> users){
        if (users == null || users.isEmpty()){
            return;
        }
        for (NimUserInfo u : users){
            account2UserMap.put(u.getAccount(),u);
        }
    }

    //获取用户信息
    public NimUserInfo getUserInfo(String account){
        if (TextUtils.isEmpty(account) || account2UserMap == null){
            LogUtil.e(TAG,"getUserInfo null,account=" + account + ",account2UserMap=" + account2UserMap);
            return null;
        }
        return account2UserMap.get(account);
    }

    //获取用户名字
    public String getUserName(String account){
        NimUserInfo userInfo = getUserInfo(account);
        if (userInfo == null){
            return account;
        }
        return TextUtils.isEmpty(userInfo.getName()) ? account : userInfo.getName();
    }
}
