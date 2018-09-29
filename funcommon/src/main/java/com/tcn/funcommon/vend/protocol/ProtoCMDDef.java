package com.tcn.funcommon.vend.protocol;

/**
 * Created by Administrator on 2017/6/2.
 */
public class ProtoCMDDef {
    public static final int SERIAL_PORT_RECEIVE_DATA        = 100;
    public static final int SERIAL_PORT_RECEIVE_DATA_NEW   = 101;
    public static final int SERIAL_PORT_CONFIG_ERROR        = 102;
    public static final int SERIAL_PORT_SECURITY_ERROR      = 103;
    public static final int SERIAL_PORT_UNKNOWN_ERROR       = 104;

//    public static final int COMMAND_TOSS_COINS                     = 110; //投硬币
//    public static final int COMMAND_TOSS_PAPER_MONEY              = 111; //投纸币
//    public static final int COMMAND_CHANGE_IN_COINS               = 112; //找硬币
//    public static final int COMMAND_CHANGE_IN_PAPER_MONEY        = 113; //找纸币
//    public static final int COMMAND_BALANCE                        = 114; //上报余额

    public static final int COMMAND_SLOTNO_INFO                  = 144;  //货道信息  此货道类型|货道号|单价|容量|现存|故障|掉货检测开关|此货道销售总数量|此货道销售总金额|商品编码

    //出货
    public static final int SHIP_SHIPING                       = 100; //出货中
    public static final int SHIP_SUCCESS                       = 101; //出货成功
    public static final int SHIP_FAIL                           = 102; //出货失败
}
