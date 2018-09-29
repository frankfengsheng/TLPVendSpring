package com.tcn.background.goods;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.tcn.background.R;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonEdit;
import com.tcn.uicommon.resources.Resources;

/**
 * Created by Administrator on 2016/12/22.
 */
public class GoodsSlotNoMap extends Dialog {
    private static final String TAG = "GoodsSlotNoMap";
    private static int m_listData_count = 0;
    public static int TYPE_MAP_GOODS_CODE = 0;
    public static int TYPE_MODIFY_GOODS_PRICE = 1;
    public static int TYPE_MODIFY_GOODS_STOCK = 2;
    private int m_iType = TYPE_MAP_GOODS_CODE;
    private int m_iSelectItemIndex = 0;
    private String m_GoodsData = null;
    private Context m_context = null;
    private TextView m_title = null;
    private TextView m_goods_slotno_map_tips = null;
    private Button m_goods_slotno_sure = null;
    private GridView m_GridView = null;


    public GoodsSlotNoMap(Context context) {
        super(context, R.style.Dialog_bocop);
        init(context);
    }

    private void init(Context context) {
        m_context = context;
        View contentView = View.inflate(context, R.layout.goods_slotno_map, null);
        setContentView(contentView);

        m_title = (TextView) findViewById(R.id.goods_slotno_map_title);
        m_goods_slotno_sure = (Button) findViewById(R.id.goods_slotno_sure);
        m_goods_slotno_sure.setOnClickListener(m_ButtonListener);
        m_goods_slotno_map_tips = (TextView) findViewById(R.id.goods_slotno_map_tips);
        m_GridView = (GridView) findViewById(R.id.goods_slotno_map_grid);

        Window win = getWindow();
        getWindow().setWindowAnimations(Resources.getAnimResourceID(com.tcn.uicommon.R.anim.alpha_in));
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.dimAmount = 0.5f;
        win.setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
        initData();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        m_GoodsData = null;
    }

    public void setGoodsData(String data) {
        m_GoodsData = data;
    }

    public void setType(int type) {
        m_iType = type;
    }

    private void initData() {
        m_title.setTextSize(35);
        m_listData_count = TcnVendIF.getInstance().getAliveCoilCountAll();
        TcnVendIF.getInstance().LoggerDebug(TAG, "initData m_listData_count: "+m_listData_count);
        if (TYPE_MAP_GOODS_CODE == m_iType) {
            m_goods_slotno_map_tips.setVisibility(View.VISIBLE);
            m_title.setText(m_context.getString(R.string.slot_info_modify_goods_code)+"("+m_GoodsData+")"+m_context.getString(R.string.slot_info_modify_map_slot));
        } else if (TYPE_MODIFY_GOODS_PRICE == m_iType) {
            m_goods_slotno_map_tips.setVisibility(View.VISIBLE);
            m_title.setText(m_context.getString(R.string.slot_info_modify_goods_price)+m_GoodsData+TcnShareUseData.getInstance().getUnitPrice()+m_context.getString(R.string.slot_info_modify_map_slot));
        } else if (TYPE_MODIFY_GOODS_STOCK == m_iType) {
            m_goods_slotno_map_tips.setVisibility(View.GONE);
            m_title.setText(m_context.getString(R.string.slot_info_modify_goods_stock));
        } else {

        }
        m_GridView.setAdapter(m_MapAdapter);
        m_MapAdapter.notifyDataSetChanged();
    }

    private void selectSlotStock(int slotNo,int which, String[] data) {
        if (null == data || which < 0 || data.length <= which) {
            TcnVendIF.getInstance().LoggerError(TAG, "selectSlotStock which: "+which);
        }
        if(which == (data.length-1)) {
            TcnVendIF.getInstance().reqWriteSlotStock(slotNo,slotNo,199);
            return;
        }
        if (!TcnVendIF.getInstance().isDigital(data[which])) {
            TcnVendIF.getInstance().LoggerError(TAG, "selectSlotStock is not isDigital");
            return;
        }
        TcnVendIF.getInstance().reqWriteSlotStock(slotNo,slotNo,Integer.valueOf(data[which]));
    }

    private void showStockSelectDialog(final int slotNo, final String[] data, final EditText v) {
        if (slotNo < 1) {
            TcnVendIF.getInstance().LoggerError(TAG, "showStockSelectDialog slotNo: "+slotNo);
            return;
        }
        Coil_info info = TcnVendIF.getInstance().getCoilInfo(slotNo);
        int checkedItem = 0;
        if (info.getExtant_quantity() == 199) {
            checkedItem = data.length - 1;
        } else {
            for (int i = 0; i < (data.length - 1); i++) {
                if (info.getExtant_quantity() == Integer.valueOf(data[i])) {
                    checkedItem = i;
                    break;
                }
            }
        }
        m_iSelectItemIndex = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
        builder.setTitle(m_context.getString(R.string.please_choose));
        builder.setSingleChoiceItems(data, checkedItem, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                m_iSelectItemIndex = which;
            }
        });
        builder.setPositiveButton(m_context.getString(R.string.backgroound_ensure), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                selectSlotStock(slotNo,m_iSelectItemIndex,data);
                v.setText(data[m_iSelectItemIndex]);
            }
        });
        builder.setNegativeButton(m_context.getString(R.string.backgroound_cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
    }

    private MapAdapter m_MapAdapter = new MapAdapter();
    private class MapAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return m_listData_count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Coil_info info = TcnVendIF.getInstance().getCoilInfoAll(position);
            if ((null == info) || (null == m_GoodsData)) {
                TcnVendIF.getInstance().LoggerError(TAG, "getView info is null");
                return convertView;
            }
            if(null == convertView) {
                convertView = getLayoutInflater().inflate(R.layout.slotno_item, null);
                holder = new ViewHolder();
                holder.mTextView = (TextView) convertView.findViewById(R.id.slotno_item_text);
                holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.slotno_item_checkbox);
                holder.mBtnStock = (ButtonEdit) convertView.findViewById(R.id.slotno_item_stock);
                if (TYPE_MODIFY_GOODS_STOCK == m_iType) {
                    holder.mTextView.setVisibility(View.GONE);
                    holder.mCheckBox.setVisibility(View.GONE);
                    holder.mBtnStock.setLayouRatio(2,1);
                    holder.mBtnStock.setVisibility(View.VISIBLE);
                    holder.mBtnStock.setTag(info.getCoil_id());
                    holder.mBtnStock.setButtonType(ButtonEdit.BUTTON_TYPE_SELECT);
                    holder.mBtnStock.setButtonListener(m_ButtonEditClickListener);
                } else {
                    holder.mBtnStock.setVisibility(View.GONE);
                    holder.mTextView.setVisibility(View.VISIBLE);
                    holder.mCheckBox.setVisibility(View.VISIBLE);
                    holder.mCheckBox.setTag(info.getCoil_id());
                 //   holder.mCheckBox.setOnCheckedChangeListener(mCheckBoxListener);
                    holder.mCheckBox.setOnClickListener(mButtonClickListener);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            if (TYPE_MAP_GOODS_CODE == m_iType) {
                if (m_GoodsData.equals(info.getGoodsCode())) {
                    holder.mCheckBox.setChecked(true);
                } else {
                    holder.mCheckBox.setChecked(false);
                }
                holder.mTextView.setText(info.getCoil_id()+"-"+info.getGoodsCode());
            } else if (TYPE_MODIFY_GOODS_PRICE == m_iType) {
                if ((Float.valueOf(m_GoodsData).floatValue()) == (Float.valueOf(info.getPar_price()).floatValue())) {
                    holder.mCheckBox.setChecked(true);
                } else {
                    holder.mCheckBox.setChecked(false);
                }
                holder.mTextView.setText(info.getCoil_id()+"-"+info.getPar_price());
            } else if (TYPE_MODIFY_GOODS_STOCK == m_iType) {
                if (info.getExtant_quantity() == 199) {
                    holder.mBtnStock.setButtonText(TcnCommon.SLOT_STOCK_SELECT[TcnCommon.SLOT_STOCK_SELECT.length - 1]);
                } else {
                    holder.mBtnStock.setButtonText(String.valueOf(info.getExtant_quantity()));
                }
                holder.mBtnStock.setButtonName(String.valueOf(info.getCoil_id()));
            }
            return convertView;
        }
    }

    private static class ViewHolder {
        public TextView mTextView;
        public CheckBox mCheckBox;
        public ButtonEdit mBtnStock;
    }

    private ButtonClickListener mButtonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (null == v) {
                return;
            }
            int iSlotNo = 0;
            if (v.getTag() == null) {
                return;
            }
            iSlotNo = (int)v.getTag();
            if (TYPE_MAP_GOODS_CODE == m_iType) {
                CheckBox mCheckBox = (CheckBox)v;
                TcnVendIF.getInstance().LoggerDebug(TAG, "-----mButtonClickListener() isChecked: "+mCheckBox.isChecked()+" isSelected: "+mCheckBox.isSelected());
                TcnVendIF.getInstance().reqWriteSlotGoodsCode(iSlotNo,iSlotNo,m_GoodsData);
                mCheckBox.setChecked(true);
              //  mCheckBox.setClickable(false);
            } else if (TYPE_MODIFY_GOODS_PRICE == m_iType) {
                CheckBox mCheckBox = (CheckBox)v;
                TcnVendIF.getInstance().reqWriteSlotPrice(iSlotNo,iSlotNo,m_GoodsData);
                mCheckBox.setChecked(true);
               // mCheckBox.setClickable(false);
            } else {

            }
        }
    }

    /*private CheckBoxListener mCheckBoxListener = new CheckBoxListener();
    private class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if ((null == m_GoodsCode) || (m_GoodsCode.isEmpty())) {
                TcnVendIF.getInstance().LoggerError(TAG, "onCheckedChanged m_GoodsCode: "+m_GoodsCode);
                return;
            }
            int iSlotNo = (int)buttonView.getTag();
            TcnVendIF.getInstance().LoggerDebug(TAG, "CheckBoxListener isChecked: "+isChecked+" iSlotNo: "+iSlotNo);
            if (isChecked) {
                TcnVendIF.getInstance().reqWriteSlotGoodsCode(iSlotNo,iSlotNo,m_GoodsCode);
            } else {
                TcnVendIF.getInstance().reqWriteSlotGoodsCode(iSlotNo,iSlotNo,String.valueOf(0));
            }
        }
    }*/

    private ButtonEditClickListener m_ButtonEditClickListener= new ButtonEditClickListener();
    private class ButtonEditClickListener implements ButtonEdit.ButtonListener {
        @Override
        public void onClick(View v, int buttonId) {
            if (null == v) {
                return;
            }
            int id = v.getId();
            int slotNo = 0;
            if (v.getTag() == null) {
                return;
            }
            slotNo = (int)v.getTag();
            if (R.id.slotno_item_stock == id) {
                if (ButtonEdit.BUTTON_ID_SELECT == buttonId) {
                    ButtonEdit mView = (ButtonEdit)v;
                    showStockSelectDialog(slotNo, TcnCommon.SLOT_STOCK_SELECT,mView.getButtonEdit());
                }
            }
        }
    }

    private ButtonListener m_ButtonListener = new ButtonListener();
    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.goods_slotno_sure == id) {
                dismiss();
            }
        }
    }
}
