package com.example.multithread;

import java.util.EventListener;

public class UnSafe {
    public final int foo = 42;

    public UnSafe(UnSafe[] leak) {
        System.out.println("?");
        leak[0] = this;   // Unsafe publication

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        // Make the "window of vulnerability" large

        System.out.println("?");
    }
}
 class Main2 {
    public static void main(String[] args) {
        final UnSafe[] leak = new UnSafe[1];
        new Thread(new Runnable() {
            public void run() {
                Thread.yield();   // (or sleep for a bit)
                new UnSafe(leak);
            }
        }).start();

        while (true) {
            if (leak[0] != null) {
                if (leak[0].foo == 42) {
                    System.err.println("OK");
                } else {
                    System.err.println("OUCH!");
                }
                System.exit(0);
            }
        }
    }
}
