package com.tcn.funcommon.socket;

public class SocketConst
{

	//public final static String SOCKET_SERVER = "www..com";
	private final static String SOCKET_SERVER = "11.2.1.1";

	private final static int SOCKET_PORT = 9801;
	//public final static int SOCKET_PORT = 9801;

	// 默认timeout 时间 60s
	public final static int SOCKET_TIMOUT = 60 * 1000;

	public final static int SOCKET_READ_TIMOUT = 20 * 1000;

	//如果没有连接无服务器。读线程的sleep时间
	public final static int SOCKET_SLEEP_SECOND = 2 ;

	//心跳包发送间隔时间
	public final static int SOCKET_HEART_SECOND =20 ;

	public final static String BC = "BC";

	//消息发送成功
	public final static int SOCKET_SEND_MSG_SUCCESS = 999 ;

	//消息发送失败
	public final static int SOCKET_SEND_MSG_FAIL = 1000 ;

	public final static int SOCKET_CONNECTE_FAIL = -1;
	public final static int SOCKET_CONNECTE_SUCCESS = 0;

	public final static int SOCKET_NETWORK_NOT_GOOD = 96 ;

	//接收消息
	public final static int SOCKET_RECEIVE_MSG = 97 ;

	public final static int SOCKET_HEART_CONNECTED = 98;

	public final static int SOCKET_HEART_RECONNECTED = 99;


	private static String m_strSocketServer = SOCKET_SERVER;
	private static int m_iSocketPort = SOCKET_PORT;
	private static int m_iSocketHeartTime = SOCKET_HEART_SECOND; //单位：秒

	public static void  setSocketServer(String server) {
		m_strSocketServer = server;
	}

	public static String  getSocketServer() {
		return m_strSocketServer;
	}

	public static void setSocketPort(int port) {
		m_iSocketPort = port;
	}

	public static int  getSocketPort() {
		return m_iSocketPort;
	}

	public static void  setSocketHeartTime(int heartTime) {
		m_iSocketHeartTime = heartTime;
	}

	public static int getSocketHeartTime() {
		return m_iSocketHeartTime;
	}
}
