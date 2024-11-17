package org.firstinspires.ftc.teamcode.Threads;

import java.util.concurrent.TimeUnit;

/**
 * Basic runnable class used for multithreading
 * Has no real purpose but as an example for multithreading
 */
public class IncThread implements Runnable {
    private volatile int counter;
    public IncThread() {
        this.counter = 0;
    }

    public void run() {
        while(true) {
            counter++;
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getCounter() {
        return counter;
    }
}
