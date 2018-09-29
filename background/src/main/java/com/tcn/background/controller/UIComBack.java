package com.tcn.background.controller;

import android.content.ContentValues;
import android.content.Context;

import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.db.Key_info;
import com.tcn.funcommon.db.UtilsDB;
import com.tcn.funcommon.media.ImageController;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendDBControl;
import com.tcn.funcommon.vend.protocol.DriveControl.GroupInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */
public class UIComBack {
    private static final String TAG = "UIComBack";
    public static final int TYPE_CJ_FLAG = 1;
    private static UIComBack m_Instance = null;
    private Context m_Context = null;
    private volatile int m_iPassardInputCount = 0;
    private volatile int m_iPassardRepInputCount = 0;

    private List<GropInfoBack> m_GrpShowListAll = new ArrayList<GropInfoBack>();
    private List<GropInfoBack> m_GrpShowListSpring = new ArrayList<GropInfoBack>();
    private List<GropInfoBack> m_GrpShowLattice = new ArrayList<GropInfoBack>();
    private List<GropInfoBack> m_GrpShowListElevator = new ArrayList<GropInfoBack>();
    private List<GropInfoBack> m_GrpShowListCoff = new ArrayList<GropInfoBack>();


    public static synchronized UIComBack getInstance() {
        if (null == m_Instance) {
            m_Instance = new UIComBack();
        }
        return m_Instance;
    }

    public void setContext(Context context) {
        m_Context = context;
    }

    public void setPassWordInputCount(int count) {
        m_iPassardInputCount = count;
    }

    public int getPassWordInputCount() {
        return m_iPassardInputCount;
    }

    public void setPassardRepInputCount(int count) {
        m_iPassardRepInputCount = count;
    }

    public int getPassardRepInputCount() {
        return m_iPassardRepInputCount;
    }


    public int getRepGoodsPxWidth() {
        int iRet = 100;
        if (ImageController.SCREEN_TYPE_S1080X1920 == TcnVendIF.getInstance().getScreenType()) {
            iRet = 100;
        } else {
            int iWith = TcnVendIF.getInstance().getScreenWidth();
            int iHeight = TcnVendIF.getInstance().getScreenHeight();
            if (iWith > iHeight) {
                iRet = iHeight / (Integer.valueOf(TcnShareUseData.getInstance().getPerFloorNumber()));
            } else {
                iRet = iWith / (Integer.valueOf(TcnShareUseData.getInstance().getPerFloorNumber()));
            }
        }

        return iRet;
    }

    public int getRepGoodsPxSlotWidth() {
        int iRet = 90;
        if (ImageController.SCREEN_TYPE_S1080X1920 == TcnVendIF.getInstance().getScreenType()) {
            iRet = 90;
        } else {
            int iWith = TcnVendIF.getInstance().getScreenWidth();
            int iHeight = TcnVendIF.getInstance().getScreenHeight();
            if (iWith > iHeight) {
                iRet = iHeight / (Integer.valueOf(TcnShareUseData.getInstance().getPerFloorNumber()));
                iRet = (iHeight - iRet - 50) / (Integer.valueOf(TcnShareUseData.getInstance().getPerFloorNumber()));
            } else {
                iRet = iWith / (Integer.valueOf(TcnShareUseData.getInstance().getPerFloorNumber()));
                iRet = (iWith - iRet - 50) / (Integer.valueOf(TcnShareUseData.getInstance().getPerFloorNumber()));
            }
        }
        return iRet;
    }

    public int getRepGoodsPxHeight() {
        int iRet = 160;
        if (ImageController.SCREEN_TYPE_S1080X1920 == TcnVendIF.getInstance().getScreenType()) {
            iRet = 160;
        } else {
            int iHeight = TcnVendIF.getInstance().getScreenHeight() - 320;

            iRet = iHeight / 8;
        }
        return iRet;
    }

    public int getRepGoodsPxAddSubWidth() {
        int iRet = getRepGoodsPxSlotWidth() / 2 + 1;

        return iRet;
    }

    public int getRepGoodsPxSlotTextHeight() {
        int iRet = 115;
        if (ImageController.SCREEN_TYPE_S1080X1920 == TcnVendIF.getInstance().getScreenType()) {
            return iRet;
        }
        iRet = getRepGoodsPxHeight() - (getRepGoodsPxSlotWidth() / 2) + 2;
        return iRet;
    }

    public int getRepGoodsPxAddSubHeight() {
        int iRet = getRepGoodsPxSlotWidth() / 2;
        if (ImageController.SCREEN_TYPE_S1080X1920 == TcnVendIF.getInstance().getScreenType()) {
            return iRet;
        }
       // iRet = iRet + 10;
        return iRet;
    }

    public int getFitScreenTextSize18() {
        int iSize = 18;
        if ((ImageController.SCREEN_TYPE_S720X1280 == TcnVendIF.getInstance().getScreenType()) || (ImageController.SCREEN_TYPE_S1280X720 == TcnVendIF.getInstance().getScreenType())) {
            iSize = 6;
        }
        return iSize;
    }

    public int getFitScreenTextSize20() {
        int iSize = 20;
        if ((ImageController.SCREEN_TYPE_S720X1280 == TcnVendIF.getInstance().getScreenType()) || (ImageController.SCREEN_TYPE_S1280X720 == TcnVendIF.getInstance().getScreenType())) {
            iSize = 8;
        }
        return iSize;
    }

    public int getFitScreenTextSize22() {
        int iSize = 22;
        if ((ImageController.SCREEN_TYPE_S720X1280 == TcnVendIF.getInstance().getScreenType()) || (ImageController.SCREEN_TYPE_S1280X720 == TcnVendIF.getInstance().getScreenType())) {
            iSize = 9;
        }
        return iSize;
    }

    public int getFitScreenTextSize24() {
        int iSize = 24;
        if ((ImageController.SCREEN_TYPE_S720X1280 == TcnVendIF.getInstance().getScreenType()) || (ImageController.SCREEN_TYPE_S1280X720 == TcnVendIF.getInstance().getScreenType())) {
            iSize = 6;
        }
        return iSize;
    }

    public int getFitScreenTextSize26() {
        int iSize = 26;
        if ((ImageController.SCREEN_TYPE_S720X1280 == TcnVendIF.getInstance().getScreenType()) || (ImageController.SCREEN_TYPE_S1280X720 == TcnVendIF.getInstance().getScreenType())) {
            iSize = 12;
        }
        return iSize;
    }

    /**
     * 根据总共货道数得出vp多少页
     */
    public int getTotalPage(int dataCount) {
        int iTotalPage = 0;
        int iPage = dataCount / TcnCommon.coil_num;
        if (0 == (dataCount % TcnCommon.coil_num)) {
            iTotalPage = iPage;
        } else {
            iTotalPage = iPage + 1;
        }
        return iTotalPage;
    }

    public List<Coil_info> getAliveCoil(int startslotNo,int endSlotNo) {
        List<Coil_info> aliveList = new ArrayList<Coil_info>();
        List<Coil_info> mList = VendDBControl.getInstance().getAliveCoil();
        if ((null == mList) || (mList.size() < 1)) {
            return aliveList;
        }
        for (Coil_info info:mList) {
            if ((info.getCoil_id() >= startslotNo) && (info.getCoil_id() <= endSlotNo)) {
                aliveList.add(info);
            }
        }
        return aliveList;
    }

    public List<Coil_info> getAliveCoilFaults() {
        List<Coil_info> aliveList = new ArrayList<Coil_info>();
        List<Coil_info> mList = VendDBControl.getInstance().getAliveCoilAll();
        if ((null == mList) || (mList.size() < 1)) {
            return aliveList;
        }
        for (Coil_info info:mList) {
            if (info.getWork_status() != 0) {
                aliveList.add(info);
            }
        }
        return aliveList;
    }

    public void addGoodsInfo(String name, String code, String price, String type, String stock, String introduce,String url, int status) {
        ContentValues values = new ContentValues();
        values.put(UtilsDB.GOODS_INFO_NAME, name);
        values.put(UtilsDB.GOODS_INFO_PRODUCT_ID, code);
        values.put(UtilsDB.GOODS_INFO_PRICE, price);
        values.put(UtilsDB.GOODS_INFO_TYPE, type);
        values.put(UtilsDB.GOODS_INFO_STOCK, stock);
        values.put(UtilsDB.GOODS_INFO_INTRODUCE, introduce);
        values.put(UtilsDB.GOODS_INFO_URL, url);
        values.put(UtilsDB.GOODS_INFO_WORK_STATUS, status);
        values.put(UtilsDB.GOODS_INFO_LAST_SHIP_SLOT, 0);
        TcnVendIF.getInstance().reqInsertData(TcnVendIF.DB_TABLE_GOODS, values);
    }

    public void updateGoodsInfo(int id, String name, String code, String price, String type, String stock, String introduce,String url, int status) {
        ContentValues values = new ContentValues();
        values.put(UtilsDB.GOODS_INFO_NAME, name);
        values.put(UtilsDB.GOODS_INFO_PRODUCT_ID, code);
        values.put(UtilsDB.GOODS_INFO_PRICE, price);
        values.put(UtilsDB.GOODS_INFO_TYPE, type);
        values.put(UtilsDB.GOODS_INFO_STOCK, stock);
        values.put(UtilsDB.GOODS_INFO_INTRODUCE, introduce);
        values.put(UtilsDB.GOODS_INFO_URL, url);
        values.put(UtilsDB.GOODS_INFO_WORK_STATUS, status);
        TcnVendIF.getInstance().reqUpdateData(TcnVendIF.DB_TABLE_GOODS, id, values);
    }

    public void deleteGoodsInfo(int id) {
        TcnVendIF.getInstance().reqDeleteData(TcnVendIF.DB_TABLE_GOODS, id);
    }


    public String[] getMapKeyList() {
        String[] keyArr = null;
        List<Key_info> mKeyList = TcnVendIF.getInstance().getAliveKeyAll();
        if ((null == mKeyList) || (mKeyList.size() < 1)) {
            TcnVendIF.getInstance().LoggerError(TAG, "getMapKeyList");
            return keyArr;
        }
        keyArr = new String[mKeyList.size()];
        for (int i = 0; i < keyArr.length; i++) {
            keyArr[i] = String.valueOf(mKeyList.get(i).getKeyNum());
        }
        return keyArr;
    }

    public int getGroupSpringId(String data) {
        int iId = -1;
        if ((null == data) || (data.length() < 1)) {
            return iId;
        }

        for (GropInfoBack info:m_GrpShowListSpring) {
            if (data.equals(info.getShowText())) {
                iId = info.getGrpID();
            }
        }
        return iId;
    }

    public boolean isMutiGrpSpring() {
        boolean bRet = false;
        List<GroupInfo> mGroupInfoList = TcnVendIF.getInstance().getGroupListSpring();
        if ((mGroupInfoList != null) && (mGroupInfoList.size() > 1)) {
            bRet = true;
        }
        return bRet;
    }

    public List<Coil_info> getAliveCoil(int machineId) {
        List<Coil_info> mRetList = new ArrayList<Coil_info>();

        List<Coil_info> mList = TcnVendIF.getInstance().getAliveCoil();

        if ((null == mList) || (mList.size() < 1)) {
            return mRetList;
        }
        int startSlotNo = TcnVendIF.getInstance().getStartSlotNo(machineId);

        for (Coil_info info:mList) {
            if ((info.getCoil_id() > startSlotNo) && (info.getCoil_id() < (startSlotNo + 100))) {
                mRetList.add(info);
            }
        }

        return mRetList;
    }

    public List<GropInfoBack> getGroupListAll() {
        m_GrpShowListAll.clear();
        List<GroupInfo> mGroupInfoList = TcnVendIF.getInstance().getGroupListAll();
        if ((mGroupInfoList != null) && (mGroupInfoList.size() > 1)) {
            for (int i = 0; i < mGroupInfoList.size(); i++) {
                GroupInfo info = mGroupInfoList.get(i);
                GropInfoBack mGropInfoBack = new GropInfoBack();
                mGropInfoBack.setID(i);
                mGropInfoBack.setGrpID(info.getID());
                if (info.getID() == 0) {
                    mGropInfoBack.setShowText("主柜");
                } else {
                    mGropInfoBack.setShowText("副柜"+info.getID());
                }
                m_GrpShowListAll.add(mGropInfoBack);
            }
        }
        return m_GrpShowListAll;
    }

    public String[] getGroupListSpringShow() {
        List<String> m_RetList = new ArrayList<String>();
        if (m_GrpShowListSpring.size() < 1) {
            List<GroupInfo> mGroupInfoList = TcnVendIF.getInstance().getGroupListSpring();
            if ((mGroupInfoList != null) && (mGroupInfoList.size() > 1)) {
                for (int i = 0; i < mGroupInfoList.size(); i++) {
                    GroupInfo info = mGroupInfoList.get(i);
                    GropInfoBack mGropInfoBack = new GropInfoBack();
                    mGropInfoBack.setID(i);
                    mGropInfoBack.setGrpID(info.getID());
                    if (info.getID() == 0) {
                        mGropInfoBack.setShowText("主柜");
                    } else {
                        mGropInfoBack.setShowText("副柜"+info.getID());
                    }
                    m_GrpShowListSpring.add(mGropInfoBack);
                }
            }
        }

        for (GropInfoBack info:m_GrpShowListSpring) {
            m_RetList.add(info.getShowText());
        }
        if (m_RetList.size() < 1) {
            return null;
        }
        String[] strArry = new String[m_RetList.size()];
        for (int i = 0; i < m_RetList.size(); i++) {
            strArry[i] = m_RetList.get(i);
        }
        return strArry;
    }
}
