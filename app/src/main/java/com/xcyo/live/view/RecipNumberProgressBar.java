package com.xcyo.live.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xcyo.live.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by TDJ on 2016/6/15.
 */
public class RecipNumberProgressBar extends View {

    public RecipNumberProgressBar(Context context) {
        this(context, null);
    }

    public RecipNumberProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecipNumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.TRANSPARENT);
        initAttrs(context, attrs);
    }

    private int innerColor;//内环颜色
    private int radius;//半径

    private int outerColor;//外环颜色
    private int outerSize;//外环大小

    private int progressbarColor; //进度条颜色;

    private int maxProgress;//最大的尺步

    private String title;//提示
    private int mTitleSize;//提示字体大小

    private int timer;//剩余时间
    private int mTimerSize;//剩余时间字体大小

    private STATE stat = STATE.READY;
    private Handler mHandler = new Handler();
    private ExecutorService exeut = Executors.newSingleThreadExecutor();

    private OnEchoTimerTask echo;


    {
        innerColor = Color.TRANSPARENT;
        radius = dp2px(getContext(), 30);

        outerColor = 0x8fffffff;
        outerSize = dp2px(getContext(), 4);

        progressbarColor = Color.RED;
        maxProgress = 100;

        timer = maxProgress *3/5;
        mTimerSize = dp2px(getContext(), 15);
    }

    private void initAttrs(Context context, AttributeSet attr){
        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.recip);
        if(a != null){
            innerColor = a.getColor(R.styleable.recip_innerColor, Color.TRANSPARENT);
            outerColor = a.getColor(R.styleable.recip_outerColor, 0x8fffffff);
            outerSize = a.getDimensionPixelSize(R.styleable.recip_outerSize, dp2px(context, 4));
            progressbarColor = a.getColor(R.styleable.recip_progressbarColor, Color.RED);
            maxProgress = a.getInteger(R.styleable.recip_maxProgress, 100);
            mTimerSize = a.getDimensionPixelSize(R.styleable.recip_timerSize, dp2px(context, 15));
            title = a.getString(R.styleable.recip_tiptitle);
            mTitleSize = a.getDimensionPixelSize(R.styleable.recip_titleSize, dp2px(context, 18));
        }
        a.recycle();
    }


    public void setInnerColor(int innerColor) {
        this.innerColor = innerColor;
    }

    public void setOuterColor(int outerColor) {
        this.outerColor = outerColor;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleSize(int size){
        this.mTitleSize = size;
    }

    public void setmTimerSize(int mTimerSize) {
        this.mTimerSize = mTimerSize;
    }

    public void setOuterSize(int outerSize) {
        this.outerSize = outerSize;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setProgressbarColor(int progressbarColor) {
        this.progressbarColor = progressbarColor;
    }

    public STATE getStat() {
        return stat;
    }

    public void setEcho(OnEchoTimerTask echo) {
        this.echo = echo;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        radius =  - outerSize + getMeasuredWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int scaleX = getWidth() / 2;
        int scaleY = getHeight() / 2;

        Paint mPaint = new Paint();
        //画外环
        mPaint.setAntiAlias(true);
        mPaint.setColor(outerColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outerSize);
        canvas.drawCircle(scaleX, scaleY, radius + outerSize / 2, mPaint);

        //画内环
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(innerColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(scaleX, scaleY, radius, mPaint);

        //画外hu
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(progressbarColor);
        mPaint.setStrokeWidth(outerSize);
        mPaint.setStyle(Paint.Style.STROKE);
        RectF rect = new RectF(scaleX-radius-outerSize/2,scaleY-radius-outerSize/2,scaleX+radius+outerSize/2, scaleY+radius+outerSize/2);
        canvas.drawArc(rect, 270, ((timer * 360) / maxProgress), false, mPaint);


        if(!TextUtils.isEmpty(title)){
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(mTitleSize);
            mPaint.setColor(Color.WHITE);
            float length_t = mPaint.measureText(title);
            canvas.drawText(title, scaleX - length_t / 2, scaleY - dp2px(getContext(), 4), mPaint);

            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(mTimerSize);
            mPaint.setColor(Color.WHITE);
            float length_i = mPaint.measureText(String.valueOf(timer));
            canvas.drawText(String.valueOf(timer), scaleX - length_i / 2, scaleY + mTimerSize + dp2px(getContext(), 4), mPaint);
        }else {
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(mTimerSize);
            mPaint.setColor(Color.WHITE);
            float length_i = mPaint.measureText(String.valueOf(timer));
            canvas.drawText(String.valueOf(timer), scaleX - length_i / 2, scaleY + mTimerSize/2, mPaint);
        }

    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    public void start(){
        if(stat == STATE.READY || stat == STATE.STOP){
            exeut.execute(getRunnable());
        }
    }

    public void reset(){
        timer = maxProgress;
        if(stat == STATE.STOP){
            mHandler.post(getHandlerRunnable());
        }
    }

    public void stop(){
        stat = STATE.STOP;
    }

    private Runnable getHandlerRunnable(){
        return new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    public enum STATE{
        RUNNING, STOP, READY;
    }

    private Runnable getRunnable(){
        return new Runnable() {
            @Override
            public void run() {
                while(timer >= 0){
                    if(stat == STATE.READY){
                        stat = STATE.RUNNING;
                        echoStart();
                        mHandler.post(getHandlerRunnable());
                    }else if(stat == STATE.RUNNING){
                        timer = timer - 1;
                        echoRunning(timer);
                        if(timer == 0){
                            stat = STATE.STOP;
                        }else if(timer > 0){
                            mHandler.post(getHandlerRunnable());
                        }
                    }else if(stat == STATE.STOP){
                        echoStop();
                        stat = STATE.READY;
                        mHandler.post(getHandlerRunnable());
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public interface OnEchoTimerTask{
        public void onStart();
        public void onRunning(RecipNumberProgressBar.STATE stat, int max, int current);
        public void onStop();
    }

    private void echoStart(){
        if(echo != null)
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    echo.onStart();
                }
            });
    }

    private void echoStop(){
        if(echo != null)
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    echo.onStop();
                }
            });
    }

    private void echoRunning(final int timer){
        if(echo!= null)
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    echo.onRunning(stat, maxProgress, timer);
                }
            });

    }

}
