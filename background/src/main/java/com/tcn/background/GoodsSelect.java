package com.tcn.background;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.view.TcnMainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;



/**
 * 展示sd卡或U盘中的所有图片
 * @author Administrator
 *
 */
@SuppressLint("ResourceAsColor")
public class GoodsSelect extends TcnMainActivity implements OnClickListener,OnItemClickListener{

	private static final String TAG = "GoodsSelect";
	private static int m_listData_count = 0;
	private Button pics_confirm,pics_back,search_btn;
	private EditText search;
	private GridView pics_grid;
	private List<Goods_info> m_goods_info_list;
	private MyAdapter adapter;
	private int m_selectPosition = -1;
	private Intent m_in;
	private Vector<Boolean> mImage_bs ;  // 定义一个向量作为选中与否容器


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_select);
		TcnVendIF.getInstance().LoggerDebug(TAG, "GoodsSelect onCreate()");

		if (null == search) {
			search = (EditText) findViewById(R.id.search);
			if (null == search) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() search is null");
				return;
			}
		}

		if (null == search_btn) {
			search_btn = (Button) findViewById(R.id.search_btn);
			if (null == search_btn) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() search_btn is null");
				return;
			}
		}

		if (null == pics_confirm) {
			pics_confirm = (Button) findViewById(R.id.pics_confirm);
			if (null == pics_confirm) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() pics_confirm is null");
				return;
			}
		}

		pics_confirm.setOnClickListener(this);
		pics_confirm.setEnabled(false);
		pics_confirm.setTextColor(Color.GRAY);


		if (null == pics_back) {
			pics_back = (Button) findViewById(R.id.pics_back);
			if (null == pics_back) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() pics_back is null");
				return;
			}
		}

		pics_back.setOnClickListener(this);

		if (null == pics_grid) {
			pics_grid = (GridView) findViewById(R.id.pics_grid);
			if (null == pics_grid) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() pics_grid is null");
				return;
			}
		}
		pics_grid.setOnItemClickListener(this);

		TcnVendIF.getInstance().registerListener(m_vendListener);
		initData();
	}


	@Override
	protected void onResume() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "GoodsSelect onResume()");
		super.onResume();
	}


	@Override
	protected void onPause() {

		super.onPause();
	}



	@Override
	protected void onDestroy() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onDestroy()");
		TcnVendIF.getInstance().unregisterListener(m_vendListener);
		search = null;
		search_btn = null;
		if (null != pics_confirm) {
			pics_confirm.setOnClickListener(null);
			pics_confirm = null;
		}
		pics_back = null;
		if (null != pics_grid) {
			pics_grid.setAdapter(null);
			pics_grid.setOnItemClickListener(null);
			pics_grid.setOnItemLongClickListener(null);
			pics_grid = null;
		}
		adapter = null;
		m_goods_info_list = null;
		m_in = null;
		mImage_bs = null;
		adapter = null;
		mCheckBoxListener = null;
		m_vendListener = null;
		super.onDestroy();
	}


	private void initData() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "GoodsSelect initData()");
		if ((m_goods_info_list != null) && (!m_goods_info_list.isEmpty())) {
			m_goods_info_list.clear();
		}
		m_goods_info_list = TcnVendIF.getInstance().getAliveGoodsAll();
		m_listData_count = TcnVendIF.getInstance().getAliveGoodsCountAll();
		TcnVendIF.getInstance().LoggerDebug(TAG, "GoodsSelect m_goods_info_list： "+m_goods_info_list+" m_listData_count: "+m_listData_count);
		if ((null == m_goods_info_list) || (m_listData_count <= 0) || (m_goods_info_list.isEmpty())) {
			TcnVendIF.getInstance().queryImagePathList();
		} else {
			initAdapter();
		}


	}

	private void initAdapter() {

		if ((null == m_goods_info_list) || (null == pics_grid)) {
			return;
		}

		m_in = getIntent();
		String goods_id = m_in.getStringExtra("goods_id");

		if("".equals(goods_id)||null == goods_id){
		} else {
			//m_selectPosition = m_goods_info_list.indexOf(goods_id);
			m_selectPosition = TcnVendIF.getInstance().getGoodsPosition(goods_id,m_goods_info_list);
		}
		TcnVendIF.getInstance().LoggerDebug(TAG, "initData m_selectPosition: "+m_selectPosition);
		mImage_bs = new Vector<Boolean>();
		for(int i = 0; i < m_listData_count; i++){
			if((m_selectPosition >= 0) && (m_selectPosition == i)){
				mImage_bs.add(true);
			}else{
				mImage_bs.add(false);
			}
		}

		adapter = new MyAdapter(false,mImage_bs,m_selectPosition);
		pics_grid.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void searchImage() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "searchImage");
		if (null == m_goods_info_list) {
			TcnVendIF.getInstance().LoggerError(TAG, "searchImage m_goods_info_list is null");
			return;
		}
		String str=search.getText().toString();
		search_btn.setBackgroundResource(R.drawable.cabcle_selector);
		//search_btn.setBackground(getResources().getDrawable(R.drawable.cabcle_selector));
		search_btn.setText(getString(R.string.backgroound_cancel));
		List<Goods_info> pics_temp=new ArrayList<Goods_info>();
		for(int i=0;i<m_goods_info_list.size();i++) {
			String substr=m_goods_info_list.get(i).getGoods_name();
//				ContentUtils.getToast(this, substr);
			if(substr.contains(str)){
				pics_temp.add(m_goods_info_list.get(i));
			}
		}
		m_goods_info_list = pics_temp;
		m_listData_count = m_goods_info_list.size();
//			ContentUtils.getToast(this, m_goods_info_list.size()+"");
		if(0 == m_listData_count){
			TcnUtility.getToast(this, getString(R.string.tip_no_search_picture));
		}
		adapter.notifyDataSetChanged();
	}

	private void cancelSearchImage() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "cancelSearchImage");
		search.setText("");
		search_btn.setBackgroundResource(R.drawable.search_selector);
		//search_btn.setBackground(getResources().getDrawable(R.drawable.search_selector));
		search_btn.setText("");
		adapter.notifyDataSetChanged();
	}

	/**
	 * 找到从AisleManage中传过来的图片所在位置
	 * @param path
	 * @return
	 */
	private int findposition(String path) {
		for (int i = 0; i < m_goods_info_list.size(); i++) {
			if(m_goods_info_list.get(i).equals(path)){
				return  i;
			}
		}
		return 0;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.pics_confirm) {
			if ((m_selectPosition <= -1) || (null == m_goods_info_list) || (m_listData_count <= 0)) {
				TcnVendIF.getInstance().LoggerError(TAG, "pics_confirm onClick() error");
				return;
			}
			m_in.putExtra("goods_id", m_goods_info_list.get(m_selectPosition).getGoods_id());
			setResult(100, m_in);
			finish();
		} else if (id == R.id.pics_back) {
			this.finish();
		} else {

		}
	}

	/**
	 * 在图库中搜索图片
	 * @param v
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	public void search(View v)
	{

		String btnstr=search_btn.getText().toString();
		String str=search.getText().toString();
		if(getString(R.string.backgroound_cancel).equals(btnstr)){

			//TcnVendIF.getInstance().queryImagePathList(0);
		}else{
			if("".equals(str)||null==str){
				TcnUtility.getToast(this, getString(R.string.tip_search_content_empty));
			}else{

				if ((null != m_goods_info_list) && (!m_goods_info_list.isEmpty())) {
					searchImage();
				} else {
					TcnVendIF.getInstance().queryImagePathList(1);
				}
			}
		}

	}

	/**
	 * 图片展示适配器
	 * @author Administrator
	 *
	 */
	@SuppressLint({ "NewApi", "InflateParams" })
	class MyAdapter extends BaseAdapter{

		private Vector<Boolean> mImage_bs ;
		private int lastPosition ;      //记录上一次选中的图片位置，-1表示未选中任何图片
		private boolean multiChoose;        //表示当前适配器是否允许多选

		public MyAdapter( boolean isMulti,Vector<Boolean> mImage, int Position ){
			multiChoose = isMulti;
			mImage_bs = mImage;
			lastPosition = Position;
		}

		// 修改选中的状态
		public void changeState(int position){
			// 多选时
			if(multiChoose){
				mImage_bs.setElementAt(!mImage_bs.elementAt(position), position);   //直接取反即可
			}
			// 单选时
			else{

				if((lastPosition != -1) && (lastPosition != position)) {
					mImage_bs.setElementAt(false, lastPosition);    //取消上一次的选中状态
				}
				boolean bTmp = mImage_bs.elementAt(position).booleanValue();
				mImage_bs.setElementAt(!bTmp, position);   //直接取反即可
				lastPosition = position;        //记录本次选中的位置

			}

			notifyDataSetChanged();     //通知适配器进行更新

		}

		@Override
		public int getCount() {
			return m_listData_count;
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
			if ((null == m_goods_info_list) || (m_goods_info_list.isEmpty())) {
				TcnVendIF.getInstance().LoggerError(TAG, "MyAdapter m_goods_info_list is null or empty");
				return convertView;
			}
			ViewHolder holder = null;
			if(null == convertView) {
				convertView = getLayoutInflater().inflate(R.layout.pic_item_goods, null);
				holder = new ViewHolder();
				holder.mImageView = (ImageView) convertView.findViewById(R.id.goosds_item_img);
				holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.goosds_item_checkbox);
				holder.mText = (TextView) convertView.findViewById(R.id.goosds_item_text);
				holder.mCheckBox.setOnCheckedChangeListener(mCheckBoxListener);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			Goods_info gInfo = m_goods_info_list.get(position);
			if (null == gInfo) {
				return convertView;
			}
			boolean bChecked = mImage_bs.get(position);
			holder.mCheckBox.setChecked(bChecked);
			if(bChecked) {
				holder.mImageView.setAlpha(0.3f);
			} else {
				holder.mImageView.setAlpha(1.0f);
			}
			holder.mText.setText(getString(R.string.name)+gInfo.getGoods_name()+"\n"+getString(R.string.ui_price)+gInfo.getGoods_price());
			TcnVendIF.getInstance().displayImage(m_goods_info_list.get(position).getGoods_url(), holder.mImageView, R.mipmap.default_ticket_poster_pic);
			return convertView;
		}

	}



	/**
	 * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画 
	 * @param view
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void addAnimation(View view){
		float [] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onItemClick position: "+position);
		m_selectPosition = position;
		adapter.changeState(position);
	}

	private static class ViewHolder {
		public ImageView mImageView;
		public CheckBox mCheckBox;
		public TextView mText;
	}

	private CheckBoxListener mCheckBoxListener = new CheckBoxListener();
	private class CheckBoxListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
									 boolean isChecked) {
			if (mImage_bs.contains(true)) {
				pics_confirm.setEnabled(true);
				pics_confirm.setTextColor(Color.WHITE);

			} else {
				pics_confirm.setEnabled(false);
				pics_confirm.setTextColor(Color.GRAY);
			}
			if(!isChecked){
				addAnimation(buttonView);
			}
		}

	}

	private VendListener m_vendListener = new VendListener();
	private class VendListener implements TcnVendIF.VendEventListener {

		@Override
		public void VendEvent(VendEventInfo cEventInfo) {
			if(null == cEventInfo) {
				TcnVendIF.getInstance().LoggerError(TAG, "VendListener cEventInfo is null");
				return;
			}

			switch (cEventInfo.m_iEventID) {
				case TcnVendEventID.QUERY_IMAGE_PATHLIST:
					if ((m_goods_info_list != null) && (!m_goods_info_list.isEmpty())) {
						m_goods_info_list.clear();
					}
					m_goods_info_list = TcnVendIF.getInstance().getAliveGoodsAll();
					if (m_goods_info_list != null) {
						m_listData_count = cEventInfo.m_lParam1;
						if (0 == cEventInfo.m_lParam2) {
							cancelSearchImage();
						} else if (1 == cEventInfo.m_lParam2) {
							searchImage();
						} else {
							initAdapter();
						}

					}
					break;

				default:
					break;
			}
		}

	}

}
