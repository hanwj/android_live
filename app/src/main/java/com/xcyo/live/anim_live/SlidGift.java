package com.xcyo.live.anim_live;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcyo.live.R;

public class SlidGift {

	public static final class GiftRecord{
		public String gid;
		public String name;
		public String uid;
		public String level;
		public String usrIcon;
		public String disc;
		
		public String giftImg;
		public String giftNumber;
	}
	
	public enum State{
		START, RUNNING,  FINISH;
	}
	
	
	private static final int TIME_PARENT_TRANSLATION = 250;
	
	
	private Context mContext;
	private GiftRecord mEntity;
	private RelativeLayout parent;
	private View mStackView;

	private int[] rules;
	
	private RelativeLayout mUsr_Layer;
	
	private ImageView mUsrIcon;
	private TextView mUsrName;
	private TextView mUsrLevel;
	private TextView mUsrDisc;
	
	private ImageView mGiftImg;
	private TextView mGiftNumber;
	
	private Animator appearAnim;
	private Animator scalAnim;
	private Animator disappearAnim;
	
	private State mState = State.START;
	
	public State getState() {
		return mState;
	}
	
	private final Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mStackView.setVisibility(View.VISIBLE);
				if(appearAnim == null)
					appearAnim = start();
				if(mState == State.START){
					appearAnim.start();
					mState = State.RUNNING;
				}
				break;
			case 2:
				if(scalAnim == null){
					scalAnim = scalText();
				}
				if(disappearAnim == null && mState == State.FINISH){
					return ;
				}
				if(disappearAnim != null && disappearAnim.isRunning()){
					return ;
				}
				if(disappearAnim != null && disappearAnim.isStarted()){
					disappearAnim.cancel();
				}
				if(scalAnim.isRunning()){
					scalAnim.cancel();
				}
				scalAnim.start();
				break;
			case 3:
				if(disappearAnim == null){
					disappearAnim = disappear();
				}else{

				}
				disappearAnim.setStartDelay(TIME_PARENT_TRANSLATION*4);
				disappearAnim.start();
				break;
			case -1:
				disappearAnim = null;
				appearAnim = null;
				scalAnim = null;
				ViewGroup group = (ViewGroup)mStackView.getParent();
				if(group != null){
					group.removeView(mStackView);
				}
				break;
			default:
				break;
			}
		};
	};
	
	private OnAnimatorEndListener mEnd;
	
	public void setOnAnimaListener(OnAnimatorEndListener listener){
		mEnd = listener;
	}
	
	
	public SlidGift(@NonNull Context context){
		this(context, null);
	}
	
	public SlidGift(@NonNull Context context, GiftRecord entity){
		this(context, entity, null);
	}
	
	public SlidGift(@NonNull Context context, GiftRecord entity, RelativeLayout parent){
		this.mContext = context;
		this.mEntity = entity;
		this.parent = parent;
		mStackView = initView();
		relationView(parent, null);
		fillGiftItem(entity);
	}
	
	private View initView(){
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		View convertView = mInflater.inflate(R.layout.item_gift_small, null);
		this.mUsr_Layer = (RelativeLayout) convertView.findViewById(R.id.gift_layer);
		this.mUsrIcon = (ImageView)convertView.findViewById(R.id.gift_usr_icon);
		this.mUsrName = (TextView) convertView.findViewById(R.id.gift_name);
		this.mUsrLevel = (TextView) convertView.findViewById(R.id.gift_level);
		this.mUsrDisc = (TextView) convertView.findViewById(R.id.gift_dis);
		
		this.mGiftImg = (ImageView) convertView.findViewById(R.id.gift_img);
		this.mGiftNumber = (TextView) convertView.findViewById(R.id.gift_number);
		return convertView;
	}
	
	public View getView(){
		return mStackView;
	}

	public GiftRecord getGift() {
		return mEntity == null ? new GiftRecord() : mEntity;
	}

	public int[] getRules() {
		return rules;
	}

	private void fillGiftItem(@NonNull GiftRecord entity){
		if(entity == null)
			return ;
		this.mUsrName.setText(switchEmpty(entity.name));
		this.mUsrLevel.setText(switchEmpty(entity.level));
		this.mUsrDisc.setText(switchEmpty(entity.disc));
	}
	
	private CharSequence switchEmpty(CharSequence c){
		if(TextUtils.isEmpty(c)){
			return "";
		}
		return c;
	}
	
	public void plusNumber(String number){
		if(this.mEntity == null){
			throw new ExceptionInInitializerError("no GiftEntity");
		}
		String content = mGiftNumber.getText().toString().trim();
		Matcher match = Pattern.compile("\\d+").matcher(content);
		if(match.find()){
			String ch = match.group();
			int num = new Integer(ch);
			if(number != null && number.matches("\\d+") && num > new Integer(ch)){
				this.mGiftNumber.setText("x" + number);
				this.mEntity.giftNumber = number;
			}else{
				this.mGiftNumber.setText("x" + (new Integer(ch) + 1));
				this.mEntity.giftNumber = (new Integer(ch) + 1)+"";
			}
		}
		this.mHandler.sendEmptyMessage(2);
	}
	
	public void relationView(@NonNull RelativeLayout vg, RelativeLayout.LayoutParams params){
		if(vg != null){
			mStackView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				@Override
				public void onGlobalLayout() {
					// TODO Auto-generated method stub
					mStackView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					mHandler.sendEmptyMessage(1);
				}
			});
			if(params == null){
				params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			}
			rules = params.getRules();
			mStackView.setVisibility(View.INVISIBLE);
			vg.addView(mStackView, params);
		}
	}
	
	/*
	 * һ���ĸ�����
	 * (1) layer ����߳��� ��100 ms, 0-100%��
	 * (2) img��text ����߳��� ��250ms, 0 - 100%��
	 * (3) number ���ӵ�ʱ��number��text (0 - 120 - 100 %)
	 * (4)3s�� ȫ�������ƶ�����ʧ������ı�number������ʱ������(3s,  1/2--height,)
	 */
	
	private Animator ofAnimators(View target, String name, float... values){
		return  ObjectAnimator.ofFloat(target, name, values);
	}
	
	private Animator start(){
		int distance = -mStackView.getPaddingLeft() - mStackView.getMeasuredWidth();
		Animator anim0 = ofAnimators(mStackView, "translationX", new float[]{distance, 0f});
		anim0.setDuration(TIME_PARENT_TRANSLATION);
		
		Animator anim1 = ofAnimators(mGiftImg, "translationX", new float[]{distance, 0f});
		anim1.setDuration((int)(TIME_PARENT_TRANSLATION*2.5));
		
		Animator anim2 = ofAnimators(mGiftNumber, "translationX", new float[]{distance, 0f});
		anim2.setDuration((int)(TIME_PARENT_TRANSLATION*2.5));
		
		AnimatorSet set = new AnimatorSet();
		set.addListener(generateAnimListener(1));
		set.playTogether(new Animator[]{anim0, anim1, anim2});
		return set;
	}
	
	private AnimatorListener generateAnimListener(final int tag){
		return new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				if(!animation.isRunning()){
					return ;
				}
				switch (tag) {
				case 1:
					mHandler.sendEmptyMessage(2);
					break;
				case 2:
					mHandler.sendEmptyMessage(3);
					break;
				case 3:
					mState = State.FINISH;
					mHandler.sendEmptyMessage(-1);
					if(mEnd != null){
						mEnd.onAnimatorEnd(SlidGift.this);
					}
					break;
				default:
					break;
				}
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {}
			
			@Override
			public void onAnimationCancel(Animator animation) {}
		};
	}
	
	private Animator scalText(){
		AnimatorSet set = new AnimatorSet();
		set.playTogether(new Animator[]{ofAnimators(mGiftNumber, "scaleX", new float[]{1.0f, 2.0f, 1.0f, 1.3f, 1.0f}), ofAnimators(mGiftNumber, "scaleY", new float[]{1.0f, 2.0f, 1.0f, 1.3f, 1.0f})});
		set.setDuration((int)(TIME_PARENT_TRANSLATION*3.5));
		set.addListener(generateAnimListener(2));
		return set;
	}
	
	private Animator disappear(){
		AnimatorSet set = new AnimatorSet();
		set.playTogether(new Animator[]{ofAnimators(mStackView, "translationY", new float[]{0.0f,  -176}), ofAnimators(mStackView, "alpha", new float[]{1.0f, 0.0f})});
		set.setDuration(TIME_PARENT_TRANSLATION*4);
		set.addListener(generateAnimListener(3));
		return set;
	}
	
	public interface OnAnimatorEndListener{
		public void onAnimatorEnd(SlidGift entity);
	}
	
}
