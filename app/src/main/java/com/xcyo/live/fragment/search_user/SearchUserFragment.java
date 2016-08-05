package com.xcyo.live.fragment.search_user;

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
import com.xcyo.live.adapter.SearchUserListViewAdapter;
import com.xcyo.live.record.SearchUserListServerRecord;
import com.xcyo.live.record.UserSimpleRecord;
import com.xcyo.live.utils.ServerEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovingyou on 2016/6/3.
 */
public class SearchUserFragment extends BaseFragment<SearchUserFragPresenter> {
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
        ((TextView) mEmptyView.findViewById(R.id.empty_tip)).setText("没有搜索到你想要的用户");
        return v;
    }
    //配置listView数据
    private void initListData() {
        List<UserSimpleRecord.User> listDatas = new ArrayList<>();
        for (int i = 0;i < 10;i++){
            UserSimpleRecord.User record = new UserSimpleRecord.User();
            record.alias = "蔡潇潇";
            record.gender = "1";
            record.level = "1";
            record.signature = "这个人很懒，什么都没有留下";
            listDatas.add(record);
        }
        mListView.setAdapter(new SearchUserListViewAdapter(getActivity(), listDatas));
    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_USER_SEARCH.equals(evt)){
            showLoading(false);
            setListView(((SearchUserListServerRecord)data.record).list);
        }
    }

    @Override
    public void onServerFailedCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_USER_SEARCH.equals(evt)){
            showLoading(false);
        }
    }

    protected void setListView(List<UserSimpleRecord.User> listData){
        if (listData.size() > 0){
            mEmptyView.setVisibility(View.GONE);
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mListView.setAdapter(new SearchUserListViewAdapter(getActivity(),listData));
    }

    protected void showLoading(boolean show){
        if (show){
            mEmptyView.setVisibility(View.GONE);
            mLoadingImg.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_loading);
            mLoadingImg.startAnimation(anim);
        }else{
            mLoadingImg.clearAnimation();
            mLoadingImg.setVisibility(View.INVISIBLE);
        }
    }
}
