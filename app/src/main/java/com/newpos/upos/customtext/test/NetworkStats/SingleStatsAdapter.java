package com.newpos.upos.customtext.test.NetworkStats;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newpos.upos.customtext.R;

import java.util.List;

/**
 * Created by Wenson_Luo on 2017/9/19.
 */

public class SingleStatsAdapter extends RecyclerView.Adapter<SingleStatsAdapter.RecyViewHolder>{
    private List<SingleStatsModel> mData;
    private Context mContext;
    PackageManager pm;

    public SingleStatsAdapter(List<SingleStatsModel> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        pm = mContext.getPackageManager();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @Override
    public RecyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyViewHolder recyViewHolder = new RecyViewHolder(LayoutInflater.
                from(mContext).inflate(R.layout.item_recy,parent,false));

        return recyViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyViewHolder holder, int position) {
        holder.appName.setText(mData.get(position).getAppInfo().loadLabel(mContext.getPackageManager()).toString());
        holder.mobile.setText(mData.get(position).getMobileConsume());
        holder.wifi.setText(mData.get(position).getWifiConsume());
        holder.icLauncher.setImageDrawable(mData.get(position).getAppInfo().loadIcon(pm));
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(int position, SingleStatsModel bean) {
        mData.add(position, bean);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    class RecyViewHolder extends RecyclerView.ViewHolder{
        TextView appName,mobile,wifi;
        ImageView icLauncher;
        public RecyViewHolder(View itemView) {
            super(itemView);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            mobile = (TextView) itemView.findViewById(R.id.app_mobile_tv);
            wifi = (TextView) itemView.findViewById(R.id.app_wifi_tv);
            icLauncher = (ImageView) itemView.findViewById(R.id.app_launcher);
        }
    }
}