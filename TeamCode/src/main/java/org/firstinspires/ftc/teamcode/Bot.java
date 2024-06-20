package org.firstinspires.ftc.teamcode;


import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.BotIMU;
import org.firstinspires.ftc.teamcode.Hardware.LEDController;
import org.firstinspires.ftc.teamcode.Hardware.LinearSlide;

/**
 * This class is used to create all of the hardware objects and store them in the bot object
 * This is used to simplify the code and make it easier to read
 * This also makes it easier to create new objects
 */
public class Bot {
    public BotIMU imu;
    public LinearSlide slide;
    public LEDController led;

    /**
     * Constructor for Bot.
     * @param opMode LinearOpMode
     */
    public Bot(@NonNull LinearOpMode opMode){
        imu = new BotIMU(opMode);
        slide = new LinearSlide(opMode);
        led = new LEDController(opMode);
    }
}
