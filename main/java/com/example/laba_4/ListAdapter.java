package com.example.laba_4;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ImageRow> {
    private LayoutInflater inflater;
    private int layout;
    private List<ImageRow> data;
    private boolean bt;
    public ListAdapter(Context context, int resource, List<ImageRow> data, boolean bt) {
        super(context, resource, data);
        this.data = data;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.bt = bt;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        WebView flagView1 = view.findViewById(R.id.flag1);
        flagView1.getSettings().setBuiltInZoomControls(true);
        flagView1.getSettings().setSupportZoom(false);

        WebView flagView2 = view.findViewById(R.id.flag2);
        flagView2.getSettings().setBuiltInZoomControls(true);
        flagView2.getSettings().setSupportZoom(false);

        Button b1 = view.findViewById(R.id.bt1);
        Button b2 = view.findViewById(R.id.bt2);
        Button b3 = view.findViewById(R.id.bt3);
        Button b4 = view.findViewById(R.id.bt4);

        String[] s = data.get(position).getS();
        int[] scale = data.get(position).getScale();

        if(scale[0] == 0){
            flagView1.getSettings().setUseWideViewPort(true);
            flagView1.getSettings().setLoadWithOverviewMode(true);
        }else{
            flagView1.setInitialScale(scale[0]);
        }
        flagView1.loadUrl(s[0]);

        if(bt){
            b1.setOnClickListener(new LikeListener(s[0], scale[0]));
            b2.setOnClickListener(new DisListener());
        }else{
            b1.setVisibility(View.GONE);
            b1.setEnabled(false);
            b2.setVisibility(View.GONE);
            b2.setEnabled(false);
        }

        if(s[1] != null){
            if(scale[1] == 0){
                flagView2.getSettings().setUseWideViewPort(true);
                flagView2.getSettings().setLoadWithOverviewMode(true);
            }else{
                flagView2.setInitialScale(scale[1]);
            }
            flagView2.loadUrl(s[1]);

            if(bt){
                b3.setOnClickListener(new LikeListener(s[1], scale[1]));
                b4.setOnClickListener(new DisListener());
            }else{
                b3.setVisibility(View.GONE);
                b3.setEnabled(false);
                b4.setVisibility(View.GONE);
                b4.setEnabled(false);
            }
        }
        else{
            b3.setEnabled(false);
            b3.setVisibility(View.GONE);
            b4.setEnabled(false);
            b4.setVisibility(View.GONE);
        }

        return view;
    }

    class DisListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Toast toast = Toast.makeText(getContext(), "Дизлайк!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }
    class LikeListener implements View.OnClickListener{
        String url;
        int scale;
        public LikeListener(String url, int scale){
            this.url = url;
            this.scale = scale;
        }
        @Override
        public void onClick(View v) {
            DbHelper dbHelper = new DbHelper(getContext());
            if(dbHelper.check(url)){
                Toast toast = Toast.makeText(getContext(), "Уже добавлено в понравившиеся!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else{
                dbHelper.insert(url, scale);
                Toast toast = Toast.makeText(getContext(), "Добавлено в понравившиеся!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            dbHelper.close();
        }
    }
}
