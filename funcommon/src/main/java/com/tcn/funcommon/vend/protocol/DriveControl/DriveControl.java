package com.tcn.funcommon.vend.protocol.DriveControl;

import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.TcnLog;
import com.tcn.funcommon.TcnUtility;
import com.tcn.funcommon.vend.protocol.MsgToSend;
import com.tcn.funcommon.vend.protocol.WriteThread;


/**
 * 作者：Jiancheng,Song on 2016/3/28 09:43
 * 邮箱：m68013@qq.com
 */
public class DriveControl {
    private static DriveControl m_Instance = null;
    private static final String TAG = "DriveControl";
    public static final String DATA_TYPE_DRIVE = "DRIVE_BOARD";

    public static final int SHIP_STATUS_SHIPING          = 1;
    public static final int SHIP_STATUS_SUCCESS          = 2;
    public static final int SHIP_STATUS_FAIL             = 3;

    public static final int ERR_CODE_0             = 0;   //正常
    public static final int ERR_CODE_1             = 1;   //光电开关在没有发射的情况下也有信号输出
    public static final int ERR_CODE_2             = 2;    //发射有改变的时候 也没有信号输出
    public static final int ERR_CODE_3             = 3;     //出货时一直有输出信号 不能判断好坏
    public static final int ERR_CODE_4             = 4;    //没有检测到出货
    public static final int ERR_CODE_16             = 16;   //P型MOS管有短路
    public static final int ERR_CODE_17             = 17;    //P型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERR_CODE_18             = 18;    //P型MOS管有短路; 发射有改变的时候 也没有信号输出;
    public static final int ERR_CODE_19             = 19;    //P型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
    public static final int ERR_CODE_32             = 32;    //N型MOS管有短路;
    public static final int ERR_CODE_33             = 33;    //N型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERR_CODE_34             = 34;    //N型MOS管有短路; 发射有改变的时候 也没有信号输出;
    public static final int ERR_CODE_35             = 35;    //N型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
    public static final int ERR_CODE_48             = 48;    //电机短路;
    public static final int ERR_CODE_49             = 49;    //电机短路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERR_CODE_50             = 50;    //电机短路; 发射有改变的时候 也没有信号输出;
    public static final int ERR_CODE_51             = 51;    //电机短路;出货时一直有输出信号 不能判断好坏;
    public static final int ERR_CODE_64             = 64;    //电机断路;
    public static final int ERR_CODE_65             = 65;    //电机断路; 光电开关在没有发射的情况下也有信号输出;
    public static final int ERR_CODE_66             = 66;    //电机断路; 发射有改变的时候 也没有信号输出;
    public static final int ERR_CODE_67             = 67;    //电机断路;出货时一直有输出信号 不能判断好坏;
    public static final int ERR_CODE_80             = 80;    //RAM出错,电机转动超时。
    public static final int ERR_CODE_81             = 81;    //在规定时间内没有接收到回复数据 表明驱动板工作不正常或者与驱动板连接有问题。
    public static final int ERR_CODE_82             = 82;    //接收到数据不完整。
    public static final int ERR_CODE_83             = 83;    //校验不正确。
    public static final int ERR_CODE_84             = 84;    //地址不正确。
    public static final int ERR_CODE_86             = 86;    //货道不存在。
    public static final int ERR_CODE_87             = 87;    //返回故障代码有错超出范围。
    public static final int ERR_CODE_90             = 90;    //连续多少次转动正常但没检测到商品售出。
    public static final int ERR_CODE_91             = 91;    //其它故障小于。
    public static final int ERR_CODE_255            = 255;   //货道号不存在

    public static final int CMD_QUERY_SLOTNO_EXISTS       = 0; //0x78+货道：查询命令，检测货道是否存在
    public static final int CMD_REQ_QUERY_SLOTNO_EXISTS       = 1; //0x78+货道：查询命令，检测货道是否存在
    public static final int CMD_SELF_CHECK                = 2;      // 自检
    public static final int CMD_RESET                      = 3; //复位命令，所有货道转一圈
    public static final int CMD_SHIP                       = 4;      // 出货
   // public static final int CMD_REQUEST_REPEAT            = 5;  //请求重发，收到驱动板数据不正确，让驱动板再发一次上次命令
    public static final int CMD_TEST_MODE                  = 6;  //测试模式，所有电机慢慢转动，用于检测货道的检测开关是否正常 这命令发了后只能断电才能停止
    public static final int CMD_SET_SLOTNO_SPRING          = 7;  //0x68~0x74 设置货道步数，0x74 为弹簧货道，其他为皮带货道
    public static final int CMD_SET_SLOTNO_BELTS          = 8;  //0x68~0x74 设置货道步数，0x74 为弹簧货道，其他为皮带货道
    public static final int CMD_SET_SLOTNO_ALL_SPRING     = 9;  //全部设置为弹簧货道
    public static final int CMD_SET_SLOTNO_ALL_BELT       = 10;   //全部设置为皮带货道
    public static final int CMD_SET_SLOTNO_SINGLE         = 11; //0XC9：设置单货道，如果该货道本来是双货道，则自动将两个货道都改为单货道
    public static final int CMD_SET_SLOTNO_DOUBLE         = 12; //0XCA：设置双货道，将该货道号和下一货道号合并为双货道
    public static final int CMD_SET_SLOTNO_ALL_SINGLE     = 13; //0xCB：全部设置为单货道
    public static final int CMD_SELECT_SLOTNO                    = 14;   // 选择货道
    public static final int CMD_BUSY                    = 15;   // 系统忙
    public static final int CMD_SHIP_TEST                    = 16;   // 测试货道

    public static final int CMD_SET_TEMP_CONTROL_OR_NOT       = 20; //设置是否使用温度控制   0 不使用   非0 使用
    public static final int CMD_SET_COOL              = 21; //设置是制冷还是加热     0 加热     非0 制冷
    public static final int CMD_SET_HEAT              = 22; //设置是制冷还是加热     0 加热     非0 制冷
    public static final int CMD_SET_TEMP             = 23; //设置制冷或加热的目标温度   有符号整数
    public static final int CMD_SET_GLASS_HEAT_OPEN         = 24; //设置是否使用玻璃加热   0 不使用   非0 使用
    public static final int CMD_SET_GLASS_HEAT_CLOSE         = 25; //设置是否使用玻璃加热   0 不使用   非0 使用
    public static final int CMD_READ_CURRENT_TEMP        = 26; //读取当前机柜内温度
    public static final int CMD_SET_LIGHT_OPEN        = 27; //LED灯条控制     PARAM=0XAA  打开LED灯条  PARAM=0X55 关闭LED灯条
    public static final int CMD_SET_LIGHT_CLOSE        = 28; //LED灯条控制     PARAM=0XAA  打开LED灯条  PARAM=0X55 关闭LED灯条
    public static final int CMD_SET_BUZZER_OPEN        = 29; //控制本机蜂鸣器  PARAM=0XAA  蜂鸣器响起   PARAM=0X55 蜂鸣器响声关闭
    public static final int CMD_SET_BUZZER_CLOSE        = 30; //控制本机蜂鸣器  PARAM=0XAA  蜂鸣器响起   PARAM=0X55 蜂鸣器响声关闭
    public static final int CMD_READ_DOOR_STATUS        = 31; //223：读取门碰开关状态       0 关门     非0 开门

    public static final int CMD_CLOSE_COOL_HEAT		        = 35;

    public static final int CMD_READ_CURRENT_TEMP_LOOP        = 40;
    public static final int CMD_READ_DOOR_STATUS_LOOP        = 41;
    public static final int CMD_QUERY_SLOTNO_ALL_LOOP        = 42;



    public static final int CMD_INVALID		            = -1;

    public static final int STATUS_DO_NONE               = -1;    //
    public static final int STATUS_DO_SELECT              = 1;    //选择货道
    public static final int STATUS_DO_SHIPING             = 2;    //出货

    public static final int SELECT_FAIL   = -1;
    public static final int SELECT_SUCCESS   = 0;

    public static final int DOOR_OPEN     = 0;
    public static final int DOOR_CLOSE    = 1;


    public static final int GROUP_SERIPORT_1             = 1;    //串口1
    public static final int GROUP_SERIPORT_2              = 2;    //串口2
    public static final int GROUP_SERIPORT_3             = 3;    //串口3
    public static final int GROUP_SERIPORT_4             = 4;    //串口4

    private volatile boolean m_bDoorOpen = false;
    private volatile boolean m_bQueryingAllSlotNo = false;
    private volatile boolean m_bHaveSlotInfo = false;
    private volatile boolean m_bUseCheck = true;
    private volatile boolean m_bHaveTemp = false;
    private volatile boolean m_bHaveDoorSwitch = false;
    private volatile int m_iCmdType = -1;
    private volatile int m_iStatusDo = STATUS_DO_NONE;
    private volatile int m_iNextCmd = CMD_INVALID;
    private volatile int m_iRepeatCount = 0;
    private volatile int m_iShipCount = 0;

    private volatile byte[] m_bCmdDataB6 = new byte[6];

    private volatile StringBuffer m_read_sbuff = new StringBuffer();
    private volatile MsgToSend m_currentSendMsg = new MsgToSend();

    private Handler m_ReceiveHandler = null;

    private WriteThread m_WriteThread = null;



    public static synchronized DriveControl getInstance() {
        if (null == m_Instance) {
            m_Instance = new DriveControl();
        }
        return m_Instance;
    }

    public void init(Handler handlerReceive) {
        m_ReceiveHandler = handlerReceive;
        m_WriteThread = new WriteThread();
        m_WriteThread.setSendHandler(handlerReceive);
        m_WriteThread.startWriteThreads();
        m_ReceiveHandler.sendEmptyMessageDelayed(CMD_READ_CURRENT_TEMP_LOOP,25000);
        m_ReceiveHandler.sendEmptyMessageDelayed(CMD_READ_DOOR_STATUS_LOOP,20000);
    }

    public boolean haveDoorSwitch() {
        return m_bHaveDoorSwitch;
    }

    public boolean isBusy() {
        boolean bRet = false;
        if (m_WriteThread != null) {
            bRet = m_WriteThread.isBusy();
        }
        return bRet;
    }

    public void handleBusyMessage(MsgToSend msgToSend) {
        if ((null == m_ReceiveHandler) || (null == msgToSend)) {
            return;
        }

        TcnLog.getInstance().LoggerError(TAG, "handleBusyMessage cmdType: "+msgToSend.getCmdType()+" slotNo: "+msgToSend.getSlotNo());

        if (msgToSend.getCurrentCount() >  msgToSend.getMaxCount()) {
            Message message = m_ReceiveHandler.obtainMessage();
            message.what = CMD_BUSY;
            message.arg1 = msgToSend.getCmdType();
            message.arg2 = -1;
            message.obj = msgToSend;
            m_ReceiveHandler.sendMessage(message);

            msgToSend.setErrCode(-1);
            if (CMD_SHIP == msgToSend.getCmdType()) {
                m_iShipCount--;
                Message msgShipFail = m_ReceiveHandler.obtainMessage();
                msgShipFail.what = CMD_SHIP;
                msgShipFail.arg1 = msgToSend.getSlotNo();
                msgShipFail.arg2 = SHIP_STATUS_FAIL;
                msgShipFail.obj = msgToSend;
                m_ReceiveHandler.sendMessage(msgShipFail);
            } else if (CMD_SHIP_TEST == msgToSend.getCmdType()) {
                Message msgShipTestFail = m_ReceiveHandler.obtainMessage();
                msgShipTestFail.what = CMD_SHIP_TEST;
                msgShipTestFail.arg1 = msgToSend.getSlotNo();
                msgShipTestFail.arg2 = SHIP_STATUS_FAIL;
                msgShipTestFail.obj = msgToSend;
                m_ReceiveHandler.sendMessage(msgShipTestFail);
            } else {

            }
            return;
        }
        long abTimeSpan = Math.abs(System.currentTimeMillis() - msgToSend.getCmdTime());
        if (msgToSend.getOverTimeSpan() < abTimeSpan) {
            Message message = m_ReceiveHandler.obtainMessage();
            message.what = CMD_BUSY;
            message.arg1 = msgToSend.getCmdType();
            message.arg2 = -1;
            message.obj = msgToSend;
            m_ReceiveHandler.sendMessage(message);
            msgToSend.setErrCode(-1);
            if (CMD_SHIP == msgToSend.getCmdType()) {
                m_iShipCount--;
                Message msgShipFail = m_ReceiveHandler.obtainMessage();
                msgShipFail.what = CMD_SHIP;
                msgShipFail.arg1 = msgToSend.getSlotNo();
                msgShipFail.arg2 = SHIP_STATUS_FAIL;
                msgShipFail.obj = msgToSend;
                m_ReceiveHandler.sendMessage(msgShipFail);
            } else if (CMD_SHIP_TEST == msgToSend.getCmdType()) {
                Message msgShipTestFail = m_ReceiveHandler.obtainMessage();
                msgShipTestFail.what = CMD_SHIP_TEST;
                msgShipTestFail.arg1 = msgToSend.getSlotNo();
                msgShipTestFail.arg2 = SHIP_STATUS_FAIL;
                msgShipTestFail.obj = msgToSend;
                m_ReceiveHandler.sendMessage(msgShipTestFail);
            } else {

            }
            return;
        }

        msgToSend.setCurrentCount(msgToSend.getCurrentCount() + 1);

        if (isBusy()) {
            Message message = m_ReceiveHandler.obtainMessage();
            message.what = CMD_BUSY;
            message.arg1 = msgToSend.getCmdType();
            message.arg2 = msgToSend.getSerialType();
            message.obj = msgToSend;
            m_ReceiveHandler.sendMessageDelayed(message,50);
            return;
        }
        if (CMD_QUERY_SLOTNO_EXISTS == msgToSend.getCmdType()) {
            reqSlotNoInfo(msgToSend.getSerialType(),msgToSend.getSlotNo(),msgToSend.getAddrNum(),msgToSend.getBoardGrp());
        } else if (CMD_SHIP == msgToSend.getCmdType()) {
            ship(msgToSend.isUseLightCheck(), msgToSend.getSerialType(),msgToSend.getSlotNo(),msgToSend.getAddrNum(),msgToSend.getBoardGrp(),msgToSend.getPayMethod()
                    ,msgToSend.getTradeNo(),msgToSend.getCurrentCount(),msgToSend.getMaxCount(),msgToSend.getOverTimeSpan());
        } else if (CMD_SELF_CHECK == msgToSend.getCmdType()) {
            selfCheck(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_RESET == msgToSend.getCmdType()) {
            reset(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_TEST_MODE == msgToSend.getCmdType()) {
            testMode(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SET_SLOTNO_SPRING == msgToSend.getCmdType()) {
            setSlotSpring(msgToSend.getSerialType(),msgToSend.getSlotNo(),msgToSend.getAddrNum(),msgToSend.getBoardGrp());
        } else if (CMD_SET_SLOTNO_BELTS == msgToSend.getCmdType()) {
            setSlotBelt(msgToSend.getSerialType(),msgToSend.getSlotNo(),msgToSend.getAddrNum(),msgToSend.getBoardGrp());
        } else if (CMD_SET_SLOTNO_ALL_SPRING == msgToSend.getCmdType()) {
            setSlotAllSpring(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (DriveControl.CMD_SET_SLOTNO_ALL_BELT == msgToSend.getCmdType()) {
            setSlotAllBelt(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SET_SLOTNO_SINGLE == msgToSend.getCmdType()) {
            setSingleSlotno(msgToSend.getSerialType(),msgToSend.getSlotNo(),msgToSend.getAddrNum(),msgToSend.getBoardGrp());
        } else if (CMD_SET_SLOTNO_DOUBLE == msgToSend.getCmdType()) {
            setDoubleSlotno(msgToSend.getSerialType(),msgToSend.getSlotNo(),msgToSend.getAddrNum(),msgToSend.getBoardGrp());
        } else if (CMD_SET_SLOTNO_ALL_SINGLE == msgToSend.getCmdType()) {
            setAllSlotnoSingle(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SELECT_SLOTNO == msgToSend.getCmdType()) {
            reqSelectSlotNo(msgToSend.getSerialType(),msgToSend.getSlotNo(),msgToSend.getAddrNum(),msgToSend.getBoardGrp());
        } else if (CMD_SHIP_TEST == msgToSend.getCmdType()) {
            shipTest(msgToSend.isUseLightCheck(), msgToSend.getSerialType(),msgToSend.getSlotNo(),msgToSend.getAddrNum(),msgToSend.getBoardGrp());
        } else if (CMD_REQ_QUERY_SLOTNO_EXISTS == msgToSend.getCmdType()) {
            reqQuerySlotExists(msgToSend.getSerialType(),msgToSend.getSlotNo(),msgToSend.getAddrNum(),msgToSend.getBoardGrp());
        } else if (CMD_SET_COOL == msgToSend.getCmdType()) {
            setCoolOpen(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SET_HEAT == msgToSend.getCmdType()) {
            setHeatOpen(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SET_TEMP == msgToSend.getCmdType()) {
            setTemp(msgToSend.getPram1(),msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SET_GLASS_HEAT_OPEN == msgToSend.getCmdType()) {
            setGlassHeatEnable(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SET_GLASS_HEAT_CLOSE == msgToSend.getCmdType()) {
            setGlassHeatDisEnable(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        }
        else if (CMD_READ_CURRENT_TEMP == msgToSend.getCmdType()) {
            reqReadTemp(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SET_LIGHT_OPEN == msgToSend.getCmdType()) {
            setLightOpen(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SET_LIGHT_CLOSE == msgToSend.getCmdType()) {
            setLightClose(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SET_BUZZER_OPEN == msgToSend.getCmdType()) {
            setBuzzerOpen(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_SET_BUZZER_CLOSE == msgToSend.getCmdType()) {
            setBuzzerClose(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_READ_DOOR_STATUS == msgToSend.getCmdType()) {
            reqReadDoorStatus(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        } else if (CMD_CLOSE_COOL_HEAT == msgToSend.getCmdType()) {
            setTempControlClose(msgToSend.getSerialType(),msgToSend.getBoardGrp());
        }
        else {

        }
    }

    public void sendBusyMessage(int serptGrp,int cmdType,int slotNo,int addrNum,int maxCount,boolean useLightCheck,byte grp,long cmdOverTimeSpan,String payMethod,String tradeNo) {
        if (null == m_ReceiveHandler) {
            return;
        }
        long cTime = System.currentTimeMillis();
        MsgToSend msfSend = new MsgToSend(serptGrp,cmdType,slotNo,addrNum,0,maxCount,-1,useLightCheck,grp,cTime,cmdOverTimeSpan,payMethod,tradeNo);
        Message message = m_ReceiveHandler.obtainMessage();
        message.what = CMD_BUSY;
        message.arg1 = cmdType;
        message.arg2 = serptGrp;
        message.obj = msfSend;
        m_ReceiveHandler.sendMessageDelayed(message,50);
    }

    public void sendBusyMessage(int serptGrp,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,boolean useLightCheck,byte grp,long cmdOverTimeSpan,String payMethod,String tradeNo) {
        if (null == m_ReceiveHandler) {
            return;
        }
        long cTime = System.currentTimeMillis();
        MsgToSend msfSend = new MsgToSend(serptGrp,cmdType,slotNo,addrNum,currentCount,maxCount,-1,useLightCheck,grp,cTime,cmdOverTimeSpan,payMethod,tradeNo);
        Message message = m_ReceiveHandler.obtainMessage();
        message.what = CMD_BUSY;
        message.arg1 = cmdType;
        message.arg2 = serptGrp;
        message.obj = msfSend;
        m_ReceiveHandler.sendMessageDelayed(message,50);
    }

    public void sendBusyMessage(int serptGrp,int cmdType,int slotNo,int addrNum,int maxCount,boolean useLightCheck,byte grp,long cmdOverTimeSpan,String payMethod) {
        if (null == m_ReceiveHandler) {
            return;
        }
        long cTime = System.currentTimeMillis();
        MsgToSend msfSend = new MsgToSend(serptGrp,cmdType,slotNo,addrNum,0,maxCount,-1,useLightCheck,grp,cTime,cmdOverTimeSpan,payMethod);
        Message message = m_ReceiveHandler.obtainMessage();
        message.what = CMD_BUSY;
        message.arg1 = cmdType;
        message.arg2 = serptGrp;
        message.obj = msfSend;
        m_ReceiveHandler.sendMessageDelayed(message,50);
    }

    public void sendBusyMessage(int slotNo,String payMethod,int serptGrp,int cmdType,int maxCount,int pram1,boolean control,byte grp,long cmdOverTimeSpan) {
        if (null == m_ReceiveHandler) {
            return;
        }
        long cTime = System.currentTimeMillis();
        MsgToSend msfSend = new MsgToSend(serptGrp,cmdType,0,maxCount,pram1,control,grp,cTime,cmdOverTimeSpan);
        msfSend.setSlotNo(slotNo);
        msfSend.setAddrNum(slotNo % 100);
        msfSend.setPayMethod(payMethod);
        Message message = m_ReceiveHandler.obtainMessage();
        message.what = CMD_BUSY;
        message.arg1 = cmdType;
        message.arg2 = serptGrp;
        message.obj = msfSend;
        m_ReceiveHandler.sendMessageDelayed(message,50);
    }

    public MsgToSend getCurrentMessage(int serptGrp,int cmdType,int slotNo,int addrNum,int maxCount,boolean useLightCheck,byte grp,long cmdOverTimeSpan,String payMethod,String tradeNo) {
        long cTime = System.currentTimeMillis();
        if (null == m_currentSendMsg) {
            m_currentSendMsg = new MsgToSend(serptGrp,cmdType,slotNo,addrNum,0,maxCount,-1,useLightCheck,grp,cTime,cmdOverTimeSpan,payMethod,tradeNo);
        } else {
            m_currentSendMsg.setMsgToSend(serptGrp,cmdType,slotNo,addrNum,0,maxCount,-1,useLightCheck,grp,cTime,cmdOverTimeSpan,payMethod,tradeNo);
        }
        return m_currentSendMsg;
    }

    public MsgToSend getCurrentMessage(int serptGrp,int cmdType,int slotNo,int addrNum,int maxCount,boolean useLightCheck,byte grp,long cmdOverTimeSpan,String payMethod) {
        long cTime = System.currentTimeMillis();
        if (null == m_currentSendMsg) {
            m_currentSendMsg = new MsgToSend(serptGrp,cmdType,slotNo,addrNum,0,maxCount,-1,useLightCheck,grp,cTime,cmdOverTimeSpan,payMethod);
        } else {
            m_currentSendMsg.setMsgToSend(serptGrp,cmdType,slotNo,addrNum,0,maxCount,-1,useLightCheck,grp,cTime,cmdOverTimeSpan,payMethod);
        }
        return m_currentSendMsg;
    }

    public MsgToSend getCurrentMessage(int slotNo,String payMethod,int serptGrp,int cmdType,int maxCount,int pram1,boolean control,byte grp,long cmdOverTimeSpan) {
        long cTime = System.currentTimeMillis();
        if (null == m_currentSendMsg) {
            m_currentSendMsg = new MsgToSend(serptGrp,cmdType,0,maxCount,pram1,control,grp,cTime,cmdOverTimeSpan);
        } else {
            m_currentSendMsg.setMsgToSend(serptGrp,cmdType,0,maxCount,pram1,control,grp,cTime,cmdOverTimeSpan);
        }
        m_currentSendMsg.setSlotNo(slotNo);
        m_currentSendMsg.setAddrNum(slotNo % 100);
        m_currentSendMsg.setPayMethod(payMethod);
        return m_currentSendMsg;
    }

    public void deInit() {
        m_read_sbuff.delete(0,m_read_sbuff.length());
        if (m_ReceiveHandler != null) {
            m_ReceiveHandler.removeCallbacksAndMessages(null);
            m_ReceiveHandler = null;
        }
    }

    public boolean isHasSlotNo() {
        return m_bHaveSlotInfo;
    }

    public int getCurrentSerptGrp() {
        return m_currentSendMsg.getSerialType();
    }

    public byte getCurrentGroupNumber() {
        return m_currentSendMsg.getBoardGrp();
    }

    public boolean isDoorOpen() {
        return m_bDoorOpen;
    }

    public void setDoorOpen(boolean open) {
        m_bDoorOpen = open;
    }


    public void sendCmdGetData(final boolean isNextGrp,final int serptGrp,final int slotNo,final int addrSlotNo,final byte boardGrpNo) {
        m_bHaveSlotInfo = false;
        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_QUERY_SLOTNO_ALL_LOOP,slotNo,addrSlotNo,10,false,boardGrpNo,1000,null);
        if (m_ReceiveHandler != null) {
            Message message = m_ReceiveHandler.obtainMessage();
            message.what = CMD_QUERY_SLOTNO_ALL_LOOP;
            if (isNextGrp) {
                m_currentSendMsg.setValueInt(0);
                m_currentSendMsg.setControl(isNextGrp);
            }
            message.obj = m_currentSendMsg;
            m_ReceiveHandler.removeMessages(CMD_QUERY_SLOTNO_ALL_LOOP);
            m_ReceiveHandler.sendMessageDelayed(message,3000);
        }
    }

    private void stopCmdGetDataTimer(){
        if (m_ReceiveHandler != null) {
            m_ReceiveHandler.removeMessages(CMD_QUERY_SLOTNO_ALL_LOOP);
        }
    }

    public void sendCmdGetData(MsgToSend msgToSend) {
        if ((null == m_ReceiveHandler) || (null == msgToSend)) {
            return;
        }
        TcnLog.getInstance().LoggerDebug(TAG, "sendCmdGetData");

        if (m_bHaveSlotInfo) {
            stopCmdGetDataTimer();
        } else if (msgToSend.isControl() && (msgToSend.getValueInt() > 2)) {
            stopCmdGetDataTimer();
            if (m_ReceiveHandler != null) {
                m_bQueryingAllSlotNo = false;
                Message message = m_ReceiveHandler.obtainMessage();
                message.what = CMD_QUERY_SLOTNO_EXISTS;
                message.arg1 = -1;
                msgToSend.setBValue(false);
                message.obj = msgToSend;
                m_ReceiveHandler.sendMessage(message);
            }
        }
        else {
            reqSlotNoInfo(msgToSend.getSerialType(),msgToSend.getSlotNo(),msgToSend.getAddrNum(),msgToSend.getBoardGrp());

            if (m_ReceiveHandler != null) {
                Message message = m_ReceiveHandler.obtainMessage();
                message.what = CMD_QUERY_SLOTNO_ALL_LOOP;
                if (msgToSend.isControl()) {
                    msgToSend.setValueInt(msgToSend.getValueInt() + 1);
                    message.arg1 = msgToSend.getValueInt();
                }

                message.obj = msgToSend;
                m_ReceiveHandler.removeMessages(CMD_QUERY_SLOTNO_ALL_LOOP);
                m_ReceiveHandler.sendMessageDelayed(message,10000);
            }

        }
    }


    private byte getGroup(String grp) {
        byte bRet = (byte)0xFF;
        byte[] bAddrArry = TcnUtility.hexStringToBytes(grp);
        if ((null == bAddrArry) || (bAddrArry.length < 1)) {
            TcnLog.getInstance().LoggerError(TAG, "getAddr() grp: "+grp);
            return bRet;
        }
        bRet = bAddrArry[0];
        return bRet;
    }

    public void writeData(int cmdType,byte[] bytessMsg) {
        long cmdOverTimeSpan = 1000;
        boolean bNotShowLog = false;
        if ((CMD_SHIP == cmdType) || (CMD_SHIP_TEST == cmdType)) {
            if (m_bUseCheck) {
                cmdOverTimeSpan = 30000;   //带光检超时设置25秒
            } else {
                cmdOverTimeSpan = 30000;  //不带光检超时设置20秒
            }
        } else if ((CMD_QUERY_SLOTNO_EXISTS == cmdType) || (CMD_REQ_QUERY_SLOTNO_EXISTS == cmdType)) {
            cmdOverTimeSpan = 1000;
        } else if ((CMD_READ_DOOR_STATUS == cmdType) || (CMD_READ_CURRENT_TEMP == cmdType)) {
            cmdOverTimeSpan = 1000;
            bNotShowLog = true;
        }
        else if ((CMD_RESET == cmdType) || (CMD_TEST_MODE == cmdType)) {
            cmdOverTimeSpan = 60 * 60 * 1000;
        }
        else {
            cmdOverTimeSpan = 5000;
        }
        m_read_sbuff.delete(0,m_read_sbuff.length());
        if (GROUP_SERIPORT_1 == m_currentSendMsg.getSerialType()) {
            m_WriteThread.sendMsg(m_WriteThread.SERIAL_PORT_TYPE_1,cmdType,cmdOverTimeSpan,bytessMsg,bNotShowLog);
        } else if (GROUP_SERIPORT_2 == m_currentSendMsg.getSerialType()) {
            m_WriteThread.sendMsg(m_WriteThread.SERIAL_PORT_TYPE_2,cmdType,cmdOverTimeSpan,bytessMsg,bNotShowLog);
        } else if (GROUP_SERIPORT_3 == m_currentSendMsg.getSerialType()) {
            m_WriteThread.sendMsg(m_WriteThread.SERIAL_PORT_TYPE_3,cmdType,cmdOverTimeSpan,bytessMsg,bNotShowLog);
        } else if (GROUP_SERIPORT_4 == m_currentSendMsg.getSerialType()) {
            m_WriteThread.sendMsg(m_WriteThread.SERIAL_PORT_TYPE_4,cmdType,cmdOverTimeSpan,bytessMsg,bNotShowLog);
        }
        else {
            m_WriteThread.sendMsg(m_WriteThread.SERIAL_PORT_TYPE_1,cmdType,cmdOverTimeSpan,bytessMsg,bNotShowLog);
        }
    }

    private String getGroup(byte grp) {
        String strGrp = "";
        if (grp == ((byte)0x00)) {
            strGrp = "00";
        } else if (grp == ((byte)0x01)) {
            strGrp = "01";
        } else if (grp == ((byte)0x02)) {
            strGrp = "02";
        } else if (grp == ((byte)0x03)) {
            strGrp = "03";
        } else if (grp == ((byte)0x04)) {
            strGrp = "04";
        } else if (grp == ((byte)0x05)) {
            strGrp = "05";
        } else if (grp == ((byte)0x06)) {
            strGrp = "06";
        } else if (grp == ((byte)0x07)) {
            strGrp = "07";
        } else if (grp == ((byte)0x08)) {
            strGrp = "08";
        } else if (grp == ((byte)0x09)) {
            strGrp = "09";
        } else {

        }
        return strGrp;

    }

    public void reqSelectSlotNo(int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(serptGrp,CMD_SELECT_SLOTNO,slotNo,addrSlotNo,20,false,boardGrpNo,1000,m_currentSendMsg.getPayMethod());
            return;
        }
        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_SELECT_SLOTNO,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());
        m_iStatusDo = STATUS_DO_SELECT;

        querySlotExists(serptGrp,slotNo,addrSlotNo,boardGrpNo);
    }

    //出货命令
    private void ship(boolean useLightCheck, int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo,String payMethod,String tradeNo
            ,int currentCount,int maxCount,long cmdOverTimeSpan) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(serptGrp,CMD_SHIP,slotNo,addrSlotNo,currentCount,maxCount,useLightCheck,boardGrpNo,cmdOverTimeSpan,payMethod,tradeNo);
            return;
        }
        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_SHIP,slotNo,addrSlotNo,maxCount,useLightCheck,boardGrpNo,cmdOverTimeSpan,payMethod,tradeNo);
        m_bUseCheck = useLightCheck;
        m_iStatusDo = STATUS_DO_SHIPING;

        Message message = m_ReceiveHandler.obtainMessage();
        message.what = CMD_SHIP;
        message.arg1 = slotNo;
        message.arg2 = SHIP_STATUS_SHIPING;
        message.obj = m_currentSendMsg;
        m_ReceiveHandler.sendMessage(message);

        querySlotExists(serptGrp,slotNo,addrSlotNo,boardGrpNo);
    }

    public void ship(boolean useLightCheck, int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo,String payMethod,String tradeNo) {
        if (null == m_WriteThread) {
            return;
        }

        m_iShipCount++;

        int maxCount = 500;
        long cmdOverTimeSpan = 25000;

        if (m_iShipCount >= 1) {
            maxCount = 100 * m_iShipCount;
            cmdOverTimeSpan = 10000 * m_iShipCount;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(serptGrp,CMD_SHIP,slotNo,addrSlotNo,maxCount,useLightCheck,boardGrpNo,cmdOverTimeSpan,payMethod,tradeNo);
            return;
        }
        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_SHIP,slotNo,addrSlotNo,maxCount,useLightCheck,boardGrpNo,cmdOverTimeSpan,payMethod,tradeNo);
        m_bUseCheck = useLightCheck;
        m_iStatusDo = STATUS_DO_SHIPING;

        Message message = m_ReceiveHandler.obtainMessage();
        message.what = CMD_SHIP;
        message.arg1 = slotNo;
        message.arg2 = SHIP_STATUS_SHIPING;
        message.obj = m_currentSendMsg;
        m_ReceiveHandler.sendMessage(message);

        querySlotExists(serptGrp,slotNo,addrSlotNo,boardGrpNo);
    }

    //出货命令
    public void shipTest(boolean useLightCheck, int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(serptGrp,CMD_SHIP_TEST,slotNo,addrSlotNo,50,useLightCheck,boardGrpNo,5000,m_currentSendMsg.getPayMethod());
            return;
        }
        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_SHIP_TEST,slotNo,addrSlotNo,50,useLightCheck,boardGrpNo,5000,m_currentSendMsg.getPayMethod());
        m_iStatusDo = STATUS_DO_SHIPING;
        m_iCmdType = CMD_SHIP_TEST;

        Message message = m_ReceiveHandler.obtainMessage();
        message.what = CMD_SHIP_TEST;
        message.arg1 = slotNo;
        message.arg2 = SHIP_STATUS_SHIPING;
        message.obj = m_currentSendMsg;
        m_ReceiveHandler.sendMessage(message);

        ship(useLightCheck,CMD_SHIP_TEST,m_currentSendMsg.getBoardGrp(),m_currentSendMsg.getAddrNum());
    }

    //出货命令
    private void ship(boolean useLightCheck, int cmdType, byte addr, int realSlotNo) {
        if (m_WriteThread.isBusy()) {
            if (CMD_SHIP == cmdType) {
                m_iShipCount--;
                m_currentSendMsg.setErrCode(-1);
                Message message = m_ReceiveHandler.obtainMessage();
                message.what = CMD_SHIP;
                message.arg1 = m_currentSendMsg.getSlotNo();
                message.arg2 = SHIP_STATUS_FAIL;
                message.obj = m_currentSendMsg;
                m_ReceiveHandler.sendMessage(message);
            } else if (CMD_SHIP_TEST == cmdType) {
                m_currentSendMsg.setErrCode(-1);
                Message message = m_ReceiveHandler.obtainMessage();
                message.what = CMD_SHIP_TEST;
                message.arg1 = m_currentSendMsg.getSlotNo();
                message.arg2 = SHIP_STATUS_FAIL;
                message.obj = m_currentSendMsg;
                m_ReceiveHandler.sendMessage(message);
            } else {

            }
            return;
        }
        m_iCmdType = cmdType;

        byte _bSlotNo = Integer.valueOf(realSlotNo).byteValue();
        byte[] bCmdData = new byte[6];
        bCmdData[0] = addr;
        bCmdData[1] = (byte)~addr;
        bCmdData[2] = _bSlotNo;
        bCmdData[3] = (byte)~_bSlotNo;
        if (useLightCheck) {
            bCmdData[4] = (byte)0xAA;
            bCmdData[5] = (byte)0x55;
        } else {
            bCmdData[4] = (byte)0x55;
            bCmdData[5] = (byte)0xAA;
        }
        writeData(cmdType,bCmdData);
    }


    //自检命令
    public void selfCheck(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SELF_CHECK,20,-1,false,boardGrp,2000);
            return;
        }
        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SELF_CHECK,20,-1,false,boardGrp,2000);
        m_iCmdType = CMD_SELF_CHECK;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0x64;
        bCmdData[3] = (byte)~0x64;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_SELF_CHECK,bCmdData);
    }

    //所有货道转一圈
    public void reset(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_RESET,20,-1,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_RESET,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_RESET;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0x65;
        bCmdData[3] = (byte)~0x65;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_RESET,bCmdData);
    }

    //请求重发，收到驱动板数据不正确，让驱动板再发一次上次命令
    private void reqRepeatSend() {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            return;
        }

        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_currentSendMsg.getBoardGrp();
        bCmdData[1] = (byte)~m_currentSendMsg.getBoardGrp();
        bCmdData[2] = (byte)0x66;
        bCmdData[3] = (byte)~0x66;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(m_iCmdType,bCmdData);
    }

    //测试模式，所有电机慢慢转动，用于检测货道的检测开关是否 正常 这命令发了后只能断电才能停止
    public void testMode(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_TEST_MODE,20,-1,false,boardGrp,2000);
            return;
        }
        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_TEST_MODE,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_TEST_MODE;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0x67;
        bCmdData[3] = (byte)~0x67;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_TEST_MODE,bCmdData);
    }

    //0x68~0x74：设置货道步数，0x74 为弹簧货道，其他为皮带货道
    //0x75：全部设置为弹簧货道
    //0x76：全部设置为皮带货道
    public void setSlotSpring(int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(serptGrp,CMD_SET_SLOTNO_SPRING,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());
            return;
        }
        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_SET_SLOTNO_SPRING,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());
        m_iCmdType = CMD_SET_SLOTNO_SPRING;


        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_currentSendMsg.getBoardGrp();
        bCmdData[1] = (byte)~m_currentSendMsg.getBoardGrp();
        bCmdData[2] = (byte)0x74;
        bCmdData[3] = (byte)~0x74;
        byte bSlotNo = Integer.valueOf(m_currentSendMsg.getAddrNum()).byteValue();
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)~bSlotNo;
        writeData(CMD_SET_SLOTNO_SPRING,bCmdData);
    }

    //0x68~0x74：设置货道步数，0x74 为弹簧货道，其他为皮带货道
    public void setSlotBelt(int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(serptGrp,CMD_SET_SLOTNO_BELTS,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());
            return;
        }

        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_SET_SLOTNO_BELTS,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());

        m_iCmdType = CMD_SET_SLOTNO_BELTS;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrpNo;
        bCmdData[1] = (byte)~boardGrpNo;
        bCmdData[2] = (byte)0x68;
        bCmdData[3] = (byte)~0x68;
        byte bSlotNo = Integer.valueOf(addrSlotNo).byteValue();
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)~bSlotNo;
        writeData(CMD_SET_SLOTNO_BELTS,bCmdData);
    }

    //0x75：全部设置为弹簧货道
    public void setSlotAllSpring(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_SLOTNO_ALL_SPRING,20,-1,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_SLOTNO_ALL_SPRING,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_SET_SLOTNO_ALL_SPRING;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0x75;
        bCmdData[3] = (byte)~0x75;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_SET_SLOTNO_ALL_SPRING,bCmdData);
    }

    //0x76：全部设置为皮带货道
    public void setSlotAllBelt(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_SLOTNO_ALL_BELT,20,-1,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_SLOTNO_ALL_BELT,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_SET_SLOTNO_ALL_BELT;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0x76;
        bCmdData[3] = (byte)~0x76;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_SET_SLOTNO_ALL_BELT,bCmdData);
    }

    public void reqQuerySlotExists(int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(serptGrp,CMD_REQ_QUERY_SLOTNO_EXISTS,slotNo,addrSlotNo,20,false,boardGrpNo,1000,m_currentSendMsg.getPayMethod());
            return;
        }

        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_REQ_QUERY_SLOTNO_EXISTS,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());

        m_iCmdType = CMD_REQ_QUERY_SLOTNO_EXISTS;

        byte _bSlotNo = Integer.valueOf(addrSlotNo).byteValue();
     //   byte[] bCmdData = new byte[6];
        m_bCmdDataB6[0] = boardGrpNo;
        m_bCmdDataB6[1] = (byte)~boardGrpNo;
        m_bCmdDataB6[2] = (byte)((byte)0x78+_bSlotNo);
        m_bCmdDataB6[3] = (byte)~((byte)0x78+_bSlotNo);
        m_bCmdDataB6[4] = (byte)0x55;
        m_bCmdDataB6[5] = (byte)0xAA;
        writeData(CMD_REQ_QUERY_SLOTNO_EXISTS,m_bCmdDataB6);
    }

    //0x78+货道：查询命令，检测货道是否存在
    private void querySlotExists(int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            return;
        }

        m_iCmdType = CMD_QUERY_SLOTNO_EXISTS;

        byte _bSlotNo = Integer.valueOf(addrSlotNo).byteValue();
       // byte[] bCmdData = new byte[6];
        m_bCmdDataB6[0] = boardGrpNo;
        m_bCmdDataB6[1] = (byte)~boardGrpNo;
        m_bCmdDataB6[2] = (byte)((byte)0x78+_bSlotNo);
        m_bCmdDataB6[3] = (byte)~((byte)0x78+_bSlotNo);
        m_bCmdDataB6[4] = (byte)0x55;
        m_bCmdDataB6[5] = (byte)0xAA;
        writeData(CMD_QUERY_SLOTNO_EXISTS,m_bCmdDataB6);
    }

    //0XC9：设置单货道，如果该货道本来是双货道，则自动将两个货道都 改为单货道
    public void setSingleSlotno(int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(serptGrp,CMD_SET_SLOTNO_SINGLE,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());
            return;
        }
        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_SET_SLOTNO_SINGLE,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());

        m_iCmdType = CMD_SET_SLOTNO_SINGLE;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrpNo;
        bCmdData[1] = (byte)~boardGrpNo;
        bCmdData[2] = (byte)0xC9;
        bCmdData[3] = (byte)~0xC9;
        byte bSlotNo = Integer.valueOf(addrSlotNo).byteValue();
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)~bSlotNo;

        /*
        //另一种驱动板 拆分货道
        //组号   255-组号    118   货道号1   货道号1   货道号2
        //货道号2 为 255 的时候拆开货道
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x76;
        byte bSlotNo = Integer.valueOf(m_iCurrentNum).byteValue();
        bCmdData[3] = bSlotNo;
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)(0xFF);
        */

        writeData(CMD_SET_SLOTNO_SINGLE,bCmdData);
    }

    //设置双货道，将该货道号和下一货道号合并为双货道
    public void setDoubleSlotno(int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(serptGrp,CMD_SET_SLOTNO_DOUBLE,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());
            return;
        }

        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_SET_SLOTNO_DOUBLE,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());

        m_iCmdType = CMD_SET_SLOTNO_DOUBLE;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrpNo;
        bCmdData[1] = (byte)~boardGrpNo;
        bCmdData[2] = (byte)0xCA;
        bCmdData[3] = (byte)~0xCA;
        byte bSlotNo = Integer.valueOf(addrSlotNo).byteValue();
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)~bSlotNo;

        /*
        //另一种驱动板 拆分货道
        //组号   255-组号    118   货道号1   货道号1   货道号2
        //货道号2 为 255 的时候拆开货道   否则 货道号1 和货道号2 合并为1
        byte[] bCmdData = new byte[6];
        bCmdData[0] = m_bGroupNumber;
        bCmdData[1] = (byte)~m_bGroupNumber;
        bCmdData[2] = (byte)0x76;
        byte bSlotNo = Integer.valueOf(m_iCurrentNum).byteValue();
        bCmdData[3] = bSlotNo;
        bCmdData[4] = bSlotNo;
        bCmdData[5] = (byte)(bSlotNo + 0x01);
        */

        writeData(CMD_SET_SLOTNO_DOUBLE,bCmdData);
    }

    //0xCB：全部设置为单货道
    public void setAllSlotnoSingle(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_SLOTNO_ALL_SINGLE,20,-1,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_SLOTNO_ALL_SINGLE,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_SET_SLOTNO_ALL_SINGLE;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xCB;
        bCmdData[3] = (byte)~0xCB;
        bCmdData[4] = (byte)0x55;
        bCmdData[5] = (byte)0xAA;
        writeData(CMD_SET_SLOTNO_ALL_SINGLE,bCmdData);
    }

    //设置是否使用温度控制   0 不使用   非0 使用
    private void setTempControl(boolean control,int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            return;
        }

        m_iCmdType = CMD_SET_TEMP_CONTROL_OR_NOT;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xCC;
        bCmdData[3] = (byte)~0xCC;
        if (control) {
            bCmdData[4] = (byte)0x01;
            bCmdData[5] = (byte)0xFE;
        } else {
            bCmdData[4] = (byte)0x00;
            bCmdData[5] = (byte)0xFF;
        }

        writeData(CMD_SET_TEMP_CONTROL_OR_NOT,bCmdData);
    }

    public void setTempControlClose(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_CLOSE_COOL_HEAT,20,-1,false,boardGrp,2000);
            return;
        }
        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_CLOSE_COOL_HEAT,20,-1,false,boardGrp,2000);
        m_iCmdType = CMD_CLOSE_COOL_HEAT;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xCC;
        bCmdData[3] = (byte)~0xCC;
        bCmdData[4] = (byte)0x00;
        bCmdData[5] = (byte)0xFF;

        writeData(CMD_CLOSE_COOL_HEAT,bCmdData);
    }

    public void setCoolOpen(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_COOL,20,-1,false,boardGrp,2000);
            return;
        }
        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_COOL,20,-1,false,boardGrp,2000);
        m_iNextCmd = CMD_SET_COOL;

        setTempControl(true,serptGrp,boardGrp);
    }

    private void setCool(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            return;
        }

        m_iCmdType = CMD_SET_COOL;

        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xCD;
        bCmdData[3] = (byte)~0xCD;
        bCmdData[4] = (byte)0x01;
        bCmdData[5] = (byte)0xFE;

        writeData(CMD_SET_COOL,bCmdData);
    }

    public void setHeatOpen(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_HEAT,20,-1,false,boardGrp,2000);
            return;
        }
        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_HEAT,20,-1,false,boardGrp,2000);
        m_iNextCmd = CMD_SET_HEAT;

        setTempControl(true,serptGrp,boardGrp);
    }

    private void setHeat(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            return;
        }

        m_iCmdType = CMD_SET_HEAT;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xCD;
        bCmdData[3] = (byte)~0xCD;
        bCmdData[4] = (byte)0x00;
        bCmdData[5] = (byte)0xFF;

        writeData(CMD_SET_HEAT,bCmdData);
    }

    public void setTemp(int temp,int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_TEMP,20,temp,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_TEMP,20,temp,false,boardGrp,2000);

        m_iCmdType = CMD_SET_TEMP;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xCE;
        bCmdData[3] = (byte)~0xCE;
        byte bTemp = Integer.valueOf(temp).byteValue();
        bCmdData[4] = bTemp;
        bCmdData[5] = (byte)~bTemp;

        writeData(CMD_SET_TEMP,bCmdData);
    }

    public void setGlassHeatEnable(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_GLASS_HEAT_OPEN,20,-1,true,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_GLASS_HEAT_OPEN,20,-1,true,boardGrp,2000);

        m_iCmdType = CMD_SET_GLASS_HEAT_OPEN;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xD4;
        bCmdData[3] = (byte)~0xD4;
        bCmdData[4] = (byte)0x01;;
        bCmdData[5] = (byte)0xFE;

        writeData(CMD_SET_GLASS_HEAT_OPEN,bCmdData);
    }

    public void setGlassHeatDisEnable(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_GLASS_HEAT_CLOSE,20,-1,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_GLASS_HEAT_CLOSE,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_SET_GLASS_HEAT_CLOSE;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xD4;
        bCmdData[3] = (byte)~0xD4;
        bCmdData[4] = (byte)0x00;;
        bCmdData[5] = (byte)0xFF;

        writeData(CMD_SET_GLASS_HEAT_CLOSE,bCmdData);
    }

    //读取当前机柜内温度  收到指令之后 第三个字节，此参数为当前温度值，为有符号整数
    public void reqReadTemp(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_READ_CURRENT_TEMP,20,-1,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_READ_CURRENT_TEMP,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_READ_CURRENT_TEMP;
       // byte[] bCmdData = new byte[6];
        m_bCmdDataB6[0] = boardGrp;
        m_bCmdDataB6[1] = (byte)~boardGrp;
        m_bCmdDataB6[2] = (byte)0xDC;
        m_bCmdDataB6[3] = (byte)~0xDC;
        m_bCmdDataB6[4] = (byte)0x55;;
        m_bCmdDataB6[5] = (byte)0xAA;

        writeData(CMD_READ_CURRENT_TEMP,m_bCmdDataB6);
    }

    private void reqReadTemp(int serptGrp,byte boardGrp,int repeatCount) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            return;
        }
        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_READ_CURRENT_TEMP,repeatCount,-1,false,boardGrp,1000);

        m_iCmdType = CMD_READ_CURRENT_TEMP;
     //   byte[] bCmdData = new byte[6];
        m_bCmdDataB6[0] = boardGrp;
        m_bCmdDataB6[1] = (byte)~boardGrp;
        m_bCmdDataB6[2] = (byte)0xDC;
        m_bCmdDataB6[3] = (byte)~0xDC;
        m_bCmdDataB6[4] = (byte)0x55;;
        m_bCmdDataB6[5] = (byte)0xAA;

        writeData(CMD_READ_CURRENT_TEMP,m_bCmdDataB6);
    }

    public void reqReadTempLoop(int serptGrp,int boardGrp,int count) {
        if (null == m_ReceiveHandler) {
            return;
        }
        if (count > 50) {
            return;
        }
        Message message = m_ReceiveHandler.obtainMessage();
        message.what = CMD_READ_CURRENT_TEMP_LOOP;
        if (m_bHaveTemp) {
            m_bHaveTemp = false;
            count = 1;
        } else {
            if (m_bHaveSlotInfo && (!m_bQueryingAllSlotNo)) {
                count++;
            } else {
                count = 2;
            }
            if (count < 1) {
                count = 1;
            }
        }
        message.arg1 = count;
        m_ReceiveHandler.removeMessages(CMD_READ_CURRENT_TEMP_LOOP);
        m_ReceiveHandler.sendMessageDelayed(message,count * 10000);
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            return;
        }
        if (!m_bQueryingAllSlotNo) {
            reqReadTemp(serptGrp,Integer.valueOf(boardGrp).byteValue(),2);
        }
    }

    public void setLightOpen(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_LIGHT_OPEN,20,-1,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_LIGHT_OPEN,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_SET_LIGHT_OPEN;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xDD;
        bCmdData[3] = (byte)~0xDD;
        bCmdData[4] = (byte)0xAA;;
        bCmdData[5] = (byte)0x55;

        writeData(CMD_SET_LIGHT_OPEN,bCmdData);
    }

    public void setLightClose(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_LIGHT_CLOSE,20,-1,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_LIGHT_CLOSE,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_SET_LIGHT_CLOSE;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xDD;
        bCmdData[3] = (byte)~0xDD;
        bCmdData[4] = (byte)0x55;;
        bCmdData[5] = (byte)0xAA;

        writeData(CMD_SET_LIGHT_CLOSE,bCmdData);
    }

    public void setBuzzerOpen(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_BUZZER_OPEN,20,-1,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_BUZZER_OPEN,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_SET_BUZZER_OPEN;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xDE;
        bCmdData[3] = (byte)~0xDE;
        bCmdData[4] = (byte)0xAA;;
        bCmdData[5] = (byte)0x55;

        writeData(CMD_SET_BUZZER_OPEN,bCmdData);
    }

    public void setBuzzerClose(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_BUZZER_CLOSE,20,-1,false,boardGrp,2000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_SET_BUZZER_CLOSE,20,-1,false,boardGrp,2000);

        m_iCmdType = CMD_SET_BUZZER_CLOSE;
        byte[] bCmdData = new byte[6];
        bCmdData[0] = boardGrp;
        bCmdData[1] = (byte)~boardGrp;
        bCmdData[2] = (byte)0xDE;
        bCmdData[3] = (byte)~0xDE;
        bCmdData[4] = (byte)0x55;;
        bCmdData[5] = (byte)0xAA;

        writeData(CMD_SET_BUZZER_CLOSE,bCmdData);
    }

    private void reqReadDoorStatus(int serptGrp,byte boardGrp,int repeatCount) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            return;
        }
        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_READ_DOOR_STATUS,repeatCount,-1,false,boardGrp,500);

        m_iCmdType = CMD_READ_DOOR_STATUS;
        //byte[] bCmdData = new byte[6];
        m_bCmdDataB6[0] = boardGrp;
        m_bCmdDataB6[1] = (byte)~boardGrp;
        m_bCmdDataB6[2] = (byte)0xDF;
        m_bCmdDataB6[3] = (byte)~0xDF;
        m_bCmdDataB6[4] = (byte)0x55;;
        m_bCmdDataB6[5] = (byte)0xAA;
        writeData(CMD_READ_DOOR_STATUS,m_bCmdDataB6);
    }

    public void reqReadDoorStatus(int serptGrp,byte boardGrp) {
        if (null == m_WriteThread) {
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_READ_DOOR_STATUS,10,-1,false,boardGrp,1000);
            return;
        }

        m_currentSendMsg = getCurrentMessage(m_currentSendMsg.getSlotNo(),m_currentSendMsg.getPayMethod(),serptGrp,CMD_READ_DOOR_STATUS,10,-1,false,boardGrp,1000);

        m_iCmdType = CMD_READ_DOOR_STATUS;
      //  byte[] bCmdData = new byte[6];
        m_bCmdDataB6[0] = boardGrp;
        m_bCmdDataB6[1] = (byte)~boardGrp;
        m_bCmdDataB6[2] = (byte)0xDF;
        m_bCmdDataB6[3] = (byte)~0xDF;
        m_bCmdDataB6[4] = (byte)0x55;;
        m_bCmdDataB6[5] = (byte)0xAA;
        writeData(CMD_READ_DOOR_STATUS,m_bCmdDataB6);
    }

    public void reqReadDoorStatusLoop(int serptGrp,int boardGrp,int count) {
        if (null == m_ReceiveHandler) {
            return;
        }
        if (count > 100) {
            return;
        }
        Message message = m_ReceiveHandler.obtainMessage();
        message.what = CMD_READ_DOOR_STATUS_LOOP;
        if (m_bHaveDoorSwitch) {
            m_bHaveDoorSwitch = false;
            count = 1;
        } else {
            if (m_bHaveSlotInfo && (!m_bQueryingAllSlotNo)) {
                count++;
            } else {
                count = 5;
            }
            if (count < 1) {
                count = 1;
            }
        }
        message.arg1 = count;
        m_ReceiveHandler.removeMessages(CMD_READ_DOOR_STATUS_LOOP);
        m_ReceiveHandler.sendMessageDelayed(message, count * 1000);

        if (!m_bQueryingAllSlotNo) {
            reqReadDoorStatus(serptGrp,Integer.valueOf(boardGrp).byteValue(),1);
        }
    }

    private void reqSlotNoInfo(int serptGrp,int slotNo,int addrSlotNo,byte boardGrpNo) {
        TcnLog.getInstance().LoggerDebug(TAG, "reqSlotNoInfo serptGrp: "+serptGrp+" slotNo: "+slotNo+" addrSlotNo: "+addrSlotNo+" boardGrpNo: "+boardGrpNo);
        m_read_sbuff.delete(0,m_read_sbuff.length());
        m_bQueryingAllSlotNo = true;
        m_iStatusDo = STATUS_DO_NONE;
        if (((byte)0xFF) == boardGrpNo) {
            TcnLog.getInstance().LoggerError(TAG, "reqSlotNoInfo()");
            return;
        }
        if (m_WriteThread.isBusy()) {
            sendBusyMessage(serptGrp,CMD_QUERY_SLOTNO_EXISTS,slotNo,addrSlotNo,20,false,boardGrpNo,2000,m_currentSendMsg.getPayMethod());
            return;
        }
        m_currentSendMsg = getCurrentMessage(serptGrp,CMD_QUERY_SLOTNO_EXISTS,slotNo,addrSlotNo,20,false,boardGrpNo,2000,null);
        querySlotExists(serptGrp,slotNo,addrSlotNo,boardGrpNo);
    }

    public boolean isQueryingAllSlotNo() {
        return m_bQueryingAllSlotNo;
    }

    private int getErr(String errHexCode, String status) {
        int iErrCode = ERR_CODE_0;
        if (null == errHexCode) {
            return iErrCode;
        }

        if (null == status) {
            return iErrCode;
        }

        if(status.equalsIgnoreCase("5D")) {   //正常
            return iErrCode;
        }

        if (errHexCode.equals("00")) {
            iErrCode = ERR_CODE_255;   //如果status
        } else if (errHexCode.equals("01")) {
            iErrCode = ERR_CODE_1;    //光电开关在没有发射的情况下也有信号输出
        } else if (errHexCode.equals("02")) {
            iErrCode = ERR_CODE_2;    //发射有改变的时候 也没有信号输出
        } else if (errHexCode.equals("03")) {
            iErrCode = ERR_CODE_3;    //出货时一直有输出信号 不能判断好坏
        } else if (errHexCode.equals("04")) {
            iErrCode = ERR_CODE_4;    //没有检测到出货
        } else if (errHexCode.equals("16")) {
            iErrCode = ERR_CODE_16;    //P型MOS管有短路
        } else if (errHexCode.equals("17")) {
            iErrCode = ERR_CODE_17;    //P型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
        } else if (errHexCode.equals("18")) {
            iErrCode = ERR_CODE_18;    //P型MOS管有短路; 发射有改变的时候 也没有信号输出;
        } else if (errHexCode.equals("19")) {
            iErrCode = ERR_CODE_19;    //P型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
        } else if (errHexCode.equals("32")) {
            iErrCode = ERR_CODE_32;    //N型MOS管有短路;
        } else if (errHexCode.equals("33")) {
            iErrCode = ERR_CODE_33;    //N型MOS管有短路; 光电开关在没有发射的情况下也有信号输出;
        } else if (errHexCode.equals("34")) {
            iErrCode = ERR_CODE_34;    //N型MOS管有短路; 发射有改变的时候 也没有信号输出;
        } else if (errHexCode.equals("35")) {
            iErrCode = ERR_CODE_35;    //N型MOS管有短路; 出货时一直有输出信号 不能判断好坏;
        } else if (errHexCode.equals("48")) {
            iErrCode = ERR_CODE_48;    //电机短路;
        } else if (errHexCode.equals("49")) {
            iErrCode = ERR_CODE_49;    //电机短路; 光电开关在没有发射的情况下也有信号输出;
        } else if (errHexCode.equals("50")) {
            iErrCode = ERR_CODE_50;    //电机短路; 发射有改变的时候 也没有信号输出;
        } else if (errHexCode.equals("51")) {
            iErrCode = ERR_CODE_51;    //电机短路;出货时一直有输出信号 不能判断好坏;
        } else if (errHexCode.equals("64")) {
            iErrCode = ERR_CODE_64;    //电机断路;
        } else if (errHexCode.equals("65")) {
            iErrCode = ERR_CODE_65;    //电机断路; 光电开关在没有发射的情况下也有信号输出;
        } else if (errHexCode.equals("66")) {
            iErrCode = ERR_CODE_66;    //电机断路; 发射有改变的时候 也没有信号输出;
        } else if (errHexCode.equals("67")) {
            iErrCode = ERR_CODE_67;    //电机断路;出货时一直有输出信号 不能判断好坏;
        } else if (errHexCode.equals("80")) {
            iErrCode = ERR_CODE_80;    //RAM出错,电机转动超时。
        } else if (errHexCode.equals("81")) {
            iErrCode = ERR_CODE_81;    //在规定时间内没有接收到回复数据 表明驱动板工作不正常或者与驱动板连接有问题。
        } else if (errHexCode.equals("82")) {
            iErrCode = ERR_CODE_82;    //接收到数据不完整。
        } else if (errHexCode.equals("83")) {
            iErrCode = ERR_CODE_83;    //校验不正确。
        } else if (errHexCode.equals("84")) {
            iErrCode = ERR_CODE_84;    //地址不正确。
        } else if (errHexCode.equals("86")) {
            iErrCode = ERR_CODE_86;    //货道不存在。
        } else if (errHexCode.equals("87")) {
            iErrCode = ERR_CODE_87;    //返回故障代码有错超出范围。
        } else if (errHexCode.equals("90")) {
            iErrCode = ERR_CODE_90;    //连续多少次转动正常但没检测到商品售出。
        } else if (errHexCode.equals("91")) {
            iErrCode = ERR_CODE_91;    //其它故障小于。
        } else {
            iErrCode = Integer.parseInt(errHexCode,16);
        }

        return iErrCode;
    }


    private boolean isValidDataPacket(String dataPacket) {
        boolean bRet = false;
        byte[] bDataPacket = TcnUtility.hexStringToBytes(dataPacket);
        if ((null == bDataPacket) || (bDataPacket.length < 1)) {
            return bRet;
        }
        int iLength = bDataPacket.length;
        byte bCheck = 0x00;
        for (int i = 0; i < (iLength - 1); i++) {
            bCheck = (byte)(bCheck + bDataPacket[i]);
        }
        if (bCheck == bDataPacket[iLength - 1]) {
            bRet = true;
        }
        return bRet;
    }

    private void handQuery(boolean useCheck, int querySlotNo,int iErrCode, String hexAddr, String status,Message message) {
        int iAddr = Integer.valueOf(hexAddr,16);
        TcnLog.getInstance().LoggerDebug(TAG, "handQuery() querySlotNo: "+querySlotNo+" iAddr: "+iAddr+" status: "+status);
        message.what = CMD_QUERY_SLOTNO_EXISTS;
        message.arg1 = m_currentSendMsg.getSlotNo();
        message.arg2 = iErrCode;
        boolean bIsNotLoadAll = false;
        if(status.equals("5D")) {   //正常
            if (STATUS_DO_SELECT == m_iStatusDo) {
                bIsNotLoadAll = true;
                m_iStatusDo = STATUS_DO_NONE;
                if (((Integer.valueOf(iAddr).byteValue())) == m_currentSendMsg.getBoardGrp()) {
                    Message msgSelect = m_ReceiveHandler.obtainMessage();
                    msgSelect.what = CMD_SELECT_SLOTNO;
                    msgSelect.arg1 = m_currentSendMsg.getSlotNo();
                    msgSelect.arg2 = SELECT_SUCCESS;
                    m_ReceiveHandler.sendMessage(msgSelect);
                } else {
                    TcnLog.getInstance().LoggerError(TAG, "handQuery() STATUS_DO_SELECT getBoardGrp: "+m_currentSendMsg.getBoardGrp());
                }
                if (m_WriteThread != null) {
                    m_WriteThread.setBusy(false);
                }
            } else if (STATUS_DO_SHIPING == m_iStatusDo) {
                TcnLog.getInstance().LoggerDebug(TAG, "handQuery() getBoardGrp: "+m_currentSendMsg.getBoardGrp());
                bIsNotLoadAll = true;
                if ((Integer.valueOf(iAddr).byteValue()) == m_currentSendMsg.getBoardGrp()) {

                    if (m_WriteThread != null) {
                        m_WriteThread.setBusy(false);
                    }

                    ship(useCheck,CMD_SHIP,m_currentSendMsg.getBoardGrp(),querySlotNo);

                } else {
                    m_iStatusDo = STATUS_DO_NONE;
                    m_iShipCount--;
                    Message msgShipFail = m_ReceiveHandler.obtainMessage();
                    msgShipFail.what = CMD_SHIP;
                    msgShipFail.arg1 = m_currentSendMsg.getSlotNo();
                    msgShipFail.arg2 = SHIP_STATUS_FAIL;
                    m_currentSendMsg.setErrCode(-1);
                    msgShipFail.obj = m_currentSendMsg;
                    m_ReceiveHandler.sendMessage(msgShipFail);

                    if (m_WriteThread != null) {
                        m_WriteThread.setBusy(false);
                    }
                }
            } else {
                if (m_WriteThread != null) {
                    m_WriteThread.setBusy(false);
                }
            }
        } else if (status.equals("5C")) {
            if (m_WriteThread != null) {
                m_WriteThread.setBusy(false);
            }
        }
        else {
            if (m_WriteThread != null) {
                m_WriteThread.setBusy(false);
            }
        }
        m_bHaveSlotInfo = true;
        stopCmdGetDataTimer();
        if (m_bQueryingAllSlotNo) {
            if (querySlotNo < 60) {
                querySlotNo++;
                m_currentSendMsg.setAddrNum(querySlotNo);
                m_currentSendMsg.setSlotNo(m_currentSendMsg.getSlotNo() + 1);
                querySlotExists(m_currentSendMsg.getSerialType(),m_currentSendMsg.getSlotNo(),querySlotNo,m_currentSendMsg.getBoardGrp());
            } else {
                m_bQueryingAllSlotNo = false;
            }
        }
        m_currentSendMsg.setBValue(bIsNotLoadAll);
        message.obj = m_currentSendMsg;
        m_ReceiveHandler.sendMessage(message);
    }

    /*
    回复命令：(固定 5 字节) D0 D1 D2 D3 D4
     *D0:组号 D1: 状态(0X5D代表正常，此时 D2 无效； 0X5C代表异常,此时D2表示 错误信息解析)
     */
    private void commondAnalyse(int cmdType,String cmdData) {
        if ((CMD_READ_DOOR_STATUS != cmdType) && (CMD_READ_CURRENT_TEMP != cmdType)) {
            TcnLog.getInstance().LoggerDebug(TAG, "commondAnalyse() cmdType: "+cmdType+" cmdData: "+cmdData);
        }
        if ((null == cmdData) || (cmdData.length() < 10)) {
            return;
        }

        String addr = cmdData.substring(0,2);
        String status = cmdData.substring(2,4);
        String errInfo = cmdData.substring(4,6);
        String shipStatus = cmdData.substring(6,8);
        int iErrCode = getErr(errInfo,status);

        m_currentSendMsg.setErrCode(iErrCode);

        Message message = m_ReceiveHandler.obtainMessage();
        if (CMD_SHIP == cmdType) {
            m_iShipCount--;
            m_iStatusDo = STATUS_DO_NONE;
            boolean bShipStatus = false;
            if (m_bUseCheck) {
                if (shipStatus.equals("AA")) {  //检测到出货
                    bShipStatus = true;
                } else {
                    iErrCode = ERR_CODE_4;   //没有检测到出货
                    m_currentSendMsg.setErrCode(ERR_CODE_4);
                }
            } else {
                bShipStatus = true;
            }
            TcnLog.getInstance().LoggerDebug(TAG, "commondAnalyse() CMD_SHIP slotNo: "+m_currentSendMsg.getSlotNo()+" shipStatus: "+shipStatus);
            message.what = CMD_SHIP;
            message.arg1 = m_currentSendMsg.getSlotNo();
            if (bShipStatus) {
                message.arg2 = SHIP_STATUS_SUCCESS;
            } else {
                message.arg2 = SHIP_STATUS_FAIL;
            }
            message.obj = m_currentSendMsg;
        } else if (CMD_SELF_CHECK == cmdType) {
            message.what = CMD_SELF_CHECK;
            message.arg1 = iErrCode;
        } else if (CMD_RESET == cmdType) {
            message.what = CMD_RESET;
            message.arg1 = iErrCode;
        } else if (CMD_TEST_MODE == cmdType) {

        } else if (CMD_SET_SLOTNO_SPRING == cmdType) {
            message.what = CMD_SET_SLOTNO_SPRING;
            message.arg1 = m_currentSendMsg.getSlotNo();
            message.arg2 = iErrCode;
        } else if (CMD_SET_SLOTNO_BELTS == cmdType) {
            message.what = CMD_SET_SLOTNO_BELTS;
            message.arg1 = m_currentSendMsg.getSlotNo();
            message.arg2 = iErrCode;
        } else if (CMD_SET_SLOTNO_ALL_SPRING == cmdType) {
            message.what = CMD_SET_SLOTNO_ALL_SPRING;
            message.arg1 = iErrCode;
        } else if (CMD_SET_SLOTNO_ALL_BELT == cmdType) {
            message.what = CMD_SET_SLOTNO_ALL_BELT;
            message.arg1 = iErrCode;
        } else if (CMD_QUERY_SLOTNO_EXISTS == cmdType) {
            handQuery(m_bUseCheck,m_currentSendMsg.getAddrNum(),iErrCode,addr,status,message);
        } else if (CMD_SET_SLOTNO_SINGLE == cmdType) {
            message.what = CMD_SET_SLOTNO_SINGLE;
            message.arg1 = m_currentSendMsg.getSlotNo();
            message.arg2 = iErrCode;
        } else if (CMD_SET_SLOTNO_DOUBLE == cmdType) {
            message.what = CMD_SET_SLOTNO_DOUBLE;
            message.arg1 = m_currentSendMsg.getSlotNo();
            message.arg2 = iErrCode;
        } else if (CMD_SET_SLOTNO_ALL_SINGLE == cmdType) {
            message.what = CMD_SET_SLOTNO_ALL_SINGLE;
            message.arg1 = iErrCode;
        } else if (CMD_SHIP_TEST == cmdType) {
            m_iStatusDo = STATUS_DO_NONE;
            boolean bShipStatus = false;
            if (m_bUseCheck) {
                if (shipStatus.equals("AA")) {  //检测到出货
                    bShipStatus = true;
                }
            } else {
                bShipStatus = true;
            }
            TcnLog.getInstance().LoggerDebug(TAG, "commondAnalyse() CMD_SHIP_TEST getSlotNo: "+m_currentSendMsg.getSlotNo()+" shipStatus: "+shipStatus);
            message.what = CMD_SHIP_TEST;
            message.arg1 = m_currentSendMsg.getSlotNo();
            if (bShipStatus) {
                message.arg2 = SHIP_STATUS_SUCCESS;
            } else {
                message.arg2 = SHIP_STATUS_FAIL;
            }
            message.obj = m_currentSendMsg;
        } else if (CMD_REQ_QUERY_SLOTNO_EXISTS == cmdType) {
            message.what = CMD_REQ_QUERY_SLOTNO_EXISTS;
            message.arg1 = m_currentSendMsg.getSlotNo();
            message.arg2 = iErrCode;
        } else if (CMD_SET_TEMP_CONTROL_OR_NOT == cmdType) {
            if (CMD_SET_COOL == m_iNextCmd) {
                m_iNextCmd = CMD_INVALID;
                if (m_WriteThread != null) {
                    m_WriteThread.setBusy(false);
                }
                setCool(m_currentSendMsg.getSerialType(),m_currentSendMsg.getBoardGrp());
            } else if (CMD_SET_HEAT == m_iNextCmd) {
                m_iNextCmd = CMD_INVALID;
                if (m_WriteThread != null) {
                    m_WriteThread.setBusy(false);
                }
                setHeat(m_currentSendMsg.getSerialType(),m_currentSendMsg.getBoardGrp());
            } else {

            }
            message.what = CMD_SET_TEMP_CONTROL_OR_NOT;
            message.arg1 = iErrCode;
            m_ReceiveHandler.sendMessage(message);
        } else if (CMD_SET_COOL == cmdType) {
            message.what = CMD_SET_COOL;
            message.arg1 = iErrCode;
        } else if (CMD_SET_HEAT == cmdType) {
            message.what = CMD_SET_HEAT;
            message.arg1 = iErrCode;
        } else if (CMD_SET_TEMP == cmdType) {
            message.what = CMD_SET_TEMP;
            message.arg1 = iErrCode;
        } else if (CMD_SET_GLASS_HEAT_OPEN == cmdType) {
            message.what = CMD_SET_GLASS_HEAT_OPEN;
            message.arg1 = iErrCode;
        } else if (CMD_SET_GLASS_HEAT_CLOSE == cmdType) {
            message.what = CMD_SET_GLASS_HEAT_CLOSE;
            message.arg1 = iErrCode;
        }
        else if (CMD_READ_CURRENT_TEMP == cmdType) {
            message.what = CMD_READ_CURRENT_TEMP;
            message.arg1 = m_currentSendMsg.getSerialType();//TcnUtility.hex2StringToDecimal(errInfo);
            message.arg2 = m_currentSendMsg.getBoardGrp();
            if ((message.arg1 <= -10) || (message.arg1 >= 100)) {
                m_bHaveTemp = false;
            } else {
                m_bHaveTemp = true;
                message.obj = String.valueOf(TcnUtility.hex2StringToDecimal(errInfo));
            }
        } else if (CMD_SET_LIGHT_OPEN == cmdType) {
            message.what = CMD_SET_LIGHT_OPEN;
            message.arg1 = iErrCode;
        } else if (CMD_SET_LIGHT_CLOSE == cmdType) {
            message.what = CMD_SET_LIGHT_CLOSE;
            message.arg1 = iErrCode;
        } else if (CMD_SET_BUZZER_OPEN == cmdType) {
            message.what = CMD_SET_BUZZER_OPEN;
            message.arg1 = iErrCode;
        } else if (CMD_SET_BUZZER_CLOSE == cmdType) {
            message.what = CMD_SET_BUZZER_CLOSE;
            message.arg1 = iErrCode;
        } else if (CMD_CLOSE_COOL_HEAT == cmdType) {
            message.what = CMD_CLOSE_COOL_HEAT;
            message.arg1 = iErrCode;
        }
        else if (CMD_READ_DOOR_STATUS == cmdType) {
            int doorStatus = Integer.parseInt(errInfo,16);

            if (doorStatus != 0) {          //关闭
                if (m_bDoorOpen) {
                    m_bDoorOpen = false;
                    message.what = CMD_READ_DOOR_STATUS;
                    message.arg1 = DOOR_CLOSE;
                    m_ReceiveHandler.sendMessage(message);
                }
            } else {
                if (!m_bDoorOpen) {
                    m_bDoorOpen = true;
                    message.what = CMD_READ_DOOR_STATUS;
                    message.arg1 = DOOR_OPEN;
                    m_ReceiveHandler.sendMessage(message);
                }
                TcnLog.getInstance().LoggerDebug(TAG, "commondAnalyse() DOOR_OPEN");
            }
            m_bHaveDoorSwitch = true;
            if (m_WriteThread != null) {
                m_WriteThread.setBusy(false);
            }
        }
        else {

        }

        if ((CMD_QUERY_SLOTNO_EXISTS != cmdType) && (CMD_SET_TEMP_CONTROL_OR_NOT != cmdType)
                && (CMD_READ_DOOR_STATUS != cmdType)) {
            if (m_WriteThread != null) {
                m_WriteThread.setBusy(false);
            }
            m_ReceiveHandler.sendMessage(message);
        }
    }

    public void protocolAnalyse(String strCmdData) {
        if ((null == strCmdData) || (strCmdData.length() <= 0)) {
            TcnLog.getInstance().LoggerError(TAG, "protocolAnalyse() strCmdData: "+strCmdData+" m_read_sbuff: "+m_read_sbuff);
            return;
        }

        try {

            m_read_sbuff.append(strCmdData);

            while ((m_read_sbuff.length()) >= 10) {
                String strGroupNumber = getGroup(m_currentSendMsg.getBoardGrp());
                int indexGroup = m_read_sbuff.indexOf(strGroupNumber);
                if ((indexGroup < 0)) {     //该组号不存在
                    //命令不对，请求驱动板重发
                    m_read_sbuff.delete(0,m_read_sbuff.length());
                    if (m_iRepeatCount > 1) {
                        m_iRepeatCount = 0;
                    } else {
                        m_iRepeatCount++;
                        if (m_WriteThread != null) {
                            m_WriteThread.setBusy(false);
                        }
                        reqRepeatSend();
                    }
                    break;  //退出
                } else if (indexGroup > 0) {
                    m_read_sbuff.delete(0,indexGroup);
                } else {

                }

                m_iRepeatCount = 0;
                if (m_read_sbuff.length() < 10) {
                    break;
                }
                String strCmd = m_read_sbuff.substring(0,10);
                if (isValidDataPacket(strCmd)) {    //校验
                    m_read_sbuff.delete(0,10);
                    commondAnalyse(m_iCmdType,strCmd);
                } else {
                    m_read_sbuff.delete(0,2);
                    continue;
                }
            }
        } catch (Exception e) {
            m_read_sbuff.delete(0,m_read_sbuff.length());
            TcnLog.getInstance().LoggerError(TAG, "protocolAnalyse isOrig Exception  e: "+e);
        }
    }
}
