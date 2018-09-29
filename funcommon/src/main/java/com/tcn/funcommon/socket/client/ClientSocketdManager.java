package com.tcn.funcommon.socket.client;

import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.socket.NetManager;
import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/6/25.
 */
public class ClientSocketdManager {
    private final static String TAG = "ClientSocket";
    public final static int SOCKET_RECEIVE_MSG = 199;
    private static ClientSocketdManager s_Tcp = null;
    private boolean mBCanReadSocket = false;
    // 要连接的服务器Ip地址
    private String hostIp;

    // 要连接的远程服务器在监听的端口
    private int hostListenningPort;

    private String m_strCodeType = null;

    /**
     * client socket
     */
    private Socket mSocket = null;
    private InputStream in = null;
    private OutputStream out = null;

    private SocketInputThread mInputThread = null;
    private SocketOutputThread mOutThread = null;


    public static synchronized ClientSocketdManager instance() {
        if (s_Tcp == null)
        {
            s_Tcp = new ClientSocketdManager();

        }
        return s_Tcp;
    }

    public void setServer(String HostIp, int HostListenningPort) {
        this.hostIp = HostIp;
        this.hostListenningPort = HostListenningPort;
    }


    /**
     * 构造函数
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message)
    {
        if ((null == hostIp) || (hostListenningPort < 1) || (null == message) || (message.length() < 1)) {
            TcnVendIF.getInstance().LoggerError(TAG, "hostIp: "+hostIp+" hostListenningPort: "+hostListenningPort+" message: "+message);
            return;
        }
        if (!NetManager.instance().isNetworkConnected()) {
            TcnVendIF.getInstance().LoggerError(TAG, "network null 1");
            //return;
        }

        closeTCPSocket();
        TcnVendIF.getInstance().LoggerDebug(TAG, "sendMessage sendMessage: "+message);
        try
        {
            initialize();
            mInputThread = new SocketInputThread();
            mInputThread.setName("SockettInput");
            mInputThread.start();

            mOutThread = new SocketOutputThread(message);
            mOutThread.setName("SocketOutput");
            mOutThread.start();
        } catch (Exception e)
        {
            e.printStackTrace();
            TcnVendIF.getInstance().LoggerError(TAG, "sendMessage Exception e " + e);
        }
    }

    /**
     * 初始化
     *
     * @throws IOException
     */
    private void initialize()
    {
        if (!NetManager.instance().isNetworkConnected()) {
            TcnVendIF.getInstance().LoggerError(TAG, "initialize network null");
        }
        boolean done = false;
        try {
            TcnVendIF.getInstance().LoggerDebug(TAG, "initialize hostIp: " + InetAddress.getByName(hostIp)+" hostListenningPort: "+hostListenningPort);
            mSocket = new Socket(InetAddress.getByName(hostIp),hostListenningPort);
            out = mSocket.getOutputStream();
            in = mSocket.getInputStream();
            done = true;
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
            TcnVendIF.getInstance().LoggerError(TAG, "initialize UnknownHostException e1: "+e1+" hostIp: "+hostIp+" hostListenningPort: "+hostListenningPort);
        } catch (IOException e1) {
            e1.printStackTrace();
            TcnVendIF.getInstance().LoggerError(TAG, "initialize IOException e1: "+e1+" hostIp: "+hostIp+" hostListenningPort: "+hostListenningPort);
        } catch (Exception e) {
            TcnVendIF.getInstance().LoggerError(TAG, "initialize Exception e: "+e+" hostIp: "+hostIp+" hostListenningPort: "+hostListenningPort);
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
    private void sendMsg(String message)
    {
        if ((null != out) && (null != message))
        {
            try {
                if ("GB2312".equalsIgnoreCase(m_strCodeType)) {
                    out.write(message.getBytes("GB2312"));
                } else if ("UTF-8".equalsIgnoreCase(m_strCodeType)) {
                    out.write(message.getBytes("UTF-8"));
                } else if ("GBK".equalsIgnoreCase(m_strCodeType)) {
                    out.write(message.getBytes("GBK"));
                } else if ("UNICODE".equalsIgnoreCase(m_strCodeType)) {
                    out.write(message.getBytes("UNICODE"));
                } else if ("UTF-16".equalsIgnoreCase(m_strCodeType)) {
                    out.write(message.getBytes("UTF-16"));
                } else if ("ISO-8859-1".equalsIgnoreCase(m_strCodeType)) {
                    out.write(message.getBytes("ISO-8859-1"));
                }
                else {
                    out.write(message.getBytes());
                }
                TcnVendIF.getInstance().LoggerError(TAG, "sendMsg m_strCodeType: "+m_strCodeType+" message: "+message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                TcnVendIF.getInstance().LoggerError(TAG, "sendMsg e: "+e +" message: "+message);
            }

        } else {
            TcnVendIF.getInstance().LoggerError(TAG, "sendMsg  message: "+message+" out: "+out);
        }

    }

    /**
     * 发送数据
     *
     * @param bytes
     * @throws IOException
     */
    private void sendMsg(byte[] bytes)
    {
        if ((null != out) && (null != bytes))
        {
            try {
                out.write(bytes);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                TcnVendIF.getInstance().LoggerError(TAG, "sendMsg e: "+e);
            }

        }
    }

    /**
     *
     * 阻塞式接收后台数据
     *
     **/
    private String readFromInputStream(InputStream in){
        int count = 0;
        byte[] inDatas = null;
        try{
            while(count == 0){
                count = in.available();
            }
            inDatas = new byte[count];
            in.read(inDatas);

            String strRet = null;
            if ("GB2312".equalsIgnoreCase(m_strCodeType)) {
                strRet = new String(inDatas,"GB2312");
            } else if ("UTF-8".equalsIgnoreCase(m_strCodeType)) {
                strRet = new String(inDatas,"UTF-8");
            } else if ("GBK".equalsIgnoreCase(m_strCodeType)) {
                strRet = new String(inDatas,"GBK");
            } else if ("UNICODE".equalsIgnoreCase(m_strCodeType)) {
                strRet = new String(inDatas,"UNICODE");
            } else if ("UTF-16".equalsIgnoreCase(m_strCodeType)) {
                strRet = new String(inDatas,"UTF-16");
            } else if ("ISO-8859-1".equalsIgnoreCase(m_strCodeType)) {
                strRet = new String(inDatas,"ISO-8859-1");
            }
            else {
                strRet = new String(inDatas);
            }
            TcnVendIF.getInstance().LoggerError(TAG, "SocketInputThread  strRet: "+strRet);
            return strRet;
        } catch(Exception e) {
            TcnVendIF.getInstance().LoggerError(TAG, "SocketInputThread Exception e: "+e);
            e.printStackTrace();
        }
        return null;
    }

    private void readSocket() {

        if (null == in)
        {
            TcnVendIF.getInstance().LoggerError(TAG, "SocketInputThread readSocket mInputStream is null");
            closeTCPSocket();
            return;
        }
        mBCanReadSocket = true;
        String receivedString = readFromInputStream(in);
        TcnVendIF.getInstance().LoggerDebug(TAG, "readSocket receivedString: "+receivedString);
        if (mOnReceiveHandler != null) {
            Message msg = mOnReceiveHandler.obtainMessage();
            msg.obj = receivedString;
            msg.what = SOCKET_RECEIVE_MSG;
            mOnReceiveHandler.sendMessage(msg);
        }
    }

    /**
     * Socket连接是否是正常的
     *
     * @return
     */
    public boolean isConnect()
    {
        boolean isConnect = false;
        if (mSocket != null)
        {
            if (mSocket.isConnected() && (!mSocket.isInputShutdown()) && (!mSocket.isOutputShutdown())) {
                isConnect = true;
            }
        }
        return isConnect;
    }


    /**
     * 关闭输入流、输出流、socket
     */
    public void closeTCPSocket() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "closeTCPSocket");
        mBCanReadSocket = false;
        if(null != in) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            in = null;
        }
        if(null != out) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out = null;
        }
        if(null != mSocket) {
            try {
                mSocket.close();
                mSocket.shutdownInput();
                mSocket.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocket=null;
        }
    }

    public void setCodingType(String type) {
        m_strCodeType = type;
    }

    private Handler mOnReceiveHandler = null;
    public void setOnReceiveHandler(Handler mOnReceiveHandler) {
        this.mOnReceiveHandler = mOnReceiveHandler;
    }

    private class SocketOutputThread extends Thread {

        private String m_strMessage = null;

        public SocketOutputThread(String message) {
            super();
            m_strMessage = message;
        }

        @Override
        public void run() {
            super.run();

            int i = 0;
            while (!mBCanReadSocket) {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (10 == i) {
                    break;
                }
                i++;
            }
            sendMsg(m_strMessage);
        }
    }

    public class SocketInputThread extends Thread {

        public SocketInputThread() {
            super();
        }

        @Override
        public void run() {
            super.run();

            readSocket();
            TcnVendIF.getInstance().LoggerDebug(TAG, "SocketInputThread end");
        }
    }

    public class SocketRunThread extends Thread {

    }

}
