package com.tcn.funcommon.vend.protocol;

/**
 * Created by Administrator on 2018/4/23.
 */
public class TradeInfo {
	private String m_tradeNo = null;
	private int m_slotNo = -1;
	private int m_payType = -1;
	private boolean m_bIsPaySuccess = false;
	private String m_amount = null;
	private String m_generatTime = null;

	public TradeInfo(int slotNo, int payType,boolean isPaySuccess,String tradeNo,String amount,String generatTime) {
		this.m_slotNo = slotNo;
		this.m_payType = payType;
		this.m_bIsPaySuccess = isPaySuccess;
		this.m_tradeNo = tradeNo;
		this.m_amount = amount;
		this.m_generatTime = generatTime;
	}

	public String getTradeNo()
	{
		return this.m_tradeNo;
	}

	public void setTradeNo(String tradeNo)
	{
		this.m_tradeNo = tradeNo;
	}

	public boolean isPaySuccess()
	{
		return this.m_bIsPaySuccess;
	}

	public void setPaySuccess(boolean paySuccess)
	{
		this.m_bIsPaySuccess = paySuccess;
	}

	public String getAmount()
	{
		return this.m_amount;
	}

	public void setAmount(String amount)
	{
		this.m_amount = amount;
	}

	public int getSlotNo()
	{
		return this.m_slotNo;
	}

	public void setSlotNo(int slotNo)
	{
		this.m_slotNo = slotNo;
	}

	public int getPayType()
	{
		return this.m_payType;
	}

	public void setPayType(int payType)
	{
		this.m_payType = payType;
	}

	public String getGeneratTime()
	{
		return this.m_generatTime;
	}

	public void setGeneratTime(String time)
	{
		this.m_generatTime = time;
	}
}
