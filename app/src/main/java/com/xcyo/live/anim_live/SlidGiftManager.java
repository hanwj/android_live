package com.xcyo.live.anim_live;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.LinkedList;

/**
 * Created by TDJ on 2016/7/6.
 */
public final class SlidGiftManager {

    private static Window mWindow;
    private static RelativeLayout mContainer;
    private static final LinkedList<SlidGift.GiftRecord> prepList = new LinkedList<>();
    private static final LinkedList<SlidGift> currentList = new LinkedList<>();

    private SlidGiftManager() {
    }

    private static final class SlidInstance {
        private static final SlidGiftManager t = new SlidGiftManager();
    }

    public static SlidGiftManager get(Window window, int container) {
        mWindow = window;
        prepList.clear();
        currentList.clear();
        mContainer = (RelativeLayout) mWindow.findViewById(container);
        if (mContainer == null)
            return null;
        return SlidInstance.t;
    }

    public void recyle() {
        mWindow = null;
        mContainer = null;
        prepList.clear();
        currentList.clear();
    }


    public void put(SlidGift.GiftRecord record) {
        if (record == null)
            throw new ExceptionInInitializerError("without gift");
        prepList.offer(record);
        synchronized (currentList) {
            if (currentList.isEmpty()) {
                SlidGift g = newSlidGift(prepList.poll());
                currentList.add(g);
                g.relationView(mContainer, getLayoutParams(0));
            } else {
                int index = isRunning(record.gid);
                if (index == -1) {
                    if (currentList.size() == 1) {
                        SlidGift g = newSlidGift(prepList.poll());
                        currentList.add(g);

                        if (currentList.get(0) != null && currentList.get(0).getRules()[RelativeLayout.ALIGN_PARENT_BOTTOM] == RelativeLayout.TRUE) {
                            g.relationView(mContainer, getLayoutParams(0));
                        } else {
                            g.relationView(mContainer, getLayoutParams(1));
                        }
                    }
                } else {
                    SlidGift gift = currentList.get(index);
                    gift.plusNumber(record.giftNumber);
                    prepList.poll();
                }
            }
        }
    }


    private RelativeLayout.LayoutParams getLayoutParams(int index) {
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                , RelativeLayout.LayoutParams.WRAP_CONTENT);
        rp.addRule(index == 0 ? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.ALIGN_PARENT_BOTTOM);
        return rp;
    }

    private int isRunning(@NonNull String gid) {
        for (int i = 0; i < currentList.size(); i++) {
            SlidGift gift = currentList.get(i);
            if (gift == null)
                break;
            if (gid.equals(gift.getGift().gid)) {
                return i;
            }
        }
        return -1;
    }

    private SlidGift newSlidGift(@NonNull SlidGift.GiftRecord record) {
        if(record == null){
            return null;
        }
        SlidGift slid = new SlidGift(mWindow.getContext(), record);
        slid.setOnAnimaListener(getEndListener());
        return slid;
    }

    private SlidGift.OnAnimatorEndListener getEndListener() {
        return new SlidGift.OnAnimatorEndListener() {
            @Override
            public void onAnimatorEnd(@NonNull SlidGift entity) {
                synchronized (currentList) {
                    currentList.remove(entity);
                    switch (currentList.size()) {
                        case 0:
                            SlidGift g1 = newSlidGift(prepList.poll());
                            SlidGift g2 = newSlidGift(prepList.poll());
                            if (g1 != null) {
                                currentList.add(g1);
                                g1.relationView(mContainer, getLayoutParams(0));
                            }
                            if (g2 != null) {
                                currentList.add(g2);
                                g2.relationView(mContainer, getLayoutParams(1));
                            }
                            break;
                        case 1:
                            SlidGift g = newSlidGift(prepList.poll());
                            if (g != null) {
                                currentList.add(g);
                                if (currentList.get(0) != null && currentList.get(0).getRules()[RelativeLayout.ALIGN_PARENT_BOTTOM] == RelativeLayout.TRUE) {
                                    g.relationView(mContainer, getLayoutParams(0));
                                } else {
                                    g.relationView(mContainer, getLayoutParams(1));
                                }
                            }
                            break;
                    }
                }
            }
        };
    }
}
