package com.tlp.vendspring.activity.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tcn.vendspring.R;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.BaseActivity;
import com.tlp.vendspring.bean.BindingMachineCodeResultBean;
import com.tlp.vendspring.util.DialogUtil;
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
 * create by feng
 */
public class MSAdminMangerActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_shelf_manager;
    private Button btn_aisle_manager;
    private Button btn_goods_manager;
    private Button btn_inventory_change;
    private Button btn_cancle_binding;
    private Button btn_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manger);
        initTitle("菜单");
        init_view();
    }

    private void init_view()
    {
        btn_shelf_manager= (Button) findViewById(R.id.ms_admin_menu_btn_shelf_manger);
        btn_aisle_manager= (Button) findViewById(R.id.ms_admin_menu_btn_aisle_manger);
        btn_goods_manager= (Button) findViewById(R.id.ms_admin_menu_btn_goods_manger);
        btn_inventory_change= (Button) findViewById(R.id.ms_admin_menu_btn_inventory_change);
        btn_cancle_binding= (Button) findViewById(R.id.ms_admin_menu_btn_cancle_binding);
        btn_exit= (Button) findViewById(R.id.ms_admin_menu_btn_exit);
        btn_shelf_manager.setOnClickListener(this);
        btn_aisle_manager.setOnClickListener(this);
        btn_goods_manager.setOnClickListener(this);
        btn_inventory_change.setOnClickListener(this);
        btn_cancle_binding.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ms_title_btn_back:
                this.finish();
                break;
            case R.id.ms_admin_menu_btn_shelf_manger:
               Intent intent=new Intent(getApplicationContext(),ShelfMangerActivity.class);
               startActivity(intent);
                break;
            case R.id.ms_admin_menu_btn_aisle_manger:
               nextView(getApplicationContext(),AisleMangerActivity.class);
                break;
            case R.id.ms_admin_menu_btn_goods_manger:
                this.finish();
                break;
            case R.id.ms_admin_menu_btn_exit:
              gotodesk();
                break;
            case R.id.ms_admin_menu_btn_inventory_change:
                nextView(getApplicationContext(),InventoryChaneActivity.class);
                break;
            case R.id.ms_admin_menu_btn_cancle_binding:
                new DialogUtil().showDialog(MSAdminMangerActivity.this, "解除设备绑定后，售货机将不能正常使用，需重新绑定后才能使用，确定解除绑定？", new DialogUtil.OnDialogSureClick() {
                    @Override
                    public void sureClick() {
                        cancleBbindingMachineCode(MSAdminMangerActivity.this,MSUserUtils.getInstance().getMachineCode(MSAdminMangerActivity.this));
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    //返回桌面
    public void gotodesk(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);

    }

    /**
     * 解除绑定设备
     * @param
     * @return
     */
    public void cancleBbindingMachineCode(final Context context, String machineCode){
        Retrofit retrofit =new RetrofitClient().getRetrofit(getApplicationContext());
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code",machineCode);
        Call<BindingMachineCodeResultBean> call=loginInfoPost.CancleBindingMachineCode(map);
        call.enqueue(new Callback<BindingMachineCodeResultBean>() {
            @Override
            public void onResponse(Call<BindingMachineCodeResultBean> call, Response<BindingMachineCodeResultBean> response) {
                BindingMachineCodeResultBean bean = response.body();
                if (bean != null&&bean.getStatus()==200) {
                    if(bean.getStatus()==200) {

                        MSUserUtils.getInstance().setMachineCode(context,null);
                        ToastUtil.showToast(context, bean.getMsg());
                        gotodesk();
                    }else {
                        ToastUtil.showToast(context, bean.getMsg());
                    }
                }else {
                    ToastUtil.showToast(context,"解除绑定失败请重试");
                }

            }
            @Override
            public void onFailure(Call<BindingMachineCodeResultBean> call, Throwable t) {
                ToastUtil.showToast(context,"解除绑定失败请重试");
            }
        });

    }
}
