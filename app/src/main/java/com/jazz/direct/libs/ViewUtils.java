package com.jazz.direct.libs;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.content.Context;

/**
 * Created by TDJ on 2016/5/31.
 */
public class ViewUtils {

    public static final FrameLayout createDirectMainView(@NonNull Context context){
        FrameLayout frame = new FrameLayout(context);
        frame.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.MATCH_PARENT));
        return frame;
    }

    public static final RelativeLayout getFrameContainer(android.support.v4.app.FragmentActivity activity, View mChildView){
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rp.addRule(RelativeLayout.CENTER_IN_PARENT);
        if(mChildView != null){
            relativeLayout.addView(mChildView, rp);
        }
        return relativeLayout;
    }

    public static final RelativeLayout getControllerContainer(android.support.v4.app.FragmentActivity activity){
        return new RelativeLayout(activity);
    }

    public static final LinearLayout createControllerLinear(FragmentActivity activity){
        LinearLayout iir = new LinearLayout(activity);
        iir.setOrientation(LinearLayout.VERTICAL);
        return iir;
    }

    public static final Button createSwitchButton(android.support.v4.app.FragmentActivity activity){
        Button st = new Button(activity);
        st.setBackgroundDrawable(createButtonDrawable(activity));
        st.setPadding(DensityUtils.dp2px(activity, 12), DensityUtils.dp2px(activity, 6), DensityUtils.dp2px(activity, 12), DensityUtils.dp2px(activity, 6));
        st.setTextColor(Color.WHITE);
        st.setTextSize(DensityUtils.px2sp(activity, DensityUtils.dp2px(activity, 13f)));
        st.setSingleLine(true);
        return st;
    }

    private static final Drawable createButtonDrawable(Context context){
        StateListDrawable listdb = new StateListDrawable();
        float[] radii = new float[] { DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f),
                DensityUtils.dp2px(context, 5f),
                DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f) };

        GradientDrawable bd = new GradientDrawable();
        bd.setGradientType(GradientDrawable.RECTANGLE);
        bd.setColor(Color.DKGRAY);
        bd.setCornerRadii(radii);
        bd.setStroke(DensityUtils.dp2px(context, 0.5f), Color.BLACK);

        GradientDrawable bds = new GradientDrawable();
        bds.setGradientType(GradientDrawable.RECTANGLE);
        bds.setColor(Color.BLACK);
        bds.setCornerRadii(radii);

        listdb.addState(new int[] { android.R.attr.state_pressed }, bds);
        listdb.addState(new int[]{}, bd);
        return listdb;
    }
}
