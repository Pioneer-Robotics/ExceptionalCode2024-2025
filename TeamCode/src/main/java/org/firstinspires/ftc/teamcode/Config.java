package org.firstinspires.ftc.teamcode;


/**
 * This class is used to store all of the constants and hardware map names for the robot
 * These can be called with Config.xxx
 */
public class Config {
    // ---- Constants ----
    // Odometer constants
    public static final double wheelDiameter = 4.8;
    public static final double ticsPerRev = 2000;
    public static final double ticsToCM = (wheelDiameter * Math.PI) / ticsPerRev;

    // PID constants
    public static final double[] drivePID = {0.1, 0.001, 1}; // kP, kI, kD
    public static final double[] turnPID = {0.1, 0.001, 1}; // kP, kI, kD

    // ---- Hardware Map ----
    // Odometer names
    public static final String odoLeft = "OdoLeft";
    public static final String odoCenter = "hang";
    public static final String odoRight = "collector";

    // Drive motor names
    public static final String motorLF = "LF";
    public static final String motorLB = "LB";
    public static final String motorRF = "RF";
    public static final String motorRB = "RB";

    // IMU name - imu for bombot, expansionIMU for hermes
    public static final String imu = "imu";
    // public static final String imu = "expansionIMU";

    // Servo names


    // Motor names
    public static final String slideMotor = "slideArm";

    // Other names
    public static final String led = "LED";
}
