package com.tcn.funcommon.socket.NIO;

import android.os.Handler;

import com.tcn.funcommon.socket.NetManager;
import com.tcn.funcommon.socket.SocketConst;
import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


/**
 * NIO TCP 客户端
 *
 */
public class TCPClient
{
	private final static String TAG = "TCPClient";
	// 信道选择器
	private Selector selector;

	// 与服务器通信的信道
	private SocketChannel socketChannel;

	// 要连接的服务器Ip地址
	private String hostIp;

	// 要连接的远程服务器在监听的端口
	private int hostListenningPort;

	private static TCPClient s_Tcp = null;

	public boolean isInitialized = false;

	private boolean isReceivedData = false;

	public static synchronized TCPClient instance()
	{
		if (s_Tcp == null)
		{
			s_Tcp = new TCPClient(SocketConst.getSocketServer(),
					SocketConst.getSocketPort());

			TcnVendIF.getInstance().LoggerDebug(TAG," SocketConst.getSocketServer(): "+SocketConst.getSocketServer()+" SocketConst.getSocketPort(): "+SocketConst.getSocketPort());
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
		} catch (IOException e)
		{
			this.isInitialized = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e)
		{
			this.isInitialized = false;
			e.printStackTrace();
		}
	}

	/**
	 * 初始化
	 *
	 * @throws IOException
	 */
	public void initialize() throws IOException
	{
		boolean done = false;

		if ((null == hostIp) || (hostIp.length() < 3) || (hostListenningPort < 0)) {
			return;
		}

		try
		{
			// 打开监听信道并设置为非阻塞模式
			socketChannel = SocketChannel.open(new InetSocketAddress(hostIp,
					hostListenningPort));
			if (socketChannel != null)
			{
				// 发送数据包，默认为 false，即客户端发送数据采用 Nagle 算法；
				// 但是对于实时交互性高的程序，建议其改为 true，即关闭 Nagle 算法，客户端每发送一次数据，无论数据包大小都会将这些数据发送出去
				socketChannel.socket().setTcpNoDelay(true);

				// true 作用：每隔一段时间检查服务器是否处于活动状态，如果服务器端长时间没响应，自动关闭客户端socket
				// 防止服务器端无效时，客户端长时间处于连接状态
				socketChannel.socket().setKeepAlive(true);
				// 设置 读socket的timeout时间
				socketChannel.socket().setSoTimeout(SocketConst.SOCKET_READ_TIMOUT);
				socketChannel.configureBlocking(false);    // 将客户端设定为异步

				// 打开并注册选择器到信道
				selector = Selector.open();
				if (selector != null)
				{
					// 在轮讯对象中注册此客户端的读取事件(就是当服务器向此客户端发送数据的时候)
					socketChannel.register(selector, SelectionKey.OP_READ);
					done = true;
				}
			}
		} catch (IOException e) {
			TcnVendIF.getInstance().LoggerError(TAG, "NIO TCPClient e " + e);
		}

		finally
		{
			if (!done && selector != null)
			{
				selector.close();
			}
			if (!done)
			{
				if (socketChannel != null) {
					socketChannel.close();
				}

			}
		}
	}

	static void blockUntil(SelectionKey key, long timeout) throws IOException
	{

		int nkeys = 0;
		if (timeout > 0)
		{
			nkeys = key.selector().select(timeout);

		} else if (timeout == 0)
		{
			nkeys = key.selector().selectNow();
		}

		if (nkeys == 0)
		{
			throw new SocketTimeoutException();
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
		if (!NetManager.instance().isNetworkConnected()) {
			if (mHandler != null) {
				mHandler.sendEmptyMessage(SocketConst.SOCKET_NETWORK_NOT_GOOD);
			}
			return;
		}

		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));

		if (socketChannel == null)
		{
			throw new IOException();
		}
		socketChannel.write(writeBuffer);
	}

	/**
	 * 发送数据
	 *
	 * @param bytes
	 * @throws IOException
	 */
	public void sendMsg(byte[] bytes) throws IOException
	{
		if (!NetManager.instance().isNetworkConnected()) {
//			TcnVendIF.getInstance().LoggerError(TAG, "NIO TCPClient isNetworkConnected false");
			if (mHandler != null) {
				mHandler.sendEmptyMessage(SocketConst.SOCKET_NETWORK_NOT_GOOD);
			}
			return;
		}

		if (null == bytes) {
			TcnVendIF.getInstance().LoggerError(TAG, "NIO TCPClient sendMsg bytes is null ");
			return;
		}
		ByteBuffer writeBuffer = ByteBuffer.wrap(bytes);

		if (socketChannel == null)
		{
			throw new IOException();
		}
		socketChannel.write(writeBuffer);
	}

	/**
	 *
	 * @return
	 */
	public synchronized Selector getSelector()
	{
		return this.selector;
	}

	/**
	 * Socket连接是否是正常的
	 *
	 * @return
	 */
	public boolean isConnect()
	{
		synchronized (this) {
			boolean isConnect = false;
			if (this.isInitialized) {
				if ((this.socketChannel != null) && (this.selector != null)) {
					if (this.socketChannel.isRegistered() && this.socketChannel.isOpen()
							&& this.socketChannel.isConnected() && this.selector.isOpen()
							&& this.socketChannel.socket().isConnected() && (!this.socketChannel.socket().isInputShutdown())
							&& (!this.socketChannel.socket().isOutputShutdown())) {
						isConnect = true;
					}
				}
			}
			return isConnect;
		}
	}

	/**
	 * 关闭socket 重新连接
	 *
	 * @return
	 */
	public boolean reConnect()
	{
		synchronized (this) {

			closeTCPSocket();

			if (!NetManager.instance().isNetworkConnected()) {
				if (mHandler != null) {
					mHandler.sendEmptyMessage(SocketConst.SOCKET_NETWORK_NOT_GOOD);
				}
				return false;
			}
			TcnVendIF.getInstance().LoggerDebug(TAG, "NIO TCPClient reConnect");

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
	}

	public boolean isReceivedData() {
		return isReceivedData;
	}

	public void setReceivedData(boolean isReceived) {
		isReceivedData = isReceived;
	}

	/**
	 * 服务器是否关闭，通过发送一个socket信息
	 *
	 * @return
	 */
	public boolean canConnectToServer()
	{
		/*try
		{
			if (socketChannel != null)
			{
				socketChannel.socket().sendUrgentData(0xff);
			} else {
				return false;
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}*/
		return true;
	}

	/**
	 * 关闭socket
	 */
	public void closeTCPSocket()
	{

		try {
			if (socketChannel != null) {
				socketChannel.socket().shutdownOutput();
			}
		} catch (IOException e) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "NIO closeTCPSocket shutdownOutput IOException e: "+e);
		}

		try {
			if (socketChannel != null) {
				socketChannel.socket().getOutputStream().close();
			}
		} catch (IOException e) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "NIO closeTCPSocket getOutputStream IOException e: "+e);
		}

		try {
			if (socketChannel != null) {
				socketChannel.socket().shutdownInput();
			}
		} catch (IOException e) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "NIO closeTCPSocket shutdownInput IOException e: "+e);
		}

		try {
			if (socketChannel != null) {
				socketChannel.socket().getInputStream().close();
			}
		} catch (IOException e) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "NIO closeTCPSocket getInputStream IOException e: "+e);
		}

		try {
			if (socketChannel != null) {
				socketChannel.socket().close();
			}
		} catch (IOException e) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "NIO closeTCPSocket socket IOException e: "+e);
		}

		try
		{
			if (socketChannel != null)
			{
				socketChannel.close();
				socketChannel = null;
			}

		} catch (IOException e)
		{
			TcnVendIF.getInstance().LoggerDebug(TAG, "NIO closeTCPSocket socketChannel IOException e: "+e);
			socketChannel = null;
		}
		try
		{
			if (selector != null)
			{
				selector.close();
				selector = null;
			}
		} catch (IOException e)
		{
			TcnVendIF.getInstance().LoggerDebug(TAG, "NIO closeTCPSocket selector IOException e: "+e);
			selector = null;
		}
	}

	/**
	 * 每次读完数据后，需要重新注册selector，读取数据
	 */
	public synchronized void repareRead()
	{
		if (socketChannel != null)
		{
			try
			{
				selector = Selector.open();
				socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (ClosedChannelException e)
			{
				e.printStackTrace();

			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private Handler mHandler = null;
	public void setHandler(Handler handler) {
		mHandler = handler;
	}
}
