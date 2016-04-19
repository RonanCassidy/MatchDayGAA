package com.example.a00227178.myapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

/**
 * Created by Ronan on 03/04/2016.
 */
public class SetUpMatch extends ActionBarActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //When the game is on a tablet set it to landscape
        if (getResources().getBoolean(R.bool.tablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_setupmatch);
    }
    public void goMatchActivity(View v) {

        String home = ((EditText) findViewById(R.id.homeTeamText)).getText().toString().trim();
        String away = ((EditText)findViewById(R.id.awayTeamText)).getText().toString().trim();
        String location = ((EditText)findViewById(R.id.locationText)).getText().toString().trim();
        String matchTitle = ((EditText)findViewById(R.id.matchTitleText)).getText().toString().trim();

        if(home.equals("") || away.equals("") || location.equals("") || matchTitle.equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("You have not completed your setup")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else
        {
            Intent i = new Intent(getApplicationContext(), MatchDay.class);
            i.putExtra("homeTeam", home);
            i.putExtra("awayTeam", away);
            i.putExtra("location",location);
            i.putExtra("matchTitle",matchTitle);
            startActivity(i);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
