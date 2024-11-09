package org.firstinspires.ftc.teamcode;


import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardware.BotIMU;
import org.firstinspires.ftc.teamcode.Hardware.Collector;
import org.firstinspires.ftc.teamcode.Hardware.LEDController;
import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;
import org.firstinspires.ftc.teamcode.Hardware.ServoClass;
import org.firstinspires.ftc.teamcode.Hardware.SpecimenArm;
import org.firstinspires.ftc.teamcode.Hardware.VoltageHandler;
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
    public static LEDController led;
    public static MecanumBase mecanumBase;
    public static Pose pose;
    public static VoltageHandler voltageHandler;
    public static SpecimenArm specimenArm;

    /**
     * Constructor for Bot.
     * @param opMode LinearOpMode
     */
    public static void init(@NonNull LinearOpMode opMode) {
        Bot.opMode = opMode;

        // Drive base and self driving
        Bot.mecanumBase = new MecanumBase();

        // Motors
        specimenArm = new SpecimenArm();

        // Servos


        // Other
        Bot.voltageHandler = new VoltageHandler();
        Bot.imu = new BotIMU();
        Bot.led = new LEDController();
    }
}
