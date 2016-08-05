package com.xcyo.live.activity.login;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.live.R;
import com.xcyo.live.activity.user_agreement.UserAgreeMentActivity;
import com.xcyo.live.utils.LuncherUtils;

/**
 * Created by TDJ on 2016/6/12.
 */
public class LoginActivity extends BaseActivity<LoginPresent> {

    @Override
    protected void initArgs() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LuncherUtils.LUNCHER_PARAMS_TAG);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        LocalBroadcastManager.getInstance(this).registerReceiver(presenter().getmReceiver(), filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(presenter().getmReceiver());
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_login);

        icon = (ImageView)findViewById(R.id.usr_login_icon);
        wechat = (ImageView)findViewById(R.id.usr_login_wechat);
        qq = (ImageView)findViewById(R.id.usr_login_qq);
        phone = (ImageView)findViewById(R.id.usr_login_phone);
        more = (ImageView)findViewById(R.id.usr_login_more);

        argee = (TextView)findViewById(R.id.usr_login_agree);

        SpannableStringBuilder ch = new SpannableStringBuilder();
        Spannable span_0 = new SpannableString("悠悠直播服务和隐私条款");
        span_0.setSpan(new ForegroundColorSpan(Color.WHITE), 0, span_0.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        Spannable span = new SpannableString("悠悠直播服务和隐私条款");
        span.setSpan(getClickable(), 0, span.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        span.setSpan(new UnderlineSpan(), 0, span.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        span.setSpan(new ForegroundColorSpan(0xFFBFA1FC), 0, span.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        argee.setText(ch.append(span_0).append(span));
        argee.setMovementMethod(new LinkMovementMethod());
    }

    private ImageView icon;
    private ImageView wechat;
    private ImageView qq;
    private ImageView phone;
    private ImageView more;

    private TextView argee;

    @Override
    protected void initEvents() {
        addOnClickListener(wechat, "wechat");
        addOnClickListener(qq, "qq");
        addOnClickListener(phone, "phone");
        addOnClickListener(more, "more");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    private ClickableSpan getClickable(){
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                LogUtil.e("TAG", widget.toString());
                startActivity(new Intent(LoginActivity.this, UserAgreeMentActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(presenter().RQ_CODE == requestCode){
            if(resultCode == presenter().R_CODE){
                this.finish();
            }
        }
    }
}
