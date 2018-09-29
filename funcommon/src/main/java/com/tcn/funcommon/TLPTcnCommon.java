package com.tcn.funcommon;

import android.content.Context;
import android.content.res.Configuration;

import com.tcn.funcommon.vend.controller.TcnVendIF;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/28 19:19
 * 邮箱：m68013@qq.com
 */
public class TLPTcnCommon {
    private static final String TAG = "TcnCommon";
    private static TLPTcnCommon m_Instance = null;
    public static final int WRITE_DATA_CMD_SUCCESS    = 1;
    public static final int WRITE_DATA_CMD_FAIL		 = 2;
    public static final int FRAGMENT_SELECTION = 1;
    public static final int FRAGMENT_PAY = 2;
    public static final int FRAGMENT_VERIFY = 3;
    public static String[] MENU_LIST={"货道管理","密码管理","信息配置","支付系统","程序更新","串口设置","菜单设置"};
   // public static String[] MENU_LIST={"货道管理","密码管理","信息配置","支付系统","程序更新","串口设置"};

    public static String[] SCREEN_INCH={"大屏","7寸"};
    public static String[] SCREEN_ORIENTATION={"竖屏","横屏"};

    public static String[] SLOT_STATE_LIST={"有货","缺货","有故障","停用","隐藏"};

    public static String[] SHIP_FAIL_CONTIN_COUNT_LOCK={"2","3","4","5","6","7","8","9"};

    //出货顺序
    public static String[] GOODSCODE_SHIPTYPE={"按上货顺序售卖","一直卖最前面一个货道"};

    public static final String[] PRICE_UNIT={"元","¥","$","￡","€","원","S$","AED","￥","฿","₭","Rs.","kr","₱","₩","Bs","₦","N$","R","Rp","JOD","ман","RM","₫","A$","PEN"
            ,"COP"};
    public static final String[] PAYTIME_SELECT={"30","60","90","120"};
    public static final String[] REOOT_TIME_SELECT={"none","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
    public static String[] SELECT_ADVERT_POLL_TIME={"10分钟","30分钟","1小时","2小时","4小时","6小时","12小时","15小时","20小时","24小时"};
    public static String[] SELECT_STANDBY_IMAGE_TIME={"20","30","60","90","120"};
    public static String[] SELECT_PLAY_IMAGE_INTERVAL_TIME={"6","10","15","20","30","60","90","120"};

    public static final String[] TIME_DELAY_CLOSE_SELECT={"30","60","90","120","300","480","600"};

    public static final String[] NUMBER_PER_FLOOR={"10","5","6","8"};

    public static String[] SELECT_PLAY_EC_POLL_TIME = {"3","5","10","30","60","120","300","600","1800","3600","10800","21600"};

    public static final String[] TEMPERATURE_SELECT = {
            "-8","-7","-6","-5","-4","-3","-2","-1","0","1","2","3","4", "5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42"
            ,"43","44","45","46","47","48","49","50"};

    public static String[] HEAT_COOL_OFF_SWITCH_SELECT = {"制冷","加热","关闭"};

    public static String[] SLOT_STOCK_SELECT = {"0","1","2","3","4","5","6","7","8","9","10","无限制"};

    public final static String[] SLOTNO_DIGIT_COUNT = {"1","2","3"};

    public final static String[] PRICE_POINT_COUNT = {"0","1","2"};

    private String[] m_MenuList = MENU_LIST;

    /**
     * 首页中每页的图片数
     */
    public static final int PRODUCT_NUM_LANDSCAPE = 12;

    public static final int PRODUCT_NUM_PORTRAIT = 8;

    /**
     * 货道管理中每页的图片数
     */
    public static final int coil_num = 12;
    private volatile int m_iPage = 0;
    private Context m_context = null;

    public static synchronized TLPTcnCommon getInstance() {
        if (null == m_Instance) {
            m_Instance = new TLPTcnCommon();
        }
        return m_Instance;
    }

    public void init(Context context) {
        m_context = context;

        if ((TcnConstant.DATA_TYPE[0]).equals(TcnShareUseData.getInstance().getTcnDataType()) || (TcnConstant.DATA_TYPE[1]).equals(TcnShareUseData.getInstance().getTcnDataType())) {
            MENU_LIST = new String []{m_context.getString(R.string.menu_slots_management),m_context.getString(R.string.menu_goods_management),m_context.getString(R.string.menu_password_management),
                    m_context.getString(R.string.menu_information_settings),m_context.getString(R.string.menu_payment_settings),
                    m_context.getString(R.string.menu_program_update),m_context.getString(R.string.menu_manage_serial_port_settings),m_context.getString(R.string.menu_set_tool),m_context.getString(R.string.salesdata)};
        } else {
            MENU_LIST = new String []{m_context.getString(R.string.menu_slots_management),m_context.getString(R.string.menu_goods_management),m_context.getString(R.string.menu_password_management),
                    m_context.getString(R.string.menu_information_settings),m_context.getString(R.string.menu_payment_settings),
                    m_context.getString(R.string.menu_program_update),m_context.getString(R.string.menu_manage_serial_port_settings),m_context.getString(R.string.menu_set_tool),m_context.getString(R.string.salesdata)
                    ,m_context.getString(R.string.menu_func_set)};
        }

        SLOT_STATE_LIST = new String []{m_context.getString(R.string.slot_state_have_goods),m_context.getString(R.string.slot_state_have_no_goods),
                m_context.getString(R.string.slot_state_fault),m_context.getString(R.string.slot_state_disable),m_context.getString(R.string.slot_state_hide)};

        GOODSCODE_SHIPTYPE = new String []{m_context.getString(R.string.goods_code_ship_order),m_context.getString(R.string.goods_code_ship_first)};

        m_MenuList = MENU_LIST;

        SELECT_ADVERT_POLL_TIME = new String [] {
                10+m_context.getString(R.string.info_time_minutes),30+m_context.getString(R.string.info_time_minutes),
                1+m_context.getString(R.string.info_time_hours),2+m_context.getString(R.string.info_time_hours),
                4+m_context.getString(R.string.info_time_hours),6+m_context.getString(R.string.info_time_hours),
                12+m_context.getString(R.string.info_time_hours),15+m_context.getString(R.string.info_time_hours),
                20+m_context.getString(R.string.info_time_hours),24+m_context.getString(R.string.info_time_hours),};

        HEAT_COOL_OFF_SWITCH_SELECT = new String [] {
                m_context.getString(R.string.menu_cool),
                m_context.getString(R.string.menu_heat),
                m_context.getString(R.string.menu_close)};

        SLOT_STOCK_SELECT = new String [] {"0","1","2","3","4","5","6","7","8","9","10",m_context.getString(R.string.slot_info_unlimited)};
    }

    public String[] getMenuList() {
        return m_MenuList;
    }

    public String[] getReplenishMenuList() {
        String[] m_ReqMenuList = new String []{m_context.getString(R.string.menu_goods_management), m_context.getString(R.string.menu_program_update),"退出售货机"};
        return m_ReqMenuList;
    }

    public void setMenuList(String[] menuList) {
        m_MenuList = menuList;
    }

    public int getProductNumEveryPage() {
        int iNum = 8;
        if (Configuration.ORIENTATION_LANDSCAPE == TcnVendIF.getInstance().getScreenOrientation()) {
            iNum = 12;
        } else {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                iNum = 20;
            } else {
                iNum = 8;
            }
        }
        return iNum;
    }

    public int getGoodsRowNum() {
        int iNum = 3;
        if (Configuration.ORIENTATION_PORTRAIT == TcnVendIF.getInstance().getScreenOrientation()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                int iCountEveryPage = TcnShareUseData.getInstance().getItemCountEveryPage();
                if ((iCountEveryPage == 30) || (iCountEveryPage == 60)) {
                    iNum = 6;
                } else if (iCountEveryPage == 80) {
                    iNum = 8;
                } else {
                    iNum = 4;
                }
            }
        } else {
            iNum = 3;
        }

        return iNum;
    }

    public int getGoodsColumnNum() {
        int iNum = 4;
        int iCountEveryPage = TcnShareUseData.getInstance().getItemCountEveryPage();
        if (Configuration.ORIENTATION_PORTRAIT == TcnVendIF.getInstance().getScreenOrientation()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                if ((iCountEveryPage == 60) || (iCountEveryPage == 80)) {
                    iNum = 10;
                } else if ((iCountEveryPage == 20) || (iCountEveryPage == 30)) {
                    iNum = 5;
                } else {

                }
            } else {
                if (iCountEveryPage == 8) {
                    iNum = 4;
                } else if (iCountEveryPage == 10) {
                    iNum = 5;
                } else {

                }
            }

        } else {
            if (iCountEveryPage == 8) {
                iNum = 4;
            } else if (iCountEveryPage == 10) {
                iNum = 5;
            } else if (iCountEveryPage == 20) {
                iNum = 10;
            } else if (iCountEveryPage == 30) {
                iNum = 15;
            }
            else {

            }
        }
        return iNum;
    }

    public int getGoodsNumEveryPage() {
        int num = getGoodsRowNum() * getGoodsColumnNum();
        return num;
    }

    public void setPage(int page) {
        m_iPage = page;
    }

    public int getPage() {
        return m_iPage;
    }

    private OnBackPayCallBack m_OnBackPayCallBack= null;

    public OnBackPayCallBack getBackPayCallBack() {
        return m_OnBackPayCallBack;
    }

    public void setOnBackPayCallBack(OnBackPayCallBack mOnBackPayCallBack) {
        this.m_OnBackPayCallBack = mOnBackPayCallBack;
    }

    public interface OnBackPayCallBack{
        public void OnPayBack(int id, String[] strr);
    }
}
