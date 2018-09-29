package com.tcn.funcommon.socket.NIO;

import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.socket.CLog;
import com.tcn.funcommon.socket.NetManager;
import com.tcn.funcommon.socket.SocketConst;


class SocketHeartThread extends Thread
{
	private final static String TAG = "SocketHeartThread";
	private boolean isStop = false;
	private volatile int  m_iConnectTimeCount = 0;
	private volatile int  m_iHeartCount = 0;
	private Handler mHandler = null;
	private static SocketHeartThread s_instance;

	private TCPClient mTcpClient = null;

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
		setName("SocketHeartThread");
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


	public boolean reConnect()
	{
		return TCPClient.instance().reConnect();
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
		TCPClient.instance().setHandler(handler);
	}

	public void run()
	{
		isStop = false;
		while (!isStop)
		{
			// 发送一个心跳包看服务器是否正常
			//boolean canConnectToServer = TCPClient.instance().canConnectToServer();
			boolean isConnect = TCPClient.instance().isConnect();
			CLog.d(TAG, "NIO SocketHeartThread isConnect: "+isConnect);
			if(isConnect) {
				if (TCPClient.instance().isReceivedData()) {
					m_iConnectTimeCount = 0;
					TCPClient.instance().setReceivedData(false);
					if (mHandler != null) {
						Message msg = mHandler.obtainMessage();
						msg.what = SocketConst.SOCKET_HEART_CONNECTED;
						if (NetManager.instance().isNetworkConnected()) {
							msg.arg1 = SocketConst.SOCKET_CONNECTE_SUCCESS;
						} else {
							msg.arg1 = SocketConst.SOCKET_CONNECTE_FAIL;
						}
						msg.arg2 = m_iHeartCount;
						mHandler.sendMessage(msg);
					}
				} else {
					if(m_iConnectTimeCount++ > 6) {
						m_iConnectTimeCount = 0;
						boolean success = reConnect();
						if (mHandler != null) {
							Message msg = mHandler.obtainMessage();
							msg.what = SocketConst.SOCKET_HEART_RECONNECTED;
							if (NetManager.instance().isNetworkConnected()) {
								if (success) {
									msg.arg1 = SocketConst.SOCKET_CONNECTE_SUCCESS;
								} else {
									msg.arg1 = SocketConst.SOCKET_CONNECTE_FAIL;
								}
							} else {
								msg.arg1 = SocketConst.SOCKET_CONNECTE_FAIL;
							}
							msg.arg2 = m_iHeartCount;
							mHandler.sendMessage(msg);
						}
					} else {
						if (mHandler != null) {
							Message msg = mHandler.obtainMessage();
							msg.what = SocketConst.SOCKET_HEART_CONNECTED;
							if (NetManager.instance().isNetworkConnected()) {
								msg.arg1 = SocketConst.SOCKET_CONNECTE_SUCCESS;
							} else {
								msg.arg1 = SocketConst.SOCKET_CONNECTE_FAIL;
							}
							msg.arg2 = m_iHeartCount;
							mHandler.sendMessage(msg);
						}
					}
				}

			} else {
				m_iConnectTimeCount = 0;
				boolean success = reConnect();
				if (mHandler != null) {
					Message msg = mHandler.obtainMessage();
					msg.what = SocketConst.SOCKET_HEART_RECONNECTED;
					if (NetManager.instance().isNetworkConnected()) {
						if (success) {
							msg.arg1 = SocketConst.SOCKET_CONNECTE_SUCCESS;
						} else {
							msg.arg1 = SocketConst.SOCKET_CONNECTE_FAIL;
						}
					} else {
						msg.arg1 = SocketConst.SOCKET_CONNECTE_FAIL;
					}
					msg.arg2 = m_iHeartCount;
					mHandler.sendMessage(msg);
				}
			}

			try
			{
				Thread.sleep(SocketConst.getSocketHeartTime() * 1000);

				m_iHeartCount++;

				if (m_iHeartCount >= Integer.MAX_VALUE) {
					m_iHeartCount = 0;
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
