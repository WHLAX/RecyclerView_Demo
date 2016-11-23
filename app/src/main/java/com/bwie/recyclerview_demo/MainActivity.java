package com.bwie.recyclerview_demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwie.recyclerview_demo.view.PullBaseView;
import com.bwie.recyclerview_demo.view.PullRecyclerView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.bwie.recyclerview_demo.R.id.recyclerView;
import static com.nostra13.universalimageloader.core.ImageLoader.getInstance;

public class MainActivity extends Activity implements PullBaseView.OnHeaderRefreshListener,
        PullBaseView.OnFooterRefreshListener {
    private HomeAdapter mAdapter;
    private ArrayList<String> mDatas;
    private List<mDataBean.DataBean> data;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    HomeAdapter mAdapter;
                    mRecyclerView.setAdapter(mAdapter = new HomeAdapter(data));
                    break;
            }
        }
    };
    private ImageLoader loader;
    private PullRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initData();

        mRecyclerView = (PullRecyclerView) findViewById(recyclerView);

        mRecyclerView.setOnHeaderRefreshListener(MainActivity.this);//设置下拉监听
        mRecyclerView.setOnFooterRefreshListener(MainActivity.this);//设置上拉监听
//        recyclerView.setCanScrollAtRereshing(true);//设置正在刷新时是否可以滑动，默认不可滑动
//        recyclerView.setCanPullDown(false);//设置是否可下拉
//        recyclerView.setCanPullUp(false);//设置是否可上拉
        loader = getInstance();
        loader.init(ImageLoaderConfiguration.createDefault(MainActivity.this));
        getHttpData();

        // mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
        //  DividerItemDecoration.VERTICAL_LIST));
        //mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        //mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));

    }

    protected void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 1; i <= 50; i++) {
            mDatas.add("条目" + i);
        }
    }

    public void getHttpData() {
        OkHttp.getAsync("http://m.yunifang.com/yunifang/mobile/goods/getall?random=39986&encode=2092d7eb33e8ea0a7a2113f2d9886c90&category_id=17", new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                String json = result.toString();
                Gson gson = new Gson();
                mDataBean mDataBean = gson.fromJson(json, mDataBean.class);
                data = mDataBean.getData();
                /*Message msg = Message.obtain();
                msg.what = 0;
                handler.sendMessage(msg);
                Toast.makeText(MainActivity.this, data.size() + "", Toast.LENGTH_SHORT).show();*/

                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                mRecyclerView.setAdapter(mAdapter = new HomeAdapter(data));
                /*mRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener2(MainActivity.this, mRecyclerView, new RecyclerViewClickListener2.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "点击触发", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "长按触发", Toast.LENGTH_SHORT).show();
                    }
                }));*/
            }
        });

    }

    @Override
    public void onFooterRefresh(PullBaseView view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // mDatas.add("TEXT更多");
                mAdapter.notifyDataSetChanged();
                mRecyclerView.onFooterRefreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onHeaderRefresh(PullBaseView view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //  mDatas.add(0, "TEXT新增");
                mAdapter.notifyDataSetChanged();
                mRecyclerView.onHeaderRefreshComplete();
            }
        }, 3000);
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        private List<mDataBean.DataBean> list;

        public HomeAdapter(List<mDataBean.DataBean> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(list.get(position).getGoods_name());
            loader.displayImage(list.get(position).getGoods_img(), holder.iv);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            ImageView iv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.item_tv);
                iv = (ImageView) view.findViewById(R.id.item_iv);
            }
        }
    }
}
