package com.quick;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.bixinwei.baservhelp.R;

/**
 * Created by bixinwei on 16/7/28.
 */
public class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {

    /**
     * 是否发生了上拉滚动
     */
    private boolean isScrolled = false;
    private View loadmoreView;
    private TextView loadmoreTxt;
    HeaderAndFooterWrapper HFAdapter;
    public boolean LoadMoreMode = true;
    public boolean noMoreData = false;
    OnLoadMoreListener onLoadMoreListener;


    public RecyclerViewOnScrollListener(Context context, OnLoadMoreListener onLoadMoreListener) {
        super();
        this.onLoadMoreListener = onLoadMoreListener;
        loadmoreView = LayoutInflater.from(context).inflate(R.layout.load_more, null);
        loadmoreTxt = (TextView) loadmoreView.findViewById(R.id.loadmoreTxt);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (!LoadMoreMode) return;
        if (newState == RecyclerView.SCROLL_STATE_IDLE && isScollBottom(recyclerView)) {
            if (isScrolled && onLoadMoreListener != null&&!noMoreData) {
                onLoadMoreListener.loadMore();
            }
        }
    }

    private boolean isScollBottom(RecyclerView recyclerView) {
        return !isCanScollVertically(recyclerView);
    }

    private boolean isCanScollVertically(RecyclerView recyclerView) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < recyclerView.getHeight();
        } else {
            return ViewCompat.canScrollVertically(recyclerView, 1);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!LoadMoreMode) return;
        isScrolled = (dy > 0);
        if (isScrolled) {
            if (HFAdapter == null) {
                HFAdapter = (HeaderAndFooterWrapper) recyclerView.getAdapter();
            }
            if (!HFAdapter.hasLoadmoreView()) {
                loadmoreView.setTag(HeaderAndFooterWrapper.LOADMORE_VIEW_TAG);
                HFAdapter.addLoadmoreView(loadmoreView);
            }
        }
    }

    public void noMoreData() {
        noMoreData=true;
        loadmoreTxt.setText("-- No More Data --");
    }

    public void setLoadMoreMode(boolean loadMoreMode) {
        this.LoadMoreMode = loadMoreMode;
    }
}
