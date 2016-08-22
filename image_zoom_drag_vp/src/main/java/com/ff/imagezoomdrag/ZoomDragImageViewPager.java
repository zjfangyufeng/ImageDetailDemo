package com.ff.imagezoomdrag;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Field;

public class ZoomDragImageViewPager extends ViewPager {
	ImageDetailActivity.MyPagerAdapter adapter;

	public ZoomDragImageViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		super.setAdapter(adapter);
		this.adapter = (ImageDetailActivity.MyPagerAdapter) adapter;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		ImageDetailActivity.ViewHolder tag = getCurrentViewHolder();
		boolean consume = tag.content_iv.dragAndZoomTouchListener.onTouch(ev);
		if(!consume || ev.getAction() != MotionEvent.ACTION_MOVE ){
			super.onTouchEvent(ev);
		} else {
			resetLastMotionX(ev.getX());
		}
		return true;
	}

	private void resetLastMotionX(float x) {
		try {
			Field mLastMotionX = ViewPager.class.getDeclaredField("mLastMotionX");
			mLastMotionX.setAccessible(true);
			mLastMotionX.set(this,x);

			Field mInitialMotionX = ViewPager.class.getDeclaredField("mInitialMotionX");
			mInitialMotionX.setAccessible(true);
			mInitialMotionX.set(this,x);
		} catch (Exception e) {
		}
	}

	public void resetImageMatrix(){
		ImageDetailActivity.ViewHolder tag = getCurrentViewHolder();
		tag.content_iv.resetImageMatrix();
	}

	public ImageDetailActivity.ViewHolder getCurrentViewHolder(){
		View item = adapter.getItem(getCurrentItem());
		ImageDetailActivity.ViewHolder tag = (ImageDetailActivity.ViewHolder) item.getTag();
		return tag;
	}

}
