package com.example.multithread.visibility;

public class StaleInteger {
    private int value;

    public int get() { return value; }
    public void set(int value) {this.value = value;}
}
