package com.george.summer.gerogenews.myfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.george.summer.gerogenews.NetNews;
import com.george.summer.gerogenews.NewsType;
import com.george.summer.gerogenews.R;
import com.george.summer.gerogenews.adapter.Adapter;
import com.george.summer.gerogenews.basefragment.BlankFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import utils.MyItemDecoration;


/**
 * Created by Administrator on 2016/8/31.
 */
public class Fragment2 extends BlankFragment {
    RecyclerView rv;
    private List<NetNews> newslist;
    ArrayList<NewsType.NewsInfo> list;
    private LinearLayoutManager mManager;
    private Adapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment2;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rv = (RecyclerView) view.findViewById(R.id.frag2_rv);
        mManager = new LinearLayoutManager(getContext());
        mAdapter = new Adapter(getContext(),false);
        getlist();
    }

    public static Fragment2 getInstance(Bundle bundle){
        Fragment2 fg = new Fragment2();
        if(bundle != null){
            fg.setArguments(bundle);
        }
        return fg;
    }

    private void getlist() {
        Bundle bundle = this.getArguments();
        if(bundle != null){
            list = (ArrayList<NewsType.NewsInfo>) bundle.getSerializable("list");
            for(int i = 0;i<list.size();i++){
                if(list.get(i).getTname().equals("哒哒趣闻")){
                    final String tid = list.get(i).getTid();

                    newslist = new ArrayList<NetNews>();

                    String url = "http://c.m.163.com/nc/article/list/" + tid + "/0-20.html";
                    x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            try{
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray array = jsonObject.getJSONArray(tid);
                                for(int i = 0; i < array.length(); i++){
                                    JSONObject obj = array.getJSONObject(i);
                                    Gson gson = new Gson();
                                    NetNews netNews = gson.fromJson(obj.toString(), NetNews.class);
                                    newslist.add(netNews);
                                }
                                mAdapter.setList(newslist);
                                rv.setAdapter(mAdapter);
                                rv.addItemDecoration(new MyItemDecoration(getContext(),
                                        LinearLayoutManager.VERTICAL));
                                //rv.addItemDecoration(new MyItemDecoration2(getContext(),
                                // LinearLayoutManager.VERTICAL));
                                rv.setLayoutManager(mManager);

                            }catch(JSONException e){
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {

                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                }
            }

        }
    }
}