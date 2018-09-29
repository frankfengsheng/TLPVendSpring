package com.tcn.background;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;



/**
 * Created by Administrator on 2016/12/16.
 */
public class ManageGoods extends TcnMainActivity {
    private Titlebar m_Titlebar = null;
    protected Button menu_slot_manage = null;
    private Button menu_slot_err = null;
    private Button menu_goods_bank = null;
    private Button menu_replenishment = null;
    private Button menu_stockcapacity = null;
    private Button menu_repl_stock = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_manage_layout);
        m_Titlebar = (Titlebar) findViewById(R.id.goods_manage_titlebar);
        m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
        m_Titlebar.setButtonName(R.string.menu_goods_management);
        m_Titlebar.setTitleBarListener(m_TitleBarListener);

        menu_slot_manage = (Button) findViewById(R.id.menu_slot_manage);
        if (menu_slot_manage != null) {
            menu_slot_manage.setText(getString(R.string.menu_slots_management));
            menu_slot_manage.setTextColor(Color.parseColor("#ffffff"));
            menu_slot_manage.setOnClickListener(m_ButtonClickListener);
        }

        menu_slot_err = (Button) findViewById(R.id.menu_slot_err);
        if (menu_slot_err != null) {
            menu_slot_err.setText(getString(R.string.menu_fault_handle));
            menu_slot_err.setTextColor(Color.parseColor("#ffffff"));
            menu_slot_err.setOnClickListener(m_ButtonClickListener);
        }

        menu_goods_bank = (Button) findViewById(R.id.menu_goods_bank);
        if (menu_goods_bank != null) {
            menu_goods_bank.setText(getString(R.string.menu_commodity));
            menu_goods_bank.setTextColor(Color.parseColor("#ffffff"));
            menu_goods_bank.setOnClickListener(m_ButtonClickListener);
        }

        menu_replenishment = (Button) findViewById(R.id.menu_replenishment);
        if (menu_replenishment != null) {
            menu_replenishment.setText(getString(R.string.menu_slot_modify_goods));
            menu_replenishment.setTextColor(Color.parseColor("#ffffff"));
            menu_replenishment.setOnClickListener(m_ButtonClickListener);
        }

        menu_stockcapacity = (Button) findViewById(R.id.menu_stockcapacity);
        if (menu_stockcapacity != null) {
            menu_stockcapacity.setText(getString(R.string.menu_slot_modify_stockcapacity));
            menu_stockcapacity.setTextColor(Color.parseColor("#ffffff"));
            menu_stockcapacity.setOnClickListener(m_ButtonClickListener);
        }

        menu_repl_stock = (Button) findViewById(R.id.menu_repl_stock);
        if (menu_repl_stock != null) {
            menu_repl_stock.setText(getString(R.string.menu_replenish_goods));
            menu_repl_stock.setTextColor(Color.parseColor("#ffffff"));
            menu_repl_stock.setOnClickListener(m_ButtonClickListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (m_Titlebar != null) {
            m_Titlebar.removeButtonListener();
            m_Titlebar = null;
        }
        if (menu_slot_manage != null) {
            menu_slot_manage.setOnClickListener(null);
            menu_slot_manage = null;
        }
        if (menu_slot_err != null) {
            menu_slot_err.setOnClickListener(null);
            menu_slot_err = null;
        }
        if (menu_goods_bank != null) {
            menu_goods_bank.setOnClickListener(null);
            menu_goods_bank = null;
        }
        if (menu_replenishment != null) {
            menu_replenishment.setOnClickListener(null);
            menu_replenishment = null;
        }
        if (menu_stockcapacity != null) {
            menu_stockcapacity.setOnClickListener(null);
            menu_stockcapacity = null;
        }
        if (menu_repl_stock != null) {
            menu_repl_stock.setOnClickListener(null);
            menu_repl_stock = null;
        }

        m_TitleBarListener = null;
        m_ButtonClickListener = null;
        m_TitleBarListener = null;
    }

    private void initView() {
        if (menu_slot_manage != null) {
            menu_slot_manage.setBackgroundResource(com.tcn.uicommon.R.drawable.btn_selector);
        }
        if (menu_slot_err != null) {
            menu_slot_err.setBackgroundResource(com.tcn.uicommon.R.drawable.btn_selector);
        }
        if (menu_goods_bank != null) {
            menu_goods_bank.setBackgroundResource(com.tcn.uicommon.R.drawable.btn_selector);
        }
        if (menu_replenishment != null) {
            menu_replenishment.setBackgroundResource(com.tcn.uicommon.R.drawable.btn_selector);
        }
        if (menu_stockcapacity != null) {
            menu_stockcapacity.setBackgroundResource(com.tcn.uicommon.R.drawable.btn_selector);
        }
        if (menu_repl_stock != null) {
            menu_repl_stock.setBackgroundResource(com.tcn.uicommon.R.drawable.btn_selector);
        }
    }

    private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
    private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

        @Override
        public void onClick(View v, int buttonId) {
            if (Titlebar.BUTTON_ID_BACK == buttonId) {
                ManageGoods.this.finish();
            }
        }
    }

    protected ButtonClickListener m_ButtonClickListener= new ButtonClickListener();
    protected class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (null == v) {
                return;
            }
            int id = v.getId();
            if (R.id.menu_slot_manage == id) {
                Intent in = new Intent(ManageGoods.this, AisleDisplay.class);
                startActivityForResult(in,300);
            } else if (R.id.menu_slot_err == id) {
                Intent in = new Intent(ManageGoods.this, ErrSlotActivity.class);
                startActivityForResult(in,301);
            }
            else if (R.id.menu_goods_bank == id) {
                Intent in = new Intent(ManageGoods.this, GoodsBank.class);
                startActivityForResult(in,302);
            } else if (R.id.menu_replenishment == id) {
                Intent in = new Intent(ManageGoods.this, ReplenishActivity.class);
                startActivityForResult(in,303);
            } else if (R.id.menu_stockcapacity == id) {
                Intent in = new Intent(ManageGoods.this, StockCapacityActivity.class);
                startActivityForResult(in,304);
            } else if (R.id.menu_repl_stock == id) {
                Intent in = new Intent(ManageGoods.this, StockReplenishActivity.class);
                startActivityForResult(in,305);
            }
            else {

            }
        }
    }
}
