package com.xcyo.live.activity.user_edit_receive;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/7.
 */
public class ReceiveActivity extends BaseActivity<ReceivePresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_usr_receive);
        this.count = (TextView)findViewById(R.id.usr_receiver_count);
        this.list = (ListView)findViewById(R.id.usr_receiver_list);
        this.list.setAdapter(new ReceiveAdapter());
    }

    private TextView count ;
    private ListView list;

    @Override
    protected void initEvents() {

    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    private class ReceiveAdapter extends BaseAdapter{

        List<String> data = new ArrayList<>();

        private ReceiveAdapter(){
            this.data.add("1");
            this.data.add("3");
            this.data.add("4");
            this.data.add("5");
            this.data.add("6");
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
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item_usr_receive, null);
            }
            final TextView coin = (TextView)convertView.findViewById(R.id.item_receive_coin);
            final TextView time = (TextView)convertView.findViewById(R.id.item_receive_time);
            final TextView tag = (TextView) convertView.findViewById(R.id.item_receive_tag);

            return convertView;
        }
    }
}
