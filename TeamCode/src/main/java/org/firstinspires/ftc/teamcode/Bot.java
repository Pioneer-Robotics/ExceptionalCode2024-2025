package org.firstinspires.ftc.teamcode;


import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.BotIMU;
import org.firstinspires.ftc.teamcode.Hardware.LEDController;
import org.firstinspires.ftc.teamcode.Hardware.LinearSlide;
import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PIDController;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.Pose;

/**
 * This class is used to create all of the hardware objects and store them in the bot object
 * This is used to simplify the code and make it easier to read
 * This also makes it easier to create new objects
 */
public class Bot {
    public PIDController pidController;
    public BotIMU imu;
    public LinearSlide slide;
    public LEDController led;
    public MecanumBase mecanumBase;
    public Pose pose;

    /**
     * Constructor for Bot.
     * @param opMode LinearOpMode
     */
    public Bot(@NonNull LinearOpMode opMode){
        imu = new BotIMU(opMode);
        slide = new LinearSlide(opMode);
        led = new LEDController(opMode);
        mecanumBase = new MecanumBase(opMode, this);
        pose = new Pose(opMode);
        pidController = new PIDController(opMode, this); // Uses the same pose object as the bot
    }
}
