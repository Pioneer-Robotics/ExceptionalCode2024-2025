package org.firstinspires.ftc.teamcode;


/**
 * This class is used to store all of the constants and hardware map names for the robot
 */
// Annotation to allow quick changes to config from FTC Dashboard
@com.acmerobotics.dashboard.config.Config
public class Config {
    // ---- Constants ----
    // Odometer constants
    public static final double wheelDiameter = 4.8;
    public static final double ticsPerRev = 2000;
    public static final double ticsToCM = (wheelDiameter * Math.PI) / ticsPerRev;
    public static final double trackWidth = 26.5; // In CM
    public static final double forwardOffset = Math.sqrt((15.5 * 15.5) - 0.4); // In CM

    // Encoder constants
    public static final double maxDriveTicksPerSecond = 2700; // ~300 RPM
    public static final double maxSlideTicksPerSecond = 2700;

    // Two wheel odometers
    public static final double offsetOdoLeft = 1800;
    public static final double offsetOdoCenter = 635;

    // PID constants
    public static double[] drivePID = {0.08, 0.0001, 0}; // kP, kI, kD
    public static double[] turnPID = {2, 0.00075, 0}; // kP, kI, kD
    public static double driveSpeed = 0.3;
    public static double minSpeed = 0.05;

    // Tolerances
    // How close the robot needs to be to the target position to stop (in cm)
    public static double driveTolerance = 0.75;
    // How close the robot needs to be to the target angle to stop (in radians)
    public static double turnTolerance = 0.05;
    public static final double specimenArmTolerance = 5; // Motor ticks

    public static double lookAhead = 15;

    // Used to gradually accelerate
    // Multiplier starts at 0.1 and increments by acceleration each loop up to 1
    public static final double acceleration = 0.03;

    // Color sensor
    public static final int[] colorRed = {190, 110, 65}; // Red sample color (rgb)
    public static final int[] colorBlue = {50, 100, 215}; // Blue sample color (rgb)
    public static final int[] colorYellow = {410, 460, 110}; // Yellow sample color (rgb)
    public static final int alphaTolerance = 100; // If alpha is above this value, a sample is detected

    // ---- Hardware Map ----
    // Odometers
    public static final String odoLeft = "odoLeft";
    public static final String odoCenter = "odoCenter";
    public static final String odoRight = "odoRight";

    // Drive motor names
    public static final String motorLF = "LF";
    public static final String motorLB = "LB";
    public static final String motorRF = "RF";
    public static final String motorRB = "RB";

    // Motor names
    public static final String specimenArmMotor = "specimenMotor";
    public static final String slideMotor = "slideMotor";

    // Servo names
    public static final String wristServo = "wristServo";
    public static final String clawServo = "clawServo";
    public static final String misumiDriveL = "misumiDriveL";
    public static final String misumiDriveR = "misumiDriveR";
    public static final String misumiWristR = "misumiWristR";
    public static final String misumiWristL = "misumiWristL";
    public static final String intakeClaw = "intakeClaw";
    public static final String intakeWrist = "intakeWrist";
    public static final String ocgBox = "ocgBox";

    // Other names
    public static final String touchSensor = "touch";
    public static final String colorSensor = "colorSensor";
    public static final String led = "led";
    public static final String imu = "expansionIMU";

    // ---- Servo Positions ----
    // Specimen Arm Claw
    public static final double clawOpen = 0.35;
    public static final double clawClose = 0.95;
    // Intake Claw
    public static final double intakeClawOpen = 0;
    public static final double intakeClawClose = 0;
    // Misumi Drive
    public static final double misumiDriveLOpen = 0;
    public static final double misumiDriveLClose = 0;
    public static final double misumiDriveROpen = 0;
    public static final double misumiDriveRClose = 0;
    // Misumi Wrist
    public static final double misumiWristLOpen = 0.5;
    public static final double misumiWristLMid = 0;
    public static final double misumiWristLClose = 0;
    public static final double misumiWristROpen = 0.5;
    public static final double misumiWristRMid = 0;
    public static final double misumiWristRClose = 0;
    // Intake Wrist
    public static final double intakeWristClose = 0;
    public static final double intakeWristOpen = 0;
    // OCG Box
    public static final double ocgBoxHold = 1;
    public static final double ocgBoxDrop = 0;

    // ---- Motor Positions ----
    // Specimen Arm
    public static final double defaultSpecimenArmSpeed = 0.5;
    public static final int specimenArmPostHang = 1400;
    public static final int specimenArmPrepHang = 900;
    public static final int specimenArmCollect = 1925;
    public static final int specimenArmPrepHangUp = 1050;
    public static final int specimenArmPostHangUp = 650;
    // Linear Slide
    public static final int slideLength = 4365;
    public static final double defaultSlideSpeed = 0.25;
    public static final int minSlideHeight = 15;
    public static final int maxSlideHeight = 4350;
    public static final int slideDown = 15;
    public static final int slideLowBasket = 2500;
    public static final int slideHighBasket = 4300;

    // ---- Coordinates of note and auto config ----
    // Y position of robot when flush with the submersible (cm)
    public static final double submersibleY = 101;

    // Specimen hang offset (space between hangs)
    public static final double hangOffset = 8;

    // Coordinates of first specimen hang
    public static final double specHangX = 170;
    public static final double specHangY = 100;

    // Coordinates of observation zone park
    public static final double parkX = 310;
    public static final double parkY = 20.5;

    // Robot starting position for specimen auto
    public static final double specimenStartX = 200;
    public static final double specimenStartY = 20.5;
}
