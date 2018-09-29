package com.tcn.background;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonEdit;
import com.tcn.uicommon.button.ButtonSwitch;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;





/**
 * 密码修改界面
 * @author Administrator
 *
 */
public class PasswordManage extends TcnMainActivity implements OnClickListener{

	private static final String TAG = "PasswordManage";

	private ButtonEdit m_old_psw_login,m_new_psw_login,m_confirm_psw_login;
	private ButtonEdit m_old_psw_login_rep,m_new_psw_login_rep,m_confirm_psw_login_rep;
	private ButtonEdit m_old_psw_login_quick,m_new_psw_login_quick,m_confirm_psw_login_quick;
	private boolean bModifyLoginPwd = false;
	private boolean bModifyLoginPwdRep = false;
	private boolean bModifyLoginPwdQuick = false;
	private Titlebar m_Titlebar = null;
	private LinearLayout password_linear,password_quick_linear,password_linear_rep;
	private ButtonSwitch password_login_switch = null;
	private ButtonSwitch password_login_switch_rep = null;
	private ButtonSwitch password_quick_login_switch = null;
	private Button password_confirm = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alterpassword);
		TcnVendIF.getInstance().LoggerDebug(TAG, "PasswordManage onCreate()");

		m_Titlebar = (Titlebar) findViewById(R.id.password_titlebar);
		m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
		m_Titlebar.setButtonName(R.string.menu_password_management);
		m_Titlebar.setTitleBarListener(m_TitleBarListener);

		password_login_switch = (ButtonSwitch) findViewById(R.id.password_login_switch);
		password_login_switch.setButtonName(R.string.password_login_modify);
		password_login_switch.setButtonListener(m_ButtonSwitchClickListener);

		password_login_switch_rep = (ButtonSwitch) findViewById(R.id.password_login_switch_rep);
		password_login_switch_rep.setButtonName(R.string.password_rep_login_modify);
		password_login_switch_rep.setButtonListener(m_ButtonSwitchClickListener);

		password_quick_login_switch = (ButtonSwitch) findViewById(R.id.password_quick_login_switch);
		password_quick_login_switch.setButtonName(R.string.password_enter_login_modify);
		password_quick_login_switch.setButtonListener(m_ButtonSwitchClickListener);

		password_linear = (LinearLayout) findViewById(R.id.password_linear);
		password_linear_rep = (LinearLayout) findViewById(R.id.password_linear_rep);
		password_quick_linear = (LinearLayout) findViewById(R.id.password_quick_linear);


		password_confirm=(Button) findViewById(R.id.password_confirm);

		password_confirm.setOnClickListener(this);

		initdata();
	}



	@Override
	protected void onDestroy() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onDestroy()");

		if (m_Titlebar != null) {
			m_Titlebar.setTitleBarListener(null);
			m_Titlebar = null;
		}

		if (password_login_switch != null) {
			password_login_switch.setButtonListener(null);
			password_login_switch = null;
		}

		if (password_login_switch_rep != null) {
			password_login_switch_rep.setButtonListener(null);
			password_login_switch_rep = null;
		}

		if (password_quick_login_switch != null) {
			password_quick_login_switch.setButtonListener(null);
			password_quick_login_switch = null;
		}

		if (password_confirm != null) {
			password_confirm.setOnClickListener(null);
			password_confirm = null;
		}
		m_TitleBarListener = null;
		m_ButtonSwitchClickListener = null;
		password_linear = null;
		password_linear_rep = null;
		password_quick_linear = null;
		m_old_psw_login = null;
		m_new_psw_login = null;
		m_old_psw_login_rep = null;
		m_new_psw_login_rep = null;
		m_confirm_psw_login_rep = null;
		m_confirm_psw_login = null;
		m_old_psw_login_quick = null;
		m_new_psw_login_quick = null;
		m_confirm_psw_login_quick = null;
		super.onDestroy();
	}

	private void initdata() {
		int orientation = TcnVendIF.getInstance().getScreenOrientation();
		if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
			password_login_switch_rep.setVisibility(View.GONE);
			password_linear_rep.setVisibility(View.GONE);
		}

	}

	private void generateLoginView() {
		password_linear.removeAllViews();
		m_old_psw_login = new ButtonEdit(this);
		m_old_psw_login.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		m_old_psw_login.setButtonName("原登录密码");
		m_old_psw_login.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		LinearLayout.LayoutParams lpOldPassword = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpOldPassword.setMargins(0, 0, 0, 0);
		m_old_psw_login.setLayoutParams(lpOldPassword);
		password_linear.addView(m_old_psw_login);

		m_new_psw_login = new ButtonEdit(this);
		m_new_psw_login.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		m_new_psw_login.setButtonName("新登录密码");
		m_new_psw_login.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		LinearLayout.LayoutParams lpNewPassword = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpNewPassword.setMargins(0, 20, 0, 0);
		m_new_psw_login.setLayoutParams(lpNewPassword);
		password_linear.addView(m_new_psw_login);


		m_confirm_psw_login = new ButtonEdit(this);
		m_confirm_psw_login.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		m_confirm_psw_login.setButtonName("确认新登录密码");
		m_confirm_psw_login.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		LinearLayout.LayoutParams lpconfirmPassword = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpconfirmPassword.setMargins(0, 20, 0, 0);
		m_confirm_psw_login.setLayoutParams(lpconfirmPassword);
		password_linear.addView(m_confirm_psw_login);

		password_linear.setVisibility(View.VISIBLE);
	}

	private void generateLoginViewRep() {
		password_linear_rep.removeAllViews();
		m_old_psw_login_rep = new ButtonEdit(this);
		m_old_psw_login_rep.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		m_old_psw_login_rep.setButtonName("原登录密码");
		m_old_psw_login_rep.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		LinearLayout.LayoutParams lpOldPassword = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpOldPassword.setMargins(0, 0, 0, 0);
		m_old_psw_login_rep.setLayoutParams(lpOldPassword);
		password_linear_rep.addView(m_old_psw_login_rep);

		m_new_psw_login_rep = new ButtonEdit(this);
		m_new_psw_login_rep.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		m_new_psw_login_rep.setButtonName("新登录密码");
		m_new_psw_login_rep.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		LinearLayout.LayoutParams lpNewPassword = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpNewPassword.setMargins(0, 20, 0, 0);
		m_new_psw_login_rep.setLayoutParams(lpNewPassword);
		password_linear_rep.addView(m_new_psw_login_rep);


		m_confirm_psw_login_rep = new ButtonEdit(this);
		m_confirm_psw_login_rep.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		m_confirm_psw_login_rep.setButtonName("确认新登录密码");
		m_confirm_psw_login_rep.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		LinearLayout.LayoutParams lpconfirmPassword = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpconfirmPassword.setMargins(0, 20, 0, 0);
		m_confirm_psw_login_rep.setLayoutParams(lpconfirmPassword);
		password_linear_rep.addView(m_confirm_psw_login_rep);

		password_linear_rep.setVisibility(View.VISIBLE);
	}

	private void generateLoginQuickView() {
		password_quick_linear.removeAllViews();
		m_old_psw_login_quick = new ButtonEdit(this);
		m_old_psw_login_quick.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		m_old_psw_login_quick.setButtonName("原进登录界面密码");
		m_old_psw_login_quick.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		LinearLayout.LayoutParams lpOldPassword = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpOldPassword.setMargins(0, 0, 0, 0);
		m_old_psw_login_quick.setLayoutParams(lpOldPassword);
		password_quick_linear.addView(m_old_psw_login_quick);

		m_new_psw_login_quick = new ButtonEdit(this);
		m_new_psw_login_quick.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		m_new_psw_login_quick.setButtonName("新进登录界面密码");
		m_new_psw_login_quick.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		LinearLayout.LayoutParams lpNewPassword = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpNewPassword.setMargins(0, 20, 0, 0);
		m_new_psw_login_quick.setLayoutParams(lpNewPassword);
		password_quick_linear.addView(m_new_psw_login_quick);


		m_confirm_psw_login_quick = new ButtonEdit(this);
		m_confirm_psw_login_quick.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		m_confirm_psw_login_quick.setButtonName("确认新进登录界面密码");
		m_confirm_psw_login_quick.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		LinearLayout.LayoutParams lpconfirmPassword = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpconfirmPassword.setMargins(0, 20, 0, 0);
		m_confirm_psw_login_quick.setLayoutParams(lpconfirmPassword);
		password_quick_linear.addView(m_confirm_psw_login_quick);

		password_quick_linear.setVisibility(View.VISIBLE);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (R.id.password_confirm == id) {
			if (!bModifyLoginPwd && !bModifyLoginPwdRep && !bModifyLoginPwdQuick) {
				TcnUtility.getToast(this, getString(R.string.tip_psw_cannot_empty));
				return;
			}
			if (bModifyLoginPwd) {
				String old_psw = m_old_psw_login.getButtonEditText();
				if (!(TcnShareUseData.getInstance().getLoginPassword()).equals(old_psw)) {
					m_old_psw_login.setErrText(getString(R.string.tip_psw_error));
					return;
				}
				String new_psw = m_new_psw_login.getButtonEditText();
				if ((new_psw == null) || (new_psw.length() < 6)) {
					m_new_psw_login.setErrText(getString(R.string.tip_psw_cannotless_than6));
					return;
				}

				String confirm_psw = m_confirm_psw_login.getButtonEditText();
				if (!new_psw.equals(confirm_psw)) {
					m_confirm_psw_login.setErrText(getString(R.string.tip_psw_not_same));
					return;
				}
				TcnShareUseData.getInstance().setLoginPassword(new_psw);
			}

			if (bModifyLoginPwdRep) {
				String old_psw = m_old_psw_login_rep.getButtonEditText();
				if (!(TcnShareUseData.getInstance().getReplenishPassword()).equals(old_psw)) {
					m_old_psw_login_rep.setErrText(getString(R.string.tip_psw_error));
					return;
				}
				String new_psw = m_new_psw_login_rep.getButtonEditText();
				if ((new_psw == null) || (new_psw.length() < 6)) {
					m_new_psw_login_rep.setErrText(getString(R.string.tip_psw_cannotless_than6));
					return;
				}

				String confirm_psw = m_confirm_psw_login_rep.getButtonEditText();
				if (!new_psw.equals(confirm_psw)) {
					m_confirm_psw_login_rep.setErrText(getString(R.string.tip_psw_not_same));
					return;
				}
				TcnShareUseData.getInstance().setReplenishPassword(new_psw);
			}

			if (bModifyLoginPwdQuick) {
				String old_psw_quick = m_old_psw_login_quick.getButtonEditText();
				if (!(TcnShareUseData.getInstance().getQuickEntrPassword()).equals(old_psw_quick)) {
					m_old_psw_login_quick.setErrText(getString(R.string.tip_psw_error));
					return;
				}
				String new_psw_quick = m_new_psw_login_quick.getButtonEditText();
				if ((new_psw_quick == null) || (new_psw_quick.length() < 6)) {
					m_new_psw_login_quick.setErrText(getString(R.string.tip_psw_cannotless_than6));
					return;
				}

				String confirm_psw_quick = m_confirm_psw_login_quick.getButtonEditText();
				if (!new_psw_quick.equals(confirm_psw_quick)) {
					m_confirm_psw_login_quick.setErrText(getString(R.string.tip_psw_not_same));
					return;
				}
				TcnShareUseData.getInstance().setQuickEntrPassword(new_psw_quick);
			}
			TcnUtility.getToast(this, getString(R.string.tip_modify_success));
			PasswordManage.this.finish();
		}
	}


	private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
	private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

		@Override
		public void onClick(View v, int buttonId) {
			if (Titlebar.BUTTON_ID_BACK == buttonId) {
				PasswordManage.this.finish();
			}
		}
	}

	private ButtonSwitchClickListener m_ButtonSwitchClickListener= new ButtonSwitchClickListener();
	private class ButtonSwitchClickListener implements ButtonSwitch.ButtonListener {
		@Override
		public void onSwitched(View v, boolean isSwitchOn) {
			int id = v.getId();
			if (R.id.password_login_switch == id) {
				if (isSwitchOn) {
					generateLoginView();
					bModifyLoginPwd = true;
				} else {
					bModifyLoginPwd = false;
					password_linear.setVisibility(View.GONE);
				}
			} else if (R.id.password_login_switch_rep == id) {
				if (isSwitchOn) {
					generateLoginViewRep();
					bModifyLoginPwdRep = true;
				} else {
					bModifyLoginPwdRep = false;
					password_linear_rep.setVisibility(View.GONE);
				}
			}
			else if (R.id.password_quick_login_switch == id) {
				if (isSwitchOn) {
					generateLoginQuickView();
					bModifyLoginPwdQuick = true;
				} else {
					bModifyLoginPwdQuick = false;
					password_quick_linear.setVisibility(View.GONE);
				}
			} else {

			}
		}
	}

}
