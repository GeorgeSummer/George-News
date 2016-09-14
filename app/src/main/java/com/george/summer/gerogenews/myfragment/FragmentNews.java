package com.george.summer.gerogenews.myfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.george.summer.gerogenews.NetNews;
import com.george.summer.gerogenews.R;
import com.george.summer.gerogenews.adapter.Adapter;
import com.george.summer.gerogenews.app.App;
import com.george.summer.gerogenews.basefragment.BlankFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.MyItemDecoration;

/**
 * Created by Administrator on 2016/8/31.
 */
public class FragmentNews extends BlankFragment implements Adapter.OnRecyclerViewItemClickListener {
    private RecyclerView rv;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isVisible;
    private boolean isPrepared;                     // 标志位，标志已经初始化完成。
    private boolean islorded;
    private LinearLayoutManager mManager;
    private Adapter mAdapter;
    private ProgressBar pb;
    private List<NetNews> list;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = (RecyclerView) view.findViewById(R.id.recycle);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        pb = (ProgressBar) view.findViewById(R.id.news_pb);
        isPrepared = true;
        mManager = new LinearLayoutManager(getContext());
        mAdapter = new Adapter(getContext(),false);
        mAdapter.setOnItemClickListener(this);
        lazyLoad();//这里我们调用以下去加载我们的数据
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       islorded = false;
                        isPrepared = true;
                        lazyLoad();
                        Toast.makeText(getContext(), "向下加载完成！！", Toast.LENGTH_SHORT).show();
                        mRefreshLayout.setRefreshing(false);

                    }
                }, 2000);
            }
        });

        //向上滑动，当滑到底的时候,有上滑的趋势，就加载更多
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!mRefreshLayout.isRefreshing()){
                    int lastVisibleItem = mManager.findLastVisibleItemPosition();
                    if(newState == RecyclerView.SCROLL_STATE_IDLE &&
                            lastVisibleItem + 1 == mAdapter.getItemCount()){
                        //调用Adapter里的changeMoreStatus方法来改变加载脚View的显示状态为：正在加载...
                        mAdapter.changeMoreStatus(Adapter.ISLOADING);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "向上加载完成！！",
                                        Toast.LENGTH_SHORT).show();
                                //当加载完数据后，再恢复加载脚View的显示状态为：上拉加载更多
                                mAdapter.changeMoreStatus(Adapter.PULLUP_LOAD_MORE);
                            }
                        }, 3000);
                    }
                }
            }
        });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //frahment从不可见到完全可见的时候，会调用该方法
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }else{
            isVisible = false;
            onInvisible();
        }
    }

    private void lazyLoad() {//懒加载的方法,在这个方法里面我们为Fragment的各个组件去添加数据
        if(isPrepared && isVisible && (!islorded)){
            getData();
            isPrepared = false;
            islorded = true;
        }
    }

    private void onVisible() {
        lazyLoad();
    }

    private void onInvisible() {

    }

    private void getData() {
        pb.setVisibility(View.VISIBLE);
        mRefreshLayout.setVisibility(View.INVISIBLE);
        final Bundle bundle = getArguments();
        if(bundle != null){
            final String tid = bundle.getString("tid");
            String url = "http://c.m.163.com/nc/article/list/" + tid + "/0-20.html";
            Request request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    list = new ArrayList<NetNews>();
                    try{
                        JSONObject json = new JSONObject(s);
                        JSONArray data = json.getJSONArray(tid);
                        for(int i = 0; i < data.length(); i++){

                            JSONObject obj = data.getJSONObject(i);
                            Gson gson = new Gson();
                            NetNews netNews = gson.fromJson(obj.toString(), NetNews.class);
                            list.add(netNews);
                        }
                        //Adapter adapter = new Adapter(list,getContext());
                        mAdapter.setList(list);
                        rv.setAdapter(mAdapter);
                        rv.addItemDecoration(new MyItemDecoration(getContext(),
                                LinearLayoutManager.VERTICAL));
                        //rv.addItemDecoration(new MyItemDecoration2(getContext(),
                        // LinearLayoutManager.VERTICAL));
                        rv.setLayoutManager(mManager);
                        pb.setVisibility(View.INVISIBLE);
                        mRefreshLayout.setVisibility(View.VISIBLE);
                    }catch(JSONException e){
                        Toast.makeText(getContext(), "******", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            App.getQueue().add(request);
        }
    }

    public static FragmentNews getInstance(Bundle bundle) {
        FragmentNews fn = new FragmentNews();
        if(bundle != null){
            fn.setArguments(bundle);
        }
        return fn;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_news;
    }

    @Override
    public void onItemClick(String url, String docid,NetNews netNews) {
        mListener.onFragmentInteraction(url,docid,netNews);
    }
}
