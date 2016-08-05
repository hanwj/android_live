package com.xcyo.live.activity.home_filter;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.adapter.HomeFilterListViewAdapter;
import com.xcyo.live.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 7/6/16.
 */
public class HomeFilterActivity extends BaseActivity<HomeFilterActPresenter> {
    private String[] citys = {"北京","上海","天津","重庆","广东","河南","河北","热门"};
    private List<String> mCityList;
    private TextView mBackText;
    private ListView mListView;
    private Button mConfirmBtn;

    private RadioGroup mRG;
    private String curCity = "热门";
    private String curSex = Constants.SEX_ALL;
    @Override
    protected void initArgs() {
        Intent intent = getIntent();
        curCity = intent.getStringExtra("city");
        curSex = intent.getStringExtra("sex");
        mCityList = new ArrayList<>();
        mCityList.add(curCity);
        for (int i = 0;i < citys.length; i++){
            if (!citys[i].equals(curCity)){
                mCityList.add(citys[i]);
            }
        }
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_home_filter);
        mBackText = (TextView)findViewById(R.id.base_title_back);
        mBackText.setText("  ");
        mListView = (ListView)findViewById(R.id.home_filter_listview);
        mConfirmBtn = (Button)findViewById(R.id.home_filter_confirm);
        mRG = (RadioGroup)findViewById(R.id.home_filter_rg);
        findViewById(R.id.base_title_name).setVisibility(View.GONE);
        mListView.setAdapter(new HomeFilterListViewAdapter(this, mCityList));
        mListView.setItemChecked(0,true);
        if (curSex.equals(Constants.SEX_MALE)){
            mRG.check(R.id.home_filter_male);
        }else if (curSex.equals(Constants.SEX_FEMALE)){
            mRG.check(R.id.home_filter_female);
        }else {
            mRG.check(R.id.home_filter_all);
        }
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mBackText, "back");
        addOnClickListener(mConfirmBtn, "confirm");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_empty,R.anim.anim_top_to_bottom_exit);
    }

    public String getCheckedSex(){
        int checkedID = mRG.getCheckedRadioButtonId();
        String sex = Constants.SEX_ALL;
        if (checkedID == R.id.home_filter_female){
            sex = Constants.SEX_FEMALE;
        }else if (checkedID == R.id.home_filter_male){
            sex = Constants.SEX_MALE;
        }
        return sex;
    }

    public String getSelectedCity(){
        return mCityList.get(mListView.getCheckedItemPosition());
    }
}
