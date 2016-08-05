package com.xcyo.live.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xcyo.baselib.ui.BaseFragment;

import java.util.List;

/**
 * Created by caixiaoxiao on 1/6/16.
 */
public class HomeFragPagerAdapter extends FragmentPagerAdapter{

    private List<BaseFragment> mFrags;
    public HomeFragPagerAdapter(FragmentManager fm,List<BaseFragment> frags) {
        super(fm);
        mFrags = frags;
    }

    @Override
    public Fragment getItem(int position) {
        return mFrags.get(position);
    }

    @Override
    public int getCount() {
        return mFrags.size();
    }
}
