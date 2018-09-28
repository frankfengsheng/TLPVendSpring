package com.tcn.background;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;



/**
 * Created by Administrator on 2016/12/9.
 */
public class SalesDataActivity extends TcnMainActivity {

    private static final String TAG = "SalesDataActivity";
    private Titlebar m_Titlebar = null;

    private Button sales_clear_sales_data = null;
    private Button sales_data_get = null;
    private TextView sales_count = null;
    private TextView sales_mount = null;
    private TextView sales_swallow_money = null;
    private TextView sales_recharge = null;
    private TextView sales_cash_count = null;
    private TextView sales_cash_mount = null;
    private TextView sales_bank_card_one_count = null;
    private TextView sales_bank_card_one_mount = null;
    private TextView sales_ic_card_count = null;
    private TextView sales_ic_card_mount = null;
    private TextView sales_wechat_count = null;
    private TextView sales_wechat_mount = null;
    private TextView sales_alipay_count = null;
    private TextView sales_alipay_mount = null;
    private TextView sales_bank_card_two_count = null;
    private TextView sales_bank_card_two_mount = null;
    private TextView sales_take_goods_code_count = null;
    private TextView sales_take_goods_code_mount = null;
    private TextView sales_remount_count = null;
    private TextView sales_remount_mount = null;
    private TextView sales_give_count = null;
    private TextView sales_give_mount = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_data);
        TcnVendIF.getInstance().LoggerDebug(TAG, "SalesDataActivity onCreate()");
        m_Titlebar = (Titlebar) findViewById(R.id.sales_setttings_titlebar);
        m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
        m_Titlebar.setButtonName(R.string.salesdata);
        m_Titlebar.setTitleBarListener(m_TitleBarListener);

        sales_clear_sales_data = (Button) findViewById(R.id.sales_clear_sales_data);
        sales_clear_sales_data.setOnClickListener(m_ButtonClickListener);

        sales_data_get = (Button) findViewById(R.id.sales_data_get);
        sales_data_get.setOnClickListener(m_ButtonClickListener);
        sales_count = (TextView) findViewById(R.id.sales_count);
        sales_mount = (TextView) findViewById(R.id.sales_mount);
        sales_swallow_money = (TextView) findViewById(R.id.sales_swallow_money);
        sales_recharge = (TextView) findViewById(R.id.sales_recharge);
        sales_cash_count = (TextView) findViewById(R.id.sales_cash_count);
        sales_cash_mount = (TextView) findViewById(R.id.sales_cash_mount);
        sales_bank_card_one_count = (TextView) findViewById(R.id.sales_bank_card_one_count);
        sales_bank_card_one_mount = (TextView) findViewById(R.id.sales_bank_card_one_mount);
        sales_ic_card_count = (TextView) findViewById(R.id.sales_ic_card_count);
        sales_ic_card_mount = (TextView) findViewById(R.id.sales_ic_card_mount);
        sales_wechat_count = (TextView) findViewById(R.id.sales_wechat_count);
        sales_wechat_mount = (TextView) findViewById(R.id.sales_wechat_mount);
        sales_alipay_count = (TextView) findViewById(R.id.sales_alipay_count);
        sales_alipay_mount = (TextView) findViewById(R.id.sales_alipay_mount);
        sales_bank_card_two_count = (TextView) findViewById(R.id.sales_bank_card_two_count);
        sales_bank_card_two_mount = (TextView) findViewById(R.id.sales_bank_card_two_mount);
        sales_take_goods_code_count = (TextView) findViewById(R.id.sales_take_goods_code_count);
        sales_take_goods_code_mount = (TextView) findViewById(R.id.sales_take_goods_code_mount);
        sales_remount_count = (TextView) findViewById(R.id.sales_remount_count);
        sales_remount_mount = (TextView) findViewById(R.id.sales_remount_mount);
        sales_give_count = (TextView) findViewById(R.id.sales_give_count);
        sales_give_mount = (TextView) findViewById(R.id.sales_give_mount);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TcnVendIF.getInstance().registerListener(m_vendListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TcnVendIF.getInstance().unregisterListener(m_vendListener);
    }

    @Override
    protected void onDestroy() {
        if (m_Titlebar != null) {
            m_Titlebar.removeButtonListener();
            m_Titlebar = null;
        }
        if (sales_clear_sales_data != null) {
            sales_clear_sales_data.setOnClickListener(null);
            sales_clear_sales_data = null;
        }
        if (sales_data_get != null) {
            sales_data_get.setOnClickListener(null);
            sales_data_get = null;
        }
        sales_count = null;
        sales_mount = null;
        sales_swallow_money = null;
        sales_recharge = null;
        sales_cash_count = null;
        sales_cash_mount = null;
        sales_bank_card_one_count = null;
        sales_bank_card_one_mount = null;
        sales_ic_card_count = null;
        sales_ic_card_mount = null;
        sales_wechat_count = null;
        sales_wechat_mount = null;
        sales_alipay_count = null;
        sales_alipay_mount = null;
        sales_bank_card_two_count = null;
        sales_take_goods_code_count = null;
        sales_take_goods_code_mount = null;
        sales_remount_count = null;
        sales_remount_mount = null;
        sales_give_count = null;
        sales_give_mount = null;
        super.onDestroy();
    }

    private void setSaleData(String data) {
        if (!data.contains("|")) {
            return;
        }
        String[] strarr = data.split("\\|");
        if (strarr.length <= 23) {
            return;
        }
        sales_count.setText(getString(R.string.sales_count)+" "+strarr[2]);
        sales_mount.setText(getString(R.string.sales_mount)+" "+strarr[3]);
        sales_swallow_money.setText(getString(R.string.sales_swallow_money)+" "+strarr[4]);
        sales_recharge.setText(getString(R.string.sales_recharge)+" "+strarr[5]);
        sales_cash_count.setText(getString(R.string.sales_cash_count)+" "+strarr[6]);
        sales_cash_mount.setText(getString(R.string.sales_cash_mount)+" "+strarr[7]);
        sales_bank_card_one_count.setText(getString(R.string.sales_bank_card_one_count)+" "+strarr[8]);
        sales_bank_card_one_mount.setText(getString(R.string.sales_bank_card_one_mount)+" "+strarr[9]);
        sales_ic_card_count.setText(getString(R.string.sales_ic_card_count)+" "+strarr[10]);
        sales_ic_card_mount.setText(getString(R.string.sales_ic_card_mount)+" "+strarr[11]);
        sales_wechat_count.setText(getString(R.string.sales_wechat_count)+" "+strarr[12]);
        sales_wechat_mount.setText(getString(R.string.sales_wechat_mount)+" "+strarr[13]);
        sales_alipay_count.setText(getString(R.string.sales_alipay_count)+" "+strarr[14]);
        sales_alipay_mount.setText(getString(R.string.sales_alipay_mount)+" "+strarr[15]);
        sales_bank_card_two_count.setText(getString(R.string.sales_bank_card_two_count)+" "+strarr[16]);
        sales_bank_card_two_mount.setText(getString(R.string.sales_bank_card_two_mount)+" "+strarr[17]);
        sales_take_goods_code_count.setText(getString(R.string.sales_take_goods_code_count)+" "+strarr[18]);
        sales_take_goods_code_mount.setText(getString(R.string.sales_take_goods_code_mount)+" "+strarr[19]);
        sales_remount_count.setText(getString(R.string.sales_remount_count)+" "+strarr[20]);
        sales_remount_mount.setText(getString(R.string.sales_remount_mount)+" "+strarr[21]);
        sales_give_count.setText(getString(R.string.sales_give_count)+" "+strarr[22]);
        sales_give_mount.setText(getString(R.string.sales_give_mount)+" "+strarr[23]);

    }

    private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
    private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

        @Override
        public void onClick(View v, int buttonId) {
            if (Titlebar.BUTTON_ID_BACK == buttonId) {
                SalesDataActivity.this.finish();
            }
        }
    }

    private ButtonClickListener m_ButtonClickListener= new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private VendListener m_vendListener = new VendListener();
    private class VendListener implements TcnVendIF.VendEventListener {
        @Override
        public void VendEvent(VendEventInfo cEventInfo) {
            if (null == cEventInfo) {
                TcnVendIF.getInstance().LoggerError(TAG, "VendListener cEventInfo is null");
                return;
            }
            switch (cEventInfo.m_iEventID) {
                case TcnVendEventID.COMMAND_TOTAL_SALES_INFO:
                    setSaleData(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.COMMAND_CONFIG_OK:
                    if (cEventInfo.m_lParam1 == 93) {
                        TcnUtility.getToast(SalesDataActivity.this, getString(R.string.tip_clear_success));
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
