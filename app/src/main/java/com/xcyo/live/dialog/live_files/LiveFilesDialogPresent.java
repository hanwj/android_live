package com.xcyo.live.dialog.live_files;

import android.text.TextUtils;
import android.view.View;

import com.xcyo.baselib.presenter.BaseDialogFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.model.RoomModel;
import com.xcyo.live.model.UserModel;
import com.xcyo.live.record.UserSimpleRecord;
import com.xcyo.live.utils.ServerEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TDJ on 2016/6/14.
 */
public class LiveFilesDialogPresent extends BaseDialogFragmentPresenter<LiveFilesDialogFragment, LiveFilesDialogRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {
        if(ServerEvents.CALL_SERVER_METHOD_OTHER_DETAIL_INFO.equals(evt)){
            mRecord.mRecord = (UserSimpleRecord)data.record;
            mFragment.fillUserInfo(mRecord.getRecord());
        }else if(ServerEvents.CALL_SERVER_METHOD_FILE_FOLLOW.equals(evt)){//{"follow":["1013387811"]}
            String response = data.responseData;
            if(!TextUtils.isEmpty(response)){
                addFollowToUsr(response);
                mFragment.setFollow(true);
            }
        }
    }

    @Override
    public void loadDatas() {
        super.loadDatas();
        uid = mFragment.getArguments().getString("files_uid");
    }

    private String uid = "";

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private void addFollowToUsr(String text){
        try {
            JSONObject json = new JSONObject(text);
            JSONArray jarry = json.getJSONObject("rm").getJSONArray("follow");
            for(int index = 0; index < jarry.length(); index++){
                UserModel.getInstance().getRecord().addFollow(jarry.getString(index));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("controll".equals(action)){
            handControll();
        }else if("close".equals(action)){
            fragment().dismiss();
        }else if("m_follow".equals(action)){
            goFollow();
        }else if("m_letter".equals(action)){
            goLetter();
        }else if("m_reply".equals(action)){
            goRePly();
        }else if("m_homepage".equals(action)){
            goHomePage();
        }
    }

    private void goHomePage(){

    }

    private void goRePly(){

    }

    private void goLetter(){

    }

    private void goFollow(){
        callServer(ServerEvents.CALL_SERVER_METHOD_FILE_FOLLOW, new PostParamHandler("toUid", mRecord.mRecord.getUser().uid));
    }

    private void handControll(){

    }

    private void loadUserInfo(){
        callServer(ServerEvents.CALL_SERVER_METHOD_OTHER_DETAIL_INFO, new PostParamHandler("uid", uid));
    }
}
