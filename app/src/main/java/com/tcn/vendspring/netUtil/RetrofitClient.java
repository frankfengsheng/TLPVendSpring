package com.tcn.vendspring.netUtil;

import android.content.Context;


import retrofit2.Retrofit;

/**
 * Created by feng
 */
public class RetrofitClient {
    private static Retrofit retrofit;

    private static void createRetrofitClient(Context context) {
        //http://test.tonglepai.com/
        //http://app.tonglepai.cn/
        retrofit = new Retrofit.Builder()
                //设置OKHttpClient
                .client(OkHttpFactory.getOkHttpClient(context))

                        //baseUrl
                .baseUrl("http://app.51mengshou.com/")

                        //gson转化器
                .addConverterFactory(JsonConverterFactory.create())

                .build();
    }

    public static Retrofit getRetrofit(Context context) {
        if (null == retrofit){
            synchronized (RetrofitClient.class){
                if (null == retrofit){
                    createRetrofitClient(context);
                }
            }
        }
        return retrofit;
    }
}
