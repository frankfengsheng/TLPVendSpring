package com.tcn.background;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonEdit;
import com.tcn.uicommon.button.ButtonSwitch;
import com.tcn.uicommon.dialog.DialogInput;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;




/**
 * 信息配置界面（只用于显示机器id，ip地址，socket号）
 * @author Administrator
 *
 */
public class FuncConfig extends TcnMainActivity implements OnClickListener {

	protected static final String TAG = "FuncConfig";
	private static final int CMD_SHIP_FAIL_COUNT_LOCK     = 1;
	private static final int CMD_GOODSCODE_SHIPTYPE       = 2;
	private static final int CMD_CLOSE_DELAY_SET          = 3;

	private Titlebar m_Titlebar = null;

	protected LinearLayout m_showByGoodsCode_linear = null;
	protected ButtonEdit func_ship_fail_count_lock = null;
	protected ButtonEdit func_card_key_set = null;
	protected ButtonEdit func_close_delay_set = null;
	private ButtonEdit m_goodsCode_shipType = null;

	protected Button func_confirm;

	protected InputMethodManager manager;

	protected int singleitem=0;





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.func_config);
		TcnVendIF.getInstance().LoggerDebug(TAG, "onCreate()");
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		deInitView();
	}

	protected void initView() {
		manager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

		m_Titlebar = (Titlebar) findViewById(R.id.menu_func_titlebar);
		if (m_Titlebar != null) {
			m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
			m_Titlebar.setButtonName(R.string.menu_func_set);
			m_Titlebar.setTitleBarListener(m_TitleBarListener);
		}

		func_ship_fail_count_lock = (ButtonEdit) findViewById(R.id.func_ship_fail_count_lock);
		func_ship_fail_count_lock.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
		func_ship_fail_count_lock.setButtonName(getString(R.string.func_set_ship_fail_count));
		func_ship_fail_count_lock.setButtonListener(m_ButtonClickListener);
		func_ship_fail_count_lock.setButtonText(String.valueOf(TcnShareUseData.getInstance().getShipFailCountLock()));


		m_showByGoodsCode_linear=(LinearLayout) findViewById(R.id.func_show_by_goods_code_linear);

		if (TcnShareUseData.getInstance().isShowByGoodsCode()) {
			generateCodeShipType();
		}

		func_confirm = (Button) findViewById(R.id.func_confirm);
		func_confirm.setOnClickListener(this);

	}

	protected void deInitView() {
		manager = null;
		if (m_Titlebar != null) {
			m_Titlebar.removeButtonListener();
			m_Titlebar = null;
		}
		if (func_ship_fail_count_lock != null) {
			func_ship_fail_count_lock.setButtonListener(null);
			func_ship_fail_count_lock = null;
		}

		if (m_goodsCode_shipType != null) {
			m_goodsCode_shipType.setButtonListener(null);
			m_goodsCode_shipType = null;
		}

		if (func_close_delay_set != null) {
			func_close_delay_set.setButtonListener(null);
			func_close_delay_set = null;
		}

		if (func_confirm != null) {
			func_confirm.setOnClickListener(null);
			func_confirm = null;
		}

		m_showByGoodsCode_linear = null;
		m_ButtonClickListener = null;
		m_SwitchButtonListener = null;
	}



	@Override
	public void onClick(View v) {
		int mId = v.getId();
		if (R.id.func_confirm == mId) {
			saveData();
			finish();
		} else if (R.id.func_coin_prestorage == mId) {

		} else {

		}
	}

	private void saveData() {

	}

	private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
	private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

		@Override
		public void onClick(View v, int buttonId) {
			if (Titlebar.BUTTON_ID_BACK == buttonId) {
				FuncConfig.this.finish();
			}
		}
	}

	private void generateCodeShipType() {
		m_showByGoodsCode_linear.removeAllViews();

		m_goodsCode_shipType = new ButtonEdit(this);
		m_goodsCode_shipType.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
		m_goodsCode_shipType.setButtonName(getString(R.string.func_set_select_ship_type));
		m_goodsCode_shipType.setButtonNameTextSize(20);
		m_goodsCode_shipType.setButtonEditTextSize(20);
		LinearLayout.LayoutParams lpShipType = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpShipType.setMargins(0, 40, 0, 0);
		m_goodsCode_shipType.setLayoutParams(lpShipType);
		m_goodsCode_shipType.setButtonText(String.valueOf(TcnShareUseData.getInstance().getGoodsCodeShipType()));
		m_goodsCode_shipType.setButtonListener(m_ButtonClickListener);
		m_showByGoodsCode_linear.addView(m_goodsCode_shipType);


	}

	protected void setSelectItem(int cmdType,int which, String[] str) {
		if (null == str || which < 0 || str.length <= which) {
			TcnVendIF.getInstance().LoggerError(TAG, "setSelectItem which: "+which);
		}
		if (CMD_SHIP_FAIL_COUNT_LOCK == cmdType) {
			if (!TcnVendIF.getInstance().isDigital(str[which])) {
				TcnVendIF.getInstance().LoggerError(TAG, "setSelectItem is not isDigital");
				return;
			}
			TcnShareUseData.getInstance().setShipContinFailCount(0);
			TcnShareUseData.getInstance().setShipFailCountLock(Integer.valueOf(str[which]).intValue());
		} else if (CMD_GOODSCODE_SHIPTYPE == cmdType) {
			TcnShareUseData.getInstance().setGoodsCodeShipType(str[which]);
		} else if (CMD_CLOSE_DELAY_SET == cmdType) {
			TcnVendIF.getInstance().setCloseDelayTime(str[which]);
		}
		else {

		}
	}

	protected void showItemDialog(final int cmdType,final EditText v,final String[] str) {
		int checkedItem = 0;
		if (CMD_SHIP_FAIL_COUNT_LOCK == cmdType) {
			for (int i = 0; i < str.length; i++) {
				if (str[i].equals(String.valueOf(TcnShareUseData.getInstance().getShipFailCountLock()))) {
					checkedItem = i;
					break;
				}
			}

		} else if (CMD_GOODSCODE_SHIPTYPE == cmdType) {
			for (int i = 0; i < str.length; i++) {
				if (str[i].equals(TcnShareUseData.getInstance().getGoodsCodeShipType())) {
					checkedItem = i;
					break;
				}
			}

		} else if (CMD_CLOSE_DELAY_SET == cmdType) {
			for (int i = 0; i < str.length; i++) {
				if (str[i].equals(TcnShareUseData.getInstance().getCloseDelayTime())) {
					checkedItem = i;
					break;
				}
			}

		}
		else {

		}
		singleitem=0;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.please_choose));
		builder.setSingleChoiceItems(str, checkedItem, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				singleitem=which;
			}
		});
		builder.setPositiveButton(getString(R.string.backgroound_ensure), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				setSelectItem(cmdType,singleitem,str);
				v.setText(str[singleitem]);
			}
		});
		builder.setNegativeButton(getString(R.string.backgroound_cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

			}
		});
		builder.show();
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

	protected ButtonClickListener m_ButtonClickListener= new ButtonClickListener();
	protected class ButtonClickListener implements ButtonEdit.ButtonListener {
		@Override
		public void onClick(View v, int buttonId) {
			if (null == v) {
				return;
			}
			int id = v.getId();
			if (R.id.func_ship_fail_count_lock == id) {
				if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
					showItemDialog(CMD_SHIP_FAIL_COUNT_LOCK,func_ship_fail_count_lock.getButtonEdit(), TcnCommon.SHIP_FAIL_CONTIN_COUNT_LOCK);
				}
			} else if (R.id.func_close_delay_set == id) {
				if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
					showItemDialog(CMD_CLOSE_DELAY_SET,func_close_delay_set.getButtonEdit(), TcnCommon.TIME_DELAY_CLOSE_SELECT);
				}
			}
			else {
				if (m_goodsCode_shipType != null) {
					if (m_goodsCode_shipType.getId() == id) {
						if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
							showItemDialog(CMD_GOODSCODE_SHIPTYPE,m_goodsCode_shipType.getButtonEdit(), TcnCommon.GOODSCODE_SHIPTYPE);
						}
					}
				}
			}
		}
	}

	protected ButtonSwitchClickListener m_SwitchButtonListener= new ButtonSwitchClickListener();
	protected class ButtonSwitchClickListener implements ButtonSwitch.ButtonListener {
		@Override
		public void onSwitched(View v, boolean isSwitchOn) {

			int id = v.getId();
			if (R.id.func_show_by_goods_code == id) {
				TcnVendIF.getInstance().setShowByGoodsCode(isSwitchOn);
				if (isSwitchOn) {
					generateCodeShipType();
				} else {
					m_showByGoodsCode_linear.removeAllViews();
				}
			} else if (R.id.func_app_vend_enable == id) {
				TcnShareUseData.getInstance().setAppVerify(isSwitchOn);
			} else {

			}
		}
	}

	//点击空白处输入法消失1
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}
	//点击空白处输入法消失2

	public  boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			//获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

}
