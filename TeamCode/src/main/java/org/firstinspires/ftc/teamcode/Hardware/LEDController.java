package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * This class is used to control the LED lights on the robot.
 */
public class LEDController {
    RevBlinkinLedDriver led;
    /**
     * Constructor for LEDController.
     * @param opMode LinearOpMode
     */
    public LEDController(LinearOpMode opMode){
        led = opMode.hardwareMap.get(RevBlinkinLedDriver.class, "LED");
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
        led.close();
    }
}
