package com.xcyo.live.record;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by caixiaoxiao on 1/6/16.
 */
public class UserRecord extends UserSimpleRecord {
    public Set<String> follows;//

    public Set<String> getFollow() {
        return follows;
    }

    public void setFollow(Set<String> follow) {
        this.follows = follow;
    }

    public void addFollow(String uid){
        if(this.follows == null){
            this.follows = new HashSet<>();
        }
        this.follows.add(uid);
    }
}
