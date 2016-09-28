package com.example.a00227178.myapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class SquadBuilder extends ActionBarActivity {
    ArrayAdapter<String> adapter;
    View content;
    String m_Text = "";
    List<String> list = new ArrayList<String>();
    ArrayList<String> myList = new ArrayList<String>();
    ListView listView;
    String teamName = "";
    View topLevelLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getBoolean(R.bool.tablet)){
            System.out.println(R.bool.tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_squad_builder);
        topLevelLayout = findViewById(R.id.top_layout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teamName = extras.getString("team");
            setTitle(teamName);
        }
        if (isFirstTime()) {
            //topLevelLayout.setVisibility(View.INVISIBLE);
        }
        content = findViewById(R.id.team_layout);
        listView = (ListView) findViewById(R.id.listview);
        ArrayList<String> squad = loadInSquad();

        for(int i = 0; i < squad.size(); i++) {
            myList.add(squad.get(i).toString());
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, myList);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select " +adapter.getItem(info.position) + " Position");
        menu.add(0, v.getId(), 1, "1: Goalkeeper");//groupId, itemId, order, title
        menu.add(0, v.getId(), 2, "2: Right-Corner Back");
        menu.add(0, v.getId(), 3, "3: Full-Back");
        menu.add(0, v.getId(), 4, "4: Left-Corner Back");
        menu.add(0, v.getId(), 5, "5: Right-Half Back");
        menu.add(0, v.getId(), 6, "6: Centre-Half Back");
        menu.add(0, v.getId(), 7, "7: Left-Half Back");
        menu.add(0, v.getId(), 8, "8: Midfield-1");
        menu.add(0, v.getId(), 9, "9: Midfield-2");
        menu.add(0, v.getId(), 10, "10: Right-Half Forward");
        menu.add(0, v.getId(), 11, "11: Centre Forward");
        menu.add(0, v.getId(), 12, "12: Left-Half Forward");
        menu.add(0, v.getId(), 13, "13: Right-Corner Forward");
        menu.add(0, v.getId(), 14, "14: Full-Forward");
        menu.add(0, v.getId(), 15, "15: Left-Corner Forward");
        menu.add(0, v.getId(), 16, "DELETE FROM SQUAD");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemTitle = item.getOrder();
        //16 is to delete
        if(menuItemTitle == 16)
        {
            myList.remove(info.position);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            registerForContextMenu(listView);
        }
        else {
            String listItemName = myList.get(info.position);
            TextView tv1 = getTextView(menuItemTitle);
            tv1.setText(listItemName);
            Toast.makeText(getApplicationContext(), listItemName + " is now at " + menuItemTitle, Toast.LENGTH_LONG).show();
        }
        return true;
    }
    private void addPlayerToSquad()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Player");

        // Set up the input
        final EditText input = new EditText(this);

        input.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                myList.add(m_Text);
                Toast.makeText(getApplicationContext(), m_Text+" has been added to "+teamName, Toast.LENGTH_LONG).show();
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
    private String checkIfOnTeam(String playerName)
    {
        String fullTitle;
        for(int i = 1; i < 15;i++)
        {
            TextView tv = getTextView(i);
            if(tv.getText().equals(playerName))
            {
                fullTitle = playerName + ","+i;
                return fullTitle;
            }
        }
        return playerName;

    }
    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        //if (!ranBefore) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
            topLevelLayout.setVisibility(View.VISIBLE);
            topLevelLayout.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    topLevelLayout.setVisibility(View.INVISIBLE);
                    return false;
                }

            });


        //}
        return ranBefore;

    }
    private void saveSquadList(ArrayList<String> data) {
        String path = Environment.getExternalStorageDirectory() + "/MatchDayGAA/";

        File dir = new File(path);
        dir.mkdirs();

        File file = new File(path +teamName+".txt");

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter osw = new OutputStreamWriter(fOut);

        try {
            for(int i = 0; i < data.size(); i++) {
                String pName = data.get(i).toString();
                pName = checkIfOnTeam(pName);
                osw.write(pName + "\n");
            }
            osw.flush();
            osw.close();
            Toast.makeText(getApplicationContext(), teamName+" has been saved", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ArrayList<String> loadInSquad()
    {
        String path = Environment.getExternalStorageDirectory() + "/MatchDayGAA/";
        File file = new File(path +teamName+".txt");
        //Read text from file
        ArrayList<String> text = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String pName = "";
            Integer pNum = 0;

            while ((line = br.readLine()) != null) {
                if(line.contains(","))
                {
                    String[] parts = line.split(",");
                    pName = parts[0];
                    pNum = Integer.valueOf(parts[1]);
                }
                else
                    pName = line;

                text.add(pName);
                if(pNum != 0) {
                    TextView tv = getTextView(pNum);
                    tv.setText(pName);
                    pNum=0;
                }
            }
            Toast.makeText(getApplicationContext(), teamName+" has successfully been loaded", Toast.LENGTH_LONG).show();
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text;
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    private void captureScreen() {
        View v = findViewById(R.id.team_layout);
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        try {
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "MatchDayGAAScreenShot"
                    + System.currentTimeMillis() + ".png");
            FileOutputStream fos = new FileOutputStream(f);
            System.out.println(fos.toString());
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

        openScreenshot(f);
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
    public TextView getTextView(int pos)
    {
        TextView tv =null;
        if(pos == 1)
            tv = (TextView)findViewById(R.id.goalkeeper);
        else if(pos == 2)
            tv = (TextView)findViewById(R.id.rcb);
        else if(pos == 3)
            tv = (TextView)findViewById(R.id.fullback);
        else if(pos == 4)
            tv = (TextView)findViewById(R.id.lcb);
        else if(pos == 5)
            tv = (TextView)findViewById(R.id.rhb);
        else if(pos == 6)
            tv = (TextView)findViewById(R.id.center_back);
        else if(pos == 7)
            tv = (TextView)findViewById(R.id.lhb);
        else if(pos == 8)
            tv = (TextView)findViewById(R.id.midfield1);
        else if(pos == 9)
            tv = (TextView)findViewById(R.id.midfield2);
        else if(pos == 10)
            tv = (TextView)findViewById(R.id.rhf);
        else if(pos == 11)
            tv = (TextView)findViewById(R.id.centre_forward);
        else if(pos == 12)
            tv = (TextView)findViewById(R.id.lhf);
        else if(pos == 13)
            tv = (TextView)findViewById(R.id.rff);
        else if(pos == 14)
            tv = (TextView)findViewById(R.id.full_forward);
        else if(pos == 15)
            tv = (TextView)findViewById(R.id.lff);

        return tv;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_squad_builder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_screen) {
            captureScreen();
            return true;
        }
        if (id == R.id.action_add) {
            addPlayerToSquad();

            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            registerForContextMenu(listView);
            return true;
        }
        if (id == R.id.action_save) {
            saveSquadList(myList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
