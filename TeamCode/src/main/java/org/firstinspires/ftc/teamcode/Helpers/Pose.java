package org.firstinspires.ftc.teamcode.Helpers;

import static org.firstinspires.ftc.teamcode.Config.trackWidth;
import org.firstinspires.ftc.teamcode.Helpers.AngleUtils;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config;

public class Pose{
//    LinearOpMode opMode;

    DcMotorEx odoLeft, odoRight, odoCenter;
    double theta = 0;
    double normTheta = 0;
    double[] poseArr = new double[3];
    double deltaLeft = 0;
    double deltaRight = 0;
    double curLeftTicks, curRightTicks, prevLeftTicks, prevRightTicks;

    public Pose(LinearOpMode opMode){
        // Set up odometers
        odoLeft = opMode.hardwareMap.get(DcMotorEx.class, Config.odoLeft);
        odoRight = opMode.hardwareMap.get(DcMotorEx.class, Config.odoRight);
        odoCenter = opMode.hardwareMap.get(DcMotorEx.class, Config.odoCenter);

        odoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        double trackWidth = Config.trackWidth;

    }
    public double getY(){return((double) -((odoLeft.getCurrentPosition() + odoRight.getCurrentPosition())/2)*Config.ticsToCM);}

    public double getX(){return((double) -(odoCenter.getCurrentPosition())*Config.ticsToCM);}

    public double calcHeading(){
//        opMode.telemetry.addData("Prev Left", prevLeftTicks);
//        opMode.telemetry.addData("Prev Right", prevRightTicks);
        // Odo readings
        curLeftTicks = -odoLeft.getCurrentPosition();
        curRightTicks = -odoRight.getCurrentPosition();
//        opMode.telemetry.addData("Cur Left", curLeftTicks);
//        opMode.telemetry.addData("Cur Right", curRightTicks);

        deltaLeft = curLeftTicks - prevLeftTicks;
        deltaRight = curRightTicks - prevRightTicks;
//        opMode.telemetry.addData("dL", deltaLeft);
//        opMode.telemetry.addData("dR", deltaRight);

        prevLeftTicks = curLeftTicks;
        prevRightTicks = curRightTicks;

        theta += ((deltaLeft-deltaRight)*Config.ticsToCM)/trackWidth;
//        theta = theta % 2*Math.PI;
        //REMINDER: CAP AT PI/2PI
        normTheta = AngleUtils.normalizeRadians(theta);

        return(normTheta);
    }

    public void DANCE(){

    }

    /**
     * 0 is X coord
     * 1 is y coord
     * 2 is theta
     * @param opMode
     */
    public double[] returnPose(LinearOpMode opMode){
        poseArr[0] = getX();
        poseArr[1] = getY();
        poseArr[2] = calcHeading();
        opMode.telemetry.addData("Pose", poseArr);
        return (poseArr);
    }

}
