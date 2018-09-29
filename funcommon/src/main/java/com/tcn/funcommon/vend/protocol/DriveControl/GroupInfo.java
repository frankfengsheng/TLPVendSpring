package com.tcn.funcommon.vend.protocol.DriveControl;

/**
 * Created by Administrator on 2017/10/18.
 */
public class GroupInfo {
    private int m_iID = -1;//柜子编号
    private int m_iSerGrpNo = -1;  //串口号
    private int m_iBoardGrpNo = -1;  //驱动板组号
    private int m_iBoardType = -1;  //驱动板类型
    private int m_iMaxSlotNo = -1;

    public int getID() {
        return m_iID;
    }
    public void setID(int iD) {
        m_iID = iD;
    }

    public int getSerGrpNo() {
        return m_iSerGrpNo;
    }

    public void setSerGrpNo(int serGrpNo) {
        m_iSerGrpNo = serGrpNo;
    }

    public int getBoardGrpNo() {
        return m_iBoardGrpNo;
    }

    public void setBoardGrpNo(int grpNo) {
        m_iBoardGrpNo = grpNo;
    }

    public int getBoardType() {
        return m_iBoardType;
    }

    public void setBoardType(int type) {
        m_iBoardType = type;
    }

    public int getMaxSlotNo() {
        return m_iMaxSlotNo;
    }

    public void setMaxSlotNo(int slotNo) {
        m_iMaxSlotNo = slotNo;
    }
}
