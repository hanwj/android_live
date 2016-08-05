package com.xcyo.live.activity.setting_security;

import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/6/18.
 */
public class SecurityActivity extends BaseActivity<SecurityPresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_security);
        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_finish = (TextView) findViewById(R.id.base_title_right);
        actionbar_title.setText("账号安全");

        img = (ImageView) findViewById(R.id.security_img);
        tag = (TextView) findViewById(R.id.security_level);
        tip = (TextView) findViewById(R.id.security_tip);
        phone = (RelativeLayout) findViewById(R.id.security_phone);

        stat = (TextView)findViewById(R.id.security_stat);
    }

    private TextView actionbar_back, actionbar_title, actionbar_finish;

    private ImageView img;
    private TextView tag;
    private TextView tip;
    private RelativeLayout phone;

    private TextView stat;

    @Override
    protected void initEvents() {
        addOnClickListener(phone, "phone");
        addOnClickListener(actionbar_back, "finish");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (false) {
            handlerSecurity(1);
        } else {
            handlerSecurity(-1);
        }
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    private void handlerSecurity(int prority) {
        if (prority >= 0) {
            tip.setVisibility(View.GONE);
            img.setImageResource(R.mipmap.img_excuse_high);
            tag.setText(getSecurityLevel("高"));

            stat.setText("13132045910");
            stat.setTextColor(0xffAC91FE);
        } else {
            tip.setVisibility(View.VISIBLE);
            img.setImageResource(R.mipmap.img_excuse_low);
            tag.setText(getSecurityLevel("低"));

            stat.setText("未绑定");
            stat.setTextColor(0xff999999);
        }
    }


    private CharSequence getSecurityLevel(@NonNull String level) {
        SpannableStringBuilder builder = new SpannableStringBuilder("安全等级:");
        SpannableString span = new SpannableString(level);
        span.setSpan(new ForegroundColorSpan(0xFFFF7895), 0, span.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder.append(span);
    }
}
