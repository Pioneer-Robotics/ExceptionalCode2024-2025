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

    // ---- Hardware Map ----
    // Odometers
    public static final String odoLeft = "OdoLeft";
    public static final String odoCenter = "hang";
    public static final String odoRight = "collector";

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
    public static final String led = "led";
    public static final String imu = "expansionIMU";

    // ---- Servo Positions ----
    public static final double wristOpen = 0.0;
    public static final double wristClose = 1.0;

    public static final double clawOpen = 0.25;
    public static final double clawClose = 0.95;

    // ---- Motor Positions ----
    // Specimen Arm
    public static final double defaultSpecimenArmSpeed = 0.5;
    public static final int specimenArmPostHang = 1100;
    public static final int specimenArmPrepHang = 825;
    public static final int specimenArmCollect = 1850;
    public static final int specimenArmPrepHangUp = 950;
    public static final int specimenArmPostHangUp = 550;
}
