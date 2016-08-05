package com.xcyo.live.activity.user_live;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.live.R;
import com.xcyo.live.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/8.
 */
public class ProLiveActivity extends BaseActivity<ProLivePresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_user_live);

        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_title.setText("我的直播");

        tag = findViewById(R.id.usr_live_tag);
        count = (TextView)findViewById(R.id.usr_live_count);
        reNew = (TextView)findViewById(R.id.usr_live_renew); reNew.setSelected(true);
        reHot = (TextView)findViewById(R.id.usr_live_rehot); reHot.setSelected(false);

        list = (ListView)findViewById(R.id.usr_live_list);
        this.list.setAdapter(new LiveAdapter());
    }

    private TextView actionbar_back, actionbar_title;

    private View tag;
    private TextView count;
    private TextView reNew;
    private TextView reHot;

    private ListView list;


    @Override
    protected void initEvents() {
        addOnClickListener(actionbar_back, "finish");
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

    private class LiveAdapter extends BaseAdapter{

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
                convertView = getLayoutInflater().inflate(R.layout.item_usr_live, null);
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
