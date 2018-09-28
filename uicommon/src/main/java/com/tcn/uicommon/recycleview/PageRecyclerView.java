package com.tcn.uicommon.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;


/**
 * Created by Song Jiancheng on 2016/5/14.
 * <p>
 * 横向分页的GridView效果
 * </p>
 * <p>
 * 默认为1行，每页3列，如果要自定义行数和列数，请在调用{@link PageRecyclerView#setAdapter(Adapter)}方法前调用
 * {@link PageRecyclerView#setPageSize(int, int)}方法自定义行数
 * </p>
 */
public class PageRecyclerView extends RecyclerView {
    private Context mContext = null;

    private PageAdapter myAdapter = null;

    private int shortestDistance; // 超过此距离的滑动才有效
    private float slideDistance = 0; // 滑动的距离
    private float scrollX = 0; // X轴当前的位置

    private static int spanRow = 1; // 行数
    private static int spanColumn = 3; // 每页列数

    private static int ROLLING_RIGHT = 0;
    private static int ROLLING_LEFT = 1;

    private int m_iRollingDirection = ROLLING_RIGHT;

    private int totalPage = 0; // 总页数
    private int currentPage = 1; // 当前页

    private static int pageMargin = 0; // 页间距

    private boolean isCustomized = false;
    private boolean isScrollEnd = false;
    private boolean isLayoutVertical = false;
    private boolean isCanScroll = true;

    private PageIndicatorView mIndicatorView = null; // 指示器布局

    //private int realPosition = 0; // 真正的位置（从上到下从左到右的排列方式变换成从左到右从上到下的排列方式后的位置）

    /*
	 * 0: 停止滚动且手指移开; 1: 开始滚动; 2: 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
	 */
    private int scrollState = 0; // 滚动状态

    public PageRecyclerView(Context context) {
        this(context, null);
    }

    public PageRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            if ("orientation".equals(attrs.getAttributeName(i))) {
                if ("1".equals(attrs.getAttributeValue(i))) {
                    isLayoutVertical = true;
                }
            }
        }
        defaultInit(context);
    }

    // 默认初始化
    private void defaultInit(Context context) {
        this.mContext = context;
        if (isLayoutVertical) {
            setLayoutManager(new AutoGridLayoutManager(
                    mContext, spanColumn, AutoGridLayoutManager.VERTICAL, false));
        } else {
            setLayoutManager(new AutoGridLayoutManager(
                    mContext, spanRow, AutoGridLayoutManager.HORIZONTAL, false));
        }

        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void destroyAdapter() {
        stopScrollView();
        clearAnimation();
        if (myAdapter != null) {
            myAdapter.removeCallBack();
        }
        myAdapter = null;
    }

    /**
     * 设置行数和每页列数
     *
     * @param spanRow    行数，<=0表示使用默认的行数
     * @param spanColumn 每页列数，<=0表示使用默认每页列数
     */
    public void setPageSize(int spanRow, int spanColumn) {
        this.spanRow = spanRow <= 0 ? this.spanRow : spanRow;
        this.spanColumn = spanColumn <= 0 ? this.spanColumn : spanColumn;

        if (isLayoutVertical) {
            setLayoutManager(new AutoGridLayoutManager(
                    mContext, this.spanColumn, AutoGridLayoutManager.VERTICAL, false));
        } else {
            setLayoutManager(new AutoGridLayoutManager(
                    mContext, this.spanRow, AutoGridLayoutManager.HORIZONTAL, false));
        }

    }

    public void setCustomized(boolean isCustom) {
        isCustomized = isCustom;
    }

    public void setLayoutVertical(boolean vertical) {
        isLayoutVertical = vertical;
    }

    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    public boolean isCanScroll() {
        return isCanScroll;
    }

    /**
     * 设置页间距
     *
     * @param pageMargin 间距(px)
     */
    public void setPageMargin(int pageMargin) {
        this.pageMargin = pageMargin;
    }

    /**
     * 设置指示器
     *
     * @param indicatorView 指示器布局
     */
    public void setIndicator(PageIndicatorView indicatorView) {
        this.mIndicatorView = indicatorView;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (isLayoutVertical) {
            shortestDistance = getMeasuredHeight() / 3;
        } else {
            shortestDistance = getMeasuredWidth() / 3;
        }

    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            this.myAdapter = (PageAdapter) adapter;
            update();
        }
    }

    private void scrollViewToPosition(int position) {
        scrollToPosition(position);
        post(new Runnable() {
            @Override
            public void run() {
                currentPage = getCurrentPage();
                if (currentPage < 0) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentPage = getCurrentPage();
                            handlePageChange(currentPage);
                        }
                    },500);
                } else {
                    handlePageChange(currentPage);
                }

            }
        });

    }

    // 更新页码指示器和相关数据
    private void update() {
        // 计算总页数
        int temp = ((int) Math.ceil(myAdapter.itemCount / (double) (spanRow * spanColumn)));
        if (temp != totalPage) {

//            if (mIndicatorView != null) {
//                mIndicatorView.initIndicator(temp);
//            }
            // 页码减少且当前页为最后一页
            if ((temp < totalPage && currentPage == totalPage)
                    || (currentPage < 1 || totalPage < 1) || (currentPage > totalPage)) {
//                currentPage = temp;
//                if (currentPage < 1) {
//                    currentPage = 1;
//                }
                // 执行滚动
                //smoothScrollBy(-getHeight(), 0);
                if (isLayoutVertical) {
                    smoothScrollBy(0, -getHeight());
                } else {
                 //   smoothScrollBy(-getWidth(), 0);
                    scrollViewToPosition(0);
                }

            } else if (currentPage >= temp) {
//                currentPage = temp;
//                if (currentPage < 1) {
//                    currentPage = 1;
//                }
                // 执行滚动
               // smoothScrollBy(-getWidth(), 0);
               // scrollToPosition((currentPage - 1) * (spanRow * spanColumn));
                scrollViewToPosition(0);
            } else {

            }

            totalPage = temp;

        }
        currentPage = getCurrentPage();
        handlePageChange(currentPage);
//        setScrollPage();
//        if (myAdapter != null) {
//            myAdapter.onPageChange(currentPage);
//        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case 2:
                scrollState = 2;
                break;
            case 1:
                scrollState = 1;
                break;
            case 0:
                currentPage = getCurrentPage();
                handlePageChange(currentPage);
                /*if (slideDistance == 0) {
                    if (mIndicatorView != null) {
                        mIndicatorView.setSelectedPage(currentPage - 1);
                    }
                    //setScrollPage();
                    break;
                }
                scrollState = 0;
                if (slideDistance < 0) { // 上页
                    if (isLayoutVertical) {
                        currentPage = (int) Math.ceil(scrollX / getHeight());
                    } else {
                        currentPage = (int) Math.ceil(scrollX / getWidth());
                        Log.i(TAG,"上页 currentPage: "+currentPage);
                    }

                    if (isLayoutVertical) {
                        if (currentPage * getHeight() - scrollX < shortestDistance) {
                            currentPage += 1;
                        }
                    } else {
                        if (currentPage * getWidth() - scrollX < shortestDistance) {
                            currentPage += 1;
                        }
                        Log.i(TAG,"上页2 currentPage: "+currentPage);
                    }

                } else { // 下页
                    if (isLayoutVertical) {
                        currentPage = (int) Math.ceil(scrollX / getHeight()) + 1;
                    } else {
                        currentPage = (int) Math.ceil(scrollX / getWidth()) + 1;
                        Log.i(TAG,"下页 currentPage: "+currentPage);
                    }

                    if (currentPage <= totalPage) {
                        if (isLayoutVertical) {
                            if (scrollX - (currentPage - 2) * getHeight() < shortestDistance) {
                                // 如果这一页滑出距离不足，则定位到前一页
                                currentPage -= 1;
                            }
                        } else {
                            if (scrollX - (currentPage - 2) * getWidth() < shortestDistance) {
                                // 如果这一页滑出距离不足，则定位到前一页
                                currentPage -= 1;
                            }
                            Log.i(TAG,"下页2 currentPage: "+currentPage);
                        }

                    } else {
                        currentPage = totalPage;
                    }
                }
                // 执行自动滚动
                if (isLayoutVertical) {
                    smoothScrollBy (0, (int) ((currentPage - 1) * getHeight() - scrollX));
                } else {
                    smoothScrollBy((int) ((currentPage - 1) * getHeight() - scrollX), 0);
                }

                // 修改指示器选中项
                if (mIndicatorView != null) {
                    mIndicatorView.setSelectedPage(currentPage - 1);
                }*/

                slideDistance = 0;
              /*  setScrollPage();
                if (myAdapter != null) {
                    myAdapter.onPageChange(currentPage);
                }*/

                break;
        }
        super.onScrollStateChanged(state);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if (isLayoutVertical) {
            scrollX += dy;
            if (scrollState == 1) {
                slideDistance += dy;
            }
        } else {
            scrollX += dx;
            if (scrollState == 1) {
                slideDistance += dx;
            }
        }
        super.onScrolled(dx, dy);
    }

    private void handlePageChange(int page) {
        if (myAdapter != null) {
            myAdapter.onPageChange(page);
        }
        if (mIndicatorView != null) {
            mIndicatorView.setSelectedPage(page - 1);
        }
    }

    public void pageUp() {
        if (currentPage >= totalPage) {
            scrollViewToPosition(0);
        } else {
            smoothScrollToPosition((currentPage + 1)*(spanRow * spanColumn)-1);
        }
    }

    public void pageDown() {
        if (currentPage <= 1) {
            if (isLayoutVertical) {
                smoothScrollToPosition(totalPage * (spanRow * spanColumn) - 1);
            } else {
                scrollViewToPosition(totalPage* (spanRow * spanColumn) -1 );
            }
        } else {
            smoothScrollToPosition((currentPage - 2)*(spanRow * spanColumn));
        }
    }

    public int getCurrentPage() {
        int iCurrentPage = 1;
        AutoGridLayoutManager mAutoGridLayoutManager = (AutoGridLayoutManager)getLayoutManager();
        int lastPosition = mAutoGridLayoutManager.findLastCompletelyVisibleItemPosition();
        if (lastPosition < 0) {
            return iCurrentPage;
        }
        int iCountEveryPage = spanRow * spanColumn;
        int mInteger = lastPosition / iCountEveryPage;
        int mRemainder = lastPosition % iCountEveryPage;
        if (mRemainder < (iCountEveryPage / 2)) {
            iCurrentPage = mInteger;
        } else {
            iCurrentPage = mInteger + 1;
        }
        return iCurrentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public ViewHolder findItemView(int position) {
        int mAdapterPosition = -1;
        if (myAdapter != null) {
            mAdapterPosition = myAdapter.getAdapterPosition(position);
        }
        return findViewHolderForAdapterPosition(mAdapterPosition);
    }

    public void  notifyItemChanged(int position) {
        int mAdapterPosition = -1;
        if (myAdapter != null) {
            mAdapterPosition = myAdapter.getAdapterPosition(position);
            myAdapter.notifyItemChanged(mAdapterPosition);
        }
    }
    private float mScrollX = -1;
    public void scrollItem(boolean pageScroll) {
        if (scrollX == mScrollX) {
            if (scrollX > 0) {
                m_iRollingDirection = ROLLING_LEFT;
                if (pageScroll) {
                    smoothScrollBy(-getMeasuredWidth(), 0);
                } else {
                    scrollBy(-2, 0);
                }
                if (isScrollEnd) {
                    mScrollX = -1;
                    m_iRollingDirection = ROLLING_RIGHT;
                }
            } else {
                m_iRollingDirection = ROLLING_RIGHT;
                if (pageScroll) {
                    smoothScrollBy(getMeasuredWidth(), 0);
                } else {
                    scrollBy(2, 0);
                }
                if (isScrollEnd) {
                    mScrollX = -1;
                    m_iRollingDirection = ROLLING_LEFT;
                }
            }
            isScrollEnd = true;
        } else {
            isScrollEnd = false;
            mScrollX = scrollX;
            if (ROLLING_RIGHT == m_iRollingDirection) {
                if (pageScroll) {
                    smoothScrollBy(getMeasuredWidth(), 0);
                } else {
                    scrollBy(2, 0);
                }

            } else {
                if (pageScroll) {
                    smoothScrollBy(-getMeasuredWidth(), 0);
                } else {
                    scrollBy(-2, 0);
                }

            }

        }
    }

    public void stopScrollView() {
        AutoGridLayoutManager mAutoGridLayoutManager = (AutoGridLayoutManager)getLayoutManager();
        if (mAutoGridLayoutManager != null) {
            int lastPosition = mAutoGridLayoutManager.findLastVisibleItemPosition();
            if (lastPosition > 0) {
                scrollViewToPosition(lastPosition);
            }
        }
    }

    private static int getRealPosition4x4(int position) {
        int mPosition = 0;
        int mInt = position / 16;
        int mRemainder = position % 16;
        switch (mRemainder) {
            case 0:
                mPosition = mInt * 16;
                break;
            case 1:
                mPosition = mInt * 16 + 4;
                break;
            case 2:
                mPosition = mInt * 16 + 8;
                break;
            case 3:
                mPosition = mInt * 16 + 12;
                break;
            case 4:
                mPosition = mInt * 16 + 1;
                break;
            case 5:
                mPosition = mInt * 16 + 5;
                break;
            case 6:
                mPosition = mInt * 16 + 9;
                break;
            case 7:
                mPosition = mInt * 16 + 13;
                break;
            case 8:
                mPosition = mInt * 16 + 2;
                break;
            case 9:
                mPosition = mInt * 16 + 6;
                break;
            case 10:
                mPosition = mInt * 16 + 10;
                break;
            case 11:
                mPosition = mInt * 16 + 14;
                break;
            case 12:
                mPosition = mInt * 16 + 3;
                break;
            case 13:
                mPosition = mInt * 16 + 7;
                break;
            case 14:
                mPosition = mInt * 16 + 11;
                break;
            case 15:
                mPosition = mInt * 16 + 15;
                break;
            default:
                break;
        }
        return mPosition;
    }

    private static int getRealPosition4x5(int position) {
        int mPosition = 0;
        int mInt = position / 20;
        int mRemainder = position % 20;
        switch (mRemainder) {
            case 0:
                mPosition = mInt * 20;
                break;
            case 1:
                mPosition = mInt * 20 + 5;
                break;
            case 2:
                mPosition = mInt * 20 + 10;
                break;
            case 3:
                mPosition = mInt * 20 + 15;
                break;
            case 4:
                mPosition = mInt * 20 + 1;
                break;
            case 5:
                mPosition = mInt * 20 + 6;
                break;
            case 6:
                mPosition = mInt * 20 + 11;
                break;
            case 7:
                mPosition = mInt * 20 + 16;
                break;
            case 8:
                mPosition = mInt * 20 + 2;
                break;
            case 9:
                mPosition = mInt * 20 + 7;
                break;
            case 10:
                mPosition = mInt * 20 + 12;
                break;
            case 11:
                mPosition = mInt * 20 + 17;
                break;
            case 12:
                mPosition = mInt * 20 + 3;
                break;
            case 13:
                mPosition = mInt * 20 + 8;
                break;
            case 14:
                mPosition = mInt * 20 + 13;
                break;
            case 15:
                mPosition = mInt * 20 + 18;
                break;
            case 16:
                mPosition = mInt * 20 + 4;
                break;
            case 17:
                mPosition = mInt * 20 + 9;
                break;
            case 18:
                mPosition = mInt * 20 + 14;
                break;
            case 19:
                mPosition = mInt * 20 + 19;
                break;
            default:
                break;
        }
        return mPosition;
    }

    private static int getRealPosition6x5(int position) {
        int mPosition = 0;
        int mRemainder = position % 30;
        switch (mRemainder) {
            case 0:
            case 6:
            case 12:
            case 18:
            case 24:
                mPosition = mRemainder / 6;
                break;
            case 1:
            case 7:
            case 13:
            case 19:
            case 25:
                mPosition = 5 + (mRemainder - 1) / 6;
                break;
            case 2:
            case 8:
            case 14:
            case 20:
            case 26:
                mPosition = 10 + (mRemainder - 2) / 6;
                break;
            case 3:
            case 9:
            case 15:
            case 21:
            case 27:
                mPosition = 15 + (mRemainder - 3) / 6;
                break;
            case 4:
            case 10:
            case 16:
            case 22:
            case 28:
                mPosition = 20 + (mRemainder - 4) / 6;
                break;
            case 5:
            case 11:
            case 17:
            case 23:
            case 29:
                mPosition = 25 + (mRemainder - 4) / 6;
                break;
            default:
                break;
        }
        return mPosition;
    }

    private static int getRealPosition6x10(int position) {
        int mPosition = 0;
        int mRemainder = position % 60;
        switch (mRemainder) {
            case 0:
            case 6:
            case 12:
            case 18:
            case 24:
            case 30:
            case 36:
            case 42:
            case 48:
            case 54:
                mPosition = mRemainder / 6;
                break;
            case 1:
            case 7:
            case 13:
            case 19:
            case 25:
            case 31:
            case 37:
            case 43:
            case 49:
            case 55:
                mPosition = 10 + (mRemainder - 1) / 6;
                break;
            case 2:
            case 8:
            case 14:
            case 20:
            case 26:
            case 32:
            case 38:
            case 44:
            case 50:
            case 56:
                mPosition = 20 + (mRemainder - 2) / 6;
                break;
            case 3:
            case 9:
            case 15:
            case 21:
            case 27:
            case 33:
            case 39:
            case 45:
            case 51:
            case 57:
                mPosition = 30 + (mRemainder - 3) / 6;
                break;
            case 4:
            case 10:
            case 16:
            case 22:
            case 28:
            case 34:
            case 40:
            case 46:
            case 52:
            case 58:
                mPosition = 40 + (mRemainder - 4) / 6;
                break;
            case 5:
            case 11:
            case 17:
            case 23:
            case 29:
            case 35:
            case 41:
            case 47:
            case 53:
            case 59:
                mPosition = 50 + (mRemainder - 5) / 6;
                break;
            default:
                break;
        }
        return mPosition;
    }

    private static int getRealPosition8x10(int position) {
        int mPosition = 0;
        int mRemainder = position % 80;
        switch (mRemainder) {
            case 0:
            case 8:
            case 16:
            case 24:
            case 32:
            case 40:
            case 48:
            case 56:
            case 64:
            case 72:
                mPosition = mRemainder / 8;
                break;
            case 1:
            case 9:
            case 17:
            case 25:
            case 33:
            case 41:
            case 49:
            case 57:
            case 65:
            case 73:
                mPosition = 10 + (mRemainder - 1) / 8;
                break;
            case 2:
            case 10:
            case 18:
            case 26:
            case 34:
            case 42:
            case 50:
            case 58:
            case 66:
            case 74:
                mPosition = 20 + (mRemainder - 2) / 8;
                break;
            case 3:
            case 11:
            case 19:
            case 27:
            case 35:
            case 43:
            case 51:
            case 59:
            case 67:
            case 75:
                mPosition = 30 + (mRemainder - 3) / 8;
                break;
            case 4:
            case 12:
            case 20:
            case 28:
            case 36:
            case 44:
            case 52:
            case 60:
            case 68:
            case 76:
                mPosition = 40 + (mRemainder - 4) / 8;
                break;
            case 5:
            case 13:
            case 21:
            case 29:
            case 37:
            case 45:
            case 53:
            case 61:
            case 69:
            case 77:
                mPosition = 50 + (mRemainder - 5) / 8;
                break;
            case 6:
            case 14:
            case 22:
            case 30:
            case 38:
            case 46:
            case 54:
            case 62:
            case 70:
            case 78:
                mPosition = 60 + (mRemainder - 6) / 8;
                break;
            case 7:
            case 15:
            case 23:
            case 31:
            case 39:
            case 47:
            case 55:
            case 63:
            case 71:
            case 79:
                mPosition = 70 + (mRemainder - 7) / 8;
                break;
            default:
                break;
        }
        return mPosition;
    }


    private static int countRealPosition(int position) {
        int mPosition = 0;
        int rowCol = spanRow * spanColumn;
        if ((4 == spanRow) && (4 == spanColumn)) {
            mPosition = getRealPosition4x4(position);
        } else if ((4 == spanRow) && (5 == spanColumn)) {
            mPosition = getRealPosition4x5(position);
        } else if ((6 == spanRow) && (5 == spanColumn)) {
            getRealPosition6x5(position);
        } else if ((6 == spanRow) && (10 == spanColumn)) {
            mPosition = getRealPosition6x10(position);
        } else if ((8 == spanRow) && (10 == spanColumn)) {
            getRealPosition8x10(position);
        }
        else {
            if (position % spanRow  == 0) {
                mPosition = (position/rowCol) * rowCol + ((position - (position/rowCol)*rowCol))/spanRow;
            } else {
                mPosition = (position/rowCol) * rowCol + ((position - (position/rowCol)*rowCol))/spanRow + rowCol/spanRow;
            }
        }
        return mPosition;
    }

    private static int countAdapterPosition(int position) {
        int mPosition = 0;
        int mRemainder = position % 10;
        switch (mRemainder) {
            case 0:
            case 9:
                mPosition = position;
                break;
            case 1:
                mPosition = position + 1;
                break;
            case 2:
                mPosition = position + 2;
                break;
            case 3:
                mPosition = position + 3;
                break;
            case 4:
                mPosition = position + 4;
                break;
            case 5:
                mPosition = position - 4;
                break;
            case 6:
                mPosition = position - 3;
                break;
            case 7:
                mPosition = position - 2;
                break;
            case 8:
                mPosition = position - 1;
                break;
            default:
                break;

        }
        return mPosition;
    }

    /**
     * 数据适配器
     */
    public class PageAdapter extends Adapter<ViewHolder> {

        private final WeakReference<PageRecyclerView> mRecyclerView;
        private PageRecyclerView mRecycler = null;
        private int mDataCount = 0;
        private int miCurrentPage = 1;
        private CallBack mCallBack = null;
        private boolean bFixed = false;
        private int itemWidth = 0;
        private int itemHeigh = 0;
        private int itemCount = 0;


        public PageAdapter(int count, CallBack callBack) {
            this.mRecyclerView = new WeakReference<PageRecyclerView>(PageRecyclerView.this);
            mRecycler = mRecyclerView.get();
            mRecycler.setAdapter(this);
            this.miCurrentPage = mRecycler.currentPage;
            if (mRecycler.isCanScroll) {
                this.itemCount = count;
            } else {
                int mRemainder = count % (mRecycler.spanRow * mRecycler.spanColumn);
                if (0 == mRemainder) {
                    this.itemCount = count;
                } else {
                    this.itemCount = count + (mRecycler.spanRow * mRecycler.spanColumn - mRemainder);
                }
            }
            this.mDataCount = count;
            this.mCallBack = callBack;
            // this.itemCount = count;
            // this.itemCount = count + spanRow * spanColumn;
        }

        public PageAdapter(boolean pageDisplay,int count,int width,int height, CallBack callBack) {
            if (pageDisplay) {
                isCustomized = true;
                isCanScroll = false;
            }
            this.mRecyclerView = new WeakReference<PageRecyclerView>(PageRecyclerView.this);
            mRecycler = mRecyclerView.get();
            mRecycler.setAdapter(this);
            this.miCurrentPage = mRecycler.currentPage;
            if (mRecycler.isCanScroll) {
                this.itemCount = count;
            } else {
                int mRemainder = count % (mRecycler.spanRow * mRecycler.spanColumn);
                if (0 == mRemainder) {
                    this.itemCount = count;
                } else {
                    this.itemCount = count + (mRecycler.spanRow * mRecycler.spanColumn - mRemainder);
                }
            }
            this.mDataCount = count;
            this.mCallBack = callBack;
            this.itemWidth = width / spanColumn;
            this.itemHeigh = height / spanRow;
            this.bFixed = true;
        }

        public void removeCallBack() {
            mCallBack = null;
        }

        public void setDataList(int count) {
            if (mRecycler.isCanScroll) {
                this.itemCount = count;
            } else {
                int mRemainder = count % (mRecycler.spanRow * mRecycler.spanColumn);
                if (0 == mRemainder) {
                    this.itemCount = count;
                } else {
                    this.itemCount = count + (mRecycler.spanRow * mRecycler.spanColumn - mRemainder);
                }
            }
            this.mDataCount = count;
            mRecycler.update();
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (null == mCallBack) {
                return null;
            }
            if (itemWidth <= 0) {
                // 计算Item的宽度
                if (mRecycler.isLayoutVertical) {
                    itemWidth = (parent.getHeight() - mRecycler.pageMargin * 2) / mRecycler.spanRow;
                } else {
                    itemWidth = (parent.getWidth() - mRecycler.pageMargin * 2) / mRecycler.spanColumn;
                }
            }

            if (bFixed) {
                if (itemHeigh <= 0) {
                    itemHeigh = (parent.getHeight() - mRecycler.pageMargin * 2) / mRecycler.spanRow;
                }
            }

            ViewHolder holder = mCallBack.onCreateViewHolder(parent, viewType);

            holder.itemView.measure(0, 0);

            if (bFixed) {
                holder.itemView.getLayoutParams().width = itemWidth;
                holder.itemView.getLayoutParams().height = itemHeigh;
            } else {
                if (mRecycler.isLayoutVertical) {
                    holder.itemView.getLayoutParams().width = holder.itemView.getMeasuredWidth()+40;
                    holder.itemView.getLayoutParams().height = itemWidth;
                } else {
                    holder.itemView.getLayoutParams().width = itemWidth;
                    holder.itemView.getLayoutParams().height = holder.itemView.getMeasuredHeight()+40;
                }
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (null == mCallBack) {
                return;
            }
           /* if (mRecycler.spanColumn == 1) {
                // 每个Item距离左右两侧各pageMargin
                if (mRecycler.isLayoutVertical) {
                    holder.itemView.getLayoutParams().height = itemWidth + mRecycler.pageMargin * 2;
                    holder.itemView.setPadding(0, mRecycler.pageMargin, 0, mRecycler.pageMargin);
                } else {
                    holder.itemView.getLayoutParams().width = itemWidth + mRecycler.pageMargin * 2;
                    holder.itemView.setPadding(mRecycler.pageMargin, 0, mRecycler.pageMargin, 0);
                }

            } else {
                /*int m = position % (spanRow * spanColumn);
                if (m < spanRow) {
                    // 每页左侧的Item距离左边pageMargin
                    holder.itemView.getLayoutParams().width = itemWidth + pageMargin;
                    holder.itemView.setPadding(pageMargin, 0, 0, 0);
                } else if (m >= spanRow * spanColumn - spanRow) {
                    // 每页右侧的Item距离右边pageMargin
                    holder.itemView.getLayoutParams().width = itemWidth + pageMargin;
                    holder.itemView.setPadding(0, 0, pageMargin, 0);
                } else {
                    // 中间的正常显示
                    holder.itemView.getLayoutParams().width = itemWidth;
                    holder.itemView.setPadding(0, 0, 0, 0);
                }*/
              /*  if (mRecycler.isLayoutVertical) {
                    holder.itemView.getLayoutParams().height = itemWidth;
                } else {
                    holder.itemView.getLayoutParams().width = itemWidth;
                }

                holder.itemView.setPadding(0, 0, 0, 0);
            }*/
            int realPosition = 0;
            if (mRecycler.isCustomized) {
                realPosition = countRealPosition(position);
            } else {
                realPosition = position;
            }
            holder.itemView.setTag(realPosition);

            setListener(holder);
            if (realPosition < mDataCount) {
                holder.itemView.setVisibility(View.VISIBLE);
                mCallBack.onBindViewHolder(holder, realPosition);
            } else {
                holder.itemView.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            return itemCount;
        }

        public void onPageChange(int currentPage) {

            miCurrentPage = currentPage;
            if (mCallBack != null) {
                mCallBack.onPageChange(currentPage);
            }
        }

        private void setListener(ViewHolder holder) {
            // 设置监听
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack != null) {
                        mCallBack.onItemClickListener(v, (Integer) v.getTag());
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mCallBack != null) {
                        mCallBack.onItemLongClickListener(v, (Integer) v.getTag());
                    }
                    return true;
                }
            });

            holder.itemView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mCallBack != null) {
                        mCallBack.onItemTouchListener(v, (Integer) v.getTag(), event);
                    }
                    return false;
                }
            });
        }

        public int getAdapterPosition(int position) {
            if (null == mRecycler) {
                return 0;
            }
            return mRecycler.countAdapterPosition(position);
        }

        /**
         * 删除Item
         * @param
         */
        /*public void remove(int position) {
            if (position < dataList.size()) {
                // 删除数据
                dataList.remove(position);
                itemCount--;
                // 删除Item
                notifyItemRemoved(position);
                // 更新界面上发生改变的Item
                notifyItemRangeChanged((currentPage - 1) * spanRow * spanColumn, currentPage * spanRow * spanColumn);
                // 更新页码指示器
                update();
            }
        }*/
    }

    public interface CallBack {

        /**
         * 创建VieHolder
         *
         * @param parent
         * @param viewType
         */
        ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

        /**
         * 绑定数据到ViewHolder
         *
         * @param holder
         * @param position
         */
        void onBindViewHolder(ViewHolder holder, int position);

        void onItemClickListener(View view, int position);
        void onItemLongClickListener(View view, int position);
        void onItemTouchListener(View view, int position, MotionEvent event);
        void onPageChange(int currentPage);
    }

}