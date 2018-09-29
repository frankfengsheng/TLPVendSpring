package com.tcn.uicommon.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

public class AutoScrollTextView extends TextView implements OnClickListener {

	private float textLength = 0f;//文本长度
	private float viewWidth = 0f;
	private float step = 0f;//文字的横坐标
	private float y = 0f;//文字的纵坐标
	private float temp_view_plus_text_length = 0.0f;//用于计算的临时变量
	private float temp_view_plus_two_text_length = 0.0f;//用于计算的临时变量
	private boolean isStarting = false;//是否开始滚动
	private Paint paint = null;//绘图样式
	private String text = "";//文本内容


	public AutoScrollTextView(Context context) {
		super(context);
		initView();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	@Override
	public void onClick(View v) {
		if(isStarting) {
			stopScroll();
		} else {
			startScroll();
		}

	}


	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.step = step;
		ss.isStarting = isStarting;

		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState)state;
		super.onRestoreInstanceState(ss.getSuperState());

		step = ss.step;
		isStarting = ss.isStarting;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (null == paint) {
			return;
		}
		canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
		if(!isStarting)
		{
			return;
		}
		step += 1.0;//0.5为文字滚动速度。
		if(step > temp_view_plus_two_text_length)
			step = textLength;
		invalidate();
	}

	private void initView() {
		setOnClickListener(this);
	}

	public void init(WindowManager windowManager) {
		paint = getPaint();
		paint.setColor(Color.WHITE);
		paint.setTypeface(Typeface.SANS_SERIF);
		text = getText().toString();
		textLength = paint.measureText(text);
		viewWidth = getWidth();
		if(viewWidth == 0) {
			if(windowManager != null) {
				Display display = windowManager.getDefaultDisplay();
				viewWidth = display.getWidth();
			}
		}
		step = textLength;
		temp_view_plus_text_length = viewWidth + textLength;
		temp_view_plus_two_text_length = viewWidth + textLength * 2;
		y = getTextSize() + getPaddingTop();
	}

	public void startScroll() {
		isStarting = true;
		invalidate();
	}

	public void stopScroll() {
		isStarting = false;
		invalidate();
	}

	public boolean isStarting() {
		return isStarting;
	}

	public static class SavedState extends BaseSavedState {

		public boolean isStarting = false;
		public float step = 0.0f;



		public SavedState(Parcelable arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			super.writeToParcel(dest, flags);

			dest.writeBooleanArray(new boolean[]{isStarting});
			dest.writeFloat(step);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}

			@Override
			public SavedState createFromParcel(Parcel source) {
				return new SavedState(source);
			}

		};

		private SavedState(Parcel in) {
			super(in);
			boolean[] b = null;
			in.readBooleanArray(b);
			if(b != null && b.length > 0) {
				isStarting = b[0];
			}
			step = in.readFloat();
		}

	}

}
