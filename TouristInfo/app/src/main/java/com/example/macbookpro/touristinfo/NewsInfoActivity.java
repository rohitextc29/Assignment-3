package com.example.macbookpro.touristinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

    private List<NewsInfoItemBean> itemBeenList;

    private RecyclerView recyclerview;
    private NewInformationRecyclerViewAdapter mAdapter;

    List<String> listtoinsert = new ArrayList<>();
    List<String> listinserted = new ArrayList<>();

    DatabaseHelper mdbhelper;
    DatabaseHelper mdbhelper1;

    String font_size,show_image,background;
    LinearLayout newsLinearLayout;

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

        mdbhelper=new DatabaseHelper(getApplicationContext(),strTitle);

        getSupportActionBar().setTitle(strTitle+" - "+name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mdbhelper1=new DatabaseHelper(getApplicationContext());

        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.activity_horizontal_margin));

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNewsInfo loadNewsInfoObj = new loadNewsInfo();
        loadNewsInfoObj.execute(linkUrl,source,apiKey);

        getInsertedDatas();

        SettingItem item = mdbhelper1.getSaveSettingData();
        if(item==null){
            item=new SettingItem();
            item.setFont_size("Medium");
            item.setShow_image("true");
            item.setBackground("Light");
            mdbhelper1.saveSettingInDatabase(item);
        }

        item = mdbhelper1.getSaveSettingData();
        font_size=item.getFont_size();
        show_image=item.getShow_image();
        background=item.getBackground();

        System.out.println("font_size "+font_size+" show_image "+show_image+" background "+background);

        if(background.toLowerCase().trim().equals("dark")){
            newsLinearLayout.setBackgroundResource(R.color.dark);
        }else{
            newsLinearLayout.setBackgroundResource(R.color.light);
        }
    }

    public void getInsertedDatas(){
        List<NewsItem> beanList = mdbhelper.getSaveData();
        for(int i=0;i<beanList.size();i++){
            NewsItem news = beanList.get(i);
            System.out.println("i---"+i);
            System.out.println("entry_id="+news.getEntry_id());
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

    class loadNewsInfo extends AsyncTask<String,String,String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            itemBeenList = new ArrayList<>();
            itemBeenList.clear();
            progressDialog=new ProgressDialog(NewsInfoActivity.this);
            progressDialog.setTitle("Loading new. Please wail");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response ="";
            try {
                URL url = new URL(params[0]+"?source="+params[1]+"&sortBy=latest&apiKey="+params[2]);
                System.out.println("url --- "+params[0]+"?source="+params[1]+"&sortBy=latest&apiKey="+params[2]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

// read the response
                System.out.println("Response Code: " + conn.getResponseCode());
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                System.out.println(response);
            }catch (Exception ex){ex.printStackTrace();}
            return response;
        }

        @Override
        protected void onPostExecute(String sJson) {
            super.onPostExecute(sJson);
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
            recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
            mAdapter = new NewInformationRecyclerViewAdapter(itemBeenList,getApplicationContext(),strTitle,font_size,show_image,background);
            recyclerview.setAdapter(mAdapter);
            recyclerview.setLayoutManager(new LinearLayoutManager(NewsInfoActivity.this));
            mAdapter.notifyDataSetChanged();

            progressDialog.dismiss();
        }
    }

}
