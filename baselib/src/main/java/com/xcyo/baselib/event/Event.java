package com.xcyo.baselib.event;

import com.xcyo.baselib.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.security.auth.callback.Callback;

/**
 * Created by wanghongyu on 30/12/15.
 */
public class Event {
    private static final String TAG = "Event";
    public interface EventCallback{
        boolean onEvent(String evt, Object data);
    }

    private HashMap<String, HashMap<Integer, EventCallback>> mEventMap;

    private Event(){
        mEventMap = new HashMap<String, HashMap<Integer, EventCallback>>();
    }

    private static Event sEvent = null;
    public synchronized static Event getInstance(){
        if(sEvent == null){
            sEvent = new Event();
        }
        return sEvent;
    }

    private HashMap<Integer, EventCallback> getTargetMap(String evt){
        HashMap<Integer ,EventCallback> targetMap = null;
        if(mEventMap.containsKey(evt)){
            targetMap = mEventMap.get(evt);
        }else{
            targetMap = new HashMap<Integer, EventCallback>();
            mEventMap.put(evt, targetMap);
        }
        return targetMap;
    }

    public boolean mapEvent(String evt, EventMapper target, EventCallback cb){
        HashMap<Integer, EventCallback> targetMap = getTargetMap(evt);
        int targetId = target.getUniqId();
        if (targetMap.containsKey(targetId)){
            unmapEvent(evt, target);
            targetMap = getTargetMap(evt);
        }
        targetMap.put(targetId, cb);
        return true;
    }

    public void unmapEvent(String evt, EventMapper target){
        if(mEventMap.containsKey(evt)){
            HashMap<Integer, EventCallback> targetMap = getTargetMap(evt);
            int targetId = target.getUniqId();
            if (targetMap.containsKey(targetId)){
                targetMap.remove(targetId);
                if (targetMap.size() == 0){
                    mEventMap.remove(evt);
                }
            }
        }
    }

    public void unmapEvents(String evt){
        if(mEventMap.containsKey(evt)){
            mEventMap.remove(evt);
        }
    }

    public void unmapEventsWithTarget(EventMapper target){
        ArrayList<String> markRemove = new ArrayList<String>();
        for(String evt: mEventMap.keySet()){
            HashMap<Integer, EventCallback> targetMap = getTargetMap(evt);
            int targetId = target.getUniqId();
            targetMap.remove(targetId);
            if (targetMap.size() == 0){
                markRemove.add(evt);
            }
        }

        for (String evt: markRemove){
            mEventMap.remove(evt);
        }
    }

    public void dispatch(String evt, Object data){
        if(mEventMap.containsKey(evt)){
            ArrayList<Integer> needRemoveTargetIds = new ArrayList<Integer>();
            HashMap<Integer, EventCallback> targetMap = getTargetMap(evt);
            for(int targetId: targetMap.keySet()){
                EventCallback cb = targetMap.get(targetId);
                if(!cb.onEvent(evt, data)){
                    needRemoveTargetIds.add(targetId);
                }
            }
            for(int removeTargetId: needRemoveTargetIds){
                targetMap.remove(removeTargetId);
            }
            if (targetMap.size() == 0){
                mEventMap.remove(evt);
            }
        }
    }

    public void dispatch(String evt){
        dispatch(evt, null);
    }
}
