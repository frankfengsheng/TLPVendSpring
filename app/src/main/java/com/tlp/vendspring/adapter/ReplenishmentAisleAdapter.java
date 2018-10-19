package com.tlp.vendspring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tcn.vendspring.R;
import com.tlp.vendspring.bean.MsReplenishmentAisleResult;
import com.tlp.vendspring.bean.MsShelfMangerInfoBean;

import java.util.List;

public class ReplenishmentAisleAdapter extends RecyclerView.Adapter<ReplenishmentAisleAdapter.MyHolder> {

    Context context;
    List<MsReplenishmentAisleResult.DataBean> list;
    OnItemclickListenner itemclickListenner;

    public ReplenishmentAisleAdapter(Context context, List<MsReplenishmentAisleResult.DataBean> list){
            this.context=context;
            this.list=list;
    }
    @Override
    public ReplenishmentAisleAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_replenishment_aisle,null);
        MyHolder myHolder=new MyHolder(view);


        return myHolder;
    }

    public void setOnItemClickLis(OnItemclickListenner itemClickLis){
        this.itemclickListenner=itemClickLis;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.tv_aisleNumber.setText(list.get(position).getChannel_num()+"货道");
        holder.tv_capacity.setText(list.get(position).getChannel_capacity());
        holder.tv_stock.setText(list.get(position).getChannel_remain());
        holder.tv_count.setText(list.get(position).getChannel_remains()+"");
        final MsReplenishmentAisleResult.DataBean dataBean=list.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getLayoutPosition();
                itemclickListenner.onItemClick(holder.itemView,pos);
            }
        });
        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getLayoutPosition();
                if(dataBean.getChannel_remains()>=(Integer.parseInt(dataBean.getChannel_capacity())-Integer.parseInt(dataBean.getChannel_remain()))){
                    Toast.makeText(context, "已经到了最大值", Toast.LENGTH_LONG).show();
                }else {
                    dataBean.setChannel_remains(dataBean.getChannel_remains() + 1);
                    itemclickListenner.onAddClick(v, pos);
                    notifyDataSetChanged();
                }
            }
        });
        holder.iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getLayoutPosition();
                if (dataBean.getChannel_remains() == 0) {
                    Toast.makeText(context, "已经到了最小值", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    dataBean.setChannel_remains(dataBean.getChannel_remains() - 1);
                    itemclickListenner.onReduceClick(v,pos);
                    notifyDataSetChanged();
                }

            }
        });
        holder.tv_replenish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getLayoutPosition();
                dataBean.setChannel_remains(Integer.parseInt(dataBean.getChannel_capacity())-Integer.parseInt(dataBean.getChannel_remain()));
                notifyDataSetChanged();
                itemclickListenner.onReplenishMentcClick(v,pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class   MyHolder extends RecyclerView.ViewHolder{
        ImageView iv_add,iv_reduce;
        TextView tv_aisleNumber,tv_capacity,tv_stock,tv_count,tv_replenish;
        public MyHolder(View itemView) {
            super(itemView);
            iv_add= (ImageView) itemView.findViewById(R.id.iv_add);
            iv_reduce= (ImageView) itemView.findViewById(R.id.iv_reduce);
            tv_aisleNumber= (TextView) itemView.findViewById(R.id.ms_tv_aisle_number);
            tv_capacity= (TextView) itemView.findViewById(R.id.ms_tv_aisle_capacity);
            tv_stock= (TextView) itemView.findViewById(R.id.ms_tv_aisle_stock);
            tv_count= (TextView) itemView.findViewById(R.id.ms_tv_aisle_count);
            tv_replenish= (TextView) itemView.findViewById(R.id.ms_tv_aisle_replenishment);
        }

    }
   public static interface OnItemclickListenner{
        void onItemClick(View view, int position);
        void onAddClick(View view, int position);
        void onReplenishMentcClick(View view, int position);
        void onReduceClick(View view, int position);
    }
}
