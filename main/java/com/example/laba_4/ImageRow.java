package com.example.laba_4;

import android.graphics.Bitmap;

public class ImageRow {
    String[] s;
    int[] scale;
    public ImageRow(String s1, String s2, int per1, int per2){
        this.s = new String[]{s1, s2};
        this.scale = new int[]{per1, per2};
    }
    public String[] getS(){
        return s;
    }
    public int[] getScale() { return scale; }
}
