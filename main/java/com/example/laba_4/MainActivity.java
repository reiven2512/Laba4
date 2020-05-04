package com.example.laba_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final static String API = "dd580d8c-dc8c-4ec7-9fd7-d56ecb330501";
    List<Info> lt;
    List<ImageRow> ir;
    String curUrl;
    boolean breed;
    Spinner sp;
    Button bt;
    ListView lv;
    ListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        lt = new ArrayList<>();
        ir = new ArrayList<>();
        download();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = findViewById(R.id.spinner);
        bt = findViewById(R.id.button);
        lv = findViewById(R.id.list);
        listAdapter = new ListAdapter(this, R.layout.list_item, ir, true);
        lv.setAdapter(listAdapter);
        List<String> names = new ArrayList<>();
        names.add("None");
        for(Info item : lt){
            names.add(item.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setPrompt("Breed");
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    curUrl = "https://api.thecatapi.com/v1/images/search?limit=100";
                    breed = false;
                }
                else{
                    String str = lt.get(position-1).getId();
                    curUrl = "https://api.thecatapi.com/v1/images/search?limit=100&breed_id=" + str;
                    breed = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download2(curUrl, breed);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.second)
        {
            startActivity(new Intent(MainActivity.this, LikeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void download()
    {
        Thread thread = new Thread() {
            public void run() {
                try {
                        URL url = new URL("https://api.thecatapi.com/v1/breeds");
                        HttpURLConnection huc= (HttpURLConnection) url.openConnection();
                        huc.setRequestProperty("x-api-key", API);
                        ReadableByteChannel rbc = Channels.newChannel(huc.getInputStream());
                        FileOutputStream fos = openFileOutput("file.json", Context.MODE_PRIVATE);
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        fos.close();
                        rbc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileReader fr = new FileReader("/data/user/0/com.example.laba_4/files/file.json");
                    JSONParser parser = new JSONParser();
                    JSONArray ja = (JSONArray) parser.parse(fr);
                    for (Object o : ja) {
                        JSONObject breed = (JSONObject) o;
                        String name = (String) breed.get("name");
                        String id = (String) breed.get("id");
                        lt.add(new Info(name, id));
                    }
                } catch (ParseException d) {
                    d.printStackTrace();
                } catch (IOException d) {
                    d.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void download2(final String str, final boolean br)
    {
        Thread thread = new Thread() {
            public void run() {
                ir.clear();
                try {
                    URL url = new URL(str);
                    HttpURLConnection huc= (HttpURLConnection) url.openConnection();
                    huc.setRequestProperty("x-api-key", API);
                    ReadableByteChannel rbc = Channels.newChannel(huc.getInputStream());
                    FileOutputStream fos = openFileOutput("file2.json", Context.MODE_PRIVATE);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();
                    rbc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileReader fr = new FileReader("/data/user/0/com.example.laba_4/files/file2.json");
                    JSONParser parser = new JSONParser();
                    JSONArray ja = (JSONArray) parser.parse(fr);
                    for (int i = 0; i < ja.size(); i+=2){
                        Object o = ja.get(i);
                        JSONObject breed = (JSONObject) o;
                        String url1 = (String) breed.get("url");
                        int per1 = 0;
                        if(br){
                            Long tmp = (Long) breed.get("width");
                            Double width = (double) tmp;
                            tmp = (Long) breed.get("height");
                            Double height = (double) tmp;
                            if(width > height){
                                double k = 340.0/width;
                                k = k*100;
                                per1 = (int) k;
                            }
                            else{
                                double k = 360.0/height;
                                k = k*100;
                                per1 = -((int) k);
                            }
                        }

                        String url2 = null;
                        int per2 = 0;
                        if(i+1 < ja.size()){
                            o = ja.get(i+1);
                            breed = (JSONObject) o;
                            url2 = (String) breed.get("url");
                            if(br){
                                Long tmp = (Long) breed.get("width");
                                Double width = (double) tmp;
                                tmp = (Long) breed.get("height");
                                Double height = (double) tmp;
                                if(width > height){
                                    double k = 340.0/width;
                                    k = k*100;
                                    per2 = (int) k;
                                }
                                else{
                                    double k = 360.0/height;
                                    k = k*100;
                                    per2 = -((int) k);
                                }
                            }
                        }
                        ir.add(new ImageRow(url1, url2, per1, per2));
                    }
                } catch (ParseException d) {
                    d.printStackTrace();
                } catch (IOException d) {
                    d.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
            listAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
