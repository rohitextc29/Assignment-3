package com.example.macbookpro.touristinfo;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by macbookpro on 08/04/17.
 */

public class NewsFragment extends Fragment{

    private View mainView;
    private String strTitle;
    private String sourceUrl;
    List<NewsItemBean> itemBeenList;
    private MyRecycleViewAdapter mAdapter;
    private RecyclerView recyclerview;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strTitle = getArguments().getString("strTitle");
        }
        sourceUrl=getResources().getString(R.string.news_url_source);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.news_layout, container,false);






        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNews loadNewsObj = new loadNews();
        loadNewsObj.execute(sourceUrl,strTitle);
    }

    class loadNews extends AsyncTask<String,String,String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            itemBeenList = new ArrayList<>();
            itemBeenList.clear();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading new. Please wail");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response ="";
            try {
                URL url = new URL(params[0]+"&category="+params[1].toLowerCase());
                System.out.println("url --- "+params[0]+"&category="+params[1].toLowerCase());
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
                    JSONArray sources = jsonObj.getJSONArray("sources");
                    // looping through All Contacts
                    for (int i = 0; i < sources.length(); i++) {
                        JSONObject c = sources.getJSONObject(i);
                        String id = c.getString("id");
                        String name=c.getString("name");
                        String description=c.getString("description");
                        String url=c.getString("url");

                        System.out.println("id -- "+id);

                        // urlsToLogos node is JSON Object
                        JSONObject urlsToLogos = c.getJSONObject("urlsToLogos");
                        String small = urlsToLogos.getString("small");
                        String medium = urlsToLogos.getString("medium");
                        String large = urlsToLogos.getString("large");
                        HashMap<String,String> iconMap = new HashMap<>();
                        iconMap.put("small",small);
                        iconMap.put("medium",medium);
                        iconMap.put("large",large);

                        NewsItemBean bean = new NewsItemBean();
                        bean.setId(id);
                        bean.setName(name);
                        bean.setDescription(description);
                        bean.setUrl(url);
                        bean.setIconMap(iconMap);

                        itemBeenList.add(bean);


                    }
                }catch (Exception e){

                }

            }
            recyclerview = (RecyclerView)mainView.findViewById(R.id.recyclerview);
            mAdapter = new MyRecycleViewAdapter(itemBeenList,getActivity().getApplicationContext(),strTitle);
            recyclerview.setAdapter(mAdapter);
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter.notifyDataSetChanged();

            progressDialog.dismiss();
        }
    }

    /*private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://192.168.1.7/test/example.json");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            List<DataFish> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataFish fishData = new DataFish();
                    fishData.fishImage= json_data.getString("fish_img");
                    fishData.fishName= json_data.getString("fish_name");
                    fishData.catName= json_data.getString("cat_name");
                    fishData.sizeName= json_data.getString("size_name");
                    fishData.price= json_data.getInt("price");
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter1 = new AdapterFish(getActivity(), data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(getActivity()));

            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }*/
}
