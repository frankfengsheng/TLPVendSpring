package com.tcn.background;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnCustomer;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.db.UtilsDB;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendEventResultID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendDBControl;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonEdit;
import com.tcn.uicommon.dialog.LoadingDialog;
import com.tcn.uicommon.dialog.OutDialog;
import com.tcn.uicommon.dialog.SelectDialog;
import com.tcn.uicommon.view.TcnMainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 货道详细信息界面（只能修改图片，货道状态，名称）
 * @author Administrator
 *
 */
public class AisleManage extends TcnMainActivity implements OnClickListener {
	protected static final String TAG = "AisleManage";
	protected EditText product_name_edit,product_price_edit,product_num_edit,
			aisle_num_edit,aisle_state_edit,aisle_num1,aisle_num2,aisle_goods_code_edit,aisle_goods_spec_edit,aisle_goods_capacity_edit
			,aisle_goods_content_edit,heat_time_edit,aisle_col_edit,aisle_row_edit,aisle_pull_back_edit;
	protected Button aisle_state_btn,aisle_pics,aisle_confirm,aisle_back,aisle_start_test,aisle_clear_fault,aisle_btn_ad;
	protected ButtonEdit aisle_category = null;
	protected TextView aisle_slot_price,aisle_col,aisle_row,aisle_pull_back;
	protected ImageView aisle_image,aisle_slot_ad;
	protected Intent in;
	protected OutDialog m_BusyDialog = null;
	protected LoadingDialog m_LoadingDialog = null;
	protected int num1=0,num2=0,m_key_num = 0;
	protected int m_iRecevCount = 0;
	protected int singleitem=0;
	protected String product_name,img_url,img_url_ad,image_path,image_path_ad,product_code,product_spec,product_capacity,product_content;
	protected int capacity=0,extant_quantity=0,work_state=UtilsDB.SLOT_STATE_HAVE_GOODS;
	protected String price;
	protected String m_strCustomer = TcnCustomer.CUSTOMER_DEFAULT;
	protected String m_strDataType = TcnConstant.DATA_TYPE[0];
	protected Coil_info coil_info;
	protected boolean isalter=false,isalter_ad=false;
	protected LinearLayout type_name_layout,aisle_goods_spec_layout,aisle_goods_capacity_layout,aisle_slot_ad_layout,aisle_heat_layout,
			aisle_col_layout,aisle_row_layout,aisle_pull_back_layout;
	protected SelectDialog mCategoryDialog = null;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setUi();
        TcnVendIF.getInstance().LoggerDebug(TAG, "onCreate()");
		aisle_slot_price = (TextView) findViewById(R.id.aisle_slot_price);
		if (null == product_name_edit) {
			product_name_edit = (EditText) findViewById(R.id.product_name_edit);//商品名
			if (null == product_name_edit) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() product_name_edit is null");
				return;
			}
		}

		if (null == product_price_edit) {
			product_price_edit=(EditText) findViewById(R.id.product_price_edit);//商品价格
			if (null == product_price_edit) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() product_price_edit is null");
				return;
			}
		}

		if (null == product_num_edit) {
			product_num_edit = (EditText) findViewById(R.id.product_num_edit);
			if (null == product_num_edit) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() product_num_edit is null");
				return;
			}
		}

		product_num_edit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String str = product_num_edit.getText().toString();
					if (("".equals(str)) || (null == str) || (!TcnVendIF.getInstance().isDigital(str))) {
					} else {
						if (work_state < UtilsDB.SLOT_STATE_FAULT) {
							int i = Integer.parseInt(str);
							if (i < 1) {
								aisle_state_edit.setText(TcnCommon.SLOT_STATE_LIST[1]);
							} else {
								aisle_state_edit.setText(TcnCommon.SLOT_STATE_LIST[0]);
							}
						}
					}
				}
			}
		});

		if (null == aisle_num_edit) {
			aisle_num_edit=(EditText) findViewById(R.id.aisle_num_edit);//货道容量
			if (null == aisle_num_edit) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() aisle_num_edit is null");
				return;
			}
		}

		aisle_heat_layout=(LinearLayout) findViewById(R.id.aisle_heat_layout);
		heat_time_edit=(EditText) findViewById(R.id.heat_time_edit);

		aisle_row_layout=(LinearLayout) findViewById(R.id.aisle_row_layout);
		aisle_row =(TextView) findViewById(R.id.aisle_row);
		aisle_row_edit=(EditText) findViewById(R.id.aisle_row_edit);

		aisle_col_layout=(LinearLayout) findViewById(R.id.aisle_col_layout);
		aisle_col =(TextView) findViewById(R.id.aisle_col);
		aisle_col_edit=(EditText) findViewById(R.id.aisle_col_edit);

		aisle_pull_back_layout=(LinearLayout) findViewById(R.id.aisle_pull_back_layout);
		aisle_pull_back =(TextView) findViewById(R.id.aisle_pull_back);
		aisle_pull_back_edit=(EditText) findViewById(R.id.aisle_pull_back_edit);


		aisle_goods_code_edit = (EditText) findViewById(R.id.aisle_goods_code_edit);//商品编码
		aisle_goods_spec_edit=(EditText) findViewById(R.id.aisle_goods_spec_edit);//商品规格
		aisle_goods_capacity_edit=(EditText) findViewById(R.id.aisle_goods_capacity_edit);//商品容量

		aisle_goods_content_edit=(EditText) findViewById(R.id.aisle_goods_content_edit);//商品介绍
		if (null == aisle_state_edit) {
			aisle_state_edit=(EditText) findViewById(R.id.aisle_state_edit);
			if (null == aisle_state_edit) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() aisle_state_edit is null");
				return;
			}
		}


		aisle_state_edit.setOnTouchListener(new View.OnTouchListener() {
			//按住和松开的标识
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showdialog(aisle_state_edit, TcnCommon.SLOT_STATE_LIST);
				}
				return false;
			}
		});

		if (null == aisle_num1) {
			aisle_num1=(EditText) findViewById(R.id.aisle_num1);
			aisle_num1.setEnabled(false);
		}

		aisle_num2=(EditText) findViewById(R.id.aisle_num2);
		aisle_num2.requestFocus();

		if (null == aisle_state_btn) {
			aisle_state_btn=(Button) findViewById(R.id.aisle_state_btn);
			if (null == aisle_state_btn) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() aisle_state_btn is null");
				return;
			}
		}

		aisle_state_btn.setOnClickListener(this);

		aisle_category  = (ButtonEdit) findViewById(R.id.aisle_category);
		aisle_category.setButtonListener(m_ButtonClickListener);
		aisle_category.setButtonName(R.string.aisle_goods_type);
		aisle_category.setButtonText(R.string.aisle_add_type);
		aisle_category.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);

		if (null == aisle_pics) {
			aisle_pics=(Button) findViewById(R.id.aisle_pics);
			if (null == aisle_pics) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() aisle_pics is null");
				return;
			}
		}

		aisle_pics.setOnClickListener(this);

		aisle_start_test = (Button) findViewById(R.id.aisle_start_test);
		aisle_start_test.setOnClickListener(this);

		aisle_clear_fault = (Button) findViewById(R.id.aisle_clear_fault);
		aisle_clear_fault.setOnClickListener(this);

		if (null == aisle_confirm) {
			aisle_confirm=(Button) findViewById(R.id.aisle_confirm);
			if (null == aisle_confirm) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() aisle_confirm is null");
				return;
			}
		}

		aisle_confirm.setOnClickListener(this);

		if (null == aisle_back) {
			aisle_back=(Button) findViewById(R.id.aisle_back);
			if (null == aisle_back) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() aisle_back is null");
				return;
			}
		}

		aisle_back.setOnClickListener(this);

		if (null == aisle_image) {
			aisle_image=(ImageView) findViewById(R.id.aisle_image);
			if (null == aisle_image) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() aisle_image is null");
				return;
			}
		}

		type_name_layout = (LinearLayout) findViewById(R.id.type_name_layout);
		aisle_goods_spec_layout = (LinearLayout) findViewById(R.id.aisle_goods_spec_layout);
		aisle_goods_capacity_layout = (LinearLayout) findViewById(R.id.aisle_goods_capacity_layout);

		aisle_slot_ad_layout = (LinearLayout) findViewById(R.id.aisle_slot_ad_layout);
		aisle_slot_ad = (ImageView) findViewById(R.id.aisle_slot_ad);
		aisle_btn_ad = (Button) findViewById(R.id.aisle_btn_ad);
		if (aisle_btn_ad != null) {
			aisle_btn_ad.setOnClickListener(this);
		}
		aisle_heat_layout.setVisibility(View.GONE);
		aisle_row_layout.setVisibility(View.GONE);
		aisle_col_layout.setVisibility(View.GONE);
		aisle_pull_back_layout.setVisibility(View.GONE);
		initdata();
		viewdata();

		if (m_strCustomer.equals(TcnCustomer.CUSTOMER_BIANJIESHEN)) {
			type_name_layout.setVisibility(View.GONE);
		}

		if ((TcnConstant.DATA_TYPE[0]).equals(m_strDataType)) {
			product_price_edit.setEnabled(false);
			product_num_edit.setEnabled(false);
			aisle_num_edit.setEnabled(false);
			aisle_goods_code_edit.setEnabled(false);
		}
	}

    protected void setUi() {
        setContentView(R.layout.asilemanage);
    }

    @Override
	protected void onResume() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onResume()");
		TcnVendIF.getInstance().registerListener(m_vendListener);
		aisle_pics.setClickable(true);

		if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())) {
			if (aisle_slot_ad_layout != null) {
				aisle_slot_ad_layout.setVisibility(View.GONE);
			}
		}

		super.onResume();
	}



	@Override
	protected void onPause() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onPause()");
		TcnVendIF.getInstance().unregisterListener(m_vendListener);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onDestroy()");
		product_name_edit = null;
		product_price_edit = null;
		if (product_num_edit != null) {
			product_num_edit.setOnFocusChangeListener(null);
			product_num_edit = null;
		}

		aisle_num_edit = null;
		aisle_goods_code_edit = null;
		aisle_goods_content_edit = null;
		aisle_goods_capacity_edit = null;
		aisle_goods_spec_edit = null;
		if (aisle_state_edit != null) {
			aisle_state_edit.setOnTouchListener(null);
			aisle_state_edit = null;
		}

		aisle_num1 = null;
		aisle_num2 = null;
		if (aisle_state_btn != null) {
			aisle_state_btn.setOnClickListener(null);
			aisle_state_btn = null;
		}

		if (aisle_pics != null) {
			aisle_pics.setOnClickListener(null);
			aisle_pics = null;
		}

		if (aisle_btn_ad != null) {
			aisle_btn_ad.setOnClickListener(null);
			aisle_btn_ad = null;
		}
		aisle_slot_ad = null;
		aisle_slot_ad_layout = null;

		if (aisle_start_test != null) {
			aisle_start_test.setOnClickListener(null);
			aisle_start_test = null;
		}

		if (aisle_clear_fault != null) {
			aisle_clear_fault.setOnClickListener(null);
			aisle_clear_fault = null;
		}

		if (aisle_confirm != null) {
			aisle_confirm.setOnClickListener(null);
			aisle_confirm = null;
		}

		if (aisle_back != null) {
			aisle_back.setOnClickListener(null);
			aisle_back = null;
		}
		if (m_handler != null) {
			m_handler.removeCallbacksAndMessages(null);
			m_handler = null;
		}
		aisle_image = null;

		if (aisle_category != null) {
			aisle_category.removeButtonListener();
			aisle_category.setButtonListener(null);
			aisle_category = null;
		}
		aisle_slot_price = null;
		in = null;
		if (m_BusyDialog != null) {
			m_BusyDialog.dismiss();
			m_BusyDialog.deInit();
			m_BusyDialog = null;
		}
		if (m_LoadingDialog != null) {
			m_LoadingDialog.dismiss();
			m_LoadingDialog.deInit();
			m_LoadingDialog = null;
		}
		product_name = null;
		img_url = null;
		img_url_ad = null;
		image_path = null;
		image_path_ad = null;
		product_code = null;
		aisle_heat_layout = null;
		heat_time_edit = null;
		aisle_col_edit = null;
		aisle_row_edit = null;
		aisle_col_layout = null;
		aisle_row_layout = null;
		aisle_pull_back_layout = null;
		aisle_pull_back = null;
		aisle_pull_back_edit = null;
		product_spec = null;
		product_capacity = null;
		product_content = null;
		price = null;
		m_strCustomer = null;
		m_strDataType = null;
		coil_info = null;
		type_name_layout = null;
		aisle_goods_spec_layout = null;
		aisle_goods_capacity_layout = null;
		if (mCategoryDialog != null) {
			mCategoryDialog.deInit();
			mCategoryDialog.setButtonListener(null);
			mCategoryDialog = null;
		}

		m_ButtonClickListener = null;
		m_vendListener = null;
		super.onDestroy();
	}

	protected void initdata() {
		in = getIntent();
		num1 = in.getIntExtra("flag", 0);
		coil_info = VendDBControl.getInstance().getCoilInfo(num1);
		m_strCustomer = TcnShareUseData.getInstance().getTcnCustomerType();
		m_strDataType = TcnShareUseData.getInstance().getTcnDataType();
	}

	/**
	 * 显示相应的货道信息
	 */
	protected void viewdata() {

		aisle_num1.setText(String.valueOf(num1));

		if(coil_info.getID() > 0){
			product_name_edit.setHint(coil_info.getPar_name());
			extant_quantity=coil_info.getExtant_quantity();
			product_num_edit.setHint(String.valueOf(extant_quantity));
			capacity=coil_info.getCapacity();
			aisle_num_edit.setHint(String.valueOf(capacity));
			heat_time_edit.setHint(String.valueOf(coil_info.getHeatTime()));
			aisle_col_edit.setHint(String.valueOf(coil_info.getColumn()));
			aisle_row_edit.setHint(String.valueOf(coil_info.getRow()));
			aisle_pull_back_edit.setHint(String.valueOf(coil_info.getBack()));
			aisle_goods_spec_edit.setHint(String.valueOf(coil_info.getGoodsSpec()));
			aisle_goods_capacity_edit.setHint(String.valueOf(coil_info.getGoodsCapacity()));
			aisle_goods_content_edit.setHint(String.valueOf(coil_info.getContent()));
			product_code = coil_info.getGoodsCode();
			aisle_goods_code_edit.setHint(product_code);
			work_state=coil_info.getSlotStatus();
			aisle_state_edit.setHint(TcnCommon.SLOT_STATE_LIST[work_state]);

		//	price=coil_info.getPar_price();
			product_price_edit.setHint(coil_info.getPar_price());
			if("".equals(coil_info.getImg_url())||null==coil_info.getImg_url()){
				aisle_image.setImageDrawable(getResources().getDrawable(R.mipmap.default_ticket_poster_pic));
				if (aisle_slot_ad != null) {
					aisle_slot_ad.setImageDrawable(getResources().getDrawable(R.mipmap.default_ticket_poster_pic));
				}
			}else{
				TcnVendIF.getInstance().displayImage(coil_info.getImg_url(), aisle_image, R.mipmap.default_ticket_poster_pic);
				TcnVendIF.getInstance().displayImage(coil_info.getAdUrl(), aisle_slot_ad, R.mipmap.default_ticket_poster_pic);
			}

			aisle_category.setButtonText(coil_info.getType());

		}

		if (TcnShareUseData.getInstance().getTcnCustomerType().equals(TcnCustomer.CUSTOMER_BIANJIESHEN)) {
			aisle_goods_spec_layout.setVisibility(View.VISIBLE);
			aisle_goods_capacity_layout.setVisibility(View.VISIBLE);
		}

	}

	protected void modifySlotInfo() {
		if(num2 >= num1) {

			if((null==price) || ("".equals(price))) {
				//do nothing
			} else {
				TcnVendIF.getInstance().reqWriteSlotPrice(num1,num2,price);
			}

			String strCapacity=aisle_num_edit.getText().toString();//容量
			if((null==strCapacity) || ("".equals(strCapacity))) {

			} else {
				if (TcnVendIF.getInstance().isDigital(strCapacity.trim())) {
					capacity = Integer.valueOf(strCapacity.trim());
					TcnVendIF.getInstance().reqWriteSlotCapacity(num1,num2,capacity);
				}
			}

			String strQuantity=product_num_edit.getText().toString();//现存容量
			if((null==strQuantity) || ("".equals(strQuantity))) {

			} else {
				if (TcnVendIF.getInstance().isDigital(strQuantity.trim())) {
					extant_quantity = Integer.valueOf(strQuantity.trim());
					TcnVendIF.getInstance().reqWriteSlotStock(num1,num2,extant_quantity);
				}

			}

			String heat_time=heat_time_edit.getText().toString();//加热时间
			if((null==heat_time) || ("".equals(heat_time))) {

			} else {
				if ((Integer.valueOf(heat_time).intValue()) > 100) {
					TcnUtility.getToast(AisleManage.this, getString(R.string.tip_heat_time_too_large));
				}
				TcnVendIF.getInstance().reqUpdateHeatTime(num1,num2,Integer.valueOf(heat_time));
			}

			String mCol=aisle_col_edit.getText().toString();//列
			if((null==mCol) || ("".equals(mCol))) {

			} else {
				TcnVendIF.getInstance().reqUpdateColumn(num1,num2,Integer.valueOf(mCol));
			}

			String mRow=aisle_row_edit.getText().toString();//行
			if((null==mRow) || ("".equals(mRow))) {

			} else {
				TcnVendIF.getInstance().reqUpdateRow(num1,num2,Integer.valueOf(mRow));
			}

			String mPullBack=aisle_pull_back_edit.getText().toString();//拉回
			if((null==mPullBack) || ("".equals(mPullBack))) {

			} else {
				TcnVendIF.getInstance().reqUpdatePullBack(num1,num2,Integer.valueOf(mPullBack));
			}

			String strCode = aisle_goods_code_edit.getText().toString();//商品编码
			if((null==strCode) || ("".equals(strCode))) {

			} else {
				product_code = strCode.trim();
				TcnVendIF.getInstance().reqWriteSlotGoodsCode(num1,num2,product_code);
			}

			product_name=product_name_edit.getText().toString();//商品名

			if((null==product_name) || ("".equals(product_name))) {
			}else{
				TcnVendIF.getInstance().reqUpdateSlotName(num1,num2,product_name);//商品名称
			}

			product_spec=aisle_goods_spec_edit.getText().toString();//商品规格
			if((null==product_spec) || ("".equals(product_spec))) {

			} else {
				TcnVendIF.getInstance().reqUpdateSlotSpec(num1,num2,product_spec);
			}

			product_capacity=aisle_goods_capacity_edit.getText().toString();//商品容量
			if((null==product_capacity) || ("".equals(product_capacity))) {

			} else {
				TcnVendIF.getInstance().reqUpdateGoodsCapacity(num1,num2,product_capacity);
			}

			product_content=aisle_goods_content_edit.getText().toString();//商品介绍
			if((null==product_content) || ("".equals(product_content))) {

			} else {
				TcnVendIF.getInstance().reqUpdateSlotIntroduce(num1,num2,product_content);
			}

			if(isalter){
				TcnVendIF.getInstance().reqUpdateSlotImageUrl(num1,num2,img_url);//图片
			}

			if(isalter_ad){
				TcnVendIF.getInstance().reqUpdateSlotAdvertUrl(num1,num2,img_url_ad);
			}
		}

		TcnVendIF.getInstance().queryAliveCoil();
	}

	/**
	 * 更新货道信息
	 */
	protected void updateDataToDatabase() {
		m_iRecevCount = 0;
		modifySlotInfo();
	}

	protected int getScreen(boolean iswidth){
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

	/**
	 *
	 */
	protected void showBusyDialog(String data) {
		if (null == m_BusyDialog) {
			m_BusyDialog = new OutDialog(AisleManage.this, "",data);
		}
		m_BusyDialog.setNumber("");
		m_BusyDialog.setTitle(data);
		m_BusyDialog.show();
	}

	protected void showBusyDialog(int maxSlotNo,int maxShowSecond,String showMsg) {
		if (null == m_BusyDialog) {
			m_BusyDialog = new OutDialog(AisleManage.this, String.valueOf(maxSlotNo),showMsg);
		}
		m_BusyDialog.setNumber(String.valueOf(maxSlotNo));
		m_BusyDialog.setShowTime(maxShowSecond);
		m_BusyDialog.setTitle(showMsg);
		m_BusyDialog.show();
	}

	protected void cancelBusyDialog() {
		if (m_BusyDialog != null) {
			m_BusyDialog.cancel();
		}
	}

	private void takeAwayGoodsMenu(int errCode,String msg) {

		if(null != m_BusyDialog) {
			m_BusyDialog.cancel();
		}
		if ((null == msg) || (msg.length() < 1) || (errCode < 1)) {
			if (m_LoadingDialog == null) {
				m_LoadingDialog = new LoadingDialog(AisleManage.this,getString(R.string.notify_shipment_success),getString(R.string.notify_receive_goods));
			} else {
				m_LoadingDialog.setLoadText(getString(R.string.notify_shipment_success));
				m_LoadingDialog.setTitle(getString(R.string.notify_receive_goods));
			}
		} else {
			if (m_LoadingDialog == null) {
				m_LoadingDialog = new LoadingDialog(AisleManage.this,getString(R.string.notify_shipment_success),msg);
			} else {
				m_LoadingDialog.setLoadText(getString(R.string.notify_shipment_success));
				m_LoadingDialog.setTitle(msg);
			}
		}
		m_LoadingDialog.setShowTime(60);
		m_LoadingDialog.show();
	}

	protected int getEndSlotNo(){
		//货道号
		String aisle_num_str=aisle_num2.getText().toString();
		if("".equals(aisle_num_str)||null==aisle_num_str){
			num2 = num1;
		}else{
			if (TcnVendIF.getInstance().isDigital(aisle_num_str.trim())) {
				num2=Integer.parseInt(aisle_num_str.trim());
			}
			if(num2<num1){
				TcnUtility.getToast(this, getString(R.string.tip_aisle_enter));
				aisle_num2.requestFocus();
				num2 = num1;
			}
		}
		return num2;
	}

	/**
	 * 保存信息前获取所有信息
	 * @return
	 */
	protected boolean getData(){

		//货道号
		String aisle_num_str=aisle_num2.getText().toString();
		if("".equals(aisle_num_str)||null==aisle_num_str){
			num2 = num1;
		}else{
			if (TcnVendIF.getInstance().isDigital(aisle_num_str.trim())) {
				num2=Integer.parseInt(aisle_num_str.trim());
			}
			if(num2<num1){
				TcnUtility.getToast(this, getString(R.string.tip_aisle_enter));
				aisle_num2.requestFocus();
				return false;
			}
		}

		product_name=product_name_edit.getText().toString();//商品名

		if(("".equals(product_name)||null==product_name)){
			product_name=coil_info.getPar_name();
		}else{
			product_name=product_name_edit.getText().toString();//商品名
		}

		//商品单价
		String price_str= product_price_edit.getText().toString();
		if((null == price_str) || ("".equals(price_str))){
			//do nothing
		} else {
			if (TcnVendIF.getInstance().isDigital(price_str.trim()) || TcnVendIF.getInstance().isContainDeciPoint(price_str.trim())) {
				price = price_str.trim();
			}
		}

		//图片
		if(coil_info.getID() > 0) {
			img_url=coil_info.getImg_url();//图片
		}

		if(isalter) {
			img_url=image_path;
		}

		if(isalter_ad) {
			img_url_ad=image_path_ad;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.aisle_state_btn) {
			 showdialog(aisle_state_edit,TcnCommon.SLOT_STATE_LIST);
		} else if (id == R.id.aisle_pics) {
			String mPath = TcnVendIF.getInstance().getImageGoodsPath();
			if (null == mPath) {
				TcnUtility.getToast(this, getString(R.string.tip_picture_unchecked));
				return;
			}
			File file = new File(mPath);
			TcnVendIF.getInstance().LoggerDebug(TAG, "onClick mPath: " + mPath);
			if(!file.exists()){
				TcnUtility.getToast(this, getString(R.string.tip_picture_unchecked));
			} else {
				Intent in=new Intent(this, AislePics.class);
				if(coil_info.getID() > 0) {
					in.putExtra("path", coil_info.getImg_url());
				}
				startActivityForResult(in, 100);
			}
		} else if (id == R.id.aisle_btn_ad) {
			String mPath = TcnVendIF.getInstance().getAdvertPath();
			if (null == mPath) {
				TcnUtility.getToast(this, getString(R.string.tip_folder_unchecked));
				return;
			}
			File file = new File(mPath);
			if(!file.exists()){
				TcnUtility.getToast(this, getString(R.string.tip_folder_unchecked));
			} else {
				Intent in=new Intent(this, AisleSlotAd.class);
				if(coil_info.getID() > 0) {
					in.putExtra("pathAd", coil_info.getAdUrl());
				}
				startActivityForResult(in, 101);
			}
		} else if (id == R.id.aisle_start_test) {
			int iEndSlotNo = getEndSlotNo();
			TcnVendIF.getInstance().reqWriteDataShipTest(num1,iEndSlotNo);
		} else if (id == R.id.aisle_clear_fault) {
			if(getData()) {
				TcnVendIF.getInstance().reqClearSlotFaults(num1,num2);
			}
		} else if (id == R.id.aisle_confirm) {
			if (TcnUtility.isFastClick()) {
				return;
			}
			TcnVendIF.getInstance().LoggerDebug(TAG, "-----onClick() 点击了确认按钮，保存货道信息--------");
			if(getData()) {
				aisle_confirm.setClickable(false);
				updateDataToDatabase();
				cancelBusyDialog();
				TcnUtility.getToast(AisleManage.this, getString(R.string.tip_modify_success));
				if(num2 > 0 ) {//将页码传回展示界面
					in.putExtra("vpnum", findVpnum(num2)-1);
				} else {
					in.putExtra("vpnum", findVpnum(num1)-1);
				}
				setResult(101, in);
				AisleManage.this.finish();
			}
		} else if (id == R.id.aisle_back) {
			aisle_back.setClickable(false);
			this.finish();
		} else {
		}
	}

	protected void successWriteData() {
		if (m_handler != null) {
			Message message = m_handler.obtainMessage();
			message.what = TcnCommon.WRITE_DATA_CMD_SUCCESS;
			message.arg1 = m_iRecevCount;
			m_handler.removeMessages(TcnCommon.WRITE_DATA_CMD_SUCCESS);
			m_handler.sendMessageDelayed(message,3000);
		}
	}
	/**
	 * 找到最后更新的货道所在的页码传回展示界面
	 * @param num
	 * @return
	 */
	protected int findVpnum(int num){
		return num%TcnCommon.coil_num==0?num/TcnCommon.coil_num:num/TcnCommon.coil_num+1;
	}

	protected void showdialog(final EditText v,final String[] str){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.please_choose));
		builder.setSingleChoiceItems(str, work_state, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				work_state=which;
			}
		});
		builder.setPositiveButton(getString(R.string.backgroound_ensure), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String aisle_num_str=aisle_num2.getText().toString();
				if("".equals(aisle_num_str)||null==aisle_num_str) {
					num2 = num1;
				} else {
					if (TcnVendIF.getInstance().isDigital(aisle_num_str.trim())) {
						num2=Integer.parseInt(aisle_num_str.trim());
					}
				}
				TcnVendIF.getInstance().reqWriteSlotStatus(num1,num2,work_state);
				v.setText(str[work_state]);
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

	/**
	 * 从图库中获取选择的图片路径
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		TcnVendIF.getInstance().LoggerDebug(TAG, "-----onActivityResult() requestCode: "+requestCode+" resultCode: "+resultCode);
		if(data!=null){
			if (100 == requestCode) {
				isalter=true;
				String extra = data.getStringExtra("pic");
				image_path=extra;
				TcnVendIF.getInstance().LoggerDebug(TAG, "-----onActivityResult() extra: "+extra);
				TcnVendIF.getInstance().displayImage(image_path, aisle_image, R.mipmap.default_ticket_poster_pic);
			} else if (101 == requestCode) {
				isalter_ad=true;
				String extra = data.getStringExtra("slotAd");
				image_path_ad=extra;
				TcnVendIF.getInstance().LoggerDebug(TAG, "-----onActivityResult() 1 extra: "+extra);
				TcnVendIF.getInstance().displayImage(image_path_ad, aisle_slot_ad, R.mipmap.default_ticket_poster_pic);
			} else {

			}

		}
	}

	protected void showPayChoujiang(final EditText v,final String[] str) {

		int checkedItem = -1;
		Coil_info sInfo = TcnVendIF.getInstance().getCoilInfo(coil_info.getCoil_id());
		if ((null == sInfo) || (sInfo.getCoil_id() < 1)) {
			return;
		}
		if ((String.valueOf(UIComBack.TYPE_CJ_FLAG)).equals(sInfo.getType())) {
			checkedItem = 1;
		} else {
			checkedItem = 0;
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
				if (0 == singleitem) {
					TcnVendIF.getInstance().reqModifyType(num1,num2,"");
				} else if (1 == singleitem) {
					TcnVendIF.getInstance().reqModifyType(num1,num2,String.valueOf(UIComBack.TYPE_CJ_FLAG));
				} else {

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

	protected Handler m_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case TcnCommon.WRITE_DATA_CMD_SUCCESS:
					if (msg.arg1 == m_iRecevCount) {
						cancelBusyDialog();
						TcnUtility.getToast(AisleManage.this, getString(R.string.tip_modify_success));
						if(num2 > 0 ) {//将页码传回展示界面
							in.putExtra("vpnum", findVpnum(num2)-1);
						} else {
							in.putExtra("vpnum", findVpnum(num1)-1);
						}
						setResult(101, in);
						AisleManage.this.finish();
					}
					break;
				default:
					break;
			}
		}
	};

	protected ButtonClickListener m_ButtonClickListener= new ButtonClickListener();
	protected class ButtonClickListener implements ButtonEdit.ButtonListener {
		@Override
		public void onClick(View v, int buttonId) {
			if (null == v) {
				return;
			}
			int id = v.getId();
			if (R.id.aisle_category == id) {

				mCategoryDialog = new SelectDialog(AisleManage.this);
				mCategoryDialog.setTitle(R.string.please_choose);
				mCategoryDialog.setPositiveButton(getString(R.string.backgroound_ensure));
				mCategoryDialog.setNegativeButton(getString(R.string.backgroound_cancel));
				mCategoryDialog.setButtonListener(new DialogSelectListener());
				List<String> mList = TcnVendIF.getInstance().getAliveType();

				if (null == mList) {
					mCategoryDialog.show(aisle_category,new ArrayList<String>(),-1);
				} else {
					TcnVendIF.getInstance().LoggerDebug(TAG, "DialogSelectListener size: "+mList.size()+" getType: "+coil_info.getType()+" getCoil_id: "+coil_info.getCoil_id());
					mCategoryDialog.show(aisle_category,mList,mList.indexOf(coil_info.getType()));
				}
			}
		}
	}

	protected class DialogSelectListener implements SelectDialog.ButtonListener {
		@Override
		public void onClick(View view,int buttonId, String itemData) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "DialogSelectListener buttonId: "+buttonId+" itemData: "+itemData+" num1: "+num1);
			if (SelectDialog.BUTTON_ADD == buttonId) {
				String aisle_num_str=aisle_num2.getText().toString();
				if("".equals(aisle_num_str)||null==aisle_num_str) {
					num2 = num1;
				} else {
					if (TcnVendIF.getInstance().isDigital(aisle_num_str.trim())) {
						num2=Integer.parseInt(aisle_num_str.trim());
					}
				}
				TcnVendIF.getInstance().reqModifyType(num1,num2,itemData);
			} else if (SelectDialog.BUTTON_DECREASE == buttonId) {
				TcnVendIF.getInstance().reqDeleteType(itemData);
			} else if (SelectDialog.BUTTON_POSITIVE == buttonId) {
				String aisle_num_str=aisle_num2.getText().toString();
				if("".equals(aisle_num_str)||null==aisle_num_str) {
					num2 = num1;
				} else {
					if (TcnVendIF.getInstance().isDigital(aisle_num_str.trim())) {
						num2=Integer.parseInt(aisle_num_str.trim());
					}
				}
				TcnVendIF.getInstance().reqModifyType(num1,num2,itemData);
			}
			else {

			}
 		}

	}

	protected VendListener m_vendListener = new VendListener();
	protected class VendListener implements TcnVendIF.VendEventListener {

		@Override
		public void VendEvent(VendEventInfo cEventInfo) {
			if (null == cEventInfo) {
				TcnVendIF.getInstance().LoggerError(TAG, "VendListener cEventInfo is null");
				return;
			}
			switch (cEventInfo.m_iEventID) {
				case TcnVendEventID.COMMAND_CONFIG_OK:
					TcnVendIF.getInstance().LoggerDebug(TAG, "VendListener COMMAND_CONFIG_OK cEventInfo.m_lParam1: "+cEventInfo.m_lParam1+"  m_iRecevCount: "+m_iRecevCount);
					if (cEventInfo.m_lParam1 == 96) {     //测试货道
						//
					} else if (cEventInfo.m_lParam1 == 73) {
						TcnUtility.getToast(AisleManage.this, getString(R.string.tip_modify_success));
					} else {
						m_iRecevCount++;
						successWriteData();
					}
					break;
				case TcnVendEventID.DELETE_TYPE:
				case TcnVendEventID.MODIFY_TYPE:
					coil_info = VendDBControl.getInstance().getCoilInfo(num1);
					TcnVendIF.getInstance().LoggerError(TAG, "VendListener coil_info.getType(): "+coil_info.getType());
					List<String> mList = TcnVendIF.getInstance().getAliveType();
					if (mCategoryDialog != null) {
						mCategoryDialog.notifydatachanged(mList,mList.indexOf(coil_info.getType()));
					}
					break;
				case TcnVendEventID.CMD_TEST_SLOT:
					TcnVendIF.getInstance().LoggerDebug(TAG, "VendListener CMD_TEST_SLOT m_lParam3: "+cEventInfo.m_lParam3);
					if (cEventInfo.m_lParam3 == TcnVendEventResultID.SHIP_SHIPING) {
						showBusyDialog(cEventInfo.m_lParam1,25, getString(R.string.drive_slot_testing));
					}
					else if (cEventInfo.m_lParam3 == TcnVendEventResultID.SHIP_FAIL) {
						showBusyDialog(cEventInfo.m_lParam1,3, getString(R.string.notify_shipment_fail));
					} else if (cEventInfo.m_lParam3 == TcnVendEventResultID.SHIP_SUCCESS) {
						showBusyDialog(cEventInfo.m_lParam1,3, getString(R.string.notify_shipment_success));
					}
					else {
						cancelBusyDialog();
					}
					break;
				case TcnVendEventID.CMD_QUERY_STATUS_LIFTER:
					if (cEventInfo.m_lParam1 == TcnVendEventResultID.STATUS_WAIT_TAKE_GOODS) {
						takeAwayGoodsMenu(cEventInfo.m_lParam2,cEventInfo.m_lParam4);
					} else if (cEventInfo.m_lParam1 == TcnVendEventResultID.STATUS_FREE) {
						TcnVendIF.getInstance().LoggerDebug(TAG, "VendListener CMD_QUERY_STATUS_LIFTER STATUS_FREE");
						if (m_LoadingDialog != null) {
							m_LoadingDialog.dismiss();
						}
					} else {

					}
					break;
				default:
					break;
			}
		}

	}
}
