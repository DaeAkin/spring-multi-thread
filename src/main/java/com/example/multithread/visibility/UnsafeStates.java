package com.example.multithread.visibility;

public class UnsafeStates {
    private String[] states = new String[] {
        "AA","BB","CC"
    };
    // 내부적으로 사용할 변수를 외부에 공개하는건 좋지 않음!
    public String[] getStates() {return  states;}
}
