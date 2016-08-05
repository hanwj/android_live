package com.xcyo.live.fragment.home;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.live.R;
import com.xcyo.live.activity.home_filter.HomeFilterActivity;
import com.xcyo.live.adapter.HomeFragPagerAdapter;
import com.xcyo.live.fragment.home_follow.HomeFollowFragment;
import com.xcyo.live.fragment.home_hot.HomeHotFragment;
import com.xcyo.live.fragment.home_new.HomeNewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 1/6/16.
 */
public class HomeFragment extends BaseFragment<HomeFragPresenter> {
    private int[] mRBIDs = {R.id.main_home_follow,R.id.main_home_hot,R.id.main_home_new};
    private ImageView mSearchImg,mMsgImg;
    private RadioGroup mTopNavRG;
    private ViewPager mViewPager;
    private RadioButton mHotRB;
    private ImageView mMoreImg;
    private TextView mMsgTipText;
    private List<BaseFragment> mFrags = new ArrayList<>();
    private long lastSelectedHotPageTime = -1;
    @Override
    protected void initArgs() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.frag_main_home,null);
        mSearchImg = (ImageView)v.findViewById(R.id.main_home_search);
        mMsgImg = (ImageView)v.findViewById(R.id.main_home_message);
        mMoreImg = (ImageView)v.findViewById(R.id.main_home_more);
        mTopNavRG = (RadioGroup)v.findViewById(R.id.main_home_radiogroup);
        mViewPager = (ViewPager)v.findViewById(R.id.main_home_viewpager);
        mHotRB = (RadioButton)v.findViewById(R.id.main_home_hot);
        mMsgTipText = (TextView)v.findViewById(R.id.main_home_red_tip);
        mFrags.add(new HomeFollowFragment());
        mFrags.add(new HomeHotFragment());
        mFrags.add(new HomeNewFragment());
        mViewPager.setAdapter(new HomeFragPagerAdapter(getChildFragmentManager(), mFrags));
        mViewPager.setCurrentItem(1);
        return v;
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mSearchImg, "search");
        addOnClickListener(mMsgImg, "message");
        addOnClickListener(mHotRB, "hot");
        mTopNavRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < mRBIDs.length; i++) {
                    if (checkedId == mRBIDs[i]) {
                        if (checkedId == R.id.main_home_hot) {
                            lastSelectedHotPageTime = System.currentTimeMillis();
                            mMoreImg.setVisibility(View.VISIBLE);
                        } else {
                            lastSelectedHotPageTime = -1;
                            mMoreImg.setVisibility(View.GONE);
                        }
                        mViewPager.setCurrentItem(i);
                    }
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTopNavRG.check(mRBIDs[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001){
            if (data != null){
                String city = data.getStringExtra("city");
                String sex = data.getStringExtra("sex");
                mHotRB.setText(city);
                ((HomeHotFragment)mFrags.get(1)).setCityCategory(city);
                ((HomeHotFragment)mFrags.get(1)).setSexCategory(sex);
            }
        }
    }

    protected void updateRecentContacts(){
        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
            @Override
            public void onResult(int i, List<RecentContact> recentContacts, Throwable throwable) {
                if (i == ResponseCode.RES_SUCCESS){
                    int count =0;
                    for (RecentContact msg : recentContacts){
                        count += msg.getUnreadCount();
                    }
                    if (count > 0){
                        mMsgTipText.setVisibility(View.VISIBLE);
                        if (count > 99){
                            mMsgTipText.setText("+99");
                        }else {
                            mMsgTipText.setText(count+"");
                        }
                    }else{
                        mMsgTipText.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public void setCurrentViewPager(int pos,boolean smoothScroll){
        mViewPager.setCurrentItem(pos,smoothScroll);
    }

    public void startHomeFilterActivity(String str){
        if (System.currentTimeMillis() - lastSelectedHotPageTime > 100){
            Intent intent = new Intent(getActivity(), HomeFilterActivity.class);
            intent.putExtra("sex",((HomeHotFragment)mFrags.get(1)).getSexCategory());
            intent.putExtra("city",((HomeHotFragment)mFrags.get(1)).getCityCategory());
            startActivityForResult(intent, 1001);
            getActivity().overridePendingTransition(R.anim.anim_bottom_to_top_enter,R.anim.anim_empty);
        }
    }
}