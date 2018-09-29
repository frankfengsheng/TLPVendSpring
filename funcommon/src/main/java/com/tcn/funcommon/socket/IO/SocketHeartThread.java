package com.tcn.funcommon.socket.IO;

import android.os.Handler;

import com.tcn.funcommon.socket.CLog;
import com.tcn.funcommon.socket.SocketConst;

/**
 * Created by Songjiancheng on 2016/4/24.
 */
public class SocketHeartThread extends Thread {

    private final static String TAG = "SocketHeartThread";
    boolean isStop = false;
    boolean mIsConnectSocketSuccess = false;
    private Handler mHandler = null;
    private static SocketHeartThread s_instance;

    private com.tcn.funcommon.socket.NIO.TCPClient mTcpClient = null;

    public static synchronized SocketHeartThread instance()
    {
        if (s_instance == null)
        {
            s_instance = new SocketHeartThread();
        }
        return s_instance;
    }

    public SocketHeartThread()
    {
        TCPClient.instance();
        // 连接服务器
        //	mIsConnectSocketSuccess = connect();

    }

    public void stopThread()
    {
        isStop = true;
    }

    /**
     * 连接socket到服务器, 并发送初始化的Socket信息
     *
     * @return
     */


    private boolean reConnect()
    {
        return TCPClient.instance().reConnect();
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void run() {

        isStop = false;
        while (!isStop)
        {
            // 发送一个心跳包看服务器是否正常
            boolean canConnectToServer = TCPClient.instance().canConnectToServer();
            CLog.i(TAG, "IO SocketHeartThread canConnectToServer " + canConnectToServer);
            if(canConnectToServer) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(SocketConst.SOCKET_HEART_CONNECTED);
                }
            } else {
                reConnect();
            }

            try
            {
                Thread.sleep(SocketConst.getSocketHeartTime() * 1000);

            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        super.run();
    }
}
