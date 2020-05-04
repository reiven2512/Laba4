package com.example.laba_4;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LikeActivity extends AppCompatActivity {
    DbHelper dbHelper;
    ListView lv;
    List<ImageRow> ir;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_activity);
        dbHelper = new DbHelper(this);
        lv = findViewById(R.id.list);
        ir = dbHelper.getAllImages();
        ListAdapter listAdapter = new ListAdapter(this, R.layout.list_item, ir, false);
        lv.setAdapter(listAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.first)
        {
            startActivity(new Intent(LikeActivity.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        dbHelper.close();
        super.onDestroy();
    }
}
