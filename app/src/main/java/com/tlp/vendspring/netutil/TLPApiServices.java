package com.tlp.vendspring.netutil;

import com.tcn.background.Entity.MSLoginBean;
import com.tlp.vendspring.bean.MSGoodsInfoBean;
import com.tlp.vendspring.bean.MsClearShelfInfoBean;
import com.tlp.vendspring.bean.MsShelfMangerInfoBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TLPApiServices {

    /***
     * 设备端展示商品
     */
    @FormUrlEncoded
    @POST("api/Channelpayment/show_goods/")
    Call<MSGoodsInfoBean> getgoods(@FieldMap Map<String,String> map);

    /***
     * 登录
     */
    @FormUrlEncoded
    @POST("api/User/login/")
    Call<MSLoginBean> login(@FieldMap Map<String,String> map);

    /***
     * 获取货架列表
     */
    @FormUrlEncoded
    @POST("api/Replenishment/check_machineshelves/")
    Call<MsShelfMangerInfoBean> getShelfInfo(@FieldMap Map<String,String> map);

    /***
     * 清空货架
     */
    @FormUrlEncoded
    @POST("api/Replenishment/empty_shelves/")
    Call<MsClearShelfInfoBean> clearShelfInfo(@FieldMap Map<String,String> map);


    }

