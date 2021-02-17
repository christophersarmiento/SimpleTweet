package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

  public static final String TAG = "TimelineActivity";
  TwitterClient client;
  RecyclerView rvTweets;
  List<Tweet> tweets;
  TweetsAdapter adapter;
  SwipeRefreshLayout swipeContainer;
  EndlessRecyclerViewScrollListener scrollListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    client = TwitterApp.getRestClient(this);

    swipeContainer = findViewById(R.id.swipeContainer);
    swipeContainer.setColorSchemeColors(getResources().getColor(R.color.twitterBlue));
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        Log.i(TAG, "fetching new data");
        populateHomeTimeline();
      }
    });

    rvTweets = findViewById(R.id.rvTweets);
    tweets = new ArrayList<>();
    adapter = new TweetsAdapter(this, tweets);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    rvTweets.setLayoutManager(layoutManager);
    rvTweets.setAdapter(adapter);

    scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        Log.i(TAG, "onLoadMore"+ page);
        loadMoreData();
      }
    };

    rvTweets.addOnScrollListener(scrollListener);

    populateHomeTimeline();
  }

  private void loadMoreData() {
    client.getNextPageOfTweets(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        Log.i(TAG,"loadMoreData onSuccess");
        JSONArray array = json.jsonArray;
        try {
          List<Tweet> tweets = Tweet.fromJsonArray(array);
          adapter.addAll(tweets);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
        Log.e(TAG,"loadMoreData onFailure", throwable);
      }
    }, tweets.get(tweets.size() - 1).id);
  }

  private void populateHomeTimeline() {
    client.getHomeTimeline(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        Log.i(TAG,"onSuccess" + json.toString());
        JSONArray array = json.jsonArray;
        try {
          adapter.clear();
          adapter.addAll(Tweet.fromJsonArray(array));
          swipeContainer.setRefreshing(false);
        } catch (JSONException e) {
          Log.e(TAG, "JSON Exception", e);
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
        Log.i(TAG,"onFailure " + response,throwable);
      }
    });
  }

}