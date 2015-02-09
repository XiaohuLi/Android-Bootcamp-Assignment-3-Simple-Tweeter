package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xiaohu on 2/7/15.
 */
public class Tweet {
    private String body;
    private long uid;
    private User user;
    private int retweetCount;
    private int favCount;

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    //private User user;
    private String createdAt;

    public int getRetweetCount() {
        return retweetCount;
    }

    public int getFavCount() {
        return favCount;
    }

    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.favCount = jsonObject.getInt("favorite_count");
            tweet.retweetCount = jsonObject.getInt("retweet_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray response) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i<response.length(); i++){
            JSONObject tweetJson = null;
            try {
                tweetJson = response.getJSONObject(i);
                Tweet tweet = fromJSON(tweetJson);
                if(tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

        }
        return tweets;
    }
}
