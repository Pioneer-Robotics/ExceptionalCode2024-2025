package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;


/**
 * MecanumBase is used to initialize and control the drive base of the robot
 */
public class MecanumBase {
    private final LinearOpMode opMode;
    private final Bot bot;
    private final DcMotorEx LF, LB, RF, RB;
    private boolean northMode;

    /**
     * Constructor for MecanumBase.
     * @param opMode LinearOpMode
     */
    public MecanumBase(LinearOpMode opMode, Bot bot) {
        // Contains hardwareMap and telemetry
        this.opMode = opMode;
        this.bot = bot;

        // Set up motors
        RF = opMode.hardwareMap.get(DcMotorEx.class, Config.motorRF);
        LF = opMode.hardwareMap.get(DcMotorEx.class, Config.motorLF);
        RB = opMode.hardwareMap.get(DcMotorEx.class, Config.motorRB);
        LB = opMode.hardwareMap.get(DcMotorEx.class, Config.motorLB);

        LF.setDirection(DcMotor.Direction.REVERSE);
        LB.setDirection(DcMotor.Direction.REVERSE);
        RF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Set the zero power behavior of the motors.
     * @param behavior ZeroPowerBehavior
     */
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        RF.setZeroPowerBehavior(behavior);
        LF.setZeroPowerBehavior(behavior);
        RB.setZeroPowerBehavior(behavior);
        LB.setZeroPowerBehavior(behavior);
    }

    /**
     * Main drive method; other methods call this one
     * Set the motor powers to move the robot in a direction.
     * Used in TeleOp and with the PID controller.
     * @param angle The angle to move at in radians
     * @param turn The amount to turn in the range [-1, 1]
     * @param speed The speed to move at in the range [-1, 1]
     */
    public void move(double angle, double turn, double speed) {
        double currentAngle = bot.imu.getRadians();

        if(northMode) {angle -= currentAngle;}

        double power1 = (Math.sin(angle - (Math.PI / 4)) * speed);
        double power2 = (Math.sin(angle + (Math.PI / 4)) * speed);

        // Set velocity by converting power in range [0, 1] to ticks per second
        RF.setVelocity((power1 + turn) * Config.maxDriveTicksPerSecond);
        LF.setVelocity((power2 - turn) * Config.maxDriveTicksPerSecond);
        RB.setVelocity((power2 + turn) * Config.maxDriveTicksPerSecond);
        LB.setVelocity((power1 - turn) * Config.maxDriveTicksPerSecond);


        opMode.telemetry.addData("Angle", currentAngle);
//        opMode.telemetry.addData("RF", RF.getPower());
//        opMode.telemetry.addData("LF", LF.getPower());
//        opMode.telemetry.addData("RB", RB.getPower());
//        opMode.telemetry.addData("LB", LB.getPower());
        opMode.telemetry.addData("NorthMode", northMode);
    }

    /**
     * Set the motor powers to move the robot in a direction at full speed.
     * Used in TeleOp and with the PID controller.
     * @param angle The angle to move at in radians
     * @param turn The amount to turn in the range [-1, 1]
     */
    public void move(double angle, double turn) {
        move(angle, turn, 1);
    }

    /**
     * Set the motor powers to move the robot based on the x and y components of a vector.
     * Used in TeleOp and with the PID controller.
     * @param x The x component of the vector to move in the range [-1, 1]
     * @param y The y component of the vector to move in the range [-1, 1]
     */
    public void move_vector(double x, double y) {
        move_vector(x, y, 0);
    }

    /**
     * Set the motor powers to move the robot based on the x and y components of a vector.
     * Used in TeleOp and with the PID controller.
     * @param x The x component of the vector to move in the range [-1, 1]
     * @param y The y component of the vector to move in the range [-1, 1]
     * @param turn The amount to turn in the range [-1, 1]
     */
    public void move_vector(double x, double y, double turn) {
        double angle = Math.atan2(y, x);
        double speed = Math.hypot(x, y);
        move(angle, -turn, speed);
    }

    /**
     * Sets all motor powers to 0.
     */
    public void stop() {
        RF.setVelocity(0);
        LF.setVelocity(0);
        RB.setVelocity(0);
        LB.setVelocity(0);
    }

    /**
     * Set north mode.
     * @param newMode Boolean value to set north mode
     */
    public void setNorthMode(boolean newMode) {
        northMode = newMode;
        // if(newMode) {bot.imu.resetYaw();}
    }

    /**
     * Get north mode.
     * @return Boolean value of north mode
     */
    public boolean getNorthMode() { return northMode; }
}
