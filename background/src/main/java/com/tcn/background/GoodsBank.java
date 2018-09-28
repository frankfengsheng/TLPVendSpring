package com.tcn.background;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendEventResultID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.dialog.DialogInput;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */
public class GoodsBank extends TcnMainActivity {
    private static final String TAG = "GoodsBank";
    private static int m_listData_count = 0;
    private int m_iIndexPage = 0;
    private int m_iTotalPageNum = 0;
    private boolean isPageAnim = false;
    private Titlebar m_Titlebar = null;
    private ViewPager m_displayvp;
    private TextView m_TextViewPageIndex = null;
    private GoodsPagerAdapter m_Adapter;
    private List<Goods_info> m_list_aliveGoodsAll;
    private DialogInput m_DialogInputDelete = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_bank_layout);
        TcnVendIF.getInstance().LoggerDebug(TAG, "GoodsBank onCreate()");

        m_Titlebar = (Titlebar) findViewById(R.id.goods_manage_titlebar);
        m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
        m_Titlebar.setButtonName(R.string.menu_commodity);
        m_Titlebar.setTitleBarListener(m_TitleBarListener);

        m_displayvp = (ViewPager) findViewById(R.id.goods_view_pager);
        if (null == m_displayvp) {
            TcnVendIF.getInstance().LoggerError(TAG, "onCreate() is null");
            return;
        }
        m_TextViewPageIndex = (TextView) findViewById(R.id.goods_page_tips);

        m_DialogInputDelete = new DialogInput(GoodsBank.this);
        m_DialogInputDelete.setButtonType(DialogInput.BUTTON_TYPE_NO_INPUT);
        m_DialogInputDelete.setButtonTiTle(R.string.aisle_dis_sure_to_delete);
        m_DialogInputDelete.setButtonListener(m_DialogInputDeleteListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TcnVendIF.getInstance().registerListener(m_vendListener);
        initViewData();
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
        if (m_displayvp != null) {
            m_displayvp.clearAnimation();
            m_displayvp.setAdapter(null);
            m_displayvp.setOnPageChangeListener(null);
            m_displayvp = null;
        }
        m_Adapter = null;
        m_TextViewPageIndex = null;
        if (m_DialogInputDelete != null) {
            m_DialogInputDelete.setButtonListener(null);
            m_DialogInputDelete = null;
        }
        m_list_aliveGoodsAll = null;
        m_DialogInputDeleteListener = null;
        m_itemClickListener = null;
        m_itemLongClickListener = null;
        m_vendListener = null;
    }

    private void initViewData() {
        m_list_aliveGoodsAll = TcnVendIF.getInstance().getAliveGoodsAll();
        m_listData_count = TcnVendIF.getInstance().getAliveGoodsCountAll()+1;
        m_iTotalPageNum = UIComBack.getInstance().getTotalPage(m_listData_count);
        m_Adapter = new GoodsPagerAdapter();
        m_displayvp.setAdapter(m_Adapter);
        m_Adapter.notifyDataSetChanged();
        m_displayvp.setCurrentItem(m_iIndexPage);
        m_displayvp.setOnPageChangeListener(new PageChangeListener());

    }

    /**
     * 切换到上一页
     */
    public void goods_prepage(){
        if (isPageAnim) {
            return;
        }
        m_iIndexPage--;
        if(m_iIndexPage < 0){
            m_iIndexPage = m_iTotalPageNum - 1;
        }
        m_displayvp.setCurrentItem(m_iIndexPage);
        //myPager.notifyDataSetChanged();
    }

    /**
     * 切换到下一页
     */
    public void goods_nextpage(){
        if (isPageAnim) {
            return;
        }
        m_iIndexPage++;
        if(m_iIndexPage >= m_iTotalPageNum){
            m_iIndexPage = 0;
        }
        m_displayvp.setCurrentItem(m_iIndexPage);
        //myPager.notifyDataSetChanged();
    }


    private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
    private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

        @Override
        public void onClick(View v, int buttonId) {
            if (Titlebar.BUTTON_ID_BACK == buttonId) {
                GoodsBank.this.finish();
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

    private class GoodsPagerAdapter extends PagerAdapter {
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
            //    TcnVendIF.getInstance().LoggerDebug(TAG, "GoodsBank instantiateItem() m_listData_count: "+m_listData_count+" position: "+position);
            View mView = null;
            if (mCacheView != null) {
                mView = mCacheView;
            } else {
                mView = LayoutInflater.from(GoodsBank.this).inflate(R.layout.displaygrid, null);
            }
            GridView mGridView = (GridView) mView.findViewById(R.id.gridv);
            mGridView.setAdapter(new GridviewAdapter(position));
            mGridView.setOnItemClickListener(m_itemClickListener);
            mGridView.setOnItemLongClickListener(m_itemLongClickListener);
            ((ViewPager)container).addView(mView);
            mCacheView = null;
            return mView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public void setData(List<Goods_info> listInfo) {
            if (null == listInfo) {
                return;
            }
            //           if (null == m_viewList) {
            //               m_viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
            //           }
            //           m_viewList.clear();
            View mView = null;
            for (int i = 0; i < listInfo.size(); i++) {
                mView = LayoutInflater.from(GoodsBank.this).inflate(R.layout.displaygrid, null);
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
//            if ((null == m_list_aliveGoodsAll)) {
//                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() m_list_aliveGoodsAll is null or empty");
//                return null;
//            }
            if((position >= TcnCommon.coil_num)){
                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() position: " + position);
                return null;
            }

            int iPosition = iCount + position;

            ViewHolder holder = null;
            if(null == convertView) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(GoodsBank.this).inflate(R.layout.goods_item,null);
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
            if (m_listData_count == (iPosition+1)) {
                holder.mImageGoods.setImageResource(R.mipmap.add);
                holder.mText.setText("");
                return convertView;
            }
            Goods_info mInfo = null;
            try {
                mInfo = m_list_aliveGoodsAll.get(iPosition);
            } catch (Exception e) {
                // TODO: handle exception
                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() e: "+e);
            }
            if (null == mInfo) {
                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() mInfo is null");
                return null;
            }
            holder.mText.setText(getString(R.string.name)+mInfo.getGoods_name()+"\n"+getString(R.string.ui_price)+mInfo.getGoods_price()+"\n"+ getString(R.string.ui_aisle)+mInfo.getGoodsSlotMap());
            if("".equals(mInfo.getGoods_url()) || (null == mInfo.getGoods_url())){
                holder.mImageGoods.setImageResource(R.mipmap.default_ticket_poster_pic);
            }else{
                TcnVendIF.getInstance().displayImage(mInfo.getGoods_url(), holder.mImageGoods,R.mipmap.default_ticket_poster_pic);
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

    private DialogInputDeleteListener m_DialogInputDeleteListener = new DialogInputDeleteListener();
    private class DialogInputDeleteListener implements DialogInput.ButtonListener {
        private String m_GoodsId = "";
        public void setGoodsId(String gId) {
            m_GoodsId = gId;
        }

        public String getGoodsId() {
            return m_GoodsId;
        }

        @Override
        public void onClick(int buttonId, String firstData, String secondData) {
            if (buttonId ==  DialogInput.BUTTON_ID_SURE) {
                TcnVendIF.getInstance().reqDeleteGoodsId(m_GoodsId);
            }
            if (m_DialogInputDelete != null) {
                m_DialogInputDelete.dismiss();
            }
        }
    }

    private GoodsItemCilckListener m_itemClickListener = new GoodsItemCilckListener();
    private class GoodsItemCilckListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            m_iIndexPage = m_displayvp.getCurrentItem();
            String mStr = TcnShareUseData.getInstance().getScreenInch();
            if (mStr.equals(TcnCommon.SCREEN_INCH[1])) {
                TcnUtility.getToast(GoodsBank.this, getString(R.string.tip_not_support_temp));
                return;
            }
            if((position < TcnCommon.coil_num) && (m_iIndexPage* TcnCommon.coil_num + position < m_listData_count)){
                Intent in=new Intent(GoodsBank.this, GoodsEdit.class);
                if ((position + 1) >= m_listData_count) {
                    TcnVendIF.getInstance().LoggerDebug(TAG, "onItemClick  position: "+position);
                    in.putExtra("goods_edit_id", -1);
                } else {
                    if ((m_iIndexPage * TcnCommon.coil_num + position) < m_list_aliveGoodsAll.size()) {
                        Goods_info mInfo = m_list_aliveGoodsAll.get(m_iIndexPage * TcnCommon.coil_num + position);
                        int iId = mInfo.getID();
                        TcnVendIF.getInstance().LoggerDebug(TAG, "onItemClick iId: "+iId+" getGoods_url: "+mInfo.getGoods_url()+" position: "+position);
                        in.putExtra("goods_edit_id", iId);
                    } else {
                        TcnVendIF.getInstance().LoggerDebug(TAG, "onItemClick m_iIndexPage: "+m_iIndexPage+" position: "+position);
                    }

                }
                startActivity(in);
            }
        }
    }
    private GoodsItemLongCilckListener m_itemLongClickListener = new GoodsItemLongCilckListener();
    private class GoodsItemLongCilckListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            m_iIndexPage = m_displayvp.getCurrentItem();
            if((position < TcnCommon.coil_num) && (m_iIndexPage* TcnCommon.coil_num + position < m_listData_count)){
                if ((position + 1) >= m_listData_count) {

                } else {
                    if ((m_iIndexPage * TcnCommon.coil_num + position) < m_list_aliveGoodsAll.size()) {
                        Goods_info minfo = m_list_aliveGoodsAll.get(m_iIndexPage * TcnCommon.coil_num + position);
                        String strId = minfo.getGoods_id();
                        TcnVendIF.getInstance().LoggerDebug(TAG, "onItemLongClick strId: "+strId+" getGoods_url: "+minfo.getGoods_url()+" position: "+position);
                        m_DialogInputDeleteListener.setGoodsId(strId);
                        m_DialogInputDelete.show();
                    } else {
                        TcnVendIF.getInstance().LoggerDebug(TAG, "onItemLongClick m_iIndexPage: "+m_iIndexPage+" position: "+position);
                    }

                }

            }
            return true;
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
                case TcnVendEventID.QUERY_ALIVE_GOODS:
                    m_list_aliveGoodsAll = TcnVendIF.getInstance().getAliveGoodsAll();
                    m_listData_count = TcnVendIF.getInstance().getAliveGoodsCountAll()+1;

                    m_iTotalPageNum = UIComBack.getInstance().getTotalPage(m_listData_count);
                    if (m_Adapter != null) {
                        m_Adapter.notifyDataSetChanged();
                    }
                    break;
                case TcnVendEventID.DELETE_GOODS_ID:
                    TcnUtility.getToast(GoodsBank.this, cEventInfo.m_lParam4);
                    if (cEventInfo.m_lParam2 == TcnVendEventResultID.SUCCESS) {
                        m_list_aliveGoodsAll = TcnVendIF.getInstance().getAliveGoodsAll();
                        m_listData_count = TcnVendIF.getInstance().getAliveGoodsCountAll()+1;

                        m_iTotalPageNum = UIComBack.getInstance().getTotalPage(m_listData_count);
                        if (m_Adapter != null) {
                            m_Adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
