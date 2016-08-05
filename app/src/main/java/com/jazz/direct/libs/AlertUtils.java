package com.jazz.direct.libs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by TDJ on 2016/5/31.
 */
public class AlertUtils {

    private static final String TAG = "[direct_logs]";

    private static final int COLOR_DIRVER = 0xD3D3D3D3;

    public synchronized static final void showTips(@NonNull android.content.Context context, String msg){
        final LinearLayout convertView = createTipsViews(context);
        android.util.Log.d(TAG, msg + "");
        convertView.addView(createTipsTitle(context, "提 醒"));
        convertView.addView(createTipSingleLine(context, LinearLayout.LayoutParams.MATCH_PARENT, 1, new int[]{DensityUtils.dp2px(context, 3), 0, DensityUtils.dp2px(context, 3), 0}));
        TextView tw = createTipsContentView(context);
        tw.setText(msg);
        convertView.addView(tw);
        convertView.addView(createTipSingleLine(context, LinearLayout.LayoutParams.MATCH_PARENT, 1, new int[]{DensityUtils.dp2px(context, 3), 0, DensityUtils.dp2px(context, 3), 0}));
        TextView btn = createTipsButton(context, "知 道 了", null);
        convertView.addView(btn);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(convertView);
        WindowManager.LayoutParams ws = dialog.getWindow().getAttributes();
        ws.width = ConsUtils.getDisplayMetrics(context)[0] * 7 / 10;
        ws.alpha = 1.0f;
        ws.dimAmount = 0.1f;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public synchronized static final void showTipsResult(@NonNull android.content.Context context, String title, String msg, final Callback ck){
        final LinearLayout convertView = createTipsViews(context);
        android.util.Log.d(TAG, msg + "");
        convertView.addView(createTipsTitle(context, title == null ? "提 醒" : title));
        convertView.addView(createTipSingleLine(context, LinearLayout.LayoutParams.MATCH_PARENT, 1, new int[]{DensityUtils.dp2px(context, 3), 0, DensityUtils.dp2px(context, 3), 0}));
        TextView tw = createTipsContentView(context);
        tw.setText(msg);
        convertView.addView(tw);
        convertView.addView(createTipSingleLine(context, LinearLayout.LayoutParams.MATCH_PARENT, 1, new int[]{DensityUtils.dp2px(context, 3), 0, DensityUtils.dp2px(context, 3), 0}));

        LinearLayout bv = new LinearLayout(context);
        bv.setOrientation(LinearLayout.HORIZONTAL);
        TextView l = createTipsButton(context, "取消" , new float[]{
                0, 0, 0, 0, 0, 0, DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f)
        });

        TextView r = createTipsButton(context, "确定" , new float[]{
                0, 0, 0, 0, DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f), 0, 0
        });
        ((LinearLayout.LayoutParams)l.getLayoutParams()).weight = 1;
        ((LinearLayout.LayoutParams)r.getLayoutParams()).weight = 1;
        bv.addView(l);
        bv.addView(createTipSingleLine(context, 1, LinearLayout.LayoutParams.MATCH_PARENT, new int[]{0, 3, 0, 3}));
        bv.addView(r);
        convertView.addView(bv);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(convertView);
        WindowManager.LayoutParams ws = dialog.getWindow().getAttributes();
        ws.width = ConsUtils.getDisplayMetrics(context)[0] * 7 / 10;
        ws.alpha = 1.0f;
        ws.dimAmount = 0.1f;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ck != null) {
                    ck.l(dialog);
                    return ;
                }
                dialog.dismiss();
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ck != null) {
                    ck.r(dialog);
                    return ;
                }
                dialog.dismiss();
            }
        });
    }

    private static final LinearLayout createTipsViews(Context context){
        LinearLayout rt = new LinearLayout(context);
        int radii = DensityUtils.dp2px(context, 5f);
        rt.setBackgroundDrawable(createShapeDrawable(context, Color.WHITE, new float[]{
                radii,radii,radii,radii,radii,radii,radii,radii
        }));
        rt.setOrientation(LinearLayout.VERTICAL);
        return rt;
    }

    private static final TextView createTipsTitle(Context context, @NonNull String t){
        TextView title = new TextView(context);
        title.setSingleLine(true);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(ll);
        title.setText(t);
        title.setTextColor(Color.DKGRAY);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(DensityUtils.px2sp(context, DensityUtils.dp2px(context, 13)));
        title.setPadding(DensityUtils.dp2px(context, 26), DensityUtils.dp2px(context, 11), DensityUtils.dp2px(context, 26), DensityUtils.dp2px(context, 11));
        float[] radii = new float[]{DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f)
                ,0, 0, 0, 0};
        title.setBackgroundDrawable(createShapeDrawable(context, Color.WHITE, radii));
        return title;
    }

    private static final Drawable createShapeDrawable(Context context, int color, float[] radii){
        GradientDrawable bd = new GradientDrawable();
        bd.setGradientType(GradientDrawable.RECTANGLE);
        bd.setColor(color);
        bd.setCornerRadii(radii);
        return bd;
    }

    private static final View createTipSingleLine(Context context, int width, int height, int[] margin){
        View view = new View(context);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.width = width;
        ll.height = height;
        if(margin != null && margin.length == 1){
            ll.leftMargin = margin[0]; ll.topMargin = margin[0]; ll.rightMargin = margin[0]; ll.bottomMargin = margin[0];
        }else if(margin != null && margin.length == 4){
            ll.leftMargin = margin[0]; ll.topMargin = margin[1]; ll.rightMargin = margin[2]; ll.bottomMargin = margin[3];
        }
        view.setLayoutParams(ll);
        view.setBackgroundColor(COLOR_DIRVER);
        return view;
    }

    private static final TextView createTipsContentView(Context context){
        TextView tw = new TextView(context);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        tw.setLayoutParams(ll);
        tw.setMinHeight(DensityUtils.dp2px(context, 58));
        tw.setGravity(Gravity.CENTER);
        tw.setTextColor(Color.GRAY);
        tw.setTextSize(DensityUtils.px2sp(context, DensityUtils.dp2px(context, 11)));
        tw.setBackgroundColor(Color.WHITE);
        return tw;
    }

    private static final TextView createTipsButton(Context context, String text, float[] radii){
        TextView btn = new TextView(context);
        btn.setSingleLine(true);
        btn.setText(text);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.height = DensityUtils.dp2px(context, 42);
        btn.setLayoutParams(ll);
        btn.setGravity(Gravity.CENTER);
        btn.setTextColor(Color.DKGRAY);
        btn.setTextSize(DensityUtils.px2sp(context, DensityUtils.dp2px(context, 13)));
        radii = radii == null ? new float[]{0, 0, 0, 0, DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f), DensityUtils.dp2px(context, 5f)} : radii;
        btn.setBackgroundDrawable(createShapeDrawable(context, Color.WHITE, radii));
        return btn;
    }

    public interface Callback{
        public void l(Dialog d);
        public void r(Dialog d);
    }
}
