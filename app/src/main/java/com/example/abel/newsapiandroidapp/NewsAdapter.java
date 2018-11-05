package com.example.abel.newsapiandroidapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abel.newsapiandroidapp.model.NewsItem;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
    Context mContext;
    ArrayList<NewsItem> mNewsItems;

    public NewsAdapter(Context context, ArrayList<NewsItem> newsItems){
        this.mContext = context;
        this.mNewsItems = newsItems;
    }

    @Override
    public NewsAdapter.NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.item, parent, shouldAttachToParentImmediately);
        NewsHolder viewHolder = new NewsHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        TextView date;

        public NewsHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title);
            description = (TextView) itemView.findViewById(R.id.news_description);
            date = (TextView) itemView.findViewById(R.id.news_date);
            Log.d("newsAdapter",   title + " " + description + " " + date);
        }

        void bind(final int listIndex) {
            title.setText(mNewsItems.get(listIndex).getTitle());
            description.setText(mNewsItems.get(listIndex).getDescription());
            date.setText(mNewsItems.get(listIndex).getPublishedAt());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Uri urlString = Uri.parse(mNewsItems.get(getAdapterPosition()).getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, urlString);
            if(intent.resolveActivity(mContext.getPackageManager()) != null){
                mContext.startActivity(intent);
            }
        }
    }
}
