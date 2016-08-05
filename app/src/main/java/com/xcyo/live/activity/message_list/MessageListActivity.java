package com.xcyo.live.activity.message_list;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.live.R;
import com.xcyo.live.adapter.HomeFragPagerAdapter;
import com.xcyo.live.fragment.message_list.MessageUserFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovingyou on 2016/6/2.
 */
public class MessageListActivity extends BaseActivity<MessageListActPresent>{
    private ImageView mImageBack;
    private int[] mMessageRadioBtnIDs = {R.id.message_act_follow,R.id.message_act_unfollow};
    private RadioGroup mMessageNavRG;
    private ViewPager mMessageViewPager;
    private List<BaseFragment> mFrags = new ArrayList<>();
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_message_list);
        mImageBack = (ImageView) findViewById(R.id.message_act_back);
        mMessageNavRG = (RadioGroup) findViewById(R.id.message_act_radiogroup);
        mMessageViewPager = (ViewPager) findViewById(R.id.message_act_viewpager);
        mFrags.add(new MessageUserFragment());
        mFrags.add(new MessageUserFragment());
        mMessageViewPager.setAdapter(new HomeFragPagerAdapter(getSupportFragmentManager(), mFrags));
        mMessageViewPager.setCurrentItem(0);
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mImageBack, "back");
        mMessageNavRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < mMessageRadioBtnIDs.length; i++) {
                    if (checkedId == mMessageRadioBtnIDs[i]) {
                        mMessageViewPager.setCurrentItem(i,false);
                    }
                }
            }
        });
        mMessageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMessageNavRG.check(mMessageRadioBtnIDs[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    protected void updateRecentContacts(List<RecentContact> recents){
        ((MessageUserFragment)mFrags.get(0)).updateListView(recents);
    }
}
