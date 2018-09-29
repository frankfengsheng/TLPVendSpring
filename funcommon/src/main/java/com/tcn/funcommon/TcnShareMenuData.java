package com.tcn.funcommon;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/8/30.
 */
public class TcnShareMenuData {
    private static TcnShareMenuData m_Instance = null;
    public Context m_context = null;


    public static synchronized TcnShareMenuData getInstance() {
        if (null == m_Instance) {
            m_Instance = new TcnShareMenuData();
        }
        return m_Instance;
    }

    public void init(Context context) {
        m_context = context;
    }

    public void setEnglishOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("English", open);
        }
        editor.commit();
    }

    public boolean isEnglishOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("English", false);
        }

        return bOpen;
    }

    public void setDropSensorWholeOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("DropSensorWhole", open);
        }
        editor.commit();
    }

    public boolean isDropSensorWholeOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("DropSensorWhole", false);
        }

        return bOpen;
    }

    public void setBillEscrowOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("BillEscrow", open);
        }
        editor.commit();
    }

    public boolean isBillEscrowOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("BillEscrow", false);
        }

        return bOpen;
    }

    public void setCoinChangeOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("CoinChange", open);
        }
        editor.commit();
    }

    public boolean isCoinChangeOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("CoinChange", false);
        }

        return bOpen;
    }

    public void setChangeDirectOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("ChangeDirect", open);
        }
        editor.commit();
    }

    public boolean isChangeDirectOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("ChangeDirect", false);
        }

        return bOpen;
    }

    public void setGlassDemisOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("GlassDemis", open);
        }
        editor.commit();
    }

    public boolean isGlassDemisOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("GlassDemis", false);
        }

        return bOpen;
    }

    public void setNoSaleGiveChangeOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("NoSaleGiveChange", open);
        }
        editor.commit();
    }

    public boolean isNoSaleGiveChangeOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("NoSaleGiveChange", false);
        }

        return bOpen;
    }

    public void setAppVendOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("AppVend", open);
        }
        editor.commit();
    }

    public boolean isAppVendOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("AppVend", false);
        }

        return bOpen;
    }

    public void setRecordFullClosingOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("RecordFullClosing", open);
        }
        editor.commit();
    }

    public boolean isRecordFullClosingOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("RecordFullClosing", false);
        }

        return bOpen;
    }

    public void setLowChangeBillOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("LowChangeBill", open);
        }
        editor.commit();
    }

    public boolean isLowChangeBillOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("LowChangeBill", false);
        }

        return bOpen;
    }

    public void setWithCoffeeVendOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("WithCoffeeVend", open);
        }
        editor.commit();
    }

    public boolean isWithCoffeeVendOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("WithCoffeeVend", false);
        }

        return bOpen;
    }

    public void setShowOptionForPaymentOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("ShowOptionForPayment", open);
        }
        editor.commit();
    }

    public boolean isShowOptionForPaymentOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("ShowOptionForPayment", false);
        }

        return bOpen;
    }

    public void setNoDonateItemStopOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("NoDonateItemStop", open);
        }
        editor.commit();
    }

    public boolean isNoDonateItemStopOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("NoDonateItemStop", false);
        }

        return bOpen;
    }

    public void setPayOutBillMethodOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("PayOutBillMethod", open);
        }
        editor.commit();
    }

    public boolean isPayOutBillMethodOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("PayOutBillMethod", false);
        }

        return bOpen;
    }

    public void setRemoteControlOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("RemoteControl", open);
        }
        editor.commit();
    }

    public boolean isRemoteControlOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("RemoteControl", false);
        }

        return bOpen;
    }

    public void setAlipayOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("Alipay", open);
        }
        editor.commit();
    }

    public boolean isAlipayOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("Alipay", false);
        }

        return bOpen;
    }

    public void setWechatPayOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("WechatPay", open);
        }
        editor.commit();
    }

    public boolean isWechatPayOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("WechatPay", false);
        }

        return bOpen;
    }

    public void setAlarmOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("Alarm", open);
        }
        editor.commit();
    }

    public boolean isAlarmOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("Alarm", false);
        }

        return bOpen;
    }

    public void setPowerOnOffOpen(boolean open) {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null != editor) {
            editor.putBoolean("PowerOnOff", open);
        }
        editor.commit();
    }

    public boolean isPowerOnOffOpen() {
        SharedPreferences sp = m_context.getSharedPreferences("menu_system", Context.MODE_PRIVATE);
        boolean bOpen = false;
        if (null != sp) {
            bOpen = sp.getBoolean("PowerOnOff", false);
        }

        return bOpen;
    }
}
