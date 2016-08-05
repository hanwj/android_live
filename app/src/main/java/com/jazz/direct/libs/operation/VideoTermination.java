package com.jazz.direct.libs.operation;

/**
 * Created by TDJ on 2016/6/30.
 */
public interface VideoTermination {
    public void onTermination(int msgCode);
    public void onTerminationFailed(int msgCode, String msg);
}
