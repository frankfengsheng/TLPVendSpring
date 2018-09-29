package com.tcn.funcommon.db;

/**
 * Created by Administrator on 2016/12/17.
 */
public class Goods_info {
    private int m_ID = -1;//自增长id
    private String m_Goods_id = "";//商品编码
    private String m_Goods_name = "";//商品名
    private String m_Goods_price = "";//商品单价
    private String m_Goods_type = "";//商品类型
    private int m_Goods_stock = 0;//商品库存
    private String m_Goods_spec = "";     //商品规格
    private String m_Goods_capacity;//商品容量
    private String m_Goods_introduce = "";//商品说明
    private String m_Goods_details_url = "";//商品详情图片地址
    private int m_Goods_status = 0;//工作状态
    private String m_Goods_url = "";
    private int m_iSlotShipLast = 0;
    private String strOtherParam1 = "";
    private String strOtherParam2 = "";
    private String Goods_Slot_Map = "";
    private String m_AdUrl = "";



    public int getID() {
        return m_ID;
    }
    public void setID(int iD) {
        m_ID = iD;
    }

    public String getGoods_id() {
        if (null == m_Goods_id) {
            m_Goods_id = "";
        }
        return m_Goods_id;
    }
    public void setGoods_id(String Goods_id) {
        m_Goods_id = Goods_id;
    }

    public String getGoods_name() {
        if (null == m_Goods_name) {
            m_Goods_name = "";
        }
        return m_Goods_name;
    }
    public void setGoods_name(String Goods_name) {
        m_Goods_name = Goods_name;
    }

    public String getGoods_price() {
        if (null == m_Goods_price) {
            m_Goods_price = "";
        }
        return m_Goods_price;
    }
    public void setGoods_price(String Goods_price) {
        m_Goods_price = Goods_price;
    }

    public String getGoods_type() {
        if (null == m_Goods_type) {
            m_Goods_type = "";
        }
        return m_Goods_type;
    }
    public void setGoods_type(String Goods_type) {
        m_Goods_type = Goods_type;
    }

    public int getGoods_stock() {
        return m_Goods_stock;
    }
    public void setGoods_stock(int Goods_stock) {
        m_Goods_stock = Goods_stock;
    }

    public String getGoods_spec() {
        if (null == m_Goods_spec) {
            m_Goods_spec = "";
        }
        return m_Goods_spec;
    }
    public void setGoods_spec(String Goods_spec) {
        m_Goods_spec = Goods_spec;
    }

    public String getGoods_capacity() {
        return m_Goods_capacity;
    }
    public void setGoods_capacity(String Goods_capacity) {
        m_Goods_capacity = Goods_capacity;
    }

    public String getGoods_introduce() {
        if (null == m_Goods_introduce) {
            m_Goods_introduce = "";
        }
        return m_Goods_introduce;
    }
    public void setGoods_introduce(String Goods_introduce) {
        m_Goods_introduce = Goods_introduce;
    }

    public String getGoods_details_url() {
        if (null == m_Goods_details_url) {
            m_Goods_details_url = "";
        }
        return m_Goods_details_url;
    }
    public void setGoods_details_url(String Goods_details_url) {
        m_Goods_details_url = Goods_details_url;
    }

    public int getGoods_status() {
        return m_Goods_status;
    }
    public void setGoods_status(int Goods_status) {
        m_Goods_status = Goods_status;
    }

    public String getGoods_url() {
        if (null == m_Goods_url) {
            m_Goods_url = "";
        }
        return m_Goods_url;
    }
    public void setGoods_url(String url) {
        m_Goods_url = url;
    }


    public void setGoodsOtherParam1(String param) {
        strOtherParam1 = param;
    }

    public String getGoodsOtherParam1() {
        if (null == strOtherParam1) {
            strOtherParam1 = "";
        }
        return strOtherParam1;
    }

    public void setGoodsOtherParam2(String param) {
        strOtherParam2 = param;
    }

    public String getGoodsOtherParam2() {
        if (null == strOtherParam2) {
            strOtherParam2 = "";
        }
        return strOtherParam2;
    }

    public void setSlotShipLast(int slotNo) {
        m_iSlotShipLast = slotNo;
    }

    public int getSlotShipLast() {
        return m_iSlotShipLast;
    }

    public void setGoodsSlotMap(String map) {
        Goods_Slot_Map = map;
    }

    public String getGoodsSlotMap() {
        return Goods_Slot_Map;
    }

    public String getGoodsAdUrl() {
        return m_AdUrl;
    }

    public void setGoodsAdUrl(String url) {
        m_AdUrl = url;
    }
}
