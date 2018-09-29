package com.tcn.background;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonEdit;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;

import java.io.File;


/**
 * Created by Administrator on 2016/12/20.
 */
public class GoodsEdit extends TcnMainActivity {
    private static final String TAG = "GoodsEdit";
    private int m_iId = -1;
    private boolean m_bIsAddGoods = false;
    private String goods_image_path = null;
    private Goods_info m_Goods_info = null;

    private Button goods_btn_slotno = null;
    private Button goods_btn_image = null;

    private Titlebar m_Titlebar = null;
    private ImageView goods_image;
    private ButtonEdit goods_name = null;
    private ButtonEdit goods_code = null;
    private ButtonEdit goods_price = null;
    private ButtonEdit goods_type = null;
    private ButtonEdit goods_stock = null;
    private ButtonEdit goods_introduce = null;
    private ButtonEdit goods_status = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_edit);
        TcnVendIF.getInstance().LoggerDebug(TAG, "onCreate()");
        m_Titlebar = (Titlebar) findViewById(R.id.goods_manage_titlebar);
        m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK_AND_EXIT);
        m_Titlebar.setButtonName(R.string.menu_goods_management);
        m_Titlebar.setButtonSecondName(R.string.menu_save);
        m_Titlebar.setTitleBarListener(m_TitleBarListener);

        goods_image = (ImageView) findViewById(R.id.goods_image);

        goods_btn_slotno = (Button) findViewById(R.id.goods_btn_slotno);
        goods_btn_slotno.setOnClickListener(m_ButtonListener);
        goods_btn_image = (Button) findViewById(R.id.goods_btn_image);
        goods_btn_image.setOnClickListener(m_ButtonListener);

        goods_name = (ButtonEdit) findViewById(R.id.goods_name);
        goods_name.setButtonName(R.string.product_name);
        goods_code = (ButtonEdit) findViewById(R.id.goods_code);
        goods_code.setButtonName(R.string.aisle_goods_code);
        goods_code.setInputType(InputType.TYPE_CLASS_NUMBER);

        goods_price = (ButtonEdit) findViewById(R.id.goods_price);
        goods_price.setButtonName(R.string.product_price);
        goods_price.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_TEXT);

        goods_type = (ButtonEdit) findViewById(R.id.goods_type);
        goods_type.setButtonName(R.string.aisle_goods_type);
        goods_type.setButtonListener(m_ButtonClickListener);

        goods_stock = (ButtonEdit) findViewById(R.id.goods_stock);
        goods_stock.setButtonName(R.string.product_num);
        goods_stock.setInputType(InputType.TYPE_CLASS_NUMBER);
        goods_stock.setVisibility(View.GONE);

        goods_introduce = (ButtonEdit) findViewById(R.id.goods_introduce);
        goods_introduce.setButtonName(R.string.aisle_goods_content);
        goods_status = (ButtonEdit) findViewById(R.id.goods_status);
        goods_status.setButtonName(R.string.goods_state);
        goods_status.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
        goods_status.setInputType(InputType.TYPE_CLASS_NUMBER);
        goods_status.setVisibility(View.GONE);

        initdata();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TcnVendIF.getInstance().LoggerDebug(TAG, "-----onResume()");
        TcnVendIF.getInstance().registerListener(m_vendListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TcnVendIF.getInstance().LoggerDebug(TAG, "-----onPause()");
        TcnVendIF.getInstance().unregisterListener(m_vendListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TcnVendIF.getInstance().LoggerDebug(TAG, "-----onDestroy()");
        if (m_Titlebar != null) {
            m_Titlebar.removeButtonListener();
            m_Titlebar = null;
        }
        if (goods_btn_slotno != null) {
            goods_btn_slotno.setOnClickListener(null);
            goods_btn_slotno = null;
        }
        if (goods_btn_image != null) {
            goods_btn_image.setOnClickListener(null);
            goods_btn_image = null;
        }
        if (goods_type != null) {
            goods_type.setOnClickListener(null);
            goods_type = null;
        }
        goods_image_path = null;
        m_Goods_info = null;
        goods_image = null;
        goods_name = null;
        goods_code = null;
        goods_price = null;
        goods_stock = null;
        goods_introduce = null;
        goods_status = null;
        m_ButtonListener = null;
        m_ButtonClickListener = null;
        m_TitleBarListener = null;
        m_vendListener = null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TcnVendIF.getInstance().LoggerDebug(TAG, "-----onActivityResult() requestCode: "+requestCode+" resultCode: "+resultCode);
        if (resultCode == 100) {
            if(data!=null){
                goods_image_path = data.getStringExtra("pic");
                TcnVendIF.getInstance().LoggerDebug(TAG, "-----onActivityResult() goods_image_path: "+goods_image_path);
                TcnVendIF.getInstance().displayImage(goods_image_path,goods_image,R.mipmap.default_ticket_poster_pic);
            }
        } else if (resultCode == 106) {
            //SlotInfoModifyActivity 返回

        } else {

        }

    }

    private void initdata() {
        Intent mIn = getIntent();
        m_iId = mIn.getIntExtra("goods_edit_id",-1);
        if (m_iId < 0) {
            m_bIsAddGoods = true;
            goods_name.setButtonText("");
            goods_code.setButtonText("");
            goods_price.setButtonText("");
            goods_type.setButtonText("");
            goods_stock.setButtonText(String.valueOf(0));
            goods_introduce.setButtonText("");
            goods_status.setButtonText(String.valueOf(0));
            m_Titlebar.setButtonName(getString(R.string.goods_edit_add_goods));
            TcnVendIF.getInstance().displayImage(goods_image_path,goods_image,R.mipmap.default_ticket_poster_pic);
        } else {
            m_bIsAddGoods = false;
            m_Goods_info = TcnVendIF.getInstance().queryGoodsInfo(m_iId);
            goods_name.setButtonText(m_Goods_info.getGoods_name());
            goods_code.setButtonName(getString(R.string.aisle_goods_code)+"（"+getString(R.string.goods_edit_canot_modify)+"）");
            goods_code.setButtonText(m_Goods_info.getGoods_id());
            goods_code.setButtonEnabled(false);
            goods_price.setButtonText(m_Goods_info.getGoods_price());
            goods_type.setButtonText(m_Goods_info.getGoods_type());
            goods_stock.setButtonText(String.valueOf(m_Goods_info.getGoods_stock()));
            goods_introduce.setButtonText(m_Goods_info.getGoods_introduce());
            goods_status.setButtonText(String.valueOf(m_Goods_info.getGoods_status()));
            goods_image_path = m_Goods_info.getGoods_url();
            TcnVendIF.getInstance().displayImage(goods_image_path,goods_image,R.mipmap.default_ticket_poster_pic);
            m_Titlebar.setButtonName(getString(R.string.goods_edit_modify_goods));
        }
    }

    private void saveData() {
        String strCode = goods_code.getButtonEditText();
        if ((null == strCode) || (strCode.isEmpty())) {
            TcnUtility.getToast(GoodsEdit.this, getString(R.string.tip_goods_code));
            return;
        }
        String price = goods_price.getButtonEditText();
        if ((null == price) || (price.isEmpty())) {
            TcnUtility.getToast(GoodsEdit.this, getString(R.string.tip_price_cannot_empty));
            return;
        }

        /*String status = goods_status.getButtonEditText();
        if ((null == status) || (status.isEmpty())) {
            status = "0";
        }*/
        if (m_iId < 0) {
            UIComBack.getInstance().addGoodsInfo(goods_name.getButtonEditText(),strCode,price,
                    goods_type.getButtonEditText(),goods_stock.getButtonEditText(),goods_introduce.getButtonEditText(),goods_image_path,0);
        } else {
            UIComBack.getInstance().updateGoodsInfo(m_iId,goods_name.getButtonEditText(),strCode,price,
                    goods_type.getButtonEditText(),goods_stock.getButtonEditText(),goods_introduce.getButtonEditText(),goods_image_path,0);
        }
    }

    private ButtonListener m_ButtonListener = new ButtonListener();
    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.goods_btn_image == id) {
                String mPath = TcnVendIF.getInstance().getImageGoodsPath();
                if (null == mPath) {
                    TcnUtility.getToast(GoodsEdit.this, getString(R.string.tip_picture_unchecked));
                    return;
                }
                File file = new File(mPath);
                TcnVendIF.getInstance().LoggerDebug(TAG, "onClick mPath: " + mPath);
                if(!file.exists()) {
                    TcnUtility.getToast(GoodsEdit.this, getString(R.string.tip_picture_unchecked));
                } else {
                    Intent in=new Intent(GoodsEdit.this, AislePics.class);
                    if((m_iId > 0) && (m_Goods_info != null)) {
                        in.putExtra("path", m_Goods_info.getGoods_url());
                    }
                    startActivityForResult(in, 100);
                }
            } else if (R.id.goods_btn_slotno == id) {
                TcnVendIF.getInstance().LoggerError(TAG,"ButtonListener click goods_btn_slotno");
                if (TcnVendIF.getInstance().isDoorOpen()) {
                    TcnUtility.getToast(GoodsEdit.this, getString(R.string.tip_close_door));
                    return;
                }
                String strCode = goods_code.getButtonEditText();
                if ((null == strCode) || (strCode.isEmpty())) {
                    TcnUtility.getToast(GoodsEdit.this, getString(R.string.tip_goods_code));
                    return;
                }

                Intent in=new Intent(GoodsEdit.this, SlotInfoModifyActivity.class);
                if((m_iId > 0) && (m_Goods_info != null)) {
                    in.putExtra("goods_id", m_Goods_info.getGoods_id());
                } else {
                    in.putExtra("goods_id", strCode);
                }
                startActivityForResult(in, 105);

                /*GoodsSlotNoMap mGoodsSlotNoMap = new GoodsSlotNoMap(GoodsEdit.this);
                mGoodsSlotNoMap.setGoodsCode(strCode);
                mGoodsSlotNoMap.setGoodsName(goods_name.getButtonEditText());
                mGoodsSlotNoMap.show();*/
            } else {

            }
        }
    }

    private ButtonClickListener m_ButtonClickListener= new ButtonClickListener();
    private class ButtonClickListener implements ButtonEdit.ButtonListener {
        @Override
        public void onClick(View v, int buttonId) {
            if (null == v) {
                return;
            }
            int id = v.getId();
            if (R.id.goods_type == id) {

            }
        }
    }

    private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
    private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

        @Override
        public void onClick(View v, int buttonId) {
            if (Titlebar.BUTTON_ID_BACK == buttonId) {
                GoodsEdit.this.finish();
            } else if (Titlebar.BUTTON_ID_EXIT == buttonId) {
                saveData();
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
                case TcnVendEventID.INSERT_DATA:
                    GoodsEdit.this.finish();
                    break;
                case TcnVendEventID.UPTE_DATA:
                    GoodsEdit.this.finish();
                    break;
                case TcnVendEventID.DELETE_DATA:
                    GoodsEdit.this.finish();
                    break;
                default:
                    break;
            }
        }
    }
}
