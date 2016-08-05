package com.xcyo.live.activity.login_sms;

import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.live.record.LoginServerRecord;

/**
 * Created by TDJ on 2016/6/7.
 */
public class LoginSMSRecord extends BaseRecord {
    //{"s":"ok","d":{"uid":1013387803,"pid":"mobile_15600279173","yunxinToken":"MDAxKjI4Ny8vMw","isNew":false}}
    public LoginServerRecord serverRecord;

    public LoginServerRecord getServerRecord() {
        return serverRecord == null ? new LoginServerRecord() : serverRecord;
    }
}
