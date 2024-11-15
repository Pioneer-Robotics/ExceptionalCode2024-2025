package org.firstinspires.ftc.teamcode;


/**
 * This class is used to store all of the constants and hardware map names for the robot
 */
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
    public static final double[] drivePID = {0, 0, 0}; // kP, kI, kD

    public static final double[] turnPID = {0, 0, 0}; // kP, kI, kD

    // Tolerances
    // How close the robot needs to be to the target position to stop (in cm)
    public static final double driveTolerance = 0.75;
    // How close the robot needs to be to the target angle to stop (in radians)
    public static final double turnTolerance = 0.05;
    public static final double specimenArmTolerance = 5; // Motor ticks

    public static final double lookAhead = 5;

    // Used to gradually accelerate
    // PID speed starts at 0.1 and increments by acceleration each loop up to 1
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
    public static final double clawClose = 1.0;

    // ---- Motor Positions ----
    // Specimen Arm
    public static final double defaultSpecimenArmSpeed = 0.5;
    public static final int specimenArmPostHang = 575;
    public static final int specimenArmPrepHang = 850;
    public static final int specimenArmCollect = 1850;
}
