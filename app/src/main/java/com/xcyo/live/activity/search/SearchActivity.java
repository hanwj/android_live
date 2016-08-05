package com.xcyo.live.activity.search;

import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.live.R;
import com.xcyo.live.adapter.HomeFragPagerAdapter;
import com.xcyo.live.fragment.search_topic.SearchTopicFragment;
import com.xcyo.live.fragment.search_user.SearchUserFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovingyou on 2016/6/2.
 */
public class SearchActivity extends BaseActivity<SearchActPresent>{
    private TextView mCancelText;
    private int[] mSearchRadioBtnIDs = {R.id.search_act_user,R.id.search_act_topic};
    private RadioGroup mSearchNavRG;
    private ViewPager mSearchViewpager;
    private EditText mInputText;
    private ImageView mClearImg;
    private SearchUserFragment mUserFragment;
    private SearchTopicFragment mTopicFragment;
    private List<BaseFragment> mFrags = new ArrayList<>();
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_search);
        mCancelText = (TextView) findViewById(R.id.search_act_cancel);
        mInputText = (EditText)findViewById(R.id.search_act_input);
        mSearchNavRG = (RadioGroup) findViewById(R.id.search_act_radiogroup);
        mSearchViewpager = (ViewPager) findViewById(R.id.search_act_viewpager);
        mClearImg = (ImageView) findViewById(R.id.search_act_clear);
        mClearImg.setVisibility(View.INVISIBLE);
        mUserFragment = new SearchUserFragment();
        mTopicFragment = new SearchTopicFragment();
        mFrags.add(mUserFragment);
        mFrags.add(mTopicFragment);
        mSearchViewpager.setAdapter(new HomeFragPagerAdapter(getSupportFragmentManager(), mFrags));
        mSearchViewpager.setCurrentItem(0);
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mCancelText,"cancel");
        addOnClickListener(mClearImg, "clear");
        mInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                startSearch(s.toString());
            }
        });
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    startSearch(mInputText.getText().toString());
                    return true;
                }
                return false;
            }
        });
        mSearchNavRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < mSearchRadioBtnIDs.length; i++) {
                    if (checkedId == mSearchRadioBtnIDs[i]) {
                        mSearchViewpager.setCurrentItem(i);
                    }
                }
            }
        });
        mSearchViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSearchNavRG.check(mSearchRadioBtnIDs[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    protected void clearText(){
        mInputText.setText("");
    }

    private void startSearch(String name){
        if (TextUtils.isEmpty(name)) {
            mClearImg.setVisibility(View.INVISIBLE);
            return;
        }
        mClearImg.setVisibility(View.VISIBLE);
        int pos = mSearchViewpager.getCurrentItem();
        if (pos == 0) {
            mUserFragment.presenter().getSearchData(name);
        }
    }
}
