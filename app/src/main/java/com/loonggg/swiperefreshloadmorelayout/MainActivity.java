package com.loonggg.swiperefreshloadmorelayout;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.loonggg.refreshloadmorelib.adapter.BaseRecyclerAdapter;
import com.loonggg.refreshloadmorelib.adapter.RecyclerViewHolder;
import com.loonggg.refreshloadmorelib.view.SwipeRefreshLoadMoreLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLoadMoreLayout.OnRefreshListener, SwipeRefreshLoadMoreLayout.OnLoadMoreListener {
    private SwipeRefreshLoadMoreLayout swipeRefreshLoadMoreLayout;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLoadMoreLayout = (SwipeRefreshLoadMoreLayout) findViewById(R.id.swipelayout);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        swipeRefreshLoadMoreLayout.setLayoutManager(mLayoutManager);
        swipeRefreshLoadMoreLayout.setOnLoadMoreListener(this);
        swipeRefreshLoadMoreLayout.setOnRefreshListener(this);
        getDatas();
        final BaseRecyclerAdapter adapter = new BaseRecyclerAdapter<String>(this, list) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_rv;
            }

            @Override
            public void bindData(RecyclerView.ViewHolder holder, int position, String item) {
                RecyclerViewHolder mHolder = (RecyclerViewHolder) holder;
            }
        };
        swipeRefreshLoadMoreLayout.setSwipeRefreshLayoutColor(Color.RED, Color.BLUE, Color.GREEN);
        swipeRefreshLoadMoreLayout.setAdapter(adapter);
        swipeRefreshLoadMoreLayout.setHeaderView(R.layout.header_layout);
        swipeRefreshLoadMoreLayout.setLoadMore(true);
        swipeRefreshLoadMoreLayout.setOnClickLoadMoreListener(new SwipeRefreshLoadMoreLayout.OnClickLoadMoreListener() {
            @Override
            public void onClickLoad() {
                Toast.makeText(MainActivity.this, "被点击了", Toast.LENGTH_SHORT).show();
            }
        });

        list.clear();
        swipeRefreshLoadMoreLayout.setEmptyView(R.layout.empty_layout);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {

            }
        });
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                getDatas();
                swipeRefreshLoadMoreLayout.refreshFinish();
                swipeRefreshLoadMoreLayout.setLoadMore(true);
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        swipeRefreshLoadMoreLayout.startLoadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDatas();
                swipeRefreshLoadMoreLayout.loadMoreFinish();
            }
        }, 2000);
    }

    private List<String> getDatas() {
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }
        return list;
    }
}
