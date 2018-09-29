package com.tcn.uicommon.recycleview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Song Jiancheng on 2016/5/14.
 * <p>
 * 重写GridLayoutManager，在{@link RecyclerView#setLayoutManager(RecyclerView.LayoutManager)}使用
 * 此类替换{@link GridLayoutManager}，使{@link RecyclerView}能够自使用内容的高度
 * </p>
 */
public class AutoGridLayoutManager extends GridLayoutManager {

    private int measuredWidth = 0;
    private int measuredHeight = 0;
    private int mOrientation = AutoGridLayoutManager.HORIZONTAL;

    public AutoGridLayoutManager(Context context, AttributeSet attrs,
                                 int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AutoGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public AutoGridLayoutManager(Context context, int spanCount,
                                 int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        mOrientation = orientation;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler,
                          RecyclerView.State state, int widthSpec, int heightSpec) {
        try {
            if (measuredWidth <= 0) {
                View view = recycler.getViewForPosition(0);
                if (view != null) {
                    measureChild(view, widthSpec, heightSpec);
                    if (AutoGridLayoutManager.VERTICAL == mOrientation) {
                        measuredWidth = view.getMeasuredWidth() * getSpanCount();
                        measuredHeight = View.MeasureSpec.getSize(heightSpec);
                    } else {
                        measuredWidth = View.MeasureSpec.getSize(widthSpec);
                        measuredHeight = view.getMeasuredHeight() * getSpanCount();
                    }

                }
            }
        } catch (Exception e) {

        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

}
