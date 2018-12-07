package com.tcn.funcommon;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 作者：Jiancheng,Song on 2016/5/28 15:18
 * 邮箱：m68013@qq.com
 */
public class TcnShareUseData {
    private static TcnShareUseData m_Instance = null;
    public Context m_context = null;
    public static synchronized TcnShareUseData getInstance() {
        if (null == m_Instance) {
            m_Instance = new TcnShareUseData();
        }
        return m_Instance;
    }

    public void init(Context context) {
        m_context = context;
    }
    /**
     *@desc 获取登录密码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public String getLoginPassword() {
        SharedPreferences sp = m_context.getSharedPreferences("psw_code", Context.MODE_PRIVATE);
        String posPayMachtId = null;
        if (null != sp) {
            posPayMachtId = sp.getString("psw", "000000");
        }

        return posPayMachtId;
    }

    /**
     *@desc 设置登录密码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:24
     */
    public void setLoginPassword(String password) {
        SharedPreferences sp = m_context.getSharedPreferences("psw_code", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("psw", password);
        editor.commit();
    }

    /**
     *@desc 获取补货员登录密码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public String getReplenishPassword() {
        SharedPreferences sp = m_context.getSharedPreferences("psw_code", Context.MODE_PRIVATE);
        String posPayMachtId = null;
        if (null != sp) {
            posPayMachtId = sp.getString("RepPsw", "000000");
        }

        return posPayMachtId;
    }

    /**
     *@desc 设置补货员登录密码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:24
     */
    public void setReplenishPassword(String password) {
        SharedPreferences sp = m_context.getSharedPreferences("psw_code", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("RepPsw", password);
        editor.commit();
    }

    /**
     *@desc 获取登录密码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public String getQuickEntrPassword() {
        SharedPreferences sp = m_context.getSharedPreferences("psw_code", Context.MODE_PRIVATE);
        String posPayMachtId = null;
        if (null != sp) {
            posPayMachtId = sp.getString("QuickEntrPassword", "73194653");
        }

        return posPayMachtId;
    }

    /**
     *@desc 设置登录密码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:24
     */
    public void setQuickEntrPassword(String password) {
        SharedPreferences sp = m_context.getSharedPreferences("psw_code", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("QuickEntrPassword", password);
        editor.commit();
    }


    /**
     *@desc 设置是否支持现金支付
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setCashPayOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("isopencash", open);
        }
        editor.commit();
    }

    /**
     *@desc 是否支持现金支付
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public boolean isCashPayOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        boolean bAliPayOpen = false;
        if (null != sp) {
            bAliPayOpen = sp.getBoolean("isopencash", false);
        }

        return bAliPayOpen;
    }

    /**
     *@desc 设置是否支持支付宝机支付
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setAliPayOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("isopenali", open);
        }
        editor.commit();
    }

    /**
     *@desc 是否支持支付宝支付
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public boolean isAliPayOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        boolean bAliPayOpen = false;
        if (null != sp) {
            bAliPayOpen = sp.getBoolean("isopenali", false);
        }

        return bAliPayOpen;
    }

    /**
     *@desc 合作身份者id
     *@author Jiancheng,Song
     *@time 2016/5/27 22:35
     */
    public void setAliPayPartner(String strPartner) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("alipartner", strPartner);
        editor.commit();
    }

    /**
     *@desc 合作身份者id
     *@author Jiancheng,Song
     *@time 2016/5/27 22:35
     */
    public String getAliPayPartner() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        String bAliPayPartner = null;
        if (null != sp) {
            bAliPayPartner = sp.getString("alipartner", null);
        }

        return bAliPayPartner;
    }

    /**
     *@desc 支付宝账号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:32
     */
    public void setAliPayEmail(String strEmail) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("aliselleremail", strEmail);
        editor.commit();
    }

    /**
     *@desc 支付宝账号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:32
     */
    public String getAliPayEmail() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        String strAliPayEmail = null;
        if (null != sp) {
            strAliPayEmail = sp.getString("aliselleremail", null);
        }

        return strAliPayEmail;
    }

    /**
     *@desc 支付宝分账账户
     *@author Jiancheng,Song
     *@time 2016/5/27 22:34
     */
    public void setAliPayTransInPartner(String strTransInPartner) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("transInPartner", strTransInPartner);
        editor.commit();
    }

    /**
     *@desc 支付宝分账账户
     *@author Jiancheng,Song
     *@time 2016/5/27 22:34
     */
    public String getAliPayTransInPartner() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        String strTransInPartner = null;
        if (null != sp) {
            strTransInPartner = sp.getString("transInPartner", "");
        }

        return strTransInPartner;
    }

    /**
     *@desc 交易安全校验码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:34
     */
    public void setAliPayKey(String key) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("alikey", key);
        editor.commit();
    }

    /**
     *@desc 交易安全校验码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:34
     */
    public String getAliPayKey() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        String bAliPayKey = null;
        if (null != sp) {
            bAliPayKey = sp.getString("alikey", null);
        }

        return bAliPayKey;
    }

    /**
     *@desc 设置是否支持微信机支付
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setWeixinOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("isopenweixin", open);
        }
        editor.commit();
    }

    /**
     *@desc 是否支持微信支付
     *@author Jiancheng,Song
     *@time 2016/5/27 22:31
     */
    public boolean isWeixinOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        boolean bWeixinOpen = false;
        if (null != sp) {
            bWeixinOpen = sp.getBoolean("isopenweixin", false);
        }

        return bWeixinOpen;
    }

    /**
     *@desc 合作身份者id
     *@author Jiancheng,Song
     *@time 2016/5/27 22:31
     */
    public void setWeixinPartner(String strPartner) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("wxpartner", strPartner);
        editor.commit();
    }

    /**
     *@desc 合作身份者id
     *@author Jiancheng,Song
     *@time 2016/5/27 22:31
     */
    public String getWeixinPartner() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        String strWxPartner = null;
        if (null != sp) {
            strWxPartner = sp.getString("wxpartner", null);
        }

        return strWxPartner;
    }

    /**
     *@desc 微信公众号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:30
     */
    public void setWeixinAppid(String strAppid) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("wxappid", strAppid);
        editor.commit();
    }

    /**
     *@desc 微信公众号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:30
     */
    public String getWeixinAppid() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        String strWxAppid = null;
        if (null != sp) {
            strWxAppid = sp.getString("wxappid", null);
        }

        return strWxAppid;
    }

    /**
     *@desc 微信交易安全校验码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:27
     */
    public void setWeixinKey(String key) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("wxkey", key);
        editor.commit();
    }

    /**
     *@desc 微信交易安全校验码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:27
     */
    public String getWeixinKey() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        String strWxKey = null;
        if (null != sp) {
            strWxKey = sp.getString("wxkey", null);
        }

        return strWxKey;
    }

    /**
     *@desc 设置是否支持京东支付
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setJidongOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("JidongOpen", open);
        }
        editor.commit();
    }

    /**
     *@desc 是否支持京东支付
     *@author Jiancheng,Song
     *@time 2016/5/27 22:31
     */
    public boolean isJidongOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        boolean bWeixinOpen = false;
        if (null != sp) {
            bWeixinOpen = sp.getBoolean("JidongOpen", false);
        }

        return bWeixinOpen;
    }

    /**
     *@desc 设置是否支被动扫码支付
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setPassiveScanCodePay(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("PsvScCdPay", open);
        }
        editor.commit();
    }

    /**
     *@desc 是否支持被动扫码支付
     *@author Jiancheng,Song
     *@time 2016/5/27 22:31
     */
    public boolean isPassiveScanCodePayOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("pay_system", Context.MODE_PRIVATE);
        boolean bPassiveScanCodePayOpen = false;
        if (null != sp) {
            bPassiveScanCodePayOpen = sp.getBoolean("PsvScCdPay", false);
        }

        return bPassiveScanCodePayOpen;
    }

    /**
     *@desc 获取服务器地址
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public String getIPAddress() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String id = sp.getString("ip", "");
        return id;
    }

    /**
     *@desc 设置服务器地址
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public void setIPAddress(String ip) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ip", ip);
        editor.commit();
    }

    /**
     *@desc 获取socket端口号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public int getSocketPort() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int id = sp.getInt("socket", TcnConstant.SCOKET);
        return id;
    }

    /**
     *@desc 设置socket端口号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setSocketPort(int socket) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("socket", socket);
        editor.commit();
    }

    /**
     *@desc 获取价格小数点位数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public int getPricePointCount() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int id = sp.getInt("PricePointCount", 1);
        return id;
    }

    /**
     *@desc 设置价格小数点位数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setPricePointCount(int count) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("PricePointCount", count);
        editor.commit();
    }

    /**
     *@desc 获取货道号位数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public int getSlotNoDigitCount() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int id = sp.getInt("SlotNoDigitCount", 1);
        return id;
    }

    /**
     *@desc 设置货道号位数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setSlotNoDigitCount(int count) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("SlotNoDigitCount", count);
        editor.commit();
    }

    /**
     *@desc 设置软件版本号
     *@author Jiancheng,Song
     *@time 2016/6/3 21:03
     */
    public void setSoftVersion(String ver) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Version", ver);
        editor.commit();
    }

    /**
     *@desc 获取机器id号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public String getMachineID() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String id = sp.getString("id", "");
        return id;
    }

    /**
     *@desc 设置机器id号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setMachineID(String id) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("id", id);
        editor.commit();
    }

    /**
     *@desc 获取下载机器ID的次数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public int getDownLoadMachineIDCount() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int id = sp.getInt("DLdMIDCount", 0);
        return id;
    }

    /**
     *@desc 设置下载机器ID的次数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setDownLoadMachineIDCount(int count) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("DLdMIDCount", count);
        editor.commit();
    }

    /**
     * 设置机器类型
     * @return
     */
    public void setMachineType(String type) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Type", type);
        editor.commit();
    }

    /**
     * 获取机器类型
     * @return
     */
    public String getMachineType() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String type = sp.getString("Type", "11");
        return type;
    }

    /**
     * 饮料机数量
     * @return
     */
    public String getDrinkMachineNum() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String type = sp.getString("DrinkMachineNum", "0");
        return type;
    }

    /**
     * 设置饮料机数量
     * @return
     */
    public void setDrinkMachineNum(String num) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("DrinkMachineNum", num);
        editor.commit();
    }

    /**
     *@desc 设置是否打开广告播放时间限制开关
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setPlayTimeLimitOpen(boolean limit) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("play_limit", limit);
        }
        editor.commit();
    }

    /**
     *@desc 是否打开广告播放时间限制开关
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public boolean isPlayTimeLimitOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bLimit = false;
        if (null != sp) {
            bLimit = sp.getBoolean("play_limit", false);
        }

        return bLimit;
    }

    /**
     *@desc 设置订单号是否包含机器号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setTradeNoWithoutMachId(boolean cotain) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("TradeNoWithoutMachId", cotain);
        }
        editor.commit();
    }

    /**
     *@desc 订单号是否包含机器号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public boolean isTradeNoWithoutMachId() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bLimit = false;
        if (null != sp) {
            bLimit = sp.getBoolean("TradeNoWithoutMachId", false);
        }

        return bLimit;
    }

    /**
     *@desc 设置后台是否定制
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setBackgroundCustomized(boolean customized) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("BackgroundCustomized", customized);
        }
        editor.commit();
    }

    /**
     *@desc 后台是否定制
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public boolean isBackgroundCustomized() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bCustomized = false;
        if (null != sp) {
            bCustomized = sp.getBoolean("BackgroundCustomized", false);
        }

        return bCustomized;
    }

    /**
     *@desc 设置U盘配置 插上U盘是否自动重启
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setUSBConfigCannotReboot(boolean canreboot) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("USBConfigCannotReboot", canreboot);
        }
        editor.commit();
    }

    /**
     *@descU盘配置  是否盘是否自动重启
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public boolean isUSBConfigCannotReboot() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bLimit = false;
        if (null != sp) {
            bLimit = sp.getBoolean("USBConfigCannotReboot", false);
        }

        return bLimit;
    }

    /**
     *@desc 设置广告是否在下半屏播放
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setAdvertOnScreenBottom(boolean bottom) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("AdvertOnScreenBottom", bottom);
        }
        editor.commit();
    }

    /**
     *@desc 广告是否在下半屏播放
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public boolean isAdvertOnScreenBottom() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bOnScreenBottom = false;
        if (null != sp) {
            bOnScreenBottom = sp.getBoolean("AdvertOnScreenBottom", false);
        }

        return bOnScreenBottom;
    }

    /**
     *@desc 设置待机图片是否全屏
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setStandbyImageFullScreen(boolean fullScreen) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("StandbyImageFullScreen", fullScreen);
        }
        editor.commit();
    }

    /**
     *@desc待机图片是否全屏
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public boolean isStandbyImageFullScreen() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean fullScreen = false;
        if (null != sp) {
            fullScreen = sp.getBoolean("StandbyImageFullScreen", true);
        }

        return fullScreen;
    }

    /**
     *@desc 设置待机图片时间
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setStandbyImageTime(int time) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("StandbyImageTime", time);
        editor.commit();
    }

    /**
     *@desc 获取待机图片时间
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public int getStandbyImageTime() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int time = sp.getInt("StandbyImageTime", 60);
        return time;
    }

    /**
     *@desc 设置图片播放时间
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setImagePlayIntervalTime(int time) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("ImagePlayIntervalTime", time);
        editor.commit();
    }

    /**
     *@desc 获取播放图片间隔时间
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public int getImagePlayIntervalTime() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int time = sp.getInt("ImagePlayIntervalTime", 10);
        return time;
    }

    /**
     *@desc 设置二维码是否显示log
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setQRCodeShowLogo(boolean showLogo) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("QRCodeShowLogo", showLogo);
        editor.commit();
    }

    /**
     *@desc 获取二维码是否显示log
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public boolean isQRCodeShowLogo() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean showLogo = sp.getBoolean("QRCodeShowLogo", true);
        return showLogo;
    }

    /**
     *@desc 设置显示购物
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setShowShopping(boolean showShopping) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("showShopping", showShopping);
        editor.commit();
    }

    /**
     *@desc 获取显示购物
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public boolean isShowShopping() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean showShopping = sp.getBoolean("showShopping", true);
        return showShopping;
    }

    /**
     *@desc 设置显示购物
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setAppForegroundCheck(boolean check) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("AppForegroundCheck", check);
        editor.commit();
    }

    /**
     *@desc 获取显示购物
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public boolean isAppForegroundCheck() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean showShopping = sp.getBoolean("AppForegroundCheck", true);
        return showShopping;
    }

    /**
     *@desc 设置验证码取货
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setAppVerify(boolean verify) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("AppVerify", verify);
        editor.commit();
    }

    /**
     *@desc 获取其它串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public String getPassiveScanPayComPath() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String pathCom = sp.getString("PasivScanPayComPath", "");
        return pathCom;
    }

    /**
     *@desc 获取其它串口波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public String getPassiveScanPayComBaudrate() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String strBaudrate = sp.getString("PasivScanPayBaudrate", "");
        return strBaudrate;
    }

    /**
     *@desc 获取其它串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public String getOtherComPath() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String pathCom = sp.getString("OTHER", "");
        return pathCom;
    }

    /**
     *@desc 获取其它串口波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public String getOtherComBaudrate() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String strBaudrate = sp.getString("OTHERBAUDRATE", "9600");
        return strBaudrate;
    }

    public void setAppFirstCreat(Context context, boolean bFirst){
        SharedPreferences sp = context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("bAppFirstCreat", bFirst);
        editor.commit();
    }

    /**
     *@desc
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public boolean isAppFirstCreat(){
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean isAppFirstCreat = sp.getBoolean("bAppFirstCreat", false);
        return isAppFirstCreat;
    }

    /**
     *@desc 是否语音提示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:21
     */
    public boolean isVoicePrompt() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bVoice = sp.getBoolean("voice", true);
        return bVoice;
    }

    /**
     *@desc 语音提示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:21
     */
    public void setVoicePrompt(boolean voice) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("voice", voice);
        editor.commit();
    }

    /**
     *@desc 是否显示log
     *@author Jiancheng,Song
     *@time 2016/5/27 22:21
     */
    public boolean isShowlogo() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bShowlogo = sp.getBoolean("showlogo", false);
        return bShowlogo;
    }

    /**
     *@desc 设置是否显示log
     *@author Jiancheng,Song
     *@time 2016/5/27 22:21
     */
    public void setShowlogo(boolean showlogo) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("showlogo", showlogo);
        editor.commit();
    }

    /**
     *@desc 是否显示屏保
     *@author Jiancheng,Song
     *@time 2016/5/27 22:21
     */
    public boolean isShowScreenProtect() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bShowlogo = sp.getBoolean("ScreenProtect", true);
        return bShowlogo;
    }

    /**
     *@desc 设置是否显示屏保
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setShowScreenProtect(boolean showlogo) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("ScreenProtect", showlogo);
        editor.commit();
    }

    /**
     *@desc 是否全屏
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public boolean isFullScreen() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bShowlogo = sp.getBoolean("FullScreen", false);
        return bShowlogo;
    }

    /**
     *@desc 设置否全屏
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setFullScreen(boolean fullScreen) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("FullScreen", fullScreen);
        editor.commit();
    }

    /**
     *@desc 是否显示页面
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public boolean isPageDisplay() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bDisplay = sp.getBoolean("PageDisplay", false);
        return bDisplay;
    }

    /**
     *@desc 设置显示页面
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setPageDisplay(boolean display) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("PageDisplay", display);
        editor.commit();
    }

    /**
     *@desc 设置每页显示的个数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setItemCountEveryPage(int time) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("ItemCountEveryPage", time);
        editor.commit();
    }

    /**
     *@desc 获取每页显示的个数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public int getItemCountEveryPage() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int time = sp.getInt("ItemCountEveryPage", 12);
        return time;
    }

    /**
     *@desc 是否已经创建MainActivity
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public boolean isMainActivityCreated() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bCreated = sp.getBoolean("MainActivityCreated", false);
        return bCreated;
    }

    /**
     *@desc 设置创建MainActivity
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setMainActivityCreated(boolean created) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("MainActivityCreated", created);
        editor.commit();
    }

    /**
     *@desc 是否先显示键盘
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public boolean isFirstShowKeyboard() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bCreated = sp.getBoolean("FirstShowKeyboard", false);
        return bCreated;
    }

    /**
     *@desc 设置先显示键盘
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setFirstShowKeyboard(boolean show) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("FirstShowKeyboard", show);
        editor.commit();
    }

    /**
     *@desc 获取屏幕尺寸
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getScreenInch() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String strScreenInch = sp.getString("ScreenInch", TcnCommon.SCREEN_INCH[0]);
        return strScreenInch;
    }

    /**
     *@desc 设置屏幕尺寸
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setScreenInch(String intch) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ScreenInch", intch);
        editor.commit();
    }

    /**
     *@desc 获取屏幕方向
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public String getScreenOrientation() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String orientation = sp.getString("ScreenOrientation", TcnCommon.SCREEN_ORIENTATION[0]);
        return orientation;
    }

    /**
     *@desc 获取屏幕方向
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setScreenOrientation(String orientation) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ScreenOrientation", orientation);
        editor.commit();
    }

    /**
     *@desc 获取支付提示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getPayTips() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String strScreenInch = sp.getString("PayTips", m_context.getString(R.string.pay_tips_default));
        return strScreenInch;
    }

    /**
     *@desc 设置支付提示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setPayTips(String tips) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("PayTips", tips);
        editor.commit();
    }


    /**
     *@desc 获取支付提示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getPayFirstQrcodeTips() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String strScreenInch = sp.getString("FstTips", m_context.getString(R.string.ui_pay_scan_wechat_qrcode));
        return strScreenInch;
    }

    /**
     *@desc 设置支付提示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setPayFirstQrcodeTips(String tips) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("FstTips", tips);
        editor.commit();
    }

    /**
     *@desc 获取支付提示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getPaySecondQrcodeTips() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String strScreenInch = sp.getString("ScdTips", m_context.getString(R.string.ui_pay_scan_alipay_qrcode));
        return strScreenInch;
    }

    /**
     *@desc 设置支付提示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setPaySecondQrcodeTips(String tips) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ScdTips", tips);
        editor.commit();
    }


    /**
     *@desc 获取键盘选项文字
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getKeyBoardText() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String strText = sp.getString("KeyBoardText", m_context.getString(R.string.ui_keyboard));
        return strText;
    }

    /**
     *@desc 设置键盘选项文字
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setKeyBoardText(String text) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("KeyBoardText", text);
        editor.commit();
    }

    /**
     *@desc 获取键盘选项文字
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getKeyBoardInputTips() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String strText = sp.getString("KeyBoardTips", m_context.getString(R.string.ui_input_slotno));
        return strText;
    }

    /**
     *@desc 设置键盘选项文字
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setKeyBoardInputTips(String text) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("KeyBoardTips", text);
        editor.commit();
    }

    /**
     *@desc 是否显示分类
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public boolean isShowType() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bCreated = sp.getBoolean("ShowType", false);
        return bCreated;
    }

    /**
     *@desc 设置显示分类
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setShowType(boolean show) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("ShowType", show);
        editor.commit();
    }

    /**
     *@desc 获取ftp地址
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getFtpAddress() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String strText = sp.getString("FtpAddress", "");
        return strText;
    }

    /**
     *@desc 设置地址
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setFtpAddress(String address) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("FtpAddress", address);
        editor.commit();
    }

    /**
     *@desc 获取ftp用户名
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getFtpUser() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String strText = sp.getString("FtpUser", "");
        return strText;
    }

    /**
     *@desc 设置ftp用户名
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setFtpUser(String user) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("FtpUser", user);
        editor.commit();
    }

    /**
     *@desc 获取ftp密码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getFtpPassword() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String strText = sp.getString("FtpPassword", "");
        return strText;
    }

    /**
     *@desc 设置ftp密码
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public void setFtpPassword(String password) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("FtpPassword", password);
        editor.commit();
    }

    /**
     *@desc 获取主板发出的数据类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getTcnDataType() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String type = sp.getString("DataType", TcnConstant.DATA_TYPE[1]);
        return type;
    }

    /**
     *@desc 设置主板对应的数据类型  1：8C  2：common
     *@author Jiancheng,Song
     *@time 2016/5/27 22:19
     */
    public void setTcnDataType(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("DataType", type);
        editor.commit();
    }

    /**
     *@desc 获取主板类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getBoardType() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String type = sp.getString("BoardType", TcnConstant.DEVICE_CONTROL_TYPE[5]);
        return type;
    }

    /**
     *@desc 设置主板类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:19
     */
    public void setBoardType(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("BoardType", type);
        editor.commit();
    }

    /**
     *@desc 获取主板类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getBoardTypeSecond() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String type = sp.getString("BoardTypeSecond", TcnConstant.DEVICE_CONTROL_TYPE[0]);
        return type;
    }

    /**
     *@desc 设置主板类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:19
     */
    public void setBoardTypeSecond(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("BoardTypeSecond", type);
        editor.commit();
    }

    /**
     *@desc 获取主板类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getBoardTypeThird() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String type = sp.getString("BoardTypeThird", TcnConstant.DEVICE_CONTROL_TYPE[0]);
        return type;
    }

    /**
     *@desc 设置主板类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:19
     */
    public void setBoardTypeThird(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("BoardTypeThird", type);
        editor.commit();
    }

    /**
     *@desc 获取主板类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getBoardTypeFourth() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String type = sp.getString("BoardTypeFourth", TcnConstant.DEVICE_CONTROL_TYPE[0]);
        return type;
    }

    /**
     *@desc 设置主板类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:19
     */
    public void setBoardTypeFourth(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("BoardTypeFourth", type);
        editor.commit();
    }

    /**
     *@desc 客户类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:20
     */
    public String getTcnCustomerType() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String type = sp.getString("TcnCustomer", TcnCustomer.CUSTOMER_DEFAULT);
        return type;
    }

    /**
     *@desc 客户类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:19
     */
    public void setTcnCustomerType(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("TcnCustomer", type);
        editor.commit();
    }

    /**
     *@desc 是否用客户自己的IP
     *@author Jiancheng,Song
     *@time 2016/5/27 22:19
     */
    public String getUseCustomerIP() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String bTmp = sp.getString("UseCustomerIP", TcnConstant.USE_TCN_OR_CUSTOMER_IP[0]);
        return bTmp;
    }

    /**
     *@desc 设置是否用客户的IP
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public void setUseCustomerIP(String useCustomerIP) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UseCustomerIP", useCustomerIP);
        editor.commit();
    }

    /**
     *@desc 获取支付系统类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getPaySystemType() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String type = sp.getString("PaySystemType", TcnConstant.PAY_SYSTEM_TYPE[1]);
        return type;
    }

    /**
     *@desc 设置支付系统类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setPaySystemType(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("PaySystemType", type);
        editor.commit();
    }

    /**
     *@desc 获取二维码显示方式
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getQrCodeShowType() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String type = sp.getString("QrCodeShowType", TcnConstant.QRCODE_SHOW_TYPE[0]);
        return type;
    }

    /**
     *@desc 设置二维码显示方式
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setQrCodeShowType(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("QrCodeShowType", type);
        editor.commit();
    }

    /**
     *@desc 获取机器按键或货道显示类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getKeyAndSlotDisplayType() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String type = sp.getString("KeySlotDisplayType", TcnConstant.KEY_SLOT_DISPLAY_TYPE[0]);
        return type;
    }

    /**
     *@desc 设置按键或货道显示类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setKeyAndSlotDisplayType(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("KeySlotDisplayType", type);
        editor.commit();
    }

    /**
     *@desc 获取编码类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getCodingType() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String type = sp.getString("CodingType", "GB2312");
        return type;
    }

    /**
     *@desc 设置编码类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setCodingType(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("CodingType", type);
        editor.commit();
    }

    /**
     *@desc 设置使用远程发布广告系统类型
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setRemoteAdvertSystemType(String type) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("RemoteAdSysType", type);
        editor.commit();
    }

    /**
     *@desc 获取组号和串口对应
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getSerPortGroupMapFirst() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("SerPtGrpMapFirst", TcnConstant.SRIPORT_GRP_MAP[0]);
        return remoteAdSysType;
    }

    /**
     *@desc 设置组号和串口对应
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setSerPortGroupMapFirst(String mapData) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("SerPtGrpMapFirst", mapData);
        editor.commit();
    }

    /**
     *@desc 获取组号和串口对应
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getSerPortGroupMapSecond() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("SerPtGrpMapSecond", TcnConstant.SRIPORT_GRP_MAP[0]);
        return remoteAdSysType;
    }

    /**
     *@desc 设置组号和串口对应
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setSerPortGroupMapSecond(String mapData) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("SerPtGrpMapSecond", mapData);
        editor.commit();
    }

    /**
     *@desc 获取组号和串口对应
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getSerPortGroupMapThird() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("SerPtGrpMapThird", TcnConstant.SRIPORT_GRP_MAP[0]);
        return remoteAdSysType;
    }

    /**
     *@desc 设置组号和串口对应
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setSerPortGroupMapThird(String mapData) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("SerPtGrpMapThird", mapData);
        editor.commit();
    }

    /**
     *@desc 获取组号和串口对应
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getSerPortGroupMapFourth() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("SerPtGrpMapFourth", TcnConstant.SRIPORT_GRP_MAP[0]);
        return remoteAdSysType;
    }

    /**
     *@desc 设置组号和串口对应
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setSerPortGroupMapFourth(String mapData) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("SerPtGrpMapFourth", mapData);
        editor.commit();
    }


    /**
     *@desc 获取串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getBoardSerPortFirst() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("MAINDEVICE", "");
        return remoteAdSysType;
    }

    /**
     *@desc 设置串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setBoardSerPortFirst(String device) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("MAINDEVICE", device);
        editor.commit();
    }

    /**
     *@desc 获取串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getBoardSerPortSecond() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("SERVERDEVICE", "");
        return remoteAdSysType;
    }

    /**
     *@desc 设置串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setBoardSerPortSecond(String device) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("SERVERDEVICE", device);
        editor.commit();
    }

    /**
     *@desc 获取串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getBoardSerPortThird() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("DEVICETHIRD", "");
        return remoteAdSysType;
    }

    /**
     *@desc 设置串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setBoardSerPortThird(String device) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("DEVICETHIRD", device);
        editor.commit();
    }

    /**
     *@desc 获取串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getBoardSerPortFourth() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("DEVICEFOURTH", "");
        return remoteAdSysType;
    }

    /**
     *@desc 设置串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setBoardSerPortFourth(String device) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("DEVICEFOURTH", device);
        editor.commit();
    }

    /**
     *@desc 获取串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getBoardSerPortKey() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("DEVICEKey", "");
        return remoteAdSysType;
    }

    /**
     *@desc 设置串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setBoardSerPortKey(String device) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("DEVICEKey", device);
        editor.commit();
    }

    /**
     *@desc 获取串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getBoardSerPortMDB() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String deviceMdb = sp.getString("DEVICEMDB", "");
        return deviceMdb;
    }

    /**
     *@desc 设置串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setBoardSerPortMDB(String device) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("DEVICEMDB", device);
        editor.commit();
    }

    /**
     *@desc 获取数码显示串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getBoardSerPortDgtDsp() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String dgtDsp = sp.getString("DEVICEDGTDSP", "");
        return dgtDsp;
    }

    /**
     *@desc 设置数码显示串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setBoardSerPortDgtDsp(String device) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("DEVICEDGTDSP", device);
        editor.commit();
    }

    /**
     *@desc 获取温度串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getSerPortTemp() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String temp = sp.getString("DEVICETEMP", "");
        return temp;
    }

    /**
     *@desc 设置温度串口
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setSerPortTemp(String device) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("DEVICETEMP", device);
        editor.commit();
    }


    /**
     *@desc 获取主板波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getBoardBaudRate() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("MAINBAUDRATE", "-1");
        return remoteAdSysType;
    }

    /**
     *@desc 设置主板波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setBoardBaudRate(String baudRate) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("MAINBAUDRATE", baudRate);
        editor.commit();
    }

    /**
     *@desc 获取按键波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getKeyBaudRate() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("KeyBAUDRATE", "9600");
        return remoteAdSysType;
    }

    /**
     *@desc 设置按键波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setKeyBaudRate(String baudRate) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("KeyBAUDRATE", baudRate);
        editor.commit();
    }

    /**
     *@desc 获取主板波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getMDBBaudRate() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String remoteAdSysType = sp.getString("MDBBAUDRATE", "9600");
        return remoteAdSysType;
    }

    /**
     *@desc 设置主板波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setMDBBaudRate(String baudRate) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("MDBBAUDRATE", baudRate);
        editor.commit();
    }

    /**
     *@desc 获取数码显示波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getDgtDspBaudRate() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String dgtDsp = sp.getString("DgtDspBAUDRATE", "9600");
        return dgtDsp;
    }

    /**
     *@desc 设置数码显示波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setDgtDspBaudRate(String baudRate) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("DgtDspBAUDRATE", baudRate);
        editor.commit();
    }


    /**
     *@desc 获取温度串口波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getTempBaudRate() {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        String temp = sp.getString("TEMPBAUDRATE", "9600");
        return temp;
    }

    /**
     *@desc 设置温度串口波特率
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setTempBaudRate(String baudRate) {
        SharedPreferences sp = m_context.getSharedPreferences(m_context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("TEMPBAUDRATE", baudRate);
        editor.commit();
    }


    /**
     *@desc 获取最小货道号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public int getMinSlotNo() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int minSlotNo = sp.getInt("MinSlotNo", 1);
        return minSlotNo;
    }

    /**
     *@desc 获取最小货道号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setMinSlotNo(int minSlotNo) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("MinSlotNo", minSlotNo);
        editor.commit();
    }

    /**
     *@desc 获取最大货道号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public int getMaxSlotNo() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int minSlotNo = sp.getInt("MaxSlotNo", 999);
        return minSlotNo;
    }

    /**
     *@desc 获取最大货道号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setMaxSlotNo(int maxSlotNo) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("MaxSlotNo", maxSlotNo);
        editor.commit();
    }

    /**
     *@desc 获取客户自己的机器id号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getCustomMachineID() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String id = sp.getString("CustomId", "");
        return id;
    }

    /**
     *@desc 设置客户自己的机器id号
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setCustomMachineID(String customId) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("CustomId", customId);
        editor.commit();
    }

    /**
     *@desc 是否不使用后台
     *@author Jiancheng,Song
     *@time 2016/5/27 22:19
     */
    public boolean isCustomerIPUsed() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bTmp = sp.getBoolean("CustomerIPUsed", false);
        return bTmp;
    }

    /**
     *@desc 设置是否不使用后台
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public void setCustomerIPUsed(boolean used) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("CustomerIPUsed", used);
        editor.commit();
    }

    /**
     *@desc 是否按照商品编码显示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public boolean isShowByGoodsCode() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean byCode = sp.getBoolean("ShowByGoodsCode", false);
        return byCode;
    }

    /**
     *@desc 设置是否按照商品编码显示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setShowByGoodsCode(boolean byCode) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("ShowByGoodsCode", byCode);
        editor.commit();
    }

    /**
     *@desc 出货顺序
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getGoodsCodeShipType() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String codeBuy = sp.getString("GoodsCodeShipType", TcnCommon.GOODSCODE_SHIPTYPE[0]);
        return codeBuy;
    }

    /**
     *@desc 出货顺序
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setGoodsCodeShipType(String shipType) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("GoodsCodeShipType", shipType);
        editor.commit();
    }

    /**
     *@desc 获取价格单位
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getUnitPrice() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String unit = sp.getString("UnitPrice", TcnCommon.PRICE_UNIT[0]);
        return unit;
    }

    /**
     *@desc 设置价格单位
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setUnitPrice(String unit) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UnitPrice", unit);
        editor.commit();
    }

    /**
     *@desc 获取Aes key
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getAesKey() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String unit = sp.getString("AesKey", "");
        return unit;
    }

    /**
     *@desc 设置Aes key
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setAesKey(String key) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("AesKey", key);
        editor.commit();
    }


    /**
     *@desc 获取欢迎语
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getWeclcome() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String unit = sp.getString("Weclcome", m_context.getString(R.string.tcn_welcome));
        return unit;
    }

    /**
     *@desc 设置欢迎语
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setWeclcome(String weclcome) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Weclcome", weclcome);
        editor.commit();
    }

    /**
     *@desc 获取支付有效时间(s)
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public int getPayTime() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int paytime = sp.getInt("PayTime", 90);
        return paytime;
    }

    /**
     *@desc 设置支付有效时间(s)
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setPayTime(int time) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("PayTime", time);
        editor.commit();
    }

    /**
     *@desc 获取定时重启
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public int getRebootTime() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int paytime = sp.getInt("RebootTime", -1);
        return paytime;
    }

    /**
     *@desc 设置定时重启
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setRebootTime(int time) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("RebootTime", time);
        editor.commit();
    }

    /**
     *@desc 获取远程更新广告地址
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getAdvertIP() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String url = sp.getString("AdvertUrl", "114.55.227.36");
        return url;
    }

    /**
     *@desc 设置远程更新广告地址
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setAdvertIP(String ip) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("AdvertUrl", ip);
        editor.commit();
    }

    /**
     *@desc 获取文字广告
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public String getAdvertText() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String url = sp.getString("AdvertText", m_context.getString(R.string.tcn_log));
        return url;
    }

    /**
     *@desc 设置文字广告
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setAdvertText(String textAd) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("AdvertText", textAd);
        editor.commit();
    }

    /**
     *@desc 获取远程更新广告轮询间隔时间
     *@author Jiancheng,Song
     *@time 2016/5/27 22:18
     */
    public int getAdvertPollTime() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int pollTime = sp.getInt("PollTime", 6*3600000);
        return pollTime;
    }

    /**
     *@desc 设置远程更新广告轮询间隔时间
     *@author Jiancheng,Song
     *@time 2016/5/27 22:17
     */
    public void setAdvertPollTime(int pollTime) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("PollTime", pollTime);
        editor.commit();
    }

    /**
     *@desc 获取二维码显示方式 是否是单个码显示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public boolean isShowSingleQRCode() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean twoQRCodeDisplay = sp.getBoolean("SingleQRCodeDisplay", false);
        return twoQRCodeDisplay;
    }

    /**
     *@desc 设置二维码显示方式 是否是单个码显示
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public void setShowSingleQRCode(boolean isSingleQRCodeDisplay) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("SingleQRCodeDisplay", isSingleQRCodeDisplay);
        editor.commit();
    }

    /**
     *@desc 获取多码合一的流水号 0 - 60000  自增长
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public int getMultQRcodeWater() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int id = sp.getInt("MultQRcodeWater", 65535);
        return id;
    }

    /**
     *@desc 设置多码合一的流水号 0 - 60000  自增长
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setMultQRcodeWater(int water) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("MultQRcodeWater", water);
        editor.commit();
    }

    /**
     *@desc 获取流水号 0 - 255  自增长
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public int getTradeNoWater() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int id = sp.getInt("TradeNoWater", 0);
        return id;
    }

    /**
     *@desc 设置流水号 0 - 255  自增长
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setTradeNoWater(int water) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("TradeNoWater", water);
        editor.commit();
    }

    /**
     *@desc 获取流水号 0 - 255  自增长
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public int getMessageNoWater() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int id = sp.getInt("MessageNoWater", 0);
        return id;
    }

    /**
     *@desc 设置流水号 0 - 255  自增长
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setMessageNoWater(int water) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("MessageNoWater", water);
        editor.commit();
    }


    /**
     *@desc 获取快速设置向导是否打开
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public boolean isQuickSetupGuideOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bGuideOpen = sp.getBoolean("QuickSetupGuide", true);
        return bGuideOpen;
    }

    /**
     *@desc 设置快速设置向导是否打开
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public void setQuickSetupGuideOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("QuickSetupGuide", open);
        editor.commit();
    }

    /**
     *@desc 第三方开发IP地址
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public String getOtherAddress() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        String id = sp.getString("OtherAddress", "");
        return id;
    }

    /**
     *@desc 设置第三方开发IP地址
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public void setOtherAddress(String address) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("OtherAddress", address);
        editor.commit();
    }

    /**
     *@desc 获取第三方开关
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public boolean isSwitchOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bSwitchOpen = sp.getBoolean("SwitchOpen", false);
        return bSwitchOpen;
    }

    /**
     *@desc 设置第三方开关
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public void setSwitchOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("SwitchOpen", open);
        editor.commit();
    }

    /**
     *@desc 获取第三方开关
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public boolean isShowTapScreenText() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        boolean bShow = sp.getBoolean("ShowTapScreenText", true);
        return bShow;
    }

    /**
     *@desc 设置第三方开关
     *@author Jiancheng,Song
     *@time 2016/5/27 22:23
     */
    public void setShowTapScreenText(boolean show) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("ShowTapScreenText", show);
        editor.commit();
    }



    /**
     *@desc 设置程序更新地址
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setApkUrl(String url) {
        SharedPreferences sp = m_context.getSharedPreferences("apk", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putString("ApkUrl", url);
        }
        editor.commit();
    }

    /**
     *@desc 获取程序更新地址
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public String getApkUrl() {
        SharedPreferences sp = m_context.getSharedPreferences("apk", Context.MODE_PRIVATE);
        String url = "";
        if (null != sp) {
            url = sp.getString("ApkUrl", TcnConstant.APK_UPDATE_URL);
        }

        return url;
    }

    /**
     *@desc 设置程序名称
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setApkName(String name) {
        SharedPreferences sp = m_context.getSharedPreferences("apk", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putString("ApkName", name);
        }
        editor.commit();
    }

    /**
     *@desc 获取程序名称
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public String getApkName() {
        SharedPreferences sp = m_context.getSharedPreferences("apk", Context.MODE_PRIVATE);
        String name = "";
        if (null != sp) {
            name = sp.getString("ApkName", TcnConstant.APK_UPDATE_NAME);
        }

        return name;
    }


    /**
     *@desc 获取掉货检测开关
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public boolean isDropSensorCheck() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_set_config", Context.MODE_PRIVATE);
        boolean bCheck = sp.getBoolean("DropSensor", true);
        return bCheck;
    }


    /**
     *@desc 获取掉货检测开关
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setDropSensorCheck(boolean bDropSensor) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_set_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("DropSensor", bDropSensor);
            editor.commit();
        }
    }

    /**
     *@desc 设置打开之后，多少s关闭
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setCloseDelayTime(String seconds) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_set_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putString("CDT", seconds);
        }
        editor.commit();
    }

    /**
     *@desc 获取打开之后，多少s关闭
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public String getCloseDelayTime() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_set_config", Context.MODE_PRIVATE);
        String cdt = "";
        if (null != sp) {
            cdt = sp.getString("CDT", TcnCommon.TIME_DELAY_CLOSE_SELECT[3]);
        }
        return cdt;
    }

    /**
     *@desc 联系多少次出货失败锁机
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setShipFailCountLock(int count) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("ShipFailCountLock", count);
        editor.commit();
    }

    /**
     *@desc 联系多少次出货失败锁机
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public int getShipFailCountLock() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int count = sp.getInt("ShipFailCountLock", 9);
        return count;
    }

    /**
     *@desc出货失败次数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public void setShipContinFailCount(int count) {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("ShipContinFailCount", count);
        editor.commit();
    }

    /**
     *@desc 出货失败次数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:22
     */
    public int getShipContinFailCount() {
        SharedPreferences sp = m_context.getSharedPreferences("info_config", Context.MODE_PRIVATE);
        int count = sp.getInt("ShipContinFailCount", 0);
        return count;
    }


    /**
     *@desc 设置每层单货道个数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:25
     */
    public void setPerFloorNumber(String number) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_set_config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putString("PFN", number);
        }
        editor.commit();
    }

    /**
     *@desc 获取每层单货道个数
     *@author Jiancheng,Song
     *@time 2016/5/27 22:37
     */
    public String getPerFloorNumber() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_set_config", Context.MODE_PRIVATE);
        String cdt = "";
        if (null != sp) {
            cdt = sp.getString("PFN", TcnCommon.NUMBER_PER_FLOOR[0]);
        }
        return cdt;
    }


}
