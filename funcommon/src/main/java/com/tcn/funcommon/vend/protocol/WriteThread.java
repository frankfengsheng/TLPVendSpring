package com.tcn.funcommon.vend.protocol;

import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.TcnUtility;
import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.util.concurrent.CopyOnWriteArrayList;

import android_serialport_api.SerialPortController;

/**
 * Created by Administrator on 2017/9/6.
 */
public class WriteThread  extends Thread {

    private final static String TAG = "WriteThread";

    public static final int SERIAL_PORT_TYPE_1        = 1;
    public static final int SERIAL_PORT_TYPE_2        = 2;
    public static final int SERIAL_PORT_TYPE_3        = 3;
    public static final int SERIAL_PORT_TYPE_4        = 4;
    public static final int SERIAL_PORT_TYPE_CARD     = 5;

    public static final int SERIAL_PORT_TYPE_MDB        = 6;
    public static final int SERIAL_PORT_TYPE_DGT_DSP   = 7;
    public static final int SERIAL_PORT_TYPE_KEY       = 8;

    private static final long CMD_OVERTIME_VALUE        = 600;

    private volatile boolean m_bIsRun = true;
    private volatile boolean m_bIsBusy = false;
    private volatile boolean m_bWriting = false;
    private volatile boolean m_bReSend = false;
    private volatile boolean m_bHasNewDada = false;
    private volatile boolean m_bNotShowLog = false;

    private volatile int m_iReSendCount = 0;
    private volatile long m_lOverTime = CMD_OVERTIME_VALUE;

    private volatile MsgToWrite m_curretnMsg = null;
    private volatile MsgToWrite m_sendMsg = null;
    private volatile CopyOnWriteArrayList<MsgToWrite> m_sendMsgList = new CopyOnWriteArrayList<MsgToWrite>();
    private volatile CopyOnWriteArrayList<MsgToWrite> m_msgToWriteList = new CopyOnWriteArrayList<MsgToWrite>();
    private Handler m_SendHandler = null;



    public WriteThread( )
    {
        setName("WriteThread");
    }

    public void setSendHandler(Handler handler) {
        m_SendHandler = handler;
    }

    public void setReSendCount(int count) {
        m_iReSendCount = count;
    }

    public void setCmdOverTime(long timeMillis) {
        m_lOverTime = timeMillis;
    }

    public void setNotShowLog(boolean notShowLog) {
        m_bNotShowLog = notShowLog;
    }

    public boolean isBusy() {
        return m_bIsBusy;
    }

    public void setBusy(boolean busy) {
        if (!m_bNotShowLog) {
//            TcnVendIF.getInstance().LoggerDebug(TAG, "setBusy() busy: "+busy);
        }
        m_bIsBusy = busy;
        if (!busy) {
            m_bWriting = false;
        }
    }

    public void setBusyAndReSend(boolean busy,boolean reSend) {
        m_bIsBusy = busy;
        m_bReSend = reSend;
        if (!busy) {
            m_bWriting = false;
        }
    }

    public boolean isOvertime(MsgToWrite msg) {

        boolean bRet = false;
        if (null == msg) {
            return bRet;
        }

        long subTime = System.currentTimeMillis() - msg.getCmdTime();

        if (subTime > 5000) {
            bRet = true;
        }
        return bRet;
    }

    public void startWriteThreads()
    {
        this.m_bIsRun = true;
        synchronized (this)
        {
            notify();
        }

        start();
    }

    public void stopWriteThreads()
    {
        this.m_bIsRun = false;
        synchronized (this)
        {
            notify();
        }
    }

    private void addMsgToSendList(MsgToWrite msg)
    {
        m_bIsBusy = true;
        synchronized (m_sendMsgList) {
            msg.setCmdTime(System.currentTimeMillis());
            if (!m_bNotShowLog) {
//             TcnVendIF.getInstance().LoggerDebug(TAG, "tcnxxx.............addMsgToSendList data " + TcnUtility.bytesToHexString(msg.getData())+" m_bIsBusy: "+m_bIsBusy+" getTime: "+msg.getCmdTime());
            }
            this.m_sendMsgList.add(msg);
        }

        synchronized (this)
        {
            if (!m_bNotShowLog) {
//                TcnVendIF.getInstance().LoggerDebug(TAG, "addMsgToSendList notify ");
            }
            m_bHasNewDada = true;
            notify();
        }
    }

    // 使用socket发送消息
    private boolean writeData(int serialPortType, byte[] data,boolean notShowLog)
    {

        boolean bRet = false;

        if (null == data)
        {
            TcnVendIF.getInstance().LoggerError(TAG, "writeData data is null");
            return bRet;
        }

        try
        {
            if (!m_bNotShowLog && !notShowLog) {
                TcnVendIF.getInstance().LoggerDebug(TAG, "writeData() data: "+TcnUtility.bytesToHexString(data)+" serialPortType: "+serialPortType);
            }
            m_bWriting = true;
            if (SERIAL_PORT_TYPE_1 == serialPortType) {
                SerialPortController.getInstance().writeDataImmediately(data);
            } else if (SERIAL_PORT_TYPE_2 == serialPortType) {
                SerialPortController.getInstance().writeDataImmediatelyNew(data);
            } else if (SERIAL_PORT_TYPE_3 == serialPortType) {
                SerialPortController.getInstance().writeDataImmediatelyThird(data);
            } else if (SERIAL_PORT_TYPE_4 == serialPortType) {
                SerialPortController.getInstance().writeDataImmediatelyFourth(data);
            }
            else {

            }
            bRet = true;
        } catch (Exception e)
        {
            m_bWriting = false;
            m_bIsBusy = false;
            TcnVendIF.getInstance().LoggerError(TAG, "writeData e " + e);
        }

        return bRet;
    }

    // 发送消息
    private boolean writeData(int serialPortType, String data,boolean notShowLog)
    {

        boolean bRet = false;

        if (null == data)
        {
            TcnVendIF.getInstance().LoggerError(TAG, "writeData data is null");
            return bRet;
        }

        try
        {
            if (!m_bNotShowLog && !notShowLog) {
                TcnVendIF.getInstance().LoggerDebug(TAG, "writeData() data: "+data+" serialPortType: "+serialPortType);
            }
            m_bWriting = true;
            if (SERIAL_PORT_TYPE_1 == serialPortType) {
                SerialPortController.getInstance().writeDataImmediately(data.getBytes());
            } else if (SERIAL_PORT_TYPE_2 == serialPortType) {
                SerialPortController.getInstance().writeDataImmediatelyNew(data.getBytes());
            } else if (SERIAL_PORT_TYPE_3 == serialPortType) {
                SerialPortController.getInstance().writeDataImmediatelyThird(data.getBytes());
            } else if (SERIAL_PORT_TYPE_4 == serialPortType) {
                SerialPortController.getInstance().writeDataImmediatelyFourth(data.getBytes());
            }
            else {

            }
            bRet = true;
        } catch (Exception e)
        {
            m_bWriting = false;
            m_bIsBusy = false;
            TcnVendIF.getInstance().LoggerError(TAG, "writeData e " + e);
        }

        return bRet;
    }

    public void sendMsg(int serialPortType, int cmdType, long cmdOverTimeSpan, byte[] data, boolean notShowLog)
    {
        if (data != null) {
            if (!m_bNotShowLog) {
//               TcnVendIF.getInstance().LoggerDebug(TAG, "sendMsg data " + TcnUtility.bytesToHexString(data)+" m_bIsBusy: "+m_bIsBusy);
            }
            if (null == m_sendMsg) {
                m_sendMsg = new MsgToWrite(serialPortType,cmdType,-1, cmdOverTimeSpan, data,notShowLog);
            } else {
                m_sendMsg.cleanData();
                m_sendMsg.setMsgToWrite(serialPortType,cmdType,-1, cmdOverTimeSpan, data,notShowLog);
            }
            addMsgToSendList(m_sendMsg);
        }
    }

    public void sendMsg(int serialPortType, int cmdType, int num, long cmdOverTimeSpan, byte[] data)
    {
        if (data != null) {
            if (!m_bNotShowLog) {
//                TcnVendIF.getInstance().LoggerDebug(TAG, "sendMsg data " + TcnUtility.bytesToHexString(data)+" m_bIsBusy: "+m_bIsBusy);
            }
            if (null == m_sendMsg) {
                m_sendMsg = new MsgToWrite(serialPortType,cmdType,num,cmdOverTimeSpan, data,false);
            } else {
                m_sendMsg.cleanData();
                m_sendMsg.setMsgToWrite(serialPortType,cmdType,num,cmdOverTimeSpan, data,false);
            }
            addMsgToSendList(m_sendMsg);
        }
    }

    public void sendMsg(int serialPortType, int cmdType,long cmdOverTimeSpan, String data)
    {
        if (data != null) {
            if (null == m_sendMsg) {
                m_sendMsg = new MsgToWrite(serialPortType,cmdType,-1,cmdOverTimeSpan, data.getBytes(),false);
            } else {
                m_sendMsg.cleanData();
                m_sendMsg.setMsgToWrite(serialPortType,cmdType,-1,cmdOverTimeSpan, data.getBytes(),false);
            }
            addMsgToSendList(m_sendMsg);
        }
    }

    private void sleepTime(long time) {
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private CopyOnWriteArrayList<MsgToWrite> getNewMessageList() {

        m_msgToWriteList.clear();

        if (null == m_sendMsgList) {
            return m_msgToWriteList;
        }
        for (MsgToWrite info:m_sendMsgList) {
            m_msgToWriteList.add(info);
        }
        return m_msgToWriteList;
    }

    private boolean isCmdReSendOverTime(long timeMillis) {
        boolean bRet = false;
        if (Math.abs(System.currentTimeMillis() - timeMillis) > 1000) {
            bRet = true;
        }
        return bRet;
    }

    private boolean isCmdReciveOverTime(long timeMillis,long overTimeSpan) {
        boolean bRet = false;
        if (Math.abs(System.currentTimeMillis() - timeMillis) > overTimeSpan) {
            bRet = true;
        }
        return bRet;
    }

    private void sendData(boolean removeOld,int what, int arg1, int arg2, Object data) {
        if (null == m_SendHandler) {
            return;
        }
        if (removeOld) {
            m_SendHandler.removeMessages(what);
        }
        Message message = m_SendHandler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = data;
        m_SendHandler.sendMessage(message);
    }

    @Override
    public void run() {
        super.run();
        while (m_bIsRun) {

            synchronized (m_sendMsgList) {

                while (m_sendMsgList.size() > 0) {
                    // 发送消息
                    CopyOnWriteArrayList<MsgToWrite> msgToWriteList = getNewMessageList();
                    if (!m_bNotShowLog) {
//                        TcnVendIF.getInstance().LoggerDebug(TAG, "while m_sendMsgList size: "+msgToWriteList.size());
                    }

                    for (MsgToWrite msg : msgToWriteList) {
                        if (!m_bNotShowLog) {
//                            TcnVendIF.getInstance().LoggerDebug(TAG, "tcnxxx.run getData " + TcnUtility.bytesToHexString(msg.getData())+" getCmdTime: "+msg.getCmdTime()+" m_bIsBusy: "+m_bIsBusy+" m_bReSend: "+m_bReSend+" m_bWriting: "+m_bWriting);
                        }
                        if (!m_bWriting) {
                            long lReSendTime = System.currentTimeMillis();
                            while (m_bReSend) {
                                if (!m_bWriting) {
                                    if (m_curretnMsg != null) {
                                        writeData(m_curretnMsg.getSerialType(),m_curretnMsg.getData(),m_curretnMsg.isNotShowLog());
                                        sleepTime(20);
                                    } else {
                                        m_bReSend = false;
                                        sleepTime(20);
                                    }
                                }
                                long lTime = System.currentTimeMillis();
                                while (m_bWriting) {
                                    sleepTime(20);
                                    if (isCmdReciveOverTime(lTime,m_curretnMsg.getOverTimeSpan())) {
                                        if (!m_bNotShowLog) {
//                                            TcnVendIF.getInstance().LoggerDebug(TAG, "while m_bReSend m_iReSendCount: "+m_iReSendCount+" m_bIsBusy: "+m_bIsBusy);
                                        }
                                        for (int i = 0; i < m_iReSendCount; i++) {
                                            sleepTime(20);
                                            if (m_bWriting) {
                                                if (!isOvertime(m_curretnMsg)) {
                                                    writeData(m_curretnMsg.getSerialType(),m_curretnMsg.getData(),m_curretnMsg.isNotShowLog());
                                                    sleepTime(300);
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        if (!m_bNotShowLog) {
//                                            TcnVendIF.getInstance().LoggerDebug(TAG, "while m_bReSend end m_iReSendCount: "+m_iReSendCount+" m_bIsBusy: "+m_bIsBusy);
                                        }
                                        if (m_bWriting) {
                                            m_bWriting = false;
                                            m_bIsBusy = false;
                                            sendData(false,SerialPortController.SERIAL_PORT_RECEIVE_NO_DATA,m_curretnMsg.getCmdType(),m_curretnMsg.getCmdNum(),m_curretnMsg.getData());
                                        }
                                    }
                                }
                                if (!m_bNotShowLog) {
//                                     TcnVendIF.getInstance().LoggerDebug(TAG, "tcnxxx..m_bReSend " + m_bReSend+" m_bIsBusy: "+m_bIsBusy+" getData: "+TcnUtility.bytesToHexString(m_curretnMsg.getData())+" getCmdTime: "+m_curretnMsg.getCmdTime());
                                }
                                if (isCmdReSendOverTime(lReSendTime)) {
                                    m_bReSend = false;
                                }

                            }
                            if (!m_bNotShowLog) {
//                                TcnVendIF.getInstance().LoggerDebug(TAG, "while data " + TcnUtility.bytesToHexString(msg.getData())+" m_bIsBusy: "+m_bIsBusy);
                            }

                            if (!isOvertime(msg)) {
                                m_curretnMsg = msg;
                                writeData(msg.getSerialType(),msg.getData(),m_curretnMsg.isNotShowLog());
                            } else {
                                m_curretnMsg = null;
                            }
                            m_sendMsgList.remove(msg);
                        }
                        if (!m_bNotShowLog) {
//                            TcnVendIF.getInstance().LoggerDebug(TAG, "end m_bIsBusy: "+m_bIsBusy+" m_bWriting: "+m_bWriting);
                        }

                        long lTime = System.currentTimeMillis();
                        while (m_bWriting) {
                            sleepTime(20);
                            if (!m_bNotShowLog) {
//                                TcnVendIF.getInstance().LoggerDebug(TAG, "lTime: "+lTime+" msg.getOverTimeSpan(): "+msg.getOverTimeSpan());
                            }

                            if (isCmdReciveOverTime(lTime,msg.getOverTimeSpan())) {
                                if (!m_bNotShowLog) {
//                                    TcnVendIF.getInstance().LoggerDebug(TAG, "while m_iReSendCount: "+m_iReSendCount+" m_bIsBusy: "+m_bIsBusy);
                                }

                                for (int i = 0; i < m_iReSendCount; i++) {
                                    sleepTime(50);
                                    if (m_bWriting) {
                                        if (!isOvertime(msg)) {
                                            writeData(msg.getSerialType(),msg.getData(),m_curretnMsg.isNotShowLog());
                                            sleepTime(300);
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                if (!m_bNotShowLog) {
//                                    TcnVendIF.getInstance().LoggerDebug(TAG, "while end m_iReSendCount: "+m_iReSendCount+" m_bIsBusy: "+m_bIsBusy);
                                }

                                if (m_bWriting) {
                                    m_bWriting = false;
                                    m_bIsBusy = false;
                                    sendData(false,SerialPortController.SERIAL_PORT_RECEIVE_NO_DATA,msg.getCmdType(),msg.getCmdNum(),msg.getData());
                                }
                            }
                            if (!m_bNotShowLog) {
//                                TcnVendIF.getInstance().LoggerDebug(TAG, "while sleep m_bIsBusy: "+m_bIsBusy+" m_bWriting: "+m_bWriting);
                            }

                        }
                        if (!m_bNotShowLog) {
//                            TcnVendIF.getInstance().LoggerDebug(TAG, "for end size: "+m_sendMsgList.size()+" msgToWriteList size: "+msgToWriteList.size());
                        }

                    }
                    if (!m_bNotShowLog) {
//                        TcnVendIF.getInstance().LoggerDebug(TAG, "while end m_bIsBusy: "+m_bIsBusy);
                    }

                }
                m_bHasNewDada = false;
            }
            if (!m_bNotShowLog) {
//                TcnVendIF.getInstance().LoggerDebug(TAG, "while end to wait m_bIsBusy: "+m_bIsBusy+" 11 m_bHasNewDada: "+m_bHasNewDada);
            }

            synchronized (this)
            {
                try
                {
                    if (!m_bHasNewDada) {
                        if (!m_bNotShowLog) {
//                            TcnVendIF.getInstance().LoggerDebug(TAG, "wait ");
                        }
                        wait();
                    }
                } catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    TcnVendIF.getInstance().LoggerError(TAG, "run e: "+e);
                }// 发送完消息后，线程进入等待状态
            }
        }
    }
}
