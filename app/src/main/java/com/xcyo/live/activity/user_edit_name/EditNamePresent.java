package com.xcyo.live.activity.user_edit_name;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.activity.user_edit.EditPresenter;

/**
 * Created by TDJ on 2016/6/6.
 */
public class EditNamePresent extends BaseActivityPresenter<EditNameActivity, EditNameRecord>{
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("del".equals(action)){
            clearEdit();
        }else if("finish".equals(action)){
            finshInput();
        }else if("finish_result".equals(action)){
            Intent result = new Intent();
            result.putExtra("r_content", mActivity.getEditContent());
            mActivity.setResult(EditPresenter.R_NAME_CODE, result);
            mActivity.finish();
        }
    }

    private void clearEdit(){
        ((EditNameActivity)getActivity()).clearEdit();
    }

    private void finshInput(){
        getActivity().finish();//
    }
}
