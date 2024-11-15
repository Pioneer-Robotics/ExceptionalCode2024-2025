package org.firstinspires.ftc.teamcode.SelfDrivingAuto;


import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.AngleUtils;
import org.firstinspires.ftc.teamcode.Helpers.Utils;

// Not tested

/**
 * Simple point to point using PID
 */
public class PIDDrive {
    public PIDDrive() {
        Bot.mecanumBase.setNorthMode(true);
    }

    /**
     * Move to a position using PID
     * @param x     double - x position
     * @param y     double - y position
     * @param theta double - theta position in degrees
     * @param speed double - speed of the PID [0, 1]
     */
    public void moveToPosition(double x, double y, double theta, double speed) {
        theta = Math.toRadians(theta);
        theta = AngleUtils.normalizeRadians(theta);

        // Get current position
        double currentX = Bot.optical_odom.getX();
        double currentY = Bot.optical_odom.getY();
        double currentTheta = Bot.optical_odom.getHeading();

        // Initialize PID controllers with initial errors
        PID xPID = new PID(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2], x - currentX);
        PID yPID = new PID(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2], y - currentY);
        PID turnPID = new PID(Config.turnPID[0], Config.turnPID[1], Config.turnPID[2], theta - currentTheta);

        // Used to gradually accelerate
        double speedMultiplier = 0.1;

        // Loop until the robot reaches the target position or the op mode is stopped
        while (Math.abs(x - currentX) > Config.driveTolerance || Math.abs(y - currentY) > Config.driveTolerance || Math.abs(theta - currentTheta) > Config.turnTolerance && !Bot.opMode.isStopRequested()) {
            // Update current position
            currentX = Bot.optical_odom.getX();
            currentY = Bot.optical_odom.getY();
            currentTheta = Bot.optical_odom.getHeading();

            // Calculate PID outputs
            double xOutput = xPID.calculate(currentX, x, speed);
            double yOutput = yPID.calculate(currentY, y, speed);
            double turnOutput = turnPID.calculate(currentTheta, theta, speed, true);

            // Move the robot based on the PID outputs
            Bot.mecanumBase.move_vector(xOutput * speedMultiplier, yOutput * speedMultiplier, turnOutput * speedMultiplier);
            speedMultiplier = Utils.increment(speedMultiplier, Config.acceleration, 1);

            // Telemetry
            Bot.opMode.telemetry.addData("X", currentX);
            Bot.opMode.telemetry.addData("Y", currentY);
            Bot.opMode.telemetry.addData("Theta", currentTheta);
            Bot.opMode.telemetry.addData("X Error", xPID.getError());
            Bot.opMode.telemetry.addData("Y Error", yPID.getError());
            Bot.opMode.telemetry.addData("Theta Error", turnPID.getError());
            Bot.opMode.telemetry.addData("X Output", xOutput);
            Bot.opMode.telemetry.addData("Y Output", yOutput);
            Bot.opMode.telemetry.addData("Rotation Output", turnOutput);
            Bot.opMode.telemetry.update();
        }

        // Stop the robot
        Bot.mecanumBase.stop();
    }

    /**
     * Move to a position using PID (default speed)
     * @param x     double - x position
     * @param y     double - y position
     * @param theta double - theta position in degrees
     */
    public void moveToPosition(double x, double y, double theta) {
        moveToPosition(x, y, theta, 0.6);
    }

    /**
     * Move to a position using PID (stay at current theta use default speed)
     * @param x double - x position
     * @param y double - y position
     */
    public void moveToPosition(double x, double y) {
        moveToPosition(x, y, Bot.optical_odom.getHeading());
    }

    /**
     * Move relative to the current position using PID
     * @param x     double - x position
     * @param y     double - y position
     * @param theta double - theta position in degrees
     * @param speed double - speed of the PID [0, 1]
     */
    public void moveRelative(double x, double y, double theta, double speed) {
        moveToPosition(Bot.optical_odom.getX() + x, Bot.optical_odom.getY() + y, Bot.optical_odom.getHeading() + theta, speed);
    }

    /**
     * Move relative to the current position using PID (use default speed)
     * @param x double - x position
     * @param y double - y position
     * @param theta - theta position in degrees
     */
    public void moveRelative(double x, double y, double theta) {
        moveRelative(x, y, theta, 0.6);
    }

    /**
     * Move relative to the current position using PID (stay at current theta and use default speed)
     * @param x double - x position
     * @param y double - y position
     */
    public void moveRelative(double x, double y) {
        moveRelative(x, y, 0, 0.6);
    }

    public void setOrigin() {}
}
