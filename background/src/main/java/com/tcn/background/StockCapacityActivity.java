package com.tcn.background;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonBatchSelect;
import com.tcn.uicommon.button.ButtonEdit;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */
public class StockCapacityActivity extends TcnMainActivity {
    private static final String TAG = "StockCapacityActivity";

    private static int m_listData_count = 0;
    private int m_iIndexPage = 0;
    private int m_iTotalPageNum = 0;
    private boolean isPageAnim = false;
    private Titlebar m_Titlebar = null;
    protected ButtonEdit goods_modify_fill_stock = null;
    private ButtonBatchSelect goods_modify_stock = null;
    private ButtonBatchSelect goods_modify_capacity = null;
    private ViewPager m_displayvp;
    private TextView m_TextViewPageIndex = null;
    private SlotPagerAdapter m_Adapter;
    private List<Coil_info> m_list_aliveCoil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.goods_stockcapaticy_layout);
        TcnVendIF.getInstance().LoggerDebug(TAG, "StockCapacityActivity onCreate()");

        m_Titlebar = (Titlebar) findViewById(R.id.goods_stockcapacity_titlebar);
        m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
        m_Titlebar.setButtonName(getString(R.string.menu_slot_modify_stockcapacity));
        m_Titlebar.setTitleBarListener(m_TitleBarListener);

        goods_modify_fill_stock = (ButtonEdit) findViewById(R.id.goods_modify_fill_stock);
        goods_modify_fill_stock.setButtonType(ButtonEdit.BUTTON_TYPE_QUERY);
        goods_modify_fill_stock.setButtonName(R.string.slot_info_fill_stock_tips);
        goods_modify_fill_stock.setButtonQueryText(R.string.slot_info_fill_stock);
        goods_modify_fill_stock.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(18));
        goods_modify_fill_stock.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(18));
        goods_modify_fill_stock.setButtonQueryTextColor("#ffffff");
        goods_modify_fill_stock.setButtonQueryBackground(R.drawable.btn_selector);
        goods_modify_fill_stock.setButtonListener(m_ButtonClickListener);

        goods_modify_stock = (ButtonBatchSelect) findViewById(R.id.goods_modify_stock);
        // goods_modify_replsh.setButtonName("上货:");
        goods_modify_stock.setButtonType(ButtonBatchSelect.BUTTON_TYPE_TITLE_AND_NO_RIGHT_BTN);
        goods_modify_stock.setButtonLeft(getString(R.string.ensureofalter));
        goods_modify_stock.setButtonName(getString(R.string.slot_info_modify_input_stock));
        goods_modify_stock.setEditTitleInputType(InputType.TYPE_CLASS_NUMBER);
        goods_modify_stock.setButtonListener(m_SlotButtonListener);

        goods_modify_capacity = (ButtonBatchSelect) findViewById(R.id.goods_modify_capacity);
        // goods_modify_replsh.setButtonName("上货:");
        goods_modify_capacity.setButtonType(ButtonBatchSelect.BUTTON_TYPE_TITLE_AND_NO_RIGHT_BTN);
        goods_modify_capacity.setButtonLeft(getString(R.string.ensureofalter));
        goods_modify_capacity.setButtonName(getString(R.string.slot_info_modify_input_capacity));
        goods_modify_capacity.setEditTitleInputType(InputType.TYPE_CLASS_NUMBER);
        goods_modify_capacity.setButtonListener(m_SlotButtonListener);

        m_displayvp = (ViewPager) findViewById(R.id.goods_stockcapacity_pager);
        if (null == m_displayvp) {
            TcnVendIF.getInstance().LoggerError(TAG, "onCreate() is null");
            return;
        }
        m_TextViewPageIndex = (TextView) findViewById(R.id.goods_stockcapacity_page_tips);

        initViewData();
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
        super.onDestroy();
        if (m_Titlebar != null) {
            m_Titlebar.removeButtonListener();
            m_Titlebar = null;
        }
        if (goods_modify_fill_stock != null) {
            goods_modify_fill_stock.removeButtonListener();
            goods_modify_fill_stock = null;
        }
        if (goods_modify_stock != null) {
            goods_modify_stock.removeButtonListener();
            goods_modify_stock = null;
        }
        if (goods_modify_capacity != null) {
            goods_modify_capacity.removeButtonListener();
            goods_modify_capacity = null;
        }
        if (m_displayvp != null) {
            m_displayvp.clearAnimation();
            m_displayvp.setAdapter(null);
            m_displayvp.setOnPageChangeListener(null);
            m_displayvp = null;
        }
        m_displayvp = null;
        m_Adapter = null;
        m_TextViewPageIndex = null;
        m_list_aliveCoil = null;
        m_TitleBarListener = null;
        m_ButtonClickListener = null;
        m_SlotButtonListener = null;
        m_vendListener = null;
    }

    private void initViewData() {
        m_Adapter = new SlotPagerAdapter();
        m_displayvp.setAdapter(m_Adapter);
        m_Adapter.notifyDataSetChanged();
        m_displayvp.setCurrentItem(m_iIndexPage);
        m_displayvp.setOnPageChangeListener(new PageChangeListener());

    }

    private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
    private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

        @Override
        public void onClick(View v, int buttonId) {
            if (Titlebar.BUTTON_ID_BACK == buttonId) {
                StockCapacityActivity.this.finish();
            }
        }
    }

    protected ButtonClickListener m_ButtonClickListener= new ButtonClickListener();
    protected class ButtonClickListener implements ButtonEdit.ButtonListener {
        @Override
        public void onClick(View v, int buttonId) {
            if (null == v) {
                return;
            }
            int id = v.getId();
            if (R.id.goods_modify_fill_stock == id) {
                TcnVendIF.getInstance().reqFillStockToCapacityAll();
            }
        }
    }

    /**
     * 设置页面切换时的动画
     * @author Administrator
     *
     */
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            m_iIndexPage = m_displayvp.getCurrentItem();
            m_TextViewPageIndex.setText(m_iIndexPage+1+"/"+m_iTotalPageNum);
            m_Adapter.notifyDataSetChanged();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (0 == state) {
                isPageAnim = false;
            } else {
                isPageAnim = true;
            }
        }
    }

    private class SlotPagerAdapter extends PagerAdapter {
        private View mCacheView = null;

        @Override
        public int getCount() {
            return m_iTotalPageNum;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView((View)object);
            mCacheView = (View)object;
            if (mCacheView != null) {
                GridView mGrid = (GridView) mCacheView.findViewById(R.id.gridv);
                if (mGrid != null) {
                    mGrid.setAdapter(null);
                    mGrid.setOnItemClickListener(null);
                }
            }
        }
        @Override
        public Object instantiateItem(View container, int position) {
            //    TcnVendIF.getInstance().LoggerDebug(TAG, "StockCapacityActivity instantiateItem() m_listData_count: "+m_listData_count+" position: "+position);
            View mView = null;
            if (mCacheView != null) {
                mView = mCacheView;
            } else {
                mView = LayoutInflater.from(StockCapacityActivity.this).inflate(R.layout.displaygrid, null);
            }
            GridView mGridView = (GridView) mView.findViewById(R.id.gridv);
            mGridView.setAdapter(new GridviewAdapter(position));
//            mGridView.setOnItemClickListener(m_itemClickListener);
//            mGridView.setOnItemLongClickListener(m_itemLongClickListener);
            ((ViewPager)container).addView(mView);
            mCacheView = null;
            return mView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public void setData(List<Coil_info> listInfo) {
            if (null == listInfo) {
                return;
            }
            View mView = null;
            for (int i = 0; i < listInfo.size(); i++) {
                mView = LayoutInflater.from(StockCapacityActivity.this).inflate(R.layout.displaygrid, null);
                GridView mGridView = (GridView) mView.findViewById(R.id.gridv);
            }
        }
    }

    private class GridviewAdapter extends BaseAdapter {
        private int iCount = 0;
        public GridviewAdapter(int position) {
            super();
            iCount = TcnCommon.coil_num * position;
        }

        @Override
        public int getCount() {
            if (m_listData_count < TcnCommon.coil_num) {
                return m_listData_count;
            }
            return TcnCommon.coil_num;
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

            if((position >= TcnCommon.coil_num)){
                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() position: " + position);
                return null;
            }

            int iPosition = iCount + position;

            ViewHolder holder = null;
            if(null == convertView) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(StockCapacityActivity.this).inflate(R.layout.goods_item,null);
                holder.mImageGoods = (ImageView) convertView.findViewById(R.id.item_goods_img);
                holder.mText = (TextView) convertView.findViewById(R.id.item_goods_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            if ((position < TcnCommon.coil_num) && (iPosition >= m_listData_count)) {
                holder.mText.setText("");
                holder.mText.setBackgroundResource(0);
                holder.mImageGoods.setImageDrawable(null);
                holder.mImageGoods.setImageResource(0);
                holder.mImageGoods.setImageBitmap(null);
                holder.mImageGoods.setVisibility(View.GONE);
                return convertView;
            }
            Coil_info mInfo = null;
            try {
                mInfo = m_list_aliveCoil.get(iPosition);
            } catch (Exception e) {
                // TODO: handle exception
                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() e: "+e);
            }
            if (null == mInfo) {
                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() mInfo is null");
                return null;
            }
            holder.mText.setText(getString(R.string.ui_aisle)+mInfo.getCoil_id()+"\n"+getString(R.string.ui_price)+mInfo.getPar_price()+"\n"
                    + getString(R.string.ui_stock)+mInfo.getExtant_quantity() + "\n"+getString(R.string.ui_capacity)+mInfo.getCapacity());
            if("".equals(mInfo.getImg_url()) || (null == mInfo.getImg_url())){
                holder.mImageGoods.setImageResource(R.mipmap.default_ticket_poster_pic);
            }else{
                TcnVendIF.getInstance().displayImage(mInfo.getImg_url(), holder.mImageGoods,R.mipmap.default_ticket_poster_pic);
            }
            return convertView;
        }
    }
    private static class ViewHolder {
        //	public RelativeLayout mLayout;
        public ImageView mImageGoods;
        //  public ImageView mImageForbid;
        public TextView mText;
    }

    private SlotButtonListener m_SlotButtonListener = new SlotButtonListener();
    private class SlotButtonListener implements ButtonBatchSelect.ButtonListener {
        @Override
        public void onClick(View v, int buttonId) {
            int id = v.getId();
            if (id == R.id.goods_modify_stock) {
                if (buttonId == ButtonBatchSelect.BUTTON_ID_LEFT) {

                    if (m_list_aliveCoil != null) {
                        m_listData_count = 0;
                        m_list_aliveCoil.clear();
                        if (m_Adapter != null) {
                            m_Adapter.notifyDataSetChanged();
                        }
                    }

                    String stock = goods_modify_stock.getTitleEditText();
                    if (!TcnVendIF.getInstance().isDigital(stock)) {
                        TcnUtility.getToast(StockCapacityActivity.this, getString(R.string.slot_info_modify_input_stock));
                        return;
                    }
                    String start = goods_modify_stock.getStartEditText();
                    if (!TcnVendIF.getInstance().isDigital(start)) {
                        TcnUtility.getToast(StockCapacityActivity.this, getString(R.string.slot_info_modify_tips_slot_err));
                        return;
                    }

                    String end = goods_modify_stock.getEndEditText();
                    if (!TcnVendIF.getInstance().isDigital(end)) {
                        end = start;
                    }

                    TcnVendIF.getInstance().reqWriteSlotStock(Integer.valueOf(start),Integer.valueOf(end),Integer.valueOf(stock));

                }
            } else if (id == R.id.goods_modify_capacity) {
                if (buttonId == ButtonBatchSelect.BUTTON_ID_LEFT) {
                    if (m_list_aliveCoil != null) {
                        m_listData_count = 0;
                        m_list_aliveCoil.clear();
                        if (m_Adapter != null) {
                            m_Adapter.notifyDataSetChanged();
                        }
                    }

                    String capacity = goods_modify_capacity.getTitleEditText();
                    if (!TcnVendIF.getInstance().isDigital(capacity)) {
                        TcnUtility.getToast(StockCapacityActivity.this, getString(R.string.slot_info_modify_input_capacity));
                        return;
                    }
                    String start = goods_modify_capacity.getStartEditText();
                    if (!TcnVendIF.getInstance().isDigital(start)) {
                        TcnUtility.getToast(StockCapacityActivity.this, getString(R.string.slot_info_modify_tips_slot_err));
                        return;
                    }

                    String end = goods_modify_capacity.getEndEditText();
                    if (!TcnVendIF.getInstance().isDigital(end)) {
                        end = start;
                    }

                    TcnVendIF.getInstance().reqWriteSlotCapacity(Integer.valueOf(start),Integer.valueOf(end),Integer.valueOf(capacity));

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
                case TcnVendEventID.UPDATE_SLOTNO_EXTQUANTITY:
                case TcnVendEventID.UPDATE_SLOTNO_CAPACITY:
                    m_list_aliveCoil = UIComBack.getInstance().getAliveCoil(cEventInfo.m_lParam1,cEventInfo.m_lParam2);
                    m_listData_count = m_list_aliveCoil.size();

                    m_iTotalPageNum = UIComBack.getInstance().getTotalPage(m_listData_count);
                    if (m_Adapter != null) {
                        m_Adapter = new SlotPagerAdapter();
                        m_displayvp.setAdapter(m_Adapter);
                        m_Adapter.notifyDataSetChanged();
                    }
                    TcnUtility.getToast(StockCapacityActivity.this, getString(R.string.tip_modify_success));
                    break;
                case TcnVendEventID.FILL_STOCK_TO_CAPACITY_ALL:
                    m_list_aliveCoil = TcnVendIF.getInstance().getAliveCoil();
                    m_listData_count = TcnVendIF.getInstance().getAliveCoilCount();
                    m_iTotalPageNum = UIComBack.getInstance().getTotalPage(m_listData_count);
                    if (m_Adapter != null) {
                        m_Adapter = new SlotPagerAdapter();
                        m_displayvp.setAdapter(m_Adapter);
                        m_Adapter.notifyDataSetChanged();
                    }
                    TcnUtility.getToast(StockCapacityActivity.this, getString(R.string.tip_modify_success));
                    break;
                default:
                    break;
            }
        }
    }
}
