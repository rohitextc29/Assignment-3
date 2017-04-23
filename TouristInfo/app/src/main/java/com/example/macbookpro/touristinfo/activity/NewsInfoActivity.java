package com.example.macbookpro.touristinfo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.macbookpro.touristinfo.adapter.MyRecycleViewAdapter;
import com.example.macbookpro.touristinfo.adapter.NewInformationRecyclerViewAdapter;
import com.example.macbookpro.touristinfo.bean.NewsInfoItemBean;
import com.example.macbookpro.touristinfo.R;
import com.example.macbookpro.touristinfo.bean.NewsItem;
import com.example.macbookpro.touristinfo.bean.SettingItem;
import com.example.macbookpro.touristinfo.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 09/04/17.
 */

public class NewsInfoActivity extends AppCompatActivity{

    private String linkUrl;
    private String apiKey;
    private String source;
    private String name;
    private String strTitle;

    private List<NewsInfoItemBean> itemBeenList = new ArrayList<>();

    private RecyclerView recyclerview;
    private NewInformationRecyclerViewAdapter mAdapter;

    private List<String> listtoinsert = new ArrayList<>();
    private List<String> listinserted = new ArrayList<>();

    private DatabaseHelper mdbhelper;
    private DatabaseHelper mdbhelper1;

    private String fontsize,showimage,background;
    private LinearLayout newsLinearLayout;
    private TextView messageTextView;

    private ProgressDialog progressDialog;
    private SharedPreferences settingPreference;
    private String mypreference = "setting";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkUrl=getResources().getString(R.string.news_url_article);
        apiKey=getResources().getString(R.string.api_key_news);
        Bundle bundle = getIntent().getExtras();
        source=bundle.getString("source");
        name=bundle.getString("name");
        strTitle=bundle.getString("strTitle");




        setContentView(R.layout.news_info_layout);

        newsLinearLayout = (LinearLayout)findViewById(R.id.newsLinearLayout);
        messageTextView = (TextView)findViewById(R.id.messageTextView);

        mdbhelper=new DatabaseHelper(getApplicationContext(),strTitle);

        getSupportActionBar().setTitle(strTitle+" - "+name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mdbhelper1=new DatabaseHelper(getApplicationContext());

        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.activity_horizontal_margin));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadNewsInfo loadNewsInfoObj = new loadNewsInfo();
        //loadNewsInfoObj.execute(linkUrl,source,apiKey);
        itemBeenList.clear();
        makevolleyRequest(linkUrl,source,apiKey);

        getInsertedDatas();

        /*SettingItem item = mdbhelper1.getSaveSettingData();
        if(item==null){
            item=new SettingItem();
            item.setFontsize("Medium");
            item.setShowimage("true");
            item.setBackground("Light");
            mdbhelper1.saveSettingInDatabase(item);
        }



        item = mdbhelper1.getSaveSettingData();
        fontsize=item.getFontsize();
        showimage=item.getShowimage();
        background=item.getBackground();
*/
        settingPreference = getSharedPreferences(mypreference,0);

        fontsize=settingPreference.getString("fontsize","Medium");
        showimage=settingPreference.getString("showimage","true");
        background=settingPreference.getString("background","Light");

        System.out.println("fontsize "+fontsize+" showimage "+showimage+" background "+background);

        if(background.toLowerCase().trim().equals("dark")){
            newsLinearLayout.setBackgroundResource(R.color.dark);
        }else{
            newsLinearLayout.setBackgroundResource(R.color.light);
        }
    }



    public void makevolleyRequest(String slinkUrl,String strSource, String strApiKey)
    {
        String strCompleteUrl = slinkUrl+"?source="+strSource+"&sortBy=latest&apiKey="+strApiKey;
        //params[0]+"?source="+params[1]+"&sortBy=latest&apiKey="+params[2]
        progressDialog=new ProgressDialog(NewsInfoActivity.this);
        progressDialog.setTitle("Loading news. Please wail");
        progressDialog.setCancelable(false);
        progressDialog.show();
        System.out.println("strCompleteUrl="+strCompleteUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(NewsInfoActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strCompleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response= "+response);
                        volleyonResponse(response);
                        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
                        mAdapter = new NewInformationRecyclerViewAdapter(itemBeenList,getApplicationContext(),strTitle,fontsize,showimage,background);
                        recyclerview.setAdapter(mAdapter);
                        recyclerview.setLayoutManager(new LinearLayoutManager(NewsInfoActivity.this));
                        mAdapter.notifyDataSetChanged();
                        if(itemBeenList.size()!=0){
                            messageTextView.setVisibility(View.GONE);
                        }else{
                            messageTextView.setVisibility(View.VISIBLE);
                            messageTextView.setText("No news yet.");
                        }

                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                messageTextView.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
                messageTextView.setText("Sorry, there was some server issue.");
            }
        });

        requestQueue.add(stringRequest);
    }

    public void volleyonResponse(String sJson){
        if(sJson !=null && !sJson.trim().equals("")){
            try {
                JSONObject jsonObj = new JSONObject(sJson);
                // Getting JSON Array node
                JSONArray articles = jsonObj.getJSONArray("articles");
                // looping through All Contacts
                for (int i = 0; i < articles.length(); i++) {
                    JSONObject c = articles.getJSONObject(i);
                    String author = c.getString("author");
                    String title=c.getString("title");
                    String description=c.getString("description");
                    String url=c.getString("url");
                    String urlToImage=c.getString("urlToImage");
                    String publishedAt=c.getString("publishedAt");


                    System.out.println("id -- "+author);

                    NewsInfoItemBean bean = new NewsInfoItemBean();
                    bean.setAuthor(author);
                    bean.setTitle(title);
                    bean.setDescription(description);
                    bean.setUrl(url);
                    bean.setUrlToImage(urlToImage);
                    bean.setPublishedAt(publishedAt);

                    if(!listinserted.contains(title)){
                        bean.setViewed(0);
                    }else {
                        bean.setViewed(1);
                    }

                    itemBeenList.add(bean);


                }
            }catch (Exception e){

            }

        }
    }

    public void getInsertedDatas(){
        List<NewsItem> beanList = mdbhelper.getSaveData();
        for(int i=0;i<beanList.size();i++){
            NewsItem news = beanList.get(i);
            System.out.println("i---"+i);
            System.out.println("entry_id="+news.getEntryid());
            System.out.println("firstName="+news.getTitle());
            if(!listinserted.contains(news.getTitle())){
                listinserted.add(news.getTitle());
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                switchToSetting();
                break;
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }

    public void switchToSetting(){
        Intent intent = new Intent(NewsInfoActivity.this,SettingActivity.class);
        startActivity(intent);
    }

}
