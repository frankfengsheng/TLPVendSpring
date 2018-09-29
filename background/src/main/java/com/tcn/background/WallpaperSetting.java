package com.tcn.background;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.view.TcnMainActivity;

import java.util.List;


public class WallpaperSetting extends TcnMainActivity {
	private static final String TAG = "WallpaperSetting";
	private static int m_image_count = 0;
	private List<String> picsPath;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initData() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "initData initData()");
		if ((picsPath != null) && (!picsPath.isEmpty())) {
			picsPath.clear();
		}
		picsPath = TcnVendIF.getInstance().getImageGoodsPathList();
		m_image_count = TcnVendIF.getInstance().getImageGoodsCount();
		TcnVendIF.getInstance().LoggerDebug(TAG, "initData picsPath： "+picsPath+" m_image_count: "+m_image_count);
		if ((null == picsPath) || (m_image_count <= 0) || (picsPath.isEmpty())) {
			TcnVendIF.getInstance().queryImagePathList();
		} else {
			initAdapter();
		}

	}

	private void initAdapter() {

		if ((null == picsPath) || (null == picsPath)) {
			return;
		}
		
		/*options = application.getOptions();
		imageLoader = ImageLoader.getInstance();
		in = getIntent();
		String path = in.getStringExtra("path");
		
		//pics_path = ContentUtils.getImgPathFromUDisk(ContentUtils.IMGS_URL);
		//m_image_count = pics_path.size();

		if("".equals(path)||null == path){
		} else {
			m_selectPosition = pics_path.indexOf(path);
			//tempp = findposition(path);
			//System.out.println(path);System.out.println(tempp);
		}
		VendIF.getInstance().LoggerDebug(TAG, "initData m_selectPosition: "+m_selectPosition);
		mImage_bs = new Vector<Boolean>();
		for(int i = 0; i < m_image_count; i++){
		    if((m_selectPosition >= 0) && (m_selectPosition == i)){
		        mImage_bs.add(true);
		    }else{
		        mImage_bs.add(false);
		    }
		}

		adapter = new MyAdapter(false,mImage_bs,m_selectPosition);
		pics_grid.setAdapter(adapter);
		adapter.notifyDataSetChanged();*/
	}

	private class WallpaperAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
