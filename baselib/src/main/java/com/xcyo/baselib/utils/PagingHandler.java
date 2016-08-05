package com.xcyo.baselib.utils;

import java.util.List;

/**
 * Created by lovingyou on 2016/1/6.
 */
public class PagingHandler {
    private List<?> mList;
    private int mMaxPage;
    private int mCurrPage;
    private int mCountOnePage;
    public PagingHandler(List<?> list, int countOnePage){
        mList = list;
        mCurrPage = 0;
        mCountOnePage = countOnePage;
        mMaxPage = (int)Math.ceil(mList.size()/mCountOnePage);
    }

    public void setList(List<?> newList){
        mList = newList;
        mMaxPage = (int)Math.ceil(mList.size()/mCountOnePage);
    }

    //获取当前页数据
    public List<?> getListCurrPage(){
        return getListInRange(0, mCurrPage + 1);
    }

    //获取下一页数据
    public List<?> getListNextPage(){
        mCurrPage++;
        return getListCurrPage();
    }

    //是否还有更多
    public boolean isMore(){
        return mCurrPage < mMaxPage - 1;
    }

    //不包含 toPage
    public List<?> getListInRange(int startPage, int len){
        if (startPage < 0 || len <= 0){
            return null;
        }
        int startIndex = startPage * mCountOnePage;
        if (startIndex >= mList.size()){
            return null;
        }
        int toIndex = (startPage + len) * mCountOnePage;
        if(toIndex >= mList.size()){
            toIndex = mList.size() - 1;
        }

        return mList.subList(startIndex, toIndex);
    }

    public List<?> getAllList(){
        return mList;
    }
}
