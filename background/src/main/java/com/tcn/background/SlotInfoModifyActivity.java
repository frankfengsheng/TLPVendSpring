package com.tcn.background;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;

import com.tcn.background.goods.GoodsSlotNoMap;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonBatchSelect;
import com.tcn.uicommon.view.TcnMainActivity;



/**
 * Created by Administrator on 2017/2/23.
 */
public class SlotInfoModifyActivity extends TcnMainActivity {
    private static final String TAG = "SlotInfoModifyActivity";

    private boolean m_bIsModified = false;
    private String m_goods_id = null;
    private Button slot_info_back = null;
    private Button slot_info_syn = null;
    private ButtonBatchSelect slot_info_modify_goods_code = null;
    private ButtonBatchSelect slot_info_modify_goods_price = null;
    private ButtonBatchSelect slot_info_modify_goods_stock = null;
    private GoodsSlotNoMap m_GoodsSlotNoMap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slot_info_modify);
        slot_info_back = (Button) findViewById(R.id.slot_info_back);
        slot_info_back.setOnClickListener(m_TitleButtonListener);
        slot_info_syn = (Button) findViewById(R.id.slot_info_syn);
        slot_info_syn.setOnClickListener(m_TitleButtonListener);
        slot_info_modify_goods_code = (ButtonBatchSelect) findViewById(R.id.slot_info_modify_goods_code);
        slot_info_modify_goods_code.setButtonType(ButtonBatchSelect.BUTTON_TYPE_EDIT_TITLE_NO);
        slot_info_modify_goods_code.setButtonListener(m_SlotButtonListener);
        slot_info_modify_goods_price = (ButtonBatchSelect) findViewById(R.id.slot_info_modify_goods_price);
        slot_info_modify_goods_price.setButtonName(getString(R.string.slot_info_modify_input_price));
        slot_info_modify_goods_price.setEditTitleInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        slot_info_modify_goods_price.setButtonListener(m_SlotButtonListener);
        slot_info_modify_goods_stock = (ButtonBatchSelect) findViewById(R.id.slot_info_modify_goods_stock);
        slot_info_modify_goods_stock.setButtonName(getString(R.string.slot_info_modify_input_stock));
        slot_info_modify_goods_stock.setButtonListener(m_SlotButtonListener);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TcnVendIF.getInstance().registerListener(m_vendListener);
        m_bIsModified = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        TcnVendIF.getInstance().unregisterListener(m_vendListener);
    }

    @Override
    protected void onDestroy() {
        if (slot_info_back != null) {
            slot_info_back.setOnClickListener(null);
            slot_info_back = null;
        }
        if (slot_info_syn != null) {
            slot_info_syn.setOnClickListener(null);
            slot_info_syn = null;
        }
        if (slot_info_modify_goods_code != null) {
            slot_info_modify_goods_code.setButtonListener(null);
            slot_info_modify_goods_code = null;
        }
        if (slot_info_modify_goods_price != null) {
            slot_info_modify_goods_price.setButtonListener(null);
            slot_info_modify_goods_price = null;
        }
        if (slot_info_modify_goods_stock != null) {
            slot_info_modify_goods_stock.setButtonListener(null);
            slot_info_modify_goods_stock = null;
        }
        m_goods_id = null;

        m_GoodsSlotNoMap = null;
        m_TitleButtonListener = null;
        m_SlotButtonListener = null;
        m_vendListener = null;
        super.onDestroy();
    }

    private void initData() {
        Intent in = getIntent();
        m_goods_id = in.getStringExtra("goods_id");
        slot_info_modify_goods_code.setButtonName(getString(R.string.slot_info_modify_goods_code)+"("+m_goods_id+")"+getString(R.string.slot_info_modify_map_slot));
        m_GoodsSlotNoMap = new GoodsSlotNoMap(SlotInfoModifyActivity.this);
    }

    private TitleButtonListener m_TitleButtonListener = new TitleButtonListener();
    private class TitleButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (null == v) {
                return;
            }
            int id = v.getId();
            if (id == R.id.slot_info_back) {
                if (m_bIsModified) {
                    TcnUtility.getToast(SlotInfoModifyActivity.this, getString(R.string.slot_info_modify_tips_syn));
                    return;
                }
                setResult(106);
                finish();
            } else if (id == R.id.slot_info_syn) {
                TcnVendIF.getInstance().reqSlotNoInfo();
            } else {

            }
        }
    }

    private SlotButtonListener m_SlotButtonListener = new SlotButtonListener();
    private class SlotButtonListener implements ButtonBatchSelect.ButtonListener {

        @Override
        public void onClick(View v, int buttonId) {
            if (null == v) {
                return;
            }
            if (TcnVendIF.getInstance().isDoorOpen()) {
                TcnUtility.getToast(SlotInfoModifyActivity.this, getString(R.string.tip_close_door));
                return;
            }
            int id = v.getId();
            if (id == R.id.slot_info_modify_goods_code) {
                if (buttonId == ButtonBatchSelect.BUTTON_ID_LEFT) {
                    String start = slot_info_modify_goods_code.getStartEditText();
                    if (!TcnVendIF.getInstance().isDigital(start)) {
                        TcnUtility.getToast(SlotInfoModifyActivity.this, getString(R.string.slot_info_modify_tips_slot_err));
                        return;
                    }
                    String end = slot_info_modify_goods_code.getEndEditText();
                    if (!TcnVendIF.getInstance().isDigital(end)) {
                        end = start;
                    }

                    TcnVendIF.getInstance().reqWriteSlotGoodsCode(Integer.valueOf(start),Integer.valueOf(end),m_goods_id);
                } else if (buttonId == ButtonBatchSelect.BUTTON_ID_RIGHT) {
                    if (!TcnVendIF.getInstance().isDigital(m_goods_id)) {
                        TcnUtility.getToast(SlotInfoModifyActivity.this, getString(R.string.tip_goods_code));
                        return;
                    }
                    m_GoodsSlotNoMap.setType(GoodsSlotNoMap.TYPE_MAP_GOODS_CODE);
                    m_GoodsSlotNoMap.setGoodsData(m_goods_id);
                    m_GoodsSlotNoMap.show();
                }
            } else if (id == R.id.slot_info_modify_goods_price) {
                String price = slot_info_modify_goods_price.getTitleEditText();
                if ((!TcnVendIF.getInstance().isDigital(price)) && (!TcnVendIF.getInstance().isContainDeciPoint(price))) {
                    TcnUtility.getToast(SlotInfoModifyActivity.this, getString(R.string.slot_info_modify_tips_price_err));
                    return;
                }
                if (buttonId == ButtonBatchSelect.BUTTON_ID_LEFT) {

                    String start = slot_info_modify_goods_price.getStartEditText();
                    if (!TcnVendIF.getInstance().isDigital(start)) {
                        TcnUtility.getToast(SlotInfoModifyActivity.this, getString(R.string.slot_info_modify_tips_slot_err));
                        return;
                    }
                    String end = slot_info_modify_goods_price.getEndEditText();
                    if (!TcnVendIF.getInstance().isDigital(end)) {
                        end = start;
                    }

                    TcnVendIF.getInstance().reqWriteSlotPrice(Integer.valueOf(start),Integer.valueOf(end),price);
                } else if (buttonId == ButtonBatchSelect.BUTTON_ID_RIGHT) {
                    m_GoodsSlotNoMap.setType(GoodsSlotNoMap.TYPE_MODIFY_GOODS_PRICE);
                    m_GoodsSlotNoMap.setGoodsData(price);
                    m_GoodsSlotNoMap.show();
                }
            } else if (id == R.id.slot_info_modify_goods_stock) {
                if (buttonId == ButtonBatchSelect.BUTTON_ID_LEFT) {
                    String stock = slot_info_modify_goods_stock.getTitleEditText();
                    if (!TcnVendIF.getInstance().isDigital(stock)) {
                        TcnUtility.getToast(SlotInfoModifyActivity.this, getString(R.string.slot_info_modify_tips_stock_err));
                        return;
                    }

                    String start = slot_info_modify_goods_stock.getStartEditText();
                    if (!TcnVendIF.getInstance().isDigital(start)) {
                        TcnUtility.getToast(SlotInfoModifyActivity.this, getString(R.string.slot_info_modify_tips_slot_err));
                        return;
                    }
                    String end = slot_info_modify_goods_stock.getEndEditText();
                    if (!TcnVendIF.getInstance().isDigital(end)) {
                        end = start;
                    }

                    TcnVendIF.getInstance().reqWriteSlotStock(Integer.valueOf(start),Integer.valueOf(end),Integer.valueOf(stock));
                } else if (buttonId == ButtonBatchSelect.BUTTON_ID_RIGHT) {
                    m_GoodsSlotNoMap.setType(GoodsSlotNoMap.TYPE_MODIFY_GOODS_STOCK);
                    m_GoodsSlotNoMap.show();
                }
            } else {

            }
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
                case TcnVendEventID.QUERY_ALIVE_COIL:
                    TcnUtility.getToast(SlotInfoModifyActivity.this, getString(R.string.slot_info_modify_tips_syn_success));
                    m_bIsModified = false;
                    setResult(106);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
