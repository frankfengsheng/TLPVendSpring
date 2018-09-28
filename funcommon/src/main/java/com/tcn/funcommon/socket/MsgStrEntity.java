package com.tcn.funcommon.socket;

import android.os.Handler;

import java.io.UnsupportedEncodingException;

public class MsgStrEntity {
	private int mProtocolType = -1;
	//要发送的消息
	private String mStrMsg;
	//错误处理的handler
	private Handler mHandler;



	public MsgStrEntity(int protocolType, String msg, Handler handler)
	{
		this.mProtocolType = protocolType;
		this.mStrMsg = msg;
		this.mHandler = handler;

	}

	public String getMsg()
	{
		return this.mStrMsg;
	}

	public byte[] getBytes()
	{
		byte[] mBytes = null;
		if (mStrMsg != null) {
			try {
				mBytes = mStrMsg.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return mBytes;
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
