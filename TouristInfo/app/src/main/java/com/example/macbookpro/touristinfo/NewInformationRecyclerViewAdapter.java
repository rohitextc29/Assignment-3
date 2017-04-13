package com.example.macbookpro.touristinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by macbookpro on 09/04/17.
 */

public class NewInformationRecyclerViewAdapter  extends RecyclerView.Adapter<NewInformationRecyclerViewAdapter.MyViewHolder>{

    private List<NewsInfoItemBean> newsList;
    private Context context;
    private String strTitle;
    String font_size;
    String show_image;
    String background;
    DatabaseHelper mdbhelper;

    public NewInformationRecyclerViewAdapter(List<NewsInfoItemBean> newsList, Context context,String strTitle,String font_size,String show_image,String background){
        this.newsList=newsList;
        this.context=context;
        this.strTitle=strTitle;
        this.font_size=font_size;
        this.show_image=show_image;
        this.background=background;
        mdbhelper=new DatabaseHelper(this.context,strTitle);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView author;
        private TextView publishedAt;
        private ImageView newImageView;
        private ImageView indicator;
        private Button buttonView;

        public MyViewHolder(View view) {
            super(view);
            newImageView=(ImageView)view.findViewById(R.id.newImageView);
            title = (TextView) view.findViewById(R.id.title);
            //description = (TextView) itemView.findViewById(R.id.description);
            author = (TextView) itemView.findViewById(R.id.author);
            publishedAt = (TextView) itemView.findViewById(R.id.publishedAt);
            indicator = (ImageView)itemView.findViewById(R.id.indicator);
            buttonView = (Button)itemView.findViewById(R.id.buttonView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_info_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        NewsInfoItemBean data = newsList.get(position);
        holder.title.setText(data.getTitle());


        //holder.description.setText(data.getDescription());
        holder.author.setText(data.getAuthor());
        holder.publishedAt.setText(data.getPublishedAt());

        if(font_size.toLowerCase().trim().equals("Small")){
            holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.txt_title_small));
            holder.author.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.txt_author_small));
            holder.publishedAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.txt_datetime_small));
        }else if(font_size.toLowerCase().trim().equals("Medium")){
            holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.txt_title_medium));
            holder.author.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.txt_author_medium));
            holder.publishedAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.txt_datetime_medium));
        }else if(font_size.toLowerCase().trim().equals("Large")){
            holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.txt_title_large));
            holder.author.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.txt_author_large));
            holder.publishedAt.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getDimension(R.dimen.txt_datetime_large));
        }

        if(!show_image.trim().equals("true")){
            holder.newImageView.setVisibility(View.GONE);
        }else {
            holder.newImageView.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(data.getUrlToImage())
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.newImageView);

        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mdbhelper.saveInDatabase(newsList.get(position).getTitle().toString());

                Intent intent=new Intent(context, FullNewInformationActivity.class);
                intent.putExtra("title",newsList.get(position).getTitle().toString());
                intent.putExtra("url",newsList.get(position).getUrl().toString());
                context.startActivity(intent);
                //Toast.makeText(context,newsList.get(position).getTitle(),Toast.LENGTH_SHORT).show();

            }
        });

        holder.indicator.setBackgroundResource(R.drawable.non_acknowledged);

        if(data.getViewed()!=0){
            holder.indicator.setBackgroundResource(R.drawable.acknowledged);
        }
    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }


}
