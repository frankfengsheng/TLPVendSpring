package com.tcn.background;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.view.TcnMainActivity;




/**
 * 菜单界面，用于跳转至各个具体功能界面
 * @author Administrator
 *
 */
public class MenuActivity extends TcnMainActivity implements OnClickListener,
		OnItemClickListener {

	private static final String TAG = "MenuActivity";

	private GridView gridView;
	private MyAdapter adapter;
	private Button btn_back,btn_exit;

	private boolean isOpen=false;
	private String[] m_MenuList = null;
	private String m_customer = null;
	private boolean m_bReplenish = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TcnVendIF.getInstance().LoggerDebug(TAG, "onCreate() onCreate");
		setContentView(R.layout.main_menu);

		if (null == gridView) {
			gridView = (GridView) findViewById(R.id.menu_grid);
			if (null == gridView) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() gridView is null");
				return;
			}
		}

		gridView.setOnItemClickListener(this);

		if (null == btn_back) {
			btn_back = (Button) findViewById(R.id.btn_back);
			if (null == btn_back) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() btn_back is null");
				return;
			}
		}

		btn_back.setOnClickListener(this);

		if (null == btn_exit) {
			btn_exit = (Button) findViewById(R.id.btn_exit);
			if (null == btn_exit) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() btn_exit is null");
				return;
			}
		}

		btn_exit.setOnClickListener(this);
		Intent mIn = getIntent();
		String login_type = mIn.getStringExtra("login_type");
		if ("Replenish".equals(login_type)) {
			m_MenuList = TcnCommon.getInstance().getReplenishMenuList();
			m_bReplenish = true;
		} else {
			m_MenuList = TcnCommon.getInstance().getMenuList();
			m_bReplenish = false;
		}
		m_customer = TcnShareUseData.getInstance().getTcnCustomerType();
		adapter = new MyAdapter();
		gridView.setAdapter(adapter);
	}



	@Override
	protected void onDestroy() {
		if (null != gridView) {
			gridView.setOnItemClickListener(null);
			gridView.setAdapter(null);
			gridView = null;
		}
		if (null != btn_back) {
			btn_back.setOnClickListener(null);
			btn_back = null;
		}
		if (null != btn_exit) {
			btn_exit.setOnClickListener(null);
			btn_exit = null;
		}
		m_MenuList = null;
		m_customer = null;
		adapter = null;
		super.onDestroy();
	}



	/**
	 * 菜单布局适配器
	 * @author Administrator
	 */
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return m_MenuList.length;
		}

		@Override
		public Object getItem(int position) {
			return m_MenuList[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(null == convertView){
				convertView = LayoutInflater.from(MenuActivity.this).inflate(R.layout.menu_list_item, null);
				holder = new ViewHolder();
				holder.mImageView = (ImageView) convertView.findViewById(R.id.menuimg);
				holder.mTextView = (TextView) convertView.findViewById(R.id.menu_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}

			switch (position) {
				case 0:
					if (m_bReplenish) {
						holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.settings));
					} else {
						holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.settings));
					}

					break;
				case 1:
					if (m_bReplenish) {
						holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.download));
					} else {
						holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.settings));
					}
					break;
				case 2:
					holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.lock1));
					break;
				case 3:
					if ("NiuNiuAD".equals(m_customer)) {
						holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.download));
					} else {
						holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.document));
					}
					break;
				case 4:
					holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.shopping));
					break;
				case 5:
					holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.download));
					break;
				case 6:
					holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.tools));
					break;
				case 7:
					holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.settings));
					break;
				case 8:
					holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.tools));
					break;
				case 9:
					holder.mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.settings));
					break;
				default:
					break;
			}
			holder.mTextView.setText(m_MenuList[position]);
			return convertView;
		}

	}

	private static class ViewHolder {
		public ImageView mImageView;
		public TextView mTextView;
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent in=null;
		switch (arg2) {
			case 0:
				if ("NiuNiuAD".equals(m_customer)) {
					in = new Intent(this, Setting.class);
					startActivity(in);
				} else {
					if (TcnShareUseData.getInstance().isBackgroundCustomized()) {
						if (m_bReplenish) {
							in = new Intent(this, AisleDisplay.class);
							in.putExtra("flag", m_MenuList[arg2]);
							startActivity(in);
						} else {
							TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.BACKGROUND_AISLE_MANAGE,-1,-1,this);
						}
					} else {
						if (m_bReplenish) {
							in = new Intent(this, ManageGoods.class);
							in.putExtra("flag", m_MenuList[arg2]);
							startActivity(in);
						} else {
							in = new Intent(this, AisleDisplay.class);
							in.putExtra("flag", m_MenuList[arg2]);
							startActivity(in);
						}

					}
				}

				break;
			case 1:
				if (TcnShareUseData.getInstance().isBackgroundCustomized()) {
					if (m_bReplenish) {
						in = new Intent(this, Updatepro.class);
						in.putExtra("flag", m_MenuList[arg2]);
						startActivity(in);
					} else {
						TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.BACKGROUND_GOODS_MANAGE,-1,-1,this);
					}
				} else {
					if ((TcnShareUseData.getInstance().getTcnDataType()).equals(TcnConstant.DATA_TYPE[0])
							|| (TcnShareUseData.getInstance().getTcnDataType()).equals(TcnConstant.DATA_TYPE[1])) {
						TcnUtility.getToast(MenuActivity.this, getString(R.string.notify_not_open));
					} else {
						if (m_bReplenish) {
							in = new Intent(this, Updatepro.class);
							in.putExtra("flag", m_MenuList[arg2]);
							startActivity(in);
						} else {
							in = new Intent(this, ManageGoods.class);
							in.putExtra("flag", m_MenuList[arg2]);
							startActivity(in);
						}
					}
				}
				break;
			case 2:
				gotodesk();
				/*in = new Intent(this, PasswordManage.class);
				in.putExtra("flag", m_MenuList[arg2]);
				startActivity(in);*/
				break;
			case 3:
				if ("NiuNiuAD".equals(m_customer)) {
					in = new Intent(this, Updatepro.class);
					in.putExtra("flag", m_MenuList[arg2]);
					startActivity(in);
				} else {
					if (TcnShareUseData.getInstance().isBackgroundCustomized()) {
						TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.BACKGROUND_INFORMATION_CONFIG,-1,-1,this);
					} else {
						in = new Intent(this, InformationConfig.class);
						in.putExtra("flag", m_MenuList[arg2]);
						startActivity(in);
					}
				}

				break;
			case 4:
				if (TcnShareUseData.getInstance().isBackgroundCustomized()) {
					TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.BACKGROUND_PAY_SYSTEM_SETTING,-1,-1,this);
				} else {
					in = new Intent(this, PaySettingActivity.class);
					in.putExtra("flag", m_MenuList[arg2]);
					startActivity(in);
				}
				break;
			case 5:
				in = new Intent(this, Updatepro.class);
				in.putExtra("flag", m_MenuList[arg2]);
				startActivity(in);
				break;
//		case 5:
//			in=new Intent(this,CheckLight.class);
//			in.putExtra("flag", ContentUtils.MENU_LIST[arg2]);
//			startActivity(in);
//			logger.debug("---------弹出对话框，是否开启掉货检测--------");
//			if(getSharedpreference()){
//				showDialog(0,"掉货检测已开启,是否关闭");
//			}else{
//				showDialog(1,"是否开启掉货检测");
//			}
//			break;
			case 6:
				TcnVendIF.getInstance().LoggerDebug(TAG, "跳转至SerialPortSetting");
				in = new Intent(this, SerialPortSetting.class);
				in.putExtra("flag", m_MenuList[arg2]);
				startActivity(in);
//			showDialog(3,"暂无出货记录");
				break;
			case 7:
				String dataType = TcnShareUseData.getInstance().getTcnDataType();
				in = new Intent(this, MenuSettingsSpringActivity.class);
				startActivity(in);
				break;
			case 8:
				if ((TcnShareUseData.getInstance().getTcnDataType()).equals(TcnConstant.DATA_TYPE[1])) {
					in=new Intent(this,SalesDataActivity.class);
					startActivity(in);
				} else {
					TcnUtility.getToast(MenuActivity.this, getString(R.string.notify_not_open));
				}
				break;
			case 9:
				if ((TcnShareUseData.getInstance().getTcnDataType()).equals(TcnConstant.DATA_TYPE[0])
					|| (TcnShareUseData.getInstance().getTcnDataType()).equals(TcnConstant.DATA_TYPE[1])) {
					TcnUtility.getToast(MenuActivity.this, getString(R.string.notify_not_open));
				} else {
					in=new Intent(this,FuncConfig.class);
					startActivity(in);
				}
				break;
			default:
				break;
		}
	}

	private boolean getSharedpreference(){
		SharedPreferences sp=getSharedPreferences("check", MODE_PRIVATE);
		boolean coil=sp.getBoolean("coil", false);
		return coil;
	}

	private void generatesp(){
		SharedPreferences sp=getSharedPreferences("check", MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putBoolean("coil", isOpen);
		editor.commit();
	}

	private void showDialog(final int type,String str){
		final Dialog dialog=new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view=getLayoutInflater().inflate(R.layout.psw_dialog, null);
		TextView dialog_text=(TextView) view.findViewById(R.id.dialog_text);
		dialog_text.setText(str);
		dialog.addContentView(view, new LayoutParams(getScreen(true)/3,LayoutParams.WRAP_CONTENT));
		Button psw_ensureof=(Button) view.findViewById(R.id.psw_ensureof);
		psw_ensureof.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (type) {
					case 0:
						TcnVendIF.getInstance().LoggerDebug(TAG, "showDialog() psw_ensureof onClick 掉货检测关闭");
						isOpen=false;
						generatesp();
						break;
					case 1:
						TcnVendIF.getInstance().LoggerDebug(TAG, "showDialog() psw_ensureof onClick 掉货检测开启");
						isOpen=true;
						generatesp();
						break;
					case 2:
						TcnVendIF.getInstance().LoggerDebug(TAG, "showDialog() psw_ensureof onClick 恢复出厂");
						break;
					default:
						break;
				}
				dialog.cancel();
			}
		});
		Button psw_cancel=(Button) view.findViewById(R.id.psw_cancel);
		psw_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (type) {
					case 1:
						TcnVendIF.getInstance().LoggerDebug(TAG, "showDialog() psw_cancel onClick 掉货检测");
						isOpen=false;
						generatesp();
						break;
					case 2:
						TcnVendIF.getInstance().LoggerDebug(TAG, "showDialog() psw_cancel onClick 恢复出厂");
						break;
					default:
						break;
				}
				dialog.cancel();
			}
		});
		dialog.show();
	}

	private int getScreen(boolean iswidth){
		DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width=metrics.widthPixels;
		int height=metrics.heightPixels;
		if(iswidth){
			return width;
		}else{
			return height;
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_back) {
			this.finish();
		} else if (id == R.id.btn_exit) {
			TcnVendIF.getInstance().sendMsgToUIDelay(TcnVendEventID.FINISH_MAIN_ACTIVITY,500);
			finish();
		} else {
		}
	}


	//返回桌面
	public void gotodesk(){
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		home.addCategory(Intent.CATEGORY_HOME);
		startActivity(home);

	}

}
