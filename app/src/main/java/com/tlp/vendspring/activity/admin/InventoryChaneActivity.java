package com.tlp.vendspring.activity.admin;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tcn.vendspring.R;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.BaseActivity;
import com.tlp.vendspring.bean.MsClearShelfInfoBean;
import com.tlp.vendspring.util.MSUserUtils;
import com.tlp.vendspring.util.TLPApiServices;
import com.tlp.vendspring.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InventoryChaneActivity extends BaseActivity implements View.OnClickListener{

    private EditText edt_start,edt_end;
    private EditText edt_inventory;
    private Button btn_save,btn_cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_change);
        initTitle("库存修改");
        init_view();

    }
    private void init_view()
    {
        edt_start= (EditText) findViewById(R.id.ms_edt_shop_edit_aisle_start);
        edt_end= (EditText) findViewById(R.id.ms_edt_shop_edit_aisle_end);
        edt_inventory= (EditText) findViewById(R.id.ms_edt_shop_edit_inventory);
        btn_save= (Button) findViewById(R.id.btn_save);
        btn_cancle= (Button) findViewById(R.id.btn_cancle);
        btn_save.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_save:
                String start=edt_start.getText().toString();
                String end=edt_end.getText().toString();
                String inventory=edt_inventory.getText().toString();
                if(TextUtils.isEmpty(start))
                {
                    ToastUtil.showToast(this,"请输入起始货道号");
                }else if(TextUtils.isEmpty(inventory)){
                    ToastUtil.showToast(this,"请输入库存");
                }else {
                    if(TextUtils.isEmpty(end))end=start;
                    changInventory(getApplicationContext(),start,end,inventory);
                }

                break;
            case R.id.btn_cancle:
                finish();
                break;
        }
    }


    /**
     * 获取补货道参数
     * @param context
     * @return
     */
    public void changInventory(final Context context, String start, String end,  String remain){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code",TLPApiServices.MACHINE_CODE);
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        map.put("channel_start",start);
        map.put("channel_end",end);
        map.put("channel_remain",remain);
        Call<MsClearShelfInfoBean> call=loginInfoPost.changeInventory(map);
        call.enqueue(new Callback<MsClearShelfInfoBean>() {
            @Override
            public void onResponse(Call<MsClearShelfInfoBean> call, Response<MsClearShelfInfoBean> response) {
                MsClearShelfInfoBean replenishmentAisleResult=response.body();
                    if(replenishmentAisleResult!=null&&replenishmentAisleResult.getStatus()==200){
                        ToastUtil.showToast(context,replenishmentAisleResult.getMsg());
                    }else {
                        ToastUtil.showToast(context,replenishmentAisleResult.getMsg());
                    }
                    finish();
            }

            @Override
            public void onFailure(Call<MsClearShelfInfoBean> call, Throwable t) {
                finish();
                ToastUtil.showToast(context,"设置货道失败");
            }
        });
    }
}
