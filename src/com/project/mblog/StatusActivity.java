package com.project.mblog;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class StatusActivity extends Activity implements OnClickListener, TextWatcher, OnSharedPreferenceChangeListener {
	
	

	
	private static final String TAG = "StatusActivity";
	static String TWITTER_CONSUMER_KEY = "";
    static String TWITTER_CONSUMER_SECRET = "";
    TextView textCount;
 
	private static final String access_token = """;
	private static final String access_token_secret = "";
	private static final String TWITTER_CALLBACK_URL = "http://mblog.project.com";
	
	 // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
 
 
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	
    private static Twitter twitter;
    private static RequestToken requestToken;
     
    // Shared Preferences
   private static SharedPreferences prefs;
   private static SharedPreferences mprefs;
  
	EditText editText;
	Button updateButton;
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		editText = (EditText) findViewById(R.id.editText); // 
		updateButton = (Button) findViewById(R.id.buttonUpdate);
		updateButton.setOnClickListener(this); // 
		
		textCount = (TextView) findViewById(R.id.textCount); // 
		textCount.setText(Integer.toString(140)); // 
		textCount.setTextColor(Color.GREEN); //
		editText.addTextChangedListener(this);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this); // 
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		mprefs= getApplicationContext().getSharedPreferences(
                "MyPref", 0);
 
			
		loginToTwitter();
	       	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) { // 
			case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class)); // 
			break;
	}
	return true; // 
	}
	
	
	
	private void loginToTwitter( ) {
        // Check if already logged in

		
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            //builder.setPassword( prefs.getString("password", ""));
            //builder.setUser(prefs.getString("username", ""));
           builder.setPassword("naturelover123@");
            builder.setUser("@PrasadMarne");
            //Configuration configuration = builder.build();
            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            
 
           
            twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
           
       } 
    
 
    private boolean isTwitterLoggedInAlready() {
		// TODO Auto-generated method stub
	return mprefs.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}
	
	private Twitter gettwitter(){
		if(isTwitterLoggedInAlready())
		return twitter;
		else
			return null;
	}
	// Asynchronously posts to twitter
	class PostToTwitter extends AsyncTask<String, Integer, String> { // 
	
	@Override
	protected String doInBackground(String... statuses) { // 
				try {
					/*
					ConfigurationBuilder builder = new ConfigurationBuilder();
	                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
	                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
	                 
	                // Access Token 
	                String access_token = mprefs.getString(PREF_KEY_OAUTH_TOKEN, "");
	                // Access Token Secret
	                String access_token_secret = mprefs.getString(PREF_KEY_OAUTH_SECRET, "");
	                 
	                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
	                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);*/
					twitter4j.Status status = twitter.updateStatus(statuses[0]);
					return status.getText();
				} 
				catch (TwitterException e) {
					Log.e(TAG, e.toString());
					e.printStackTrace();
					return "Failed to post";
				}
	}
	// Called when there's a status to be updated
	@Override
	protected void onProgressUpdate(Integer... values) { // 
				super.onProgressUpdate(values);
	// Not used in this case
	}
	// Called once the background activity has completed
	@Override
	protected void onPostExecute(String result) { // 
				Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
				}
	}
	
	
	
	
	public void onClick(View v) {
	String status = editText.getText().toString();
	new PostToTwitter().execute(status); // 
	Log.d(TAG, "onClicked");
	}
		
	

	
	
	public void afterTextChanged(Editable statusText) { // 
		int count = 140 - statusText.length(); // 
		textCount.setText(Integer.toString(count));
		textCount.setTextColor(Color.GREEN); // 
		if (count < 10)
		textCount.setTextColor(Color.YELLOW);
		if (count < 0)
		textCount.setTextColor(Color.RED);
		}
	
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after) { // 
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		
		twitter=null;
		
	}
}

