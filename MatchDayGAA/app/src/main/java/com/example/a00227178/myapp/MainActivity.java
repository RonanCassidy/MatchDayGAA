package com.example.a00227178.myapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.Console;
import java.net.URI;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "21ezbECEwOrCca0ntNnmzB9p1";
    private static final String TWITTER_SECRET = "J4UzGX3uVNDuWCFT74KOHkCgfOIC9p4cTEvEypmiF7o9m8n6da";

    TwitterLoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //When the game is on a tablet set it to lanscape
        if(getResources().getBoolean(R.bool.tablet)){
            System.out.println(R.bool.tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig),new TweetComposer());
        setContentView(R.layout.activity_main);

        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                String UserName = result.data.getUserName();
                Toast.makeText(MainActivity.this, UserName, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void btnClick(View v)
    {
        boolean twitterInstalled = false;

        try{
            ApplicationInfo info = getPackageManager().
                    getApplicationInfo("com.twitter.android", 0 );
            twitterInstalled = true;
        }
        catch( PackageManager.NameNotFoundException e )
        {
        }
        System.out.println("hellooooo " + twitterInstalled);
        if(twitterInstalled) {
            TweetComposer.Builder builder = new TweetComposer.Builder(this)
                    .text("@A00227178 this is placeholder text");
            builder.show();
        }
        else{

            final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                    .getActiveSession();
            System.out.println("hellooooo "+session);
            final Intent intent = new ComposerActivity.Builder(MainActivity.this)
                    .session(session)
                    .createIntent();
            startActivity(intent);
        }
    }
    public void goSquadActivity(View v)
    {
        Intent intent = new Intent(this, SquadBuilder.class);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
