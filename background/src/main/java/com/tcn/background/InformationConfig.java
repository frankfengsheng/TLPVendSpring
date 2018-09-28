package com.tcn.background;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonEdit;
import com.tcn.uicommon.button.ButtonSwitch;
import com.tcn.uicommon.view.TcnMainActivity;





/**
 * 信息配置界面（只用于显示机器id，ip地址，socket号）
 * @author Administrator
 *
 */
public class InformationConfig extends TcnMainActivity implements OnClickListener {

	protected static final String TAG = "InformationConfig";

	protected long starttime = 0;

	protected EditText ip_address_edit,socket_edit,machine_id_edit;

	protected RelativeLayout info_remote_ad_layout = null;
	protected Button   info_confirm,info_back;
	protected Button info_unit_price_btn;
	protected EditText info_unit_price_edit;
	protected ButtonEdit info_advert_poll_time = null;
	protected ButtonEdit info_standby_image_time = null;
	protected ButtonEdit info_image_play_interval_time = null;
	protected ButtonEdit info_item_count_page = null;
	protected ButtonEdit info_slotno_digt_count = null;
	protected ButtonEdit info_price_point_count = null;
	protected ButtonEdit info_paytime_select = null;
	protected ButtonEdit info_reboot_time = null;
	protected ButtonEdit info_default_ad = null;
	protected ButtonEdit info_pay_tips = null;
	protected ButtonEdit info_pay_first_tips = null;
	protected ButtonEdit info_pay_second_tips = null;
	protected ButtonEdit info_welcom_text = null;
	protected ButtonEdit info_keyboard_text = null;
	protected ButtonEdit info_keyboard_input_tips = null;
	protected String m_strAdTips = "";
	protected String m_strPayTips = "";
	protected String m_strPayFirstTips = "";
	protected String m_strPaySecondTips = "";
	protected String m_strWelcome = "";
	protected String m_strKeyboardText = "";
	protected String m_strKeyboardInputTips = "";
	protected String m_strUnitPrice;
	protected int m_iPayTime = 90;
	protected String m_strAdvetPollTime = "";
	protected String ip,socket,tel,com_name,id;

	protected int singleitem=0;

	protected ButtonSwitch m_ScreenProtectSwitch;
	protected ButtonSwitch m_page_display;
	protected ButtonSwitch m_info_reboot;
	protected ButtonSwitch m_voiceOpen;
	protected ButtonSwitch m_singleCode;
	protected ButtonSwitch m_BtnAdPositionSwitch;
	protected ButtonSwitch m_BtnAdStandbyFull;
	protected ButtonSwitch m_BtnShowType;
	protected ButtonSwitch m_full_screen;
	protected ButtonSwitch m_show_shopping;
	protected ButtonSwitch m_app_running_check;
	protected ButtonSwitch m_showTapScreenText;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLayout();
		TcnVendIF.getInstance().LoggerDebug(TAG, "onCreate()");
		getSharedpreference();
		initView();
	}

	protected void setLayout() {
		setContentView(R.layout.information);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		deInitView();
	}

	protected void getSharedpreference() {
		id = TcnShareUseData.getInstance().getMachineID();
		ip = TcnShareUseData.getInstance().getIPAddress();
		socket =String.valueOf(TcnShareUseData.getInstance().getSocketPort());
	}

	protected void initView() {
 		/*----------------编辑框----------------*/
		ip_address_edit=(EditText) findViewById(R.id.ip_address_edit);
		ip_address_edit.setText(ip);
		socket_edit=(EditText) findViewById(R.id.socket_edit);
		socket_edit.setText(socket);
		machine_id_edit=(EditText) findViewById(R.id.machine_id_edit);
		if("".equals(id)||null==id){
		}else{
			machine_id_edit.setText(id);
		}

		m_strAdTips = TcnShareUseData.getInstance().getAdvertText();
		info_default_ad = (ButtonEdit) findViewById(R.id.info_default_ad);
		info_default_ad.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		info_default_ad.setButtonName(R.string.info_ad_set);
		m_strAdTips = TcnShareUseData.getInstance().getAdvertText();
		info_default_ad.setButtonText(m_strAdTips);

		info_pay_tips = (ButtonEdit) findViewById(R.id.info_pay_tips);
		info_pay_tips.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		info_pay_tips.setButtonName(R.string.info_pay_tips);
		m_strPayTips = TcnShareUseData.getInstance().getPayTips();
		info_pay_tips.setButtonText(m_strPayTips);

		info_pay_first_tips = (ButtonEdit) findViewById(R.id.info_pay_first_tips);
		info_pay_first_tips.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		info_pay_first_tips.setButtonName(R.string.info_pay_first_tips);
		m_strPayFirstTips = TcnShareUseData.getInstance().getPayFirstQrcodeTips();
		info_pay_first_tips.setButtonText(m_strPayFirstTips);

		info_pay_second_tips = (ButtonEdit) findViewById(R.id.info_pay_second_tips);
		info_pay_second_tips.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		info_pay_second_tips.setButtonName(R.string.info_pay_second_tips);
		m_strPaySecondTips = TcnShareUseData.getInstance().getPaySecondQrcodeTips();
		info_pay_second_tips.setButtonText(m_strPaySecondTips);

		info_welcom_text = (ButtonEdit) findViewById(R.id.info_welcom_text);
		info_welcom_text.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		info_welcom_text.setButtonName(R.string.info_welcome_set);
		m_strWelcome = TcnShareUseData.getInstance().getWeclcome();
		info_welcom_text.setButtonText(m_strWelcome);


		info_keyboard_text = (ButtonEdit) findViewById(R.id.info_keyboard_text);
		info_keyboard_text.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		info_keyboard_text.setButtonName(R.string.info_keyboard_text_set);
		m_strKeyboardText = TcnShareUseData.getInstance().getKeyBoardText();
		info_keyboard_text.setButtonText(m_strKeyboardText);

		info_keyboard_input_tips = (ButtonEdit) findViewById(R.id.info_keyboard_input_tips);
		info_keyboard_input_tips.setButtonType(ButtonEdit.BUTTON_TYPE_EDIT);
		info_keyboard_input_tips.setButtonName(R.string.info_keyboard_input_tips);
		m_strKeyboardInputTips = TcnShareUseData.getInstance().getKeyBoardInputTips();
		info_keyboard_input_tips.setButtonText(m_strKeyboardInputTips);

		info_unit_price_btn = (Button) findViewById(R.id.info_unit_price_btn);
		info_unit_price_edit = (EditText) findViewById(R.id.info_unit_price_edit);
		m_strUnitPrice =  TcnShareUseData.getInstance().getUnitPrice();
		info_unit_price_btn.setOnTouchListener(new View.OnTouchListener() {
			//按住和松开的标识
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showPriceUnitDialog(info_unit_price_edit, TcnCommon.PRICE_UNIT);
				}
				return false;
			}
		});
		info_unit_price_edit.setText(m_strUnitPrice);
		info_paytime_select = (ButtonEdit) findViewById(R.id.info_paytime_select);
		info_paytime_select.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
		info_paytime_select.setButtonName(R.string.info_pay_time);
		info_paytime_select.setButtonListener(m_ButtonClickListener);
		m_iPayTime = TcnShareUseData.getInstance().getPayTime();
		info_paytime_select.setButtonText(String.valueOf(m_iPayTime));

		info_reboot_time = (ButtonEdit) findViewById(R.id.info_reboot_time);
		info_reboot_time.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
		info_reboot_time.setButtonName(R.string.info_reboot_time);
		info_reboot_time.setButtonListener(m_ButtonClickListener);
		info_reboot_time.setButtonText(String.valueOf(TcnShareUseData.getInstance().getRebootTime()));

		info_advert_poll_time = (ButtonEdit) findViewById(R.id.info_advert_poll_time);
		info_advert_poll_time.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
		info_advert_poll_time.setButtonName(R.string.info_advert_poll_time);
		info_advert_poll_time.setButtonListener(m_ButtonClickListener);
		m_strAdvetPollTime = getAdvertDisplayTime();
		info_advert_poll_time.setButtonText(m_strAdvetPollTime);
		info_advert_poll_time.setVisibility(View.GONE);

		info_standby_image_time = (ButtonEdit) findViewById(R.id.info_standby_image_time);
		info_standby_image_time.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
		info_standby_image_time.setButtonName(R.string.info_standby_image_time);
		info_standby_image_time.setButtonListener(m_ButtonClickListener);
		info_standby_image_time.setButtonText(TcnShareUseData.getInstance().getStandbyImageTime()+getString(R.string.ui_pay_seconds));

		info_image_play_interval_time = (ButtonEdit) findViewById(R.id.info_image_play_interval_time);
		info_image_play_interval_time.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
		info_image_play_interval_time.setButtonName(R.string.info_play_image_interval_time);
		info_image_play_interval_time.setButtonListener(m_ButtonClickListener);
		info_image_play_interval_time.setButtonText(TcnShareUseData.getInstance().getImagePlayIntervalTime()+getString(R.string.ui_pay_seconds));

		info_item_count_page = (ButtonEdit) findViewById(R.id.info_item_count_page);
		if (info_item_count_page != null) {
			info_item_count_page.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
			info_item_count_page.setButtonName(R.string.info_item_count_by_paging);
			info_item_count_page.setButtonListener(m_ButtonClickListener);
			info_item_count_page.setButtonText(String.valueOf(TcnShareUseData.getInstance().getItemCountEveryPage()));
		}

		info_slotno_digt_count = (ButtonEdit) findViewById(R.id.info_slotno_digt_count);
		if (info_slotno_digt_count != null) {
			info_slotno_digt_count.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
			info_slotno_digt_count.setButtonName(R.string.info_slotno_digit_count);
			info_slotno_digt_count.setButtonListener(m_ButtonClickListener);
			info_slotno_digt_count.setButtonText(String.valueOf(TcnShareUseData.getInstance().getSlotNoDigitCount()));
		}

		info_price_point_count = (ButtonEdit) findViewById(R.id.info_price_point_count);
		if (info_price_point_count != null) {
			info_price_point_count.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
			info_price_point_count.setButtonName(R.string.info_price_point_count);
			info_price_point_count.setButtonListener(m_ButtonClickListener);
			info_price_point_count.setButtonText(String.valueOf(TcnShareUseData.getInstance().getPricePointCount()));
			if ((TcnShareUseData.getInstance().getTcnDataType()).equals(TcnConstant.DATA_TYPE[0])
					|| (TcnShareUseData.getInstance().getTcnDataType()).equals(TcnConstant.DATA_TYPE[1])) {

			} else {
				info_price_point_count.setVisibility(View.VISIBLE);
			}
		}

		m_ScreenProtectSwitch=(ButtonSwitch) findViewById(R.id.info_screen_protect);
		m_ScreenProtectSwitch.setButtonName(R.string.machine_screen_protection);
		m_ScreenProtectSwitch.setButtonListener(m_ButtonSwitchClickListener);
		m_ScreenProtectSwitch.setSwitchState(TcnShareUseData.getInstance().isShowScreenProtect());

		m_page_display=(ButtonSwitch) findViewById(R.id.page_display);
		if (m_page_display != null) {
			m_page_display.setButtonName(R.string.info_paging_display);
			m_page_display.setButtonListener(m_ButtonSwitchClickListener);
			m_page_display.setSwitchState(TcnShareUseData.getInstance().isPageDisplay());
		}


		m_BtnAdPositionSwitch = (ButtonSwitch) findViewById(R.id.info_advert_position);
		m_BtnAdPositionSwitch.setButtonName(R.string.info_advert_position);
		m_BtnAdPositionSwitch.setButtonListener(m_ButtonSwitchClickListener);
		m_BtnAdPositionSwitch.setSwitchState(TcnShareUseData.getInstance().isAdvertOnScreenBottom());

		m_BtnAdStandbyFull = (ButtonSwitch) findViewById(R.id.info_advert_standby);
		m_BtnAdStandbyFull.setButtonName(R.string.info_advert_standby);
		m_BtnAdStandbyFull.setButtonListener(m_ButtonSwitchClickListener);
		m_BtnAdStandbyFull.setSwitchState(TcnShareUseData.getInstance().isStandbyImageFullScreen());

		m_BtnShowType = (ButtonSwitch) findViewById(R.id.info_show_type);
		if (m_BtnShowType != null) {
			m_BtnShowType.setButtonName(R.string.info_show_type);
			m_BtnShowType.setButtonListener(m_ButtonSwitchClickListener);
			m_BtnShowType.setSwitchState(TcnShareUseData.getInstance().isShowType());
		}

		m_full_screen = (ButtonSwitch) findViewById(R.id.info_full_screen);

		m_full_screen.setButtonName(R.string.machine_full_screen);
		m_full_screen.setButtonListener(m_ButtonSwitchClickListener);
		m_full_screen.setSwitchState(TcnShareUseData.getInstance().isFullScreen());

		m_voiceOpen=(ButtonSwitch) findViewById(R.id.info_voice);
		m_voiceOpen.setButtonName(R.string.machine_voice_prompt);
		m_voiceOpen.setButtonListener(m_ButtonSwitchClickListener);
		m_voiceOpen.setSwitchState(TcnShareUseData.getInstance().isVoicePrompt());

		m_singleCode=(ButtonSwitch) findViewById(R.id.info_show_single_code);
		m_singleCode.setButtonName(R.string.info_show_single_code);
		m_singleCode.setButtonListener(m_ButtonSwitchClickListener);
		m_singleCode.setSwitchState(TcnShareUseData.getInstance().isShowSingleQRCode());

		m_info_reboot=(ButtonSwitch) findViewById(R.id.info_reboot);
		m_info_reboot.setSwitchState(false);
		m_info_reboot.setButtonName(R.string.machine_reboot);
		m_info_reboot.setButtonListener(m_ButtonSwitchClickListener);

		m_show_shopping=(ButtonSwitch) findViewById(R.id.info_show_shopping);
		m_show_shopping.setButtonName(R.string.machine_show_shopping);
		m_show_shopping.setButtonListener(m_ButtonSwitchClickListener);
		m_show_shopping.setSwitchState(TcnShareUseData.getInstance().isShowShopping());

		m_app_running_check=(ButtonSwitch) findViewById(R.id.info_app_running_check);
		m_app_running_check.setButtonName(R.string.info_app_running_check);
		m_app_running_check.setButtonListener(m_ButtonSwitchClickListener);
		m_app_running_check.setSwitchState(TcnShareUseData.getInstance().isAppForegroundCheck());

		m_showTapScreenText=(ButtonSwitch) findViewById(R.id.info_show_tap_screen_text);
		m_showTapScreenText.setButtonName(R.string.info_show_tap_text);
		m_showTapScreenText.setButtonListener(m_ButtonSwitchClickListener);
		m_showTapScreenText.setSwitchState(TcnShareUseData.getInstance().isShowTapScreenText());

		info_confirm=(Button) findViewById(R.id.info_confirm);
		info_confirm.setOnClickListener(this);
		info_back=(Button) findViewById(R.id.info_back);
		info_back.setOnClickListener(this);

		initScreenShowSet();

		if (TcnShareUseData.getInstance().isShowSingleQRCode()) {
			info_pay_first_tips.setVisibility(View.GONE);
			info_pay_second_tips.setVisibility(View.GONE);
		} else {
			info_pay_first_tips.setVisibility(View.VISIBLE);
			info_pay_second_tips.setVisibility(View.VISIBLE);
		}
	}

	protected void deInitView() {

		if (info_unit_price_btn != null) {
			info_unit_price_btn.setOnTouchListener(null);
			info_unit_price_btn = null;
		}

		if (info_paytime_select != null) {
			info_paytime_select.setButtonListener(null);
			info_paytime_select = null;
		}

		if (info_reboot_time != null) {
			info_reboot_time.setButtonListener(null);
			info_reboot_time = null;
		}

		if (info_default_ad != null) {
			info_default_ad.setButtonListener(null);
			info_default_ad = null;
		}

		if (info_pay_tips != null) {
			info_pay_tips.setButtonListener(null);
			info_pay_tips = null;
		}

		if (info_pay_first_tips != null) {
			info_pay_first_tips.setButtonListener(null);
			info_pay_first_tips = null;
		}

		if (info_pay_second_tips != null) {
			info_pay_second_tips.setButtonListener(null);
			info_pay_second_tips = null;
		}

		if (info_welcom_text != null) {
			info_welcom_text.setButtonListener(null);
			info_welcom_text = null;
		}

		if (info_keyboard_text != null) {
			info_keyboard_text.setButtonListener(null);
			info_keyboard_text = null;
		}

		if (info_keyboard_input_tips != null) {
			info_keyboard_input_tips.setButtonListener(null);
			info_keyboard_input_tips = null;
		}

		if (info_advert_poll_time != null) {
			info_advert_poll_time.setButtonListener(null);
			info_advert_poll_time = null;
		}

		if (info_standby_image_time != null) {
			info_standby_image_time.setButtonListener(null);
			info_standby_image_time = null;
		}

		if (info_image_play_interval_time != null) {
			info_image_play_interval_time.setButtonListener(null);
			info_image_play_interval_time = null;
		}

		if (m_ScreenProtectSwitch != null) {
			m_ScreenProtectSwitch.setButtonListener(null);
			m_ScreenProtectSwitch = null;
		}

		if (m_BtnAdPositionSwitch != null) {
			m_BtnAdPositionSwitch.setButtonListener(null);
			m_BtnAdPositionSwitch = null;
		}

		if (m_BtnAdStandbyFull != null) {
			m_BtnAdStandbyFull.setButtonListener(null);
			m_BtnAdStandbyFull = null;
		}

		if (m_BtnShowType != null) {
			m_BtnShowType.setButtonListener(null);
			m_BtnShowType = null;
		}

		if (m_full_screen != null) {
			m_full_screen.setButtonListener(null);
			m_full_screen = null;
		}

		if (m_voiceOpen != null) {
			m_voiceOpen.setButtonListener(null);
			m_voiceOpen = null;
		}

		if (m_info_reboot != null) {
			m_info_reboot.setButtonListener(null);
			m_info_reboot = null;
		}

		if (m_singleCode != null) {
			m_singleCode.setButtonListener(null);
			m_singleCode = null;
		}

		if (m_show_shopping != null) {
			m_show_shopping.setButtonListener(null);
			m_show_shopping = null;
		}

		if (m_app_running_check != null) {
			m_app_running_check.setButtonListener(null);
			m_app_running_check = null;
		}

		if (info_confirm != null) {
			info_confirm.setOnClickListener(null);
			info_confirm = null;
		}

		if (info_back != null) {
			info_back.setOnClickListener(null);
			info_back = null;
		}

		if (info_item_count_page != null) {
			info_item_count_page.setButtonListener(null);
			info_item_count_page = null;
		}

		if (info_slotno_digt_count != null) {
			info_slotno_digt_count.setButtonListener(null);
			info_slotno_digt_count = null;
		}

		if (info_price_point_count != null) {
			info_price_point_count.setButtonListener(null);
			info_price_point_count = null;
		}
		ip_address_edit = null;
		socket_edit = null;
		machine_id_edit = null;
		info_remote_ad_layout = null;
		info_unit_price_edit = null;
		info_default_ad = null;
		info_pay_tips = null;
		info_welcom_text = null;
		info_keyboard_text = null;
		m_strAdTips = null;
		m_strPayTips = null;
		m_strPayFirstTips = null;
		m_strPaySecondTips = null;
		m_strWelcome = null;
		m_strKeyboardText = null;
		m_strKeyboardInputTips = null;
		m_strUnitPrice = null;
		m_strAdvetPollTime = null;
		ip = null;
		socket = null;
		tel = null;
		com_name = null;
		id = null;
		m_ButtonClickListener = null;
		m_ButtonSwitchClickListener = null;

		if (info_price_point_count != null) {
			info_price_point_count.setButtonListener(null);
			info_price_point_count = null;
		}

		if (m_page_display != null) {
			m_page_display.setButtonListener(null);
			m_page_display = null;
		}

		if (m_showTapScreenText != null) {
			m_showTapScreenText.setButtonListener(null);
			m_showTapScreenText = null;
		}
	}

	protected void initScreenShowSet() {
		if (TcnVendIF.getInstance().getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
			m_full_screen.setVisibility(View.VISIBLE);
			if (TcnShareUseData.getInstance().isFullScreen()) {
				m_BtnAdPositionSwitch.setVisibility(View.GONE);
				m_BtnAdStandbyFull.setVisibility(View.GONE);
			} else {
				m_BtnAdPositionSwitch.setVisibility(View.VISIBLE);
				m_BtnAdStandbyFull.setVisibility(View.VISIBLE);
			}
		} else {
			m_full_screen.setVisibility(View.GONE);
			m_BtnAdPositionSwitch.setVisibility(View.GONE);
			m_BtnAdStandbyFull.setVisibility(View.GONE);
		}

		if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())) {
			if (m_show_shopping != null) {
				m_show_shopping.setVisibility(View.GONE);
			}
		}

	}

	protected void getData(){
		ip=ip_address_edit.getText().toString().trim();
		socket=socket_edit.getText().toString().trim();
//		tel=phone_number_edit.getText().toString().trim();
//		com_name=company_name_edit.getText().toString().trim();
		id=machine_id_edit.getText().toString().trim();
		m_strAdTips = info_default_ad.getButtonEditText();
		m_strPayTips = info_pay_tips.getButtonEditText();
		m_strPayFirstTips = info_pay_first_tips.getButtonEditText();
		m_strPaySecondTips = info_pay_second_tips.getButtonEditText();
		m_strWelcome = info_welcom_text.getButtonEditText();
		m_strKeyboardText = info_keyboard_text.getButtonEditText();
		m_strKeyboardInputTips = info_keyboard_input_tips.getButtonEditText();
	}

	@Override
	public void onClick(View v) {
		int mId = v.getId();
		if (mId == R.id.info_back) {
			this.finish();
		} else if (mId == R.id.info_confirm) {
			getData();
			generatesp();
			TcnVendIF.getInstance().LoggerDebug(TAG, "initSocket getIPAddress: " + TcnShareUseData.getInstance().getIPAddress());
			this.finish();
		}
	}

	protected void generatesp() {

		//TcnShareUseData.getInstance().setMachineID(id);
		TcnShareUseData.getInstance().setIPAddress(ip.trim());
		if (TcnVendIF.getInstance().isDigital(socket.trim())) {
			TcnShareUseData.getInstance().setSocketPort(Integer.valueOf(socket.trim()));
		}
		TcnShareUseData.getInstance().setAdvertText(m_strAdTips);
		TcnShareUseData.getInstance().setPayTips(m_strPayTips);
		TcnShareUseData.getInstance().setPayFirstQrcodeTips(m_strPayFirstTips);
		TcnShareUseData.getInstance().setPaySecondQrcodeTips(m_strPaySecondTips);
		TcnShareUseData.getInstance().setWeclcome(m_strWelcome);
		TcnShareUseData.getInstance().setKeyBoardText(m_strKeyboardText);
		TcnShareUseData.getInstance().setKeyBoardInputTips(m_strKeyboardInputTips);
		TcnShareUseData.getInstance().setUnitPrice(m_strUnitPrice);
		TcnShareUseData.getInstance().setPayTime(m_iPayTime);
	}

	protected void selectPriceUnit(int which, String[] str) {
		if (null == str || which < 0 || str.length <= which) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectPriceUnit which: "+which);
		}
		m_strUnitPrice = str[which];
	}

	protected void selectPayTime(int which, String[] str) {
		if (null == str || which < 0 || str.length <= which) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectPayTime which: "+which);
		}
		if (!TcnVendIF.getInstance().isDigital(str[which])) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectPayTime is not isDigital");
			return;
		}
		m_iPayTime = Integer.valueOf(str[which]);
	}

	protected void selectAdvertPollTime(int which, String[] str) {
		if (null == str || which < 0 || str.length <= which) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectAdvertPollTime which: "+which);
		}
		if (0 == which) {
			TcnShareUseData.getInstance().setAdvertPollTime(10*60*1000);
		} else if (1 == which) {
			TcnShareUseData.getInstance().setAdvertPollTime(30*60*1000);
		} else if (2 == which) {
			TcnShareUseData.getInstance().setAdvertPollTime(60*60*1000);
		} else if (3 == which) {
			TcnShareUseData.getInstance().setAdvertPollTime(2*60*60*1000);
		} else if (4 == which) {
			TcnShareUseData.getInstance().setAdvertPollTime(4*60*60*1000);
		} else if (5 == which) {
			TcnShareUseData.getInstance().setAdvertPollTime(6*60*60*1000);
		} else if (6 == which) {
			TcnShareUseData.getInstance().setAdvertPollTime(12*60*60*1000);
		} else if (7 == which) {
			TcnShareUseData.getInstance().setAdvertPollTime(15*60*60*1000);
		} else if (8 == which) {
			TcnShareUseData.getInstance().setAdvertPollTime(20*60*60*1000);
		} else if (9 == which) {
			TcnShareUseData.getInstance().setAdvertPollTime(24*60*60*1000);
		} else {

		}
		m_strAdvetPollTime = str[which];
		info_advert_poll_time.setButtonText(m_strAdvetPollTime);
	}

	protected String getAdvertDisplayTime() {
		String strRet = "";
		if (TcnShareUseData.getInstance().getAdvertPollTime() == 10*60*1000) {
			strRet = "10"+getString(R.string.info_time_minutes);
		} else if (TcnShareUseData.getInstance().getAdvertPollTime() == 30*60*1000) {
			strRet = "30"+getString(R.string.info_time_minutes);
		} else if (TcnShareUseData.getInstance().getAdvertPollTime() == 60*60*1000) {
			strRet = "1"+getString(R.string.info_time_hours);
		} else if (TcnShareUseData.getInstance().getAdvertPollTime() == 2*60*60*1000) {
			strRet = "2"+getString(R.string.info_time_hours);
		} else if (TcnShareUseData.getInstance().getAdvertPollTime() == 4*60*60*1000) {
			strRet = "4"+getString(R.string.info_time_hours);
		} else if (TcnShareUseData.getInstance().getAdvertPollTime() == 6*60*60*1000) {
			strRet = "6"+getString(R.string.info_time_hours);
		} else if (TcnShareUseData.getInstance().getAdvertPollTime() == 12*60*60*1000) {
			strRet = "12"+getString(R.string.info_time_hours);
		} else if (TcnShareUseData.getInstance().getAdvertPollTime() == 15*60*60*1000) {
			strRet = "15"+getString(R.string.info_time_hours);
		} else if (TcnShareUseData.getInstance().getAdvertPollTime() == 20*60*60*1000) {
			strRet = "20"+getString(R.string.info_time_hours);
		} else if (TcnShareUseData.getInstance().getAdvertPollTime() == 24*60*60*1000) {
			strRet = "24"+getString(R.string.info_time_hours);
		} else {

		}
		return strRet;
	}

	protected void selectStandbyImageTime(int which, String[] str) {
		if (null == str || which < 0 || str.length <= which) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectStandbyImageTime which: "+which);
		}
		if (!TcnVendIF.getInstance().isDigital(str[which])) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectStandbyImageTime is not isDigital");
			return;
		}
		TcnShareUseData.getInstance().setStandbyImageTime(Integer.valueOf(str[which]));
		info_standby_image_time.setButtonText(Integer.valueOf(str[which])+getString(R.string.ui_pay_seconds));
	}

	protected void selectPlayImageIntervalTime(int which, String[] str) {
		if (null == str || which < 0 || str.length <= which) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectPlayImageIntervalTime which: "+which);
		}
		if (!TcnVendIF.getInstance().isDigital(str[which])) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectPlayImageIntervalTime is not isDigital");
			return;
		}
		TcnShareUseData.getInstance().setImagePlayIntervalTime(Integer.valueOf(str[which]));
		info_image_play_interval_time.setButtonText(TcnShareUseData.getInstance().getImagePlayIntervalTime()+getString(R.string.ui_pay_seconds));
	}

	protected void selectItemCount(int which, String[] str) {
		if (null == str || which < 0 || str.length <= which) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectItemCount which: "+which);
		}
		if (!TcnVendIF.getInstance().isDigital(str[which])) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectItemCount is not isDigital");
			return;
		}
		TcnShareUseData.getInstance().setItemCountEveryPage(Integer.valueOf(str[which]));
		if (info_item_count_page != null) {
			info_item_count_page.setButtonText(String.valueOf(TcnShareUseData.getInstance().getItemCountEveryPage()));
		}
	}

	protected void selectSlotNoDigitCount(int which, String[] str) {
		if (null == str || which < 0 || str.length <= which) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectSlotNoDigitCount which: "+which);
		}
		if (!TcnVendIF.getInstance().isDigital(str[which])) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectSlotNoDigitCount is not isDigital");
			return;
		}
		TcnShareUseData.getInstance().setSlotNoDigitCount(Integer.valueOf(str[which]));
		if (info_slotno_digt_count != null) {
			info_slotno_digt_count.setButtonText(String.valueOf(TcnShareUseData.getInstance().getSlotNoDigitCount()));
		}
	}

	protected void selectPriceDigitCount(int which, String[] str) {
		if (null == str || which < 0 || str.length <= which) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectPriceDigitCount which: "+which);
		}
		if (!TcnVendIF.getInstance().isDigital(str[which])) {
			TcnVendIF.getInstance().LoggerError(TAG, "selectPriceDigitCount is not isDigital");
			return;
		}
		TcnShareUseData.getInstance().setPricePointCount(Integer.valueOf(str[which]));
		if (info_price_point_count != null) {
			info_price_point_count.setButtonText(String.valueOf(TcnShareUseData.getInstance().getPricePointCount()));
		}
	}

	protected void showPriceUnitDialog(final EditText v,final String[] str) {
		singleitem=0;
		int checkedItem = 0;
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals(String.valueOf(m_strUnitPrice))) {
				checkedItem = i;
				break;
			}
		}
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
				selectPriceUnit(singleitem,str);
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

	protected void showPayTimeDialog(final EditText v,final String[] str) {
		int checkedItem = 0;
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals(String.valueOf(m_iPayTime))) {
				checkedItem = i;
				break;
			}
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
				selectPayTime(singleitem,str);
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

	protected void showRebootTimeDialog(final EditText v,final String[] str) {
		int checkedItem = -1;
		int iRebootTime = TcnShareUseData.getInstance().getRebootTime();
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals(String.valueOf(iRebootTime))) {
				checkedItem = i;
				break;
			}
			if(-1 == iRebootTime) {
				checkedItem = 0;
				break;
			}
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
				if (TcnVendIF.getInstance().isDigital(str[singleitem])) {
					TcnShareUseData.getInstance().setRebootTime(Integer.valueOf(str[singleitem]).intValue());
				} else {
					TcnShareUseData.getInstance().setRebootTime(-1);
				}
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

	protected void showAdvertPollTimeDialog(final EditText v,final String[] str) {
		int checkedItem = 0;
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals(String.valueOf(m_strAdvetPollTime))) {
				checkedItem = i;
				break;
			}
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
				selectAdvertPollTime(singleitem,str);
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

	protected void showStandbyImageTimeDialog(final EditText v,final String[] str) {
		int checkedItem = 0;
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals(String.valueOf(TcnShareUseData.getInstance().getStandbyImageTime()))) {
				checkedItem = i;
				break;
			}
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
				selectStandbyImageTime(singleitem,str);
				v.setText(str[singleitem]+getString(R.string.ui_pay_seconds));
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

	protected void showPlayImageIntervalTimeDialog(final EditText v,final String[] str) {
		int checkedItem = 0;
		for (int i = 0; i < str.length; i++) {
			if (TcnVendIF.getInstance().isDigital(str[i])) {
				if (Integer.valueOf(str[i]) == TcnShareUseData.getInstance().getImagePlayIntervalTime()) {
					checkedItem = i;
					break;
				}
			}
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
				selectPlayImageIntervalTime(singleitem,str);
				v.setText(str[singleitem]+getString(R.string.ui_pay_seconds));
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

	protected void showItemCountDialog(final EditText v,final String[] str) {
		int checkedItem = 0;
		for (int i = 0; i < str.length; i++) {
			if (TcnVendIF.getInstance().isDigital(str[i])) {
				if (Integer.valueOf(str[i]) == TcnShareUseData.getInstance().getItemCountEveryPage()) {
					checkedItem = i;
					break;
				}
			}
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
				selectItemCount(singleitem,str);
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

	protected void showSlotNoDigitCountDialog(final EditText v,final String[] str) {
		int checkedItem = 0;
		for (int i = 0; i < str.length; i++) {
			if (TcnVendIF.getInstance().isDigital(str[i])) {
				if (Integer.valueOf(str[i]) == TcnShareUseData.getInstance().getSlotNoDigitCount()) {
					checkedItem = i;
					break;
				}
			}
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
				selectSlotNoDigitCount(singleitem,str);
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

	protected void showPricePointCountDialog(final EditText v,final String[] str) {
		int checkedItem = 0;
		for (int i = 0; i < str.length; i++) {
			if (TcnVendIF.getInstance().isDigital(str[i])) {
				if (Integer.valueOf(str[i]) == TcnShareUseData.getInstance().getSlotNoDigitCount()) {
					checkedItem = i;
					break;
				}
			}
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
				selectPriceDigitCount(singleitem,str);
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

	protected ButtonClickListener m_ButtonClickListener= new ButtonClickListener();
	protected class ButtonClickListener implements ButtonEdit.ButtonListener {
		@Override
		public void onClick(View v, int buttonId) {
			if (null == v) {
				return;
			}
			int id = v.getId();
			if (R.id.info_paytime_select == id) {
				if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
					showPayTimeDialog(info_paytime_select.getButtonEdit(), TcnCommon.PAYTIME_SELECT);
				}
			} else if (R.id.info_reboot_time == id) {
				if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
					showRebootTimeDialog(info_reboot_time.getButtonEdit(), TcnCommon.REOOT_TIME_SELECT);
				}
			}
			else if (R.id.info_advert_poll_time == id) {
				if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
					showAdvertPollTimeDialog(info_advert_poll_time.getButtonEdit(), TcnCommon.SELECT_ADVERT_POLL_TIME);
				}
			} else if (R.id.info_standby_image_time == id) {
				if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
					showStandbyImageTimeDialog(info_standby_image_time.getButtonEdit(),TcnCommon.SELECT_STANDBY_IMAGE_TIME);
				}
			} else if (R.id.info_image_play_interval_time == id) {
				if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
					showPlayImageIntervalTimeDialog(info_image_play_interval_time.getButtonEdit(),TcnCommon.SELECT_PLAY_IMAGE_INTERVAL_TIME);
				}
			} else if (R.id.info_item_count_page == id) {
				if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
					showItemCountDialog(info_item_count_page.getButtonEdit(),TcnVendIF.getInstance().getItemCountArrEveryPage());
				}
			} else if (R.id.info_slotno_digt_count == id) {
				if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
					showSlotNoDigitCountDialog(info_slotno_digt_count.getButtonEdit(),TcnCommon.SLOTNO_DIGIT_COUNT);
				}
			} else if (R.id.info_price_point_count == id) {
				if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
					showPricePointCountDialog(info_price_point_count.getButtonEdit(),TcnCommon.PRICE_POINT_COUNT);
				}
			}
			else {

			}
		}
	}

	protected ButtonSwitchClickListener m_ButtonSwitchClickListener= new ButtonSwitchClickListener();
	protected class ButtonSwitchClickListener implements ButtonSwitch.ButtonListener {
		@Override
		public void onSwitched(View v, boolean isSwitchOn) {

			int id = v.getId();
			if (R.id.info_advert_position == id) {
				TcnShareUseData.getInstance().setAdvertOnScreenBottom(isSwitchOn);
				TcnVendIF.getInstance().reqImageScreen();
			} else if (R.id.info_advert_standby == id) {
				TcnShareUseData.getInstance().setStandbyImageFullScreen(isSwitchOn);
				TcnVendIF.getInstance().reqImageScreen();
			} else if (R.id.info_full_screen == id) {
				TcnShareUseData.getInstance().setFullScreen(isSwitchOn);
				initScreenShowSet();
			} else if (R.id.page_display == id) {
				TcnShareUseData.getInstance().setPageDisplay(isSwitchOn);
			} else if (R.id.info_screen_protect == id) {
				TcnShareUseData.getInstance().setShowScreenProtect(isSwitchOn);
			} else if (R.id.info_show_type == id) {
				TcnVendIF.getInstance().setShowType(isSwitchOn);
			} else if (R.id.info_voice == id) {
				TcnShareUseData.getInstance().setVoicePrompt(isSwitchOn);
			} else if (R.id.info_show_single_code == id) {
				TcnShareUseData.getInstance().setShowSingleQRCode(isSwitchOn);
			} else if (R.id.info_show_shopping == id) {
				TcnShareUseData.getInstance().setShowShopping(isSwitchOn);
			} else if (R.id.info_app_running_check == id) {
				TcnShareUseData.getInstance().setAppForegroundCheck(isSwitchOn);
			} else if (R.id.info_show_tap_screen_text == id) {
				TcnShareUseData.getInstance().setShowTapScreenText(isSwitchOn);
			}
			else if (R.id.info_reboot == id) {
				if (isSwitchOn) {
					TcnVendIF.getInstance().rebootDevice();
					TcnUtility.getToast(InformationConfig.this, getString(R.string.tip_reboot_device));
				}
				m_info_reboot.setSwitchState(isSwitchOn);
			}
		}
	}
}
