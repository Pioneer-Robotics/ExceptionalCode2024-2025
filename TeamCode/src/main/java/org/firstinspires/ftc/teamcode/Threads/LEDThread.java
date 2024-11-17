package org.firstinspires.ftc.teamcode.Threads;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;
import org.firstinspires.ftc.teamcode.Hardware.LEDController;

import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * Runnable class for fun led patterns with multithreading
 * Convenient because you can start the thread and not
 * have to update it while opMode is running
 */
public class LEDThread implements Runnable {

    private final long patternTime;
    LEDController ledController;
    Random random = new Random();


    public LEDThread(long patternTime, LEDController ledController) {
        this.ledController = ledController;
        this.patternTime = patternTime;

    }

    public void run() {
        while (true) {
            int index = random.nextInt(BlinkinPattern.values().length);
            BlinkinPattern pattern = BlinkinPattern.values()[index];

            ledController.lightsOn(pattern);

            try { TimeUnit.SECONDS.sleep(patternTime); }
            catch (InterruptedException e) {throw new RuntimeException(e);}

        }
    }
}
