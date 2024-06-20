package org.firstinspires.ftc.teamcode.Helpers;

import static org.firstinspires.ftc.teamcode.Config.trackWidth;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config;

public class Pose{
//    LinearOpMode opMode;

    DcMotorEx odoLeft, odoRight, odoCenter;
    double theta = 0;
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
    public double getX(){
        return((double) ((odoLeft.getCurrentPosition() - odoRight.getCurrentPosition())/2)*Config.ticsToCM);
    }

    public void calcPose(LinearOpMode opMode){
        opMode.telemetry.addData("Prev Left", prevLeftTicks);
        opMode.telemetry.addData("Prev Right", prevRightTicks);
        // Odo readings
        curLeftTicks = -odoLeft.getCurrentPosition();
        curRightTicks = -odoRight.getCurrentPosition();
        opMode.telemetry.addData("Cur Left", curLeftTicks);
        opMode.telemetry.addData("Cur Right", curRightTicks);

        deltaLeft = curLeftTicks - prevLeftTicks;
        deltaRight = curRightTicks - prevRightTicks;
        opMode.telemetry.addData("dL", deltaLeft);
        opMode.telemetry.addData("dR", deltaRight);

        prevLeftTicks = curLeftTicks;
        prevRightTicks = curRightTicks;

        theta += ((deltaLeft-deltaRight)*Config.ticsToCM)/trackWidth;
//        theta = theta % 2*Math.PI;
        //REMINDER: CAP AT PI/2PI

        opMode.telemetry.addData("Theta", theta);
//        return(theta);
    }

    public void DANCE(){

    }

}
