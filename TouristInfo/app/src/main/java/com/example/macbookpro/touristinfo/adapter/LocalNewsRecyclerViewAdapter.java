package com.example.macbookpro.touristinfo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.macbookpro.touristinfo.R;
import com.example.macbookpro.touristinfo.bean.LocalNewsBean;
import com.example.macbookpro.touristinfo.bean.NewsInfoItemBean;
import com.example.macbookpro.touristinfo.database.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by macbookpro on 22/04/17.
 */

public class LocalNewsRecyclerViewAdapter extends RecyclerView.Adapter<LocalNewsRecyclerViewAdapter.MyViewHolder> {

    private List<LocalNewsBean> newsList;
    private Context context;

    public LocalNewsRecyclerViewAdapter(List<LocalNewsBean> newsList, Context context){
        this.newsList=newsList;
        this.context=context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private ImageView newImageView;

        public MyViewHolder(View view) {
            super(view);
            newImageView=(ImageView)view.findViewById(R.id.newImageView);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_news_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LocalNewsBean bean  = newsList.get(position);
        holder.title.setText(bean.getTitle());
        holder.description.setText(bean.getDescription());

        File imgFile = new  File(bean.getImagepath());

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            holder.newImageView.setImageBitmap(myBitmap);

        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
