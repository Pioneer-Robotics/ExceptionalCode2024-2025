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
//    LinearOpMode opMode;

    DcMotorEx odoLeft, odoRight, odoCenter;
    private double x, y;
    private double theta = 0;
    private double curTheta = 0;
    private double prevLeftTicks, prevRightTicks, prevCenterTicks, prevTheta;

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
    }

    /**
     * This method updates the current position of the robot on the field by using delta values from the odometers
     */
    public void calculate(){
        // Odo readings
        double curLeftTicks = -odoLeft.getCurrentPosition();
        double curRightTicks = -odoRight.getCurrentPosition();
        double curCenterTicks = -odoCenter.getCurrentPosition();

        double dLeftTicks = curLeftTicks - prevLeftTicks;
        double dRightTicks = curRightTicks - prevRightTicks;
        double dCenterTicks = curCenterTicks - prevCenterTicks;

        double dLeftCM = dLeftTicks * Config.ticsToCM;
        double dRightCM = dRightTicks * Config.ticsToCM;
        double dCenterCM = dCenterTicks * Config.ticsToCM;

        double phi = ((dLeftTicks - dRightTicks) * Config.ticsToCM) / Config.trackWidth;
        theta += phi;
        curTheta = AngleUtils.normalizeRadians(theta);
        double dTheta = AngleUtils.subtractAnglesRad(curTheta, prevTheta);

        // Arc length travelled by each odometer, in CM
        double centerArc = dTheta *(Config.center20FullRotationOdosInTicksDiv40pi * Config.ticsToCM);
        double leftArc = dTheta *(Config.left20FullRotationOdosInTicksDiv40pi * Config.ticsToCM);
        double rightArc = dTheta *(Config.right20FullRotationOdosInTicksDiv40pi * Config.ticsToCM);
//        double centerArc = dTheta*(Config.forwardOffset);
//        double leftArc = dTheta*(Config.forwardOffset);
//        double rightArc = dTheta*(Config.forwardOffset);

        double dX = (dCenterCM - centerArc);
        double dLeftFinal = dLeftCM - leftArc;
        double dRightFinal = dRightCM + rightArc;
        double avgDY = (dLeftFinal + dRightFinal)/2;

        x += dX;
        y += avgDY;

        prevLeftTicks = curLeftTicks;
        prevRightTicks = curRightTicks;
        prevCenterTicks = curCenterTicks;
        prevTheta = curTheta;
    }


    public double getY() {
        return (y);
    }

    public double getX() {
        return (x);
    }

    public double getTheta(){return(curTheta);}

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
        return (new double[]{x, y, curTheta});
    }

}
