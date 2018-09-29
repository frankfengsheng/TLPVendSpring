package com.tcn.funcommon.vend.controller;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnLog;
import com.tcn.funcommon.TcnShareMenuData;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.TcnUtility;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.db.Key_info;
import com.tcn.funcommon.db.TcnSQLiteOpenHelper;
import com.tcn.funcommon.media.ImageController;
import com.tcn.funcommon.media.ImageVideoController;
import com.tcn.funcommon.media.Utils;
import com.tcn.funcommon.systeminfo.SystemInfo;
import com.tcn.funcommon.vend.protocol.DriveControl.GroupInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import android_serialport_api.SerialPortController;
import android_serialport_api.SerialPortFinder;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/28 16:36
 * 邮箱：m68013@qq.com
 */
public class TcnVendIF {
    private static final String TAG = "TcnVendIF";
    
    public static final int SHOW_AD_MEDIA = 1;
    public static final int HIDE_AD_MEDIA = 2;

    public static final int SCREEN_FULL = 1;
    public static final int SCREEN_HALF  = 2;

    public static final int MENU_CLOSED = 0;
    public static final int MENU_OPENED = 1;

    public static final int RESTOCK_WITH_ONE_KEY_FRAME_IN = 17;
    public static final int RESTOCK_WITH_ONE_KEY_FRAME_OUT = 18;

    public static final int COMMAND_CLOSE_DOOR = 0;
    public static final int COMMAND_OPEN_DOOR = 1;

    public static final int DB_TABLE_SLOTNO = 1;
    public static final int DB_TABLE_GOODS = 2;
    public static final int DB_TABLE_KEY = 3;

    public static final int COIL_ID_OPERATION_FAIL     = -1;
    public static final int COIL_ID_OPERATION_SUCCESS  = -1;

    public static final int COPY_LOG_FAIL    = -1;
    public static final int COPY_LOG_SUCCESS = 1;


    public static final int COPY_IMAGEGOODS_FAIL    = -1;
    public static final int COPY_IMAGEGOODS_SUCCESS = 1;

    public static final int USB_READ_INFO_FAIL    = -1;
    public static final int USB_READ_INFO_SUCCESS = 1;


    public static final String GOODS_TYPE_ALL = "TYPE_ALL";
    public static final String GOODS_TYPE_OTHER = "TYPE_OTHER";

    private volatile int m_iEventIDTemp = -1;
    private volatile int m_ilParam1 = -1;

    private volatile int m_orientation = Configuration.ORIENTATION_PORTRAIT;
    private static HandlerThreadCommon m_HandlerThreadCommon = null;
    private static VendControl m_VendControl = null;
    private volatile boolean m_bOnlyPlayMedia = false;
    private volatile boolean m_bIsSendForQRCodeInOne = false;
    private volatile boolean m_bIsInbackGround = false;
    private String m_newVersionName = "";
    private volatile String m_strDataType = TcnConstant.DATA_TYPE[1];
    //private String m_TextAd = null;
    private Context m_context = null;
    private List<String> m_strMountedPathList = null;



    private static class SingletonHolder {
        private static TcnVendIF instance = new TcnVendIF();
        private SingletonHolder() {
            //do nothing
        }

    }

    public static TcnVendIF getInstance() {
        return SingletonHolder.instance;
    }

    public void setOnlyPlayMedia(boolean onlyPlayMedia) {
        m_bOnlyPlayMedia = onlyPlayMedia;
    }

    public void init(Context context) {
        if (null == context) {
            return;
        }
        m_context = context;
        m_orientation = context.getResources().getConfiguration().orientation;
        TcnShareUseData.getInstance().init(context);
        TcnShareMenuData.getInstance().init(context);
        TcnLog.getInstance().initLog(context);
        TcnCommon.getInstance().init(context);
        SerialPortController.getInstance().init(context);
        ImageController.instance().init(context);
        m_strDataType = TcnShareUseData.getInstance().getTcnDataType();
        TcnShareUseData.getInstance().setMainActivityCreated(false);

        if ((TcnCommon.SCREEN_INCH[0]).equals(TcnShareUseData.getInstance().getScreenInch())) {
            /*if (getScreenWidth() > getScreenHeight()) {
                TcnShareUseData.getInstance().setFullScreen(true);
            }*/
        } else {
            TcnShareUseData.getInstance().setShowType(false);
        }
        if (TcnShareUseData.getInstance().isFullScreen()) {
            TcnShareUseData.getInstance().setShowType(false);
        }

        if (!TcnShareUseData.getInstance().isShowType()) {
            setGoodsType(GOODS_TYPE_ALL);
        }

        initDisPlayCount();

        LoggerDebug(TAG, "init getVersionName: "+getVersionName()+" getVersionCode: "+getVersionCode()+
                " m_orientation: "+m_orientation+" getScreenType: "+getScreenType()+" isAdvertOnScreenBottom: "+TcnShareUseData.getInstance().isAdvertOnScreenBottom()+
                " isFullScreen: "+TcnShareUseData.getInstance().isFullScreen());

        if (TcnShareUseData.getInstance().isAppForegroundCheck()) {
            if (!isAppForeground()) {
                sendMsgToUIDelay(TcnVendEventID.RESTART_MAIN_ACTIVITY, -1, -1, -1,3000, null);
            }
        }
    }

    public String getPermission() {
        StringBuffer mSb = new StringBuffer();
        mSb.append(Manifest.permission.READ_EXTERNAL_STORAGE);
        mSb.append("|");
        mSb.append(Manifest.permission.WRITE_EXTERNAL_STORAGE);
       return mSb.toString();
    }

    public String[] getPermission(String permissions) {
        String[] mPermissionArry = null;
        if ((null == permissions) || (permissions.length() < 1)) {
            return mPermissionArry;
        }
        if (permissions.contains("|")) {
            mPermissionArry = permissions.split("\\|");
        } else {
            mPermissionArry = new String[1];
            mPermissionArry[0] = permissions;
        }
        return mPermissionArry;
    }

    public boolean isUserMainBoard() {
        boolean bRet = false;
        if (null == m_strDataType) {
            m_strDataType = TcnShareUseData.getInstance().getTcnDataType();
        }
        if ((m_strDataType.equals(TcnConstant.DATA_TYPE[0])) || (m_strDataType.equals(TcnConstant.DATA_TYPE[1]))) {
            bRet = true;
        }
        return bRet;
    }

    public void setConfig() {
        TcnShareUseData.getInstance().setBoardBaudRate("9600");
        TcnShareUseData.getInstance().setPaySystemType(TcnConstant.PAY_SYSTEM_TYPE[0]);
        TcnShareUseData.getInstance().setKeyAndSlotDisplayType(TcnConstant.KEY_SLOT_DISPLAY_TYPE[0]);
        TcnShareUseData.getInstance().setTcnDataType(TcnConstant.DATA_TYPE[2]);
    }

    public void setShowQrcodeType(String data) {

    }

    private void initDisPlayCount() {
        if (Configuration.ORIENTATION_PORTRAIT == m_orientation) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                if (TcnShareUseData.getInstance().getItemCountEveryPage() < 16) {
                    TcnShareUseData.getInstance().setItemCountEveryPage(16);
                }
            } else {
                if (TcnShareUseData.getInstance().getItemCountEveryPage() > 10) {
                    TcnShareUseData.getInstance().setItemCountEveryPage(8);
                }
            }
        } else {
            if (TcnShareUseData.getInstance().getItemCountEveryPage() > 30) {
                TcnShareUseData.getInstance().setItemCountEveryPage(8);
            }
        }

    }

    public String[] getItemCountArrEveryPage() {
        String[] strArr = null;
        if (Configuration.ORIENTATION_PORTRAIT == m_orientation) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                if (TcnShareUseData.getInstance().getItemCountEveryPage() < 16) {
                    TcnShareUseData.getInstance().setItemCountEveryPage(16);
                }
                strArr = new String[5];
                strArr[0] = String.valueOf(16);
                strArr[1] = String.valueOf(20);
                strArr[2] = String.valueOf(30);
                strArr[3] = String.valueOf(60);
                strArr[4] = String.valueOf(80);
            } else {
                if (TcnShareUseData.getInstance().getItemCountEveryPage() > 10) {
                    TcnShareUseData.getInstance().setItemCountEveryPage(8);
                }
                strArr = new String[2];
                strArr[0] = String.valueOf(8);
                strArr[1] = String.valueOf(10);
            }
        } else {
            if (TcnShareUseData.getInstance().getItemCountEveryPage() > 30) {
                TcnShareUseData.getInstance().setItemCountEveryPage(8);
            }
            strArr = new String[4];
            strArr[0] = String.valueOf(8);
            strArr[1] = String.valueOf(10);
            strArr[2] = String.valueOf(20);
            strArr[3] = String.valueOf(30);
        }
        return strArr;
    }

    public boolean isServiceRunning() {
        if (null == m_context) {
            return false;
        }
        ActivityManager manager = (ActivityManager) m_context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(1000)) {
            if (("controller.VendService").equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     *判断当前应用程序处于前台还是后台
     */
    public boolean isAppForeground() {
        boolean bRet = false;
        ActivityManager am = (ActivityManager) m_context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(m_context.getPackageName())) {
                bRet = true;
            }
        }
        return bRet;
    }

    public void startWorkThread() {
        LoggerDebug(TAG, "startWorkThread");

        if (null != m_HandlerThreadCommon) {
            m_HandlerThreadCommon.quit();
            m_HandlerThreadCommon = null;
        }
        m_HandlerThreadCommon = new HandlerThreadCommon(m_context,"CommonHandThread");
        m_HandlerThreadCommon.setPriority(Thread.NORM_PRIORITY-1);
        m_HandlerThreadCommon.start();

        setConfig();
        setShowQrcodeType(TcnShareUseData.getInstance().getQrCodeShowType());

        VendDBControl.getInstance().setShowType(TcnShareUseData.getInstance().isShowType());
        VendDBControl.getInstance().setShowByGoodsCode(TcnShareUseData.getInstance().isShowByGoodsCode());

        m_strDataType = TcnShareUseData.getInstance().getTcnDataType();
        if (null != m_VendControl) {
            m_VendControl.quit();
            m_VendControl = null;
        }
        m_VendControl = new VendControl(m_context,"VendControl");
        m_VendControl.start();
    }

    public void stopWorkThread() {
        LoggerDebug(TAG,"stopWorkThread");

        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.quit();
            m_HandlerThreadCommon = null;
        }

        if (m_VendControl != null) {
            m_VendControl.quit();
            m_VendControl = null;
        }
    }

    public void setBackGround(boolean isInbackGround) {
        m_bIsInbackGround = isInbackGround;
    }

    public boolean isInBackGround() {
        return m_bIsInbackGround;
    }

    public boolean isVaildSeriPort(String seriPort) {
        boolean bRet = false;
        if ((null == seriPort) || (seriPort.isEmpty())) {
            return bRet;
        }
        if (seriPort.contains("ttyS") || seriPort.contains("ttyO")
                || seriPort.contains("ttymxc")|| seriPort.contains("ttyES")
                || seriPort.contains("ttysWK")|| seriPort.contains("ttyCOM")|| seriPort.contains("ttyHSL")) {
            bRet = true;
        }
        return bRet;
    }

    public String getFileName(boolean hasSuffix, String path) {
        String strName = null;
        if ((null == path) || (path.length() < 1)) {
            return strName;
        }
        int index = path.lastIndexOf("/");
        if ((index >= 0) && (path.length() > (index + 1))) {
            if (hasSuffix) {
                strName = path.substring(index + 1);
            } else {
                int indexP = path.indexOf(".",index + 1);
                strName = path.substring(index + 1,indexP);
            }

        }
        return strName;
    }

    public String getVersionName() {
        String strVerName = null;
        if (null == m_context) {
            return strVerName;
        }
        try {
            PackageInfo pInfo = m_context.getPackageManager().getPackageInfo(m_context.getPackageName(), 0);
            if (pInfo != null) {
                strVerName = pInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return strVerName;
    }

    public int getVersionCode() {
        int iVerCode = -1;
        if (null == m_context) {
            return iVerCode;
        }
        try {
            PackageInfo pInfo = m_context.getPackageManager().getPackageInfo(m_context.getPackageName(), 0);
            if (pInfo != null) {
                iVerCode = pInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return iVerCode;
    }

    public String getServerTcnIpAdress(String machineID) {
        String ipAd = TcnShareUseData.getInstance().getIPAddress();

        if ((ipAd != null) && (ipAd.length() > 8) && (!ipAd.contains("www.tcnvmms.com"))) {
            return ipAd;
        }

        String strIP = TcnConstant.IP;
        if ((null == machineID) || (machineID.length() < 1)) {
            return "172.tcnvmms.com";
        }
        if (machineID.length() >= 10) {
            String year = machineID.substring(0,2);
            String month = machineID.substring(2,4);
            if (isDigital(month)) {
                int iMonth = Integer.valueOf(month);
                String quarter = "";
                if ((iMonth >= 1) && (iMonth <= 12)) {
                    if (iMonth >= 10) {
                        quarter = "4";
                    } else if (iMonth >= 7) {
                        quarter = "3";
                    } else if (iMonth >= 4) {
                        quarter = "2";
                    } else {
                        quarter = "1";
                    }
                    strIP = year + quarter + (TcnConstant.IP).substring(3);
                }
            }
        }
        return strIP;
    }

    //www
    public String getServerAdress() {
        String ipAd = TcnShareUseData.getInstance().getIPAddress();

        if ((ipAd != null) && ((ipAd.contains("1.tcnvmms.com")) || (ipAd.contains("2.tcnvmms.com"))
                || (ipAd.contains("3.tcnvmms.com")) || (ipAd.contains("4.tcnvmms.com")))) {
            ipAd = TcnConstant.IP;
        }

        return ipAd;
    }

    public String getSlotNoDisplay(int slotNo) {
        StringBuffer sbSlotNo = new StringBuffer();
        sbSlotNo.append(slotNo);
        int iCount = TcnShareUseData.getInstance().getSlotNoDigitCount();
        if (2 == iCount) {
            if (sbSlotNo.length() == 1) {
                sbSlotNo.insert(0,0);
            }
        } else if (3 == iCount) {
            if (sbSlotNo.length() == 1) {
                sbSlotNo.insert(0,0);
                sbSlotNo.insert(0,0);
            } else if (sbSlotNo.length() == 2) {
                sbSlotNo.insert(0,0);
            } else {

            }
        } else {

        }
        return sbSlotNo.toString();
    }

    public String getShowPrice(String price) {
        String strPrice = "";
        String strPriceUnit = TcnShareUseData.getInstance().getUnitPrice();
        if ((strPriceUnit.equals(TcnCommon.PRICE_UNIT[0])) || (strPriceUnit.equals(TcnCommon.PRICE_UNIT[5]))) {
            strPrice = price + strPriceUnit;
        } else {
            strPrice = strPriceUnit + price;
        }
        return strPrice;
    }

    public Context getContext() {
        return m_context;
    }

    public boolean isMachineLocked() {
        boolean bRet = false;
        int iFailCountLock = TcnShareUseData.getInstance().getShipFailCountLock();
        if ((iFailCountLock < 9) && ((TcnShareUseData.getInstance().getShipContinFailCount()) >= iFailCountLock)) {
            bRet = true;
        }
        return bRet;
    }

    public boolean isShipGoodsByOrder() {
        boolean bRet = true;
        if ((TcnShareUseData.getInstance().getGoodsCodeShipType()).equals(TcnCommon.GOODSCODE_SHIPTYPE[1])) {
            bRet = false;
        }
        return bRet;
    }

    public void setShowType(boolean showType) {
        TcnShareUseData.getInstance().setShowType(showType);
        VendDBControl.getInstance().setShowType(TcnShareUseData.getInstance().isShowType());
    }

    public void setShowByGoodsCode(boolean byCode) {
        TcnShareUseData.getInstance().setShowByGoodsCode(byCode);
        VendDBControl.getInstance().setShowByGoodsCode(TcnShareUseData.getInstance().isShowByGoodsCode());
    }

    public boolean isVaildSeriportDevice(String device,String baudRate) {
        boolean bRet = false;
        if ((null == device) || (device.length() < 1)) {
            return bRet;
        }

        if (!isDigital(baudRate)) {
            return bRet;
        }

        if (device.contains("ttyS") || device.contains("ttyO")
                || device.contains("ttymxc")|| device.contains("ttyES")
                || device.contains("ttysWK")|| device.contains("ttyCOM")|| device.contains("ttyHSL")) {
            bRet = true;
        }

        return bRet;
    }

    public void rebootDevice() {
        if (m_context != null) {
            Intent intent = new Intent("com.android.action.REBOOT");
            m_context.sendBroadcast(intent);
        }
        SystemInfo.rebootDevice();
    }

    public int getScreenOrientation() {
        return m_orientation;
    }

    public int getScreenWidth() {
        return ImageController.instance().getScreenWidth();
    }

    public int  getScreenHeight() {
        return ImageController.instance().getScreenHeight();
    }

    public int  getScreenVideoWidth() {
        return ImageController.instance().getVideoWidth();
    }

    public int  getScreenVideoHeight() {
        return ImageController.instance().getVideoHeight();
    }

    public int getFitScreenSize(int defaultSize) {
        return ImageController.instance().getFitScreenSize(defaultSize);
    }

    public String getScreenInch() {
        return TcnShareUseData.getInstance().getScreenInch();
    }

    public int getScreenType() {
        return ImageController.instance().getScreenType();
    }

    public boolean isNetConnected() {
        return TcnUtility.isNetConnected(m_context);
    }
    public List<String> getMountedDevicePathList() {
        return m_strMountedPathList;
    }

    public void addMountedDevicePath(String path) {
        if (null == m_strMountedPathList) {
            m_strMountedPathList = new ArrayList<String>();
        }
        if (!m_strMountedPathList.contains(path)) {
            m_strMountedPathList.add(path);
        }
    }

    public String getCurrentPlayFileUrlAdvert() {
        return ImageVideoController.instance().getCurrentPlayFileUrl();
    }

    public String getCurrentPlayFileUrlStanby() {
        return ImageController.instance().getCurrentPlayFileUrl();
    }

    public void setCloseDelayTime(String seconds) {
        if (isDigital(seconds)) {
            TcnShareUseData.getInstance().setCloseDelayTime(seconds);
            VendProtoControl.getInstance().setCloseDelaySeconds(Integer.valueOf(seconds));
        }
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

        if (newInfo.getImg_url() != null) {
            if (!(newInfo.getImg_url()).equals(origInfo.getImg_url())) {
                bRet = false;
            }
        }

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

        if (newInfo.getGoods_details_url() != null) {
            if (!(newInfo.getGoods_details_url()).equals(origInfo.getGoods_details_url())) {
                bRet = false;
            }

        }

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

    public boolean isScreenFitted() {
        boolean bRet = false;
        if ((ImageController.SCREEN_TYPE_S768X1360 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S1360X768 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S768X1366 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S1366X768 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S1080X1920 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S1920X1080 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S600X1024 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S1024X600 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S800X1280 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S1280X800 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S480X800 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S800X480 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S1680X1050 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S1050X1680 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S1280X720 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S720X1280 == ImageController.instance().getScreenType())) {

            bRet = true;
        }
        return bRet;
    }

    public void hideSystemBar() {
        if (null == m_context) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("elc_hide_systembar"); //不起作用
        m_context.sendBroadcast(intent);

        Intent intentBsd = new Intent();
        intentBsd.setAction("com.outform.hidebar");  //邦仕达
        m_context.sendBroadcast(intentBsd);

        Intent intentZb = new Intent();
        String action = "com.android.systemui.statusbar.phone.navigationbar.hide.or.show"; //致宝
        intentZb.putExtra("hideorshow","hide"); //致宝
        intentZb.setAction(action);
        m_context.sendBroadcast(intentZb);

        Intent intentRyd = new Intent();
        intentRyd.setAction("com.android.action.hide_all_statusbar"); //锐益达
        m_context.sendBroadcast(intentRyd);

        Intent intentRyd39 = new Intent();
        intentRyd39.setAction("android.navigationbar.state");
        intentRyd39.putExtra("state", "off");
        m_context.sendBroadcast(intentRyd39);

        Intent intentTcn = new Intent();
        String actionTcn = "android.intent.action.NAVIGATIONBAR";
        intentTcn.putExtra("enable","false");
        intentTcn.setAction(actionTcn);
        m_context.sendBroadcast(intentTcn);

        SystemInfo.hideBar();
    }

    public void showSystembar() {
        if (null == m_context) {
            return;
        }
        Intent intent =new Intent();
        intent.setAction("elc_unhide_systembar");//不起作用
        m_context.sendBroadcast(intent);

        Intent intentBsd = new Intent();
        intentBsd.setAction("com.outform.unhidebar");   //邦仕达
        m_context.sendBroadcast(intentBsd);

        Intent intentZb = new Intent();
        String action = "com.android.systemui.statusbar.phone.navigationbar.hide.or.show"; //致宝
        intentZb.putExtra("hideorshow","show"); //致宝
        intentZb.setAction(action);
        m_context.sendBroadcast(intentZb);

        Intent intentRyd = new Intent();
        intentRyd.setAction("com.android.action.show_all_statusbar"); //锐益达
        m_context.sendBroadcast(intentRyd);

        Intent intentRyd39 = new Intent();
        intentRyd39.setAction("android.navigationbar.state");
        intentRyd39.putExtra("state", "on");
        m_context.sendBroadcast(intentRyd39);

        Intent intentTcn = new Intent();
        String actionTcn = "android.intent.action.NAVIGATIONBAR";
        intentTcn.putExtra("enable","true");
        intentTcn.setAction(actionTcn);
        m_context.sendBroadcast(intentTcn);

        SystemInfo.showBar();
    }


    public boolean isExistSlotConfigFile() {
        boolean bRet = false;
        String path = Utils.getExternalMountPath();
        String configPath = path+"/"+TcnConstant.USB_CONFIG_SLOT_FILE;
        try {
            File file = new File(configPath);
            if(file.exists()){
                if (file.isFile()) {
                    bRet = true;
                }
            }
            if (!bRet) {
                if ((m_strMountedPathList != null) && (m_strMountedPathList.size() > 0)) {
                    for (String tmpPath:m_strMountedPathList) {
                        file = new File(tmpPath+"/"+TcnConstant.USB_CONFIG_SLOT_FILE);
                        if(file.exists()){
                            if (file.isFile()) {
                                bRet = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!bRet) {
                String configPath_usb = "/mnt/usb_storage/"+TcnConstant.USB_CONFIG_SLOT_FILE;
                file = new File(configPath_usb);
                if(file.exists()){
                    if (file.isFile()) {
                        bRet = true;
                    }
                }
            }

            if (!bRet) {
                String configPath_sd = "/mnt/internal_sd/"+TcnConstant.USB_CONFIG_SLOT_FILE;
                file = new File(configPath_sd);
                if(file.exists()){
                    if (file.isFile()) {
                        bRet = true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bRet;
    }

    public boolean isExistImageGoodsConfigFile() {
        boolean bRet = false;
        String path = Utils.getExternalMountPath();
        String configPath = path+"/"+TcnConstant.USB_CONFIG_IMAGEGOODS_FILE;
        try {
            File file = new File(configPath);
            if(file.exists()){
                if(file.isDirectory()) {
                    bRet = true;
                }
            }
            if (!bRet) {
                if ((m_strMountedPathList != null) && (m_strMountedPathList.size() > 0)) {
                    for (String tmpPath:m_strMountedPathList) {
                        file = new File(tmpPath+"/"+TcnConstant.USB_CONFIG_IMAGEGOODS_FILE);
                        if(file.exists()){
                            if (file.isDirectory()) {
                                bRet = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (!bRet) {
                String configPath_usb = "/mnt/usb_storage/"+TcnConstant.USB_CONFIG_IMAGEGOODS_FILE;
                file = new File(configPath_usb);
                if(file.exists()){
                    if (file.isFile()) {
                        bRet = true;
                    }
                }
            }

            if (!bRet) {
                String configPath_sd = "/mnt/internal_sd/"+TcnConstant.USB_CONFIG_IMAGEGOODS_FILE;
                file = new File(configPath_sd);
                if(file.exists()){
                    if (file.isFile()) {
                        bRet = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bRet;
    }

    /**
     * 获取资源文件的路径
     * @return
     */
    public String getResPath(int resId) {
        if (null == m_context) {
            return null;
        }
        Uri mUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + m_context.getResources().getResourcePackageName(resId)
                + "/" + m_context.getResources().getResourceTypeName(resId) + "/" + m_context.getResources().getResourceEntryName(resId));
        if (null == mUri) {
            return null;
        }
        return mUri.toString();
    }

    /**
     * 获取imageloader的option对象
     * @return
     */
    public DisplayImageOptions getOptions(int resId) {
        return ImageController.instance().getImageOptions(resId);
    }

    public ImageLoader getImageLoader() {
        return ImageController.instance().getImageLoader();
    }

    public void displayImage(String url,ImageView imageView,int resId) {
        if (null == imageView) {
            return;
        }
        if ((null == url) || (url.length() < 1)) {
            imageView.setImageResource(resId);
            return;
        }
        ImageLoader mImageLoader = ImageController.instance().getImageLoader();
        if (mImageLoader != null) {
            if (url.startsWith("file:///")) {
                ImageLoader.getInstance().displayImage(url, imageView,ImageController.instance().getImageOptions(resId));
            } else {
                ImageLoader.getInstance().displayImage("file:///"+url, imageView,ImageController.instance().getImageOptions(resId));
            }
        }
    }

    public Bitmap getScreenBitmap() {
        return ImageController.instance().getScreenBitmap();
    }

    public Bitmap getBackgroundBitmap() {
        return ImageController.instance().getBackgroundBitmap();
    }

    public List<String> getImageListHelp() {
        return ImageController.instance().getImageListHelp();
    }

    public void startGoBackShopTimer() {
        removeMsgToUI(TcnVendEventID.BACK_TO_SHOPPING);
        sendMsgToUIDelay(TcnVendEventID.BACK_TO_SHOPPING, 60000);
    }

    public void stopGoBackShopTimer() {
        removeMsgToUI(TcnVendEventID.BACK_TO_SHOPPING);
    }

    /**
     * 设置imageloader的option对象
     * @return
     */
    public void setOptions(DisplayImageOptions options) {
        ImageController.instance().setImageOptions(options);
    }

    public List<String> getImageGoodsPathList() {
        return ImageController.instance().getImageGoodsPathList();
    }

    public int getImageGoodsCount() {
        return ImageController.instance().getImageGoodsCount();
    }

    public String getImageGoodsPath() {
        return ImageController.instance().getImageGoodsPath();
    }

    public List<String> getAdvertPathList() {
        return ImageVideoController.instance().getAdvertPathList();
    }

    public int getAdvertCount() {
        return ImageVideoController.instance().getAdvertCount();
    }

    public String getAdvertPath() {
        return ImageVideoController.instance().getAdvertPath();
    }

    public void setPlaybackLoop(boolean loop) {
        ImageVideoController.instance().setPlaybackLoop(loop);
    }

    public SerialPortFinder getSerialPortFinder() {
        return SerialPortController.getInstance().getSerialPortFinder();
    }

    public String[] getAllDevicesPath() {
        String[] values = null;
        SerialPortFinder mSerialPortFinder = SerialPortController.getInstance().getSerialPortFinder();
        if (mSerialPortFinder != null) {
            values = mSerialPortFinder.getAllDevicesPath();
        }
        return values;
    }

//	public String getTextAd() {
//		return m_TextAd;
//	}

    //判断字符串是否包含中文
    public boolean isContainChinese(String str) {
        return TcnUtility.isContainChinese(str);
    }

    /**
     * 判断是否是含小数
     * @param data
     * @return
     */
    public boolean isContainDeciPoint(String data) {
        if (null == data) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]+\\.{0,1}[0-9]{0,2}$");
        return pattern.matcher(data).matches();
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

    public void setNewVerionName(String ver) {
        m_newVersionName = ver;
    }

    public String getNewVerionName() {
        return m_newVersionName;
    }

    public void LoggerDebug(String tag, String msg) {
        TcnLog.getInstance().LoggerDebug(tag, msg);
    }

    public void LoggerInfo(String tag, String msg) {
        TcnLog.getInstance().LoggerInfo(tag, msg);
    }

    public void LoggerError(String tag, String msg) {
        TcnLog.getInstance().LoggerError(tag, msg);
    }


    public boolean isHasQRCodePay() {
        boolean bRet = false;
        if ((TcnShareUseData.getInstance().isWeixinOpen()) || (TcnShareUseData.getInstance().isAliPayOpen())) {
            bRet = true;
        }

        return bRet;
    }

    public void setSelecting(boolean select) {
        VendDBControl.getInstance().setSelecting(select);
    }

    public boolean isSelecting() {
        return VendDBControl.getInstance().isSelecting();
    }

    public boolean isLoadSlotNoSuccess() {
        return VendDBControl.getInstance().isLoadSlotNoSuccess();
    }


    public boolean isValidAmount(String amount) {
        boolean bRet = false;
        if (TcnUtility.isDigital(amount) || TcnUtility.isContainDeciPoint(amount)) {
            bRet = true;
        }
        return bRet;
    }

    public void stopPlayAdvert() {
       // ImageVideoController.instance().stopPlayAdvert();
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.stopPlayAdvert();
        }
    }

    public void stopPlayStandbyAdvert() {
      //  ImageController.instance().stopPlayStandbyAdvert();
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.stopPlayStandbyAdvert();
        }
    }

    public void reqStopShowOrHideAdMedia() {
        removeMsgToUI(TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA);
    }

    public void reqShowOrHideAdMediaImmediately(int showOrHide) {
        if (!(TcnShareUseData.getInstance().isShowScreenProtect())) {
            return;
        }

        if (SHOW_AD_MEDIA == showOrHide) {
            long delayTime = 0;
            if (TcnShareUseData.getInstance().isFullScreen()) {
                TcnVendIF.getInstance().sendMsgToUIDelay(TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA, SHOW_AD_MEDIA, SCREEN_FULL, -1, delayTime, null);
            } else {
                TcnVendIF.getInstance().sendMsgToUIDelay(TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA, SHOW_AD_MEDIA, SCREEN_HALF, -1, delayTime,null);
            }

        } else {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                TcnVendIF.getInstance().sendMsgToUIDelay(TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA, HIDE_AD_MEDIA, SCREEN_FULL, -1, 0, null);
            } else {
                TcnVendIF.getInstance().sendMsgToUIDelay(TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA, HIDE_AD_MEDIA, SCREEN_HALF, -1, 0, null);
            }
        }
    }

    public void reqShowOrHideAdMedia(int showOrHide) {
        if (!(TcnShareUseData.getInstance().isShowScreenProtect())) {
            return;
        }

        if (SHOW_AD_MEDIA == showOrHide) {
            long delayTime = TcnShareUseData.getInstance().getStandbyImageTime()*1000L;
            if (TcnShareUseData.getInstance().isFullScreen()) {
                TcnVendIF.getInstance().sendMsgToUIDelay(TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA, SHOW_AD_MEDIA, SCREEN_FULL, -1, delayTime, null);
            } else {
                TcnVendIF.getInstance().sendMsgToUIDelay(TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA, SHOW_AD_MEDIA, SCREEN_HALF, -1, delayTime,null);
            }

        } else {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                TcnVendIF.getInstance().sendMsgToUIDelay(TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA, HIDE_AD_MEDIA, SCREEN_FULL, -1, 0, null);
            } else {
                TcnVendIF.getInstance().sendMsgToUIDelay(TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA, HIDE_AD_MEDIA, SCREEN_HALF, -1, 0, null);
            }
        }

    }


    /************************** common start *******************************************/
    //待机图片播放
    public void setScreenSurfaceCreated(SurfaceHolder holder, int width,int height) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.setScreenSurfaceCreated(holder, width, height);
        }
    }

    public void setScreenSurfaceDestroyed(SurfaceHolder holder) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.setScreenSurfaceDestroyed(holder);
        }
    }

    //待机视频播放
    public void setScreenSurfaceVideoCreated(SurfaceHolder holder, int width,int height) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.setScreenSurfaceVideoCreated(holder, width, height);
        }
    }

    public void setScreenSurfaceVideoChange(SurfaceHolder holder, int width,int height) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.setScreenSurfaceVideoChange(holder, width, height);
        }
    }

    public void setScreenSurfaceVideoDestroyed(SurfaceHolder holder) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.setScreenSurfaceVideoDestroyed(holder);
        }
    }

    public void setImageSurfaceCreated(SurfaceHolder holder, int width,int height) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.setImageSurfaceCreated(holder, width, height);
        }
    }

    public void setVideoSurfaceCreated(SurfaceHolder holder, int width,int height) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.setVideoSurfaceCreated(holder, width, height);
        }
    }

    public void setVideoSurfaceDestroyed(SurfaceHolder holder) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.setVideoSurfaceDestroyed(holder);
        }
    }

    public void setImageSurfaceDestroyed(SurfaceHolder holder) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.setImageSurfaceDestroyed(holder);
        }

    }

    public void reqReadMeText() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqReadMeText();
        }
    }

    public void queryImagePathList() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.queryImagePathList();
        }

    }

    public void queryImagePathList(int iSearch) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.queryImagePathList(iSearch);
        }

    }

    public void queryAdvertPathList() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.queryAdvertPathList();
        }

    }

    public void queryAdvertPathList(int iSearch) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.queryAdvertPathList(iSearch);
        }

    }

    public void reqImageScreen() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqImageScreen();
        }

    }

    public void reqImageBackground() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqImageBackground();
        }

    }

    public void reqHelpImage() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqHelpImage();
        }

    }

    public void reqTouchSoundLoad() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqTouchSoundLoad();
        }

    }

    public void reqTouchSoundPlay() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqTouchSoundPlay();
        }

    }

    public void reqImageShowPath(int pathType, String fileName) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqImageShowPath(pathType,fileName);
        }
    }

    public boolean isPlayingScreen() {
        return ImageController.instance().isPlaying();
    }

    public void playGivenFolder() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqPlayGivenFolder();
        }
    }

    public boolean isPlayGivenFolder() {
        return ImageVideoController.instance().isPlayGivenFolder();
    }

    public void play() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqPlay();
        }
    }

    public void reqPlay(boolean loop,String url) {
        if (m_HandlerThreadCommon != null) {
            if ((url != null) && (url.length() > 3)) {
                m_HandlerThreadCommon.reqPlay(loop,url);
            }
        }
    }

    public void next() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqNext();
        }
    }

    public void pause() {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqPause();
        }
    }

    public boolean isPlaying() {
        boolean isPlaying = false;
        if (m_HandlerThreadCommon != null) {
            isPlaying = m_HandlerThreadCommon.isPlaying();
        }
        return isPlaying;
    }

    public void setForbiddenPlay(boolean forbiddenPlay) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.setForbiddenPlay(forbiddenPlay);
        }
    }

    public void reqVideoAndImageAdvertList(String path) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqVideoAndImageAdvertList(path);
        }
    }

    public void reqVideoAndImageStandBytList(String path) {
        if (m_HandlerThreadCommon != null) {
            m_HandlerThreadCommon.reqVideoAndImageStandBytList(path);
        }
    }

    public boolean isForbiddenPlay() {
        return ImageVideoController.instance().isForbiddenPlay();
    }

    public boolean isDoorOpen() {
        if (null == m_VendControl) {
            return false;
        }
        return m_VendControl.isDoorOpen();
    }

    public void setDoorOpen(boolean open) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.setDoorOpen(open);
    }

    /************************** common end *******************************************/



    /************************** vend start *******************************************/

    public void setServerReciveHandler(Handler handler) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.setServerReciveHandler(handler);
    }

    public void ship(int slotNo,String shipMethod, String amount,String tradeNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqShip(slotNo,shipMethod,amount,tradeNo);

    }

    public void reqShip(int slotNo,String shipMethod, String amount,String tradeNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqShip(slotNo,shipMethod,amount,tradeNo);
    }

    /**
     * 得到操作数据库的操作对象
     * @return
     */
    public TcnSQLiteOpenHelper getHelper() {
        return VendDBControl.getInstance().getHelper();
    }

    /**
     * 设置操作数据库的操作对象
     * @return
     */
    public void setHelper(TcnSQLiteOpenHelper helper) {
        VendDBControl.getInstance().setHelper(helper);
    }

    public void reqQueryFaults() {
        if (m_VendControl != null) {
            m_VendControl.reqQueryFaults();
        }
    }


    public void reqClearFaults() {
        if (m_VendControl != null) {
            m_VendControl.reqCleanFaults();
        }
    }

    public int getStartSlotNo(int grpId) {
        return VendProtoControl.getInstance().getStartSlotNo(grpId);
    }

    public String[] getBoardGroupNumberArr() {
        return VendProtoControl.getInstance().getBoardGroupNumberArr();
    }

    public String[] getBoardLatticeGroupNumberArr() {
        return VendProtoControl.getInstance().getBoardLatticeGroupNumberArr();
    }

    public Handler getCmunicatHandler() {
        if (m_strDataType.equals(TcnConstant.DATA_TYPE[1])) {
            return null;
        } else {
            return m_VendControl.getCmunicatHandler();
        }
    }

    public void reqQueryStatus(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqQueryStatus(grpId);
        }
    }

    public void reqTakeGoodsDoorControl(int grpId, boolean bOpen) {
        if (m_VendControl != null) {
            m_VendControl.reqTakeGoodsDoorControl(grpId,bOpen);
        }
    }

    public void reqLifterUp(int grpId,int floor) {
        if (m_VendControl != null) {
            m_VendControl.reqLifterUp(grpId,floor);
        }
    }

    public void reqBackHome(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqBackHome(grpId);
        }
    }

    public void reqClapboardSwitch(int grpId, boolean bOpen) {
        if (m_VendControl != null) {
            m_VendControl.reqClapboardSwitch(grpId,bOpen);
        }
    }

    public void reqOpenCool(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqOpenCool(grpId);
        }
    }

    public void reqOpenCoolSpring(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqOpenCoolSpring(grpId);
        }
    }

    public void reqHeat(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqHeat(grpId);
        }
    }

    public void reqHeatSpring(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqHeatSpring(grpId);
        }
    }

    public void reqCloseCoolHeat(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqCloseCoolHeat(grpId);
        }
    }

    public void reqCloseCoolHeatSpring(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqCloseCoolHeatSpring(grpId);
        }
    }


    public void reqCleanDriveFaults(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqCleanDriveFaults(grpId);
        }
    }

    public void reqTemperControl(int grpId,String cmdData) {
        if ((null == cmdData) || (cmdData.length() < 1)) {
            return;
        }
        if ((TcnCommon.HEAT_COOL_OFF_SWITCH_SELECT[0]).equals(cmdData)) {
            reqOpenCool(grpId);
        } else if ((TcnCommon.HEAT_COOL_OFF_SWITCH_SELECT[1]).equals(cmdData)) {
            reqHeat(grpId);
        } else if ((TcnCommon.HEAT_COOL_OFF_SWITCH_SELECT[2]).equals(cmdData)) {
            reqCloseCoolHeat(grpId);
        } else {

        }
    }

    public void reqTemperControlSpring(int grpId,String cmdData) {
        if ((null == cmdData) || (cmdData.length() < 1)) {
            return;
        }
        if ((TcnCommon.HEAT_COOL_OFF_SWITCH_SELECT[0]).equals(cmdData)) {
            reqOpenCoolSpring(grpId);
        } else if ((TcnCommon.HEAT_COOL_OFF_SWITCH_SELECT[1]).equals(cmdData)) {
            reqHeatSpring(grpId);
        } else if ((TcnCommon.HEAT_COOL_OFF_SWITCH_SELECT[2]).equals(cmdData)) {
            reqCloseCoolHeatSpring(grpId);
        } else {

        }
    }

    public void reqSetTemp(int grpId,int temp) {
        if (m_VendControl != null) {
            m_VendControl.reqSetTemp(grpId,temp);
        }
    }

    public void reqSetGlassHeatEnable(int grpId,boolean enable) {
        if (m_VendControl != null) {
            m_VendControl.reqSetGlassHeatEnable(grpId,enable);
        }
    }

    public void reqSetLedOpen(int grpId,boolean open) {
        if (m_VendControl != null) {
            m_VendControl.reqSetLedOpen(grpId,open);
        }
    }

    public void reqSetBuzzerOpen(int grpId,boolean open) {
        if (m_VendControl != null) {
            m_VendControl.reqSetBuzzerOpen(grpId,open);
        }
    }

    public void reqQueryAddress(int slotNo) {
        if (m_VendControl != null) {
            m_VendControl.reqQueryAddress(slotNo);
        }
    }

    public void reqSetLight(int grpId,int minRow, int maxRow) {
        if (m_VendControl != null) {
            m_VendControl.reqSetLight(grpId,minRow,maxRow);
        }
    }

    public void reqLatticeNumSetVaild(int grpId,int row, int startColum,int endColum) {
        if (m_VendControl != null) {
            m_VendControl.reqLatticeNumSetVaild(grpId,row,startColum,endColum);
        }
    }

    public void reqLatticeNumSetInVaild(int grpId,int row, int startColum,int endColum) {
        if (m_VendControl != null) {
            m_VendControl.reqLatticeNumSetInVaild(grpId,row,startColum,endColum);
        }
    }

    public void reqOffCabinetNo(int start, int end) {
        if (m_VendControl != null) {
            m_VendControl.reqOffCabinetNo(start,end);
        }
    }


    public void reqQueryWaterTemp(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqQueryWaterTemp(grpId);
        }
    }

    public void reqShipCup(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqShipCup(grpId);
        }
    }

    public void reqCleanMachine(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqCleanMachine(grpId);
        }
    }

    public void reqQueryCoffStatus(int grpId) {
        if (m_VendControl != null) {
            m_VendControl.reqQueryCoffStatus(grpId);
        }
    }

    public void reqQueryStatusSnake() {
        if (m_VendControl != null) {
            m_VendControl.reqQueryStatusSnake();
        }
    }

    public void reqLEDOff() {
        if (m_VendControl != null) {
            m_VendControl.reqLEDOff();
        }
    }

    public void reqLEDOn() {
        if (m_VendControl != null) {
            m_VendControl.reqLEDOn();
        }
    }

    public void reqReadSnakeTemp() {
        if (m_VendControl != null) {
            m_VendControl.reqReadSnakeTemp();
        }
    }

    //modeLeft:| H: 48 | R: 52 | N: 4E |   modeRight:| H: 48 | R: 52 | N: 4E |
    public void reqSetHeatCoolMode(int modeLeft,int tempLeft,int modeRight,int tempRight) {
        if (m_VendControl != null) {
            m_VendControl.reqSetHeatCoolMode(modeLeft,tempLeft,modeRight,tempRight);
        }
    }

    public void reqSetSetCoolHeatParams(int leftTempDif,int rightTempDif,int compdelayStartTime,int compWorkTime
            ,int compRestTime,int lftHetDelyStTime,int rightHetDelyStTime,int lftRfriTime,int rightRfriTime) {
        if (m_VendControl != null) {
            m_VendControl.reqSetSetCoolHeatParams(leftTempDif,rightTempDif,compdelayStartTime,compWorkTime,
                    compRestTime,lftHetDelyStTime,rightHetDelyStTime,lftRfriTime,rightRfriTime);
        }
    }

    public void reqQueryHaveGoods(int slotNo) {
        if (m_VendControl != null) {
            m_VendControl.reqQueryHaveGoods(slotNo);
        }
    }

    public void reqSellLastOne() {
        if (m_VendControl != null) {
            m_VendControl.reqSellLastOne();
        }
    }

    public void reqNotSellLastOne() {
        if (m_VendControl != null) {
            m_VendControl.reqNotSellLastOne();
        }
    }

    public void reqSetAddr(int addr) {
        if (m_VendControl != null) {
            m_VendControl.reqSetAddr(addr);
        }
    }


    public void reqSetKeyStatus(int key,int status,int point,int tempStatus,String price) {
        if (m_VendControl != null) {
            if ((point > -1) && (tempStatus > -1) && ((isDigital(price)) || (isContainDeciPoint(price)))) {
                m_VendControl.reqSetKeyStatus(key,status,point,tempStatus,(int) (Double.parseDouble(price) * 100));
            } else {
                m_VendControl.reqSetKeyStatus(key,status);
            }
        }
    }

    public void reqSetKeyValuePre() {
        if (m_VendControl != null) {
            m_VendControl.reqSetKeyValuePre();
        }
    }

    public void reqSetKeyValue(int key1,int key2,int key3,int key4,int key5,int key6,int key7,int key8) {
        if (m_VendControl != null) {
            m_VendControl.reqSetKeyValue(key1,key2,key3,key4,key5,key6,key7,key8);
        }
    }

    public void reqSetPatternMode(int x1,int x2,int x3,int x4) {
        if (m_VendControl != null) {
            m_VendControl.reqSetPatternMode(x1,x2,x3,x4);
        }
    }

    public void reqExitPatternMode() {
        if (m_VendControl != null) {
            m_VendControl.reqExitPatternMode();
        }
    }

    public void reqExitFlickerStatus() {
        if (m_VendControl != null) {
            m_VendControl.reqExitFlickerStatus();
        }
    }


    public void reqSetSlodOutKeyPattern() {
        if (m_VendControl != null) {
            m_VendControl.reqSetSlodOutKeyPattern();
        }
    }


    public void reqSetSlodOutKey() {
        if (m_VendControl != null) {
            m_VendControl.reqSetSlodOutKey();
        }
    }

    public void reqSetAllSameColor(int color) {
        if (m_VendControl != null) {
            m_VendControl.reqSetAllSameColor(color);
        }
    }

    public void reqSetKeyFlicker(int key,int color1,int color2) {
        if (m_VendControl != null) {
            m_VendControl.reqSetKeyFlicker(key,color1,color2);
        }
    }


    public void reqClearSlotFaults(int startSlotNo, int endSlotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqCleanFaults(startSlotNo,endSlotNo);
    }

    public void setTemperature(String temp) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.setTemperature(temp);
    }

    public String getTemp() {
        if (null == m_VendControl) {
            return null;
        }
        return m_VendControl.getTemp();
    }

    public void setHumidity(String humidity) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.setHumidity(humidity);
    }

    public String getHumidity() {
        if (null == m_VendControl) {
            return null;
        }
        return m_VendControl.getHumidity();
    }

    public void setTemperatureAndHumidity(String temp,String humidity) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.setTemperatureAndHumidity(temp,humidity);
    }

    public int getServerDropSensor() {
        int iRet = 0;
        if (m_strDataType.equals(TcnConstant.DATA_TYPE[2])) {
            if (TcnShareUseData.getInstance().isDropSensorCheck()) {
                iRet = 1;
            }
        }
        return iRet;
    }

    public String getGoodsName(int slotNo) {
        return VendDBControl.getInstance().getGoodsName(slotNo);
    }
    public String getGoodsCode(int slotNo) {
        return VendDBControl.getInstance().getGoodsCode(slotNo);
    }

    public String getGoodsImageUrl(int slotNo) {
        return VendDBControl.getInstance().getGoodsImageUrl(slotNo);
    }

    public String getGoodsContent(int slotNo) {
        return VendDBControl.getInstance().getGoodsContent(slotNo);
    }

    public boolean isShipping() {
        return VendProtoControl.getInstance().isShiping();
    }

    public void reqSlotNoInfoOpenSerialPort() {
        VendProtoControl.getInstance().reqSlotNoInfoOpenSerialPort();
    }

    public void reqSlotNoInfo() {
        VendProtoControl.getInstance().reqSlotNoInfo();
    }

    public boolean haveDoorSwitch() {
        return VendProtoControl.getInstance().haveDoorSwitch();
    }

    public void setGoodsType(String type) {
        VendDBControl.getInstance().setGoodsType(type);
    }

    public List<Coil_info> getCoilListFromKey(int keyNumber) {
        return VendDBControl.getInstance().getCoilListFromKey(keyNumber);
    }

    public int getCoilExtantQuantityForKeyMap(int keyNumber) {
        return VendDBControl.getInstance().getCoilExtantQuantityForKeyMap(keyNumber);
    }

    public boolean isFirstStockCoilErrForKeyMap(int keyNumber) {
        return VendDBControl.getInstance().isErrForKey(keyNumber);
    }

    public String getGoodsType() {
        return VendDBControl.getInstance().getGoodsType();
    }

    public List<Coil_info> getAliveCoil() {
        return VendDBControl.getInstance().getAliveCoil();
    }

    public int getAliveCoilCount() {
        return VendDBControl.getInstance().getAliveCoilCount();
    }

    public int getAliveCoilCountExcept() {
        return VendDBControl.getInstance().getAliveCoilCountExcept();
    }

    public List<Coil_info> getAliveCoilExcept() {
        return VendDBControl.getInstance().getAliveCoilExcept();
    }

    public List<Coil_info> getAliveCoilAll() {
        return VendDBControl.getInstance().getAliveCoilAll();
    }

    public int getAliveCoilCountAll() {
        return VendDBControl.getInstance().getAliveCoilCountAll();
    }


    public int getAliveCoilCountFaults() {
        return VendDBControl.getInstance().getAliveCoilCountFaults();
    }

    public List<Key_info> getAliveKey() {
        return VendDBControl.getInstance().getAliveKey();
    }

    public int getAliveKeyCountAll() {
        return VendDBControl.getInstance().getAliveKeyCountAll();
    }

    public List<Key_info> getAliveKeyAll() {
        return VendDBControl.getInstance().getAliveKeyAll();
    }

    public int getAliveKeyCount() {
        return VendDBControl.getInstance().getAliveKeyCount();
    }

    public List<Integer> getAliveKeyCoil() {
        return VendDBControl.getInstance().getAliveKeyCoil();
    }

    public List<Integer> getAliveKeyCoilAll() {
        return VendDBControl.getInstance().getAliveKeyCoilAll();
    }

    public Coil_info getCoilInfoAll(int position) {
        return VendDBControl.getInstance().getCoilInfoAll(position);
    }

    public int getAliveGoodsCountAll() {
        return VendDBControl.getInstance().getAliveGoodsCountAll();
    }

    public List<Goods_info> getAliveGoodsAll() {
        return VendDBControl.getInstance().getAliveGoodsAll();
    }

    public int getAliveGoodsCount() {
        return VendDBControl.getInstance().getAliveGoodsCount();
    }

    public List<Goods_info> getAliveGoods() {
        return VendDBControl.getInstance().getAliveGoods();
    }

    public Goods_info getGoodsInfoAll(String code) {
        return VendDBControl.getInstance().getGoodsInfoAll(code);
    }

    public List<Coil_info> getAliveCoilFromType(String type) {
        return VendDBControl.getInstance().getAliveCoilFromType(type);
    }

    public List<String> getAliveType() {
        return VendDBControl.getInstance().getAliveType();
    }

    public List<Coil_info> queryAliveType(String type) {
        return VendDBControl.getInstance().queryAliveType(type);
    }

    public int getGoodsPosition(String goodsId,List<Goods_info> grpList) {
        int index = -1;
        if ((null == goodsId) || (null == grpList) || (grpList.size() < 1)) {
            return index;
        }

        for (int i = 0; i < grpList.size(); i++) {
            if (goodsId.equals(grpList.get(i).getGoods_id())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void updateSlotNoInfo(int startSlotNo,int endSlotNo, String price, String name,String type, String content,
                                 String imgUrl,String spec, String goodsCode, int extantQuantity, int capacity) {
        VendDBControl.getInstance().OnUpdateSlotNoInfo(startSlotNo,endSlotNo,price,name,type,content,imgUrl,spec,goodsCode,extantQuantity,capacity);
    }

    public void updateDataGoodsInfo(int slotNo, String imgUrl, String name,String content, String spec,boolean notify) {
        VendDBControl.getInstance().updateDataGoodsInfo(slotNo,imgUrl,name,content,spec,notify);
    }

    public boolean updateDataGoodsInfo(int slotNo, int capacity,int stock, String price,String code, String imgUrl, String name,String content, String spec, boolean notify,boolean needUpToServer) {
        return VendDBControl.getInstance().updateDataGoodsInfo(slotNo,capacity,stock,price,code,imgUrl,name,content,spec,notify,needUpToServer);
    }

    public boolean updateDataGoodsInfo(int slotNoOrKey, int capacity,int stock, String price,String code, String imgUrl, String name,String content, String spec, boolean needUpToServer) {
        return VendDBControl.getInstance().updateDataGoodsInfo(slotNoOrKey,capacity,stock,price,code,imgUrl,name,content,spec,needUpToServer);
    }


    public List<Coil_info> queryCoilListFromKey(int key) {
        return VendDBControl.getInstance().queryCoilListFromKey(key);
    }

    public void updateDataGoodsContent(int slotNo,String content) {
        VendDBControl.getInstance().updateDataGoodsContent(slotNo,content);
    }

    public Coil_info getSelectCoilInfo() {
        return VendDBControl.getInstance().getSelectCoilInfo();
    }

    public void setSelectCoilInfo(Coil_info info) {
        VendDBControl.getInstance().setSelectCoilInfo(info);
    }

    public Coil_info getCoilInfo(int slotNo) {
        return VendDBControl.getInstance().getCoilInfo(slotNo);
    }

    public Key_info getKeyInfo(int slotNo) {
        return VendDBControl.getInstance().getKeyInfo(slotNo);
    }

    public Coil_info getAliveInfoFromKey(int key) {
        return VendDBControl.getInstance().getAliveInfoFromKey(key);
    }

    public Goods_info getGoodsInfo(String code) {
        return VendDBControl.getInstance().getGoodsInfo(code);
    }

    public Coil_info getCoilInfoLastestByCode(String goodsCode) {
        return VendDBControl.getInstance().getCoilInfoLastestByCode(goodsCode);
    }

    public Coil_info getCoilInfoLastestByType(String goodsType) {
        return VendDBControl.getInstance().getCoilInfoLastestByType(goodsType);
    }

    public Coil_info getCoilInfoLastestByType(String type,String selectDbInfo,int startRangeValue) {
        return VendDBControl.getInstance().getCoilInfoLastestByType(type,selectDbInfo,startRangeValue);
    }

    public void queryAliveCoil() {
        VendDBControl.getInstance().queryAliveCoil();
    }

    public void queryAliveCoil(boolean needUpToServer) {
        VendDBControl.getInstance().queryAliveCoil(needUpToServer);
    }

    public void queryAliveCoilExcept(List<String> listType) {
        VendDBControl.getInstance().queryAliveCoilExcept(listType);
    }

    public void reqDeleteType(String type) {
        VendDBControl.getInstance().reqDeleteType(type);
    }

    public Goods_info queryGoodsInfo(int id) {
        return VendDBControl.getInstance().queryGoodsInfo(id);
    }


    public void reqInsertData(int tableType, ContentValues values) {
        VendDBControl.getInstance().reqInsertData(tableType,values);
    }

    public void reqUpdateData(int tableType,int id,ContentValues values) {
        VendDBControl.getInstance().reqUpdateData(tableType,id,values);
    }

    public void reqDeleteData(int tableType,int id) {
        VendDBControl.getInstance().reqDeleteData(tableType,id);
    }

    public void reqModifyType(int startId, int endId, String type) {
        VendDBControl.getInstance().reqModifyType(startId,endId,type);
    }

    public void reqAddShowSlotNo(int startcoilId,int endcoilId) {
        VendDBControl.getInstance().reqAddShowSlotNo(startcoilId,endcoilId);
    }

    public void reqDeleteSlotNo(int coil_id) {
        VendDBControl.getInstance().reqDeleteSlotNo(coil_id);
    }

    public void reqDeleteKeyMap(int key) {
        VendDBControl.getInstance().reqDeleteKeyMap(key);
    }

    public void reqAddStock(int coil_id) {
        VendDBControl.getInstance().reqAddStock(coil_id);
    }

    public void reqSubStock(int coil_id) {
        VendDBControl.getInstance().reqSubStock(coil_id);
    }

    public void reqDeleteGoodsId(String goodsId) {
        VendDBControl.getInstance().reqDeleteGoodsId(goodsId);
    }

    public void reqAddSlotGoods(int startSlotNo, int endSlotNo,Goods_info info) {
        if (info != null) {
            VendDBControl.getInstance().reqAddSlotGoods(startSlotNo,endSlotNo,info);
        }
    }

    public List<GroupInfo> getGroupListAll() {
        return VendProtoControl.getInstance().getGroupListAll();
    }

    public List<GroupInfo> getGroupListSpring() {
        return VendProtoControl.getInstance().getGroupListSpring();
    }

    public void reqQuerySlotStatus(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqQuerySlotStatus(slotNo);
    }

    public void reqSelfCheck(int grpId) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSelfCheck(grpId);
    }

    public void reqReset(int grpId) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqReset(grpId);
    }

    public void reqSetSpringSlot(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSetSpringSlot(slotNo);
    }

    public void reqSetBeltsSlot(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSetBeltsSlot(slotNo);
    }

    public void reqSpringAllSlot(int grpId) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSpringAllSlot(grpId);
    }

    public void reqBeltsAllSlot(int grpId) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqBeltsAllSlot(grpId);
    }

    public void reqSingleSlot(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSingleSlot(slotNo);
    }

    public void reqDoubleSlot(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqDoubleSlot(slotNo);
    }

    public void reqSingleAllSlot(int grpId) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSingleAllSlot(grpId);
    }

    public void reqTestMode(int grpId) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqTestMode(grpId);
    }

    public void reqWriteDataShipTest(int start, int end) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqTestSlotNo(start, end);
    }

    public void reqUpdateSlotName(int startSlotNo, int endtSlotNo, String name) {
        VendDBControl.getInstance().reqUpdateSlotName(startSlotNo,endtSlotNo,name);
    }

    public void reqUpdateSlotType(int startslotNo,int endSlotNo, String type) {
        VendDBControl.getInstance().reqUpdateSlotType(startslotNo,endSlotNo,type);
    }

    public void reqUpdateSlotIntroduce(int startslotNo,int endSlotNo, String content) {
        VendDBControl.getInstance().reqUpdateSlotIntroduce(startslotNo,endSlotNo,content);
    }

    public void reqUpdateSlotImageUrl(int startslotNo,int endSlotNo, String imgUrl) {
        VendDBControl.getInstance().reqUpdateSlotImageUrl(startslotNo,endSlotNo,imgUrl);
    }

    public void reqUpdateSlotAdvertUrl(int startslotNo,int endSlotNo, String imgUrl) {
        VendDBControl.getInstance().reqUpdateSlotAdvertUrl(startslotNo,endSlotNo,imgUrl);
    }

    public void reqUpdateSlotSpec(int startslotNo,int endSlotNo, String spec) {
        VendDBControl.getInstance().reqUpdateSlotSpec(startslotNo,endSlotNo,spec);
    }

    public void reqUpdateGoodsCapacity(int startslotNo,int endSlotNo, String capacity) {
        VendDBControl.getInstance().reqUpdateGoodsCapacity(startslotNo,endSlotNo,capacity);
    }

    public void reqFillStockToCapacityAll() {
        VendDBControl.getInstance().reqFillStockToCapacityAll();
    }

    public void reqFillStockToCapacity(int start, int end) {
        VendDBControl.getInstance().reqFillStockToCapacity(start,end);
    }

    public void reqFillStockToCapacity(List<Coil_info> sList) {
        if ((sList != null) && (sList.size() > 0)) {
            VendDBControl.getInstance().reqFillStockToCapacity((sList.get(0)).getCoil_id(),(sList.get(sList.size() - 1)).getCoil_id());
        }
    }

    public void reqWriteSlotCapacity(int startSlotNo, int endtSlotNo, int capacity) {
        VendDBControl.getInstance().reqUpdateSlotCapacity(startSlotNo,endtSlotNo,capacity);
    }

    public void reqWriteSlotStock(int startSlotNo, int endtSlotNo, int stock) {
        VendDBControl.getInstance().reqUpdateSlotExtantQuantity(startSlotNo,endtSlotNo,stock);
    }

    public void reqWriteSlotStatus(int startSlotNo, int endtSlotNo, int status) {
        VendDBControl.getInstance().reqUpdateSlotStatus(startSlotNo,endtSlotNo,status);
    }

    public void reqWriteSlotPrice(int startSlotNo, int endtSlotNo, String price) {
        VendDBControl.getInstance().reqUpdateSlotPrice(startSlotNo,endtSlotNo,price);
    }

    public void reqWriteSlotSalePrice(int startSlotNo, int endtSlotNo, String price) {
        VendDBControl.getInstance().reqUpdateSlotSalePrice(startSlotNo,endtSlotNo,price);
    }

    public void reqUpdateHeatTime(int startSlotNo, int endtSlotNo, int heatTime) {
        VendDBControl.getInstance().reqUpdateHeatTime(startSlotNo,endtSlotNo,heatTime);
    }

    public void reqUpdateColumn(int startSlotNo, int endtSlotNo, int column) {
        VendDBControl.getInstance().reqUpdateColumn(startSlotNo,endtSlotNo,column);
    }

    public void reqUpdateRow(int startSlotNo, int endtSlotNo, int column) {
        VendDBControl.getInstance().reqUpdateRow(startSlotNo,endtSlotNo,column);
    }

    public void reqUpdatePullBack(int startSlotNo, int endtSlotNo, int back) {
        VendDBControl.getInstance().reqUpdatePullBack(startSlotNo,endtSlotNo,back);
    }

    public void reqWriteSlotGoodsCode(int startSlotNo, int endtSlotNo, String code) {
        VendDBControl.getInstance().reqUpdateSlotGoodsCode(startSlotNo,endtSlotNo,code);
    }

    public void reqSelectGoods(int position) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSelectGoods(position);
    }

    public void reqSelectSlotNo(int slotNo) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqSelectSlotNo(slotNo);
    }


    public void reqEndEffectiveTime() {
        m_VendControl.reqEndEffectiveTime();
    }

    public void closeTrade() {
        closeTrade(true);
    }

    public void closeTrade(boolean canRefund) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.closeTrade(canRefund);
    }

    public void reqTextSpeak(String strSpeak) {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqTextSpeak(strSpeak);
    }

    public void reqTextSpeakStop() {
        if (null == m_VendControl) {
            return;
        }
        m_VendControl.reqTextSpeakStop();
    }

    public void reqGoodsSoldOut() {
        VendDBControl.getInstance().reqGoodsSoldOut();
    }


    /************************** vend end *******************************************/

    private final Handler m_cEventHandlerForUI = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            handleMessageToUI(msg.what,(Bundle)msg.obj);
        }
    };

    public void handleMessageToUI(int what, Bundle bundle) {
       /* switch (what) {
            case TcnVendEventID.QUERY_ALIVE_COIL:
            case TcnVendEventID.QUERY_IMAGE_PATHLIST:
            case TcnVendEventID.QUERY_VIDEO_PATHLIST:
            case TcnVendEventID.IMAGE_SCREEN:
            case TcnVendEventID.IMAGE_BACKGROUND:
            case TcnVendEventID.ACTION_RECEIVE_DATA:
            case TcnVendEventID.SHOW_TOAST:
            case TcnVendEventID.WX_QR_CODE_GENERATED:
            case TcnVendEventID.ALI_QR_CODE_GENERATED:
            case TcnVendEventID.COMMAND_INPUT_MONEY:
            case TcnVendEventID.COMMAND_SHIPPING:
            case TcnVendEventID.COMMAND_SHIPMENT_SUCCESS:
            case TcnVendEventID.COMMAND_SHIPMENT_FAILURE:
            case TcnVendEventID.COMMAND_SHIPMENT_FAULT:
            case TcnVendEventID.COMMAND_SELECT_GOODS:
            case TcnVendEventID.COMMAND_INVALID_SLOTNO:
            case TcnVendEventID.COMMAND_FAULT_SLOTNO:
            case TcnVendEventID.COMMAND_SOLD_OUT:
            case TcnVendEventID.COMMAND_COIN_REFUND_START:
            case TcnVendEventID.COMMAND_COIN_REFUND_END:
            case TcnVendEventID.COMMAND_CONNECT_SERVER_SUCCESS:
            case TcnVendEventID.COMMAND_CONNECT_SERVER_FAILED:
            case TcnVendEventID.COMMAND_NOTSET_CIPMODE:
            case TcnVendEventID.COMMAND_DOOR_SWITCH:
            case TcnVendEventID.WX_TRADE_REFUND:
            case TcnVendEventID.WX_TRADE_CLOSE:
            case TcnVendEventID.ALIPAY_SCAN_COLSE:
            case TcnVendEventID.ALIPAY_WAVE_COLSE:
            case TcnVendEventID.PAY_POS_FAILED:
            case TcnVendEventID.NETWORK_NOT_GOOOD:
            case TcnVendEventID.ADJUST_TIME_REQ:
            case TcnVendEventID.UPDATE_PAY_TIME:
            case TcnVendEventID.TEXT_AD:
            case TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA:
            case TcnVendEventID.SERIAL_PORT_CONFIG_ERROR:
            case TcnVendEventID.SERIAL_PORT_SECURITY_ERROR:
            case TcnVendEventID.SERIAL_PORT_UNKNOWN_ERROR:
            case TcnVendEventID.BACK_TO_SHOPPING:
            case TcnVendEventID.COMMAND_CANCEL_PAY:
            case TcnVendEventID.COMMAND_TOSS_PAPER_MONEY:
            case TcnVendEventID.QUERY_IMAGE_SHOW_PATH:
            case TcnVendEventID.COMMAND_CONFIG_OK:
            case TcnVendEventID.COMMAND_CONFIG_INFO:
            case TcnVendEventID.TRK_PLAY:
            case TcnVendEventID.TRK_PAUSE:
            case TcnVendEventID.PLAY_GIVEN_FOLDER_COMPLETION:
            case TcnVendEventID.TRK_PLAY_COMPLETION:
            case TcnVendEventID.APP_QR_CODE_GENERATED:
            case TcnVendEventID.CMD_PLAY_IMAGE:
            case TcnVendEventID.CMD_PLAY_VIDEO:
            case TcnVendEventID.NETWORK_CHANGE:
            case TcnVendEventID.COMMAND_RESTOCK_WITH_ONE_KEY:
            case TcnVendEventID.COMMAND_TWO_CODE_IN_ONE:
            case TcnVendEventID.UPDATE_TIME:
            case TcnVendEventID.COMMAND_CHANGEIN_COINS:
            case TcnVendEventID.COMMAND_CHANGEIN_PAPER_MONEY:
            case TcnVendEventID.COMMAND_BALANCE:
            case TcnVendEventID.COMMAND_MACHINE_ID:
            case TcnVendEventID.COMMAND_FRAME_NUMBER_1:
            case TcnVendEventID.COMMAND_FRAME_NUMBER_4:
            case TcnVendEventID.COMMAND_GIVE_CHANGE:
            case TcnVendEventID.CMD_PLAY_SCREEN_IMAGE:
            case TcnVendEventID.CMD_PLAY_SCREEN_VIDEO:
            case TcnVendEventID.COMMAND_TOTAL_SALES_INFO:
            case TcnVendEventID.REQ_DELETE_TYPE:
            case TcnVendEventID.REQ_MODIFY_TYPE:
            case TcnVendEventID.QUERY_ALIVE_GOODS:
            case TcnVendEventID.INSERT_DATA:
            case TcnVendEventID.UPTE_DATA:
            case TcnVendEventID.DELETE_DATA:
            case TcnVendEventID.COMMAND_FRAME_NUMBER_3:
            case TcnVendEventID.COMMAND_FRAME_NUMBER_20:
            case TcnVendEventID.COMMAND_FAULT_INFORMATION:
            case TcnVendEventID.COMMAND_DOOR_IS_OPEND:
            case TcnVendEventID.QUERY_ALIVE_COIL_EXCEPTIONE:
            case TcnVendEventID.USB_CONFIG_COPY_IMAGEGOODS_START:
            case TcnVendEventID.USB_CONFIG_COPY_IMAGEGOODS_END:
            case TcnVendEventID.USB_CONFIG_COPY_LOG_START:
            case TcnVendEventID.USB_CONFIG_COPY_LOG_END:
            case TcnVendEventID.USB_CONFIG_READINFO_START:
            case TcnVendEventID.USB_CONFIG_READINFO_END:
            case TcnVendEventID.USB_CONFIG_CONFIG_INFO:
            case TcnVendEventID.USB_CONFIG_SYN_DATA_START:
            case TcnVendEventID.USB_CONFIG_SYN_DATA_END:
            case TcnVendEventID.PROMPT_INFO:
            case TcnVendEventID.TEMPERATURE_INFO:

                Bundle msgBundle = bundle;

                int iEventID = msgBundle.getInt("eID");
                int lParam1 = msgBundle.getInt("lP1");
                int lParam2 = msgBundle.getInt("lP2");
                long lParam3 = msgBundle.getLong("lP3");
                String lParam4 = msgBundle.getString("lP4");
                notifyUI(iEventID, lParam1, lParam2, lParam3, lParam4);
                break;

            default:
                break;
        }*/
        Bundle msgBundle = bundle;

        int iEventID = msgBundle.getInt("eID");
        int lParam1 = msgBundle.getInt("lP1");
        int lParam2 = msgBundle.getInt("lP2");
        long lParam3 = msgBundle.getLong("lP3");
        String lParam4 = msgBundle.getString("lP4");
        notifyUI(iEventID, lParam1, lParam2, lParam3, lParam4);
    }

    private final Handler m_cEventHandlerForUIObj = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            handleMessageToUIObj(msg.what,msg.arg1,msg.arg2,(Object)msg.obj);
        }
    };


    public void handleMessageToUIObj(int iEventID, int lParam1, int lParam2,Object lParam5) {
        notifyUI(iEventID, lParam1, lParam2, -1, null,lParam5);
    }

    public void sendMsgToUI(int iEventID, int lParam1, int lParam2, long lParam3, String lParam4) {

        if ((m_iEventIDTemp == iEventID) && (m_ilParam1 == lParam1)) {
            TcnUtility.removeMessages(m_cEventHandlerForUI, iEventID);
        }

        m_iEventIDTemp = iEventID;
        m_ilParam1 = lParam1;

        Bundle msgBundle = new Bundle();

        msgBundle.putInt("eID", iEventID);
        msgBundle.putInt("lP1", lParam1);
        msgBundle.putInt("lP2", lParam2);
        msgBundle.putLong("lP3", lParam3);
        msgBundle.putString("lP4", lParam4);
        TcnUtility.sendMsg(m_cEventHandlerForUI, iEventID, -1, -1, msgBundle);
    }

    public void sendMsgToUIDelay(int iEventID,long delayMillis) {
        Bundle msgBundle = new Bundle();

        msgBundle.putInt("eID", iEventID);
        msgBundle.putInt("lP1", -1);
        msgBundle.putInt("lP2", -1);
        msgBundle.putLong("lP3", -1);
        msgBundle.putString("lP4", null);
        TcnUtility.removeMessages(m_cEventHandlerForUI,iEventID);
        TcnUtility.sendMsgDelayed(m_cEventHandlerForUI, iEventID, -1, delayMillis, msgBundle);
    }

    public void sendMsgToUIDelay(int iEventID,int lParam1, int lParam2,long lParam3,long delayMillis, String lParam4) {
        Bundle msgBundle = new Bundle();

        msgBundle.putInt("eID", iEventID);
        msgBundle.putInt("lP1", lParam1);
        msgBundle.putInt("lP2", lParam2);
        msgBundle.putLong("lP3", lParam3);
        msgBundle.putString("lP4", lParam4);
        TcnUtility.removeMessages(m_cEventHandlerForUI,iEventID);
        TcnUtility.sendMsgDelayed(m_cEventHandlerForUI, iEventID, -1, delayMillis, msgBundle);
    }

    public void sendMsgToUI(int iEventID, int lParam1, int lParam2,Object obj) {
        TcnUtility.sendMsg(m_cEventHandlerForUIObj, iEventID, lParam1, lParam2, obj);
    }

    public void removeMsgToUI(int iEventID) {
        TcnUtility.removeMessages(m_cEventHandlerForUI,iEventID);
    }


    // VendEventListener interface
    public interface VendEventListener {
        public void VendEvent(VendEventInfo cEventInfo);
    }

    private final CopyOnWriteArrayList<VendEventListener> m_Callbacks = new CopyOnWriteArrayList<VendEventListener>();

    public void registerListener (VendEventListener callback) {
        synchronized (m_Callbacks) {
            if (null == callback) {
                return;
            }

            if (!(m_Callbacks.contains(callback))) {
                m_Callbacks.add(callback);
            }
        }
    }

    public void unregisterListener (VendEventListener callback) {
        synchronized (m_Callbacks) {
            if (null == callback) {
                return;
            }
            if (m_Callbacks.contains(callback)) {
                m_Callbacks.remove(callback);
            }

        }
    }

    private void sendNotifyToUI(VendEventInfo cEventInfo) {
        synchronized (m_Callbacks) {
            for (VendEventListener c : m_Callbacks) {
                c.VendEvent(cEventInfo);
            }
        }
    }
    private void notifyUI(int iEventID, int lParam1, int lParam2, long lParam3, String lParam4) {
        VendEventInfo cEventInfo = new VendEventInfo();

        cEventInfo.SetEventID(iEventID);
        cEventInfo.SetlParam1(lParam1);
        cEventInfo.SetlParam2(lParam2);
        cEventInfo.SetlParam3(lParam3);
        cEventInfo.SetlParam4(lParam4);
        sendNotifyToUI(cEventInfo);
    }

    private void notifyUI(int iEventID, int lParam1, int lParam2, long lParam3, String lParam4,Object lParam5) {
        VendEventInfo cEventInfo = new VendEventInfo();

        cEventInfo.SetEventID(iEventID);
        cEventInfo.SetlParam1(lParam1);
        cEventInfo.SetlParam2(lParam2);
        cEventInfo.SetlParam3(lParam3);
        cEventInfo.SetlParam4(lParam4);
        cEventInfo.SetlParam5(lParam5);
        sendNotifyToUI(cEventInfo);
    }

    public void handleVendMessage(Message msg) {
        if (m_VendListener != null) {
            m_VendListener.handleMessage(msg);
        }
    }

    public void handleThirdPartyMessage(Message msg) {
        if (m_ThirdPartyListener != null) {
            m_ThirdPartyListener.handleMessage(msg);
        }
    }

    public void handlePayMessage(Message msg) {
        if (m_PayListener != null) {
            m_PayListener.handleMessage(msg);
        }
    }

    public void handleDBMessage(Message msg) {
        if (m_DBListener != null) {
            m_DBListener.handleMessage(msg);
        }
    }

    public void handleCommunicationMessage(Message msg) {
        if (m_CommunicationListener != null) {
            m_CommunicationListener.handleMessage(msg);
        }
    }

    public void handleServerMessage(Message msg) {
        if (m_ServerListener != null) {
            m_ServerListener.handleMessage(msg);
        }
    }

    private VendListener m_VendListener = null;
    private boolean m_bVendNotUseDefault = false;
    public void setOnVendListener(VendListener listener,boolean notUseDefault) {
        this.m_VendListener = listener;
        this.m_bVendNotUseDefault = notUseDefault;
    }

    public boolean isVendNotUseDefault() {
        return m_bVendNotUseDefault;
    }

    private ThirdPartyListener m_ThirdPartyListener = null;
    private boolean m_bThirdPartyNotUseDefault = false;
    public void setOnThirdPartyListener(ThirdPartyListener listener,boolean notUseDefault) {
        this.m_ThirdPartyListener = listener;
        this.m_bThirdPartyNotUseDefault = notUseDefault;
    }

    public boolean isThirdPartyNotUseDefault() {
        return m_bThirdPartyNotUseDefault;
    }

    private PayListener m_PayListener = null;
    private boolean m_bPayNotUseDefault = false;
    public void setOnPayListener(PayListener listener,boolean notUseDefault) {
        this.m_PayListener = listener;
        this.m_bPayNotUseDefault = notUseDefault;
    }

    public boolean isPayNotUseDefault() {
        return m_bPayNotUseDefault;
    }

    private DBListener m_DBListener = null;
    private boolean m_bDBNotUseDefault = false;
    public void setOnDBListener(DBListener listener,boolean notUseDefault) {
        this.m_DBListener = listener;
        this.m_bDBNotUseDefault = notUseDefault;
    }

    public boolean isDBNotUseDefault() {
        return m_bDBNotUseDefault;
    }

    private CommunicationListener m_CommunicationListener = null;
    private boolean m_bCommunicationNotUseDefault = false;
    public void setOnCommunicationListener(CommunicationListener listener,boolean notUseDefault) {
        this.m_CommunicationListener = listener;
        this.m_bCommunicationNotUseDefault = notUseDefault;
    }

    public boolean isCommunicationNotUseDefault() {
        return m_bCommunicationNotUseDefault;
    }


    private ServerListener m_ServerListener = null;
    private boolean m_bServerNotUseDefault = false;
    public void setOnServerListener(ServerListener listener,boolean notUseDefault) {
        this.m_ServerListener = listener;
        this.m_bServerNotUseDefault = notUseDefault;
    }

    public boolean isServerNotUseDefault() {
        return m_bServerNotUseDefault;
    }

    public interface ServerListener{
        public void handleMessage(Message msg);
    }

    public interface VendListener{
        public void handleMessage(Message msg);
    }

    public interface ThirdPartyListener{
        public void handleMessage(Message msg);
    }

    public interface PayListener{
        public void handleMessage(Message msg);
    }

    public interface DBListener{
        public void handleMessage(Message msg);
    }

    public interface CommunicationListener{
        public void handleMessage(Message msg);
    }
}
