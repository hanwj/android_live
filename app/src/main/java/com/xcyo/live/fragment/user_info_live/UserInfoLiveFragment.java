package com.xcyo.live.fragment.user_info_live;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.live.R;
import com.xcyo.live.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 13/6/16.
 */
public class UserInfoLiveFragment extends BaseFragment<UserInfoLiveFragPresenter> {
    private View tag;
    private TextView count;
    private TextView reNew;
    private TextView reHot;
    private ListView list;


    @Override
    protected void initArgs() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.frag_user_info_live,null);
        tag = v.findViewById(R.id.usr_live_tag);
        count = (TextView)v.findViewById(R.id.usr_live_count);
        reNew = (TextView)v.findViewById(R.id.usr_live_renew); reNew.setSelected(true);
        reHot = (TextView)v.findViewById(R.id.usr_live_rehot); reHot.setSelected(false);

        list = (ListView)v.findViewById(R.id.usr_live_list);
        this.list.setAdapter(new LiveAdapter());
        return v;
    }

    @Override
    protected void initEvents() {
        addOnClickListener(reNew, "reNew");
        addOnClickListener(reHot, "reHot");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    protected void handReAction(@NonNull String action){
        if("reNew".equals(action)){
            this.reNew.setSelected(true);
            this.reHot.setSelected(false);
        }else if("reHot".equals(action)){
            this.reHot.setSelected(true);
            this.reNew.setSelected(false);
        }
    }
    private class LiveAdapter extends BaseAdapter {

        protected LiveAdapter(){
            this.data.add("100");
            this.data.add("200");
            this.data.add("300");
            this.data.add("400");
            this.data.add("500");
            this.data.add("600");
            this.data.add("700");
            this.data.add("800");
            this.data.add("900");
            this.data.add("1000");
            this.data.add("1100");
            this.data.add("1200");
        }

        private final List<String> data = new ArrayList<>();

        protected void setData(List<String> list){
            if(list == null){
                return ;
            }
            this.data.clear();
            this.data.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.data.size();
        }

        @Override
        public Object getItem(int position) {
            return this.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_usr_live, null);
            final RoundImageView icon = (RoundImageView)convertView.findViewById(R.id.usr_live_icon);
            final View tip = convertView.findViewById(R.id.usr_live_tip);
            final TextView name = (TextView)convertView.findViewById(R.id.usr_live_name);
            final TextView timer = (TextView)convertView.findViewById(R.id.usr_live_timer);

            final TextView number = (TextView)convertView.findViewById(R.id.usr_live_number);
            final TextView playback = (TextView)convertView.findViewById(R.id.usr_live_playback);
            final String content = this.data.get(position);
            name.setText(content);
            return convertView;
        }
    }
}
