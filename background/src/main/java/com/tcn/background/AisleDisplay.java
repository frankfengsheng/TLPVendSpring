package com.tcn.background;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.dialog.DialogInput;
import com.tcn.uicommon.view.TcnMainActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



/**
 * 货道商品展示界面
 * @author Administrator
 *
 */

public class AisleDisplay extends TcnMainActivity implements AdapterView.OnItemClickListener {

	private static final String TAG = "AisleDisplay";
	protected static int m_listData_count = -1;
	private static final int DIALOG_CREATE = 3;
	private static final int PAGE_DOWN_CMD = 4;
	private static final int PAGE_UP_CMD = 5;
	private static final int QUERY_TIME_EVERYONCE = 3000;
	private boolean isPageAnim = false;

	private int m_totalPageNum = 0;
	private int m_viewHeight,m_viewWidth;
	private int m_iPageBigFont = 28;
	private int m_iPageSmallFont = 20;
	private Timer m_Timer = null;
	private TimerTask m_TimerTask = null;

	private Button aisledisplay_back;
	private Button aisle_display_add_show_slotno;
	protected ViewPager displayvp;
	protected List<Coil_info> coil_list;
	private LinearLayout loadlinear;
	private RotateAnimation mAnim ;
	private ImageView loadimg;
	protected int vp_temp=0;
	private TextView nowpage,allpage;
	private RelativeLayout vplinear;
	private MyPager myPager;
	private SpannableStringBuilder m_stringBuilder = null;
	private AbsoluteSizeSpan m_textSizeSpan = null;

	private DialogInput m_DialogInputAddShow = null;
	private DialogInput m_DialogInputDelete = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TcnVendIF.getInstance().LoggerDebug(TAG, "进入AisleDisplay onCreate");
		setContentView(R.layout.aisledisplay);

		if (null == vplinear) {
			vplinear=(RelativeLayout) findViewById(R.id.vplinear);
			if (null == vplinear) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() vplinear is null");
				return;
			}
		}

		if (null == displayvp) {
			displayvp = (ViewPager) findViewById(R.id.displayvp);
			if (null == displayvp) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() displayvp is null");
				return;
			}
		}

		if (null == nowpage) {
			nowpage = (TextView) findViewById(R.id.nowpage);
			if (null == nowpage) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() nowpage is null");
				return;
			}
		}

		if (null == allpage) {
			allpage = (TextView) findViewById(R.id.allpage);
			if (null == allpage) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() allpage is null");
				return;
			}
		}

		if (null == loadlinear) {
			loadlinear=(LinearLayout) findViewById(R.id.loadlinear);
			if (null == loadlinear) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() loadlinear is null");
				return;
			}
		}

		if (null == aisledisplay_back) {
			aisledisplay_back=(Button) findViewById(R.id.aisledisplay_back);
			if (null == aisledisplay_back) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() aisledisplay_back is null");
				return;
			}
		}

		aisledisplay_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AisleDisplay.this.finish();
			}
		});

		aisle_display_add_show_slotno=(Button) findViewById(R.id.aisle_display_add_show_slotno);
		aisle_display_add_show_slotno.setOnClickListener(m_ClickListener);

		int iScreenWidth = TcnVendIF.getInstance().getScreenWidth();
		int iScreenHeight = TcnVendIF.getInstance().getScreenHeight();

		m_viewWidth = (int)((iScreenWidth-iScreenWidth*2.0f/10)/3);
		m_viewHeight = (int)((iScreenHeight -iScreenHeight*2.0f/10)/4);
		m_iPageBigFont = TcnVendIF.getInstance().getFitScreenSize(m_iPageBigFont);
		m_iPageSmallFont = TcnVendIF.getInstance().getFitScreenSize(m_iPageSmallFont);

		handler.removeMessages(DIALOG_CREATE);
		handler.sendEmptyMessageDelayed(DIALOG_CREATE,300);
		
		initData();

	}

	@Override
	protected void onResume() {
		super.onResume();
		TcnVendIF.getInstance().LoggerDebug(TAG, "进入AisleDisplay onResume");
		TcnVendIF.getInstance().registerListener(m_vendListener);

		coil_list = TcnVendIF.getInstance().getAliveCoilAll();
		m_listData_count = TcnVendIF.getInstance().getAliveCoilCountAll();

		if((coil_list != null) && (m_listData_count > 0) && (!coil_list.isEmpty())) {
			handler.removeMessages(1);
			handler.sendEmptyMessage(1);
		}else{
			//还未获取到货道信息，则开启线程一直查询，直到查询到为止
			loadlinear.setVisibility(View.VISIBLE);
			vplinear.setVisibility(View.GONE);
			loadimg=(ImageView) loadlinear.findViewById(R.id.loadimg);
			initAnim();
			loadimg.startAnimation(mAnim);
			TcnVendIF.getInstance().queryAliveCoil();
			// new SearchCoilThread().start();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		TcnVendIF.getInstance().LoggerDebug(TAG, "onPause()");
		TcnVendIF.getInstance().unregisterListener(m_vendListener);
		cancelTimerTask();
		if(null != mAnim){
			mAnim.cancel();
		}
	}


	@Override
	protected void onDestroy() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onDestroy()");
		vplinear = null;
		stopTimer();
		if (displayvp != null) {
			displayvp.clearAnimation();
			displayvp.setAdapter(null);
			displayvp.setOnPageChangeListener(null);
			displayvp = null;
		}

		if (myPager != null) {
			myPager = null;
		}
		coil_list = null;
		nowpage = null;
		isPageAnim = false;
		mAnim = null;
		allpage = null;
		loadlinear = null;
		loadimg = null;
		m_stringBuilder = null;
		m_textSizeSpan = null;
		if (aisledisplay_back != null) {
			aisledisplay_back.setOnClickListener(null);
			aisledisplay_back = null;
		}

		if (m_DialogInputAddShow != null) {
			m_DialogInputAddShow.setButtonListener(null);
			m_DialogInputAddShow.deInit();
			m_DialogInputAddShow = null;
		}

		if (m_DialogInputDelete != null) {
			m_DialogInputDelete.setButtonListener(null);
			m_DialogInputDelete.deInit();
			m_DialogInputDelete = null;
		}

		if (aisle_display_add_show_slotno != null) {
			aisle_display_add_show_slotno.setOnClickListener(null);
			aisle_display_add_show_slotno = null;
		}

		if (aisledisplay_back != null) {
			aisledisplay_back.setOnClickListener(null);
			aisledisplay_back = null;
		}

		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}

		m_DialogInputDeleteListener = null;
		m_ItemLongClickListener = null;
		m_ClickListener = null;
		m_vendListener = null;
		m_DialogInputAddShowListener = null;
		super.onDestroy();
	}

	public void add(View v) {
		Intent in=new Intent(this, AisleManage.class);
		in.putExtra("flag", 1);
		startActivity(in);
	}

	private void initData() {

	}

	/**
	 * 初始化旋转动画
	 */
	private void initAnim() {
		mAnim = new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		mAnim.setDuration(1500);
		mAnim.setRepeatCount(Animation.INFINITE);
		mAnim.setRepeatMode(Animation.RESTART);
		LinearInterpolator lir = new LinearInterpolator();
		mAnim.setInterpolator(lir);
	}


	@SuppressLint("HandlerLeak")
	private Handler handler=new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					TcnVendIF.getInstance().LoggerDebug(TAG, "Handler handleMessage 检测到有货道信息，更新布局");
					//检测到有货道信息，更新布局
					if(null!=mAnim){
						mAnim.cancel();
					}
					loadlinear.setVisibility(View.GONE);
					vplinear.setVisibility(View.VISIBLE);

					nowpage.setText(vp_temp+1+"");
					m_listData_count = coil_list.size();
					setTotalPage();
					allpage.setText(m_totalPageNum+"");
					if (null == myPager) {
						myPager = new MyPager();
					}

					displayvp.setAdapter(myPager);
					displayvp.setCurrentItem(vp_temp);
					displayvp.setOnPageChangeListener(new PageChangeListener());
					//myPager.notifyDataSetChanged();

					if (TcnShareUseData.getInstance().isAppFirstCreat()) {
						postDelayed(new Runnable() {

							@Override
							public void run() {
								TcnShareUseData.getInstance().setAppFirstCreat(AisleDisplay.this, false);
							}
						}, 500);
					}

					break;
				case DIALOG_CREATE:
					m_DialogInputAddShow = new DialogInput(AisleDisplay.this);
					m_DialogInputAddShow.setButtonType(DialogInput.BUTTON_TYPE_INPUT);
					m_DialogInputAddShow.setButtonInputType(InputType.TYPE_CLASS_NUMBER);
					m_DialogInputAddShow.setButtonTiTle(R.string.aisle_dis_input_add_show_slotno);
					m_DialogInputAddShow.setButtonSureText(getString(R.string.backgroound_ensure));
					m_DialogInputAddShow.setButtonCancelText(getString(R.string.backgroound_cancel));
					m_DialogInputAddShow.setButtonListener(m_DialogInputAddShowListener);

					m_DialogInputDelete = new DialogInput(AisleDisplay.this);
					m_DialogInputDelete.setButtonType(DialogInput.BUTTON_TYPE_NO_INPUT);
					m_DialogInputDelete.setButtonTiTle(R.string.aisle_dis_sure_to_delete);
					m_DialogInputDelete.setButtonListener(m_DialogInputDeleteListener);
					break;
				case PAGE_DOWN_CMD:
					nextpage((View)(msg.obj));
					break;
				case PAGE_UP_CMD:
					prepage((View)(msg.obj));
					break;
				default:
					break;
			}
		};
	};

	/**
	 * 设置页面切换时的动画
	 * @author Administrator
	 *
	 */
	class PageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (0 == arg0) {
				isPageAnim = false;
			} else {
				isPageAnim = true;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(final int arg0) {
			vp_temp=displayvp.getCurrentItem();
			nowpage.setText(vp_temp+1+"");
			myPager.notifyDataSetChanged();
		}

	}

	/**
	 * 切换到下一页
	 * @param v
	 */
	public void prepage(View v){
        /*if (Untilly.isFastClick()) {
        	//发送handler消息之前，清空该消息
        	handler.removeMessages(PAGE_DOWN_CMD);
        	handler.removeMessages(PAGE_UP_CMD);
   	 
   	        //绑定一个msg
   	        Message msg = Message.obtain();
   	        msg.what = PAGE_UP_CMD;
   	        msg.obj = v;
   	     
   	        //发送消息间隔500秒
   	        handler.sendMessageDelayed(msg, 800);
			return;
		}*/
		if (isPageAnim) {
			return;
		}
		vp_temp--;
		if(vp_temp<0){
			vp_temp = m_totalPageNum-1;
		}
		displayvp.setCurrentItem(vp_temp);

		//myPager.notifyDataSetChanged();

	}

	/**
	 * 切换到上一页
	 * @param v
	 */
	public void nextpage(View v){
    	/*if (Untilly.isFastClick()) {
    		//发送handler消息之前，清空该消息
  		  	handler.removeMessages(PAGE_UP_CMD);
  	      	handler.removeMessages(PAGE_DOWN_CMD);
  	 
  	      	//绑定一个msg
  	      	Message msg = Message.obtain(); 	
  	      	msg.what = PAGE_DOWN_CMD;
  	      	msg.obj = v;
  	 
  	      	//发送消息间隔500秒
  	      	handler.sendMessageDelayed(msg, 800);
    		return;
    	}*/
		if (isPageAnim) {
			return;
		}
		vp_temp++;
		if(vp_temp >= m_totalPageNum){
			vp_temp=0;
		}
		displayvp.setCurrentItem(vp_temp);

		//myPager.notifyDataSetChanged();

	}


	/**
	 * 定时
	 */
	private void startTimer(final int iMillis) {
		if (null == m_Timer) {
			m_Timer = new Timer("rueryAliveCoil");
		}
		if (m_TimerTask != null) {
			m_TimerTask.cancel();
			m_TimerTask = null;
		}
		if (null == m_TimerTask) {
			m_TimerTask = new TimerTask() {
				@Override
				public void run() {
					if (QUERY_TIME_EVERYONCE == iMillis) {
						TcnVendIF.getInstance().queryAliveCoil();
					}
				}
			};
		}
		m_Timer.schedule(m_TimerTask, iMillis);
	}

	private void cancelTimerTask(){
		if (m_TimerTask != null) {
			m_TimerTask.cancel();
			m_TimerTask = null;
		}
	}

	private void stopTimer(){
		if (m_Timer != null) {
			m_Timer.cancel();
			m_Timer.purge();
			m_Timer = null;
		}

		if (m_TimerTask != null) {
			m_TimerTask.cancel();
			m_TimerTask = null;
		}
	}

	/**
	 * 根据总共货道数得出vp多少页
	 */
	private void setTotalPage() {

		int iPage = m_listData_count / TcnCommon.coil_num;
		if (0 == (m_listData_count % TcnCommon.coil_num)) {
			m_totalPageNum = iPage;
		} else {
			m_totalPageNum = iPage + 1;
		}
		//displayvp.setOffscreenPageLimit(2);
	}

	/**
	 * vp的适配器
	 * @author Administrator
	 *
	 */
	@SuppressLint({ "UseSparseArrays", "InflateParams" })
	class MyPager extends PagerAdapter{

		private View mCacheView = null;

		@Override
		public int getCount() {
			return m_totalPageNum;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView((View)object);
			mCacheView = (View)object;
			if (mCacheView != null) {
				GridView mGrid = (GridView) mCacheView.findViewById(R.id.gridv);
				if (mGrid != null) {
					mGrid.setAdapter(null);
					mGrid.setOnItemClickListener(null);
				}
			}
		}

		@Override
		public Object instantiateItem(View container, int position) {
			View mView = null;
			if (mCacheView != null) {
				mView = mCacheView;
			} else {
				mView = LayoutInflater.from(AisleDisplay.this).inflate(R.layout.displaygrid, null);
			}
			GridView mGridView = (GridView) mView.findViewById(R.id.gridv);
			mGridView.setAdapter(new GridviewAdapter(position));
			mGridView.setOnItemClickListener(AisleDisplay.this);
			mGridView.setOnItemLongClickListener(m_ItemLongClickListener);
			((ViewPager)container).addView(mView);
			mCacheView = null;
			return mView;
		}


	}

	/**
	 * vp的每一页的适配器
	 * @author Administrator
	 *
	 */
	@SuppressLint({ "NewApi", "InflateParams", "ResourceAsColor" }) private class GridviewAdapter extends BaseAdapter{


		private int iCount = 0;
		public GridviewAdapter(int position) {
			super();
			iCount = TcnCommon.coil_num * position;
		}

		@Override
		public int getCount() {
			return TcnCommon.coil_num;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if ((null == coil_list) || (coil_list.isEmpty())) {
				TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() coil_list is null or empty");
				return null;
			}
			if((position >= TcnCommon.coil_num)){
				TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() position: " + position);
				return null;
			}

			int iPosition = iCount + position;

			ViewHolder holder = null;
			if(null == convertView) {
				holder = new ViewHolder();
				RelativeLayout mLayout = new RelativeLayout(AisleDisplay.this);
				AbsListView.LayoutParams linearParams = new AbsListView.LayoutParams(m_viewWidth, m_viewHeight);
				int iTemp = m_viewWidth;
				if (m_viewWidth > m_viewHeight) {
					iTemp = m_viewHeight;
				}
				ImageView mImageView = new ImageView(AisleDisplay.this);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(iTemp - 60, iTemp - 60);
				params.setMargins(m_viewWidth/10, m_viewHeight/10, m_viewWidth/10, (int)(m_viewHeight/10));
				//params.setMargins(m_viewWidth/10, m_viewHeight/10, m_viewWidth/10, 0);
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				mLayout.addView(mImageView, params);

				ImageView mImageForbid= new ImageView(AisleDisplay.this);
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				//params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				params2.topMargin = m_viewHeight*2/10;
				params2.rightMargin = m_viewWidth*2/10;
				mLayout.addView(mImageForbid, params2);

				TextView mTextView = new TextView(AisleDisplay.this);
				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				params3.addRule(RelativeLayout.CENTER_HORIZONTAL);
				params3.setMargins(m_viewWidth/10, 0, m_viewWidth/10, 20);
				params3.addRule(RelativeLayout.BELOW,mImageView.getId());
				mTextView.setGravity(Gravity.CENTER);
				mTextView.setTextSize(m_iPageBigFont);
				mTextView.setTextColor(Color.WHITE);
				mLayout.addView(mTextView,params3);

				holder.mText = mTextView;
				holder.mText.setBackgroundResource(R.drawable.bottom_circle);

				holder.mImageMenu = mImageView;

				holder.mImageForbid = mImageForbid;

				convertView = mLayout;
				convertView.setLayoutParams(linearParams);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			if ((position < TcnCommon.coil_num) && (iPosition >= m_listData_count)) {
				//holder.mLayout.setBackgroundResource(0);
				holder.mText.setText("");
				holder.mText.setBackgroundResource(0);
				holder.mImageMenu.setImageDrawable(null);
				holder.mImageMenu.setImageResource(0);
				holder.mImageMenu.setImageBitmap(null);
				holder.mImageForbid.setVisibility(View.GONE);
				return convertView;
			}
			Coil_info mInfo = null;
			try {
				mInfo = coil_list.get(iPosition);
			} catch (Exception e) {
				TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() coil_list.get(iPosition)");
			}
			if (null == mInfo) {
				TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() mInfo is null");
				return null;
			}
			int iCoilId = mInfo.getCoil_id();

			if(mInfo.getWork_status() != 0) {
				//holder.mImageForbid.setVisibility(View.VISIBLE);
				holder.mText.setText(iCoilId+" "+getString(R.string.notify_slot_err)+mInfo.getWork_status());
				holder.mImageForbid.setImageResource(R.mipmap.txl_interception_block);
				holder.mImageMenu.setAlpha(0.3f);
			} else if (mInfo.getExtant_quantity() <= 0) {
				holder.mText.setText(iCoilId+" "+getString(R.string.ui_sold_out));
				holder.mImageForbid.setImageResource(R.mipmap.txl_interception_block);
				holder.mImageMenu.setAlpha(0.3f);
			} else {
				//holder.mImageForbid.setVisibility(View.GONE);
				holder.mText.setText(getSpanString(iCoilId, mInfo.getPar_price()));
				holder.mImageForbid.setImageResource(0);
				holder.mImageMenu.setAlpha(1f);
			}

			if("".equals(mInfo.getImg_url()) || (null == mInfo.getImg_url())){
				holder.mImageMenu.setImageResource(R.mipmap.default_ticket_poster_pic);
			}else{
				TcnVendIF.getInstance().displayImage(mInfo.getImg_url(), holder.mImageMenu, R.mipmap.default_ticket_poster_pic);
			}
			return convertView;
		}

	}

	private static class ViewHolder {
		//	public RelativeLayout mLayout;
		public ImageView mImageMenu;
		public ImageView mImageForbid;
		public TextView mText;
	}

	private SpannableStringBuilder getSpanString(int coilId, String price) {
		if (null == m_stringBuilder) {
			m_stringBuilder = new SpannableStringBuilder();
		}

		if (null == m_textSizeSpan) {
			m_textSizeSpan = new AbsoluteSizeSpan(m_iPageSmallFont);
		}
		m_stringBuilder.clear();
		m_stringBuilder.clearSpans();
		m_stringBuilder.append(coilId+"    ");
		int start = m_stringBuilder.length();
		String strPrice = TcnVendIF.getInstance().getShowPrice(price);
		int end = start + strPrice.length();
		m_stringBuilder.append(strPrice);
		m_stringBuilder.setSpan(m_textSizeSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return m_stringBuilder;
	}


	/**
	 * 点击单个货道，跳转至货道详细界面
	 */

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {

		vp_temp = displayvp.getCurrentItem();
		if((position < TcnCommon.coil_num) && (vp_temp* TcnCommon.coil_num + position < m_listData_count)){

			Coil_info mCoil_info = coil_list.get(vp_temp * TcnCommon.coil_num + position);
			int iCoilId = mCoil_info.getCoil_id();
			TcnVendIF.getInstance().LoggerDebug(TAG, "onItemClick iCoilId: "+iCoilId+" getImg_url: "+mCoil_info.getImg_url());
			Intent in=new Intent(this, AisleManage.class);
			in.putExtra("flag", coil_list.get(vp_temp*TcnCommon.coil_num+position).getCoil_id());
			startActivityForResult(in,101);
		}
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			vp_temp=data.getIntExtra("vpnum", 0);
		}
	}

	private ItemLongClickListener m_ItemLongClickListener = new ItemLongClickListener();
	private class ItemLongClickListener implements AdapterView.OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			vp_temp = displayvp.getCurrentItem();
			if((position < TcnCommon.coil_num) && (vp_temp* TcnCommon.coil_num + position < m_listData_count)){

				Coil_info mCoil_info = coil_list.get(vp_temp * TcnCommon.coil_num + position);
				int iCoilId = mCoil_info.getCoil_id();
				TcnVendIF.getInstance().LoggerDebug(TAG, "onItemLongClick iCoilId: "+iCoilId+" getImg_url: "+mCoil_info.getImg_url());
				m_DialogInputDeleteListener.setSlotNo(iCoilId);
				m_DialogInputDelete.show();
			}
			return true;
		}
	}

	private ButtonClickListener m_ClickListener = new ButtonClickListener();
	private class ButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (R.id.aisle_display_add_show_slotno == id) {
				if (m_DialogInputAddShow != null) {
					m_DialogInputAddShow.setButtonText("");
					m_DialogInputAddShow.show();
				}
			}
		}
	}

	private DialogInputAddShowListener m_DialogInputAddShowListener = new DialogInputAddShowListener();
	private class DialogInputAddShowListener implements DialogInput.ButtonListener {

		@Override
		public void onClick(int buttonId, String firstData, String secondData) {
			if (buttonId ==  DialogInput.BUTTON_ID_SURE) {
				if (TcnVendIF.getInstance().isDigital(firstData)) {
					if (TcnVendIF.getInstance().isDigital(secondData)) {
						TcnVendIF.getInstance().reqAddShowSlotNo(Integer.valueOf(firstData),Integer.valueOf(secondData));
					} else {
						TcnVendIF.getInstance().reqAddShowSlotNo(Integer.valueOf(firstData),Integer.valueOf(firstData));
					}

					if (m_DialogInputAddShow != null) {
						m_DialogInputAddShow.dismiss();
					}
				} else {
					m_DialogInputAddShow.setButtonError(getString(R.string.tip_input_error));
				}
			} else {
				if (m_DialogInputAddShow != null) {
					m_DialogInputAddShow.dismiss();
				}
			}
		}
	}

	private DialogInputDeleteListener m_DialogInputDeleteListener = new DialogInputDeleteListener();
	private class DialogInputDeleteListener implements DialogInput.ButtonListener {
		private int m_iCoilId = -1;
		public void setSlotNo(int coilId) {
			m_iCoilId = coilId;
		}

		public int getCoilId() {
			return m_iCoilId;
		}

		@Override
		public void onClick(int buttonId, String firstData, String secondData) {
			if (buttonId ==  DialogInput.BUTTON_ID_SURE) {
				TcnVendIF.getInstance().reqDeleteSlotNo(m_iCoilId);
			}
			if (m_DialogInputDelete != null) {
				m_DialogInputDelete.dismiss();
			}
		}
	}

	private VendListener m_vendListener = new VendListener();
	private class VendListener implements TcnVendIF.VendEventListener {

		@Override
		public void VendEvent(VendEventInfo cEventInfo) {
			if (null == cEventInfo) {
				TcnVendIF.getInstance().LoggerError(TAG, "VendListener cEventInfo is null");
				return;
			}
			switch (cEventInfo.m_iEventID) {
				case TcnVendEventID.QUERY_ALIVE_COIL:
					coil_list = TcnVendIF.getInstance().getAliveCoilAll();
					m_listData_count = TcnVendIF.getInstance().getAliveCoilCountAll();

					if ((m_listData_count > 0) && (coil_list != null)) {
						handler.sendEmptyMessage(1);
					} else {
						startTimer(QUERY_TIME_EVERYONCE);
					}
					break;
				case TcnVendEventID.ADD_SHOW_COIL_ID:
					TcnUtility.getToast(AisleDisplay.this,cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.DELETE_COIL_ID:
					TcnUtility.getToast(AisleDisplay.this,cEventInfo.m_lParam4);
					break;

				default:
					break;
			}
		}

	}

}
