package com.xcyo.live.activity.user_edit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.gson.Gson;
import com.jazz.direct.libs.AlertUtils;
import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.activity.user_edit_icon.EditIconActivity;
import com.xcyo.live.activity.user_edit_name.EditNameActivity;
import com.xcyo.live.activity.user_edit_signature.EditSignatureActivity;
import com.xcyo.live.utils.ServerEvents;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by TDJ on 2016/6/3.
 */
public class EditPresenter extends BaseActivityPresenter<EditActivity, EditRecord>{

    public static final int R_NAME_CODE = 0x524e43;
    public static final int R_ICON_CODE = 0x524943;
    private ExecutorService excu = Executors.newSingleThreadExecutor();

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {
        if(ServerEvents.CALL_SERVER_METHOD_USR_UPLOAD_NAME.equals(evt)){
            mActivity.setUserName(data.params.getValue("alias"));
        }
    }

    @Override
    protected void onServerFailedCallback(String evt, ServerBinderData data) {
        super.onServerFailedCallback(evt, data);
        if(ServerEvents.CALL_SERVER_METHOD_USR_UPLOAD_NAME.equals(evt)){
            AlertUtils.showTips(mActivity, "上传昵称失败");
        }
    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String)data;
        if("icon_layer".equals(action)){
            goIconPage();
        }else if("name_layer".equals(action)){
            goEditName();
        }else if("number_layer".equals(action)){

        }else if("sex_layer".equals(action)){

        }else if("signature_layer".equals(action)){
            goEditSignature();
        }else if("identity_layer".equals(action)){

        }else if("identification_layer".equals(action)){

        }else if("age_layer".equals(action)){
            showBirthSelector();
        }else if("emotion_layer".equals(action)){
            ((EditActivity)getActivity()).showEmotionState();
        }else if("hometown_layer".equals(action)){
            parseCn();
        }else if("profession_layer".equals(action)){

        }else if("finish".equals(action)){
            mActivity.finish();
        }
    }

    private void goIconPage(){
        getActivity().startActivityForResult(new Intent(getActivity(), EditIconActivity.class), 123);
    }

    private void goEditSignature(){
        getActivity().startActivity(new Intent(getActivity(), EditSignatureActivity.class));
    }

    private void goEditName(){
        getActivity().startActivityForResult(new Intent(getActivity(), EditNameActivity.class), 123);
    }

    private void showHometownSelector(final EditRecord.CN cn){
        if(cn != null){
            ((EditActivity)getActivity()).showHomeTown(cn);
        }
    }

    private void showBirthSelector(){
        ((EditActivity)getActivity()).showBirthSelector();
    }

    private void parseCn(){
        excu.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStreamReader reader = new InputStreamReader(getActivity().getAssets().open("cn"), Charset.forName("GBK"));
                    if (reader != null) {
                        EditRecord.CN cn = new Gson().fromJson(reader, EditRecord.CN.class);
                        showHometownSelector(cn);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    protected void upLoadUsrName(@NonNull String name){
        callServer(ServerEvents.CALL_SERVER_METHOD_USR_UPLOAD_NAME, new PostParamHandler("alias", name));
    }
}
