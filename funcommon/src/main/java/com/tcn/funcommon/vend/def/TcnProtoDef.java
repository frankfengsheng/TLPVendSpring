package com.tcn.funcommon.vend.def;

/**
 * Created by Administrator on 2017/6/7.
 */
public class TcnProtoDef {
    //串口
    public static final int SERIAL_PORT_RECEIVE_DATA                = 100;
    public static final int SERIAL_PORT_RECEIVE_DATA_OTHER         = 101;
    public static final int SERIAL_PORT_CONFIG_ERROR                = 102;
    public static final int SERIAL_PORT_SECURITY_ERROR              = 103;
    public static final int SERIAL_PORT_UNKNOWN_ERROR               = 104;


    public static final int COMMAND_TOSS_COINS                     = 110; //投硬币
    public static final int COMMAND_TOSS_PAPER_MONEY              = 111; //投纸币
    public static final int COMMAND_CHANGE_IN_COINS               = 112; //找硬币
    public static final int COMMAND_CHANGE_IN_PAPER_MONEY        = 113; //找纸币
    public static final int COMMAND_BALANCE                        = 114; //上报余额

    //出货
    public static final int COMMAND_SHIPMENT_CASHPAY             = 120; //现金购买
    public static final int COMMAND_SHIPMENT_WECHATPAY           = 121; //微信支付出货
    public static final int COMMAND_SHIPMENT_ALIPAY              = 122; //支付宝出货
    public static final int COMMAND_SHIPMENT_GIFTS               = 123; //赠送出货
    public static final int COMMAND_SHIPMENT_REMOTE              = 124; //远程App出货   此次支付金额|货道号|状态
    public static final int COMMAND_SHIPMENT_VERIFY              = 125; //提货码出货  此次支付金额|货道号|状态
    public static final int COMMAND_SHIPMENT_BANKCARD_ONE       = 126; //刷银行卡1支付出货 MDB卡支付
    public static final int COMMAND_SHIPMENT_BANKCARD_TWO       = 127; //刷银行卡2支付出货
    public static final int COMMAND_SHIPMENT_TCNCARD_OFFLINE   = 128; //刷中吉IC卡离线支付出货
    public static final int COMMAND_SHIPMENT_TCNCARD_ONLINE    = 129; //刷中吉IC卡在线支付出货
    public static final int COMMAND_SHIPMENT_OTHER_PAY          = 130; //其它支付出货  此次支付金额|货道号|状态|支付方式


    public static final int COMMAND_TOTAL_SLOT_NUMBER           = 140;  //总货道数   最大货道号|总有效货道数
    public static final int COMMAND_TEMPERATURE_INFO            = 141;  //温度   柜号|温度|柜号|温度......|柜号|温度
    public static final int COMMAND_DOOR_SWITCH                  = 142;  //门状态  C/O
    public static final int COMMAND_KEY_NUMBER                   = 143;  //按键编号  按键号|已经选择的货道号(如果为0表示还没选择货道号)


    public static final int COMMAND_SLOTNO_INFO                  = 144;  //货道信息  此货道类型|货道号|单价|容量|现存|故障|掉货检测开关|此货道销售总数量|此货道销售总金额|商品编码

    public static final int COMMAND_SLOTNO_INFO_SINGLE                  = 145;

    public static final int CMD_LOOP                                     = 146;

    public static final int CMD_READ_TEMP                               = 147;


    public static final int COMMAND_SELECT_SLOTNO                        = 150; //选择货道
    public static final int COMMAND_INVALID_SLOTNO                       = 151;
    public static final int COMMAND_FAULT_SLOTNO                        = 152;
    public static final int COMMAND_SELECT_FAIL                        = 153;
    public static final int COMMAND_SELECT_KEY                        = 154;

    public static final int COMMAND_SNAKE_SELECT_SLOTNO                       = 155;
    public static final int COMMAND_SNAKE_SELECT_FAIL                        = 156;
    public static final int COMMAND_SNAKE_SELECT_SLOT_ERR                   = 157;
    public static final int COMMAND_SNAKE_SELECT_SLOT_SOLD_OUT                   = 158;
    public static final int COMMAND_SNAKE_SELECT_SELECT_BUSY                   = 159;

    public static final int COMMAND_BUSY                              = 180;
    public static final int REQ_CMD_TEST_SLOT                          = 181;
    public static final int CMD_TEST_SLOT                              = 182; //测试货道

    public static final int REQ_QUERY_SLOT_STATUS                   = 190;
    public static final int QUERY_SLOT_STATUS                       = 191;

    public static final int REQ_SELF_CHECK                   = 194;
    public static final int SELF_CHECK                        = 195;
    public static final int REQ_CMD_RESET                   = 196;
    public static final int CMD_RESET                       = 197;
    public static final int REQ_SET_SLOTNO_SPRING                   = 198;
    public static final int SET_SLOTNO_SPRING                       = 199;
    public static final int REQ_SET_SLOTNO_BELTS                   = 200;
    public static final int SET_SLOTNO_BELTS                       = 201;
    public static final int REQ_SET_SLOTNO_ALL_SPRING                   = 202;
    public static final int SET_SLOTNO_ALL_SPRING                       = 203;
    public static final int REQ_SET_SLOTNO_ALL_BELT                   = 204;
    public static final int SET_SLOTNO_ALL_BELT                       = 205;
    public static final int REQ_SET_SLOTNO_SINGLE                   = 206;
    public static final int SET_SLOTNO_SINGLE                       = 207;
    public static final int REQ_SET_SLOTNO_DOUBLE                   = 208;
    public static final int SET_SLOTNO_DOUBLE                       = 209;
    public static final int REQ_SET_SLOTNO_ALL_SINGLE                   = 210;
    public static final int SET_SLOTNO_ALL_SINGLE                       = 211;
    public static final int REQ_SET_TEST_MODE                             = 212;
    public static final int SET_TEST_MODE                       = 213;

    public static final int REQ_SET_TEMP_CONTROL_OR_NOT             = 215;
    public static final int SET_TEMP_CONTROL_OR_NOT                  = 216;
    public static final int REQ_CMD_SET_COOL                          = 217;
    public static final int CMD_SET_COOL                               = 218;
    public static final int REQ_CMD_SET_HEAT                          = 219;
    public static final int CMD_SET_HEAT                               = 220;
    public static final int REQ_CMD_SET_TEMP                          = 221;
    public static final int CMD_SET_TEMP                              = 222;
    public static final int REQ_CMD_SET_GLASS_HEAT_OPEN                  = 223;
    public static final int CMD_SET_GLASS_HEAT_OPEN                       = 224;
    public static final int REQ_CMD_SET_GLASS_HEAT_CLOSE                  = 225;
    public static final int CMD_SET_GLASS_HEAT_CLOSE                       = 226;
    public static final int REQ_CMD_READ_CURRENT_TEMP               = 227;
    public static final int CMD_READ_CURRENT_TEMP                    = 228;
    public static final int REQ_CMD_SET_LIGHT_OPEN                   = 229;
    public static final int CMD_SET_LIGHT_OPEN                        = 230;
    public static final int REQ_CMD_SET_LIGHT_CLOSE                  = 231;
    public static final int CMD_SET_LIGHT_CLOSE                       = 232;
    public static final int REQ_CMD_SET_BUZZER_OPEN                  = 233;
    public static final int CMD_SET_BUZZER_OPEN                       = 234;
    public static final int REQ_CMD_SET_BUZZER_CLOSE                 = 235;
    public static final int CMD_SET_BUZZER_CLOSE                      = 236;
    public static final int REQ_CMD_SET_COOL_HEAT_CLOSE                 = 237;
    public static final int CMD_SET_COOL_HEAT_CLOSE                      = 238;
    public static final int REQ_CMD_READ_DOOR_STATUS                 = 239;
    public static final int CMD_READ_DOOR_STATUS                      = 240;

    /*************************升降机 start************************************/
    public static final int REQ_CMD_QUERY_STATUS               = 250;
    public static final int CMD_QUERY_STATUS                    = 251;
    public static final int REQ_CMD_TAKE_GOODS_DOOR            = 254;
    public static final int CMD_TAKE_GOODS_DOOR                 = 255;
    public static final int REQ_CMD_LIFTER_UP                    = 256;
    public static final int CMD_LIFTER_UP                        = 257;
    public static final int REQ_CMD_LIFTER_BACK_HOME            = 258;
    public static final int CMD_LIFTER_BACK_HOME                = 259;
    public static final int REQ_CMD_CLAPBOARD_SWITCH            = 260;
    public static final int CMD_CLAPBOARD_SWITCH                = 261;
    public static final int REQ_CMD_OPEN_COOL                    = 262;
    public static final int CMD_OPEN_COOL                       = 263;
    public static final int REQ_CMD_OPEN_HEAT                 = 264;
    public static final int CMD_OPEN_HEAT                      = 265;
    public static final int REQ_CMD_CLOSE_COOL_HEAT           = 266;
    public static final int CMD_CLOSE_COOL_HEAT               = 267;
    public static final int REQ_CMD_CLEAN_FAULTS               = 268;
    public static final int CMD_CLEAN_FAULTS                   = 269;
    public static final int REQ_CMD_QUERY_PARAMETERS           = 270;
    public static final int CMD_QUERY_PARAMETERS               = 271;
    public static final int REQ_CMD_QUERY_DRIVER_CMD           = 272;
    public static final int CMD_QUERY_DRIVER_CMD                = 273;
    public static final int REQ_CMD_SET_SWITCH_OUTPUT_STATUS  = 274;
    public static final int CMD_SET_SWITCH_OUTPUT_STATUS               = 275;
    public static final int REQ_CMD_SET_ID                        = 276;
    public static final int CMD_SET_ID                            = 277;
    public static final int REQ_CMD_SET_LIGHT_OUT_STEP               = 278;
    public static final int CMD_SET_LIGHT_OUT_STEP               = 279;
    public static final int REQ_CMD_SET_PARAMETERS               = 280;
    public static final int CMD_SET_PARAMETERS                    = 281;
    public static final int REQ_CMD_FACTORY_RESET               = 282;
    public static final int CMD_FACTORY_RESET                   = 283;
    public static final int REQ_CMD_DETECT_LIGHT               = 284;
    public static final int CMD_DETECT_LIGHT                    = 285;
    public static final int REQ_CMD_DETECT_SHIP                = 286;
    public static final int CMD_DETECT_SHIP                     = 287;
    public static final int REQ_CMD_DETECT_SWITCH_INPUT       = 288;
    public static final int CMD_DETECT_SWITCH_INPUT            = 289;

    public static final int CMD_TAKE_GOODS_FIRST                = 290;

    public static final int CMD_SHIP_FAIL_TAKE_GOODS_FIRST    = 291;
    public static final int CMD_SHIP_SLOT_ERRCODE_UPDATE      = 292;


    public static final int REQ_CMD_QUERY_ADDRESS                  = 350;
    public static final int CMD_QUERY_ADDRESS                       = 351;
    public static final int REQ_CMD_SCAN_LIGHT_SET                 = 352;
    public static final int CMD_SCAN_LIGHT_SET                      = 353;
    public static final int REQ_CMD_CABINETNO_SET_VAILD            = 354;
    public static final int CMD_CABINETNO_SET_VAILD                 = 355;
    public static final int REQ_CMD_CABINETNO_SET_INVAILD          = 356;   // 柜号设置
    public static final int CMD_CABINETNO_SET_INVAILD               = 357;   // 柜号设置
    public static final int REQ_CMD_CABINETNO_OFF                  = 358;
    public static final int CMD_CABINETNO_OFF                      = 359;
    public static final int CMD_CABINETNO_ON                       = 360;


    public static final int REQ_CMD_QUERY_WATER_TEMP               = 370;
    public static final int CMD_QUERY_WATER_TEMP                    = 371;
    public static final int REQ_CMD_QUERY_SHIP_CUP                  = 372;
    public static final int CMD_QUERY_SHIP_CUP                      = 373;
    public static final int REQ_CMD_QUERY_CLEAN                     = 374;
    public static final int CMD_QUERY_CLEAN                          = 375;
    public static final int REQ_CMD_QUERY_COFF_STATUS               = 376;
    public static final int CMD_QUERY_COFF_STATUS                    = 377;

    public static final int REQ_CMD_QUERY_SNAKE_STATUS                  = 390;
    public static final int CMD_QUERY_SNAKE_STATUS                  = 391;

    public static final int REQ_CMD_LED_OFF                               = 393;
    public static final int CMD_LED_OFF                               = 394;

    public static final int REQ_CMD_LED_ON                               = 395;
    public static final int CMD_LED_ON                               = 396;

    public static final int REQ_CMD_HEAT_COOL_TEMP_MODE                        = 397;

    public static final int CMD_COOL_TEMP_MODE                        = 398;

    public static final int CMD_HEAT_TEMP_MODE                        = 399;

    public static final int CMD_COOL_LEFT_TEMP_MODE                        = 400;

    public static final int CMD_COOL_RIGHT_TEMP_MODE                        = 402;

    public static final int CMD_HEAT_LEFT_TEMP_MODE                        = 404;

    public static final int CMD_HEAT_RIGHT_TEMP_MODE                        = 406;

    public static final int CMD_COOL_LEFT_HEAT_RIGHT_TEMP_MODE                        = 408;

    public static final int CMD_HEAT_LEFT_COOL_RIGHT_TEMP_MODE                        = 410;
    public static final int REQ_CMD_COOL_HEAT_DETAIL_PARAME                           = 411;
    public static final int CMD_COOL_HEAT_DETAIL_PARAME                        = 412;

    public static final int CMD_HEAT_COOL_NONE_MODE                  = 413;

    public static final int REQ_CMD_QUERY_GOODS                     = 416;
    public static final int CMD_QUERY_GOODS                        = 417;

    public static final int CMD_QUERY_GOODS_SELF                        = 418;

    public static final int REQ_CMD_SET_NOT_SELL_LASTONE                     = 419;
    public static final int CMD_SET_NOT_SELL_LASTONE                        = 420;

    public static final int REQ_CMD_SET_SELL_LASTONE                     = 421;
    public static final int CMD_SET_SELL_LASTONE                        = 422;

    public static final int REQ_CMD_SET_BOARD_ADDR                     = 423;
    public static final int CMD_SET_BOARD_ADDR                        = 424;

    public static final int REQ_CMD_READ_SNAKE_CURRENT_TEMP       = 425;


    public static final int REQ_CMD_KEY_SET_STATUS               = 430;
    public static final int CMD_KEY_SET_STATUS               = 431;

    public static final int REQ_CMD_KEY_VALUE_PRE                = 432;
    public static final int CMD_KEY_VALUE_PRE                = 433;
    public static final int REQ_CMD_KEY_SET_VALUE                 = 434;
    public static final int CMD_KEY_SET_VALUE                 = 435;
    public static final int REQ_CMD_KEY_SET_PATTERN_MODE         = 436;
    public static final int CMD_KEY_SET_PATTERN_MODE            = 437;
    public static final int REQ_CMD_KEY_EXIT_PATTERN_MODE       = 438;
    public static final int CMD_KEY_EXIT_PATTERN_MODE            = 439;
    public static final int REQ_CMD_KEY_EXIT_FLICKER_STATUS         = 440;
    public static final int CMD_KEY_EXIT_FLICKER_STATUS         = 441;
    public static final int REQ_CMD_KEY_SET_SOLDOUT_KEY_PATTERN    = 442;
    public static final int CMD_KEY_SET_SOLDOUT_KEY_PATTERN    = 443;
    public static final int REQ_CMD_KEY_SET_SOLDOUT_KEY               = 444;
    public static final int CMD_KEY_SET_SOLDOUT_KEY               = 445;
    public static final int REQ_CMD_KEY_SET_ALL_SAME_COLOR          = 446;
    public static final int CMD_KEY_SET_ALL_SAME_COLOR          = 447;
    public static final int REQ_CMD_KEY_KEY_FLICKER                   = 448;
    public static final int CMD_KEY_KEY_FLICKER                   = 449;
    public static final int CMD_KEY_VALUE_SET_WAIT                   = 450;


    public static final int CMD_NO_DATA_RECIVE       = 500;


    /*************************升降机 end************************************/

}
