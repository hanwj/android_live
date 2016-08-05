package com.xcyo.live.activity.user_edit_signature;

import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by TDJ on 2016/6/6.
 */
public class EditSignaturePresent extends BaseActivityPresenter<EditSignatureActivity, SignedRecord>{
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
        }
    }

    private void clearEdit(){
        ((EditSignatureActivity)getActivity()).clearEdit();
    }

    private void finshInput(){
        getActivity().finish();//
    }
}
