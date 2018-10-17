package com.tlp.vendspring.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tcn.vendspring.R;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.BaseActivity;
import com.tlp.vendspring.bean.MSGoodsInfoBean;
import com.tlp.vendspring.bean.MsGoodTypeInfoBean;
import com.tlp.vendspring.bean.MsShelfGoodInfoBean;
import com.tlp.vendspring.bean.MsShelfMangerInfoBean;
import com.tlp.vendspring.bean.MsShlefGoodInfoSubmitBean;
import com.tlp.vendspring.netutil.MSUserUtils;
import com.tlp.vendspring.netutil.TLPApiServices;
import com.tlp.vendspring.netutil.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShopEditorActivity extends BaseActivity implements View.OnClickListener{

    private Button btn_save,btn_cancle;
    private EditText edt_start,edt_end;
    private TextView tv_guige,tv_price,tv_baozhiqi,tv_aislecapacity;
    private Spinner sp_type,sp_shop;
    MsGoodTypeInfoBean goodTypeInfoBean;
    private String[] shoptype_arr;
    private String[] shop_arr;
    private MsShelfGoodInfoBean shelfGoodInfoBean;
    private String goodId,goodType,goodGuige,goodPrice,goodBaozhiqi,capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_editor);
        initTitle("商品编辑");
        init_view();

    }
    private void init_view()
    {
        btn_save= (Button) findViewById(R.id.btn_save);
        btn_cancle= (Button) findViewById(R.id.btn_cancle);
        edt_start= (EditText) findViewById(R.id.ms_edt_shop_edit_aisle_start);
        edt_end= (EditText) findViewById(R.id.ms_edt_shop_edit_aisle_end);
        tv_guige= (TextView) findViewById(R.id.ms_tv_good_guige);
        tv_price= (TextView) findViewById(R.id.ms_tv_good_price);
        tv_baozhiqi= (TextView) findViewById(R.id.ms_tv_good_baozhiqi);
        tv_aislecapacity= (TextView) findViewById(R.id.ms_tv_aisle_capacity);
        sp_type= (Spinner) findViewById(R.id.ms_sp_shop_type);
        sp_shop= (Spinner) findViewById(R.id.ms_sp_shop);

        btn_save.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        GetGoodTypeInfo(getApplicationContext());
        GetGoodsInfo(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ms_title_btn_back:
                finish();
                break;
            case R.id.btn_save:
                String start=edt_start.getText().toString();
                String end=edt_end.getText().toString();
                if(TextUtils.isEmpty(start)){
                    ToastUtil.showToast(this,"请输入起始货道号");
                }else if(TextUtils.isEmpty(end)){
                    ToastUtil.showToast(this,"请输入结束货道号");
                }else if(TextUtils.isEmpty(goodId)){
                    ToastUtil.showToast(this,"请选择商品");
                }else if(TextUtils.isEmpty(goodType)){
                    ToastUtil.showToast(this,"请选择商品类型");
                }else {
                    submitGoodsInfo(getApplicationContext(),start,end,goodId,capacity,goodPrice,goodType);
                }
                break;
            case R.id.btn_cancle:
                finish();
                break;
        }
    }
    /**
     * 获取货架管理页面
     * @param context
     * @return
     */
    public MSGoodsInfoBean GetGoodTypeInfo(final Context context){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code","10020030011");
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        Call<MsGoodTypeInfoBean> call=loginInfoPost.getGoodTypeInfo(map);
        call.enqueue(new Callback<MsGoodTypeInfoBean>() {
            @Override
            public void onResponse(Call<MsGoodTypeInfoBean> call, Response<MsGoodTypeInfoBean> response) {
                goodTypeInfoBean=response.body();
                if(goodTypeInfoBean!=null&&goodTypeInfoBean.getStatus()==200&&goodTypeInfoBean.getData()!=null
                        &&goodTypeInfoBean.getData().size()>0){
                    refreshUI();
                }
            }
            @Override
            public void onFailure(Call<MsGoodTypeInfoBean> call, Throwable t) {

            }
        });
        return null;
    }
    /**
     * 获取商品管理
     * @param context
     * @return
     */
    public void GetGoodsInfo(final Context context){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code","10020030011");
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        Call<MsShelfGoodInfoBean> call=loginInfoPost.getShelfGoodInfo(map);
        call.enqueue(new Callback<MsShelfGoodInfoBean>() {
            @Override
            public void onResponse(Call<MsShelfGoodInfoBean> call, Response<MsShelfGoodInfoBean> response) {
                shelfGoodInfoBean=response.body();
                if(shelfGoodInfoBean!=null&&shelfGoodInfoBean.getData()!=null&&shelfGoodInfoBean.getData().size()>0){
                    initGoodSpinner();
                }

            }

            @Override
            public void onFailure(Call<MsShelfGoodInfoBean> call, Throwable t) {

            }
        });
    }
    private void refreshUI(){
        shoptype_arr=new String[goodTypeInfoBean.getData().size()];
        for(int i=0;i<goodTypeInfoBean.getData().size();i++){
            shoptype_arr[i]=goodTypeInfoBean.getData().get(i).getName();
        }
        initGoodTypeSpinner();
    }

    /**
     * 初始化商品类型spinner
     */
    private void initGoodTypeSpinner(){
        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.unit_spinner_item,shoptype_arr);
        sp_type.setAdapter(adapter);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                goodType=goodTypeInfoBean.getData().get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 初始化商品spinner
     */
    private void initGoodSpinner(){
        shop_arr=new String[shelfGoodInfoBean.getData().size()];
        for(int i=0;i<shelfGoodInfoBean.getData().size();i++){
            shop_arr[i]=shelfGoodInfoBean.getData().get(i).getGoods_name();
        }
        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.unit_spinner_item,shop_arr);
        sp_shop.setAdapter(adapter);
        sp_shop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_guige.setText(shelfGoodInfoBean.getData().get(position).getGoods_model());
                tv_price.setText(shelfGoodInfoBean.getData().get(position).getPrice_sales());
                tv_baozhiqi.setText(shelfGoodInfoBean.getData().get(position).getGoods_shelf_life());
                tv_aislecapacity.setText(shelfGoodInfoBean.getCapacity());

                goodId=shelfGoodInfoBean.getData().get(position).getGoods_id();
                goodBaozhiqi=shelfGoodInfoBean.getData().get(position).getGoods_shelf_life();
                goodPrice=shelfGoodInfoBean.getData().get(position).getPrice_sales();
                goodGuige=shelfGoodInfoBean.getData().get(position).getGoods_model();
                capacity=shelfGoodInfoBean.getCapacity();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * 上架商品
     * @param context
     * @return
     */
    public void submitGoodsInfo(final Context context,String start,String end,String goodid,String capacity,String price_sales,String goodType){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code","10020030011");
        map.put("channel_start", start);
        map.put("channel_end",end);
        map.put("goods_id", goodid);
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        map.put("capacity",capacity);
        map.put("price_sales", price_sales);
        map.put("goods_type",goodType);
        Call<MsShlefGoodInfoSubmitBean> call=loginInfoPost.submitShelf(map);
        call.enqueue(new Callback<MsShlefGoodInfoSubmitBean>() {
            @Override
            public void onResponse(Call<MsShlefGoodInfoSubmitBean> call, Response<MsShlefGoodInfoSubmitBean> response) {
                MsShlefGoodInfoSubmitBean shlefGoodInfoSubmitBean=response.body();
                if(shlefGoodInfoSubmitBean.getStatus()==200)
                {
                    ToastUtil.showToast(context,shlefGoodInfoSubmitBean.getMsg());
                    finish();
                }else {
                    ToastUtil.showToast(context,shlefGoodInfoSubmitBean.getMsg());
                }
            }

            @Override
            public void onFailure(Call<MsShlefGoodInfoSubmitBean> call, Throwable t) {

            }
        });
    }


}
