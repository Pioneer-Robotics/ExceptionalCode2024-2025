/**
This class is used to calculate the robots current position on the field with 3 values, an X coordinate, Y coordinate, and Theta(Robot Angle).
 */

package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Config;

public class Pose{
//    LinearOpMode opMode;

    DcMotorEx odoLeft, odoRight, odoCenter;
    double x, y;
    double theta = 0;
    double curTheta = 0;
    double[] poseArr = new double[3];
    double dLeftTicks, dRightTicks, dCenterTicks, dX, dY, dTheta, dLeftCM, dRightCM, dCenterCM, dLeftFinal, dRightFinal;
    double curLeftTicks, curRightTicks, curCenterTicks, prevLeftTicks, prevRightTicks, prevCenterTicks, prevTheta, phi;

    public Pose(LinearOpMode opMode){
        // Set up odometers
        odoLeft = opMode.hardwareMap.get(DcMotorEx.class, Config.odoLeft);
        odoRight = opMode.hardwareMap.get(DcMotorEx.class, Config.odoRight);
        odoCenter = opMode.hardwareMap.get(DcMotorEx.class, Config.odoCenter);

        odoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    public void calculate(){
        // Odo readings
        curLeftTicks = -odoLeft.getCurrentPosition();
        curRightTicks = -odoRight.getCurrentPosition();
        curCenterTicks = -odoCenter.getCurrentPosition();

        dLeftTicks = curLeftTicks - prevLeftTicks;
        dRightTicks = curRightTicks - prevRightTicks;
        dCenterTicks = curCenterTicks - prevCenterTicks;

        dLeftCM = dLeftTicks * Config.ticsToCM;
        dRightCM = dRightTicks * Config.ticsToCM;
        dCenterCM = dCenterTicks * Config.ticsToCM;

        phi = ((dLeftTicks - dRightTicks)*Config.ticsToCM)/Config.trackWidth;
        theta += phi;
        curTheta = AngleUtils.normalizeRadians(theta);
        dTheta =  AngleUtils.subtractAnglesRad(curTheta,prevTheta);

        //Arc length travelled by each odometer, in CM
        double centerArc = dTheta*(Config.center20FullRotationOdosInTicksDiv40pi * Config.ticsToCM);
        double leftArc = dTheta*(Config.left20FullRotationOdosInTicksDiv40pi * Config.ticsToCM);
        double rightArc = dTheta*(Config.right20FullRotationOdosInTicksDiv40pi * Config.ticsToCM);
//        double centerArc = dTheta*(Config.forwardOffset);
//        double leftArc = dTheta*(Config.forwardOffset);
//        double rightArc = dTheta*(Config.forwardOffset);

        dX = (dCenterCM - centerArc);
        dLeftFinal = dLeftCM - leftArc;
        dRightFinal = dRightCM + rightArc;
        double avgDY = (dLeftFinal + dRightFinal)/2;

        //Should this be added instead of divided????
        //MAIN PROBLEM right now is that the Y keeps changing when just strafing, and things are adding up to quick, X is going into the hundreds, which cant be right because it is in CM
        //Need to figure out why it increases so much, maybe missing a conversion from ticks to CM at some point, double check variables, can get confusing, might be using the wrong ones in places

//        x += dX*Math.cos(curTheta) + avgDY*Math.sin(curTheta);
//        y += -dX*Math.sin(curTheta) + avgDY*Math.cos(curTheta);
        x += dX;
        y += avgDY;

        prevLeftTicks = curLeftTicks;
        prevRightTicks = curRightTicks;
        prevCenterTicks = curCenterTicks;
        prevTheta = curTheta;
    }


    public double getY(){return(x);}

    public double getX(){return(y);}

    public double getTheta(){return(curTheta);}

    public void DANCE(){

    }

    /**
     * 0 is X coord
     * 1 is y coord
     * 2 is theta
     */
    public double[] returnPose(){
        calculate();
        poseArr[0] = getX();
        poseArr[1] = getY();
        poseArr[2] = getTheta();
        return (poseArr);
    }

}
