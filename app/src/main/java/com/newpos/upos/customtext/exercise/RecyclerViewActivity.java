package com.newpos.upos.customtext.exercise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.newpos.upos.customtext.R;
import com.newpos.upos.customtext.exercise.recy.DividerGridItemDecoration;
import com.newpos.upos.customtext.exercise.recy.RecyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 效果测试
 * Created by Wenson_Luo on 2017/9/19.
 */

public class RecyclerViewActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List<String> mData;
    RecyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mAdapter = new RecyAdapter(mData, RecyclerViewActivity.this);
        mAdapter.setOnItemClickLitener(new RecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(RecyclerViewActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
                mAdapter.addData(position, "hhh");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(RecyclerViewActivity.this, position + " long click",
                        Toast.LENGTH_SHORT).show();
                mAdapter.removeData(position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mData = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mData.add(""+(char)i);
        }
    }


}
