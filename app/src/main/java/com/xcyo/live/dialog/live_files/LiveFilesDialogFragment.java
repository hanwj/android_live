package com.xcyo.live.dialog.live_files;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseDialogFragment;
import com.xcyo.live.R;
import com.xcyo.live.model.UserModel;
import com.xcyo.live.record.UserRecord;
import com.xcyo.live.record.UserSimpleRecord;
import com.xutils.x;

import java.util.Collection;
import java.util.List;

/**
 * Created by TDJ on 2016/6/14.
 */
public class LiveFilesDialogFragment extends BaseDialogFragment<LiveFilesDialogPresent> {
    @Override
    protected void initArgs() {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams ll = getDialog().getWindow().getAttributes();
        ll.alpha = 1.0f;
        ll.dimAmount = 0.1f;
        ll.gravity = Gravity.CENTER;
        ll.width = getResources().getDisplayMetrics().widthPixels * 108 / 125;
        getDialog().getWindow().setAttributes(ll);
        setCancelable(false);
        super.onResume();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View convertView = inflater.inflate(R.layout.dialog_live_files, null);
        controll = (TextView)convertView.findViewById(R.id.files_controll);
        close = (ImageView)convertView.findViewById(R.id.files_close);
        icon_me = (ImageView)convertView.findViewById(R.id.files_icon_me);
        icon_tag = convertView.findViewById(R.id.files_icon_tag);
        icon_fance = (ImageView)convertView.findViewById(R.id.files_icon_fance);
        name = (TextView)convertView.findViewById(R.id.files_name);
        sex = (ImageView)convertView.findViewById(R.id.files_sex);
        level = (TextView)convertView.findViewById(R.id.files_level);
        usr_id = (TextView)convertView.findViewById(R.id.files_usr_id);
        usr_location = (TextView)convertView.findViewById(R.id.files_usr_location);
        tag_layer = (GridLayout)convertView.findViewById(R.id.files_tag_layer);
        signature = (TextView)convertView.findViewById(R.id.files_signature);

        follow = (TextView)convertView.findViewById(R.id.files_follow);
        fance = (TextView)convertView.findViewById(R.id.files_fance);
        gift_send = (TextView)convertView.findViewById(R.id.files_gift_send);
        coin = (TextView)convertView.findViewById(R.id.files_coin);

        m_follow = (TextView)convertView.findViewById(R.id.files_m_follow);
        m_letter = (TextView)convertView.findViewById(R.id.files_m_letter);
        m_reply = (TextView)convertView.findViewById(R.id.files_m_reply);
        m_homepage = (TextView)convertView.findViewById(R.id.files_m_homepage);
        return convertView;
    }

    private TextView controll;
    private ImageView close;

    private ImageView icon_me;
    private ImageView icon_fance;
    private View icon_tag;

    private TextView name;
    private ImageView sex;
    private TextView level;

    private TextView usr_id, usr_location;

    private GridLayout tag_layer;
    private TextView signature;

    private TextView follow, fance, gift_send, coin;

    private TextView m_follow, m_letter, m_reply, m_homepage;

    @Override
    protected void initEvents() {
        addOnClickListener(controll, "controll");
        addOnClickListener(close, "close");

        addOnClickListener(m_follow, "m_follow");
        addOnClickListener(m_letter, "m_letter");
        addOnClickListener(m_reply, "m_reply");
        addOnClickListener(m_homepage, "m_homepage");
    }

    protected void setFollow(boolean isFollow){
        if(isFollow){
            m_follow.setText("已关注");
            m_follow.setTextColor(0xffd3d3d3);
            m_follow.setClickable(false);
        }else{
            m_follow.setText("关注");
            m_follow.setTextColor(0xff8149FF);
            m_follow.setClickable(true);
        }
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    protected void fillUserInfo(@NonNull UserSimpleRecord record){
        x.image().bind(icon_me, "http://file.xcyo.com/" + record.getUser().avatar);
        name.setText(record.getUser().alias);
        if("男".equals(record.getUser().sex)){
            sex.setImageResource(R.mipmap.male_icon);
        }else{
            sex.setImageResource(R.mipmap.female_icon);
        }
        level.setText("123");
        usr_id.setText(record.getUser().uid);
        usr_location.setText(record.getUser().location);
        signature.setText(TextUtils.isEmpty(record.getUser().signature) ? "这家伙很懒, 什么也没有留下~" : record.getUser().signature);

        UserRecord model = UserModel.getInstance().getRecord();

        follow.setText("关注:"+"00");
        fance.setText("粉丝:"+"00");
        gift_send.setText("送出:"+"00");
        coin.setText("悠币:" + "00");//1013387814

        if(model.getUser().uid != null && record.getUser().uid.equals(model.getUser().uid)){
            ((ViewGroup)m_follow.getParent()).setVisibility(View.GONE);
            getView().findViewById(R.id.files_line).setVisibility(View.GONE);
            controll.setVisibility(View.INVISIBLE);
        }else{
            ((ViewGroup)m_follow.getParent()).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.files_line).setVisibility(View.VISIBLE);
            controll.setVisibility(View.VISIBLE);
            Collection<String> follows = model.getFollow();
            if(follows != null && follows.contains(record.getUser().uid)){
                setFollow(true);
            }else{
                setFollow(false);
            }
        }
    }
}
