package com.xcyo.live.fragment.user_info_home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.live.R;

/**
 * Created by caixiaoxiao on 13/6/16.
 */
public class UserInfoHomeFragment extends BaseFragment<UserInfoHomeFragPresenter>{
    private View mRankLayout;
    private ImageView mRank1,mRank2,mRank3;
    private TextView mAgeText;
    private TextView mEmotionalText;
    private TextView mCityText;
    private TextView mWorkText;
    private TextView mUidText;
    private TextView mSignatureText;
    @Override
    protected void initArgs() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.frag_user_info_home,null);
        mRankLayout = v.findViewById(R.id.user_info_home_rank);
        mAgeText = (TextView)v.findViewById(R.id.user_info_home_age);
        mEmotionalText = (TextView)v.findViewById(R.id.user_info_home_emotional);
        mCityText = (TextView)v.findViewById(R.id.user_info_home_city);
        mWorkText = (TextView)v.findViewById(R.id.user_info_home_work);
        mUidText = (TextView)v.findViewById(R.id.user_info_home_uid);
        mSignatureText = (TextView)v.findViewById(R.id.user_info_home_signature);
        return v;
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mRankLayout,"rank");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }
}
