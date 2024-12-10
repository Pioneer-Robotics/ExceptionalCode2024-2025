/**
 This class is used to calculate the robots current position on the field with 3 values, an X coordinate, Y coordinate, and Theta(Robot Angle).
 */

package org.firstinspires.ftc.teamcode.SelfDrivingAuto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

/**
 * This class is used to calculate the robots current position on the field.
 * It uses the odometer encoders to calculate the robots position.
 * The robot's position is stored as an X coordinate, Y coordinate, and Theta (Robot Yaw).
 */
public class TwoWheelOdometry {
    DcMotorEx odoLeft, odoCenter;
    private double x, y, theta;
    private double prevRightTicks, prevCenterTicks, prevTheta;

    public TwoWheelOdometry() {
        // Set up odometers
        odoLeft = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.odoLeft);
        odoCenter = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.odoCenter);

        odoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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

        x = startX;
        y = startY;
    }

    /**
     * This method updates the current position of the robot on the field by using delta values from the odometers
     */
    public void calculate(){
        // Odo readings
        // Center odometer calculates the horizontal displacement
        // Right odometer calculates the vertical displacement
        double curRightTicks = -odoLeft.getCurrentPosition();
        double curCenterTicks = -odoCenter.getCurrentPosition();

        // Get current rotation from IMU
        theta = -Bot.imu.getRadians();

        // Calculate the change in odometer readings
        double dRightCM = (curRightTicks - prevRightTicks) * Config.ticsToCM;
        double dCenterCM = (curCenterTicks - prevCenterTicks) * Config.ticsToCM;
        double dTheta = theta - prevTheta;

        // Calculate the change in X and Y based on the robot's current orientation (theta)
        // Uses rotation matrix to account for when the robot is not facing forward
        double dX = Math.cos(theta) * dCenterCM + Math.sin(theta) * dRightCM;
        double dY = Math.sin(theta) * dCenterCM - Math.cos(theta) * dRightCM;

        // TODO: Account for arc when the robot turns in place
        double arc_x = dTheta * Config.offsetOdoCenter * Config.ticsToCM;
        double arc_y = dTheta * Config.offsetOdoLeft * Config.ticsToCM;

        // Update the current X and Y values
        x += dX - arc_x;
        y += dY - arc_y;

        // Set the previous odometer readings for the next cycle
        prevRightTicks = curRightTicks;
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
