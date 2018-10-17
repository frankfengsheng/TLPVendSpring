package com.tlp.vendspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.tcn.background.Entity.MSLoginBean;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendEventResultID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.view.TcnMainActivity;
import com.tcn.vendspring.R;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.activity.admin.MSAdminMangerActivity;
import com.tlp.vendspring.bean.MSGoodsInfoBean;
import com.tlp.vendspring.netutil.MSUserUtils;
import com.tlp.vendspring.netutil.TLPApiServices;
import com.tlp.vendspring.netutil.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * 猛兽登录菜单界面
 * create by feng
 * @author Administrator
 *
 */
public class MSLoginMenu extends TcnMainActivity implements OnClickListener {

	private EditText edt_phoneNumber,edt_pwd;
	private Button btn_login;
	private Button btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ms_loginmenu);

		btn_login= (Button) findViewById(R.id.login_btn);
		btn_back= (Button) findViewById(R.id.login_back);
		edt_phoneNumber= (EditText) findViewById(R.id.login_edit_phone);
		edt_pwd= (EditText) findViewById(R.id.login_edit_pwd);
		btn_login.setOnClickListener(this);
		btn_back.setOnClickListener(this);
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

		if (btn_login != null) {
			btn_login.setOnClickListener(null);
			btn_login = null;
		}
		if(btn_back!=null){
			btn_back.setOnClickListener(null);
			btn_back=null;
		}
		edt_pwd=null;
		edt_phoneNumber=null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.login_back) {
			this.finish();
		} else if (id == R.id.login_btn) {
			String phoneNumber=edt_phoneNumber.getText().toString();
			String pwd=edt_pwd.getText().toString();
			if(TextUtils.isEmpty(phoneNumber)){
				ToastUtil.showToast(getApplicationContext(),"帐号不能为空");
			}else if(TextUtils.isEmpty(pwd)) {
				ToastUtil.showToast(getApplicationContext(),"密码不能为空");
			}else {
				LoginInfo(phoneNumber,pwd,"");
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
	/**
	 * 获取设备端展示商品
	 * @param
	 * @return
	 */
	public MSGoodsInfoBean LoginInfo(String phoneNumber,String pwd,String machine_code){
		Retrofit retrofit =new RetrofitClient().getRetrofit(getApplicationContext());
		TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
		Map map=new HashMap();
		map.put("tel",phoneNumber);
		map.put("pwd",pwd);
		map.put("machine_code","10020030011");
		Call<MSLoginBean> call=loginInfoPost.login(map);
		call.enqueue(new Callback<MSLoginBean>() {
			@Override
			public void onResponse(Call<MSLoginBean> call, Response<MSLoginBean> response) {
				MSLoginBean loginBean=response.body();
				if(loginBean.getStatus()==1){
					MSUserUtils.getInstance().setUserId(getApplicationContext(),loginBean.getData().getUserid());
					if(loginBean.getData().getType().equals("4"))
					{

					}else if(loginBean.getData().getType().equals("6")){
						Intent intent=new Intent(getApplicationContext(),MSAdminMangerActivity.class);
						startActivity(intent);
					}
					ToastUtil.showToast(getApplicationContext(),loginBean.getMsg());
				}else {
					ToastUtil.showToast(getApplicationContext(),loginBean.getMsg());
				}
			}
			@Override
			public void onFailure(Call<MSLoginBean> call, Throwable t) {

			}
		});
		return null;
	}
}
