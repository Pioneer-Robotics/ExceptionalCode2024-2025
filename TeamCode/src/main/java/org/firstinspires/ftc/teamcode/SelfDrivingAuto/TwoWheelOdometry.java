/**
 This class is used to calculate the robots current position on the field with 3 values, an X coordinate, Y coordinate, and Theta(Robot Angle).
 */

package org.firstinspires.ftc.teamcode.SelfDrivingAuto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.TrueAngle;

/**
 * This class is used to calculate the robots current position on the field.
 * It uses the odometer encoders to calculate the robots position.
 * The robot's position is stored as an X coordinate, Y coordinate, and Theta (Robot Yaw).
 */
public class TwoWheelOdometry {
    DcMotorEx odoLeft, odoCenter;
    private double x, y, theta;
    private double prevLeftTicks, prevCenterTicks, prevTheta;
    private TrueAngle trueAngle;

    public TwoWheelOdometry() {
        // Set up odometers
        odoLeft = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.odoLeft);
        odoCenter = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.odoCenter);

        odoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        trueAngle = new TrueAngle(0);

        x = y = theta = 0;
    }

    /**
     * Constructor with starting position
     *
     * @param startX double - starting x position of the robot
     * @param startY double - starting y position of the robot
     */
    public TwoWheelOdometry(double startX, double startY) {
        // Set up odometers
        odoLeft = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.odoLeft);
        odoCenter = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.odoCenter);

        odoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        trueAngle = new TrueAngle(0);

        x = startX;
        y = startY;
    }

    /**
     * This method updates the current position of the robot on the field by using delta values from the odometers
     */
    public void calculate(){
        // Get current rotation from IMU
        // TODO: Theta jumps when around 3 pi
        theta = trueAngle.updateAngle(-Bot.imu.getRadians());
        double dTheta = theta - prevTheta;

        Bot.opMode.telemetry.addData("True Angle", theta);

        // Odo readings
        // Center odometer calculates the horizontal displacement
        // Left odometer calculates the vertical displacement
        double curLeftTicks = odoLeft.getCurrentPosition();
        double curCenterTicks = odoCenter.getCurrentPosition();

        // Account for arc when robot turns
        double arc_x = dTheta * Config.offsetOdoCenter;
        double arc_y = dTheta * Config.offsetOdoLeft;

        // Calculate the change in odometer readings
        double dLeftCM = ((curLeftTicks - prevLeftTicks) - arc_y) * Config.ticsToCM;
        double dCenterCM = ((curCenterTicks - prevCenterTicks) - arc_x) * Config.ticsToCM;

        // Calculate the change in X and Y based on the robot's current orientation (theta)
        // Uses rotation matrix to account for when the robot is not facing forward
        double dX = Math.cos(theta) * dCenterCM + Math.sin(theta) * dLeftCM;
        double dY = Math.sin(theta) * dCenterCM - Math.cos(theta) * dLeftCM;

        // Update the current X and Y values
        x += dX;
        y -= dY; // Y is flipped

        // Set the previous odometer readings for the next cycle
        prevLeftTicks = curLeftTicks;
        prevCenterTicks = curCenterTicks;
        prevTheta = theta;
    }


    public double getY() {
        return (y);
    }

    public double getX() {
        return (x);
    }

    /**
     * Returns the current x, y values of the robot
     * Calls calculate() to update the values
     * @return double[] {x, y}
     */
    public double[] returnPose(){
        calculate();
        return (new double[]{x, y});
    }

    public int getRawOdoLeft() { return odoLeft.getCurrentPosition(); }
    public int getRawOdoCenter() { return odoCenter.getCurrentPosition(); }
}
