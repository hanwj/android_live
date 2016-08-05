package com.xcyo.live.activity.user_exchange;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.live.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/6.
 */
public class ExchangeActivity extends BaseActivity<ExchangePresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_user_exchange);
        coin_count = (TextView) findViewById(R.id.usr_coin_count);
        exchange_list = (ListView) findViewById(R.id.usr_exchange_list);
        exchange_list.setAdapter(new ExchageAdapter());
    }

    private TextView coin_count;
    private ListView exchange_list;

    @Override
    protected void initEvents() {
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    final class ExchageAdapter extends BaseAdapter{

        private ExchageAdapter(){
            data.add("100");
            data.add("200");
            data.add("300");
            data.add("400");
            data.add("500");
        }

        private final List<String> data = new ArrayList<>();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item_exchange, null);
            }
            final ImageView icon = (ImageView)convertView.findViewById(R.id.item_exchange_icon);
            final TextView content = (TextView) convertView.findViewById(R.id.item_exchange_content);
            final TextView submit = (TextView) convertView.findViewById(R.id.item_exchange_submit);
            content.setText(data.get(position));
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.e("TAG", data.get(position));
                }
            });
            return convertView;
        }
    }
}
