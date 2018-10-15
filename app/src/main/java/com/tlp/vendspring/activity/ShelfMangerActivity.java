package com.tlp.vendspring.activity;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcn.vendspring.R;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.adapter.RecycleShelfMangerAdapter;
import com.tlp.vendspring.bean.MSGoodsInfoBean;
import com.tlp.vendspring.bean.MsShelfMangerInfoBean;
import com.tlp.vendspring.netutil.MSUserUtils;
import com.tlp.vendspring.netutil.TLPApiServices;
import com.tlp.vendspring.view.RecycleViewDivider;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShelfMangerActivity extends Activity implements View.OnClickListener{
    TextView tv_title;
    Button btn_back;
    private Button btn_shop_onsale;
    private RecyclerView recyclerView;
    RecycleShelfMangerAdapter adapter;
    MsShelfMangerInfoBean shelfMangerInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_manger);
        init_view();

    }
    private void init_view()
    {

        tv_title= (TextView) findViewById(R.id.ms_tv_title_name);
        tv_title.setText("货架");
        btn_back= (Button) findViewById(R.id.ms_title_btn_back);
        btn_shop_onsale= (Button) findViewById(R.id.btn_shelf_manage_shop_onsale);
        btn_back.setOnClickListener(this);
        btn_shop_onsale.setOnClickListener(this);
        recyclerView= (RecyclerView) findViewById(R.id.ry_shelf_manager);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                getApplicationContext(), LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.text_gray)));


        GetShelfInfo(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ms_title_btn_back:
                finish();
                break;
            case R.id.btn_shelf_manage_shop_onsale:

                break;
        }
    }

    /**
     * 获取货架管理页面
     * @param context
     * @return
     */
    public MSGoodsInfoBean GetShelfInfo(final Context context){
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
        return null;
    }
    private void refreshUI(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RecycleShelfMangerAdapter(this,shelfMangerInfoBean.getData());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLis(new RecycleShelfMangerAdapter.OnItemclickListenner() {
            @Override
            public void onItemClick(View view) {
                int position=recyclerView.getChildAdapterPosition(view);
            }
        });
    }
}
