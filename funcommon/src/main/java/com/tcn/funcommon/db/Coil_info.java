package com.tcn.funcommon.db;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/6/1 08:51
 * 邮箱：m68013@qq.com
 */
public class Coil_info {
    private int ID;//自增长id
    private int Coil_id;//货道号
    private String Par_name = "";//商品名
    private int Capacity;//容量
    private int Extant_quantity = 0;//现存数量
    private int Work_status = -1;//工作状态
    private int ray;//电光开关
    private String Content = "";//商品说明
    private String Par_price;//商品单价
    private String Sale_price;//优惠单价
    private String Img_url = "";//商品图片
    private String m_strType = "";//商品类型
    private int iSaleNum;
    private String dSaleAmount = "";
    private String strGoodsCode = "";
    private String sGoodsSpec = "";     //商品规格
    private String sGoodsCapacity = "";    //商品容量
    private int iKeyNum = 0;    //按键号
    private int iSlot_status = 0;    //货道状态
    private int iSlotOrder = 0;

    private String strOtherParam1 = "";
    private String strOtherParam2 = "";

    private String m_Goods_details_url = "";//商品详情图片地址

    private String m_QrPayUrl = "";
    private String m_AdUrl = "";

    private int m_iHeatTime = -1;

    private int m_iCloseStatus = -1;

    private int m_iRow = -1;

    private int m_iCol = -1;

    private int m_iBack = 0;  // 1：取回



    public Coil_info() {
        super();
    }

    public int getID() {
        return ID;
    }
    public void setID(int iD) {
        ID = iD;
    }
    public int getCoil_id() {
        return Coil_id;
    }
    public void setCoil_id(int coil_id) {
        Coil_id = coil_id;
    }
    public String getPar_name() {
        if (null == Par_name) {
            Par_name = "";
        }
        return Par_name;
    }
    public void setPar_name(String par_name) {
        Par_name = par_name;
    }
    public int getExtant_quantity() {
        return Extant_quantity;
    }
    public void setExtant_quantity(int extant_quantity) {
        Extant_quantity = extant_quantity;
    }
    public int getWork_status() {
        return Work_status;
    }
    public void setWork_status(int work_status) {
        Work_status = work_status;
    }
    public int getRay() {
        return ray;
    }
    public void setRay(int ray) {
        this.ray = ray;
    }
    public String getContent() {
        if (null == Content) {
            Content = "";
        }
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }
    public String getPar_price() {
        if (null == Par_price) {
            Par_price = "";
        }
        return Par_price;
    }
    public void setPar_price(String par_price) {
        Par_price = par_price;
    }
    public String getImg_url() {
        if (null == Img_url) {
            Img_url = "";
        }
        return Img_url;
    }

    public String getSalePrice() {
        return Sale_price;
    }
    public void setSalePrice(String par_price) {
        Sale_price = par_price;
    }

    public void setImg_url(String img_url) {
        Img_url = img_url;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public void setType(String type) {
        m_strType = type;
    }

    public String getType() {
        if (null == m_strType) {
            m_strType = "";
        }
        return m_strType;
    }

    public int getSaleNum() {
        return iSaleNum;
    }
    public void setSaleNum(int saleNum) {
        iSaleNum = saleNum;
    }

    public void setSaleAmount(String amount) {
        dSaleAmount = amount;
    }
    public String getSaleAmount() {
        if (null == dSaleAmount) {
            dSaleAmount = "";
        }
        return dSaleAmount;
    }

    public String getGoodsCode() {
        if (null == strGoodsCode) {
            strGoodsCode = "";
        }
        return strGoodsCode;
    }
    public void setGoodsCode(String goodsCode) {
        strGoodsCode = goodsCode;
    }

    public void setGoodsSpec(String spec) {
        sGoodsSpec = spec;
    }

    public String getGoodsSpec() {
        if (null == sGoodsSpec) {
            sGoodsSpec = "";
        }
        return sGoodsSpec;
    }

    public void setGoodsCapacity(String capacity) {
        sGoodsCapacity = capacity;
    }

    public String getGoodsCapacity() {
        if (null == sGoodsCapacity) {
            sGoodsCapacity = "";
        }
        return sGoodsCapacity;
    }

    public void setKeyNum(int num) {
        iKeyNum = num;
    }

    public int getKeyNum() {
        return iKeyNum;
    }

    public void setSlotStatus(int status) {
        iSlot_status = status;
    }

    public int getSlotStatus() {
        return iSlot_status;
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

    public void setSlotOrder(int order) {
        iSlotOrder = order;
    }

    public int getSlotOrder() {
        return iSlotOrder;
    }

    public void setOtherParam1(String param) {
        strOtherParam1 = param;
    }

    public String getOtherParam1() {
        if (null == strOtherParam1) {
            strOtherParam1 = "";
        }
        return strOtherParam1;
    }

    public void setOtherParam2(String param) {
        strOtherParam2 = param;
    }

    public String getOtherParam2() {
        if (null == strOtherParam2) {
            strOtherParam2 = "";
        }
        return strOtherParam2;
    }

    public String getQrPayUrl() {
        return m_QrPayUrl;
    }

    public void setQrPayUrl(String url) {
        m_QrPayUrl = url;
    }

    public String getAdUrl() {
        return m_AdUrl;
    }

    public void setAdUrl(String url) {
        m_AdUrl = url;
    }

    public void setHeatTime(int heatTime) {
        m_iHeatTime = heatTime;
    }

    public int getHeatTime() {
        return m_iHeatTime;
    }

    public void setCloseStatus(int status) {
        m_iCloseStatus = status;
    }

    public int getCloseStatus() {
        return m_iCloseStatus;
    }

    public void setRow(int row) {
        m_iRow = row;
    }

    public int getRow() {
        return m_iRow;
    }

    public void setColumn(int col) {
        m_iCol = col;
    }

    public int getColumn() {
        return m_iCol;
    }

    public int getBack() {
        return m_iBack;
    }
    public void setBack(int back) {
        this.m_iBack = back;
    }


    @Override
    public String toString() {
        return "Coil_info [ID=" + ID + ", Coil_id=" + Coil_id + ", Par_name="
                + Par_name + ", Capacity=" + Capacity + ", Extant_quantity="
                + Extant_quantity + ", Work_status=" + Work_status + ", ray="
                + ray + ", Content=" + Content + ", Par_price=" + Par_price
                + ", Img_url=" + Img_url + "]";
    }
}
