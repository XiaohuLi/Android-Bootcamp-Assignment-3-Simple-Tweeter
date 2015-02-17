package com.codepath.apps.mysimpletweets.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohu on 2/15/15.
 */
public abstract class TweetsListFragment extends Fragment{
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private EndlessScrollListener listener;
    private TwitterClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        client = TwitterApplication.getRestClient();
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                //customLoadMoreDataFromApi(page);
                long lastId = tweets.get(tweets.size() - 1).getUid();
                populateTimelineLessThan(lastId - 1);


                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
        return v;
    }

    protected abstract void populateTimelineLessThan(long id);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
        super.onCreate(savedInstanceState);
    }

    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }

    public void clear(){
        aTweets.clear();
        aTweets.notifyDataSetChanged();
    }

    public void setOnScrollListener(EndlessScrollListener listener){
        this.listener = listener;
    }

    public ArrayList<Tweet> getTweets(){
        return tweets;
    }
}
