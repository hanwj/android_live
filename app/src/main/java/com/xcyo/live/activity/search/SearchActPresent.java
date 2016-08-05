package com.xcyo.live.activity.search;

import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by lovingyou on 2016/6/2.
 */
public class SearchActPresent extends BaseActivityPresenter<SearchActivity,SearchActRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String) data;
            if("cancel".equals(str)){
                mActivity.finish();
            }else if ("clear".equals(str)){
                mActivity.clearText();
            }
        }
    }
}
