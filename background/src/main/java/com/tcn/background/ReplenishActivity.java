package com.tcn.background;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonBatchSelect;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */
public class ReplenishActivity extends TcnMainActivity {
    private static final String TAG = "ReplenishActivity";
    private static int m_listData_count = 0;
    private int m_iIndexPage = 0;
    private int m_iTotalPageNum = 0;
    private boolean isPageAnim = false;
    private Titlebar m_Titlebar = null;
    private ImageView goods_replsh = null;
    private TextView goods_repl_name = null;
    private TextView goods_repl_id = null;
    private TextView goods_repl_price = null;
    private Button goods_btn_replsh = null;
    private ButtonBatchSelect goods_modify_replsh = null;
    private ViewPager m_displayvp;
    private TextView m_TextViewPageIndex = null;
    private GoodsPagerAdapter m_Adapter;
    private Goods_info goods_info = null;
    private List<Goods_info> m_list_aliveGoods;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_replenish_layout);
        TcnVendIF.getInstance().LoggerDebug(TAG, "ReplenishActivity onCreate()");
        m_Titlebar = (Titlebar) findViewById(R.id.goods_replsh_titlebar);
        m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
        m_Titlebar.setButtonName(getString(R.string.menu_slot_modify_goods));
        m_Titlebar.setTitleBarListener(m_TitleBarListener);

        goods_replsh = (ImageView) findViewById(R.id.goods_replsh);
        goods_repl_name = (TextView) findViewById(R.id.goods_repl_name);
        goods_repl_id = (TextView) findViewById(R.id.goods_repl_id);
        goods_repl_price = (TextView) findViewById(R.id.goods_repl_price);
        goods_btn_replsh = (Button) findViewById(R.id.goods_btn_replsh);
        goods_btn_replsh.setOnClickListener(m_ButtonListener);


        goods_modify_replsh = (ButtonBatchSelect) findViewById(R.id.goods_modify_replsh);
        goods_modify_replsh.setButtonType(ButtonBatchSelect.BUTTON_TYPE_TITLE_AND_NO_RIGHT_BTN);
        goods_modify_replsh.setButtonLeft(getString(R.string.ensureofalter));
        goods_modify_replsh.setButtonName(getString(R.string.slot_info_add_count_input));
        goods_modify_replsh.setEditTitleInputType(InputType.TYPE_CLASS_NUMBER);
        goods_modify_replsh.setButtonListener(m_SlotButtonListener);

        m_displayvp = (ViewPager) findViewById(R.id.goods_replsh_view_pager);
        if (null == m_displayvp) {
            TcnVendIF.getInstance().LoggerError(TAG, "onCreate() is null");
            return;
        }
        m_TextViewPageIndex = (TextView) findViewById(R.id.goods_replsh_page_tips);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TcnVendIF.getInstance().registerListener(m_vendListener);
        initViewData();
        TcnVendIF.getInstance().LoggerDebug(TAG, "ReplenishActivity onResume()");
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
        if (goods_btn_replsh != null) {
            goods_btn_replsh.setOnClickListener(null);
            goods_btn_replsh = null;
        }
        if (goods_modify_replsh != null) {
            goods_modify_replsh.removeButtonListener();
            goods_modify_replsh = null;
        }
        if (m_displayvp != null) {
            m_displayvp.clearAnimation();
            m_displayvp.setAdapter(null);
            m_displayvp.setOnPageChangeListener(null);
            m_displayvp = null;
        }
        m_Adapter = null;
        goods_replsh = null;
        goods_repl_name = null;
        goods_repl_id = null;
        m_TextViewPageIndex = null;
        goods_repl_price = null;
        goods_info = null;
        m_list_aliveGoods = null;
        m_TitleBarListener = null;
        m_ButtonListener = null;
        m_itemClickListener = null;
        m_itemLongClickListener = null;
        m_SlotButtonListener = null;
        m_vendListener = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TcnVendIF.getInstance().LoggerDebug(TAG, "-----onActivityResult() requestCode: "+requestCode+" resultCode: "+resultCode);
        if(data!=null) {
            String extra = data.getStringExtra("goods_id");
            goods_info = TcnVendIF.getInstance().getGoodsInfo(extra);
            TcnVendIF.getInstance().LoggerDebug(TAG, "-----onActivityResult() extra: "+extra+" getGoods_url: "+goods_info.getGoods_url());
            if (goods_info != null) {
                goods_repl_name.setText(getString(R.string.name)+goods_info.getGoods_name());
                goods_repl_id.setText(getString(R.string.goods_id)+goods_info.getGoods_id());
                goods_repl_price.setText(getString(R.string.ui_price)+goods_info.getGoods_price());
            }
            TcnVendIF.getInstance().displayImage(goods_info.getGoods_url(),goods_replsh,R.mipmap.default_ticket_poster_pic);
        }
    }

    private void initViewData() {
        TcnVendIF.getInstance().displayImage("",goods_replsh,R.mipmap.default_ticket_poster_pic);
        m_list_aliveGoods = TcnVendIF.getInstance().getAliveGoods();
        m_listData_count = TcnVendIF.getInstance().getAliveGoodsCount();
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
                ReplenishActivity.this.finish();
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
            //    TcnVendIF.getInstance().LoggerDebug(TAG, "ReplenishActivity instantiateItem() m_listData_count: "+m_listData_count+" position: "+position);
            View mView = null;
            if (mCacheView != null) {
                mView = mCacheView;
            } else {
                mView = LayoutInflater.from(ReplenishActivity.this).inflate(R.layout.displaygrid, null);
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
            View mView = null;
            for (int i = 0; i < listInfo.size(); i++) {
                mView = LayoutInflater.from(ReplenishActivity.this).inflate(R.layout.displaygrid, null);
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
                convertView = LayoutInflater.from(ReplenishActivity.this).inflate(R.layout.goods_item,null);
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
            Goods_info mInfo = null;
            try {
                mInfo = m_list_aliveGoods.get(iPosition);
            } catch (Exception e) {
                // TODO: handle exception
                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() e: "+e);
            }
            if (null == mInfo) {
                TcnVendIF.getInstance().LoggerError(TAG, "GridviewAdapter getView() mInfo is null");
                return null;
            }
            holder.mText.setText(getString(R.string.name)+mInfo.getGoods_name()+"\n"+getString(R.string.ui_price)+mInfo.getGoods_price()+"\n"
                    + getString(R.string.ui_aisle)+mInfo.getGoodsSlotMap() + "\n"+getString(R.string.ui_stock)+mInfo.getGoods_stock());
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

    private ButtonListener m_ButtonListener = new ButtonListener();
    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.goods_btn_replsh == id) {
                List<Goods_info> mGoodList = TcnVendIF.getInstance().getAliveGoodsAll();
                if ((null == mGoodList) || (mGoodList.size() < 1)) {
                    TcnUtility.getToast(ReplenishActivity.this, getString(R.string.tip_no_vaild_goods));
                    return;
                }
                Intent in=new Intent(ReplenishActivity.this, GoodsSelect.class);
                startActivityForResult(in, 110);
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
                TcnUtility.getToast(ReplenishActivity.this, getString(R.string.tip_not_support_temp));
                return;
            }
            if((position < TcnCommon.coil_num) && (m_iIndexPage* TcnCommon.coil_num + position < m_listData_count)){
                Intent in=new Intent(ReplenishActivity.this, ReplenishActivity.class);
                if ((position + 1) == m_listData_count) {
                    in.putExtra("goods_edit_id", -1);
                } else {
                    Goods_info mInfo = m_list_aliveGoods.get(m_iIndexPage * TcnCommon.coil_num + position);
                    int iId = mInfo.getID();
                    TcnVendIF.getInstance().LoggerDebug(TAG, "onItemClick iId: "+iId+" getGoods_url: "+mInfo.getGoods_url()+" position: "+position);
                    in.putExtra("goods_edit_id", iId);
                }
               // startActivity(in);
            }
        }
    }
    private GoodsItemLongCilckListener m_itemLongClickListener = new GoodsItemLongCilckListener();
    private class GoodsItemLongCilckListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            m_iIndexPage = m_displayvp.getCurrentItem();
            if((position < TcnCommon.coil_num) && (m_iIndexPage* TcnCommon.coil_num + position < m_listData_count)){
                if ((position + 1) == m_listData_count) {

                } else {
                    Goods_info minfo = m_list_aliveGoods.get(m_iIndexPage * TcnCommon.coil_num + position);
                    String strId = minfo.getGoods_id();
                    TcnVendIF.getInstance().LoggerDebug(TAG, "onItemLongClick strId: "+strId+" getGoods_url: "+minfo.getGoods_url()+" position: "+position);
                }

            }
            return true;
        }
    }

    private SlotButtonListener m_SlotButtonListener = new SlotButtonListener();
    private class SlotButtonListener implements ButtonBatchSelect.ButtonListener {
        @Override
        public void onClick(View v, int buttonId) {
            int id = v.getId();
            if (id == R.id.goods_modify_replsh) {
                if (buttonId == ButtonBatchSelect.BUTTON_ID_LEFT) {

                    String addCount = goods_modify_replsh.getTitleEditText();
                    if (!TcnVendIF.getInstance().isDigital(addCount)) {
                        TcnUtility.getToast(ReplenishActivity.this, getString(R.string.slot_info_add_count_cannot_empty));
                        return;
                    }

                    String start = goods_modify_replsh.getStartEditText();
                    if (!TcnVendIF.getInstance().isDigital(start)) {
                        TcnUtility.getToast(ReplenishActivity.this, getString(R.string.slot_info_modify_tips_slot_err));
                        return;
                    }

                    String end = goods_modify_replsh.getEndEditText();
                    if (!TcnVendIF.getInstance().isDigital(end)) {
                        end = start;
                    }

                    if (goods_info != null) {
                        goods_info.setGoods_stock(Integer.valueOf(addCount));
                        TcnVendIF.getInstance().reqAddSlotGoods(Integer.valueOf(start),Integer.valueOf(end),goods_info);
                    } else {

                        if (Integer.valueOf(addCount).intValue() == 0) {
                            TcnUtility.getToast(ReplenishActivity.this, getString(R.string.menu_replishment_select_goods));
                        } else {
                            Goods_info mInfo = new Goods_info();
                            mInfo.setGoods_stock(Integer.valueOf(addCount));
                            TcnVendIF.getInstance().reqAddSlotGoods(Integer.valueOf(start),Integer.valueOf(end),mInfo);
                        }

                    }

                } else if (buttonId == ButtonBatchSelect.BUTTON_ID_RIGHT) {

                } else {
                    
                }
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
                case TcnVendEventID.ADD_SLOT_GOODS:
                    m_list_aliveGoods = TcnVendIF.getInstance().getAliveGoods();
                    m_listData_count = TcnVendIF.getInstance().getAliveGoodsCount();

                    m_iTotalPageNum = UIComBack.getInstance().getTotalPage(m_listData_count);
                    m_Adapter = new GoodsPagerAdapter();
                    m_displayvp.setAdapter(m_Adapter);
                    m_Adapter.notifyDataSetChanged();

                    TcnUtility.getToast(ReplenishActivity.this, getString(R.string.tip_modify_success));
                    break;
                default:
                    break;
            }
        }
    }
}
