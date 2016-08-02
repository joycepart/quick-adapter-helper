package com.quick;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持多布局item的adapter
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected List<T> mDatas = new ArrayList<>();


    IViewType viewTypes;

    public BaseRecyclerAdapter() {

    }

    public BaseRecyclerAdapter(IViewType viewTypes) {
        this.viewTypes = viewTypes;
    }


    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        /**
         * @param itemView normalItem view
         * @param itemBean normalItem WrapBean
         * @param position normalItem real position ,be consistent with mDatas
         */
        void onItemClick(View itemView, Object itemBean, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void append(T itemBean) {
        if (itemBean != null) {
            mDatas.add(itemBean);
            notifyDataSetChanged();
        }
    }

    public void append(List<T> itemBeans) {
        if (itemBeans == null) return;
        if (itemBeans.size() > 0) {
            for (T itemBean : itemBeans) {
                mDatas.add(itemBean);
            }

            notifyDataSetChanged();
        }
    }

    public void replaceAll(List<T> itemBeans) {
        mDatas.clear();
        if (itemBeans != null && itemBeans.size() > 0) {
            mDatas.addAll(itemBeans);
            notifyDataSetChanged();
        }
    }

    public void removeAt(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    public void remove(T itemBean) {
        mDatas.remove(itemBean);
        notifyDataSetChanged();
    }

    public void removeAll() {
        mDatas.clear();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public abstract void bindData(BaseRecyclerViewHolder holder, T itemBean, int position, int viewType);


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewTypes.getItemViewLayoutId(viewType), parent,
                false);
        return new BaseRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.itemView, mDatas.get(position), position);
                }
            }
        });
        bindData(holder, mDatas.get(position), position, getItemViewType(position));
    }

    @Override
    public int getItemViewType(int position) {

        return viewTypes.getItemViewType(position, mDatas.get(position));

    }

}
