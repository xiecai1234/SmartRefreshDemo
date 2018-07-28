package com.bjc.xcb.smartrefreshdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    private SingleAdapter mSingleAdapter;
    private List<String> mDataList = new ArrayList<>();
    private int mPageIndex = 0;
    private int mPageSize = 10;
    private int MAX_PAGE_SIZE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
//        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishRefresh(2000, true);
//            }
//        });
//        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishLoadMore(2000, true, false);
//            }
//        });

//        //设置 Header 为 贝塞尔雷达 样式
//        smartRefreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
//        //设置 Footer 为 球脉冲 样式
//        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        for (int i=0;i<mPageSize;i++) {
            mDataList.add("初始数据" + String.valueOf(i));
        }
        mSingleAdapter = new SingleAdapter(this, mDataList, android.R.layout.simple_list_item_2);
        mSingleAdapter.enableLoadAnimation();
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(mSingleAdapter);

//        smartRefreshLayout.setEnableRefresh(true);//启用刷新
//        smartRefreshLayout.setEnableLoadMore(true);//启用加载
//        smartRefreshLayout.finishRefresh();//关闭刷新
//        smartRefreshLayout.finishLoadMore();//关闭加载
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mDataList.clear();
                mPageIndex = 0;
                for (int i=0;i<mPageSize;i++) {
                    mDataList.add("刷新数据" + String.valueOf(i));
                }
                mSingleAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                if (mPageIndex < MAX_PAGE_SIZE) {
                    for (int i=mPageIndex * mPageSize ;i<(mPageIndex+1) * mPageSize;i++) {
                        mDataList.add("加载更多数据" + String.valueOf(i));
                    }
                    mPageIndex++;
                    mSingleAdapter.notifyDataSetChanged();
                    refreshLayout.finishLoadMore();
                }else {
                    refreshLayout.finishLoadMore();
                    refreshLayout.setNoMoreData(true);
                }
            }
        });
    }

    public class SingleAdapter extends SuperAdapter<String> {
        public SingleAdapter(Context context, List<String> list, int layoutResId) {
            super(context, list, layoutResId);
        }

        @Override
        public void onBind(SuperViewHolder holder, int viewType, int position, String item) {
            holder.setText(android.R.id.text1, item);
            holder.setText(android.R.id.text2, item);
        }
    }
}
