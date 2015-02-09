package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {
    private ImageView ivProfile;
    private TextView tvUserId;
    private EditText etTweet;
    private String screenName;
    private TextView tvCount;
    private TwitterClient client;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvUserId = (TextView) findViewById(R.id.tvUserId);
        etTweet = (EditText) findViewById(R.id.etTweet);
        tvCount = (TextView) findViewById(R.id.tvCount);
        client = TwitterApplication.getRestClient();
        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCount.setText(String.valueOf(140 - (etTweet.getText().toString().length())));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getUserProfileAndId();
    }

    private void getUserProfileAndId() {
        client.getAccountSetting(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    screenName = response.getString("screen_name");
                    client.getUserInfoByScreenName(screenName, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            user = User.fromJSON(response);
                            Picasso.with(ComposeActivity.this).load(user.getProfileImageUrl()).into(ivProfile);
                            tvUserId.setText(user.getScreenName());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Toast.makeText(ComposeActivity.this, responseString, Toast.LENGTH_SHORT).show();
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.d("debug", "success");
            }


        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void onCancel(View v){
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        this.finish();
    }

    public void onTweet(View v){
        client.postNewTweet(etTweet.getText().toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                Toast.makeText(ComposeActivity.this, "Tweet published!", Toast.LENGTH_SHORT).show();
                ComposeActivity.this.finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ComposeActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
