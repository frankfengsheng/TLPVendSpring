package com.tlp.vendspring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tcn.vendspring.R;
import com.tlp.vendspring.bean.MsShelfMangerInfoBean;

import java.util.List;

public class RecycleShelfMangerAdapter extends RecyclerView.Adapter<RecycleShelfMangerAdapter.MyHolder> {

    Context context;
    List<MsShelfMangerInfoBean.DataBean> list;
    OnItemclickListenner itemclickListenner;

    public  RecycleShelfMangerAdapter(Context context,List<MsShelfMangerInfoBean.DataBean> list){
            this.context=context;
            this.list=list;
    }
    @Override
    public RecycleShelfMangerAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_shelf_manger,null);
        MyHolder myHolder=new MyHolder(view);


        return myHolder;
    }

    public void setOnItemClickLis(OnItemclickListenner itemClickLis){
        this.itemclickListenner=itemClickLis;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        Glide.with(context).load(list.get(position).getGoods_url()).into(holder.imageView);
        holder.tv_title.setText(list.get(position).getGoods_name());
        holder.tv_price.setText("￥"+list.get(position).getPrice_sales());
        holder.tv_stock.setText("存量："+list.get(position).getChannel_capacity()+"");//存量
        if(Integer.parseInt(list.get(position).getChannel_end())>Integer.parseInt(list.get(position).getChannel_start())) {
            holder.tv_aisle.setText("货道号："+list.get(position).getChannel_start()+"-"+list.get(position).getChannel_end());
        }else
        {
            holder.tv_aisle.setText("货道号："+list.get(position).getChannel_start());
        }
        holder.tv_inventory.setText("库存"+list.get(position).getChannel_remain()+"");//库存

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getLayoutPosition();
                itemclickListenner.onItemClick(holder.itemView,pos);
            }
        });
        holder.tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getLayoutPosition();
                itemclickListenner.onClearClick(v,pos);
            }
        });
        holder.tv_replenish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getLayoutPosition();
                itemclickListenner.onClearClick(v,pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class   MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tv_title,tv_price,tv_stock,tv_aisle,tv_inventory,tv_clear,tv_replenish;
        public MyHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.ms_iv_shelfmanger_adapter);
            tv_title= (TextView) itemView.findViewById(R.id.ms_tv_shelf_manger_adapter_title);
            tv_price= (TextView) itemView.findViewById(R.id.ms_tv_shelf_manger_adapter_price);
            tv_stock= (TextView) itemView.findViewById(R.id.ms_tv_shelf_manger_adapter_stock);
            tv_aisle= (TextView) itemView.findViewById(R.id.ms_tv_shelf_manger_adapter_aisle);
            tv_inventory= (TextView) itemView.findViewById(R.id.ms_tv_shelf_manger_adapter_inventory);
            tv_clear= (TextView) itemView.findViewById(R.id.ms_tv_shelf_manger_clear);
            tv_replenish= (TextView) itemView.findViewById(R.id.ms_tv_shelf_manger_replenish_goods);
        }

    }
   public static interface OnItemclickListenner{
        void onItemClick(View view,int position);
        void onClearClick(View view,int position);
        void onReplenishMentcClick(View view,int position);
    }
}
