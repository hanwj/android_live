package com.xcyo.live.fragment.me;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.xcyo.baselib.presenter.BaseFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.activity.message_list.MessageListActivity;
import com.xcyo.live.activity.search.SearchActivity;
import com.xcyo.live.activity.setting.SettingActivity;
import com.xcyo.live.activity.user_edit.EditActivity;
import com.xcyo.live.activity.user_edit_icon.EditIconActivity;
import com.xcyo.live.activity.user_level.LevelActivity;
import com.xcyo.live.activity.user_live.ProLiveActivity;
import com.xcyo.live.activity.user_profit.ProfitActivity;
import com.xcyo.live.activity.user_wealth.WealthActivity;
import com.xcyo.live.model.UserModel;
import com.xcyo.live.record.UserRecord;
import com.xcyo.live.utils.ServerEvents;


/**
 * Created by lovingyou on 2016/6/2.
 */
public class MeFragPresenter extends BaseFragmentPresenter<MeFragment,MeFragRecord> {

    @Override
    public void loadDatas() {
        super.loadDatas();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserInfo();
    }

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {
        if(ServerEvents.CALL_SERVER_METHOD_USER_START.equals(evt)){
            UserRecord record = (UserRecord)data.record;
            if(record != null){
                UserModel.getInstance().setRecord(record);
                fragment().showUserInfo(record.getUser());
            }
        }
    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String)data;
        if("contribution".equals(action)){

        }else if("identification".equals(action)){

        }else if("setting".equals(action)){
            goSetting();
        }else if("usr_icon".equals(action)){
            goIconPage();
        }else if("level".equals(action)){
            goUserLevel();
        }else if("account".equals(action)){
            goUserExchange();
        }else if("profit".equals(action)){
            goUserProfit();
        }else if("live".equals(action)){
            goUserLive();
        }else if("usr_edit".equals(action)){
            goEidtUsr();
        }else if ("search".equals(action)){
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            getActivity().startActivity(intent);
        }else if("message".equals(action)){
            Intent intent = new Intent(getActivity(), MessageListActivity.class);
            getActivity().startActivity(intent);
        }
    }

    private void goIconPage(){
        fragment().startActivityForResult(new Intent(getActivity(), EditIconActivity.class), 123);
    }

    private void goEidtUsr(){
        getActivity().startActivity(new Intent(getActivity(), EditActivity.class));
    }

    private void goUserLevel(){
        getActivity().startActivity(new Intent(getActivity(), LevelActivity.class));
    }

    private void goUserExchange(){
//        getActivity().startActivity(new Intent(getActivity(), ExchangeActivity.class));
        getActivity().startActivity(new Intent(getActivity(), WealthActivity.class));
    }

    private void goUserProfit(){
        getActivity().startActivity(new Intent(getActivity(), ProfitActivity.class));
    }

    private void goUserLive(){
        getActivity().startActivity(new Intent(getActivity(), ProLiveActivity.class));
    }

    protected void refreshUserInfo(){
        String uid = UserModel.getInstance().getUid();
        if (TextUtils.isEmpty(uid)){
            return;
        }
        callServer(ServerEvents.CALL_SERVER_METHOD_USER_START, new PostParamHandler("uid", UserModel.getInstance().getUid()));
    }

    private void goSetting(){
        getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
    }
}
