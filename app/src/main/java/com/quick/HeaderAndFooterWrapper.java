package com.quick;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhy on 16/6/23.
 */
public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    protected List<View> mHeaderViews = new ArrayList<>();
    protected List<View> mFootViews = new ArrayList<>();
    protected List<Integer> mHeaderViewTypes = new ArrayList<>();
    protected List<Integer> mFooterViewTypes = new ArrayList<>();
    protected final static int VIEW_TYPE_HEADER = 8888;
    protected final static int VIEW_TYPE_FOOTER = 9999;

    View loadmoreView;
    public static final String LOADMORE_VIEW_TAG="loadmoreView";
    private BaseRecyclerAdapter mInnerAdapter;

    public HeaderAndFooterWrapper(BaseRecyclerAdapter adapter)
    {
        mInnerAdapter = adapter;
    }


    public void addLoadmoreView(View loadmoreView){
        addFootView(loadmoreView);
    }

    public boolean hasLoadmoreView(){
        return (loadmoreView!=null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (mHeaderViewTypes.contains(viewType)) {
            return new BaseRecyclerViewHolder(mHeaderViews.get(viewType - VIEW_TYPE_HEADER));
        } else if (mFooterViewTypes.contains(viewType)) {
            return new BaseRecyclerViewHolder(mFootViews.get(VIEW_TYPE_FOOTER - viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isHeaderViewPos(position))
        {
            int mHeaderViewType = VIEW_TYPE_HEADER + position;
            mHeaderViewTypes.add(mHeaderViewType);
            return mHeaderViewType;
        } else if (isFooterViewPos(position))
        {
            int mFooterViewType = VIEW_TYPE_FOOTER - (position - getHeadersCount() - getRealItemCount());
            mFooterViewTypes.add(mFooterViewType);
            return mFooterViewType;
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount()
    {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (isHeaderViewPos(position))
        {
            return;
        }
        if (isFooterViewPos(position))
        {
            return;
        }
        mInnerAdapter.onBindViewHolder((BaseRecyclerViewHolder) holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount()
    {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback()
        {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position)
            {
                if (isHeaderViewPos(position)||isFooterViewPos(position))
                {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
//        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
//            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    return (isHeaderViewPos(position) || isFooterViewPos(position)) ? ((GridLayoutManager) layoutManager).getSpanCount() : 1;
//                }
//            });
//        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
    {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position))
        {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isHeaderViewPos(int position)
    {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position)
    {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view)
    {
        mHeaderViews.add(view);
        notifyDataSetChanged();
    }

    public void addFootView(View view)
    {
        //如果是loadmoreView
        if (LOADMORE_VIEW_TAG.equals(view.getTag())) {
            if (hasLoadmoreView()) {
                mFootViews.remove(loadmoreView);
            }
            this.loadmoreView=view;
            mFootViews.add(view);
        } else {//如果是一般的footview
            //如果已经包含loadmoreview
            if (hasLoadmoreView()) {
                mFootViews.add(mFootViews.size() - 1, view);
            } else {
                mFootViews.add(view);
            }
        }
        notifyDataSetChanged();
    }

    public int getHeadersCount()
    {
        return mHeaderViews.size();
    }

    public int getFootersCount()
    {
        return mFootViews.size();
    }


}
