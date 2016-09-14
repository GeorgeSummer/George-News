package com.george.summer.gerogenews.myfragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.george.summer.gerogenews.NetNews;
import com.george.summer.gerogenews.R;
import com.george.summer.gerogenews.adapter.Adapter;
import com.george.summer.gerogenews.basefragment.BlankFragment;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import utils.MyItemDecoration;
import utils.MySQLiteDataBase;


/**
 * Created by Administrator on 2016/8/31.
 */
public class Fragment3 extends BlankFragment {
    RecyclerView rv;
    List<NetNews> list;
    SQLiteDatabase db;
    Adapter mAdapter;
    SwipeRefreshLayout mRefreshLayout;
    LinearLayoutManager mManager;

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment3;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.frag3_refresh);
        rv = (RecyclerView) view.findViewById(R.id.frag3_rv);
        list = new ArrayList<NetNews>();
        db = new MySQLiteDataBase(getContext()).getWritableDatabase();
        mManager = new LinearLayoutManager(getContext());
        getList();
        mAdapter = new Adapter(getContext(),true);
        if(list != null){
            mAdapter.setList(list);
            rv.setAdapter(mAdapter);
            rv.setLayoutManager(mManager);
            rv.addItemDecoration(new MyItemDecoration(getContext(),
                    LinearLayoutManager.VERTICAL));
        }else {
            Toast.makeText(getContext(), "hahahahhh", Toast.LENGTH_SHORT).show();
        }
       mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       getList();
                       mAdapter.notifyDataSetChanged();
                       mRefreshLayout.setRefreshing(false);
                   }
               },2000);
           }
       });
        mAdapter.setOnItemClickListener(new Adapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(String url, String docid, NetNews netNews) {
                mListener.onFragmentInteraction(url,docid,netNews);
            }
        });
        mAdapter.setOnItemLongClickListener(new Adapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(NetNews netNews) {
                showDialog(netNews);
            }
        });
    }

    private void showDialog(final NetNews netNews) {
        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
        b.setTitle("是否取消收藏？").setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                list.remove(netNews);
                mAdapter.notifyDataSetChanged();
            }
        }).setPositiveButton("否",null);
        b.setCancelable(false);
        b.show();

    }

    private void getList() {
        Cursor cursor = db.rawQuery("select * from NewsTable", null);
        if(cursor != null){
            while(cursor.moveToNext()){
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex("news"));
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                try{
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    NetNews netNews = (NetNews) ois.readObject();
                    list.add(netNews);
                    bais.close();
                    ois.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


}
