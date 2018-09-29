package com.tcn.uicommon.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class TcnGridViewInterceptTouch extends GridView {

	public TcnGridViewInterceptTouch(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public TcnGridViewInterceptTouch(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public TcnGridViewInterceptTouch(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
		// TODO Auto-generated method stub
		//return super.onInterceptTouchEvent(ev);
	}

	
}
