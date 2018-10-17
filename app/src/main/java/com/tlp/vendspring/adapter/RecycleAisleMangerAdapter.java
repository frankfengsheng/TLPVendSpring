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
import com.tlp.vendspring.bean.AisleInfoBean;
import com.tlp.vendspring.bean.MsShelfMangerInfoBean;

import java.util.List;

public class RecycleAisleMangerAdapter extends RecyclerView.Adapter<RecycleAisleMangerAdapter.MyHolder> {

    Context context;
    List<AisleInfoBean.DataBean> list;
    OnItemclickListenner itemclickListenner;

    public RecycleAisleMangerAdapter(Context context, List<AisleInfoBean.DataBean> list){
            this.context=context;
            this.list=list;
    }
    @Override
    public RecycleAisleMangerAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.ms_layout_aisle_item,null);
        MyHolder myHolder=new MyHolder(view);


        return myHolder;
    }

    public void setOnItemClickLis(OnItemclickListenner itemClickLis){
        this.itemclickListenner=itemClickLis;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        Glide.with(context).load(list.get(position).getGoods_url()).into(holder.imageView);
        holder.tvName.setText(list.get(position).getGoods_name());
        holder.tvPrice.setText("￥"+list.get(position).getPrice_sales());
        holder.tvCapacity.setText("存/容："+list.get(position).getChannel_remain()+"/"+list.get(position).getChannel_capacity()+"");//存量
        holder.tvAisleNumber.setText(list.get(position).getChannel_num()+"货道");
        if(list.get(position).getStatus().equals("0")){
            holder.tvStop.setVisibility(View.VISIBLE);
        }else {
            holder.tvStop.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getLayoutPosition();
                itemclickListenner.onItemClick(holder.itemView,pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class   MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvName,tvPrice,tvCapacity,tvAisleNumber,tvStop;
        public MyHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.img);
            tvName= (TextView) itemView.findViewById(R.id.textname);
            tvPrice= (TextView) itemView.findViewById(R.id.textprice);
            tvCapacity= (TextView) itemView.findViewById(R.id.ms_tv_item_aisle_capacity);
            tvAisleNumber= (TextView) itemView.findViewById(R.id.ms_tv_aisle_number_item);
            tvStop= (TextView) itemView.findViewById(R.id.ms_tv_stop_aisle_item);

        }

    }
   public static interface OnItemclickListenner{
        void onItemClick(View view, int position);

    }
}
