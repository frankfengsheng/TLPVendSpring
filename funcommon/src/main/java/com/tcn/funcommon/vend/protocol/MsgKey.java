package com.tcn.funcommon.vend.protocol;

/**
 * Created by Administrator on 2018/3/24.
 */
public class MsgKey {
    private int m_iKeyNum = -1;
    private int m_iCmdType = -1;
    private int m_iStatus = -1;
    private int m_iFlick = -1;
    private byte[] m_bCmdData = null;
    private int m_iCurrentCount = 0;
    private int m_iMaxCount = 0;
    private long m_lCmdTime = 0;
    private long m_lCmdOverTimeSpan = 0;

    public MsgKey(int key,int status)
    {
        this.m_iKeyNum = key;
        this.m_iStatus = status;
    }

    public MsgKey(int cmdType,int key,int status, int flick, byte[] data, int currentCount, int maxCount, long cmdTime,long cmdOverTimeSpan)
    {
        this.m_iCmdType = cmdType;
        this.m_iKeyNum = key;
        this.m_iStatus = status;
        this.m_iFlick = flick;
        this.m_bCmdData = data;
        this.m_iCurrentCount = currentCount;
        this.m_iMaxCount = maxCount;
        this.m_lCmdTime = cmdTime;
        this.m_lCmdOverTimeSpan = cmdOverTimeSpan;
    }


    public void setMsgKey(int cmdType,int key,byte[] data,int maxCount, long cmdTime,long cmdOverTimeSpan)
    {
        this.m_iCmdType = cmdType;
        this.m_iKeyNum = key;
        this.m_bCmdData = data;
        this.m_iMaxCount = maxCount;
        this.m_lCmdTime = cmdTime;
        this.m_lCmdOverTimeSpan = cmdOverTimeSpan;
    }

    public int getKeyNum()
    {
        return this.m_iKeyNum;
    }

    public void setKeyNum(int key)
    {
        this.m_iKeyNum = key;
    }

    public void setCmdType(int type)
    {
        this.m_iCmdType = type;
    }

    public int getCmdType()
    {
        return this.m_iCmdType;
    }

    public void setStatus(int status)
    {
        this.m_iStatus = status;
    }

    public int getStatus()
    {
        return this.m_iStatus;
    }

    public void setFlick(int flick)
    {
        this.m_iFlick = flick;
    }

    public int getFlick()
    {
        return this.m_iFlick;
    }

    public void setCmdData(byte[] data)
    {
        this.m_bCmdData = data;
    }

    public void setCurrentCount(int count)
    {
        this.m_iCurrentCount = count;
    }

    public int getCurrentCount()
    {
        return this.m_iCurrentCount;
    }

    public int getMaxCount()
    {
        return this.m_iMaxCount;
    }

    public byte[] getCmdData()
    {
        return this.m_bCmdData;
    }

    public long getCmdTime()
    {
        return this.m_lCmdTime;
    }

    public long getOverTimeSpan()
    {
        return this.m_lCmdOverTimeSpan;
    }

}
