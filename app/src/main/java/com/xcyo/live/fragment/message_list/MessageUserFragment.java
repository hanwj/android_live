package com.xcyo.live.fragment.message_list;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.live.R;
import com.xcyo.live.activity.room_live.LiveActivity;
import com.xcyo.live.activity.message.MessageActivity;
import com.xcyo.live.adapter.MessageUserListViewAdapter;
import com.xcyo.live.dialog.live_message.LiveMessageDialogFragment;
import com.xcyo.live.nim.NIMUserInfoCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovingyou on 2016/6/3.
 */
public class MessageUserFragment extends BaseFragment<MessageUserFragPresenter> {
    private ListView mListView;
    private View mEmptyView;
    private List<RecentContact> mListData = new ArrayList<>();
    private MessageUserListViewAdapter mAdapter;
    @Override
    protected void initArgs() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.frag_message_user,null);
        mEmptyView = v.findViewById(R.id.empty_layout);
        mListView = (ListView) v.findViewById(R.id.search_user_frag_listview);
        mAdapter = new MessageUserListViewAdapter(getActivity(),mListData);
        mListView.setAdapter(mAdapter);
        return v;
    }

    @Override
    protected void initEvents() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecentContact record = (RecentContact) mListView.getItemAtPosition(position);
                String uid = record.getContactId();
                String alias = NIMUserInfoCache.getInstance().getUserName(uid);
                if (getActivity() instanceof LiveActivity) {
                    new LiveMessageDialogFragment().init(uid, alias)
                            .show(getChildFragmentManager(), LiveMessageDialogFragment.class.getName());
                } else {
                    Intent intent = new Intent(getActivity(), MessageActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("alias", alias);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    public void updateListView(List<RecentContact> recents){
        int index;
        for (RecentContact recent:recents){
            index = -1;
            for (int i = 0;i < mListData.size();i++){
                if (mListData.get(i).getContactId().equals(recent.getContactId())){
                    index = i;
                    break;
                }
            }
            if (index >= 0){
                mListData.remove(index);
            }
            mListData.add(recent);
        }
        mEmptyView.setVisibility(mListData.size() > 0 ? View.GONE : View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }
}
