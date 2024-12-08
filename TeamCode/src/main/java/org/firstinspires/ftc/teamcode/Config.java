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
    public static final double offsetOdoLeft = 500;
    public static final double offsetOdoCenter = 2500;

    // PID constants
    public static double[] drivePID = {0.08, 0.00015, 0}; // kP, kI, kD
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

    // Servo names
    public static final String wristServo = "wristServo";
    public static final String clawServo = "clawServo";

    // Other names
    public static final String touchSensor = "touch";
    public static final String colorSensor = "colorSensor";
    public static final String led = "led";
    public static final String imu = "expansionIMU";

    // ---- Servo Positions ----
    public static final double clawOpen = 0.25;
    public static final double clawClose = 0.95;

    // ---- Motor Positions ----
    // Specimen Arm
    public static final double defaultSpecimenArmSpeed = 0.5;
    public static final int specimenArmPostHang = 1215; //was 1250
    public static final int specimenArmPrepHang = 800;
    public static final int specimenArmCollect = 1860;
    public static final int specimenArmPrepHangUp = 1000;
    public static final int specimenArmPostHangUp = 600;

    // ---- Coordinates of note and auto config ----
    // Y position of robot when flush with the submersible (cm)
    public static final double submersibleY = 101;

    // Specimen hang offset (space between hangs)
    public static final double hangOffset = 10;

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
