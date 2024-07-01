/**
This class is used to calculate the robots current position on the field with 3 values, an X coordinate, Y coordinate, and Theta(Robot Angle).
 */

package org.firstinspires.ftc.teamcode.SelfDrivingAuto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.AngleUtils;

/**
 * This class is used to calculate the robots current position on the field.
 * It uses the odometer encoders to calculate the robots position.
 * The robot's position is stored as an X coordinate, Y coordinate, and Theta (Robot Yaw).
 */
public class Pose{
    LinearOpMode opMode;

    DcMotorEx odoLeft, odoRight, odoCenter;
    private double x, y, theta;
    private double prevLeftTicks, prevRightTicks, prevCenterTicks;

    /**
     * Constructor for Pose
     *
     * @param opMode LinearOpMode
     */
    public Pose(LinearOpMode opMode){
        // Set up odometers
        odoLeft = opMode.hardwareMap.get(DcMotorEx.class, Config.odoLeft);
        odoRight = opMode.hardwareMap.get(DcMotorEx.class, Config.odoRight);
        odoCenter = opMode.hardwareMap.get(DcMotorEx.class, Config.odoCenter);

        odoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.opMode = opMode;

        x = y = theta = 0;
    }

    /**
     * Constructor for Pose with starting position
     *
     * @param opMode   LinearOpMode
     * @param startPos double[] - starting position of the robot {x, y, theta}
     */
    public Pose(LinearOpMode opMode, double[] startPos) {
        // Set up odometers
        odoLeft = opMode.hardwareMap.get(DcMotorEx.class, Config.odoLeft);
        odoRight = opMode.hardwareMap.get(DcMotorEx.class, Config.odoRight);
        odoCenter = opMode.hardwareMap.get(DcMotorEx.class, Config.odoCenter);

        odoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        x = startPos[0];
        y = startPos[1];
        theta = startPos[2];

        this.opMode = opMode;
    }

    /**
     * This method updates the current position of the robot on the field by using delta values from the odometers
     */
    public void calculate(){
        // Odo readings
        double curLeftTicks = -odoLeft.getCurrentPosition();
        double curRightTicks = -odoRight.getCurrentPosition();
        double curCenterTicks = -odoCenter.getCurrentPosition();

        // Calculate the change in odometers
        double dLeftCM = (curLeftTicks - prevLeftTicks) * Config.ticsToCM;
        double dRightCM = (curRightTicks - prevRightTicks) * Config.ticsToCM;
        double dCenterCM = (curCenterTicks - prevCenterTicks) * Config.ticsToCM;

        // Calculate the change in angle
        double dTheta = (dLeftCM - dRightCM) / Config.trackWidth;

        double centerDisplacement = (dLeftCM + dRightCM) / 2;
        double horizontalDisplacement = dCenterCM - (Config.forwardOffset * dTheta);

        // Calculate the change in x and y
        double a, b;
        if (dTheta != 0) {
            a = (Math.sin(dTheta) / dTheta) * centerDisplacement + ((Math.cos(dTheta) - 1) / dTheta) * horizontalDisplacement;
            b = ((1 - Math.cos(dTheta)) / dTheta) * centerDisplacement + (Math.sin(dTheta) / dTheta) * horizontalDisplacement;
        } else {
            a = centerDisplacement;
            b = horizontalDisplacement;
        }

        double dX = Math.sin(theta) * a + Math.cos(theta) * b;
        double dY = Math.cos(theta) * a - Math.sin(theta) * b;

        // Update the current x, y, and theta values
        x += dX;
        y += dY;
        theta += dTheta;

        // Normalize theta [-pi to pi]
        theta = AngleUtils.normalizeRadians(theta);

        // Set the previous values to the current values
        prevLeftTicks = curLeftTicks;
        prevRightTicks = curRightTicks;
        prevCenterTicks = curCenterTicks;
    }


    public double getY() {
        return (y);
    }

    public double getX() {
        return (x);
    }

    public double getTheta() {
        return (theta);
    }

    public void DANCE(){
        // TODO: DANCE!
    }

    /**
     * Returns the current x, y, and theta values of the robot
     * Calls calculate() to update the values
     * @return double[] {x, y, theta}
     */
    public double[] returnPose(){
        calculate();
        return (new double[]{x, y, theta});
    }

}
