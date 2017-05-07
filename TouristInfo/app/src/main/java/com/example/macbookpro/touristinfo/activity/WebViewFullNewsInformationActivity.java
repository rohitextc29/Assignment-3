package com.example.macbookpro.touristinfo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.macbookpro.touristinfo.R;
import com.example.macbookpro.touristinfo.database.DatabaseHelper;

/**
 * Created by macbookpro on 09/04/17.
 */

public class WebViewFullNewsInformationActivity extends AppCompatActivity{

    private DatabaseHelper mdbhelper;
    private String strUrl;
    private String title;
    private String imgUrl;
    private static final int REQUEST_CODE = 0x11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_app);
        Bundle bundle = getIntent().getExtras();
        strUrl = bundle.getString("url");
        title=bundle.getString("title");
        WebView webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl(strUrl);

        getSupportActionBar().setTitle(title);
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
}
