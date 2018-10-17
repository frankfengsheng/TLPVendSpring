package com.tlp.vendspring.netutil;

import com.tcn.background.Entity.MSLoginBean;
import com.tlp.vendspring.bean.AisleEditorResultbean;
import com.tlp.vendspring.bean.AisleInfoBean;
import com.tlp.vendspring.bean.MSGoodsInfoBean;
import com.tlp.vendspring.bean.MsClearShelfInfoBean;
import com.tlp.vendspring.bean.MsGoodTypeInfoBean;
import com.tlp.vendspring.bean.MsReplenishmentAisleResult;
import com.tlp.vendspring.bean.MsShelfGoodInfoBean;
import com.tlp.vendspring.bean.MsShelfMangerInfoBean;
import com.tlp.vendspring.bean.MsShlefGoodInfoSubmitBean;

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

    /***
     * 获取商品类型
     */
    @FormUrlEncoded
    @POST("api/Auxiliary/goods_type/")
    Call<MsGoodTypeInfoBean> getGoodTypeInfo(@FieldMap Map<String,String> map);

    /***
     * 获取商品
     */
    @FormUrlEncoded
    @POST("api/Auxiliary/goods_list/")
    Call<MsShelfGoodInfoBean> getShelfGoodInfo(@FieldMap Map<String,String> map);

    /***
     * 上架商品
     */
    @FormUrlEncoded
    @POST("api/Administrators/onshelf_Goods/")
    Call<MsShlefGoodInfoSubmitBean> submitShelf(@FieldMap Map<String,String> map);

    /***
     * 管理员获取货道
     */
    @FormUrlEncoded
    @POST("api/Replenishment/show_channel_goods/")
    Call<AisleInfoBean> getAisleInfo(@FieldMap Map<String,String> map);

    /***
     * 管理员修改货道
     */
    @FormUrlEncoded
    @POST("api/Replenishment/channel_on_off/")
    Call<AisleEditorResultbean> editorAisleInfo(@FieldMap Map<String,String> map);
    /***
     * 获取补货货道
     */
    @FormUrlEncoded
    @POST("api/Replenishment/check_replenishment/")
    Call<MsReplenishmentAisleResult> getReplenishmentAisle(@FieldMap Map<String,String> map);
    /***
     * 补货员补货（不是补满）
     */
    @FormUrlEncoded
    @POST("api/Replenishment/replenishment/")
    Call<MsClearShelfInfoBean> replenishmentPost(@FieldMap Map<String,String> map);
}

