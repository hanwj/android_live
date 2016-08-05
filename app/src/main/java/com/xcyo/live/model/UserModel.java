package com.xcyo.live.model;

import android.support.annotation.NonNull;

import com.xcyo.baselib.model.BaseModel;
import com.xcyo.live.record.LoginServerRecord;
import com.xcyo.live.record.UserRecord;

/**
 * Created by caixiaoxiao on 15/6/16.
 */
public class UserModel extends BaseModel{
    private UserModel(){

    }
    private static class Singleton{
        private static final UserModel instance = new UserModel();
    }
    public static UserModel getInstance(){
        return Singleton.instance;
    }

    private String uid;
    private String pid;
    private String yunxinToken;
    private boolean isNew;

    private UserRecord mRecord;

    public void updateAccountInfo(LoginServerRecord record){
        uid = record.uid;
        pid = record.pid;
        yunxinToken = record.yunxinToken;
    }

    public UserRecord getRecord() {
        return mRecord == null? new UserRecord() : mRecord;
    }

    public void setRecord(@NonNull UserRecord record) {
        this.mRecord = record;
    }

    public UserRecord.User getUser(){
        return mRecord == null ? null : mRecord.getUser();
    }

    public UserRecord.UserInfo getRecordInfo() {
        return mRecord == null ? null : mRecord.getUserInfo();
    }

    public String getUid(){
        return uid;
    }

    public String getPid(){
        return pid;
    }

    public String getYunxinToken(){
        return yunxinToken;
    }

    public void clearUserInfo(){
        uid = null;
        pid = null;
        yunxinToken = null;
        mRecord = null;
    }
}
