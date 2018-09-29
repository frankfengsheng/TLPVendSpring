package com.tcn.funcommon.vend.controller;

/**
 * Created by Administrator on 2017/6/9.
 */
public class TcnVendEventResultID {

    public static final int CMD_NO_DATA_RECIVE        = -10;

    public static final int QR_CODE_GENERATE_QRURL_SUCCESS = 2;
    public static final int QR_CODE_GENERATE_SUCCESS = 1;
    public static final int QR_CODE_GENERATE_FAILED = -1;
    public static final int QR_CODE_MACHINE_OFFLINE  = -2;

    public static final int MDB_PAYOUT_START = -1;
    public static final int MDB_PAYOUT_END = 0;

    public static final int SHIP_SHIPING             = 1; //出货中
    public static final int SHIP_SUCCESS             = 2; //出货成功
    public static final int SHIP_FAIL                = 3; //出货失败

    public static final int FAIL                   = -1; //操作失败
    public static final int SUCCESS                = 0; //操作成功

    public static final int OFF_SUCCESS		            = 1;
    public static final int OFF_FAIL		            = 0;
    public static final int OFF_CLOSING		         = -1;

    public static final int STATUS_INVALID		             = -1;
    public static final int STATUS_FREE		             = 1;
    public static final int STATUS_BUSY		             = 2;
    public static final int STATUS_WAIT_TAKE_GOODS		 = 3;
    public static final int STATUS_SHIPING_DRINK		    = 4;
    public static final int STATUS_SHIPING_CUP		    = 5;
    public static final int STATUS_CLEAN		             = 6;
    public static final int STATUS_HEATING		             = 7;
    public static final int STATUS_HEATING_START		     = 8;
    public static final int STATUS_HEATING_END 		     = 9;



    public static final int CMD_DETECT_LIGHT_INVALID        = -1;
    public static final int CMD_DETECT_LIGHT_BLOCKED        = 0;  //检测升降机光检   表示挡住
    public static final int CMD_DETECT_LIGHT_NOT_BLOCKED   = 1; //检测升降机光检   表示没挡住

    public static final int CMD_DETECT_SHIP_INVALID        = -1;
    public static final int CMD_DETECT_SHIP_NO_GOODS      = 0;  //0 表示没有货物，1 表示有货物
    public static final int CMD_DETECT_SHIP_HAVE_GOODS    = 1;  //0 表示没有货物，1 表示有货物

    public static final int DO_INVALID   = -1;
    public static final int DO_OPEN   = 1;     //开门
    public static final int DO_CLOSE   = 2;     //关门


    public static final int DO_NONE   = -1;
    public static final int DO_START   = 0;
    public static final int DO_END   = 1;


    public static final int SLOTNO_STATUS_INVALID      = -1;
    public static final int SLOTNO_STATUS_HAVE_GOODS   = 1;
    public static final int SLOTNO_STATUS_ERR           = 2;
    public static final int SLOTNO_STATUS_SOLD_OUT     = 3;


    public static final int BUTTON_EMPTY                   = 1;
    public static final int BUTTON_PERPARE                   = 2;
    public static final int BUTTON_NORMAL                   = 4;
    public static final int BUTTON_PRESS                   = 8;

}
