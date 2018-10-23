package com.tlp.vendspring.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tcn.vendspring.R;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.BaseActivity;
import com.tlp.vendspring.adapter.ReplenishmentAisleAdapter;
import com.tlp.vendspring.bean.MsClearShelfInfoBean;
import com.tlp.vendspring.bean.MsReplenishmentAisleResult;
import com.tlp.vendspring.bean.MsShelfMangerInfoBean;
import com.tlp.vendspring.util.DialogUtil;
import com.tlp.vendspring.util.MSUserUtils;
import com.tlp.vendspring.util.TLPApiServices;
import com.tlp.vendspring.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReplenishmentEditorActivity extends BaseActivity implements View.OnClickListener{

    private ImageView imageView;
    private TextView tv_name;
    private TextView tv_price;
    private TextView tv_capacity;
    private TextView tv_stock;
    private Button btn_clear;
    private Button btn_reset;
    private Button btn_onkey;
    private Button btn_cancle;
    private Button btn_sure;
    private RecyclerView recyclerView;
    MsReplenishmentAisleResult replenishmentAisleResult;
    ReplenishmentAisleAdapter adapter;
    MsShelfMangerInfoBean.DataBean dataBean;
    List<MsReplenishmentAisleResult.DataBean> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replenishment_editor);
        initTitle("商品编辑");
        init_view();

    }
    private void init_view()
    {
        dataBean= (MsShelfMangerInfoBean.DataBean) getIntent().getSerializableExtra("dateBean");
        imageView= (ImageView) findViewById(R.id.ms_iv_shelfmanger_adapter);
        tv_name= (TextView) findViewById(R.id.ms_tv_shelf_manger_adapter_title);
        tv_price= (TextView) findViewById(R.id.ms_tv_shelf_manger_adapter_price);
        tv_capacity= (TextView) findViewById(R.id.ms_tv_shelf_manger_capacity);
        tv_stock= (TextView) findViewById(R.id.ms_tv_shelf_manger_adapter_aisle);
        btn_clear= (Button) findViewById(R.id.btn_clear_inventory);
        btn_reset= (Button) findViewById(R.id.ms_btn_reset);
        btn_onkey= (Button) findViewById(R.id.ms_btn_one_key_replenishment);
        btn_cancle= (Button) findViewById(R.id.btn_cancle);
        btn_sure= (Button) findViewById(R.id.btn_save);
        recyclerView= (RecyclerView) findViewById(R.id.ms_ry_replenishment);

        btn_clear.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_onkey.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);

        initData();


    }
    private void initData(){
        if(dataBean!=null) {
            tv_name.setText(dataBean.getGoods_name());
            tv_price.setText("零售价: ￥"+dataBean.getPrice_sales());
            tv_stock.setText("存量："+dataBean.getChannel_remain());
            tv_capacity.setText("容量："+dataBean.getChannel_capacity());
            Glide.with(getApplicationContext()).load(dataBean.getGoods_url()).into(imageView);
            getAisleInfo(this, dataBean.getChannel_start(), dataBean.getChannel_end());
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_clear_inventory:
                new DialogUtil().showDialog(this, "确定清空库存？", new DialogUtil.OnDialogSureClick() {
                    @Override
                    public void sureClick() {
                        for(MsReplenishmentAisleResult.DataBean dataBean:list){
                            dataBean.setChannel_remain("0");
                        }
                        if(adapter!=null) adapter.notifyDataSetChanged();
                    }
                });
                break;
            case R.id.ms_btn_reset:
                new DialogUtil().showDialog(this, "确定重置？", new DialogUtil.OnDialogSureClick() {
                    @Override
                    public void sureClick() {
                        for(MsReplenishmentAisleResult.DataBean dataBean:list){
                            dataBean.setChannel_remains(0);
                        }
                       if(adapter!=null) adapter.notifyDataSetChanged();
                    }
                });
                break;
            case R.id.ms_btn_one_key_replenishment:
                new DialogUtil().showDialog(this, "确定一键补满所有货道？", new DialogUtil.OnDialogSureClick() {
                    @Override
                    public void sureClick() {
                        for(MsReplenishmentAisleResult.DataBean dataBean:list){
                            dataBean.setChannel_remains(Integer.parseInt(dataBean.getChannel_capacity())-Integer.parseInt(dataBean.getChannel_remain()));
                        }
                        if(adapter!=null)adapter.notifyDataSetChanged();
                    }
                });

                break;
            case R.id.btn_save:

                Gson gson=new Gson();
                String string_json=gson.toJson(list);
                submitInfo(getApplicationContext(),string_json);
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
    public void getAisleInfo(final Context context,String start,String end){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code",MSUserUtils.getInstance().getMachineCode(getApplicationContext()));
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        map.put("channel_start",start);
        map.put("channel_end",end);
        map.put("page","1");
        Call<MsReplenishmentAisleResult> call=loginInfoPost.getReplenishmentAisle(map);
        call.enqueue(new Callback<MsReplenishmentAisleResult>() {
            @Override
            public void onResponse(Call<MsReplenishmentAisleResult> call, Response<MsReplenishmentAisleResult> response) {
                replenishmentAisleResult=response.body();
                if(replenishmentAisleResult!=null&&replenishmentAisleResult.getData()!=null&&replenishmentAisleResult.getData().size()>0)
                {
                    refreshUI();
                }

            }

            @Override
            public void onFailure(Call<MsReplenishmentAisleResult> call, Throwable t) {
                finish();
                ToastUtil.showToast(context,"设置货道失败");
            }
        });
    }
    private void refreshUI(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list=replenishmentAisleResult.getData();
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ReplenishmentAisleAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLis(new ReplenishmentAisleAdapter.OnItemclickListenner() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onAddClick(View view, int position) {

            }

            @Override
            public void onReplenishMentcClick(View view, int position) {

            }

            @Override
            public void onReduceClick(View view, int position) {

            }
        });

    }

    /**
     * 获取补货道参数
     * @param context
     * @return
     */
    public void submitInfo(final Context context,String replenishment_data){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code",MSUserUtils.getInstance().getMachineCode(getApplicationContext()));
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        map.put("goods_id",dataBean.getGoods_id());
        map.put("goods_name",dataBean.getGoods_name());
        map.put("replenishment_data",replenishment_data);
        Call<MsClearShelfInfoBean> call=loginInfoPost.replenishmentPost(map);
        call.enqueue(new Callback<MsClearShelfInfoBean>() {
            @Override
            public void onResponse(Call<MsClearShelfInfoBean> call, Response<MsClearShelfInfoBean> response) {
                MsClearShelfInfoBean aisleEditorResultbean=response.body();
                if(aisleEditorResultbean!=null&&aisleEditorResultbean.getStatus()==200){
                    finish();
                    ToastUtil.showToast(context,aisleEditorResultbean.getMsg());
                }

            }

            @Override
            public void onFailure(Call<MsClearShelfInfoBean> call, Throwable t) {
                finish();
                ToastUtil.showToast(context,"补货失败");
            }
        });
    }
}
