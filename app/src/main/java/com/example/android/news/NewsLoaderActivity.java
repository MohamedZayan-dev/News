package com.example.android.news;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;


import java.util.List;

public class NewsLoaderActivity extends AsyncTaskLoader<List<NewsInfoActivity>> {

    private String mUrl;
   public NewsLoaderActivity(Context context, String url){

        super(context);
       mUrl=url;
    }
    @Nullable
    @Override
    public List<NewsInfoActivity> loadInBackground() {

       if(mUrl==null){
           return null;
       }
        List<NewsInfoActivity> news=NewsNetworkingActivity.fetchData(mUrl);
       return news;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
