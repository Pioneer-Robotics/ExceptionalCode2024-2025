package org.firstinspires.ftc.teamcode.SelfDrivingAuto;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;
import org.firstinspires.ftc.teamcode.Helpers.AngleUtils;

// Not tested

/**
 * Controls the mecanum base using PID
 */
public class PIDController {
    private final LinearOpMode opMode;
    private final MecanumBase base;
    private final Pose pose;

    /**
     * Constructor for PID controller with pose
     *
     * @param opMode LinearOpMode
     * @param bot   Bot object for pose and IMU
     */
    public PIDController(LinearOpMode opMode, Bot bot) {
        this.opMode = opMode;
        // Define pose
        this.pose = bot.pose;
        // Define base
        base = bot.mecanumBase;
        base.setNorthMode(true);
    }

    /**
     * Move to a position using PID
     *
     * @param x     double - x position
     * @param y     double - y position
     * @param theta double - theta position in degrees
     * @param speed double - speed of the PID [0, 1]
     */
    public void moveToPosition(double x, double y, double theta, double speed) {
        theta = Math.toRadians(theta);
        theta = AngleUtils.normalizeRadians(theta);

        // Get current position
        pose.calculate();
        double currentX = pose.getX();
        double currentY = pose.getY();
        double currentTheta = pose.getTheta();

        // Initialize PID controllers with initial errors
        PID xPID = new PID(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2], x - currentX);
        PID yPID = new PID(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2], y - currentY);
        PID turnPID = new PID(Config.turnPID[0], Config.turnPID[1], Config.turnPID[2], theta - currentTheta);

        // Loop until the robot reaches the target position or the op mode is stopped
        while (Math.abs(x - currentX) > Config.driveTolerance || Math.abs(y - currentY) > Config.driveTolerance || Math.abs(theta - currentTheta) > Config.turnTolerance && !opMode.isStopRequested()) {
            // Update current position
            pose.calculate();
            currentX = pose.getX();
            currentY = pose.getY();
            currentTheta = pose.getTheta();


            // Calculate PID outputs
            double xOutput = xPID.calculate(currentX, x, speed);
            double yOutput = yPID.calculate(currentY, y, speed);
            double turnOutput = turnPID.calculate(currentTheta, theta, speed, true);

            // Move the robot based on the PID outputs
            base.move_vector(xOutput, yOutput, turnOutput);

            // Telemetry
            opMode.telemetry.addData("X", currentX);
            opMode.telemetry.addData("Y", currentY);
            opMode.telemetry.addData("Theta", currentTheta);
            opMode.telemetry.addData("X Error", xPID.getError());
            opMode.telemetry.addData("Y Error", yPID.getError());
            opMode.telemetry.addData("Theta Error", turnPID.getError());
            opMode.telemetry.addData("X Output", xOutput);
            opMode.telemetry.addData("Y Output", yOutput);
            opMode.telemetry.addData("Rotation Output", turnOutput);
            opMode.telemetry.update();
        }

        // Stop the robot
        base.stop();
    }

    /**
     * Move to a position using PID (stay at current theta)
     *
     * @param x     double - x position
     * @param y     double - y position
     * @param speed double - speed of the PID [0, 1]
     */
    public void moveToPosition(double x, double y, double speed) {
        moveToPosition(x, y, pose.getTheta(), speed);
    }

    /**
     * Move to a position using PID (stay at current theta and speed = 0.8)
     *
     * @param x double - x position
     * @param y double - y position
     */
    public void moveToPosition(double x, double y) {
        moveToPosition(x, y, 0.8);
    }

    /**
     * Move relative to the current position using PID
     *
     * @param x     double - x position
     * @param y     double - y position
     * @param theta double - theta position in degrees
     */
    public void moveRelative(double x, double y, double theta, double speed) {
        moveToPosition(pose.getX() + x, pose.getY() + y, pose.getTheta() + theta, speed);
    }

    /**
     * Move relative to the current position using PID (stay at current theta)
     *
     * @param x double - x position
     * @param y double - y position
     */
    public void moveRelative(double x, double y) {
        moveRelative(x, y, 0, 0.8);
    }

    /**
     * Move relative to the current position using PID (stay at current theta and speed = 0.8)
     *
     * @param x double - x position
     * @param y double - y position
     */
    public void moveRelative(double x, double y, double theta) {
        moveRelative(x, y, theta, 0.8);
    }
}
