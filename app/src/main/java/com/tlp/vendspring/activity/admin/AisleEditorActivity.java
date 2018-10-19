package com.tlp.vendspring.activity.admin;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tcn.vendspring.R;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.BaseActivity;
import com.tlp.vendspring.activity.MSLoginMenu;
import com.tlp.vendspring.bean.AisleEditorResultbean;
import com.tlp.vendspring.bean.AisleInfoBean;
import com.tlp.vendspring.util.MSUserUtils;
import com.tlp.vendspring.util.TLPApiServices;
import com.tlp.vendspring.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AisleEditorActivity extends BaseActivity implements View.OnClickListener{

    private Button btn_save,btn_cancle;
    private TextView tv_guige,tv_price,tv_baozhiqi,tv_aislecapacity;
    private TextView tv_aisleNumber,tv_goodname,tv_goodType;
    private RadioGroup radioGroup;
    AisleInfoBean.DataBean dataBean;
    RadioButton rd_start,rd_stop;
    private String off_on;//设备是启用还是禁用状态
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aisle_editor);
        initTitle("货道管理");
        init_view();

    }
    private void init_view()
    {
        dataBean= (AisleInfoBean.DataBean) getIntent().getSerializableExtra("dataBean");
        btn_save= (Button) findViewById(R.id.btn_save);
        btn_cancle= (Button) findViewById(R.id.btn_cancle);

        tv_guige= (TextView) findViewById(R.id.ms_tv_good_guige);
        tv_price= (TextView) findViewById(R.id.ms_tv_good_price);
        tv_baozhiqi= (TextView) findViewById(R.id.ms_tv_good_baozhiqi);
        tv_aislecapacity= (TextView) findViewById(R.id.ms_tv_aisle_capacity);
        tv_aisleNumber= (TextView) findViewById(R.id.ms_tv_good_aisle_number);
        tv_goodname= (TextView) findViewById(R.id.ms_tv_good_name);
        tv_goodType= (TextView) findViewById(R.id.ms_tv_good_type);

        radioGroup= (RadioGroup) findViewById(R.id.ms_rg_aisle_editor);
        rd_start= (RadioButton) findViewById(R.id.ms_rd_aisle_start_usering);
        rd_stop= (RadioButton) findViewById(R.id.ms_rd_aisle_stop_usering);
        if(MSLoginMenu.INDENTITY==1){
            btn_cancle.setVisibility(View.GONE);
            btn_save.setVisibility(View.GONE);
            rd_start.setEnabled(false);
            rd_stop.setEnabled(false);
        }
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        btn_save.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        initData();

    }
    private void initData(){
        if(dataBean!=null){
            tv_goodname.setText(dataBean.getGoods_name());
            tv_aisleNumber.setText(dataBean.getChannel_num());
            tv_aislecapacity.setText(dataBean.getChannel_capacity());
            tv_price.setText(dataBean.getPrice_sales());
            tv_goodType.setText(dataBean.getGoods_type_name());
            tv_baozhiqi.setText(dataBean.getGoods_shelf_life());
            tv_guige.setText(dataBean.getGoods_model());
            if (dataBean.getStatus().equals("0")){
                off_on="off";
                rd_stop.setChecked(true);
            }else {
                off_on="on";
                rd_start.setChecked(true);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ms_title_btn_back:
                finish();
                break;
            case R.id.btn_save:
                submitAisleInfo(getApplicationContext(),dataBean.getChannel_num(),off_on,dataBean.getChannel_remain());
                break;
            case R.id.btn_cancle:
                finish();
                break;
        }
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.ms_rd_aisle_start_usering:
                        off_on="on";
                        break;
                    case R.id.ms_rd_aisle_stop_usering:
                        off_on="off";
                        break;
                }
        }
    };

    /**
     * 上架商品
     * @param context
     * @return
     */
    public void submitAisleInfo(final Context context,String channel_num,String on_off,String channel_remain){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code","10020030011");
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        map.put("channel_num",channel_num);
        map.put("on_off",on_off);
        map.put("channel_remain",channel_remain);
        Call<AisleEditorResultbean> call=loginInfoPost.editorAisleInfo(map);
        call.enqueue(new Callback<AisleEditorResultbean>() {
            @Override
            public void onResponse(Call<AisleEditorResultbean> call, Response<AisleEditorResultbean> response) {
                AisleEditorResultbean aisleEditorResultbean=response.body();
                if(aisleEditorResultbean!=null&&aisleEditorResultbean.getStatus()==200){
                    finish();
                    ToastUtil.showToast(context,aisleEditorResultbean.getMsg());
                }

            }

            @Override
            public void onFailure(Call<AisleEditorResultbean> call, Throwable t) {
                finish();
                ToastUtil.showToast(context,"设置货道失败");
            }
        });
    }


}
