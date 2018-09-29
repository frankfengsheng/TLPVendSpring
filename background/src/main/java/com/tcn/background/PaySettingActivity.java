package com.tcn.background;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonSwitch;
import com.tcn.uicommon.view.MySlipSwitch;
import com.tcn.uicommon.view.TcnMainActivity;





/**
 * 支付系统（支付宝，微信，现金，pos）
 * @author Administrator
 *
 */
public class PaySettingActivity extends TcnMainActivity implements OnClickListener {

	private static final String TAG = "PaySettingActivity";
	private MySlipSwitch alipay_switch;
	private MySlipSwitch weixinpay_switch;

	private Button  payback,payconfirm,ali_more,wx_more;

	private LinearLayout alipay_linear,weixinpay_linear;
	private TextView aliseller_email_text,alipartner_text,alikey_text,aliTransInPartner_text,
			wxpartner_text,wxkey_text,wxappid_text,wxappkey_text;

	private EditText aliseller_email_edit,alipartner_edit,alikey_edit,aliTransInPartner_edit,//账号，合作身份者id，交易安全检验码,支付宝
			wxpartner_edit,wxkey_edit,wxappid_edit,wxappkey_edit;


	private String aliseller_email,ali_partner,alikey,ali_transInPartner,wxpartner,wxkey,wxappid,wxappkey;
	private boolean isopenali=false,isopenweixin=false;

	private int wxpay_version=3//默认是V3
			,ali_click=0,//未点击按钮
			wx_click=0;

	protected ButtonSwitch pay_jingdong_switch;
	protected ButtonSwitch pay_passive_scan_switch;






	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLayout();
		TcnVendIF.getInstance().LoggerDebug(TAG, "PaySettingActivity onCreate()");
		initview();
	}

    protected void setLayout() {
        setContentView(R.layout.pay);
    }

    @Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (m_handler != null) {
			m_handler.removeCallbacksAndMessages(null);
			m_handler = null;
		}
		m_ButtonSwitchClickListener = null;
		if (alipay_switch != null) {
			alipay_switch.setOnSwitchListener(null);
			alipay_switch = null;
		}
		if (weixinpay_switch != null) {
			weixinpay_switch.setOnSwitchListener(null);
			weixinpay_switch = null;
		}

		if (payback != null) {
			payback.setOnClickListener(null);
			payback = null;
		}

		if (payconfirm != null) {
			payconfirm.setOnClickListener(null);
			payconfirm = null;
		}

		if (ali_more != null) {
			ali_more.setOnClickListener(null);
			ali_more = null;
		}

		if (wx_more != null) {
			wx_more.setOnClickListener(null);
			wx_more = null;
		}


		alipay_linear = null;

		weixinpay_linear = null;

		aliseller_email_text = null;
		alipartner_text = null;
		alikey_text = null;
		aliTransInPartner_text = null;
		wxpartner_text = null;
		wxkey_text = null;
		wxappid_text = null;
		wxappkey_text = null;

		aliseller_email_edit = null;
		alipartner_edit = null;
		alikey_edit = null;
		aliTransInPartner_edit = null;
		wxpartner_edit = null;
		wxkey_edit = null;
		wxappid_edit = null;
		wxappkey_edit = null;
		aliseller_email = null;
		ali_partner = null;
		alikey = null;
		ali_transInPartner = null;
		wxpartner = null;
		wxkey = null;
		wxappid = null;
		wxappkey = null;
		wxappid = null;
		if (pay_jingdong_switch != null) {
			pay_jingdong_switch.setButtonListener(null);
			pay_jingdong_switch = null;
		}

		if (pay_passive_scan_switch != null) {
			pay_passive_scan_switch.setButtonListener(null);
			pay_passive_scan_switch = null;
		}
		if (pay_passive_scan_switch != null) {
			pay_passive_scan_switch.setButtonListener(null);
			pay_passive_scan_switch = null;
		}
		super.onDestroy();
	}

	private void initview() {

		alipay_linear=(LinearLayout) findViewById(R.id.alipay_linear);
		weixinpay_linear=(LinearLayout) findViewById(R.id.weixinpay_linear);

		payback=(Button) findViewById(R.id.pay_back);
		payback.setOnClickListener(this);
		payconfirm=(Button) findViewById(R.id.pay_confirm);
		payconfirm.setOnClickListener(this);
		ali_more=(Button) findViewById(R.id.ali_more);
		ali_more.setOnClickListener(this);
		wx_more=(Button) findViewById(R.id.wx_more);
		wx_more.setOnClickListener(this);

		alipay_switch=(MySlipSwitch) findViewById(R.id.alipay_switch);
		alipay_switch.setImageResource(R.mipmap.switch_bkg_switch,
				R.mipmap.switch_bkg_switch, R.mipmap.switch_btn_slip);
		alipay_switch.setSwitchState(false);
		alipay_switch.setOnSwitchListener(new MySlipSwitch.OnSwitchListener() {

			@Override
			public void onSwitched(boolean isSwitchOn) {
				if (isSwitchOn) {
					isopenali = true;
					generateAli();

				} else {
					isopenali = false;
					ali_more.setVisibility(View.INVISIBLE);
					alipay_linear.removeAllViews();

				}

			}
		});

		weixinpay_switch=(MySlipSwitch) findViewById(R.id.weixinpay_switch);
		weixinpay_switch.setImageResource(R.mipmap.switch_bkg_switch,
				R.mipmap.switch_bkg_switch, R.mipmap.switch_btn_slip);
		weixinpay_switch.setSwitchState(false);
		weixinpay_switch.setOnSwitchListener(new MySlipSwitch.OnSwitchListener() {

			@Override
			public void onSwitched(boolean isSwitchOn) {
				if (isSwitchOn) {
					isopenweixin = true;
					generateWeixin();
				} else {
					isopenweixin = false;
					wx_more.setVisibility(View.INVISIBLE);
					weixinpay_linear.removeAllViews();
				}
			}
		});


		pay_jingdong_switch =(ButtonSwitch) findViewById(R.id.pay_jingdong_switch);
		pay_jingdong_switch.setButtonName(R.string.pay_jingdong_pay);
		pay_jingdong_switch.setButtonListener(m_ButtonSwitchClickListener);
		pay_jingdong_switch.setSwitchState(TcnShareUseData.getInstance().isJidongOpen());

		pay_passive_scan_switch =(ButtonSwitch) findViewById(R.id.pay_passive_scan_switch);
		pay_passive_scan_switch.setButtonName(R.string.pay_passive_scan_pay);
		pay_passive_scan_switch.setButtonListener(m_ButtonSwitchClickListener);
		pay_passive_scan_switch.setSwitchState(TcnShareUseData.getInstance().isPassiveScanCodePayOpen());

		viewData();
	}

	/**
	 * 之前存储的信息显示
	 */
	private void viewData() {
		SharedPreferences sp=getSharedPreferences("pay_system", MODE_PRIVATE);
		if(null!=sp){

			isopenali=sp.getBoolean("isopenali", false);
			isopenweixin=sp.getBoolean("isopenweixin", false);
			wxpay_version=sp.getInt("wxpayversion", 3);
			wxpartner=sp.getString("wxpartner", null);
			wxappid=sp.getString("wxappid", null);
			wxkey=sp.getString("wxkey", null);
			wxappkey=sp.getString("wxappkey", null);

			ali_partner=sp.getString("alipartner", null);
			aliseller_email=sp.getString("aliselleremail", null);
			alikey=sp.getString("alikey", null);
			ali_transInPartner = sp.getString("transInPartner", "");


			if(isopenali){
				alipay_switch.setSwitchState(true);
				ali_more.setVisibility(View.VISIBLE);
			}

			if(isopenweixin){
				weixinpay_switch.setSwitchState(true);
				wx_more.setVisibility(View.VISIBLE);
			}
		}

	}

	/**
	 * 点击支付宝前的小按钮，显示添加的所有支付宝信息
	 */
	private void generateAli(){
		aliseller_email_text=new TextView(this);
		aliseller_email_text.setText(R.string.pay_alipay_account);
		aliseller_email_text.setTextColor(getResources().getColor(R.color.text_color_dialog_title));
		aliseller_email_text.setTextSize(20);
		aliseller_email_text.setGravity(Gravity.LEFT);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 20, 0, 0);
		aliseller_email_text.setLayoutParams(lp);
		alipay_linear.addView(aliseller_email_text);

		aliseller_email_edit=new EditText(this);
		aliseller_email_edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.circlebuttonstyle));
		aliseller_email_edit.setPadding(0, 5, 0, 5);
		aliseller_email_edit.setTextSize(25);
		aliseller_email_edit.setGravity(Gravity.CENTER);
		//aliseller_email_edit.setText("1710102127@qq.com");
		if ((null == aliseller_email) || "".equals(aliseller_email)) {
			aliseller_email_edit.setText("171225156271@qq.com");
		} else {
			aliseller_email_edit.setText(aliseller_email);
		}

		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp1.setMargins(0, 5, 0, 0);
		aliseller_email_edit.setLayoutParams(lp1);
		alipay_linear.addView(aliseller_email_edit);


		alipartner_text=new TextView(this);
		alipartner_text.setText(R.string.pay_alipay_partner);
		alipartner_text.setTextColor(getResources().getColor(R.color.text_color_dialog_title));
		alipartner_text.setTextSize(20);
		alipartner_text.setGravity(Gravity.LEFT);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp2.setMargins(0, 15, 0, 0);
		alipartner_text.setLayoutParams(lp2);
		alipay_linear.addView(alipartner_text);

		alipartner_edit=new EditText(this);
		alipartner_edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.circlebuttonstyle));
		alipartner_edit.setPadding(5, 5, 5, 5);
		alipartner_edit.setTextSize(25);
		alipartner_edit.setGravity(Gravity.CENTER);
		//alipartner_edit.setText("2088601049466268");
		if ((null == ali_partner) || "".equals(ali_partner)) {
			alipartner_edit.setText("2088651010494626231");
		} else {
			alipartner_edit.setText(ali_partner);
		}

		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp3.setMargins(0, 5, 0, 0);
		alipartner_edit.setLayoutParams(lp3);
		alipay_linear.addView(alipartner_edit);



		alikey_text=new TextView(this);
		alikey_text.setText(R.string.pay_alipay_key);
		alikey_text.setTextColor(getResources().getColor(R.color.text_color_dialog_title));
		alikey_text.setTextSize(20);
		alikey_text.setGravity(Gravity.LEFT);
		LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp4.setMargins(0, 15, 0, 0);
		alikey_text.setLayoutParams(lp4);
		alipay_linear.addView(alikey_text);

		alikey_edit=new EditText(this);
		alikey_edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.circlebuttonstyle));
		alikey_edit.setPadding(5, 5, 5, 5);
		alikey_edit.setTextSize(25);
		//alikey_edit.setText("gmw9mczhqlfbrphwqhfweatznicbpbbx");
		if ((null == alikey) || "".equals(alikey)) {
			alikey_edit.setText("gmw9mczhqlfbr3phwqhfwwesznicbpbbx1");
		} else {
			alikey_edit.setText(alikey);
		}

		LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp5.setMargins(0, 5, 0, 0);
		alikey_edit.setLayoutParams(lp5);
		alikey_edit.setGravity(Gravity.CENTER);
		alipay_linear.addView(alikey_edit);


		aliTransInPartner_text=new TextView(this);
		aliTransInPartner_text.setText(R.string.pay_alipay_sub_account);
		aliTransInPartner_text.setTextColor(getResources().getColor(R.color.text_color_dialog_title));
		aliTransInPartner_text.setTextSize(20);
		aliTransInPartner_text.setGravity(Gravity.LEFT);
		LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp6.setMargins(0, 15, 0, 0);
		aliTransInPartner_text.setLayoutParams(lp6);
		alipay_linear.addView(aliTransInPartner_text);

		aliTransInPartner_edit=new EditText(this);
		aliTransInPartner_edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.circlebuttonstyle));
		aliTransInPartner_edit.setPadding(5, 5, 5, 5);
		aliTransInPartner_edit.setTextSize(25);
		//alikey_edit.setText("gmw9mczhqlfbrphwqhfweatznicbpbbx");
		if (null == ali_transInPartner) {
			aliTransInPartner_edit.setText("");
		} else {
			aliTransInPartner_edit.setText(ali_transInPartner);
		}

		LinearLayout.LayoutParams lp7 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp7.setMargins(0, 5, 0, 0);
		aliTransInPartner_edit.setLayoutParams(lp7);
		aliTransInPartner_edit.setGravity(Gravity.CENTER);
		alipay_linear.addView(aliTransInPartner_edit);

	}


	/**
	 * 点击微信前的小按钮，显示添加的所有微信信息
	 */
	private void generateWeixin(){
		wxappid_text=new TextView(this);
		wxappid_text.setText(R.string.pay_wxpay_official_account);
		wxappid_text.setTextColor(getResources().getColor(R.color.text_color_dialog_title));
		wxappid_text.setTextSize(20);
		wxappid_text.setGravity(Gravity.LEFT);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 20, 0, 0);
		wxappid_text.setLayoutParams(lp);
		weixinpay_linear.addView(wxappid_text);

		wxappid_edit=new EditText(this);
		wxappid_edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.circlebuttonstyle));
		wxappid_edit.setPadding(0, 5, 0, 5);
		wxappid_edit.setTextSize(25);
		wxappid_edit.setGravity(Gravity.CENTER);
		//wxappid_edit.setText("wx62918b7315172803");

		if ((null == wxappid) || "".equals(wxappid)) {
			wxappid_edit.setText("wx629168wert51728031");
		} else {
			wxappid_edit.setText(wxappid);
		}
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp1.setMargins(0, 5, 0, 0);
		wxappid_edit.setLayoutParams(lp1);
		weixinpay_linear.addView(wxappid_edit);


		wxpartner_text=new TextView(this);
		wxpartner_text.setText(R.string.pay_wxpay_partner);
		wxpartner_text.setTextColor(getResources().getColor(R.color.text_color_dialog_title));
		wxpartner_text.setTextSize(20);
		wxpartner_text.setGravity(Gravity.LEFT);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp2.setMargins(0, 15, 0, 0);
		wxpartner_text.setLayoutParams(lp2);
		weixinpay_linear.addView(wxpartner_text);

		wxpartner_edit=new EditText(this);
		wxpartner_edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.circlebuttonstyle));
		wxpartner_edit.setPadding(5, 5, 5, 5);
		wxpartner_edit.setTextSize(25);
		wxpartner_edit.setGravity(Gravity.CENTER);
		//wxpartner_edit.setText("1220505201");
		if ((null == wxpartner) || "".equals(wxpartner)) {
			wxpartner_edit.setText("122057025011");
		} else {
			wxpartner_edit.setText(wxpartner);
		}

		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp3.setMargins(0, 5, 0, 0);
		wxpartner_edit.setLayoutParams(lp3);
		weixinpay_linear.addView(wxpartner_edit);



		wxkey_text=new TextView(this);
		wxkey_text.setText(R.string.pay_wxpay_key);
		wxkey_text.setTextColor(getResources().getColor(R.color.text_color_dialog_title));
		wxkey_text.setTextSize(20);
		wxkey_text.setGravity(Gravity.LEFT);
		LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp4.setMargins(0, 15, 0, 0);
		wxkey_text.setLayoutParams(lp4);
		weixinpay_linear.addView(wxkey_text);

		wxkey_edit=new EditText(this);
		wxkey_edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.circlebuttonstyle));
		wxkey_edit.setPadding(5, 5, 5, 5);
		wxkey_edit.setTextSize(25);
		//wxkey_edit.setText("16a06627a1184baabe39c0763093c6d2");
		if ((null == wxkey) || "".equals(wxkey)) {
			wxkey_edit.setText("16a06627a71184baab23er0763093c6d21");
		} else {
			wxkey_edit.setText(wxkey);
		}

		LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp5.setMargins(0, 5, 0, 0);
		wxkey_edit.setLayoutParams(lp5);
		wxkey_edit.setGravity(Gravity.CENTER);
		weixinpay_linear.addView(wxkey_edit);


		wxappkey_text=new TextView(this);
		wxappkey_text.setText(R.string.pay_wxpay_app_signature);
		wxappkey_text.setTextColor(getResources().getColor(R.color.text_color_dialog_title));
		wxappkey_text.setTextSize(20);
		wxappkey_text.setGravity(Gravity.LEFT);
		LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp6.setMargins(0, 15, 0, 0);
		wxappkey_text.setLayoutParams(lp6);
		weixinpay_linear.addView(wxappkey_text);

		wxappkey_edit=new EditText(this);
		wxappkey_edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.circlebuttonstyle));
		wxappkey_edit.setPadding(5, 5, 5, 5);
		wxappkey_edit.setTextSize(25);
		//wxappkey_edit.setText("16a06627a1184baabe39c0763093c6d2");
		if ((null == wxappkey) || "".equals(wxappkey)) {
			wxappkey_edit.setText("16a06627a71184baab23er0763093c6d21");
		} else {
			wxappkey_edit.setText(wxappkey);
		}

		LinearLayout.LayoutParams lp7 = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp7.setMargins(0, 5, 0, 0);
		wxappkey_edit.setLayoutParams(lp7);
		wxappkey_edit.setGravity(Gravity.CENTER);
		weixinpay_linear.addView(wxappkey_edit);

	}

	private boolean getAliData(){
		if(null!=alipartner_edit){
			ali_partner=alipartner_edit.getText().toString();
			if("".equals(ali_partner)||null==ali_partner){
				TcnUtility.getToast(this, getString(R.string.tip_alipay_partner_empty));
				alipartner_edit.requestFocus();
				return false;
			}
		}

		if(null!=aliseller_email_edit){
			aliseller_email=aliseller_email_edit.getText().toString();
			if("".equals(aliseller_email)||null==aliseller_email){
				TcnUtility.getToast(this, getString(R.string.tip_alipay_account_empty));
				aliseller_email_edit.requestFocus();
				return false;
			}
		}

		if(null!=alikey_edit){
			alikey=alikey_edit.getText().toString();
			if("".equals(alikey)||null==alikey){
				TcnUtility.getToast(this, getString(R.string.tip_alipay_key_empty));
				alikey_edit.requestFocus();
				return false;
			}
		}

		if(null!=aliTransInPartner_edit){
			ali_transInPartner = aliTransInPartner_edit.getText().toString();
			if (null == ali_transInPartner) {
				ali_transInPartner = "";
			}
		}

		return true;
	}


	private boolean getWeiXinData(){
		if(null!=wxappid_edit){
			wxappid=wxappid_edit.getText().toString();
			if("".equals(wxappid)||null==wxappid){
				TcnUtility.getToast(this, getString(R.string.tip_wxpay_official_account_empty));
				wxappid_edit.requestFocus();
				return false;
			}
		}

		if(null!=wxpartner_edit){
			wxpartner=wxpartner_edit.getText().toString();
			if("".equals(wxpartner)||null==wxpartner){
				TcnUtility.getToast(this, getString(R.string.tip_wxpay_partner_empty));
				wxpartner_edit.requestFocus();
				return false;
			}
		}

		if(null!=wxkey_edit){
			wxkey=wxkey_edit.getText().toString();
			if("".equals(wxkey)||null==wxkey){
				TcnUtility.getToast(this, getString(R.string.tip_wxpay_key_empty));
				wxkey_edit.requestFocus();
				return false;
			}
		}

		if(null!=wxappkey_edit){
			wxappkey=wxappkey_edit.getText().toString();
			if("".equals(wxappkey)||null==wxappkey){
				TcnUtility.getToast(this, getString(R.string.tip_wxpay_app_signature_empty));
				wxappkey_edit.requestFocus();
				return false;
			}
		}

		return true;
	}

	private void writeDataFailHandle() {
		if (m_handler != null) {
			m_handler.removeMessages(TcnCommon.WRITE_DATA_CMD_FAIL);
			m_handler.sendEmptyMessageDelayed(TcnCommon.WRITE_DATA_CMD_FAIL,3000);
		}
	}

	/*预留方法，便于重写*/
    protected void addClick() {

    }

    @Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.pay_back) {
			this.finish();
		} else if (id == R.id.pay_confirm) {
            addClick();

			if(getAliData()&&getWeiXinData()){
				generateSp();
				this.finish();
			}

		}
		else if (id == R.id.ali_more) {
			if(ali_click==0){
				ali_click=1;
				ali_more.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_menu_open_pressed));
				SharedPreferences sp1=getSharedPreferences("pay_system", MODE_PRIVATE);
				if(null!=sp1){
					generateAli();
					if("".equals(ali_partner)||null==ali_partner){}else{
						alipartner_edit.setText(ali_partner);
					}
					if("".equals(aliseller_email)||null==aliseller_email){}else{
						aliseller_email_edit.setText(aliseller_email);
					}
					if("".equals(alikey)||null==alikey){}else{
						alikey_edit.setText(alikey);
					}
				}
			}else{
				ali_click=0;
				ali_more.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_menu_close_pressed));
				alipay_linear.removeAllViews();
			}
		} else if (id == R.id.wx_more) {
			if(wx_click==0){
				wx_click=1;
				wx_more.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_menu_open_pressed));
				SharedPreferences sp=getSharedPreferences("pay_system", MODE_PRIVATE);
				if(null!=sp){
					generateWeixin();
					wxpay_version=sp.getInt("wxpayversion", 3);
					if(wxpay_version==2){
						//wxbg_v2.setChecked(true);
					}else if(wxpay_version==3){
						//wxbg_v3.setChecked(true);
					}
					if("".equals(wxpartner)||null==wxpartner){}else{
						wxpartner_edit.setText(wxpartner);
					}
					if("".equals(wxkey)||null==wxkey){}else{
						wxkey_edit.setText(wxkey);
					}
					if("".equals(wxappid)||null==wxappid){}else{
						wxappid_edit.setText(wxappid);
					}
					if("".equals(wxappkey)||null==wxappkey){}else{
						wxappkey_edit.setText(wxappkey);
					}
				}
			}else{
				wx_click=0;
				wx_more.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_menu_close_pressed));
				weixinpay_linear.removeAllViews();
			}
		}
		else {
		}
	}

	private Handler m_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage( msg );
			switch (msg.what) {
				case TcnCommon.WRITE_DATA_CMD_SUCCESS:
					TcnUtility.getToast(PaySettingActivity.this, getString(R.string.tip_modify_success));
					break;
				case TcnCommon.WRITE_DATA_CMD_FAIL:
					TcnShareUseData.getInstance().setAliPayOpen(isopenali);
					TcnShareUseData.getInstance().setWeixinOpen(isopenweixin);
					TcnUtility.getToast(PaySettingActivity.this, getString(R.string.tip_modify_fail));
					break;
				default:
					break;
			}
		}
	};

	private void generateSp() {
		SharedPreferences sp=getSharedPreferences("pay_system", MODE_PRIVATE);
		Editor editor=sp.edit();

		editor.putBoolean("isopenali", isopenali);
		editor.putBoolean("isopenweixin", isopenweixin);

		editor.putInt("wxpayversion", wxpay_version);
		editor.putString("wxpartner", wxpartner);
		editor.putString("wxappid", wxappid);
		editor.putString("wxkey", wxkey);
		editor.putString("wxappkey", wxappkey);

		editor.putString("alipartner", ali_partner);
		editor.putString("aliselleremail", aliseller_email);
		editor.putString("transInPartner", ali_transInPartner);
		editor.putString("alikey", alikey);
		editor.commit();

	}

	private ButtonSwitchClickListener m_ButtonSwitchClickListener= new ButtonSwitchClickListener();
	private class ButtonSwitchClickListener implements ButtonSwitch.ButtonListener {
		@Override
		public void onSwitched(View v, boolean isSwitchOn) {
			int id = v.getId();
			if (R.id.pay_jingdong_switch == id) {
				TcnShareUseData.getInstance().setJidongOpen(isSwitchOn);
			} else if (R.id.pay_passive_scan_switch == id) {
				TcnShareUseData.getInstance().setPassiveScanCodePay(isSwitchOn);
			}

		}
	}

}
