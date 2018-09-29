package com.tcn.funcommon.vend.protocol.DriveControl;

import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */
public class BoardGroupControl {
    public static final int BOARD_SPRING               = 5;    //弹簧机
    public static final int BOARD_LATTICE              = 6;    //格子机
    public static final int BOARD_ELEVATOR             = 7;    //升降机
    public static final int BOARD_COFF                 = 8;    //咖啡机
    public static final int BOARD_SNAKE                 = 9;    //蛇形机

    public static final int GROUP_SERIPORT_1             = 1;    //串口1
    public static final int GROUP_SERIPORT_2              = 2;    //串口2
    public static final int GROUP_SERIPORT_3             = 3;    //串口3
    public static final int GROUP_SERIPORT_4             = 4;    //串口4

    private static final String[] BOARD_GROUP_NUMBER_ARR = {"0","1","2","3","4","5","6","7","8","9"};
    private static final String[] BOARD_LATTICE_GROUP_NUMBER_ARR = {"1","2","3","4","5","6","7","8","9"};

    private static BoardGroupControl m_Instance = null;

    private volatile List<GroupInfo> m_GroupInfoList = new ArrayList<GroupInfo>();


    public static synchronized BoardGroupControl getInstance() {
        if (null == m_Instance) {
            m_Instance = new BoardGroupControl();
        }
        return m_Instance;
    }

    public void initialize(String board1,String board2,String board3,String board4,String group1,String group2,String group3,String group4) {
        m_GroupInfoList.clear();
        if ((TcnConstant.DEVICE_CONTROL_TYPE[9]).equals(board1)) {
            GroupInfo mGroupInfo = new GroupInfo();
            mGroupInfo.setID(0);
            mGroupInfo.setSerGrpNo(GROUP_SERIPORT_1);
            mGroupInfo.setBoardGrpNo(1);
            mGroupInfo.setBoardType(BOARD_SNAKE);
            mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_SNAKE,1,0));
            m_GroupInfoList.add(mGroupInfo);
        } else if ((TcnConstant.DEVICE_CONTROL_TYPE[7]).equals(board1)) {
            List<Integer> mGroupList = getGroupList(group1);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_1);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_ELEVATOR);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_ELEVATOR,mGroupList.get(i),0));
                m_GroupInfoList.add(mGroupInfo);
            }
        } else if ((TcnConstant.DEVICE_CONTROL_TYPE[5]).equals(board1)) {
            List<Integer> mGroupList = getGroupList(group1);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_1);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_SPRING);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_SPRING,mGroupList.get(i),0));
                m_GroupInfoList.add(mGroupInfo);
            }
        }
        else if ((TcnConstant.DEVICE_CONTROL_TYPE[6]).equals(board1)) {  //格子机
            List<Integer> mGroupList = getGroupList(group1);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_1);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_LATTICE);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_LATTICE,mGroupList.get(i),0));
                m_GroupInfoList.add(mGroupInfo);
            }
        } else if ((TcnConstant.DEVICE_CONTROL_TYPE[8]).equals(board1)) {
            List<Integer> mGroupList = getGroupList(group1);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_1);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_COFF);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_COFF,mGroupList.get(i),0));
                m_GroupInfoList.add(mGroupInfo);
            }
        }
        else {

        }

        int iMaxId = getMaxId();
        int iMaxSlotNo = getMaxSlotNo();
        if ((TcnConstant.DEVICE_CONTROL_TYPE[7]).equals(board2)) {
            List<Integer> mGroupList = getGroupList(group2);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_2);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_ELEVATOR);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_ELEVATOR,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        } else if ((TcnConstant.DEVICE_CONTROL_TYPE[5]).equals(board2)) {
            List<Integer> mGroupList = getGroupList(group2);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_2);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_SPRING);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_SPRING,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        }
        else if ((TcnConstant.DEVICE_CONTROL_TYPE[6]).equals(board2)) {  //格子机
            List<Integer> mGroupList = getGroupList(group2);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_2);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_LATTICE);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_LATTICE,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        } else if ((TcnConstant.DEVICE_CONTROL_TYPE[8]).equals(group2)) {
            List<Integer> mGroupList = getGroupList(group2);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_2);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_COFF);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_COFF,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        }
        else {

        }

        iMaxId = getMaxId();
        iMaxSlotNo = getMaxSlotNo();
        if ((TcnConstant.DEVICE_CONTROL_TYPE[7]).equals(board3)) {
            List<Integer> mGroupList = getGroupList(group3);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_3);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_ELEVATOR);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_ELEVATOR,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        } else if ((TcnConstant.DEVICE_CONTROL_TYPE[5]).equals(board3)) {
            List<Integer> mGroupList = getGroupList(group3);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_3);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_SPRING);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_SPRING,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        }
        else if ((TcnConstant.DEVICE_CONTROL_TYPE[6]).equals(board3)) {  //格子机
            List<Integer> mGroupList = getGroupList(group3);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_3);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_LATTICE);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_LATTICE,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        } else if ((TcnConstant.DEVICE_CONTROL_TYPE[8]).equals(board3)) {
            List<Integer> mGroupList = getGroupList(board3);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_3);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_COFF);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_COFF,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        }
        else {

        }

        iMaxId = getMaxId();
        iMaxSlotNo = getMaxSlotNo();
        if ((TcnConstant.DEVICE_CONTROL_TYPE[7]).equals(board4)) {
            List<Integer> mGroupList = getGroupList(group4);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_4);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_ELEVATOR);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_ELEVATOR,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        } else if ((TcnConstant.DEVICE_CONTROL_TYPE[5]).equals(board4)) {
            List<Integer> mGroupList = getGroupList(group4);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_4);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_SPRING);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_SPRING,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        } else if ((TcnConstant.DEVICE_CONTROL_TYPE[6]).equals(board4)) {  //格子机
            List<Integer> mGroupList = getGroupList(group4);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_4);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_LATTICE);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_LATTICE,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        } else if ((TcnConstant.DEVICE_CONTROL_TYPE[8]).equals(board3)) {
            List<Integer> mGroupList = getGroupList(board3);
            for (int i = 0; i < mGroupList.size(); i++) {
                GroupInfo mGroupInfo = new GroupInfo();
                mGroupInfo.setID(iMaxId + 1 + i);
                mGroupInfo.setSerGrpNo(GROUP_SERIPORT_4);
                mGroupInfo.setBoardGrpNo(mGroupList.get(i));
                mGroupInfo.setBoardType(BOARD_COFF);
                mGroupInfo.setMaxSlotNo(getMaxSlotNo(BOARD_COFF,mGroupList.get(i),iMaxSlotNo));
                m_GroupInfoList.add(mGroupInfo);
            }
        }
        else {

        }
    }

    public void setBoardGroup(int serPortType,int boardType, List<Integer> groupList) {
        int iMaxSlotNo = getMaxSlotNo();
        for (int i = 0; i < groupList.size(); i++) {
            GroupInfo mGroupInfo = new GroupInfo();
            mGroupInfo.setID(i);
            mGroupInfo.setSerGrpNo(serPortType);
            mGroupInfo.setBoardGrpNo(groupList.get(i));
            mGroupInfo.setBoardType(boardType);
            mGroupInfo.setMaxSlotNo(getMaxSlotNo(boardType,groupList.get(i),iMaxSlotNo));
            m_GroupInfoList.add(mGroupInfo);
        }
    }

    public String[] getBoardGroupNumberArr() {
        return BOARD_GROUP_NUMBER_ARR;
    }

    public String[] getBoardLatticeGroupNumberArr() {
        return BOARD_LATTICE_GROUP_NUMBER_ARR;
    }

    public List<GroupInfo> getGroupListAll() {
        return m_GroupInfoList;
    }

    public List<GroupInfo> getGroupListSpring() {
        List<GroupInfo> m_list = new ArrayList<GroupInfo>();
        for (GroupInfo info:m_GroupInfoList) {
            if (info.getBoardType() == BOARD_SPRING) {
                m_list.add(info);
            }
        }
        return m_list;
    }

    public List<GroupInfo> getGroupListElevator() {
        List<GroupInfo> m_list = new ArrayList<GroupInfo>();
        for (GroupInfo info:m_GroupInfoList) {
            if (info.getBoardType() == BOARD_ELEVATOR) {
                m_list.add(info);
            }
        }
        return m_list;
    }

    public List<GroupInfo> getGroupListLattice() {
        List<GroupInfo> m_list = new ArrayList<GroupInfo>();
        for (GroupInfo info:m_GroupInfoList) {
            if (info.getBoardType() == BOARD_LATTICE) {
                m_list.add(info);
            }
        }
        return m_list;
    }

    public List<GroupInfo> getGroupListCoff() {
        List<GroupInfo> m_list = new ArrayList<GroupInfo>();
        for (GroupInfo info:m_GroupInfoList) {
            if (info.getBoardType() == BOARD_COFF) {
                m_list.add(info);
            }
        }
        return m_list;
    }


    public List<Integer> getMachineGroupListAll() {
        List<Integer> m_list = new ArrayList<Integer>();
        for (GroupInfo info:m_GroupInfoList) {
            m_list.add(info.getID());
        }
        return m_list;
    }

    public List<Integer> getMachineGroupListSpring() {
        List<Integer> m_list = new ArrayList<Integer>();
        for (GroupInfo info:m_GroupInfoList) {
            if (info.getBoardType() == BOARD_SPRING) {
                m_list.add(info.getID());
            }
        }
        return m_list;
    }

    public List<Integer> getMachineGroupListLattice() {
        List<Integer> m_list = new ArrayList<Integer>();
        for (GroupInfo info:m_GroupInfoList) {
            if (info.getBoardType() == BOARD_LATTICE) {
                m_list.add(info.getID());
            }
        }
        return m_list;
    }

    public GroupInfo getMachineGroupInfo(int grpId) {
        GroupInfo mGroupInfo = null;
        for (GroupInfo info:m_GroupInfoList) {
            if (info.getID() == grpId) {
                mGroupInfo = info;
                break;
            }
        }
        return mGroupInfo;
    }

    public int getStartSlotNo(int grpId) {
        int iStartSlotNo = -1;
        for (GroupInfo info:m_GroupInfoList) {
            if (info.getID() == grpId) {
                iStartSlotNo = info.getMaxSlotNo() - 100 + 1;
                break;
            }
        }
        return iStartSlotNo;
    }

    public GroupInfo getGroupInfo(int slotNo) {
        GroupInfo mGroupInfo = null;
        for (GroupInfo info:m_GroupInfoList) {
            if (slotNo < info.getMaxSlotNo()) {
                if ((info.getMaxSlotNo() - slotNo) < 100) {
                    mGroupInfo = info;
                    break;
                }
            }
        }
        return mGroupInfo;
    }

    public int getAddrSlotNo(int slotNo) {

        if (slotNo < 100) {
            return slotNo;
        }

        int iAddrSlotNo = slotNo % 100;

        return iAddrSlotNo;
    }

    public int getFirstSlotNo(int boardType) {
        int iSlotNo = -1;
        for (GroupInfo info:m_GroupInfoList) {
            if (info.getBoardType() == boardType) {
                iSlotNo = info.getMaxSlotNo() - 100 + 1;
                break;
            }
        }
        return iSlotNo;
    }

    public int getFirstSlotNo(GroupInfo grpinfo) {
        int iSlotNo = -1;
        if (null == grpinfo) {
            return iSlotNo;
        }
        iSlotNo = grpinfo.getMaxSlotNo() - 100 + 1;

        return iSlotNo;
    }

    public GroupInfo getGroupInfoFirst(int boardType) {
        GroupInfo mGroupInfo = null;
        for (GroupInfo info:m_GroupInfoList) {
            if (info.getBoardType() == boardType) {
                mGroupInfo = info;
                break;
            }
        }
        return mGroupInfo;
    }

    public GroupInfo getGroupInfoFirst() {
        GroupInfo mGroupInfo = null;
        if (m_GroupInfoList.size() < 1) {
            return mGroupInfo;
        }

        mGroupInfo = m_GroupInfoList.get(0);

        return mGroupInfo;
    }

    public GroupInfo getGroupInfoNext(int serGrp, byte boardGrp) {
        int index = -1;
        GroupInfo mGroupInfo = null;
        for (GroupInfo info:m_GroupInfoList) {
            if ((info.getSerGrpNo() == serGrp) && (info.getBoardGrpNo() == getGroupNum(boardGrp))) {
                index = m_GroupInfoList.indexOf(info);
                break;
            }
        }
        if (index < 0) {
            return mGroupInfo;
        }
        if (m_GroupInfoList.size() > (index + 1)) {
            mGroupInfo = m_GroupInfoList.get(index + 1);
        }
        return mGroupInfo;
    }

    public GroupInfo getGroupInfoNext(int serGrp, String boardHexGrp) {
        int index = -1;
        GroupInfo mGroupInfo = null;
        for (GroupInfo info:m_GroupInfoList) {
            if ((info.getSerGrpNo() == serGrp) && (info.getBoardGrpNo() == (Integer.parseInt(boardHexGrp,16)))) {
                index = m_GroupInfoList.indexOf(info);
                break;
            }
        }
        if (index < 0) {
            return mGroupInfo;
        }
        if (m_GroupInfoList.size() > (index + 1)) {
            mGroupInfo = m_GroupInfoList.get(index + 1);
        }
        return mGroupInfo;
    }

    public String getGroupHexNum(int grp) {
        String strStrGrp = "FF";
        if (grp == 0) {
            strStrGrp = "00";
        } else if (grp == 1) {
            strStrGrp = "01";
        } else if (grp == 2) {
            strStrGrp = "02";
        } else if (grp == 3) {
            strStrGrp = "03";
        } else if (grp == 4) {
            strStrGrp = "04";
        } else if (grp == 5) {
            strStrGrp = "05";
        } else if (grp == 6) {
            strStrGrp = "06";
        } else if (grp == 7) {
            strStrGrp = "07";
        } else if (grp == 8) {
            strStrGrp = "08";
        } else if (grp == 9) {
            strStrGrp = "09";
        } else {

        }
        return strStrGrp;
    }

    public byte getGroup(int grp) {
        byte bStrGrp = (byte)0xFF;
        if (grp == 0) {
            bStrGrp = (byte)0x00;
        } else if (grp == 1) {
            bStrGrp = (byte)0x01;
        } else if (grp == 2) {
            bStrGrp = (byte)0x02;
        } else if (grp == 3) {
            bStrGrp = (byte)0x03;
        } else if (grp == 4) {
            bStrGrp = (byte)0x04;
        } else if (grp == 5) {
            bStrGrp = (byte)0x05;
        } else if (grp == 6) {
            bStrGrp = (byte)0x06;
        } else if (grp == 7) {
            bStrGrp = (byte)0x07;
        } else if (grp == 8) {
            bStrGrp = (byte)0x08;
        } else if (grp == 9) {
            bStrGrp = (byte)0x09;
        } else {

        }
        return bStrGrp;
    }

    private int getGroupNum(byte grp) {
        int iStrGrp = -1;
        if (grp == ((byte)0x00)) {
            iStrGrp = 0;
        } else if (grp == ((byte)0x01)) {
            iStrGrp = 1;
        } else if (grp == ((byte)0x02)) {
            iStrGrp = 2;
        } else if (grp == ((byte)0x03)) {
            iStrGrp = 3;
        } else if (grp == ((byte)0x04)) {
            iStrGrp = 4;
        } else if (grp == ((byte)0x05)) {
            iStrGrp = 5;
        } else if (grp == ((byte)0x06)) {
            iStrGrp = 6;
        } else if (grp == ((byte)0x07)) {
            iStrGrp = 7;
        } else if (grp == ((byte)0x08)) {
            iStrGrp = 8;
        } else if (grp == ((byte)0x09)) {
            iStrGrp = 9;
        } else {

        }
        return iStrGrp;

    }

    private int getMaxSlotNo(int boardType, int boardGrp,int curMaxSlotNo) {
        int iMaxSlotNo = 0;
        int iAddNum = 0;

        if (0 == boardGrp) {
            iMaxSlotNo = iAddNum + 100;
        } else if (1 == boardGrp) {
            iMaxSlotNo = iAddNum + 200;
        } else if (2 == boardGrp) {
            iMaxSlotNo = iAddNum + 300;
        } else if (3 == boardGrp) {
            iMaxSlotNo = iAddNum + 400;
        } else if (4 == boardGrp) {
            iMaxSlotNo = iAddNum + 500;
        } else if (5 == boardGrp) {
            iMaxSlotNo = iAddNum + 600;
        }  else if (6 == boardGrp) {
            iMaxSlotNo = iAddNum + 700;
        } else if (7 == boardGrp) {
            iMaxSlotNo =  iAddNum + 800;
        } else if (8 == boardGrp) {
            iMaxSlotNo = iAddNum + 900;
        } else if (9 == boardGrp) {
            iMaxSlotNo = iAddNum + 1000;
        }
        else {

        }
        if ((boardType == BOARD_LATTICE) && (0 != iMaxSlotNo)) {  //格子机组号从1开始
            iMaxSlotNo = iMaxSlotNo - 100;
        } else if ((boardType == BOARD_SNAKE) && (0 != iMaxSlotNo)) {
            iMaxSlotNo = iMaxSlotNo - 100;
        } else {

        }

        if (curMaxSlotNo > 0) {
            iMaxSlotNo = iMaxSlotNo + curMaxSlotNo;
        }
        return iMaxSlotNo;
    }

    private int getMaxId() {
        int iMaxId = 0;
        if (m_GroupInfoList.size() < 1) {
            return iMaxId;
        }
        GroupInfo info = m_GroupInfoList.get(m_GroupInfoList.size() - 1);
        if (info != null) {
            iMaxId = info.getID();
        }
        return iMaxId;
    }

    private int getMaxSlotNo() {
        int iMaxSlotNo = 0;
        if (m_GroupInfoList.size() < 1) {
            return iMaxSlotNo;
        }
        GroupInfo info = m_GroupInfoList.get(m_GroupInfoList.size() - 1);
        if (info != null) {
            iMaxSlotNo = info.getMaxSlotNo();
        }
        return iMaxSlotNo;
    }

    private List<Integer> getGroupList(String group) {
        List<Integer> m_List = new ArrayList<Integer>();
        if ((null == group)) {
            return m_List;
        }
        List<Integer> groupList = new ArrayList<Integer>();
        if (group.contains("|")) {
            String[] strArr = group.split("\\|");
            for (String data:strArr) {
                if (TcnUtility.isDigital(data)) {
                    groupList.add(Integer.valueOf(data));
                }
            }
        } else {
            if (TcnUtility.isDigital(group)) {
                groupList.add(Integer.valueOf(group));
            }
        }
        return groupList;
    }

    public int getMaxSlotNo(int type) {
        int iRet = -1;
        GroupInfo mInfo = null;
        for (GroupInfo info: m_GroupInfoList) {
            if (info.getBoardType() == type) {
                mInfo = info;
                break;
            }
        }
        if ((mInfo != null) && (mInfo.getMaxSlotNo() > 0)) {
            iRet = mInfo.getMaxSlotNo();
        }
        return iRet;
    }

    public boolean hasGroupFirst() {
        boolean bRet = false;
        for (GroupInfo info: m_GroupInfoList) {
            if (info.getSerGrpNo() == GROUP_SERIPORT_1) {
                bRet = true;
                break;
            }
        }
        return bRet;
    }

    public boolean hasGroupSecond() {
        boolean bRet = false;
        for (GroupInfo info: m_GroupInfoList) {
            if (info.getSerGrpNo() == GROUP_SERIPORT_2) {
                bRet = true;
                break;
            }
        }
        return bRet;
    }

    public boolean hasGroupThird() {
        boolean bRet = false;
        for (GroupInfo info: m_GroupInfoList) {
            if (info.getSerGrpNo() == GROUP_SERIPORT_3) {
                bRet = true;
                break;
            }
        }
        return bRet;
    }

    public boolean hasGroupFourth() {
        boolean bRet = false;
        for (GroupInfo info: m_GroupInfoList) {
            if (info.getSerGrpNo() == GROUP_SERIPORT_4) {
                bRet = true;
                break;
            }
        }
        return bRet;
    }

    public int getGroupFirstType() {
        int iRet = -1;
        for (GroupInfo info: m_GroupInfoList) {
            if (info.getSerGrpNo() == GROUP_SERIPORT_1) {
                iRet = info.getBoardType();
                break;
            }
        }
        return iRet;
    }

    public int getGroupSecondType() {
        int iRet = -1;
        for (GroupInfo info: m_GroupInfoList) {
            if (info.getSerGrpNo() == GROUP_SERIPORT_2) {
                iRet = info.getBoardType();
                break;
            }
        }
        return iRet;
    }

    public int getGroupThirdType() {
        int iRet = -1;
        for (GroupInfo info: m_GroupInfoList) {
            if (info.getSerGrpNo() == GROUP_SERIPORT_3) {
                iRet = info.getBoardType();
                break;
            }
        }
        return iRet;
    }

    public int getGroupFourthType() {
        int iRet = -1;
        for (GroupInfo info: m_GroupInfoList) {
            if (info.getSerGrpNo() == GROUP_SERIPORT_4) {
                iRet = info.getBoardType();
                break;
            }
        }
        return iRet;
    }

    public boolean hasMachine(int type) {
        boolean bRet = false;
        for (GroupInfo info: m_GroupInfoList) {
            if (info.getBoardType() == type) {
                bRet = true;
                break;
            }
        }
        return bRet;
    }


    public boolean hasSprMachine() {
        boolean bRet = hasMachine(BOARD_SPRING);
        return bRet;
    }

    public boolean hasLatMachine() {
        boolean bRet = hasMachine(BOARD_LATTICE);
        return bRet;
    }

    public boolean hasLiftMachine() {
        boolean bRet = hasMachine(BOARD_ELEVATOR);
        return bRet;
    }

    public boolean hasCoffMachine() {
        boolean bRet = hasMachine(BOARD_COFF);
        return bRet;
    }

    public boolean hasSnakeMachine() {
        boolean bRet = hasMachine(BOARD_SNAKE);
        return bRet;
    }

    private String getFirstGroupFromList(List<String> groupList) {
        String strRet = "FF";
        if (null == groupList) {
            return strRet;
        }
        if (groupList.size() > 0) {
            strRet = groupList.get(0);
        }
        return strRet;
    }

    private String getNextGroupFromList(List<String> groupList, String group) {
        String strRet = "FF";
        if (null == groupList) {
            return strRet;
        }
        int index = groupList.indexOf(group);
        if (-1 == index) {
            return strRet;
        }
        int length = groupList.size();
        if ((index + 1) >= length) {
            return strRet;
        }
        String grp = groupList.get(index + 1);
        if (!TcnUtility.isDigital(grp)) {
            return strRet;
        }
        strRet = grp;
        return strRet;
    }

    private int getEndSlotNo(int boardType,List<String> grpList) {
        int iEndSlotNo = 0;
        if ((null == grpList) || (grpList.size() < 1)) {
            return iEndSlotNo;
        }
        String strSlotNo = grpList.get(grpList.size() - 1);
        if (TcnUtility.isDigital(strSlotNo)) {
            int iMaxGrp = Integer.parseInt(strSlotNo);
            if (0 == iMaxGrp) {
                iEndSlotNo = 100;
            } else if (1 == iMaxGrp) {
                iEndSlotNo = 200;
            } else if (2 == iMaxGrp) {
                iEndSlotNo = 300;
            } else if (3 == iMaxGrp) {
                iEndSlotNo = 400;
            } else if (4 == iMaxGrp) {
                iEndSlotNo = 500;
            } else if (5 == iMaxGrp) {
                iEndSlotNo = 600;
            }  else if (6 == iMaxGrp) {
                iEndSlotNo = 700;
            } else if (7 == iMaxGrp) {
                iEndSlotNo =  800;
            } else if (8 == iMaxGrp) {
                iEndSlotNo = 900;
            } else if (9 == iMaxGrp) {
                iEndSlotNo = 1000;
            }
            else {

            }
            if ((boardType == BOARD_LATTICE) && (0 != iMaxGrp)) {
                iEndSlotNo = iEndSlotNo - 100;
            }
        }
        return iEndSlotNo;
    }
}
