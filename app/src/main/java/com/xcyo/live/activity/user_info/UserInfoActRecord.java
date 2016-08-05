package com.xcyo.live.activity.user_info;

import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.live.record.UserSimpleRecord;

/**
 * Created by caixiaoxiao on 12/6/16.
 */
public class UserInfoActRecord extends BaseRecord {
    public UserSimpleRecord mRecord;

    public UserSimpleRecord getRecord() {
        return mRecord == null ? new UserSimpleRecord() : mRecord;
    }
}
