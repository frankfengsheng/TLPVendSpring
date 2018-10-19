package com.tlp.vendspring.activity.admin;

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
import com.tlp.vendspring.activity.MSLoginMenu;
import com.tlp.vendspring.activity.ReplenishmentEditorActivity;
import com.tlp.vendspring.adapter.RecycleShelfMangerAdapter;
import com.tlp.vendspring.bean.MsClearShelfInfoBean;
import com.tlp.vendspring.bean.MsShelfMangerInfoBean;
import com.tlp.vendspring.util.DialogUtil;
import com.tlp.vendspring.util.MSUserUtils;
import com.tlp.vendspring.util.TLPApiServices;
import com.tlp.vendspring.util.ToastUtil;
import com.tlp.vendspring.view.RecycleViewDivider;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShelfMangerActivity extends BaseActivity implements View.OnClickListener{

    private Button btn_shop_onsale;
    private RecyclerView recyclerView;
    RecycleShelfMangerAdapter adapter;
    MsShelfMangerInfoBean shelfMangerInfoBean;
    private Button btn_onkey_replenishment;
    private Button btn_inventory_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_manger);
        initTitle("商品货架");
        init_view();

    }
    private void init_view()
    {
        btn_shop_onsale= (Button) findViewById(R.id.btn_shelf_manage_shop_onsale);
        if(MSLoginMenu.INDENTITY==1)btn_shop_onsale.setVisibility(View.GONE);//如果身份为补货员不显示上架商品
        btn_onkey_replenishment= (Button) findViewById(R.id.btn_shelf_manage_a_key_replenishment);
        btn_inventory_change= (Button) findViewById(R.id.btn_shelf_manage_inventory_change);
        btn_shop_onsale.setOnClickListener(this);
        recyclerView= (RecyclerView) findViewById(R.id.ry_shelf_manager);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                getApplicationContext(), LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.text_gray)));

        btn_shop_onsale.setOnClickListener(this);
        btn_onkey_replenishment.setOnClickListener(this);
        btn_inventory_change.setOnClickListener(this);

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
                new DialogUtil().showDialog(ShelfMangerActivity.this, "确定补满当前设备全部货道？", new DialogUtil.OnDialogSureClick() {
                    @Override
                    public void sureClick() {
                        oneKeyReplenishment(getApplicationContext());
                    }
                });
                break;
            case R.id.btn_shelf_manage_inventory_change:
                nextView(this,InventoryChaneActivity.class);
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
            public void onClearClick(View view, final int position) {
                new DialogUtil().showDialog(ShelfMangerActivity.this, "确定清空该货架？", new DialogUtil.OnDialogSureClick() {
                    @Override
                    public void sureClick() {
                        ClearShelfInfo(getApplicationContext(),shelfMangerInfoBean.getData().get(position).getChannel_start(),
                                shelfMangerInfoBean.getData().get(position).getChannel_end());
                    }
                });

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
            public void onOneKeyClick(View view, final int position) {
                new DialogUtil().showDialog(ShelfMangerActivity.this, "确定一键补货该货架？", new DialogUtil.OnDialogSureClick() {
                    @Override
                    public void sureClick() {
                        oneKeyReplenishmentByAisle(getApplicationContext(),shelfMangerInfoBean.getData().get(position).getChannel_start(),shelfMangerInfoBean.getData().get(position).getChannel_end());
                    }
                });
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

    /**
     * 一键补货按货道
     * @param context
     * @return
     */
    public void oneKeyReplenishmentByAisle(final Context context,String start,String end){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code","10020030011");
        map.put("channel_start",start);
        map.put("channel_end",end);
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        Call<MsClearShelfInfoBean> call=loginInfoPost.oneKeyReplenishmentByAisle(map);
        call.enqueue(new Callback<MsClearShelfInfoBean>() {
            @Override
            public void onResponse(Call<MsClearShelfInfoBean> call, Response<MsClearShelfInfoBean> response) {
                MsClearShelfInfoBean clearShelfInfoBean=response.body();
                if(clearShelfInfoBean.getStatus()==200){
                    ToastUtil.showToast(context,"一键补货成功");
                    GetShelfInfo(context);
                }
            }
            @Override
            public void onFailure(Call<MsClearShelfInfoBean> call, Throwable t) {

            }
        });
    }

    /**
     * 一键补货整个设备
     * @param context
     * @return
     */
    public void oneKeyReplenishment(final Context context){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code","10020030011");
        map.put("userid", MSUserUtils.getInstance().getUserId(getApplicationContext()));
        Call<MsClearShelfInfoBean> call=loginInfoPost.oneKeyReplenishment(map);
        call.enqueue(new Callback<MsClearShelfInfoBean>() {
            @Override
            public void onResponse(Call<MsClearShelfInfoBean> call, Response<MsClearShelfInfoBean> response) {
                MsClearShelfInfoBean clearShelfInfoBean=response.body();
                if(clearShelfInfoBean.getStatus()==200){
                    ToastUtil.showToast(context,"一键补货成功");
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
