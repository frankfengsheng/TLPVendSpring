package com.tcn.uicommon.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

/**
 *解决ViewPager滑动过于灵敏，只有滑动距离大于100才滑到另一页
 *
 *@authorAdministrator
 *
 */
public class TcnViewPager extends ViewPager {


    private static final int MOVE_LIMITATION = 20;//触发移动的像素距离

    private int mCurScreen = 0;
    private int mScreenCount = 0;
    private float mLastMotionX;//手指触碰屏幕的最后一次x坐标
    private float preX = 0;
    private Scroller mScroller;//滑动控件

    public TcnViewPager(Context context) {
        super(context);
        init(context);
    }

    public TcnViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final float x = ev.getX();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(x-mLastMotionX) > MOVE_LIMITATION) {
                    if (mLastMotionX > x) {
                        //setCurrentItem(getCurrentItem() + 1, true);
                        snapToDestination(true);
                        //arrowScroll(2);
                    } else if (mLastMotionX < x) {
                        snapToDestination(false);
                    } else {

                    }
                    //snapToDestination(true);//跳到指定页
                    //snapToScreen(getCurrentItem());
                    //return true;
                }
                break;
            default:
                break;
        }
        // TODO Auto-generated method stub
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        final float x = ev.getX();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        if(action == MotionEvent.ACTION_DOWN) {
            preX = ev.getX();
            Log.e("test", "Math preX: "+preX);
        } else {
            if(Math.abs(ev.getX() - preX) > MOVE_LIMITATION) {
                return true;
            } else {
                preX = ev.getX();
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void init(Context context) {
        //controlViewPagerSpeed();
        mScroller = new Scroller(context);
        mCurScreen = 0;//默认设置显示第一个VIEW
        mScreenCount = 0;
    }

    public void setPageCount(int count) {
        mScreenCount = count;
    }

    /**
     *根据滑动的距离判断移动到第几个视图
     */
    public void snapToDestination(boolean right) {
        //final int screenWidth = getWidth();
        //final int destScreen=(getScrollX()+4*screenWidth/5)/screenWidth;
        //Log.e("test", "snapToDestination right: "+right+" getCurrentItem(): "+getCurrentItem()+" mScreenCount: "+mScreenCount+" getScrollX(): "+getScrollX());
        int destScreen = 0;
        if (right) {
            //destScreen = (getScrollX()+955)/screenWidth;
            destScreen = getCurrentItem() + 1;
        } else {
            //destScreen = (getScrollX()-5)/screenWidth;
            destScreen = getCurrentItem() - 1;
        }

        if (right) {
            if (destScreen  == mScreenCount) {
                this.post(new Runnable() {

                    @Override
                    public void run() {
                        setCurrentItem(0, false);
                    }

                });

            } else {
                //snapToScreen(destScreen);
            }
        } else {
            if ((getScrollX() == 0) && (0 == getCurrentItem())) {
                this.post(new Runnable() {

                    @Override
                    public void run() {
                        setCurrentItem(mScreenCount-1, false);
                    }

                });

            } else {
                //snapToScreen(destScreen);
            }
        }
    }

    public void snapToDestination222() {
        mScroller.startScroll(getScrollX(),0,960,0,2000);
        //mScroller.startScroll(getScrollX(),0,delta,0,2000);
        invalidate();
    }
    /**
     *滚动到制定的视图
     *
     *@paramwhichScreen
     *视图下标
     */
    public void snapToScreen(int whichScreen) {
        //whichScreen=Math.max(0,Math.min(whichScreen,getChildCount()-
        //1));
        if(getScrollX() != (whichScreen*getWidth())) {
            final int delta = whichScreen*getWidth()-getScrollX();
            mScroller.startScroll(getScrollX(),0,delta,0,Math.abs(delta)*2);
            //mScroller.startScroll(getScrollX(),0,delta,0,2000);
            mCurScreen = whichScreen;
            //setCurrentItem(whichScreen + 1, true);
            invalidate();
            if (mCurScreen != getCurrentItem()) {
                this.post(new Runnable() {

                    @Override
                    public void run() {
                        setCurrentItem(mCurScreen, true);
                    }
                });
            }

        }
    }

}
