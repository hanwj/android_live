package com.Sortlistview;


import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author tan
 */
public abstract class SortAdapter extends BaseExpandableListAdapter{

	protected Context mContext;
	private Map<String, List<SortHand>> sortBody;
	private ExpandableListView expandableListView;
	private int[] groupindex = new int[0];//若为1，表示该Group为打开状态 若为0，表示闭合状态
	private boolean isdefaultExpand = true; // 默认不展开

	public SortAdapter(Context mCt, ExpandableListView listView) {
		sortBody = new TreeMap<String, List<SortHand>>();
		this.expandableListView = listView;
		this.mContext = mCt;
		T t = new T();
		this.expandableListView.setOnGroupCollapseListener(t);
		this.expandableListView.setOnGroupExpandListener(t);
	}

	public void setDefaultExpand(boolean defaultExpand){
		this.isdefaultExpand = defaultExpand;
	}

	public void setData(List<SortHand> st) {
		if (st == null) {
			return;
		} else {
			sortBody = SortPlat.handTreesSort(st);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		if(expandableListView != null){
			super.notifyDataSetChanged();
			int[] enti = groupindex.clone();
			groupindex = new int[this.getGroupCount()];
			for(int position = 0; position < groupindex.length; position++){
				if(isdefaultExpand && ((enti.length -1 >= position && ( enti.length == 0 || enti[position] == 1)) || enti.length - 1 < position)){
					this.expandableListView.expandGroup(position);
					groupindex[position] = 1;
				}else{
					this.expandableListView.collapseGroup(position);
					groupindex[position] = 0;
				}
			}
		}
	}

	public void expandGroup(int index){
		if(groupindex.length >= index ){
			boolean isExpand = expandableListView.isGroupExpanded(index);
			if(!isExpand){
				expandableListView.expandGroup(index);
				groupindex[index] = 1;
			}
		}
	}

	public void closeGroup(int index){
		if(groupindex.length >= index ){
			boolean isExpand = expandableListView.isGroupExpanded(index);
			if(isExpand){
				expandableListView.collapseGroup(index);
				groupindex[index] = 0;
			}
		}
	}

	public void selectPostion(String group){
		if(group != null){
			int position = -1;
			for(String key : sortBody.keySet()){
				position++;
				if(key.equals(group)){
//					this.expandableListView.smoothScrollToPosition(position);
					this.expandableListView.setSelectedGroup(position);
					break;
				}
			}
		}
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return sortBody.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		String parent = SortPlat.interSort(sortBody, groupPosition);
		if(parent == null)
			return 0;
		List<SortHand> child = sortBody.get(parent);
		return child == null ? 0 : child.size();
	}

	@Override
	public String getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return SortPlat.interSort(sortBody, groupPosition);
	}

	@Override
	public SortHand getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		String parent = SortPlat.interSort(sortBody, groupPosition);
		if(parent == null)
			return null;
		List<SortHand> child = sortBody.get(parent);
		return child == null ? null : child.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent){

		if(convertView == null){
			TextView tv = new TextView(mContext);
			tv.setTextSize(px2sp(mContext, dip2px(mContext, 18)));
			tv.setTextColor(Color.parseColor("#C15538"));
			tv.setBackgroundColor(Color.parseColor("#eeeeee"));
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setPadding(dip2px(mContext, 8), 0, 0, 0);
			convertView = tv;
		}
		String content = getGroup(groupPosition);
		((TextView)convertView).setText(Html.fromHtml("<B>"+content+"</B>"));
		LayoutParams lyp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		lyp.height =  dip2px(mContext, 38);
		convertView.setLayoutParams(lyp);
		return convertView;
	}

	@Override
	public abstract View getChildView(int groupPosition, int childPosition,
									  boolean isLastChild, View convertView, ViewGroup parent);

	private int px2sp(Context context, float pxValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	private int dip2px(Context context, int dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	private class T implements OnGroupCollapseListener, OnGroupExpandListener{

		@Override
		public void onGroupCollapse(int groupPosition) {
			// TODO Auto-generated method stub
			groupindex[groupPosition] = 0;
		}

		@Override
		public void onGroupExpand(int groupPosition) {
			// TODO Auto-generated method stub
			groupindex[groupPosition] = 1;
		}
	}
}