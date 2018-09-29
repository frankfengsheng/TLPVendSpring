package com.tcn.funcommon.vend.controller;

/**
 * Created by Administrator on 2018/3/27.
 */
public class MsgSend {
    private int m_iData1 = -1;
    private int m_iData2 = -1;
    private int m_iData3 = -1;
    private int m_iData4 = -1;
    private long m_lData5 = -1;
    private long m_lData6 = -1;
    private String m_strData7 = null;
    private String m_strData8 = null;

    public MsgSend() {

    }

    public MsgSend(int data1,int data2,int data3,int data4,long data5,long data6,String data7,String data8)
    {
        this.m_iData1 = data1;
        this.m_iData2 = data2;
        this.m_iData3 = data3;
        this.m_iData4 = data4;
        this.m_lData5 = data5;
        this.m_lData6 = data6;
        this.m_strData7 = data7;
        this.m_strData8 = data8;
    }

    public void setData1(int data)
    {
        this.m_iData1 = data;
    }

    public int getData1()
    {
        return this.m_iData1;
    }

    public void setData2(int data)
    {
        this.m_iData2 = data;
    }

    public int getData2()
    {
        return this.m_iData2;
    }

    public void setData3(int data)
    {
        this.m_iData3 = data;
    }

    public int getData3()
    {
        return this.m_iData3;
    }

    public void setData4(int data)
    {
        this.m_iData4 = data;
    }

    public int getData4()
    {
        return this.m_iData4;
    }

    public void setData5(long data)
    {
        this.m_lData5 = data;
    }

    public long getData5()
    {
        return this.m_lData5;
    }

    public void setData6(long data)
    {
        this.m_lData6 = data;
    }

    public long getData6()
    {
        return this.m_lData6;
    }

    public void setData7(String data)
    {
        this.m_strData7 = data;
    }

    public String getData7()
    {
        return this.m_strData7;
    }

    public void setData8(String data)
    {
        this.m_strData8 = data;
    }

    public String getData8()
    {
        return this.m_strData8;
    }
}
