package com.xcyo.live.fragment.me;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.live.R;
import com.xcyo.live.model.UserModel;
import com.xcyo.live.record.UserRecord;
import com.xutils.x;

/**
 * Created by lovingyou on 2016/6/2.
 */
public class MeFragment extends BaseFragment<MeFragPresenter> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.frag_main_me,null);
        search = (ImageView)v.findViewById(R.id.main_home_search);
        tip = (ImageView) v.findViewById(R.id.main_me_tip);
        tipMsg = (TextView) v.findViewById(R.id.main_me_msg);
        message = (ImageView) v.findViewById(R.id.main_home_message);

        icon = (com.xcyo.live.view.RoundImageView) v.findViewById(R.id.frag_me_icon);
        number = (TextView) v.findViewById(R.id.frag_me_no);
        attention = (TextView) v.findViewById(R.id.frag_me_attention);
        fans = (TextView) v.findViewById(R.id.frag_me_fans);
        signature = (TextView) v.findViewById(R.id.frag_me_signature);
        tag = (RelativeLayout) v.findViewById(R.id.frag_me_tag);

        name = (TextView) v.findViewById(R.id.frag_me_name);
        sex = (ImageView)v.findViewById(R.id.frag_me_sex);
        level_tip = (TextView)v.findViewById(R.id.frag_me_level_tip);
        usr_edit = v.findViewById(R.id.usr_edit);

        live = (TextView) v.findViewById(R.id.frag_me_live);
        level = (TextView) v.findViewById(R.id.frag_me_level);
        profit = (TextView) v.findViewById(R.id.frag_me_profit);
        account = (TextView) v.findViewById(R.id.frag_me_account);

        mMsgTipText = (TextView) v.findViewById(R.id.main_home_red_tip);
        contribution = (LinearLayout) v.findViewById(R.id.frag_me_contribution);
        identification = (RelativeLayout) v.findViewById(R.id.frag_me_identification);
        setting = (RelativeLayout) v.findViewById(R.id.frag_me_setting);
        contribution_info = (LinearLayout) v.findViewById(R.id.frag_me_contribution_info);
        return v;
    }

    private ImageView search;
    private ImageView tip; private TextView tipMsg;
    private ImageView message;

    private com.xcyo.live.view.RoundImageView icon;
    private TextView number;
    private TextView attention;
    private TextView fans;
    private TextView signature;
    private RelativeLayout tag;

    private TextView name;
    private ImageView sex;
    private TextView level_tip;
    private View usr_edit;

    private TextView live;
    private TextView level;
    private TextView profit;
    private TextView account;
    private TextView mMsgTipText;
    private LinearLayout contribution, contribution_info;
    private RelativeLayout identification, setting;

    @Override
    protected void initEvents() {
        addOnClickListener(contribution, "contribution");
        addOnClickListener(identification, "identification");
        addOnClickListener(setting, "setting");
        addOnClickListener(icon, "usr_icon");
        addOnClickListener(usr_edit, "usr_edit");
        addOnClickListener(search, "search");
        addOnClickListener(message, "message");

        addOnClickListener((ViewGroup)(level.getParent()), "level");
        addOnClickListener((ViewGroup)(account.getParent()), "account");
        addOnClickListener((ViewGroup)(profit.getParent()), "profit");
        addOnClickListener((ViewGroup)(live.getParent()), "live");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    protected void showUserInfo(UserRecord.User record){
        x.image().bind(icon, "http://file.xcyo.com/"+record.avatar);
        name.setText(handEmpty(record.alias));
        number.setText("悠悠号:" + handEmpty(record.uid));
        attention.setText("关注:"+0);
        fans.setText("粉丝:"+0);
        signature.setText(TextUtils.isEmpty(record.signature) ? "这家伙很懒,没有留下任何东西~" : record.signature);

        live.setText(handEmpty(record.totalPay)+"个");
        level.setText(handEmpty(record.level)+"级");
        profit.setText(handEmpty(record.totalAward)+"悠币");
        account.setText(handEmpty(record.bean)+"");
    }

    private String handEmpty(String content){
        if(TextUtils.isEmpty(content)){
            return "";
        }
        return content;
    }
}
