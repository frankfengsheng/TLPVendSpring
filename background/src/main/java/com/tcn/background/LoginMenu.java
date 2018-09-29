package com.tcn.background;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.media.ImageController;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendEventResultID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.view.TcnMainActivity;



/**
 * 登录菜单界面
 * @author Administrator
 *
 */
public class LoginMenu extends TcnMainActivity implements OnClickListener {

	private static final String TAG = "LoginMenu";
	private Button login_back,login_btn,login_more,login_more_replenish;
	private EditText login_edit,login_edit_replenish;
	private TextView login_txt,login_txt_replenish;
	private RelativeLayout login_layout = null;
	private RelativeLayout login_layout_replenish = null;
	private boolean isLand;//判断是否横屏



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginmenu);
		TcnVendIF.getInstance().LoggerDebug(TAG, "LoginMenu onCreate()");
		if (TcnVendIF.getInstance().getScreenType() == ImageController.SCREEN_TYPE_S1024X600 ||
				TcnVendIF.getInstance().getScreenType() == ImageController.SCREEN_TYPE_S1280X800) {
			isLand = true;
		} else {
			isLand=false;
		}
		if (null == login_back) {
			login_back=(Button) findViewById(R.id.login_back);
			if (null == login_back) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() login_back is null");
				return;
			}
		}

		login_back.setOnClickListener(this);

		login_layout = (RelativeLayout) findViewById(R.id.login_layout);
		login_layout_replenish = (RelativeLayout) findViewById(R.id.login_layout_replenish);

		login_more = (Button) findViewById(R.id.login_more);
		if (login_more != null) {
			login_more.setOnClickListener(this);
		}

		if (null == login_btn) {
			login_btn=(Button) findViewById(R.id.login_btn);
			if (null == login_btn) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() login_btn is null");
				return;
			}
		}

		login_btn.setOnClickListener(this);

		if (null == login_edit) {
			login_edit=(EditText) findViewById(R.id.login_edit);
			if (null == login_edit) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() login_edit is null");
				return;
			}
		}

		if (null == login_txt) {
			login_txt=(TextView) findViewById(R.id.login_txt);
			if (null == login_txt) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() login_txt is null");
				return;
			}
		}

		login_txt_replenish = (TextView) findViewById(R.id.login_txt_replenish);

		login_more_replenish = (Button) findViewById(R.id.login_more_replenish);
		if (login_more_replenish != null) {
			login_more_replenish.setOnClickListener(this);
		}

		login_edit_replenish=(EditText) findViewById(R.id.login_edit_replenish);

		initdata();

		if (isLand) {
			initKey();//横屏加载键盘
		}

		if (TcnVendIF.getInstance().isUserMainBoard()) {
			if (login_layout_replenish != null) {
			//	login_layout_replenish.setVisibility(View.GONE);
			} else {
				if (login_layout != null) {
					login_layout.setVisibility(View.VISIBLE);
				}
				if (login_txt != null) {
					login_txt.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private void initKey() {
		findViewById(R.id.number0).setOnClickListener(this);
		findViewById(R.id.number1).setOnClickListener(this);
		findViewById(R.id.number2).setOnClickListener(this);
		findViewById(R.id.number3).setOnClickListener(this);
		findViewById(R.id.number4).setOnClickListener(this);
		findViewById(R.id.number5).setOnClickListener(this);
		findViewById(R.id.number6).setOnClickListener(this);
		findViewById(R.id.number7).setOnClickListener(this);
		findViewById(R.id.number8).setOnClickListener(this);
		findViewById(R.id.number9).setOnClickListener(this);
		findViewById(R.id.lm_clear).setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		TcnVendIF.getInstance().registerListener(m_vendListener);
		//startShowTimer(60000);
	}

	@Override
	protected void onPause() {
		TcnVendIF.getInstance().unregisterListener(m_vendListener);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onDestroy()");
		if (login_back != null) {
			login_back.setOnClickListener(null);
			login_back = null;
		}

		if (login_btn != null) {
			login_btn.setOnClickListener(null);
			login_btn = null;
		}

		if (login_more != null) {
			login_more.setOnClickListener(null);
			login_more = null;
		}

		if (login_more_replenish != null) {
			login_more_replenish.setOnClickListener(null);
			login_more_replenish = null;
		}

		login_edit = null;
		login_txt = null;
		login_txt_replenish = null;
		login_edit_replenish = null;
		m_vendListener = null;
		login_layout = null;
		login_layout_replenish = null;
		super.onDestroy();
	}

	private void initdata() {
		if((TcnShareUseData.getInstance().getLoginPassword()).equals("000000")){
			login_txt.setText(R.string.tip_psw_too_simple);
			login_txt.setTextColor(Color.RED);
		} else {
			String strReplenishPassword = TcnShareUseData.getInstance().getReplenishPassword();
			if((null == strReplenishPassword) || (strReplenishPassword.length() < 1) || (strReplenishPassword).equals("000000")){
				TcnShareUseData.getInstance().setReplenishPassword(TcnShareUseData.getInstance().getLoginPassword());
			}
		}

	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.login_back) {
			this.finish();
		} else if (id == R.id.login_btn) {
			if (login_layout.getVisibility() == View.VISIBLE) {
				String login_psw =login_edit.getText().toString();
				TcnVendIF.getInstance().LoggerDebug(TAG, "------onClick() 登录 login_psw: " + login_psw);
				if (UIComBack.getInstance().getPassWordInputCount() >= 3) {
					login_txt.setText(getString(R.string.tip_psw_limit));
					login_txt.setTextColor(Color.RED);
					return;
				}
				if("".equals(login_psw)||null==login_psw){
					login_txt.setText(R.string.tip_psw_cannot_empty);
					login_txt.setTextColor(Color.RED);
				} else if((TcnShareUseData.getInstance().getLoginPassword()).equals(login_psw)){
					UIComBack.getInstance().setPassWordInputCount(0);
					Intent in=new Intent(this,MenuActivity.class);
					in.putExtra("login_type", "Admin");
					startActivity(in);
					this.finish();
				} else {
					UIComBack.getInstance().setPassWordInputCount(UIComBack.getInstance().getPassWordInputCount() + 1);
					login_txt.setText(getString(R.string.tip_psw_error)+getString(R.string.tip_psw_remain)
							+(3-UIComBack.getInstance().getPassWordInputCount())+getString(R.string.tip_psw_oppor));
					login_txt.setTextColor(Color.RED);
				}
			} else if ((login_edit_replenish != null) && (login_layout_replenish != null) && (login_layout_replenish.getVisibility() == View.VISIBLE)) {
				String lginRqpsw=login_edit_replenish.getText().toString();
				TcnVendIF.getInstance().LoggerDebug(TAG, "------onClick() lginRqpsw: " + lginRqpsw);
				if (UIComBack.getInstance().getPassardRepInputCount() >= 3) {
					if (login_txt_replenish != null) {
						login_txt_replenish.setText(getString(R.string.tip_psw_limit));
						login_txt_replenish.setTextColor(Color.RED);
					}
					return;
				}
				if("".equals(lginRqpsw)||null==lginRqpsw){
					if (login_txt_replenish != null) {
						login_txt_replenish.setText(R.string.tip_psw_cannot_empty);
						login_txt_replenish.setTextColor(Color.RED);
					}
				} else if((TcnShareUseData.getInstance().getReplenishPassword()).equals(lginRqpsw)){
					UIComBack.getInstance().setPassardRepInputCount(0);
					Intent in=new Intent(this,MenuActivity.class);
					in.putExtra("login_type", "Replenish");
					startActivity(in);
					this.finish();
				} else {
					if (login_txt_replenish != null) {
						UIComBack.getInstance().setPassardRepInputCount(UIComBack.getInstance().getPassardRepInputCount() + 1);
						login_txt_replenish.setText(getString(R.string.tip_psw_error)+getString(R.string.tip_psw_remain)
								+(3-UIComBack.getInstance().getPassardRepInputCount())+getString(R.string.tip_psw_oppor));
						login_txt_replenish.setTextColor(Color.RED);
					}
				}
			}
			else {
				TcnUtility.getToast(LoginMenu.this,getString(R.string.tip_login_type));
			}

		}  else if (id == R.id.lm_clear) {
			if (isLand) {
				if (null==login_edit || TextUtils.isEmpty(login_edit.getText())) {
				} else {
					String psw = login_edit.getText().toString();
					login_edit.setText(psw.substring(0, psw.length() - 1));
				}
			}
		} else if (id == R.id.login_more) {
			if (login_layout.getVisibility() == View.VISIBLE) {
				login_layout.setVisibility(View.GONE);
				login_txt.setVisibility(View.GONE);
			} else {

				login_layout_replenish.setVisibility(View.GONE);
				login_txt_replenish.setVisibility(View.GONE);

				login_layout.setVisibility(View.VISIBLE);
				login_txt.setVisibility(View.VISIBLE);
			}
		} else if (id == R.id.login_more_replenish) {
			if ((login_layout_replenish != null) && (login_txt_replenish != null)) {
				if (login_layout_replenish.getVisibility() == View.VISIBLE) {
					login_layout_replenish.setVisibility(View.GONE);
					login_txt_replenish.setVisibility(View.GONE);
				} else {

					login_layout.setVisibility(View.GONE);
					login_txt.setVisibility(View.GONE);

					login_layout_replenish.setVisibility(View.VISIBLE);
					login_txt_replenish.setVisibility(View.VISIBLE);
				}
			}

		}
		else {
			if (isLand) {
				String psw = login_edit.getText().toString();
				if (psw.length() < 6) {
					Button x= (Button) findViewById(id);
					login_edit.setText(psw+x.getText());
				}
			}
		}
	}

	private VendListener m_vendListener = new VendListener();
	private class VendListener implements TcnVendIF.VendEventListener {
		@Override
		public void VendEvent(VendEventInfo cEventInfo) {
			switch (cEventInfo.m_iEventID) {
				case TcnVendEventID.COMMAND_DOOR_SWITCH:
					if (cEventInfo.m_lParam1 == TcnVendIF.COMMAND_CLOSE_DOOR) {
						finish();
					}
					break;
				case TcnVendEventID.CMD_READ_DOOR_STATUS:
					if (TcnVendEventResultID.DO_CLOSE == cEventInfo.m_lParam1) {
						finish();
					}
					break;
				default:
					break;
			}
		}
	}

}
