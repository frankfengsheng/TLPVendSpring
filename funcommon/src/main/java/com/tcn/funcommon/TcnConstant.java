package com.tcn.funcommon;

import com.tcn.funcommon.vend.protocol.DriveControl.DriveControl;

/**
 * Created by Songjiancheng on 2016/4/12.
 */
public class TcnConstant {

    //public static final String IP="11xxx.158";
    public static final String IP = "www.xxx.com";
    public static final int SCOKET = 9801;

    public static final String APK_UPDATE_URL = "http://xxx.com:103/Vending/update.xml";
    public static final String APK_UPDATE_NAME = "TcnVending.apk";
    public static final String CARD_DEFAULT_KEY = "123456";

    public final static String[] USE_TCN_OR_CUSTOMER_IP = {"默认", "使用自己的服务器"};
    public final static String[] PAY_SYSTEM_TYPE = {"不使用主板支付系统", "使用主板支付系统"};
    public final static String[] QRCODE_SHOW_TYPE = {"NONE","单码显示(多码合一)", "单码显示","两个码显示","不显示二维码","两个码(微信&支付宝)"};
    public final static String[] DATA_TYPE = {"23", "45", DriveControl.DATA_TYPE_DRIVE};

    public final static String[] KEY_SLOT_DISPLAY_TYPE = {"无按键对应","有按键对应", "只有按键对应"};
    public final static String[] DEVICE_CONTROL_TYPE = {"NONE","1", "2","3","OD","弹簧驱动", "4", "5", "6","7"};

    public final static String[] LIFT_MODE = {"default","BoxLunch","RowCol"};

    public final static String[] LATTI_MODE = {"default","refrig"};




    public final static String[] SERIPORT_BAUD_RATE = {"2400","4800","9600","19200","38400","57600","115200"};
    public final static String[] ITEM_COUNT_EVERY_PAGE = {"8","10","14","16","20"};

    public final static String[] SRIPORT_GRP_MAP = {"NONE","0","1","2","0|1","1|2","2|3","0|1|2","1|2|3","2|3|4","0|1|2|3","1|2|3|4","2|3|4|5"};

    public static final String FOLDER_VIDEO_IMAGE_AD_REMOTE = "/TcnFolder/VideoAndImageRemote";
    public static final String FOLDER_VIDEO_IMAGE_AD_GIVEN = "/TcnFolder/VideoAndImage";
    public static final String FOLDER_VIDEO_IMAGE_AD = "/TcnFolder/VideoAndImageAd";
    public static final String FOLDER_IMAGE_GOODS = "/TcnFolder/ImageGoods";
    public static final String FOLDER_IMAGE_SCREEN = "/TcnFolder/ImageScreen";
    public static final String FOLDER_IMAGE_BACKGROUND = "/TcnFolder/ImageBackground";
    public static final String FOLDER_IMAGE_HELP = "/TcnFolder/ImageHelp";
    public static final String FOLDER_TEXT = "/TcnFolder/Text";
    public static final String FOLDER_SHOW = "/TcnFolder/Show";
    public static final String FOLDER_IMAGE_QRPAY = "/QrPayImage";

    public static final String FILENAME_README = "ReadMe.xml";


    public static final String TCN_LOG_NAME = "microlog.txt";

    public static final String TIME="HH:mm";
    public static final String TIME1="HHmm";

    public static final String DATE_FORMAT_YMDHMS = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_HMS = "HHmmss";
    public static final String YEAR = "yyyy/MM/dd-HH:mm:ss";
    public static final String YEAR_HM = "yyyy-MM-dd  HH:mm";
    public static final String YEAR_M_D = "yyyy/MM/dd";
    public static final String Y_M_D = "yyyy-MM-dd";
    public static final String YMD = "yyyyMMdd";

    public static final String USB_CONFIG_SLOT_FILE = "tcn_product.xlsx";
    public static final String USB_CONFIG_IMAGEGOODS_FILE = "TcnImageGoods";


}
