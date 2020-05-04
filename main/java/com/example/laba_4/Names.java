package com.example.laba_4;

public final class Names {
    public static class con{
        public static final String TABLE = "Images";
        public static final String URL = "URL";
        public static final String SCALE = "SCALE";
        public static final String DB_CREATE = "create table " + TABLE + "("
                + URL + " text primary key, "
                + SCALE + " integer"
                + ");";
    }
}
