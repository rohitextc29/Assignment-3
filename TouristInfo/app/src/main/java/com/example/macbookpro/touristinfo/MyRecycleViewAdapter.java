package com.example.macbookpro.touristinfo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by macbookpro on 09/04/17.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder>{

    private List<NewsItemBean> newsList;
    private Context context;
    private String strTitle;

    public MyRecycleViewAdapter(List<NewsItemBean> newsList, Context context,String strTitle){
        this.newsList=newsList;
        this.context=context;
        this.strTitle=strTitle;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private TextView name;
        private ImageView newImageView;
        private TextView papertitle;

        public MyViewHolder(View view) {
            super(view);
            //newImageView=(ImageView)view.findViewById(R.id.newImageView);
            //description = (TextView) view.findViewById(R.id.description);
            //name = (TextView) itemView.findViewById(R.id.name);
            papertitle = (TextView) itemView.findViewById(R.id.papertitle);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        NewsItemBean data = newsList.get(position);
        //holder.description.setText(data.getDescription());
        //holder.name.setText(data.getName());
        /*Glide.with(context).load(data.getIconMap().get("small"))
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.newImageView);
        */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NewsInfoActivity.class);
                intent.putExtra("source",newsList.get(position).getId());
                intent.putExtra("name",newsList.get(position).getName());
                intent.putExtra("strTitle",strTitle);
                context.startActivity(intent);
            }
        });
        holder.papertitle.setText(data.getName());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

}
