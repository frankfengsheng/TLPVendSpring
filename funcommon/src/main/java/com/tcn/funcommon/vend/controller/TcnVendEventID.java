package com.tcn.funcommon.vend.controller;

/**
 * 作者：Jiancheng,Song on 2016/5/28 16:19
 * 邮箱：m68013@qq.com
 */
public class TcnVendEventID {
    public static final int	QUERY_ALIVE_COIL			    = 1;
    public static final int	QUERY_IMAGE_PATHLIST            = 2;
    public static final int	QUERY_VIDEO_PATHLIST            = 3;
    public static final int DELETE_TYPE               = 4;
    public static final int MODIFY_TYPE              = 6;
    public static final int IMAGE_AD                        = 7;
    public static final int ACTION_RECEIVE_DATA             = 8;
    public static final int SHOW_TOAST                      = 9;
    public static final int ALL_FILES_PLAY_FAILED           = 10;
    public static final int WX_QR_CODE_GENERATED            = 11;
    public static final int ALI_QR_CODE_GENERATED           = 12;
    public static final int COMMAND_MCU_RECEIVED            = 13;
    public static final int COMMAND_INPUT_MONEY             = 14;
    public static final int COMMAND_SHIPPING                = 15;
    public static final int COMMAND_SHIPMENT_SUCCESS        = 16;
    public static final int COMMAND_SHIPMENT_FAILURE        = 17;
    public static final int COMMAND_SHIPMENT_FAULT          = 18;
    public static final int COMMAND_SELECT_GOODS            = 19;
    public static final int COMMAND_INVALID_SLOTNO          = 20;
    public static final int COMMAND_SOLD_OUT                = 21;
    public static final int COMMAND_COIN_REFUND_START       = 22;
    public static final int COMMAND_COIN_REFUND_END         = 23;
    public static final int COMMAND_CONNECT_SERVER_SUCCESS  = 24;
    public static final int COMMAND_CONNECT_SERVER_FAILED   = 25;
    public static final int COMMAND_NOTSET_CIPMODE          = 26;

    public static final int WX_TRADE_REFUND                 = 27;
    public static final int WX_TRADE_CLOSE                  = 28;
    public static final int ALIPAY_SCAN_COLSE               = 29;
    public static final int ALIPAY_WAVE_COLSE               = 30;
    public static final int PAY_POS_FAILED                  = 31;
    public static final int NETWORK_NOT_GOOOD               = 32;

    public static final int ADJUST_TIME_REQ                 = 33;

    public static final int UPDATE_PAY_TIME                 = 34;
    public static final int IMAGE_SCREEN                    = 35;
    public static final int TEXT_AD                         = 36;
    public static final int SHOW_OR_HIDE_AD_MEDIA         = 37;

    public static final int COMMAND_DOOR_SWITCH             = 38;

    public static final int COMMAND_TEST             = 39;
    public static final int	SERIAL_PORT_CONFIG_ERROR			    = 40;
    public static final int	SERIAL_PORT_SECURITY_ERROR			    = 41;
    public static final int	SERIAL_PORT_UNKNOWN_ERROR			    = 42;
//    public static final int SHOW_VIDEO_SURFACEVIEW    = 43;
//    public static final int HIDE_VIDEO_SURFACEVIEW    = 44;
//    public static final int SHOW_IMAGE_SURFACEVIEW    = 45;
//    public static final int HIDE_IMAGE_SURFACEVIEW    = 46;

    public static final int BACK_TO_SHOPPING =        47;

    public static final int COMMAND_CANCEL_PAY   = 48;
    public static final int COMMAND_TOSS_PAPER_MONEY = 49;
    public static final int QUERY_IMAGE_SHOW_PATH  = 50;

    public static final int COMMAND_FAULT_SLOTNO      = 51;

    public static final int COMMAND_CONFIG_OK      = 52;

    public static final int TRK_PLAY                 = 53;
    public static final int TRK_PAUSE                = 54;
    public static final int TRK_STOP                = 55;
    public static final int PLAY_GIVEN_FOLDER_COMPLETION                = 56;
    public static final int TRK_PLAY_COMPLETION        = 57;

    public static final int	TRK_NEXT_STANDBY                        = 58;
    public static final int	TRK_PLAY_STANDBY                         = 59;
    public static final int	TRK_PAUSE_STANDBY                        = 60;
    public static final int	TRK_STOP_STANDBY                        = 61;

    public static final int	QUERY_ADVERT_PATHLIST            = 65;

    public static final int APP_QR_CODE_GENERATED                = 70;
    public static final int CMD_PLAY_IMAGE                = 71;
    public static final int CMD_PLAY_VIDEO                = 72;
    public static final int IMAGE_BACKGROUND              = 73;
    public static final int NETWORK_CHANGE                = 74;
    public static final int IMAGE_HELP                  = 75;


    public static final int UPDATE_TIME = 77;

    public static final int COMMAND_CHANGEIN_COINS           = 78;
    public static final int COMMAND_CHANGEIN_PAPER_MONEY        = 79;
    public static final int COMMAND_BALANCE        = 80;
    public static final int COMMAND_MACHINE_ID  = 81;

    public static final int COMMAND_FRAME_NUMBER_1     = 90;
    public static final int	COMMAND_FRAME_NUMBER_3          = 93;
    public static final int COMMAND_FRAME_NUMBER_4     = 94;
    public static final int COMMAND_GIVE_CHANGE       =  95;
    public static final int COMMAND_FRAME_NUMBER_9     = 98;
    public static final int COMMAND_FRAME_NUMBER_10       =  99;
    public static final int COMMAND_FRAME_NUMBER_13       =  100;

    public static final int COMMAND_RESTOCK_WITH_ONE_KEY  = 107;
    public static final int COMMAND_TWO_CODE_IN_ONE        = 108;
    public static final int COMMAND_FRAME_NUMBER_19       = 109;  //App（提货码）取货未启用
    public static final int	COMMAND_FRAME_NUMBER_20          = 110;

    public static final int COMMAND_NO_MAPED_SLOTNO          = 120;
    public static final int COMMAND_KEY                        = 121;


    public static final int CMD_PLAY_SCREEN_IMAGE                = 150;
    public static final int CMD_PLAY_SCREEN_VIDEO                = 151;

    public static final int COMMAND_TOTAL_SALES_INFO      = 152;

    public static final int COMMAND_CONFIG_INFO      =  153;

    public static final int	QUERY_ALIVE_GOODS			    = 154;

    public static final int	INSERT_DATA                     = 155;
    public static final int	UPTE_DATA                         = 156;
    public static final int	DELETE_DATA                     = 157;


    public static final int	 COMMAND_FAULT_INFORMATION         = 158;

    public static final int	COMMAND_DOOR_IS_OPEND              = 159;

    public static final int	QUERY_ALIVE_COIL_EXCEPTIONE			= 160;

    public static final int	USB_CONFIG_COPY_IMAGEGOODS_START			= 170;
    public static final int	USB_CONFIG_COPY_IMAGEGOODS_END			= 171;
    public static final int	USB_CONFIG_COPY_LOG_START			= 172;
    public static final int	USB_CONFIG_COPY_LOG_END			= 173;
    public static final int	USB_CONFIG_READINFO_START			= 174;
    public static final int	USB_CONFIG_READINFO_END		    	= 175;
    public static final int	USB_CONFIG_CONFIG_INFO		    	= 176;
    public static final int	USB_CONFIG_SYN_DATA_START			= 177;
    public static final int	USB_CONFIG_SYN_DATA_END			  = 178;

    public static final int	 PROMPT_INFO                          = 190;
    public static final int TEMPERATURE_INFO                   = 191;


    public static final int BACKGROUND_AISLE_MANAGE            = 200;
    public static final int BACKGROUND_GOODS_MANAGE            = 201;
    public static final int BACKGROUND_INFORMATION_CONFIG     = 202;
    public static final int BACKGROUND_PAY_SYSTEM_SETTING     = 203;

    public static final int DELETE_KEY_MAP                        = 209;
    public static final int ADD_SHOW_COIL_ID                       = 210;
    public static final int DELETE_COIL_ID                       = 211;
    public static final int DELETE_GOODS_ID                       = 212;

    public static final int ADD_STOCK                       = 213;
    public static final int SUB_STOCK                       = 214;

    public static final int QRCODE_UNION                        = 220;
    public static final int QRCODE_UNION_APP                   = 221;

    public static final int ECOMMERCE_QRCODE_GENERATE              = 230;

    public static final int	RESTART_MAIN_ACTIVITY			    = 250;
    public static final int	FINISH_MAIN_ACTIVITY			        = 251;

    public static final int	SERVER_HEART_CONNECTED			    = 255;
    public static final int	SERVER_HEART_RECONNECTED			= 256;

    public static final int	THMQH_REQ			= 257;
    public static final int	THMQH_REQ_CANCEL			= 258;

    public static final int	REMOUT_ADVERT_TEXT             = 260;

    public static final int	README_TEXT                    = 265;

    public static final int	PLESE_CLOSE_FOOR               = 270;

    public static final int COMMAND_SYSTEM_BUSY          = 280;

    public static final int ADD_SLOT_GOODS           = 281;
    public static final int UPDATE_SLOTNO_EXTQUANTITY   = 282;
    public static final int UPDATE_SLOTNO_CAPACITY          = 283;
    public static final int FILL_STOCK_TO_CAPACITY_ALL          = 284;
    public static final int FILL_STOCK_TO_CAPACITY           = 285;

    public static final int MDB_RECIVE_PAPER_MONEY = 310;
    public static final int MDB_RECIVE_COIN_MONEY = 311;
    public static final int MDB_BALANCE_CHANGE = 312;
    public static final int MDB_PAYOUT_PAPERMONEY = 313;
    public static final int MDB_PAYOUT_COINMONEY = 314;

    public static final int MDB_SHORT_CHANGE_PAPER = 318;
    public static final int MDB_SHORT_CHANGE_COIN = 319;
    public static final int MDB_SHORT_CHANGE = 320;
    public static final int CMD_COIN_NO_CHANGE = 321;

    public static final int COMMAND_SELECT_FAIL            = 337;
    public static final int CMD_TEST_SLOT            = 338;

    public static final int CMD_QUERY_SLOT_FAULTS            = 340;
    public static final int CMD_CLEAR_SLOT_FAULTS            = 341;
    public static final int CMD_QUERY_SLOT_STATUS            = 342;
    public static final int CMD_QUERY_ADDRESS                = 343;
    public static final int CMD_SELF_CHECK                    = 344;
    public static final int CMD_RESET                    = 345;
    public static final int CMD_QUERY_CABINET_STATUS     = 346;

    public static final int SET_SLOTNO_SPRING                    = 350;
    public static final int SET_SLOTNO_BELTS                    = 351;
    public static final int SET_SLOTNO_ALL_SPRING                    = 352;
    public static final int SET_SLOTNO_ALL_BELT                    = 353;
    public static final int SET_SLOTNO_SINGLE                    = 354;
    public static final int SET_SLOTNO_DOUBLE                    = 355;
    public static final int SET_SLOTNO_ALL_SINGLE                    = 356;

    public static final int SET_TEMP_CONTROL_OR_NOT                  = 360;
    public static final int CMD_SET_COOL                               = 361;
    public static final int CMD_SET_HEAT                               = 362;
    public static final int CMD_SET_TEMP                              = 363;
    public static final int CMD_SET_GLASS_HEAT_OPEN                  = 364;
    public static final int CMD_SET_GLASS_HEAT_CLOSE                  = 365;
    public static final int CMD_READ_CURRENT_TEMP                    = 366;
    public static final int CMD_SET_LIGHT_OPEN                        = 367;
    public static final int CMD_SET_LIGHT_CLOSE                       = 368;
    public static final int CMD_SET_BUZZER_OPEN                       = 369;
    public static final int CMD_SET_BUZZER_CLOSE                      = 370;
    public static final int CMD_READ_DOOR_STATUS                      = 371;
    public static final int CMD_SET_COOL_HEAT_CLOSE                  = 372;


    public static final int CMD_QUERY_STATUS_LIFTER               = 380;
    public static final int CMD_TAKE_GOODS_DOOR                   = 381;
    public static final int CMD_LIFTER_UP                        = 382;
    public static final int CMD_LIFTER_BACK_HOME               = 383;
    public static final int CMD_CLAPBOARD_SWITCH               = 384;
    public static final int CMD_OPEN_COOL                       = 385;
    public static final int CMD_OPEN_HEAT                      = 386;
    public static final int CMD_CLOSE_COOL_HEAT               = 387;
    public static final int CMD_CLEAN_FAULTS                   = 388;
    public static final int CMD_QUERY_PARAMETERS               = 389;
    public static final int CMD_QUERY_DRIVER_CMD               = 390;
    public static final int CMD_SET_SWITCH_OUTPUT_STATUS       = 391;
    public static final int CMD_SET_ID                             = 392;
    public static final int CMD_SET_LIGHT_OUT_STEP               = 393;
    public static final int CMD_SET_PARAMETERS                    = 394;
    public static final int CMD_FACTORY_RESET                    = 395;
    public static final int CMD_DETECT_LIGHT                     = 396;
    public static final int CMD_DETECT_SHIP                       = 397;
    public static final int CMD_DETECT_SWITCH_INPUT               = 398;

    public static final int COMMAND_QUERY_PARAMETERS           = 399;

    public static final int CMD_TAKE_GOODS_FIRST              = 400;
    public static final int CMD_SHIP_FAIL_TAKE_GOODS_FIRST              = 401;


    public static final int CMD_SCAN_LIGHT_SET                   = 430;      //扫描灯设置
    public static final int CMD_CABINETNO_SET_VAILD                  = 431;    //格子设置有效无效
    public static final int CMD_CABINETNO_SET_INVAILD                  = 432;    //格子设置有效无效
    public static final int CMD_CABINETNO_OFF                  = 433;


    public static final int PAY_FAIL            = 450;

    public static final int CMD_MACHINE_LOCKED           = 451;

    public static final int CMD_CARD_CONSUM           = 460;
    public static final int CMD_CARD_CONSUMING           = 461;
    public static final int CMD_CARD_HAS_REQ_CONSUM     = 462;
    public static final int CMD_CARD_BALANCE           = 463;
    public static final int CMD_CARD_FAULT             = 464;
    public static final int CMD_CARD_REFUND           = 465;


    public static final int CMD_VERIFY_SHIP           = 470;
    public static final int CMD_VERIFY_SHIP_LUCKY           = 471;

    public static final int CMD_MACHINE_QRCODE           = 475;

    public static final int CMD_QUERY_WATER_TEMP           = 500;
    public static final int CMD_QUERY_SHIP_CUP            = 501;
    public static final int CMD_QUERY_CLEAN               = 502;
    public static final int CMD_QUERY_COFF_STATUS           = 503;

    public static final int CMD_QUERY_SNAKE_STATUS     = 510;
    public static final int CMD_LED_OFF           = 511;
    public static final int CMD_LED_ON           = 512;
    public static final int CMD_COOL_TEMP_MODE           = 513;
    public static final int CMD_HEAT_TEMP_MODE           = 514;
    public static final int CMD_COOL_LEFT_TEMP_MODE           = 515;
    public static final int CMD_COOL_RIGHT_TEMP_MODE           = 516;
    public static final int CMD_HEAT_LEFT_TEMP_MODE           = 517;
    public static final int CMD_HEAT_RIGHT_TEMP_MODE           = 518;
    public static final int CMD_COOL_LEFT_HEAT_RIGHT_TEMP_MODE           = 519;
    public static final int CMD_HEAT_LEFT_COOL_RIGHT_TEMP_MODE           = 520;
    public static final int CMD_HEAT_COOL_NONE_MODE                  = 521;
    public static final int CMD_COOL_HEAT_DETAIL_PARAME           = 522;
    public static final int CMD_QUERY_GOODS           = 523;
    public static final int CMD_SET_SELL_LASTONE           = 524;
    public static final int CMD_SET_NOT_SELL_LASTONE           = 525;
    public static final int CMD_SET_BOARD_ADDR           = 526;


    public static final int CMD_KEY_SET_STATUS               = 531;

    public static final int CMD_KEY_VALUE_PRE                = 533;

    public static final int CMD_KEY_SET_VALUE                 = 535;
    public static final int CMD_KEY_SET_PATTERN_MODE            = 537;
    public static final int CMD_KEY_EXIT_PATTERN_MODE            = 539;
    public static final int CMD_KEY_EXIT_FLICKER_STATUS         = 541;
    public static final int CMD_KEY_SET_SOLDOUT_KEY_PATTERN    = 543;
    public static final int CMD_KEY_SET_SOLDOUT_KEY               = 545;
    public static final int CMD_KEY_SET_ALL_SAME_COLOR          = 547;
    public static final int CMD_KEY_KEY_FLICKER                   = 549;
    public static final int CMD_KEY_VALUE_SET_WAIT                   = 550;

    public static final int CMD_REQ_PERMISSION           = 600;

}
