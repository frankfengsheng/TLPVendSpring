package com.tcn.funcommon.socket.IO;


import android.util.Log;

import com.tcn.funcommon.socket.SocketConst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/4/24.
 */
public class TCPClient {

    // 要连接的服务器Ip地址
    private String hostIp;

    // 要连接的远程服务器在监听的端口
    private int hostListenningPort;

    private static TCPClient s_Tcp = null;

    public boolean isInitialized = false;

    /**
     * client socket
     */
    private Socket mSocket = null;
    private InputStream in = null;
    private OutputStream out = null;

    public static synchronized TCPClient instance()
    {
        if (s_Tcp == null)
        {
            s_Tcp = new TCPClient(SocketConst.getSocketServer(),
                    SocketConst.getSocketPort());
        }
        return s_Tcp;
    }

    /**
     * 构造函数
     *
     * @param HostIp
     * @param HostListenningPort
     * @throws IOException
     */
    public TCPClient(String HostIp, int HostListenningPort)
    {
        this.hostIp = HostIp;
        this.hostListenningPort = HostListenningPort;

        try
        {
            initialize();
            this.isInitialized = true;
        } catch (Exception e)
        {
            this.isInitialized = false;
            e.printStackTrace();
        }

    }

    public void initialize() throws IOException
    {
        boolean done = false;
        try {
            mSocket = new Socket(InetAddress.getByName(hostIp),hostListenningPort);
            out = mSocket.getOutputStream();
            in = mSocket.getInputStream();
            done = true;
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        finally
        {
            if (!done)
            {
                closeTCPSocket();
            }
        }
    }

    /**
     * 发送字符串到服务器
     *
     * @param message
     * @throws IOException
     */
    public void sendMsg(String message) throws IOException
    {
        if ((null != out) && (null != message))
        {
            out.write(message.getBytes());
            out.flush();
        }

    }

    /**
     * 发送数据
     *
     * @param bytes
     * @throws IOException
     */
    public void sendMsg(byte[] bytes) throws IOException
    {
        if ((null != out) && (null != bytes))
        {
            Log.i("test", "sendMsg .....");
            out.write(bytes);
            out.flush();
        }
    }

    /**
     *
     * @return
     */
    public synchronized InputStream getInputStream()
    {
        return this.in;
    }

    /**
     * Socket连接是否是正常的
     *
     * @return
     */
    public boolean isConnect()
    {
        boolean isConnect = false;
        if (this.isInitialized && (mSocket != null))
        {
            if (mSocket.isConnected() && (!mSocket.isInputShutdown()) && (!mSocket.isOutputShutdown())) {
                isConnect = true;
            }
        }
        return isConnect;
    }

    /**
     * 关闭socket 重新连接
     *
     * @return
     */
    public boolean reConnect()
    {
        try {
            closeTCPSocket();
        } catch (IOException e) {

        }

        try
        {
            initialize();
            isInitialized = true;
        } catch (IOException e)
        {
            isInitialized = false;
            e.printStackTrace();
        }
        catch (Exception e)
        {
            isInitialized = false;
            e.printStackTrace();
        }
        return isInitialized;
    }

    /**
     * 服务器是否关闭，通过发送一个socket信息
     *
     * @return
     */
    public boolean canConnectToServer()
    {
        boolean isCanConnect = false;
        try {
            if (mSocket != null) {
                mSocket.sendUrgentData(0xff);
                isCanConnect = true;
            }
        } catch (IOException e) {

        }
        return isCanConnect;
    }

    /**
     * 关闭输入流、输出流、socket
     */
    public void closeTCPSocket()  throws IOException {
        if(null != in) {
            in.close();
            in = null;
        }
        if(null != out) {
            out.close();
            out = null;
        }
        if(null != mSocket) {
            mSocket.shutdownInput();
            mSocket.shutdownOutput();
            mSocket.close();
            mSocket=null;
        }
    }
}
