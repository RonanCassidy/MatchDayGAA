package com.example.a00227178.myapp;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class SquadBuilder extends ActionBarActivity {

    String[] squad = {"Ronan Cassidy","Tom Devery","Johnny Kenny","Georgie Digan", "Darren Kelly","Colin Kenny","Colm Kelly","Pat Brennan",
            "Jordan Drennan","Steven Thompson","Billy Dunican", "Joe Kenny","Oisin Kelly","David Kelly","Ronan McGuire","Simon Goodman","B.Kenny"};
    ArrayAdapter<String> adapter;
    View content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getBoolean(R.bool.tablet)){
            System.out.println(R.bool.tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_squad_builder);
        content = findViewById(R.id.team_layout);
        ListView listView = (ListView) findViewById(R.id.listview);
        final ArrayList<String> list = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, squad);


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
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemTitle = item.getOrder();

        String listItemName = squad[info.position];
        TextView tv1 = getTextView(menuItemTitle);
        tv1.setText(listItemName);
        Toast.makeText(getApplicationContext(),listItemName+ " is now at " + menuItemTitle,Toast.LENGTH_LONG).show();

        return true;
    }
    private void addPlayerToSquad()
    {

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
