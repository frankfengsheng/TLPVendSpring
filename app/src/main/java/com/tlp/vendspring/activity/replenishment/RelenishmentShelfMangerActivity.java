package com.tlp.vendspring.activity.replenishment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tcn.vendspring.R;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.BaseActivity;
import com.tlp.vendspring.activity.ReplenishmentEditorActivity;
import com.tlp.vendspring.activity.admin.ShopEditorActivity;
import com.tlp.vendspring.activity.admin.ShopOnSaleActivity;
import com.tlp.vendspring.adapter.RecycleShelfMangerAdapter;
import com.tlp.vendspring.bean.MsClearShelfInfoBean;
import com.tlp.vendspring.bean.MsShelfMangerInfoBean;
import com.tlp.vendspring.netutil.MSUserUtils;
import com.tlp.vendspring.netutil.TLPApiServices;
import com.tlp.vendspring.netutil.ToastUtil;
import com.tlp.vendspring.view.RecycleViewDivider;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RelenishmentShelfMangerActivity extends BaseActivity implements View.OnClickListener{

    private Button btn_shop_onsale;
    private RecyclerView recyclerView;
    RecycleShelfMangerAdapter adapter;
    MsShelfMangerInfoBean shelfMangerInfoBean;
    private Button btn_akey_replenishment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replenishment_shelf_manger);
        initTitle("商品货架");
        init_view();

    }
    private void init_view()
    {
        btn_shop_onsale= (Button) findViewById(R.id.btn_shelf_manage_inventory_change);
        btn_akey_replenishment= (Button) findViewById(R.id.btn_shelf_manage_a_key_replenishment);
        btn_shop_onsale.setOnClickListener(this);
        btn_akey_replenishment.setOnClickListener(this);
        recyclerView= (RecyclerView) findViewById(R.id.ry_shelf_manager);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                getApplicationContext(), LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.text_gray)));
        btn_shop_onsale.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_shelf_manage_shop_onsale:
                //nextView(ShopEditorActivity.class);
               nextView(this,ShopOnSaleActivity.class);
                break;
            case R.id.btn_shelf_manage_a_key_replenishment:

                break;
        }
    }

    /**
     * 获取货架管理页面
     * @param context
     * @return
     */
    public void GetShelfInfo(final Context context){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code","10020030011");
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        Call<MsShelfMangerInfoBean> call=loginInfoPost.getShelfInfo(map);
        call.enqueue(new Callback<MsShelfMangerInfoBean>() {
            @Override
            public void onResponse(Call<MsShelfMangerInfoBean> call, Response<MsShelfMangerInfoBean> response) {
                shelfMangerInfoBean=response.body();
                if(shelfMangerInfoBean!=null&&shelfMangerInfoBean.getData()!=null&&shelfMangerInfoBean.getData().size()>0){
                    refreshUI();
                }

            }

            @Override
            public void onFailure(Call<MsShelfMangerInfoBean> call, Throwable t) {

            }
        });
    }
    private void refreshUI(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RecycleShelfMangerAdapter(this,shelfMangerInfoBean.getData());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLis(new RecycleShelfMangerAdapter.OnItemclickListenner() {
            @Override
            public void onItemClick(View view,int postion) {
                Intent intent=new Intent(getApplicationContext(),ShopEditorActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("dateBean",shelfMangerInfoBean.getData().get(postion));
                intent.putExtras(bundle);
                startActivity(intent);

            }

            @Override
            public void onClearClick(View view,int position) {

                ClearShelfInfo(getApplicationContext(),shelfMangerInfoBean.getData().get(position).getChannel_start(),
                        shelfMangerInfoBean.getData().get(position).getChannel_end());
            }

            @Override
            public void onReplenishMentcClick(View view,int position) {
                Intent intent=new Intent(getApplicationContext(), ReplenishmentEditorActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("dateBean",shelfMangerInfoBean.getData().get(position));
                intent.putExtras(bundle);
                startActivity(intent);

            }

            @Override
            public void onOneKeyClick(View view, int position) {

            }
        });
    }

    /**
     * 清空货架
     * @param context
     * @return
     */
    public void ClearShelfInfo(final Context context,String start,String end){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code","10020030011");
        map.put("channel_start",start);
        map.put("channel_end",end);
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        Call<MsClearShelfInfoBean> call=loginInfoPost.clearShelfInfo(map);
        call.enqueue(new Callback<MsClearShelfInfoBean>() {
            @Override
            public void onResponse(Call<MsClearShelfInfoBean> call, Response<MsClearShelfInfoBean> response) {
                //shelfMangerInfoBean=response.body();
               MsClearShelfInfoBean clearShelfInfoBean=response.body();
               if(clearShelfInfoBean.getStatus()==200){
                   ToastUtil.showToast(context,"清空成功");
                   GetShelfInfo(context);
               }

            }

            @Override
            public void onFailure(Call<MsClearShelfInfoBean> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetShelfInfo(getApplicationContext());
    }
}
