package com.tcn.funcommon.vend.protocol;

/**
 * Created by Administrator on 2017/12/9.
 */
public class ShipSlotInfo {

    private int m_iSlotNo = -1;
    private long m_lNeedShipTime = -1;
    private long m_lTomeOut = -1;

    public ShipSlotInfo(int slotNo, long needShipTime, int timeOut) {
        m_iSlotNo = slotNo;
        m_lNeedShipTime = needShipTime;
        m_lTomeOut = timeOut;
    }

    public int getShipSlotNo() {
        return m_iSlotNo;
    }

    public void setShipSlotNo(int slotNo) {
        m_iSlotNo = slotNo;
    }

    public long getNeedShipTime() {
        return m_lNeedShipTime;
    }

    public void setNeedShipTime(long time) {
        m_lNeedShipTime = time;
    }

    public long getTomeOut() {
        return m_lTomeOut;
    }

    public void setTomeOut(long timeOutValue) {
        m_lTomeOut = timeOutValue;
    }
}
