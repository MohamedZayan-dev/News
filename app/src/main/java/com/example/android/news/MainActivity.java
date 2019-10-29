package com.example.android.news;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsInfoActivity>> {

    private static final String url = "https://content.guardianapis.com/search";
    private TextView emptyView;
    NewsAdapterActivity newsAdapterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView) findViewById(R.id.list);
        newsAdapterActivity = new NewsAdapterActivity(getApplication(), new ArrayList<NewsInfoActivity>());
        listView.setAdapter(newsAdapterActivity);

        emptyView = (TextView) findViewById(R.id.Empty);
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsInfoActivity currentNews = newsAdapterActivity.getItem(i);
                Uri browserUrl = Uri.parse(currentNews.getmUrl());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUrl);
                startActivity(browserIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(0, null, this);
        } else {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            emptyView.setText("There's no Internet");
        }

    }


    @NonNull
    @Override
    public Loader<List<NewsInfoActivity>> onCreateLoader(int id, @Nullable Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Section = sharedPreferences.getString(getString(R.string.sectionNameKey), getString(R.string.sectionNameDefault));
        String OrderBy = sharedPreferences.getString(getString(R.string.ListPreferenceKey), getString(R.string.ListPreferenceDefault));
        Uri baseUrl = Uri.parse(url);
        Uri.Builder builder = baseUrl.buildUpon();
       builder.appendQueryParameter("q", Section);
        builder.appendQueryParameter("order-by", OrderBy);
        builder.appendQueryParameter("api-key", "0eb0d519-ec54-489f-9ad3-79e68d5d74be");

        return new NewsLoaderActivity(this, builder.toString());

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsInfoActivity>> loader, List<NewsInfoActivity> data) {
        emptyView.setText("No News Found");
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        newsAdapterActivity.clear();

        if (data != null && !data.isEmpty()) {
            newsAdapterActivity.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsInfoActivity>> loader) {
        newsAdapterActivity.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId==R.id.prefernces) {
            Intent intent = new Intent(this, NewsSettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

