package com.xcyo.live.dialog.live_message_list;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseDialogFragment;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.baselib.utils.Util;
import com.xcyo.live.R;
import com.xcyo.live.adapter.HomeFragPagerAdapter;
import com.xcyo.live.fragment.message_list.MessageUserFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 27/6/16.
 */
public class LiveMessageListDialogFragment extends BaseDialogFragment<LiveMessageListDialogPresenter>{
    private ImageView mImageBack;
    private int[] mMessageRadioBtnIDs = {R.id.message_act_follow,R.id.message_act_unfollow};
    private RadioGroup mMessageNavRG;
    private ViewPager mMessageViewPager;
    private List<BaseFragment> mFrags = new ArrayList<>();
    @Override
    protected void initArgs() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setWindowAnimations(R.style.verticalAnim);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.activity_message_list,null);
        mImageBack = (ImageView) v.findViewById(R.id.message_act_back);
        mMessageNavRG = (RadioGroup) v.findViewById(R.id.message_act_radiogroup);
        mMessageViewPager = (ViewPager) v.findViewById(R.id.message_act_viewpager);
        mFrags.add(new MessageUserFragment());
        mFrags.add(new MessageUserFragment());
        mMessageViewPager.setAdapter(new HomeFragPagerAdapter(getChildFragmentManager(), mFrags));
        mMessageViewPager.setCurrentItem(0);
        return v;
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams ll = getDialog().getWindow().getAttributes();
        ll.gravity = Gravity.BOTTOM;
        ll.width = WindowManager.LayoutParams.MATCH_PARENT;
        ll.height = getResources().getDimensionPixelOffset(R.dimen.room_message_height);
        getDialog().getWindow().setAttributes(ll);
        super.onResume();
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mImageBack,"back");
        mMessageNavRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0;i < mMessageRadioBtnIDs.length;i++){
                    if (checkedId == mMessageRadioBtnIDs[i]){
                        mMessageViewPager.setCurrentItem(i);
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
}
