package com.quick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 简单的adapter处理单一布局
 * Created by bixinwei on 16/3/9.
 */
public abstract class QuickAdapter<T> extends BaseRecyclerAdapter<T> {

    private final static int VIEW_TYPE_NORMAL = Integer.MAX_VALUE;

    public  int itemViewLayoutId;

    public abstract void bindData(BaseRecyclerViewHolder holder, T t, int position);

    public void bindData(BaseRecyclerViewHolder holder, T itemBean, int position, int viewType) {
        bindData(holder, itemBean, position);
    }

    public  QuickAdapter(int itemViewLayoutId){
        this.itemViewLayoutId=itemViewLayoutId;
    }
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemViewLayoutId, parent,
                false);
        return new BaseRecyclerViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {

        return VIEW_TYPE_NORMAL;

    }

}