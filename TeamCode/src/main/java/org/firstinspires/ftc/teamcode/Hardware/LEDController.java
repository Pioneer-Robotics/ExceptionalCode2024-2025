package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

/**
 * This class is used to control the LED lights on the robot.
 */
public class LEDController {
    RevBlinkinLedDriver led;

    public LEDController() {
        led = Bot.opMode.hardwareMap.get(RevBlinkinLedDriver.class, Config.led);
    }

    /**
     * Turns on the lights with a specific pattern from RevBlinkinLedDriver.
     * @param revBlinkinLedDriver BlinkinPattern
     */
    public void lightsOn(RevBlinkinLedDriver.BlinkinPattern revBlinkinLedDriver){
        led.setPattern(revBlinkinLedDriver);
    }

    /**
     * Turns off the lights.
     */
    public void lightsOff(){
        led.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
        led.close();
    }
}
