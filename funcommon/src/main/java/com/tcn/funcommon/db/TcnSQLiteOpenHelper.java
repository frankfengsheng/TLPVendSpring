package com.tcn.funcommon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/6/1 08:48
 * 邮箱：m68013@qq.com
 */
public class TcnSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "TcnSQLiteOpenHelper";

    private static TcnSQLiteOpenHelper mInstance = null;

    public TcnSQLiteOpenHelper(Context context, String name,
                              int version) {
        super(context, name, null, version);
    }

    public TcnSQLiteOpenHelper(Context context) {
        super(context, UtilsDB.DATABASE_NAME, null, UtilsDB.DATABASE_VERSION);
    }

    public static synchronized TcnSQLiteOpenHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TcnSQLiteOpenHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UtilsDB.COIL_INFO_CREATE);
        db.execSQL(UtilsDB.COIL_INFO_CREATE1);
        db.execSQL(UtilsDB.COIL_INFO_CREATE2);
        db.execSQL(UtilsDB.ADVERT_CREATE);
        db.execSQL(UtilsDB.GOODS_CREATE);
        db.execSQL(UtilsDB.KEY_INFO_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
            case 15:
                if (6 == oldVersion) {
                    db.execSQL(UtilsDB.GOODS_TABLE_RENAME);
                    db.execSQL(UtilsDB.GOODS_CREATE);
                    db.execSQL("insert into Goods_info select *,'','',0,'','' from _temp_Goods_info");
                    db.execSQL(UtilsDB.GOODS_TABLE_DROP);

                    db.execSQL(UtilsDB.COIL_TABLE_RENAME);
                    db.execSQL(UtilsDB.COIL_INFO_CREATE);
                    db.execSQL(UtilsDB.INSERT_COIL_DATA_10);
                    db.execSQL(UtilsDB.COIL_TABLE_DROP);

                    db.execSQL(UtilsDB.ADVERT_TABLE_RENAME);
                    db.execSQL(UtilsDB.ADVERT_CREATE);
                    db.execSQL(UtilsDB.INSERT_ADVERT_DATA_VER8);
                    db.execSQL(UtilsDB.ADVERT_TABLE_DROP);

                    db.execSQL(UtilsDB.KEY_INFO_CREATE);

                } else if (7 == oldVersion) {
                    db.execSQL(UtilsDB.ADVERT_TABLE_RENAME);
                    db.execSQL(UtilsDB.ADVERT_CREATE);
                    db.execSQL(UtilsDB.INSERT_ADVERT_DATA_VER8);
                    db.execSQL(UtilsDB.ADVERT_TABLE_DROP);

                    db.execSQL(UtilsDB.GOODS_TABLE_RENAME);
                    db.execSQL(UtilsDB.GOODS_CREATE);
                    db.execSQL("insert into Goods_info select *,0,'','' from _temp_Goods_info");
                    db.execSQL(UtilsDB.GOODS_TABLE_DROP);

                    db.execSQL(UtilsDB.COIL_TABLE_RENAME);
                    db.execSQL(UtilsDB.COIL_INFO_CREATE);
                    db.execSQL(UtilsDB.INSERT_COIL_DATA_8);
                    db.execSQL(UtilsDB.COIL_TABLE_DROP);

                    db.execSQL(UtilsDB.KEY_INFO_CREATE);

                } else if (8 == oldVersion) {
                    db.execSQL(UtilsDB.GOODS_TABLE_RENAME);
                    db.execSQL(UtilsDB.GOODS_CREATE);
                    db.execSQL("insert into Goods_info select *,0,'','' from _temp_Goods_info");
                    db.execSQL(UtilsDB.GOODS_TABLE_DROP);

                    db.execSQL(UtilsDB.COIL_TABLE_RENAME);
                    db.execSQL(UtilsDB.COIL_INFO_CREATE);
                    db.execSQL(UtilsDB.INSERT_COIL_DATA_8);
                    db.execSQL(UtilsDB.COIL_TABLE_DROP);

                    db.execSQL(UtilsDB.KEY_INFO_CREATE);

                } else if (9 == oldVersion) {
                    db.execSQL(UtilsDB.GOODS_TABLE_RENAME);
                    db.execSQL(UtilsDB.GOODS_CREATE);
                    db.execSQL("insert into Goods_info select *,'' from _temp_Goods_info");
                    db.execSQL(UtilsDB.GOODS_TABLE_DROP);

                    db.execSQL(UtilsDB.COIL_TABLE_RENAME);
                    db.execSQL(UtilsDB.COIL_INFO_CREATE);
                    db.execSQL(UtilsDB.INSERT_COIL_DATA_8);
                    db.execSQL(UtilsDB.COIL_TABLE_DROP);

                    db.execSQL(UtilsDB.KEY_INFO_CREATE);

                } else if (10 == oldVersion) {
                    db.execSQL(UtilsDB.GOODS_TABLE_RENAME);
                    db.execSQL(UtilsDB.GOODS_CREATE);
                    db.execSQL("insert into Goods_info select *,'' from _temp_Goods_info");
                    db.execSQL(UtilsDB.GOODS_TABLE_DROP);

                    db.execSQL(UtilsDB.COIL_TABLE_RENAME);
                    db.execSQL(UtilsDB.COIL_INFO_CREATE);
                    db.execSQL(UtilsDB.INSERT_COIL_DATA_7);
                    db.execSQL(UtilsDB.COIL_TABLE_DROP);

                    db.execSQL(UtilsDB.KEY_INFO_CREATE);

                } else if (11 == oldVersion) {
                    db.execSQL(UtilsDB.COIL_TABLE_RENAME);
                    db.execSQL(UtilsDB.COIL_INFO_CREATE);
                    db.execSQL(UtilsDB.INSERT_COIL_DATA_6);
                    db.execSQL(UtilsDB.COIL_TABLE_DROP);

                    db.execSQL(UtilsDB.KEY_INFO_CREATE);

                } else if (12 == oldVersion) {
                    db.execSQL(UtilsDB.COIL_TABLE_RENAME);
                    db.execSQL(UtilsDB.COIL_INFO_CREATE);
                    db.execSQL(UtilsDB.INSERT_COIL_DATA_5);
                    db.execSQL(UtilsDB.COIL_TABLE_DROP);

                    db.execSQL(UtilsDB.KEY_INFO_CREATE);

                } else if (13 == oldVersion) {
                    db.execSQL(UtilsDB.COIL_TABLE_RENAME);
                    db.execSQL(UtilsDB.COIL_INFO_CREATE);
                    db.execSQL(UtilsDB.INSERT_COIL_DATA_2);
                    db.execSQL(UtilsDB.COIL_TABLE_DROP);

                    db.execSQL(UtilsDB.KEY_INFO_CREATE);
                } else if (14 == oldVersion) {
                    db.execSQL(UtilsDB.COIL_TABLE_RENAME);
                    db.execSQL(UtilsDB.COIL_INFO_CREATE);
                    db.execSQL(UtilsDB.INSERT_COIL_DATA_1);
                    db.execSQL(UtilsDB.COIL_TABLE_DROP);

                    db.execSQL(UtilsDB.KEY_INFO_CREATE);
                }
                else {

                }
                break;

            default:
                break;
        }
    }

    public boolean insertData(String tablename,ContentValues values){
        SQLiteDatabase database=this.getWritableDatabase();
        long insert = database.insert(tablename, null, values);
        if(insert!=-1){
            return true;
        }else{
            return false;
        }
    }

    public int deleteSlotNo(String tablename,int coil_id) {
        SQLiteDatabase database=this.getWritableDatabase();
        return database.delete(tablename,UtilsDB.COIL_INFO_COIL_ID + "=?", new String[]{String.valueOf(coil_id)});
    }

    public boolean deleteKeyMap(int keyNum) {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UtilsDB.KEY_INFO_COILS, "");

        int i = database.update(UtilsDB.KEY_INFO_TABLE_NAME, values, UtilsDB.KEY_INFO_KEY_ID + "=?", new String[]{String.valueOf(keyNum)});

        if(i==-1){
            return false;
        }else{
            return true;
        }
    }


    public Cursor queryData(String tablename){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.query(tablename, null, null, null, null, null, null);
    }

    public boolean updateData(String tablename,ContentValues values,int id) {
        if (null == values) {
            return false;
        }
        SQLiteDatabase database=this.getWritableDatabase();
        int i = -1;
        if (tablename.equals(UtilsDB.COIL_INFO_TABLE_NAME)) {
            i = database.update(tablename, values, UtilsDB.COIL_INFO_COIL_ID + "=?", new String[]{String.valueOf(id)});
        } else if (tablename.equals(UtilsDB.GOODS_INFO_TABLE_NAME)) {
            i = database.update(tablename, values, UtilsDB.GOODS_INFO_ID + "=?", new String[]{String.valueOf(id)});
        } else if (tablename.equals(UtilsDB.KEY_INFO_TABLE_NAME)) {
            i = database.update(tablename, values, UtilsDB.KEY_INFO_KEY_ID + "=?", new String[]{String.valueOf(id)});
        }
        else {

        }
        if(i==-1){
            return false;
        }else{
            return true;
        }
    }

    public int deleteData(String tablename,int id) {
        SQLiteDatabase database=this.getWritableDatabase();
        int i = -1;
        if (tablename.equals(UtilsDB.COIL_INFO_TABLE_NAME)) {
            i = database.delete(tablename,UtilsDB.COIL_INFO_COIL_ID + "=?", new String[]{String.valueOf(id)});
        } else if (tablename.equals(UtilsDB.GOODS_INFO_TABLE_NAME)) {
            i = database.delete(tablename,UtilsDB.GOODS_INFO_ID + "=?", new String[]{String.valueOf(id)});
        } else if (tablename.equals(UtilsDB.KEY_INFO_TABLE_NAME)) {
            i = database.delete(tablename,UtilsDB.KEY_INFO_KEY_ID + "=?", new String[]{String.valueOf(id)});
        }
        else {

        }
        return i;
    }

    public int deleteData(String tablename,String goodsId) {
        SQLiteDatabase database=this.getWritableDatabase();
        int i = -1;
        if (tablename.equals(UtilsDB.COIL_INFO_TABLE_NAME)) {
            i = database.delete(tablename,UtilsDB.COIL_INFO_GOODS_CODE + "=?", new String[]{goodsId});
        } else if (tablename.equals(UtilsDB.GOODS_INFO_TABLE_NAME)) {
            i = database.delete(tablename,UtilsDB.GOODS_INFO_PRODUCT_ID + "=?", new String[]{goodsId});
        } else {

        }
        return i;
    }


    public boolean updateDataGoods(ContentValues values,int id) {
        SQLiteDatabase database=this.getWritableDatabase();
        int i = database.update(UtilsDB.GOODS_INFO_TABLE_NAME, values, UtilsDB.GOODS_INFO_ID + "=?", new String[]{String.valueOf(id)});
        if(i==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateSell(String tablename,ContentValues values,int coil_id) {
        SQLiteDatabase database=this.getWritableDatabase();
        int i = database.update(tablename, values, UtilsDB.SELL_COIL_ID+"=?", new String[]{String.valueOf(coil_id)});
        if(i==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateServerData(String tablename,ContentValues values,int coil_id) {
        SQLiteDatabase database=this.getWritableDatabase();
        int i = database.update(tablename, values, UtilsDB.SERVERDATA_ID + "=?", new String[]{String.valueOf(coil_id)});
        if(i==-1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateAdvert(String tablename,ContentValues values,String adId) {
        SQLiteDatabase database=this.getWritableDatabase();
        int i = database.update(tablename, values, UtilsDB.ADVERT_ADID + "=?", new String[]{adId});
        if(i==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateAdvertByName(String tablename,ContentValues values,String name) {
        SQLiteDatabase database=this.getWritableDatabase();
        int i = database.update(tablename, values, UtilsDB.ADVERT_NAME + "=?", new String[]{name});
        if(i==-1){
            return false;
        }else{
            return true;
        }
    }

    public int deleteAdvert(String tablename,String adId) {
        SQLiteDatabase database=this.getWritableDatabase();
        return database.delete(tablename,UtilsDB.ADVERT_ADID + "=?", new String[]{adId});
    }

    public int deleteAdvertByName(String tablename,String name) {
        SQLiteDatabase database=this.getWritableDatabase();
        return database.delete(tablename,UtilsDB.ADVERT_NAME + "=?", new String[]{name});
    }

    public Coil_info queryCoilInfo(int coil_id) {
        Coil_info info = new Coil_info();
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null,
                    UtilsDB.COIL_INFO_COIL_ID + "=?",
                    new String[]{String.valueOf(coil_id)}, null, null, UtilsDB.COIL_INFO_COIL_ID);
            if(c.moveToNext()) {
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));

            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public List<Coil_info> queryKeyCoilInfo(int key) {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null,
                    UtilsDB.COIL_INFO_KEY_NUM + "=?",
                    new String[]{String.valueOf(key)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            if(c.moveToNext()) {
                Coil_info info = new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));
                coil_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coil_list;
    }


    public List<Coil_info> queryUsefulCoil() {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_EXTANT_QUANTITY + ">? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(0),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, null);
            while (c.moveToNext()) {
                Coil_info info = new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));
                coil_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coil_list;
    }


    public List<Coil_info> queryAliveCoil() {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS + "<? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                Coil_info info=new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));
                coil_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coil_list;
    }

    public List<Coil_info> queryAliveCoilAll() {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS + "<? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),String.valueOf(UtilsDB.SLOT_STATE_HIDE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                Coil_info info=new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));
                coil_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coil_list;
    }

    public List<Coil_info> queryOpendCoil() {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS + "<? and "+ UtilsDB.COIL_INFO_CLOSE_STATUS + "=?",
                    new String[]{String.valueOf(255),String.valueOf(UtilsDB.OPENED)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                Coil_info info=new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));
                coil_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coil_list;
    }

    public List<Coil_info> queryCoilListExceptKey() {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS + "<? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                Coil_info info=new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));
                if (info.getCoil_id() > 60) {
                    coil_list.add(info);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coil_list;
    }

    public List<Coil_info> queryKeyMapCoilList() {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        List<Coil_info> coil_listOther = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_KEY_NUM);

            List<Integer> mKeyList = new ArrayList<Integer>();
            while (c.moveToNext()) {
                Coil_info info=new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));
                if (info.getCoil_id() < 100) {
                    if (mKeyList.contains(info.getKeyNum()) && (info.getKeyNum() > 0)) {
                        //do nothing
                    } else {
                        mKeyList.add(info.getKeyNum());
                        coil_list.add(info);
                    }
                } else if (info.getCoil_id() > 100) {
                    coil_listOther.add(info);
                } else {

                }

            }
            if (coil_listOther.size() > 0) {
                for (Coil_info tmpInfo:coil_listOther) {
                    coil_list.add(tmpInfo);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coil_list;
    }

    public List<Coil_info> queryKeyMapCoilList(int keynum) {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+ UtilsDB.COIL_INFO_KEY_NUM+"=? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),String.valueOf(keynum),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                Coil_info info=new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));
                coil_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coil_list;
    }

    public List<Coil_info> queryKeyListFromCoil(int slotNo) {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+ UtilsDB.COIL_INFO_COIL_ID+"=? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),String.valueOf(slotNo),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_KEY_NUM);

            while (c.moveToNext()) {
                Coil_info info=new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));
                coil_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coil_list;
    }

    public Coil_info queryCoilFromKeyMap(int keynum) {
        Coil_info info=new Coil_info();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+ UtilsDB.COIL_INFO_KEY_NUM+"=? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),String.valueOf(keynum),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));
                if (info.getExtant_quantity() > 0) {
                    break;
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }


    public List<Coil_info> queryCoilByEachCode() {
        List<Coil_info> slotNo_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS + "<? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            List<String> mGoodsCodeList = new ArrayList<String>();
            while (c.moveToNext()) {
                Coil_info info = new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));

                if (mGoodsCodeList.contains(info.getGoodsCode())) {

                    for (int i = 0; i < slotNo_list.size(); i++) {
                        Coil_info tmpInfo = slotNo_list.get(i);
                        if (tmpInfo != null) {
                            if (tmpInfo.getGoodsCode().equals(info.getGoodsCode())) {
                                if ((tmpInfo.getExtant_quantity() < 1) && (info.getExtant_quantity() > 0)) {
                                    slotNo_list.set(i,info);
                                }
                                break;
                            }
                        }
                    }
                } else {
                    mGoodsCodeList.add(info.getGoodsCode());
                    slotNo_list.add(info);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return slotNo_list;
    }

    public List<Coil_info> queryCoilByCode() {
        List<Coil_info> slotNo_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS + "<? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            List<String> mGoodsCodeList = new ArrayList<String>();
            while (c.moveToNext()) {
                Coil_info info = new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));

                if ((info.getGoodsCode() != null) && (info.getGoodsCode().length() > 0)) {
                    if (mGoodsCodeList.contains(info.getGoodsCode())) {
                        //do nothing
                    } else {
                        mGoodsCodeList.add(info.getGoodsCode());
                        slotNo_list.add(info);
                    }
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return slotNo_list;
    }

    public List<Coil_info> queryCoilByCode(String goodCode) {
        List<Coil_info> slotNo_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+ UtilsDB.COIL_INFO_GOODS_CODE+"=? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),goodCode,String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                Coil_info info = new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));

                slotNo_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return slotNo_list;
    }

    /**
     *@desc 根据货道商品编码找到最近的有货的货道
     *@author Jiancheng,Song
     *@time 2016/6/1 9:21
     */
    public Coil_info queryCoilInfoAvailable(String goodCode) {

        Coil_info mFirstInfo = null;

        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+ UtilsDB.COIL_INFO_GOODS_CODE+"=? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),goodCode,String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);
            Coil_info info = new Coil_info();
            while (c.moveToNext()) {
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                if (info.getExtant_quantity() > 0) {
                    info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                    info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                    info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                    info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                    info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                    info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                    info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                    info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                    info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                    info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                    info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                    info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                    info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                    info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                    info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                    info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                    info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                    info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                    info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                    info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                    info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                    info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                    info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                    info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                    info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                    info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                    info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                    info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                    info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));

                    if (null == mFirstInfo) {
                        mFirstInfo = info;
                    }
                    if (info.getSlotOrder() < mFirstInfo.getSlotOrder()) {
                        mFirstInfo = info;
                    }
                    break;
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mFirstInfo;
    }

    public Coil_info queryCoilInfoAvailableNotFault(String goodCode) {

        Coil_info mFirstInfo = null;

        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+ UtilsDB.COIL_INFO_GOODS_CODE+"=? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),goodCode,String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);
            Coil_info info = new Coil_info();
            while (c.moveToNext()) {
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));

                mFirstInfo = info;
                if (info.getExtant_quantity() > 0) {
                    if(info.getWork_status() == 0) {
                        if (null == mFirstInfo) {
                            mFirstInfo = info;
                        }
                        if (info.getSlotOrder() < mFirstInfo.getSlotOrder()) {
                            mFirstInfo = info;
                        }
                        break;
                    }
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mFirstInfo;
    }

    /**
     *@desc 根据货道商品类型找到最近的有货的货道
     *@author Jiancheng,Song
     *@time 2017/02/09 13:26
     */
    public Coil_info queryCoilInfoAvailableByType(String type) {

        Coil_info mFirstInfo = null;

        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+ UtilsDB.COIL_INFO_TYPE+"=? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),type,String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);
            Coil_info info = new Coil_info();
            while (c.moveToNext()) {
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                if (info.getExtant_quantity() > 0) {
                    info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                    info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                    info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                    info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                    info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                    info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                    info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                    info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                    info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                    info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                    info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                    info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                    info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                    info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                    info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                    info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                    info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                    info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                    info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                    info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                    info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                    info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                    info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                    info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                    info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                    info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                    info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                    info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                    info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));

                    if (null == mFirstInfo) {
                        mFirstInfo = info;
                    }
                    if (info.getSlotOrder() < mFirstInfo.getSlotOrder()) {
                        mFirstInfo = info;
                    }
                    break;
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mFirstInfo;
    }

    /**
     *@desc 根据货道商品类型找到最近的有用的货道
     *@author Jiancheng,Song
     *@time 2017/02/09 13:26
     */
    public Coil_info queryCoilInfoAvailableByType(String type,String selectDbInfo,int startRangeValue) {

        if (selectDbInfo == null) {
            return null;
        }
        boolean bHaveAvailableGoods = false;
        Coil_info info = new Coil_info();
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+ UtilsDB.COIL_INFO_TYPE+"=? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),type,String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));

                if (selectDbInfo.equals(UtilsDB.COIL_INFO_EXTANT_QUANTITY)) {
                    if (info.getExtant_quantity() >= startRangeValue) {
                        bHaveAvailableGoods = true;
                        break;
                    }
                } else if (selectDbInfo.equals(UtilsDB.COIL_INFO_CAPACITY)) {
                    if (info.getCapacity() >= startRangeValue) {
                        bHaveAvailableGoods = true;
                        break;
                    }
                } else if (selectDbInfo.equals(UtilsDB.COIL_INFO_RAY)) {
                    if (info.getRay() >= startRangeValue) {
                        bHaveAvailableGoods = true;
                        break;
                    }
                } else if (selectDbInfo.equals(UtilsDB.COIL_INFO_WORK_STATUS)) {
                    if (info.getWork_status() >= startRangeValue) {
                        bHaveAvailableGoods = true;
                        break;
                    }
                } else if (selectDbInfo.equals(UtilsDB.COIL_INFO_SALE_NUM)) {
                    if (info.getSaleNum() >= startRangeValue) {
                        bHaveAvailableGoods = true;
                        break;
                    }
                } else if (selectDbInfo.equals(UtilsDB.COIL_INFO_KEY_NUM)) {
                    if (info.getKeyNum() >= startRangeValue) {
                        bHaveAvailableGoods = true;
                        break;
                    }
                } else if (selectDbInfo.equals(UtilsDB.COIL_INFO_SLOT_STATUS)) {
                    if (info.getSlotStatus() >= startRangeValue) {
                        bHaveAvailableGoods = true;
                        break;
                    }
                } else if (selectDbInfo.equals(UtilsDB.COIL_INFO_SHIP_ORDER)) {
                    if (info.getSlotOrder() >= startRangeValue) {
                        bHaveAvailableGoods = true;
                        break;
                    }
                } else {

                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!bHaveAvailableGoods) {
            info = new Coil_info();
        }

        return info;
    }
    
    public List<Coil_info> queryAliveType(String type) {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+ UtilsDB.COIL_INFO_TYPE + "=? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),type,String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                Coil_info info = new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));

                coil_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coil_list;
    }

    public List<Coil_info> queryAliveExceptType(List<String> listType) {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS + "<? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            boolean bEffective = false;
            while (c.moveToNext()) {
                bEffective = true;
                Coil_info info=new Coil_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ID)));
                info.setCoil_id(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_CONTENT)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_EXTANT_QUANTITY)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_NAME)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CAPACITY)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_IMG_URL)));
                info.setRay(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_RAY)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_WORK_STATUS)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_TYPE)));
                info.setSaleNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_NUM)));
                info.setSaleAmount(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_AMOUNT)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
                info.setGoodsSpec(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_SPEC)));
                info.setGoodsCapacity(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CAPACITY)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_KEY_NUM)));
                info.setSlotStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SLOT_STATUS)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_DETAIL_URL)));
                info.setSlotOrder(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_SHIP_ORDER)));
                info.setOtherParam1(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM1)));
                info.setOtherParam2(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_OTHER_PARAM2)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_QRPAY_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_AD_URL)));
                info.setHeatTime(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_HEAT_TIME)));
                info.setCloseStatus(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_CLOSE_STATUS)));
                info.setRow(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_ROW)));
                info.setColumn(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COL)));
                info.setBack(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_BACK)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_SALE_PRICE)));

                for (String type: listType) {
                    if (type.equals(info.getType())) {
                        bEffective = false;
                    }
                }
                if (bEffective) {
                    coil_list.add(info);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coil_list;
    }

    public void deleteType(String type) {
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_WORK_STATUS+"<? and "+ UtilsDB.COIL_INFO_TYPE + "=? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(255),type,String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                ContentValues values = new ContentValues();
                values.put(UtilsDB.COIL_INFO_TYPE, "");
                updateData(UtilsDB.COIL_INFO_TABLE_NAME,values,c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modifyType(int startId, int endId, String type) {
        try {
            Coil_info infoStart = queryCoilInfo(startId);
            Coil_info infoEnd = queryCoilInfo(endId);
            if ((infoStart.getCoil_id() > 0) && (infoEnd.getCoil_id() > 0)) {
                ContentValues values = new ContentValues();
                for (int i = startId; i <= endId; i++) {
                    values.clear();
                    values.put(UtilsDB.COIL_INFO_TYPE, type);
                    updateData(UtilsDB.COIL_INFO_TABLE_NAME,values,i);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<String> getAllType(List<Coil_info> data){
        List<String> mTypeList = new ArrayList<String>();
        try {

            if (data != null) {
                for (Coil_info info : data) {
                    if ((info.getType() != null) && (!((info.getType()).equals("")))) {
                        if (mTypeList.contains(info.getType())) {
                            //do nothing
                        } else {
                            mTypeList.add(info.getType());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mTypeList;
    }


    public List<Integer> getCoilList() {
        List<Integer> slotNo_list=new ArrayList<Integer>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_COIL_ID + ">? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(0),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                slotNo_list.add(c.getInt(c.getColumnIndex(UtilsDB.COIL_INFO_COIL_ID)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return slotNo_list;
    }

    public List<Double> getCoilPriceList() {
        List<Double> slotNo_list=new ArrayList<Double>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_COIL_ID + ">? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(0),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                slotNo_list.add(c.getDouble(c.getColumnIndex(UtilsDB.COIL_INFO_PAR_PRICE)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return slotNo_list;
    }

    public List<String> getCoilGoodsCodeList(){
        List<String> slotNo_list=new ArrayList<String>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.COIL_INFO_TABLE_NAME,
                    null, UtilsDB.COIL_INFO_COIL_ID + ">? and "+ UtilsDB.COIL_INFO_SLOT_STATUS + "<?",
                    new String[]{String.valueOf(0),String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.COIL_INFO_COIL_ID);

            while (c.moveToNext()) {
                slotNo_list.add(c.getString(c.getColumnIndex(UtilsDB.COIL_INFO_GOODS_CODE)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return slotNo_list;
    }

    public List<Goods_info> queryGoodsInfoAll() {
        List<Goods_info> goods_list = new ArrayList<Goods_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.GOODS_INFO_TABLE_NAME,
                    null, null,
                    null, null, null, UtilsDB.GOODS_INFO_ID);

            while (c.moveToNext()) {
                Goods_info info=new Goods_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_ID)));
                info.setGoods_id(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_PRODUCT_ID)));
                info.setGoods_name(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_NAME)));
                info.setGoods_price(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_PRICE)));
                info.setGoods_type(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_TYPE)));
                info.setGoods_stock(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_STOCK)));
                info.setGoods_spec(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_SPEC)));
                info.setGoods_capacity(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_CAPACITY)));
                info.setGoods_introduce(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_INTRODUCE)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_DETAILS_URL)));
                info.setGoods_status(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_WORK_STATUS)));
                info.setGoods_url(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_URL)));
                info.setGoodsOtherParam1(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_OTHER_PARAM1)));
                info.setGoodsOtherParam2(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_OTHER_PARAM2)));
                info.setSlotShipLast(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_LAST_SHIP_SLOT)));
                info.setGoodsSlotMap(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_SLOT_MAP)));
                info.setGoodsAdUrl(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_AD_URL)));
                goods_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods_list;
    }

    public List<Goods_info> queryGoodsInfoAlive() {
        List<Goods_info> goods_list = new ArrayList<Goods_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.GOODS_INFO_TABLE_NAME,
                    null, UtilsDB.GOODS_INFO_WORK_STATUS + "<?",
                    new String[]{String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.GOODS_INFO_ID);

            while (c.moveToNext()) {
                Goods_info info=new Goods_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_ID)));
                info.setGoods_id(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_PRODUCT_ID)));
                info.setGoodsSlotMap(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_SLOT_MAP)));
                if ((info.getGoods_id() != null) && (info.getGoods_id()).length() > 0 && (info.getGoodsSlotMap() != null)
                        && (info.getGoodsSlotMap().length() > 0)) {
                    info.setGoods_name(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_NAME)));
                    info.setGoods_price(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_PRICE)));
                    info.setGoods_type(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_TYPE)));
                    info.setGoods_stock(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_STOCK)));
                    info.setGoods_spec(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_SPEC)));
                    info.setGoods_capacity(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_CAPACITY)));
                    info.setGoods_introduce(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_INTRODUCE)));
                    info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_DETAILS_URL)));
                    info.setGoods_status(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_WORK_STATUS)));
                    info.setGoods_url(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_URL)));
                    info.setGoodsOtherParam1(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_OTHER_PARAM1)));
                    info.setGoodsOtherParam2(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_OTHER_PARAM2)));
                    info.setSlotShipLast(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_LAST_SHIP_SLOT)));
                    info.setGoodsAdUrl(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_AD_URL)));
                    goods_list.add(info);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods_list;
    }

    public List<Goods_info> queryGoodsInfo() {
        List<Goods_info> goods_list = new ArrayList<Goods_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.GOODS_INFO_TABLE_NAME,
                    null, UtilsDB.GOODS_INFO_WORK_STATUS + "<?",
                    new String[]{String.valueOf(UtilsDB.SLOT_STATE_DISABLE)}, null, null, UtilsDB.GOODS_INFO_ID);

            while (c.moveToNext()) {
                Goods_info info=new Goods_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_ID)));
                info.setGoods_id(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_PRODUCT_ID)));
                info.setGoodsSlotMap(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_SLOT_MAP)));
                info.setGoods_name(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_NAME)));
                info.setGoods_price(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_PRICE)));
                info.setGoods_type(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_TYPE)));
                info.setGoods_stock(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_STOCK)));
                info.setGoods_spec(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_SPEC)));
                info.setGoods_capacity(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_CAPACITY)));
                info.setGoods_introduce(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_INTRODUCE)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_DETAILS_URL)));
                info.setGoods_status(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_WORK_STATUS)));
                info.setGoods_url(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_URL)));
                info.setGoodsOtherParam1(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_OTHER_PARAM1)));
                info.setGoodsOtherParam2(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_OTHER_PARAM2)));
                info.setSlotShipLast(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_LAST_SHIP_SLOT)));
                info.setGoodsAdUrl(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_AD_URL)));
                goods_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods_list;
    }

    public Goods_info queryGoodsInfo(int id) {
        Goods_info info = new Goods_info();
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.GOODS_INFO_TABLE_NAME,
                    null,
                    UtilsDB.GOODS_INFO_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, UtilsDB.GOODS_INFO_ID);
            if(c.moveToNext()) {
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_ID)));
                info.setGoods_id(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_PRODUCT_ID)));
                info.setGoods_name(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_NAME)));
                info.setGoods_price(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_PRICE)));
                info.setGoods_type(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_TYPE)));
                info.setGoods_stock(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_STOCK)));
                info.setGoods_spec(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_SPEC)));
                info.setGoods_capacity(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_CAPACITY)));
                info.setGoods_introduce(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_INTRODUCE)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_DETAILS_URL)));
                info.setGoods_status(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_WORK_STATUS)));
                info.setGoods_url(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_URL)));
                info.setGoodsOtherParam1(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_OTHER_PARAM1)));
                info.setGoodsOtherParam2(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_OTHER_PARAM2)));
                info.setSlotShipLast(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_LAST_SHIP_SLOT)));
                info.setGoodsSlotMap(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_SLOT_MAP)));
                info.setGoodsAdUrl(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_AD_URL)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public Goods_info queryGoodsInfo(String goodsId) {
        Goods_info info = new Goods_info();
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.GOODS_INFO_TABLE_NAME,
                    null,
                    UtilsDB.GOODS_INFO_PRODUCT_ID + "=?",
                    new String[]{goodsId}, null, null, UtilsDB.GOODS_INFO_ID);
            if(c.moveToNext()) {
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_ID)));
                info.setGoods_id(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_PRODUCT_ID)));
                info.setGoods_name(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_NAME)));
                info.setGoods_price(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_PRICE)));
                info.setGoods_type(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_TYPE)));
                info.setGoods_stock(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_STOCK)));
                info.setGoods_spec(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_SPEC)));
                info.setGoods_capacity(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_CAPACITY)));
                info.setGoods_introduce(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_INTRODUCE)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_DETAILS_URL)));
                info.setGoods_status(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_WORK_STATUS)));
                info.setGoods_url(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_URL)));
                info.setGoodsOtherParam1(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_OTHER_PARAM1)));
                info.setGoodsOtherParam2(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_OTHER_PARAM2)));
                info.setSlotShipLast(c.getInt(c.getColumnIndex(UtilsDB.GOODS_INFO_LAST_SHIP_SLOT)));
                info.setGoodsSlotMap(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_SLOT_MAP)));
                info.setGoodsAdUrl(c.getString(c.getColumnIndex(UtilsDB.GOODS_INFO_AD_URL)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public void querySellInfo() {
        List<Sell> sell_list = new ArrayList<Sell>();
        try {
            Cursor c=queryData(UtilsDB.SELL_TABLE_NAME);

            while (c.moveToNext()) {
                Sell sell=new Sell();
                sell.setId(c.getInt(c.getColumnIndex(UtilsDB.SELL_ID)));
                sell.setPaytype(c.getInt(c.getColumnIndex(UtilsDB.SELL_PAYTYPE)));
                sell.setResult(c.getInt(c.getColumnIndex(UtilsDB.SELL_RESULT)));
                sell.setUp_flag(c.getInt(c.getColumnIndex(UtilsDB.SELL_UP_FLAG)));
                sell.setUp_flag(c.getInt(c.getColumnIndex(UtilsDB.SELL_Partner)));
                sell.setUp_flag(c.getInt(c.getColumnIndex(UtilsDB.SELL_Termina)));
                sell.setUp_flag(c.getInt(c.getColumnIndex(UtilsDB.SELL_Time)));
                sell.setUp_flag(c.getInt(c.getColumnIndex(UtilsDB.SELL_DATA)));
                sell.setUp_flag(c.getInt(c.getColumnIndex(UtilsDB.SELL_CardNumber)));
                sell.setUp_flag(c.getInt(c.getColumnIndex(UtilsDB.SELL_SerialNumber)));
                sell.setUp_flag(c.getInt(c.getColumnIndex(UtilsDB.SELL_ReferNo)));
            }
            c.close();
            for (Sell sell : sell_list) {
                System.out.println(sell.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ServerData queryServerData(String str,String date){
        ServerData data=new ServerData();
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.SERVERDATA_TABLE_NAME,
                    null,
                    UtilsDB.SERVERDATA_DATA+"=? and "+UtilsDB.SERVERDATA_Time+"=?",
                    new String[]{str,date}, null, null, null);

            if(c.moveToNext()){
                data.setId(c.getInt(c.getColumnIndex(UtilsDB.SERVERDATA_ID)));
                data.setDATA(c.getString(c.getColumnIndex(UtilsDB.SERVERDATA_DATA)));
                data.setTime(c.getString(c.getColumnIndex(UtilsDB.SERVERDATA_Time)));
                data.setUpflag(c.getInt(c.getColumnIndex(UtilsDB.SERVERDATA_UPFLAG)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public List<AdvertInfo> queryAdvert() {
        List<AdvertInfo> advert_list = new ArrayList<AdvertInfo>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.ADVERT_TABLE_NAME,
                    null, null,
                    null, null, null, UtilsDB.ADVERT_ADID);

            while (c.moveToNext()) {
                AdvertInfo info=new AdvertInfo();
                info.setId(c.getInt(c.getColumnIndex(UtilsDB.ADVERT_ID)));
                info.setAdId(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ADID)));
                info.setAdMachineID(c.getString(c.getColumnIndex(UtilsDB.ADVERT_MACHINEID)));
                info.setAdImg(c.getString(c.getColumnIndex(UtilsDB.ADVERT_NAME)));
                info.setAdDownload(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ADRDOWNLOAD)));
                info.setAdRsTime(c.getString(c.getColumnIndex(UtilsDB.ADVERT_RSTIME)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_URL)));
                info.setAdPlayType(c.getString(c.getColumnIndex(UtilsDB.ADVERT_PLAYTYPE)));
                info.setAdLocalUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_URL_LOCAL)));
                info.setAdActId(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ACTIVITY_ID)));
                info.setAdContent(c.getString(c.getColumnIndex(UtilsDB.ADVERT_CONTENT)));
                info.setAdDetailsUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_DETAIL_URL)));
                advert_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return advert_list;
    }

    public List<AdvertInfo> queryAdvertAll() {
        List<AdvertInfo> advert_list = new ArrayList<AdvertInfo>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.ADVERT_TABLE_NAME,
                    null, null,
                    null, null, null, UtilsDB.ADVERT_ID);

            while (c.moveToNext()) {
                AdvertInfo info=new AdvertInfo();
                info.setId(c.getInt(c.getColumnIndex(UtilsDB.ADVERT_ID)));
                info.setAdId(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ADID)));
                info.setAdMachineID(c.getString(c.getColumnIndex(UtilsDB.ADVERT_MACHINEID)));
                info.setAdImg(c.getString(c.getColumnIndex(UtilsDB.ADVERT_NAME)));
                info.setAdDownload(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ADRDOWNLOAD)));
                info.setAdRsTime(c.getString(c.getColumnIndex(UtilsDB.ADVERT_RSTIME)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_URL)));
                info.setAdPlayType(c.getString(c.getColumnIndex(UtilsDB.ADVERT_PLAYTYPE)));
                info.setAdLocalUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_URL_LOCAL)));
                info.setAdActId(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ACTIVITY_ID)));
                info.setAdContent(c.getString(c.getColumnIndex(UtilsDB.ADVERT_CONTENT)));
                info.setAdDetailsUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_DETAIL_URL)));
                advert_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return advert_list;
    }

    public AdvertInfo queryAdvert(String adId) {
        AdvertInfo info = new AdvertInfo();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.ADVERT_TABLE_NAME,
                    null, UtilsDB.ADVERT_ADID + "=?",
                    new String[]{adId}, null, null, UtilsDB.ADVERT_ADID);
            if(c.moveToNext()) {
                info.setId(c.getInt(c.getColumnIndex(UtilsDB.ADVERT_ID)));
                info.setAdId(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ADID)));
                info.setAdMachineID(c.getString(c.getColumnIndex(UtilsDB.ADVERT_MACHINEID)));
                info.setAdImg(c.getString(c.getColumnIndex(UtilsDB.ADVERT_NAME)));
                info.setAdDownload(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ADRDOWNLOAD)));
                info.setAdRsTime(c.getString(c.getColumnIndex(UtilsDB.ADVERT_RSTIME)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_URL)));
                info.setAdPlayType(c.getString(c.getColumnIndex(UtilsDB.ADVERT_PLAYTYPE)));
                info.setAdLocalUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_URL_LOCAL)));
                info.setAdActId(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ACTIVITY_ID)));
                info.setAdContent(c.getString(c.getColumnIndex(UtilsDB.ADVERT_CONTENT)));
                info.setAdDetailsUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_DETAIL_URL)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    public AdvertInfo queryAdvertByFileName(String fileName) {
        AdvertInfo info = new AdvertInfo();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.ADVERT_TABLE_NAME,
                    null, UtilsDB.ADVERT_NAME + "=?",
                    new String[]{fileName}, null, null, UtilsDB.ADVERT_ID);
            if(c.moveToNext()) {
                info.setId(c.getInt(c.getColumnIndex(UtilsDB.ADVERT_ID)));
                info.setAdId(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ADID)));
                info.setAdMachineID(c.getString(c.getColumnIndex(UtilsDB.ADVERT_MACHINEID)));
                info.setAdImg(c.getString(c.getColumnIndex(UtilsDB.ADVERT_NAME)));
                info.setAdDownload(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ADRDOWNLOAD)));
                info.setAdRsTime(c.getString(c.getColumnIndex(UtilsDB.ADVERT_RSTIME)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_URL)));
                info.setAdPlayType(c.getString(c.getColumnIndex(UtilsDB.ADVERT_PLAYTYPE)));
                info.setAdLocalUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_URL_LOCAL)));
                info.setAdActId(c.getString(c.getColumnIndex(UtilsDB.ADVERT_ACTIVITY_ID)));
                info.setAdContent(c.getString(c.getColumnIndex(UtilsDB.ADVERT_CONTENT)));
                info.setAdDetailsUrl(c.getString(c.getColumnIndex(UtilsDB.ADVERT_DETAIL_URL)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    public Key_info queryKeyInfo(int key) {
        Key_info info = new Key_info();
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.KEY_INFO_TABLE_NAME,
                    null,
                    UtilsDB.KEY_INFO_KEY_ID + "=?",
                    new String[]{String.valueOf(key)}, null, null, UtilsDB.KEY_INFO_KEY_ID);
            if(c.moveToNext()) {
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_ID)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_KEY_ID)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_EXTANT_QUANTITY)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_CAPACITY)));
                info.setCoils(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_COILS)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_PAR_NAME)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_CONTENT)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_PAR_PRICE)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_SALE_PRICE)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_GOODS_CODE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_TYPE)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_IMG_URL)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_QRPAY_URL)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_DETAIL_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_AD_URL)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_WORK_STATUS)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public List<Coil_info> queryCoilsInfo(int key) {
        Key_info info = new Key_info();
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.KEY_INFO_TABLE_NAME,
                    null,
                    UtilsDB.KEY_INFO_KEY_ID + "=?",
                    new String[]{String.valueOf(key)}, null, null, UtilsDB.KEY_INFO_KEY_ID);
            if(c.moveToNext()) {
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_ID)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_KEY_ID)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_EXTANT_QUANTITY)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_CAPACITY)));
                info.setCoils(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_COILS)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_PAR_NAME)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_CONTENT)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_PAR_PRICE)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_SALE_PRICE)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_GOODS_CODE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_TYPE)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_IMG_URL)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_QRPAY_URL)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_DETAIL_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_AD_URL)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_WORK_STATUS)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Coil_info> mCoilList = new ArrayList<Coil_info>();
        if ((info != null) && (info.getKeyNum() > 0)) {
            String mcL = info.getCoils();
            if ((mcL != null) && (mcL.length() > 0)) {
                if (mcL.contains("~")) {
                    String[] mSlotList = mcL.split("\\~");
                    if ((mSlotList != null) && (mSlotList.length > 0)) {
                        for (String data:mSlotList) {
                            if (isDigital(data)) {
                                Coil_info sInfo = queryCoilInfo(Integer.valueOf(data).intValue());
                                if ((sInfo != null) && (sInfo.getCoil_id() > 0)) {
                                    mCoilList.add(sInfo);
                                }
                            }
                        }
                    }
                } else {
                    if (isDigital(mcL)) {
                        Coil_info sInfo = queryCoilInfo(Integer.valueOf(mcL).intValue());
                        if ((sInfo != null) && (sInfo.getCoil_id() > 0)) {
                            mCoilList.add(sInfo);
                        }
                    }
                }
            }
        }

        return mCoilList;
    }

    public boolean isDigital(String data) {
        if ((null == data) || (data.length() < 1)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]*$");
        return pattern.matcher(data).matches();
    }

    public List<Key_info> queryAliveKey() {
        List<Key_info> coil_list = new ArrayList<Key_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.KEY_INFO_TABLE_NAME,
                    null, UtilsDB.KEY_INFO_WORK_STATUS + "<?",
                    new String[]{String.valueOf(UtilsDB.KEY_STATE_NO_COIL)}, null, null, UtilsDB.KEY_INFO_KEY_ID);

            while (c.moveToNext()) {
                Key_info info=new Key_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_ID)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_KEY_ID)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_EXTANT_QUANTITY)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_CAPACITY)));
                info.setCoils(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_COILS)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_PAR_NAME)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_CONTENT)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_PAR_PRICE)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_SALE_PRICE)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_GOODS_CODE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_TYPE)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_IMG_URL)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_QRPAY_URL)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_DETAIL_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_AD_URL)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_WORK_STATUS)));
                coil_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coil_list;
    }

    public List<Key_info> queryAliveKeyAll() {
        List<Key_info> coil_list = new ArrayList<Key_info>();
        try {
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor c = database.query(UtilsDB.KEY_INFO_TABLE_NAME,
                    null, UtilsDB.KEY_INFO_WORK_STATUS + "<?",
                    new String[]{String.valueOf(UtilsDB.KEY_STATE_INVAILD)}, null, null, UtilsDB.KEY_INFO_KEY_ID);

            while (c.moveToNext()) {
                Key_info info=new Key_info();
                info.setID(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_ID)));
                info.setKeyNum(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_KEY_ID)));
                info.setExtant_quantity(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_EXTANT_QUANTITY)));
                info.setCapacity(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_CAPACITY)));
                info.setCoils(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_COILS)));
                info.setPar_name(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_PAR_NAME)));
                info.setContent(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_CONTENT)));
                info.setPar_price(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_PAR_PRICE)));
                info.setSalePrice(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_SALE_PRICE)));
                info.setGoodsCode(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_GOODS_CODE)));
                info.setType(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_TYPE)));
                info.setImg_url(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_IMG_URL)));
                info.setQrPayUrl(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_QRPAY_URL)));
                info.setGoods_details_url(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_DETAIL_URL)));
                info.setAdUrl(c.getString(c.getColumnIndex(UtilsDB.KEY_INFO_AD_URL)));
                info.setWork_status(c.getInt(c.getColumnIndex(UtilsDB.KEY_INFO_WORK_STATUS)));
                coil_list.add(info);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coil_list;
    }

    public void clean() {
        try {
            this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS Coil_info");
            this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS Sell");
            this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS ServerData");
            this.onCreate(this.getWritableDatabase());
            this.getWritableDatabase().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
