package com.tcn.funcommon.socket.NIO;

import android.os.Handler;

import com.tcn.funcommon.socket.MsgStrEntity;


public class SocketThreadManager
{

	private static SocketThreadManager s_SocketManager = null;

	private SocketInputThread mInputThread = null;

	private SocketOutputThread mOutThread = null;

	private SocketHeartThread mHeartThread = null;


	// 获取单例
	public static SocketThreadManager sharedInstance()
	{
		if (s_SocketManager == null)
		{
			s_SocketManager = new SocketThreadManager();
			s_SocketManager.startThreads();
		}
		return s_SocketManager;
	}

	// 单例，不允许在外部构建对象
	private SocketThreadManager()
	{
		mHeartThread = new SocketHeartThread();
		mInputThread = new SocketInputThread();
		mOutThread = new SocketOutputThread();
	}

	/**
	 * 启动线程
	 */

	private void startThreads()
	{
		mHeartThread.start();
		mInputThread.setStart(true);
		mInputThread.start();
		mOutThread.setStart(true);
		mOutThread.start();
		// mDnsthread.start();
	}

	/**
	 * stop线程
	 */
	public void stopThreads()
	{
		mHeartThread.stopThread();
		mInputThread.setStart(false);
		mOutThread.setStart(false);
	}

	public boolean isBusy() {
		boolean bRet = false;
		if (null == mOutThread) {
			return bRet;
		}
		return mOutThread.isBusy();
	}

	public static void releaseInstance()
	{
		if (s_SocketManager != null)
		{
			s_SocketManager.stopThreads();
			s_SocketManager = null;
		}
	}

	public void setHandler(Handler handler) {
		if (mHeartThread != null) {
			mHeartThread.setHandler(handler);
			mInputThread.setOnReceiveHandler(handler);
		}
	}

//	public void sendMsg(int protocolType, byte [] buffer, Handler handler)
//	{
//		MsgEntity entity = new MsgEntity(protocolType, buffer, handler);
//		mOutThread.addMsgToSendList(entity);
//	}

	public void sendMsg(int protocolType, String str, Handler handler)
	{
		if (str != null) {
			MsgStrEntity entity = new MsgStrEntity(protocolType, str, handler);
			mOutThread.addMsgToSendList(entity);
		}

	}

	public void receiveMsg(Handler handler)
	{
		mInputThread.setOnReceiveHandler(handler);
	}

	public boolean reConnect()
	{
		if (null == mHeartThread) {
			return false;
		}
		return mHeartThread.reConnect();
	}

}
