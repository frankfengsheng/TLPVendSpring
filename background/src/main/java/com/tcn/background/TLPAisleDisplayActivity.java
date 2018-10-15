package com.tcn.background;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tcn.background.Entity.MSLoginBean;
import com.tcn.background.Entity.WeatherEntity;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public class TLPAisleDisplayActivity extends Activity implements View.OnClickListener{
    Context context;
    private RecyclerView recyclerView;
    Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlpaisle_display);
        context=this;
        init_view();
        doLogin();
       // doRequestByRxRetrofit();

    }

    /**
     * 初始化组件
     */
    private void init_view()
    {
        btn_back= (Button) findViewById(R.id.aisledisplay_back);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        recyclerView= (RecyclerView) findViewById(R.id.tlp_aisle_display_recycle);
        recyclerView.setLayoutManager(gridLayoutManager);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       if(v.getId()==R.id.aisledisplay_back)
       {
           finish();
       }
    }
    //http://app.51mengshou.com/api/User/login/

    public interface RxLoginService{
        @FormUrlEncoded
        @POST("api/User/login")
        Call<MSLoginBean> login(@Field("tel") long tel, @Field("pwd")String pwd, @Field("machine_code") String machine_code);
    }

    private void doLogin(){
        String  tel = "17721006035";
        Retrofit retrofit = new Retrofit.Builder() .baseUrl("http://app.51mengshou.com/").
                 addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        RxLoginService loginInfoPost=retrofit.create(RxLoginService.class);
        Call<MSLoginBean> call=loginInfoPost.login(Long.parseLong(tel),"123123","");
        call.enqueue(new Callback<MSLoginBean>() {
            @Override
            public void onResponse(Call<MSLoginBean> call, Response<MSLoginBean> response) {
                MSLoginBean bean=response.body();
                Toast.makeText(TLPAisleDisplayActivity.this,response.body().toString()+"",Toast.LENGTH_SHORT).show();
                Log.e("TAG", "response == " + bean.getData().getName());

            }

            @Override
            public void onFailure(Call<MSLoginBean> call, Throwable t) {
                Log.e("TAG", "Throwable : " + t);
            }
        });

    }

    public interface RxWeatherService{

        @GET("api/User/login")
        Call<WeatherEntity> getMessage();
    }

    private void doRequestByRxRetrofit() {
        Retrofit retrofit = new Retrofit.Builder() .baseUrl("http://app.51mengshou.com/").
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RxWeatherService rxjavaService = retrofit.create(RxWeatherService .class);
        Call<WeatherEntity> call = rxjavaService.getMessage();
        call.enqueue(new Callback<WeatherEntity>()
        {
            @Override
             public void onResponse(Call<WeatherEntity> call, Response<WeatherEntity> response)
            { //测试数据返回
            String result=response.body().toString();
            WeatherEntity weatherEntity = response.body();
            Toast.makeText(TLPAisleDisplayActivity.this,result+"",Toast.LENGTH_SHORT).show();
            Log.e("TAG", "response == " + weatherEntity.getResults().get(0).getDesc());
             }

        @Override
        public void onFailure(Call<WeatherEntity> call, Throwable t)
        {
                Log.e("TAG", "Throwable : " + t);

         }
        });

    }

    public void add(View v) {
        Intent in=new Intent(this, AisleManage.class);
        in.putExtra("flag", 1);
        startActivity(in);
    }


}
