package com.tcn.background;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonEdit;
import com.tcn.uicommon.button.ButtonEditSelectD;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/25.
 */
public class ErrSlotActivity extends TcnMainActivity {
    private static final String TAG = "ErrSlotActivity";
    private static int m_listData_count = 0;
    private int m_iIndexPage = 0;
    private int m_iTotalPageNum = 0;
    private int singleitem=0;
    private boolean isPageAnim = false;
    private Titlebar m_Titlebar = null;
    protected ButtonEdit goods_clean_err_slot = null;
    private ViewPager m_displayvp;
    private TextView m_TextViewPageIndex = null;
    private SlotPagerAdapter m_Adapter;
    private List<Coil_info> m_list_faultCoil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slot_err_layout);

        m_Titlebar = (Titlebar) findViewById(R.id.slot_err_titlebar);
        m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
        m_Titlebar.setButtonName(getString(R.string.menu_fault_handle));
        m_Titlebar.setTitleBarListener(m_TitleBarListener);

        goods_clean_err_slot = (ButtonEdit) findViewById(R.id.slot_err_clean);
        goods_clean_err_slot.setButtonType(ButtonEdit.BUTTON_TYPE_QUERY);
        goods_clean_err_slot.setButtonQueryText(R.string.backgroound_clear_fault);
        goods_clean_err_slot.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(18));
        goods_clean_err_slot.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(18));
        goods_clean_err_slot.setButtonQueryTextColor("#ffffff");
        goods_clean_err_slot.setButtonQueryBackground(R.drawable.btn_selector);
        goods_clean_err_slot.setButtonListener(m_ButtonClickListener);

        m_displayvp = (ViewPager) findViewById(R.id.slot_err_pager);
        if (null == m_displayvp) {
            TcnVendIF.getInstance().LoggerError(TAG, "onCreate() is null");
            return;
        }
      //  m_TextViewPageIndex = (TextView) findViewById(R.id.goods_stockcapacity_page_tips);

        initViewData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TcnVendIF.getInstance().registerListener(m_vendListener);
        TcnVendIF.getInstance().queryAliveCoil();
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
        if (goods_clean_err_slot != null) {
            goods_clean_err_slot.removeButtonListener();
            goods_clean_err_slot = null;
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
        m_list_faultCoil = null;
        m_TitleBarListener = null;
        m_ButtonClickListener = null;
        m_vendListener = null;
    }

    private void initViewData() {
        m_Adapter = new SlotPagerAdapter();
        m_displayvp.setAdapter(m_Adapter);
        m_Adapter.notifyDataSetChanged();
        m_displayvp.setCurrentItem(m_iIndexPage);
        m_displayvp.setOnPageChangeListener(new PageChangeListener());

    }

    private void showSelectDialog(final int type, String title, final EditText v, String selectData, final String[] str) {
        if (null == str) {
            return;
        }
        int checkedItem = -1;
        if ((selectData != null) && (selectData.length() > 0)) {
            for (int i = 0; i < str.length; i++) {
                if (str[i].equals(selectData)) {
                    checkedItem = i;
                    break;
                }
            }
        }

        singleitem=0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setSingleChoiceItems(str, checkedItem, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                singleitem=which;
            }
        });
        builder.setPositiveButton(getString(R.string.backgroound_ensure), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                v.setText(str[singleitem]);
            }
        });
        builder.setNegativeButton(getString(R.string.backgroound_cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
    }

    private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
    private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

        @Override
        public void onClick(View v, int buttonId) {
            if (Titlebar.BUTTON_ID_BACK == buttonId) {
                ErrSlotActivity.this.finish();
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
            if (R.id.slot_err_clean == id) {
                TcnVendIF.getInstance().reqClearFaults();
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
            //    TcnVendIF.getInstance().LoggerDebug(TAG, "ErrSlotActivity instantiateItem() m_listData_count: "+m_listData_count+" position: "+position);
            View mView = null;
            if (mCacheView != null) {
                mView = mCacheView;
            } else {
                mView = LayoutInflater.from(ErrSlotActivity.this).inflate(R.layout.displaygrid, null);
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
                mView = LayoutInflater.from(ErrSlotActivity.this).inflate(R.layout.displaygrid, null);
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
                convertView = LayoutInflater.from(ErrSlotActivity.this).inflate(R.layout.goods_item,null);
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
                mInfo = m_list_faultCoil.get(iPosition);
            } catch (Exception e) {
                // TODO: handle exception
                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() e: "+e);
            }
            if (null == mInfo) {
                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() mInfo is null");
                return null;
            }
            holder.mText.setText(getString(R.string.ui_aisle)+mInfo.getCoil_id()+"\n"+getString(R.string.ui_price)+mInfo.getPar_price()+"\n"
                    + getString(R.string.notify_slot_err)+mInfo.getWork_status());
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
                    m_list_faultCoil = UIComBack.getInstance().getAliveCoilFaults();
                    m_listData_count = m_list_faultCoil.size();

                    m_iTotalPageNum = UIComBack.getInstance().getTotalPage(m_listData_count);
                    m_Adapter = new SlotPagerAdapter();
                    m_displayvp.setAdapter(m_Adapter);
                    m_Adapter.notifyDataSetChanged();
                    if (m_listData_count < 1) {
                        TcnUtility.getToast(ErrSlotActivity.this, getString(R.string.tip_no_fault_slots));
                    }
                    break;
                case TcnVendEventID.CMD_CLEAR_SLOT_FAULTS:
                    TcnUtility.getToast(ErrSlotActivity.this, getString(R.string.tip_clear_success));
                    break;
                default:
                    break;
            }
        }
    }
}
