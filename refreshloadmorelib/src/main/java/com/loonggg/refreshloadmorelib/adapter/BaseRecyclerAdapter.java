package com.loonggg.refreshloadmorelib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loonggg.refreshloadmorelib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gengguanglong on 2016/4/20.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int TYPE_ITEM = 0;  //普通Item View
    private int TYPE_FOOTER = 1;  //底部FootView
    private int TYPE_HEADER = 2;
    private int TYPE_EMPTY = -1;//内容为空时返回的布局
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    //默认为0
    private int load_more_status = 0;
    public static final int LOADING_MORE = 1;
    public List<T> mData;
    public Context mContext;
    public LayoutInflater mInflater;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;
    private boolean isLoadMore = false;
    private int size;
    private String loadMoreString = "上拉加载更多";
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isHeader = false;
    private int resId;
    private int headerViewId;
    private boolean isShowEmptyView = false;

    public BaseRecyclerAdapter(Context context, List<T> list) {
        mData = (list != null) ? list : new ArrayList<T>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setIsShowLoadMore(boolean flag) {
        isLoadMore = flag;
    }

    public void setLoadMoreString(String lms) {
        loadMoreString = lms;
    }

    public void setHeaderItem(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public void setHeaderView(int resId) {
        setHeaderItem(true);
        mData.add(0, (T) "header");
        headerViewId = resId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            View v = mInflater.inflate(resId, parent, false);
            return new MyEmptyViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            final RecyclerViewHolder holder = new RecyclerViewHolder(mContext, mInflater.inflate
                    (getItemLayoutId(viewType), parent, false));
            if (mClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                    }
                });
            }

            if (mLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mLongClickListener.onItemLongClick(holder.itemView, holder
                                .getLayoutPosition());

                        return true;
                    }
                });
            }
            return holder;
        } else if (isLoadMore && viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.recyclerview_footer_view, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        } else if (viewType == TYPE_HEADER) {
//            final RecyclerViewHolder holder = new RecyclerViewHolder(mContext, mInflater.inflate
//                    (getItemLayoutId(viewType), parent, false));
//            if (mClickListener != null) {
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
//                    }
//                });
//            }
//
//            if (mLongClickListener != null) {
//                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        mLongClickListener.onItemLongClick(holder.itemView, holder
//                                .getLayoutPosition());
//
//                        return true;
//                    }
//                });
//            }
            View v = mInflater.inflate(headerViewId, parent, false);
            return new MyHeaderViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isLoadMore) {
            if (holder instanceof FootViewHolder) {
                FootViewHolder footViewHolder = (FootViewHolder) holder;
                footViewHolder.footer_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onLoadMoreListener != null)
                            onLoadMoreListener.loadMore();
                    }
                });
                switch (load_more_status) {
                    case PULLUP_LOAD_MORE:
                        footViewHolder.foot_view_item_tv.setVisibility(View.VISIBLE);
                        footViewHolder.foot_view_item_tv.setText(loadMoreString);
                        footViewHolder.pb.setVisibility(View.GONE);
                        break;
                    case LOADING_MORE:
                        footViewHolder.foot_view_item_tv.setVisibility(View.GONE);
                        footViewHolder.pb.setVisibility(View.VISIBLE);
                        break;
                }
            } else if (holder instanceof MyHeaderViewHolder) {
            } else {
                bindData(holder, position, mData.get(position));
            }
        } else if (holder instanceof MyEmptyViewHolder) {
        } else if (holder instanceof MyHeaderViewHolder) {
        } else {
            bindData(holder, position, mData.get(position));
        }
    }

    public void setIsShowEmptyView(boolean flag) {
        isShowEmptyView = flag;
    }

    public void setEmptyView(int resId) {
        setIsShowLoadMore(false);
        setIsShowEmptyView(true);
        this.resId = resId;
    }

    @Override
    public int getItemCount() {
        size = mData.size();
        if (isShowEmptyView) {
            if (size <= 0) {
                return 1;
            }
        }
        if (isLoadMore) {
            return size + 1;
        } else {
            return size;
        }
    }

    public void add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    abstract public int getItemLayoutId(int viewType);

    abstract public void bindData(RecyclerView.ViewHolder holder, int position, T item);

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(View itemView, int pos);
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowEmptyView) {
            if (mData.size() <= 0) {
                return TYPE_EMPTY;
            }
        }
        // 最后一个item设置为footerView
        if (isLoadMore) {
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                if (isHeader) {
                    if (position == 0) {
                        return TYPE_HEADER;
                    } else {
                        return TYPE_ITEM;
                    }
                }
                return TYPE_ITEM;
            }
        } else {
            if (isHeader) {
                if (position == 0) {
                    return TYPE_HEADER;
                } else {
                    return TYPE_ITEM;
                }
            }
            return TYPE_ITEM;
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        public TextView foot_view_item_tv;
        public ProgressBar pb;
        public LinearLayout footer_layout;

        public FootViewHolder(View view) {
            super(view);
            pb = (ProgressBar) view.findViewById(R.id.progress_view);
            foot_view_item_tv = (TextView) view.findViewById(R.id.tv_content);
            footer_layout = (LinearLayout) view.findViewById(R.id.footer_layout);
        }
    }

    public static class MyEmptyViewHolder extends RecyclerView.ViewHolder {

        public MyEmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class MyHeaderViewHolder extends RecyclerView.ViewHolder {

        public MyHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
