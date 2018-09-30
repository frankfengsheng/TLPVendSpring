package com.tcn.vendspring.netUtil;

import com.tcn.vendspring.bean.TLPGoodsInfoBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TLPApiServices {

    /***
     * 设备端展示商品
     */
    @FormUrlEncoded
    @POST("api/Channelpayment/show_goods/")
    Call<TLPGoodsInfoBean> getgoods(@FieldMap Map<String,String> map);

    /***
     * 登录
     */
    @FormUrlEncoded
    @POST("api/User/login")
    Call<TLPGoodsInfoBean> login(@FieldMap Map<String,String> map);

    }

