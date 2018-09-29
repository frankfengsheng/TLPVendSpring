package com.tcn.funcommon.db;

/**
 * Created by Administrator on 2018/3/29.
 */
public class Key_info {
    private int ID;//自增长id
    private int m_iKeyId;//按键号
    private int Work_status = -1;//工作状态
    private int Extant_quantity = 0;//现存数量
    private int Capacity = 0;;//容量
    private String Coils = "";//货道
    private String Par_name = "";//商品名
    private String Content = "";//商品说明
    private String Par_price;//商品单价
    private String Sale_price;//优惠单价
    private String strGoodsCode = "";
    private String m_strType = "";//商品类型
    private String Img_url = "";//商品图片
    private String m_QrPayUrl = "";
    private String dSaleAmount = "";
    private String sGoodsSpec = "";     //商品规格
    private String sGoodsCapacity = "";    //商品容量
    private String m_Goods_details_url = "";//商品详情图片地址
    private String m_AdUrl = "";


    public Key_info() {
        super();
    }

    public int getID() {
        return ID;
    }
    public void setID(int iD) {
        ID = iD;
    }

    public void setKeyNum(int num) {
        m_iKeyId = num;
    }

    public int getKeyNum() {
        return m_iKeyId;
    }

    public int getWork_status() {
        return Work_status;
    }
    public void setWork_status(int work_status) {
        Work_status = work_status;
    }

    public int getExtant_quantity() {
        return Extant_quantity;
    }
    public void setExtant_quantity(int extant_quantity) {
        Extant_quantity = extant_quantity;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public String getCoils() {
        return Coils;
    }

    public void setCoils(String coils) {
        Coils = coils;
    }

    public String getPar_name() {
        return Par_name;
    }
    public void setPar_name(String par_name) {
        Par_name = par_name;
    }

    public String getContent() {
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }

    public String getPar_price() {
        return Par_price;
    }
    public void setPar_price(String par_price) {
        Par_price = par_price;
    }

    public String getSalePrice() {
        return Sale_price;
    }
    public void setSalePrice(String par_price) {
        Sale_price = par_price;
    }

    public void setType(String type) {
        m_strType = type;
    }

    public String getType() {
        return m_strType;
    }

    public String getImg_url() {
        return Img_url;
    }
    public void setImg_url(String img_url) {
        Img_url = img_url;
    }

    public String getGoodsCode() {
        return strGoodsCode;
    }
    public void setGoodsCode(String goodsCode) {
        strGoodsCode = goodsCode;
    }

    public String getQrPayUrl() {
        return m_QrPayUrl;
    }

    public void setQrPayUrl(String url) {
        m_QrPayUrl = url;
    }

    public String getGoods_details_url() {
        return m_Goods_details_url;
    }

    public void setGoods_details_url(String Goods_details_url) {
        m_Goods_details_url = Goods_details_url;
    }

    public String getAdUrl() {
        return m_AdUrl;
    }

    public void setAdUrl(String url) {
        m_AdUrl = url;
    }
}
