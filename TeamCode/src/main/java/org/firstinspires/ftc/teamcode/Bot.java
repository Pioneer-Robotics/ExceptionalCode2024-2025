package org.firstinspires.ftc.teamcode;


import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Hardware.BotIMU;
import org.firstinspires.ftc.teamcode.Hardware.Camera.AprilTagProcessor;
import org.firstinspires.ftc.teamcode.Hardware.LEDController;
import org.firstinspires.ftc.teamcode.Hardware.LinearSlide;
import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;
import org.firstinspires.ftc.teamcode.Hardware.VoltageHandler;
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
    public VoltageHandler voltageHandler;
    public WebcamName webcam;
    public AprilTagProcessor aprilTagProcessor;

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
        voltageHandler = new VoltageHandler(opMode);
        webcam = opMode.hardwareMap.get(WebcamName.class, Config.webcam);
        aprilTagProcessor = new AprilTagProcessor(webcam);
    }
}
