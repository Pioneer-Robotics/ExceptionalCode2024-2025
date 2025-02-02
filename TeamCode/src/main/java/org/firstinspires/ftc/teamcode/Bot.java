package org.firstinspires.ftc.teamcode;
import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Hardware.BotIMU;
import org.firstinspires.ftc.teamcode.Hardware.ColorSensor;
import org.firstinspires.ftc.teamcode.Hardware.CurrentDetection;
import org.firstinspires.ftc.teamcode.Hardware.DiffyClaw;
import org.firstinspires.ftc.teamcode.Hardware.Intake;
import org.firstinspires.ftc.teamcode.Hardware.IntakeClaw;
import org.firstinspires.ftc.teamcode.Hardware.LEDController;
import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;
import org.firstinspires.ftc.teamcode.Hardware.OCGBox;
import org.firstinspires.ftc.teamcode.Hardware.SlideArm;
import org.firstinspires.ftc.teamcode.Hardware.SpecimenArm;
import org.firstinspires.ftc.teamcode.Hardware.VoltageHandler;
import org.firstinspires.ftc.teamcode.Helpers.CurrentUtils;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PIDDrive;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.Pinpoint;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PurePursuit;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.SparkfunOTOS;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.TwoWheelOdometry;

/**
 * This class is used to create all of the hardware objects and store them in the bot object
 * This is used to simplify the code and make it easier to read
 * This also makes it easier to create new objects
 */
public class Bot {
    public static LinearOpMode opMode;
    public static BotIMU imu;
    public static LEDController led;
    public static SpecimenArm specimenArm;
    public static PIDDrive pidDrive;
    public static MecanumBase mecanumBase;
    public static SparkfunOTOS optical_odom;
    public static TwoWheelOdometry deadwheel_odom;
    public static VoltageHandler voltageHandler;
    public static ColorSensor colorSensor;
    public static PurePursuit purePursuit;
    public static AnalogInput frontTouchSensor;
    public static Intake intake;
    public static SlideArm slideArm;
    public static Pinpoint pinpoint;
    public static CurrentUtils currentThreads;
    public static FtcDashboard dashboard;
    public static Telemetry dashboardTelemetry;
    public static IntakeClaw intakeClaw;
    public static OCGBox ocgBox;

    /**
     * Constructor for Bot.
     * @param opMode LinearOpMode
     */
    public static void init(@NonNull LinearOpMode opMode, double startX, double startY) {
        Bot.opMode = opMode;
        Bot.dashboard = FtcDashboard.getInstance();
        Bot.dashboardTelemetry = dashboard.getTelemetry();

        // Drive base and self driving
        Bot.pinpoint = new Pinpoint(startX, startY);
//        Bot.optical_odom = new SparkfunOTOS();
//        Bot.deadwheel_odom = new TwoWheelOdometry(Config.specimenStartX, Config.specimenStartY);
        Bot.mecanumBase = new MecanumBase();
        Bot.pidDrive = new PIDDrive();
        Bot.purePursuit = new PurePursuit(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2]);

        // Motors
        Bot.specimenArm = new SpecimenArm();
        Bot.slideArm = new SlideArm();

        // Servos
        Bot.intake = new Intake();
        Bot.intakeClaw = new IntakeClaw();
        Bot.ocgBox = new OCGBox();


        // Other
        Bot.frontTouchSensor = opMode.hardwareMap.get(AnalogInput.class, Config.touchSensor);
        Bot.colorSensor = new ColorSensor();
        Bot.voltageHandler = new VoltageHandler();
        Bot.imu = new BotIMU();
        Bot.led = new LEDController();


        // Threads
        Bot.currentThreads = new CurrentUtils(opMode);
    }

    public static void init(@NonNull LinearOpMode opMode) {
        Bot.init(opMode, 0, 0);
    }
}
