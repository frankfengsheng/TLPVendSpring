package com.tcn.funcommon.db;

/**
 * Created by Administrator on 2016/4/16.
 */
public class UtilsDB {

    public static final int SLOT_STATE_HAVE_GOODS    = 0;
    public static final int SLOT_STATE_NO_GOODS       = 1;
    public static final int SLOT_STATE_FAULT          = 2;
    public static final int SLOT_STATE_DISABLE        = 3;
    public static final int SLOT_STATE_HIDE           = 4;


    public static final int KEY_STATE_HAVE_GOODS     = 0;
    public static final int KEY_STATE_NO_GOODS       = 1;
    public static final int KEY_STATE_NO_COIL       = 20;
    public static final int KEY_STATE_INVAILD        = 255;

    public static final int OPENED        = 1;
    public static final int CLOSED        = 0;

    /**Coil_info表名
     *
     */
    public static final String COIL_INFO_TABLE_NAME="Coil_info";
    public static final String COIL_INFO_ID="ID";
    public static final String COIL_INFO_COIL_ID="Coil_id";
    public static final String COIL_INFO_PAR_NAME="Par_name";
    public static final String COIL_INFO_EXTANT_QUANTITY="Extant_quantity"; //现存商品数
    public static final String COIL_INFO_WORK_STATUS="Work_status";
    public static final String COIL_INFO_RAY="ray";
    public static final String COIL_INFO_CONTENT="Content";   //商品介绍
    public static final String COIL_INFO_CAPACITY="Capacity"; //此货道商品容量
    public static final String COIL_INFO_PAR_PRICE="Par_price";
    public static final String COIL_INFO_IMG_URL="Img_url";
    public static final String COIL_INFO_TYPE="Type";
    public static final String COIL_INFO_SALE_NUM="Sale_num";
    public static final String COIL_INFO_SALE_AMOUNT="Sale_Amount";
    public static final String COIL_INFO_GOODS_CODE="Goods_code";
    public static final String COIL_INFO_GOODS_SPEC="Goods_spec";  //商品规格
    public static final String COIL_INFO_GOODS_CAPACITY="Goods_capacity";  //商品容量
    public static final String COIL_INFO_KEY_NUM="Key_num";  //按键号
    public static final String COIL_INFO_SLOT_STATUS="Slot_status";
    public static final String COIL_INFO_DETAIL_URL="Slot_details_url";
    public static final String COIL_INFO_SHIP_ORDER="Slot_order";
    public static final String COIL_INFO_OTHER_PARAM1="Other_param1";
    public static final String COIL_INFO_OTHER_PARAM2="Other_param2";
    public static final String COIL_INFO_QRPAY_URL="QR_url";
    public static final String COIL_INFO_AD_URL="Ad_url";   //对应广告Url
    public static final String COIL_INFO_HEAT_TIME="H_T";
    public static final String COIL_INFO_CLOSE_STATUS="C_S";    //0：关闭    1：打开
    public static final String COIL_INFO_ROW="s_row";
    public static final String COIL_INFO_COL="s_col";
    public static final String COIL_INFO_BACK="s_back";
    public static final String COIL_INFO_SALE_PRICE="Sale_price"; //优惠价格

    /**
     * Key map Slot表名
     */
    public static final String KEY_INFO_TABLE_NAME="KEY";
    public static final String KEY_INFO_ID="ID";
    public static final String KEY_INFO_KEY_ID="key_id";
    public static final String KEY_INFO_WORK_STATUS="Work_status";
    public static final String KEY_INFO_EXTANT_QUANTITY="Extant_quantity"; // 库存
    public static final String KEY_INFO_CAPACITY="Capacity"; //商品容量
    public static final String KEY_INFO_COILS="Coils";
    public static final String KEY_INFO_PAR_NAME="Par_name";
    public static final String KEY_INFO_CONTENT="Content";
    public static final String KEY_INFO_PAR_PRICE="Par_price";
    public static final String KEY_INFO_SALE_PRICE="Sale_price"; //优惠价格
    public static final String KEY_INFO_GOODS_CODE="Goods_code";
    public static final String KEY_INFO_TYPE="Type";
    public static final String KEY_INFO_IMG_URL="Img_url";
    public static final String KEY_INFO_QRPAY_URL="QR_url";
    public static final String KEY_INFO_DETAIL_URL="Slot_details_url";
    public static final String KEY_INFO_AD_URL="Ad_url";   //对应广告Url


    /**
     * Machine表名
     */
    public static final String MACHI_INFO_TABLE_NAME="Machine";
    public static final String MACHI_INFO_ID="id";
    public static final String MACHI_INFO_GRP_ID="grp_id";
    public static final String MACHI_INFO_TYPE="type";
    public static final String MACHI_INFO_ERR_CODE="err_code";


    /**
     * Sell表名
     */
    public static final String SELL_TABLE_NAME="Sell";
    public static final String SELL_ID="id";
    public static final String SELL_COIL_ID="Coil_id";
    public static final String SELL_PAYTYPE="paytype";
    public static final String SELL_RESULT="result";
    public static final String SELL_UP_FLAG="up_flag";
    public static final String SELL_Partner="Partner";
    public static final String SELL_Termina="Termina";
    public static final String SELL_DATA="DATA";
    public static final String SELL_Time="Time";
    public static final String SELL_CardNumber="CardNumber";
    public static final String SELL_SerialNumber="SerialNumber";
    public static final String SELL_ReferNo="ReferNo";

    /**
     * ServerData表名
     */
    public static final String SERVERDATA_TABLE_NAME="ServerData";
    public static final String SERVERDATA_ID="id";
    public static final String SERVERDATA_DATA="DATA";
    public static final String SERVERDATA_Time="Time";
    public static final String SERVERDATA_UPFLAG="Upflag";

    /**
     * AD
     */
    public static final String ADVERT_TABLE_NAME="Advert";
    public static final String ADVERT_ID="id";
    public static final String ADVERT_ADID="Ad_id";
    public static final String ADVERT_MACHINEID="Ad_machineID";
    public static final String ADVERT_NAME="Ad_img";
    public static final String ADVERT_ADRDOWNLOAD="Ad_download";
    public static final String ADVERT_RSTIME="Ad_rsTime";
    public static final String ADVERT_URL="Ad_url";
    public static final String ADVERT_PLAYTYPE="Ad_type";
    public static final String ADVERT_URL_LOCAL="Ad_local_url";
    public static final String ADVERT_ACTIVITY_ID="Ad_act_id";
    public static final String ADVERT_CONTENT="Ad_content";
    public static final String ADVERT_DETAIL_URL="Ad_details_url";



    /**Goods_info表名
     *
     */
    public static final String GOODS_INFO_TABLE_NAME="Goods_info";
    public static final String GOODS_INFO_ID="id";
    public static final String GOODS_INFO_PRODUCT_ID="Goods_id";    //商品编码
    public static final String GOODS_INFO_NAME="Goods_name";
    public static final String GOODS_INFO_PRICE="Goods_price";
    public static final String GOODS_INFO_TYPE="Goods_type";
    public static final String GOODS_INFO_STOCK="Goods_stock";
    public static final String GOODS_INFO_SPEC="Goods_spec";
    public static final String GOODS_INFO_CAPACITY="Goods_capacity";
    public static final String GOODS_INFO_INTRODUCE="Goods_introduce";
    public static final String GOODS_INFO_DETAILS_URL="Goods_details_url";
    public static final String GOODS_INFO_WORK_STATUS="Goods_status";
    public static final String GOODS_INFO_URL="Goods_url";
    public static final String GOODS_INFO_OTHER_PARAM1="Other_param1";
    public static final String GOODS_INFO_OTHER_PARAM2="Other_param2";
    public static final String GOODS_INFO_LAST_SHIP_SLOT="Goods_lSlot"; //最后出货的货道
    public static final String GOODS_INFO_SLOT_MAP="Goods_Slot_Map";
    public static final String GOODS_INFO_AD_URL="Goods_ad_url";   //对应广告Url


    /** 数据库名称 **/
    public static final String DATABASE_NAME = "TcnVending.db";
    /** 数据库版本号 **/
    public static final int DATABASE_VERSION = 15;

    /**
     * 建表语句
     */
    public static final String COIL_INFO_CREATE="create table if not exists Coil_info(ID integer primary key autoincrement," +
            "Coil_id integer,Par_name text,Extant_quantity integer,Work_status integer," +
            "ray integer,Content text,Capacity integer,Par_price text,Img_url text,Type text,Sale_num integer,Sale_Amount text,Goods_code text" +
            ",Goods_spec text,Goods_capacity text,Key_num integer,Slot_status integer,Slot_details_url text,Slot_order integer,Other_param1 text" +
            ",Other_param2 text,QR_url text,Ad_url text,H_T integer,C_S integer,s_row integer,s_col integer,s_back integer,Sale_price text)";


    public static final String COIL_INFO_CREATE1="create table if not exists Sell(id integer primary key autoincrement,Coil_id integer,paytype integer," +
            "result integer,up_flag integer,Partner text,Termina text,DATA text,Time text," +
            "CardNumber text,SerialNumber text,ReferNo text)";

    public static final String COIL_INFO_CREATE2="create table if not exists ServerData(id integer primary key autoincrement," +
            "DATA text,Time text,Upflag integer)";

    public static final String ADVERT_CREATE="create table if not exists Advert(id integer primary key autoincrement," +
            "Ad_id text,Ad_machineID text,Ad_img text,Ad_download text,Ad_rsTime text,Ad_url text,Ad_type text,Ad_local_url text,Ad_act_id text," +
            "Ad_content text,Ad_details_url text)";

    public static final String GOODS_CREATE="create table if not exists Goods_info(id integer primary key autoincrement," +
            "Goods_id text,Goods_name text,Goods_price text,Goods_type text,Goods_stock integer,Goods_spec text,Goods_capacity text,Goods_introduce text," +
            "Goods_details_url text,Goods_status integer,Goods_url text,Other_param1 text,Other_param2 text,Goods_lSlot integer,Goods_Slot_Map text," +
            "Goods_ad_url text)";


    public static final String KEY_INFO_CREATE="create table if not exists KEY(ID integer primary key autoincrement," +
            "key_id integer,Work_status integer,Extant_quantity integer,Capacity integer,Coils text,Par_name text,Content text," +
            "Par_price text,Sale_price text,Goods_code text,Type text,Img_url text,QR_url text,Slot_details_url text,Ad_url text)";

    public static final String COIL_TABLE_RENAME = "alter table Coil_info rename to _temp_Coil_info";
    public static final String COIL_TABLE_DROP = "drop table _temp_Coil_info";

    public static final String GOODS_TABLE_RENAME = "alter table Goods_info rename to _temp_Goods_info";
    public static final String GOODS_TABLE_DROP = "drop table _temp_Goods_info";

    public static final String ADVERT_TABLE_RENAME = "alter table Advert rename to _temp_Advert";
    public static final String ADVERT_TABLE_DROP = "drop table _temp_Advert";

    //更新至版本7
    public static final String INSERT_COIL_DATA_VER7 = "insert into Coil_info select *,'','' from _temp_Coil_info";
    public static final String INSERT_GOODS_DATA_VER7 = "insert into Goods_info select *,'','' from _temp_Goods_info";

    //更新至版本8
    public static final String INSERT_ADVERT_DATA_VER8 = "insert into Advert select *,'','','','' from _temp_Advert";

    //更新至版本9
    public static final String INSERT_GOODS_DATA_VER9 = "insert into Goods_info select *,0,'' from _temp_Goods_info";

    //更新至版本14
    public static final String INSERT_COIL_DATA_1 = "insert into Coil_info select *,0 from _temp_Coil_info";

    public static final String INSERT_COIL_DATA_2 = "insert into Coil_info select *,0 ,0 from _temp_Coil_info";

    public static final String INSERT_COIL_DATA_5 = "insert into Coil_info select *,-1,-1,-1,0,0 from _temp_Coil_info";

    public static final String INSERT_COIL_DATA_6 = "insert into Coil_info select *,-1,-1,-1,-1,0,0 from _temp_Coil_info";

    public static final String INSERT_COIL_DATA_7 = "insert into Coil_info select *,'',-1,-1,-1,-1,0,0 from _temp_Coil_info";

    public static final String INSERT_COIL_DATA_8 = "insert into Coil_info select *,'','',-1,-1,-1,-1,0,0 from _temp_Coil_info";

    public static final String INSERT_COIL_DATA_10 = "insert into Coil_info select *,'','','','',-1,-1,-1,-1,0,0 from _temp_Coil_info";

    //更新至版本11
    public static final String INSERT_GOODS_DATA_1 = "insert into Goods_info select *,'' from _temp_Goods_info";

}
