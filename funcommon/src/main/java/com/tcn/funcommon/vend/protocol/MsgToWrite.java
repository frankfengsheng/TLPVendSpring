package com.tcn.funcommon.vend.protocol;

/**
 * Created by Administrator on 2017/9/6.
 */
public class MsgToWrite {
    private int m_iSerialPortType = -1;
    private int m_iCmdType = -1;
    private int m_iNum = -1;
    private long m_lCmdTime = 0;
    private long m_lCmdOverTimeSpan = 0;
    private boolean bNotShowLog = false;


    //要发送的消息
    private byte[] m_msgData;


    public MsgToWrite(int serialPortType,int cmdType,int num,long cmdTime,long cmdOverTimeSpan, byte[] msgData,boolean notShowLog)
    {
        this.m_iSerialPortType = serialPortType;
        this.m_iCmdType = cmdType;
        this.m_iNum = num;
        this.m_lCmdTime = cmdTime;
        this.m_lCmdOverTimeSpan = cmdOverTimeSpan;
        this.m_msgData = msgData;
        this.bNotShowLog = notShowLog;
    }

    public MsgToWrite(int serialPortType,int cmdType,int num,long cmdOverTimeSpan, byte[] msgData,boolean notShowLog)
    {
        this.m_iSerialPortType = serialPortType;
        this.m_iCmdType = cmdType;
        this.m_iNum = num;
        this.m_lCmdOverTimeSpan = cmdOverTimeSpan;
        this.m_msgData = msgData;
        this.bNotShowLog = notShowLog;
    }

    public void setMsgToWrite(int serialPortType,int cmdType,int num,long cmdTime,long cmdOverTimeSpan, byte[] msgData,boolean notShowLog)
    {
        this.m_iSerialPortType = serialPortType;
        this.m_iCmdType = cmdType;
        this.m_iNum = num;
        this.m_lCmdTime = cmdTime;
        this.m_lCmdOverTimeSpan = cmdOverTimeSpan;
        this.m_msgData = msgData;
        this.bNotShowLog = notShowLog;
    }

    public void setMsgToWrite(int serialPortType,int cmdType,int num,long cmdOverTimeSpan, byte[] msgData,boolean notShowLog)
    {
        this.m_iSerialPortType = serialPortType;
        this.m_iCmdType = cmdType;
        this.m_iNum = num;
        this.m_lCmdOverTimeSpan = cmdOverTimeSpan;
        this.m_msgData = msgData;
        this.bNotShowLog = notShowLog;
    }

    public void cleanData() {
        m_iSerialPortType = -1;
        m_iCmdType = -1;
        m_iNum = -1;
        m_lCmdTime = 0;
        m_lCmdOverTimeSpan = 0;
        bNotShowLog = false;
        m_msgData = null;
    }

    public byte[] getData()
    {
        return this.m_msgData;
    }

    public int getSerialType()
    {
        return this.m_iSerialPortType;
    }

    public long getCmdTime()
    {
        return this.m_lCmdTime;
    }

    public void setCmdTime(long cmdTime)
    {
        this.m_lCmdTime = cmdTime;
    }

    public int getCmdType()
    {
        return this.m_iCmdType;
    }

    public int getCmdNum()
    {
        return this.m_iNum;
    }

    public long getOverTimeSpan()
    {
        return this.m_lCmdOverTimeSpan;
    }

    public boolean isNotShowLog()
    {
        return this.bNotShowLog;
    }

}
