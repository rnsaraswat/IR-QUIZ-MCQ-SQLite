package com.example.irquizmcqsqlite;

import android.util.Log;

public class Category {

    public static final int PROGRAMMING = 1;
    public static final int GEOGRAPHY = 2;
    public static final int MATH = 3;
    public static final int RAILWAY = 4;
    private int id;
    private String name;

    public Category() {
        Log.d("Ravi", "Category Category  Start 83");
    }

    public Category(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return getName();
    }
}
