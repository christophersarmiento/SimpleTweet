package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
  public String body;
  public String createdAt;
  public long id;
  public User user;
  public String name;

  public static Tweet fromJson(JSONObject json) throws JSONException {
    Tweet tweet = new Tweet();
    tweet.body = json.getString("text");
    tweet.createdAt = json.getString("created_at");
    tweet.id = json.getLong("id");
    tweet.user = User.fromJson(json.getJSONObject("user"));
    return tweet;
  }

  public static List<Tweet> fromJsonArray(JSONArray json) throws JSONException {
    List<Tweet> tweets = new ArrayList<>();
    for (int i = 0; i< json.length(); i++){
      tweets.add(fromJson(json.getJSONObject(i)));
    }
    return tweets;
  }
}
