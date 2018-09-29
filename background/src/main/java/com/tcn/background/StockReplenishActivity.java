package com.tcn.background;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tcn.background.controller.GropInfoBack;
import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */
public class StockReplenishActivity extends TcnMainActivity {

    private static final String TAG = "StockReplenishActivity";
    private static int m_listData_count = 0;

    private int m_mchGrpId = 0;

    private SpannableStringBuilder m_stringBuilder = null;
    private AbsoluteSizeSpan m_textSizeSpan = null;

    private List<Coil_info> m_list_aliveCoil;
    private List<Button> m_list_btn_slot;
    private List<Button> m_list_btn_ad_sb;
    private List<Button> m_list_btn_floor;
    private List<Button> m_list_btn_cb;
    private Titlebar m_Titlebar = null;
    private LinearLayout rep_cabinet = null;
    private LinearLayout rep_set = null;
    private LinearLayout m_table = null;

    private Button rep_btn_all = null;
    private Button rep_btn_clean_fault = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_stockreplenish_layout);
        TcnVendIF.getInstance().LoggerDebug(TAG, "StockReplenishActivity onCreate()");

        m_Titlebar = (Titlebar) findViewById(R.id.goods_stockreplenish_titlebar);
        m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
        m_Titlebar.setButtonName(getString(R.string.menu_replenish_goods));
        m_Titlebar.setTitleBarListener(m_TitleBarListener);

        rep_cabinet = (LinearLayout) findViewById(R.id.rep_cabinet);
        rep_set = (LinearLayout) findViewById(R.id.rep_set);
        m_table = (LinearLayout) findViewById(R.id.dictTable);

        rep_btn_all = (Button) findViewById(R.id.rep_btn_all);
        rep_btn_all.setTag("reqAll");
        rep_btn_all.setOnClickListener(m_BtnOnClickListener);

        rep_btn_clean_fault = (Button) findViewById(R.id.rep_btn_clean_fault);
        rep_btn_clean_fault.setTag("reqClean");
        rep_btn_clean_fault.setOnClickListener(m_BtnOnClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TcnVendIF.getInstance().LoggerDebug(TAG, "onResume()");
        TcnVendIF.getInstance().registerListener(m_vendListener);
        List<GropInfoBack> mInfoBackList = UIComBack.getInstance().getGroupListAll();
        setCabinet(mInfoBackList);
        if (mInfoBackList.size() > 1) {
            m_list_aliveCoil = UIComBack.getInstance().getAliveCoil(0);
            m_listData_count = m_list_aliveCoil.size();
            setCabinetSelected("cb0");
        } else {
            m_list_aliveCoil = TcnVendIF.getInstance().getAliveCoil();
            m_listData_count = TcnVendIF.getInstance().getAliveCoilCount();
        }
        m_mchGrpId = 0;
        appendNewRow(m_table,m_list_aliveCoil);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TcnVendIF.getInstance().LoggerDebug(TAG, "onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TcnVendIF.getInstance().LoggerDebug(TAG, "onDestroy()");
        TcnVendIF.getInstance().unregisterListener(m_vendListener);
        if (m_Titlebar != null) {
            m_Titlebar.removeButtonListener();
            m_Titlebar = null;
        }
        if (m_list_btn_cb != null) {
            for (Button btn: m_list_btn_cb) {
                btn.setOnClickListener(null);
            }
            m_list_btn_cb.clear();
            m_list_btn_cb = null;
        }
        if (m_list_btn_floor != null) {
            for (Button btn: m_list_btn_floor) {
                btn.setOnClickListener(null);
            }
            m_list_btn_floor.clear();
            m_list_btn_floor = null;
        }
        if (m_list_btn_ad_sb != null) {
            for (Button btn: m_list_btn_ad_sb) {
                btn.setOnClickListener(null);
            }
            m_list_btn_ad_sb.clear();
            m_list_btn_ad_sb = null;
        }
        if (m_list_btn_slot != null) {
            m_list_btn_slot.clear();
            m_list_btn_slot = null;
        }
        if (rep_btn_all != null) {
            rep_btn_all.setOnClickListener(null);
            rep_btn_all = null;
        }
        if (rep_btn_clean_fault != null) {
            rep_btn_clean_fault.setOnClickListener(null);
            rep_btn_clean_fault = null;
        }
        m_BtnOnClickListener = null;
        m_vendListener = null;
        m_table = null;
    }

    private boolean isContainBtn(List<Button> listBtn, String btnTag) {
        boolean bRet = false;

        for (Button btn:listBtn) {
            if (btnTag.equals(btn.getTag())) {
                bRet = true;
                break;
            }
        }

        return bRet;
    }

    private Button getContainBtn(List<Button> listBtn, String btnTag) {
        Button bRet = null;

        for (Button btn:listBtn) {
            if (btnTag.equals(btn.getTag())) {
                bRet = btn;
                break;
            }
        }

        return bRet;
    }


    private void setBtnStock(List<Button> listBtn, String btnTag,int stock,Coil_info info) {
        for (Button btn:listBtn) {
            if (btnTag.equals(btn.getTag())) {
                SpannableStringBuilder mText = getSpanPaySlotString(info.getCoil_id(),stock,info.getCapacity(),info.getWork_status());
                btn.setText(mText);
                break;
            }
        }
    }

    private void setBtnStock(List<Button> listBtn, int start,int end) {

        Coil_info sInfo = null;
        Button sBtn = null;

        for (int i = start; i <= end ; i++) {
            sBtn = getContainBtn(listBtn,"st"+i);
            if (sBtn != null) {
                sInfo = TcnVendIF.getInstance().getCoilInfo(i);
                SpannableStringBuilder mText = getSpanPaySlotString(sInfo.getCoil_id(),sInfo.getExtant_quantity(),sInfo.getCapacity(),sInfo.getWork_status());
                sBtn.setText(mText);
            }
        }
    }

    private void setFloorBtn(int floor,LinearLayout tableRow) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIComBack.getInstance().getRepGoodsPxWidth(), UIComBack.getInstance().getRepGoodsPxHeight());
        params.setMargins(2, 0, 2, 0);

        Button floorBtn = new Button(this);
        floorBtn.setText(getString(R.string.req_no)+String.valueOf(floor)+getString(R.string.req_floor_fill));
        floorBtn.setTextSize(UIComBack.getInstance().getFitScreenTextSize22());

        floorBtn.setGravity(Gravity.CENTER);
        floorBtn.setBackgroundResource(R.drawable.btn_selector);
        floorBtn.setTextColor(Color.parseColor("#ffffff"));
        floorBtn.setTag("bm"+floor);
        floorBtn.setOnClickListener(m_BtnOnClickListener);
        tableRow.addView(floorBtn,params);

        m_list_btn_floor.add(floorBtn);
    }

    private void setSlotInfoBtn(int slotNo,int stock,int capacity,int status,LinearLayout tableRow) {


        RelativeLayout mReLayout = new RelativeLayout(this);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIComBack.getInstance().getRepGoodsPxSlotWidth(), LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(2, 0, 2, 0);
        params.gravity = Gravity.CENTER;

        Button slotNoBtn = new Button(this);
       // slotNoBtn.setText("货道"+slotNo+"\n"+"库存："+stock+"\n容量："+capacity);
        slotNoBtn.setTextSize(UIComBack.getInstance().getFitScreenTextSize18());
        slotNoBtn.setGravity(Gravity.CENTER_HORIZONTAL);
        slotNoBtn.setText(getSpanPaySlotString(slotNo,stock,capacity,status));
//        slotNoBtn.setPadding(3, 3, 3, 3);
//        slotNoBtn.setWidth(90);
//        slotNoBtn.setHeight(150);
        if (status != 0) {
            slotNoBtn.setBackgroundResource(R.color.red);
        } else if (stock <= 0) {
            slotNoBtn.setBackgroundResource(R.color.darkorange);
        }
        else {
            slotNoBtn.setBackgroundResource(R.drawable.btn_selector_no_circle);
        }

        slotNoBtn.setTextColor(Color.parseColor("#ffffff"));

        slotNoBtn.setTag("st"+slotNo);

        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, UIComBack.getInstance().getRepGoodsPxSlotTextHeight());
        mReLayout.addView(slotNoBtn, params1);
        m_list_btn_slot.add(slotNoBtn);


        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(UIComBack.getInstance().getRepGoodsPxAddSubWidth(), UIComBack.getInstance().getRepGoodsPxAddSubHeight());
        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        Button subBtn = new Button(this);
        subBtn.setText("-");
        subBtn.setTextColor(Color.parseColor("#ffffff"));
        subBtn.setTextSize(UIComBack.getInstance().getFitScreenTextSize24());
        subBtn.setBackgroundResource(R.drawable.btn_selector_no_circle);
        subBtn.setTag("sb"+slotNo);
        subBtn.setGravity(Gravity.CENTER);
        subBtn.setOnClickListener(m_BtnOnClickListener);
        //subBtn.setAlpha(0.6f);
        mReLayout.addView(subBtn, params2);

        m_list_btn_ad_sb.add(subBtn);

        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(UIComBack.getInstance().getRepGoodsPxAddSubWidth(), UIComBack.getInstance().getRepGoodsPxAddSubHeight());
        params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        Button addBtn = new Button(this);
        addBtn.setText("+");
        addBtn.setTextSize(UIComBack.getInstance().getFitScreenTextSize24());
        addBtn.setTextColor(Color.parseColor("#ffffff"));
        addBtn.setBackgroundResource(R.drawable.btn_selector_no_circle);
        addBtn.setTag("ad"+slotNo);
        addBtn.setGravity(Gravity.CENTER);
        addBtn.setOnClickListener(m_BtnOnClickListener);
       // addBtn.setAlpha(0.6f);
        mReLayout.addView(addBtn, params3);

        m_list_btn_ad_sb.add(addBtn);

        tableRow.addView(mReLayout,params);
    }

    private void setSlotData(Button btn, int slotNo,int stock,int capcity,int status) {
        if (btn != null) {
            btn.setText(getSpanPaySlotString(slotNo,stock,capcity,status));
        }
    }

    private SpannableStringBuilder getSpanPaySlotString(int slotNo,int stock,int capcity,int status) {
        if (null == m_stringBuilder) {
            m_stringBuilder = new SpannableStringBuilder();
        }

        if (null == m_textSizeSpan) {
            m_textSizeSpan = new AbsoluteSizeSpan(UIComBack.getInstance().getFitScreenTextSize26());
        }
        m_stringBuilder.clear();
        m_stringBuilder.clearSpans();
        m_stringBuilder.append(String.valueOf(slotNo));
        int end = m_stringBuilder.length();
        m_stringBuilder.append("\n");
      //  m_stringBuilder.append("\n");
        if ((0 == status) && (stock > 0)) {
            m_stringBuilder.append("\n");
        }
        m_stringBuilder.append(getString(R.string.req_stock));
        m_stringBuilder.append(String.valueOf(stock));
        m_stringBuilder.append("\n");
        m_stringBuilder.append(getString(R.string.req_capacity));
        m_stringBuilder.append(String.valueOf(capcity));
        if (0 != status) {
            m_stringBuilder.append("\n");
            m_stringBuilder.append(getString(R.string.req_fault));
            m_stringBuilder.append(String.valueOf(status));
        } else if (stock <= 0) {
            m_stringBuilder.append("\n");
            m_stringBuilder.append(getString(R.string.req_no_goods));
        } else {

        }

        m_stringBuilder.setSpan(m_textSizeSpan, 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return m_stringBuilder;
    }

    private void setCabinet(List<GropInfoBack> GropList) {
        if ((null == GropList) || (GropList.size() < 2)) {
            return;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(1, 0, 1, 0);
        params.weight = (1 * 1.0f) / (GropList.size());
        params.gravity = Gravity.CENTER;

        if (null == m_list_btn_cb) {
            m_list_btn_cb = new ArrayList<Button>();
        }
        m_list_btn_cb.clear();
        for (GropInfoBack info:GropList) {
            Button cbBtn = new Button(this);
            cbBtn.setTextSize(UIComBack.getInstance().getFitScreenTextSize26());
            cbBtn.setTextColor(Color.parseColor("#ffffff"));
            cbBtn.setText(info.getShowText());
            cbBtn.setBackgroundResource(R.drawable.btn_selector_no_circle);
            cbBtn.setTag("cb"+info.getID());
            cbBtn.setOnClickListener(m_BtnOnClickListener);
            rep_cabinet.addView(cbBtn,params);
            m_list_btn_cb.add(cbBtn);
        }
    }

    private void setCabinetSelected(String btnTag) {
        if ((null == m_list_btn_cb) || (m_list_btn_cb.size() < 2)) {
            return;
        }
        for (Button btn:m_list_btn_cb) {
            if (btnTag.equals(btn.getTag())) {
                btn.setAlpha(0.6f);
            } else {
                btn.setAlpha(1.0f);
            }
        }
    }

    private void appendNewRow(final LinearLayout table,List<Coil_info> coilInfoList) {
        table.removeAllViews();
        table.removeAllViewsInLayout();

        if (null == m_list_btn_floor) {
            m_list_btn_floor = new ArrayList<Button>();
        } else {
            for (Button btn: m_list_btn_floor) {
                btn.setOnClickListener(null);
            }
        }
        m_list_btn_floor.clear();

        if (null == m_list_btn_ad_sb) {
            m_list_btn_ad_sb = new ArrayList<Button>();
        } else {
            for (Button btn: m_list_btn_ad_sb) {
                btn.setOnClickListener(null);
            }
        }
        m_list_btn_ad_sb.clear();


        if (null == m_list_btn_slot) {
            m_list_btn_slot = new ArrayList<Button>();
        }
        m_list_btn_slot.clear();

        if ((null == coilInfoList) || (coilInfoList.size() < 1)) {
            return;
        }

        int numPerFloor = Integer.valueOf(TcnShareUseData.getInstance().getPerFloorNumber());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 3, 2, 3);
        LinearLayout row = new LinearLayout(this);

        int sIndex = 0;
        Coil_info mInfo = null;
        for (int i = 0; i < coilInfoList.size(); i++) {
            mInfo = coilInfoList.get(i);
            if ((mInfo.getCoil_id() % 100) > (numPerFloor * 1)) {
                sIndex = i;
                break;
            }

            if (0 == i) {
                setFloorBtn(1,row);
            }
            setSlotInfoBtn(mInfo.getCoil_id(),mInfo.getExtant_quantity(),mInfo.getCapacity(),mInfo.getWork_status(),row);
            sIndex = -1;
        }

        table.addView(row,params);

        if (sIndex < 1) {
            return;
        }

        row = new LinearLayout(this);

        for (int i = sIndex; i < coilInfoList.size(); i++) {
            mInfo = coilInfoList.get(i);

            if ((mInfo.getCoil_id() % 100) > (numPerFloor * 2)) {
                sIndex = i;
                break;
            }
            if (sIndex == i) {
                setFloorBtn(2,row);
            }
            setSlotInfoBtn(mInfo.getCoil_id(),mInfo.getExtant_quantity(),mInfo.getCapacity(),mInfo.getWork_status(),row);
            sIndex = -1;
        }
        table.addView(row,params);

        if (sIndex < 1) {
            return;
        }

        row = new LinearLayout(this);

        for (int i = sIndex; i < coilInfoList.size(); i++) {
            mInfo = coilInfoList.get(i);

            if ((mInfo.getCoil_id() % 100) > (numPerFloor * 3)) {
                sIndex = i;
                break;
            }
            if (sIndex == i) {
                setFloorBtn(3,row);
            }
            setSlotInfoBtn(mInfo.getCoil_id(),mInfo.getExtant_quantity(),mInfo.getCapacity(),mInfo.getWork_status(),row);
            sIndex = -1;
        }
        table.addView(row,params);

        if (sIndex < 1) {
            return;
        }

        row = new LinearLayout(this);

        for (int i = sIndex; i < coilInfoList.size(); i++) {
            mInfo = coilInfoList.get(i);
            if ((mInfo.getCoil_id() % 100) > (numPerFloor * 4)) {
                sIndex = i;
                break;
            }
            if (sIndex == i) {
                setFloorBtn(4,row);
            }
            setSlotInfoBtn(mInfo.getCoil_id(),mInfo.getExtant_quantity(),mInfo.getCapacity(),mInfo.getWork_status(),row);
            sIndex = -1;
        }
        table.addView(row,params);

        if (sIndex < 1) {
            return;
        }

        row = new LinearLayout(this);

        for (int i = sIndex; i < coilInfoList.size(); i++) {
            mInfo = coilInfoList.get(i);
            if ((mInfo.getCoil_id() % 100) > (numPerFloor * 5)) {
                sIndex = i;
                break;
            }
            if (sIndex == i) {
                setFloorBtn(5,row);
            }
            setSlotInfoBtn(mInfo.getCoil_id(),mInfo.getExtant_quantity(),mInfo.getCapacity(),mInfo.getWork_status(),row);
            sIndex = -1;
        }
        table.addView(row,params);

        if (sIndex < 1) {
            return;
        }

        row = new LinearLayout(this);

        for (int i = sIndex; i < coilInfoList.size(); i++) {
            mInfo = coilInfoList.get(i);
            if ((mInfo.getCoil_id() % 100) > (numPerFloor * 6)) {
                sIndex = i;
                break;
            }
            if (sIndex == i) {
                setFloorBtn(6,row);
            }
            setSlotInfoBtn(mInfo.getCoil_id(),mInfo.getExtant_quantity(),mInfo.getCapacity(),mInfo.getWork_status(),row);
            sIndex = -1;
        }
        table.addView(row,params);

        if (sIndex < 1) {
            return;
        }

        row = new LinearLayout(this);

        for (int i = sIndex; i < coilInfoList.size(); i++) {
            mInfo = coilInfoList.get(i);
            if ((mInfo.getCoil_id() % 100) > (numPerFloor * 7)) {
                sIndex = i;
                break;
            }
            if (sIndex == i) {
                setFloorBtn(7,row);
            }
            setSlotInfoBtn(mInfo.getCoil_id(),mInfo.getExtant_quantity(),mInfo.getCapacity(),mInfo.getWork_status(),row);
            sIndex = -1;
        }
        table.addView(row,params);

        if (sIndex < 1) {
            return;
        }
        row = new LinearLayout(this);

        for (int i = sIndex; i < coilInfoList.size(); i++) {
            mInfo = coilInfoList.get(i);
            if ((mInfo.getCoil_id() % 100) > (numPerFloor * 8)) {
                sIndex = i;
                break;
            }
            if (sIndex == i) {
                setFloorBtn(8,row);
            }
            setSlotInfoBtn(mInfo.getCoil_id(),mInfo.getExtant_quantity(),mInfo.getCapacity(),mInfo.getWork_status(),row);
            sIndex = -1;
        }
        table.addView(row,params);
    }

    private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
    private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

        @Override
        public void onClick(View v, int buttonId) {
            if (Titlebar.BUTTON_ID_BACK == buttonId) {
                StockReplenishActivity.this.finish();
            }
        }
    }

    private BtnOnClickListener m_BtnOnClickListener = new BtnOnClickListener();
    private class BtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String mTag = (String)v.getTag();
            if (mTag.equals("reqAll")) {
                TcnVendIF.getInstance().reqFillStockToCapacity(m_list_aliveCoil);
            } else if (mTag.equals("reqClean")) {
                if ((m_list_aliveCoil != null) && (m_list_aliveCoil.size() > 0)) {
                    TcnVendIF.getInstance().reqClearSlotFaults((m_list_aliveCoil.get(0)).getCoil_id(),(m_list_aliveCoil.get(m_list_aliveCoil.size() - 1)).getCoil_id());
                    TcnVendIF.getInstance().reqCleanDriveFaults(m_mchGrpId);
                    TcnVendIF.getInstance().reqCleanMachine(m_mchGrpId);
                }
            } else if (mTag.startsWith("cb")) {
                setCabinetSelected(mTag);
                int iCab = Integer.valueOf(mTag.substring(2).trim());
                m_mchGrpId = iCab;
                m_list_aliveCoil = UIComBack.getInstance().getAliveCoil(iCab);
                m_listData_count = m_list_aliveCoil.size();
                appendNewRow(m_table,m_list_aliveCoil);
            } else if (mTag.startsWith("bm")) {
                int floor = Integer.valueOf(mTag.substring(2).trim());
                int numPerFloor = Integer.valueOf(TcnShareUseData.getInstance().getPerFloorNumber());
                TcnVendIF.getInstance().reqFillStockToCapacity(numPerFloor*(floor - 1) + 1,numPerFloor*floor);
            } else if (mTag.startsWith("sb")) {
                int slotNo = Integer.valueOf(mTag.substring(2).trim());
                TcnVendIF.getInstance().reqSubStock(slotNo);
            } else if (mTag.startsWith("ad")) {
                int slotNo = Integer.valueOf(mTag.substring(2).trim());
                TcnVendIF.getInstance().reqAddStock(slotNo);
            }
            else {

            }
        }
    }

    private VendListener m_vendListener = new VendListener();
    private class VendListener implements TcnVendIF.VendEventListener {
        @Override
        public void VendEvent(VendEventInfo cEventInfo) {
            switch (cEventInfo.m_iEventID) {
                case TcnVendEventID.ADD_STOCK:
                    setBtnStock(m_list_btn_slot,"st"+cEventInfo.m_lParam1,cEventInfo.m_lParam2,TcnVendIF.getInstance().getCoilInfo(cEventInfo.m_lParam1));
                    break;
                case TcnVendEventID.SUB_STOCK:
                    setBtnStock(m_list_btn_slot,"st"+cEventInfo.m_lParam1,cEventInfo.m_lParam2,TcnVendIF.getInstance().getCoilInfo(cEventInfo.m_lParam1));
                    break;
                case TcnVendEventID.FILL_STOCK_TO_CAPACITY_ALL:
                  //  setBtnStock(m_list_btn_slot,cEventInfo.m_lParam1,cEventInfo.m_lParam2);
                    break;
                case TcnVendEventID.FILL_STOCK_TO_CAPACITY:
                    setBtnStock(m_list_btn_slot,cEventInfo.m_lParam1,cEventInfo.m_lParam2);
                    break;
                case TcnVendEventID.CMD_CLEAR_SLOT_FAULTS:
                    setBtnStock(m_list_btn_slot,cEventInfo.m_lParam1,cEventInfo.m_lParam2);
                    break;
                default:
                    break;
            }
        }
    }
}
