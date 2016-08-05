package com.xcyo.live.activity.login;

import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.live.record.LoginServerRecord;

/**
 * Created by TDJ on 2016/6/12.
 */
public class LoginRecord extends BaseRecord {

    public LoginServerRecord serverRecord;

    public LoginServerRecord getServerRecord() {
        return serverRecord == null ? new LoginServerRecord() : serverRecord;
    }
}
