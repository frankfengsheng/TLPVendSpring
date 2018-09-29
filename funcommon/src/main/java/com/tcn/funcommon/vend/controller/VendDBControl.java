package com.tcn.funcommon.vend.controller;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.R;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.TcnUtility;
import com.tcn.funcommon.db.AdvertInfo;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.db.Key_info;
import com.tcn.funcommon.db.TcnSQLiteOpenHelper;
import com.tcn.funcommon.db.UtilsDB;
import com.tcn.funcommon.vend.def.TcnDBDef;
import com.tcn.funcommon.vend.def.TcnDBResultDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/2.
 */
public class VendDBControl {
    private static final String TAG = "VendDBControl";
    private static final float DEFAULT_COIL_PRICE              = 6553.5f;
    public static final int DEFAULT_COIL_CAPACITY           = 199;

    public static final int NEED_UP_TO_SERVER                = -1;
    public static final int NEED_NOT_UP_TO_SERVER           = 0;



    private static VendDBControl m_Instance = null;
    private volatile boolean m_bHaveKeyMap = false;
    private volatile boolean m_bOnlyKeyMap = false;
    private volatile boolean m_bIsLoadSuccess = false;
    private volatile boolean m_bShowType = false;
    private volatile boolean m_bShowByGoodsCode = false;
    private volatile boolean m_bIsSelecting = false;
    private volatile boolean m_bIsMainBoardControl = false;
    private volatile int m_listData_count = 0;
    private volatile int m_listData_countExcept = 0;
    private volatile int m_listData_countAll = 0;
    private volatile int m_listData_countGoods = 0;
    private volatile int m_listData_countGoodsAll = 0;
    private volatile int m_listData_countFaults = 0;
    private volatile int m_listData_countKey = 0;
    private volatile int m_listData_countKeyAll = 0;
    private volatile List<Integer> m_list_count_slot_key = null;
    private volatile List<Integer> m_list_count_slot_key_all = null;
    private volatile int m_iMaxSlotNo = 0;
    private volatile int m_iSelectedKey = 0;
    private volatile TcnSQLiteOpenHelper m_helper;
    private List<Coil_info> m_list_aliveCoilExcept;
    private List<String> m_list_strType = new ArrayList<String>();
    private List<List<Coil_info>> m_list_aliveCoilType;
    private List<Goods_info> m_list_aliveGoodsAll = new ArrayList<Goods_info>();
    private List<Goods_info> m_list_aliveGoods = new ArrayList<Goods_info>();
    private Integer m_aliveCoil_lock = new Integer(2);
    private List<Coil_info> m_list_aliveCoilAll;
    private List<Coil_info> m_list_aliveCoil;
    private List<Coil_info> m_list_origAiveCoil;
    private List<Key_info> m_list_aliveKeyAll;
    private List<Key_info> m_list_aliveKey;
    private Coil_info m_selectCoilInfo = new Coil_info();
    private volatile String m_strCurrentGoodsType = TcnVendIF.GOODS_TYPE_ALL;
    private Context m_context = null;
    private TreeMap<Integer, ArrayList<Integer>> m_mapKeySlotNo = null;


    private volatile Handler m_SendHandler = null;


    public static synchronized VendDBControl getInstance() {
        if (null == m_Instance) {
            m_Instance = new VendDBControl();
        }
        return m_Instance;
    }

    public void initialize(Context context, TcnSQLiteOpenHelper helper, Handler sendHandler, boolean isMainBoardControl) {
        m_context = context;
        m_helper = helper;
        m_SendHandler = sendHandler;
        m_bIsMainBoardControl = isMainBoardControl;
    }

    public void deInitialize() {
        if (m_list_aliveCoilType != null) {
            m_list_aliveCoilType.clear();
            m_list_aliveCoilType = null;
        }
    }

    public List<String> getAliveType() {
        return m_list_strType;
    }

    public void setHaveKeyMap(boolean needKeyMap) {
        m_bHaveKeyMap = needKeyMap;
    }

    public boolean isHaveKeyMap() {
        return m_bHaveKeyMap;
    }

    public void setOnlyKeyMap(boolean onlyKeyMap) {
        m_bOnlyKeyMap = onlyKeyMap;
    }

    public boolean isOnlyKeyMap() {
        return m_bOnlyKeyMap;
    }

    public void setKeyMapSlotNo(TreeMap<Integer, ArrayList<Integer>> keyMapSlotNo) {
        m_mapKeySlotNo = keyMapSlotNo;
    }

    public TreeMap<Integer, ArrayList<Integer>> getKeyMapSlotNo() {
        return m_mapKeySlotNo;
    }

    public int getMaxSlotNo() {
        return m_iMaxSlotNo;
    }

    public void reqGoodsSoldOut() {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.SLOTNO_SOLD_OUT);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.SLOTNO_SOLD_OUT, -1, -1, null);
    }

    public void queryAliveCoil() {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL, -1, -1, Boolean.valueOf(true));
    }

    public void queryAliveCoil(boolean needUpToServer) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL, -1, -1, Boolean.valueOf(needUpToServer));
    }

    public void queryAliveCoilDelay(boolean needUpToServer,long delaytime) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsgDelayed(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL, -1, -1,delaytime, Boolean.valueOf(needUpToServer));
    }

    public void queryAliveCoilExcept(List<String> listType) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL_EXCEPTIONE);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL_EXCEPTIONE, -1, -1, listType);
    }

    public void queryAliveGoods() {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_GOODS);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_GOODS, -1, -1, null);
    }

    public void reqUpdateSlotPrice(int startslotNo,int endSlotNo, String price) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_PRICE,startslotNo, endSlotNo, price);
    }

    public void reqUpdateSlotSalePrice(int startslotNo,int endSlotNo, String price) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_SALE_PRICE,startslotNo, endSlotNo, price);
    }

    public void reqUpdateHeatTime(int startSlotNo, int endtSlotNo, int heatTime) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_UPDATE_HEATTIME);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_HEATTIME,startSlotNo, endtSlotNo, heatTime);
    }

    public void reqUpdateColumn(int startSlotNo, int endtSlotNo, int column) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_COLUMN,startSlotNo, endtSlotNo, column);
    }

    public void reqUpdateRow(int startSlotNo, int endtSlotNo, int row) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_ROW,startSlotNo, endtSlotNo, row);
    }

    public void reqUpdatePullBack(int startSlotNo, int endtSlotNo, int back) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_PULL_BACK,startSlotNo, endtSlotNo, back);
    }

    public void reqUpdateSlotName(int startslotNo,int endSlotNo, String name) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_NAME,startslotNo, endSlotNo, name);
    }

    public void reqUpdateSlotType(int startslotNo,int endSlotNo, String type) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_TYPE,startslotNo, endSlotNo, type);
    }

    public void reqUpdateSlotIntroduce(int startslotNo,int endSlotNo, String content) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_INTROdDUCE,startslotNo, endSlotNo, content);
    }

    public void reqUpdateSlotImageUrl(int startslotNo,int endSlotNo, String imgUrl) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_IMGURL,startslotNo, endSlotNo, imgUrl);
    }

    public void reqUpdateSlotAdvertUrl(int startslotNo,int endSlotNo, String adUrl) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_ADURL,startslotNo, endSlotNo, adUrl);
    }

    public void reqUpdateSlotSpec(int startslotNo,int endSlotNo, String spec) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_SPEC,startslotNo, endSlotNo, spec);
    }

    public void reqUpdateGoodsCapacity(int startslotNo,int endSlotNo, String capacity) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_GOODS_CAPACITY,startslotNo, endSlotNo, capacity);
    }

    public void reqUpdateSlotGoodsCode(int startslotNo,int endSlotNo, String goodsCode) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_GOODSCODE,startslotNo, endSlotNo, goodsCode);
    }

    public void reqUpdateSlotExtantQuantity(int startslotNo,int endSlotNo, int extantQuantity) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_EXTQUANTITY,startslotNo, endSlotNo, extantQuantity);
    }

    public void reqUpdateSlotWorkStatus(int startslotNo,int endSlotNo, int status) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOT_WORK_STATUS,startslotNo, endSlotNo, status);
    }

    public void reqUpdateSlotStatus(int startslotNo,int endSlotNo, int status) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOT_STATUS,startslotNo, endSlotNo, status);
    }

    public void reqFillStockToCapacityAll() {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_FILL_STOCK_TO_CAPACITY_ALL,-1, -1, null);
    }

    public void reqFillStockToCapacity(int start, int end) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_FILL_STOCK_TO_CAPACITY,start, end, null);
    }

    public void reqUpdateSlotCapacity(int startslotNo,int endSlotNo, int capacity) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPDATE_SLOTNO_CAPACITY,startslotNo, endSlotNo, capacity);
    }

    public void reqDeleteType(String type) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_DELETE_TYPE, -1, -1, type);
    }

    public void reqInsertData(int tableType, ContentValues values) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_INSERT_DATA,tableType, -1, values);
    }

    public void reqUpdateData(int tableType,int id,ContentValues values) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_UPTE_DATA,tableType, id, values);
    }

    public void reqDeleteData(int tableType,int id) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_DELETE_DATA,tableType, id, null);
    }

    public void reqModifyType(int startId, int endId, String type) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_MODIFY_TYPE, startId, endId, type);
    }

    public void reqAddShowSlotNo(int startcoilId,int endcoilId) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_ADD_SHOW_COIL_ID,startcoilId, endcoilId, null);
    }

    public void reqDeleteSlotNo(int coil_id) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_DELETE_COIL_ID,coil_id, -1, null);
    }

    public void reqDeleteKeyMap(int keyNum) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_DELETE_KEY_MAP,keyNum, -1, null);
    }

    public void reqAddStock(int coil_id) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_ADD_STOCK,coil_id, -1, null);
    }

    public void reqSubStock(int coil_id) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_SUB_STOCK,coil_id, -1, null);
    }

    public void reqDeleteGoodsId(String goodsId) {
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_DELETE_GOODS_ID,-1, -1, goodsId);
    }

    public void reqAddSlotGoods(int startSlotNo, int endSlotNo, Goods_info info) {
        TcnUtility.removeMessages(m_SendHandler, TcnDBDef.REQ_QUERY_ALIVE_COIL);
        TcnUtility.sendMsg(m_SendHandler, TcnDBDef.REQ_ADD_SLOT_GOODS,startSlotNo, endSlotNo, info);
    }

    private void sendReceiveData(boolean notRemoveOld,int what, int arg1, int arg2, Object data) {
        if (null == m_SendHandler) {
            return;
        }
        if (!notRemoveOld) {
            m_SendHandler.removeMessages(what);
        }
        Message message = m_SendHandler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = data;
        m_SendHandler.sendMessage(message);
    }

    /**
     * 得到操作数据库的操作对象
     * @return
     */
    public TcnSQLiteOpenHelper getHelper() {
        return m_helper;
    }

    /**
     * 设置操作数据库的操作对象
     * @return
     */
    public void setHelper(TcnSQLiteOpenHelper helper) {
        this.m_helper = helper;
    }

    public void setShowType(boolean showType) {
        m_bShowType = showType;
    }

    public void setShowByGoodsCode(boolean byCode) {
        m_bShowByGoodsCode = byCode;
    }

    public boolean isLoadSlotNoSuccess() {
        return m_bIsLoadSuccess;
    }

    private String getAdjustPrice(String price) {
        if (m_bIsMainBoardControl) {
            return price;
        } else {
            StringBuffer strPrice = new StringBuffer();
            int count = TcnShareUseData.getInstance().getPricePointCount();
            int iIndexPoint = price.indexOf(".");
            if (iIndexPoint >= 0) {
                int iPriceLength = price.length();
                if (0 == count) {
                    strPrice.append(price.substring(0,iIndexPoint));
                } else if (1 == count) {
                    if (iPriceLength > (iIndexPoint + 1)) {
                        strPrice.append(price.substring(0,iIndexPoint+2));
                    } else {
                        strPrice.append(price.substring(0,iIndexPoint + 1));
                        strPrice.append(0);
                    }

                } else if (2 == count) {
                    if (iPriceLength > (iIndexPoint + 2)) {
                        strPrice.append(price.substring(0,iIndexPoint+3));
                    } else if (iPriceLength > (iIndexPoint + 1)) {
                        strPrice.append(price.substring(0,iIndexPoint + 2));
                        strPrice.append(0);
                    } else {
                        strPrice.append(price.substring(0,iIndexPoint + 1));
                        strPrice.append(00);
                    }
                } else {

                }
            } else {
                strPrice.append(price);
                if (1 == count) {
                    strPrice.append(".0");
                } else if (2 == count) {
                    strPrice.append(".00");
                } else {

                }
            }
            return strPrice.toString();
        }
    }

    public void OnUpdateSlotPrice(boolean notify,int startslotNo,int endSlotNo, String price) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((price != null) && (price.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_PAR_PRICE, getAdjustPrice(price));//价格
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdateSlotSalePrice(boolean notify,int startslotNo,int endSlotNo, String price) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((price != null) && (price.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_SALE_PRICE, getAdjustPrice(price));//价格
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnFillStockToCapacityAll() {
        if ((null == m_list_aliveCoil) || (m_list_aliveCoil.size() < 1)) {
            return;
        }
        ContentValues values = new ContentValues();
        for (Coil_info info:m_list_aliveCoil) {
            values.clear();
            values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, info.getCapacity());//商品库存
            if(info.getCoil_id() > 0){
                m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,info.getCoil_id());
            }
        }

        queryAliveCoil(true);

        sendReceiveData(false,TcnDBDef.FILL_STOCK_TO_CAPACITY_ALL,m_listData_count,-1,null);
    }

    public void OnFillStockToCapacity(int start, int end) {
        Coil_info info = null;
        ContentValues values = new ContentValues();
        for (int i = start; i <= end; i++) {
            if (isSlotNoExist(i)) {
                info = m_helper.queryCoilInfo(i);
                if(info.getCoil_id() > 0) {
                    values.clear();
                    values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, info.getCapacity());//商品库存
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,info.getCoil_id());
                }
            }
        }

        queryAliveCoil(true);

        sendReceiveData(false,TcnDBDef.FILL_STOCK_TO_CAPACITY,start,end,null);
    }

    public void OnUpdateSlotName(boolean notify,int startslotNo,int endSlotNo, String name) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((name != null) && (name.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_PAR_NAME, name);//商品名称
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdateSlotType(boolean notify,int startslotNo,int endSlotNo, String type) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((type != null) && (type.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_TYPE, type);//商品类型
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdateSlotIntroduce(boolean notify,int startslotNo,int endSlotNo, String content) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((content != null) && (content.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_CONTENT, content);//商品介绍
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdateSlotImageUrl(boolean notify,int startslotNo,int endSlotNo, String imgUrl) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((imgUrl != null) && (imgUrl.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_IMG_URL, imgUrl);//商品图片
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdateSlotAdUrl(boolean notify,int startslotNo,int endSlotNo, String adUrl) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((adUrl != null) && (adUrl.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_AD_URL, adUrl);//商品图片
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdateHeatTime(boolean notify,int startslotNo,int endSlotNo, int heatTime) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            values.put(UtilsDB.COIL_INFO_HEAT_TIME, heatTime);//加热时间
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdateColumn(boolean notify,int startslotNo,int endSlotNo, int col) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            values.put(UtilsDB.COIL_INFO_COL, col);//列
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdateRow(boolean notify,int startslotNo,int endSlotNo, int row) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            values.put(UtilsDB.COIL_INFO_ROW, row);//列
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdatePullBack(boolean notify,int startslotNo,int endSlotNo, int back) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            values.put(UtilsDB.COIL_INFO_BACK, back);//拉回
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
        sendReceiveData(false,TcnDBDef.UPDATE_PULL_BACK,startslotNo,endSlotNo,null);
    }

    public void OnUpdateSlotSpec(boolean notify,int startslotNo,int endSlotNo, String spec) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((spec != null) && (spec.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_GOODS_SPEC, spec);//商品规格
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdateGoodsCapacity(boolean notify,int startslotNo,int endSlotNo, String capacity) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((capacity != null) && (capacity.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_GOODS_CAPACITY, capacity);//商品规格
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnUpdateSlotGoodsCode(boolean notify,int startslotNo,int endSlotNo, String goodsCode) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((goodsCode != null) && (goodsCode.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_GOODS_CODE, goodsCode);//商品编码
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public boolean OnUpdateSlotExtantQuantity(boolean notify,int slotNo,int extantQuantity) {
        boolean bRet = false;
        if ((slotNo < 1) || ((extantQuantity < 0)) || (null == m_helper) || (extantQuantity < 0)) {
            return bRet;
        }

        ContentValues values = new ContentValues();
        values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, extantQuantity);//商品库存

        Coil_info coil = m_helper.queryCoilInfo(slotNo);
        if(coil.getCoil_id() > 0){
            bRet = m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNo);
            if (notify) {
                queryAliveCoil(true);
            }
        }
        return bRet;
    }

    public void OnUpdateSlotExtantQuantity(boolean notify,int startslotNo,int endSlotNo, int extantQuantity) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo)) || (null == m_helper) || (extantQuantity < 0)) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if (extantQuantity >= 0) {
                values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, extantQuantity);//商品库存
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
        sendReceiveData(false,TcnDBDef.UPDATE_SLOTNO_EXTQUANTITY,startslotNo,endSlotNo,extantQuantity);
    }

    public void OnUpdateSlotCapacity(boolean notify,int startslotNo,int endSlotNo, int capacity) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo)) || (capacity < 0)) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if (capacity >= 0) {
                values.put(UtilsDB.COIL_INFO_CAPACITY, capacity);//货道容量
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
        sendReceiveData(false,TcnDBDef.UPDATE_SLOTNO_CAPACITY,startslotNo,endSlotNo,capacity);
    }

    public void OnUpdateSlotStatus(boolean notify,int startslotNo,int endSlotNo, int status) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            values.put(UtilsDB.COIL_INFO_SLOT_STATUS, status);//货道状态
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (notify) {
            queryAliveCoil(true);
        }
    }

    public void OnAddSlotGoods(int startSlotNo, int endSlotNo,Goods_info gInfo) {
        if ((startSlotNo < 1) || ((endSlotNo < startSlotNo)) || (null == gInfo)) {
            return;
        }

        if ((gInfo.getID() < 0) || (null == gInfo.getGoods_id()) || (gInfo.getGoods_id().length() < 1)) {
            if (gInfo.getGoods_stock() < 1) {       //此处为上货的个数
                return;
            }
        }

        ContentValues values = new ContentValues();
        for (int i = startSlotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            if ((gInfo.getID() < 0) || (null == gInfo.getGoods_id()) || (gInfo.getGoods_id().length() < 1)) {
                //do nothing
            } else {
                values.put(UtilsDB.COIL_INFO_PAR_NAME, gInfo.getGoods_name());
                values.put(UtilsDB.COIL_INFO_GOODS_CODE, gInfo.getGoods_id());
                values.put(UtilsDB.COIL_INFO_CONTENT, gInfo.getGoods_introduce());
                values.put(UtilsDB.COIL_INFO_PAR_PRICE, gInfo.getGoods_price());
                values.put(UtilsDB.COIL_INFO_IMG_URL, gInfo.getGoods_url());
                values.put(UtilsDB.COIL_INFO_TYPE, gInfo.getGoods_type());
                values.put(UtilsDB.COIL_INFO_DETAIL_URL, gInfo.getGoods_details_url());
                values.put(UtilsDB.COIL_INFO_GOODS_SPEC, gInfo.getGoods_spec());
                values.put(UtilsDB.COIL_INFO_OTHER_PARAM1, gInfo.getGoodsOtherParam1());
                values.put(UtilsDB.COIL_INFO_OTHER_PARAM2, gInfo.getGoodsOtherParam2());
            }

            Coil_info coil = m_helper.queryCoilInfo(i);
            if(coil.getCoil_id() > 0){
                if (coil.getExtant_quantity() != 199) {   //此处getGoods_stock 为上货的个数
                    values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, coil.getExtant_quantity() + gInfo.getGoods_stock());
                }
                if (values.size() > 0) {
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        queryAliveCoil(true);
        sendReceiveData(false,TcnDBDef.ADD_SLOT_GOODS,m_listData_countGoods,-1,null);
    }

    public void OnUpdateSlotWorkStatus(boolean needUpToServer,int startslotNo,int endSlotNo, int status) {
        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {
            if (!isSlotNoExist(i)) {
                continue;
            }
            values.clear();
            values.put(UtilsDB.COIL_INFO_WORK_STATUS, status);//货道状态
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        queryAliveCoil(needUpToServer);
    }

    /**
     * 判断是否全部由数字组成
     * @param data
     * @return
     */
    public boolean isDigital(String data) {
        if ((null == data) || (data.length() < 1)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]*$");
        return pattern.matcher(data).matches();
    }

    public void OnClearSlotsFaults(int startSlotNo, int endSlotNo) {
        if ((null == m_list_aliveCoilAll) || (m_list_aliveCoilAll.size() < 1)) {
            sendReceiveData(false,TcnDBDef.CLEAR_SLOT_FAULTS,-1,-1,null);
            return;
        }
        if (startSlotNo < 1) {
            Coil_info startInfo = m_list_aliveCoilAll.get(0);
            Coil_info endInfo = m_list_aliveCoilAll.get(m_list_aliveCoilAll.size() - 1);
            OnUpdateSlotWorkStatus(true,startInfo.getCoil_id(),endInfo.getCoil_id(), 0);
            sendReceiveData(false,TcnDBDef.CLEAR_SLOT_FAULTS,startInfo.getCoil_id(),endInfo.getCoil_id(),null);
        } else {
            OnUpdateSlotWorkStatus(true,startSlotNo,endSlotNo, 0);
            sendReceiveData(false,TcnDBDef.CLEAR_SLOT_FAULTS,startSlotNo,endSlotNo,null);
        }
    }

    public Goods_info queryGoodsInfo(int id) {
        if (null == m_helper) {
            return null;
        }
        return m_helper.queryGoodsInfo(id);
    }

    public Goods_info queryGoodsInfo(String goodsId) {
        if (null == m_helper) {
            return null;
        }
        return m_helper.queryGoodsInfo(goodsId);
    }

    public int getKeyFromCoil(int slotNo) {
        int iKey = -1;
        Coil_info mInfo = getCoilInfo(slotNo);
        if (null == mInfo) {
            return iKey;
        }
        iKey = mInfo.getKeyNum();
        return iKey;
    }

    public Coil_info getAliveCoilFromKey(int key) {
        Coil_info mInfo = null;
        if ((m_list_aliveCoil != null) && (m_list_aliveCoil.size() > 0)) {
            for (Coil_info info:m_list_aliveCoil) {
                if (info.getKeyNum() == key) {
                    mInfo = info;
                    break;
                }
            }
        }
        return mInfo;
    }

    public Coil_info getCoilCanShipFromKey(int key) {
        Coil_info mInfo = null;
        if ((m_list_aliveCoil != null) && (m_list_aliveCoil.size() > 0)) {
            for (Coil_info info:m_list_aliveCoil) {
                if (info.getKeyNum() == key) {
                    mInfo = info;
                    if (0 == info.getWork_status()) {
                        mInfo = info;
                        if (info.getExtant_quantity() > 0) {
                            mInfo = info;
                            break;
                        }
                    }
                }
            }
        }
        return mInfo;
    }

    public Coil_info getAliveStockCoilFromKey(int key) {
        Coil_info mInfo = null;
        if ((m_list_aliveCoil != null) && (m_list_aliveCoil.size() > 0)) {
            for (Coil_info info:m_list_aliveCoil) {
                if (info.getKeyNum() == key) {
                    mInfo = info;
                    if (info.getExtant_quantity() > 0) {
                        break;
                    }
                }
            }
        }
        return mInfo;
    }

    public Goods_info getGoodsInfoAll(String code) {
        if ((null == code) || (code.isEmpty()) || (null == m_list_aliveGoodsAll)) {
            return null;
        }
        Goods_info mInfo = null;
        for (Goods_info info: m_list_aliveGoodsAll) {
            if (code.equals(info.getGoods_id())) {
                mInfo = info;
                break;
            }
        }
        return mInfo;
    }

    public Coil_info getUsableCoilFromKey(int key) {
        Coil_info mInfo = null;
        if ((m_list_aliveCoil != null) && (m_list_aliveCoil.size() > 0)) {
            for (Coil_info info:m_list_aliveCoil) {
                if (info.getKeyNum() == key) {
                    if (info.getWork_status() == 0) {
                        mInfo = info;
                        if (info.getExtant_quantity() > 0) {
                            mInfo = info;
                            break;
                        } else {
                            mInfo = info;
                        }
                    } else {
                        mInfo = info;
                    }
                }
            }
        }
        return mInfo;
    }


    public Coil_info getCoilInfoWithKey(int slotNoOrKey) {
        Coil_info info = null;
        if (m_bHaveKeyMap) {
            if (slotNoOrKey > 60) {
                info = m_helper.queryCoilInfo(slotNoOrKey);
            } else {
                info = getAliveCoilFromKey(slotNoOrKey);
            }
        } else {
            info = m_helper.queryCoilInfo(slotNoOrKey);
        }
        return info;
    }

    public Coil_info getCoilInfoCanShipWithKey(int slotNoOrKey) {
        Coil_info info = null;
        if (m_bHaveKeyMap) {
            if (slotNoOrKey > 60) {
                info = m_helper.queryCoilInfo(slotNoOrKey);
            } else {
                info = getCoilCanShipFromKey(slotNoOrKey);
            }
        } else {
            info = m_helper.queryCoilInfo(slotNoOrKey);
        }
        return info;
    }

    public Coil_info getUsableCoilInfo(int slotNo) {
        Coil_info info = null;
        if (m_bHaveKeyMap) {
            if (slotNo > 60) {
                info = m_helper.queryCoilInfo(slotNo);
            } else {
                int key = getKeyFromCoil(slotNo);
                if (key > 0) {
                    info = getUsableCoilFromKey(key);
                }
            }
        } else {
            info = m_helper.queryCoilInfo(slotNo);
        }
        return info;
    }

    public Coil_info getCoilInfo(int slotNo) {
        if (null == m_helper) {
            return null;
        }
        return m_helper.queryCoilInfo(slotNo);
    }

    public Key_info getKeyInfo(int key) {
        if (null == m_helper) {
            return null;
        }
        return m_helper.queryKeyInfo(key);
    }

    public boolean isErrForKey(int keyNumber) {
        boolean bRet = false;
        if ((null == m_list_aliveKey) || (m_list_aliveKey.size() < 1)) {
            return bRet;
        }
        /*for (Key_info kInfo:m_list_aliveKey) {
            if (kInfo.getKeyNum() == keyNumber) {
                if (kInfo.getWork_status() > UtilsDB.KEY_STATE_NO_COIL) {
                    bRet = true;
                    break;
                }
            }
        }*/
        return bRet;
    }

    public int getCoilExtantQuantityForKeyMap(int keyNumber) {
        int iStock = 0;
        if ((null == m_list_aliveKey) || (m_list_aliveKey.size() < 1)) {
            return iStock;
        }
        for (Key_info kInfo:m_list_aliveKey) {
            if (kInfo.getKeyNum() == keyNumber) {
                iStock = kInfo.getExtant_quantity();
                break;
            }
        }
        return iStock;
    }

    public Coil_info getAliveInfoFromKey(int key) {
        Coil_info sInfo = null;
        if (null == m_helper) {
            return sInfo;
        }
        Key_info kInfo = m_helper.queryKeyInfo(key);
        if ((null == kInfo) || (null == kInfo.getCoils())) {
            return sInfo;
        }
        if ((kInfo.getCoils()).contains("~")) {
            String[] mSlotList = (kInfo.getCoils()).split("\\~");
            if ((mSlotList != null) && (mSlotList.length > 0)) {
                for (String data:mSlotList) {
                    if (isDigital(data)) {
                        sInfo = m_helper.queryCoilInfo(Integer.valueOf(data).intValue());
                        if (sInfo.getWork_status() == 0) {
                            if (sInfo.getExtant_quantity() > 0) {
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            if (isDigital(kInfo.getCoils())) {
                sInfo = m_helper.queryCoilInfo(Integer.valueOf(kInfo.getCoils()).intValue());
            }
        }
        return sInfo;
    }

    public Coil_info getAliveCoilInfo(String goodsCode) {
        if (null == m_helper) {
            return null;
        }
        Coil_info sInfo = null;
        List<Coil_info> mList = m_helper.queryCoilByCode(goodsCode);
        if (mList.size() < 1) {
            return sInfo;
        }
        for (Coil_info info:mList) {
            if ((info.getCoil_id() > 0) && (info.getWork_status() == 0) && (info.getExtant_quantity() > 0)) {
                sInfo = info;
                break;
            }
        }
        if (null == sInfo) {
            sInfo = mList.get(0);
        }
        return sInfo;
    }

    public int getAliveSlotNo(String goodsCode) {
        int iSlotNo = -1;
        if (null == m_helper) {
            return iSlotNo;
        }

        List<Coil_info> mList = m_helper.queryCoilByCode(goodsCode);
        if (mList.size() < 1) {
            return iSlotNo;
        }
        for (Coil_info info:mList) {
            if ((info.getCoil_id() > 0) && (info.getWork_status() == 0) && (info.getExtant_quantity() > 0)) {
                iSlotNo = info.getCoil_id();
                break;
            }
        }
        if (-1 == iSlotNo) {
            iSlotNo = mList.get(0).getCoil_id();
        }
        return iSlotNo;
    }

    public Goods_info getGoodsInfo(String code) {
        if (null == m_helper) {
            return null;
        }
        return m_helper.queryGoodsInfo(code);
    }

    public List<Coil_info> queryAliveType(String type) {
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        if (m_helper != null) {
            coil_list = m_helper.queryAliveType(type);
        }
        return coil_list;
    }

    public void OnQueryModifyType(int startId, int endId, String type) {
        if (startId > endId) {
            endId = startId;
        }
        if (m_helper != null) {
            m_helper.modifyType(startId,endId,type);
            queryAliveCoil(true);
            int mTypeCount = 0;
            if (m_list_strType != null) {
                mTypeCount = m_list_strType.size();
            }
            TcnVendIF.getInstance().LoggerDebug(TAG, "OnQueryModifyType type: " + type+" mTypeCount: "+mTypeCount+" startId: "+startId+" endId: "+endId);
            sendReceiveData(false,TcnDBDef.MODIFY_TYPE,mTypeCount,m_listData_count,null);

        }
    }

    public void addShowSlotNo(int startcoilId,int endcoilId) {
        if (null == m_helper) {
            return;
        }
        Coil_info info = null;
        for (int i = startcoilId; i <= endcoilId; i++) {
            info = getCoilInfo(i);
            if (null != info) {
                if (info.getCoil_id() == i) {
                    ContentValues values = getAndModifySlotStatusValues(i);
                    if ((null == values) || (values.size() < 1)) {
                        //sendReceiveData(false,TcnDBDef.ADD_SHOW_COIL_ID, TcnDBResultDef.COIL_ID_OPERATION_FAIL,m_listData_count,m_context.getString(R.string.tip_modify_fail));
                    } else {
                        m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values, i);
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put(UtilsDB.COIL_INFO_COIL_ID, i);
                    values.put(UtilsDB.COIL_INFO_WORK_STATUS, 0);
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
                    values.put(UtilsDB.COIL_INFO_PAR_PRICE, String.valueOf(DEFAULT_COIL_PRICE));
                    values.put(UtilsDB.COIL_INFO_CAPACITY, 199);
                    values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, 199);
                    values.put(UtilsDB.COIL_INFO_GOODS_CODE, i);

                    m_helper.insertData(UtilsDB.COIL_INFO_TABLE_NAME, values);
                }
            } else {
                ContentValues values = new ContentValues();
                values.put(UtilsDB.COIL_INFO_COIL_ID, i);
                values.put(UtilsDB.COIL_INFO_WORK_STATUS, 0);
                values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
                values.put(UtilsDB.COIL_INFO_PAR_PRICE, String.valueOf(DEFAULT_COIL_PRICE));
                values.put(UtilsDB.COIL_INFO_SALE_PRICE, String.valueOf(DEFAULT_COIL_PRICE));
                values.put(UtilsDB.COIL_INFO_CAPACITY, 199);
                values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, 199);
                values.put(UtilsDB.COIL_INFO_GOODS_CODE, i);

                m_helper.insertData(UtilsDB.COIL_INFO_TABLE_NAME, values);
            }
        }
        queryAliveCoil(true);
        sendReceiveData(false,TcnDBDef.ADD_SHOW_COIL_ID, TcnDBResultDef.COIL_ID_OPERATION_SUCCESS,m_listData_count,m_context.getString(R.string.tip_modify_success));
    }

    public void addAndUpdateStatusSlotNo(int coilId,int status) {
        if ((null == m_helper) && (coilId < 1)) {
            return;
        }
        boolean bNeedUpdate = false;
        Coil_info info = getCoilInfo(coilId);
        if (null != info) {
            if (info.getCoil_id() == coilId) {
                if (info.getWork_status() != status) {
                    ContentValues values = getAndModifySlotStatusValues(coilId,status);
                    if ((null == values) || (values.size() < 1)) {

                    } else {
                        bNeedUpdate = true;
                        values.put(UtilsDB.COIL_INFO_WORK_STATUS, status);
                        m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values, coilId);
                    }
                }
            } else {
                bNeedUpdate = true;
                ContentValues values = new ContentValues();
                values.put(UtilsDB.COIL_INFO_COIL_ID, coilId);
                values.put(UtilsDB.COIL_INFO_WORK_STATUS, 0);
                values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
                values.put(UtilsDB.COIL_INFO_PAR_PRICE, String.valueOf(DEFAULT_COIL_PRICE));
                values.put(UtilsDB.COIL_INFO_SALE_PRICE, String.valueOf(DEFAULT_COIL_PRICE));
                values.put(UtilsDB.COIL_INFO_CAPACITY, 199);
                values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, 199);
                values.put(UtilsDB.COIL_INFO_GOODS_CODE, coilId);

                m_helper.insertData(UtilsDB.COIL_INFO_TABLE_NAME, values);
            }
        } else {
            bNeedUpdate = true;
            ContentValues values = new ContentValues();
            values.put(UtilsDB.COIL_INFO_COIL_ID, coilId);
            values.put(UtilsDB.COIL_INFO_WORK_STATUS, 0);
            values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
            values.put(UtilsDB.COIL_INFO_PAR_PRICE, String.valueOf(DEFAULT_COIL_PRICE));
            values.put(UtilsDB.COIL_INFO_SALE_PRICE, String.valueOf(DEFAULT_COIL_PRICE));
            values.put(UtilsDB.COIL_INFO_CAPACITY, 199);
            values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, 199);
            values.put(UtilsDB.COIL_INFO_GOODS_CODE, coilId);

            m_helper.insertData(UtilsDB.COIL_INFO_TABLE_NAME, values);
        }

        if (bNeedUpdate) {
            queryAliveCoil(true);
        }
    }

    public void deleteSlotNo(int coil_id) {
        int iRet = TcnDBResultDef.COIL_ID_OPERATION_FAIL;
        String strTips = m_context.getString(R.string.delete_success);
        if (m_helper != null) {
            int iDelete = m_helper.deleteSlotNo(UtilsDB.COIL_INFO_TABLE_NAME,coil_id);
            if (iDelete > -1) {
                iRet = TcnDBResultDef.COIL_ID_OPERATION_SUCCESS;
                strTips = m_context.getString(R.string.delete_fail);
                queryAliveCoil(true);
            }
            TcnVendIF.getInstance().LoggerDebug(TAG, "deleteSlotNo iDelete: " + iDelete);
        }
        sendReceiveData(false,TcnDBDef.DELETE_COIL_ID, iRet,m_listData_count,strTips);
    }

    public void deleteKeyMap(int keyNum) {
        if (null == m_helper) {
            return;
        }
        m_helper.deleteKeyMap(keyNum);
        queryAliveCoil(true);
    }

    public void addStock(int coil_id) {
        if (null == m_helper) {
            sendReceiveData(true,TcnDBDef.ADD_STOCK, coil_id,-1,null);
            return;
        }
        Coil_info sInfo = m_helper.queryCoilInfo(coil_id);
        if ((null == sInfo) || (sInfo.getCoil_id() < 1)) {
            sendReceiveData(true,TcnDBDef.ADD_STOCK, coil_id,-1,null);
            return;
        }
        int iStock = sInfo.getExtant_quantity();
        iStock++;
        ContentValues values = new ContentValues();
        values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, iStock);


        if (m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values, coil_id)) {
            queryAliveCoilDelay(true,3000);
            sendReceiveData(true,TcnDBDef.ADD_STOCK, coil_id,iStock,null);
        } else {
            sendReceiveData(true,TcnDBDef.ADD_STOCK, coil_id,sInfo.getExtant_quantity(),null);
        }
    }

    public void subStock(int coil_id) {
        if (null == m_helper) {
            sendReceiveData(true,TcnDBDef.SUB_STOCK, coil_id,-1,null);
            return;
        }
        Coil_info sInfo = m_helper.queryCoilInfo(coil_id);
        if ((null == sInfo) || (sInfo.getCoil_id() < 1)) {
            sendReceiveData(true,TcnDBDef.SUB_STOCK, coil_id,-1,null);
            return;
        }
        int iStock = sInfo.getExtant_quantity();
        if (iStock > 0) {
            iStock--;
        }
        ContentValues values = new ContentValues();
        values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, iStock);


        if (m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values, coil_id)) {
            queryAliveCoilDelay(true,3000);
            sendReceiveData(true,TcnDBDef.SUB_STOCK, coil_id,iStock,null);
        } else {
            sendReceiveData(true,TcnDBDef.SUB_STOCK, coil_id,sInfo.getExtant_quantity(),null);
        }
    }

    public void OnDeleteGoodsId(String goodsId) {
        if (null == m_helper) {
            return;
        }
        if ((null == goodsId) || (goodsId.length() < 1)) {
            return;
        }
        if (null == m_list_aliveCoil) {
            return;
        }
        boolean bCannotDelete = false;
        for (Coil_info info:m_list_aliveCoil) {
            if (goodsId.equals(info.getGoodsCode())) {
                bCannotDelete = true;
                break;
            }
        }
        if (bCannotDelete) {
            sendReceiveData(false,TcnDBDef.DELETE_GOODS_ID, -1,-1,m_context.getString(R.string.delete_cannot_sale));
        } else {
            int iDelete = m_helper.deleteData(UtilsDB.GOODS_INFO_TABLE_NAME,goodsId);
            if (iDelete > -1) {
                queryAliveCoil(false);
                sendReceiveData(false,TcnDBDef.DELETE_GOODS_ID, iDelete,-1,m_context.getString(R.string.delete_success));
            } else {
                sendReceiveData(false,TcnDBDef.DELETE_GOODS_ID, iDelete,-1,m_context.getString(R.string.delete_fail));
            }

        }
    }

    public int getSelectedSlotNo() {
        int iSlotNo = -1;
        if (null == m_selectCoilInfo) {
            return iSlotNo;
        }
        return m_selectCoilInfo.getCoil_id();
    }

    public List<Integer> getAliveKeyCoil() {
        return m_list_count_slot_key;
    }

    public List<Integer> getAliveKeyCoilAll() {
        return m_list_count_slot_key_all;
    }

    public List<Coil_info> getAliveCoilAll() {
        return m_list_aliveCoilAll;
    }

    public int getAliveCoilCountAll() {
        return m_listData_countAll;
    }

    public int getAliveCoilCountFaults() {
        return m_listData_countFaults;
    }

    public List<Key_info> getAliveKey() {
        return m_list_aliveKey;
    }

    public int getAliveKeyCount() {
        return m_listData_countKey;
    }

    public List<Key_info> getAliveKeyAll() {
        return m_list_aliveKeyAll;
    }

    public int getAliveKeyCountAll() {
        return m_listData_countKeyAll;
    }

    public Coil_info getCoilInfoAll(int position) {
        Coil_info info = null;
        if (m_list_aliveCoilAll != null) {
            if((position >= m_listData_countAll) || (position < 0)) {
                return info;
            }
            info = m_list_aliveCoilAll.get(position);
        }
        return info;
    }

    public void setSelecting(boolean select) {
        m_bIsSelecting = select;
    }

    public boolean isSelecting() {
        return m_bIsSelecting;
    }


    public void setSelectedKey(int key) {
        if (m_bHaveKeyMap) {
            m_iSelectedKey = key;
        }
    }

    public int getSelectedKey() {
        return m_iSelectedKey;
    }


    public void setSelectCoilInfo(Coil_info info) {
        m_selectCoilInfo = info;
    }

    public Coil_info getSelectCoilInfo() {
        return m_selectCoilInfo;
    }

    public List<Coil_info> getAliveCoilExcept() {
        return m_list_aliveCoilExcept;
    }

    public int getAliveCoilCountExcept() {
        return m_listData_countExcept;
    }

    public Coil_info getAliveCoilExcept(int position) {
        Coil_info info = null;
        if (null == m_list_aliveCoilExcept) {
            return info;
        }
        if (m_list_aliveCoilExcept.size() <= position) {
            return info;
        }
        return m_list_aliveCoilExcept.get(position);
    }

    public void setGoodsType(String type) {
        m_strCurrentGoodsType = type;
    }

    public String getGoodsType() {
        return m_strCurrentGoodsType;
    }

    public List<Coil_info> getAliveCoil() {
        synchronized (m_aliveCoil_lock) {
            return m_list_aliveCoil;
        }
    }

    public int getAliveCoilCount() {
        synchronized (m_aliveCoil_lock) {
            return m_listData_count;
        }
    }

    public int getCoilCountWithKey(List<Coil_info> coilInfoList) {
        int iCount = 0;
        if ((null == coilInfoList) || (coilInfoList.size() < 1)) {
            return iCount;
        }
        for (Coil_info info:coilInfoList) {
            if (info.getKeyNum() > 0) {
                iCount++;
            }
        }
        return iCount;
    }

    public Coil_info getAliveCoil(int position) {
        Coil_info info = null;
        if (null == m_list_aliveCoil) {
            TcnVendIF.getInstance().LoggerError(TAG, "getAliveCoil() m_list_aliveCoil is null");
            return info;
        }
        if (m_list_aliveCoil.size() <= position) {
            TcnVendIF.getInstance().LoggerError(TAG, "getAliveCoil() position: "+position+" size: "+m_list_aliveCoil.size());
            queryAliveCoil(false);
            return info;
        }
        return m_list_aliveCoil.get(position);
    }

    public List<Coil_info> getAliveCoilFromType(String type) {
        if (null == m_list_strType || null == m_list_aliveCoilType) {
            return null;
        }

        int index = m_list_strType.indexOf(type);
        if (index < 0 || index >= m_list_aliveCoilType.size()) {
            return null;
        }
        return m_list_aliveCoilType.get(index);
    }

    public String getGoodsName(int slotNo) {
        String name = "";
        Coil_info info = getCoilInfo(slotNo);
        if (info != null) {
            name = info.getPar_name();
        }

        return name;
    }
    public String getGoodsCode(int slotNo) {
        String name = "";
        Coil_info info = getCoilInfo(slotNo);
        if (info != null) {
            name = info.getGoodsCode();
        }

        return name;
    }

    public String getGoodsImageUrl(int slotNo) {
        String mImageUrl = "";
        Coil_info info = getCoilInfo(slotNo);
        if (info != null) {
            mImageUrl = info.getImg_url();
        }

        return mImageUrl;
    }

    public String getGoodsContent(int slotNo) {
        String content = "";
        Coil_info info = getCoilInfo(slotNo);
        if (info != null) {
            content = info.getContent();
            if (null == content) {
                content = "";
            }
        }

        return content;
    }

    private String getPriceFromSlotNo(int slotNo) {
        if (null == m_helper) {
            return null;
        }
        Coil_info mInfo = m_helper.queryCoilInfo(slotNo);
        String price = null;
        if (mInfo != null) {
            price = mInfo.getPar_price();
        }
        return price;
    }

    public boolean isSlotInfoSame(Coil_info origInfo, Coil_info newInfo) {
        boolean bRet = true;
        if ((null == origInfo) || (null == newInfo)) {
            bRet = false;
            return bRet;
        }
        if ((origInfo.getCoil_id()) != (newInfo.getCoil_id())) {
            bRet = false;
        }

        if ((origInfo.getExtant_quantity()) != (newInfo.getExtant_quantity())) {
            bRet = false;
        }

        if ((origInfo.getWork_status()) != (newInfo.getWork_status())) {
            bRet = false;
        }

        if ((origInfo.getRay()) != (newInfo.getRay())) {
            bRet = false;
        }

        if ((origInfo.getCapacity()) != (newInfo.getCapacity())) {
            bRet = false;
        }

        if ((origInfo.getSaleNum()) != (newInfo.getSaleNum())) {
            bRet = false;
        }

        if ((origInfo.getKeyNum()) != (newInfo.getKeyNum())) {
            bRet = false;
        }

        if ((origInfo.getSlotStatus()) != (newInfo.getSlotStatus())) {
            bRet = false;
        }

        if (newInfo.getContent() != null) {
            if (!(newInfo.getContent()).equals(origInfo.getContent())) {
                bRet = false;
            }
        }

        if (newInfo.getPar_price() != null) {
            if ((!(newInfo.getPar_price()).equals(origInfo.getPar_price()))) {
                bRet = false;
            }
        }

        /*if (newInfo.getImg_url() != null) {
            if (!(newInfo.getImg_url()).equals(origInfo.getImg_url())) {
                bRet = false;
            }
        }*/

        if (newInfo.getType() != null) {
            if (!(newInfo.getType()).equals(origInfo.getType())) {
                bRet = false;
            }
        }


        if (newInfo.getGoodsCode() != null) {
            if (!(newInfo.getGoodsCode()).equals(origInfo.getGoodsCode())) {
                bRet = false;
            }
        }


        if (newInfo.getGoodsSpec() != null) {
            if (!(newInfo.getGoodsSpec()).equals(origInfo.getGoodsSpec())) {
                bRet = false;
            }
        }

        if (newInfo.getGoodsCapacity() != null) {
            if (!(newInfo.getGoodsCapacity()).equals(origInfo.getGoodsCapacity())) {
                bRet = false;
            }
        }

        /*if (newInfo.getGoods_details_url() != null) {
            if (!(newInfo.getGoods_details_url()).equals(origInfo.getGoods_details_url())) {
                bRet = false;
            }

        }*/

        return bRet;
    }


    public Coil_info getSlotInfo(int slotNo, List<Coil_info> infoList) {
        Coil_info mInfo = null;
        if (null == infoList) {
            return mInfo;
        }
        for (Coil_info info: infoList) {
            if (info.getCoil_id() == slotNo) {
                mInfo = info;
                break;
            }
        }
        return mInfo;
    }

    public List<Coil_info> getSlotInfoDifferent(List<Coil_info> origInfoList, List<Coil_info> newInfoList) {
        List<Coil_info> infoList = new ArrayList<Coil_info>();
        if(null == newInfoList) {
            return newInfoList;
        }
        if (null == origInfoList) {
            return newInfoList;
        }
        if (origInfoList.size() <= 0) {
            return newInfoList;
        }
        for (Coil_info newInfo: newInfoList) {
            Coil_info mInfo = getSlotInfo(newInfo.getCoil_id(),origInfoList);
            if ((mInfo != null) && (mInfo.getCoil_id() > 0)) {
                if (!isSlotInfoSame(mInfo,newInfo)) {
                    infoList.add(newInfo);
                }
            } else {
                infoList.add(newInfo);
            }
        }
        return infoList;
    }

    //获取所有按键对应的货道
    private List<Coil_info>  getKeyAliveCoilList(TreeMap<Integer, ArrayList<Integer>> keyMapSlotNo) {
        List<Coil_info>  mListAliveCoil = new ArrayList<Coil_info>();
        if (null == keyMapSlotNo) {
            return mListAliveCoil;
        }

        Set<Integer> keySet = keyMapSlotNo.keySet();//先获取map集合的所有键的Set集合  keynum 集合
        for (Integer key:keySet) {
            ArrayList<Integer> mList = keyMapSlotNo.get(key);
            int size = mList.size();
            for (int i = 0; i < size; i++) {
                Coil_info info = m_helper.queryCoilInfo(mList.get(i));
                if (i == (size - 1)) {
                    info.setKeyNum(key);
                    mListAliveCoil.add(info);
                } else {
                    if (info.getExtant_quantity() > 0) {
                        info.setKeyNum(key);
                        mListAliveCoil.add(info);
                        break;
                    }
                }
            }
        }
        return mListAliveCoil;
    }


    public List<Goods_info> getAliveGoodsAll() {
        return m_list_aliveGoodsAll;
    }

    public int getAliveGoodsCountAll() {
        return m_listData_countGoodsAll;
    }

    public List<Goods_info> getAliveGoods() {
        return m_list_aliveGoods;
    }

    public int getAliveGoodsCount() {
        return m_listData_countGoods;
    }

    public Goods_info getAliveGoods(int position) {
        Goods_info info = null;
        if (null == m_list_aliveGoods) {
            return info;
        }
        return m_list_aliveGoods.get(position);
    }

    private boolean querySlotInfo() {
        synchronized (m_aliveCoil_lock) {
            boolean bRet = false;
            if (null == m_helper) {
                return bRet;
            }

            bRet = true;

            if (m_bHaveKeyMap) {
                if (m_bIsMainBoardControl) {
                    m_list_aliveCoil = getKeyAliveCoilList(m_mapKeySlotNo);
                    if (!m_bOnlyKeyMap) {
                        List<Coil_info> mListAliveCoil = m_helper.queryCoilListExceptKey();
                        for (Coil_info info : mListAliveCoil) {
                            m_list_aliveCoil.add(info);
                        }
                    }
                } else {
                    m_list_aliveCoil = m_helper.queryAliveCoil();
                    m_list_aliveKey = m_helper.queryAliveKey();
                    m_list_aliveKeyAll = m_helper.queryAliveKeyAll();
                    TcnVendIF.getInstance().LoggerDebug(TAG, "querySlotInfo m_list_aliveKeyAll size: " + m_list_aliveKeyAll.size());
                }
                //  m_list_aliveCoil = m_helper.queryAliveCoil();
            } else {
                m_list_aliveCoil = m_helper.queryAliveCoil();
            }

            m_list_aliveCoilAll = m_helper.queryAliveCoilAll();

            if (m_list_aliveCoil != null) {
                m_listData_count = m_list_aliveCoil.size();
            }

            if (m_list_aliveCoilAll != null) {
                m_listData_countAll = m_list_aliveCoilAll.size();
            }

            OnQueryAliveType();

            OnQueryAliveGoods();

            OnQueryAliveKey();

            TcnVendIF.getInstance().LoggerDebug(TAG, "querySlotInfo m_listData_count: " + m_listData_count+" m_listData_countAll: "+m_listData_countAll
                    +" m_listData_countKey: "+m_listData_countKey+" m_list_count_slot_key: "+m_list_count_slot_key+" m_bHaveKeyMap: "+m_bHaveKeyMap);

            return bRet;
        }
    }

    public void OnQueryAliveCoil(boolean needUpToServer) {
        synchronized (m_aliveCoil_lock) {
            boolean bRet = querySlotInfo();
            if (!bRet) {
                return;
            }

            List<Coil_info> mDiffList = getSlotInfoDifferent(m_list_origAiveCoil, m_list_aliveCoil);

            TcnVendIF.getInstance().LoggerDebug(TAG, "OnQueryAliveCoil mDiffList size: " + mDiffList.size());

            sendReceiveData(false,TcnDBDef.QUERY_ALIVE_COIL,m_listData_count,-1,mDiffList);

            if (needUpToServer) {
                sendReceiveData(false,TcnDBDef.CHANGED_ALIVE_COIL,NEED_UP_TO_SERVER,-1,mDiffList);
            } else {
                sendReceiveData(false,TcnDBDef.CHANGED_ALIVE_COIL,NEED_NOT_UP_TO_SERVER,-1,mDiffList);
            }

            m_list_origAiveCoil = m_list_aliveCoil;
        }
    }

    public void OnQueryAliveCoil(boolean needUpToServer,int slotNo) {
        synchronized (m_aliveCoil_lock) {

            boolean bRet = querySlotInfo();
            if (!bRet) {
                return;
            }

            TcnVendIF.getInstance().LoggerDebug(TAG, "OnQueryAliveCoil m_listData_count: " + m_listData_count+" m_listData_countAll: "+m_listData_countAll+" slotNo: "+slotNo);
            sendReceiveData(false,TcnDBDef.QUERY_ALIVE_COIL,m_listData_count,slotNo,null);
            List<Coil_info> mList = new ArrayList<Coil_info>();
            mList.add(getCoilInfo(slotNo));
            if (needUpToServer) {
                sendReceiveData(true,TcnDBDef.CHANGED_ALIVE_COIL,NEED_UP_TO_SERVER,slotNo,mList);
            } else {
                sendReceiveData(true,TcnDBDef.CHANGED_ALIVE_COIL,NEED_NOT_UP_TO_SERVER,slotNo,mList);
            }
        }
    }

    public void OnQueryAliveType() {
        if (!m_bShowType) {
            return;
        }
        m_list_strType = m_helper.getAllType(m_list_aliveCoil);

        OnQueryAliveExceptType(m_list_strType);

        if (m_list_strType != null && m_list_strType.size() > 0) {
            if (null == m_list_aliveCoilType) {
                m_list_aliveCoilType = new ArrayList<List<Coil_info>>();
            }
            m_list_aliveCoilType.clear();
            for (int i = 0; i < m_list_strType.size(); i++) {
                m_list_aliveCoilType.add(m_helper.queryAliveType(m_list_strType.get(i)));
            }

        }
    }

    public void OnQueryAliveExceptType(List<String> listType) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnQueryAliveExceptType");
        if (m_helper != null) {
            m_list_aliveCoilExcept = m_helper.queryAliveExceptType(listType);
            if (m_list_aliveCoilExcept != null) {
                m_listData_countExcept = m_list_aliveCoilExcept.size();
            }
            sendReceiveData(false,TcnDBDef.QUERY_ALIVE_COIL_EXCEPTIONE,m_listData_countExcept,-1,null);
        }
    }

    public List<Coil_info> OnQueryOpendSlot() {
        List<Coil_info> mList = null;
        if (m_helper != null) {
            mList = m_helper.queryOpendCoil();
        }
        return mList;
    }

    public void OnUpdateCloseStatus(int slotNo, boolean isClosed) {
        if (m_helper != null) {
            ContentValues values = new ContentValues();
            if (isClosed) {
                values.put(UtilsDB.COIL_INFO_CLOSE_STATUS, UtilsDB.CLOSED);
            } else {
                values.put(UtilsDB.COIL_INFO_CLOSE_STATUS, UtilsDB.OPENED);
            }
            m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNo);
        }
    }

    private List<Integer> getGoodsMapSlotNoSortList(String goodsSlotMap) {
        List<Integer> mList = new ArrayList<Integer>();
        if ((null == goodsSlotMap) || (goodsSlotMap.length() < 1)) {
            return mList;
        }
        if (goodsSlotMap.contains("~")) {
            String[] strarr = goodsSlotMap.split( "\\~" );
            for (String data:strarr) {
                if (TcnVendIF.getInstance().isDigital(data)) {
                    mList.add(Integer.valueOf(data));
                }
            }
        } else {
            if (TcnVendIF.getInstance().isDigital(goodsSlotMap)) {
                mList.add(Integer.valueOf(goodsSlotMap));
            }
        }

        Collections.sort(mList);  //排序

        return mList;
    }

    private int getStock(List<Coil_info> slotList) {
        int stock = 0;
        if ((null == slotList) || (slotList.size() <= 0)) {
            return stock;
        }
        for (Coil_info info:slotList) {
            stock += info.getExtant_quantity();
        }
        return stock;
    }

    private int getCapacity(List<Coil_info> slotList) {
        int iCapacity = 0;
        if ((null == slotList) || (slotList.size() <= 0)) {
            return iCapacity;
        }
        for (Coil_info info:slotList) {
            iCapacity += info.getCapacity();
        }
        return iCapacity;
    }

    private void OnUpdateGoodsLastShipSlotNo(Coil_info sInfo) {
        if (!m_bShowByGoodsCode) {
            return;
        }

        if (null == m_helper) {
            return;
        }

        if ((null == sInfo) || (sInfo.getCoil_id() < 1)) {
            return;
        }

        if ((sInfo.getGoodsCode() == null) || (sInfo.getGoodsCode().length() < 1)) {
            return;
        }

        Goods_info gInfo = m_helper.queryGoodsInfo(sInfo.getGoodsCode());
        ContentValues values = new ContentValues();
        values.put(UtilsDB.GOODS_INFO_LAST_SHIP_SLOT, sInfo.getCoil_id());
        m_helper.updateData(UtilsDB.GOODS_INFO_TABLE_NAME, values,gInfo.getID());
    }

    private void OnUpdateAliveGoods(Goods_info gInfo,List<Coil_info> slotList) {
        if (null == m_helper) {
            return;
        }
        if ((null == gInfo) || (null == slotList) || (slotList.size() <= 0)) {
            return;
        }
        StringBuffer slotGoodsMap = new StringBuffer();
        StringBuffer slotData = new StringBuffer();
        for (Coil_info info:slotList) {
            slotData.delete(0,slotData.length());
            slotData.append(String.valueOf(info.getCoil_id()));
            slotData.append("~");
            slotGoodsMap.append(slotData.toString());
        }
        int length = slotGoodsMap.length();
        slotGoodsMap.delete(length - 1,length);
        Coil_info sInfo = slotList.get(0);
        ContentValues values = new ContentValues();
        values.put(UtilsDB.GOODS_INFO_NAME, sInfo.getPar_name());
        values.put(UtilsDB.GOODS_INFO_PRICE, sInfo.getPar_price());
        values.put(UtilsDB.GOODS_INFO_TYPE, sInfo.getType());
        values.put(UtilsDB.GOODS_INFO_INTRODUCE, sInfo.getContent());
        values.put(UtilsDB.GOODS_INFO_SPEC, sInfo.getGoodsSpec());
        values.put(UtilsDB.GOODS_INFO_URL, sInfo.getImg_url());
        values.put(UtilsDB.GOODS_INFO_DETAILS_URL, sInfo.getGoods_details_url());
        values.put(UtilsDB.GOODS_INFO_OTHER_PARAM1, sInfo.getOtherParam1());
        values.put(UtilsDB.GOODS_INFO_OTHER_PARAM2, sInfo.getOtherParam2());

        values.put(UtilsDB.GOODS_INFO_STOCK,getStock(slotList));
        values.put(UtilsDB.GOODS_INFO_CAPACITY, getCapacity(slotList));
        values.put(UtilsDB.GOODS_INFO_SLOT_MAP, slotGoodsMap.toString());

        if (gInfo.getGoods_status() < 0) {
            values.put(UtilsDB.GOODS_INFO_WORK_STATUS,0);
        }

        m_helper.updateData(UtilsDB.GOODS_INFO_TABLE_NAME, values,gInfo.getID());
    }

    private void OnInsertAliveGoods(Coil_info sInfo) {
        if (null == m_helper) {
            return;
        }
        if ((null == sInfo.getGoodsCode()) || (sInfo.getGoodsCode().length() < 1)) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(UtilsDB.GOODS_INFO_NAME, sInfo.getPar_name());
        values.put(UtilsDB.GOODS_INFO_PRODUCT_ID, sInfo.getGoodsCode());
        values.put(UtilsDB.GOODS_INFO_PRICE, sInfo.getPar_price());
        values.put(UtilsDB.GOODS_INFO_TYPE, sInfo.getType());
        values.put(UtilsDB.GOODS_INFO_INTRODUCE, sInfo.getContent());
        values.put(UtilsDB.GOODS_INFO_STOCK, sInfo.getExtant_quantity());
        values.put(UtilsDB.GOODS_INFO_CAPACITY, sInfo.getCapacity());
        values.put(UtilsDB.GOODS_INFO_SPEC, sInfo.getGoodsSpec());
        values.put(UtilsDB.GOODS_INFO_URL, sInfo.getImg_url());
        values.put(UtilsDB.GOODS_INFO_DETAILS_URL, sInfo.getGoods_details_url());
        values.put(UtilsDB.GOODS_INFO_OTHER_PARAM1, sInfo.getOtherParam1());
        values.put(UtilsDB.GOODS_INFO_OTHER_PARAM2, sInfo.getOtherParam2());
        values.put(UtilsDB.GOODS_INFO_WORK_STATUS, 0);
        values.put(UtilsDB.GOODS_INFO_LAST_SHIP_SLOT, 0);
        values.put(UtilsDB.GOODS_INFO_SLOT_MAP, String.valueOf(sInfo.getCoil_id()));
        m_helper.insertData(UtilsDB.GOODS_INFO_TABLE_NAME, values);
    }

    private void OnUpdateKey(List<Key_info> keyList,List<Coil_info> slotList) {
        if ((null == keyList) || (keyList.size() < 1) || (null == slotList) || (slotList.size() < 1)) {
            return;
        }
        for (Key_info kInfo:keyList) {
            int iExtant_quantity = 0;
            int iCapacity = 0;
            StringBuffer CoilsBf = new StringBuffer();
            for (Coil_info sInfo:slotList) {
           //     TcnVendIF.getInstance().LoggerDebug(TAG, "OnUpdateKey xxx getCoil_id: " + sInfo.getCoil_id()+" sKeyNum: "+sInfo.getKeyNum()+" getKeyNum: "+kInfo.getKeyNum());
                if ((sInfo.getCoil_id() <= 60) && (sInfo.getKeyNum() > 0)) {
                    if ((sInfo.getKeyNum()) == (kInfo.getKeyNum())) {
                        iExtant_quantity += sInfo.getExtant_quantity();
                        iCapacity += sInfo.getCapacity();
                        CoilsBf.append(String.valueOf(sInfo.getCoil_id()));
                        CoilsBf.append("~");

                        ContentValues values = new ContentValues();
                        values.put(UtilsDB.KEY_INFO_PAR_NAME, sInfo.getPar_name());
                        values.put(UtilsDB.KEY_INFO_CONTENT, sInfo.getContent());
                        values.put(UtilsDB.KEY_INFO_PAR_PRICE, sInfo.getPar_price());
                        values.put(UtilsDB.KEY_INFO_SALE_PRICE, sInfo.getSalePrice());
                        values.put(UtilsDB.KEY_INFO_GOODS_CODE, sInfo.getGoodsCode());
                        values.put(UtilsDB.KEY_INFO_TYPE, sInfo.getType());
                        values.put(UtilsDB.KEY_INFO_IMG_URL, sInfo.getImg_url());
                        values.put(UtilsDB.KEY_INFO_QRPAY_URL, sInfo.getQrPayUrl());
                        values.put(UtilsDB.KEY_INFO_DETAIL_URL, sInfo.getGoods_details_url());
                        values.put(UtilsDB.KEY_INFO_AD_URL, sInfo.getAdUrl());
                        m_helper.updateData(UtilsDB.KEY_INFO_TABLE_NAME, values,kInfo.getKeyNum());
                    }
                }
            }
            if (CoilsBf.toString().endsWith("~")) {
                CoilsBf.delete(CoilsBf.length() - 1,CoilsBf.length());
            }
          //  TcnVendIF.getInstance().LoggerDebug(TAG, "OnUpdateKey ......getCoils: " + kInfo.getCoils()+" CoilsBf: "+CoilsBf);
            ContentValues values = new ContentValues();
            if (CoilsBf.length() < 1) {
                values.put(UtilsDB.KEY_INFO_WORK_STATUS, UtilsDB.KEY_STATE_NO_COIL);
            } else if (iExtant_quantity < 1) {
                values.put(UtilsDB.KEY_INFO_WORK_STATUS, UtilsDB.KEY_STATE_NO_GOODS);
            } else {
                values.put(UtilsDB.KEY_INFO_WORK_STATUS, UtilsDB.KEY_STATE_HAVE_GOODS);
            }
            if (iExtant_quantity > 199) {
                iExtant_quantity = 199;
            }
            if (iCapacity > 199) {
                iCapacity = 199;
            }
            values.put(UtilsDB.KEY_INFO_EXTANT_QUANTITY, iExtant_quantity);
            values.put(UtilsDB.KEY_INFO_CAPACITY, iCapacity);
            values.put(UtilsDB.KEY_INFO_COILS, CoilsBf.toString());
            m_helper.updateData(UtilsDB.KEY_INFO_TABLE_NAME, values,kInfo.getKeyNum());
        }
    }

    private void OnUpdateGoodsSlotMap(Goods_info gInfo) {
        if (null == m_helper) {
            return;
        }
        if ((null == gInfo.getGoods_id()) || (gInfo.getGoods_id().length() < 1)) {
            m_helper.deleteData(UtilsDB.GOODS_INFO_TABLE_NAME,gInfo.getID());
            return;
        }

        if ((null == gInfo.getGoodsSlotMap()) || (gInfo.getGoodsSlotMap().length() < 1)) {
            return;
        }
        List<Integer> mNewSlotNoList = new ArrayList<Integer>();
        List<Integer> mSlotNoList = getGoodsMapSlotNoSortList(gInfo.getGoodsSlotMap());
        Coil_info sInfo = null;
        boolean bHasInVaild = false;
        for (Integer slotNo:mSlotNoList) {
            sInfo = m_helper.queryCoilInfo(slotNo.intValue());
            if ((sInfo.getCoil_id() > 0) && (sInfo.getWork_status() < 255)) {
                if (((gInfo.getGoods_id()).equals(sInfo.getGoodsCode()))) {
                    mNewSlotNoList.add(slotNo);
                } else {
                    bHasInVaild = true;
                }
            } else {
                bHasInVaild = true;
            }
        }

        if (bHasInVaild) {
            StringBuffer slotGoodsMap = new StringBuffer();
            StringBuffer slotData = new StringBuffer();
            if (mNewSlotNoList.size() > 0) {
                for (Integer slotNo:mNewSlotNoList) {
                    slotData.delete(0,slotData.length());
                    slotData.append(String.valueOf(slotNo.intValue()));
                    slotData.append("~");
                    slotGoodsMap.append(slotData.toString());
                }
                int length = slotGoodsMap.length();
                slotGoodsMap.delete(length - 1,length);

                ContentValues values = new ContentValues();
                values.put(UtilsDB.GOODS_INFO_SLOT_MAP, slotGoodsMap.toString());
                m_helper.updateData(UtilsDB.GOODS_INFO_TABLE_NAME, values,gInfo.getID());
            } else {
                m_helper.deleteData(UtilsDB.GOODS_INFO_TABLE_NAME,gInfo.getID());
            }
        }
    }

    public void OnQueryAliveGoods() {

        if (!m_bShowByGoodsCode) {
            return;
        }

        if (null == m_helper) {
            return;
        }

        //重新绑定货道
        List<Goods_info>  goodsList = m_helper.queryGoodsInfoAll();
        for (Goods_info gInfo:goodsList) {
            OnUpdateGoodsSlotMap(gInfo);
        }

        List<Coil_info> codeCoilList = m_helper.queryCoilByCode();
        if ((null == codeCoilList) || (codeCoilList.size() < 1)) {
            m_list_aliveGoods = m_helper.queryGoodsInfoAlive();
            if (m_list_aliveGoods != null) {
                m_listData_countGoods = m_list_aliveGoods.size();
            }

            m_list_aliveGoodsAll = m_helper.queryGoodsInfoAll();
            if (m_list_aliveGoodsAll != null) {
                m_listData_countGoodsAll = m_list_aliveGoodsAll.size();
            }
            sendReceiveData(false,TcnDBDef.QUERY_ALIVE_GOODS,m_listData_countGoods,-1,null);
            return;
        }
        for (Coil_info sInfo:codeCoilList) {
            List<Goods_info>  mGoodsList = m_helper.queryGoodsInfoAll();
            if ((mGoodsList != null) && (mGoodsList.size() > 0)) {
                boolean bExistCode = false;
                for (Goods_info gInfo:mGoodsList) {
                    if ((gInfo.getGoods_id()).equals(sInfo.getGoodsCode())) {
                        bExistCode = true;
                        List<Coil_info> mSlotList = m_helper.queryCoilByCode(sInfo.getGoodsCode());
                        OnUpdateAliveGoods(gInfo,mSlotList);
                        break;
                    }
                }
                if (!bExistCode) {
                    OnInsertAliveGoods(sInfo);
                }
            } else {
                OnInsertAliveGoods(sInfo);
            }
        }

        m_list_aliveGoods = m_helper.queryGoodsInfoAlive();
        if (m_list_aliveGoods != null) {
            m_listData_countGoods = m_list_aliveGoods.size();
        }

        m_list_aliveGoodsAll = m_helper.queryGoodsInfoAll();
        if (m_list_aliveGoodsAll != null) {
            m_listData_countGoodsAll = m_list_aliveGoodsAll.size();
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnQueryAliveGoods m_listData_countGoods: " + m_listData_countGoods+" m_listData_countGoodsAll: "+m_listData_countGoodsAll);
        sendReceiveData(false,TcnDBDef.QUERY_ALIVE_GOODS,m_listData_countGoods,-1,null);
    }

    private void OnQueryAliveKey() {
        if (!m_bHaveKeyMap) {
            return;
        }
        if (m_bIsMainBoardControl) {
            return;
        }
        OnUpdateKey(m_list_aliveKeyAll,m_list_aliveCoilAll);

        m_list_aliveKey = m_helper.queryAliveKey();
        m_list_aliveKeyAll = m_helper.queryAliveKeyAll();

        if (null == m_list_count_slot_key) {
            m_list_count_slot_key = new ArrayList<Integer>();
        } else {
            m_list_count_slot_key.clear();
        }
        if (null == m_list_count_slot_key_all) {
            m_list_count_slot_key_all = new ArrayList<Integer>();
        } else {
            m_list_count_slot_key_all.clear();
        }

        if (m_list_aliveKey != null) {
            for (Key_info info:m_list_aliveKey) {
                m_list_count_slot_key.add(info.getKeyNum());
            }
            m_listData_countKey = m_list_aliveKey.size();
        }

        if (m_list_aliveCoil != null) {
            for (Coil_info info:m_list_aliveCoil) {
                if (info.getCoil_id() > 100) {
                    m_list_count_slot_key.add(info.getCoil_id());
                }

            }
        }


        if (m_list_aliveKeyAll != null) {
            for (Key_info info:m_list_aliveKeyAll) {
                m_list_count_slot_key_all.add(info.getKeyNum());
            }
            m_listData_countKeyAll = m_list_aliveKeyAll.size();
        }

        if (m_list_aliveCoilAll != null) {
            for (Coil_info info:m_list_aliveCoilAll) {
                if (info.getCoil_id() > 100) {
                    m_list_count_slot_key_all.add(info.getCoil_id());
                }
            }
        }
    }

    public void OnQueryDeleteType(String type) {
        if (m_helper != null) {
            m_helper.deleteType(type);
            queryAliveCoil(true);
            int mTypeCount = 0;
            if (m_list_strType != null) {
                mTypeCount = m_list_strType.size();
            }
            TcnVendIF.getInstance().LoggerDebug(TAG, "OnQueryDeleteType type: " + type+" mTypeCount: "+mTypeCount);
            sendReceiveData(false,TcnDBDef.DELETE_TYPE,mTypeCount,m_listData_count,null);
        }
    }


    public void OnInsertData(int tableType, ContentValues values) {
        if ((null == m_helper) || (null == values)) {
            TcnVendIF.getInstance().LoggerError(TAG, "insertData tableType: " + tableType+" values: "+values);
            return;
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "insertData tableType: " + tableType+" values.size(): "+values.size());
        if (tableType == TcnVendIF.DB_TABLE_SLOTNO) {
            m_helper.insertData(UtilsDB.COIL_INFO_TABLE_NAME,values);
        } else if (tableType == TcnVendIF.DB_TABLE_GOODS) {
            m_helper.insertData(UtilsDB.GOODS_INFO_TABLE_NAME,values);
        } else {

        }
        queryAliveCoil(true);
        sendReceiveData(false,TcnDBDef.INSERT_DATA,tableType,m_listData_count,null);
    }

    public void OnUpdateData(int tableType,int id,ContentValues values) {
        if ((null == m_helper) || (null == values) || (id < 0)) {
            TcnVendIF.getInstance().LoggerError(TAG, "udateData tableType: " + tableType+" values: "+values+" id: "+id);
            return;
        }
        if (tableType == TcnVendIF.DB_TABLE_SLOTNO) {
            m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME,values,id);
        } else if (tableType == TcnVendIF.DB_TABLE_GOODS) {
            m_helper.updateData(UtilsDB.GOODS_INFO_TABLE_NAME,values,id);
            String strCode = values.getAsString(UtilsDB.GOODS_INFO_PRODUCT_ID);
            if ((strCode != null) && (strCode.length() > 0)) {
                List<Coil_info> mList = m_helper.queryCoilByCode(strCode);
                if ((mList != null) && (mList.size() > 0)) {
                    String name = values.getAsString(UtilsDB.GOODS_INFO_NAME);
                    String price = values.getAsString(UtilsDB.GOODS_INFO_PRICE);
                    String type = values.getAsString(UtilsDB.GOODS_INFO_TYPE);
                    String introduce = values.getAsString(UtilsDB.GOODS_INFO_INTRODUCE);
                    String imgUrl = values.getAsString(UtilsDB.GOODS_INFO_URL);

                    ContentValues valuesSlot = new ContentValues();

                    for (Coil_info info : mList) {

                        valuesSlot.clear();

                        if ((name != null) && (name.length() > 0)) {
                            valuesSlot.put(UtilsDB.COIL_INFO_PAR_NAME, name);//名称
                        }

                        if ((price != null) && (price.length() > 0)) {
                            valuesSlot.put(UtilsDB.COIL_INFO_PAR_PRICE, price);//价格
                        }

                        if ((type != null) && (type.length() > 0)) {
                            valuesSlot.put(UtilsDB.COIL_INFO_TYPE, type);//种类
                        }

                        if ((introduce != null) && (introduce.length() > 0)) {
                            valuesSlot.put(UtilsDB.COIL_INFO_CONTENT, introduce);//说明
                        }

                        if ((imgUrl != null) && (imgUrl.length() > 0)) {
                            valuesSlot.put(UtilsDB.COIL_INFO_IMG_URL, imgUrl);//图片
                        }

                        if (valuesSlot.size() > 0) {
                            m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, valuesSlot,info.getCoil_id());
                        }
                    }
                }
            }

        } else if (tableType == TcnVendIF.DB_TABLE_KEY) {
            m_helper.updateData(UtilsDB.KEY_INFO_TABLE_NAME,values,id);
        }
        else {

        }
        queryAliveCoil(true);
        sendReceiveData(false,TcnDBDef.UPTE_DATA,tableType,m_listData_count,null);
    }

    public boolean OnUpdateData(String tableName,ContentValues values, int id) {
        boolean bRet = false;
        if ((null == m_helper) || (id < 0)) {
            TcnVendIF.getInstance().LoggerError(TAG, "OnUpdateData tableName: " + tableName+" id: "+id);
            return bRet;
        }
        return m_helper.updateData(tableName,values,id);
    }

    public boolean OnInsertData(String tableName,ContentValues values) {
        boolean bRet = false;
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "OnInsertData tableName: " + tableName);
            return bRet;
        }
       return m_helper.insertData(tableName,values);
    }

    public void OnDeleteData(int tableType,int id) {
        if ((null == m_helper) || (id < 0)) {
            TcnVendIF.getInstance().LoggerError(TAG, "deleteData tableType: " + tableType+" id: "+id);
            return;
        }
        if (tableType == TcnVendIF.DB_TABLE_SLOTNO) {
            m_helper.deleteSlotNo(UtilsDB.COIL_INFO_TABLE_NAME,id);
        } else if (tableType == TcnVendIF.DB_TABLE_GOODS) {
            m_helper.deleteData(UtilsDB.GOODS_INFO_TABLE_NAME,id);
        } else {

        }
        queryAliveCoil(true);
        sendReceiveData(false,TcnDBDef.DELETE_DATA,tableType,m_listData_count,null);
    }

    public boolean updateDataOtherInfo(int slotNo, String otherParam1, String otherParam2,boolean notify) {
        boolean bRet = false;
        if ((slotNo < 1) || (null == m_helper)) {
            return bRet;
        }
        ContentValues values = new ContentValues();
        if ((otherParam1 != null) && (otherParam1.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_OTHER_PARAM1, otherParam1);
        }

        if ((otherParam2 != null) && (otherParam2.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_OTHER_PARAM2, otherParam2);
        }

        bRet = m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNo);

        if (notify) {
            queryAliveCoil(true);
        }

        return bRet;
    }

    public boolean updateDataGoodsInfo(int slotNo, String imgGoodsUrl, String imgGoodsdetails,String name,String content, String spec, String type,String qrUrl,boolean notify) {
        boolean bRet = false;
        if ((slotNo < 1) || (null == m_helper)) {
            return bRet;
        }
        ContentValues values = new ContentValues();
        if ((imgGoodsUrl != null) && (imgGoodsUrl.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_IMG_URL, imgGoodsUrl);//商品图片
        }

        if ((imgGoodsdetails != null) && (imgGoodsdetails.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_DETAIL_URL, imgGoodsdetails);//商品详情图片
        }

        if ((name != null) && (name.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_PAR_NAME, name);//商品名称
        }

        if ((content != null) && (content.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_CONTENT, content);//商品说明
        }

        if ((spec != null) && (spec.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_GOODS_SPEC, spec);//商品规格
        }

        if ((type != null) && (type.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_TYPE, type);//商品类型
        }

        if ((qrUrl != null) && (qrUrl.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_QRPAY_URL, qrUrl);//每个货道的支付码
        }

        if (values.size() < 1) {
            return bRet;
        }

        bRet = m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNo);

        if (notify) {
            queryAliveCoil(true);
        }

        return bRet;
    }

    public boolean updateDataGoodsInfo(int slotNo, String imgGoodsUrl, String imgGoodsdetails,String name,String content, String spec, String type,boolean notify) {
        boolean bRet = false;
        if ((slotNo < 1) || (null == m_helper)) {
            return bRet;
        }
        ContentValues values = new ContentValues();
        if ((imgGoodsUrl != null) && (imgGoodsUrl.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_IMG_URL, imgGoodsUrl);//商品图片
        }

        if ((imgGoodsdetails != null) && (imgGoodsdetails.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_DETAIL_URL, imgGoodsdetails);//商品详情图片
        }

        if ((name != null) && (name.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_PAR_NAME, name);//商品名称
        }

        if ((content != null) && (content.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_CONTENT, content);//商品说明
        }

        if ((spec != null) && (spec.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_GOODS_SPEC, spec);//商品规格
        }

        if ((type != null) && (type.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_TYPE, type);//商品类型
        }

        if (values.size() < 1) {
            return bRet;
        }

        bRet = m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNo);

        if (notify) {
            queryAliveCoil(true);
        }

        return bRet;
    }

    public void updateDataGoodsInfo(int slotNo, String imgUrl, String name,String content, String spec, boolean notify) {
        if ((slotNo < 1) || (null == m_helper)) {
            return;
        }
        ContentValues values = new ContentValues();
        if ((imgUrl != null) && (imgUrl.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_IMG_URL, imgUrl);//图片
        }

        if ((name != null) && (name.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_PAR_NAME, name);//商品名称
        }

        if ((content != null) && (content.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_CONTENT, content);//商品说明
        }

        if ((spec != null) && (spec.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_GOODS_SPEC, spec);//商品规格
        }
        if (values.size() < 1) {
            return;
        }
        m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNo);

        if (notify) {
            queryAliveCoil(true);
        }
    }

    public boolean updateDataGoodsInfo(int slotNo, int capacity, int stock, String price, String salePrice,String code,String imgGoodsUrl, String imgGoodsdetails,String name,String content, String spec,
                                       String type,int status,boolean notify) {
        boolean bRet = false;
        if ((slotNo < 1) || (null == m_helper)) {
            return bRet;
        }
        ContentValues values = new ContentValues();

        if (capacity > -1) {
            values.put(UtilsDB.COIL_INFO_CAPACITY, capacity);//商品容量
        }

        if (stock > -1) {
            values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, stock);//商品库存
        }

        if ((price != null) && (price.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_PAR_PRICE, price);//商品价格
        }

        if ((salePrice != null) && (salePrice.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_SALE_PRICE, salePrice);//商品会员价格
        }

        if ((code != null) && (code.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_GOODS_CODE, code);//商品价格
        }

        if ((imgGoodsUrl != null) && (imgGoodsUrl.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_IMG_URL, imgGoodsUrl);//商品图片
        }

        if ((imgGoodsdetails != null) && (imgGoodsdetails.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_DETAIL_URL, imgGoodsdetails);//商品详情图片
        }

        if ((name != null) && (name.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_PAR_NAME, name);//商品名称
        }

        if ((content != null) && (content.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_CONTENT, content);//商品说明
        }

        if ((spec != null) && (spec.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_GOODS_SPEC, spec);//商品规格
        }

        if ((type != null) && (type.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_TYPE, type);//商品类型
        }

        if ((status > -1) && (status <= 255)) {
            values.put(UtilsDB.COIL_INFO_WORK_STATUS, status);//货道状态
        }

        if (values.size() < 1) {
            return bRet;
        }
        bRet = m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNo);

        if (notify) {
            queryAliveCoil(true);
        }

        return bRet;
    }

    public boolean updateDataGoodsInfo(int slotNo, int capacity,int stock, String price,String code, String imgUrl, String name,String content, String spec, boolean notify,boolean needUpToServer) {
        boolean bRet = false;
        if ((slotNo < 1) || (null == m_helper)) {
            return bRet;
        }
        ContentValues values = new ContentValues();

        if (capacity >= 0) {
            values.put(UtilsDB.COIL_INFO_CAPACITY, capacity);//货道容量
        }

        if (stock >= 0) {
            values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, stock);//货道现存
        }

        if (TcnUtility.isDigital(price) || TcnUtility.isContainDeciPoint(price)) {
            values.put(UtilsDB.COIL_INFO_PAR_PRICE, price);//货道价格
        }

        if ((code != null) && (code.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_GOODS_CODE, code);//商品编码
        }

        if ((imgUrl != null) && (imgUrl.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_IMG_URL, imgUrl);//图片
        }

        if ((name != null) && (name.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_PAR_NAME, name);//商品名称
        }

        if ((content != null) && (content.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_CONTENT, content);//商品说明
        }

        if ((spec != null) && (spec.length() > 0)) {
            values.put(UtilsDB.COIL_INFO_GOODS_SPEC, spec);//商品规格
        }
        if (values.size() < 1) {
            return bRet;
        }
        bRet = m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNo);

        if (notify) {
            OnQueryAliveCoil(needUpToServer);
        }

        return bRet;
    }


    public boolean updateDataGoodsInfo(int slotNoOrKey, int capacity,int stock, String price,String code, String imgUrl, String name,String content, String spec, boolean needUpToServer) {
        boolean bRet = false;
        if ((slotNoOrKey < 1) || (null == m_helper)) {
            return bRet;
        }

        if (m_bHaveKeyMap) {
            if (slotNoOrKey > 100) {
                ContentValues values = new ContentValues();

                if (capacity >= 0) {
                    values.put(UtilsDB.COIL_INFO_CAPACITY, capacity);//货道容量
                }

                if (stock >= 0) {
                    values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, stock);//货道现存
                }

                if (TcnUtility.isDigital(price) || TcnUtility.isContainDeciPoint(price)) {
                    values.put(UtilsDB.COIL_INFO_PAR_PRICE, price);//货道价格
                }

                if ((code != null) && (code.length() > 0)) {
                    values.put(UtilsDB.COIL_INFO_GOODS_CODE, code);//商品编码
                }

                if ((imgUrl != null) && (imgUrl.length() > 0)) {
                    values.put(UtilsDB.COIL_INFO_IMG_URL, imgUrl);//图片
                }

                if ((name != null) && (name.length() > 0)) {
                    values.put(UtilsDB.COIL_INFO_PAR_NAME, name);//商品名称
                }

                if ((content != null) && (content.length() > 0)) {
                    values.put(UtilsDB.COIL_INFO_CONTENT, content);//商品说明
                }

                if ((spec != null) && (spec.length() > 0)) {
                    values.put(UtilsDB.COIL_INFO_GOODS_SPEC, spec);//商品规格
                }
                if (values.size() > 0) {
                    bRet = m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNoOrKey);
                }

            } else {
                List<Coil_info> mSList = m_helper.queryCoilsInfo(slotNoOrKey);
                if ((mSList != null) && (mSList.size() > 0)) {
                    for (Coil_info info:mSList) {
                        ContentValues values = new ContentValues();

                        if (capacity >= 0) {
                            values.put(UtilsDB.COIL_INFO_CAPACITY, capacity);//货道容量
                        }

                        if (stock >= 0) {
                            values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, stock);//货道现存
                        }

                        if (TcnUtility.isDigital(price) || TcnUtility.isContainDeciPoint(price)) {
                            values.put(UtilsDB.COIL_INFO_PAR_PRICE, price);//货道价格
                        }

                        if ((code != null) && (code.length() > 0)) {
                            values.put(UtilsDB.COIL_INFO_GOODS_CODE, code);//商品编码
                        }

                        if ((imgUrl != null) && (imgUrl.length() > 0)) {
                            values.put(UtilsDB.COIL_INFO_IMG_URL, imgUrl);//图片
                        }

                        if ((name != null) && (name.length() > 0)) {
                            values.put(UtilsDB.COIL_INFO_PAR_NAME, name);//商品名称
                        }

                        if ((content != null) && (content.length() > 0)) {
                            values.put(UtilsDB.COIL_INFO_CONTENT, content);//商品说明
                        }

                        if ((spec != null) && (spec.length() > 0)) {
                            values.put(UtilsDB.COIL_INFO_GOODS_SPEC, spec);//商品规格
                        }
                        if (values.size() > 0) {
                            bRet = m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,info.getCoil_id());
                        }

                    }
                }
            }
        } else {
            ContentValues values = new ContentValues();

            if (capacity >= 0) {
                values.put(UtilsDB.COIL_INFO_CAPACITY, capacity);//货道容量
            }

            if (stock >= 0) {
                values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, stock);//货道现存
            }

            if (TcnUtility.isDigital(price) || TcnUtility.isContainDeciPoint(price)) {
                values.put(UtilsDB.COIL_INFO_PAR_PRICE, price);//货道价格
            }

            if ((code != null) && (code.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_GOODS_CODE, code);//商品编码
            }

            if ((imgUrl != null) && (imgUrl.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_IMG_URL, imgUrl);//图片
            }

            if ((name != null) && (name.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_PAR_NAME, name);//商品名称
            }

            if ((content != null) && (content.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_CONTENT, content);//商品说明
            }

            if ((spec != null) && (spec.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_GOODS_SPEC, spec);//商品规格
            }
            if (values.size() > 0) {
                bRet = m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNoOrKey);
            }

        }

        OnQueryAliveCoil(needUpToServer);

        return bRet;
    }

    public List<Coil_info> queryCoilListFromKey(int key) {
        List<Coil_info> mSList = m_helper.queryCoilsInfo(key);
        return mSList;
    }


    public void updateDataGoodsContent(int slotNo, String content) {
        if ((slotNo < 1) || (null == m_helper)) {
            return;
        }
        if ((content != null) && (content.length() > 0)) {
            ContentValues values = new ContentValues();
            values.put(UtilsDB.COIL_INFO_CONTENT, content);//商品说明
            m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,slotNo);
            queryAliveCoil(true);
        }
    }

    public boolean isExtantQuantityEmpty(Coil_info info) {
        boolean bRet = false;
        if (null == info) {
            return bRet;
        }
        if (info.getExtant_quantity() <= 0) {
            bRet = true;
        }
        return bRet;
    }

    public boolean isFaultSlotNo(Coil_info info) {
        boolean bRet = false;
        if (null == info) {
            return bRet;
        }
        if (info.getWork_status() > 0) {
            bRet = true;
        }
        return bRet;
    }

    public boolean isValidSlotNo(Coil_info info) {
        boolean bRet = true;
        if (null == info) {
            return bRet;
        }
        if (info.getSlotStatus() > UtilsDB.SLOT_STATE_FAULT) {
            bRet = false;
        } else if ((info.getCoil_id() >= 1000) || (info.getCoil_id() <= 0)) {
            bRet = false;
        } else {

        }
        return bRet;
    }

    public boolean isValid(int slotNo, String price) {
        boolean bRet = false;

        if (slotNo < 0) {
            return bRet;
        }

        Coil_info info = getCoilInfo(slotNo);
        if (null == info) {
            return bRet;
        }

        if ((!TcnUtility.isDigital(price)) && (!TcnUtility.isContainDeciPoint(price))) {
            return bRet;
        }

        if ((info.getCoil_id() != slotNo) || (Double.valueOf(price).doubleValue() != Double.valueOf(info.getPar_price()).doubleValue())) {
            return bRet;
        }

        bRet = true;

        return bRet;
    }

    private boolean isVaildSelectSlotNo(Coil_info sInfo) {
        boolean bRet = false;
        if (null == sInfo) {
            return bRet;
        }
        if ((sInfo.getCoil_id() > 0) && (0 == sInfo.getWork_status()) && (sInfo.getExtant_quantity() > 0)
                && (sInfo.getSlotStatus() < UtilsDB.SLOT_STATE_DISABLE)) {
            bRet = true;
        }
        return bRet;
    }
    public Coil_info getSelectSlotNo(int slotNo) {

        m_selectCoilInfo = getCoilInfo(slotNo);

        return m_selectCoilInfo;
    }

    public Coil_info getSelectSlotNo() {
        return m_selectCoilInfo;
    }

    public Coil_info getSelectGoods(boolean isShowByCode,boolean isShipByOrder,int position) {
        m_selectCoilInfo = null;
        if (position < 0) {
            TcnVendIF.getInstance().LoggerError(TAG, "getSelectGoods() Invalid position");
            return m_selectCoilInfo;
        }
        if (isShowByCode) {
            TcnVendIF.getInstance().LoggerDebug(TAG, "getSelectGoods() size: "+m_list_aliveGoods.size()+" position: "+position);
            if (m_list_aliveGoods.size() > position) {
                m_selectCoilInfo = getShipGoods(isShipByOrder,m_list_aliveGoods.get(position));
            }
            return m_selectCoilInfo;
        }
        if (m_strCurrentGoodsType.equals(TcnVendIF.GOODS_TYPE_ALL)) {
            if (m_bHaveKeyMap) {
                if (m_bIsMainBoardControl) {
                    m_selectCoilInfo = m_list_aliveCoil.get(position);
                } else {
                    if ((m_list_count_slot_key != null) && (m_list_count_slot_key.size() > 0)) {
                        int keyOrSlotNo = m_list_count_slot_key.get(position);
                        if (keyOrSlotNo < 100) {
                            m_selectCoilInfo = getAliveInfoFromKey(keyOrSlotNo);
                        } else {
                            m_selectCoilInfo = getCoilInfo(keyOrSlotNo);
                        }
                    }
                }

            } else {
                m_selectCoilInfo = m_list_aliveCoil.get(position);
            }
        } else if (m_strCurrentGoodsType.equals(TcnVendIF.GOODS_TYPE_OTHER)) {
            m_selectCoilInfo = m_list_aliveCoilExcept.get(position);
        } else {
            List<Coil_info> mList = getAliveCoilFromType(m_strCurrentGoodsType);
            if (mList != null) {
                m_selectCoilInfo = mList.get(position);
            }
        }
        return m_selectCoilInfo;

    }

    public Coil_info getShipGoodsSlotNo(boolean isShowByCode,boolean isShipByOrder, int slotNo) {
        Coil_info sInfo = null;
        if (m_bHaveKeyMap) {
            if (slotNo < 100) {
                sInfo = getAliveInfoFromKey(slotNo);
            } else {
                sInfo = getCoilInfo(slotNo);
            }
        } else {
            sInfo = getCoilInfo(slotNo);
        }
        if (!isShowByCode) {
            return sInfo;
        }
        if ((null == sInfo) || (sInfo.getCoil_id() < 1) || (sInfo.getGoodsCode() == null)
                || (sInfo.getGoodsCode().length() < 1)) {
            return sInfo;
        }
        Goods_info goodsInfo = m_helper.queryGoodsInfo(sInfo.getGoodsCode());
        int lastSlotNo = goodsInfo.getSlotShipLast();
        String goodsSlotMap = goodsInfo.getGoodsSlotMap();
        TcnVendIF.getInstance().LoggerDebug(TAG, "getShipGoodsSlotNo() lastSlotNo: "+lastSlotNo+" goodsSlotMap: "+goodsSlotMap);

        List<Integer> mList = getGoodsMapSlotNoSortList(goodsSlotMap);

        if (mList.size() < 1) {
            return sInfo;
        }

        if (lastSlotNo < 1) {
            sInfo = m_helper.queryCoilInfo(mList.get(0));
            if ((isVaildSelectSlotNo(sInfo))) {
                return sInfo;
            }
        }
        boolean bIsHasEqualSlotNo = false;
        if (isShipByOrder) {
            //寻找下一个有效的货道
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).intValue() == lastSlotNo) {
                    bIsHasEqualSlotNo = true;
                    if ((mList.size() - 1) == i) {
                        sInfo = m_helper.queryCoilInfo(mList.get(0));
                        if ((isVaildSelectSlotNo(sInfo))) {
                            break;
                        }
                    } else {
                        sInfo = m_helper.queryCoilInfo(mList.get(i + 1));
                        if ((isVaildSelectSlotNo(sInfo))) {
                            break;
                        }
                    }
                } else {
                    if (bIsHasEqualSlotNo) {
                        sInfo = m_helper.queryCoilInfo(mList.get(i));
                        if ((isVaildSelectSlotNo(sInfo))) {
                            break;
                        }
                    }
                }
            }
        }

        if (!bIsHasEqualSlotNo) {
            for (int i = 0; i < mList.size(); i++) {
                sInfo = m_helper.queryCoilInfo(mList.get(i));
                if ((isVaildSelectSlotNo(sInfo))) {
                    break;
                }
            }
        } else {
            if (!(isVaildSelectSlotNo(sInfo))) {
                for (int i = 0; i < mList.size(); i++) {
                    sInfo = m_helper.queryCoilInfo(mList.get(i));
                    if ((isVaildSelectSlotNo(sInfo))) {
                        break;
                    }
                }
            }
        }

        if ((null == sInfo) || (sInfo.getCoil_id() < 1)) {
            return sInfo;
        }

        return sInfo;
    }

    public Coil_info getShipGoods(boolean isShipByOrder, Goods_info goodsInfo) {
        Coil_info sInfo = null;
        if ((null == goodsInfo) || (goodsInfo.getGoodsSlotMap()).length() < 1) {
            return sInfo;
        }
        int lastSlotNo = goodsInfo.getSlotShipLast();
        String goodsSlotMap = goodsInfo.getGoodsSlotMap();
        TcnVendIF.getInstance().LoggerDebug(TAG, "getShipGoods() lastSlotNo: "+lastSlotNo+" goodsSlotMap: "+goodsSlotMap);

        List<Integer> mList = getGoodsMapSlotNoSortList(goodsSlotMap);

        if (mList.size() < 1) {
            return sInfo;
        }

        if (lastSlotNo < 1) {
            sInfo = m_helper.queryCoilInfo(mList.get(0));
            if ((isVaildSelectSlotNo(sInfo))) {
                return sInfo;
            }
        }

        boolean bIsHasEqualSlotNo = false;
        if (isShipByOrder) {
            //寻找下一个有效的货道
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).intValue() == lastSlotNo) {
                    bIsHasEqualSlotNo = true;
                    if ((mList.size() - 1) == i) {
                        sInfo = m_helper.queryCoilInfo(mList.get(0));
                        if ((isVaildSelectSlotNo(sInfo))) {
                            break;
                        }
                    } else {
                        sInfo = m_helper.queryCoilInfo(mList.get(i + 1));
                        if ((isVaildSelectSlotNo(sInfo))) {
                            break;
                        }
                    }
                } else {
                    if (bIsHasEqualSlotNo) {
                        sInfo = m_helper.queryCoilInfo(mList.get(i));
                        if ((isVaildSelectSlotNo(sInfo))) {
                            break;
                        }
                    }
                }
            }
        }

        if (!bIsHasEqualSlotNo) {
            for (int i = 0; i < mList.size(); i++) {
                sInfo = m_helper.queryCoilInfo(mList.get(i));
                if ((isVaildSelectSlotNo(sInfo))) {
                    break;
                }
            }
        }

        if (!(isVaildSelectSlotNo(sInfo))) {
            sInfo = m_helper.queryCoilInfo(mList.get(0));
        }

        return sInfo;
    }

    public boolean isSlotNoExist(int slotNo) {
        boolean bRet = false;
        if (null == m_helper) {
            return bRet;
        }
        Coil_info info = m_helper.queryCoilInfo(slotNo);
        if ((info.getCoil_id() == slotNo) && (info.getWork_status() != 255)) {
            bRet = true;
        }
        return bRet;
    }


    public void OnUpdateSlotNoInfo(int startslotNo,int endSlotNo, String price, String name,String type, String content,
                                 String imgUrl,String spec, String goodsCode, int extantQuantity, int capacity) {

        if ((startslotNo < 1) || ((endSlotNo < startslotNo))) {
            return;
        }
        boolean bHasVaild = false;
        ContentValues values = new ContentValues();
        for (int i = startslotNo; i <= endSlotNo ; i++) {

            if (!isSlotNoExist(i)) {
                continue;
            }
            bHasVaild = true;
            values.clear();

            if ((price != null) && (price.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_PAR_PRICE, price);//价格
            }
            if ((name != null) && (name.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_PAR_NAME, name);//名称
            }
            if ((type != null) && (type.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_TYPE, type);//种类
            }
            if ((content != null) && (content.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_CONTENT, content);//说明
            }
            if ((imgUrl != null) && (imgUrl.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_IMG_URL, imgUrl);//图片
            }
            if ((spec != null) && (spec.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_GOODS_SPEC, spec);//规格
            }
            if ((goodsCode != null) && (goodsCode.length() > 0)) {
                values.put(UtilsDB.COIL_INFO_GOODS_CODE, goodsCode);//商品编码
            }
            if (extantQuantity > -1) {
                values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, extantQuantity);//现存数量
            }
            if (capacity > -1) {
                values.put(UtilsDB.COIL_INFO_CAPACITY, capacity);//容量
            }
            if(values.size() > 0) {
                Coil_info coil = m_helper.queryCoilInfo(i);
                if(coil.getCoil_id() > 0){
                    m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values,i);
                }
            }
        }
        if (bHasVaild) {
            queryAliveCoil(true);
        }
    }

    public void OnInsertKey(int keyNum) {
        if (keyNum < 1) {
            return;
        }
        Key_info kInfo = m_helper.queryKeyInfo(keyNum);
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnSelectedKey keyNum: "+keyNum+" getKeyNum: "+kInfo.getKeyNum());
        if ((null == kInfo) || (kInfo.getKeyNum() < 1)) {
            ContentValues values = new ContentValues();
            values.put(UtilsDB.KEY_INFO_KEY_ID, keyNum);
            values.put(UtilsDB.KEY_INFO_WORK_STATUS, UtilsDB.KEY_STATE_HAVE_GOODS);
            m_helper.insertData(UtilsDB.KEY_INFO_TABLE_NAME, values);
        } else {
            if (kInfo.getKeyNum() == keyNum) {
                //do nothing
            } else {
                ContentValues values = new ContentValues();
                values.put(UtilsDB.KEY_INFO_KEY_ID, keyNum);
                values.put(UtilsDB.KEY_INFO_WORK_STATUS, UtilsDB.KEY_STATE_HAVE_GOODS);
                m_helper.insertData(UtilsDB.KEY_INFO_TABLE_NAME, values);
            }
        }
    }

    //驱动板上报过来的数据
    public void OnUploadSlotNoInfo(boolean finish, int slotNo, int status) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnUploadSlotNoInfo slotNo: "+slotNo+" status: "+status+" finish: "+finish);

        if (slotNo < 1) {
            if (finish) {
                m_bIsLoadSuccess = true;
                queryAliveCoil(true);
            }
            return;
        }

        Coil_info info = m_helper.queryCoilInfo(slotNo);
        ContentValues values = new ContentValues();
        if (info != null) {
            if (info.getCoil_id() == slotNo) {
                int iSlotStatus = info.getSlotStatus();
                if (iSlotStatus >= UtilsDB.SLOT_STATE_DISABLE) {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, iSlotStatus);
                } else if (status != 0) {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_FAULT);
                } else if (info.getExtant_quantity() > 0) {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
                } else if (info.getExtant_quantity() < 1) {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_NO_GOODS);
                } else {

                }
                values.put(UtilsDB.COIL_INFO_WORK_STATUS, status);
                m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values, slotNo);
            } else {
                values.put(UtilsDB.COIL_INFO_COIL_ID, slotNo);
                values.put(UtilsDB.COIL_INFO_PAR_NAME, m_context.getString(R.string.tcn_log));
                values.put(UtilsDB.COIL_INFO_WORK_STATUS, status);
                values.put(UtilsDB.COIL_INFO_SLOT_STATUS, 0);
                values.put(UtilsDB.COIL_INFO_GOODS_CODE, String.valueOf(slotNo));
                values.put(UtilsDB.COIL_INFO_PAR_PRICE, DEFAULT_COIL_PRICE);
                values.put(UtilsDB.COIL_INFO_SALE_PRICE, DEFAULT_COIL_PRICE);
                values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, DEFAULT_COIL_CAPACITY);
                values.put(UtilsDB.COIL_INFO_CAPACITY, DEFAULT_COIL_CAPACITY);
                m_helper.insertData(UtilsDB.COIL_INFO_TABLE_NAME, values);
            }
        } else {
            values.put(UtilsDB.COIL_INFO_COIL_ID, slotNo);
            values.put(UtilsDB.COIL_INFO_PAR_NAME, m_context.getString(R.string.tcn_log));
            values.put(UtilsDB.COIL_INFO_WORK_STATUS, status);
            values.put(UtilsDB.COIL_INFO_SLOT_STATUS, 0);
            values.put(UtilsDB.COIL_INFO_GOODS_CODE, String.valueOf(slotNo));
            values.put(UtilsDB.COIL_INFO_PAR_PRICE, DEFAULT_COIL_PRICE);
            values.put(UtilsDB.COIL_INFO_SALE_PRICE, DEFAULT_COIL_PRICE);
            values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, DEFAULT_COIL_CAPACITY);
            values.put(UtilsDB.COIL_INFO_CAPACITY, DEFAULT_COIL_CAPACITY);
            m_helper.insertData(UtilsDB.COIL_INFO_TABLE_NAME, values);
        }

        if ((slotNo > 0) && (status < 255)) {
            if (slotNo > m_iMaxSlotNo) {
                m_iMaxSlotNo = slotNo;
            }
        }

        if (finish) {
            m_bIsLoadSuccess = true;
            queryAliveCoil(true);
        }

    }

    //驱动板上报过来的数据
    public void OnUploadSlotNoInfoWithStock(boolean finish, int slotNo, int stock) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnUploadSlotNoInfoWithStock slotNo: "+slotNo+" stock: "+stock+" finish: "+finish);

        if (slotNo < 1) {
            if (finish) {
                m_bIsLoadSuccess = true;
                queryAliveCoil(true);
            }
            return;
        }

        Coil_info info = m_helper.queryCoilInfo(slotNo);
        ContentValues values = new ContentValues();
        if (info != null) {
            if (info.getCoil_id() == slotNo) {
                int iSlotStatus = info.getSlotStatus();
                if (iSlotStatus >= UtilsDB.SLOT_STATE_DISABLE) {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, iSlotStatus);
                } else if ((info.getWork_status()) != 0) {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_FAULT);
                } else if (stock > 0) {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
                } else if (stock < 1) {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_NO_GOODS);
                } else {

                }
                values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, stock);
                m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values, slotNo);
            } else {
                values.put(UtilsDB.COIL_INFO_COIL_ID, slotNo);
                values.put(UtilsDB.COIL_INFO_PAR_NAME, m_context.getString(R.string.tcn_log));
                values.put(UtilsDB.COIL_INFO_WORK_STATUS, 0);
                if (stock > 0) {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
                } else {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_NO_GOODS);
                }
                values.put(UtilsDB.COIL_INFO_GOODS_CODE, String.valueOf(slotNo));
                values.put(UtilsDB.COIL_INFO_PAR_PRICE, DEFAULT_COIL_PRICE);
                values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, stock);
                values.put(UtilsDB.COIL_INFO_CAPACITY, DEFAULT_COIL_CAPACITY);
                m_helper.insertData(UtilsDB.COIL_INFO_TABLE_NAME, values);
            }
        } else {
            values.put(UtilsDB.COIL_INFO_COIL_ID, slotNo);
            values.put(UtilsDB.COIL_INFO_PAR_NAME, m_context.getString(R.string.tcn_log));
            values.put(UtilsDB.COIL_INFO_WORK_STATUS, 0);
            if (stock > 0) {
                values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
            } else {
                values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_NO_GOODS);
            }
            values.put(UtilsDB.COIL_INFO_GOODS_CODE, String.valueOf(slotNo));
            values.put(UtilsDB.COIL_INFO_PAR_PRICE, DEFAULT_COIL_PRICE);
            values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, stock);
            values.put(UtilsDB.COIL_INFO_CAPACITY, DEFAULT_COIL_CAPACITY);
            m_helper.insertData(UtilsDB.COIL_INFO_TABLE_NAME, values);
        }

        if (slotNo > 0) {
            if (slotNo > m_iMaxSlotNo) {
                m_iMaxSlotNo = slotNo;
            }
        }

        if (finish) {
            m_bIsLoadSuccess = true;
            queryAliveCoil(true);
        }

    }

    public void OnUploadSlotNoInfoSingle(boolean finish, int slotNo, int status) {
        if ((slotNo < 1) || (status < 1)) {
            return;
        }
        Coil_info info = m_helper.queryCoilInfo(slotNo);
        if (info != null) {
            if (info.getCoil_id() == slotNo) {
                ContentValues values = new ContentValues();
                values.put(UtilsDB.COIL_INFO_WORK_STATUS, status);
                m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values, slotNo);
                queryAliveCoil(false);
            }
        }
    }

    public Coil_info OnQueryCoilInfoAvailableNotFault(String goodCode) {
        Coil_info info = null;
        if (m_helper != null) {
            info = m_helper.queryCoilInfoAvailableNotFault(goodCode);
        }
        return info;
    }

    public boolean OnShipSuccess(int slotNo) {
        boolean bRet = false;
        Coil_info info = getCoilInfo(slotNo);
        if (null == info) {
            return bRet;
        }
        int mQuantity = info.getExtant_quantity();
        if ((mQuantity >= 1) && (mQuantity != 199)) {
            mQuantity = mQuantity - 1;
            ContentValues values = new ContentValues();
            if (info.getSlotStatus() > UtilsDB.SLOT_STATE_FAULT) {
                //
            } else {
                if (mQuantity > 0) {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
                } else {
                    values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_NO_GOODS);
                }
            }
            values.put(UtilsDB.COIL_INFO_EXTANT_QUANTITY, mQuantity);
            m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values, slotNo);

            OnUpdateGoodsLastShipSlotNo(info);

            OnQueryAliveCoil(false,slotNo);
            bRet = true;
        } else {
            OnUpdateGoodsLastShipSlotNo(info);
            OnQueryAliveCoil(false);
        }

        return bRet;
    }

    public void OnShipFail(int slotNo) {
        Coil_info info = getCoilInfo(slotNo);
        if (null == info) {
            return;
        }
        OnUpdateGoodsLastShipSlotNo(info);
        if (m_bShowByGoodsCode) {
            queryAliveCoil(false);
        }
    }

    public List<Integer>  getSameSlotKeyList(int slotNo, TreeMap<Integer, ArrayList<Integer>> keyMapSlotNo) {
        List<Integer>  mListKey = new ArrayList<Integer>();

        Set<Integer> keySet = keyMapSlotNo.keySet();//先获取map集合的所有键的Set集合   keySet：按键集合
        for (Integer key:keySet) {
            ArrayList<Integer> mList = keyMapSlotNo.get(key);   //按键对应的slotNo集合
            int size = mList.size();
            for (int i = 0; i < size; i++) {
                if (mList.get(i) == slotNo) {
                    mListKey.add(key);
                    break;
                }
            }
        }
        return mListKey;
    }

    public List<Integer> getSameSlotKeyList(int slotNo) {
        List<Integer>  mListKey = new ArrayList<Integer>();
        if ((null == m_list_aliveKeyAll) || (m_list_aliveKeyAll.size() < 1)) {
            return mListKey;
        }
        for (Key_info kInfo:m_list_aliveKeyAll) {
            if (isKeyContainsSlotNo(kInfo,slotNo)) {
                if (!mListKey.contains(Integer.valueOf(kInfo.getKeyNum()))) {
                    mListKey.add(Integer.valueOf(kInfo.getKeyNum()));
                }
            }
        }
        return mListKey;
    }

    public boolean isKeyContainsSlotNo(Key_info kInfo, int slotNo) {
        boolean bRet = false;
        if ((null == kInfo) || (kInfo.getKeyNum() < 1)) {
            return bRet;
        }
        String slotNos = kInfo.getCoils();
        if (slotNos.contains("~")) {
            String[] strArr = slotNos.split("\\~");
            if ((null == strArr) || (strArr.length < 1)) {
                return bRet;
            }
            for (String data:strArr) {
                if (isDigital(data)) {
                    if (Integer.valueOf(data).intValue() == slotNo) {
                        bRet = true;
                        break;
                    }
                }
            }
        } else {
            if (isDigital(slotNos)) {
                if (Integer.valueOf(slotNos).intValue() == slotNo) {
                    bRet = true;
                }
            }
        }
        return bRet;
    }

    public void OnUpdatePriceData(int coil_id, String price) {
        TcnVendIF.getInstance().LoggerError(TAG, "OnUpdatePriceData coil_id: " + coil_id + " price: " + price);
        Coil_info info = m_helper.queryCoilInfo(coil_id);
        if (info != null) {
            ContentValues values = new ContentValues();
            values.put(UtilsDB.COIL_INFO_PAR_PRICE, price);
            m_helper.updateData(UtilsDB.COIL_INFO_TABLE_NAME, values, coil_id);
        }
    }

    private ContentValues getAndModifySlotStatusValues(int slotNo) {
        if ((slotNo < 1) || (slotNo > 999) || (null == m_helper)) {
            return null;
        }
        Coil_info info = m_helper.queryCoilInfo(slotNo);
        if ((null == info) || (info.getCoil_id() < 1)) {
            return null;
        }
        ContentValues values = new ContentValues();
        if (info.getWork_status() == 0) {
            if (info.getExtant_quantity() > 0) {
                values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
            } else {
                values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_NO_GOODS);
            }
        } else if (info.getWork_status() == 255) {
            values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HIDE);
        } else {
            values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_FAULT);
        }
        return values;
    }

    public ContentValues getAndModifySlotStatusValues(int slotNo, int work_status) {
        if ((slotNo < 1) || (slotNo > 999) || (null == m_helper)) {
            return null;
        }
        Coil_info info = m_helper.queryCoilInfo(slotNo);
        if ((null == info) || (info.getCoil_id() < 1)) {
            return null;
        }
        ContentValues values = new ContentValues();
        if (work_status == 0) {
            if (info.getExtant_quantity() > 0) {
                values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HAVE_GOODS);
            } else {
                values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_NO_GOODS);
            }
        } else if (work_status == 255) {
            values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_HIDE);
        } else {
            values.put(UtilsDB.COIL_INFO_SLOT_STATUS, UtilsDB.SLOT_STATE_FAULT);
        }
        return values;
    }


    public List<Coil_info> getCoilListFromKey(int keyNumber) {
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "getCoilListFromKey m_helper null");
            return null;
        }
        List<Coil_info>  mListAliveCoil = new ArrayList<Coil_info>();
        if ((null == m_mapKeySlotNo) || (m_mapKeySlotNo.size() < 1)) {
            return m_helper.queryKeyMapCoilList(keyNumber);
        }
        List<Coil_info> coil_list = new ArrayList<Coil_info>();
        Set<Integer> keySet = m_mapKeySlotNo.keySet();//先获取map集合的所有键的Set集合  keynum 集合
        for (Integer key:keySet) {
            if (key.intValue() == keyNumber) {
                ArrayList<Integer> mList = m_mapKeySlotNo.get(key);
                int size = mList.size();
                for (int i = 0; i < size; i++) {
                    Coil_info info = m_helper.queryCoilInfo(mList.get(i));
                    coil_list.add(info);
                }
                break;
            }

        }
        return coil_list;
    }

    public Coil_info getCoilInfoLastestByCode(String code) {
        Coil_info info = m_helper.queryCoilInfoAvailable(code);
        return info;
    }

    public Coil_info getCoilInfoLastestByType(String type) {
        Coil_info info = m_helper.queryCoilInfoAvailableByType(type);
        return info;
    }

    public Coil_info getCoilInfoLastestByType(String type,String selectDbInfo,int startRangeValue) {
        Coil_info info = m_helper.queryCoilInfoAvailableByType(type,selectDbInfo,startRangeValue);
        return info;
    }

    public List<AdvertInfo> queryAdvert() {
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "queryAdvert m_helper null");
            return null;
        }
        return m_helper.queryAdvert();
    }

    public List<AdvertInfo> queryAdvertAll() {
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "queryAdvertAll m_helper null");
            return null;
        }
        return m_helper.queryAdvertAll();
    }

    public AdvertInfo queryAdvert(String mAdId) {
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "queryAdvert 2 m_helper null");
            return null;
        }
        return m_helper.queryAdvert(mAdId);
    }

    public AdvertInfo queryAdvertByFileName(String fileName) {
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "queryAdvertByFileNamem_helper null");
            return null;
        }
        return m_helper.queryAdvertByFileName(fileName);
    }

    public void updateAdvert(String machingID, String mAdId,String adDownload,String strType,String strFile,String playAdTime,String AdUrl) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "updateAdvert machingID: "+machingID+" mAdId: "+mAdId+" adDownload: "+adDownload
                +" strType: "+strType+" strFile: "+strFile+" playAdTime: "+playAdTime+" AdUrl: "+AdUrl);
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "updateAdvert m_helper null");
            return;
        }
        ContentValues values = new ContentValues();
        values.put(UtilsDB.ADVERT_ADID, mAdId);
        values.put(UtilsDB.ADVERT_MACHINEID, machingID);
        values.put(UtilsDB.ADVERT_NAME, strFile);
        values.put(UtilsDB.ADVERT_ADRDOWNLOAD, adDownload);
        values.put(UtilsDB.ADVERT_RSTIME, playAdTime);
        values.put(UtilsDB.ADVERT_URL, AdUrl);
        values.put(UtilsDB.ADVERT_PLAYTYPE, strType);

        AdvertInfo mAdInfo = m_helper.queryAdvert(mAdId);
        if (mAdInfo != null) {
            if (mAdId.equals(mAdInfo.getAdId())) {
                m_helper.updateAdvert(UtilsDB.ADVERT_TABLE_NAME, values, mAdId);
            } else {
                m_helper.insertData(UtilsDB.ADVERT_TABLE_NAME, values);
            }
        } else {
            m_helper.insertData(UtilsDB.ADVERT_TABLE_NAME, values);
        }
    }

    public void updateAdvertByName(String machingID,String actId,String adDownload,String strType,String name,String url,String localUrl) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "updateAdvertByName machingID: "+machingID+" adDownload: "+adDownload
                +" strType: "+strType+" name: "+name+" url: "+url+" localUrl: "+localUrl+" actId: "+actId);
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "updateAdvert m_helper null");
            return;
        }
        if ((null == name) || (name.length() < 1)) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(UtilsDB.ADVERT_MACHINEID, machingID);
        values.put(UtilsDB.ADVERT_ACTIVITY_ID, actId);
        values.put(UtilsDB.ADVERT_ADRDOWNLOAD, adDownload);
        values.put(UtilsDB.ADVERT_PLAYTYPE, strType);
        values.put(UtilsDB.ADVERT_NAME, name);
        values.put(UtilsDB.ADVERT_URL, url);
        values.put(UtilsDB.ADVERT_URL, localUrl);


        AdvertInfo mAdInfo = m_helper.queryAdvertByFileName(name);
        if ((mAdInfo != null) && (name != null)) {
            if (name.equals(mAdInfo.getAdName())) {
                m_helper.updateAdvertByName(UtilsDB.ADVERT_TABLE_NAME, values, name);
            } else {
                m_helper.insertData(UtilsDB.ADVERT_TABLE_NAME, values);
            }
        } else {
            m_helper.insertData(UtilsDB.ADVERT_TABLE_NAME, values);
        }
    }

    public void updateAdvertByName(String name,int adDownload) {
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "updateAdvertByName m_helper null");
            return;
        }
        if ((null == name) || (name.length() < 1)) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(UtilsDB.ADVERT_ADRDOWNLOAD, String.valueOf(adDownload));
        AdvertInfo mAdInfo = m_helper.queryAdvertByFileName(name);
        if ((mAdInfo != null) && (name != null)) {
            if (name.equals(mAdInfo.getAdName())) {
                m_helper.updateAdvertByName(UtilsDB.ADVERT_TABLE_NAME, values, name);
            } else {
                m_helper.insertData(UtilsDB.ADVERT_TABLE_NAME, values);
            }
        } else {
            m_helper.insertData(UtilsDB.ADVERT_TABLE_NAME, values);
        }
    }


    public boolean updateAdvert(ContentValues mValues, String adIdOrName) {
        boolean bRet = false;
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "updateAdvert m_helper null");
            return bRet;
        }
        return m_helper.updateAdvert(UtilsDB.ADVERT_TABLE_NAME, mValues, adIdOrName);
    }

    public int deleteAdvert(String strAdId) {
        int iRet = -1;
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "deleteAdvert m_helper null");
            return iRet;
        }
        return m_helper.deleteAdvert(UtilsDB.ADVERT_TABLE_NAME,strAdId);
    }

    public int deleteAdvertByName(String name) {
        int iRet = -1;
        if (null == m_helper) {
            TcnVendIF.getInstance().LoggerError(TAG, "deleteAdvertByName m_helper null");
            return iRet;
        }
        return m_helper.deleteAdvertByName(UtilsDB.ADVERT_TABLE_NAME,name);
    }
}
