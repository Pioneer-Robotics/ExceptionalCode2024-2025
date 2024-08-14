package org.firstinspires.ftc.teamcode;


import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.BotIMU;
import org.firstinspires.ftc.teamcode.Hardware.Collector;
import org.firstinspires.ftc.teamcode.Hardware.LEDController;
import org.firstinspires.ftc.teamcode.Hardware.LinearSlide;
import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;
import org.firstinspires.ftc.teamcode.Hardware.ServoClass;
import org.firstinspires.ftc.teamcode.Hardware.VoltageHandler;
import org.firstinspires.ftc.teamcode.Helpers.Konami;
import org.firstinspires.ftc.teamcode.Helpers.Ticker;
import org.firstinspires.ftc.teamcode.Helpers.Utils;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PIDController;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.Pose;

/**
 * This class is used to create all of the hardware objects and store them in the bot object
 * This is used to simplify the code and make it easier to read
 * This also makes it easier to create new objects
 */
public class Bot {
    public static LinearOpMode opMode;
    public static PIDController pidController;
    public static BotIMU imu;
    public static LinearSlide slide;
    public static LEDController led;
    public static MecanumBase mecanumBase;
    public static Pose pose;
    public static VoltageHandler voltageHandler;
    public static Ticker ticker;
    public static Collector collector;
    public static ServoClass pixelDropLeft, pixelDropRight, gripper, wrist;
    public static Utils utils;
    public static Konami konami;

    /**
     * Constructor for Bot.
     * @param opMode LinearOpMode
     */
    public static void init(@NonNull LinearOpMode opMode) {
        Bot.opMode = opMode;

        // Drive base and self driving
        Bot.pose = new Pose(); // Needs to be initialized before pidController
        Bot.mecanumBase = new MecanumBase();
        Bot.pidController = new PIDController();

        // Motors
        Bot.slide = new LinearSlide();
        Bot.collector = new Collector();

        // Servos
        Bot.pixelDropLeft = new ServoClass(opMode.hardwareMap.get(Servo.class, Config.leftDropServo), Config.leftOpenPos, Config.leftClosedPos, Config.leftClosedPos);
        Bot.pixelDropRight = new ServoClass(opMode.hardwareMap.get(Servo.class, Config.rightDropServo), Config.rightOpenPos, Config.rightClosedPos, Config.rightClosedPos);
        Bot.gripper = new ServoClass(opMode.hardwareMap.get(Servo.class, Config.gripperServo), Config.gripperOpen, Config.gripperClosed, Config.gripperOpen);
        Bot.wrist = new ServoClass(opMode.hardwareMap.get(Servo.class, Config.wristServo), Config.wristVertical, Config.wristHorizontal, Config.wristHorizontal);

        // Other
        Bot.voltageHandler = new VoltageHandler();
        Bot.imu = new BotIMU();
        Bot.led = new LEDController();
    }
}
