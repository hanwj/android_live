package com.xcyo.live.activity.setting_chatmanager;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.view.SlideSwitch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/18.
 */
public class ChatManagerActivity extends BaseActivity<ChatManagerPresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_chatmanager);
        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_title.setText("消息管理");

        recordList = (ListView)findViewById(R.id.setting_chatmanager_list);
        recordList.setAdapter(new ChatManagerAdapter());

        all = (SlideSwitch)findViewById(R.id.chatmanager_all);
        close = (SlideSwitch)findViewById(R.id.chatmanager_close);
    }

    private TextView actionbar_back, actionbar_title;

    private SlideSwitch all, close;

    private ListView recordList;

    @Override
    protected void initEvents() {
        addOnClickListener(actionbar_back, "finish");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    private class ChatManagerAdapter extends BaseAdapter {

        private final List<String> data = new ArrayList<>();

        public ChatManagerAdapter(){
            data.add("1000000");
            data.add("2000000");
            data.add("3000000");
            data.add("4000000");
        }

        public void setData(List<String> list){
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
                convertView = getLayoutInflater().inflate(R.layout.item_chatrecord, null);
            final ImageView icon = (ImageView) convertView.findViewById(R.id.usr_chat_icon);
            final View icon_tip = convertView.findViewById(R.id.usr_chat_tip);
            final ImageView sex = (ImageView) convertView.findViewById(R.id.usr_chat_sex);
            final TextView level_tip = (TextView) convertView.findViewById(R.id.usr_chat_levle_tip);
            final TextView name = (TextView) convertView.findViewById(R.id.usr_chat_name);
            final SlideSwitch slide = (SlideSwitch)convertView.findViewById(R.id.usr_chat_switch);
            name.setText(data.get(position));
            return convertView;
        }
    }

    private SlideSwitch.SlideListener getSlideLiStener(@NonNull final View v){
        return new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                switch (v.getId()){

                }
            }

            @Override
            public void close() {
                switch (v.getId()){

                }
            }
        };
    }
}
