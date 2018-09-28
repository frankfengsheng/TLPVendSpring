package com.tcn.funcommon.socket;

import android.os.Handler;


/**
 * 存储发送socket的类，包含要发送的BufTest，以及对应的返回结果的Handler
 * @author Administrator
 *
 */
public class MsgEntity
{
	private int mProtocolType = -1;
	//要发送的消息
	private byte [] bytes;
	//错误处理的handler
	private Handler mHandler;



	public MsgEntity(int protocolType, byte [] bytes, Handler handler)
	{
		this.mProtocolType = protocolType;
		this.bytes = bytes;
		this.mHandler = handler;

	}

	public byte []  getBytes()
	{
		return this.bytes;
	}

	public Handler getHandler()
	{
		return mHandler;
	}

	public int getProtocolType()
	{
		return mProtocolType;
	}

}
