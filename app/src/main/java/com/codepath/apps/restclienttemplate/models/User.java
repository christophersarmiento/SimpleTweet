package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
  public String name;
  public String screenName;
  public String profileImageUrl;

  public static User fromJson(JSONObject json) throws JSONException {
    User user = new User();
    user.name = json.getString("name");
    user.screenName = json.getString("screen_name");
    user.profileImageUrl = json.getString("profile_image_url_https");
    return user;
  }
}
