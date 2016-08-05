package com.xcyo.live.activity.login_area;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.Sortlistview.SideBar;
import com.Sortlistview.SortAdapter;
import com.Sortlistview.SortHand;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.utils.Util;
import com.xcyo.live.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/27.
 */
public class AreaActivity extends BaseActivity<AreaPresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_area);
        this.mList = (ExpandableListView)findViewById(R.id.list);
        bar = (SideBar)findViewById(R.id.side);
        bar.setOnTouchingLetterChangedListener(letterChangedListener);
        AreaAdapter mdapter = new AreaAdapter(this, this.mList);
        mList.setAdapter(mdapter);

        try {
            List<SortHand> areas = parseXMLArea(getResources().getAssets().open("areacode_cn.xml"));
            mdapter.setData(areas);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private ExpandableListView mList;
    private SideBar bar;

    @Override
    protected void initEvents() {

    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }


    private List<SortHand> parseXMLArea(InputStream ips){
        if(ips == null){
            return null;
        }
        List<SortHand> records = null;
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(ips, "UTF-8");
            int eventType = parser.getEventType();
            AreaRecord record = null;
            while(eventType != XmlPullParser.END_DOCUMENT){
                android.util.Log.e("TAG", eventType+"");
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        records = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if("string".equals(parser.getName())){
                            String value = parser.nextText();
                            record = getAreaRecord(value);
                            if(records != null && record != null){
                                records.add(record);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if(ips != null){
                try {
                    ips.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return records;
    }

    private AreaRecord getAreaRecord(String content){
        if(content != null && content.contains(":")){
            String[] p = content.split(":");
            AreaRecord record = new AreaRecord();
            record.setCh(p[0]);
            record.setNumber(p[1]);
            record.setPinyi(p[2]);
            return record;
        }
        return null;
    }

    private final class AreaAdapter extends SortAdapter{

        public AreaAdapter(Context mCt, ExpandableListView listView) {
            super(mCt, listView);
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = new TextView(AreaActivity.this);
            }
            ViewGroup.LayoutParams vpg = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            vpg.width = ViewGroup.LayoutParams.MATCH_PARENT;
            vpg.height = Util.dp2px(48);
            convertView.setLayoutParams(vpg);
            AreaRecord record = (AreaRecord) getChild(groupPosition, childPosition);
            ((TextView)convertView).setText(record.getCh());
            ((TextView)convertView).setGravity(Gravity.CENTER_VERTICAL);
            ((TextView)convertView).setTextSize(Util.dp2sp(14));
            ((TextView)convertView).setTextColor(Color.BLACK);
            ((TextView)convertView).setPadding(Util.dp2px(8), 0, 0, 0);
            convertView.setBackgroundDrawable(getTextDrawable());
            convertView.setOnClickListener(getItemClick(record));
            return convertView;
        }
    }


    android.graphics.drawable.Drawable getTextDrawable(){
        StateListDrawable sd = new StateListDrawable();
        sd.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(Color.parseColor("#d3d3d3")));
        sd.addState(new int[0], new ColorDrawable(Color.WHITE));
        return sd;
    }

    private SideBar.OnTouchingLetterChangedListener letterChangedListener = new SideBar.OnTouchingLetterChangedListener() {
        @Override
        public void onTouchingLetterChanged(String s) {
            ((AreaAdapter)(mList.getExpandableListAdapter())).selectPostion(s);
        }
    };

    private View.OnClickListener getItemClick(final AreaRecord record){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(record != null){
                    Intent mIntent = new Intent();
                    mIntent.putExtra("area_result", record);
                    setResult(1001, mIntent);
                    AreaActivity.this.finish();
                }
            }
        };
    }
}
