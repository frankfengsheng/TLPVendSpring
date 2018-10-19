package com.tlp.vendspring.activity.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tcn.vendspring.R;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.BaseActivity;
import com.tlp.vendspring.adapter.RecycleAisleMangerAdapter;
import com.tlp.vendspring.bean.AisleInfoBean;
import com.tlp.vendspring.util.MSUserUtils;
import com.tlp.vendspring.util.TLPApiServices;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AisleMangerActivity extends BaseActivity implements View.OnClickListener{

    private Button btn_shop_onsale,btn_inventorychange,btn_oneKey;
    private RecyclerView recyclerView;
    RecycleAisleMangerAdapter adapter;
    AisleInfoBean aisleInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_manger);
        initTitle("设备货道");
        init_view();

    }
    private void init_view()
    {
        btn_shop_onsale= (Button) findViewById(R.id.btn_shelf_manage_shop_onsale);
        btn_inventorychange= (Button) findViewById(R.id.btn_shelf_manage_inventory_change);
        btn_oneKey= (Button) findViewById(R.id.btn_shelf_manage_a_key_replenishment);
        btn_inventorychange.setVisibility(View.GONE);
        btn_oneKey.setVisibility(View.GONE);
        btn_shop_onsale.setVisibility(View.GONE);
        btn_shop_onsale.setOnClickListener(this);
        recyclerView= (RecyclerView) findViewById(R.id.ry_shelf_manager);
        btn_shop_onsale.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_shelf_manage_shop_onsale:
                //nextView(ShopEditorActivity.class);
                Intent intent=new Intent(this,ShopEditorActivity.class);
                startActivity(intent);
                break;
        }
    }
    /**
     * 获取货架管理页面
     * @param context
     * @return
     */
    public void GetAisleInfo(final Context context){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code","10020030011");
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        Call<AisleInfoBean> call=loginInfoPost.getAisleInfo(map);
        call.enqueue(new Callback<AisleInfoBean>() {
            @Override
            public void onResponse(Call<AisleInfoBean> call, Response<AisleInfoBean> response) {
                aisleInfoBean=response.body();
                if(aisleInfoBean!=null&&aisleInfoBean.getData()!=null&&aisleInfoBean.getData().size()>0){
                    refreshUI();
                }

            }

            @Override
            public void onFailure(Call<AisleInfoBean> call, Throwable t) {

            }
        });
    }
    private void refreshUI(){
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RecycleAisleMangerAdapter(this,aisleInfoBean.getData());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLis(new RecycleAisleMangerAdapter.OnItemclickListenner() {
            @Override
            public void onItemClick(View view,int postion) {

                Intent intent=new Intent(getApplicationContext(),AisleEditorActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("dataBean",aisleInfoBean.getData().get(postion));
                bundle.putInt("aisleCount",aisleInfoBean.getData().size());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        GetAisleInfo(getApplicationContext());
    }
}
