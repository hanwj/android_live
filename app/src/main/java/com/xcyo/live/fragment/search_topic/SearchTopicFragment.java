package com.xcyo.live.fragment.search_topic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.live.R;
import com.xcyo.live.adapter.SearchTopicListViewAdapter;
import com.xcyo.live.record.TopicRecord;

import java.util.List;

/**
 * Created by lovingyou on 2016/6/3.
 */
public class SearchTopicFragment extends BaseFragment<SearchTopicFragPresenter> {
    private ListView mListView;
    private View mEmptyView;
    private ImageView mLoadingImg;
    @Override
    protected void initArgs() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.frag_search_user,null);
        mListView = (ListView) v.findViewById(R.id.search_user_frag_listview);
        mLoadingImg = (ImageView)v.findViewById(R.id.server_loading);
        mEmptyView = v.findViewById(R.id.empty_layout);
        mEmptyView.setVisibility(View.GONE);
        mLoadingImg.setVisibility(View.INVISIBLE);
        ((TextView)mEmptyView.findViewById(R.id.empty_tip)).setText("抱歉，没有搜到相关内容");
        return v;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    protected void setListView(List<TopicRecord> listData){
        if (listData.size() > 0){
            mEmptyView.setVisibility(View.GONE);
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mListView.setAdapter(new SearchTopicListViewAdapter(getActivity(),listData));
    }

    protected void showLoading(boolean show){
        if (show){
            mLoadingImg.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_loading);
            mLoadingImg.startAnimation(anim);
        }else{
            mLoadingImg.clearAnimation();
            mLoadingImg.setVisibility(View.INVISIBLE);
        }
    }
}
