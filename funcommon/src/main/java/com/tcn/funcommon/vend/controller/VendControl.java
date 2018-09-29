package com.tcn.funcommon.vend.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.tcn.funcommon.R;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.TcnUtility;
import com.tcn.funcommon.advert.RemoutAdvertControl;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.db.Key_info;
import com.tcn.funcommon.db.TcnSQLiteOpenHelper;
import com.tcn.funcommon.db.UtilsDB;
import com.tcn.funcommon.ftp.FtpControl;
import com.tcn.funcommon.update.UpdateInfo;
import com.tcn.funcommon.update.UpdateManager;
import com.tcn.funcommon.vend.def.TcnDBDef;
import com.tcn.funcommon.vend.def.TcnMDBResultDef;
import com.tcn.funcommon.vend.def.TcnProtoDef;
import com.tcn.funcommon.vend.def.TcnProtoResultDef;
import com.tcn.funcommon.vend.protocol.MsgKey;
import com.tcn.funcommon.vend.protocol.MsgToSend;
import com.tcn.funcommon.voice.VoiceController;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import android_serialport_api.SerialPortController;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/20 20:30
 * 邮箱：m68013@qq.com
 */
public class VendControl extends HandlerThread {
    private static final String TAG = "VendControl";


    private static VendHandler m_vendHandler = null;
    private static DBHandler m_dbHandler = null;
    private static TCNCommunicationHandler m_cmunicatHandler = null;
    private static Handler m_serverReciveHandler = null;

    private volatile String m_strTemp = "";
    private volatile String m_strTotalTemp = "";
    private volatile String m_strHumidity = "";
    private volatile int m_EffectiveTime = 90;

    private volatile boolean m_bHasScaned = false;

    private UpdateManager m_updateMan;

    private Context m_context = null;

    private Timer m_UpdatePayTimer = null;
    private TimerTask m_UpdatePayTimerTask = null;


    public VendControl(Context context, String name) {
        super(name);
        m_context = context;
    }

    public VendControl(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "onLooperPrepared()");
        initialize();
        super.onLooperPrepared();
    }

    @Override
    public void run() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "run()");
        super.run();
    }

    @Override
    public boolean quit() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "quit()");
        deInitialize();
        return super.quit();
    }

    private void initialize() {
        m_vendHandler = new VendHandler();
        m_dbHandler = new DBHandler();
        m_cmunicatHandler = new TCNCommunicationHandler();
        VendDBControl.getInstance().initialize(m_context,TcnSQLiteOpenHelper.getInstance(m_context),m_dbHandler,false);

        String strKeyType = TcnShareUseData.getInstance().getKeyAndSlotDisplayType();
        if (strKeyType.equals( TcnConstant.KEY_SLOT_DISPLAY_TYPE[1])) {
            VendDBControl.getInstance().setHaveKeyMap(true);
        } else if ((strKeyType.equals( TcnConstant.KEY_SLOT_DISPLAY_TYPE[2]))) {
            VendDBControl.getInstance().setHaveKeyMap(true);
            VendDBControl.getInstance().setOnlyKeyMap(true);
        } else {

        }

        
        VendProtoControl.getInstance().initialize(TcnShareUseData.getInstance().getBoardType(),TcnShareUseData.getInstance().getBoardTypeSecond(),TcnShareUseData.getInstance().getBoardTypeThird(),
                TcnShareUseData.getInstance().getBoardTypeFourth(),TcnShareUseData.getInstance().getSerPortGroupMapFirst(),TcnShareUseData.getInstance().getSerPortGroupMapSecond(),
                TcnShareUseData.getInstance().getSerPortGroupMapThird(),TcnShareUseData.getInstance().getSerPortGroupMapFourth(),m_cmunicatHandler);

        m_vendHandler.removeMessages(TcnVendCMDDef.HANDLER_THREAD_VEND_STARED);
        m_vendHandler.sendEmptyMessage(TcnVendCMDDef.HANDLER_THREAD_VEND_STARED);
        m_vendHandler.removeMessages(TcnVendCMDDef.HANDLER_THREAD_VEND_STARED_DELAY);
        m_vendHandler.sendEmptyMessageDelayed(TcnVendCMDDef.HANDLER_THREAD_VEND_STARED_DELAY,10000);

        OnTimer();
    }

    public void deInitialize() {
        if (m_vendHandler != null) {
            m_vendHandler.removeCallbacksAndMessages(null);
            m_vendHandler = null;
        }

        if (m_dbHandler != null) {
            m_dbHandler.removeCallbacksAndMessages(null);
            m_dbHandler = null;
        }

        if (m_cmunicatHandler != null) {
            m_cmunicatHandler.removeCallbacksAndMessages(null);
            m_cmunicatHandler = null;
        }

        if (m_serverReciveHandler != null) {
            m_serverReciveHandler.removeCallbacksAndMessages(null);
            m_serverReciveHandler = null;
        }

        if (m_updateMan != null) {
            m_updateMan.deInitialize();
        }
        m_updateMan = null;

        m_context = null;

        stopUpdatePayTimer();

        TcnShareUseData.getInstance().setMainActivityCreated(false);
        closeSerialPort();
       // reqTextSpeakStop();
    }

    public Handler getCmunicatHandler() {
        return m_cmunicatHandler;
    }

    public void setTemperature(String temp) {
        m_strTemp = temp;
        m_strTotalTemp = temp;
    }

    public String getTemp() {
        if (m_strTotalTemp.contains("|")) {
            return m_strTotalTemp;
        }
        return m_strTemp;
    }

    public void setHumidity(String humidity) {
        m_strHumidity = humidity;
    }

    public String getHumidity() {
        return m_strHumidity;
    }

    public void setTemperatureAndHumidity(String temp,String humidity) {
        m_strTemp = temp;
        m_strTotalTemp = temp;
        m_strHumidity = humidity;
    }

    public boolean isDoorOpen() {
        return VendProtoControl.getInstance().isDoorOpen();
    }

    public void setDoorOpen(boolean open) {
        VendProtoControl.getInstance().setDoorOpen(open);
    }

    public void setServerReciveHandler(Handler handler) {
        m_serverReciveHandler = handler;
    }


    private void closeSerialPort() {
        SerialPortController.getInstance().closeSerialPort();
        SerialPortController.getInstance().closeSerialPortNew();
        SerialPortController.getInstance().closeSerialPortThird();
        SerialPortController.getInstance().closeSerialPortFourth();
    }

    public void reqCopyLog() {
        TcnUtility.removeMessages(m_vendHandler, TcnVendCMDDef.REQ_COPY_LOG);
        TcnUtility.sendMsg(m_vendHandler, TcnVendCMDDef.REQ_COPY_LOG, -1, -1, null);
    }

    public void reqShip(int slotNo,String shipMethod, String amount,String tradeNo) {
        String methodAmountTradeNo = shipMethod+"|"+amount+"|"+tradeNo;
        TcnUtility.sendMsg(m_vendHandler, TcnVendCMDDef.REQ_SHIP, slotNo, -1, methodAmountTradeNo);
    }

    public void ship(int slotNo,String shipMethod, String amount,String tradeNo) {
        Coil_info info = VendDBControl.getInstance().getCoilInfo(slotNo);
        ship(false,slotNo,shipMethod, amount,info,tradeNo);
    }

    private void ship(boolean notSave,int slotNo,String shipMethod, String amount,Coil_info info, String tradeNo) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "ship slotNo: "+slotNo+" shipMethod: "+shipMethod+" amount: "+amount+" tradeNo: "+tradeNo);
        if (TcnVendIF.getInstance().isMachineLocked()) {
            onTextSpeak(m_context.getString(R.string.tip_machine_locked));
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_MACHINE_LOCKED, -1, -1, -1, m_context.getString(R.string.tip_machine_locked));
            return;
        }
        if (slotNo < 1) {
            return;
        }

        if (info != null) {
            if (!TcnVendIF.getInstance().isValidAmount(amount)) {
                amount = info.getPar_price();
            }
            TcnVendIF.getInstance().LoggerDebug(TAG, "ship getCoil_id: "+info.getCoil_id()+" getPar_price: "+info.getPar_price()+" tradeNo: "+tradeNo);
        }

        shipTcn(info,shipMethod,amount,tradeNo);

    }

    private void shipTcn(Coil_info info,String shipMethod, String amount,String tradeNo) {
        if (!OnCheckCoilInfo(info)) {
            OnShipFailureHandle(info,shipMethod,1,tradeNo);
            return;
        }
        if (null == info) {
            TcnVendIF.getInstance().LoggerError(TAG, "shipTcn shipMethod: "+shipMethod+" amount: "+amount+" tradeNo: "+tradeNo);
            return;
        }
        VendProtoControl.getInstance().ship(info.getCoil_id(),shipMethod,tradeNo);
    }

    public void reqTextSpeak(String strSpeak) {
        TcnUtility.removeMessages(m_vendHandler, TcnVendCMDDef.TEXT_SPEAK);
        TcnUtility.sendMsg(m_vendHandler, TcnVendCMDDef.TEXT_SPEAK, -1, -1, strSpeak);
    }

    public void reqTextSpeakStop() {
        TcnUtility.removeMessages(m_vendHandler, TcnVendCMDDef.TEXT_SPEAK_STOP);
        TcnUtility.sendMsg(m_vendHandler, TcnVendCMDDef.TEXT_SPEAK_STOP, -1, -1, null);
    }

    public void closeTrade(boolean canRefund) {
        TcnUtility.removeMessages(m_vendHandler, TcnVendCMDDef.CLOSE_TRADE);
        TcnUtility.sendMsg(m_vendHandler, TcnVendCMDDef.CLOSE_TRADE, -1, -1, canRefund);
    }

    public void reqSelectGoods(int position) {
        TcnUtility.removeMessages(m_vendHandler, TcnVendCMDDef.SELECT_GOODS_REQ);
        TcnUtility.sendMsg(m_vendHandler, TcnVendCMDDef.SELECT_GOODS_REQ, position, -1, null);
    }

    public void reqSelectSlotNo(int slotNo) {
        TcnUtility.removeMessages(m_vendHandler, TcnVendCMDDef.SELECT_SLOTNO);
        TcnUtility.sendMsg(m_vendHandler, TcnVendCMDDef.SELECT_SLOTNO, slotNo, -1, null);
    }

    public void reqEndEffectiveTime() {
        TcnUtility.removeMessages(m_vendHandler, TcnVendCMDDef.END_EFFECTIVETIME);
        TcnUtility.sendMsg(m_vendHandler, TcnVendCMDDef.END_EFFECTIVETIME, -1, -1, null);
    }

    public void reqTestSlotNo(int start, int end) {
        TcnUtility.sendMsg(m_vendHandler, TcnVendCMDDef.WRITE_DATA_SHIP_TEST, start, end, null);
    }

    private void reqShipTest(int slotNo, CopyOnWriteArrayList<Integer> slotNoList) {
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_TEST_SLOT, slotNo, -1, slotNoList);
    }

    public void reqQueryFaults() {
        TcnUtility.removeMessages(m_dbHandler, TcnDBDef.REQ_QUERY_SLOT_FAULTS);
        TcnUtility.sendMsg(m_dbHandler, TcnDBDef.REQ_QUERY_SLOT_FAULTS, -1, -1, null);
    }

    public void reqCleanFaults() {
        TcnUtility.removeMessages(m_dbHandler, TcnDBDef.REQ_CLEAR_SLOT_FAULTS);
        TcnUtility.sendMsg(m_dbHandler, TcnDBDef.REQ_CLEAR_SLOT_FAULTS, -1, -1, null);
    }

    public void reqCleanFaults(int startSlotNo, int endSlotNo) {
        TcnUtility.removeMessages(m_dbHandler, TcnDBDef.REQ_CLEAR_SLOT_FAULTS);
        TcnUtility.sendMsg(m_dbHandler, TcnDBDef.REQ_CLEAR_SLOT_FAULTS, startSlotNo, endSlotNo, null);
    }

    public void reqQuerySlotStatus(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_QUERY_SLOT_STATUS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_QUERY_SLOT_STATUS, slotNo, -1, null);
    }

    public void reqSelfCheck(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SELF_CHECK);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SELF_CHECK, grpId, -1, null);
    }

    public void reqReset(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_RESET);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_RESET, grpId, -1, null);
    }

    public void reqSetSpringSlot(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_SPRING);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_SPRING, slotNo, -1, null);
    }

    public void reqSetBeltsSlot(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_BELTS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_BELTS, slotNo, -1, null);
    }


    public void reqSpringAllSlot(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_SPRING);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_SPRING, grpId, -1, null);
    }

    public void reqBeltsAllSlot(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_BELT);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_BELT, grpId, -1, null);
    }

    public void reqSingleSlot(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_SINGLE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_SINGLE, slotNo, -1, null);
    }

    public void reqDoubleSlot(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_DOUBLE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_DOUBLE, slotNo, -1, null);
    }

    public void reqSingleAllSlot(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_SINGLE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_SLOTNO_ALL_SINGLE, grpId, -1, null);
    }

    public void reqTestMode(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_SET_TEST_MODE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_SET_TEST_MODE, grpId, -1, null);
    }

    public void reqQueryStatus(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_STATUS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_STATUS, grpId, -1, null);
    }

    public void reqTakeGoodsDoorControl(int grpId, boolean bOpen) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_TAKE_GOODS_DOOR);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_TAKE_GOODS_DOOR, grpId, -1, bOpen);
    }

    public void reqLifterUp(int grpId,int floor) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_LIFTER_UP);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_LIFTER_UP, grpId, floor, null);
    }

    public void reqBackHome(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_LIFTER_BACK_HOME);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_LIFTER_BACK_HOME, grpId, -1, null);
    }

    public void reqClapboardSwitch(int grpId, boolean bOpen) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CLAPBOARD_SWITCH);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CLAPBOARD_SWITCH, grpId, -1, bOpen);
    }

    public void reqOpenCool(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_OPEN_COOL);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_OPEN_COOL, grpId, -1, null);
    }

    public void reqOpenCoolSpring(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_COOL);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_COOL, grpId, -1, null);
    }

    public void reqHeat(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_OPEN_HEAT);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_OPEN_HEAT, grpId, -1, null);
    }

    public void reqHeatSpring(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_HEAT);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_HEAT, grpId, -1, null);
    }

    public void reqCloseCoolHeat(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CLOSE_COOL_HEAT);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CLOSE_COOL_HEAT, grpId, -1, null);
    }

    public void reqCloseCoolHeatSpring(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_COOL_HEAT_CLOSE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_COOL_HEAT_CLOSE, grpId, -1, null);
    }

    public void reqSetTemp(int grpId,int temp) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_TEMP);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_TEMP, grpId, temp, null);
    }

    public void reqSetGlassHeatEnable(int grpId,boolean enable) {
        if (enable) {
            TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_GLASS_HEAT_OPEN);
            TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_GLASS_HEAT_OPEN, grpId, -1, enable);
        } else {
            TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_GLASS_HEAT_CLOSE);
            TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_GLASS_HEAT_CLOSE, grpId, -1, enable);
        }
    }

    public void reqSetLedOpen(int grpId,boolean open) {
        if (open) {
            TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_LIGHT_OPEN);
            TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_LIGHT_OPEN, grpId, -1, null);
        } else {
            TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_LIGHT_CLOSE);
            TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_LIGHT_CLOSE, grpId, -1, null);
        }
    }

    public void reqSetBuzzerOpen(int grpId,boolean open) {
        if (open) {
            TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_BUZZER_OPEN);
            TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_BUZZER_OPEN, grpId, -1, null);
        } else {
            TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_BUZZER_CLOSE);
            TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_BUZZER_CLOSE, grpId, -1, null);
        }
    }

    public void reqCleanDriveFaults(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CLEAN_FAULTS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CLEAN_FAULTS, grpId, -1, null);
    }
    public void reqQueryParameters(int grpId,int address) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_PARAMETERS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_PARAMETERS, grpId, address, null);
    }

    public void reqSetId(int grpId,int id) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_ID);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_ID, grpId, id, null);
    }

    public void reqSetLightOutSteps(int grpId,int steps) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_LIGHT_OUT_STEP);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_LIGHT_OUT_STEP, grpId, steps, null);
    }

    public void reqSetParameters(int grpId,int address,String value) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_PARAMETERS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_PARAMETERS, grpId, address, value);
    }

    public void reqFactoryReset(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_FACTORY_RESET);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_FACTORY_RESET, grpId, -1, null);
    }

    public void reqDetectLight(int grpId, String direction) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_DETECT_LIGHT);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_DETECT_LIGHT, grpId, -1, direction);
    }

    public void reqDetectShip(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_DETECT_SHIP);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_DETECT_SHIP, grpId, -1, null);
    }

    public void reqQueryAddress(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_ADDRESS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_ADDRESS, slotNo, -1, null);
    }

    public void reqSetLight(int grpId,int minRow, int maxRow) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SCAN_LIGHT_SET);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SCAN_LIGHT_SET, minRow, maxRow, grpId);
    }

    public void reqLatticeNumSetVaild(int grpId,int row, int startColum,int endColum) {
        String[] strArrColum = new String[2];
        strArrColum[0] = String.valueOf(startColum);
        strArrColum[1] = String.valueOf(endColum);
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CABINETNO_SET_VAILD);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CABINETNO_SET_VAILD, grpId, row, strArrColum);
    }

    public void reqLatticeNumSetInVaild(int grpId,int row, int startColum,int endColum) {
        String[] strArrColum = new String[2];
        strArrColum[0] = String.valueOf(startColum);
        strArrColum[1] = String.valueOf(endColum);
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CABINETNO_SET_INVAILD);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CABINETNO_SET_INVAILD, grpId, row, strArrColum);
    }

    public void reqOffCabinetNo(int start, int end) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CABINETNO_OFF);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_CABINETNO_OFF, start, end, null);
    }

    public void reqQueryWaterTemp(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_WATER_TEMP);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_WATER_TEMP, grpId, -1, null);
    }

    public void reqShipCup(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_SHIP_CUP);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_SHIP_CUP, grpId, -1, null);
    }

    public void reqCleanMachine(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_CLEAN);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_CLEAN, grpId, -1, null);
    }

    public void reqQueryCoffStatus(int grpId) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_COFF_STATUS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_COFF_STATUS, grpId, -1, null);
    }

    public void reqQueryStatusSnake() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.CMD_QUERY_SNAKE_STATUS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.CMD_QUERY_SNAKE_STATUS, -1, -1, null);
    }

    public void reqLEDOff() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_LED_OFF);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_LED_OFF, -1, -1, null);
    }

    public void reqLEDOn() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_LED_ON);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_LED_ON, -1, -1, null);
    }

    public void reqReadSnakeTemp() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_READ_SNAKE_CURRENT_TEMP);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_READ_SNAKE_CURRENT_TEMP, -1, -1, null);
    }

    public void reqSetHeatCoolMode(int modeLeft,int tempLeft,int modeRight,int tempRight) {
        MsgSend mStatusMsgSend = new MsgSend();
        mStatusMsgSend.setData1(modeRight);
        mStatusMsgSend.setData2(tempRight);
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_HEAT_COOL_TEMP_MODE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_HEAT_COOL_TEMP_MODE, modeLeft, tempLeft, mStatusMsgSend);
    }

    public void reqSetSetCoolHeatParams(int leftTempDif,int rightTempDif,int compdelayStartTime,int compWorkTime
            ,int compRestTime,int lftHetDelyStTime,int rightHetDelyStTime,int lftRfriTime,int rightRfriTime) {
        MsgSend mStatusMsgSend = new MsgSend();
        mStatusMsgSend.setData1(compdelayStartTime);
        mStatusMsgSend.setData2(compWorkTime);
        mStatusMsgSend.setData3(compRestTime);
        mStatusMsgSend.setData4(lftHetDelyStTime);
        mStatusMsgSend.setData5(rightHetDelyStTime);
        mStatusMsgSend.setData6(lftRfriTime);
        mStatusMsgSend.setData7(String.valueOf(rightRfriTime));
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_COOL_HEAT_DETAIL_PARAME);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_COOL_HEAT_DETAIL_PARAME, leftTempDif, rightTempDif, mStatusMsgSend);
    }

    public void reqQueryHaveGoods(int slotNo) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_GOODS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_QUERY_GOODS, slotNo, -1, null);
    }

    public void reqSellLastOne() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_SELL_LASTONE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_SELL_LASTONE, -1, -1, null);
    }

    public void reqNotSellLastOne() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_NOT_SELL_LASTONE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_NOT_SELL_LASTONE, -1, -1, null);
    }

    public void reqSetAddr(int addr) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_BOARD_ADDR);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_SET_BOARD_ADDR, addr, -1, null);
    }

    public void reqSetKeyStatus(int key,int status,int point,int tempStatus,int price) {
        MsgSend mStatusMsgSend = new MsgSend();
        mStatusMsgSend.setData1(point);
        mStatusMsgSend.setData2(tempStatus);
        mStatusMsgSend.setData3(price);
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_STATUS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_STATUS, key, status, mStatusMsgSend);
    }

    public void reqSetKeyStatus(int key,int status) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "reqSetKeyStatus key: "+key+" status: "+status);
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_STATUS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_STATUS, key, status, null);
    }

    public void reqSetKeyValuePre() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_VALUE_PRE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_VALUE_PRE, -1, -1, null);
    }

    public void reqSetKeyValue(int key1,int key2,int key3,int key4,int key5,int key6,int key7,int key8) {
        MsgSend mStatusMsgSend = new MsgSend();
        mStatusMsgSend.setData1(key3);
        mStatusMsgSend.setData2(key4);
        mStatusMsgSend.setData3(key5);
        mStatusMsgSend.setData4(key6);
        mStatusMsgSend.setData5(key7);
        mStatusMsgSend.setData6(key8);
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_VALUE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_VALUE, key1, key2, mStatusMsgSend);
    }

    public void reqSetPatternMode(int x1,int x2,int x3,int x4) {
        MsgSend mStatusMsgSend = new MsgSend();
        mStatusMsgSend.setData1(x3);
        mStatusMsgSend.setData2(x4);
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_PATTERN_MODE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_PATTERN_MODE, x1, x2, mStatusMsgSend);
    }

    public void reqExitPatternMode() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_EXIT_PATTERN_MODE);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_EXIT_PATTERN_MODE, -1, -1, null);
    }

    public void reqExitFlickerStatus() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_EXIT_FLICKER_STATUS);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_EXIT_FLICKER_STATUS, -1, -1, null);
    }

    public void reqSetSlodOutKeyPattern() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_SOLDOUT_KEY_PATTERN);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_SOLDOUT_KEY_PATTERN, -1, -1, null);
    }

    public void reqSetSlodOutKey() {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_SOLDOUT_KEY);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_SOLDOUT_KEY, -1, -1, null);
    }

    public void reqSetAllSameColor(int color) {
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_ALL_SAME_COLOR);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_SET_ALL_SAME_COLOR, color, -1, null);
    }

    public void reqSetKeyFlicker(int key,int color1,int color2) {
        MsgSend mStatusMsgSend = new MsgSend();
        mStatusMsgSend.setData1(color1);
        mStatusMsgSend.setData2(color2);
        TcnUtility.removeMessages(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_KEY_FLICKER);
        TcnUtility.sendMsg(m_cmunicatHandler, TcnProtoDef.REQ_CMD_KEY_KEY_FLICKER, key, -1, mStatusMsgSend);
    }

    private void sendMessageToServer(int what, int arg1, int arg2, String data) {
        if (null == m_serverReciveHandler) {
            return;
        }
        TcnUtility.sendMsg(m_serverReciveHandler, what, arg1, arg2, data);
    }

    //单位秒
    public void resetPayTimer(int payTime) {
        m_EffectiveTime = payTime;
    }


    private void startUpdatePayTimer() {

        m_EffectiveTime = TcnShareUseData.getInstance().getPayTime();

        if (null == m_UpdatePayTimer) {
            m_UpdatePayTimer = new Timer("startUpdatePayTimer");
        }
        if (m_UpdatePayTimerTask != null) {
            m_UpdatePayTimerTask.cancel();
            m_UpdatePayTimerTask = null;
        }
        m_UpdatePayTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (m_EffectiveTime >= 0) {
                    m_EffectiveTime--;
                    TcnUtility.removeMessages(m_vendHandler, TcnVendCMDDef.UPDATE_PAY_TIME);
                    TcnUtility.sendMsg(m_vendHandler, TcnVendCMDDef.UPDATE_PAY_TIME, m_EffectiveTime, -1, null);
                }
            }
        };
        m_UpdatePayTimer.schedule(m_UpdatePayTimerTask, 0, 1000);
    }

    private void stopUpdatePayTimer() {
        TcnVendIF.getInstance().setPlaybackLoop(false);
        if (m_UpdatePayTimer != null) {
            m_UpdatePayTimer.cancel();
            m_UpdatePayTimer.purge();
            m_UpdatePayTimer = null;
        }

        if (m_UpdatePayTimerTask != null) {
            m_UpdatePayTimerTask.cancel();
            m_UpdatePayTimerTask = null;
        }
    }

    private void OnCheckUpdate() {
        if (null == m_updateMan) {
            m_updateMan = new UpdateManager(m_context, TcnShareUseData.getInstance().getApkName(), TcnShareUseData.getInstance().getApkUrl(), m_appUpdateCb);
        }

        m_updateMan.checkUpdate();
    }

    private boolean OnCheckCoilInfo(Coil_info info) {
        boolean isNormal = false;

        if ((null == info) || (info.getCoil_id() < 1)) {
            OnInvalidSlotNo();
            return isNormal;
        }

        if (!VendDBControl.getInstance().isValidSlotNo(info)) {
            OnInvalidSlotNo(info.getCoil_id());
            return isNormal;
        }

        if (VendDBControl.getInstance().isFaultSlotNo(info)) {
            OnFaultSlotNo(info.getCoil_id(),info.getWork_status());
            return isNormal;
        }

        if (VendDBControl.getInstance().isExtantQuantityEmpty(info)) {
            OnSoldOut(info.getCoil_id());
            return isNormal;
        }

        isNormal = true;

        return isNormal;
    }

    private void OnCloseTrade(boolean canRefund) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnCloseTrade canRefund: "+canRefund);
        m_bHasScaned = false;
        TcnVendIF.getInstance().setPlaybackLoop(false);
        VendDBControl.getInstance().setSelecting(false);
        stopUpdatePayTimer();
        if (VendProtoControl.getInstance().isShiping()) {
            return;
        }
    }

    private void OnTimer() {
        String strTime = TcnUtility.getTime(TcnConstant.YEAR_HM);
        TcnUtility.sendMsgDelayed(m_vendHandler, TcnVendCMDDef.UPDATE_TIME, -1, 30000, null);
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.UPDATE_TIME, -1, -1, -1, strTime);
    }

    private void onTextSpeak(String strSpeak) {
        if ((null == strSpeak) || (strSpeak.length() < 1)) {
            return;
        }
        if (!TcnShareUseData.getInstance().isVoicePrompt()) {
            return;
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "onTextSpeak() strSpeak: " + strSpeak);

        VoiceController.instance().speak(strSpeak);
    }

    private void OnSoldOut() {
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SOLD_OUT, -1, -1, -1, m_context.getString(R.string.notify_sold_out));
        onTextSpeak(m_context.getString(R.string.notify_sold_out));
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnSoldOut COMMAND_SOLD_OUT");
    }

    private void OnSoldOut(int slotNoOrKey) {
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SOLD_OUT, slotNoOrKey, -1, -1, m_context.getString(R.string.notify_sold_out));
        onTextSpeak(m_context.getString(R.string.notify_sold_out));
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnSoldOut COMMAND_SOLD_OUT slotNoOrKey: "+slotNoOrKey);
    }

    private void OnSystemBusy() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnSystemBusy");
        onTextSpeak(m_context.getString(R.string.notify_sys_busy));
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SYSTEM_BUSY,-1, -1, -1, m_context.getString(R.string.notify_sys_busy));
    }

    private void OnInvalidSlotNo() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnInvalidSlotNo");
        onTextSpeak(m_context.getString(R.string.notify_invalid_slot));
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_INVALID_SLOTNO,-1, -1, -1, m_context.getString(R.string.notify_invalid_slot));
    }

    private void OnInvalidSlotNo(int slotNo) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnInvalidSlotNo slotNo: "+slotNo);
        onTextSpeak(m_context.getString(R.string.notify_invalid_slot));
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_INVALID_SLOTNO,slotNo, -1, -1, m_context.getString(R.string.notify_invalid_slot));
    }

    private void OnFaultSlotNo(int slotNoOrKey) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnFaultSlotNo slotNoOrKey: "+slotNoOrKey);
        String speakText = m_context.getString(R.string.notify_slot_faultSlot) +String.valueOf(slotNoOrKey);;
        onTextSpeak(speakText);
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_FAULT_SLOTNO, slotNoOrKey, -1, -1, speakText);
    }

    private void OnFaultSlotNo(int slotNo,int errCode) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnFaultSlotNo slotNo: "+slotNo+" errCode: "+errCode);
        String speakText = null;
        if (slotNo > 0) {
            if (errCode > 0) {
                speakText = m_context.getString(R.string.notify_slot_fault) +String.valueOf(slotNo) + "\n"+m_context.getString(R.string.notify_slot_code)+String.valueOf(errCode);
                onTextSpeak(speakText);
            } else {
                speakText = m_context.getString(R.string.notify_slot_fault) +String.valueOf(slotNo);
                onTextSpeak(speakText);
            }
        } else {
            speakText = m_context.getString(R.string.notify_slot_faultSlot);
            onTextSpeak(speakText);
        }
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_FAULT_SLOTNO, slotNo, errCode, -1, speakText);
    }

    private void OnSelectFail(int slotNo,int errCode,int boardType) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnSelectFail slotNo: "+slotNo+" errCode: "+errCode+" boardType: "+boardType);
        String strMsg = getErrCodeMessageSpring(false,errCode);

        onTextSpeak(strMsg);
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SELECT_FAIL,slotNo, errCode, -1, strMsg);
    }

    private void OnShipping() {
        onTextSpeak(m_context.getString(R.string.notify_shipping));
    }

    private void OnUpdatePayTimer(int time) {
        if (time >= 0) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.UPDATE_PAY_TIME, time, -1, -1, null);
        } else {
            OnCloseTrade(true);
        }
    }


    private void OnSelectGoods(int position) {

        if (TcnVendIF.getInstance().isMachineLocked()) {
            onTextSpeak(m_context.getString(R.string.tip_machine_locked));
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_MACHINE_LOCKED, -1, -1, -1, m_context.getString(R.string.tip_machine_locked));
            return;
        }
        VendDBControl.getInstance().setSelecting(false);
        Coil_info info = VendDBControl.getInstance().getSelectGoods(TcnShareUseData.getInstance().isShowByGoodsCode(),
                TcnVendIF.getInstance().isShipGoodsByOrder(),position);
        if (!OnCheckCoilInfo(info)) {
            TcnVendIF.getInstance().LoggerError(TAG, "OnSelectGoods invaild");
            return;
        }
        VendProtoControl.getInstance().reqSelectSlotNo(info.getCoil_id());
    }

    private void OnSelectSlotNo(int slotNo) {

        if (TcnVendIF.getInstance().isMachineLocked()) {
            onTextSpeak(m_context.getString(R.string.tip_machine_locked));
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_MACHINE_LOCKED, -1, -1, -1, m_context.getString(R.string.tip_machine_locked));
            return;
        }
        VendDBControl.getInstance().setSelecting(false);
        Coil_info info = VendDBControl.getInstance().getShipGoodsSlotNo(TcnShareUseData.getInstance().isShowByGoodsCode(),
                TcnVendIF.getInstance().isShipGoodsByOrder(),slotNo);
        if (!OnCheckCoilInfo(info)) {
            return;
        }
        VendProtoControl.getInstance().reqSelectSlotNo(info.getCoil_id());
    }


    private void OnSelectedSlotNo(Coil_info info) {

        if (null == info) {
            TcnVendIF.getInstance().LoggerError(TAG, "OnSelectedSlotNo info is null");
            return;
        }
        if (!OnCheckCoilInfo(info)) {
            return;
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnSelectedSlotNo getCoil_id: "+info.getCoil_id());
        m_bHasScaned = false;

        VendDBControl.getInstance().setSelecting(true);

        TcnVendIF.getInstance().reqPlay(true,info.getAdUrl());  //播放选中的广告

        StringBuffer msgBfSpeak = new StringBuffer();
        if (TcnShareUseData.getInstance().isShowByGoodsCode() || (info.getKeyNum() > 0)) {
            if ((info.getPar_name() != null) && (info.getPar_name().length() > 0)) {
                msgBfSpeak.append(m_context.getString(R.string.speak_select_name));
                msgBfSpeak.append(info.getPar_name());
                msgBfSpeak.append(m_context.getString(R.string.speak_select_price));
                msgBfSpeak.append(info.getPar_price());
                msgBfSpeak.append(TcnShareUseData.getInstance().getUnitPrice());
            } else {
                msgBfSpeak.append(m_context.getString(R.string.speak_select));
                if (info.getKeyNum() > 0) {
                    msgBfSpeak.append(String.valueOf(info.getKeyNum()));
                    msgBfSpeak.append(m_context.getString(R.string.speak_key_price));
                } else {
                    msgBfSpeak.append(String.valueOf(info.getCoil_id()));
                    msgBfSpeak.append(m_context.getString(R.string.speak_slot_price));
                }

                msgBfSpeak.append(info.getPar_price());
                msgBfSpeak.append(TcnShareUseData.getInstance().getUnitPrice());
            }
        } else {
            msgBfSpeak.append(m_context.getString(R.string.speak_select));
            msgBfSpeak.append(String.valueOf(info.getCoil_id()));
            msgBfSpeak.append(m_context.getString(R.string.speak_slot_price));
            msgBfSpeak.append(info.getPar_price());
            msgBfSpeak.append(TcnShareUseData.getInstance().getUnitPrice());
        }
        onTextSpeak(msgBfSpeak.toString());

        VendDBControl.getInstance().setSelectCoilInfo(info);
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SELECT_GOODS, info.getCoil_id(), info.getKeyNum(), -1, info.getPar_price());


        boolean bStartTime = false;

        if (bStartTime) {
            startUpdatePayTimer();
        }
    }



    private void OnShipForTestSlot(int slotNo,int errCode, int shipStatus) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnShipForTestSlot slotNo: "+slotNo+" errCode: "+errCode+" shipStatus: "+shipStatus);
        if (TcnProtoResultDef.SHIP_SHIPING == shipStatus) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_TEST_SLOT, slotNo, errCode, TcnVendEventResultID.SHIP_SHIPING, null);
        } else if (TcnProtoResultDef.SHIP_SUCCESS == shipStatus) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_TEST_SLOT, slotNo, errCode, TcnVendEventResultID.SHIP_SUCCESS, null);
        } else if (TcnProtoResultDef.SHIP_FAIL == shipStatus) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_TEST_SLOT, slotNo, errCode, TcnVendEventResultID.SHIP_FAIL, null);
        } else {

        }
    }

    private void OnShipMessage(int boardType, int slotNo, int shipStatus) {
        if (TcnProtoResultDef.SHIP_SHIPING == shipStatus) {
            if (boardType == VendProtoControl.BOARD_SPRING) {
                onTextSpeak(m_context.getString(R.string.notify_shipping));
                TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SHIPPING, slotNo, -1, -1, m_context.getString(R.string.notify_shipping));
            }
        } else if (TcnProtoResultDef.SHIP_SUCCESS == shipStatus) {
            if (boardType == VendProtoControl.BOARD_SPRING) {
                onTextSpeak(m_context.getString(R.string.notify_shipsuc_rec_notify));
                TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SHIPMENT_SUCCESS, slotNo, -1, -1, m_context.getString(R.string.notify_shipsuc_rec_notify));
            }

        } else if (TcnProtoResultDef.SHIP_FAIL == shipStatus) {
            if (boardType == VendProtoControl.BOARD_SPRING) {
                onTextSpeak(m_context.getString(R.string.notify_fail_contact));
                TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.COMMAND_SHIPMENT_FAILURE, slotNo, -1, -1, m_context.getString(R.string.notify_fail_contact));
            }

        } else {

        }
    }

    private void OnShipSuccessHandle(Coil_info info,String shipMethod,String tradeNo) {
        if (null == info) {
            TcnVendIF.getInstance().LoggerDebug(TAG, "OnShipSuccessHandle info is null");
            return;
        }

    }

    private void OnShipFailureHandle(Coil_info info,String shipMethod, int errCode,String tradeNo) {
        if (null == info) {
            TcnVendIF.getInstance().LoggerDebug(TAG, "OnShipFailureHandle info is null");
            return;
        }
    }

    private void OnShipWithMethod(int shipMethod, int slotNo, int shipStatus, MsgToSend msgToSend) {

        VendDBControl.getInstance().setSelecting(false);

        if (null == msgToSend) {
            TcnVendIF.getInstance().LoggerError(TAG, "OnShipWithMethod slotNo: "+slotNo+" shipStatus: "+shipStatus+" shipMethod: "+shipMethod);
            return;
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnShip slotNo: "+slotNo+" shipStatus: "+shipStatus+" errCode: "+msgToSend.getErrCode()
                +" shipMethod: "+shipMethod+" PayMethod: "+msgToSend.getPayMethod()+" TradeNo: "+msgToSend.getTradeNo());
        Coil_info info = VendDBControl.getInstance().getCoilInfo(slotNo);
        if ((null == info) || (slotNo < 1)) {
            return;
        }
        stopUpdatePayTimer();
        if (TcnProtoResultDef.SHIP_SHIPING == shipStatus) {
            //
        } else if (TcnProtoResultDef.SHIP_SUCCESS == shipStatus) {
            VendDBControl.getInstance().OnShipSuccess(slotNo);
            OnShipSuccessHandle(info,msgToSend.getPayMethod(),msgToSend.getTradeNo());

        } else {    //出货失败
            VendDBControl.getInstance().OnShipFail(slotNo);
            OnShipFailureHandle(info,msgToSend.getPayMethod(),msgToSend.getErrCode(),msgToSend.getTradeNo());

        }

        OnShipMessage(msgToSend.getBoardType(),slotNo,shipStatus);

    }

    private void OnEndEffectiveTime() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnEndEffectiveTime()");
        stopUpdatePayTimer();
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.BACK_TO_SHOPPING, -1, -1, -1, null);
    }

    private void OnTestSlotNo(int start, int end) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "OnTestSlotNo start: "+start+" end: "+end);
        if (start < 1) {
            return;
        }
        if ((start == end) || (end < 1)) {
            reqShipTest(start,null);
            return;
        }
        CopyOnWriteArrayList<Integer> slotNoList = new CopyOnWriteArrayList<Integer>();
        Coil_info info = null;
        for (int i = start; i <= end; i++) {
            info = VendDBControl.getInstance().getCoilInfo(i);
            if ((info.getCoil_id() > 0) && (info.getWork_status() != 255)) {
                slotNoList.add(i);
            }
        }
        reqShipTest(start,slotNoList);
    }

    //升降机故障代码
    private String getErrCodeMessageSpring(boolean isSet,int errCode) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "getErrCodeMessageSpring() errCode: "+errCode);
        StringBuffer errMsg = new StringBuffer();
        if (errCode == VendProtoControl.ERROR_SPRING_CODE_0) {
            if (isSet) {
                errMsg.append(m_context.getString(R.string.drive_success));
            } else {
                errMsg.append(m_context.getString(R.string.drive_errcode_normal));
            }
            return errMsg.toString();
        }
        errMsg.append(m_context.getString(R.string.drive_errcode));
        errMsg.append(errCode);
        errMsg.append(" ");
        if (errCode == VendProtoControl.ERROR_SPRING_CODE_1) {
            errMsg.append(m_context.getString(R.string.spring_errcode_1));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_2) {
            errMsg.append(m_context.getString(R.string.spring_errcode_2));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_3) {
            errMsg.append(m_context.getString(R.string.spring_errcode_3));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_4) {
            errMsg.append(m_context.getString(R.string.spring_errcode_4));
        } else if (errCode == VendProtoControl.ERROR_CODE_16) {
            errMsg.append(m_context.getString(R.string.spring_errcode_16));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_17) {
            errMsg.append(m_context.getString(R.string.spring_errcode_17));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_18) {
            errMsg.append(m_context.getString(R.string.spring_errcode_18));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_19) {
            errMsg.append(m_context.getString(R.string.spring_errcode_19));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_32) {
            errMsg.append(m_context.getString(R.string.spring_errcode_32));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_33) {
            errMsg.append(m_context.getString(R.string.spring_errcode_33));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_34) {
            errMsg.append(m_context.getString(R.string.spring_errcode_34));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_35) {
            errMsg.append(m_context.getString(R.string.spring_errcode_35));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_48) {
            errMsg.append(m_context.getString(R.string.spring_errcode_48));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_49) {
            errMsg.append(m_context.getString(R.string.spring_errcode_49));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_50) {
            errMsg.append(m_context.getString(R.string.spring_errcode_50));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_51) {
            errMsg.append(m_context.getString(R.string.spring_errcode_51));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_64) {
            errMsg.append(m_context.getString(R.string.spring_errcode_64));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_65) {
            errMsg.append(m_context.getString(R.string.spring_errcode_65));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_66) {
            errMsg.append(m_context.getString(R.string.spring_errcode_66));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_67) {
            errMsg.append(m_context.getString(R.string.spring_errcode_67));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_80) {
            errMsg.append(m_context.getString(R.string.spring_errcode_80));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_81) {
            errMsg.append(m_context.getString(R.string.spring_errcode_81));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_82) {
            errMsg.append(m_context.getString(R.string.spring_errcode_82));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_83) {
            errMsg.append(m_context.getString(R.string.spring_errcode_83));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_84) {
            errMsg.append(m_context.getString(R.string.spring_errcode_84));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_86) {
            errMsg.append(m_context.getString(R.string.spring_errcode_86));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_87) {
            errMsg.append(m_context.getString(R.string.spring_errcode_87));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_90) {
            errMsg.append(m_context.getString(R.string.spring_errcode_90));
        } else if (errCode == VendProtoControl.ERROR_SPRING_CODE_91) {
            errMsg.append(m_context.getString(R.string.spring_errcode_91));
        } else if (errCode == VendProtoControl.ERROR_CODE_255) {
            errMsg.append(m_context.getString(R.string.drive_errcode_255));
        }
        else {

        }
        return errMsg.toString();
    }

    // 自动更新回调函数
    private UpdateManager.UpdateCallback m_appUpdateCb = new UpdateManager.UpdateCallback() {

        @Override
        public void checkUpdateCompleted(Boolean hasUpdate,
                                         CharSequence updateInfo) {
            TcnVendIF.getInstance().LoggerDebug(TAG, "downloadapk checkUpdateCompleted hasUpdate: "+hasUpdate);

            UpdateInfo mUpdateInfo = m_updateMan.getUpdataInfo();
            if (null == mUpdateInfo) {
                return;
            }
            String[] str = m_updateMan.getCurVerInfo();
            String mCurVerName = str[1];
            String mNewVerName = mUpdateInfo.getVersionName();
            TcnVendIF.getInstance().setNewVerionName(mNewVerName);
            if (mCurVerName.equals(mNewVerName)) {

                return;
            }
        }

        @Override
        public void downloadProgressChanged(int progress) {
//				TcnVendIF.getInstance().LoggerDebug("downloadapk downloadProgressChanged progress: "+progress);
//				if (m_updateProDialog != null && m_updateProDialog.isShowing()) {
//					m_updateProDialog.setProgress(progress);
//				}
        }

        @Override
        public void downloadCanceled() {
        }

        @Override
        public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {

        }

    };

    private class VendHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (TcnVendIF.getInstance().isVendNotUseDefault()) {
                TcnVendIF.getInstance().handleVendMessage(msg);
                return;
            }
            switch (msg.what) {
                case TcnVendCMDDef.HANDLER_THREAD_VEND_STARED:
                    break;
                case TcnVendCMDDef.HANDLER_THREAD_VEND_STARED_DELAY:
                    OnCheckUpdate();
                    if (!(TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())) {
                        int orientation = TcnVendIF.getInstance().getScreenOrientation();
                        if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
                            TcnShareUseData.getInstance().setFullScreen(true);
                        }
                    }
                    break;
                case TcnVendCMDDef.END_EFFECTIVETIME:
                    OnEndEffectiveTime();
                    break;
                case TcnVendCMDDef.WRITE_DATA_SHIP_TEST:
                    OnTestSlotNo(msg.arg1,msg.arg2);
                    break;
                case TcnVendCMDDef.CLOSE_TRADE:
                    OnCloseTrade((Boolean) msg.obj);
                    break;
                case TcnVendCMDDef.SELECT_GOODS_REQ:
                    OnSelectGoods(msg.arg1);
                    break;
                case TcnVendCMDDef.SELECT_SLOTNO:
                    OnSelectSlotNo(msg.arg1);
                    break;
                case TcnVendCMDDef.TEXT_SPEAK:
                    onTextSpeak((String) msg.obj);
                    break;
                case TcnVendCMDDef.UPDATE_PAY_TIME:
                    OnUpdatePayTimer(msg.arg1);
                    break;
                case TcnVendCMDDef.COMMAND_SHIPPING:
                    OnShipping();
                    break;
                case RemoutAdvertControl.CMD_REMOUT_ADVERT:
               //     OnDownLoadAdvert(msg.arg2,(JSONObject)msg.obj);
                    break;
                case FtpControl.CMD_DOWNLOAD_SUCCESS:
               //     OnDownLoadAdvertSuccess((String)msg.obj);
                    break;
                case FtpControl.CMD_DOWNLOAD_FAIL:
                    break;
                case TcnVendCMDDef.TEMPERATURE_INFO:
                    String temper = (String)msg.obj;
                    if ((temper != null) && (temper.length() > 0)) {
                        temper = m_context.getString(R.string.current_temperature)+temper+"℃";
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TEMPERATURE_INFO, -1, -1, -1, temper);
                    } else {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TEMPERATURE_INFO, -1, -1, -1, "");
                    }
                    if ((temper != null) && (temper.length() > 0) && (!temper.equals(m_strTemp))) {
                       // TcnVendIF.getInstance().sendMessageTempServer(temper);
                    }
                    m_strTemp = (String)msg.obj;
                    break;
                case TcnVendCMDDef.REQ_SHIP:
                    String methodAmountTradeNo = (String)msg.obj;
                    TcnVendIF.getInstance().LoggerDebug(TAG, "REQ_SHIP arg1: "+msg.arg1+" methodAmountTradeNo: "+methodAmountTradeNo);
                    if ((methodAmountTradeNo != null) && (methodAmountTradeNo.contains("|"))) {
                        String[] strarr = methodAmountTradeNo.split("\\|");
                        if (strarr.length == 3) {
                            String method = strarr[0];
                            String amount = strarr[1];
                            String tradeNo = strarr[2];
                            ship(msg.arg1,method,amount,tradeNo);
                        }
                    }
                    break;
                default:
                    break;
            }
            TcnVendIF.getInstance().handleVendMessage(msg);
        }
    }

    private class DBHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (TcnVendIF.getInstance().isDBNotUseDefault()) {
                TcnVendIF.getInstance().handleDBMessage(msg);
                return;
            }
            switch (msg.what) {
                case TcnDBDef.REQ_QUERY_ALIVE_COIL:
                    boolean bNeedUpToServer = ((Boolean)msg.obj).booleanValue();
                    VendDBControl.getInstance().OnQueryAliveCoil(bNeedUpToServer);
                    break;
                case TcnDBDef.QUERY_ALIVE_COIL:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.QUERY_ALIVE_COIL, msg.arg1, -1, -1, null);
                    break;
                case TcnDBDef.REQ_QUERY_ALIVE_COIL_EXCEPTIONE:
                    VendDBControl.getInstance().OnQueryAliveExceptType((List<String>)(msg.obj));
                    break;
                case TcnDBDef.QUERY_ALIVE_COIL_EXCEPTIONE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.QUERY_ALIVE_COIL_EXCEPTIONE, msg.arg1, -1, -1, null);
                    break;
                case TcnDBDef.REQ_QUERY_ALIVE_GOODS:
                    VendDBControl.getInstance().OnQueryAliveGoods();
                    break;
                case TcnDBDef.QUERY_ALIVE_GOODS:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.QUERY_ALIVE_GOODS, msg.arg1, -1, -1, null);
                    break;
                case TcnDBDef.REQ_DELETE_TYPE:
                    VendDBControl.getInstance().OnQueryDeleteType((String)msg.obj);
                    break;
                case TcnDBDef.DELETE_TYPE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.DELETE_TYPE, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_INSERT_DATA:
                    VendDBControl.getInstance().OnInsertData(msg.arg1,(ContentValues)msg.obj);
                    break;
                case TcnDBDef.INSERT_DATA:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.INSERT_DATA, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_UPTE_DATA:
                    VendDBControl.getInstance().OnUpdateData(msg.arg1,msg.arg2,(ContentValues)msg.obj);
                    break;
                case TcnDBDef.UPTE_DATA:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.UPTE_DATA, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_DELETE_DATA:
                    VendDBControl.getInstance().OnDeleteData(msg.arg1,msg.arg2);
                    break;
                case TcnDBDef.DELETE_DATA:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.DELETE_DATA, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_MODIFY_TYPE:
                    VendDBControl.getInstance().OnQueryModifyType(msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.MODIFY_TYPE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.MODIFY_TYPE, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_ADD_SHOW_COIL_ID:
                    VendDBControl.getInstance().addShowSlotNo(msg.arg1,msg.arg2);
                    break;
                case TcnDBDef.ADD_SHOW_COIL_ID:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.ADD_SHOW_COIL_ID, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_DELETE_COIL_ID:
                    VendDBControl.getInstance().deleteSlotNo(msg.arg1);
                    break;
                case TcnDBDef.DELETE_COIL_ID:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.DELETE_COIL_ID, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_DELETE_KEY_MAP:
                    VendDBControl.getInstance().deleteKeyMap(msg.arg1);
                    break;
                case TcnDBDef.DELETE_KEY_MAP:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.DELETE_KEY_MAP, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_ADD_STOCK:
                    VendDBControl.getInstance().addStock(msg.arg1);
                    break;
                case TcnDBDef.ADD_STOCK:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.ADD_STOCK, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_SUB_STOCK:
                    VendDBControl.getInstance().subStock(msg.arg1);
                    break;
                case TcnDBDef.SUB_STOCK:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SUB_STOCK, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_DELETE_GOODS_ID:
                    VendDBControl.getInstance().OnDeleteGoodsId((String) msg.obj);
                    break;
                case TcnDBDef.DELETE_GOODS_ID:
                    if (msg.arg1 > -1) {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.DELETE_GOODS_ID, msg.arg1, TcnVendEventResultID.SUCCESS, -1, (String)msg.obj);
                    } else {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.DELETE_GOODS_ID, msg.arg1, TcnVendEventResultID.FAIL, -1, (String)msg.obj);
                    }
                    break;
                case TcnDBDef.REQ_FILL_STOCK_TO_CAPACITY_ALL:
                    VendDBControl.getInstance().OnFillStockToCapacityAll();
                    break;
                case TcnDBDef.FILL_STOCK_TO_CAPACITY_ALL:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.FILL_STOCK_TO_CAPACITY_ALL, msg.arg1, -1, -1, null);
                    break;
                case TcnDBDef.REQ_FILL_STOCK_TO_CAPACITY:
                    VendDBControl.getInstance().OnFillStockToCapacity(msg.arg1,msg.arg2);
                    break;
                case TcnDBDef.FILL_STOCK_TO_CAPACITY:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.FILL_STOCK_TO_CAPACITY, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_PRICE:
                    VendDBControl.getInstance().OnUpdateSlotPrice(true,msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_PRICE:
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_SALE_PRICE:
                    VendDBControl.getInstance().OnUpdateSlotSalePrice(true,msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_SALE_PRICE:
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_NAME:
                    VendDBControl.getInstance().OnUpdateSlotName(true,msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_NAME:
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_TYPE:
                    VendDBControl.getInstance().OnUpdateSlotType(true,msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_TYPE:
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_INTROdDUCE:
                    VendDBControl.getInstance().OnUpdateSlotIntroduce(true,msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_INTROdDUCE:
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_IMGURL:
                    VendDBControl.getInstance().OnUpdateSlotImageUrl(true,msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_IMGURL:
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_ADURL:
                    VendDBControl.getInstance().OnUpdateSlotAdUrl(true,msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_ADURL:
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_SPEC:
                    VendDBControl.getInstance().OnUpdateSlotSpec(true,msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_SPEC:
                    break;
                case TcnDBDef.REQ_UPDATE_GOODS_CAPACITY:
                    VendDBControl.getInstance().OnUpdateGoodsCapacity(true,msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.UPDATE_GOODS_CAPACITY:
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_GOODSCODE:
                    VendDBControl.getInstance().OnUpdateSlotGoodsCode(true,msg.arg1,msg.arg2,(String)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_GOODSCODE:
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_EXTQUANTITY:
                    VendDBControl.getInstance().OnUpdateSlotExtantQuantity(true,msg.arg1,msg.arg2,(Integer) msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_EXTQUANTITY:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.UPDATE_SLOTNO_EXTQUANTITY, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_UPDATE_SLOTNO_CAPACITY:
                    VendDBControl.getInstance().OnUpdateSlotCapacity(true,msg.arg1,msg.arg2,(Integer)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOTNO_CAPACITY:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.UPDATE_SLOTNO_CAPACITY, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.REQ_UPDATE_SLOT_WORK_STATUS:
                    VendDBControl.getInstance().OnUpdateSlotWorkStatus(false,msg.arg1,msg.arg2,(Integer)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOT_WORK_STATUS:
                    break;
                case TcnDBDef.REQ_UPDATE_SLOT_STATUS:
                    VendDBControl.getInstance().OnUpdateSlotStatus(true,msg.arg1,msg.arg2,(Integer)msg.obj);
                    break;
                case TcnDBDef.UPDATE_SLOT_STATUS:
                    break;
                case TcnDBDef.REQ_ADD_SLOT_GOODS:
                    VendDBControl.getInstance().OnAddSlotGoods(msg.arg1,msg.arg2,(Goods_info) msg.obj);
                    break;
                case TcnDBDef.ADD_SLOT_GOODS:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.ADD_SLOT_GOODS, msg.arg1, -1, -1, null);
                    break;
                case TcnDBDef.REQ_QUERY_SLOT_FAULTS:
                   // VendDBControl.getInstance().OnQuerySlotsFaults();
                    break;
                case TcnDBDef.QUERY_SLOT_FAULTS:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_QUERY_SLOT_FAULTS, msg.arg1, -1, -1, null);
                    break;
                case TcnDBDef.REQ_CLEAR_SLOT_FAULTS:
                    VendDBControl.getInstance().OnClearSlotsFaults(msg.arg1,msg.arg2);
                    break;
                case TcnDBDef.CLEAR_SLOT_FAULTS:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_CLEAR_SLOT_FAULTS, msg.arg1, msg.arg2, -1, null);
                    break;
                case TcnDBDef.SLOTNO_INVALID:
                    OnInvalidSlotNo(msg.arg1);
                    break;
                case TcnDBDef.SLOTNO_FAULT:
                    OnFaultSlotNo(msg.arg1,msg.arg2);
                    break;
                case TcnDBDef.SLOTNO_SOLD_OUT:
                    OnSoldOut();
                    break;
                case TcnDBDef.REQ_UPDATE_HEATTIME:
                    VendDBControl.getInstance().OnUpdateHeatTime(true,msg.arg1,msg.arg2,(Integer) msg.obj);
                    break;
                case TcnDBDef.UPDATE_HEATTIME:
                    break;
                case TcnDBDef.REQ_UPDATE_COLUMN:
                    VendDBControl.getInstance().OnUpdateColumn(true,msg.arg1,msg.arg2,(Integer) msg.obj);
                    break;
                case TcnDBDef.UPDATE_COLUMN:
                    break;
                case TcnDBDef.REQ_UPDATE_ROW:
                    VendDBControl.getInstance().OnUpdateRow(true,msg.arg1,msg.arg2,(Integer) msg.obj);
                    break;
                case TcnDBDef.UPDATE_ROW:
                    break;
                case TcnDBDef.REQ_UPDATE_PULL_BACK:
                    VendDBControl.getInstance().OnUpdatePullBack(true,msg.arg1,msg.arg2,(Integer) msg.obj);
                    break;
                case TcnDBDef.UPDATE_PULL_BACK:
                    break;
                case TcnDBDef.CHANGED_ALIVE_COIL:

                    break;
                default:
                    break;
            }
            TcnVendIF.getInstance().handleDBMessage(msg);
        }
    }


    private class TCNCommunicationHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (TcnVendIF.getInstance().isCommunicationNotUseDefault()) {
                TcnVendIF.getInstance().handleCommunicationMessage(msg);
                return;
            }
            switch (msg.what) {
                case TcnProtoDef.SERIAL_PORT_CONFIG_ERROR:
                    TcnVendIF.getInstance().LoggerDebug(TAG, "SERIAL_PORT_CONFIG_ERROR");
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SERIAL_PORT_CONFIG_ERROR, -1, -1, -1, null);
                    break;
                case TcnProtoDef.SERIAL_PORT_SECURITY_ERROR:
                    TcnVendIF.getInstance().LoggerDebug(TAG, "SERIAL_PORT_SECURITY_ERROR");
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SERIAL_PORT_SECURITY_ERROR, -1, -1, -1, null);
                    break;
                case TcnProtoDef.SERIAL_PORT_UNKNOWN_ERROR:
                    TcnVendIF.getInstance().LoggerDebug(TAG, "SERIAL_PORT_UNKNOWN_ERROR");
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SERIAL_PORT_UNKNOWN_ERROR, -1, -1, -1, null);
                    break;
                case TcnProtoDef.CMD_LOOP:

                    break;
                case TcnProtoDef.COMMAND_SELECT_SLOTNO:
                    OnSelectedSlotNo(VendDBControl.getInstance().getSelectSlotNo(msg.arg1));
                    break;
                case TcnProtoDef.COMMAND_INVALID_SLOTNO:
                    OnInvalidSlotNo(msg.arg1);
                    break;
                case TcnProtoDef.COMMAND_SELECT_FAIL:
                    OnSelectFail(msg.arg1,msg.arg2,(Integer) msg.obj);
                    break;

                case TcnProtoDef.COMMAND_SHIPMENT_CASHPAY:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_WECHATPAY:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_ALIPAY:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_GIFTS:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_REMOTE:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_VERIFY:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_BANKCARD_ONE:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_BANKCARD_TWO:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_TCNCARD_OFFLINE:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_TCNCARD_ONLINE:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SHIPMENT_OTHER_PAY:
                    OnShipWithMethod(msg.what,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case TcnProtoDef.COMMAND_SLOTNO_INFO:
                    VendDBControl.getInstance().OnUploadSlotNoInfo((boolean)msg.obj,msg.arg1,msg.arg2);
                    break;
                case TcnProtoDef.COMMAND_SLOTNO_INFO_SINGLE:
                    VendDBControl.getInstance().OnUploadSlotNoInfoSingle((boolean)msg.obj,msg.arg1,msg.arg2);
                    break;
                case TcnProtoDef.COMMAND_BUSY:
                    OnSystemBusy();
                    break;
                case TcnProtoDef.REQ_CMD_TEST_SLOT:
                    VendProtoControl.getInstance().reqShipTest(msg.arg1,(CopyOnWriteArrayList<Integer>)msg.obj);

                    break;
                case TcnProtoDef.CMD_TEST_SLOT:
                    OnShipForTestSlot(msg.arg1,msg.arg2,(Integer)msg.obj);
                    break;
                case TcnProtoDef.REQ_QUERY_SLOT_STATUS:
                    VendProtoControl.getInstance().reqQuerySlotExists(msg.arg1);
                    break;
                case TcnProtoDef.QUERY_SLOT_STATUS:

                    if (msg.obj != null) {
                        int bQType = ((Integer) msg.obj).intValue();
                        if (VendProtoControl.BOARD_SPRING == bQType) {
                            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_QUERY_SLOT_STATUS, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(false,msg.arg2));
                        }
                    }
                    break;
                case TcnProtoDef.REQ_SELF_CHECK:
                    VendProtoControl.getInstance().selfCheck(msg.arg1);
                    break;
                case TcnProtoDef.SELF_CHECK:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SELF_CHECK, msg.arg1, -1, -1, getErrCodeMessageSpring(false,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_RESET:

                    break;
                case TcnProtoDef.CMD_RESET:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_RESET, msg.arg1, -1, -1, getErrCodeMessageSpring(false,msg.arg1));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_SPRING:
                    VendProtoControl.getInstance().setSlotSpring(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_SPRING:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_SPRING, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg2));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_BELTS:
                    VendProtoControl.getInstance().setSlotBelt(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_BELTS:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_BELTS, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg2));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_ALL_SPRING:
                    VendProtoControl.getInstance().setSlotAllSpring(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_ALL_SPRING:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_ALL_SPRING, msg.arg1, -1, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_ALL_BELT:
                    VendProtoControl.getInstance().setSlotAllBelt(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_ALL_BELT:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_ALL_BELT, msg.arg1, -1, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_SINGLE:
                    VendProtoControl.getInstance().setSingleSlotno(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_SINGLE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_SINGLE, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg2));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_DOUBLE:
                    VendProtoControl.getInstance().setDoubleSlotno(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_DOUBLE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_DOUBLE, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg2));
                    break;
                case TcnProtoDef.REQ_SET_SLOTNO_ALL_SINGLE:
                    VendProtoControl.getInstance().setAllSlotnoSingle(msg.arg1);
                    break;
                case TcnProtoDef.SET_SLOTNO_ALL_SINGLE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.SET_SLOTNO_ALL_SINGLE, msg.arg1, -1, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_SET_TEST_MODE:
                    VendProtoControl.getInstance().setTestMode(msg.arg1);
                    break;
                case TcnProtoDef.REQ_SET_TEMP_CONTROL_OR_NOT:
                    break;
                case TcnProtoDef.SET_TEMP_CONTROL_OR_NOT:
                    break;
                case TcnProtoDef.REQ_CMD_SET_COOL:
                    VendProtoControl.getInstance().reqCoolOpen(msg.arg1,VendProtoControl.BOARD_SPRING);
                    break;
                case TcnProtoDef.CMD_SET_COOL:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SET_COOL, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_SET_HEAT:
                    VendProtoControl.getInstance().reqHeatOpen(msg.arg1,VendProtoControl.BOARD_SPRING);
                    break;
                case TcnProtoDef.CMD_SET_HEAT:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SET_HEAT, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_SET_TEMP:
                    VendProtoControl.getInstance().setTemp(msg.arg1,msg.arg2);
                    break;
                case TcnProtoDef.CMD_SET_TEMP:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SET_TEMP, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_SET_GLASS_HEAT_OPEN:
                    VendProtoControl.getInstance().setGlassHeatEnable(msg.arg1,true);
                    break;
                case TcnProtoDef.CMD_SET_GLASS_HEAT_OPEN:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SET_GLASS_HEAT_OPEN, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_SET_GLASS_HEAT_CLOSE:
                    VendProtoControl.getInstance().setGlassHeatEnable(msg.arg1,false);
                    break;
                case TcnProtoDef.CMD_SET_GLASS_HEAT_CLOSE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SET_GLASS_HEAT_CLOSE, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_READ_CURRENT_TEMP:
                    break;
                case TcnProtoDef.CMD_READ_CURRENT_TEMP:
                    String temper = (String)msg.obj;
                    if ((temper != null) && (temper.length() > 0) && (temper.length() > 0)) {
                        StringBuffer msgBf = new StringBuffer();
                        msgBf.append(m_context.getString(R.string.current_temperature));
                        msgBf.append(temper);
                        msgBf.append("℃");
                        temper = msgBf.toString();
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TEMPERATURE_INFO, -1, -1, -1, temper);
                    } else {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TEMPERATURE_INFO, -1, -1, -1, "");
                    }
                    if ((temper != null) && (temper.length() > 0) && (!temper.equals(m_strTemp))) {
                        // TcnVendIF.getInstance().sendMessageTempServer(temper);
                    }
                    m_strTemp = (String)msg.obj;
                    break;
                case TcnProtoDef.CMD_READ_TEMP:
                    m_strTotalTemp = (String)msg.obj;
                    break;
                case TcnProtoDef.REQ_CMD_SET_COOL_HEAT_CLOSE:
                    VendProtoControl.getInstance().reqCoolAndHeatClose(msg.arg1,VendProtoControl.BOARD_SPRING);
                    break;
                case TcnProtoDef.CMD_SET_COOL_HEAT_CLOSE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SET_COOL_HEAT_CLOSE, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_SET_LIGHT_OPEN:
                    VendProtoControl.getInstance().setLightControl(msg.arg1,true);
                    break;
                case TcnProtoDef.CMD_SET_LIGHT_OPEN:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SET_LIGHT_OPEN, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_SET_LIGHT_CLOSE:
                    VendProtoControl.getInstance().setLightControl(msg.arg1,false);
                    break;
                case TcnProtoDef.CMD_SET_LIGHT_CLOSE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SET_LIGHT_CLOSE, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_SET_BUZZER_OPEN:
                    VendProtoControl.getInstance().setBuzzerControl(msg.arg1,true);
                    break;
                case TcnProtoDef.CMD_SET_BUZZER_OPEN:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SET_BUZZER_OPEN, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_SET_BUZZER_CLOSE:
                    VendProtoControl.getInstance().setBuzzerControl(msg.arg1,false);
                    break;
                case TcnProtoDef.CMD_SET_BUZZER_CLOSE:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_SET_BUZZER_CLOSE, msg.arg1, msg.arg2, -1, getErrCodeMessageSpring(true,msg.arg1));
                    break;
                case TcnProtoDef.REQ_CMD_READ_DOOR_STATUS:
                    break;
                case TcnProtoDef.CMD_READ_DOOR_STATUS:
                    if (TcnProtoResultDef.DOOR_CLOSE == msg.arg1) {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_READ_DOOR_STATUS, TcnVendEventResultID.DO_CLOSE, msg.arg2, -1, null);
                    } else {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_READ_DOOR_STATUS, TcnVendEventResultID.DO_OPEN, msg.arg2, -1, null);
                    }
                    break;
                default:
                    break;
            }
            TcnVendIF.getInstance().handleCommunicationMessage(msg);
        }
    }
}
