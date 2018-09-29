package com.tcn.funcommon.socket.IO;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tcn.funcommon.socket.CLog;
import com.tcn.funcommon.socket.MsgStrEntity;
import com.tcn.funcommon.socket.SocketConst;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2016/4/24.
 */
public class SocketOutputThread extends Thread {

    private boolean isStart = true;
    private static String tag = "socketOutputThread";
    private List<MsgStrEntity> sendMsgList;

    public SocketOutputThread( )
    {

        sendMsgList = new CopyOnWriteArrayList<MsgStrEntity>();
    }

    public void setStart(boolean isStart)
    {
        this.isStart = isStart;
        synchronized (this)
        {
            notify();
        }
    }

    // 使用socket发送消息
    public boolean sendMsg(byte[] msg) throws Exception
    {


        if (msg == null)
        {
            CLog.e(tag, "sendMsg is null");
            return false;
        }

        try
        {
            TCPClient.instance().sendMsg(msg);

        } catch (Exception e)
        {
            Log.i("test", "SocketOutputThread Exception .....e: "+e);
            throw (e);
        }

        return true;
    }

    // 使用socket发送消息
    public void addMsgToSendList(MsgStrEntity msg)
    {

        synchronized (this)
        {
            this.sendMsgList.add(msg);
            notify();
        }
    }

    @Override
    public void run() {
        super.run();

        while (isStart)
        {
            // 锁发送list
            synchronized (sendMsgList)
            {
                // 发送消息
                for (MsgStrEntity msg : sendMsgList)
                {

                    Handler handler = msg.getHandler();
                    try
                    {
                        sendMsg(msg.getBytes());
                        sendMsgList.remove(msg);
                        // 成功消息，通过hander回传
                        if (handler != null)
                        {
                            Message message = handler.obtainMessage();
                            message.arg1 = msg.getProtocolType();
                            message.obj = msg.getMsg();
                            message.what = SocketConst.SOCKET_SEND_MSG_SUCCESS;
                            handler.sendMessage(message);
                            Log.i("test", "SocketOutputThread OutPut getSocketServer: " + SocketConst.getSocketServer() + " what: " + message.what+" msg.getMsg(): "+msg.getMsg());
                        }

                        sleep(500);

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        CLog.e(tag, e.toString());
                        Log.i("test", "OutPut e: "+e+" e.toString(): "+e.toString());
                        // 错误消息，通过hander回传
                        if (handler != null)
                        {
                            Message message = handler.obtainMessage();
                            message.arg1 = msg.getProtocolType();
                            message.obj = msg.getMsg();
                            message.what = SocketConst.SOCKET_SEND_MSG_FAIL;
                            handler.sendMessage(message);

                        }
                    }
                }
            }

            synchronized (this)
            {
                try
                {
                    wait();

                } catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }// 发送完消息后，线程进入等待状态
            }
        }
    }
}
