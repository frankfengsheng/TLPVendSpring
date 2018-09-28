package com.tcn.vendspring.shopping;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcn.funcommon.TLPTcnCommon;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.recycleview.PageRecyclerView;
import com.tcn.vendspring.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import controller.TlpUICommon;
import controller.UICommon;
import controller.UIGoodsInfo;


public class FragmentSelection extends Fragment {

	private static final String TAG = "FragmentSelection";
	private static final int SCROLL_CMD = 1;
	private static final int PAGE_TIME = 50;
	private static final int SCROLL_START_TIME = 10000;
	private static int m_listData_count = 0;

	private volatile boolean m_bShowByGoodsCode;
	private Context m_Context = null;
	private volatile String m_strDataType = null;
	//private MyPager myPager;
	private PageRecyclerView m_RecyclerView;
	private PageRecyclerView.PageAdapter m_Adapter = null;
	private Button m_btn_pre = null;
	private Button m_btn_next = null;
	private TextView m_TextView = null;
	private Timer m_Timer;
	private TimerTask m_TimerTask;
	private View m_view;

	/** 将小圆点的图片用数组表示 */
	//private ImageView[] imageViews;
	// 包裹小圆点的LinearLayout
	private LinearLayout m_select_type_layout;
	private LinearLayout m_select_type_layout_s;

	private LinearLayout  select_page_layout = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		m_view = inflater.inflate(UICommon.getInstance().getSelectionLayout(), container, false);
		TcnVendIF.getInstance().LoggerDebug(TAG, "onCreateView()");
		m_Context = this.getActivity();

		m_RecyclerView = (PageRecyclerView) m_view.findViewById(R.id.vp);
		if (null == m_RecyclerView) {
			TcnVendIF.getInstance().LoggerError(TAG, "onCreateView() m_RecyclerView is null");
			return null;
		}
		setButtonStateChangeListener(m_RecyclerView);

	    // 实例化小圆点的linearLayout和viewpager
		m_select_type_layout = (LinearLayout) m_view.findViewById(R.id.viewGroup);

		m_select_type_layout_s = (LinearLayout) m_view.findViewById(R.id.viewGroup_s);

		select_page_layout = (LinearLayout) m_view.findViewById(R.id.select_page_layout);

		m_btn_pre = (Button) m_view.findViewById(R.id.select_pre);
		if (m_btn_pre != null) {
			m_btn_pre.setOnClickListener(m_cClickListener);
		}
		m_btn_next = (Button) m_view.findViewById(R.id.select_next);
		if (m_btn_next != null) {
			m_btn_next.setOnClickListener(m_cClickListener);
		}

		m_TextView = (TextView) m_view.findViewById(R.id.select_page);

		return m_view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TcnVendIF.getInstance().LoggerDebug(TAG, "onResume()");
    	TcnVendIF.getInstance().registerListener(m_vendListener);
		m_bShowByGoodsCode = TcnShareUseData.getInstance().isShowByGoodsCode();
		m_strDataType = TcnShareUseData.getInstance().getTcnDataType();
		m_listData_count = UICommon.getInstance().getGoodsCount(UICommon.getInstance().getGoodsType());
		if (m_listData_count <= 0) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "onResume() m_listData_count: "+m_listData_count);
			return;
		}
		initdata(true);
		TcnVendIF.getInstance().reqShowOrHideAdMedia(TcnVendIF.SHOW_AD_MEDIA);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		TcnVendIF.getInstance().LoggerDebug(TAG, "onAttach()");
		m_Context = activity;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TcnVendIF.getInstance().LoggerDebug(TAG, "onPause()");
		TcnVendIF.getInstance().unregisterListener(m_vendListener);
		if (m_handler != null ) {
			m_handler.removeCallbacksAndMessages(null);
		}
		stopScrollTimer();

		if (m_RecyclerView != null) {
			m_RecyclerView.destroyAdapter();
		}
		if (m_Adapter != null) {
			m_Adapter.removeCallBack();
			m_Adapter = null;
		}

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		if (m_RecyclerView != null) {
			m_RecyclerView.setOnTouchListener(null);
			m_RecyclerView = null;
		}
		if (m_btn_pre != null) {
			m_btn_pre.setOnClickListener(null);
			m_btn_pre = null;
		}
		if (m_btn_next != null) {
			m_btn_next.setOnClickListener(null);
			m_btn_next = null;
		}

		if (m_handler != null ) {
			m_handler.removeCallbacksAndMessages(null);
			m_handler = null;
		}
		m_Context = null;
		m_PageAdapterCallBack = null;
		m_TextView = null;
		m_Timer = null;
		m_TimerTask = null;
		m_view = null;
		m_Adapter = null;
		select_page_layout = null;
		m_select_type_layout = null;
		m_select_type_layout_s = null;
		m_cClickListener = null;
		m_touchListener = null;
		m_vendListener = null;
		super.onDestroyView();
		TcnVendIF.getInstance().LoggerDebug(TAG, "onDestroyView()");
	}
	
	
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onHiddenChangedis hidden: " + hidden);
		if (hidden) {
			refreshPause();
		} else {
			refresh();
		}
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}

	public void destroyView() {
		stopScrollTimer();
	}


	private void initTypePoints() {
		if (null == m_select_type_layout) {
			TcnVendIF.getInstance().LoggerError(TAG, "initTypePoints m_select_type_layout is null, return");
			return;
		}
		if (!TcnShareUseData.getInstance().isShowType()) {
			m_select_type_layout.setVisibility(View.GONE);
			return;
		}
		List<String> mTypeList = UICommon.getInstance().getAliveType();
		if ((null == mTypeList) || (mTypeList.size() < 1)) {
			m_select_type_layout.setVisibility(View.GONE);
			return;
		}
		m_select_type_layout.setVisibility(View.VISIBLE);
		int iTypeSize = mTypeList.size();
		m_select_type_layout.removeAllViewsInLayout();

		if (m_select_type_layout_s != null) {
			if (iTypeSize > 10) {
				m_select_type_layout_s.setVisibility(View.VISIBLE);
			} else {
				m_select_type_layout_s.setVisibility(View.GONE);
			}
			m_select_type_layout_s.removeAllViewsInLayout();
		}

		for (int i = 0; i < iTypeSize; i++) {
			Button mButton = new Button(m_Context);
			mButton.setTextColor(Color.BLACK);
			mButton.setGravity(Gravity.CENTER);
			mButton.setTextSize(26);
			// 设置小圆点imageview的参数
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 80);
			layoutParams.setMargins(3, 0, 3, 0);
			mButton.setLayoutParams(layoutParams);// 创建一个宽高均为20 的布局
			// 将小圆点layout添加到数组中
			//mButton.setBackgroundResource(R.mipmap.page_indicator_unfocused);
			mButton.setBackgroundResource(R.drawable.button_type_style);
			if (i == 0) {
				if ((mTypeList.get(i)).equals(TcnVendIF.GOODS_TYPE_ALL)) {
					mButton.setText(R.string.type_all);
					if ((UICommon.getInstance().getGoodsType()).equals(TcnVendIF.GOODS_TYPE_ALL)) {
						mButton.setSelected(true);
					}
				} else {
					mButton.setText(mTypeList.get(i));
					if ((UICommon.getInstance().getGoodsType()).equals(mTypeList.get(i))) {
						mButton.setSelected(true);
					}
				}
			} else {
				mButton.setText(mTypeList.get(i));
				if ((UICommon.getInstance().getGoodsType()).equals(mTypeList.get(i))) {
					mButton.setSelected(true);
				}
			}
			if (TcnVendIF.getInstance().isContainChinese(mTypeList.get(i))) {
				if ((mTypeList.get(i)).length() > 5) {
					mButton.setTextSize(16);
				} else if ((mTypeList.get(i)).length() > 4) {
					mButton.setTextSize(20);
				} else if ((mTypeList.get(i)).length() > 3) {
					mButton.setTextSize(22);
				} else {

				}
			}
			if (i > 9) {
				if (m_select_type_layout_s != null) {
					m_select_type_layout_s.addView(mButton);
				}
			} else {
				m_select_type_layout.addView(mButton);
			}
			mButton.setTag("type"+i);
			mButton.setOnClickListener(m_cClickListener);
		}
	}
	
	private void initdata(boolean query) {
		if (TcnShareUseData.getInstance().isPageDisplay()) {
			if (m_RecyclerView != null) {
				m_RecyclerView.setCustomized(true);
				m_RecyclerView.setCanScroll(false);
			}
			if (m_listData_count <= TcnCommon.getInstance().getGoodsNumEveryPage()) {
				if (select_page_layout != null) {
					select_page_layout.setVisibility(View.GONE);
				}
			} else {
				if (select_page_layout != null) {
					select_page_layout.setVisibility(View.VISIBLE);
				}
			}
		} else {
			if (select_page_layout != null) {
				select_page_layout.setVisibility(View.GONE);
			}
		}
		if (null == m_Adapter) {
			m_RecyclerView.setHasFixedSize(true);
			// 设置行数和列数
			m_RecyclerView.setPageSize(TLPTcnCommon.getInstance().getGoodsRowNum(), TLPTcnCommon.getInstance().getGoodsColumnNum());
			m_PageAdapterCallBack.setItemLayout(TlpUICommon.getInstance().getSelectionItemLayout());
			// 设置页间距
			//m_RecyclerView.setPageMargin(20);
			if (UICommon.getInstance().isRecyclerViewMeasured()) {
				m_Adapter = m_RecyclerView.new PageAdapter(TlpUICommon.getInstance().isPageDisplay(),m_listData_count, TlpUICommon.getInstance().getRecyclerViewWidth(),
						TlpUICommon.getInstance().getRecyclerViewHeight(),m_PageAdapterCallBack);
			} else {
				m_Adapter = m_RecyclerView.new PageAdapter(m_listData_count,m_PageAdapterCallBack);
			}

			// 设置数据
			//m_RecyclerView.setAdapter(m_Adapter);
			m_Adapter.setDataList(m_listData_count);
		} else {
			m_PageAdapterCallBack.setItemLayout(UICommon.getInstance().getSelectionItemLayout());
		}
		initTypePoints();
		if (query) {
			TcnVendIF.getInstance().queryAliveCoil();
		}
	}

	public void startScrollTimer(int startTime) {
		if (m_listData_count <= TcnCommon.getInstance().getGoodsNumEveryPage()) {
			if (select_page_layout != null) {
				select_page_layout.setVisibility(View.GONE);
			}
			return;
		}
		if (null == m_RecyclerView) {
			return;
		}

		if (!m_RecyclerView.isCanScroll()) {
			return;
		}

		stopScrollTimer();

		if (null == m_Timer) {
			m_Timer = new Timer("startScrollTimer");
		}

		m_TimerTask = new TimerTask() {
			@Override
			public void run() {
				if (m_handler != null ) {
					m_handler.removeMessages(SCROLL_CMD);
					m_handler.sendEmptyMessage(SCROLL_CMD);
				}
			}
		};
		m_Timer.schedule(m_TimerTask, startTime,PAGE_TIME);
	}

	public void cancelScrollTimerTask() {
		if (m_RecyclerView != null) {
			m_RecyclerView.stopScroll();
		}
		if (m_TimerTask != null) {
			m_TimerTask.cancel();
			m_TimerTask = null;
		}
	}

	public void stopScrollTimer() {
		if (m_RecyclerView != null) {
			m_RecyclerView.stopScrollView();
		}
		if (m_Timer != null) {
			m_Timer.cancel();
			m_Timer.purge();
			m_Timer = null;
		}

		if (m_TimerTask != null) {
			m_TimerTask.cancel();
			m_TimerTask = null;
		}

		if (m_handler != null) {
			m_handler.removeMessages(SCROLL_CMD);
		}

	}

	private void refresh() {
        TcnVendIF.getInstance().registerListener(m_vendListener);
		m_listData_count = UICommon.getInstance().getGoodsCount(UICommon.getInstance().getGoodsType());
		if (!UICommon.getInstance().isPayShowing()) {
			TcnVendIF.getInstance().reqShowOrHideAdMedia(TcnVendIF.SHOW_AD_MEDIA);
		}
		if (m_listData_count <= 0) {
	    	TcnVendIF.getInstance().LoggerDebug(TAG, "refresh() m_listData_count: "+m_listData_count);
			return;
		}
		initdata(true);
	}

	private void refreshPause() {
		TcnVendIF.getInstance().unregisterListener(m_vendListener);
		if (!UICommon.getInstance().isPayShowing()) {
			TcnVendIF.getInstance().reqShowOrHideAdMedia(TcnVendIF.HIDE_AD_MEDIA);
		}
		stopScrollTimer();
	}

	private void itemClick(int position) {
		/*AnimationSet set =new AnimationSet(true);
      //  ScaleAnimation scaleAnim = new ScaleAnimation (1.0f,0.0f,1.0f,0.0f,0.5f,0.5f);
        VendIF.getInstance().LoggerDebug(TAG, "----onItemClick left: "+left+" top: "+top);
        TranslateAnimation tranAnim = new TranslateAnimation(
                Animation.ABSOLUTE,
                768,Animation.ABSOLUTE,view.getLeft(),
                Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0);
        set.addAnimation(tranAnim);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(800);
        stopPageTimer();
        view.startAnimation(set); */

		if (isHidden()) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "----onItemClick is isHidden");
			return;
		}

		if (TcnUtility.isFastClick()) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "----onItemClick isFastClick ");
			return;
		}

		TcnVendIF.getInstance().reqTouchSoundPlay();
		TcnVendIF.getInstance().LoggerDebug(TAG, "----onItemClick ---PC选货---- position: " + position + " m_listData_count: " + m_listData_count);
		cancelScrollTimerTask();
//		if(null != m_OnBackPayCallBack) {
//			m_OnBackPayCallBack.OnPayBack(TcnCommon.FRAGMENT_PAY, null);
//		}

		TcnVendIF.getInstance().reqSelectGoods(position);
        TcnVendIF.getInstance().ship(position+1,"00 FF 01 FE AA 55","","00 FF 01 FE AA 55");
	}

	private PageAdapterCallBack m_PageAdapterCallBack = new PageAdapterCallBack();
	private class PageAdapterCallBack implements PageRecyclerView.CallBack {

		private UIGoodsInfo mInfo = null;

		int res = 0;

		public void setItemLayout(int layout) {
			res = layout;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
			view.setOnTouchListener(m_touchListener);
			return new PageHolder(view);
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

			mInfo = UICommon.getInstance().getGoodsInfo(position);
			if (mInfo != null) {
				if (mInfo.getKeyNum() > 0) {
					if (((PageHolder) holder).textname != null) {
						((PageHolder) holder).textname.setText(getString(R.string.ui_keynum)+String.valueOf(mInfo.getKeyNum()));
					}
					if (TcnVendIF.getInstance().isFirstStockCoilErrForKeyMap(mInfo.getKeyNum())) {
						((PageHolder) holder).textprice.setText(getString(R.string.notify_slot_err)+mInfo.getGoods_status());
						((PageHolder) holder).imageGoods.setAlpha(0.2f);
						holder.itemView.setEnabled(false);
					} else if (TcnVendIF.getInstance().getCoilExtantQuantityForKeyMap(mInfo.getKeyNum()) <= 0) {
						((PageHolder) holder).textprice.setText(R.string.ui_sold_out);
						((PageHolder) holder).imageGoods.setAlpha(0.3f);
						holder.itemView.setEnabled(false);
					} else {
						((PageHolder) holder).textprice.setText(TcnVendIF.getInstance().getShowPrice(String.valueOf(mInfo.getGoods_price())));
						((PageHolder) holder).imageGoods.setAlpha(1.0f);
						holder.itemView.setEnabled(true);
					}

				} else {
					if (m_bShowByGoodsCode) {
						if (((PageHolder) holder).textname != null) {
							((PageHolder) holder).textname.setText(String.valueOf(mInfo.getGoods_name()));
						}
						if (mInfo.getGoods_status() != 0) {
							((PageHolder) holder).textprice.setText(getString(R.string.notify_slot_err)+mInfo.getGoods_status());
							((PageHolder) holder).imageGoods.setAlpha(0.2f);
							holder.itemView.setEnabled(false);
						} else if (mInfo.getGoods_stock() > 0) {
							((PageHolder) holder).textprice.setText(TcnVendIF.getInstance().getShowPrice(String.valueOf(mInfo.getGoods_price())));
							((PageHolder) holder).imageGoods.setAlpha(1.0f);
							holder.itemView.setEnabled(true);
						} else if (mInfo.getGoods_stock() <= 0) {
							((PageHolder) holder).textprice.setText(R.string.ui_sold_out);
							((PageHolder) holder).imageGoods.setAlpha(0.3f);
							holder.itemView.setEnabled(false);
						} else {

						}
					} else {
						if (mInfo.getCoil_id() > 0) {
							if (((PageHolder) holder).textname != null) {
								((PageHolder) holder).textname.setText(getString(R.string.ui_aisle)+TcnVendIF.getInstance().getSlotNoDisplay(mInfo.getCoil_id()));
							}
							if (m_strDataType.equals(TcnConstant.DATA_TYPE[0])) {
								((PageHolder) holder).textprice.setText(TcnVendIF.getInstance().getShowPrice(String.valueOf(mInfo.getGoods_price())));
							} else {
								if (mInfo.getGoods_status() != 0) {
									((PageHolder) holder).textprice.setText(getString(R.string.notify_slot_err)+mInfo.getGoods_status());
									((PageHolder) holder).imageGoods.setAlpha(0.2f);
									holder.itemView.setEnabled(false);
								} else if (mInfo.getGoods_stock() > 0) {
									((PageHolder) holder).textprice.setText(TcnVendIF.getInstance().getShowPrice(String.valueOf(mInfo.getGoods_price())));
									((PageHolder) holder).imageGoods.setAlpha(1.0f);
									holder.itemView.setEnabled(true);
								} else if (mInfo.getGoods_stock() <= 0) {
									((PageHolder) holder).textprice.setText(R.string.ui_sold_out);
									((PageHolder) holder).imageGoods.setAlpha(0.3f);
									holder.itemView.setEnabled(false);
								} else {

								}
							}

						} else {
							//
						}
					}

				}

				TcnVendIF.getInstance().displayImage(mInfo.getGoods_url(), ((PageHolder) holder).imageGoods, R.mipmap.default_ticket_poster_pic);

			}

		}

		@Override
		public void onItemClickListener(View view, int position) {
			//itemClick(position);
		}

		@Override
		public void onItemLongClickListener(View view, int position) {

		}

		@Override
		public void onItemTouchListener(View view, int position, MotionEvent event) {
			if ((null == view) || (null == event)) {
				TcnVendIF.getInstance().LoggerError(TAG, "onItemTouchListener view or event is null");
				return;
			}
			if(event.getAction() == MotionEvent.ACTION_UP) {
				itemClick(position);
				ImageView mImageView = (ImageView)view.findViewById(R.id.img);
				if (mImageView != null) {
					mImageView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.in_translate_top));
				}
			}
		}

		@Override
		public void onPageChange(int currentPage) {
			if ((m_TextView != null) && (m_RecyclerView != null)) {
				m_TextView.setText(currentPage+"/"+m_RecyclerView.getTotalPage());
			}
		}
	}

	private class PageHolder extends RecyclerView.ViewHolder {

		public ImageView imageGoods = null;
		public TextView textname = null;
		public TextView textcoilId = null;
		public TextView textprice = null;
		public ImageView imgprice = null;

		public PageHolder(View itemView) {
			super(itemView);
			imageGoods = (ImageView)itemView.findViewById(R.id.img);
			textname = (TextView) itemView.findViewById(R.id.textname);
			textcoilId = (TextView) itemView.findViewById(R.id.textcoilId);
			textprice = (TextView) itemView.findViewById(R.id.textprice);
			imgprice = (ImageView)itemView.findViewById(R.id.imgprice);
		}

	}

	private Handler m_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case SCROLL_CMD:
				if (m_RecyclerView != null) {
					if (!m_RecyclerView.isCanScroll()) {
						stopScrollTimer();
					} else {
						if (!isHidden() && isVisible()) {
							m_RecyclerView.scrollItem(false);
						}
					}
				} else {
					stopScrollTimer();
				}
				break;
			default:
			    break;
			}
		}
	};

	private ClickListener m_cClickListener = new ClickListener();
	private class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int mId = v.getId();
			if (R.id.select_pre == mId) {
				if (m_RecyclerView != null) {
					m_RecyclerView.pageDown();
				}
			} else if (R.id.select_next == mId) {
				if (m_RecyclerView != null) {
					m_RecyclerView.pageUp();
				}
			} else {
				String tag = (String )v.getTag();
				if (tag.contains("type")) {
					List<String> mTypeList = UICommon.getInstance().getAliveType();
					Button mBtnSelect = (Button)v;
					String type = mBtnSelect.getText().toString();
					int index = mTypeList.indexOf(UICommon.getInstance().getGoodsType());
					if (index < 10) {
						if (m_select_type_layout != null) {
							Button mBtn = (Button)m_select_type_layout.getChildAt(index);
							if (mBtn != null) {
								mBtn.setSelected(false);
							}
						}

					} else {
						if (m_select_type_layout_s != null) {
							Button mBtn = (Button)m_select_type_layout_s.getChildAt(index-10);
							if (mBtn != null) {
								mBtn.setSelected(false);
							}
						}

					}

					v.setSelected(true);
					if (getString(R.string.type_all).equals(type)) {
						type = TcnVendIF.GOODS_TYPE_ALL;
					}
					refreshType(type);
				}
			}
		}
		
	}
	 
	public void setButtonStateChangeListener(View v) {

		if (null == v) {
			TcnVendIF.getInstance().LoggerError(TAG, "setButtonStateChangeListener v is null");
			return;
		}
		v.setOnTouchListener(m_touchListener);

	}

	private TouchListener m_touchListener = new TouchListener();
	private class TouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if ((null == v) || (null == event)) {
				  TcnVendIF.getInstance().LoggerError(TAG, "OnTouchListener onTouch v or event is null");
				  return false;
			  }
			  
			  if(event.getAction() == MotionEvent.ACTION_DOWN) {

				int id = v.getId();
				if (id == R.id.vp) {
					//cancelPageTimerTask();
					cancelScrollTimerTask();
				}
		
			  } else if(event.getAction() == MotionEvent.ACTION_UP) {
		
				int id = v.getId();
				if (id == R.id.vp) {
					//startPageTimer();
					startScrollTimer(SCROLL_START_TIME);
					TcnVendIF.getInstance().reqShowOrHideAdMedia(TcnVendIF.SHOW_AD_MEDIA);
				}
		
			  } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				int id = v.getId();
				if (id == R.id.vp) {
					//cancelPageTimerTask();
					cancelScrollTimerTask();
					TcnVendIF.getInstance().reqShowOrHideAdMedia(TcnVendIF.SHOW_AD_MEDIA);
				}
			}
		
		   return false;
		}
		
	}

	private void refreshType(String type){
		m_listData_count = UICommon.getInstance().getGoodsCount(type);
		if (m_listData_count <= 0) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "refreshType() m_listData_count: "+m_listData_count);
			TcnUtility.getToast(m_Context,"没有该分类的商品");
		}
		if (m_Adapter != null) {
			m_Adapter.setDataList(m_listData_count);
		}
		startScrollTimer(SCROLL_START_TIME);
		if (!UICommon.getInstance().isPayShowing()) {
			TcnVendIF.getInstance().reqShowOrHideAdMedia(TcnVendIF.SHOW_AD_MEDIA);
		}
	}

	private VendListener m_vendListener = new VendListener();
	private class VendListener implements TcnVendIF.VendEventListener {

		@Override
		public void VendEvent(VendEventInfo cEventInfo) {
			switch (cEventInfo.m_iEventID) {
				case TcnVendEventID.QUERY_ALIVE_COIL:
					if (m_Adapter != null) {
						refreshType(UICommon.getInstance().getGoodsType());
					} else {
						m_listData_count = UICommon.getInstance().getGoodsCount(UICommon.getInstance().getGoodsType());
						initdata(false);
					}
					break;
				case TcnVendEventID.COMMAND_SHIPMENT_FAULT:
					TcnUtility.getToast(m_Context, getString(R.string.notify_slot_fault) + cEventInfo.m_lParam1 + getString(R.string.notify_slot_code) + cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.COMMAND_COIN_REFUND_START:

					TcnUtility.getToast(m_Context , getString(R.string.notify_coin_back));
					break;
				case TcnVendEventID.COMMAND_COIN_REFUND_END:
					// cEventInfo.m_lParam4
					TcnUtility.getToast(m_Context , getString(R.string.notify_coinback_success));
					break;
				case TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA:
					if (cEventInfo.m_lParam1 == TcnVendIF.SHOW_AD_MEDIA) {
						stopScrollTimer();
					} else {
						startScrollTimer(SCROLL_START_TIME);
					}
					break;
				case TcnVendEventID.COMMAND_FAULT_INFORMATION:
					if (cEventInfo.m_lParam1 < 1000) {
						refreshType(UICommon.getInstance().getGoodsType());
					}
					break;
				default:
					break;
			}
		}

	}
}
