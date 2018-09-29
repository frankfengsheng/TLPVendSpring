package com.tcn.uicommon.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Scroller;

import com.bumptech.glide.Glide;
import com.tcn.uicommon.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;



public class AutoScrollViewPager extends ViewPager {

    public static final int        DEFAULT_INTERVAL            = 1500;

    public static final int        LEFT                        = 0;
    public static final int        RIGHT                       = 1;

    public static final int        SLIDE_BORDER_MODE_NONE      = 0;

    public static final int        SLIDE_BORDER_MODE_CYCLE     = 1;

    public static final int        SLIDE_BORDER_MODE_TO_PARENT = 2;


    private long                   interval                    = DEFAULT_INTERVAL;

    private int                    direction                   = RIGHT;

    private boolean                isCycle                     = true;

    private boolean                stopScrollWhenTouch         = true;

    private int                    slideBorderMode             = SLIDE_BORDER_MODE_NONE;

    private boolean                isBorderAnimation           = true;

    private double                 autoScrollFactor            = 1.0;

    private double                 swipeScrollFactor           = 1.0;

    private Handler                handler;
    private boolean                isAutoScroll                = false;
    private boolean                isStopByTouch               = false;
    private float                  touchX                      = 0f, downX = 0f;
    private CustomDurationScroller scroller                    = null;

    public static final int        SCROLL_WHAT                 = 0;
    public         String          mPageTransFormer    =  null ;

    public AutoScrollViewPager(Context paramContext) {
        super(paramContext);
        init();
    }

    public AutoScrollViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init() {
        handler = new MyHandler(this);
        setViewPagerScroller();
    }


    public void startAutoScroll() {
        if (scroller != null) {
            isAutoScroll = true;
            sendScrollMessage((long)(interval + scroller.getDuration() / autoScrollFactor * swipeScrollFactor));
        }
    }

    public void startAutoScroll(int delayTimeInMills) {
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }


    public void stopAutoScroll() {
        isAutoScroll = false;
        if (handler != null) {
            handler.removeMessages(SCROLL_WHAT);
            handler.removeCallbacksAndMessages(null);
        }
        scroller = null;
    }

    public void setSwipeScrollDurationFactor(double scrollFactor) {
        swipeScrollFactor = scrollFactor;
    }

    public void setAutoScrollDurationFactor(double scrollFactor) {
        autoScrollFactor = scrollFactor;
    }

    private void sendScrollMessage(long delayTimeInMills) {
        if (handler != null) {
            handler.removeMessages(SCROLL_WHAT);
            handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
        }
    }


    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            scroller = new CustomDurationScroller(getContext(), (Interpolator)interpolatorField.get(null));
            scrollerField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int totalCount;
        if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
            return;
        }

        int nextItem = (direction == LEFT) ? --currentItem : ++currentItem;
        if (nextItem < 0) {
            if (isCycle) {
                setCurrentItem(totalCount - 1, isBorderAnimation);
            }
        } else if (nextItem >= totalCount) {
            if (isCycle) {
                setCurrentItem(0, false);
            }
        } else {
            setCurrentItem(nextItem, isBorderAnimation);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);

        if (stopScrollWhenTouch) {
            if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
                isStopByTouch = true;
                stopAutoScroll();
            } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
                startAutoScroll();
            }
        }

        if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT || slideBorderMode == SLIDE_BORDER_MODE_CYCLE) {
            touchX = ev.getX();
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                downX = touchX;
            }
            int currentItem = getCurrentItem();
            PagerAdapter adapter = getAdapter();
            int pageCount = adapter == null ? 0 : adapter.getCount();

            if ((currentItem == 0 && downX <= touchX) || (currentItem == pageCount - 1 && downX >= touchX)) {
                if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (pageCount > 1) {
                        setCurrentItem(pageCount - currentItem - 1, isBorderAnimation);
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.dispatchTouchEvent(ev);
            }
        }
        getParent().requestDisallowInterceptTouchEvent(true);

        return super.dispatchTouchEvent(ev);
    }

    private static class MyHandler extends Handler {

        private final WeakReference<AutoScrollViewPager> autoScrollViewPager;

        public MyHandler(AutoScrollViewPager autoScrollViewPager) {
            this.autoScrollViewPager = new WeakReference<AutoScrollViewPager>(autoScrollViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SCROLL_WHAT:
                    AutoScrollViewPager pager = this.autoScrollViewPager.get();
                    if (pager != null) {
                        pager.scroller.setScrollDurationFactor(pager.autoScrollFactor);
                        pager.scrollOnce();
                        pager.scroller.setScrollDurationFactor(pager.swipeScrollFactor);
                        pager.sendScrollMessage(pager.interval + pager.scroller.getDuration());
                    }
                default:
                    break;
            }
        }
    }


    public long getInterval() {
        return interval;
    }


    public void setInterval(long interval) {
        this.interval = interval;
    }


    public int getDirection() {
        return (direction == LEFT) ? LEFT : RIGHT;
    }


    public void setDirection(int direction) {
        this.direction = direction;
    }


    public boolean isCycle() {
        return isCycle;
    }


    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }


    public boolean isStopScrollWhenTouch() {
        return stopScrollWhenTouch;
    }


    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.stopScrollWhenTouch = stopScrollWhenTouch;
    }


    public int getSlideBorderMode() {
        return slideBorderMode;
    }


    public void setSlideBorderMode(int slideBorderMode) {
        this.slideBorderMode = slideBorderMode;
    }


    public boolean isBorderAnimation() {
        return isBorderAnimation;
    }


    public void setBorderAnimation(boolean isBorderAnimation) {
        this.isBorderAnimation = isBorderAnimation;
    }

    public void configMode(List<String> views,RadioGroup radioGroup,double scrollDuration,int slideBorderMode){
        if(views != null){
            setOffscreenPageLimit(4);
            setSlideBorderMode(slideBorderMode);
            setAdapter(new MPagerAdapter(views));
            setAutoScrollDurationFactor(scrollDuration);
            setInterval(10000);
           setOnPageChangeListener( new MyOnPageChangeListener(radioGroup));
         }else {
           throw new NullPointerException();
         }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    //mmm
    public void setDepthTransFormer(){
        setPageTransformer(true, new DepthPageTransformer());
        mPageTransFormer = "depth";
    }

    public void setZoomOutTransFormer(){
        setPageTransformer(true, new ZoomOutPageTransformer());
        mPageTransFormer = "zoomout";
    }

    public void setNoTransFormer(){
        setPageTransformer(false, null);
        mPageTransFormer = "none";
    }

    //mmm
    public void close(){
        handler.removeCallbacksAndMessages(null);
        handler = null;//清除handler 避免内存泄漏
    }

    private class MPagerAdapter extends PagerAdapter{
       // private List < ?extends View>views;
        private List <String> m_pathList;
        private ImageView mCacheView = null;
        MPagerAdapter(List<String> views){
            //this.views = views;
            this.m_pathList = views;
        }
        @Override
        public int getCount() {
            return m_pathList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= m_pathList.size();

            if (position < 0) {
                position = m_pathList.size()+position;
            }

           // View view = views.get(position);
            String path = m_pathList.get(position);

            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。

           /* ViewParent vp =view.getParent();

            if (vp!=null){

                ViewGroup parent = (ViewGroup)vp;

                parent.removeView(view);

            }*/
            ImageView mView = null;
            if (null == mCacheView) {
                mView = new ImageView(getContext());
            } else {
                mView = mCacheView;
            }
            //mView.setImageURI(Uri.parse(path));
            Glide.with(getContext()).load(path).into(mView);
            container.addView(mView);
            mCacheView = null;
            return mView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

          //  container.removeView(views.get(position));
            container.removeView((View)object);
            mCacheView = (ImageView)object;
        }
    }
    /**淡入淡出效果*/
    private class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) {

                view.setAlpha(0);
            } else if (position <= 0) {

                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) {

                view.setAlpha(1 - position);

                view.setTranslationX(pageWidth * -position);

                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else {

                view.setAlpha(0);
            }
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer
    {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @SuppressLint("NewApi")
        public void transformPage(View view, float position)
        {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            Log.e("TAG", view + " , " + position + "");

            if (position < -1)
            {
                view.setAlpha(0);

            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0)
                {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else
                {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }


                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);


                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else
            {
                view.setAlpha(0);
            }
        }
    }


    private class CustomDurationScroller extends Scroller {

        private double scrollFactor = 1;

        public CustomDurationScroller(Context context) {
            super(context);
        }

        public CustomDurationScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }


        public void setScrollDurationFactor(double scrollFactor) {
            this.scrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int)(duration * scrollFactor));
        }
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {
        private RadioGroup radioGroup;
        private int mCount = 0;

        public MyOnPageChangeListener(RadioGroup radioGroup){
            if (null == radioGroup) {
                return;
            }
            this.radioGroup = radioGroup;
            mCount = radioGroup.getChildCount();
            if (mCount > 0) {
                radioGroup.check(0);
                if (radioGroup.getChildAt(0) != null) {
                    radioGroup.getChildAt(0).setBackgroundResource(R.mipmap.page_indicator_focused);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (null == radioGroup) {
                return;
            }
            if (mCount > 1) {
                if (0 == position) {
                    radioGroup.getChildAt(mCount-1).setBackgroundResource(R.mipmap.page_indicator_unfocused);
                } else if ((mCount-1) == position) {
                    radioGroup.getChildAt(0).setBackgroundResource(R.mipmap.page_indicator_unfocused);
                } else {

                }

                if (position+1 < mCount) {
                    radioGroup.getChildAt(position+1).setBackgroundResource(R.mipmap.page_indicator_unfocused);
                }

                if (position-1 >= 0) {
                    radioGroup.getChildAt(position-1).setBackgroundResource(R.mipmap.page_indicator_unfocused);
                }
            }
            radioGroup.check(position);
            radioGroup.getChildAt(position).setBackgroundResource(R.mipmap.page_indicator_focused);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }
}
