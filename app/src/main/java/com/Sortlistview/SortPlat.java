package com.Sortlistview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.annotation.SuppressLint;

/**
 * @author tan
 */
class SortPlat {

	@SuppressLint("NewApi")
	protected static Map<String, List<SortHand>> handTreesSort(List<SortHand> list){
		TreeMap<String, List<SortHand>> stree= new TreeMap<String, List<SortHand>>();
		for(SortHand model:list){
			String str = model.getSort();
			if(str == null) str = "#";
			if(stree.containsKey(str)){
				List<SortHand> body = stree.get(str);
				body.add(model);
			}else{
				List<SortHand> body = new ArrayList<SortHand>();
				stree.put(str, body);
				body.add(model);
			}
		}
		if(stree.containsKey("#")){
			Map<String, List<SortHand>> hands = new LinkedHashMap<String, List<SortHand>>();
			hands.putAll(stree.tailMap("#", false));
			hands.put("#", stree.get("#"));
			return hands;
		}
		return stree;
	}
	
    protected static String interSort(Map<String, List<SortHand>> tree, int position){
		if(tree != null){
			List<String> sortKey = new LinkedList<String>();
			sortKey.addAll(tree.keySet());
			if(position < sortKey.size())
				return sortKey.get(position);
		}
		return null;
	}
}
