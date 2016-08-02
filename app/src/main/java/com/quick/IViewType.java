package com.quick;

public interface IViewType {
    int getItemViewLayoutId(int viewType);
    int getItemViewType(int position, Object itemBean);
}