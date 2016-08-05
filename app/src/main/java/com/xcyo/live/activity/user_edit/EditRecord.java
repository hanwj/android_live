package com.xcyo.live.activity.user_edit;

import com.xcyo.baselib.record.BaseRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/3.
 */
public class EditRecord extends BaseRecord{

    static class CN{
        private List<ProVince> cn;

        public int getLength(){
            if(cn != null){
                return cn.size();
            }
            return 0;
        }

        private ProVince getProVince(int position){
            if(cn != null && position > 0 && position < cn.size()){
                return cn.get(position);
            }
            return null;
        }

        public List<ProVince> getCn() {
            return cn == null ? new ArrayList<ProVince>() : cn;
        }
    }

    static class ProVince{
        private String name;
        private List<City> sub;

        private City getCity(int position){
            if(sub != null && position > 0 && position < sub.size()){
                return sub.get(position);
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public List<City> getSub() {
            return sub == null? new ArrayList<City>() : sub;
        }

        public List<String> getSubString(){
            List<String> allSub = new ArrayList<>();
            if(sub != null){
                for(City c : sub){
                    allSub.add(c.toString());
                }
            }
            return allSub;
        }

        @Override
        public String toString() {
            return name+"{ 共有地级市:"+ (sub != null?sub.size() : 0)+"个}";
        }
    }

    static final class City{
        private String name;

        @Override
        public String toString() {
            return name+"";
        }
    }
}
