package com.tcn.funcommon.socket.IO;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tcn.funcommon.socket.CLog;
import com.tcn.funcommon.socket.NetManager;
import com.tcn.funcommon.socket.SocketConst;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/4/24.
 */
public class SocketInputThread extends Thread {

    private final static String TAG = "SocketInputThread";
    private boolean isStart = true;
    // private MessageListener messageListener;// 消息监听接口对象

    public SocketInputThread()
    {
    }

    public void setStart(boolean isStart)
    {
        this.isStart = isStart;
    }

    @Override
    public void run() {
        super.run();

        while (isStart)
        {
            // 手机能联网，读socket数据
            if (NetManager.instance().isNetworkConnected())
            {
                // 如果连接服务器失败,服务器连接失败，sleep固定的时间，能联网，就不需要sleep
                if (!TCPClient.instance().isConnect())
                {
                    sleep();

                    if (!TCPClient.instance().isConnect()) {
                        TCPClient.instance().reConnect();
                    }
                }

                readSocket();

                // 如果连接服务器失败,服务器连接失败，sleep固定的时间，能联网，就不需要sleep

                CLog.i(TAG, "IO isConnect() " + TCPClient.instance().isConnect()+" isStart: "+isStart);

           } else {
                sleep();
            }
        }

    }

    private void sleep() {
        try
        {
            sleep(SocketConst.SOCKET_SLEEP_SECOND * 1000);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void readSocket()
    {
        InputStream mInputStream = TCPClient.instance().getInputStream();
        if (null == mInputStream)
        {
            CLog.i(TAG, "SocketInputThread readSocket .....mInputStream is null: ");
            return;
        }
        if (TCPClient.instance().isConnect()) {
            String receivedString = readFromInputStream(mInputStream);
            CLog.i(TAG, "SocketInputThread readSocket .....receivedString : "+receivedString);
            if (mOnReceiveHandler != null) {
                Message msg = mOnReceiveHandler.obtainMessage();
                msg.obj = receivedString;
                msg.what = SocketConst.SOCKET_RECEIVE_MSG;
                mOnReceiveHandler.sendMessage(msg);
            }
        }
    }

    /**
     *
     * 阻塞式接收后台数据
     *
     **/
    public String readFromInputStream(InputStream in){
        int count = 0;
        byte[] inDatas = null;
        try{
            while(count == 0){
                count = in.available();
            }
            inDatas = new byte[count];
            in.read(inDatas);
            return new String(inDatas);
        }catch(Exception e){
            CLog.i(TAG, "SocketInputThread Exception .....e: "+e);
            e.printStackTrace();
        }
        return null;
    }

    private Handler mOnReceiveHandler = null;
    public void setOnReceiveHandler(Handler mOnReceiveHandler) {
        this.mOnReceiveHandler = mOnReceiveHandler;
    }
}
