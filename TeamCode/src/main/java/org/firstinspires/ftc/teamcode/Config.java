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
    public static final double trackWidth = 26.5; //In CM
    public static final double forwardOffset = Math.sqrt((15.5 * 15.5) - 0.4); // In CM

    // Encoder constants
    public static final double maxDriveTicksPerSecond = 2700; // ~300 RPM
    public static final double maxSlideTicksPerSecond = 2700;

    // PID constants
    public static final double[] drivePID = {0.1, 0.00075, 0.5}; // kP, kI, kD
    public static final double[] turnPID = {2, 0.00075, 0.5}; // kP, kI, kD

    public static final double driveTolerance = 1; // How close the robot needs to be to the target position to stop (in cm)
    public static final double turnTolerance = 0.05; // How close the robot needs to be to the target angle to stop (in radians)

    // Used to gradually accelerate
    // PID speed starts at 0.1 and increments by acceleration each loop up to 1
    public static final double acceleration = 0.025;

    // Color sensor
    public static final int[] colorRed = {190, 110, 65}; // Red sample color (rgb)
    public static final int[] colorBlue = {50, 100, 215}; // Blue sample color (rgb)
    public static final int[] colorYellow = {410, 460, 110}; // Yellow sample color (rgb)
    public static final int alphaTolerance = 100; // If alpha is above this value, a sample is detected

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

    // Servo names
    public static final String leftDropServo = "PixelDropLeft";
    public static final String rightDropServo = "PixelDropRight";
    public static final String wristServo = "wristServo";
    public static final String gripperServo = "gripperServo";
    public static final String intakeServo = "intakeServo";

    // Motor names
    public static final String slideMotor = "slideArm";
    public static final String intakeMotor = "collector";

    // Other names
    public static final String colorSensor = "colorSensor";
    public static final String led = "led";
    public static final String imu = "expansionIMU";

    // ---- Servo Positions ----
    public static final double leftOpenPos = .475;
    public static final double leftClosedPos = .055;

    public static final double rightOpenPos = .61;
    public static final double rightClosedPos = .175;

    public static final double wristVertical = .53;
    public static final double wristHorizontal = .885;

    public static final double gripperOpen = .2;
    public static final double gripperClosed = .5;

    public static final double intakeUp = .4;
    public static final double intakeDown = .02;

    //Other
    public static final String[] calmDownMessages = new String[]{"Please calm down", "Use courtesy on the field", "Practice defensive driving", "Keep a safe distance from other robots", "AVOID AGGRESSIVE DRIVING BEHAVIOR", "Stay calm and patient", "Do not use your cell phone while driving(Except for the driver station)","Don't drink and drive", "Chance takers are accident makers", "Drive like hell and you'll get there", "All accidents are preventable", "If you try to rush or zoom, you're sure to meet your doom (Rushdie, 13)", "All the dangerous overtakers end up safe at the undertaker's (Rushdie, 13)", "Look out! Slow down! Don't be funny! Life is precious! Cars cost money! (Rushdie, 13)", "GP GP GP GP GP GP GP GP GP GP GP GP GP GP GP GP", "Remember core values!"};
    public static final boolean competition = true;

}
