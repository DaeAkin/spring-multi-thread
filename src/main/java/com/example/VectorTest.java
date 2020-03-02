package com.example;

import java.util.Vector;

public class VectorTest {
    public static void main(String[] args) {
         Vector<Integer> vector = new Vector<>();
        vector.add(1);
        vector.add(2);
        vector.add(3);

        Thread vt = new VetorThread(vector);
        Thread vt2 = new VetorThread(vector);

        vt.start();
        vt2.start();


    }
    static class VetorThread extends Thread {
        private Vector<Integer> vector;

        VetorThread(Vector<Integer> vector) {
            this.vector = vector;
        }


        @Override
        public void run() {
            getLast(vector);
            deleteLast(vector);
        }
    }

    public static Object getLast(Vector list) {

        int lastIndex = list.size() - 1;
        System.out.println("getLast() size:" + list.size() + "and Thread : " + Thread.currentThread().getName());
        //오류
        return list.get(lastIndex);

    }

    public static Object deleteLast(Vector list) {

        int lastIndex = list.size() - 1;
        System.out.println("deleteLast() size:" + list.size() + "and :" + list.hashCode());
        return list.remove(lastIndex);
    }
}
