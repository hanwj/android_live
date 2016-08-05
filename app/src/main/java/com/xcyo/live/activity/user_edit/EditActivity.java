package com.xcyo.live.activity.user_edit;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.jazz.direct.libs.ConsUtils;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.model.UserModel;
import com.xcyo.live.record.UserRecord;
import com.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by TDJ on 2016/6/3.
 */
public class EditActivity extends BaseActivity<EditPresenter>{
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_user_edit);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_finish = (TextView) findViewById(R.id.base_title_right);
        actionbar_title.setText("编辑资料");

        icon_layer = (RelativeLayout) findViewById(R.id.usr_edit_icon_layer);
        name_layer = (RelativeLayout) findViewById(R.id.usr_edit_name_layer);
        number_layer = (RelativeLayout) findViewById(R.id.usr_edit_number_layer);
        sex_layer = (RelativeLayout) findViewById(R.id.usr_edit_sex_layer);
        signature_layer = (RelativeLayout) findViewById(R.id.usr_edit_signature_layer);
        identity_layer = (RelativeLayout) findViewById(R.id.usr_edit_identity_layer);
        identification_layer = (RelativeLayout) findViewById(R.id.usr_edit_identification_layer);
        age_layer = (RelativeLayout) findViewById(R.id.usr_edit_age_layer);
        emotion_layer = (RelativeLayout) findViewById(R.id.usr_edit_emotion_layer);
        hometown_layer = (RelativeLayout) findViewById(R.id.usr_edit_hometown_layer);
        profession_layer = (RelativeLayout) findViewById(R.id.usr_edit_profession_layer);

        icon = (ImageView) findViewById(R.id.usr_edit_icon);
        name = (TextView) findViewById(R.id.usr_edit_name);
        number = (TextView) findViewById(R.id.usr_edit_number);
        sex = (TextView) findViewById(R.id.usr_edit_sex);
        signature = (TextView) findViewById(R.id.usr_edit_signature);
        identity = (TextView) findViewById(R.id.usr_edit_identity);
        identification = (TextView) findViewById(R.id.usr_edit_identification);
        age = (TextView) findViewById(R.id.usr_edit_age);
        emotion = (TextView) findViewById(R.id.usr_edit_emotion);
        hometown = (TextView) findViewById(R.id.usr_edit_hometown);
        profession = (TextView) findViewById(R.id.usr_edit_profession);

        UserRecord mR = UserModel.getInstance().getRecord();

        if(mR != null){
            UserRecord.User record = mR.getUser();
            UserRecord.UserInfo info = mR.getUserInfo();
            setUserName(switchNull(record.alias));
            setUsrIcon(switchNull(record.avatar));
            number.setText(switchNull(record.uid));
            sex.setText(switchNull(record.sex));
            signature.setText(switchNull(record.signature));

            age.setText(switchNull(info.birthday));
            hometown.setText(switchNull(info.city));
        }
    }

    private TextView actionbar_back, actionbar_title, actionbar_finish;

    private RelativeLayout icon_layer;
    private RelativeLayout name_layer;
    private RelativeLayout number_layer;
    private RelativeLayout sex_layer;
    private RelativeLayout signature_layer;
    private RelativeLayout identity_layer;
    private RelativeLayout identification_layer;
    private RelativeLayout age_layer;
    private RelativeLayout emotion_layer;
    private RelativeLayout hometown_layer;
    private RelativeLayout profession_layer;

    private ImageView icon;
    private TextView name;
    private TextView number;
    private TextView sex;
    private TextView signature;
    private TextView identity;
    private TextView identification;
    private TextView age;
    private TextView emotion;
    private TextView hometown;
    private TextView profession;


    private String switchNull(String attr){
        if(attr == null || attr.equalsIgnoreCase("null")){
            return "";
        }
        return attr;
    }

    @Override
    protected void initEvents() {
        addOnClickListener(actionbar_back, "finish");
        addOnClickListener(icon_layer, "icon_layer");
        addOnClickListener(name_layer, "name_layer");
        addOnClickListener(number_layer, "number_layer");
        addOnClickListener(sex_layer, "sex_layer");
        addOnClickListener(signature_layer, "signature_layer");
        addOnClickListener(identity_layer, "identity_layer");
        addOnClickListener(identification_layer, "identification_layer");
        addOnClickListener(age_layer, "age_layer");
        addOnClickListener(emotion_layer, "emotion_layer");
        addOnClickListener(hometown_layer, "hometown_layer");
        addOnClickListener(profession_layer, "profession_layer");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    protected void showHomeTown(final EditRecord.CN cn){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OptionsPickerView<String> options = new OptionsPickerView<>(EditActivity.this);
                List<EditRecord.ProVince> pv = cn.getCn();
                if (pv == null) {
                    return;
                }
                ArrayList<String> province = new ArrayList<String>();
                ArrayList<ArrayList<String>> cities = new ArrayList<ArrayList<String>>();
                for (EditRecord.ProVince p : pv) {
                    province.add(p.getName());
                    cities.add((ArrayList<String>) p.getSubString());
                }
                options.setPicker(province, cities, true);
                options.setSelectOptions(0, 0);
                options.setCancelable(true);
                options.setCyclic(false);
                options.setOnoptionsSelectListener(getOptionsListener(cn));
                options.show();
            }
        });
    }

    private void setHometown(final CharSequence c){
        hometown.setText(c);
    }

    private OptionsPickerView.OnOptionsSelectListener getOptionsListener(final EditRecord.CN cn) {
        return new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                final EditRecord.ProVince proVince = cn.getCn().get(options1);
                if(proVince != null){
                    final String pro = proVince.getName();
                    final String ct = proVince.getSub().get(option2).toString();
                    setHometown(pro+","+ct);
                }
            }
        };
    }

    protected void showBirthSelector(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TimePickerView picker = new TimePickerView(EditActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
//                try {
//                    java.lang.reflect.Field field = picker.getClass().getSuperclass().getDeclaredField("rootView");
//                    if(field != null){
//                        field.setAccessible(true);
//                        View v = (View)field.get(picker);
//                        if(v != null){
//                            v.setFitsSystemWindows(true);
//                            v.setBackgroundColor(Color.RED);
//                            ((ViewGroup)v).getChildAt(0).setVisibility(View.GONE);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                Calendar cal = Calendar.getInstance();
                picker.setRange(cal.get(Calendar.YEAR) - 40, cal.get(Calendar.YEAR) - 5);
                picker.setTime(new Date(1990, 01, 01));
                picker.setCancelable(true);
                picker.setCyclic(true);
                picker.setOnTimeSelectListener(getOnTimeSelector());
                picker.show();
            }
        });
    }

    private void setAge(final int year){
        age.setText(year + " 岁");
    }

    private TimePickerView.OnTimeSelectListener getOnTimeSelector(){
        return new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if(date != null){
                    int year = new Date().getYear() - date.getYear();
                    setAge(year);
                }
            }
        };
    }

    private View.OnClickListener emotionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            synchronized (emotionClick){
                int res = v.getId();
                switch (res){
                    case R.id.emotion_scr:
                        setEmotion("保密");
                        break;
                    case R.id.emotion_single:
                        setEmotion("单身");
                        break;
                    case R.id.emotion_love:
                        setEmotion("恋爱中");
                        break;
                    case R.id.emotion_marry:
                        setEmotion("结婚");
                        break;
                    case R.id.emotion_gay:
                        setEmotion("同性");
                        break;
                }
            }
            dialog.dismiss();
        }
    };

    private void setEmotion(final CharSequence c){
        emotion.setText(c);
    }

    private Dialog dialog ;

    protected void showEmotionState(){
        View convertView = getLayoutInflater().inflate(R.layout.dialog_edit_emotion, null);
        TextView src = (TextView)convertView.findViewById(R.id.emotion_scr);
        TextView single = (TextView)convertView.findViewById(R.id.emotion_single);
        TextView love = (TextView)convertView.findViewById(R.id.emotion_love);
        TextView marry = (TextView)convertView.findViewById(R.id.emotion_marry);
        TextView gay = (TextView)convertView.findViewById(R.id.emotion_gay);
        src.setSelected(true);
        src.setOnClickListener(emotionClick);
        single.setOnClickListener(emotionClick);
        love.setOnClickListener(emotionClick);
        marry.setOnClickListener(emotionClick);
        gay.setOnClickListener(emotionClick);

        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(convertView);
        WindowManager.LayoutParams ws = dialog.getWindow().getAttributes();
        ws.width = WindowManager.LayoutParams.MATCH_PARENT;
        ws.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ws.alpha = 1.0f;
        ws.dimAmount = 0.5f;
        ws.gravity = Gravity.BOTTOM;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    protected void setUserName(@NonNull String name){
        this.name.setText(name);
    }

    protected void setUsrIcon(@NonNull String path){
        if(path != null){
            x.image().bind(icon, "http://file.xcyo.com/"+path);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123){
            switch (resultCode){
                case EditPresenter.R_NAME_CODE:
                    presenter().upLoadUsrName(data.getStringExtra("r_content"));
                    break;
                case EditPresenter.R_ICON_CODE:
                    setUsrIcon(data.getStringExtra("r_icon"));
                    if(UserModel.getInstance().getRecord() != null){
                        UserModel.getInstance().getRecord().getUser().avatar = data.getStringExtra("r_icon");
                    }
                    break;
            }
        }
    }


}
