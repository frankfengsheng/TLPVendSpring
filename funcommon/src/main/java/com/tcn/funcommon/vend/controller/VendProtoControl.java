package com.tcn.funcommon.vend.controller;

import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.PayMethod;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.TcnUtility;
import com.tcn.funcommon.vend.def.TcnProtoDef;
import com.tcn.funcommon.vend.def.TcnProtoResultDef;
import com.tcn.funcommon.vend.protocol.DriveControl.BoardGroupControl;
import com.tcn.funcommon.vend.protocol.DriveControl.DriveControl;
import com.tcn.funcommon.vend.protocol.DriveControl.GroupInfo;
import com.tcn.funcommon.vend.protocol.MsgKey;
import com.tcn.funcommon.vend.protocol.MsgToSend;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android_serialport_api.SerialPortController;

/**
 * Created by Administrator on 2017/6/2.
 */
public class VendProtoControl {
    private static final String TAG = "VendProtoControl";
    private static VendProtoControl m_Instance = null;

    public static final int BOARD_SPRING               = 5;    //弹簧机


    public static final int ERROR_CODE_BUSY   = -10;

    public static final int ERROR_SPRING_CODE_0             = 0;   //正常
    public static final int ERROR_SPRING_CODE_1             = 1;   //光电开关在没有发射的情况下也有信号输出
    public static final int ERROR_SPRING_CODE_2             = 2;    //发射有改变的时候 也没有信号输出
    public static final int ERROR_SPRING_CODE_3             = 3;     //出货时一直有输出信号 不能判断好坏
    public static final int ERROR_SPRING_CODE_4             = 4;    //没有检测到出货
    public static final int ERROR_CODE_16             = 16;   //P型MOS管有短路
    public static final int ERROR_SPRING_CODE_17             = 17;    //P型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERROR_SPRING_CODE_18             = 18;    //P型MOS管有短路; 发射有改变的时候 也没有信号输出;
    public static final int ERROR_SPRING_CODE_19             = 19;    //P型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
    public static final int ERROR_SPRING_CODE_32             = 32;    //N型MOS管有短路;
    public static final int ERROR_SPRING_CODE_33             = 33;    //N型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERROR_SPRING_CODE_34             = 34;    //N型MOS管有短路; 发射有改变的时候 也没有信号输出;
    public static final int ERROR_SPRING_CODE_35             = 35;    //N型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
    public static final int ERROR_SPRING_CODE_48             = 48;    //电机短路;
    public static final int ERROR_SPRING_CODE_49             = 49;    //电机短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERROR_SPRING_CODE_50             = 50;    //电机短路; 发射有改变的时候 也没有信号输出;
    public static final int ERROR_SPRING_CODE_51             = 51;    //电机短路;出货时一直有输出信号 不能判断好坏;
    public static final int ERROR_SPRING_CODE_64             = 64;    //电机断路;
    public static final int ERROR_SPRING_CODE_65             = 65;    //电机断路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERROR_SPRING_CODE_66             = 66;    //电机断路; 发射有改变的时候 也没有信号输出;
    public static final int ERROR_SPRING_CODE_67             = 67;    //电机断路;出货时一直有输出信号 不能判断好坏;
    public static final int ERROR_SPRING_CODE_80             = 80;    //RAM出错,电机转动超时。
    public static final int ERROR_SPRING_CODE_81             = 81;    //在规定时间内没有接收到回复数据 表明驱动板工作不正常或者与驱动板连接有问题。
    public static final int ERROR_SPRING_CODE_82             = 82;    //接收到数据不完整。
    public static final int ERROR_SPRING_CODE_83             = 83;    //校验不正确。
    public static final int ERROR_SPRING_CODE_84             = 84;    //地址不正确。
    public static final int ERROR_SPRING_CODE_86             = 86;    //货道不存在。
    public static final int ERROR_SPRING_CODE_87             = 87;    //返回故障代码有错超出范围。
    public static final int ERROR_SPRING_CODE_90             = 90;    //连续多少次转动正常但没检测到商品售出。
    public static final int ERROR_SPRING_CODE_91             = 91;    //其它故障小于。


    public static final int ERROR_CODE_255             = 255;    //货道号不存在

    private volatile boolean m_bShiping = false;
    private volatile boolean m_isTestingSlotNo = false;
    private volatile Handler m_ReceiveHandler = null;
    private volatile Handler m_SendHandler = null;

    private volatile String m_strDataType = TcnConstant.DATA_TYPE[1];

    private volatile int m_iCloseDelaySeconds = 120;
    private volatile int m_iCurrentKey = -1;
    private volatile long m_lSnakeLastTimeModify = 0;

    private volatile String m_strTemp1 = null;
    private volatile String m_strTemp2 = null;
    private volatile String m_strTemp3 = null;

    private CopyOnWriteArrayList<Integer> m_slotNoTestList = null;
    private CopyOnWriteArrayList<Integer> m_lactticeOffList = null;
    private CopyOnWriteArrayList<Integer> m_columSetList = null;
    private CopyOnWriteArrayList<Integer> m_slotOpenedList = null;
    private CopyOnWriteArrayList<Integer> m_slotSnakeList = null;
    private CopyOnWriteArrayList<MsgKey> m_keyStatusList = null;
    private CopyOnWriteArrayList<MsgKey> m_keyStatusAllList = null;



    public static synchronized VendProtoControl getInstance() {
        if (null == m_Instance) {
            m_Instance = new VendProtoControl();
        }
        return m_Instance;
    }

    public void initialize(String board1,String board2,String board3,String board4,String group1,String group2,String group3,String group4,Handler sendHandler) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "initialize board1: "+board1+" board2: "+board2+" board3: "+board3+" board4: "+board4+" group1: "+group1
                +" group2: "+group2+" group3: "+group3+" group4: "+group4);
        m_SendHandler = sendHandler;
        m_ReceiveHandler = new CommunicationHandler();
        m_strDataType = TcnShareUseData.getInstance().getTcnDataType();
        BoardGroupControl.getInstance().initialize(board1,board2,board3,board4,group1,group2,group3,group4);
        if ((TcnConstant.DEVICE_CONTROL_TYPE[5]).equals(board1)) {
            DriveControl.getInstance().init(m_ReceiveHandler);
        }

        reqSlotNoInfoOpenSerialPort();
    }

    public int getStartSlotNo(int grpId) {
        return BoardGroupControl.getInstance().getStartSlotNo(grpId);
    }

    public String[] getBoardGroupNumberArr() {
        return BoardGroupControl.getInstance().getBoardGroupNumberArr();
    }

    public String[] getBoardLatticeGroupNumberArr() {
        return BoardGroupControl.getInstance().getBoardLatticeGroupNumberArr();
    }

    public void setShiping(boolean shiping) {
        m_bShiping = shiping;
    }
    public boolean isShiping() {
        return m_bShiping;
    }

    public void setCloseDelaySeconds(int delaySeconds) {
        m_iCloseDelaySeconds = delaySeconds;
    }
    public int getCloseDelaySeconds() {
        return m_iCloseDelaySeconds;
    }

    public void setSendHandler(Handler handler) {
        m_SendHandler = handler;
    }

    public boolean isDoorOpen() {

        return DriveControl.getInstance().isDoorOpen();
    }

    public void setDoorOpen(boolean open) {
        DriveControl.getInstance().setDoorOpen(open);
    }

    private void openSerialPort() {
        SerialPortController.getInstance().setHandler(m_ReceiveHandler);
        int iFirstType = BoardGroupControl.getInstance().getGroupFirstType();
        SerialPortController.getInstance().openSerialPort(iFirstType,"MAINDEVICE", "MAINBAUDRATE");
        if (BoardGroupControl.getInstance().hasGroupSecond()) {
            SerialPortController.getInstance().setHandlerNew(m_ReceiveHandler);
            int iSecondType = BoardGroupControl.getInstance().getGroupSecondType();
            SerialPortController.getInstance().openSerialPortNew(iSecondType,"SERVERDEVICE", "MAINBAUDRATE");
        }
        if (BoardGroupControl.getInstance().hasGroupThird()) {
            SerialPortController.getInstance().setHandlerThird(m_ReceiveHandler);
            int iThirdType = BoardGroupControl.getInstance().getGroupThirdType();
            SerialPortController.getInstance().openSerialPortThird(iThirdType,"DEVICETHIRD", "MAINBAUDRATE");
        }
        if (BoardGroupControl.getInstance().hasGroupFourth()) {
            SerialPortController.getInstance().setHandlerFourth(m_ReceiveHandler);
            int iFourthType = BoardGroupControl.getInstance().getGroupFourthType();
            SerialPortController.getInstance().openSerialPortFourth(iFourthType,"DEVICEFOURTH", "MAINBAUDRATE");
        }
    }

    public void reqSlotNoInfo() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "reqSlotNoInfo");
        if (BoardGroupControl.getInstance().hasSprMachine()) {
            GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
            if ((mGroupInfo != null) && (mGroupInfo.getID() >= 0)) {
                int iStartSlotNo = BoardGroupControl.getInstance().getFirstSlotNo(BoardGroupControl.BOARD_SPRING);
                int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(iStartSlotNo);
                byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
                TcnVendIF.getInstance().LoggerDebug(TAG, "reqSlotNoInfo Spr iStartSlotNo: "+iStartSlotNo+" addrSlotNo: "+addrSlotNo+" SerGrpNo: "+mGroupInfo.getSerGrpNo());
                DriveControl.getInstance().sendCmdGetData(false,mGroupInfo.getSerGrpNo(),iStartSlotNo,addrSlotNo,bBoardGrpNo);
            } else {
                TcnVendIF.getInstance().LoggerError(TAG, "reqSlotNoInfo mGroupInfo: "+mGroupInfo);
            }

        }
        else {
            sendReceiveData(TcnProtoDef.COMMAND_SLOTNO_INFO,-1,-1,true);
        }

    }


    public void reqSlotNoInfoOpenSerialPort() {
        openSerialPort();
        reqSlotNoInfo();
    }

    public boolean haveDoorSwitch() {
        return DriveControl.getInstance().haveDoorSwitch();
    }

    public void setSlotOpenedList(CopyOnWriteArrayList<Integer> openedList) {
        if (null == openedList) {
            return;
        }
        m_slotOpenedList = openedList;
    }


    public void reqShipTest(int slotNo,CopyOnWriteArrayList<Integer> slotNoList) {

        if ((null == slotNoList) || (slotNoList.size() < 1)) {
            if (slotNo > 0) {
                if (m_slotNoTestList != null) {
                    m_slotNoTestList.clear();
                }
                reqWriteDataShipTest(slotNo);
            }
            return;
        }
        reqWriteDataShipTest(-1,-1,-1,-1,slotNoList);
    }

    public void reqShipTest(int slotNo,int heatTime,int row,int column,int back,CopyOnWriteArrayList<Integer> slotNoList) {

        if ((null == slotNoList) || (slotNoList.size() < 1)) {
            if (slotNo > 0) {
                if (m_slotNoTestList != null) {
                    m_slotNoTestList.clear();
                }
                reqWriteDataShipTest(slotNo,heatTime,row,column,back);
            }
            return;
        }
        reqWriteDataShipTest(heatTime,row,column,back,slotNoList);
    }

    private void cleanShipTestList() {
        m_isTestingSlotNo = false;
        if (m_slotNoTestList != null) {
            m_slotNoTestList.clear();
        }
    }

    private void reqWriteDataShipTest(int slotNo) {
        m_isTestingSlotNo = true;
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            return;
        }
        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(slotNo);
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());

        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().shipTest(TcnShareUseData.getInstance().isDropSensorCheck(),mGroupInfo.getSerGrpNo(),slotNo,addrSlotNo,bBoardGrpNo);
        }
    }

    private void reqWriteDataShipTest(int slotNo,int heatTime,int row,int column,int back) {
        m_isTestingSlotNo = true;
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            return;
        }
        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(slotNo);
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());

        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().shipTest(TcnShareUseData.getInstance().isDropSensorCheck(),mGroupInfo.getSerGrpNo(),slotNo,addrSlotNo,bBoardGrpNo);
        }
    }


    private void reqWriteDataShipTest(int heatTime,int row,int column,int back, CopyOnWriteArrayList<Integer> slotNoList) {
        if ((null == slotNoList) || (slotNoList.size() < 1)) {
            return;
        }
        m_slotNoTestList = slotNoList;
        m_isTestingSlotNo = true;
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNoList.get(0));
        if (null == mGroupInfo) {
            return;
        }
        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(slotNoList.get(0));
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().shipTest(TcnShareUseData.getInstance().isDropSensorCheck(),mGroupInfo.getSerGrpNo(),slotNoList.get(0),addrSlotNo,bBoardGrpNo);
        }
    }


    public void reqSelectSlotNo(int slotNo) {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            return;
        }
        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(slotNo);
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        TcnVendIF.getInstance().LoggerDebug(TAG, "reqSelectSlotNo() slotNo: "+slotNo+" addrSlotNo: "+addrSlotNo+" bBoardGrpNo: "+bBoardGrpNo+" getSerGrpNo: "+mGroupInfo.getSerGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().reqSelectSlotNo(mGroupInfo.getSerGrpNo(),slotNo,addrSlotNo,bBoardGrpNo);
        }
    }

    public List<GroupInfo> getGroupListAll() {
        return BoardGroupControl.getInstance().getGroupListAll();
    }

    public List<GroupInfo> getGroupListSpring() {
        return BoardGroupControl.getInstance().getGroupListSpring();
    }

    public List<Integer> getMachineGroupListAll() {
        return BoardGroupControl.getInstance().getMachineGroupListAll();
    }

    public List<Integer> getMachineGroupListSpring() {
        return BoardGroupControl.getInstance().getMachineGroupListSpring();
    }

    public List<Integer> getMachineGroupListLattice() {
        return BoardGroupControl.getInstance().getMachineGroupListLattice();
    }

    public GroupInfo getMachineGroupInfo(int grpId) {
        return BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
    }

    /************************************************ 出货方式 start ************************************************/


    public void ship(int slotNo,String payMedthod,String tradeNo) {
        if (slotNo < 1) {
            return;
        }

        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            shipFail(slotNo,payMedthod,tradeNo);
            return;
        }
        ship(slotNo,mGroupInfo,payMedthod,tradeNo,-1,-1,-1,0);
    }

    public void ship(int slotNo,String payMedthod,String tradeNo,int heatTime) {
        if (slotNo < 1) {
            return;
        }

        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            shipFail(slotNo,payMedthod,tradeNo);
            return;
        }
        if (heatTime < 0) {
            heatTime = 45;
        }
        ship(slotNo,mGroupInfo,payMedthod,tradeNo,heatTime,-1,-1,0);
    }



    public void ship(int slotNo,String payMedthod,String tradeNo,int row,int column,int back) {
        if (slotNo < 1) {
            return;
        }

        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            shipFail(slotNo,payMedthod,tradeNo);
            return;
        }

       /* if (isBusy(mGroupInfo)) {
            TcnVendIF.getInstance().LoggerError(TAG, "ship isBusy slotNo: "+slotNo+" tradeNo: "+tradeNo);
            shipFail(slotNo,payMedthod,tradeNo);
            return;
        }*/

        ship(slotNo,mGroupInfo,payMedthod,tradeNo,-1,row,column,back);
    }

    private void ship(int slotNo,GroupInfo groupInfo,String payMedthod,String tradeNo,int heatTime,int row,int column,int back) {
        m_bShiping = true;
        if (null == groupInfo) {
            return;
        }

        cleanShipTestList();

        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(slotNo);
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(groupInfo.getBoardGrpNo());
        TcnVendIF.getInstance().LoggerDebug(TAG, "ship slotNo: "+slotNo+" getBoardType: "+groupInfo.getBoardType()+" tradeNo: "+tradeNo+" bBoardGrpNo: "+bBoardGrpNo
                +" heatTime: "+heatTime+" row: "+row+" column: "+column);
        if (groupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().ship(TcnShareUseData.getInstance().isDropSensorCheck(),groupInfo.getSerGrpNo(),slotNo,addrSlotNo,bBoardGrpNo,payMedthod,tradeNo);
        }
    }

    private boolean isBusy(GroupInfo groupInfo) {
        boolean bRet = false;
        if (null == groupInfo) {
            return bRet;
        }
        if (groupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            bRet = DriveControl.getInstance().isBusy();
        }
        return bRet;
    }

    /************************************************ 出货方式 end ************************************************/

    public void reqCoolOpen(int grpId,int boardType) {
        if (grpId < 0) {
            reqCoolOpen(boardType);
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setCoolOpen(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void reqCoolOpen(int boardType) {
        GroupInfo mGroupInfo = null;
        if (boardType == BOARD_SPRING) {
            mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        }
        if ((null == mGroupInfo) || (mGroupInfo.getID() < 0)) {
            return;
        }

        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setCoolOpen(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void reqHeatOpen(int grpId,int boardType) {
        if (grpId < 0) {
            reqHeatOpen(boardType);
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setHeatOpen(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void reqCoolAndHeatClose(int grpId,int boardType) {
        if (grpId < 0) {
            reqCoolAndHeatClose(boardType);
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setTempControlClose(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void reqCoolAndHeatClose(int boardType) {
        GroupInfo mGroupInfo = null;
        if (boardType == BOARD_SPRING) {
            mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        }
        if ((null == mGroupInfo) || (mGroupInfo.getID() < 0)) {
            return;
        }

        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setTempControlClose(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void reqHeatOpen(int boardType) {
        GroupInfo mGroupInfo = null;
        if (boardType == BOARD_SPRING) {
            mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        }
        if ((null == mGroupInfo) || (mGroupInfo.getID() < 0)) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setHeatOpen(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void reqQuerySlotExists(int slotNo) {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            return;
        }
        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(slotNo);
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        TcnVendIF.getInstance().LoggerDebug(TAG, "reqQuerySlotExists addrSlotNo: "+addrSlotNo+" bBoardGrpNo: "+bBoardGrpNo+" BoardType: "+mGroupInfo.getBoardType()+" slotNo: "+slotNo);
        if ((mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) && (addrSlotNo > 0)) {
            DriveControl.getInstance().reqQuerySlotExists(mGroupInfo.getSerGrpNo(),slotNo,addrSlotNo,bBoardGrpNo);
        }
    }

    public void selfCheck(int grpId) {
        if (grpId < 0) {
            selfCheck();
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().selfCheck(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void selfCheck() {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        if (null == mGroupInfo) {
            return;//mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_ELEVATOR);
        }

        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().selfCheck(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void setSlotSpring(int slotNo) {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            return;
        }
        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(slotNo);
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setSlotSpring(mGroupInfo.getSerGrpNo(),slotNo,addrSlotNo,bBoardGrpNo);
        }

    }

    public void setSlotBelt(int slotNo) {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            return;
        }
        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(slotNo);
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());

        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setSlotBelt(mGroupInfo.getSerGrpNo(),slotNo,addrSlotNo,bBoardGrpNo);
        }

    }

    public void setSlotAllSpring(int grpId) {
        if (grpId < 0) {
            setSlotAllSpring();
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setSlotAllSpring(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void setSlotAllSpring() {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setSlotAllSpring(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void setSlotAllBelt(int grpId) {
        if (grpId < 0) {
            setSlotAllBelt();
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setSlotAllBelt(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void setSlotAllBelt() {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setSlotAllBelt(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void setSingleSlotno(int slotNo) {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            return;
        }
        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(slotNo);
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setSingleSlotno(mGroupInfo.getSerGrpNo(),slotNo,addrSlotNo,bBoardGrpNo);
        }
    }

    public void setDoubleSlotno(int slotNo) {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(slotNo);
        if (null == mGroupInfo) {
            return;
        }
        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(slotNo);
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setDoubleSlotno(mGroupInfo.getSerGrpNo(),slotNo,addrSlotNo,bBoardGrpNo);
        }
    }

    public void setAllSlotnoSingle(int grpId) {
        if (grpId < 0) {
            setAllSlotnoSingle();
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setAllSlotnoSingle(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void setAllSlotnoSingle() {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());

        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setAllSlotnoSingle(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void setTestMode() {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());

        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().testMode(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void setTestMode(int grpId) {
        if (grpId < 0) {
            setTestMode();
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().testMode(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void setGlassHeatEnable(int grpId,boolean enable) {
        if (grpId < 0) {
            setGlassHeatEnable(enable);
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            if (enable) {
                DriveControl.getInstance().setGlassHeatEnable(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            } else {
                DriveControl.getInstance().setGlassHeatDisEnable(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            }
        }
    }

    public void setGlassHeatEnable(boolean enable) {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());

        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            if (enable) {
                DriveControl.getInstance().setGlassHeatEnable(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            } else {
                DriveControl.getInstance().setGlassHeatDisEnable(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            }
        }
    }

    public void setLightControl(int grpId,boolean open) {
        if (grpId < 0) {
            setLightControl(open);
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            if (open) {
                DriveControl.getInstance().setLightOpen(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            } else {
                DriveControl.getInstance().setLightClose(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            }

        }
    }

    public void setLightControl(boolean open) {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());

        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            if (open) {
                DriveControl.getInstance().setLightOpen(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            } else {
                DriveControl.getInstance().setLightClose(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            }

        }
    }

    public void setBuzzerControl(int grpId,boolean open) {
        if (grpId < 0) {
            setBuzzerControl(open);
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            if (open) {
                DriveControl.getInstance().setBuzzerOpen(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            } else {
                DriveControl.getInstance().setBuzzerClose(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            }

        }
    }

    public void setBuzzerControl(boolean open) {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());

        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            if (open) {
                DriveControl.getInstance().setBuzzerOpen(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            } else {
                DriveControl.getInstance().setBuzzerClose(mGroupInfo.getSerGrpNo(),bBoardGrpNo);
            }

        }
    }

    public void setTemp(int grpId,int temp) {
        if (grpId < 0) {
            setTemp(temp);
            return;
        }
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getMachineGroupInfo(grpId);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setTemp(temp,mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    public void setTemp(int temp) {
        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
        if (null == mGroupInfo) {
            return;
        }
        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
        if (mGroupInfo.getBoardType() == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().setTemp(temp,mGroupInfo.getSerGrpNo(),bBoardGrpNo);
        }
    }

    private void OnAnalyseProtocolData(int bytesCount, int boardType, byte[] bytesData) {
        if ((null == bytesData) || (bytesData.length < 1)) {
            return;
        }
        String hexData = TcnUtility.bytesToHexString(bytesData,bytesCount);
        if (boardType == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().protocolAnalyse(hexData);
        }
    }

    private void OnAnalyseProtocolDataNew(int bytesCount, int boardType, byte[] bytesData) {
        if ((null == bytesData) || (bytesData.length < 1)) {
            return;
        }
        String hexData = TcnUtility.bytesToHexString(bytesData,bytesCount);
        if (boardType == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().protocolAnalyse(hexData);
        }
    }

    private void OnAnalyseProtocolDataThird(int bytesCount,int boardType, byte[] bytesData) {
        if ((null == bytesData) || (bytesData.length < 1)) {
            return;
        }
        String hexData = TcnUtility.bytesToHexString(bytesData,bytesCount);
        if (boardType == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().protocolAnalyse(hexData);
        }

    }

    private void OnAnalyseProtocolDataFourth(int bytesCount, int boardType, byte[] bytesData) {
        if ((null == bytesData) || (bytesData.length < 1)) {
            return;
        }
        String hexData = TcnUtility.bytesToHexString(bytesData,bytesCount);
        if (boardType == BoardGroupControl.BOARD_SPRING) {
            DriveControl.getInstance().protocolAnalyse(hexData);
        }
    }

    public int getPayShipMedthod(String payMethod) {
        int sendWhat = -1;
        int iPayMethod = -1;
        if (TcnUtility.isDigital(payMethod)) {
            iPayMethod = Integer.valueOf(payMethod);
            if (Integer.valueOf(PayMethod.PAYMETHED_CASH) == iPayMethod) {
                sendWhat = TcnProtoDef.COMMAND_SHIPMENT_CASHPAY;
            } else if (Integer.valueOf(PayMethod.PAYMETHED_MDB_CARD) == iPayMethod) {
                  sendWhat = TcnProtoDef.COMMAND_SHIPMENT_BANKCARD_ONE;
            } else if (Integer.valueOf(PayMethod.PAYMETHED_TCNICCARD) == iPayMethod) {
                sendWhat = TcnProtoDef.COMMAND_SHIPMENT_TCNCARD_OFFLINE;
            } else if (Integer.valueOf(PayMethod.PAYMETHED_BANKPOSCARD) == iPayMethod) {
                sendWhat = TcnProtoDef.COMMAND_SHIPMENT_BANKCARD_TWO;
            } else if (Integer.valueOf(PayMethod.PAYMETHED_WECHAT) == iPayMethod) {
                sendWhat = TcnProtoDef.COMMAND_SHIPMENT_WECHATPAY;
            } else if (Integer.valueOf(PayMethod.PAYMETHED_ALI) == iPayMethod) {
                sendWhat = TcnProtoDef.COMMAND_SHIPMENT_ALIPAY;
            } else if (Integer.valueOf(PayMethod.PAYMETHED_GIFTS) == iPayMethod) {
                sendWhat = TcnProtoDef.COMMAND_SHIPMENT_GIFTS;
            } else if (Integer.valueOf(PayMethod.PAYMETHED_REMOUT) == iPayMethod) {
                sendWhat = TcnProtoDef.COMMAND_SHIPMENT_REMOTE;
            } else if (Integer.valueOf(PayMethod.PAYMETHED_VERIFY) == iPayMethod) {
                sendWhat = TcnProtoDef.COMMAND_SHIPMENT_VERIFY;
            } else {
                sendWhat = TcnProtoDef.COMMAND_SHIPMENT_OTHER_PAY;
            }
        } else {
            sendWhat = TcnProtoDef.COMMAND_SHIPMENT_OTHER_PAY;
        }

        return sendWhat;
    }

    private int getShipStatus(int boardType,int shipStatus) {
        int iArg2 = -1;
        if (BoardGroupControl.BOARD_SPRING == boardType) {
            if (DriveControl.SHIP_STATUS_SHIPING == shipStatus) {
                iArg2 = TcnProtoResultDef.SHIP_SHIPING;
                m_bShiping = true;
            } else if (DriveControl.SHIP_STATUS_SUCCESS == shipStatus) {
                m_bShiping = false;
                iArg2 = TcnProtoResultDef.SHIP_SUCCESS;
            } else if (DriveControl.SHIP_STATUS_FAIL == shipStatus) {
                m_bShiping = false;
                iArg2 = TcnProtoResultDef.SHIP_FAIL;
            } else {
                m_bShiping = false;
            }
        }

        return iArg2;

    }

    private void shipForTestSlot(int boardType,int slotNo, int shipStatus,MsgToSend msgToSend) {
        if (null == msgToSend) {
            TcnVendIF.getInstance().LoggerError(TAG, "shipForTestSlot msgToSend is null");
            return;
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "shipForTestSlot() boardType: "+boardType+" slotNo: "+slotNo+" shipStatus: "+shipStatus+" getErrCode: "+msgToSend.getErrCode());
        int errCode = msgToSend.getErrCode();
        if (BoardGroupControl.BOARD_SPRING == boardType) {
            if (DriveControl.SHIP_STATUS_SHIPING == shipStatus) {
                sendReceiveData(TcnProtoDef.CMD_TEST_SLOT,slotNo,errCode,TcnProtoResultDef.SHIP_SHIPING);
            } else if (DriveControl.SHIP_STATUS_SUCCESS == shipStatus) {
                if (errCode > 0) {
                    sendReceiveData(TcnProtoDef.CMD_SHIP_SLOT_ERRCODE_UPDATE,slotNo,errCode,null);
                }
                sendReceiveData(TcnProtoDef.CMD_TEST_SLOT,slotNo,errCode,TcnProtoResultDef.SHIP_SUCCESS);
            } else if (DriveControl.SHIP_STATUS_FAIL == shipStatus) {
                if (errCode > 0) {
                    sendReceiveData(TcnProtoDef.CMD_SHIP_SLOT_ERRCODE_UPDATE,slotNo,errCode,null);
                }
                sendReceiveData(TcnProtoDef.CMD_TEST_SLOT,slotNo,errCode,TcnProtoResultDef.SHIP_FAIL);
            } else {

            }
        }

    }

    private void shipFail(int slotNo,String payMethod,String tradeNo) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "shipFail slotNo: "+slotNo+" payMethod: "+payMethod+" tradeNo: "+tradeNo);
        int sendWhat = getPayShipMedthod(payMethod);
        MsgToSend msgToSend = new MsgToSend();
        msgToSend.setSlotNo(slotNo);
        msgToSend.setPayMethod(payMethod);
        msgToSend.setTradeNo(tradeNo);
        sendReceiveData(sendWhat,slotNo,TcnProtoResultDef.SHIP_FAIL,msgToSend);
    }

    private void handShipData(int boardType, int slotNo, int shipStatus, MsgToSend msgToSend) {
        if (null == msgToSend) {
            TcnVendIF.getInstance().LoggerError(TAG, "handShipData msgToSend is null");
            return;
        }

        if (BoardGroupControl.BOARD_SPRING == boardType) {
            msgToSend.setBoardType(BOARD_SPRING);
        }

        TcnVendIF.getInstance().LoggerDebug(TAG, "handShipData slotNo: "+slotNo+" shipStatus: "+shipStatus+" errCode: "+msgToSend.getErrCode()+" payMethod: "+msgToSend.getPayMethod());

        int sendWhat = getPayShipMedthod(msgToSend.getPayMethod());

        int iShipStatus = getShipStatus(boardType,shipStatus);

        if (TcnProtoResultDef.SHIP_SUCCESS == iShipStatus) {
            TcnShareUseData.getInstance().setShipContinFailCount(0);
        } else if (TcnProtoResultDef.SHIP_FAIL == iShipStatus) {
            int iFailCountLock = TcnShareUseData.getInstance().getShipFailCountLock();
            if(iFailCountLock < 9) {
                int count = TcnShareUseData.getInstance().getShipContinFailCount();
                TcnShareUseData.getInstance().setShipContinFailCount(count+1);
            }
        } else {

        }

        int errCode = msgToSend.getErrCode();

        sendReceiveData(sendWhat,slotNo,iShipStatus,msgToSend);

        if (errCode > 0) {
            sendReceiveData(TcnProtoDef.CMD_SHIP_SLOT_ERRCODE_UPDATE,slotNo,errCode,null);
        }
    }


    private void sendReceiveData(int what, int arg1, int arg2, Object data) {
        if (null == m_SendHandler) {
            return;
        }
        Message message = m_SendHandler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = data;
        m_SendHandler.sendMessage(message);
    }

    private void sendNoDataCmd(int cmdType) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "sendNoDataCmd cmdType: "+cmdType);
        sendReceiveData(TcnProtoDef.CMD_NO_DATA_RECIVE,cmdType,-1,-1);
    }


    private class CommunicationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SerialPortController.SERIAL_PORT_CONFIG_ERROR:
                    TcnVendIF.getInstance().LoggerDebug(TAG, "SERIAL_PORT_CONFIG_ERROR");
                    sendReceiveData(TcnProtoDef.SERIAL_PORT_CONFIG_ERROR,msg.arg1,msg.arg2,null);
                    break;
                case SerialPortController.SERIAL_PORT_SECURITY_ERROR:
                    TcnVendIF.getInstance().LoggerDebug(TAG, "SERIAL_PORT_SECURITY_ERROR");
                    sendReceiveData(TcnProtoDef.SERIAL_PORT_SECURITY_ERROR,msg.arg1,msg.arg2,null);
                    break;
                case SerialPortController.SERIAL_PORT_UNKNOWN_ERROR:
                    TcnVendIF.getInstance().LoggerDebug(TAG, "SERIAL_PORT_UNKNOWN_ERROR");
                    sendReceiveData(TcnProtoDef.SERIAL_PORT_UNKNOWN_ERROR,msg.arg1,msg.arg2,null);
                    break;
                case SerialPortController.SERIAL_PORT_RECEIVE_DATA:
                    OnAnalyseProtocolData(msg.arg1, msg.arg2,(byte[])msg.obj);
                    break;
                case SerialPortController.SERIAL_PORT_RECEIVE_DATA_NEW:
                    OnAnalyseProtocolDataNew(msg.arg1, msg.arg2,(byte[])msg.obj);
                    break;
                case SerialPortController.SERIAL_PORT_RECEIVE_DATA_THIRD:
                    OnAnalyseProtocolDataThird(msg.arg1, msg.arg2,(byte[])msg.obj);
                    break;
                case SerialPortController.SERIAL_PORT_RECEIVE_DATA_FOURTH:
                    OnAnalyseProtocolDataFourth(msg.arg1, msg.arg2,(byte[])msg.obj);
                    break;
                case DriveControl.CMD_QUERY_SLOTNO_ALL_LOOP:
                    DriveControl.getInstance().sendCmdGetData((MsgToSend)msg.obj);
                    break;
                case DriveControl.CMD_QUERY_SLOTNO_EXISTS:
                    if (DriveControl.getInstance().isQueryingAllSlotNo()) {
                        sendReceiveData(TcnProtoDef.COMMAND_SLOTNO_INFO,msg.arg1,msg.arg2,false);
                    } else {    //查询完成

                        boolean bIsNotLoadAll = false;  //是否加载
                        if (msg.obj != null) {
                            bIsNotLoadAll = ((MsgToSend)msg.obj).getBValue();
                        }
                        if (bIsNotLoadAll) {
                            sendReceiveData(TcnProtoDef.COMMAND_SLOTNO_INFO_SINGLE,msg.arg1,msg.arg2,true);
                            break;
                        }
                        GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfoNext(DriveControl.getInstance().getCurrentSerptGrp(),DriveControl.getInstance().getCurrentGroupNumber());
                        if ((mGroupInfo != null) && (mGroupInfo.getID() >= 0) && (msg.arg1 > 0)) {

                            sendReceiveData(TcnProtoDef.COMMAND_SLOTNO_INFO,msg.arg1,msg.arg2,false);

                            int startSlotNo = BoardGroupControl.getInstance().getFirstSlotNo(mGroupInfo);
                            int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(startSlotNo);
                            byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
                            TcnVendIF.getInstance().LoggerDebug(TAG, "CMD_QUERY_SLOTNO_EXISTS SerGrpNo: "+mGroupInfo.getSerGrpNo()+" startSlotNo: "+startSlotNo+" addrSlotNo: "+addrSlotNo+" BoardGrpNo: "+mGroupInfo.getBoardGrpNo());
                            if (BoardGroupControl.BOARD_SPRING == (mGroupInfo.getBoardType())) {
                                DriveControl.getInstance().sendCmdGetData(true,mGroupInfo.getSerGrpNo(),startSlotNo,addrSlotNo,bBoardGrpNo);
                            } else {

                            }
                        } else {
                            sendReceiveData(TcnProtoDef.COMMAND_SLOTNO_INFO,msg.arg1,msg.arg2,true);
                        }
                    }

                    break;
                case DriveControl.CMD_SHIP:
                    handShipData(BoardGroupControl.BOARD_SPRING,msg.arg1,msg.arg2,(MsgToSend) msg.obj);
                    break;
                case DriveControl.CMD_SHIP_TEST:
                    shipForTestSlot(BoardGroupControl.BOARD_SPRING,msg.arg1,msg.arg2,(MsgToSend)msg.obj);
                    if (m_isTestingSlotNo) {
                        if (msg.arg2 != DriveControl.SHIP_STATUS_SHIPING) {
                            if (m_slotNoTestList != null) {
                                if (m_slotNoTestList.contains(Integer.valueOf(msg.arg1))) {
                                    m_slotNoTestList.remove(Integer.valueOf(msg.arg1));
                                }
                                if (m_slotNoTestList.size() > 0) {
                                    GroupInfo mGroupInfo = BoardGroupControl.getInstance().getGroupInfo(m_slotNoTestList.get(0));
                                    if (mGroupInfo != null) {
                                        int addrSlotNo = BoardGroupControl.getInstance().getAddrSlotNo(m_slotNoTestList.get(0));
                                        byte bBoardGrpNo = BoardGroupControl.getInstance().getGroup(mGroupInfo.getBoardGrpNo());
                                        if (BoardGroupControl.BOARD_SPRING == (mGroupInfo.getBoardType())) {
                                            DriveControl.getInstance().shipTest(TcnShareUseData.getInstance().isDropSensorCheck(),mGroupInfo.getSerGrpNo(),m_slotNoTestList.get(0),addrSlotNo,bBoardGrpNo);
                                        }
                                        else {
                                            cleanShipTestList();
                                        }
                                    }
                                } else {
                                    m_isTestingSlotNo = false;
                                }
                            } else {
                                m_isTestingSlotNo = false;
                            }
                        }
                    }
                    break;
                case DriveControl.CMD_SELECT_SLOTNO:
                    if (DriveControl.SELECT_SUCCESS == msg.arg2) {
                        sendReceiveData(TcnProtoDef.COMMAND_SELECT_SLOTNO,msg.arg1,-1,null);
                    } else {
                        sendReceiveData(TcnProtoDef.COMMAND_INVALID_SLOTNO,msg.arg1,-1,null);
                    }
                    break;
                case DriveControl.CMD_SELF_CHECK:
                    sendReceiveData(TcnProtoDef.SELF_CHECK,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_RESET:
                    sendReceiveData(TcnProtoDef.CMD_RESET,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_TEST_MODE:
                    break;
                case DriveControl.CMD_SET_SLOTNO_SPRING:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_SPRING,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_BELTS:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_BELTS,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_ALL_SPRING:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_ALL_SPRING,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_ALL_BELT:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_ALL_BELT,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_SINGLE:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_SINGLE,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_DOUBLE:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_DOUBLE,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_SLOTNO_ALL_SINGLE:
                    sendReceiveData(TcnProtoDef.SET_SLOTNO_ALL_SINGLE,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_REQ_QUERY_SLOTNO_EXISTS:
                    sendReceiveData(TcnProtoDef.COMMAND_SLOTNO_INFO_SINGLE,msg.arg1,msg.arg2,true);
                    sendReceiveData(TcnProtoDef.QUERY_SLOT_STATUS,msg.arg1,msg.arg2,BOARD_SPRING);
                    break;
                case DriveControl.CMD_BUSY:
                    if (msg.arg2 >= 0) {
                        DriveControl.getInstance().handleBusyMessage((MsgToSend) msg.obj);
                    } else {
                        sendReceiveData(TcnProtoDef.COMMAND_BUSY,msg.arg1,msg.arg2,null);
                    }
                    break;
                case DriveControl.CMD_SET_TEMP_CONTROL_OR_NOT:      //msg.arg1:   故障代码
                    sendReceiveData(TcnProtoDef.SET_TEMP_CONTROL_OR_NOT,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_COOL:     //msg.arg1:   故障代码
                    sendReceiveData(TcnProtoDef.CMD_SET_COOL,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_HEAT:     //msg.arg1:   故障代码
                    sendReceiveData(TcnProtoDef.CMD_SET_HEAT,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_TEMP: //msg.arg1:   故障代码
                    sendReceiveData(TcnProtoDef.CMD_SET_TEMP,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_GLASS_HEAT_OPEN:   //msg.arg1:   故障代码
                    sendReceiveData(TcnProtoDef.CMD_SET_GLASS_HEAT_OPEN,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_GLASS_HEAT_CLOSE:   //msg.arg1:   故障代码
                    sendReceiveData(TcnProtoDef.CMD_SET_GLASS_HEAT_CLOSE,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_READ_CURRENT_TEMP:    //msg.arg1:   温度值getMachineGroupInfo
                    if (DriveControl.GROUP_SERIPORT_1 == msg.arg1) {
                        if (2 == msg.arg2) {
                            m_strTemp3 = (String) msg.obj;
                            if (null == m_strTemp3) {
                                m_strTemp3 = "";
                            }
                        } else if (1 == msg.arg2) {
                            m_strTemp2 = (String) msg.obj;
                            if (null == m_strTemp2) {
                                m_strTemp2 = "";
                            }
                        }
                        else {
                            m_strTemp1 = (String) msg.obj;
                            if (null == m_strTemp1) {
                                m_strTemp1 = "";
                            }
                        }
                    } else if (DriveControl.GROUP_SERIPORT_2 == msg.arg1) {
                        m_strTemp2 = (String) msg.obj;
                        if (null == m_strTemp2) {
                            m_strTemp2 = "";
                        }
                    } else if (DriveControl.GROUP_SERIPORT_3 == msg.arg1) {
                        m_strTemp3 = (String) msg.obj;
                        if (null == m_strTemp3) {
                            m_strTemp3 = "";
                        }
                    }
                    else {
                        m_strTemp1 = (String) msg.obj;
                        if (null == m_strTemp1) {
                            m_strTemp1 = "";
                        }
                    }
                    sendReceiveData(TcnProtoDef.CMD_READ_CURRENT_TEMP,msg.arg1,msg.arg2,msg.obj);

                    StringBuffer mTempBf = new StringBuffer();
                    if ((m_strTemp1 != null) && (m_strTemp1.length() > 0)) {
                        mTempBf.append(m_strTemp1);
                        if ((m_strTemp2 != null) && (m_strTemp2.length() > 0)) {
                            mTempBf.append("|");
                            mTempBf.append(m_strTemp2);
                            if ((m_strTemp3 != null) && (m_strTemp3.length() > 0)) {
                                mTempBf.append("|");
                                mTempBf.append(m_strTemp3);
                            }
                        }
                    }
                    sendReceiveData(TcnProtoDef.CMD_READ_TEMP,msg.arg1,msg.arg2,mTempBf.toString());
                    break;
                case DriveControl.CMD_SET_LIGHT_OPEN:   //msg.arg1:   故障代码
                    sendReceiveData(TcnProtoDef.CMD_SET_LIGHT_OPEN,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_LIGHT_CLOSE:  //msg.arg1:   故障代码
                    sendReceiveData(TcnProtoDef.CMD_SET_LIGHT_CLOSE,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_BUZZER_OPEN:  //msg.arg1:   故障代码
                    sendReceiveData(TcnProtoDef.CMD_SET_BUZZER_OPEN,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_SET_BUZZER_CLOSE: //msg.arg1:   故障代码
                    sendReceiveData(TcnProtoDef.CMD_SET_BUZZER_CLOSE,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_CLOSE_COOL_HEAT:
                    sendReceiveData(TcnProtoDef.CMD_SET_COOL_HEAT_CLOSE,msg.arg1,msg.arg2,null);
                    break;
                case DriveControl.CMD_READ_DOOR_STATUS:
                    if (DriveControl.DOOR_CLOSE == msg.arg1) {
                        sendReceiveData(TcnProtoDef.CMD_READ_DOOR_STATUS,TcnProtoResultDef.DOOR_CLOSE,msg.arg2,null);
                    } else {
                        sendReceiveData(TcnProtoDef.CMD_READ_DOOR_STATUS,TcnProtoResultDef.DOOR_OPEN,msg.arg2,null);
                    }

                    break;
                case DriveControl.CMD_READ_CURRENT_TEMP_LOOP:
                    GroupInfo mDriveTmpGroupInfo = BoardGroupControl.getInstance().getGroupInfoNext(DriveControl.getInstance().getCurrentSerptGrp(),DriveControl.getInstance().getCurrentGroupNumber());
                    if ((mDriveTmpGroupInfo != null) && (mDriveTmpGroupInfo.getID() >= 0)) {
                        if (BoardGroupControl.BOARD_SPRING == (mDriveTmpGroupInfo.getBoardType())) {

                        } else {
                            mDriveTmpGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
                        }
                    } else {
                        mDriveTmpGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst(BoardGroupControl.BOARD_SPRING);
                    }
                    if ((mDriveTmpGroupInfo != null) && (mDriveTmpGroupInfo.getID() >= 0)) {
                        DriveControl.getInstance().reqReadTempLoop(mDriveTmpGroupInfo.getSerGrpNo(),mDriveTmpGroupInfo.getBoardGrpNo(),msg.arg1);
                    }
                    break;
                case DriveControl.CMD_READ_DOOR_STATUS_LOOP:
                    GroupInfo mDriveDoorGroupInfo = BoardGroupControl.getInstance().getGroupInfoFirst();
                    if ((mDriveDoorGroupInfo != null) && (mDriveDoorGroupInfo.getID() >= 0)) {
                        if (BoardGroupControl.BOARD_SPRING == (mDriveDoorGroupInfo.getBoardType())) {
                            DriveControl.getInstance().reqReadDoorStatusLoop(mDriveDoorGroupInfo.getSerGrpNo(),mDriveDoorGroupInfo.getBoardGrpNo(),msg.arg1);
                        }
                    }
                    break;
                case SerialPortController.SERIAL_PORT_RECEIVE_NO_DATA:
                    sendNoDataCmd(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    }
}
