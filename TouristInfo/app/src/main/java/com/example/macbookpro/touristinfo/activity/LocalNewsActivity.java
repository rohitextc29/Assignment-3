package com.example.macbookpro.touristinfo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.macbookpro.touristinfo.R;
import com.example.macbookpro.touristinfo.adapter.LocalNewsRecyclerViewAdapter;
import com.example.macbookpro.touristinfo.bean.LocalNewsBean;
import com.example.macbookpro.touristinfo.database.DatabaseHelper;

import java.util.List;

public class LocalNewsActivity extends AppCompatActivity {

    private TextView messageTextView;
    private RecyclerView recyclerview;
    private DatabaseHelper mDBHelper;
    private LocalNewsRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_news);
        messageTextView = (TextView)findViewById(R.id.messageTextView);
        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        mDBHelper = new DatabaseHelper(getApplicationContext());
        getLocalNewsList();
        getSupportActionBar().setTitle("Saved News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }

    public void getLocalNewsList(){
        List<LocalNewsBean> newsbeanList = mDBHelper.getLocalNews();
        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        mAdapter = new LocalNewsRecyclerViewAdapter(newsbeanList,getApplicationContext());
        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(LocalNewsActivity.this));
        mAdapter.notifyDataSetChanged();
        if(newsbeanList.size()!=0){
            messageTextView.setVisibility(View.GONE);
        }else{
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText("No news yet.");
        }
    }

}
