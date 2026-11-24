package org.example.threading;

public class Main {

    public static void main(String[] args) {

        System.out.println("main start");

        Runnable evenTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 2; i <= 20; i += 2) {
                    System.out.println("Even: " + i);
                }
            }
        };

        Runnable oddTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 20; i += 2) {
                    System.out.println("Odd: " + i);
                }
            }
        };

        Thread evenThread = new Thread(evenTask);
        Thread oddThread = new Thread(oddTask);

        evenThread.start();
        oddThread.start();

        System.out.println("main over");
    }
}
