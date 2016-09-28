package com.example.a00227178.myapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ronan on 03/04/2016.
 */
public class MatchDay extends ActionBarActivity {

    String homeTeam = "";
    String awayTeam = "";
    String location = "";
    String title = "";
    String newTime = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //When the game is on a tablet set it to landscape
        if (getResources().getBoolean(R.bool.tablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_matchday);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            homeTeam = extras.getString("homeTeam");
            awayTeam = extras.getString("awayTeam");
            location = extras.getString("location");
            title = extras.getString("matchTitle");
        }

        TextView home =(TextView)findViewById(R.id.homeTeamName);
        TextView away =(TextView)findViewById(R.id.awayTeamName);
        Button homeGoalAdd =(Button)findViewById(R.id.addHomeGoal);
        Button homePointAdd =(Button)findViewById(R.id.addHomePoint);
        Button awayGoalAdd =(Button)findViewById(R.id.addAwayGoal);
        Button awayPointAdd =(Button)findViewById(R.id.addAwayPoint);

        home.setText(homeTeam);
        away.setText(awayTeam);
        homeGoalAdd.setText(homeTeam+" Goal");
        homePointAdd.setText(homeTeam+" Point");
        awayGoalAdd.setText(awayTeam+" Goal");
        awayPointAdd.setText(awayTeam + " Point");


    }
    public String getDetails()
    {
        TextView currentHomeGoals = (TextView) findViewById(R.id.homeGoals);
        TextView currentHomePoints = (TextView) findViewById(R.id.homePoints);
        TextView currentAwayGoals = (TextView) findViewById(R.id.awayGoals);
        TextView currentAwayPoints = (TextView) findViewById(R.id.awayPoints);
        TextView halfIndicator = (TextView)findViewById(R.id.halfText);

        TextView homeTotal = (TextView) findViewById(R.id.homeTotal);
        TextView awayTotal = (TextView) findViewById(R.id.awayTotal);

        Chronometer ch = (Chronometer)findViewById(R.id.chronometer);
        String prev = halfIndicator.getText().toString();

        long elapsedMillis = SystemClock.elapsedRealtime() - ch.getBase();
        String time = String.format("%d min",
                TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)

        );
        if(halfIndicator.getText().toString().equals("Full Time") ||halfIndicator.getText().toString().equals("Half Time") ||halfIndicator.getText().toString().equals("Start 1st Half"))
        {

            time = "";
        }

        String s = (title +" "+ time + System.getProperty("line.separator")
                + halfIndicator.getText().toString() + System.getProperty("line.separator")
                + homeTeam +" "+currentHomeGoals.getText().toString()+"-"+currentHomePoints.getText().toString()+" "+homeTotal.getText().toString()+ System.getProperty("line.separator")
                + awayTeam +" "+currentAwayGoals.getText().toString()+"-"+currentAwayPoints.getText().toString()+" "+awayTotal.getText().toString()
                + System.getProperty("line.separator")+"in " + location);
        halfIndicator.setText(prev);
        return s;
    }
    public void btnClick(View v) {
        boolean twitterInstalled = false;
        try {
            ApplicationInfo info = getPackageManager().
                    getApplicationInfo("com.twitter.android", 0);
            twitterInstalled = true;
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        if (twitterInstalled) {
            TweetComposer.Builder builder = new TweetComposer.Builder(this)
                    .text(getDetails());
            builder.show();
        } else {

            final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                    .getActiveSession();

            final Intent intent = new ComposerActivity.Builder(MatchDay.this)
                    .session(session)
                    .createIntent();
            startActivity(intent);
        }
    }
    public void btnTimeClick(View v) {

        Chronometer ch = (Chronometer)findViewById(R.id.chronometer);
        Button timeButton = (Button)findViewById(R.id.timeManagement);
        TextView halfIndicator = (TextView)findViewById(R.id.halfText);

        if(timeButton.getText().equals("Start 1st Half")) {
            ch.setBase(SystemClock.elapsedRealtime());
            ch.start();
            halfIndicator.setText("1st Half");
            timeButton.setText("Half Time");
        }
        else if(timeButton.getText().equals("Half Time")) {
            ch.stop();
            halfIndicator.setText("Half Time");
            timeButton.setText("Start 2nd Half");
        }
        else if(timeButton.getText().equals("Start 2nd Half")) {
            ch.setBase(SystemClock.elapsedRealtime());
            ch.start();
            halfIndicator.setText("2nd Half");
            timeButton.setText("Full Time");
        }
        else if(timeButton.getText().equals("Full Time")) {
            ch.stop();
            halfIndicator.setEnabled(false);
            halfIndicator.setText("Full Time");
        }

    }
    public void minusHomeGoal(View v)
    {
        TextView halfIndicator = (TextView)findViewById(R.id.halfText);
        TextView currentHomeGoals = (TextView) findViewById(R.id.homeGoals);
        int homeGoals = Integer.valueOf(currentHomeGoals.getText().toString());
        if(halfIndicator.getText().toString().equals("Full Time") ||halfIndicator.getText().toString().equals("Half Time") ||halfIndicator.getText().toString().equals("Throw-in"))
        {
            Toast.makeText(getApplicationContext(), "You must be in Game Time before adding score", Toast.LENGTH_LONG).show();
        }
        else if( homeGoals == 0)
        {
            Toast.makeText(getApplicationContext(), "No goals left to takeaway", Toast.LENGTH_LONG).show();
        }
        else {

            homeGoals = homeGoals - 1;
            currentHomeGoals.setText(String.valueOf(homeGoals));
            updateTotals();
        }
    }
    public void btnHomeGoalClick(View v) {
        TextView halfIndicator = (TextView)findViewById(R.id.halfText);
        if(halfIndicator.getText().toString().equals("Full Time") ||halfIndicator.getText().toString().equals("Half Time") ||halfIndicator.getText().toString().equals("Throw-in"))
        {
            Toast.makeText(getApplicationContext(), "You must be in Game Time before adding score", Toast.LENGTH_LONG).show();
        }
        else {
            TextView currentHomeGoals = (TextView) findViewById(R.id.homeGoals);
            int homeGoals = Integer.valueOf(currentHomeGoals.getText().toString());
            homeGoals = homeGoals + 1;
            currentHomeGoals.setText(String.valueOf(homeGoals));
            updateTotals();
        }
    }
    public void minusHomePoint(View v)
    {
        TextView halfIndicator = (TextView)findViewById(R.id.halfText);
        TextView currentHomePoints = (TextView) findViewById(R.id.homePoints);
        int homePoints = Integer.valueOf(currentHomePoints.getText().toString());
        if(halfIndicator.getText().toString().equals("Full Time") ||halfIndicator.getText().toString().equals("Half Time") ||halfIndicator.getText().toString().equals("Throw-in"))
        {
            Toast.makeText(getApplicationContext(), "You must be in Game Time before adding score", Toast.LENGTH_LONG).show();
        }
        else if(homePoints == 0)
        {
            Toast.makeText(getApplicationContext(), "No points left to takeaway", Toast.LENGTH_LONG).show();
        }
        else {

            homePoints = homePoints - 1;
            currentHomePoints.setText(String.valueOf(homePoints));
            updateTotals();
        }
    }
    public void btnHomePointClick(View v) {
        TextView halfIndicator = (TextView)findViewById(R.id.halfText);
        if(halfIndicator.getText().toString().equals("Full Time") ||halfIndicator.getText().toString().equals("Half Time") ||halfIndicator.getText().toString().equals("Throw-in"))
        {
            Toast.makeText(getApplicationContext(), "You must be in Game Time before adding score", Toast.LENGTH_LONG).show();
        }
        else {
            TextView currentHomePoints = (TextView) findViewById(R.id.homePoints);
            int homePoints = Integer.valueOf(currentHomePoints.getText().toString());
            homePoints = homePoints + 1;
            currentHomePoints.setText(String.valueOf(homePoints));
            updateTotals();
        }
    }
    public void minusAwayGoal(View v)
    {
        TextView halfIndicator = (TextView)findViewById(R.id.halfText);
        TextView currentAwayGoals = (TextView) findViewById(R.id.awayGoals);
        int awayGoals = Integer.valueOf(currentAwayGoals.getText().toString());

        if(halfIndicator.getText().toString().equals("Full Time") ||halfIndicator.getText().toString().equals("Half Time") ||halfIndicator.getText().toString().equals("Throw-in"))
        {
            Toast.makeText(getApplicationContext(), "You must be in Game Time before adding score", Toast.LENGTH_LONG).show();
        }
        else if(awayGoals == 0)
        {
            Toast.makeText(getApplicationContext(), "No goals left to takeaway", Toast.LENGTH_LONG).show();
        }
        else {
            awayGoals = awayGoals - 1;
            currentAwayGoals.setText(String.valueOf(awayGoals));
            updateTotals();
        }
    }
    public void btnAwayGoalClick(View v) {

        TextView halfIndicator = (TextView)findViewById(R.id.halfText);
        if(halfIndicator.getText().toString().equals("Full Time") ||halfIndicator.getText().toString().equals("Half Time") ||halfIndicator.getText().toString().equals("Throw-in"))
        {
            Toast.makeText(getApplicationContext(), "You must be in Game Time before adding score", Toast.LENGTH_LONG).show();
        }
        else {
            TextView currentAwayGoals = (TextView) findViewById(R.id.awayGoals);
            int awayGoals = Integer.valueOf(currentAwayGoals.getText().toString());
            awayGoals = awayGoals + 1;
            currentAwayGoals.setText(String.valueOf(awayGoals));
            updateTotals();
        }
    }
    public void minusAwayPoint(View v)
    {
        TextView halfIndicator = (TextView)findViewById(R.id.halfText);
        TextView currentAwayPoints = (TextView) findViewById(R.id.awayPoints);
        int awayPoints = Integer.valueOf(currentAwayPoints.getText().toString());
        if(halfIndicator.getText().toString().equals("Full Time") ||halfIndicator.getText().toString().equals("Half Time") ||halfIndicator.getText().toString().equals("Throw-in"))
        {
            Toast.makeText(getApplicationContext(), "You must be in Game Time before adding score", Toast.LENGTH_LONG).show();
        }
        else if(awayPoints == 0)
        {
            Toast.makeText(getApplicationContext(), "No points left to takeaway", Toast.LENGTH_LONG).show();
        }
        else {


            awayPoints = awayPoints - 1;
            currentAwayPoints.setText(String.valueOf(awayPoints));
            updateTotals();
        }
    }
    public void btnAwayPointClick(View v) {
        TextView halfIndicator = (TextView)findViewById(R.id.halfText);
        if(halfIndicator.getText().toString().equals("Full Time") || halfIndicator.getText().toString().equals("Half Time") ||halfIndicator.getText().toString().equals("Throw-in"))
        {
            Toast.makeText(getApplicationContext(), "You must be in Game Time before adding score", Toast.LENGTH_LONG).show();
        }
        else {
            TextView currentAwayPoints = (TextView) findViewById(R.id.awayPoints);
            int awayPoints = Integer.valueOf(currentAwayPoints.getText().toString());
            awayPoints = awayPoints + 1;
            currentAwayPoints.setText(String.valueOf(awayPoints));
            updateTotals();
        }
    }
    public void updateTotals()
    {
        TextView currentHomeGoals = (TextView) findViewById(R.id.homeGoals);
        int homeGoals = Integer.valueOf(currentHomeGoals.getText().toString());
        TextView currentHomePoints = (TextView) findViewById(R.id.homePoints);
        int homePoints = Integer.valueOf(currentHomePoints.getText().toString());
        TextView currentAwayGoals = (TextView) findViewById(R.id.awayGoals);
        int awayGoals = Integer.valueOf(currentAwayGoals.getText().toString());
        TextView currentAwayPoints = (TextView) findViewById(R.id.awayPoints);
        int awayPoints = Integer.valueOf(currentAwayPoints.getText().toString());

        TextView homeTotal = (TextView) findViewById(R.id.homeTotal);
        TextView awayTotal = (TextView) findViewById(R.id.awayTotal);

        int homeTotalScore = (homeGoals * 3) + homePoints;
        int awayTotalScore = (awayGoals * 3) + awayPoints;

        homeTotal.setText("("+String.valueOf(homeTotalScore)+")");
        awayTotal.setText("(" + String.valueOf(awayTotalScore) + ")");
    }
    public void clockClick(View v) {
        Button timeButton = (Button)findViewById(R.id.timeManagement);
        if(timeButton.getText().equals("Start 1st Half"))
        {
            Toast.makeText(getApplicationContext(), "You must Start the game before editing time", Toast.LENGTH_LONG).show();
        }
        else if(timeButton.getText().equals("Start 2nd Half"))
        {
            Toast.makeText(getApplicationContext(), "You begin 2nd Half before editing time", Toast.LENGTH_LONG).show();
        }
        else
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Set Current Minute");
            final Chronometer ch = (Chronometer)findViewById(R.id.chronometer);
            // Set up the input
            final EditText input = new EditText(this);
            int maxLength = 2;
            input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
            input.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);

            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(input.getText().toString().equals("") || Integer.parseInt(input.getText().toString()) > 30 )
                    {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "Time cant be empty or more than 30", Toast.LENGTH_LONG).show();
                    }
                    else {
                        newTime = input.getText().toString();
                        ch.setBase(SystemClock.elapsedRealtime() - Integer.parseInt(newTime) * 60 * 1000);
                        Toast.makeText(getApplicationContext(), newTime, Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        new AlertDialog.Builder(this)
                .setTitle("Quit Match?")
                .setMessage("Are you sure you want to quit the game?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
