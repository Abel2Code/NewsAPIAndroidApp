package com.example.abel.newsapiandroidapp;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abel.newsapiandroidapp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private static final String TAG = "MainActivity";
    private String newsSearchResults;
    private static final int LOADER_ID = 1;

    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private TextView mTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateLoader(0 , savedInstanceState);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        setSupportActionBar(mToolbar);

        mTextView = (TextView) findViewById(R.id.queryJSON);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.get_news) {
            Bundle bundle = new Bundle();
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> newsSearchLoader = loaderManager.getLoader(LOADER_ID);
            if(newsSearchLoader == null){
                loaderManager.initLoader(LOADER_ID, bundle, this).forceLoad();
            }else{
                loaderManager.restartLoader(LOADER_ID, bundle, this).forceLoad();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private URL makeSearchUrl() {
        URL newsSearchURL = NetworkUtils.buildUrl();
        return newsSearchURL;
    }

    private void populateWithNewsData(String data){
        mTextView.setText(data);
    }

    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, final Bundle args) {
        Log.d(TAG, NetworkUtils.buildUrl() + "\n\n\n\n\n");
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                Log.d(TAG, "onStartLoading called");
                super.onStartLoading();
                if(args == null){
                    Log.d(TAG, "bundle null");
                    return;
                }
                mTextView.setText(R.string.waiting_for_search);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public String loadInBackground() {
                Log.d(TAG, "loadInBackground called");

                try {
                    Log.d(TAG, "begin network call");
                    newsSearchResults = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, newsSearchResults);
                return newsSearchResults;
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {
        Log.d("mycode", data);
        mProgressBar.setVisibility(View.GONE);
        populateWithNewsData(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {}
}
