package org.firstinspires.ftc.teamcode;


/**
 * This class is used to store all of the constants and hardware map names for the robot
 */
// Annotation to allow quick changes to config from FTC Dashboard
@com.acmerobotics.dashboard.config.Config
public class Config {

    /* --------------------
       ---- Constants  ----
       -------------------- */

    // Odometer constants
    public static final double wheelDiameter = 4.8;
    public static final double ticsPerRev = 2000;
    public static final double ticsToCM = (wheelDiameter * Math.PI) / ticsPerRev;
    public static final double trackWidth = 26.5; // In CM
    public static final double forwardOffset = Math.sqrt((15.5 * 15.5) - 0.4); // In CM

    // Encoder constants
    public static final double maxDriveTicksPerSecond = 2500; // ~300 RPM
    public static final double maxSlideTicksPerSecond = 2700;
    public static final double maxSpecTicksPerSecond = 2700;

    // Two wheel odometers
    public static final double offsetOdoLeft = 1800;
    public static final double offsetOdoCenter = 635;

    // PID constants
    public static double[] drivePID = {0.08, 0.0001, 0.25}; // kP, kI, kD
    public static double[] turnPID = {2.5, 0.00001, 0.1}; // kP, kI, kD
    public static double driveSpeed = 0.4;

    // Feedforward constants
    public static double fLF = 0.0015;
    public static double fRF = 0.0015;
    public static double fLB = 0.0015;
    public static double fRB = 0.0015;

    // Tolerances
    // How close the robot needs to be to the target position to stop (in cm)
    public static double driveTolerance = 1.5;
    // How close the robot needs to be to the target angle to stop (in radians)
    public static double turnTolerance = 0.25;
    public static final double specimenArmTolerance = 5; // Motor ticks

    public static double lookAhead = 15; // Pure pursuit lookahead

    // Used to move "virtual robot" ahead of actual robot in pure pursuit
    public static double overshootDistance(double velocity) {
        // Polynomial fit for overshoot distance
        return ((0.000714 * velocity * velocity) + (0.145 * velocity) + 0.216); // *1.075
    }

    // Used to gradually accelerate
    // Multiplier starts at 0.1 and increments by acceleration each loop up to 1
    // Only used in PID Drive (deprecated)
    public static final double acceleration = 0.04;

    // Color sensor
    public static final int[] colorRed = {190, 110, 65}; // Red sample color (rgb)
    public static final int[] colorBlue = {50, 100, 215}; // Blue sample color (rgb)
    public static final int[] colorYellow = {410, 460, 110}; // Yellow sample color (rgb)
    public static final int alphaTolerance = 100; // If alpha is above this value, a sample is detected

    /* --------------------
       --- Hardware Map ---
       -------------------- */
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
    public static final String slideMotor1 = "slideMotor1";
    public static final String slideMotor2 = "slideMotor2"; //Correct one

    // Servo names
    public static final String clawServo = "clawServo";
    public static final String misumiDriveL = "misumiDriveL";
    public static final String misumiDriveR = "misumiDriveR";
    public static final String misumiWristR = "misumiWristR";
    public static final String misumiWristL = "misumiWristL";
    public static final String intakeClaw = "intakeClaw";
    public static final String intakeWrist = "intakeWrist";
    public static final String ocgBox = "ocgBox";
    public static final String diffyServo1 = "diffyServo1";
    public static final String diffyServo2 = "diffyServo2";
    public static final String ocgPitchServo = "pitchServo";
    public static final String ocgRollServo = "rollServo";
    public static final String intakeYawServo = "intakeYawServo";
    public static final String intakeRollServo = "intakeRollServo";
    public static final String intakeClawServo = "intakeClawServo";

    // Other names
    public static final String touchSensor = "touch";
    public static final String colorSensor = "colorSensor";
    public static final String led = "led";
    public static final String imu = "expansionIMU";

    /* --------------------
       - Servo Positions  -
       -------------------- */
    // Specimen Arm Claw
    public static final double clawOpen = 0.3;
    public static final double clawClose = 0.5;
    // Misumi Drive
    public static final double misumiDriveLOpen = 0.475;
    public static final double misumiDriveLMid = 0.3;
    public static final double misumiDriveLClose = 0.2;
    //    public static double misumiDriveROpen = 0.175;
//    public static double misumiDriveRMid = 0.4;
//    public static double misumiDriveRClose = 0.525;
    // Misumi Wrist
    public static final double misumiWristLDown = 0.85;
    public static final double misumiWristLMid = 0.75;
    public static final double misumiWristLUp = 0.5; // Up to OCG box
    public static final double misumiWristRDown = 0.15;
    public static final double misumiWristRMid = 0.24;
    public static final double misumiWristRUp = 0.4;
    // Intake Wrist
    public static final double intakeWristClose = 0;
    public static final double intakeWristOpen = 1;
    // Intake Claw
    public static final double intakeYawLeft = 0.05;
    public static final double intakeYawRight = 0.58;
    public static final double intakeRollUp = 0.43;
    public static final double intakeRollDown = 0.96;
    public static final double intakeClawOpen = 1;
    public static final double intakeClawClose = 0.5;
    // OCG Box
    public static final double ocgBoxDropRight = 0.1;
    public static final double ocgBoxHold = 0.375;
    public static final double ocgBoxDrop = 0.8;
    public static final double ocgBoxPitchUp = 0.705;
    public static final double ocgBoxPitchDown = 0.275;
    public static final double ocgBoxRollUp = 0.867;
    public static final double ocgBoxRollDown = 0.635;

    /* --------------------
       - Motor Positions  -
       -------------------- */
    // Specimen Arm
    public static final double defaultSpecimenArmSpeed = 0.4;
    public static int specimenArmPostHang = -1250;
    public static int specimenArmPrepHang = -800;
//    public static int specimenArmPostHang = 800;
//    public static int specimenArmPrepHang = 1250;
//    public static final int specimenArmPrepHangUp = 1000;
//    public static final int specimenArmPostHangUp = 650;
    public static final int specimenArmPrepHangUp = -1000;
    public static final int specimenArmPostHangUp = -650;
    public static final int specimenArmCollect1 = 2010;
    public static final int specimenArmCollect2 = 0;
//    public static final int specimenArmCollect1 = -2010;
//    public static final int specimenArmCollect2 = -0;
    // Linear Slide
    public static final double defaultSlideSpeed = 0.25;
    public static final int minSlideHeight = 15;
    public static final int maxSlideHeight = 4350;
    public static final int slideDown = 15;
    public static final int slideLowBasket = 2500;
    public static final int slideHighBasket = 4300;

    /* -------------------------
       -  Coordinates of Note  -
       -    and Auto Config    -
       ------------------------- */

    // Specimen hang offset (space between hangs)
    public static final double hangOffset = 6.5;

    // Coordinates of default specimen hang
    public static final double specHangX = 195;
    public static final double specHangY = 94.95;

    // Coordinates of observation zone park
    public static final double parkX = 300;
    public static final double parkY = 30;

    // Robot starting position for autos
    public static final double specimenStartX = 198;
    public static final double specimenStartY = 21.5;
    public static final double sampleStartX = 85;
    public static final double sampleStartY = 20.5;

    // Hermite Points of Interest
    public static final double[] pickSample1 = {58, 65};
    public static final double[] pickSample2 = {25, 65};
    public static final double[] pickSample3 = {25, 65};
    public static final double[] submersablePickup = {120, 160};

    // ---- Misc ----
    public static final double defaultMaxCurrent = 8000;
}
