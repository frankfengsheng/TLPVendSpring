package com.tcn.funcommon.socket.NIO;

import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.socket.CLog;
import com.tcn.funcommon.socket.MsgStrEntity;
import com.tcn.funcommon.socket.SocketConst;
import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 客户端写消息线程
 *
 * @author way
 *
 */
public class SocketOutputThread extends Thread
{
	private final static String TAG = "SocketOutputThread";
	private boolean isStart = true;
	private volatile boolean m_bHasNewDada = false;
	private List<MsgStrEntity> sendMsgList = new CopyOnWriteArrayList<MsgStrEntity>();;

	public SocketOutputThread( )
	{
		setName("SocketOutputThread");
	}

	public boolean isBusy() {
		boolean bIsBusy = false;
		if ((sendMsgList != null) && (sendMsgList.size() > 1)) {
			bIsBusy = true;
		}
		return bIsBusy;
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
			CLog.e(TAG, "sendMsg is null");
			return false;
		}

		try
		{
			TCPClient.instance().sendMsg(msg);

		} catch (Exception e)
		{
			TcnVendIF.getInstance().LoggerError(TAG, "NIO SocketOutputThread e " + e);
			throw (e);
		}

		return true;
	}

	// 使用socket发送消息
	public void addMsgToSendList(MsgStrEntity msg)
	{
		synchronized (sendMsgList) {
			this.sendMsgList.add(msg);
		}

		synchronized (this)
		{
			m_bHasNewDada = true;
			notify();
		}
	}

	private CopyOnWriteArrayList<MsgStrEntity> getNewMessageList() {
		CopyOnWriteArrayList<MsgStrEntity> msgToWriteList = new CopyOnWriteArrayList<MsgStrEntity>();
		if (null == sendMsgList) {
			return msgToWriteList;
		}
		for (MsgStrEntity info:sendMsgList) {
			msgToWriteList.add(info);
		}
		return msgToWriteList;
	}

	@Override
	public void run()
	{
		while (isStart)
		{

			// 锁发送list
			synchronized (sendMsgList)
			{
				while (sendMsgList.size() > 0) {

					CopyOnWriteArrayList<MsgStrEntity> msgToWriteList = getNewMessageList();

					// 发送消息
					for (MsgStrEntity msg : msgToWriteList)
					{

						Handler handler = msg.getHandler();
						try
						{
							sendMsg(msg.getMsg().getBytes("UTF-8"));
							sendMsgList.remove(msg);
							// 成功消息，通过hander回传
							if ((handler != null) && isStart)
							{
								Message message = handler.obtainMessage();
								message.arg1 = msg.getProtocolType();
								message.obj = msg.getMsg();
								message.what = SocketConst.SOCKET_SEND_MSG_SUCCESS;
								handler.sendMessage(message);
								//message.obj = msg.getBytes();
								//message.what = SocketConst.SOCKET_SEND_MSG_SUCCESS;

								//handler.sendEmptyMessage(SocketConst.SOCKET_SEND_MSG_SUCCESS);
							}
							CLog.d(TAG, msg.getMsg());
							sleep(200);

						} catch (Exception e)
						{
							e.printStackTrace();
							CLog.e(TAG, e.toString());
							if (sendMsgList != null) {
								sendMsgList.remove(msg);
							}

							// 错误消息，通过hander回传
							if (handler != null)
							{
								Message message = handler.obtainMessage();
								message.arg1 = msg.getProtocolType();
								message.obj = msg.getMsg();
								message.what = SocketConst.SOCKET_SEND_MSG_FAIL;
								handler.sendMessage(message);

							}

							if (!TCPClient.instance().isConnect()) {

								sleep();

								if (!TCPClient.instance().isConnect()) {
									TCPClient.instance().reConnect();
								}

								if (handler != null) {
									handler.sendEmptyMessage(SocketConst.SOCKET_HEART_RECONNECTED);
								}
							}

						}
					}
				}
				m_bHasNewDada = false;
			}

			synchronized (this)
			{
				try
				{
					if (!m_bHasNewDada) {
						wait();
					}
				} catch (InterruptedException e)
				{
					TcnVendIF.getInstance().LoggerError(TAG, "InterruptedException e: "+e.toString());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 发送完消息后，线程进入等待状态
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
}
