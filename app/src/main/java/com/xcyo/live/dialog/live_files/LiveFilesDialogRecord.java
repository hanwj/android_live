package com.xcyo.live.dialog.live_files;

import com.xcyo.baselib.record.BaseRecord;
import com.xcyo.live.record.UserSimpleRecord;

/**
 * Created by TDJ on 2016/6/14.
 */
public class LiveFilesDialogRecord extends BaseRecord {
    public UserSimpleRecord mRecord;

    public UserSimpleRecord getRecord() {
        return mRecord == null ? new UserSimpleRecord() : mRecord;
    }
}