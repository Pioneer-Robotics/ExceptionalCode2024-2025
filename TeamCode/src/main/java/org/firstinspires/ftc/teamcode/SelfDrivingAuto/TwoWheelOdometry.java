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
    private double x, y;
    private double prevLeftTicks, prevCenterTicks;

    public TwoWheelOdometry(double initX, double initY) {
        // Set up odometers
        odoLeft = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.odoLeft);
        odoCenter = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.odoCenter);

        odoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        x = initX;
        y = initY;
    }

    public TwoWheelOdometry() {
        this(0,0);
    }

        /**
         * This method updates the current position of the robot on the field by using delta values from the odometers
         */
    public void calculate(){
        // TODO: Make calculation work with turning

        double dx = odoCenter.getCurrentPosition() - prevCenterTicks;
        double dy = odoLeft.getCurrentPosition() - prevLeftTicks;

        x += dx * Config.ticsToCM;
        y += dy * Config.ticsToCM;

        prevCenterTicks = odoCenter.getCurrentPosition();
        prevLeftTicks = odoLeft.getCurrentPosition();
    }

    public double getY() {
        return (-y);
    }

    public double getX() {
        return (x);
    }

    public double getTheta() {
        return (Bot.imu.getRadians());
    }

    /**
     * Returns the current x, y, and theta values of the robot
     * Calls calculate() to update the values
     * @return double[] {x, y, theta}
     */
    public double[] returnPose(){
        calculate();
        return (new double[]{x, y, Bot.imu.getRadians()}); //Does imu need to be negative
    }
}
