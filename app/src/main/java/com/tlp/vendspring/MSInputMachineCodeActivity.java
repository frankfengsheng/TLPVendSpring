package com.tlp.vendspring;

import android.content.Context;
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
import com.tlp.vendspring.bean.BindingMachineCodeResultBean;
import com.tlp.vendspring.bean.MSGoodsInfoBean;
import com.tlp.vendspring.util.MSUserUtils;
import com.tlp.vendspring.util.TLPApiServices;
import com.tlp.vendspring.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * 猛售登录菜单界面
 * create by feng
 * @author Administrator
 *
 */
public class MSInputMachineCodeActivity extends TcnMainActivity implements OnClickListener {

	private EditText edt_phoneNumber;
	private Button btn_login;
	private Button btn_back;
	public static int  INDENTITY=0;//身份0管理员  1补货员
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ms_machine_code_input);

		btn_login= (Button) findViewById(R.id.login_btn);
		btn_back= (Button) findViewById(R.id.login_back);
		edt_phoneNumber= (EditText) findViewById(R.id.login_edit_phone);
		btn_login.setOnClickListener(this);
		btn_back.setOnClickListener(this);
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
			if(TextUtils.isEmpty(phoneNumber)){
				ToastUtil.showToast(getApplicationContext(),"设备编号不能为空");
			}else {
				bindingMachineCode(this,phoneNumber);
			}
		}
	}


	/**
	 * 绑定设备编号
	 * @param
	 * @return
	 */
	public void bindingMachineCode(final Context context, String machineCode){
		Retrofit retrofit =new RetrofitClient().getRetrofit(getApplicationContext());
		TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
		Map map=new HashMap();
		map.put("machine_code",machineCode);
		Call<BindingMachineCodeResultBean> call=loginInfoPost.BindingMachineCode(map);
		call.enqueue(new Callback<BindingMachineCodeResultBean>() {
			@Override
			public void onResponse(Call<BindingMachineCodeResultBean> call, Response<BindingMachineCodeResultBean> response) {
				BindingMachineCodeResultBean bean = response.body();
				if (bean != null) {
					if(bean.getStatus()==200) {
						if(bean.getData()!=null){
							MSUserUtils.getInstance().setMachineCode(context,bean.getData().getMachine_code());
						}
						ToastUtil.showToast(context, bean.getMsg());
						finish();
					}else {
						if(bean!=null)ToastUtil.showToast(context, bean.getMsg());
					}
				}else {
					ToastUtil.showToast(context,"绑定失败请重试");
				}

			}
			@Override
			public void onFailure(Call<BindingMachineCodeResultBean> call, Throwable t) {
				ToastUtil.showToast(context,"绑定失败请重试");
			}
		});

	}
}
