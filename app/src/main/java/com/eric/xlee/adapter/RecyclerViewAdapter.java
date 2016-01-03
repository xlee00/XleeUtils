/**
 * RecyclerViewAdpater.java[V 1.0.0]
 * classes : com.xlee.recyclerview.RecyclerViewAdpater
 * 李志华 Xlee Create at Nov 14, 2015 11:38:19 AM
 */
package com.eric.xlee.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eric.xlee.utils.R;

/**
 * com.xlee.recyclerview.RecyclerViewAdpater
 *
 * @author 李志华 Xlee<br/>
 *         create at Nov 14, 2015 11:38:19 AM
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private List<String> mDatas;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public RecyclerViewAdapter(Context context, List<String> datas) {
        mDatas = datas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /*
     * @see
     * android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android
     * .view.ViewGroup, int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(mLayoutInflater.inflate(R.layout.demo_recycler_item, parent,
                false));
        return vh;
    }

    /*
     * 
     * @see
     * android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(android
     * .support.v7.widget.RecyclerView.ViewHolder, int)
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTxtTitle.setText(mDatas.get(position));
        holder.mTxtTitle.setClickable(true);
        holder.mTxtTitle.setLongClickable(true);
        if (null != mOnItemClickListener) {
            holder.mTxtTitle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.mTxtTitle.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    public void addData(int position, String data) {
        mDatas.add(position, data);
        this.notifyItemInserted(position);
    }

    public void delData(int position) {
        mDatas.remove(position);
        this.notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTxtTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtTitle = (TextView) itemView.findViewById(R.id.txt_item);
        }
    }
}
