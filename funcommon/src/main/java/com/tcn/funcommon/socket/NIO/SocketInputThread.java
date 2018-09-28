package com.tcn.funcommon.socket.NIO;

import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.socket.CLog;
import com.tcn.funcommon.socket.NetManager;
import com.tcn.funcommon.socket.SocketConst;
import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * 客户端读消息线程
 *
 * @author way
 *
 */
public class SocketInputThread extends Thread
{
	private final static String TAG = "SocketInputThread";
	private boolean isStart = true;

	// private MessageListener messageListener;// 消息监听接口对象

	public SocketInputThread()
	{
		setName("SocketInputThread");
	}

	public void setStart(boolean isStart)
	{
		this.isStart = isStart;
		if (!isStart) {
			if (mOnReceiveHandler != null) {
				mOnReceiveHandler.removeCallbacksAndMessages(null);
			}
		}
	}

	@Override
	public void run()
	{
		while (isStart)
		{
			// 手机能联网，读socket数据
			if (NetManager.instance().isNetworkConnected())
			{
				if (!TCPClient.instance().isConnect())
				{
					TcnVendIF.getInstance().LoggerDebug(TAG, "NIO connet server is fail,sleep second: " + SocketConst.SOCKET_SLEEP_SECOND);

					sleep();

					if (!TCPClient.instance().isConnect()) {
						TCPClient.instance().reConnect();
					}

					if (mOnReceiveHandler != null) {
						mOnReceiveHandler.sendEmptyMessage(SocketConst.SOCKET_HEART_RECONNECTED);
					}
				}

				readSocket();

				// 如果连接服务器失败,服务器连接失败，sleep固定的时间，能联网，就不需要sleep

				//CLog.e(TAG, "NIO isConnect() " + TCPClient.instance().isConnect()+" isStart: "+isStart);


			} else {
				if (mOnReceiveHandler != null) {
					mOnReceiveHandler.sendEmptyMessage(SocketConst.SOCKET_NETWORK_NOT_GOOD);
				}
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
		try
		{
			Selector selector = TCPClient.instance().getSelector();
			if (selector == null)
			{
				CLog.e(TAG, "NIO readSocket() selector is null ");
				TCPClient.instance().closeTCPSocket();
				return;
			}

			boolean isDisconnect = false;


			// 如果没有数据过来，一直柱塞
			while (selector.select() > 0)
			{
				int index = 0;
				isDisconnect = true;

				for (SelectionKey sk : selector.selectedKeys())
				{
					index++;
					isDisconnect = false;
					// 如果该SelectionKey对应的Channel中有可读的数据
					if (sk.isValid() && sk.isReadable())
					{
						// 使用NIO读取Channel中的数据
						SocketChannel sc = (SocketChannel) sk.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						buffer.clear();
						int readsize = 0;

						try
						{
							readsize = sc.read(buffer);
						} catch (IOException e)
						{
							TcnVendIF.getInstance().LoggerError(TAG, "NIO readSocket() IOException e: " + e);
							// TODO Auto-generated catch block
							e.printStackTrace();
							//sc.close();
							//sk.cancel();
						}

						buffer.flip();

						if (readsize > 0) {
							String receivedString = "";
							// 打印收到的数据
							try
							{
								receivedString = Charset.forName("UTF-8")
										.newDecoder().decode(buffer).toString();

								TCPClient.instance().setReceivedData(true);

								TcnVendIF.getInstance().LoggerDebug(TAG, "readSocket server msg: " + receivedString);

								if (mOnReceiveHandler != null) {
									Message msg = mOnReceiveHandler.obtainMessage();
									msg.obj = receivedString;
									msg.what = SocketConst.SOCKET_RECEIVE_MSG;
									mOnReceiveHandler.sendMessage(msg);
								}

							} catch (CharacterCodingException e)
							{
								TcnVendIF.getInstance().LoggerError(TAG, "NIO readSocket() CharacterCodingException e: " + e);
								// TODO Auto-generated catch block
								e.printStackTrace();
								//sc.close();
								//sk.cancel();
							}
						} else if(-1 == readsize) {
							//当客户端断开连接时会触发read事件，并且可以读到-1，这时关闭通道
							sc.close();
							sk.cancel();
							if (selector.selectedKeys().size() <= index) {
								isDisconnect = true;
							}
							TcnVendIF.getInstance().LoggerDebug(TAG, "readSocket index: " + index+" size: "+selector.selectedKeys().size());
						} else {

						}

						buffer.clear();
						buffer = null;

						try
						{
							// 为下一次读取作准备
							sk.interestOps(SelectionKey.OP_READ);
							// 删除正在处理的SelectionKey
							selector.selectedKeys().remove(sk);

						} catch (CancelledKeyException e)
						{
							e.printStackTrace();
							sc.close();
							sk.cancel();
							if (selector.selectedKeys().size() <= index) {
								isDisconnect = true;
							}
							TcnVendIF.getInstance().LoggerError(TAG, "NIO readSocket() CancelledKeyException e: " + e+" index: "+index);
						}


					} else {
						TcnVendIF.getInstance().LoggerError(TAG, "NIO readSocket() continue");
						continue;
					}
				}    //end for

				if (isDisconnect) {
					isDisconnect = false;
					break;
				}

				selector = TCPClient.instance().getSelector();
				if (null == selector) {
					break;
				}
			}
			//selector.selectedKeys().clear();
			// selector.close();
			// TCPClient.instance().repareRead();

		} catch (IOException e1)
		{
			TcnVendIF.getInstance().LoggerError(TAG, "111 NIO readSocket() IOException e1: " + e1);
			TCPClient.instance().closeTCPSocket();

			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClosedSelectorException e2)
		{
			TcnVendIF.getInstance().LoggerError(TAG, "222 NIO readSocket() IOException e2: " + e2);
			TCPClient.instance().closeTCPSocket();
		} catch (Exception e) {
			TcnVendIF.getInstance().LoggerError(TAG, "NIO readSocket() Exception e: " + e);
		}
	}

	private Handler mOnReceiveHandler = null;
	public void setOnReceiveHandler(Handler mOnReceiveHandler) {
		this.mOnReceiveHandler = mOnReceiveHandler;
	}

//	private SocketResponse<String> mOnSocketListener = null;
//	private void setOnSocketListener(SocketResponse<String> mOnSocketListener) {
//		this.mOnSocketListener = mOnSocketListener;
//	}
//	
//	private void removeSocketListener() {
//		this.mOnSocketListener = null;
//	}

}
