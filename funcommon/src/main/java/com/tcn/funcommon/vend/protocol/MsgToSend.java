package com.tcn.funcommon.vend.protocol;

/**
 * Created by Administrator on 2017/12/4.
 */
public class MsgToSend {
    private int m_iSerialPortType = -1;
    private int m_iCmdType = -1;
    private int m_iSlotNo = -1;
    private int m_iAddrNum = -1;
    private int m_iParam1 = -1;
    private int m_iParam2 = -1;
    private int m_iErrCode = -1;
    private int m_iCurrentCount = 0;
    private int m_iMaxCount = 0;
    private int m_iHeatTime = -1;
    private int m_iRow = -1;
    private int m_iColumn = -1;
    private int m_iBack = 0;
    private int m_iBoardType = -1;
    private int m_iKeyNum = -1;
    private byte m_iBoardGrp = (byte) 0xFF;
    private long m_lCmdTime = 0;
    private long m_lCmdOverTimeSpan = 0;
    private boolean m_bUseLightCheck = false;
    private boolean m_bControl = false;
    private boolean m_bValue = false;

    private String m_strPayMethod = null;
    private String m_strValue = null;
    private String m_strTradeNo = null;

    private int m_iData = -1;
    private byte[] m_bData = null;

    private int m_iValue = -1;

    public MsgToSend() {

    }

    public MsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,boolean useLightCheck,byte grp,long cmdTime,long cmdOverTimeSpan,
                     String payMethod,String tradeNo)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_bUseLightCheck = useLightCheck;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
        m_strTradeNo = tradeNo;
    }

    public MsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,int key,byte grp,long cmdTime,long cmdOverTimeSpan,
                     String payMethod,String tradeNo)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_iKeyNum = key;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
        m_strTradeNo = tradeNo;
    }

    public MsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,boolean useLightCheck,byte grp,long cmdTime,long cmdOverTimeSpan,
                     String payMethod,String tradeNo,int heatTime,int row,int column,int back)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_bUseLightCheck = useLightCheck;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
        m_strTradeNo = tradeNo;
        m_iHeatTime = heatTime;
        m_iRow = row;
        m_iColumn = column;
        m_iBack = back;
    }


    public MsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,boolean useLightCheck,byte grp,long cmdTime,long cmdOverTimeSpan,
                     String payMethod)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_bUseLightCheck = useLightCheck;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
    }

    public MsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,int keyNum,byte grp,long cmdTime,long cmdOverTimeSpan,
                     String payMethod)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_iKeyNum = keyNum;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
    }

    public MsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,boolean useLightCheck,byte grp,long cmdTime,long cmdOverTimeSpan,
                     String payMethod,int heatTime,int row,int column,int back)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_bUseLightCheck = useLightCheck;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
        m_iHeatTime = heatTime;
        m_iRow = row;
        m_iColumn = column;
        m_iBack = back;
    }

//    public MsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int pram1,int pram2,int errCode,byte grp,long cmdTime,long cmdOverTimeSpan,String payMethod)
//    {
//        m_iSerialPortType = serialPortType;
//        m_iCmdType = cmdType;
//        m_iSlotNo = slotNo;
//        m_iAddrNum = addrNum;
//        m_iCurrentCount = currentCount;
//        m_iMaxCount = maxCount;
//        m_iParam1 = pram1;
//        m_iParam2 = pram2;
//        m_iErrCode = errCode;
//        m_iBoardGrp = grp;
//        m_lCmdTime = cmdTime;
//        m_lCmdOverTimeSpan = cmdOverTimeSpan;
//        m_strPayMethod = payMethod;
//    }

    public MsgToSend(int serialPortType,int cmdType,int currentCount,int maxCount,int pram1,boolean control,byte grp,long cmdTime,long cmdOverTimeSpan)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iParam1 = pram1;
        m_bControl = control;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
    }

    public MsgToSend(int serialPortType,int cmdType,int currentCount,int maxCount,int pram1,int pram2,byte grp,long cmdTime,long cmdOverTimeSpan)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iParam1 = pram1;
        m_iParam2 = pram2;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
    }

    public MsgToSend(int serialPortType,int cmdType,int currentCount,int maxCount,byte grp,long cmdTime,long cmdOverTimeSpan,int idata, byte[] bdata)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_iData = idata;
        m_bData = bdata;
    }


    public void setMsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,boolean useLightCheck,byte grp,long cmdTime,long cmdOverTimeSpan,String payMethod)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_bUseLightCheck = useLightCheck;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
    }

    public void setMsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,int key,byte grp,long cmdTime,long cmdOverTimeSpan,String payMethod)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_iKeyNum = key;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
    }

    public void setMsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,boolean useLightCheck,byte grp,long cmdTime,long cmdOverTimeSpan,
                             String payMethod,int heatTime,int row,int column,int back)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_bUseLightCheck = useLightCheck;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
        m_iHeatTime = heatTime;
        m_iRow = row;
        m_iColumn = column;
        m_iBack = back;
    }

    public void setMsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,boolean useLightCheck,byte grp,long cmdTime,long cmdOverTimeSpan
            ,String payMethod,String tradeNo,int heatTime,int row,int column,int back)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_bUseLightCheck = useLightCheck;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
        m_strTradeNo = tradeNo;
        m_iHeatTime = heatTime;
        m_iRow = row;
        m_iColumn = column;
        m_iBack = back;
    }

    public void setMsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,boolean useLightCheck,byte grp,long cmdTime,long cmdOverTimeSpan
            ,String payMethod,String tradeNo)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_bUseLightCheck = useLightCheck;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
        m_strTradeNo = tradeNo;
    }

    public void setMsgToSend(int serialPortType,int cmdType,int slotNo,int addrNum,int currentCount,int maxCount,int errCode,int key,byte grp,long cmdTime,long cmdOverTimeSpan
            ,String payMethod,String tradeNo)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iSlotNo = slotNo;
        m_iAddrNum = addrNum;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iErrCode = errCode;
        m_iKeyNum = key;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
        m_strPayMethod = payMethod;
        m_strTradeNo = tradeNo;
    }

    public void setMsgToSend(int serialPortType,int cmdType,int currentCount,int maxCount,int pram1,boolean control,byte grp,long cmdTime,long cmdOverTimeSpan)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iParam1 = pram1;
        m_bControl = control;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
    }

    public void setMsgToSend(int serialPortType,int cmdType,int currentCount,int maxCount,int pram1,int pram2,byte grp,long cmdTime,long cmdOverTimeSpan)
    {
        m_iSerialPortType = serialPortType;
        m_iCmdType = cmdType;
        m_iCurrentCount = currentCount;
        m_iMaxCount = maxCount;
        m_iParam1 = pram1;
        m_iParam2 = pram2;
        m_iBoardGrp = grp;
        m_lCmdTime = cmdTime;
        m_lCmdOverTimeSpan = cmdOverTimeSpan;
    }


    public int getSerialType()
    {
        return this.m_iSerialPortType;
    }

    public void setCmdType(int type)
    {
        this.m_iCmdType = type;
    }

    public int getCmdType()
    {
        return this.m_iCmdType;
    }

    public void setSlotNo(int slotNo)
    {
        this.m_iSlotNo = slotNo;
    }

    public int getSlotNo()
    {
        return this.m_iSlotNo;
    }

    public void setAddrNum(int num)
    {
        this.m_iAddrNum = num;
    }

    public int getAddrNum()
    {
        return this.m_iAddrNum;
    }

    public int getPram1()
    {
        return this.m_iParam1;
    }

    public int getPram2()
    {
        return this.m_iParam2;
    }

    public void setCurrentCount(int count)
    {
        this.m_iCurrentCount = count;
    }

    public int getCurrentCount()
    {
        return this.m_iCurrentCount;
    }

    public void setErrCode(int errCode)
    {
        this.m_iErrCode = errCode;
    }

    public int getErrCode()
    {
        return this.m_iErrCode;
    }

    public int getMaxCount()
    {
        return this.m_iMaxCount;
    }

    public int getHeatTime()
    {
        return this.m_iHeatTime;
    }

    public int getRow()
    {
        return this.m_iRow;
    }

    public int getColumn()
    {
        return this.m_iColumn;
    }

    public int getBack()
    {
        return this.m_iBack;
    }

    public int getKeyNumber()
    {
        return this.m_iKeyNum;
    }

    public byte getBoardGrp()
    {
        return this.m_iBoardGrp;
    }

    public long getCmdTime()
    {
        return this.m_lCmdTime;
    }

    public long getOverTimeSpan()
    {
        return this.m_lCmdOverTimeSpan;
    }

    public boolean isUseLightCheck()
    {
        return this.m_bUseLightCheck;
    }

    public void setControl(boolean control)
    {
        m_bControl = control;
    }

    public boolean isControl()
    {
        return this.m_bControl;
    }

    public void setPayMethod(String payMethod)
    {
        this.m_strPayMethod = payMethod;
    }

    public String getPayMethod()
    {
        return this.m_strPayMethod;
    }

    public void setTradeNo(String tradeNo)
    {
        this.m_strTradeNo = tradeNo;
    }

    public String getTradeNo()
    {
        return this.m_strTradeNo;
    }

    public void setBValue(boolean bValue)
    {
        this.m_bValue = bValue;
    }

    public boolean getBValue()
    {
        return this.m_bValue;
    }

    public void setValue(String value)
    {
        this.m_strValue = value;
    }

    public String getValue()
    {
        return this.m_strValue;
    }

    public void setValueInt(int value)
    {
        this.m_iValue = value;
    }

    public int getValueInt()
    {
        return this.m_iValue;
    }

    public void setBoardType(int boardType)
    {
        this.m_iBoardType = boardType;
    }

    public int getBoardType()
    {
        return this.m_iBoardType;
    }

    public int getDataInt()
    {
        return this.m_iData;
    }

    public void setDataBytes(byte[] datas)
    {
        this.m_bData = datas;
    }

    public byte[] getDataBytes()
    {
        return this.m_bData;
    }
}
